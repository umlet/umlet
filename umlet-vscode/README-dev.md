# UMLet-VSCode

This project contains our UMLet Visual Studio Code extension!

## Architecture

The extension uses _umlet-gwt_ as library and extends it by specific functionality required by the VSCode environment.

![Architecture overview](./images/architecture.png)

In general, _umlet-gwt_ handles most of the functionality required for UMLet to work. For special cases, like clipboard
handling, a custom implementation especially for VSCode has to be developed. This is done by replacing an interface or 
abstract class by an actual implementation of the functionality. The use of _umlet-gwt_ and all replacements are
 declared in _module.gwt.xml_.

To run the extension, a [Custom Editor](https://code.visualstudio.com/api/extension-guides/custom-editors) is created
which handles the setup and communication between UMLet and VSCode. You can find the code inside
_UmletEditorProvider.ts_.

The communication between VSCode and UMLet relies on 
[_message passing_](https://code.visualstudio.com/api/extension-guides/webview#scripts-and-message-passing). Since this 
communication is asynchronously, the architecture of a functionality has to be planned accordingly. An example workflow
of pasting the clipboard using the context menu of UMLet is shown in the following.

![Architecture overview](./images/example-paste.png)