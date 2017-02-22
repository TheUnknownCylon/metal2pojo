package io.metal2pojo.testtokens.cho;

import static io.parsingdata.metal.Shorthand.def;
import static io.parsingdata.metal.Shorthand.seq;

import io.metal2pojo.pojo.MetalField;
import io.metal2pojo.pojo.MetalPojo;
import io.parsingdata.metal.token.Token;

@MetalPojo("ChoToken2")
public interface ChoToken2 {

	@MetalField
	public Long value1();

	public static Token TOKEN = seq("ChoToken2", def("value1", 1), def("dontcare", 0));

}
