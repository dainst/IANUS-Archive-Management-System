package de.ianus.util;

public class ExampleClass {

	private Integer testField = null;
	
	public ExampleClass() {}
	
	private Integer privateMethod(Integer one, Integer two) {
		if(one == null && two == null) return null;
		if(two == null) return one; 
		return one + two;
	}
	
	public Integer publicMethod(Integer one, Integer two) {
		if(one == null && two == null) return null;
		if(two == null) return one; 
		return one + two;
	}
}