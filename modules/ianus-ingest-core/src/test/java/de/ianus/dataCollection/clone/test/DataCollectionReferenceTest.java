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
import de.ianus.metadata.bo.resource.Publication;
import de.ianus.metadata.bo.resource.Reference;



/**
 * This test class is going to test the Reference DataCollection Clone. 
 *
 * @author MR
 *
 */
public class DataCollectionReferenceTest {
	
	
	DataCollection originalDc;
	DataCollection H2Dc;
	DataCollection cloneDc;
	
	Reference ref1;
	Reference ref2;
	Reference ref3;
	
	Publication pub1;
	Publication pub2;
	Publication pub3;


	
	@BeforeClass
	public static void startTheTest(){
		System.out.println("Data Collection Reference Clone Test START");
		System.out.println();
	}
	
	/**
	 * Create the Object, initialize the test value and store the value in the H2 database and make it clone.  
	 *
	 */
	@Before
	public void beforeTest() throws Exception{
		
		this.ref1 = new Reference();
		this.ref2 = new Reference();
		this.ref3 = new Reference();
		
		this.pub1 = new Publication();
		this.pub2 = new Publication();
		this.pub3 = new Publication();
		
	
		this.originalDc = new DataCollection();
		
		Services.getInstance().getMDService().saveDataCollection(originalDc);
		
		Services.getInstance().getMDService().saveEntry(pub1);
		Services.getInstance().getMDService().saveEntry(pub2);
		Services.getInstance().getMDService().saveEntry(pub3);
		
		
		ref1.setRelationType("Project1");
		ref1.setPagesNumber("page1");
		ref1.setPublicationId(pub1.getId()); 
		
	
		ref2.setRelationType("Project2");
		ref2.setPagesNumber("page2");
		ref2.setPublicationId(pub2.getId());
		
		
		ref3.setRelationType("Project3");
		ref3.setPagesNumber("page3");
		ref3.setPublicationId(pub3.getId()); 
	
		
		ref1.setSource(originalDc);
		ref2.setSource(originalDc);
		ref3.setSource(originalDc);

		
		Services.getInstance().getMDService().saveEntry(ref1);
		Services.getInstance().getMDService().saveEntry(ref2);
		Services.getInstance().getMDService().saveEntry(ref3);
		
		
		this.originalDc.getReferenceList().add(ref1);
		this.originalDc.getReferenceList().add(ref2);
		this.originalDc.getReferenceList().add(ref3);
		
		
		H2Dc = Services.getInstance().getMDService().getDataCollection(originalDc.getId());
		
		cloneDc = Services.getInstance().getMDService().cloneDataCollection0(originalDc).getDc();
		cloneDc = Services.getInstance().getMDService().getDataCollection(cloneDc.getId());
		
		
	}
	
	
	
	@Test
	public void testSize(){
		
		assertEquals("Original and H2 Reference Size Does not match: ", originalDc.getReferenceList().size(), H2Dc.getReferenceList().size());
		
		assertEquals("Original and Clone Reference Size Does not match: ", originalDc.getReferenceList().size(), cloneDc.getReferenceList().size());
		
	}
	
	
	@Test
	public void testCompareReferenceRelationType(){
		
		
		Reference originalR1 = getReferenceAfterTest(this.originalDc, this.ref1.getRelationType());
		Reference cloneR1 = getReferenceAfterTest(this.cloneDc, this.ref1.getRelationType());
		
		Reference originalR2 = getReferenceAfterTest(this.originalDc, this.ref2.getRelationType());
		Reference cloneR2 = getReferenceAfterTest(this.cloneDc, this.ref2.getRelationType());
		
		Reference originalR3 = getReferenceAfterTest(this.originalDc, this.ref3.getRelationType());
		Reference cloneR3 = getReferenceAfterTest(this.cloneDc, this.ref3.getRelationType());
		
		assertNotNull(originalR1.getRelationType(), cloneR1.getRelationType());
		assertNotNull(originalR2.getRelationType(), cloneR2.getRelationType());
		assertNotNull(originalR3.getRelationType(), cloneR3.getRelationType());

		assertNotEquals("originalR1 & cloneR1 Id is Equal",originalR1.getId(), cloneR1.getId());
		assertNotEquals("originalR2 & cloneR2 Id is Equal", originalR2.getId(), cloneR2.getId());
		assertNotEquals("originalR3 & cloneR3 Id is Equal", originalR3.getId(), cloneR3.getId());
	}
	
	@Test
	public void testCompareReferencePagesNumber(){
		
		
		Reference originalR1 = getReferenceAfterTest(this.originalDc, this.ref1.getPagesNumber());
		Reference cloneR1 = getReferenceAfterTest(this.cloneDc, this.ref1.getPagesNumber());
		
		Reference originalR2 = getReferenceAfterTest(this.originalDc, this.ref2.getPagesNumber());
		Reference cloneR2 = getReferenceAfterTest(this.cloneDc, this.ref2.getPagesNumber());
		
		Reference originalR3 = getReferenceAfterTest(this.originalDc, this.ref3.getPagesNumber());
		Reference cloneR3 = getReferenceAfterTest(this.cloneDc, this.ref3.getPagesNumber());
		
		assertNotNull(originalR1.getPagesNumber(), cloneR1.getPagesNumber());
		assertNotNull(originalR2.getPagesNumber(), cloneR2.getPagesNumber());
		assertNotNull(originalR3.getPagesNumber(), cloneR3.getPagesNumber());

		assertNotEquals("originalR1 & cloneR1 Id is Equal",originalR1.getId(), cloneR1.getId());
		assertNotEquals("originalR2 & cloneR2 Id is Equal", originalR2.getId(), cloneR2.getId());
		assertNotEquals("originalR3 & cloneR3 Id is Equal", originalR3.getId(), cloneR3.getId());
	}

	private Reference getReferenceAfterTest(DataCollection dc, String value){
		
		for(Reference r : dc.getReferenceList()){
			
			if(r.getRelationType().equals(value)){
				return r;
			}else if(r.getPagesNumber().equals(value)){
				return r;
			}
		}
		return null;
		
	}

	
	@AfterClass
	public static void endTheTest(){
		System.out.println();
		System.out.println("Data Collection Reference Clone Test DONE");
	}

}
