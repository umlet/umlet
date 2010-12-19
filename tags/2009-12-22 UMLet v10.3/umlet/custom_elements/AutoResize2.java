import java.awt.*;
import java.util.*;

import com.umlet.constants.Constants;

@SuppressWarnings("serial")
public class <!CLASSNAME!> extends com.umlet.custom.CustomElement {

	public CustomElementImpl() {
		
	}
	
	@Override
	public void paint() {
		Vector<String> textlines = Constants.decomposeStrings(this.getState());
	
		/****CUSTOM_CODE START****/
//This is a tutorial for a self resizing component 
//In addition you are able to resize at manually
//As soon as it is resized manually a new text is
//added to the property panel
setAutoresize(20,20,10);

int y=0;
//draws the text

boolean center = true;
for(int i = 0; i < textlines.size(); i++) {
	String textline = textlines.get(i);
	if(textline.equals("--")) {
		y += 10;
		if (!isManualResized()) height -= (int) (textHeight()*0.45);
		drawLineHorizontal(y-2);
	center = false;
   }
   else {
		y = y + textHeight();
		if (center) printCenter(textline,y); 
	else printLeft(textline,y); 
   }
}

//draws the outer Rectangle
drawRectangle(0,0,onGrid(width),onGrid(height));
		/****CUSTOM_CODE END****/
	}
}