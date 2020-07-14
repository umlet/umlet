package com.vscode.gwt.client.view.widgets;

import com.baselet.element.interfaces.Diagram;
import com.baselet.gwt.client.element.DiagramXmlParser;
import com.baselet.gwt.client.view.CanvasUtils;
import com.baselet.gwt.client.view.widgets.DownloadPopupPanel;
import com.baselet.gwt.client.view.widgets.FilenameAndScaleHolder;
import com.baselet.gwt.client.view.widgets.InputEvent;
import com.baselet.gwt.client.view.widgets.InputHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;

public class VsCodeDownloadPopupPanel extends DownloadPopupPanel {

    public VsCodeDownloadPopupPanel() {
        initListener();
    }

    @Override
    public void prepare(FilenameAndScaleHolder filenameAndScaleHolder) {
        Diagram diagram = drawPanelDiagram.getDiagram();

        setHeader("Export Diagram");
        FlowPanel panel = new FlowPanel();

        //handle scaling
        HTML scaleHtml = new HTML("Set scaling of Image file:");
        panel.add(scaleHtml);
        final TextBox scaleBox = new TextBox();
        panel.add(scaleBox);
        scaleBox.setValue("1.0");
        filenameAndScaleHolder.setScaling(1d);

        panel.add(new HTML("<br>"));

        String uxfUrl = "data:text/xml;charset=utf-8," + DiagramXmlParser.diagramToXml(true, true, diagram);

        Button saveDiagramButton = new Button();
        saveDiagramButton.setText("Save Diagram File");
        saveDiagramButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                exportDiagramVSCode(uxfUrl);
            }
        });
        panel.add(saveDiagramButton);
        Button savePictureButton = new Button();
        savePictureButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String scaledPngUrl = CanvasUtils.createPngCanvasDataUrl(diagram, filenameAndScaleHolder.getScaling());
                exportPngVSCode(scaledPngUrl);

					/*
						this line is to prevent a bug in the vs code version
						if this is not recalculated with a 1.0 scaling, then the display size of the elements in the diagram will change
						to whatever scaling was calculated for exporting
					 */
                CanvasUtils.createPngCanvasDataUrl(diagram, 1.0d);
            }
        });
        savePictureButton.setText("Save Image File");
        panel.add(savePictureButton);
        setWidget(panel);
        //renew download links when scaling is changed
        scaleBox.addDomHandler(new InputHandler() {
            @Override
            public void onInput(InputEvent event) {
                try {
                    double scalingValue = Double.parseDouble(scaleBox.getValue());
                    if (scalingValue <= 0)
                        throw new Exception();
                    filenameAndScaleHolder.setScaling(scalingValue);
                } catch (Exception e) {
                    //wrong scaling value, just default to standard
                    filenameAndScaleHolder.setScaling(1.0d);
                }
            }
        }, InputEvent.getType());
        // listen to all input events from the browser (http://stackoverflow.com/a/43089693)
        /* textBox.addDomHandler(new InputHandler() {
         * @Override public void onInput(InputEvent event) { //not needed in vs code version since filename will be entered in popup dialog //filenameAndScaleHolder.setFilename(textBox.getText()); } }, InputEvent.getType()); */
    }



    private void handleExport(String size) {
        double scalingValue = Double.parseDouble(size);
        String scaledPngUrl = CanvasUtils.createPngCanvasDataUrl(drawPanelDiagram.getDiagram(), scalingValue);
        exportPngVSCode(scaledPngUrl);
    }

    private native void initListener() /*-{
        var that = this;
        $wnd.addEventListener('message', function (event) {
            var message = event.data;
            switch (message.command) {
                case 'requestExport':
                    that.@com.vscode.gwt.client.view.widgets.VsCodeDownloadPopupPanel::handleExport(Ljava/lang/String;)(message.text);
                    break;
                case 'myUpdate': //message.text is expected to be the new diagram the editor should changed to
                    that.@com.vscode.gwt.client.view.widgets.VsCodeDownloadPopupPanel::handleUpdateContent(Ljava/lang/String;)(message.text);
                    break;
            }

        });
    }-*/;

    public  void handleUpdateContent(String content)
    {
        //TODO this try/catch does not work since xmlToDiagram apparently does not throw exceptions
        try {
            this.drawPanelDiagram.setDiagram(DiagramXmlParser.xmlToDiagram(content));
        } catch (Exception e) {
            //TODO display error message if state is currently invalid
            //GWT.log("failed to load diagram passed from vscode, loading preset empty diagram defaults...");
        }
    }

    private native void exportDiagramVSCode(String msg) /*-{
        window.parent.vscode.postMessage({
            command: 'exportUxf',
            text: msg
        });
    }-*/;

    private native void exportPngVSCode(String msg) /*-{
        window.parent.vscode.postMessage({
            command: 'exportPng',
            text: msg
        });
    }-*/;
}
