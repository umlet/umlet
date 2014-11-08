package com.baselet.control.config;

import com.umlet.language.enums.FieldOptions;
import com.umlet.language.enums.MethodOptions;
import com.umlet.language.enums.SignatureOptions;
import com.umlet.language.enums.SortOptions;

public class ConfigClassGen {
	private static final ConfigClassGen instance = new ConfigClassGen();

	public static ConfigClassGen getInstance() {
		return instance;
	}

	private ConfigClassGen() {}

	private boolean generateClassPackage = true;
	private FieldOptions generateClassFields = FieldOptions.ALL;
	private MethodOptions generateClassMethods = MethodOptions.ALL;
	private SignatureOptions generateClassSignatures = SignatureOptions.ALL;
	private SortOptions generateClassSortings = SortOptions.HEIGHT;

	public boolean isGenerateClassPackage() {
		return generateClassPackage;
	}

	public void setGenerateClassPackage(boolean generateClassPackage) {
		this.generateClassPackage = generateClassPackage;
	}

	public FieldOptions getGenerateClassFields() {
		return generateClassFields;
	}

	public void setGenerateClassFields(FieldOptions generateClassFields) {
		this.generateClassFields = generateClassFields;
	}

	public MethodOptions getGenerateClassMethods() {
		return generateClassMethods;
	}

	public void setGenerateClassMethods(MethodOptions generateClassMethods) {
		this.generateClassMethods = generateClassMethods;
	}

	public SignatureOptions getGenerateClassSignatures() {
		return generateClassSignatures;
	}

	public void setGenerateClassSignatures(SignatureOptions generateClassSignatures) {
		this.generateClassSignatures = generateClassSignatures;
	}

	public SortOptions getGenerateClassSortings() {
		return generateClassSortings;
	}

	public void setGenerateClassSortings(SortOptions generateClassSortings) {
		this.generateClassSortings = generateClassSortings;
	}

}
