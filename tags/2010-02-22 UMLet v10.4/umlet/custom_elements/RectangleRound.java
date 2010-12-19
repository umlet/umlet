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
int y=textHeight();

drawRectangleRound(0,0,width,height,20,20);

for(String textline : textlines) {
	printCenter(textline,y);
	y = y + textHeight();
}
		/****CUSTOM_CODE END****/
	}
}