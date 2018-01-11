package de.ianus.dataCollection.clone.test;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.ianus.ingest.core.Services;
import de.ianus.metadata.bo.DataCollection;


/**
 * This test class is going to test the DataCollection Clone method. 
 *
 * @author MR
 *
 */
public class DataCollectionAttributeCloneTest {
	
	DataCollection originalDc;
	DataCollection H2Dc;
	DataCollection cloneDc;

	
	
	@BeforeClass
	public static void startTheTest(){
		System.out.println("Data Collection Identifier Clone Test START");
		System.out.println();
	}
	
	/**
	 * Create the Object, initialize the test value and store the value in the H2 database and make it clone.  
	 *
	 */
	@Before
	public void beforeTest() throws Exception{
	
		this.originalDc = new DataCollection();
		
    	originalDc.setLabel("DataCollection_TEST");
    	originalDc.setLicenseName("IANUS_V.1.0");
    	originalDc.setAccessability("Access Test Data");
  
		Services.getInstance().getMDService().saveDataCollection(originalDc);
			
    	H2Dc = Services.getInstance().getMDService().getDataCollection(originalDc.getId());
			
		System.out.println("Data Collection ID: " + H2Dc.getId());
        
		cloneDc = Services.getInstance().getMDService().cloneDataCollection0(originalDc).getDc();

	}
	
	/**
	 * 
	 * This method test the Data Collection one attributes  
	 */
	@Test
	public void test1() throws Exception{

		assertEquals("DataCollection_TEST", H2Dc.getLabel());
		assertEquals(H2Dc.getLabel(), cloneDc.getLabel());
		assertEquals(H2Dc.getLicenseName(), cloneDc.getLicenseName());
		assertEquals(H2Dc.getAccessability(), cloneDc.getAccessability());
		assertNotEquals(H2Dc.getId(), cloneDc.getId());
	}
	
	

	@AfterClass
	public static void endTheTest(){
		System.out.println();
		System.out.println("Data Collection Identifier Clone Test DONE");
	}

}
