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

// this method is called when your extension is activated
// your extension is activated the very first time the command is executed
export function activate(context: vscode.ExtensionContext) {
	// Use the console to output diagnostic information (console.log) and errors (console.error)
  // This line of code will only be executed once when your extension is activated
  console.log("Umlet extension started");

  const previewAndCloseSrcDoc = async (document: TextDocument): Promise<void> => {
    //if uxf file is opened, close the editor and open a new one with the file loaded
    if (document.fileName.split('.').pop() === "uxf") {
      await vscode.commands.executeCommand("workbench.action.closeActiveEditor");
      await vscode.commands.executeCommand("extension.launchUmlet", document.getText().toString());
    }
  };

  const openedEvent = vscode.workspace.onDidOpenTextDocument((document: TextDocument) => {
    previewAndCloseSrcDoc(document);
  });
  
	// The command has been defined in the package.json file
	// Now provide the implementation of the command with registerCommand
  // The commandId parameter must match the command field in package.json
	let disposable = vscode.commands.registerCommand('extension.launchUmlet', (fileContents: string) => {
		// The code you place here will be executed every time your command is executed
		startUmletino(context, fileContents);

	});

	context.subscriptions.push(disposable);
}

function startUmletino(context: vscode.ExtensionContext, fileContents: string) {

  const panel = vscode.window.createWebviewPanel('umletino', 'Umletino', vscode.ViewColumn.One, {
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
  
  if (fileContents === undefined)
  {
    panel.webview.html = GetUmletWebviewPage(localUmletFolder.toString(), 'undefined'); //TODO not working, loads uninteractable umletino without borders
  } else {
    panel.webview.html = GetUmletWebviewPage(localUmletFolder.toString(), fileContents.toString());
  }
  }

  





//shows popup savefile dialog for uxf files
function SaveFile(fileContent: string)
{
  vscode.window.showSaveDialog({
    filters:{
      'UML Diagram': ['uxf']
    } 
  })
  .then(fileInfos => {
    if(fileInfos !== undefined)
    {
      fs.writeFile(fileInfos.fsPath, fileContent, function (err) {
      if (err) {return console.log(err);}
  });
    }
  });
}

//shows popup savefile dialog for png files
function SaveFileDecode(fileContent: string)
{
  vscode.window.showSaveDialog({
    filters:{
      'Image': ['png']
    } 
  })
  .then(fileInfos => {
    if(fileInfos !== undefined)
    {
      fs.writeFile(fileInfos.fsPath, fileContent, {encoding: 'base64'}, function (err) {
      if (err) {return console.log(err);}
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
function GetUmletWebviewPage(localUmletFolder: string, diagramData:string)
{
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
            var themeFromClass = getTheme(document.body.className);
            window.changeTheme(themeFromClass);
            switchBodyColor(themeFromClass);
        });    
      });
      
      var target = document.body;
      observer.observe(target, { attributes : true, attributeFilter : ['class'] });

      // Retrieving current theme
      var theme = 'LIGHT';
      theme = getTheme(document.body.className);
      switchBodyColor(theme);

      var vscode = acquireVsCodeApi();
      var vsCodeInitialDiagramData = \`${diagramData}\`;
    </script>

  </html>`;
}

// this method is called when your extension is deactivated
export function deactivate() {}


