"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
// The module 'vscode' contains the VS Code extensibility API
// Import the module and reference it with the alias vscode in your code below
const vscode = require("vscode");
const path = require("path");
const fs = require("fs");
exports.currentlyActivePanel = null;
let lastChangeTriggeredByUri = ""; //whenever a document change is triggered by an instance of the UMLet Gwt application, the uri of the corresponding document will be tracked here.
class UmletEditorProvider {
    constructor(context) {
        this.context = context;
        this.outputChannel = vscode.window.createOutputChannel('UMLet');
    }
    /*
    Overrides multiple vscode commands like copy and paste so they can be intercepted
  
    Select all for webviews is also intercepted so it can be disabled in umlet since it would select the property panel
    */
    static overrideVsCodeCommands(context) {
        console.log("Overriding commands....");
        //COPY
        //override the editor.action.clipboardCopyAction with our own
        var clipboardCopyDisposable = vscode.commands.registerCommand('editor.action.clipboardCopyAction', overriddenClipboardCopyAction);
        context.subscriptions.push(clipboardCopyDisposable);
        /*
         * Function that overrides the default copy behavior. We get the selection and use it, dispose of this registered
         * command (returning to the default editor.action.clipboardCopyAction), invoke the default one, and then re-register it after the default completes
         */
        function overriddenClipboardCopyAction(textEditor, edit, params) {
            //debug
            //Write to output.
            console.log("Copy registered");
            //dispose of the overridden editor.action.clipboardCopyAction- back to default copy behavior
            clipboardCopyDisposable.dispose();
            //execute the default editor.action.clipboardCopyAction to copy
            vscode.commands.executeCommand("editor.action.clipboardCopyAction").then(function () {
                console.log("After Copy");
                if (exports.currentlyActivePanel !== null) {
                    exports.currentlyActivePanel === null || exports.currentlyActivePanel === void 0 ? void 0 : exports.currentlyActivePanel.webview.postMessage({ command: 'copy' });
                }
                //add the overridden editor.action.clipboardCopyAction back
                clipboardCopyDisposable = vscode.commands.registerCommand('editor.action.clipboardCopyAction', overriddenClipboardCopyAction);
                context.subscriptions.push(clipboardCopyDisposable);
            });
        }
        //PASTE
        //override the editor.action.clipboardPasteAction with our own
        var clipboardPasteDisposable = vscode.commands.registerCommand('editor.action.clipboardPasteAction', overriddenClipboardPasteAction);
        context.subscriptions.push(clipboardPasteDisposable);
        /*
         * Function that overrides the default paste behavior. We get the selection and use it, dispose of this registered
         * command (returning to the default editor.action.clipboardPasteAction), invoke the default one, and then re-register it after the default completes
         */
        function overriddenClipboardPasteAction(textEditor, edit, params) {
            //debug
            //Write to output.
            console.log("Paste registered");
            //dispose of the overridden editor.action.clipboardPasteAction- back to default paste behavior
            clipboardPasteDisposable.dispose();
            //execute the default editor.action.clipboardPasteAction to paste
            vscode.commands.executeCommand("editor.action.clipboardPasteAction").then(function () {
                console.log("After Paste");
                if (exports.currentlyActivePanel !== null) {
                    vscode.env.clipboard.readText().then((text) => {
                        let clipboard_content = text;
                        console.log("MESSAGE Paste, content is:" + clipboard_content);
                        exports.currentlyActivePanel === null || exports.currentlyActivePanel === void 0 ? void 0 : exports.currentlyActivePanel.webview.postMessage({
                            command: 'paste',
                            text: clipboard_content
                        });
                    });
                }
                //add the overridden editor.action.clipboardPasteAction back
                clipboardPasteDisposable = vscode.commands.registerCommand('editor.action.clipboardPasteAction', overriddenClipboardPasteAction);
                context.subscriptions.push(clipboardPasteDisposable);
            });
        }
        //CUT
        //override the editor.action.clipboardCutDisposable with our own
        var clipboardCutDisposable = vscode.commands.registerCommand('editor.action.clipboardCutAction', overriddenClipboardCutAction);
        context.subscriptions.push(clipboardCutDisposable);
        /*
         * Function that overrides the default paste behavior. We get the selection and use it, dispose of this registered
         * command (returning to the default editor.action.clipboardPasteAction), invoke the default one, and then re-register it after the default completes
         */
        function overriddenClipboardCutAction(textEditor, edit, params) {
            //debug
            //Write to output.
            console.log("Cut registered");
            //dispose of the overridden editor.action.clipboardCutAction- back to default cut behavior
            clipboardCutDisposable.dispose();
            //execute the default editor.action.clipboardCutAction to cut
            vscode.commands.executeCommand("editor.action.clipboardCutAction").then(function () {
                console.log("After Cut");
                if (exports.currentlyActivePanel !== null) {
                    console.log("MESSAGE Cut");
                    exports.currentlyActivePanel === null || exports.currentlyActivePanel === void 0 ? void 0 : exports.currentlyActivePanel.webview.postMessage({ command: 'cut' });
                }
                //add the overridden editor.action.clipboardCutAction back
                clipboardCutDisposable = vscode.commands.registerCommand('editor.action.clipboardCutAction', overriddenClipboardCutAction);
                context.subscriptions.push(clipboardCutDisposable);
            });
        }
        //SELECT ALL
        //overwrite so that strg+a does not select any text in vs code
        var selectAllDisposable = vscode.commands.registerCommand('editor.action.webvieweditor.selectAll', overriddenSelectAllAction);
        context.subscriptions.push(selectAllDisposable);
        /*
         * Function that overrides the default select all behavior. We get the selection and use it, dispose of this registered
         * command (returning to the default editor.action.webvieweditor.selectAll), invoke the default one, and then re-register it after the default completes
         */
        function overriddenSelectAllAction(textEditor, edit, params) {
            //dispose of the overridden editor.action.clipboardCutAction- back to default cut behavior
            selectAllDisposable.dispose();
            //execute the default editor.action.webvieweditor.selectAll to cut
            if (!exports.currentlyActivePanel) {
                vscode.commands.executeCommand("editor.action.webvieweditor.selectAll");
            }
            //add the overridden editor.action.webvieweditor.selectAll back
            selectAllDisposable = vscode.commands.registerCommand('editor.action.webvieweditor.selectAll', overriddenSelectAllAction);
            //set the subscription again so vscode can clean it up properly
            context.subscriptions.push(selectAllDisposable);
        }
    }
    consoleLog(params) {
        var channel = vscode.window.createOutputChannel('myoutputchannel');
        channel.appendLine('new clip pushg');
        channel.show();
    }
    /**
     * Called when our custom editor is opened.
     *
     */
    resolveCustomTextEditor(document, webviewPanel, token) {
        //whenever the .uxf file is changed (for example throough a text editor in vs code), these changes shoule be reflected in umlet
        const changeDocumentSubscription = vscode.workspace.onDidChangeTextDocument(e => {
            //console.log("text document changed!, last change  uri:" + lastChangeTriggeredByUri + "       " + e.document.uri.toString() + "     " + document.uri.toString());
            //everytime something is changed by the gwt application, lastChangeTriggeredByUri will be set to the according document uri. 
            //this is used to avoid a reset when the last change came directly from the gwt application, which would de-select current elements
            //currentlyActivePanel has to be checked, otherwise this will only be triggered for the first opened editor. we only want it on the active
            if (lastChangeTriggeredByUri === document.uri.toString() && webviewPanel === exports.currentlyActivePanel) {
                console.log("last change came from the gwt application" + e.contentChanges.length);
                lastChangeTriggeredByUri = "";
            }
            else {
                //only trigger for right document
                //if e.contentChanges.length === 0, then there was no actual content change, but the grey dirty indicator was set by vs code
                //in that case we do not want to set gwt again, because that would unselect all selected elements
                if (e.document.uri.toString() === document.uri.toString() && e.contentChanges.length !== 0) {
                    console.log("match text change, injecting changes to gwt ");
                    console.log('webview panel is: ' + webviewPanel);
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
        // Handle messages from the webview
        webviewPanel.webview.onDidReceiveMessage(message => {
            switch (message.command) {
                case 'exportUxf':
                    this.SaveFile(message.text);
                    return;
                case 'updateFiledataUxf':
                    //console.log("uri in log is now: +" + document.uri.toString());
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
                        exports.currentlyActivePanel === null || exports.currentlyActivePanel === void 0 ? void 0 : exports.currentlyActivePanel.webview.postMessage({
                            command: 'paste-response',
                            text: clipboard_content
                        });
                    });
                    return;
                case 'consoleLog':
                    this.postLog(message.text);
                    return;
                case 'onFocus':
                    exports.currentlyActivePanel = webviewPanel;
                    return;
                case 'onBlur':
                    exports.currentlyActivePanel = null;
                    return;
            }
        }, undefined, this.context.subscriptions);
        // Get path to resource on disk
        const onDiskPath = vscode.Uri.file(path.join(this.context.extensionPath, 'target', 'umlet-vscode-14.4.0-SNAPSHOT'));
        // And get the special URI to use with the webview
        const localUmletFolder = webviewPanel.webview.asWebviewUri(onDiskPath);
        let fileContents = document.getText().toString();
        webviewPanel.webview.html = this.getUmletWebviewPage(localUmletFolder.toString(), fileContents.toString());
        /*
        webviewPanel.webview.options = {
          enableScripts: true,
          localResourceRoots: [vscode.Uri.file(path.join(this.context.extensionPath, 'target', 'umlet-vscode-14.4.0-SNAPSHOT'))]
        };
        webviewPanel.webview.html = `<!DOCTYPE html>
        <html>
          <head>
            <meta charset="utf-8">
          </head>
          <body>
            <h1>Test/h1>
            <canvas id="maincanvas" tabindex="0" width=200 height=200>
            </canvas>
            <textarea rows="4" cols="50">
              Bisschen Text
              </textarea>
            <script>
              var canv = document.getElementById("maincanvas");
              var ctx = canv.getContext("2d");
              ctx.moveTo(0, 0);
              ctx.lineTo(200, 100);
              ctx.stroke();
              canv.addEventListener("focus", function(){console.log("focus")}, false);
              canv.addEventListener("blur", function(){console.log("blur")}, false);
              canv.addEventListener("keydown", function(e) {console.log("key:"+e.keyCode+" "+e.metaKey)}, false);
            </script>
          </body>
        </html>`;
        */
    }
    //gets the updated filedata from the webview if anything has changed
    UpdateCurrentFile(fileContent, document) {
        lastChangeTriggeredByUri = document.uri.toString(); //used to avoid ressetting the webview if a change was triggered by the webview anyway
        console.log("lastChangeTriggeredByUri set to: " + lastChangeTriggeredByUri);
        const edit = new vscode.WorkspaceEdit();
        edit.replace(document.uri, new vscode.Range(0, 0, document.lineCount, 0), fileContent);
        return vscode.workspace.applyEdit(edit);
    }
    //shows popup savefile dialog for uxf files
    SaveFile(fileContent) {
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
    SaveFileDecode(fileContent) {
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
    postLog(message) {
        this.outputChannel.appendLine(message);
    }
    /**
      *
      * Gets a modified version of the initial starting page of the GWT umletino version
      * @param localUmletFolder folder which holds the local umletino gwt version.
      * @param diagramData XML data of a diagram which should be loaded on start
      */
    getUmletWebviewPage(localUmletFolder, diagramData) {
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

    <input type="text" id="copypaste" style="opacity: 0"/>
      <!-- the following line is necessary for history support -->
      <iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1' style="position:absolute;width:0;height:0;border:0"></iframe>
      
      <!-- the website will not work without JavaScript -->
      <noscript>
        <div style="width: 25em; position: absolute; left: 50%; margin-left: -11em; background-color: white; border: 1px solid red; padding: 4px; font-family: sans-serif">
          You must enable JavaScript to use this web application.
      </div>
      </noscript>
      <div align="left" id="featurewarning" style="color: red; font-family: sans-serif; font-weight:bold; font-size:1.2em"></div>
      <script>
      //track focus/blur
      window.addEventListener("focus", function(){consoleLogVsCode("focus TT"); notifyFocusVsCode();}, false);
      window.addEventListener("blur", function(){consoleLogVsCode("blur TT"); notifyBlurVsCode();}, false);
      </script>
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

      function consoleLogVsCode(consoleMessage) {
        vscode.postMessage({
          command: 'consoleLog',
          text: consoleMessage
        })
      }

      function notifyFocusVsCode() {
        vscode.postMessage({
          command: 'onFocus'
        })
      }

      function notifyBlurVsCode() {
        vscode.postMessage({
          command: 'onBlur'
        })
      }
      
    </script>

  </html>`;
    }
}
exports.UmletEditorProvider = UmletEditorProvider;
UmletEditorProvider.viewType = 'uxfCustoms.umletEditor';
//# sourceMappingURL=CustomTextEditorProvider.js.map