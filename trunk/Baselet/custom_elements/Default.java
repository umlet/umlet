import java.awt.*;
import java.util.*;

import com.baselet.control.Constants;
import com.baselet.control.Utils;

@SuppressWarnings("serial")
public class <!CLASSNAME!> extends com.umlet.custom.CustomElement {

	public CustomElementImpl()
	{
		
	}
	
	@Override
	public void paint() {
		Vector<String> textlines = Utils.decomposeStrings(this.getPanelAttributes());
	
		/****CUSTOM_CODE START****/
//Modify the code below to
//define the element's behavior.
//
//Example:  Change the line
//  y = y + textHeight();
//to
//  y = y + 2 * textHeight();
//and observe the element preview.

int y=textHeight();

drawRectangle(0,0,width,height);

for(String textline : textlines) {
	printCenter(textline,y);
	y = y + textHeight();
}
		/****CUSTOM_CODE END****/
	}
}