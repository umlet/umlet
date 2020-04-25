package com.baselet.element.sequence_aio.facet;

public class ExecutionSpecification {

	private int startTick;
	/** the last tick on which it is active */
	private int endTick;

	public ExecutionSpecification(int startTick, int endTick) {
		super();
		this.startTick = startTick;
		this.endTick = endTick;
	}

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
