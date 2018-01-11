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
import de.ianus.metadata.bo.resource.RelatedResource;




/**
 * This test class is going to test the RelatedResource DataCollection Clone. 
 *
 * @author MR
 *
 */
public class DataCollectionRelatedResourceTest {
	
	
	DataCollection originalDc;
	DataCollection H2Dc;
	DataCollection cloneDc;
	
	RelatedResource rResource1;
	RelatedResource rResource2;
	RelatedResource rResource3;

	
	@BeforeClass
	public static void startTheTest(){
		System.out.println("Data Collection RelatedResource Clone Test START");
		System.out.println();
	}
	
	/**
	 * Create the Object, initialize the test value and store the value in the H2 database and make it clone.  
	 *
	 */
	@Before
	public void beforeTest() throws Exception{
		
		this.rResource1 = new RelatedResource();
		this.rResource2 = new RelatedResource();
		this.rResource3 = new RelatedResource();
	
		this.originalDc = new DataCollection();
		
		Services.getInstance().getMDService().saveDataCollection(originalDc);
		
		rResource1.setDescription("IANUS Project1");
		rResource1.setLocation("Berlin");
		rResource1.setScope("DAI1");
	
		rResource2.setDescription("IANUS Project2");
		rResource2.setLocation("Hannover");
		rResource2.setScope("DAI2");
		
		rResource3.setDescription("IANUS Project3");
		rResource3.setLocation("Hamburg");
		rResource3.setScope("DAI3");
		
		
		rResource1.setSource(originalDc);
		rResource2.setSource(originalDc);
		rResource3.setSource(originalDc);

		
		Services.getInstance().getMDService().saveEntry(rResource1);
		Services.getInstance().getMDService().saveEntry(rResource2);
		Services.getInstance().getMDService().saveEntry(rResource3);
		
		this.originalDc.getRelatedResourceList().add(rResource1);
		this.originalDc.getRelatedResourceList().add(rResource2);
		this.originalDc.getRelatedResourceList().add(rResource3);
		
		
		H2Dc = Services.getInstance().getMDService().getDataCollection(originalDc.getId());
		
		cloneDc = Services.getInstance().getMDService().cloneDataCollection0(originalDc).getDc();
		cloneDc = Services.getInstance().getMDService().getDataCollection(cloneDc.getId());
		
		
	}
	
	@Test
	public void testSize(){
		
		assertEquals("Original and H2 Actor Size Does not match: ", originalDc.getRelatedResourceList().size(), H2Dc.getRelatedResourceList().size());
		
		assertEquals("Original and Clone Actor Size Does not match: ", originalDc.getRelatedResourceList().size(), cloneDc.getRelatedResourceList().size());
		
	}
	
	@Test
	public void testCompareRelatedResourceDescription(){
		
		
		RelatedResource originalR1 = getRelatedResourceAfterTest(this.originalDc, this.rResource1.getDescription());
		RelatedResource cloneR1 = getRelatedResourceAfterTest(this.cloneDc, this.rResource1.getDescription());
		
		RelatedResource originalR2 = getRelatedResourceAfterTest(this.originalDc, this.rResource2.getDescription());
		RelatedResource cloneR2 = getRelatedResourceAfterTest(this.cloneDc, this.rResource2.getDescription());
		
		RelatedResource originalR3 = getRelatedResourceAfterTest(this.originalDc, this.rResource3.getDescription());
		RelatedResource cloneR3 = getRelatedResourceAfterTest(this.cloneDc, this.rResource3.getDescription());
		
		assertNotNull(originalR1.getDescription(), cloneR1.getDescription());
		assertNotNull(originalR2.getDescription(), cloneR2.getDescription());
		assertNotNull(originalR3.getDescription(), cloneR3.getDescription());

		assertNotEquals("originalR1 & cloneR1 Id is Equal",originalR1.getId(), cloneR1.getId());
		assertNotEquals("originalR2 & cloneR2 Id is Equal", originalR2.getId(), cloneR2.getId());
		assertNotEquals("originalR3 & cloneR3 Id is Equal", originalR3.getId(), cloneR3.getId());
	}
	
	@Test
	public void testCompareRelatedResourceLocation(){
		
		
		RelatedResource originalR1 = getRelatedResourceAfterTest(this.originalDc, this.rResource1.getLocation());
		RelatedResource cloneR1 = getRelatedResourceAfterTest(this.cloneDc, this.rResource1.getLocation());
		
		RelatedResource originalR2 = getRelatedResourceAfterTest(this.originalDc, this.rResource2.getLocation());
		RelatedResource cloneR2 = getRelatedResourceAfterTest(this.cloneDc, this.rResource2.getLocation());
		
		RelatedResource originalR3 = getRelatedResourceAfterTest(this.originalDc, this.rResource3.getLocation());
		RelatedResource cloneR3 = getRelatedResourceAfterTest(this.cloneDc, this.rResource3.getLocation());
		
		assertNotNull(originalR1.getLocation(), cloneR1.getLocation());
		assertNotNull(originalR2.getLocation(), cloneR2.getLocation());
		assertNotNull(originalR3.getLocation(), cloneR3.getLocation());

		assertNotEquals("originalR1 & cloneR1 Id is Equal",originalR1.getId(), cloneR1.getId());
		assertNotEquals("originalR2 & cloneR2 Id is Equal", originalR2.getId(), cloneR2.getId());
		assertNotEquals("originalR3 & cloneR3 Id is Equal", originalR3.getId(), cloneR3.getId());
	}

	
	@Test
	public void testCompareRelatedResourceScope(){
		
		
		RelatedResource originalR1 = getRelatedResourceAfterTest(this.originalDc, this.rResource1.getScope());
		RelatedResource cloneR1 = getRelatedResourceAfterTest(this.cloneDc, this.rResource1.getScope());
		
		RelatedResource originalR2 = getRelatedResourceAfterTest(this.originalDc, this.rResource2.getScope());
		RelatedResource cloneR2 = getRelatedResourceAfterTest(this.cloneDc, this.rResource2.getScope());
		
		RelatedResource originalR3 = getRelatedResourceAfterTest(this.originalDc, this.rResource3.getScope());
		RelatedResource cloneR3 = getRelatedResourceAfterTest(this.cloneDc, this.rResource3.getScope());
		
		assertNotNull(originalR1.getScope(), cloneR1.getScope());
		assertNotNull(originalR2.getScope(), cloneR2.getScope());
		assertNotNull(originalR3.getScope(), cloneR3.getScope());

		assertNotEquals("originalR1 & cloneR1 Id is Equal",originalR1.getId(), cloneR1.getId());
		assertNotEquals("originalR2 & cloneR2 Id is Equal", originalR2.getId(), cloneR2.getId());
		assertNotEquals("originalR3 & cloneR3 Id is Equal", originalR3.getId(), cloneR3.getId());
	}
	private RelatedResource getRelatedResourceAfterTest(DataCollection dc, String value){
		
		for(RelatedResource rr : dc.getRelatedResourceList()){
			
			if(rr.getDescription().equals(value)){
				return rr;
			}else if(rr.getLocation().equals(value)){
				return rr;
			}else if(rr.getScope().equals(value)){
				return rr;
			}
		}
		return null;
		
	}

	
	@AfterClass
	public static void endTheTest(){
		System.out.println();
		System.out.println("Data Collection RelatedResource Clone Test DONE");
	}

}
