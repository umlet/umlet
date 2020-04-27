package com.baselet.plugin.refactoring;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.baselet.plugin.refactoring.JavaDocParser.HtmlTagStartNode;
import com.baselet.plugin.refactoring.JavaDocParser.JavaDocCommentNode;
import com.baselet.plugin.refactoring.JavaDocParser.SourceString;

public class JavaDocParserTest {

	@Test
	public void testSimpleJavadoc() {
		String source = "/**\n * Hello World\n */";
		JavaDocCommentNode comment = new JavaDocParser(source).comment();
		assertEquals(0, comment.children.size());
	}

	@Test
	public void testHtmlTag() {
		String source = "/**<p>*/";
		JavaDocCommentNode comment = new JavaDocParser(source).comment();
		assertEquals(1, comment.children.size());

		assertEquals(new HtmlTagStartNode(new SourceString(source, 4, 5), 3, 6), comment.children.get(0));
	}

	@Test
	public void testHtmlTagNoClosingParenthesis() {
		String source = "/**<a<b>*/";
		JavaDocCommentNode comment = new JavaDocParser(source).comment();
		assertEquals(1, comment.children.size());
		assertEquals(new HtmlTagStartNode(new SourceString(source, 6, 7), 5, 8), comment.children.get(0));
	}

	@Test
	public void testClosingTag() {
		String source = "/**</a><b>*/";
		JavaDocCommentNode comment = new JavaDocParser(source).comment();
		assertEquals(1, comment.children.size());
		assertEquals(new HtmlTagStartNode(new SourceString(source, 8, 9), 7, 10), comment.children.get(0));
	}

	@Test
	public void testImgRef() {
		JavaDocCommentNode comment = new JavaDocParser("*<img src=\"foo.png\" alt=\"\"/>*").comment();
		assertEquals(1, comment.children.size());
		HtmlTagStartNode ref = (HtmlTagStartNode) comment.children.get(0);
		assertEquals("img", ref.tagName.getValue());
		assertEquals("src", ref.attrs.get(0).key.getValue());
		assertEquals("foo.png", ref.attrs.get(0).value.getValue());
		assertEquals(11, ref.attrs.get(0).value.start);
		assertEquals(18, ref.attrs.get(0).value.end);
	}
}
