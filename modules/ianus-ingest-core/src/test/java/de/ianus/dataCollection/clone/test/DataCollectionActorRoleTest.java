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
import de.ianus.metadata.bo.actor.ActorRole;


/**
 * This test class is going to test the ActorRole DataCollection Clone. 
 *
 * @author MR
 *
 */
public class DataCollectionActorRoleTest {
	
	
	DataCollection originalDc;
	DataCollection H2Dc;
	DataCollection cloneDc;
	
	ActorRole aRole1;
	ActorRole aRole2;
	ActorRole aRole3;

	
	@BeforeClass
	public static void startTheTest(){
		System.out.println("Data Collection ActorRole Clone Test START");
		System.out.println();
	}
	
	/**
	 * Create the Object, initialize the test value and store the value in the H2 database and make it clone.  
	 *
	 */
	@Before
	public void beforeTest() throws Exception{
		
		this.aRole1 = new ActorRole();
		this.aRole2 = new ActorRole();
		this.aRole3 = new ActorRole();
	
		this.originalDc = new DataCollection();
		
		Services.getInstance().getMDService().saveDataCollection(originalDc);
		
		aRole1.setTypeId("IANUS1");
		aRole2.setTypeId("IANUS2");
		aRole3.setTypeId("IANUS3");
		
		aRole1.setSource(originalDc);
		aRole2.setSource(originalDc);
		aRole3.setSource(originalDc);

		
		Services.getInstance().getMDService().saveEntry(aRole1);
		Services.getInstance().getMDService().saveEntry(aRole2);
		Services.getInstance().getMDService().saveEntry(aRole3);
		
		this.originalDc.getRoleList().add(aRole1);
		this.originalDc.getRoleList().add(aRole2);
		this.originalDc.getRoleList().add(aRole3);
		
		
		H2Dc = Services.getInstance().getMDService().getDataCollection(originalDc.getId());
		
		cloneDc = Services.getInstance().getMDService().cloneDataCollection0(originalDc).getDc();
		cloneDc = Services.getInstance().getMDService().getDataCollection(cloneDc.getId());
		
		
	}
	
	@Test
	public void testSize(){
		
		assertEquals("Original and H2 Actor Size Does not match: ", originalDc.getRoleList().size(), H2Dc.getRoleList().size());
		
		assertEquals("Original and Clone Actor Size Does not match: ", originalDc.getRoleList().size(), cloneDc.getRoleList().size());
		
	}
	
	
	@Test
	public void testCompareActorRole(){
		
		
		ActorRole OriginalAR1 = getActorRoleAfterTest(this.originalDc, this.aRole1.getTypeId());
		ActorRole cloneaAR1 = getActorRoleAfterTest(this.cloneDc, this.aRole1.getTypeId());
		
		ActorRole OriginalAR2 = getActorRoleAfterTest(this.originalDc, this.aRole2.getTypeId());
		ActorRole cloneaAR2 = getActorRoleAfterTest(this.cloneDc, this.aRole2.getTypeId());
		
		ActorRole OriginalActorRole3 = getActorRoleAfterTest(this.originalDc, this.aRole3.getTypeId());
		ActorRole cloneaAtorRole3 = getActorRoleAfterTest(this.cloneDc, this.aRole3.getTypeId());
		
		assertNotNull(OriginalAR1.getTypeId(), cloneaAR1.getTypeId());
		assertNotNull(OriginalAR2.getTypeId(), cloneaAR2.getTypeId());


		assertNotEquals("OriginalActorRole & cloneaAtorRole Id is Equal",OriginalAR1.getId(), cloneaAR1.getId());
		assertNotEquals("OriginalActorRole & cloneaAtorRole Id is Equal",OriginalAR2.getId(), cloneaAR2.getId());
		assertNotEquals("OriginalActorRole & cloneaAtorRole Id is Equal",OriginalActorRole3.getId(), cloneaAtorRole3.getId());

	}

	private ActorRole getActorRoleAfterTest(DataCollection dc, String value){
		
		for(ActorRole ar : dc.getRoleList()){
			
			if(ar.getTypeId().equals(value)){
				
				return ar;
			}
		}
		return null;
	}
	
	@AfterClass
	public static void endTheTest(){
		System.out.println();
		System.out.println("Data Collection ActorRole Clone Test DONE");
	}

}
