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
import { UmletEditorProvider } from './CustomTextEditorProvider';

// this method is called when your extension is activated
// your extension is activated the very first time the command is executed
export function activate(context: vscode.ExtensionContext) {
  context.subscriptions.push(vscode.window.registerCustomEditorProvider(
    "uxfCustoms.umletEditor",
    new UmletEditorProvider(context),
    {
      webviewOptions: {retainContextWhenHidden: true}
    }
  ));
}