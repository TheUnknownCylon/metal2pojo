package io.metal2token.pojo;

import io.metal2token.exceptions.TokenConversionException;
import io.metal2token.exceptions.TokenNotFoundException;

public interface MetalTypeConverter<T> {

	T convert(final MapContext mapContext) throws TokenNotFoundException, TokenConversionException;

}