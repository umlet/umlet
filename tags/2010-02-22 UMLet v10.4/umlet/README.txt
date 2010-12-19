
Question:
How do I import Umlet into Eclipse?

Answer:
1.) Create a "Umlet" project folder in your Eclipse Workspace
2.) Copy "custom_elements", "html", "icons", "lib", "palettes" and "src/*" (=everything inside the "src" folder) in your project directory.
3.) Click "Import..." -> "Existing Projects into Workspace" and select the project folder

-----------------------

Question:
Some classes in the "com.umlet.plugin" package have errors.

Answer:
These classes are needed for the UMLet eclipse plugin. It seems that your Eclipse version has no Eclipse Plug-In development support.
To avoid these errors please download an Eclipse version with Eclipse Plug-In development support like
"Eclipse for RCP/Plug-in Developers" or "Eclipse Classic"

-----------------------

Question:
How do I start UMLet from Eclipse?

Answer:
You can use the "start" target from the ant-script "umlet.xml" or you can use "Run as Application" -> "Umlet - com.umlet.control"

-----------------------

Question:
The ant-script "umlet.xml" isn't working.

Answer:
The ant file is only working in Eclipse and only if it running in the same JRE as the workspace.
Therefore right click on the target to run and select "Run As" -> "External Tools Configurations"
Click on the "JRE" tab and select "Run in the same JRE as the workspace"