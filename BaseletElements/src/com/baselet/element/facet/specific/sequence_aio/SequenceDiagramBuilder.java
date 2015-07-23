package com.baselet.element.facet.specific.sequence_aio;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.baselet.control.enums.LineType;
import com.baselet.element.facet.specific.sequence_aio.StateInvariant.StateInvariantStyle;

/**
 * Constructs a sequence diagram.
 * No modifications are allowed after the diagram was returned.
 * Events which affect a lifeline must be added after the affected lifelines.
 *
 */
public class SequenceDiagramBuilder {

	private final SequenceDiagram dia;
	private final Map<Lifeline, LifelineState> currentLifelineState;

	/** stores all warnings which are found while the sequence diagram is built */
	private final List<String> warnings;

	private int currentTick;
	private boolean diagramRetrieved;

	public SequenceDiagramBuilder()
	{
		dia = new SequenceDiagram();
		currentLifelineState = new HashMap<Lifeline, SequenceDiagramBuilder.LifelineState>();
		warnings = new LinkedList<String>();
		currentTick = 0;
		diagramRetrieved = false;
	}

	public void setTitle(String title) {
		checkState();
		dia.setTitle(title);
	}

	public void setText(String text) {
		checkState();
		dia.setText(text);
	}

	/**
	 *
	 * @param name
	 * @param id can be NULL, if none of reorder and idoverride option is active
	 * @param createdOnStart if false the lifeline will be created by the first message sent to this lifeline
	 */
	public void addLiveline(String headText, String id, Lifeline.LifelineHeadType headType, boolean createdOnStart) {
		checkState();
		if (dia.isOverrideDefaultIds() && id == null) {
			throw new SequenceDiagramException("If the override option is set to true then every lifeline needs an id!");
		}
		else {
			Lifeline newLifeline = dia.addLiveline(headText, id, headType, createdOnStart);
			currentLifelineState.put(newLifeline, new LifelineState());
		}
	}

	/**
	 *
	 * @param id of the lifeline
	 * @param event e.g. message, invariant, activity
	 */
	private void addLifelineOccurrence(String id, LifelineOccurrence occurrence) {
		checkState();
		dia.getLifelineException(id).addLifelineOccurrenceAtTick(occurrence, currentTick);
	}

	public void addStateInvariant(String lifelineId, String text, boolean drawAsState) {
		addLifelineOccurrence(lifelineId, new StateInvariant(text, drawAsState ? StateInvariantStyle.STATE : StateInvariantStyle.CURLY_BRACKETS));
	}

	public void addCoregion(String id, boolean start) {
		checkState();
		// check that every coregion should be closed and coregion should not overlap
		Lifeline lifeline = dia.getLifelineException(id);
		LifelineState lifelineState = currentLifelineState.get(lifeline);
		if (lifelineState.coregionActive && start) {
			addWarning(id, "A new coregion was started while an old one was still active.");
		}
		else if (!lifelineState.coregionActive && !start) {
			addWarning(id, "A coregion was closed, but no coregion was active.");
		}
		else {
			lifeline.addLifelineOccurrenceAtTick(new Coregion(lifeline, currentTick, start), currentTick);
		}
		lifelineState.coregionActive = start;
	}

	public void changeExecutionSpecification(String lifelineId, boolean on) {
		checkState();

		Lifeline lifeline = dia.getLifelineException(lifelineId);
		LifelineState lifelineState = currentLifelineState.get(lifeline);
		if (!on && lifelineState.execSpecStartTickStack.size() == 0) {
			throw new SequenceDiagramException("On lifeline " + lifelineId + " a executionspecification was closed but no active executionspecification exists.");
		}
		if (on) {
			lifelineState.execSpecStartTickStack.addFirst(currentTick);
		}
		else {
			lifeline.addExecutionSpecification(new ExecutionSpecification(lifelineState.execSpecStartTickStack.poll(), currentTick));
		}
	}

	public void addMessage(String fromId, String toId, int duration, String text, LineType lineType, Message.ArrowType arrowType) {
		checkState();
		Message msg = new Message(dia.getLifelineException(fromId), dia.getLifelineException(toId), duration, currentTick, text, arrowType, lineType);
		dia.addLifelineSpanningTickSpanningOccurrence(msg);
	}

	// public void addLostMessage(String fromId, String text, LineType lineType, Message.ArrowType arrowType) {
	// checkState();
	// // TODO error handling
	// // add as ll occurrence
	// addLifelineOccurrence(fromId, new LostMessage());
	// }
	//
	// public void addFoundMessage(String toId, String text, LineType lineType, Message.ArrowType arrowType) {
	// checkState();
	// // TODO error handling
	// // add as ll occurrence
	// addLifelineOccurrence(toId, new FoundMessage())
	// }

	/**
	 * advances a step down
	 */
	public void tick() {
		tick(1);
	}

	/**
	 *
	 * @param tickCount needs to be &gt; 0
	 */
	public void tick(int tickCount) {
		checkState();
		if (tickCount < 1) {
			throw new IllegalArgumentException("The tickCount must be greater than 0.");
		}
		currentTick += tickCount;
	}

	public void setOverrideDefaultIds(boolean overrideDefaultIds) {
		checkState();
		if (dia.getLifelineCount() > 0) {
			throw new SequenceDiagramException("The override ids option must be specified before any lifeline is specified.");
		}
		else {
			dia.setOverrideDefaultIds(overrideDefaultIds);
		}
	}

	/**
	 * should be called after the last built step in the diagram to make a sanity check (i.e. message to all Lifelines which are created later, but could be problematic while writting the diagram!)
	 * @return null if everything is correct, otherwise return warnings as string
	 */
	public String checkIfDiagramIsComplete() {
		// TODO check that every coregion should be closed and coregion should not overlap
		return "Not implemented";
	}

	// TODO perform action that couldn't be done while building
	public SequenceDiagram generateDiagram()
	{
		// only throw an exception if it is not possible to draw the diagram, else add a warning
		// close all execution specifications and add warnings for all lifelines which had active ones.
		// TODO for each operand condition add a LL occurence to the lifeline with the first message (or to the lowest index)
		diagramRetrieved = true;

		return dia;
	}

	/**
	 * throws an exception if the diagram was already returned
	 */
	private void checkState() {
		if (diagramRetrieved) {
			throw new IllegalStateException("The final diagram was returned and therefore no more changes are allowed.");
		}
	}

	private void addWarning(String id, String text) {
		warnings.add("On lifeline " + id + ": " + text);
	}

	private class LifelineState {
		LinkedList<Integer> execSpecStartTickStack = new LinkedList<Integer>();
		boolean coregionActive = false;
	}
}
