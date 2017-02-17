package example.test;

import static io.parsingdata.metal.util.EncodingFactory.le;
import static io.parsingdata.metal.util.EnvironmentFactory.stream;

import java.io.IOException;

import io.parsingdata.metal.data.Environment;
import io.parsingdata.metal.data.ParseResult;
import mapper.MetalPojoFillerException;
import mapper.PojoMapper;

public class Main {

	public static void main(final String[] args) throws MetalPojoFillerException, IOException {
		final Environment env = stream(7, 1, 2);
		final ParseResult result = Aa.TOKEN.parse(env, le());

		System.out.println(result);
		final Aa x = PojoMapper.fillPojo(Aa.class, result.environment.order);
		x.opt2.ifPresent(y -> System.out.println(y.opt2B));
		
	}
}