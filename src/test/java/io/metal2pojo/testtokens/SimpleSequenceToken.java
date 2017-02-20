package io.metal2pojo.testtokens;

import static io.parsingdata.metal.Shorthand.def;
import static io.parsingdata.metal.Shorthand.seq;

import io.metal2pojo.pojo.MetalField;
import io.metal2pojo.pojo.MetalPojo;
import io.parsingdata.metal.token.Token;

/**
 * Most basic form of a token.
 * 
 * Contains 2 integer definitions.
 */
@MetalPojo("SimpleSequenceToken")
public class SimpleSequenceToken {

	@MetalField
	public int def1;

	@MetalField
	public int def2;

	public static final Token TOKEN = seq("SimpleSequenceToken", def("def1", 1), def("def2", 1));

}