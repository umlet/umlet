package com.umlet.element.experimental.element.uml.relation.facet;

import java.util.Arrays;
import java.util.List;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.gui.AutocompletionText;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.element.uml.relation.RelationPoints;

public class ArrowNormal extends Arrow {

	private static final String NORMAL = ">";
	private static final String INVERSE = "<";
	private static final String CLOSED = ">>";
	private static final String INV_CLOSED = "<<";
	private List<String> endings = Arrays.asList(NORMAL, INVERSE, CLOSED, INV_CLOSED);
	
	private static final String IMG_CLOSED = "R0lGODlhPgALANUAAAAAAP////f39+/v7+vr6+fn59/f39vb29fX19PT08vLy8PDw7e3t6urq6enp6Ojo5+fn5ubm4eHh4CAgHh4eGxsbGhoaGBgYFhYWFRUVExMTEhISEBAQDg4ODQ0NCQkJCAgIBwcHBgYGAwMDP///wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAEAACQALAAAAAA+AAsAAAZiwIBwSCwaj8ik0ZE5KJ/QaPSiCVEG0qwWeoEMKqHIdkwWdoWIjWdRbk8hRMYHYwhMAPi8fs/v+/+AeiN1boVFZ0MPIBYEho5DiAodHAmPlgFdBRcfDZeXFxwiEgKenxcFSEEAOw==";
	private static final String IMG_INVERSE = "R0lGODlhPgALANUAAAAAAP////v7+/f39/Pz8+/v7+vr6+Pj49/f39vb29fX18/Pz8vLy8PDw7e3t6enp5ubm4uLi4CAgHh4eHBwcGRkZFxcXFhYWExMTDw8PDAwMCwsLCgoKCQkJCAgIBwcHBgYGAwMDP///wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAEAACIALAAAAAA+AAsAAAZYwIBwSCwaj8ikcqjAJJbQqPRYoHwg06wWKYCAJoWtWNzQOMfoKeLCcSAlgLh8Tq/b7/h5aENI+6UGFR4Pf4VLCxkZDIaMRw8dFQaNk0IDEiARApSTBxZPQQA7";
	private static final String IMG_INV_CLOSED = "R0lGODlhPgALANUAAAAAAP////v7+/f39/Pz8+/v7+vr6+Pj49/f39vb29fX18/Pz8vLy8PDw7e3t6urq6enp5+fn5ubm4CAgHh4eHBwcGRkZGBgYFhYWFBQUExMTEhISDw8PDAwMCwsLCgoKCQkJCAgIBwcHBgYGAwMDP///wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAEAACUALAAAAAA+AAsAAAZlwIBwSCwaj8ikcqjQPJbQqPRYqIgyl6l2exRIRpRCJMstaxsdTUI4NruXCMzHQWwHJoC8fs/v+/+AeyQeBEV2b4hFBhYhEHVkiZFDCxwcDGyQkpoQIBYGh5qaAxMjG5mhmgcYWUEAOw==";

	@Override
	public List<AutocompletionText> getAutocompletionStrings() {
		return Arrays.asList();
//				new AutocompletionText(START+NORMAL, "normal start arrow", IMG_NORMAL),
//				new AutocompletionText(START+CLOSED, "closed start arrow", IMG_CLOSED),
//				new AutocompletionText(START+INVERSE, "inversed normal start arrow" , IMG_INVERSE),
//				new AutocompletionText(START+INV_CLOSED, "inversed closed start arrow", IMG_INV_CLOSED),
//				new AutocompletionText(END+NORMAL, "normal end arrow", IMG_NORMAL),
//				new AutocompletionText(END+CLOSED, "closed end arrow", IMG_CLOSED),
//				new AutocompletionText(END+INVERSE, "inversed normal end arrow", IMG_INVERSE),
//				new AutocompletionText(END+INV_CLOSED, "inversed closed arrow", IMG_INV_CLOSED),
//				};
	}

	@Override
	void handleLine(String line, BaseDrawHandler drawer, PropertiesConfig propConfig, RelationPoints relationPoints) {
//		boolean start = line.startsWith(START);
//		boolean isClosed = line.endsWith(CLOSED) || line.endsWith(INV_CLOSED);
//		
//		Line lineToDraw = start ? relationPoints.getFirstLine() : relationPoints.getLastLine();
//		drawArrowToLine(drawer, lineToDraw, line.startsWith(START), line.endsWith(INVERSE), isClosed);
	}
	
}
