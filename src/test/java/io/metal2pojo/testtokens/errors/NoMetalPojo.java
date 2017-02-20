package io.metal2pojo.testtokens.errors;

import static io.parsingdata.metal.Shorthand.def;
import static io.parsingdata.metal.Shorthand.seq;

import io.metal2pojo.pojo.MetalField;
import io.parsingdata.metal.token.Token;

public class NoMetalPojo {
	@MetalField
	public int def1;

	@MetalField
	public int def2;

	public static final Token TOKEN = seq("SimpleSequenceToken", def("def1", 1), def("def2", 1));

}
