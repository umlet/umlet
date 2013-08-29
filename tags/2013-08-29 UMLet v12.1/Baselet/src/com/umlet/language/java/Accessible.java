package com.umlet.language.java;

public interface Accessible {
	
	public enum AccessFlag { 
		
		PRIVATE {
			@Override public String toString() { return "-"; }
		},
		PROTECTED {
			@Override public String toString() { return "#"; }
		}, 
		PACKAGE {
			@Override public String toString() { return "~"; }
		}, 
		PUBLIC {
			@Override public String toString() { return "+"; }
		};
	}

	public AccessFlag getAccess();
}
