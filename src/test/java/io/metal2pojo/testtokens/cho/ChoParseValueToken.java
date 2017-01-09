package io.metal2pojo.testtokens.cho;

import static io.parsingdata.metal.Shorthand.cho;
import static io.parsingdata.metal.Shorthand.con;
import static io.parsingdata.metal.Shorthand.def;
import static io.parsingdata.metal.Shorthand.ltNum;
import static io.parsingdata.metal.Shorthand.seq;

import java.util.Optional;

import io.metal2pojo.pojo.MetalField;
import io.metal2pojo.pojo.MetalPojo;
import io.parsingdata.metal.token.Token;

@MetalPojo("ChoToken")
public class ChoParseValueToken {

	@MetalField
	public Optional<Integer> value1;

	@MetalField
	public Optional<Long> value2;

	public static Token TOKEN = seq(
			"ChoToken", 
			cho(
					def("value1", 1, ltNum(con(9))),
					def("value2", 1)
			),
			def("dontcare", 0)
		);

}
