package de.ianus.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PrivateAccessTestHelperTest {
	
	
	@Test
	public void shouldInvokePrivateMethod() {
		ExampleClass object = new ExampleClass();
		// get the reflection
		Class<?> c = object.getClass();
		ExampleClass reflection = (ExampleClass) PrivateAccessTestHelper.getReflection(c);
		
		Integer result = (Integer) PrivateAccessTestHelper.invokeMethod(
				c,
				reflection,
				"privateMethod",
				new Class[]{Integer.class, Integer.class},
				new Integer[]{3, 2});
		assertEquals("Reflection mismatch", (Integer) 5, result);
	}
	
	
	@Test
	public void shouldInvokePublicMethod() {
		ExampleClass object = new ExampleClass();
		assertEquals("Result mismatch with input null",
	    		null,
	    		(Integer) object.publicMethod(null, null));
	    assertEquals("Result mismatch",
	    		(Integer) 5,
	    		(Integer) object.publicMethod(2, 3));
	}
	
	
	@Test
	public void shouldAccessPrivateField() {
		ExampleClass object = new ExampleClass();
		// get the reflection
		Class<?> c = object.getClass();
		ExampleClass reflection = (ExampleClass) PrivateAccessTestHelper.getReflection(c);
		
		assertTrue("Failed to set private field",
				PrivateAccessTestHelper.setField(c, reflection, "testField",
				(Integer) 13));
		assertEquals("Failed to get private field",
				PrivateAccessTestHelper.getField(c, reflection, "testField"),
				(Integer) 13);
	}
	
	
	
	
	
	
}


