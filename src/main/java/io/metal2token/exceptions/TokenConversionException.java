package io.metal2token.exceptions;

/**
 * Indicates that the conversion of a token failed.
 */
public class TokenConversionException extends Exception {

	public TokenConversionException(final String string) {
		super(string);
	}

	public TokenConversionException(final Exception e) {
		super(e);
	}

	private static final long serialVersionUID = -311256878209702799L;

}
