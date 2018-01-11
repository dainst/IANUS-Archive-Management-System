package de.ianus.metadata.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import de.ianus.metadata.xml.XMLObject.Type;



public class XMLObjectTest {
	
	@Rule
	public TemporaryFolder folder= new TemporaryFolder();
	
	private XMLObject root = null;
	
	
	private void createXML() {
		this.root = new XMLObject("foo", null, Type.ELEMENT);
		XMLObject el1 = new XMLObject("bar", "Aelomen, Aelomen. Lapitaluminei, bominuskaya bomballabunga.", Type.ELEMENT);
		XMLObject el2 = new XMLObject("baz", null, Type.ELEMENT);
		XMLObject el3 = new XMLObject("nose", null, Type.ELEMENT);
		
		XMLObject att = new XMLObject("bat", "cat", Type.ATTRIBUTE);
		XMLObject attNull = new XMLObject("badCat", null, Type.ATTRIBUTE);
		
		root.addDescendant(el1);
		root.addDescendant(el2);
		root.addDescendant(att);
		
		el2.addDescendant(el3);
		el2.addDescendant(attNull);
		
		el3.addDescendant(att);
	}
	
	
	
	private static String readFile(String path, Charset encoding) {
		byte[] encoded = null;
		try {
			encoded = Files.readAllBytes(Paths.get(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String(encoded, encoding);
	}
	
	
	@Test
	public void shouldCreateXMLString() {
		this.createXML();
		String str = root.toString();
		assertEquals(
				"<?xml version=\"1.0\" ?><foo bat=\"cat\"><bar>Aelomen, Aelomen. Lapitaluminei, bominuskaya bomballabunga.</bar><baz badCat=\"\"><nose bat=\"cat\"></nose></baz></foo>",
				str);
	}
	
	
	
	@Test
	public void shouldCreateXMLFile() {
		this.createXML();
		File file = null;
		try {
			file = folder.newFile("test.xml");
			this.root.toFile(file.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		assertTrue(file.exists());
		assertEquals(root.toString(), readFile(file.getAbsolutePath(), StandardCharsets.UTF_8));
	}
}
