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
import de.ianus.metadata.bo.utils.Language;
import de.ianus.metadata.bo.utils.Language.ContentType;




/**
 * This test class is going to test the Language DataCollection Clone. 
 *
 * @author MR
 *
 */
public class DataCollectionLanguageTest {
	
	
	DataCollection originalDc;
	DataCollection H2Dc;
	DataCollection cloneDc;
	
	Language lan1;
	Language lan2;
	Language lan3;

	
	@BeforeClass
	public static void startTheTest(){
		System.out.println("Data Collection Language Clone Test START");
		System.out.println();
	}
	
	/**
	 * Create the Object, initialize the test value and store the value in the H2 database and make it clone.  
	 *
	 */
	@Before
	public void beforeTest() throws Exception{
		
		this.lan1 = new Language();
		this.lan2 = new Language();
		this.lan3 = new Language();
	
		this.originalDc = new DataCollection();
		
		Services.getInstance().getMDService().saveDataCollection(originalDc);
		
		lan1.setCode("IANUSTest1");
		lan1.setLabel("DAI2");
		lan1.setContentType(ContentType.collection_language);
		
	
		lan2.setCode("IANUSTest2");
		lan2.setLabel("DAI2");
		lan2.setContentType(ContentType.collection_language);
	
		
		lan3.setCode("IANUSTest3");
		lan3.setLabel("DAI3");
		lan3.setContentType(ContentType.collection_language);
		
		
		lan1.setSource(originalDc);
		lan2.setSource(originalDc);
		lan3.setSource(originalDc);

		
		Services.getInstance().getMDService().saveEntry(lan1);
		Services.getInstance().getMDService().saveEntry(lan2);
		Services.getInstance().getMDService().saveEntry(lan3);
		
		this.originalDc.getCollectionLanguageList().add(lan1);
		this.originalDc.getCollectionLanguageList().add(lan2);
		this.originalDc.getCollectionLanguageList().add(lan3);
		
		
		H2Dc = Services.getInstance().getMDService().getDataCollection(originalDc.getId());
		
		cloneDc = Services.getInstance().getMDService().cloneDataCollection0(originalDc).getDc();
		cloneDc = Services.getInstance().getMDService().getDataCollection(cloneDc.getId());
		
		
	}
	
	@Test
	public void testSize(){
		
		assertEquals("Original and H2 Language Attribute Size Does not match: ", originalDc.getCollectionLanguageList().size(), H2Dc.getCollectionLanguageList().size());
		
		assertEquals("Original and Clone Language Attribute Does not match: ", originalDc.getCollectionLanguageList().size(), cloneDc.getCollectionLanguageList().size());
		
	}
	
	@Test
	public void testCompareLanguageCode(){
		
		
		Language originalP1 = getLanguageAfterTest(this.originalDc, this.lan1.getCode());
		Language cloneP1 = getLanguageAfterTest(this.cloneDc, this.lan1.getCode());
		
		Language originalP2 = getLanguageAfterTest(this.originalDc, this.lan2.getCode());
		Language cloneP2 = getLanguageAfterTest(this.cloneDc, this.lan2.getCode());
		
		Language originalP3 = getLanguageAfterTest(this.originalDc, this.lan3.getCode());
		Language cloneP3 = getLanguageAfterTest(this.cloneDc, this.lan3.getCode());
		
		assertNotNull(originalP1.getCode(), cloneP1.getCode());
		assertNotNull(originalP2.getCode(), cloneP2.getCode());
		assertNotNull(originalP3.getCode(), cloneP3.getCode());

		assertNotEquals("originalP1 & cloneP1 Id is Equal",originalP1.getId(), cloneP1.getId());
		assertNotEquals("originalP2 & cloneP2 Id is Equal", originalP2.getId(), cloneP2.getId());
		assertNotEquals("originalP3 & cloneP3 Id is Equal", originalP3.getId(), cloneP3.getId());
	}

	
	@Test
	public void testCompareLanguageLabel(){
		
		
		Language originalP1 = getLanguageAfterTest(this.originalDc, this.lan1.getLabel());
		Language cloneP1 = getLanguageAfterTest(this.cloneDc, this.lan1.getLabel());
		
		Language originalP2 = getLanguageAfterTest(this.originalDc, this.lan2.getLabel());
		Language cloneP2 = getLanguageAfterTest(this.cloneDc, this.lan2.getLabel());
		
		Language originalP3 = getLanguageAfterTest(this.originalDc, this.lan3.getLabel());
		Language cloneP3 = getLanguageAfterTest(this.cloneDc, this.lan3.getLabel());
		
		assertNotNull(originalP1.getLabel(), cloneP1.getLabel());
		assertNotNull(originalP2.getLabel(), cloneP2.getLabel());
		assertNotNull(originalP3.getLabel(), cloneP3.getLabel());

		assertNotEquals("originalP1 & cloneP1 Id is Equal",originalP1.getId(), cloneP1.getId());
		assertNotEquals("originalP2 & cloneP2 Id is Equal", originalP2.getId(), cloneP2.getId());
		assertNotEquals("originalP3 & cloneP3 Id is Equal", originalP3.getId(), cloneP3.getId());
	}
	
	private Language getLanguageAfterTest(DataCollection dc, String value){
		
		for(Language l : dc.getCollectionLanguageList()){
			
			if(l.getCode().equals(value)){
				return l;
			}else if(l.getLabel().equals(value)){
				return l;
			}
		}
		return null;
		
	}
	

	
	@AfterClass
	public static void endTheTest(){
		System.out.println();
		System.out.println("Data Collection Language Clone Test DONE");
	}

}
