package example.test;

import static io.parsingdata.metal.Shorthand.def;
import static io.parsingdata.metal.Shorthand.seq;

import io.parsingdata.metal.token.Token;
import pojo.Convert;
import pojo.MetalField;
import pojo.MetalPojo;

@MetalPojo("OPT2")
public class Opt2 {

	@MetalField("opt2A")
	public int opt2A;

	@Convert(MyMapper.class)
	@MetalField("opt2B")
	public int opt2B;

	public static final Token TOKEN = seq("OPT2", def("opt2A", 1), def("opt2B", 1));

}
