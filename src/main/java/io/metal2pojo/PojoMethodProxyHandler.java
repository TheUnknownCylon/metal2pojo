package io.metal2pojo;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.metal2pojo.exceptions.TokenNotFoundException;
import io.metal2pojo.pojo.MapContext;
import io.metal2pojo.pojo.MetalField;
import io.metal2pojo.pojo.MetalTypeConverter;
import io.parsingdata.metal.data.ParseGraph;

/**
 * Each POJO returned by the {@link PojoMapper} is a {@link Proxy} instance.
 * This handler will be used to handle invokes on the methods of this proxy. If
 * the method is annotated with {@link MetalField}, it will look into the
 * token's {@link ParseGraph} context to determine it's value.
 */
class PojoMethodProxyHandler implements InvocationHandler {

	private final GraphContext graph;

	public PojoMethodProxyHandler(final GraphContext graph) {
		this.graph = graph;
	}

	@Override
	public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {

		if (!(method.isAnnotationPresent(MetalField.class))) {
			final Class<?> declaringClass = method.getDeclaringClass();
			final Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class
					.getDeclaredConstructor(Class.class, int.class);
			constructor.setAccessible(true);
			return constructor.newInstance(declaringClass, MethodHandles.Lookup.PRIVATE)
					.unreflectSpecial(method, declaringClass).bindTo(proxy).invokeWithArguments(args);
		}

		final MetalTypeConverter<?> converter = (MetalTypeConverter<?>) method.getAnnotation(MetalField.class)
				.converter().newInstance();

		if (List.class.isAssignableFrom(method.getReturnType())) {
			final List<MapContext> items = PojoMapperUtil.toContextList(method, graph);
			final List<Object> values = new ArrayList<>();
			for (final MapContext item : items) {
				values.add(converter.convert(item));
			}
			return values;

		} else if (Optional.class.isAssignableFrom(method.getReturnType())) {
			try {
				final MapContext context = PojoMapperUtil.toContext(method, graph);
				return Optional.of(converter.convert(context));
			} catch (final TokenNotFoundException e) {
				return Optional.empty();
			}

		} else {
			// just a field...
			final MapContext context = PojoMapperUtil.toContext(method, graph);
			return converter.convert(context);
		}
	}

}