package io.metal2pojo.testtokens;

import static io.parsingdata.metal.Shorthand.def;
import static io.parsingdata.metal.Shorthand.seq;

import io.metal2pojo.pojo.MetalPojo;
import io.parsingdata.metal.token.Token;

@MetalPojo("TokenWithTokenReference2")
public class TokenWithTokenReference2 extends TokenWithTokenReference {
	public static Token TOKEN = seq("TokenWithTokenReference2", def("def1", 1), SimpleSequenceToken.TOKEN);
}
