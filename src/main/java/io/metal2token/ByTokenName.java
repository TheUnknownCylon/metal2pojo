package io.metal2token;

import static io.parsingdata.metal.data.ParseResult.failure;

import java.io.IOException;

import io.parsingdata.metal.data.Environment;
import io.parsingdata.metal.data.ImmutableList;
import io.parsingdata.metal.data.ParseGraph;
import io.parsingdata.metal.data.ParseItem;
import io.parsingdata.metal.data.ParseResult;
import io.parsingdata.metal.data.selection.ByToken;
import io.parsingdata.metal.encoding.Encoding;
import io.parsingdata.metal.token.Token;
import io.parsingdata.metal.token.TokenRef;

/**
 * Utility class that can find subgraphs in parse graphs given a token name.
 * Based on {@link TokenRef}.
 * 
 * @see ByToken
 * @see ByName.
 */
public class ByTokenName {

	/**
	 * Given a ParseGraph and a token name (referenceName), get all sub-graphs
	 * for this token.
	 * 
	 * @param graph
	 *            Graph to find subgraphs in.
	 * @param referenceName
	 *            The name of the token to be found.
	 * @return All sub-root-graphs for matches with referenceName.
	 */
	public static ImmutableList<ParseItem> getAllRoots(final ParseGraph graph, final String referenceName) {
		final Token token = lookup(graph, referenceName);
		return ByToken.getAllRoots(graph, token);
	}

	private static final Token LOOKUP_FAILED = new Token("LOOKUP_FAILED", null) {
		@Override
		protected ParseResult parseImpl(final String scope, final Environment environment, final Encoding encoding)
				throws IOException {
			return failure(environment);
		}
	};

	private static Token lookup(final ParseItem item, final String referenceName) {
		if (item.getDefinition().name.equals(referenceName)) {
			return item.getDefinition();
		}
		if (!item.isGraph() || item.asGraph().isEmpty()) {
			return LOOKUP_FAILED;
		}
		final Token headResult = lookup(item.asGraph().head, referenceName);
		if (headResult != LOOKUP_FAILED) {
			return headResult;
		}
		return lookup(item.asGraph().tail, referenceName);
	}
}