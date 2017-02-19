package metal2pojo;

import static io.parsingdata.metal.util.EncodingFactory.le;
import static io.parsingdata.metal.util.EnvironmentFactory.stream;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Test;

import io.metal2token.PojoMapper;
import io.metal2token.exceptions.TokenConversionException;
import io.metal2token.exceptions.TokenNotFoundException;
import io.parsingdata.metal.data.Environment;
import metal2pojo.testtokens.RepnToken;
import metal2pojo.testtokens.SimpleSequencePojo;
import metal2pojo.testtokens.SimpleSequencePojo2;
import metal2pojo.testtokens.SubToken;

public class PojoMapperTest {

	@Test
	public void testSimpleToken() throws IOException, TokenNotFoundException, TokenConversionException {
		final Environment env = stream(1, 2);
		final SimpleSequencePojo pojo = PojoMapper.fillPojo(SimpleSequencePojo.class, SimpleSequencePojo.TOKEN, env,
				le());
		assertThat(pojo.def1, is(equalTo(1)));
		assertThat(pojo.def2, is(equalTo(2)));
	}

	@Test
	public void testSubToken() throws IOException, TokenNotFoundException, TokenConversionException {
		final Environment env = stream(1, 2, 3);
		final SubToken pojo = PojoMapper.fillPojo(SubToken.class, SubToken.TOKEN, env, le());
		assertThat(pojo.value, is(equalTo(3L)));
		assertThat(pojo.noname.def1, is(equalTo(1)));
		assertThat(pojo.noname.def2, is(equalTo(2)));
	}

	@Test
	public void testRepn() throws IOException, TokenNotFoundException, TokenConversionException {
		final Environment env = stream(1, 2, 3, 4, 5, 6, 7, 8, 9);
		final RepnToken pojo = PojoMapper.fillPojo(RepnToken.class, RepnToken.TOKEN, env, le());

		assertThat(pojo.simpleToken.size(), is(equalTo(2)));
		assertThat(pojo.simpleToken.get(0).def1, is(equalTo(1)));
		assertThat(pojo.simpleToken.get(0).def2, is(equalTo(2)));
		assertThat(pojo.simpleToken.get(1).def1, is(equalTo(3)));
		assertThat(pojo.simpleToken.get(1).def2, is(equalTo(4)));

		assertThat(pojo.s.size(), is(equalTo(2)));
		assertThat(pojo.s.get(0), is(equalTo(5)));
		assertThat(pojo.s.get(1), is(equalTo(6)));
	}

	@Test
	public void duplicateDefName() throws IOException, TokenNotFoundException, TokenConversionException {
		final Environment env = stream(1, 2, 3);
		final SimpleSequencePojo2 pojo = PojoMapper.fillPojo(SimpleSequencePojo2.class, SimpleSequencePojo2.TOKEN, env,
				le());
		assertThat(pojo.def1, is(equalTo(1)));
		assertThat(pojo.seq1.def1, is(equalTo(2)));
		assertThat(pojo.seq1.def2, is(equalTo(3)));
	}

	@Test
	public void duplicateDefNameRev() throws IOException, TokenNotFoundException, TokenConversionException {
		final Environment env = stream(1, 2, 3);
		final SimpleSequencePojo2 pojoRef = PojoMapper.fillPojo(SimpleSequencePojo2.class, SimpleSequencePojo2.TOKENREV,
				env, le());
		assertThat(pojoRef.def1, is(equalTo(3)));
		assertThat(pojoRef.seq1.def1, is(equalTo(1)));
		assertThat(pojoRef.seq1.def2, is(equalTo(2)));
	}
}
