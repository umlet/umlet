"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
// The module 'vscode' contains the VS Code extensibility API
// Import the module and reference it with the alias vscode in your code below
const vscode = require("vscode");
const CustomTextEditorProvider_1 = require("./CustomTextEditorProvider");
// this method is called when your extension is activated
// your extension is activated the very first time the command is executed
function activate(context) {
    createUmletCommands(context);
    console.log("activitaing umlet extension...");
    context.subscriptions.push(vscode.window.registerCustomEditorProvider("uxfCustoms.umletEditor", new CustomTextEditorProvider_1.UmletEditorProvider(context), {
        webviewOptions: { retainContextWhenHidden: true }
    }));
}
exports.activate = activate;
function createUmletCommands(context) {
    //Register Commands for exporting in default and x4 size
    //x1 Size
    const commandHandlerOneExport = () => {
        CustomTextEditorProvider_1.currentlyActivePanel === null || CustomTextEditorProvider_1.currentlyActivePanel === void 0 ? void 0 : CustomTextEditorProvider_1.currentlyActivePanel.webview.postMessage({
            command: 'requestExport',
            text: "1"
        });
    };
    context.subscriptions.push(vscode.commands.registerCommand('umlet.exportPngOne', commandHandlerOneExport));
    //x4 Size
    const commandHandlerFourExport = () => {
        CustomTextEditorProvider_1.currentlyActivePanel === null || CustomTextEditorProvider_1.currentlyActivePanel === void 0 ? void 0 : CustomTextEditorProvider_1.currentlyActivePanel.webview.postMessage({
            command: 'requestExport',
            text: "4"
        });
    };
    context.subscriptions.push(vscode.commands.registerCommand('umlet.exportPngFour', commandHandlerFourExport));
    //keyboard commands for copy paste cut
    //copy
    const commandHandlerKeyboardCopy = () => {
        if (CustomTextEditorProvider_1.currentlyActivePanel !== null && vscode.window.activeTextEditor === undefined) {
            console.log("MESSAGE Copy but keyboard");
            //vscode.commands.executeCommand("editor.action.clipboardCopyAction");
            CustomTextEditorProvider_1.currentlyActivePanel === null || CustomTextEditorProvider_1.currentlyActivePanel === void 0 ? void 0 : CustomTextEditorProvider_1.currentlyActivePanel.webview.postMessage({ command: 'copy' });
        }
    };
    context.subscriptions.push(vscode.commands.registerCommand('umlet.executeCopy', commandHandlerKeyboardCopy));
    //paste
    const commandHandlerKeyboardPaste = () => {
        if (CustomTextEditorProvider_1.currentlyActivePanel !== null && vscode.window.activeTextEditor === undefined) {
            console.log("MESSAGE paste but keyboard");
            //vscode.commands.executeCommand("editor.action.clipboardPasteAction");
            vscode.env.clipboard.readText().then((text) => {
                let clipboard_content = text;
                console.log("MESSAGE Paste, content is:" + clipboard_content);
                CustomTextEditorProvider_1.currentlyActivePanel === null || CustomTextEditorProvider_1.currentlyActivePanel === void 0 ? void 0 : CustomTextEditorProvider_1.currentlyActivePanel.webview.postMessage({
                    command: 'paste',
                    text: clipboard_content
                });
            });
        }
    };
    context.subscriptions.push(vscode.commands.registerCommand('umlet.executePaste', commandHandlerKeyboardPaste));
    //cut
    const commandHandlerKeyboardCut = () => {
        if (CustomTextEditorProvider_1.currentlyActivePanel !== null && vscode.window.activeTextEditor === undefined) {
            console.log("MESSAGE Cut but keyboard");
            CustomTextEditorProvider_1.currentlyActivePanel === null || CustomTextEditorProvider_1.currentlyActivePanel === void 0 ? void 0 : CustomTextEditorProvider_1.currentlyActivePanel.webview.postMessage({ command: 'cut' });
        }
    };
    context.subscriptions.push(vscode.commands.registerCommand('umlet.executeCut', commandHandlerKeyboardCut));
}
//# sourceMappingURL=extension.js.map