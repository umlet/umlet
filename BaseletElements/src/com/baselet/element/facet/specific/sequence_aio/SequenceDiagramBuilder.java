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

	private final String DEFAULT_ID_PREFIX = "id";
	private final SequenceDiagram dia;
	private final Map<Lifeline, LifelineState> currentLifelineState;

	/** if true no default ids are generated and every lifeline needs an id */
	private boolean overrideDefaultIds;

	private final Map<String, Lifeline> ids;

	/** ids on this lifeline e.g. ids for receiving and sending messages, value is the tick */
	// TODO private final Map<Lifeline,Map<String, Integer>> llLocalIds;

	/** stores all warnings which are found while the sequence diagram is built */
	private final List<String> warnings;

	private int currentTick;
	private int lastMessageReceiveTick;
	private boolean diagramRetrieved;

	public SequenceDiagramBuilder()
	{
		overrideDefaultIds = false;
		ids = new HashMap<String, Lifeline>();
		dia = new SequenceDiagram();
		currentLifelineState = new HashMap<Lifeline, SequenceDiagramBuilder.LifelineState>();
		warnings = new LinkedList<String>();
		currentTick = 0;
		lastMessageReceiveTick = 0;
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
	 * @param id of the lifeline which should be returned
	 * @return the lifeline or null if no lifeline is associated with the given id
	 */
	public Lifeline getLifeline(String id) {
		return ids.get(id);
	}

	/**
	 * @param id of the lifeline which should be returned
	 * @return the lifeline
	 * @throws SequenceDiagramException if no lifeline is associated with the given id
	 */
	Lifeline getLifelineException(String id) {
		if (!ids.containsKey(id)) {
			throw new SequenceDiagramException("No lifeline is associated with the id: '" + id + "'");
		}
		return ids.get(id);
	}

	/**
	 *
	 * @param name
	 * @param id can be NULL, if none of reorder and idoverride option is active
	 * @param createdOnStart if false the lifeline will be created by the first message sent to this lifeline
	 */
	public void addLiveline(String headText, String id, Lifeline.LifelineHeadType headType, boolean createdOnStart) {
		checkState();

		if (isOverrideDefaultIds() && id == null) {
			throw new SequenceDiagramException("If the override option is set to true then every lifeline needs an id!");
		}
		Lifeline newLifeline = dia.addLiveline(headText, headType, createdOnStart);
		if (id != null) {
			if (ids.containsKey(id)) {
				throw new SequenceDiagramException("There is already a lifeline which is associated with the id '" + id +
													"', please choose another identifier.");
			}
			ids.put(id, newLifeline);
		}
		if (!overrideDefaultIds) {
			if (ids.containsKey(DEFAULT_ID_PREFIX + dia.getLifelineCount())) {
				throw new SequenceDiagramException("There is already a lifeline which is associated with the default id '" +
													DEFAULT_ID_PREFIX + dia.getLifelineCount() +
													"',\nplease choose another identifier or add the option 'overrideIds=true'.");
			}
			ids.put(DEFAULT_ID_PREFIX + dia.getLifelineCount(), newLifeline);
		}
		currentLifelineState.put(newLifeline, new LifelineState());
	}

	/**
	 *
	 * @param id of the lifeline
	 * @param event e.g. message, invariant, activity
	 */
	private void addLifelineOccurrence(String id, LifelineOccurrence occurrence) {
		checkState();
		getLifelineException(id).addLifelineOccurrenceAtTick(occurrence, currentTick);
	}

	public void addStateInvariant(String lifelineId, String text, boolean drawAsState) {
		addLifelineOccurrence(lifelineId, new StateInvariant(text, drawAsState ? StateInvariantStyle.STATE : StateInvariantStyle.CURLY_BRACKETS));
	}

	public void addCoregion(String id, boolean start) {
		checkState();
		// check that every coregion should be closed and coregion should not overlap, but only add a warning and trust the user
		Lifeline lifeline = getLifelineException(id);
		LifelineState lifelineState = currentLifelineState.get(lifeline);
		if (lifelineState.coregionActive && start) {
			addWarning(id, "A new coregion was started while an old one was still active.");
		}
		else if (!lifelineState.coregionActive && !start) {
			addWarning(id, "A coregion was closed, but no coregion was active.");
		}
		lifeline.addLifelineOccurrenceAtTick(new Coregion(lifeline, currentTick, start), currentTick);
		lifelineState.coregionActive = start;
	}

	public void changeExecutionSpecification(String lifelineId, boolean on) {
		checkState();
		// check that they don't collide i.e. 2 changes at the same tick
		Lifeline lifeline = getLifelineException(lifelineId);
		LifelineState lifelineState = currentLifelineState.get(lifeline);
		if (on) {
			if (lifelineState.execSpecStartTickStack.size() > 0 && lifelineState.execSpecStartTickStack.peek() == currentTick) {
				throw new SequenceDiagramException("On lifeline " + lifelineId + " two executionspecifications start at the same tick, this is not possible.");
			}
			if (lifelineState.lastEndOfExecSpec == currentTick) {
				throw new SequenceDiagramException("On lifeline " + lifelineId + " two executionspecifications overlap, this is not possible.");
			}
			lifelineState.execSpecStartTickStack.addFirst(currentTick);
		}
		else {
			if (lifelineState.execSpecStartTickStack.size() == 0) {
				throw new SequenceDiagramException("On lifeline " + lifelineId + " a executionspecification was closed but no active executionspecification exists.");
			}
			int startTick = lifelineState.execSpecStartTickStack.poll();
			if (startTick == currentTick) {
				throw new SequenceDiagramException("On lifeline " + lifelineId + " a executionspecification was closed too soon, every executionspecification needs to be at least 1 tick long.");
			}
			if (lifelineState.lastEndOfExecSpec == currentTick) {
				throw new SequenceDiagramException("On lifeline " + lifelineId + " two executionspecifications end at the same tick, this is not possible.");
			}
			lifelineState.lastEndOfExecSpec = currentTick;
			lifeline.addExecutionSpecification(new ExecutionSpecification(startTick, currentTick));
		}
	}

	/**
	 * @param fromId
	 * @param toId
	 * @param duration
	 * @param text
	 * @param lineType
	 * @param arrowType
	 */
	public void addMessage(String fromId, String toId, int duration, String text, LineType lineType, Message.ArrowType arrowType) {
		checkState();
		// check that duration >= 0 and check that self message has a duration > 0
		if (duration < 0) {
			throw new SequenceDiagramException("The duration must be positive, but was " + duration + ".");
		}
		else if (fromId.equals(toId)) {
			if (duration < 1) {
				throw new SequenceDiagramException("The duration of a self message must be greater than 0, but was " + duration + ".");
			}
		}
		lastMessageReceiveTick = Math.max(lastMessageReceiveTick, currentTick + duration);
		Lifeline from = getLifelineException(fromId);
		checkLifelineSendMessage(from, fromId);
		Lifeline to = getLifelineException(toId);
		if (!to.isCreatedOnStart() && to.getCreated() == null) {
			to.setCreated(currentTick + duration);
		}
		Message msg = new Message(from, to, duration, currentTick, text, arrowType, lineType);
		dia.addLifelineSpanningTickSpanningOccurrence(msg);
	}

	public void addLostMessage(String fromId, String text, LineType lineType, Message.ArrowType arrowType) {
		checkState();
		Lifeline from = getLifelineException(fromId);
		checkLifelineSendMessage(from, fromId);
		// TODO error handling
		// add as ll occurrence
		// addLifelineOccurrence(fromId, new LostMessage());
	}

	public void addFoundMessage(String toId, String text, LineType lineType, Message.ArrowType arrowType) {
		checkState();
		Lifeline to = getLifelineException(toId);
		if (!to.isCreatedOnStart()) {
			if (to.getCreated() == null || to.getCreated() >= currentTick) {
				throw new SequenceDiagramException("The lifeline " + toId + " was not yet created, therefore it is not possible to send a found message to it.");
			}
		}
		// TODO error handling
		// add as ll occurrence
		// addLifelineOccurrence(toId, new FoundMessage())
	}

	private void checkLifelineSendMessage(Lifeline from, String id) {
		if (!from.isCreatedOnStart()) {
			if (from.getCreated() == null || from.getCreated() >= currentTick) {
				throw new SequenceDiagramException("The lifeline " + id + " was not yet created, therefore it is not possible to send a message from it.");
			}
		}
	}

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
			this.overrideDefaultIds = overrideDefaultIds;
		}
	}

	public boolean isOverrideDefaultIds() {
		return overrideDefaultIds;
	}

	/**
	 * Should be called after the diagram is generated, because this last step can add further warnings.
	 * @return the empty string if no warnings exists, otherwise a headline and the warnings (with \n as line delimiter) are returned
	 */
	public String getWarnings() {
		if (warnings.size() == 0) {
			return "";
		}
		else {
			StringBuffer strBuffer = new StringBuffer();
			strBuffer.append("Warnings:");
			for (String w : warnings) {
				strBuffer.append('\n');
				strBuffer.append(w);
			}
			return strBuffer.toString();
		}
	}

	/**
	 * Called as final step.
	 * Can be called multiple times, but every time the same diagram (same reference) is returned.
	 * If there exists a major issue with the diagram an exception is thrown, otherwise the diagram is returned
	 * and if there are minor issues these are added to the warnings.
	 *
	 * @return the generated diagram
	 * @throws SequenceDiagramException if the diagram could not be generated.
	 * @see #getWarnings()
	 */
	public SequenceDiagram generateDiagram()
	{
		if (diagramRetrieved) {
			return dia;
		}
		else {
			// if a created later lifeline has no message it is draw at the start and a warning is added
			boolean createdLaterWarning = false;
			for (Lifeline ll : dia.getLifelines()) {
				if (!ll.isCreatedOnStart() && ll.getCreated() == null) {
					createdLaterWarning = true;
					ll.setCreatedOnStart(true);
				}
			}
			if (createdLaterWarning) {
				warnings.add("At least one lifeline was specified as created later, but didn't receive a message");
			}

			// TODO for each operand condition add a LL occurence to the lifeline with the first message (or to the lowest index)

			// close all execution specifications and add a warning.
			boolean openExecSpecs;
			boolean foundOpenExecSpecs = false;
			do {
				openExecSpecs = false;
				for (Map.Entry<Lifeline, LifelineState> e : currentLifelineState.entrySet()) {
					if (e.getValue().execSpecStartTickStack.size() > 0) {
						if (e.getValue().lastEndOfExecSpec < currentTick) {
							e.getValue().lastEndOfExecSpec = currentTick;
							e.getKey().addExecutionSpecification(
									new ExecutionSpecification(e.getValue().execSpecStartTickStack.pop(), currentTick));
							openExecSpecs = openExecSpecs || e.getValue().execSpecStartTickStack.size() > 0;
							foundOpenExecSpecs = true;
						}
					}
				}
				if (openExecSpecs) {
					currentTick++;
				}
			} while (openExecSpecs);
			if (foundOpenExecSpecs) {
				warnings.add("At least one executionspecification was not closed, any open executionspecification was closed at the end of the diagram.");
			}
			dia.setLastTick(Math.max(currentTick, lastMessageReceiveTick));
			diagramRetrieved = true;
			return dia;
		}
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
		warnings.add("On lifeline '" + id + "': " + text);
	}

	private class LifelineState {
		/** stores the last end of an execution specifications. */
		int lastEndOfExecSpec = -1; // Starts at -1 since the first tick is 0 and therefore no overlap is possible
		LinkedList<Integer> execSpecStartTickStack = new LinkedList<Integer>();
		boolean coregionActive = false;
	}
}
