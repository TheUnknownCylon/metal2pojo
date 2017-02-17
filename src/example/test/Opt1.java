package example.test;

import static io.parsingdata.metal.Shorthand.con;
import static io.parsingdata.metal.Shorthand.def;
import static io.parsingdata.metal.Shorthand.eq;
import static io.parsingdata.metal.Shorthand.seq;

import io.parsingdata.metal.token.Token;
import pojo.MetalField;
import pojo.MetalPojo;

@MetalPojo("OPT1")
public class Opt1 {

	@MetalField("opt1A")
	public int opt1A;

	@MetalField("opt1B")
	public int opt1B;

	public static final Token TOKEN = seq("OPT1", def("opt1A", 1, eq(con(0))), def("opt1B", 1));

}
