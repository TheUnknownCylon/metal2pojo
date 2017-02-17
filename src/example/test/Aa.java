package example.test;

import static io.parsingdata.metal.Shorthand.cho;
import static io.parsingdata.metal.Shorthand.def;
import static io.parsingdata.metal.Shorthand.seq;

import java.util.Optional;

import io.parsingdata.metal.token.Token;
import pojo.MetalField;
import pojo.MetalPojo;
import pojo.MetalTokenRef;

@MetalPojo("AA")
public class Aa {

	@MetalField("header")
	public int header;

	@MetalTokenRef
	public Optional<Opt1> opt1;

	@MetalTokenRef
	public Optional<Opt2> opt2;

	public static final Token TOKEN = seq("AA", def("header", 1), cho(Opt1.TOKEN, Opt2.TOKEN));

}
