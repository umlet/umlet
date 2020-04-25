package com.baselet.element.sequence_aio.facet;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baselet.control.enums.LineType;
import com.baselet.element.sequence_aio.facet.StateInvariant.StateInvariantStyle;

/**
 * Constructs a sequence diagram.
 * No modifications are allowed after the diagram was returned.
 * Events which affect a lifeline must be added after the affected lifelines.
 *
 */
public class SequenceDiagramBuilder {

	private static final Logger log = LoggerFactory.getLogger(SequenceDiagramBuilder.class);

	private final String DEFAULT_ID_PREFIX = "id";
	private final SequenceDiagram dia;
	private final Map<Lifeline, LifelineState> currentLifelineState;

	/** if true no default ids are generated and every lifeline needs an id */
	private boolean overrideDefaultIds;

	private final Map<String, Lifeline> ids;
	private final LinkedList<ActiveCombinedFragment> activeCombinedFragmentStack;
	/** ids on a lifeline for occurrenceSpecifications (message endpoint or execution specification start/end) */
	private final Map<Lifeline, Map<String, OccurrenceSpecification>> lifelineLocalIds;
	/** stores all warnings which are found while the sequence diagram is built */
	private final List<String> warnings;

	private int currentTick;
	private int lastMessageReceiveTick;
	private boolean diagramRetrieved;

	public SequenceDiagramBuilder() {
		overrideDefaultIds = false;
		ids = new HashMap<String, Lifeline>();
		activeCombinedFragmentStack = new LinkedList<SequenceDiagramBuilder.ActiveCombinedFragment>();
		lifelineLocalIds = new HashMap<Lifeline, Map<String, OccurrenceSpecification>>();
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

	public Lifeline[] getLifelineInterval(String id1, String id2) {
		try {
			return getLifelineIntervalException(id1, id2);
		} catch (SequenceDiagramException e) {
			return new Lifeline[0];
		}
	}

	public Lifeline[] getLifelineIntervalException(String id1, String id2) {
		Lifeline ll1 = getLifelineException(id1);
		Lifeline ll2 = getLifelineException(id2);
		if (ll1.getIndex() > ll2.getIndex()) {
			Lifeline tmp = ll1;
			ll1 = ll2;
			ll2 = tmp;
		}
		return Arrays.copyOfRange(dia.getLifelinesArray(), ll1.getIndex(), ll2.getIndex() + 1);
	}

	/**
	 *
	 * @param headText the text which is drawn in the head
	 * @param id can be NULL, if none of reorder and idoverride option is active
	 * @param headType the lifeline style
	 * @param createdOnStart if false the lifeline will be created by the first message sent to this lifeline
	 */
	public void addLiveline(String headText, String id, Lifeline.LifelineHeadType headType, boolean createdOnStart,
			boolean execSpecFromStart) {
		checkState();

		if (isOverrideDefaultIds() && id == null) {
			throw new SequenceDiagramException("If the override option is set to true then every lifeline needs an id!");
		}
		Lifeline newLifeline = dia.addLiveline(headText, headType, createdOnStart, execSpecFromStart);
		if (id != null) {
			if (ids.containsKey(id)) {
				throw new SequenceDiagramException("There is already a lifeline which is associated with the id '" +id +
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
		lifelineLocalIds.put(newLifeline, new HashMap<String, OccurrenceSpecification>());
		LifelineState newLifelineState = new LifelineState();
		if (execSpecFromStart && createdOnStart) {
			newLifelineState.execSpecStartTickStack.addFirst(-1);
		}
		currentLifelineState.put(newLifeline, newLifelineState);
	}

	/**
	 *
	 * @param id of the lifeline
	 * @param event e.g. message, invariant, activity
	 */
	private void addLifelineOccurrence(String id, LifelineOccurrence occurrence) {
		checkState();
		try {
			getLifelineException(id).addLifelineOccurrenceAtTick(occurrence, currentTick);
		} catch (SequenceDiagramCheckedException ex) {
			throw new SequenceDiagramException("Error on lifeline '" + id + "': " + ex.getMessage(), ex);
		}
	}

	public void addTextOnLifeline(String lifelineId, String text) {
		addLifelineOccurrence(lifelineId, new TextOnLifeline(text));
	}

	public void addStateInvariant(String lifelineId, String text, boolean drawAsState) {
		addLifelineOccurrence(lifelineId, new StateInvariant(text,
				drawAsState ? StateInvariantStyle.STATE : StateInvariantStyle.CURLY_BRACKETS));
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
		try {
			lifeline.addLifelineOccurrenceAtTick(new Coregion(lifeline, currentTick, start), currentTick);
			lifelineState.coregionActive = start;
		} catch (SequenceDiagramCheckedException ex) {
			throw new SequenceDiagramException("Error on lifeline '" + id + "': " + ex.getMessage(), ex);
		}
	}

	public void destroyLifeline(String id) {
		checkState();
		// check that every coregion should be closed and coregion should not overlap, but only add a warning and trust the user
		Lifeline lifeline = getLifelineException(id);
		if (lifeline.getDestroyed() != null) {
			addWarning(id, "The lifeline was already destroyed.");
		}
		else {
			lifeline.setDestroyed(currentTick);
		}
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
			if (!lifeline.isCreatedOnStart() && (lifeline.getCreated() == null || lifeline.getCreated() >= currentTick)) {
				throw new SequenceDiagramException("Error on lifeline '" + lifelineId + "': the lifeline can not contain executionspecifications before it is created.");
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
	 * @param fromLocalId can be null, if not null the send end point will be associated with the given id
	 * @param toLocalId can be null, if not null the receive end point will be associated with the given id
	 */
	public void addMessage(String fromId, String toId, int duration, String text, LineType lineType,
			Message.ArrowType arrowType, String fromLocalId, String toLocalId) {
		checkState();
		// check that a self message has a duration > 0
		if (fromId.equals(toId)) {
			if (duration < 1) {
				throw new SequenceDiagramException("The duration of a self message must be greater than 0, but was " + duration + ".");
			}
		}
		lastMessageReceiveTick = Math.max(lastMessageReceiveTick, currentTick + duration);
		Lifeline from = getLifelineException(fromId);
		checkLifelineSendMessage(from, fromId);
		Lifeline to = getLifelineException(toId);
		// check if the message doesn't end before the lifeline was created
		if (!to.isCreatedOnStart() && to.getCreated() != null && currentTick + duration <= to.getCreated()) {
			throw new SequenceDiagramException("A message can't end on a lifeline before this lifeline was created.\n" +
												"Please increase the messages duration by at least " + (to.getCreated() + 1 - (currentTick + duration)) + ".");
		}
		else if (currentTick + duration < 0) {
			throw new SequenceDiagramException("A message can't end on a lifeline before this lifeline was created.\n" +
												"Please increase the messages duration by at least " + -(currentTick + duration) + ".");
		}
		if (!to.isCreatedOnStart() && to.getCreated() == null) {
			to.setCreated(currentTick + duration);
			if (to.isExecSpecFromStart()) {
				currentLifelineState.get(to).execSpecStartTickStack.push(currentTick + duration);
			}
		}
		Message msg = new Message(from, to, duration, currentTick, text, arrowType, lineType);
		if (fromLocalId != null) {
			addOccurrenceSpecification(from, fromId, fromLocalId, msg.sendOccurrenceSpecification());
		}
		if (toLocalId != null) {
			addOccurrenceSpecification(to, toId, toLocalId, msg.receiveOccurrenceSpecification());
		}
		dia.addLifelineSpanningTickSpanningOccurrence(msg);
	}

	public void addLostMessage(String fromId, String text, LineType lineType, Message.ArrowType arrowType, String fromLocalId) {
		checkState();
		Lifeline from = getLifelineException(fromId);
		checkLifelineSendMessage(from, fromId);
		LostOrFoundMessage msg = new LostOrFoundMessage(from, false, currentTick, text, arrowType, lineType);
		if (fromLocalId != null) {
			addOccurrenceSpecification(from, fromId, fromLocalId, msg.sendOccurrenceSpecification());
		}
		addLifelineOccurrence(fromId, msg);
	}

	public void addFoundMessage(String toId, String text, LineType lineType, Message.ArrowType arrowType, String toLocalId) {
		checkState();
		Lifeline to = getLifelineException(toId);
		if (!to.isCreatedOnStart()) {
			if (to.getCreated() == null || to.getCreated() >= currentTick) {
				throw new SequenceDiagramException("The lifeline " +toId +
													" was not yet created, therefore it is not possible to send a found message to it.");
			}
		}
		LostOrFoundMessage msg = new LostOrFoundMessage(to, true, currentTick, text, arrowType, lineType);
		if (toLocalId != null) {
			addOccurrenceSpecification(to, toId, toLocalId, msg.receiveOccurrenceSpecification());
		}

		addLifelineOccurrence(toId, msg);
	}

	public void addReceiveGateMessage(String fromId, String text, LineType lineType, Message.ArrowType arrowType, String fromLocalId) {
		checkState();
		Lifeline from = getLifelineException(fromId);
		checkLifelineSendMessage(from, fromId);
		GateMessage msg = GateMessage.createReceiveGateMessage(from, currentTick, text, arrowType, lineType,
				dia.getLifelinesArray()[0], dia.getLifelinesArray()[dia.getLifelinesArray().length - 1]);
		if (fromLocalId != null) {
			addOccurrenceSpecification(from, fromId, fromLocalId, msg.sendOccurrenceSpecification());
		}
		dia.addLifelineSpanningTickSpanningOccurrence(msg);
	}

	public void addSendGateMessage(String toId, String text, LineType lineType, Message.ArrowType arrowType, String toLocalId) {
		checkState();
		Lifeline to = getLifelineException(toId);
		GateMessage msg = GateMessage.createSendGateMessage(to, currentTick, text, arrowType, lineType,
				dia.getLifelinesArray()[0], dia.getLifelinesArray()[dia.getLifelinesArray().length - 1]);
		if (toLocalId != null) {
			addOccurrenceSpecification(to, toId, toLocalId, msg.receiveOccurrenceSpecification());
		}
		if (!to.isCreatedOnStart() && to.getCreated() == null) {
			to.setCreated(currentTick);
		}
		dia.addLifelineSpanningTickSpanningOccurrence(msg);
	}

	private void checkLifelineSendMessage(Lifeline from, String id) {
		if (!from.isCreatedOnStart()) {
			if (from.getCreated() == null || from.getCreated() >= currentTick) {
				throw new SequenceDiagramException("The lifeline " + id + " was not yet created, therefore it is not possible to send a message from it.");
			}
		}
	}

	public void addGeneralOrdering(String earlierLifelineId, String earlierLifelineLocalId,
			String laterLifelineId, String laterLifelineLocalId) {
		checkState();
		dia.addLifelineSpanningTickSpanningOccurrence(new GeneralOrdering(
				getLifelineOccurrenceSpecException(earlierLifelineId, earlierLifelineLocalId),
				getLifelineOccurrenceSpecException(laterLifelineId, laterLifelineLocalId),
				getLifelineIntervalException(earlierLifelineId, laterLifelineId)));
	}

	// private void addOccurrenceSpecification(String lifelineId, String localId, OccurrenceSpecification occurrence) {
	// addOccurrenceSpecification(getLifelineException(lifelineId), lifelineId, localId, occurrence);
	// }

	private void addOccurrenceSpecification(Lifeline lifeline, String lifelineId, String localId, OccurrenceSpecification occurrence) {
		if (lifelineLocalIds.get(lifeline).containsKey(localId)) {
			throw new SequenceDiagramException("The lifeline '" + lifelineId + "' has already a local id '" + localId + "', please choose another id.");
		}
		lifelineLocalIds.get(lifeline).put(localId, occurrence);
	}

	private OccurrenceSpecification getLifelineOccurrenceSpecException(String lifelineId, String localId) {
		Lifeline llifeline = getLifelineException(lifelineId);
		OccurrenceSpecification occurrenceSpec = lifelineLocalIds.get(llifeline).get(localId);
		if (occurrenceSpec == null) {
			throw new SequenceDiagramException("No lifeline occurrence with the id '" + localId + "' could be found on lifeline '" + lifelineId + "'.");
		}
		return occurrenceSpec;
	}

	public void addInteractionUse(String startId, String endId, String text) {
		checkState();
		Lifeline[] lifelines = getLifelineIntervalException(startId, endId);
		dia.addLifelineSpanningTickSpanningOccurrence(new InteractionUse(currentTick, text, lifelines));
	}

	public void addContinuation(String startId, String endId, String text) {
		checkState();
		Lifeline[] lifelines = getLifelineIntervalException(startId, endId);
		dia.addLifelineSpanningTickSpanningOccurrence(new Continuation(currentTick, text, lifelines));
	}

	/**
	 * @param startId starting lifeline
	 * @param endId ending lifeline
	 * @param cfId the id of the combined Fragment, can be null
	 * @param operator of the combined fragment
	 */
	public void beginCombinedFragment(String startId, String endId, String cfId, String operator) {
		checkState();
		Lifeline[] lifelines;
		if (startId == null && endId == null) {
			lifelines = Arrays.<Lifeline> copyOf(dia.getLifelinesArray(), dia.getLifelinesArray().length);
		}
		else {
			lifelines = getLifelineIntervalException(startId, endId);
		}
		activeCombinedFragmentStack.push(new ActiveCombinedFragment(new CombinedFragment(lifelines, currentTick, operator), cfId));
		activeCombinedFragmentStack.peek().activeOperand = new ActiveOperand(currentTick);
	}

	/**
	 * @param cfId can be null, then the latest created combined fragment which was not closed is closed.
	 * Otherwise the combined fragment associated with the given id is closed.
	 */
	public void endCombinedFragment(String cfId) {
		checkState();
		if (activeCombinedFragmentStack.size() == 0) {
			throw new SequenceDiagramException("Error a combined fragment was closed, but no open one exists.");
		}
		ActiveCombinedFragment currentCombFrag = null;
		if (cfId == null) {
			currentCombFrag = activeCombinedFragmentStack.pop();
		}
		else {
			ListIterator<ActiveCombinedFragment> activeCFIter = activeCombinedFragmentStack.listIterator();
			while (activeCFIter.hasNext()) {
				currentCombFrag = activeCFIter.next();
				if (cfId.equals(currentCombFrag.id)) {
					activeCFIter.remove();
					break;
				}
				currentCombFrag = null;
			}
		}
		if (currentCombFrag == null) {
			throw new SequenceDiagramException("Error no combined fragment with id '" + cfId + "' was found.");
		}
		else {
			// TODO operand rewrite
			// if (currentCombFrag.activeOperand != null) {
			// throw new SequenceDiagramException("Error a combined fragment was closed, but there is still an open operand present.");
			// }
			currentCombFrag.combFrag.addOperand(currentCombFrag.activeOperand.startTick, currentTick);
			dia.addLifelineSpanningTickSpanningOccurrence(currentCombFrag.combFrag);
		}
	}

	public void endAndBeginOperand(String cfId) {
		checkState();
		if (activeCombinedFragmentStack.size() == 0) {
			throw new SequenceDiagramException("Error a combined fragment was closed, but no open one exists.");
		}
		ActiveCombinedFragment currentCombFrag = null;
		if (cfId == null) {
			currentCombFrag = activeCombinedFragmentStack.peek();
		}
		else {
			ListIterator<ActiveCombinedFragment> activeCFIter = activeCombinedFragmentStack.listIterator();
			while (activeCFIter.hasNext()) {
				currentCombFrag = activeCFIter.next();
				if (cfId.equals(currentCombFrag.id)) {
					break;
				}
				currentCombFrag = null;
			}
		}
		if (currentCombFrag == null) {
			throw new SequenceDiagramException("Error no combined fragment with id '" + cfId + "' was found.");
		}
		else {
			// TODO operand rewrite
			currentCombFrag.combFrag.addOperand(currentCombFrag.activeOperand.startTick, currentTick);
			currentCombFrag.activeOperand = new ActiveOperand(currentTick);
		}
	}

	// public void beginOperand() {
	// checkState();
	// beginOperand(null, "");
	// }

	/**
	 *
	 * @param text if empty or null no constraint will be generated
	 * @param lifelineId if empty or null the first occurrence in the operand will determine the lifeline on which the constraint is placed
	 */
	// public void beginOperand(String text, String lifelineId) {
	// checkState();
	// if (activeCombinedFragmentStack.size() == 0) {
	// throw new SequenceDiagramException("Error an operand must lie in a combined fragment, but no open one exists.");
	// }
	// ActiveCombinedFragment currentCombFrag = activeCombinedFragmentStack.peek();
	// if (currentCombFrag.activeOperand != null) {
	// throw new SequenceDiagramException("Error a new operand was started, but there is still an open operand present.");
	// }
	// ActiveOperand operand;
	// if (text == null || text.isEmpty()) {
	// operand = new ActiveOperand(currentTick);
	// }
	// else {
	// operand = new ActiveOperand(text, getLifeline(lifelineId), currentTick);
	// }
	// currentCombFrag.activeOperand = operand;
	// }

	// public void endOperand() {
	// checkState();
	// if (activeCombinedFragmentStack.size() == 0) {
	// throw new SequenceDiagramException("Error an operand must lie in a combined fragment, but no open one exists.");
	// }
	// ActiveCombinedFragment currentCombFrag = activeCombinedFragmentStack.peek();
	// if (currentCombFrag.activeOperand == null) {
	// throw new SequenceDiagramException("Error a operand was closed, but there is no open operand.");
	// }
	// ActiveOperand operand = currentCombFrag.activeOperand;
	// currentCombFrag.activeOperand = null;
	// if (operand.constraint == null) {
	// currentCombFrag.combFrag.addOperand(operand.startTick, currentTick);
	// }
	// else {
	// Lifeline lifeline = operand.assoicatedLifeline;
	// if (lifeline == null) { // if it wasn't set place it on the first lifeline of the combined fragment
	// lifeline = currentCombFrag.combFrag.getFirstLifeline();
	// }
	// try {
	// currentCombFrag.combFrag.addOperand(operand.startTick, currentTick, operand.constraint, lifeline);
	// } catch (SequenceDiagramCheckedException e) {
	// throw new SequenceDiagramException("Error while placing the interaction '" + operand.constraint
	// + "' constraint on the " + (lifeline.getIndex() + 1)
	// + " lifeline from the left.\n" + e.getMessage(), e);
	// }
	// }
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
	public SequenceDiagram generateDiagram() {
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

	private static class LifelineState {
		/** stores the last end of an execution specifications. */
		int lastEndOfExecSpec = -1; // Starts at -1 since the first tick is 0 and therefore no overlap is possible
		LinkedList<Integer> execSpecStartTickStack = new LinkedList<Integer>();
		boolean coregionActive = false;
	}

	private static class ActiveCombinedFragment {
		public ActiveCombinedFragment(CombinedFragment combFrag, String cfId) {
			super();
			this.combFrag = combFrag;
			id = cfId;
		}

		String id;
		CombinedFragment combFrag;
		ActiveOperand activeOperand = null;
	}

	private static class ActiveOperand {
		public ActiveOperand(int startTick) {
			super();
			this.startTick = startTick;
		}

		// public ActiveOperand(String constraint, Lifeline assoicatedLifeline, int startTick) {
		// super();
		// this.constraint = constraint;
		// this.assoicatedLifeline = assoicatedLifeline;
		// this.startTick = startTick;
		// }
		//
		// String constraint = null; // if empty no constraint needed
		// Lifeline assoicatedLifeline = null; // lifeline on which the constraint should be drawn
		int startTick;
	}

}
