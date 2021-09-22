package com.web.gwt.client.view.commands;

import com.baselet.gwt.client.base.Notification;
import com.baselet.gwt.client.element.DiagramXmlParser;
import com.baselet.gwt.client.element.WebStorage;
import com.baselet.gwt.client.view.commands.SaveCommand;
import com.baselet.gwt.client.view.widgets.SaveDialogBox;

public class WebSaveCommand extends SaveCommand {

    private final SaveDialogBox saveDialogBox = new SaveDialogBox(new SaveDialogBox.Callback() {
        @Override
        public void callback(final String chosenName) {
            boolean itemIsNewlyAdded = WebStorage.getSavedDiagram(chosenName) == null;
            WebStorage.addSavedDiagram(chosenName, DiagramXmlParser.diagramToXml(mainView.getDiagramPanel().getDiagram()));
            if (itemIsNewlyAdded) {
                mainView.addRestoreMenuItem(chosenName);
            }
            Notification.showInfo("Diagram saved as: " + chosenName);
        }
    });

    @Override
    public void execute() {
        saveDialogBox.clearAndCenter();
    }
}
