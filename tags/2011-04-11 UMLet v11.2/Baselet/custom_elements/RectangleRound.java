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
int y=textHeight();

drawRectangleRound(0,0,width,height,20,20);

for(String textline : textlines) {
	y += printCenter(textline,y);
}
		/****CUSTOM_CODE END****/
	}
}