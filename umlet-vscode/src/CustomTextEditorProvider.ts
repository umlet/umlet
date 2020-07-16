// The module 'vscode' contains the VS Code extensibility API
// Import the module and reference it with the alias vscode in your code below
import * as vscode from 'vscode';
import {
  ExtensionContext,
  TextDocument,
  ViewColumn,
  Uri,
  WebviewPanel,
  CustomTextEditorProvider
} from "vscode";
import * as path from 'path';
import fs = require('fs');

var globalContext: vscode.ExtensionContext;
var nextUmletEditorId = 0; //used to assign ids to each text editor
var initialSwapWasAlreadySkipped = false; //used to prevent instant disabling of a newly created tab by another tab which would overwrite the currentlyActivePanel to null because it deactivates after the new panel is created

export var currentlyActivePanel: WebviewPanel | null = null;
let lastChangeTriggeredByUri = ""; //whenever a document change is triggered by an instance of the UMLet Gwt application, the uri of the corresponding document will be tracked here.



export class UmletEditorProvider implements vscode.CustomTextEditorProvider {

  private outputChannel: vscode.OutputChannel;






  public static register(context: vscode.ExtensionContext): vscode.Disposable {

    const provider = new UmletEditorProvider(context);
    globalContext = context;







    provider.consoleLog('asdasd');
    context.subscriptions.push(clipboardCopyDisposable);
    context.subscriptions.push(clipboardPasteDisposable);
    provider.consoleLog('asdasd');

    const providerRegistration = vscode.window.registerCustomEditorProvider(UmletEditorProvider.viewType, provider);
    return providerRegistration;
  }

  constructor(
    private readonly context: vscode.ExtensionContext
  ) {
    this.outputChannel = vscode.window.createOutputChannel('UMLet');
  }

  private static readonly viewType = 'uxfCustoms.umletEditor';

  consoleLog(params: string) {
    var channel = vscode.window.createOutputChannel('myoutputchannel');
    channel.appendLine('new clip pushg');
    channel.show();
  }



  /**
   * Called when our custom editor is opened.
   *
   */
  resolveCustomTextEditor(document: vscode.TextDocument, webviewPanel: vscode.WebviewPanel, token: vscode.CancellationToken): void | Thenable<void> {
    console.log("resolving custom editor, uri is: " + document.uri.toString());
    //vscode.commands.executeCommand('setContext', 'textInputFocus', true); //permanently sets textfocus, never to be changed again?
    //vscode.commands.executeCommand('getContext', 'textInputFocus');

    console.log("Opened Custom Editor opened, id " + nextUmletEditorId);
    let myId = nextUmletEditorId;
    initialSwapWasAlreadySkipped = false;
    nextUmletEditorId++;
    currentlyActivePanel = webviewPanel;
    //Set myWebviewFocused as in context, so the extension can decide wheter or not to show the umlet specific export commands in vscode
    vscode.commands.executeCommand('setContext',
      'myWebviewFocused',
      currentlyActivePanel);
    console.log("editor swapped to active");




    //track viewstate changes so currentlyActivePanel stays accurate
    webviewPanel.onDidChangeViewState(
      e => {
        console.log("A panel did change state");
        if (e.webviewPanel.active) {
          console.log("editor swapped to active");
          currentlyActivePanel = webviewPanel;
        } else {
          //Do not set to null if this was triggere was a newly opened panel
          //otherwise this would always overwrite newly opened panels
          if (myId === (nextUmletEditorId - 1) || initialSwapWasAlreadySkipped === true) {
            console.log("editor swapped to DEACT, myId: " + myId + ", next-1: " + (nextUmletEditorId - 1));
            currentlyActivePanel = null;
          }
        }
        initialSwapWasAlreadySkipped = true;

        //Set myWebviewFocused as in context, so the extension can decide wheter or not to show the umlet specific export commands in vscode
        vscode.commands.executeCommand('setContext',
          'myWebviewFocused',
          currentlyActivePanel);
      }
    );



    //whenever the .uxf file is changed (for example throough a text editor in vs code), these changes shoule be reflected in umlet
    const changeDocumentSubscription = vscode.workspace.onDidChangeTextDocument(e => {
      console.log("text document changed!, last change  uri:" + lastChangeTriggeredByUri +  "       " + e.document.uri.toString() + "     " + document.uri.toString());
      
      //everytime something is changed by the gwt application, lastChangeTriggeredByUri will be set to the according document uri. 
      //this is used to avoid a reset when the last change came directly from the gwt application, which would de-select current elements
      //currentlyActivePanel has to be checked, otherwise this will only be triggered for the first opened editor. we only want it on the active
      if (lastChangeTriggeredByUri === document.uri.toString() && webviewPanel === currentlyActivePanel) {
        console.log("last change came from the gwt application");
        lastChangeTriggeredByUri = "";
      }  else {
        //only trigger for right document
        if (e.document.uri.toString() === document.uri.toString()) {
          console.log("match text change");
          webviewPanel.webview.postMessage({
            command: 'myUpdate',
            text: document.getText()
          });
        }
      }
			
		});

    webviewPanel.webview.options = {
      enableScripts: true,
      localResourceRoots: [vscode.Uri.file(path.join(this.context.extensionPath, 'target', 'umlet-vscode-14.4.0-SNAPSHOT'))]
    };

    let WebviewPanelOptions = webviewPanel.options;
    WebviewPanelOptions = {
      retainContextWhenHidden: true
    };


    // Handle messages from the webview
    webviewPanel.webview.onDidReceiveMessage(message => {
      switch (message.command) {
        case 'exportUxf':
          this.SaveFile(message.text);
          return;
        case 'updateFiledataUxf':
          console.log("uri in log is now: +" + document.uri.toString());
          this.UpdateCurrentFile(message.text, document);
          return;
        case 'exportPng':
          var actual_data = message.text.replace("data:image/png;base64,", "");
          this.SaveFileDecode(actual_data);
          return;
        case 'postLog':
          this.postLog(message.text);
          return;
        case 'setClipboard':
          vscode.env.clipboard.writeText(message.text);
          return;
        case 'requestPasteClipboard':
          vscode.env.clipboard.readText().then((text) => {
            let clipboard_content = text;
            console.log("MESSAGE Paste, content is:" + clipboard_content);
            currentlyActivePanel?.webview.postMessage({
              command: 'paste-response',
              text: clipboard_content
            });
          });
          return;
      }
    }, undefined, this.context.subscriptions);

    // Get path to resource on disk
    const onDiskPath = vscode.Uri.file(path.join(this.context.extensionPath, 'target', 'umlet-vscode-14.4.0-SNAPSHOT'));
    // And get the special URI to use with the webview
    const localUmletFolder = webviewPanel.webview.asWebviewUri(onDiskPath);

    let fileContents = document.getText().toString();

    webviewPanel.webview.html = this.getUmletWebviewPage(localUmletFolder.toString(), fileContents.toString());

  }

  //gets the updated filedata from the webview if anything has changed
  UpdateCurrentFile(fileContent: string, document: vscode.TextDocument) {
    lastChangeTriggeredByUri = document.uri.toString(); //used to avoid ressetting the webview if a change was triggered by the webview anyway
    console.log("lastChangeTriggeredByUri set to: " + lastChangeTriggeredByUri)
    const edit = new vscode.WorkspaceEdit();

    edit.replace(
      document.uri,
      new vscode.Range(0, 0, document.lineCount, 0),
      fileContent);

    return vscode.workspace.applyEdit(edit);
  }




  //shows popup savefile dialog for uxf files
  SaveFile(fileContent: string) {
    vscode.window.showSaveDialog({
      filters: {
        'UML Diagram': ['uxf']
      }
    })
      .then(fileInfos => {
        if (fileInfos !== undefined) {
          fs.writeFile(fileInfos.fsPath, fileContent, function (err) {
            if (err) { return console.log(err); }
          });
        }
      });
  }

  //shows popup savefile dialog for png files
  SaveFileDecode(fileContent: string) {
    vscode.window.showSaveDialog({
      filters: {
        'Image': ['png']
      }
    })
      .then(fileInfos => {
        if (fileInfos !== undefined) {
          fs.writeFile(fileInfos.fsPath, fileContent, { encoding: 'base64' }, function (err) {
            if (err) { return console.log(err); }
          });
        }
      });
  }

  postLog(message: string) {
    this.outputChannel.appendLine(message);
  }

  /**
    *
    * Gets a modified version of the initial starting page of the GWT umletino version
    * @param localUmletFolder folder which holds the local umletino gwt version.
    * @param diagramData XML data of a diagram which should be loaded on start
    */
  getUmletWebviewPage(localUmletFolder: string, diagramData: string) {
    let encodedDiagramData = encodeURIComponent(diagramData); //encode diagramData to prevent special characters to escape the string quotes which could lead to arbitrary javascript or html
    return `<!DOCTYPE html>
  <html>
    <head>
      <base href="${localUmletFolder}/" />
      <meta name="viewport" content="user-scalable=no" />
      <meta http-equiv="content-type" content="text/html; charset=UTF-8">
      <link type="text/css" rel="stylesheet" href="umletino.css">
      <link rel="icon" type="image/x-icon" href="favicon.ico">
      <title>UMLetino - Free Online UML Tool for Fast UML Diagrams</title>
      <script type="text/javascript" src="umletvscode/umletvscode.nocache.js?2020-03-15_09-48-08"></script>
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
    
      var vsCodeClipboardManager = null;

      function getTheme() {
        switch(document.body.className) {
          case 'vscode-light':
            return 'LIGHT'; 
          case 'vscode-dark':
          case 'vscode-hight-contrast':
            return 'DARK';
          default:
            return 'LIGHT';
        }
      }

      function switchBodyColor(theme) {
        switch(theme) {
          case 'DARK':
            document.body.style.backgroundColor = 'black';
            break;
          case 'LIGHT':
            document.body.style.backgroundColor = '';
            break;
          default:
            document.body.style.backgroundColor = '';
        }
      }

      // Observing theme changes
      var observer = new MutationObserver(function(mutations) {
        mutations.forEach(function(mutationRecord) {
            var themeFromClass = getTheme();
            if(window.changeTheme) {
              window.changeTheme(themeFromClass);
            }
            switchBodyColor(themeFromClass);
        });    
      });
      
      var target = document.body;
      observer.observe(target, { attributes : true, attributeFilter : ['class'] });

      // Retrieving current theme
      var theme = 'LIGHT';
      theme = getTheme();
      switchBodyColor(theme);

      var vscode = acquireVsCodeApi();
      var vsCodeInitialDiagramData = \`${encodedDiagramData}\`;
      console.log("vsCodeInitialDiagramData: " + vsCodeInitialDiagramData);
    </script>

  </html>`;
  }

}

//Has to be set to true, so copy paste commands via keyboard are registered by vs code
//vscode.commands.executeCommand('setContext', 'textInputFocus', true);


//override the editor.action.clipboardCopyAction with our own
var clipboardCopyDisposable = vscode.commands.registerCommand('editor.action.clipboardCopyAction', overriddenClipboardCopyAction);
/*
 * Function that overrides the default copy behavior. We get the selection and use it, dispose of this registered
 * command (returning to the default editor.action.clipboardCopyAction), invoke the default one, and then re-register it after the default completes
 */
function overriddenClipboardCopyAction(textEditor: any, edit: any, params: any) {

  //debug
  //Write to output.
  console.log("Copy registered");
  //dispose of the overridden editor.action.clipboardCopyAction- back to default copy behavior
  clipboardCopyDisposable.dispose();


  //execute the default editor.action.clipboardCopyAction to copy
  vscode.commands.executeCommand("editor.action.clipboardCopyAction").then(function () {

    console.log("After Copy");
    if (currentlyActivePanel !== null && vscode.window.activeTextEditor === undefined) {
      console.log("MESSAGE Copy");
      currentlyActivePanel?.webview.postMessage({ command: 'copy' });
    }

    //add the overridden editor.action.clipboardCopyAction back
    clipboardCopyDisposable = vscode.commands.registerCommand('editor.action.clipboardCopyAction', overriddenClipboardCopyAction);

    //complains about globalConext beeing undefined, not needed? seems to work fine without
    //globalContext.subscriptions.push(clipboardCopyDisposable);
  });
}

//override the editor.action.clipboardPasteAction with our own
var clipboardPasteDisposable = vscode.commands.registerCommand('editor.action.clipboardPasteAction', overriddenClipboardPasteAction);
/*
 * Function that overrides the default paste behavior. We get the selection and use it, dispose of this registered
 * command (returning to the default editor.action.clipboardPasteAction), invoke the default one, and then re-register it after the default completes
 */
function overriddenClipboardPasteAction(textEditor: any, edit: any, params: any) {

  //debug
  //Write to output.
  console.log("Paste registered");
  //dispose of the overridden editor.action.clipboardPasteAction- back to default paste behavior
  clipboardPasteDisposable.dispose();



  //execute the default editor.action.clipboardPasteAction to paste
  vscode.commands.executeCommand("editor.action.clipboardPasteAction").then(function () {

    console.log("After Paste");
    if (currentlyActivePanel !== null && vscode.window.activeTextEditor === undefined) {
      vscode.env.clipboard.readText().then((text) => {
        let clipboard_content = text;
        console.log("MESSAGE Paste, content is:" + clipboard_content);
        currentlyActivePanel?.webview.postMessage({
          command: 'paste',
          text: clipboard_content
        });
      });

    }
    //add the overridden editor.action.clipboardPasteAction back
    clipboardPasteDisposable = vscode.commands.registerCommand('editor.action.clipboardPasteAction', overriddenClipboardPasteAction);


    //complains about globalConext beeing undefined, not needed? seems to work fine without
    //globalContext.subscriptions.push(clipboardPasteDisposable);
  });
}


//override the editor.action.clipboardCutDisposable with our own
var clipboardCutDisposable = vscode.commands.registerCommand('editor.action.clipboardCutAction', overriddenClipboardCutAction);
/*
 * Function that overrides the default paste behavior. We get the selection and use it, dispose of this registered
 * command (returning to the default editor.action.clipboardPasteAction), invoke the default one, and then re-register it after the default completes
 */
function overriddenClipboardCutAction(textEditor: any, edit: any, params: any) {

  //debug
  //Write to output.
  console.log("Cut registered");
  //dispose of the overridden editor.action.clipboardCutAction- back to default cut behavior
  clipboardCutDisposable.dispose();



  //execute the default editor.action.clipboardCutAction to cut
  vscode.commands.executeCommand("editor.action.clipboardCutAction").then(function () {

    console.log("After Cut");
    if (currentlyActivePanel !== null && vscode.window.activeTextEditor === undefined) {
      console.log("MESSAGE Cut");
      currentlyActivePanel?.webview.postMessage({ command: 'cut' });
    }
    //add the overridden editor.action.clipboardCutAction back
    clipboardCutDisposable = vscode.commands.registerCommand('editor.action.clipboardCutAction', overriddenClipboardCutAction);


    //complains about globalConext beeing undefined, not needed? seems to work fine without
    //globalContext.subscriptions.push(clipboardPasteDisposable);
  });
}




