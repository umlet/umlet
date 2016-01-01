# UMLet
UMLet is an open-source UML tool with a simple user interface: draw UML diagrams fast, export diagrams to eps, pdf, jpg, svg, and clipboard, share diagrams using Eclipse, and create new, custom UML elements. 

* Please check out the [Wiki](https://github.com/umlet/umlet/wiki) for frequently asked questions

* Go to http://www.umlet.com to get the latest compiled versions

## Build Structure
* BaseletElements: the element implementations, including JavaCC generated compiler
* Baselet: the main part of the source code
* BaseletEclipse: eclipse plugin project. It is built with tycho. The maven build
makes sure everything is set up exactly as PDE/Tycho expect it.
* BaseletDeps: project referencing all dependencies of Baselet
* BaseletEclipseDeps: packaged transitive dependencies of BaseletDeps. Required to get the transitive dependencies.
* BaseletStandalone: standalone umlet program

Setup: checkout, run a `mvn clean install`, in Eclipse use `Import existing projects`