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
//  y = y + textHeight();
//to
//  y = y + 2 * textHeight();
//and observe the element preview.

int y=0;

drawRectangleRound(0,0,width,height,20,20);

for(String textline : textlines) {
	y = y + textHeight();
	printCenter(textline,y);
}
		/****CUSTOM_CODE END****/
	}
}