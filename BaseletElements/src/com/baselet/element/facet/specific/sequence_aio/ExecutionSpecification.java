package com.baselet.element.facet.specific.sequence_aio;

public class ExecutionSpecification {

	private int startTick;
	/** the last tick on which it is active */
	private int endTick;

	public ExecutionSpecification() {}

	public int getStartTick() {
		return startTick;
	}

	public void setStartTick(int start) {
		startTick = start;
	}

	public int getEndTick() {
		return endTick;
	}

	public void setEndTick(int end) {
		endTick = end;
	}

	public boolean enclosesTick(int tick)
	{
		return startTick <= tick && tick <= endTick;
	}
}
