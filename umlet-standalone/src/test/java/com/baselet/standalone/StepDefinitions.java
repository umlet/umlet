package com.baselet.standalone;

import com.baselet.control.basics.geom.Rectangle;
import com.baselet.control.config.handler.ConfigHandler;
import com.baselet.control.enums.ElementId;
import com.baselet.control.enums.Program;
import com.baselet.control.enums.RuntimeType;
import com.baselet.control.util.Utils;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.Notifier;
import com.baselet.diagram.io.DiagramFileHandler;
import com.baselet.element.ElementFactorySwing;
import com.baselet.element.NewGridElement;
import com.baselet.element.interfaces.GridElement;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class StepDefinitions {
    private DiagramHandler diagramToSave;
    private DiagramHandler diagramToLoad;
    private File ufxTempFile;

    @Given("^A new diagram with a element positioned at '(\\d+),(\\d+)'$")
    public void aNewDiagramWithAElementPositionedAt(int arg0, int arg1) {
        Utils.BuildInfo buildInfo = Utils.readBuildInfo();
        Program.init(buildInfo.version, RuntimeType.STANDALONE);
        ConfigHandler.loadConfig();

        ufxTempFile = new File("src/test/resources/empty.uxf");
        diagramToSave = new DiagramHandler(ufxTempFile);

        NewGridElement element = ElementFactorySwing.create(
                ElementId.UMLClass,
                new Rectangle(100, 100, 100, 100),
                "Properties",
                "Properties",
                diagramToSave);
        diagramToSave.getDrawPanel().addElement(element);
    }

    @When("the diagram has been saved")
    public void theDiagramHasBeenSaved() throws IOException {
        ufxTempFile = File.createTempFile("temp", ".ufx");
        DiagramFileHandler diagramFileHandler = DiagramFileHandler.createInstance(diagramToSave, ufxTempFile);
        diagramFileHandler.doSave();
    }

    @Then("load the saved diagram")
    public void loadTheSavedDiagram() {
        diagramToLoad = new DiagramHandler(ufxTempFile);
    }

    @Then("^verify that the element is positioned at '(\\d+,(\\d+))'$")
    public void verifyThatTheElementIsPositionedAt(int arg0, int arg1) {
        GridElement gridElement = diagramToLoad.getDrawPanel().getGridElements().get(0);
        assertEquals(gridElement.getRectangle().getX(), arg0);
        assertEquals(gridElement.getRectangle().getY(), arg1);
    }
}
