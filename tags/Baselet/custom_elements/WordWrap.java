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
//This is an element with activated wordwrap
//If a line would exceed the elements border, instead
//the exceeding part is written into the next line

int y=textHeight();

drawRectangle(0,0,width,height);
setWordWrap(true);

for(String textline : textlines) {
	y += printCenter(textline,y);
}
		/****CUSTOM_CODE END****/
	}
}
