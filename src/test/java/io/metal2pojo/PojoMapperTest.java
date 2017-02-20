package io.metal2pojo;

import static io.parsingdata.metal.util.EncodingFactory.le;
import static io.parsingdata.metal.util.EnvironmentFactory.stream;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Test;

import io.metal2pojo.exceptions.TokenConversionException;
import io.metal2pojo.exceptions.TokenNotFoundException;
import io.metal2pojo.testtokens.RepnToken;
import io.metal2pojo.testtokens.SimpleSequenceToken;
import io.metal2pojo.testtokens.TokenWithTokenReference;
import io.metal2pojo.testtokens.TokenWithTwoIdenticalSubtokens;
import io.parsingdata.metal.data.Environment;

public class PojoMapperTest {

	@Test
	public void testSimpleToken() throws IOException, TokenNotFoundException, TokenConversionException {
		final Environment env = stream(1, 2);
		final SimpleSequenceToken pojo = PojoMapper.fillPojo(SimpleSequenceToken.class, SimpleSequenceToken.TOKEN, env,
				le());
		assertThat(pojo.def1, is(equalTo(1)));
		assertThat(pojo.def2, is(equalTo(2)));
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
		final TokenWithTokenReference pojo = PojoMapper.fillPojo(TokenWithTokenReference.class,
				TokenWithTokenReference.TOKEN, env,
				le());
		assertThat(pojo.def1, is(equalTo(1)));
		assertThat(pojo.seq1.def1, is(equalTo(2)));
		assertThat(pojo.seq1.def2, is(equalTo(3)));
	}

	@Test
	public void duplicateDefNameRev() throws IOException, TokenNotFoundException, TokenConversionException {
		final Environment env = stream(1, 2, 3);
		final TokenWithTokenReference pojoRef = PojoMapper.fillPojo(TokenWithTokenReference.class,
				TokenWithTokenReference.TOKENREV,
				env, le());
		assertThat(pojoRef.def1, is(equalTo(3)));
		assertThat(pojoRef.seq1.def1, is(equalTo(1)));
		assertThat(pojoRef.seq1.def2, is(equalTo(2)));
	}

	@Test
	public void duplicateSubGraph() throws IOException, TokenNotFoundException, TokenConversionException {
		final Environment env = stream(1, 2, 3, 4, 5, 6);
		final TokenWithTwoIdenticalSubtokens pojoRef = PojoMapper.fillPojo(TokenWithTwoIdenticalSubtokens.class, TokenWithTwoIdenticalSubtokens.TOKEN, env, le());
		assertThat(pojoRef.sub1.def1, is(equalTo(1)));
		assertThat(pojoRef.sub1.seq1.def1, is(equalTo(2)));
		assertThat(pojoRef.sub1.seq1.def2, is(equalTo(3)));

		assertThat(pojoRef.sub2.def1, is(equalTo(4)));
		assertThat(pojoRef.sub2.seq1.def1, is(equalTo(5)));
		assertThat(pojoRef.sub2.seq1.def2, is(equalTo(6)));
	}

	@Test
	public void duplicateSubGraphREV() throws IOException, TokenNotFoundException, TokenConversionException {
		final Environment env = stream(1, 2, 3, 4, 5, 6);
		final TokenWithTwoIdenticalSubtokens pojoRef = PojoMapper.fillPojo(TokenWithTwoIdenticalSubtokens.class, TokenWithTwoIdenticalSubtokens.TOKENREV, env, le());
		assertThat(pojoRef.sub2.def1, is(equalTo(1)));
		assertThat(pojoRef.sub2.seq1.def1, is(equalTo(2)));
		assertThat(pojoRef.sub2.seq1.def2, is(equalTo(3)));

		assertThat(pojoRef.sub1.def1, is(equalTo(4)));
		assertThat(pojoRef.sub1.seq1.def1, is(equalTo(5)));
		assertThat(pojoRef.sub1.seq1.def2, is(equalTo(6)));
	}

}
