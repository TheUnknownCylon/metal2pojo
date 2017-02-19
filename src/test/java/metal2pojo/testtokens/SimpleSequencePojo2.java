package metal2pojo.testtokens;

import static io.parsingdata.metal.Shorthand.def;
import static io.parsingdata.metal.Shorthand.seq;

import io.metal2token.pojo.MetalField;
import io.metal2token.pojo.MetalPojo;
import io.parsingdata.metal.token.Token;

@MetalPojo("seq2")
public class SimpleSequencePojo2 {
	@MetalField
	public int def1;

	@MetalField
	public SimpleSequencePojo seq1;

	public static final Token TOKEN = seq("seq2", def("def1", 1), SimpleSequencePojo.TOKEN);

	public static final Token TOKENREV = seq("seq2", SimpleSequencePojo.TOKEN, def("def1", 1));
}
