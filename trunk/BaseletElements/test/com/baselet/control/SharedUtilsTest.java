package com.baselet.control;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SharedUtilsTest {

	@Test
	public void testrealignToRoundToNearest() {
		assertEquals(10, SharedUtils.realignToRoundToNearest(false, 5.0, SharedConstants.DEFAULT_GRID_SIZE));
		assertEquals(10, SharedUtils.realignToRoundToNearest(false, 9.0, SharedConstants.DEFAULT_GRID_SIZE));
		assertEquals(0, SharedUtils.realignToRoundToNearest(false, 4.0, SharedConstants.DEFAULT_GRID_SIZE));
		assertEquals(-10, SharedUtils.realignToRoundToNearest(false, -5.0, SharedConstants.DEFAULT_GRID_SIZE));
		assertEquals(-10, SharedUtils.realignToRoundToNearest(false, -9.0, SharedConstants.DEFAULT_GRID_SIZE));
		assertEquals(0, SharedUtils.realignToRoundToNearest(false, -3.0, SharedConstants.DEFAULT_GRID_SIZE));
		assertEquals(0, SharedUtils.realignToRoundToNearest(false, 0, SharedConstants.DEFAULT_GRID_SIZE));
	}
}
