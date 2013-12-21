
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
BaseletElement is the basic project which is needed by both other projects
Baselet is the Swing Standalone and Eclipse Plugin project
BaseletGWT is the Web-Version which is based on GWT

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
You can use the "start" target from the ant-script "ant.xml" or you can use "Run as Application" -> "Main.java"

-----------------------

Question:
The ant-script "ant.xml" isn't working.

Answer:
The ant file is only working in Eclipse and only if it running in the same JRE as the workspace.
Therefore right click on the target to run and select "Run As" -> "External Tools Configurations"
Click on the "JRE" tab and select "Run in the same JRE as the workspace"
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

