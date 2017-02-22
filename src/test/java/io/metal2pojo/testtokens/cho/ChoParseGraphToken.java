package io.metal2pojo.testtokens.cho;

import static io.parsingdata.metal.Shorthand.cho;
import static io.parsingdata.metal.Shorthand.def;
import static io.parsingdata.metal.Shorthand.seq;

import java.util.Optional;

import io.metal2pojo.pojo.MetalField;
import io.metal2pojo.pojo.MetalPojo;
import io.parsingdata.metal.token.Token;

@MetalPojo("ChoParseGraphToken")
public interface ChoParseGraphToken {

	@MetalField
	public Optional<ChoToken1> value1();

	@MetalField
	public Optional<ChoToken2> value2();

	public static Token TOKEN = seq("ChoParseGraphToken", cho(ChoToken1.TOKEN, ChoToken2.TOKEN), def("dontcare", 0));
}
