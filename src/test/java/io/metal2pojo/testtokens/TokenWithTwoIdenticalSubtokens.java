package io.metal2pojo.testtokens;

import static io.parsingdata.metal.Shorthand.seq;

import io.metal2pojo.pojo.MetalField;
import io.metal2pojo.pojo.MetalPojo;
import io.parsingdata.metal.token.Token;

@MetalPojo("superOfTwoSubs")
public interface TokenWithTwoIdenticalSubtokens {

	@MetalField
	public TokenWithTokenReference sub1();

	@MetalField
	public TokenWithTokenReference2 sub2();

	public static Token TOKEN = seq("superOfTwoSubs", TokenWithTokenReference.TOKEN, TokenWithTokenReference2.TOKEN);

	public static Token TOKENREV = seq("superOfTwoSubs", TokenWithTokenReference2.TOKEN, TokenWithTokenReference.TOKEN);

}
