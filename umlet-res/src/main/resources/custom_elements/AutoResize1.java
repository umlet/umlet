import java.awt.*;
import java.util.*;

import com.baselet.control.constants.Constants;
import com.baselet.control.util.Utils;
import com.baselet.element.old.custom.CustomElement;

@SuppressWarnings("serial")
public class <!CLASSNAME!> extends CustomElement {

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
allowResize(false);
setAutoresize(30,40,10);

drawCircle(width/2,15,10);

int y=45;
for(String textline : textlines) {
	y += printCenter(textline,y);
}
		/****CUSTOM_CODE END****/
	}
}
