package io.metal2pojo.testtokens.errors;

import static io.parsingdata.metal.Shorthand.def;
import static io.parsingdata.metal.Shorthand.seq;

import java.math.BigDecimal;

import io.metal2pojo.pojo.MetalField;
import io.metal2pojo.pojo.MetalPojo;
import io.parsingdata.metal.token.Token;

@MetalPojo("TokenRefsNonMetalToken")
public class TokenRefsNonMetalToken {

	@MetalField
	public BigDecimal def1;

	public static final Token TOKEN = seq("TokenRefsNonMetalToken", def("def1", 1), def("dc", 0));

}
