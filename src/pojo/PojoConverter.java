package pojo;

import mapper.MetalRefNotFound;
import mapper.MetalTokenConversionException;

public interface PojoConverter<T> {

	T convert(final MapContext mapContext) throws MetalTokenConversionException, MetalRefNotFound;

}
