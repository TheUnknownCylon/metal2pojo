package io.metal2pojo.testtokens;

import static io.parsingdata.metal.Shorthand.con;
import static io.parsingdata.metal.Shorthand.def;
import static io.parsingdata.metal.Shorthand.repn;
import static io.parsingdata.metal.Shorthand.seq;

import java.util.List;

import io.metal2pojo.pojo.MetalField;
import io.metal2pojo.pojo.MetalPojo;
import io.parsingdata.metal.token.Token;

@MetalPojo("repntoken")
public class RepnToken {

	@MetalField
	public List<SimpleSequencePojo> simpleToken;

	@MetalField
	public List<Integer> s;

	public static Token TOKEN = seq("repntoken", repn(SimpleSequencePojo.TOKEN, con(2)), repn(def("s", 1), con(2)));

}
