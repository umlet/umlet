package com.baselet.control;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SharedUtilsTest {

	@Test
	public void testRealignToGridRoundToNearest() {
		assertEquals(10, SharedUtils.realignToGridRoundToNearest(false, 5.0));
		assertEquals(10, SharedUtils.realignToGridRoundToNearest(false, 9.0));
		assertEquals(0, SharedUtils.realignToGridRoundToNearest(false, 4.0));
		assertEquals(-10, SharedUtils.realignToGridRoundToNearest(false, -5.0));
		assertEquals(-10, SharedUtils.realignToGridRoundToNearest(false, -9.0));
		assertEquals(0, SharedUtils.realignToGridRoundToNearest(false, -3.0));
		assertEquals(0, SharedUtils.realignToGridRoundToNearest(false, 0));
	}
}
