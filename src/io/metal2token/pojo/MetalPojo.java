package io.metal2token.pojo;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(TYPE)
public @interface MetalPojo {

	/**
	 * Name of token.
	 * 
	 * E.g. for a token that is a sequence: seq(tokenname, ...)
	 * 
	 * @return Name of token.
	 */
	String value();
}
