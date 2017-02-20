package io.metal2pojo.testtokens.errors;

import static io.parsingdata.metal.Shorthand.def;
import static io.parsingdata.metal.Shorthand.seq;

import io.metal2pojo.pojo.MetalField;
import io.metal2pojo.pojo.MetalPojo;
import io.parsingdata.metal.token.Token;

@MetalPojo("FieldNotInToken")
public class FieldNotInToken {

	@MetalField
	public int notintoken;

	public static final Token TOKEN = seq("FieldNotInToken", def("def1", 1), def("def2", 1));

}
