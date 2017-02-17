package example.png;

import static io.parsingdata.metal.Shorthand.cat;
import static io.parsingdata.metal.Shorthand.con;
import static io.parsingdata.metal.Shorthand.def;
import static io.parsingdata.metal.Shorthand.eq;
import static io.parsingdata.metal.Shorthand.last;
import static io.parsingdata.metal.Shorthand.not;
import static io.parsingdata.metal.Shorthand.ref;
import static io.parsingdata.metal.Shorthand.seq;
import static io.parsingdata.metal.format.Callback.crc32;

import io.parsingdata.metal.token.Token;
import pojo.MetalField;
import pojo.MetalPojo;

@MetalPojo("chunk")
public class Chunk {
	
	@MetalField("length")
	public Integer length;
	
	@MetalField("chunktype")
	public String chunktype;
	
	@MetalField("chunkdata")
	public Byte[] chunkdata;
	
	@MetalField("crc32")
	public Long crc32;
	
    static final Token FORMAT =
            seq("chunk",
                def("length", con(4)),
                def("chunktype", con(4), not(eq(con("IEND")))),
                def("chunkdata", last(ref("length"))),
                def("crc32", con(4), eq(crc32(cat(last(ref("chunktype")), last(ref("chunkdata")))))));

}
