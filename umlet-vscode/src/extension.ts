// The module 'vscode' contains the VS Code extensibility API
// Import the module and reference it with the alias vscode in your code below
import * as vscode from 'vscode';
import {
  ExtensionContext,
  TextDocument,
  ViewColumn,
  Uri,
  WebviewPanel,
} from "vscode";
import * as path from 'path';
import fs = require('fs');
import { UmletEditorProvider, currentlyActivePanel } from './CustomTextEditorProvider';

// this method is called when your extension is activated
// your extension is activated the very first time the command is executed
export function activate(context: vscode.ExtensionContext) {
  


  
  createUmletCommands(context);


  console.log("activitaing umlet extension...");
  UmletEditorProvider.overrideVsCodeCommands(context);
  context.subscriptions.push(vscode.window.registerCustomEditorProvider(
    "uxfCustoms.umletEditor",
    new UmletEditorProvider(context),
    {
      webviewOptions: {retainContextWhenHidden: true}
    }
  ));


  
}

function createUmletCommands(context: vscode.ExtensionContext) {
  //Register Commands for exporting in default and x4 size
  //x1 Size
  const commandHandlerOneExport = () => {
    currentlyActivePanel?.webview.postMessage({
      command: 'requestExport',
      text: "1"
    });
  };
  context.subscriptions.push(vscode.commands.registerCommand('umlet.exportPngOne', commandHandlerOneExport));
  //x4 Size
  const commandHandlerFourExport = () => {
    currentlyActivePanel?.webview.postMessage({
      command: 'requestExport',
      text: "4"
    });
  };
  context.subscriptions.push(vscode.commands.registerCommand('umlet.exportPngFour', commandHandlerFourExport));

  //create a new file and open it in editor
  const commandHandlerCreateNewDiagram = () => {
    let folderPath = vscode.workspace.rootPath;
    if (folderPath === undefined)
    {
      vscode.window.showErrorMessage("Unable to create new .uxf file, since there is currently no folder/workspace opened. Please open a folder/workspace and try again!");
      return;
    }
    let fileName = "Diagram "+ getCurrentDateTimeString() + ".uxf"; 
    
    let completePath = path.join(folderPath , fileName);
    
    fs.writeFile(completePath, "", function (err) {
      if (err) { return console.log(err); }
    });
    let url = Uri.file(completePath);
    /*
      note: this does not guarantee the new file is opened with UMLet, but currently there does not seem to be an option to tell vscode to open a file with a certain editor.
      VSCode prefers Custom Editors over its built-in editors, but especially if the extention is not fully loaded yet, vscode might open this file as plaintext. 
      To prevent this case "onStartupFinished" is a startup event for umlet, so the probability that the umlet extension is not loaded when a user calls the command is greatly reduced.
      if other .uxf extensions are installed, vscode might not open the newly created file with UMLet as well, and has to be manually reopened in UMLet
    */
    vscode.commands.executeCommand('vscode.open', url); 
  };
  context.subscriptions.push(vscode.commands.registerCommand('umlet.createNewDiagram', commandHandlerCreateNewDiagram));

  function getCurrentDateTimeString(): string {
    let today = new Date();
    let dd = String(today.getDate()).padStart(2, '0');
    let mm = String(today.getMonth() + 1).padStart(2, '0'); 
    let yyyy = today.getFullYear();

    let hh = String( today.getHours()).padStart(2, '0');
    let mins = String(today.getMinutes()).padStart(2, '0');
    let ss = String(today.getSeconds()).padStart(2, '0');

    return ("" + yyyy + "-" + mm + "-" + dd + " " + hh + "-" + mins + "-" + ss );
  }

  //keyboard commands for copy paste cut
  //copy
  const commandHandlerKeyboardCopy = () => {
    if (currentlyActivePanel !== null && vscode.window.activeTextEditor === undefined) {
      console.log("MESSAGE Copy but keyboard");
      //vscode.commands.executeCommand("editor.action.clipboardCopyAction");
      currentlyActivePanel?.webview.postMessage({ command: 'copy' });
    }
  };
  context.subscriptions.push(vscode.commands.registerCommand('umlet.executeCopy', commandHandlerKeyboardCopy));

  //paste
  const commandHandlerKeyboardPaste = () => {
    if (currentlyActivePanel !== null && vscode.window.activeTextEditor === undefined) {
      console.log("MESSAGE paste but keyboard");
      //vscode.commands.executeCommand("editor.action.clipboardPasteAction");
      vscode.env.clipboard.readText().then((text) => {
        let clipboard_content = text;
        console.log("MESSAGE Paste, content is:" + clipboard_content);
        currentlyActivePanel?.webview.postMessage({
          command: 'paste',
          text: clipboard_content
        });
      });
    }
  };
  context.subscriptions.push(vscode.commands.registerCommand('umlet.executePaste', commandHandlerKeyboardPaste));

  //cut
  const commandHandlerKeyboardCut = () => {
    if (currentlyActivePanel !== null && vscode.window.activeTextEditor === undefined) {
      console.log("MESSAGE Cut but keyboard");
      currentlyActivePanel?.webview.postMessage({ command: 'cut' });
    }
  };
  context.subscriptions.push(vscode.commands.registerCommand('umlet.executeCut', commandHandlerKeyboardCut));
}
