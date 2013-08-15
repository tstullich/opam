package com.tim.stullich.drawerapp;

/**
 * Wrapper class in order to facilitate JSON parsing
 * @author Tim
 *
 */
public class Target {
	String targetType;
	String displayName;
	Attribute[] basicAttributes;
	Attribute[] advancedAttributes;
	
	public class Attribute {
		String name;
		String type;
		String description;
		String label;
		boolean mask;
		boolean array;
		boolean required;
	}
}
