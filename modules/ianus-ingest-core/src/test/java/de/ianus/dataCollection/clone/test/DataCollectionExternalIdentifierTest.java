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
 *
 * @author MR
 *
 */
public class DataCollectionExternalIdentifierTest {
	
	DataCollection originalDc;
	DataCollection H2Dc;
	DataCollection cloneDc;
	Identifier externalId1;
	Identifier externalId2;
	Identifier externalId3;
	
	@BeforeClass
	public static void startTheTest(){
		System.out.println("Data Collection External Identifier Clone Test START");
		System.out.println();
	}
	
	
	/**
	 * Create the Object, initialize the test value and store value in the H2 database and make that clone.  
	 *
	 */
	@Before
	public void beforeTest() throws Exception{
		
		this.externalId1 = new Identifier();
		this.externalId2 = new Identifier();
		this.externalId3 = new Identifier();
		this.originalDc = new DataCollection();
		
		Services.getInstance().getMDService().saveDataCollection(originalDc);
		
		externalId1.setType("Identifier1");
		externalId1.setValue("This is external Identifier1");
		externalId1.setInstitution("IANUS1"); 
		externalId1.setContentType(ContentType.external_id);
		
		externalId2.setType("Identifier2");
		externalId2.setValue("This is external Identifier2");
		externalId2.setInstitution("IANUS2"); 
		externalId2.setContentType(ContentType.external_id);

		externalId3.setType("Identifier3");
		externalId3.setValue("This is external Identifier3");
		externalId3.setInstitution("IANUS3"); 
		externalId3.setContentType(ContentType.external_id);
		
		externalId1.setSource(originalDc);
		externalId2.setSource(originalDc);
		externalId3.setSource(originalDc);
		
		Services.getInstance().getMDService().saveEntry(externalId1);
		Services.getInstance().getMDService().saveEntry(externalId2);
		Services.getInstance().getMDService().saveEntry(externalId3);
		
		this.originalDc.getExternalIdList().add(externalId1);
		this.originalDc.getExternalIdList().add(externalId2);
		this.originalDc.getExternalIdList().add(externalId3);
	
			
		H2Dc = Services.getInstance().getMDService().getDataCollection(originalDc.getId());
		
		cloneDc = Services.getInstance().getMDService().cloneDataCollection0(originalDc).getDc();
		cloneDc = Services.getInstance().getMDService().getDataCollection(cloneDc.getId());
		
	}
	
	/**
	 * Test the size of External Identifier List
	 */
	
	@Test
	public void testSize(){
		
		assertEquals("Original and H2 Identifier Size Does not match: ", originalDc.getExternalIdList().size(), H2Dc.getExternalIdList().size());
		
		assertEquals("Original and Clone Identifier Size Does not match: ", originalDc.getExternalIdList().size(), cloneDc.getExternalIdList().size());
		
	}
	
	@Test
	public void testCompareIdentifierType(){
		
		
		Identifier originalP1 = getExternalIdentifierAfterTest(this.originalDc, this.externalId1.getType());
		Identifier cloneP1 = getExternalIdentifierAfterTest(this.cloneDc, this.externalId1.getType());
		
		Identifier originalP2 = getExternalIdentifierAfterTest(this.originalDc, this.externalId2.getType());
		Identifier cloneP2 = getExternalIdentifierAfterTest(this.cloneDc, this.externalId2.getType());
		
		Identifier originalP3 = getExternalIdentifierAfterTest(this.originalDc, this.externalId3.getType());
		Identifier cloneP3 = getExternalIdentifierAfterTest(this.cloneDc, this.externalId3.getType());
		
		assertNotNull(originalP1.getType(), cloneP1.getType());
		assertNotNull(originalP2.getType(), cloneP2.getType());
		assertNotNull(originalP3.getType(), cloneP3.getType());

		assertNotEquals("originalP1 & cloneP1 Id is Equal",originalP1.getId(), cloneP1.getId());
		assertNotEquals("originalP2 & cloneP2 Id is Equal", originalP2.getId(), cloneP2.getId());
		assertNotEquals("originalP3 & cloneP3 Id is Equal", originalP3.getId(), cloneP3.getId());
	}

	@Test
	public void testCompareIdentifierValue(){
		
		
		Identifier originalP1 = getExternalIdentifierAfterTest(this.originalDc, this.externalId1.getValue());
		Identifier cloneP1 = getExternalIdentifierAfterTest(this.cloneDc, this.externalId1.getValue());
		
		Identifier originalP2 = getExternalIdentifierAfterTest(this.originalDc, this.externalId2.getValue());
		Identifier cloneP2 = getExternalIdentifierAfterTest(this.cloneDc, this.externalId2.getValue());
		
		Identifier originalP3 = getExternalIdentifierAfterTest(this.originalDc, this.externalId3.getValue());
		Identifier cloneP3 = getExternalIdentifierAfterTest(this.cloneDc, this.externalId3.getValue());
		
		assertNotNull(originalP1.getValue(), cloneP1.getValue());
		assertNotNull(originalP2.getValue(), cloneP2.getValue());
		assertNotNull(originalP3.getValue(), cloneP3.getValue());

		assertNotEquals("originalP1 & cloneP1 Id is Equal",originalP1.getId(), cloneP1.getId());
		assertNotEquals("originalP2 & cloneP2 Id is Equal", originalP2.getId(), cloneP2.getId());
		assertNotEquals("originalP3 & cloneP3 Id is Equal", originalP3.getId(), cloneP3.getId());
	}
	
	@Test
	public void testCompareIdentifierInstitution(){
		
		
		Identifier originalP1 = getExternalIdentifierAfterTest(this.originalDc, this.externalId1.getInstitution());
		Identifier cloneP1 = getExternalIdentifierAfterTest(this.cloneDc, this.externalId1.getInstitution());
		
		Identifier originalP2 = getExternalIdentifierAfterTest(this.originalDc, this.externalId2.getInstitution());
		Identifier cloneP2 = getExternalIdentifierAfterTest(this.cloneDc, this.externalId2.getInstitution());
		
		Identifier originalP3 = getExternalIdentifierAfterTest(this.originalDc, this.externalId3.getInstitution());
		Identifier cloneP3 = getExternalIdentifierAfterTest(this.cloneDc, this.externalId3.getInstitution());
		
		assertNotNull(originalP1.getInstitution(), cloneP1.getInstitution());
		assertNotNull(originalP2.getInstitution(), cloneP2.getInstitution());
		assertNotNull(originalP3.getInstitution(), cloneP3.getInstitution());

		assertNotEquals("originalP1 & cloneP1 Id is Equal",originalP1.getId(), cloneP1.getId());
		assertNotEquals("originalP2 & cloneP2 Id is Equal", originalP2.getId(), cloneP2.getId());
		assertNotEquals("originalP3 & cloneP3 Id is Equal", originalP3.getId(), cloneP3.getId());
	}
	
	
	private Identifier getExternalIdentifierAfterTest(DataCollection dc, String value){
		
		for(Identifier p : dc.getExternalIdList()){
			
			if(p.getType().equals(value)){
				return p;
			}else if(p.getValue().equals(value)){
				return p;
			}else if(p.getInstitution().equals(value)){
				return p;
			}
		}
		return null;
		
	}
	
	

	@AfterClass
	public static void endTheTest(){
		System.out.println();
		System.out.println("Data Collection External Identifier Clone Test DONE");
	}

}
