package io.metal2pojo;

import java.io.IOException;
import java.lang.reflect.Proxy;

import io.metal2pojo.exceptions.TokenConversionException;
import io.metal2pojo.exceptions.TokenNotFoundException;
import io.metal2pojo.pojo.MetalPojo;
import io.parsingdata.metal.data.Environment;
import io.parsingdata.metal.data.ParseGraph;
import io.parsingdata.metal.encoding.Encoding;
import io.parsingdata.metal.token.Token;

/**
 * Contains all logic to map a {@link ParseGraph} to an instance of a
 * {@link MetalPojo}.
 */
public final class PojoMapper {

	public static <T> T fillPojo(final Class<T> pojoClass, final Token token, final Environment env, final Encoding enc)
			throws TokenNotFoundException, TokenConversionException, IOException {

		final String pojoTokenName = PojoMapperUtil.pojoTokenName(pojoClass);
		return fillPojo(pojoClass, new GraphContext(token.parse(env, enc).environment.order).subGraph(pojoTokenName));
	}

	protected static <T> T fillPojo(final Class<T> pojoClass, final GraphContext graph)
			throws TokenNotFoundException, TokenConversionException {

		try {
			@SuppressWarnings("unchecked")
			final T pojo = (T) Proxy.newProxyInstance(pojoClass.getClassLoader(), new Class[] { pojoClass },
					new PojoMethodProxyHandler(graph));
			return pojo;
		} catch (final Exception e) {
			throw new TokenConversionException(e);
		}
	}
}
