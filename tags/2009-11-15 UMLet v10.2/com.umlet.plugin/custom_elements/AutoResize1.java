import java.awt.*;
import java.util.*;

import com.umlet.constants.Constants;

@SuppressWarnings("serial")
public class CustomElementImpl extends com.umlet.custom.CustomElement {

	public CustomElementImpl()
	{
		
	}
	
	@Override
	public void paint() {
		Vector<String> textlines = Constants.decomposeStrings(this.getState());
	
		/****CUSTOM_CODE START****/
//This is a self resizing custom element

//Disable manual resizing
allowResize(false);

height=40; //minimal height
width=10; //minimal width
//Calculate the width and height of the element
for(String textline : textlines) {
	height = height + textheight();
	width = max(textwidth(textline)+5,width); 
}

//Draw the circle and the line
drawCircle(width/2,15,10);

//set the element Location to centered
//the element will resize to left and right
//not only to right which is default
setElementCentered();

//Print out the text
int y=30;
for(String textline : textlines) {
	y = y + textheight();
	printCenter(textline,y);
}
		/****CUSTOM_CODE END****/
	}
}
