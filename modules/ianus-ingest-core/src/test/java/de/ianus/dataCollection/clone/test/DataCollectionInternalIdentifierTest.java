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
import de.ianus.metadata.bo.utils.Identifier;
import de.ianus.metadata.bo.utils.Identifier.ContentType;


/**
 * This test class is going to test the DataCollection Clone method. 
 *
 * @author MR
 *
 */
public class DataCollectionInternalIdentifierTest {
	
	DataCollection originalDc;
	DataCollection H2Dc;
	DataCollection cloneDc;
	Identifier internalId1;
	Identifier internalId2;
	Identifier internalId3;
	
	@BeforeClass
	public static void startTheTest(){
		System.out.println("Data Collection Internal Identifier Clone Test START");
		System.out.println();
	}
	
	
	/**
	 * Create the Object, initialize the test value and store the value in the H2 database and make it clone.  
	 *
	 */
	@Before
	public void beforeTest() throws Exception{
		
		this.internalId1 = new Identifier();
		this.internalId2 = new Identifier();
		this.internalId3 = new Identifier();
		this.originalDc = new DataCollection();
		
		Services.getInstance().getMDService().saveDataCollection(originalDc);
		
		internalId1.setType("Identifier1");
		internalId1.setValue("This is Internal Identifier1");
		internalId1.setInstitution("IANUS1"); 
		internalId1.setContentType(ContentType.internal_id);
		
		internalId2.setType("Identifier2");
		internalId2.setValue("This is Internal Identifier2");
		internalId2.setInstitution("IANUS2"); 
		internalId2.setContentType(ContentType.internal_id);

		internalId3.setType("Identifier3");
		internalId3.setValue("This is Internal Identifier3");
		internalId3.setInstitution("IANUS3"); 
		internalId3.setContentType(ContentType.internal_id);
		
		internalId1.setSource(originalDc);
		internalId2.setSource(originalDc);
		internalId3.setSource(originalDc);
		
		Services.getInstance().getMDService().saveEntry(internalId1);
		Services.getInstance().getMDService().saveEntry(internalId2);
		Services.getInstance().getMDService().saveEntry(internalId3);
		
		this.originalDc.getInternalIdList().add(internalId1);
		this.originalDc.getInternalIdList().add(internalId2);
		this.originalDc.getInternalIdList().add(internalId3);
	
			
		H2Dc = Services.getInstance().getMDService().getDataCollection(originalDc.getId());
		
		cloneDc = Services.getInstance().getMDService().cloneDataCollection0(originalDc).getDc();
		cloneDc = Services.getInstance().getMDService().getDataCollection(cloneDc.getId());
		
	}
	
	@Test
	public void testSize(){
		
		assertEquals("Original and H2 Identifier Size Does not match: ", originalDc.getInternalIdList().size(), H2Dc.getInternalIdList().size());
		
		assertEquals("Original and Clone Identifier Size Does not match: ", originalDc.getInternalIdList().size(), cloneDc.getInternalIdList().size());
		
	}
	
	@Test
	public void testCompareIdentifierType(){
		
		
		Identifier originalI1 = getIdentifierIndentifierAfterTest(this.originalDc, this.internalId1.getType());
		Identifier cloneI1 = getIdentifierIndentifierAfterTest(this.cloneDc, this.internalId1.getType());
		
		Identifier originalI2 = getIdentifierIndentifierAfterTest(this.originalDc, this.internalId2.getType());
		Identifier cloneI2 = getIdentifierIndentifierAfterTest(this.cloneDc, this.internalId2.getType());
		
		Identifier originalI3 = getIdentifierIndentifierAfterTest(this.originalDc, this.internalId3.getType());
		Identifier cloneI3 = getIdentifierIndentifierAfterTest(this.cloneDc, this.internalId3.getType());
		
		assertNotNull(originalI1.getType(), cloneI1.getType());
		assertNotNull(originalI2.getType(), cloneI2.getType());
		assertNotNull(originalI3.getType(), cloneI3.getType());

		assertNotEquals("originalI1 & cloneI1 Id is Equal",originalI1.getId(), cloneI1.getId());
		assertNotEquals("originalI2 & cloneI2 Id is Equal", originalI2.getId(), cloneI2.getId());
		assertNotEquals("originalI3 & cloneI3 Id is Equal", originalI3.getId(), cloneI3.getId());
	}

	@Test
	public void testCompareIdentifierValue(){
		
		
		Identifier originalI1 = getIdentifierIndentifierAfterTest(this.originalDc, this.internalId1.getValue());
		Identifier cloneI1 = getIdentifierIndentifierAfterTest(this.cloneDc, this.internalId1.getValue());
		
		Identifier originalI2 = getIdentifierIndentifierAfterTest(this.originalDc, this.internalId2.getValue());
		Identifier cloneI2 = getIdentifierIndentifierAfterTest(this.cloneDc, this.internalId2.getValue());
		
		Identifier originalI3 = getIdentifierIndentifierAfterTest(this.originalDc, this.internalId3.getValue());
		Identifier cloneI3 = getIdentifierIndentifierAfterTest(this.cloneDc, this.internalId3.getValue());
		
		assertNotNull(originalI1.getValue(), cloneI1.getValue());
		assertNotNull(originalI2.getValue(), cloneI2.getValue());
		assertNotNull(originalI3.getValue(), cloneI3.getValue());

		assertNotEquals("originalI1 & cloneI1 Id is Equal",originalI1.getId(), cloneI1.getId());
		assertNotEquals("originalI2 & cloneI2 Id is Equal", originalI2.getId(), cloneI2.getId());
		assertNotEquals("originalI3 & cloneI3 Id is Equal", originalI3.getId(), cloneI3.getId());
	}
	
	@Test
	public void testCompareIdentifierInstitution(){
		
		
		Identifier originalI1 = getIdentifierIndentifierAfterTest(this.originalDc, this.internalId1.getInstitution());
		Identifier cloneI1 = getIdentifierIndentifierAfterTest(this.cloneDc, this.internalId1.getInstitution());
		
		Identifier originalI2 = getIdentifierIndentifierAfterTest(this.originalDc, this.internalId2.getInstitution());
		Identifier cloneI2 = getIdentifierIndentifierAfterTest(this.cloneDc, this.internalId2.getInstitution());
		
		Identifier originalI3 = getIdentifierIndentifierAfterTest(this.originalDc, this.internalId3.getInstitution());
		Identifier cloneI3 = getIdentifierIndentifierAfterTest(this.cloneDc, this.internalId3.getInstitution());
		
		assertNotNull(originalI1.getInstitution(), cloneI1.getInstitution());
		assertNotNull(originalI2.getInstitution(), cloneI2.getInstitution());
		assertNotNull(originalI3.getInstitution(), cloneI3.getInstitution());

		assertNotEquals("originalI1 & cloneI1 Id is Equal",originalI1.getId(), cloneI1.getId());
		assertNotEquals("originalI2 & cloneI2 Id is Equal", originalI2.getId(), cloneI2.getId());
		assertNotEquals("originalI3 & cloneI3 Id is Equal", originalI3.getId(), cloneI3.getId());
	}
	
	
	private Identifier getIdentifierIndentifierAfterTest(DataCollection dc, String value){
		
		for(Identifier i : dc.getInternalIdList()){
			
			if(i.getType().equals(value)){
				return i;
			}else if(i.getValue().equals(value)){
				return i;
			}else if(i.getInstitution().equals(value)){
				return i;
			}
		}
		return null;
		
	}
	
	
	@AfterClass
	public static void endTheTest(){
		System.out.println();
		System.out.println("Data Collection Internal Identifier Clone Test DONE");
	}

}
