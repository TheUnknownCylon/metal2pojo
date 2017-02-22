package io.metal2pojo.testtokens;

import static io.parsingdata.metal.Shorthand.con;
import static io.parsingdata.metal.Shorthand.def;
import static io.parsingdata.metal.Shorthand.repn;
import static io.parsingdata.metal.Shorthand.seq;

import java.util.List;

import io.metal2pojo.pojo.MetalField;
import io.metal2pojo.pojo.MetalPojo;
import io.parsingdata.metal.token.Token;

/**
 * Token that is used to test repn.
 */
@MetalPojo("RepnToken")
public interface RepnToken {

	@MetalField
	public List<SimpleSequenceToken> simpleToken();

	@MetalField
	public List<Integer> s();

	public static Token TOKEN = seq("RepnToken", repn(SimpleSequenceToken.TOKEN, con(2)), repn(def("s", 1), con(2)));

}
