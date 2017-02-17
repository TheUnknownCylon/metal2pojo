package example.png;

import static io.parsingdata.metal.Shorthand.con;
import static io.parsingdata.metal.Shorthand.def;
import static io.parsingdata.metal.Shorthand.eq;
import static io.parsingdata.metal.Shorthand.eqNum;
import static io.parsingdata.metal.Shorthand.seq;

import io.parsingdata.metal.token.Token;
import pojo.MetalField;
import pojo.MetalPojo;

@MetalPojo("footer")
public class Footer {
	
	@MetalField("footerlength")
	public Integer footerLength;
	
	@MetalField("footertype")
	public String footertype;
	
	@MetalField("footercrc32")
	public Long footercrc32;
	
	static final Token FORMAT = seq("footer",
            def("footerlength", con(4), eqNum(con(0))),
            def("footertype", con(4), eq(con("IEND"))),
            def("footercrc32", con(4), eq(con(0xae, 0x42, 0x60, 0x82))));
	
	public String toString() {
		return "FOOTER: length: " + footerLength + " type: " + footertype;
	}

}
