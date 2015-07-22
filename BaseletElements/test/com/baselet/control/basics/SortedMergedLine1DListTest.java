package com.baselet.control.basics;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class SortedMergedLine1DListTest {

	private SortedMergedLine1DList list;

	@Before
	public void init() {
		list = new SortedMergedLine1DList();
	}

	@Test
	public void testAddLine1DEmpty() {
		list.add(new Line1D(0, 10));
		assertEquals(1, list.size());
		assertEquals(new Line1D(0, 10), list.get(0));
	}

	@Test
	public void testAddLine1DNoOverlap() {
		list.add(new Line1D(0, 10));
		list.add(new Line1D(10.1, 20));
		list.add(new Line1D(30, 40));
		assertEquals(3, list.size());
		assertEquals(new Line1D(10.1, 20), list.get(1));
	}

	@Test
	public void testAddLine1DOverlap1() {
		list.add(new Line1D(0, 10));
		list.add(new Line1D(10.1, 20));
		list.add(new Line1D(30, 40));
		assertEquals(3, list.size());
		list.add(new Line1D(0, 40));
		assertEquals(1, list.size());
		assertEquals(new Line1D(0, 40), list.get(0));
	}

	@Test
	public void testAddLine1DOverlap2() {
		list.add(new Line1D(0, 10));
		list.add(new Line1D(30, 40));
		assertEquals(2, list.size());
		list.add(new Line1D(5, 15));
		assertEquals(2, list.size());
		assertEquals(new Line1D(0, 15), list.get(0));
		assertEquals(new Line1D(30, 40), list.get(1));
	}

	@Test
	public void testAddLine1DOverlap3() {
		list.add(new Line1D(0, 10));
		list.add(new Line1D(30, 40));
		assertEquals(2, list.size());
		list.add(new Line1D(25, 35));
		assertEquals(2, list.size());
		assertEquals(new Line1D(0, 10), list.get(0));
		assertEquals(new Line1D(25, 40), list.get(1));
	}

}
