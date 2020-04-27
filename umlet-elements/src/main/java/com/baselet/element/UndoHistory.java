package com.baselet.element;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UndoHistory {

	private final Logger log = LoggerFactory.getLogger(UndoHistory.class);

	private final List<UndoInformation> history = new ArrayList<UndoInformation>();
	private int currentIndex = -1;

	public void add(UndoInformation undoInformation) {
		while (history.size() > currentIndex + 1) {
			history.remove(history.size() - 1);
		}
		history.add(undoInformation);
		currentIndex++;
	}

	public UndoInformation remove() {
		UndoInformation undoInformation = history.remove(currentIndex);
		currentIndex = Math.min(currentIndex, history.size() - 1); // stay at current index except if it was pointing to the last element
		return undoInformation;
	}

	public UndoInformation get(boolean undo) {
		if (history.isEmpty()) {
			return null;
		}
		if (!undo) {
			currentIndex++;
		}
		UndoInformation undoInformation = history.get(currentIndex);
		log.trace("GET " + currentIndex + " = " + undoInformation.getDiffRectangle(10, undo) + "/size" + history.size());
		if (undo) {
			currentIndex--;
		}
		return undoInformation;
	}

}
