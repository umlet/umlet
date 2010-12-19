import java.awt.*;
import java.util.*;

import com.baselet.control.Constants;
import com.baselet.control.Utils;

@SuppressWarnings("serial")
public class CustomElementImpl extends com.umlet.custom.CustomElement {

	public CustomElementImpl()
	{
		
	}
	
	@Override
	public void paint() {
		Vector<String> textlines = Utils.decomposeStrings(this.getPanelAttributes());
	
		/****CUSTOM_CODE START****/
//This is a self resizing custom element

//Disable manual resizing and enable autoresizing
allowResize(false);
setAutoresize(30,40,10);

//Draw the circle and the line
drawCircle(width/2,15,10);

//Print out the text
int y=30;
for(String textline : textlines) {
	y = y + textHeight();
	printCenter(textline,y);
}
		/****CUSTOM_CODE END****/
	}
}
