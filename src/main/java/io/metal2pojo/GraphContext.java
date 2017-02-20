package io.metal2pojo;

import java.util.ArrayList;
import java.util.List;

import io.metal2pojo.exceptions.TokenNotFoundException;
import io.parsingdata.metal.data.ImmutableList;
import io.parsingdata.metal.data.ParseGraph;
import io.parsingdata.metal.data.ParseItem;

public class GraphContext {
	private final ArrayList<String> _namePath;
	private final ParseGraph _graph;

	public GraphContext(final ParseGraph graph) {
		this(new ArrayList<>(), graph);
	}

	public GraphContext(final ArrayList<String> namePath, final ParseGraph graph) {
		_namePath = namePath;
		_graph = graph;
	}

	public GraphContext subGraph(final String pojoTokenName) throws TokenNotFoundException {
		final ArrayList<String> newName = new ArrayList<>(_namePath);
		newName.add(pojoTokenName);
		final ImmutableList<ParseItem> newGraph = ByTokenName.getAllRoots(_graph, pojoTokenName);

		if (newGraph.isEmpty()) {
			throw new TokenNotFoundException();
		} else if (!newGraph.tail.isEmpty()) {
			throw new IllegalStateException("More than one subgraph found");
		}

		return new GraphContext(newName, newGraph.head.asGraph());
	}

	public List<GraphContext> subGraphs(final String pojoTokenName) {
		final ArrayList<String> newName = new ArrayList<>(_namePath);
		newName.add(pojoTokenName);
		ImmutableList<ParseItem> values = ByTokenName.getAllRoots(_graph, pojoTokenName).reverse();
		final List<GraphContext> subGraphs = new ArrayList<>();
		while (values.size > 0) {
			subGraphs.add(new GraphContext(newName, values.head.asGraph()));
			values = values.tail;
		}
		return subGraphs;
	}

	public ParseGraph graph() {
		return _graph;
	}

	public String nameOfParseValue(final String name) {
		final ArrayList<String> newName = new ArrayList<>(_namePath);
		newName.add(name);
		return nameToPath(newName);
	}

	private static String nameToPath(final ArrayList<String> names) {
		// o no : no 8
		final StringBuffer buf = new StringBuffer();
		for (final String name : names) {
			if (buf.length() > 0) {
				buf.append('.');
			}
			buf.append(name);
		}
		return buf.toString();
	}

}
