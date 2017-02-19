package io.metal2token;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.metal2token.exceptions.TokenConversionException;
import io.metal2token.exceptions.TokenNotFoundException;
import io.metal2token.pojo.MapContext;
import io.metal2token.pojo.MetalField;
import io.metal2token.pojo.MetalPojo;
import io.metal2token.pojo.MetalTypeConverter;
import io.parsingdata.metal.data.Environment;
import io.parsingdata.metal.data.ImmutableList;
import io.parsingdata.metal.data.ParseGraph;
import io.parsingdata.metal.data.ParseValue;
import io.parsingdata.metal.data.selection.ByName;
import io.parsingdata.metal.encoding.Encoding;
import io.parsingdata.metal.token.Token;

/**
 * Contains all logic to map a {@link ParseGraph} to an instance of a
 * {@link MetalPojo}.
 */
public final class PojoMapper {

	public static <T> T fillPojo(final Class<T> pojoClass, final Token token, final Environment env, final Encoding enc)
			throws TokenNotFoundException, TokenConversionException, IOException {

		final String pojoTokenName = pojoTokenName(pojoClass);
		return fillPojo(pojoClass, new GraphContext(token.parse(env, enc).environment.order).subGraph(pojoTokenName));
	}

	static <T> T fillPojo(final Class<T> pojoClass, final GraphContext graph)
			throws TokenNotFoundException, TokenConversionException {

		try {
			final T pojo = pojoClass.newInstance();
			for (final Field field : pojoClass.getFields()) {
				parseField(field, graph, pojo);
			}
			return pojo;
		} catch (final Exception e) {
			throw new TokenConversionException(e);
		}
	}

	private static <T> void parseField(final Field field, final GraphContext graph, final T pojo)
			throws TokenNotFoundException, TokenConversionException {
		try {
			_parseField(field, graph, pojo);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new TokenConversionException(e);
		}
	}

	private static <T> void _parseField(final Field field, final GraphContext graph, final T pojo)
			throws TokenNotFoundException, TokenConversionException, InstantiationException, IllegalAccessException {

		if (!(field.isAnnotationPresent(MetalField.class))) {
			return;
		}

		final MetalTypeConverter<?> converter = (MetalTypeConverter<?>) field.getAnnotation(MetalField.class)
				.converter().newInstance();

		if (List.class.isAssignableFrom(field.getType())) {
			final List<MapContext> items = toContextList(field, graph);
			final List<Object> values = new ArrayList<>();
			for (final MapContext item : items) {
				values.add(converter.convert(item));
			}
			field.set(pojo, values);

		} else if (Optional.class.isAssignableFrom(field.getType())) {
			final MapContext context = toContext(field, graph);
			try {
				field.set(pojo, Optional.of(converter.convert(context)));
			} catch (final TokenNotFoundException e) {
				field.set(pojo, Optional.empty());
			}

		} else {
			// just a field...
			final MapContext context = toContext(field, graph);
			field.set(pojo, converter.convert(context));
		}
	}

	// -------------------------------------------------------------------------------------------
	// Map a field and graph to a mapping context
	// this is done to avoid the reduce the logic in parseField:
	// * mapping / field class can be different
	// * avoid if(graph) else if(value) pattern in parseField()

	private static List<MapContext> toContextList(final Field field, final GraphContext graph) {
		final Class<?> innerType = getTargetClass(field);

		final List<MapContext> items = new ArrayList<>();

		if (targetIsPojo(field)) {
			final String pojoTokenName = pojoTokenName(innerType);
			final List<GraphContext> subGraphs = graph.subGraphs(pojoTokenName);
			for (final GraphContext subGraph : subGraphs) {
				items.add(new MapContext(innerType, subGraph));
			}

		} else {
			final String name = field.getName();
			ImmutableList<ParseValue> values = ByName.getAllValues(graph.graph(), graph.nameOfParseValue(name))
					.reverse();
			while (values.size > 0) {
				items.add(new MapContext(innerType, values.head));
				values = values.tail;
			}
		}
		return items;
	}

	private static MapContext toContext(final Field field, final GraphContext graph) throws TokenNotFoundException {
		if (targetIsPojo(field)) {
			final String pojoTokenName = pojoTokenName(field.getType());
			return new MapContext(getTargetClass(field), graph.subGraph(pojoTokenName));
		} else {
			final String name = field.getName();
			return new MapContext(getTargetClass(field), ByName.getValue(graph.graph(), graph.nameOfParseValue(name)));
		}
	}

	private static Class<?> getTargetClass(final Field field) {
		final Class<?> type = field.getType();
		if (List.class.isAssignableFrom(type) || Optional.class.isAssignableFrom(type)) {
			return getInnerType(field);
		} else {
			return type;
		}
	}

	private static boolean targetIsPojo(final Field field) {
		return targetIsPojo(getTargetClass(field));
	}

	private static boolean targetIsPojo(final Class<?> clz) {
		return clz.isAnnotationPresent(MetalPojo.class);
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
