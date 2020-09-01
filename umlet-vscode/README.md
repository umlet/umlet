# **UMLet -- Free UML Tool for Fast UML Diagrams**

[umlet.com](https://www.umlet.com/) -- [info@umlet.com](mailto:info@umlet.com) -- [patreon.com/umlet](https://www.patreon.com/umlet)

<br/>
<br/>

## >> For a new diagram, create an empty text file with a ".uxf"-extension! <<

Then you can 
* double click on a UML element in the palette;
* edit it in the lower-right markdown panel; or
* drag the background to move the whole diagram.

You're ready to go -- enjoy!

<br/>
<br/>


![A gif demonstrating UMLet](https://s7.gifyu.com/images/800px-speed-up.gif)

<br/>
<br/>

# Introduction

UMLet is a free, open-source UML tool with a simple user interface: draw UML diagrams fast, build sequence and activity diagrams from plain text, export diagrams to eps, pdf, jpg, svg, and clipboard, and create new custom UML elements.

It runs 
- as [stand-alone UMLet](https://www.umlet.com/) on Windows, macOS, and Linux;
- as [Eclipse plug-in](https://marketplace.eclipse.org/content/umlet-uml-tool-fast-uml-diagrams);
- as Web-based app [UMLetino](https://www.umletino.com/); and
- as [**VS Code extension**](https://marketplace.visualstudio.com/items?itemName=TheUMLetTeam.umlet).

The stand-alone version has the most extensive feature set; here is a brief [tutorial video](https://www.youtube.com/watch?v=3UHZedDtr28). 

<br/>
<br/>

# Concepts

Early UML tools were often a bit cumbersome -- they relied on pop-up windows to set attributes, and aimed at model consistency up to an ever-allusive round-trip engineering.

UMLet main goal is to allow users to sketch UML (and other) diagrams fast.

* It uses a pop-up-free, markdown-based way of quickly editing elements. Even the direction or type of a relation is changed with just a few keystrokes.

* It lets users learn about UML elements of various complexity by providing palettes of many element variants as templates or prototypes. Just double-click on any element and tweak the clone. You can even modify the palettes as if you'd edit a normal diagram -- no more icon guessing.

* It supports more complex element types like activity or sequence diagrams, with their own tailored markdown dialect. With more generic, non-UML elements, you can even draw any kind of "boxy" diagram usually done in Word or PowerPoint.

* It allows users to create their own custom elements. An element's look can be modified at run-time by changing a few lines of Java code; UMLet then compiles the new element's code on the fly and displays it. Without leaving UMLet, users can thus create and add new graphical element types. (Stand-alone only, for now.)

* It provides simple batch processing on the command line. You can thus convert the uxf-format (an XML dialect) to various file formats, e.g., for you LaTeX workflow.

<br/>
<br/>

# Links

* Play with our codebase, or create tickets on [Github](https://github.com/umlet/umlet).

* Find more examples in our [screenshots](https://www.umlet.com/screenshots.htm) and [sample diagrams](http://www.itmeyer.at/umlet/uml2/).

* Read about UMLet's underlying ideas in our [papers](https://scholar.google.com/scholar?hl=en&as_sdt=0%2C5&q=umlet+uml+martin+auer&btnG=).

<br/>
<br/>

# *Hmm.., You a VS Code Extension Guru?*

*One technical issue still niggles us: handling copy/paste commands if triggerend via the menu on Windows, as there the focus is lost. We also use a "custom visual editor" -- those are just a bit problematic as activeTextEditor becomes null if another custom editor gets activated.. If you have any idea to better our current heuristic, let us know (and claim our bottle of Italian red)!*

<br/>
<br/>

# Your Support

We highly appreciate your support!

* Please send feedback and bug reports to [info@umlet.com](mailto:info@umlet.com), or create a ticket on [Github](https://github.com/umlet/umlet). Especially if you teach UML, let us know which element types you're still missing.

* Follow [@twumlet](https://twitter.com/twumlet) on Twitter, or visit us on [Facebook](https://www.facebook.com/UML.tool.UMLet).

* If you like UMLet, please find the time to [rate it](https://marketplace.visualstudio.com/items?itemName=TheUMLetTeam.umlet&ssr=false#review-details) -- we'd really enjoy that!

* (Finally, \*cough\*, we're also on [PayPal](https://www.paypal.com/donate?token=qqvXeoMHEtyT1MmjDW6qO-7N0-12zVqK8fq1L8QUCx2wqGAGYJRTq0ajEaUklM066SgEBxA-Oo_gq6vz) and [Patreon](https://www.patreon.com/umlet).)

<br/>
<br/>
<br/>
<br/>

*To our past contributors (chronologically) Thomas Tschurtschenthaler, Ludwig Meyer, Johannes Pölz, Elisabeth Blümelhuber, Julian Thöndel, and Thomas Bretterbauer: thanks! The UMLet Team -- Martin Auer, Andreas Fürnweger*

[umlet.com](https://www.umlet.com/) -- [info@umlet.com](mailto:info@umlet.com) -- [patreon.com/umlet](https://www.patreon.com/umlet)