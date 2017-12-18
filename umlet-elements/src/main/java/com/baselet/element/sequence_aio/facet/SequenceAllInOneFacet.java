package com.baselet.element.sequence_aio.facet;

import java.util.Arrays;
import java.util.List;

import com.baselet.control.SharedUtils;
import com.baselet.control.basics.geom.DimensionDouble;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.element.facet.Facet;
import com.baselet.element.facet.PropertiesParserState;
import com.baselet.element.sequence_aio.facet.SequenceDiagram.DoubleConverter;
import com.baselet.element.sequence_aio.facet.specific.gen.ParseException;
import com.baselet.element.sequence_aio.facet.specific.gen.SequenceAllInOneParser;
import com.baselet.element.sequence_aio.facet.specific.gen.TokenMgrException;
import com.baselet.gui.AutocompletionText;

public class SequenceAllInOneFacet extends Facet {

	public static final SequenceAllInOneFacet INSTANCE = new SequenceAllInOneFacet();

	private SequenceAllInOneFacet() {}

	@Override
	public boolean checkStart(String line, PropertiesParserState state) {
		// consume every line that wasn't handled by another facet
		return true;
	}

	@Override
	public List<AutocompletionText> getAutocompletionStrings() {
		return Arrays.asList(new AutocompletionText[] {
				new AutocompletionText("title=", "Title of the sequence diagram"),
				new AutocompletionText("desc=", "Additional text, e.g. for local variables"),
				new AutocompletionText("autoTick=true", "(Default) Each line with an command is placed at a new time tick."),
				new AutocompletionText("autoTick=false", "Advancements down the time have to specified manually with the tick= command."),
				new AutocompletionText("overrideIds=true", "Each lifeline needs a explicit id."),
				new AutocompletionText("overrideIds=false", "(Default) each lifeline has a default id idX. X is a number starting at 1 and is increased from left to right."),
				new AutocompletionText("obj=New lifeline~", "Declares a new Lifeline"),

				new AutocompletionText("tick=", "Advances down the timeline"),
				new AutocompletionText("on=", "Starts a new execution specification on the given lifelines."),
				new AutocompletionText("off=", "Ends a existing execution specification on the given lifelines."),
				new AutocompletionText("ref=", ""),
				new AutocompletionText("continuation=", ""),
				new AutocompletionText("invariant=", ""),
				new AutocompletionText("stateInvariant=", ""),
				new AutocompletionText("coregionStart=", ""),
				new AutocompletionText("coregionEnd=", ""),
				new AutocompletionText("combinedFragment=", ""),
				new AutocompletionText("constraint=", "Specifies a constrain for an operand."),
				new AutocompletionText("destroy=", "Destroys a lifeline"),

				new AutocompletionText("lost", "Pseudo id for lost messages."),
				new AutocompletionText("found", "Pseudo id for found messages."),
				new AutocompletionText("gate", "Pseudo id for gate messages."),

				new AutocompletionText("ACTIVE", "Creates an active class as head for the lifeline."),
				new AutocompletionText("ACTOR", "The Lifeline head is an actor."),
				new AutocompletionText("CREATED_LATER", "The lifeline is created later with the first received message."),
				new AutocompletionText("EXECUTION", "The Lifeline has an active execution specification at the start.")
		});
	}

	@Override
	public void handleLine(String line, PropertiesParserState state) {
		// everything is done in parsingFinished
	}

	@Override
	public void parsingFinished(PropertiesParserState state, List<String> handledLines) {
		final DrawHandler drawer = state.getDrawer();
		// pass the whole text to the parser
		StringBuilder strBuilder = new StringBuilder();
		for (String str : handledLines) {
			strBuilder.append(str);
			strBuilder.append('\n');
		}
		try {
			DoubleConverter gridConverter = new DoubleConverter() {
				@Override
				public double convert(double value) {
					return SharedUtils.realignToGrid(false, value, true);
				}
			};
			DimensionDouble size = new SequenceAllInOneParser(strBuilder.toString()).start().generateDiagram()
					.draw(drawer, gridConverter, gridConverter);
			state.updateMinimumSize(size.getWidth(), size.getHeight());
		} catch (ParseException e) {
			throw new SequenceDiagramException(e);
		} catch (TokenMgrException e) {
			throw new SequenceDiagramException(e);
		}
	}
}
