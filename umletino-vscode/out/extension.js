"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
// The module 'vscode' contains the VS Code extensibility API
// Import the module and reference it with the alias vscode in your code below
const vscode = require("vscode");
const CustomTextEditorProvider_1 = require("./CustomTextEditorProvider");
// this method is called when your extension is activated
// your extension is activated the very first time the command is executed
function activate(context) {
    context.subscriptions.push(vscode.window.registerCustomEditorProvider("uxfCustoms.umletEditor", new CustomTextEditorProvider_1.UmletEditorProvider(context), {
        webviewOptions: { retainContextWhenHidden: true }
    }));
}
exports.activate = activate;
//# sourceMappingURL=extension.js.map