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
//This is a tutorial for a self resizing component 
//In addition you are able to resize at manually
//As soon as it is resized manually a new text is
//added to the property panel

if(!isManualResized())
{
	height=5; //minimal height
	width=10; //minimal width
	//calculates the width and height of the component
	for(String textline : textlines) {
		height = height + textheight();
  	 width = max(width(textline)+10,width);
	}
}

//draws the outer Rectangle
drawRect(0,0,width,height);

int y=0;
//draws the text
for(String textline : textlines) {
	y = y + textheight();
	printCenter(textline,y);
}
		/****CUSTOM_CODE END****/
	}
}