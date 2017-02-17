package example.png;

import static io.parsingdata.metal.Shorthand.con;
import static io.parsingdata.metal.Shorthand.def;
import static io.parsingdata.metal.Shorthand.eq;
import static io.parsingdata.metal.Shorthand.seq;

import io.parsingdata.metal.token.Token;
import pojo.MetalField;
import pojo.MetalPojo;

@MetalPojo("signature")
public class Header {
	
	@MetalField("highbit")
	public Long highbit;

	@MetalField("PNG")
	public String png;
	
	@MetalField("controlchars")
	public Long controlchars;
	
    static final Token FORMAT =
            seq("signature",
                def("highbit", con(1), eq(con(0x89))),
                def("PNG", con(3), eq(con("PNG"))),
                def("controlchars", con(4), eq(con(0x0d, 0x0a, 0x1a, 0x0a))));
    
    @Override
	public String toString() {
    	final StringBuffer buffer = new StringBuffer();
    	buffer.append("HEADER.png: ");
    	buffer.append(png);
    	return buffer.toString();
    }
}
