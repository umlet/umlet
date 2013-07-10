package com.umlet.element.experimental.uml.relation;

import java.util.Arrays;
import java.util.List;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.Line;
import com.baselet.gui.AutocompletionText;
import com.umlet.element.experimental.PropertiesConfig;

public class ArrowNormal extends Arrow {

	private static final String NORMAL = ">";
	private static final String INVERSE = "<";
	private static final String CLOSED = ">>";
	private static final String INV_CLOSED = "<<";
	private List<String> endings = Arrays.asList(NORMAL, INVERSE, CLOSED, INV_CLOSED);

	@Override
	public boolean checkStart(String line) {
		for (String ending : endings) {
			if (line.equals(START + ending) || line.equals(END + ending)) return true;
		}
		return false;
	}

	@Override
	public AutocompletionText[] getAutocompletionStrings() {
		return new AutocompletionText[] {
				new AutocompletionText(START+NORMAL, "normal start arrow", "R0lGODlhPgALANUAAAAAAP////v7+/f39+/v7+vr6+fn59/f39vb29fX19PT08vLy8PDw7e3t6urq6Ojo5ubm5eXl4eHh4ODg4CAgHh4eGxsbGhoaGBgYFhYWFRUVEhISEBAQDg4ODQ0NCQkJCAgIBwcHBgYGAwMDP///wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAEAACQALAAAAAA+AAsAAAZawIBwKDRgFMSkcslsOpkDiWgyeFqvWKXjgzFkv2CmgtNZhM/nwgX0QLu/hxFgTq/b7/i8fl+nBA4ZHw1vhFkMHhsJhYtPECEWBIySSgQVIRECk5pCCBpmm2hBADs="),
				new AutocompletionText(START+CLOSED, "closed start arrow", "R0lGODlhPgALANUAAAAAAP////f39+/v7+vr6+fn59/f39vb29fX19PT08vLy8PDw7e3t6urq6enp6Ojo5+fn5ubm4eHh4CAgHh4eGxsbGhoaGBgYFhYWFRUVExMTEhISEBAQDg4ODQ0NCQkJCAgIBwcHBgYGAwMDP///wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAEAACQALAAAAAA+AAsAAAZkwIBwKCxcLsSkcslsOpkCiYiDfFqv2GTjcylAqtmwmJjgdBTC73idJVhAD6KaTW8aRoC8fs/v+/+AgXsTAQYYHwxyYHWMSgseGwhpi42VQxEhFQNzlp1CAxQhGpSelgcZDqV1QQA7"),
				new AutocompletionText(START+INVERSE, "inversed normal start arrow" , "R0lGODlhPgALANUAAAAAAP////v7+/f39/Pz8+/v7+vr6+Pj49/f39vb29fX18/Pz8vLy8PDw7e3t6enp5ubm4uLi4CAgHh4eHBwcGRkZFxcXFhYWExMTDw8PDAwMCwsLCgoKCQkJCAgIBwcHBgYGAwMDP///wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAEAACIALAAAAAA+AAsAAAZZwEDCcggYj8ikcslsOpGCCEgyeFqv2Kah0nlkv2ArI5NZhM/o48NTMaTfX8ImBKjb7/i8fs+/S5gOHBcIcIVNCRgaDYaMSQUTIBACjZQBEB8UBZWUiAqblEEAOw=="),
				new AutocompletionText(START+INV_CLOSED, "inversed closed start arrow", "R0lGODlhPgALANUAAAAAAP////v7+/f39/Pz8+/v7+vr6+Pj49/f39vb29fX18/Pz8vLy8PDw7e3t6urq6enp5+fn5ubm4CAgHh4eHBwcGRkZGBgYFhYWFBQUExMTEhISDw8PDAwMCwsLCgoKCQkJCAgIBwcHBgYGAwMDP///wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAEAACUALAAAAAA+AAsAAAZmwMAFcwgYj8ikcslsOpGXzWgyeFqv2OUlYrCAINmwmLk1MjicxXgtLh8hIYuBTX+6jwQPCcDv+/+AgYKDfhNGdwEOHxgIdY5aEUYJGh0Nj5dJWwUUIxICmKCHGSIVBaGnDxoKp6dBADs="),
				new AutocompletionText(END+NORMAL, "normal end arrow", "R0lGODlhPgALANUAAAAAAP////v7+/f39+/v7+vr6+fn59/f39vb29fX19PT08vLy8PDw7e3t6urq6Ojo5ubm5eXl4eHh4ODg4CAgHh4eGxsbGhoaGBgYFhYWFRUVEhISEBAQDg4ODQ0NCQkJCAgIBwcHBgYGAwMDP///wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAEAACQALAAAAAA+AAsAAAZawIBwKDRgFMSkcslsOpkDiWgyeFqvWKXjgzFkv2CmgtNZhM/nwgX0QLu/hxFgTq/b7/i8fl+nBA4ZHw1vhFkMHhsJhYtPECEWBIySSgQVIRECk5pCCBpmm2hBADs="),
				new AutocompletionText(END+CLOSED, "closed end arrow", "R0lGODlhPgALANUAAAAAAP////f39+/v7+vr6+fn59/f39vb29fX19PT08vLy8PDw7e3t6urq6enp6Ojo5+fn5ubm4eHh4CAgHh4eGxsbGhoaGBgYFhYWFRUVExMTEhISEBAQDg4ODQ0NCQkJCAgIBwcHBgYGAwMDP///wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAEAACQALAAAAAA+AAsAAAZkwIBwKCxcLsSkcslsOpkCiYiDfFqv2GTjcylAqtmwmJjgdBTC73idJVhAD6KaTW8aRoC8fs/v+/+AgXsTAQYYHwxyYHWMSgseGwhpi42VQxEhFQNzlp1CAxQhGpSelgcZDqV1QQA7"),
				new AutocompletionText(END+INVERSE, "inversed normal end arrow", "R0lGODlhPgALANUAAAAAAP////v7+/f39/Pz8+/v7+vr6+Pj49/f39vb29fX18/Pz8vLy8PDw7e3t6enp5ubm4uLi4CAgHh4eHBwcGRkZFxcXFhYWExMTDw8PDAwMCwsLCgoKCQkJCAgIBwcHBgYGAwMDP///wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAEAACIALAAAAAA+AAsAAAZZwEDCcggYj8ikcslsOpGCCEgyeFqv2Kah0nlkv2ArI5NZhM/o48NTMaTfX8ImBKjb7/i8fs+/S5gOHBcIcIVNCRgaDYaMSQUTIBACjZQBEB8UBZWUiAqblEEAOw=="),
				new AutocompletionText(END+INV_CLOSED, "inversed closed arrow", "R0lGODlhPgALANUAAAAAAP////v7+/f39/Pz8+/v7+vr6+Pj49/f39vb29fX18/Pz8vLy8PDw7e3t6urq6enp5+fn5ubm4CAgHh4eHBwcGRkZGBgYFhYWFBQUExMTEhISDw8PDAwMCwsLCgoKCQkJCAgIBwcHBgYGAwMDP///wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAEAACUALAAAAAA+AAsAAAZmwMAFcwgYj8ikcslsOpGXzWgyeFqv2OUlYrCAINmwmLk1MjicxXgtLh8hIYuBTX+6jwQPCcDv+/+AgYKDfhNGdwEOHxgIdY5aEUYJGh0Nj5dJWwUUIxICmKCHGSIVBaGnDxoKp6dBADs="),
				};
	}

	@Override
	void handleLine(String line, BaseDrawHandler drawer, PropertiesConfig propConfig, RelationPoints relationPoints) {
		boolean start = line.startsWith(START);
		boolean isClosed = line.endsWith(CLOSED) || line.endsWith(INV_CLOSED);
		
		Line lineToDraw = start ? relationPoints.getFirstLine() : relationPoints.getLastLine();
		drawArrowToLine(drawer, lineToDraw, line.startsWith(START), line.endsWith(INVERSE), isClosed);
	}
	
}
