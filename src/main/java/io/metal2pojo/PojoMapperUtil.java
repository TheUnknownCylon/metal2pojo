package io.metal2pojo;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.metal2pojo.exceptions.TokenNotFoundException;
import io.metal2pojo.pojo.MapContext;
import io.metal2pojo.pojo.MetalPojo;
import io.parsingdata.metal.data.ImmutableList;
import io.parsingdata.metal.data.ParseValue;
import io.parsingdata.metal.data.selection.ByName;

public final class PojoMapperUtil {

	private PojoMapperUtil() {
		// final
	}

	protected static List<MapContext> toContextList(final Method field, final GraphContext graph) {
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

	protected static MapContext toContext(final Method method, final GraphContext graph) throws TokenNotFoundException {
		if (targetIsPojo(method)) {
			final String pojoTokenName = pojoTokenName(getTargetClass(method));
			return new MapContext(getTargetClass(method), graph.subGraph(pojoTokenName));
		} else {
			final String name = method.getName();
			return new MapContext(getTargetClass(method), ByName.getValue(graph.graph(), graph.nameOfParseValue(name)));
		}
	}

	protected static Class<?> getTargetClass(final Method method) {
		final Class<?> type = method.getReturnType();
		if (List.class.isAssignableFrom(type) || Optional.class.isAssignableFrom(type)) {
			return getInnerType(method);
		} else {
			return type;
		}
	}

	protected static boolean targetIsPojo(final Method method) {
		return targetIsPojo(getTargetClass(method));
	}

	protected static boolean targetIsPojo(final Class<?> clz) {
		return clz.isAnnotationPresent(MetalPojo.class);
	}

	static Class<?> getInnerType(final Method method) {
		final Type type = method.getGenericReturnType();

		if (type instanceof ParameterizedType) {
			final ParameterizedType pType = (ParameterizedType) type;
			final Type[] arr = pType.getActualTypeArguments();

			for (final Type tp : arr) {
				return (Class<?>) tp;
			}
		}
		throw new IllegalStateException("No inner type found for field " + method.getGenericReturnType());
	}

	/**
	 * Utility that validates that a class is a {@link MetalPojo}. Returns the
	 * token name for the expected child.
	 * 
	 * @param pojoClass
	 * @return
	 */
	protected static String pojoTokenName(final Class<?> pojoClass) {
		if (pojoClass.getAnnotation(MetalPojo.class) == null) {
			throw new IllegalStateException("Not a metal pojo: " + pojoClass);
		}

		return pojoClass.getAnnotation(MetalPojo.class).value();
	}
}
