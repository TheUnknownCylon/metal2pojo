package io.metal2pojo;

import io.metal2pojo.exceptions.TokenConversionException;
import io.metal2pojo.exceptions.TokenNotFoundException;
import io.metal2pojo.pojo.MapContext;
import io.metal2pojo.pojo.MetalTypeConverter;
import io.parsingdata.metal.data.ParseValue;

public class DefaultConverter implements MetalTypeConverter<Object> {

	@Override
	public Object convert(final MapContext context) throws TokenNotFoundException, TokenConversionException {
		final Class<?> type = context.type();

		if (context.parseValue() != null) {
			final ParseValue value = context.parseValue();
			if (type == Long.class || type == long.class) {
				return value.asNumeric().longValue();
			} else if (type == String.class) {
				return value.asString();
			} else if (type == Integer.class || type == int.class) {
				return value.asNumeric().intValue();
			} else {
				throw new IllegalStateException("Unknown type to interpret: " + type + " for value: " + value);
			}
		} else if (context.parseGraph() != null) {
			return PojoMapper.fillPojo(type, context.parseGraph());
		} else {
			throw new TokenNotFoundException();
		}
	}
}
