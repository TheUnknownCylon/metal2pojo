package metal2pojo.testtokens;

import static io.parsingdata.metal.Shorthand.def;
import static io.parsingdata.metal.Shorthand.seq;

import io.metal2token.pojo.MetalField;
import io.metal2token.pojo.MetalPojo;
import io.parsingdata.metal.token.Token;

@MetalPojo("super")
public class SubToken {

	@MetalField
	public SimpleSequencePojo noname;

	@MetalField
	public long value;

	public static Token TOKEN = seq("super", SimpleSequencePojo.TOKEN, def("value", 1));
}
