package com.baselet.element.facet.specific.sequence_aio;

public interface Container {

	int getStartTick();

	int getEndTick();

	Lifeline getFirstLifeline();

	Lifeline getLastLifeline();
}
