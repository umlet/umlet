package com.baselet.gwt.client.view;
/*
	This class is responsible for providing methods to find out in which "host" the application is currently in (In VsCode or in the Web version currently)
	Furthermore this class provides VSCode specific methods (getting the data of a file the application was launched with  etc.)
 */
public class VersionChecker {
	public static enum Version {
		WEB, VSCODE
	}

	public static Version GetVersion() {
		if (isVsCodeVersion()) {
			return Version.VSCODE;
		}
		return Version.WEB;

	}

	public static native boolean isVsCodeVersion() /*-{
		if (typeof window.parent.vscode !== 'undefined') {
			return true;
		}
		return false;
	}-*/;

	//used for debugging only
	public static String mockPredifinedFile()
	{
		return "<diagram program=\"umletino\" version=\"14.4.0-SNAPSHOT\"><zoom_level>10</zoom_level><element><id>UMLClass</id><coordinates><x>230</x><y>280</y><w>210</w><h>70</h></coordinates><panel_attributes>_object: Class_\n" +
				"--\n" +
				"id: Long=\"36548\"\n" +
				"[waiting for message]</panel_attributes><additional_attributes></additional_attributes></element><element><id>UMLClass</id><coordinates><x>530</x><y>360</y><w>210</w><h>70</h></coordinates><panel_attributes>_object: Class_\n" +
				"--\n" +
				"id: Long=\"36548\"\n" +
				"[waiting for message]</panel_attributes><additional_attributes></additional_attributes></element><element><id>Relation</id><coordinates><x>430</x><y>300</y><w>120</w><h>110</h></coordinates><panel_attributes>lt=&lt;.\n" +
				"&lt;&lt;instanceOf&gt;&gt;</panel_attributes><additional_attributes>100;90;10;10</additional_attributes></element></diagram>";
	}
}
