package com.baselet.generator.java.jp;

import java.io.StringReader;

import org.junit.Test;

import com.github.javaparser.JavaParser;

public class JpJavaClassTest {
	@Test
	public void testDiamond() throws Throwable {
		JavaParser.parse(new StringReader(
				"public class Test {List list = new ArrayList<>();}"),
				false);

	}
}
