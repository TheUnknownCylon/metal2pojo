package io.metal2pojo.testtokens;

import static io.parsingdata.metal.Shorthand.def;
import static io.parsingdata.metal.Shorthand.seq;

import io.metal2pojo.pojo.MetalField;
import io.metal2pojo.pojo.MetalPojo;
import io.parsingdata.metal.token.Token;

/**
 * Basic token that references another token: {@link SimpleSequenceToken}
 */
@MetalPojo("TokenWithTokenReference")
public interface TokenWithTokenReference {
	@MetalField
	public int def1();

	@MetalField
	public SimpleSequenceToken seq1();

	public static final Token TOKEN = seq("TokenWithTokenReference", def("def1", 1), SimpleSequenceToken.TOKEN);

	public static final Token TOKENREV = seq("TokenWithTokenReference", SimpleSequenceToken.TOKEN, def("def1", 1));
}
