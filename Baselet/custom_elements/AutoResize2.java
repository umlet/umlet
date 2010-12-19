import java.awt.*;
import java.util.*;

import com.baselet.control.Constants;
import com.baselet.control.Utils;

@SuppressWarnings("serial")
public class <!CLASSNAME!> extends com.umlet.custom.CustomElement {

	public CustomElementImpl() {
		
	}
	
	@Override
	public void paint() {
		Vector<String> textlines = Utils.decomposeStrings(this.getPanelAttributes());
	
		/****CUSTOM_CODE START****/
//This is a tutorial for a self resizing component 
//In addition you are able to resize at manually
//As soon as it is resized manually a new text is
//added to the property panel
setAutoresize(20,20,10);

int y=textHeight();
//draws the text

boolean center = true;
for(int i = 0; i < textlines.size(); i++) {
	String textline = textlines.get(i);
	if(textline.equals("--")) {
		drawLineHorizontal((int) (y-textHeight()*0.7));
		center = false;
		if (!isManualResized()) height -= (int) (textHeight()*0.75);
		y += textHeight()*0.3;
	}
	else {
		if (center) printCenter(textline,y); 
		else printLeft(textline,y);
		y += textHeight();
	}
}

//draws the outer Rectangle
drawRectangle(0,0,onGrid(width),onGrid(height));
		/****CUSTOM_CODE END****/
	}
}