package com.baselet.gwt.client.view.commands;

import com.baselet.command.CommandTarget;
import com.baselet.command.RemoveGridElementCommand;
import com.baselet.element.interfaces.GridElement;
import com.baselet.gwt.client.view.DrawPanelDiagram;

import java.util.List;

//Update Command which will not trigger that the diagram was updated for vscode
//supposed to be used with DrawPanelDiagram
public class RemoveGridElementCommandNoUpdate extends RemoveGridElementCommand {
    public RemoveGridElementCommandNoUpdate(CommandTarget target, List<GridElement> elements) {
        super(target, elements);
    }

    @Override
    public void execute() {
        if (target instanceof DrawPanelDiagram)
        {
            DrawPanelDiagram targetAsDPD = (DrawPanelDiagram) target;
            targetAsDPD.removeGridElementsDontNotifyUpdate(elements);
        } else {
            super.execute();
        }

    }

    @Override
    public void undo() {
        if (target instanceof DrawPanelDiagram)
        {
            DrawPanelDiagram targetAsDPD = (DrawPanelDiagram) target;
            targetAsDPD.addGridElementsDontNotifyUpdate(elements);
        } else {
            super.undo();
        }
    }
}
