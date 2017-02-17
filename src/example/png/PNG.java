package example.png;

import static io.parsingdata.metal.Shorthand.rep;
import static io.parsingdata.metal.Shorthand.seq;

import java.util.List;

import io.parsingdata.metal.encoding.Encoding;
import io.parsingdata.metal.token.Token;
import pojo.MetalField;
import pojo.MetalPojo;
import pojo.MetalTokenRef;

@MetalPojo(value = "PNG")
public class PNG {
	
	@MetalTokenRef
	public Header header;
	
	@MetalField("chunktype")
	public List<String> chunktype;

	@MetalTokenRef
	public Footer footer;
	
    public static final Token FORMAT =
            seq("PNG", new Encoding(),
                Header.FORMAT,
                rep(Chunk.FORMAT),
                Footer.FORMAT);
    
    @Override
	public String toString() {
    	final StringBuffer buffer = new StringBuffer();
    	
    	buffer.append("PNG\n___\n");
    	buffer.append(header);
    	buffer.append('\n');
    	for(final String ct : chunktype) {
    		buffer.append(ct);
    		buffer.append('\n');
    	}
    	buffer.append(footer);
    	return buffer.toString();
    }
}
