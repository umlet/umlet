package com.baselet.standalone;

import com.baselet.control.basics.geom.Rectangle;
import com.baselet.control.config.handler.ConfigHandler;
import com.baselet.control.enums.ElementId;
import com.baselet.control.enums.Program;
import com.baselet.control.enums.RuntimeType;
import com.baselet.control.util.Utils;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.io.DiagramFileHandler;
import com.baselet.element.ElementFactorySwing;
import com.baselet.element.NewGridElement;
import com.baselet.element.interfaces.GridElement;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.io.File;

import static org.junit.Assert.*;

public class StepDefinitions {
    private DiagramHandler diagramToSave;
    private DiagramHandler diagramToLoad;
    private File ufxTempFile;

    private int x;
    private int y;
    private int width;
    private int height;

    @Given("A new diagram with a class element positioned at {int}, {int} with a width of {int} and a height of {int}")
    public void aNewDiagramWithAElementPositionedAt(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        Utils.BuildInfo buildInfo = Utils.readBuildInfo();
        Program.init(buildInfo.version, RuntimeType.BATCH);
        ConfigHandler.loadConfig();

        ufxTempFile = new File("src/test/resources/empty_diagram.uxf");
        diagramToSave = new DiagramHandler(ufxTempFile);

        NewGridElement element = ElementFactorySwing.create(
                ElementId.UMLClass,
                new Rectangle(x, y, width, height),
                "Properties",
                "Properties",
                diagramToSave);
        diagramToSave.getDrawPanel().addElement(element);
    }

    @When("the diagram has been saved")
    public void theDiagramHasBeenSaved() throws Throwable {
        ufxTempFile = File.createTempFile("temp", ".ufx");
        DiagramFileHandler diagramFileHandler = DiagramFileHandler.createInstance(diagramToSave, ufxTempFile);
        diagramFileHandler.doSave();
    }

    @Then("load the saved diagram")
    public void loadTheSavedDiagram() {
        diagramToLoad = new DiagramHandler(ufxTempFile);
    }

    @Then("verify that the loaded element is positioned at the same position with the same size")
    public void verifyThatTheElementIsPositionedAt() {
        GridElement gridElement = diagramToLoad.getDrawPanel().getGridElements().get(0);
        assertEquals(gridElement.getRectangle().getX(), this.x);
        assertEquals(gridElement.getRectangle().getY(), this.y);
        assertEquals(gridElement.getRectangle().getWidth(), this.width);
        assertEquals(gridElement.getRectangle().getHeight(), this.height);
    }
}
