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
import de.ianus.metadata.bo.utils.ElementOfList;
import de.ianus.metadata.bo.utils.ElementOfList.ContentType;


/**
 * This test class is going to test the ElementOfList DataCollection Clone. 
 *
 * @author MR
 *
 */
public class DataCollectionElementOfListTest {
	
	
	DataCollection originalDc;
	DataCollection H2Dc;
	DataCollection cloneDc;
	
	
	ElementOfList element1;
	ElementOfList element2;
	ElementOfList element3;
	
	@BeforeClass
	public static void startTheTest(){
		System.out.println("Data Collection ElementOfList Clone Test START");
		System.out.println();
	}
	
	/**
	 * Create the Object, initialize the test value and store the value in the H2 database and make it clone.  
	 *
	 */
	@Before
	public void beforeTest() throws Exception{
		
		this.element1 = new ElementOfList();
		this.element2 = new ElementOfList();
		this.element3 = new ElementOfList();
		
		this.originalDc = new DataCollection();
		
		Services.getInstance().getMDService().saveDataCollection(originalDc);
		
		element1.setListId("IANUS1");
		element1.setValue("DAI1");
		element1.setValueId("Project1");
		element1.setUri("datenportal.ianus-fdz.de");
		element1.setContentType(ContentType.mainDiscipline);
		
		element2.setListId("IANUS2");
		element2.setValue("DAI2");
		element2.setValueId("Project2");
		element2.setUri("datenportal.ianus-fdz.de");
		element2.setContentType(ContentType.mainDiscipline);
		
		
		element3.setListId("IANUS3");
		element3.setValue("DAI3");
		element3.setValueId("Project3");
		element3.setUri("datenportal.ianus-fdz.de");
		element3.setContentType(ContentType.mainDiscipline);
		
		
		
		
		element1.setSource(originalDc);
		element2.setSource(originalDc);
		element3.setSource(originalDc);
		
		Services.getInstance().getMDService().saveEntry(element1);
		Services.getInstance().getMDService().saveEntry(element2);
		Services.getInstance().getMDService().saveEntry(element3);
		
		this.originalDc.getMainDisciplineList().add(element1);
		this.originalDc.getMainDisciplineList().add(element2);
		this.originalDc.getMainDisciplineList().add(element3);
		
		H2Dc = Services.getInstance().getMDService().getDataCollection(originalDc.getId());
		
		cloneDc = Services.getInstance().getMDService().cloneDataCollection0(originalDc).getDc();
		cloneDc = Services.getInstance().getMDService().getDataCollection(cloneDc.getId());
		
		
	}
	
	@Test
	public void testSize(){
		
		assertEquals("Original and H2 Text Attribute Size Does not match: ", originalDc.getMainDisciplineList().size(), H2Dc.getMainDisciplineList().size());
		
		assertEquals("Original and Clone Text Attribute Size Does not match: ", originalDc.getMainDisciplineList().size(), cloneDc.getMainDisciplineList().size());
		
	}
	
	
	@Test
	public void testCompareElementOfListListId(){
		
		
		ElementOfList originalE1 = getElementAfterTest(this.originalDc, this.element1.getListId());
		ElementOfList cloneE1 = getElementAfterTest(this.cloneDc, this.element1.getListId());
		
		ElementOfList originalE2 = getElementAfterTest(this.originalDc, this.element2.getListId());
		ElementOfList cloneE2 = getElementAfterTest(this.cloneDc, this.element2.getListId());
		
		ElementOfList originalE3 = getElementAfterTest(this.originalDc, this.element3.getListId());
		ElementOfList cloneE3 = getElementAfterTest(this.cloneDc, this.element3.getListId());
		
		assertNotNull(originalE1.getListId(), cloneE1.getListId());
		assertNotNull(originalE2.getListId(), cloneE2.getListId());
		assertNotNull(originalE3.getListId(), cloneE3.getListId());

		assertNotEquals("originalE1 & cloneE1 Id is Equal",originalE1.getId(), cloneE1.getId());
		assertNotEquals("originalE2 & cloneE2 Id is Equal", originalE2.getId(), cloneE2.getId());
		assertNotEquals("originalE3 & cloneE3 Id is Equal", originalE3.getId(), cloneE3.getId());
	}
	
	@Test
	public void testCompareElementOfListValue(){
		
		
		ElementOfList originalE1 = getElementAfterTest(this.originalDc, this.element1.getValue());
		ElementOfList cloneE1 = getElementAfterTest(this.cloneDc, this.element1.getValue());
		
		ElementOfList originalE2 = getElementAfterTest(this.originalDc, this.element2.getValue());
		ElementOfList cloneE2 = getElementAfterTest(this.cloneDc, this.element2.getValue());
		
		ElementOfList originalE3 = getElementAfterTest(this.originalDc, this.element3.getValue());
		ElementOfList cloneE3 = getElementAfterTest(this.cloneDc, this.element3.getValue());
		
		assertNotNull(originalE1.getValue(), cloneE1.getValue());
		assertNotNull(originalE2.getValue(), cloneE2.getValue());
		assertNotNull(originalE3.getValue(), cloneE3.getValue());

		assertNotEquals("originalE1 & cloneE1 Id is Equal",originalE1.getId(), cloneE1.getId());
		assertNotEquals("originalE2 & cloneE2 Id is Equal", originalE2.getId(), cloneE2.getId());
		assertNotEquals("originalE3 & cloneE3 Id is Equal", originalE3.getId(), cloneE3.getId());
	}
	
	@Test
	public void testCompareElementOfListValueId(){
		
		
		ElementOfList originalE1 = getElementAfterTest(this.originalDc, this.element1.getValueId());
		ElementOfList cloneE1 = getElementAfterTest(this.cloneDc, this.element1.getValueId());
		
		ElementOfList originalE2 = getElementAfterTest(this.originalDc, this.element2.getValueId());
		ElementOfList cloneE2 = getElementAfterTest(this.cloneDc, this.element2.getValueId());
		
		ElementOfList originalE3 = getElementAfterTest(this.originalDc, this.element3.getValueId());
		ElementOfList cloneE3 = getElementAfterTest(this.cloneDc, this.element3.getValueId());
		
		assertNotNull(originalE1.getValueId(), cloneE1.getValueId());
		assertNotNull(originalE2.getValueId(), cloneE2.getValueId());
		assertNotNull(originalE3.getValueId(), cloneE3.getValueId());

		assertNotEquals("originalE1 & cloneE1 Id is Equal",originalE1.getId(), cloneE1.getId());
		assertNotEquals("originalE2 & cloneE2 Id is Equal", originalE2.getId(), cloneE2.getId());
		assertNotEquals("originalE3 & cloneE3 Id is Equal", originalE3.getId(), cloneE3.getId());
	}
	
	@Test
	public void testCompareElementOfListUri(){
		
		
		ElementOfList originalE1 = getElementAfterTest(this.originalDc, this.element1.getUri());
		ElementOfList cloneE1 = getElementAfterTest(this.cloneDc, this.element1.getUri());
		
		ElementOfList originalE2 = getElementAfterTest(this.originalDc, this.element2.getUri());
		ElementOfList cloneE2 = getElementAfterTest(this.cloneDc, this.element2.getUri());
		
		ElementOfList originalE3 = getElementAfterTest(this.originalDc, this.element3.getUri());
		ElementOfList cloneE3 = getElementAfterTest(this.cloneDc, this.element3.getUri());
		
		assertNotNull(originalE1.getUri(), cloneE1.getUri());
		assertNotNull(originalE2.getUri(), cloneE2.getUri());
		assertNotNull(originalE3.getUri(), cloneE3.getUri());

		assertNotEquals("originalE1 & cloneE1 Id is Equal",originalE1.getId(), cloneE1.getId());
		assertNotEquals("originalE2 & cloneE2 Id is Equal", originalE2.getId(), cloneE2.getId());
		assertNotEquals("originalE3 & cloneE3 Id is Equal", originalE3.getId(), cloneE3.getId());
	}

	
	private ElementOfList getElementAfterTest(DataCollection dc, String value){
		
		for(ElementOfList e : dc.getMainDisciplineList()){
			
			if(e.getListId().equals(value)){
				return e;
			}else if(e.getValue().equals(value)){
				return e;
			}else if(e.getValueId().equals(value)){
				return e;
			}else if(e.getUri().equals(value)){
				return e;
			}
		}
		return null;
		
	}
	
	@AfterClass
	public static void endTheTest(){
		System.out.println();
		System.out.println("Data Collection ElementOfList Clone Test DONE");
	}

}
