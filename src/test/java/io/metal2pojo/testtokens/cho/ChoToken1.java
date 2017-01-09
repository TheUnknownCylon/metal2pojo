package io.metal2pojo.testtokens.cho;

import static io.parsingdata.metal.Shorthand.con;
import static io.parsingdata.metal.Shorthand.def;
import static io.parsingdata.metal.Shorthand.ltNum;
import static io.parsingdata.metal.Shorthand.seq;

import io.metal2pojo.pojo.MetalField;
import io.metal2pojo.pojo.MetalPojo;
import io.parsingdata.metal.token.Token;

@MetalPojo("ChoToken1")
public class ChoToken1 {

	@MetalField
	public Integer value1;

	public static Token TOKEN = seq("ChoToken1", def("value1", 1, ltNum(con(9))), def("dontcare", 0));

}
