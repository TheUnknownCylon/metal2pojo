package example.test;

import mapper.MetalRefNotFound;
import mapper.MetalTokenConversionException;
import pojo.MapContext;
import pojo.PojoConverter;

public class MyMapper implements PojoConverter<Object> {
	@Override
	public Object convert(final MapContext mapContext) throws MetalTokenConversionException, MetalRefNotFound {
		return 0 - mapContext.parseValue().asNumeric().intValue();
	}
}
