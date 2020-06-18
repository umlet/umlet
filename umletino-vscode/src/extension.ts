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

  //keyboard commands for copy paste cut
  //copy
  const commandHandlerKeyboardCopy = () => {
    if (currentlyActivePanel !== null && vscode.window.activeTextEditor === undefined) {
      console.log("MESSAGE Copy but keyboard");
      vscode.commands.executeCommand("editor.action.clipboardCopyAction");
    }
  };
  context.subscriptions.push(vscode.commands.registerCommand('umlet.executeCopy', commandHandlerKeyboardCopy));

  //paste
  const commandHandlerKeyboardPaste = () => {
    if (currentlyActivePanel !== null && vscode.window.activeTextEditor === undefined) {
      console.log("MESSAGE paste but keyboard");
      vscode.commands.executeCommand("editor.action.clipboardPasteAction");
    }
  };
  context.subscriptions.push(vscode.commands.registerCommand('umlet.executePaste', commandHandlerKeyboardPaste));

  //cut
  const commandHandlerKeyboardCut = () => {
    if (currentlyActivePanel !== null && vscode.window.activeTextEditor === undefined) {
      console.log("MESSAGE cut but keyboard");
      vscode.commands.executeCommand("editor.action.clipboardCutAction");
    }
  };
  context.subscriptions.push(vscode.commands.registerCommand('umlet.executeCut', commandHandlerKeyboardCut));
}
