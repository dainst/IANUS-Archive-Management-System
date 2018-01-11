package de.ianus.dataCollection.clone.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.ianus.ingest.core.Services;
import de.ianus.metadata.bo.DataCollection;
import de.ianus.metadata.bo.utils.TextAttribute;
import de.ianus.metadata.bo.utils.TextAttribute.ContentType;


/**
 * This test class is going to test the Text Attribute DataCollection Clone. 
 *
 * @author MR
 *
 */
public class DataCollectionTextAttributeTest {
	
	
	DataCollection originalDc;
	DataCollection H2Dc;
	DataCollection cloneDc;
	
	TextAttribute textAttribute1;
	TextAttribute textAttribute2;
	TextAttribute textAttribute3;
	
	@BeforeClass
	public static void startTheTest(){
		System.out.println("Data Collection Text Attribute Clone Test START");
		System.out.println();
	}
	
	/**
	 * Create the Object, initialize the test value and store the value in the H2 database and make it clone.  
	 *
	 */
	@Before
	public void beforeTest() throws Exception{
		
		this.textAttribute1 = new TextAttribute();
		this.textAttribute2 = new TextAttribute();
		this.textAttribute3 = new TextAttribute();
		
		this.originalDc = new DataCollection();
		
		Services.getInstance().getMDService().saveDataCollection(originalDc);
		
		textAttribute1.setLabel("IANUS1");
		textAttribute1.setValue("This is Text Attribute Value1");
		textAttribute1.setLanguageCode("Deutsch");
		textAttribute1.setContentType(ContentType.alternativeTitle);
		
		textAttribute2.setLabel("IANUS2");
		textAttribute2.setValue("This is Text Attribute Value2");
		textAttribute2.setLanguageCode("English");
		textAttribute2.setContentType(ContentType.alternativeTitle);
		
		textAttribute3.setLabel("IANUS3");
		textAttribute3.setValue("This is Text Attribute Value3");
		textAttribute3.setLanguageCode("Bengali");
		textAttribute3.setContentType(ContentType.alternativeTitle);
		
		textAttribute1.setSource(originalDc);
		textAttribute2.setSource(originalDc);
		textAttribute3.setSource(originalDc);
		
		Services.getInstance().getMDService().saveEntry(textAttribute1);
		Services.getInstance().getMDService().saveEntry(textAttribute2);
		Services.getInstance().getMDService().saveEntry(textAttribute3);
		
		this.originalDc.getAlternativeTitleList().add(textAttribute1);
		this.originalDc.getAlternativeTitleList().add(textAttribute2);
		this.originalDc.getAlternativeTitleList().add(textAttribute3);
		
		H2Dc = Services.getInstance().getMDService().getDataCollection(originalDc.getId());
		
		cloneDc = Services.getInstance().getMDService().cloneDataCollection0(originalDc).getDc();
		cloneDc = Services.getInstance().getMDService().getDataCollection(cloneDc.getId());
		
		
	}
	
	@Test
	public void testSize(){
		
		assertEquals("Original and H2 Text Attribute Size Does not match: ", originalDc.getAlternativeTitleList().size(), H2Dc.getAlternativeTitleList().size());
		
		assertEquals("Original and Clone Text Attribute Size Does not match: ", originalDc.getAlternativeTitleList().size(), cloneDc.getAlternativeTitleList().size());
		
	}
	
	@Test
	public void testCompareTextAttributeLabel(){
		
		
		TextAttribute originalTA1 = getTextAttributeAfterTest(this.originalDc, this.textAttribute1.getLabel());
		TextAttribute cloneTA1 = getTextAttributeAfterTest(this.cloneDc, this.textAttribute1.getLabel());
		
		TextAttribute originalTA2 = getTextAttributeAfterTest(this.originalDc, this.textAttribute2.getLabel());
		TextAttribute cloneTA2 = getTextAttributeAfterTest(this.cloneDc, this.textAttribute2.getLabel());
		
		TextAttribute originalTA3 = getTextAttributeAfterTest(this.originalDc, this.textAttribute3.getLabel());
		TextAttribute cloneTA3 = getTextAttributeAfterTest(this.cloneDc, this.textAttribute3.getLabel());
		
		assertNotNull(originalTA1.getLabel(), cloneTA1.getLabel());
		assertNotNull(originalTA2.getLabel(), cloneTA2.getLabel());
		assertNotNull(originalTA3.getLabel(), cloneTA3.getLabel());

		assertNotEquals("originalTA1 & cloneTA1 Id is Equal",originalTA1.getId(), cloneTA1.getId());
		assertNotEquals("originalTA2 & cloneTA2 Id is Equal", originalTA2.getId(), cloneTA2.getId());
		assertNotEquals("originalTA3 & cloneTA3 Id is Equal", originalTA3.getId(), cloneTA3.getId());
	}
	
	@Test
	public void testCompareTextAttributeValue(){
		
		
		TextAttribute originalTA1 = getTextAttributeAfterTest(this.originalDc, this.textAttribute1.getValue());
		TextAttribute cloneTA1 = getTextAttributeAfterTest(this.cloneDc, this.textAttribute1.getValue());
		
		TextAttribute originalTA2 = getTextAttributeAfterTest(this.originalDc, this.textAttribute2.getValue());
		TextAttribute cloneTA2 = getTextAttributeAfterTest(this.cloneDc, this.textAttribute2.getValue());
		
		TextAttribute originalTA3 = getTextAttributeAfterTest(this.originalDc, this.textAttribute3.getValue());
		TextAttribute cloneTA3 = getTextAttributeAfterTest(this.cloneDc, this.textAttribute3.getValue());
		
		assertNotNull(originalTA1.getValue(), cloneTA1.getValue());
		assertNotNull(originalTA2.getValue(), cloneTA2.getValue());
		assertNotNull(originalTA3.getValue(), cloneTA3.getValue());

		assertNotEquals("originalTA1 & cloneTA1 Id is Equal",originalTA1.getId(), cloneTA1.getId());
		assertNotEquals("originalTA2 & cloneTA2 Id is Equal", originalTA2.getId(), cloneTA2.getId());
		assertNotEquals("originalTA3 & cloneTA3 Id is Equal", originalTA3.getId(), cloneTA3.getId());
	}
	
	
	@Test
	public void testCompareTextAttributeLanguageCode(){
		
		
		TextAttribute originalTA1 = getTextAttributeAfterTest(this.originalDc, this.textAttribute1.getLanguageCode());
		TextAttribute cloneTA1 = getTextAttributeAfterTest(this.cloneDc, this.textAttribute1.getLanguageCode());
		
		TextAttribute originalTA2 = getTextAttributeAfterTest(this.originalDc, this.textAttribute2.getLanguageCode());
		TextAttribute cloneTA2 = getTextAttributeAfterTest(this.cloneDc, this.textAttribute2.getLanguageCode());
		
		TextAttribute originalTA3 = getTextAttributeAfterTest(this.originalDc, this.textAttribute3.getLanguageCode());
		TextAttribute cloneTA3 = getTextAttributeAfterTest(this.cloneDc, this.textAttribute3.getLanguageCode());
		
		assertNotNull(originalTA1.getLanguageCode(), cloneTA1.getLanguageCode());
		assertNotNull(originalTA2.getLanguageCode(), cloneTA2.getLanguageCode());
		assertNotNull(originalTA3.getLanguageCode(), cloneTA3.getLanguageCode());

		assertNotEquals("originalTA1 & cloneTA1 Id is Equal",originalTA1.getId(), cloneTA1.getId());
		assertNotEquals("originalTA2 & cloneTA2 Id is Equal", originalTA2.getId(), cloneTA2.getId());
		assertNotEquals("originalTA3 & cloneTA3 Id is Equal", originalTA3.getId(), cloneTA3.getId());
	}

	private TextAttribute getTextAttributeAfterTest(DataCollection dc, String value){
		
		for(TextAttribute ta : dc.getAlternativeTitleList()){
			
			if(ta.getLabel().equals(value)){
				return ta;
			}else if(ta.getValue().equals(value)){
				return ta;
			}else if(ta.getLanguageCode().equals(value)){
				return ta;
			}
		}
		return null;
		
	}
	
	
	
	@AfterClass
	public static void endTheTest(){
		System.out.println();
		System.out.println("Data Collection Text Attribute Clone Test DONE");
	}

}
