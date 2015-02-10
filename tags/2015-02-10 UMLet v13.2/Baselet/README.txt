
Question:
Where do I get the sourcecode?

Answer:
We use a SVN Repository for development which can be found at: http://code.google.com/p/umlet/source/
There you can check out the latest development snapshot in "trunk" (although we try to only check in working code, we cannot guarantee it)
Also we create a tag for every released version which can be found in the "tags" subdirectory.

All of these projects are working Eclipse Projects. Currently we do not support other IDEs or use maven.

-----------------------

Question:
Why is there more than one project?

Answer:
Umlet is available as Swing Standalone application, Eclipse Plugin and GWT Application.
Baselet is the Swing Standalone and Eclipse Plugin project
BaseletGWT is the GWT based Web-Version
BaseletElement is the project containing shared files

-----------------------

Question:
Some classes in the "com.baselet.plugin" package have errors.

Answer:
The classes in this package represent the sourcecode of the eclipse plugin version of the program.
It seems that your Eclipse version has no Eclipse Plug-In development support.
To avoid these errors please download an Eclipse version with Eclipse Plug-In development support like
"Eclipse for RCP/Plug-in Developers" or "Eclipse Classic"

-----------------------

Question:
How do I start the program from Eclipse?

Answer:
Swing:			Run as Application -> Java Application. Then select Main.java (or use the "start" target from the ant-script "ant.xml")
Eclipse Plugin:	Run As -> Eclipse Application
GWT:			Run As -> Web Application

-----------------------

Question:
The ant-script "ant.xml" shows the following error: "ant.xml:69: Problem: failed to create task or type eclipse.refreshLocal"

Answer:
The ant file only works in Eclipse and only if it running in the same JRE as the workspace.
Therefore right click on the target to run and select "Run As" -> "External Tools Configurations"
Click on the "JRE" tab and select "Run in the same JRE as the workspace"

-----------------------

Question:
BaseletGWT has BaseletElements as Project Reference, but Baselet links the sources instead. Why?

Answer:
As described above Baselet is a mixed project for standalone and plugin version of Umlet.
The standalone part and the ant-exports work using a project reference, but when using "Run As -> Eclipse Application" (e.g. for debugging code) it will fail due to missing BaseletElement classes.
The reason is plugin development uses its own dependency handling which ignores project dependencies, but uses linked sources.

-----------------------

Question:
Do I need a JDK or can I compile UMLet using a JRE?

Answer:
The JRE is enough to run the compiled UMLet, but for development and for starting UMLet using Eclipse launchers you should use JDK

-----------------------

Question:
How do you create exe files?

Answer:
Executables are created using http://launch4j.sourceforge.net
Download Launch4J, import the launch4j_project.xml file and you can create updated versions of the executable

