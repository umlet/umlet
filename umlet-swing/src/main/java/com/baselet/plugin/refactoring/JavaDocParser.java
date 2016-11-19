package com.baselet.plugin.refactoring;

import java.util.ArrayList;
import java.util.List;

/**
 * Parser for javadoc comments.
 *
 * <p> Used to for refactorings in comments. It is implemented as PEG parser</p>
 *<p>Placed here since unit tests are not available in the eclipse plugin project.
 *</p>
 */
public class JavaDocParser {
	private final String source;
	private int idx;
	private int sourceEnd;

	public JavaDocParser(String source) {
		this(source, 0, source.length());
	}

	public JavaDocParser(String source, int sourceStart, int sourceEnd) {
		this.source = source;
		idx = sourceStart;
		this.sourceEnd = sourceEnd;
	}

	public static abstract class JavaDocNodeBase {}

	public static class JavaDocCommentNode extends JavaDocNodeBase {
		public List<JavaDocNodeBase> children = new ArrayList<JavaDocNodeBase>();

		public <T extends JavaDocNodeBase> List<T> ofType(Class<T> clazz) {
			ArrayList<T> result = new ArrayList<T>();
			for (JavaDocNodeBase child : children) {
				if (clazz.isInstance(child))
					result.add(clazz.cast(child));
			}
			return result;
		}
	}

	public static class HtmlTagAttr {

		public SourceString key;
		public SourceString value;
		public int start;
		public int end;

		public HtmlTagAttr(SourceString key, SourceString value, int start, int end) {
			this.key = key;
			this.value = value;
			this.start = start;
			this.end = end;
		}

		@Override
		public String toString() {
			return "HtmlTagAttr [key=" + key + ", value=" + value + ", start=" + start + ", end=" + end + "]";
		}

		public int length() {
			return end - start;
		}
	}

	public static class SourceString {
		public String source;
		public int start;
		public int end;

		public SourceString(String source, int start, int end) {
			super();
			this.source = source;
			this.start = start;
			this.end = end;
		}

		public String getValue() {
			return source.substring(start, end);
		}

		public int length() {
			return end - start;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + end;
			result = prime * result + start;
			result = prime * result + ((source == null) ? 0 : source.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof SourceString))
				return false;
			SourceString other = (SourceString) obj;
			if (end != other.end)
				return false;
			if (start != other.start)
				return false;
			if (source == null) {
				if (other.source != null)
					return false;
			}
			else if (!source.equals(other.source))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "SourceString [value=" + getValue() + ", start=" + start + ", end=" + end + "]";
		}
	}

	public static class HtmlTagStartNode extends JavaDocNodeBase {
		public SourceString tagName;
		public List<HtmlTagAttr> attrs = new ArrayList<JavaDocParser.HtmlTagAttr>();
		public int start;
		public int end;

		public HtmlTagStartNode() {}

		public HtmlTagStartNode(SourceString tagName, int start, int end) {
			this.tagName = tagName;
			this.start = start;
			this.end = end;
		}

		public HtmlTagStartNode with(SourceString key, SourceString value, int start, int end) {
			attrs.add(new HtmlTagAttr(key, value, start, end));
			return this;
		}

		@Override
		public String toString() {
			return "HtmlTagStartNode [tagName=" + tagName + ", attrs=" + attrs + ", start=" + start + ", end=" + end + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((attrs == null) ? 0 : attrs.hashCode());
			result = prime * result + end;
			result = prime * result + start;
			result = prime * result + ((tagName == null) ? 0 : tagName.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof HtmlTagStartNode))
				return false;
			HtmlTagStartNode other = (HtmlTagStartNode) obj;
			if (attrs == null) {
				if (other.attrs != null)
					return false;
			}
			else if (!attrs.equals(other.attrs))
				return false;
			if (end != other.end)
				return false;
			if (start != other.start)
				return false;
			if (tagName == null) {
				if (other.tagName != null)
					return false;
			}
			else if (!tagName.equals(other.tagName))
				return false;
			return true;
		}

		public HtmlTagAttr getAttr(String key) {
			for (HtmlTagAttr attr : attrs) {
				if (attr.key.getValue().equals(key))
					return attr;
			}
			return null;
		}

		public boolean hasAttr(String key) {
			return getAttr(key) != null;
		}

		public int length() {
			return end - start;
		}

	}

	/**
	 * <pre>
	 * comment = (
	 *     htmlTagStart
	 *   | anyChar
	 *   )*
	 * </pre>
	 */
	public JavaDocCommentNode comment() {
		JavaDocCommentNode result = new JavaDocCommentNode();
		while (!isEof()) {
			int start = idx;
			HtmlTagStartNode node = htmlTagStart();
			if (node != null) {
				result.children.add(node);
				continue;
			}
			idx = start;
			nextCp();
		}
		return result;
	}

	/**
	 * <pre>
	 * "<" ident w* attrs "/"? ">"
	 * </pre>
	 */
	public HtmlTagStartNode htmlTagStart() {
		HtmlTagStartNode result = new HtmlTagStartNode();
		result.start = idx;
		if (!consume('<')) {
			return null;
		}

		result.tagName = ident();
		if (result.tagName == null) {
			return null;
		}
		optWhitespace();
		result.attrs = attrs();

		consume('/');

		if (!consume('>')) {
			return null;
		}
		result.end = idx;

		return result;
	}

	/**
	 * <pre>
	 * attrs = (ident w* "=" w* "\"" !"\"" "\"" w*)*
	 * </pre>
	 */
	public List<HtmlTagAttr> attrs() {
		List<HtmlTagAttr> result = new ArrayList<JavaDocParser.HtmlTagAttr>();
		while (true) {
			int start = idx;
			SourceString key = ident();
			if (key == null) {
				idx = start;
				return result;
			}
			optWhitespace();
			if (!consume('=')) {
				idx = start;
				return result;
			}
			optWhitespace();
			if (!consume('"')) {
				idx = start;
				return result;
			}
			int valueStart = idx;
			while (!isEof() && cp() != '"') {
				nextCp();
			}
			SourceString value = new SourceString(source, valueStart, idx);
			if (!consume('"')) {
				idx = start;
				return result;
			}
			optWhitespace();
			result.add(new HtmlTagAttr(key, value, start, idx));
		}
	}

	public void optWhitespace() {
		while (!isEof() && (Character.isWhitespace(cp()) || cp() == '\n' || cp() == '\r')) {
			nextCp();
		}
	}

	public SourceString ident() {
		if (isEof() || !Character.isJavaIdentifierStart(cp())) {
			return null;
		}
		int start = idx;
		nextCp();

		while (!isEof() && Character.isJavaIdentifierPart(cp())) {
			nextCp();
		}
		return new SourceString(source, start, idx);
	}

	private boolean isEof() {
		return idx >= sourceEnd;
	}

	private void nextCp() {
		idx += Character.charCount(cp());
	}

	private int cp() {
		return source.codePointAt(idx);
	}

	private boolean consume(char ch) {
		if (isEof()) {
			return false;
		}

		if (cp() != ch) {
			return false;
		}
		nextCp();
		return true;
	}
}
