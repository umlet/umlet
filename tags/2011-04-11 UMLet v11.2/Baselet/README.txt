
Question:
How do I import the sourcefiles into Eclipse?

Answer:
1.) Create a new folder in your Eclipse Workspace.
2.) Copy the content of "sourcefiles" into your project directory. Also copy the "lib" folder into your project directory.
3.) Click "Import..." -> "Existing Projects into Workspace" and select the project folder

-----------------------

Question:
What are all those variable names like "project.name.uc"?

Answer:
UMLet and Plotlet use a shared program base called Baselet. We are using those variables to avoid manual adjustment of many files before every release.

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