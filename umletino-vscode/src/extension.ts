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
//import path = require('path');

// this method is called when your extension is activated
// your extension is activated the very first time the command is executed
export function activate(context: vscode.ExtensionContext) {
  console.log("extension started");
	// Use the console to output diagnostic information (console.log) and errors (console.error)
	// This line of code will only be executed once when your extension is activated
	console.log('Congratulations, your extension "MFTHE" is now active!');

	// The command has been defined in the package.json file
	// Now provide the implementation of the command with registerCommand
  // The commandId parameter must match the command field in package.json
  const previewAndCloseSrcDoc = async (document: TextDocument): Promise<void> => {
    console.log("filename is:" + document.fileName);
    if (document.fileName.split('.').pop() === "uxf") {
      await vscode.commands.executeCommand("workbench.action.closeActiveEditor");
      await vscode.commands.executeCommand("extension.helloWorld", document.getText().toString());
    }
  };

  const openedEvent = vscode.workspace.onDidOpenTextDocument((document: TextDocument) => {
    previewAndCloseSrcDoc(document);
  });
	let disposable = vscode.commands.registerCommand('extension.helloWorld', (fileContents: string) => {
		// The code you place here will be executed every time your command is executed
		startUmletino(context, fileContents);

	});

	context.subscriptions.push(disposable);
}

function startUmletino(context: vscode.ExtensionContext, fileContents: string) {
  console.log("Trying to display xml data of file: " + fileContents);

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
        //todo
        console.log("implement actually exporting the png, but url is:" + message.text);
        var actual_data = message.text.replace("data:image/png;base64,", "");
        console.log("actual data:" + actual_data);
        SaveFileDecode(actual_data);
        console.log("FINISHED");
        return;
    }
  }, undefined, context.subscriptions);
  //GetCacheOneJavascript(context.extensionPath);
  /*
  SaveFile(`<diagram program="umletino" version="14.3.0"><zoom_level>10</zoom_level><element><id>UMLClass</id><coordinates><x>410</x><y>240</y><w>210</w><h>70</h></coordinates><panel_attributes>_object: Class_
  --
  id: Long="1234"
  [first umlet]</panel_attributes><additional_attributes></additional_attributes></element><element><id>Relation</id><coordinates><x>500</x><y>300</y><w>170</w><h>160</h></coordinates><panel_attributes>lt=&lt;.
  &lt;&lt;instanceOf&gt;&gt;</panel_attributes><additional_attributes>140;140;10;10</additional_attributes></element><element><id>UMLUseCase</id><coordinates><x>590</x><y>440</y><w>120</w><h>80</h></coordinates><panel_attributes>Use case 1</panel_attributes><additional_attributes></additional_attributes></element></diagram>`);
  */
  //convert local folder to a vscode readable uri
  // Get path to resource on disk
  const onDiskPath = vscode.Uri.file(path.join(context.extensionPath, 'src', 'umlet-gwt'));
  // And get the special URI to use with the webview
  const localUmletFolder = panel.webview.asWebviewUri(onDiskPath);
  console.log('the global source is:' + localUmletFolder);
  /*
  const filePath: vscode.Uri = vscode.Uri.file(path.join(context.extensionPath, 'src', 'html', 'file.html'));
  //const filePath: vscode.Uri = vscode.Uri.file(path.join(context.extensionPath, 'src', 'umlet-gwt', 'index.html'));
  console.log ('the html is: ' + fs.readFileSync(filePath.fsPath, 'utf8'));
  panel.webview.html = fs.readFileSync(filePath.fsPath, 'utf8'); */
  console.log('globaluri as string is:' + localUmletFolder.toString());
  console.log('try to prepr file:' + path.join(context.extensionPath, 'helloworld.txt'));
  fs.writeFile(path.join(context.extensionPath, 'helloworld.txt'), 'Hello World!', function (err) {
    if (err) {
      return console.log(err);
    }
    console.log('Hello World > helloworld.txt');
  });
  console.log('try to save');
  console.log('save done');
  panel.webview.html = GetUmletWebviewPage(localUmletFolder.toString(), fileContents.toString());
}






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
      console.log('my path:' + fileInfos.fsPath);
      fs.writeFile(fileInfos.fsPath, fileContent, function (err) {
      if (err) {return console.log(err);}
      console.log('Hello World > helloworld.txt');
  });
  console.log('try to save');
    }
  });
}

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
      console.log('my path:' + fileInfos.fsPath);
      fs.writeFile(fileInfos.fsPath, fileContent, {encoding: 'base64'}, function (err) {
      if (err) {return console.log(err);}
      console.log('Hello World > helloworld.txt');
  });
  console.log('try to save');
    }
  });
}

function GetCacheOneJavascript(extensionPath: string)
{
  //change the dom created by javascript
  var cacheOnePath = path.join(extensionPath, 'src', 'umlet-gwt', 'baseletgwt', 'deferredjs', '274F3D0FF78DA14FE85FDE4ABAB0B8E5', '1.cache.js');
  console.log("trying to read from:" + cacheOnePath);
  
  var data;
  try {
    data = fs.readFileSync(cacheOnePath, 'utf8');
  } catch (err) {
    console.error(err);
  }

  //find and replace the wanted tag
  let pathReplacementTag = '#TESTER_FOR_LATER_REPLACEMENT_WITH_USER_DIRECTORY#';
  let positionInText: number | undefined = data?.indexOf(pathReplacementTag);
  console.log("found text in :" + positionInText );
  if (positionInText !== undefined && data !== undefined)
  {
    data = data?.replace(pathReplacementTag, extensionPath);
  
    console.log(data.substring(positionInText-10, 1000));
  
    console.log("finished reading data");
  }
  
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
            return theme = 'DARK';
        }
      }

      var theme = 'light';
      theme = getTheme();
      var vscode = acquireVsCodeApi();
      var vsCodeInitialDiagramData = \`${diagramData}\`;
    </script>

  </html>`;
}

/**
  * 
  * Gets a modified version of the initial starting page of the GWT umletino version without loading an initial document
  * @param localUmletFolder folder which holds the local umletino gwt version.
  */
 function GetUmletWebviewPageNoPreload(localUmletFolder: string)
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
       var vscode = acquireVsCodeApi();
     </script>
 
   </html>`;
 }

// this method is called when your extension is deactivated
export function deactivate() {}


