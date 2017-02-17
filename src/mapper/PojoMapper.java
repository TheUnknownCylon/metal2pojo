package mapper;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.parsingdata.metal.data.ImmutableList;
import io.parsingdata.metal.data.ParseGraph;
import io.parsingdata.metal.data.ParseItem;
import io.parsingdata.metal.data.ParseValue;
import io.parsingdata.metal.data.selection.ByName;
import pojo.Convert;
import pojo.MapContext;
import pojo.MetalField;
import pojo.MetalPojo;
import pojo.MetalTokenRef;
import pojo.PojoConverter;

/**
 * Mappers to map a {@link ParseGraph} to an instance of a {@link MetalPojo}.
 * 
 * This class was written-up in some sort of hurry :) Consider it a POC.
 * 
 * @author Netherlands Forensic Institute
 */
public final class PojoMapper {

	public static <T> T fillPojo(final Class<T> pojoClass, final ParseGraph graph) throws MetalPojoFillerException {
		try {
			return fillPojoInner(pojoClass, graph);
		} catch (final Exception e) {
			throw new MetalPojoFillerException(e);
		}
	}

	/**
	 * Method based on a parse graph and a {@link MetalPojo}, create a new
	 * instance of the pojo.
	 * 
	 * @param pojoClass
	 *            Class of pojo to fill.
	 * @param graph
	 *            Parsed graph containing the Pojo.
	 * @return A filled POJO that is in instance of pojoClass, filled with data
	 *         from graph.
	 * @throws IllegalArgumentException
	 * 
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws MetalRefNotFound
	 * @throws MetalTokenConversionException
	 */
	private static <T> T fillPojoInner(final Class<T> pojoClass, final ParseGraph graph) throws InstantiationException,
			IllegalAccessException, IllegalArgumentException, MetalTokenConversionException, MetalRefNotFound {

		final String pojoTokenName = pojoTokenName(pojoClass);

		// Context switch to sub-graph of pojo token.
		final ImmutableList<ParseItem> roots = ByTokenName.getAllRoots(graph, pojoTokenName);
		if (roots.size != 1) {
			System.out.println(graph);
			throw new MetalRefNotFound(
					"Unsupported number of pojo occurences for " + pojoTokenName + " in graph root: " + roots.size);
		}

		final T pojo = pojoClass.newInstance();
		for (final Field field : pojoClass.getFields()) {
			parseField(field, roots.head.asGraph(), pojo);
		}
		return pojo;
	}

	/**
	 * Helper method that maps a {@link MetalTokenRef} to a Pojo.
	 * 
	 * @param field
	 * @param graph
	 * @param pojo
	 * @throws MetalRefNotFound
	 * @throws MetalTokenConversionException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws MetalNotFound
	 */
	private static <T> void parseField(final Field field, final ParseGraph graph, final T pojo)
			throws MetalTokenConversionException, MetalRefNotFound, IllegalArgumentException, IllegalAccessException,
			InstantiationException {

		if (!(field.isAnnotationPresent(MetalField.class) || field.isAnnotationPresent(MetalTokenRef.class))) {
			return;
		}

		if (List.class.isAssignableFrom(field.getType())) {
			final List<MapContext> items = toContextList(field, graph);

			// Java8: map
			final List<Object> values = new ArrayList<>();
			for (final MapContext item : items) {
				values.add(getConverter(field, item.type()).convert(item));
			}
			field.set(pojo, values);

		} else if (Optional.class.isAssignableFrom(field.getType())) {
			final MapContext context = toContextFromGenericTyped(field, graph);
			try {
				field.set(pojo, Optional.of(getConverter(field, context.type()).convert(context)));
			} catch (final MetalRefNotFound e) {
				field.set(pojo, Optional.empty());
			}

		} else {
			// just a field...
			final MapContext mapContext = toContext(field, graph);
			field.set(pojo, getConverter(field, field.getType()).convert(mapContext));
		}
	}

	// -------------------------------------------------------------------------------------------
	// Map a field and graph to a mapping context
	// this is done to avoid the reduce the logic in parseField:
	// * mapping / field class can be different
	// * avoid if(graph) else if(value) pattern in parseField()

	private static List<MapContext> toContextList(final Field field, final ParseGraph graph) {
		final Class<?> innerType = getInnerType(field);
		final List<MapContext> mapItems = new ArrayList<>();

		if (field.isAnnotationPresent(MetalField.class)) {
			final String name = field.getAnnotation(MetalField.class).value();
			ImmutableList<ParseValue> values = ByName.getAllValues(graph.head.asGraph(), name).reverse();
			while (values.size > 0) {
				mapItems.add(new MapContext(innerType, values.head));
				values = values.tail;
			}
		} else {
			final String pojoTokenName = pojoTokenName(innerType);
			ImmutableList<ParseItem> values = ByTokenName.getAllRoots(graph.head.asGraph(), pojoTokenName).reverse();
			while (values.size > 0) {
				mapItems.add(new MapContext(innerType, values.head.asGraph()));
				values = values.tail;
			}
		}
		return mapItems;
	}

	private static MapContext toContextFromGenericTyped(final Field field, final ParseGraph graph) {
		final Class<?> innerType = getInnerType(field);
		if (field.isAnnotationPresent(MetalField.class)) {
			final String name = field.getAnnotation(MetalField.class).value();
			return new MapContext(innerType, ByName.getValue(graph, name));
		} else {
			return new MapContext(innerType, graph);
		}
	}

	private static MapContext toContext(final Field field, final ParseGraph graph) {
		if (field.isAnnotationPresent(MetalField.class)) {
			final String name = field.getAnnotation(MetalField.class).value();
			return new MapContext(field.getType(), ByName.getValue(graph, name));
		} else {
			return new MapContext(field.getType(), graph);
		}
	}

	// -------------------------------------------------------------------------------------------
	// All conversion logic (with the exception of a complete pojo) is
	// provided under a converter.

	private static PojoConverter<?> getConverter(final Field field, final Class<?> clz)
			throws InstantiationException, IllegalAccessException {
		if (field.isAnnotationPresent(Convert.class)) {
			return field.getAnnotation(Convert.class).value().newInstance();
		} else {
			return new PojoMapper().new SimpleConverter();
		}
	}

	/**
	 * Simple converter of a ParseValue to the expected type.
	 * 
	 * @param type
	 * @param value
	 * @return
	 */
	class SimpleConverter implements PojoConverter<Object> {
		@Override
		public Object convert(final MapContext context) throws MetalTokenConversionException, MetalRefNotFound {
			final Class<?> type = context.type();

			if (context.parseValue() != null) {
				final ParseValue value = context.parseValue();

				if (value == null) {
					throw new MetalRefNotFound();
				} else if (type == Long.class || type == long.class) {
					return value.asNumeric().longValue();
				} else if (type == String.class) {
					return value.asString();
				} else if (type == Integer.class || type == int.class) {
					return value.asNumeric().intValue();
				} else {
					throw new IllegalStateException("Unknown type to interpret: " + type + " for value: " + value);
				}
			} else {
				try {
					return fillPojoInner(type, context.parseGraph());
				} catch (IllegalArgumentException | IllegalAccessException | InstantiationException e) {
					throw new MetalTokenConversionException(e);
				}
			}
		}
	}

	// -------------------------------------------------------------------------------------------

	/**
	 * Utility that returns the generic type of a field. E.g. for ArrayList<X>
	 * returns class of X.
	 * 
	 * @param field
	 * @return
	 */
	private static Class<?> getInnerType(final Field field) {
		final Type type = field.getGenericType();

		if (type instanceof ParameterizedType) {
			final ParameterizedType pType = (ParameterizedType) type;
			final Type[] arr = pType.getActualTypeArguments();

			for (final Type tp : arr) {
				return (Class<?>) tp;
			}
		}
		throw new IllegalStateException("No inner type found for field " + field.getGenericType());
	}

	/**
	 * Utility that validates that a class is a {@link MetalPojo}. Returns the
	 * token name for the expected child.
	 * 
	 * @param pojoClass
	 * @return
	 */
	private static String pojoTokenName(final Class<?> pojoClass) {
		if (pojoClass.getAnnotation(MetalPojo.class) == null) {
			throw new IllegalStateException("Not a metal pojo: " + pojoClass);
		}

		return pojoClass.getAnnotation(MetalPojo.class).value();
	}
}