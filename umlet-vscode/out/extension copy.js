"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
Object.defineProperty(exports, "__esModule", { value: true });
// The module 'vscode' contains the VS Code extensibility API
// Import the module and reference it with the alias vscode in your code below
const vscode = require("vscode");
const path = require("path");
const fs = require("fs");
class UmletEditorProvider {
}
exports.UmletEditorProvider = UmletEditorProvider;
// this method is called when your extension is activated
// your extension is activated the very first time the command is executed
function activate(context) {
    // Use the console to output diagnostic information (console.log) and errors (console.error)
    // This line of code will only be executed once when your extension is activated
    console.log("Umlet extension started");
    //array to keep track of opened panels to avoid re-opening
    const openedPanels = [];
    const previewAndCloseSrcDoc = (document) => __awaiter(this, void 0, void 0, function* () {
        //if uxf file is opened, close the editor and open a new one with the file loaded
        if (document.fileName.split('.').pop() === "uxf") {
            yield vscode.commands.executeCommand("workbench.action.closeActiveEditor");
            //Check if panel already opened, just focus it if it already is, otherwise open new one
            let openedPanel = getOpenedPanel(document.fileName.toString());
            if (!openedPanel) {
                yield vscode.commands.executeCommand("extension.umlet", document);
            }
            else {
                openedPanel.panel.reveal(openedPanel.panel.viewColumn);
            }
        }
    });
    /*
      checks if a certain file is already opened in an umlet tab
    */
    function getOpenedPanel(filePath) {
        let openedPanel = openedPanels.find(panel => panel.filePath === filePath);
        if (!openedPanel) {
            return undefined;
        }
        return openedPanel;
    }
    const openedEvent = vscode.workspace.onDidOpenTextDocument((document) => {
        previewAndCloseSrcDoc(document);
    });
    let disposable = vscode.commands.registerCommand('extension.umlet', (document) => {
        //document.fileName.toString() provides the full path, we only want the filename itself
        let filename = document.fileName.toString().replace(/^.*[\\\/]/, '');
        //start umlet and save the new panel with its uri
        let openedUmletWebview = startUmlet(context, document.getText().toString(), filename);
        //make sure to remove an opened panel when its closed
        let openedUmletPanelWithPath = { panel: openedUmletWebview, filePath: document.fileName.toString() };
        openedUmletWebview.onDidDispose(() => {
            openedPanels.splice(openedPanels.indexOf(openedUmletPanelWithPath), 1);
        });
        openedPanels.push(openedUmletPanelWithPath);
    });
    context.subscriptions.push(disposable);
}
exports.activate = activate;
function startUmlet(context, fileContents, fileName) {
    const panel = vscode.window.createWebviewPanel('umlet', fileName, vscode.ViewColumn.One, {
        enableScripts: true,
        retainContextWhenHidden: true,
        localResourceRoots: [vscode.Uri.file(path.join(context.extensionPath, 'src', 'umlet-gwt'))]
    });
    // Handle messages from the webview
    panel.webview.onDidReceiveMessage(message => {
        switch (message.command) {
            case 'exportUxf':
                SaveFile(message.text);
                return;
            case 'exportPng':
                var actual_data = message.text.replace("data:image/png;base64,", "");
                SaveFileDecode(actual_data);
                return;
        }
    }, undefined, context.subscriptions);
    // Get path to resource on disk
    const onDiskPath = vscode.Uri.file(path.join(context.extensionPath, 'src', 'umlet-gwt'));
    // And get the special URI to use with the webview
    const localUmletFolder = panel.webview.asWebviewUri(onDiskPath);
    if (fileContents === undefined) {
        panel.webview.html = GetUmletWebviewPage(localUmletFolder.toString(), 'undefined'); //TODO not working, loads uninteractable umletino without borders
    }
    else {
        panel.webview.html = GetUmletWebviewPage(localUmletFolder.toString(), fileContents.toString());
    }
    return panel;
}
//shows popup savefile dialog for uxf files
function SaveFile(fileContent) {
    vscode.window.showSaveDialog({
        filters: {
            'UML Diagram': ['uxf']
        }
    })
        .then(fileInfos => {
        if (fileInfos !== undefined) {
            fs.writeFile(fileInfos.fsPath, fileContent, function (err) {
                if (err) {
                    return console.log(err);
                }
            });
        }
    });
}
//shows popup savefile dialog for png files
function SaveFileDecode(fileContent) {
    vscode.window.showSaveDialog({
        filters: {
            'Image': ['png']
        }
    })
        .then(fileInfos => {
        if (fileInfos !== undefined) {
            fs.writeFile(fileInfos.fsPath, fileContent, { encoding: 'base64' }, function (err) {
                if (err) {
                    return console.log(err);
                }
            });
        }
    });
}
/**
  *
  * Gets a modified version of the initial starting page of the GWT umletino version
  * @param localUmletFolder folder which holds the local umletino gwt version.
  * @param diagramData XML data of a diagram which should be loaded on start
  */
function GetUmletWebviewPage(localUmletFolder, diagramData) {
    return `<!DOCTYPE html>
  <html>
    <head>
      <base href="${localUmletFolder}/" />
      <meta name="viewport" content="user-scalable=no" />
      <meta http-equiv="content-type" content="text/html; charset=UTF-8">
      <link type="text/css" rel="stylesheet" href="umletino.css">
      <link rel="icon" type="image/x-icon" href="favicon.ico">
      <title>UMLetino - Free Online UML Tool for Fast UML Diagrams</title>
      <script type="text/javascript" src="baseletgwt/baseletgwt.nocache.js?2020-03-15_09-48-08"></script>
    </head>
    <body>
      <!-- the following line is necessary for history support -->
      <iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1' style="position:absolute;width:0;height:0;border:0"></iframe>
      
      <!-- the website will not work without JavaScript -->
      <noscript>
        <div style="width: 25em; position: absolute; left: 50%; margin-left: -11em; background-color: white; border: 1px solid red; padding: 4px; font-family: sans-serif">
          You must enable JavaScript to use this web application.
      </div>
      </noscript>
      <div align="left" id="featurewarning" style="color: red; font-family: sans-serif; font-weight:bold; font-size:1.2em"></div>
      
    </body>
    <script>
      function getTheme() {
        switch(document.body.className) {
          case 'vscode-light':
            return 'LIGHT';
          case 'vscode-dark':
          case 'vscode-hight-contrast':
            return theme = 'DARK';
        }
      }

      var theme = 'LIGHT';
      theme = getTheme();
      var vscode = acquireVsCodeApi();
      var vsCodeInitialDiagramData = \`${diagramData}\`;
    </script>

  </html>`;
}
// this method is called when your extension is deactivated
function deactivate() { }
exports.deactivate = deactivate;
//# sourceMappingURL=extension copy.js.map