package io.metal2token.pojo;

import io.parsingdata.metal.data.ParseGraph;
import io.parsingdata.metal.data.ParseValue;

public class MapContext {

	private final Class<?> _type;
	private ParseGraph _parseGraph;
	private ParseValue _parseValue;

	public MapContext(final Class<?> type, final ParseValue value) {
		_type = type;
		_parseValue = value;
	}

	public MapContext(final Class<?> type, final ParseGraph value) {
		_type = type;
		_parseGraph = value;
	}

	public Class<?> type() {
		return _type;
	}

	public ParseGraph parseGraph() {
		return _parseGraph;
	}

	public ParseValue parseValue() {
		return _parseValue;
	}

}
