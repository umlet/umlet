package com.baselet.gwt.client.view.palettes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface Resources extends ClientBundle {

	Resources INSTANCE = GWT.create(Resources.class);

	@Source("UML Common Elements.uxf")
	TextResource UML_Common_Elements();

	@Source("Generic Colors.uxf")
	TextResource Generic_Colors();

	@Source("Generic Layers.uxf")
	TextResource Generic_Layers();

	@Source("Generic Text and Alignment.uxf")
	TextResource Generic_Text_and_Alignment();

	@Source("UML Activity.uxf")
	TextResource UML_Activity();

	@Source("UML Class.uxf")
	TextResource UML_Class();

	@Source("UML Composite Structure.uxf")
	TextResource UML_Composite_Structure();

	@Source("UML Package.uxf")
	TextResource UML_Package();

	@Source("UML Sequence.uxf")
	TextResource UML_Sequence();

	@Source("UML Sequence - All in one.uxf")
	TextResource UML_Sequence_All_in_one();

	@Source("UML State Machine.uxf")
	TextResource UML_State_Machine();

	@Source("UML Structure and Deployment.uxf")
	TextResource UML_Structure_and_Deployment();

	@Source("UML Use Case.uxf")
	TextResource UML_Use_Case();

	@Source("Plots.uxf")
	TextResource Plots();

	@Source("Custom Drawings.uxf")
	TextResource Custom_Drawings();

}
