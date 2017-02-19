package io.metal2token;

import io.metal2token.exceptions.TokenConversionException;
import io.metal2token.exceptions.TokenNotFoundException;
import io.metal2token.pojo.MapContext;
import io.metal2token.pojo.PojoConverter;
import io.parsingdata.metal.data.ParseValue;

public class DefaultConverter implements PojoConverter<Object> {

	@Override
	public Object convert(final MapContext context) throws TokenNotFoundException, TokenConversionException {
		final Class<?> type = context.type();

		if (context.parseValue() != null) {
			final ParseValue value = context.parseValue();

			if (value == null) {
				throw new TokenNotFoundException();
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
			return PojoMapper.fillPojo(type, context.parseGraph());
		}
	}
}
