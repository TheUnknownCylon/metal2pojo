package io.metal2pojo.pojo;

import io.metal2pojo.exceptions.TokenConversionException;
import io.metal2pojo.exceptions.TokenNotFoundException;

public interface MetalTypeConverter<T> {

	T convert(final MapContext mapContext) throws TokenNotFoundException, TokenConversionException;

}
