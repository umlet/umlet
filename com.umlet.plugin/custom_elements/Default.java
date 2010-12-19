import java.awt.*;
import java.util.*;

import com.umlet.constants.Constants;

@SuppressWarnings("serial")
public class <!CLASSNAME!> extends com.umlet.custom.CustomElement {

	public CustomElementImpl()
	{
		
	}
	
	@Override
	public void paint() {
		Vector<String> textlines = Constants.decomposeStrings(this.getState());
	
		/****CUSTOM_CODE START****/
//Modify the code below to
//define the element's behavior.
//
//Example:  Change the line
//  y = y + textheight();
//to
//  y = y + 2 * textheight();
//and observe the element preview.

int y=0;

drawRect(0,0,width,height);

for(String textline : textlines) {
	y = y + textheight();
	printCenter(textline,y);
}
		/****CUSTOM_CODE END****/
	}
}