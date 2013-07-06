package com.baselet.control;

import org.apache.log4j.Logger;

public class SharedUtils {

	private static final Logger log = Logger.getLogger(SharedUtils.class);

	public static int realignToGrid(float val) {
		return realignTo(true, val, false, NewGridElementConstants.DEFAULT_GRID_SIZE);
	}

	public static int realignToGrid(boolean logRealign, float val) {
		return realignTo(logRealign, val, false, NewGridElementConstants.DEFAULT_GRID_SIZE);
	}

	/**
	 * returns the integer which is nearest to val but on the grid (round down)
	 * 
	 * @param logRealign
	 *            if true a realign is logged as an error
	 * @param val
	 *            value which should be rounded to the grid
	 * @param roundUp
	 *            if true the realign rounds up instead of down
	 * @return value on the grid
	 */
	public static int realignTo(boolean logRealign, double val, boolean roundUp, int gridSize) {
		double alignedVal = val;
		double mod = val % gridSize;
		if (mod != 0) {
			alignedVal -= mod; //ExampleA: 14 - 4 = 10 // ExampleB: -14 - -4 = -10 // (positive vals get round down, negative vals get round up)
			if (val > 0 && roundUp) { //eg ExampleA: 10 + 10 = 20 (for positive vals roundUp must be specifically handled by adding gridSize)
				alignedVal += gridSize;
			}
			if (val < 0 && !roundUp) { //eg ExampleB: -10 - 10 = -20 (for negative vals roundDown must be specifically handled by subtracting gridSize)
				alignedVal -= gridSize;
			}
			if (logRealign) log.error("realignToGrid from " + val + " to " + alignedVal);
		}
		return (int) alignedVal;
	}

}
