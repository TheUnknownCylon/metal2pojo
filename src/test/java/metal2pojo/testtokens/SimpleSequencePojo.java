package metal2pojo.testtokens;

import static io.parsingdata.metal.Shorthand.def;
import static io.parsingdata.metal.Shorthand.seq;

import io.metal2token.pojo.MetalField;
import io.metal2token.pojo.MetalPojo;
import io.parsingdata.metal.token.Token;

@MetalPojo("seq1")
public class SimpleSequencePojo {

	@MetalField
	public int def1;

	@MetalField
	public int def2;

	public static final Token TOKEN = seq("seq1", def("def1", 1), def("def2", 1));

}