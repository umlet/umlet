package com.baselet.element.sequence_aio.facet;

public interface Container {

	int getStartTick();

	int getEndTick();

	Lifeline getFirstLifeline();

	Lifeline getLastLifeline();
}
