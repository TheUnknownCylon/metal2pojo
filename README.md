# Metal2Pojo

## Introduction

This package contains some utilities to map a Metal parse graph to a Java Pojo.

[Metal](https://github.com/parsingdata/metal) is "a Java library for parsing 
binary data formats, using declarative descriptions". The description of a 
format can be defined in a very strong way. However, the output of a parsed 
byte string is simply returned as a parse graph. This package is an attempt to
make the use of such a parse graph more easy, by providing a way to map the
parse graph to a more simple to use Java object.

Creating the mapping of a parse graph to another object is achieved by using
a set of annotations on fields on a simple Java object. Mapper logic will look
for the annotated fields in a parse graph. Then the mapper converts the value
to the correct type and sets the fields in a newly created instance of the
simple Java object.


**DISCLAIMER** The work in this package is not well tested. Please consider 
this as a Proof Of Concept.


## Usage

There are two annotations that have to be used to describe a mapping:

* `MetalPojo`, annotate on your Pojo class, to indicate that this Pojo maps to 
   a Metal Token.
* `MetalField`, annotate on a field fields of a MetalPojo annotated class.
   Indicates that the field maps to a value or Token in a parse graph.

### SEQ as a simple token

**IMPORTANT** Only named sequences can be mapped from a Metal parse graph to a 
Pojo.

Example TOKEN (see [PNG format](https://github.com/parsingdata/metal/blob/master/formats/src/main/java/io/parsingdata/metal/format/PNG.java)):


    private static final Token STRUCT =
            seq("chunk",
                def("length", con(4)),
                def("chunktype", con(4), not(eq(con("IEND")))),
                def("chunkdata", last(ref("length"))),
                def("crc32", con(4), eq(crc32(cat(last(ref("chunktype")), last(ref("chunkdata")))))));


Example Pojo:

    @MetalPojo("chunk")  // value must match 'name' of token
    public class PNGCHunk {
    
    	@MetalField // maps the 4 bytes value chunk.length to a long
    	public long length;
    	
    	@MetalField
    	public String chunktype;
    	
    	@MetalField
    	public String chunkdata;
    	
    	@MetalField
    	public long crc32;
    	
    }


### Reference to another token, and repetitions

Example Token:

    public static final Token FORMAT =
            seq("PNG", new Encoding(),
                HEADER,
                rep(STRUCT),
                FOOTER);


Example POJO:

    @MetalPojo(value = "PNG")
    public class PNG {

      @MetalField
      public Header header; // Header is a class also annotated with @MetalPojo
                            // Field name does not matter because anoter Pojo is referenced
      
      @MetalField
      public List<Struct> struct;
      
      @MetalField
      public Footer footer;
      
    }


### CHO

In token:

    cho(TOKENA, TOKENB)

In POJO, define an Optional, where the provided generic type is the actual
type in the parse graph. Use the Optional API to check which of the elements
was present in the parse graph.

    @MetalField
    Optional<TOKENA> tokenA;  // field name does not matter

    @MetalField
    Optional<TOKENB> tokenB;


### Supported types:

By default Metal2Pojo supports the following types:

 * Integer values (`long`, `Long`)
 * Long values (`int`, `Integer`)
 * String values (`String`)
 * Other tokens (classes annotated with `@MetalPojo`)a

Additionally, it is possible to define a custom converter. Converters
must implement the `MetalTypeConverter` interface. Currently only `ParseValue`s
in a parse graph can be converted (as opposed to sub-parse graphs that can not
be converted yet). The argument `MetalField.converter`can be used to set a 
different type converter:

    @MetalField(converter = MyConverter.class)
    public MyType foo;


### Limitations

Limitations (behaviour is undefined if not true):

 * Use a specific token only once inside a token.
 * Static and distinct tokens names inside a Token.


## Token and Pojo co-evolution

 * Is not handled. Recommended is to make proper unit tests. Also one could
   put the token and Pojo in same class.
