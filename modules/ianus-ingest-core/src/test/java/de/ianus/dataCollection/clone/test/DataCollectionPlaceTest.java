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
import de.ianus.metadata.bo.utils.Place;


/**
 * This test class is going to test the Place DataCollection Clone. 
 *
 * @author MR
 *
 */
public class DataCollectionPlaceTest {
	
	
	DataCollection originalDc;
	DataCollection H2Dc;
	DataCollection cloneDc;
	
	Place place1;
	Place place2;
	Place place3;

	
	@BeforeClass
	public static void startTheTest(){
		System.out.println("Data Collection Place Clone Test START");
		System.out.println();
	}
	
	/**
	 * Create the Object, initialize the test value and store the value in the H2 database and make it clone.  
	 *
	 */
	@Before
	public void beforeTest() throws Exception{
		
		this.place1 = new Place();
		this.place2 = new Place();
		this.place3 = new Place();
	
		this.originalDc = new DataCollection();
		
		Services.getInstance().getMDService().saveDataCollection(originalDc);
		
		place1.setStreet("Maulbeerallee");
		place1.setDistrict("Berlin");
		place1.setParish("DAI1");
	
		place2.setStreet("Maulbeer");
		place2.setDistrict("Berlin2");
		place2.setParish("DAI2");
		
		place3.setStreet("Maulbeera");
		place3.setDistrict("Berlin3");
		place3.setParish("DAI3");
		
		
		place1.setSource(originalDc);
		place2.setSource(originalDc);
		place3.setSource(originalDc);

		
		Services.getInstance().getMDService().saveEntry(place1);
		Services.getInstance().getMDService().saveEntry(place2);
		Services.getInstance().getMDService().saveEntry(place3);
		
		this.originalDc.getPlaceList().add(place1);
		this.originalDc.getPlaceList().add(place2);
		this.originalDc.getPlaceList().add(place3);
		
		
		H2Dc = Services.getInstance().getMDService().getDataCollection(originalDc.getId());
		
		cloneDc = Services.getInstance().getMDService().cloneDataCollection0(originalDc).getDc();
		cloneDc = Services.getInstance().getMDService().getDataCollection(cloneDc.getId());
		
	}
	
	@Test
	public void testSize(){
		
		assertEquals("Original and H2 Place Size Does not match: ", originalDc.getPlaceList().size(), H2Dc.getPlaceList().size());
		
		assertEquals("Original and Clone Place Size Does not match: ", originalDc.getPlaceList().size(), cloneDc.getPlaceList().size());
		
	}
	
	@Test
	public void testComparePlaceStreet(){
		
		
		Place originalP1 = getPlaseAfterTest(this.originalDc, this.place1.getStreet());
		Place cloneP1 = getPlaseAfterTest(this.cloneDc, this.place1.getStreet());
		
		Place originalP2 = getPlaseAfterTest(this.originalDc, this.place2.getStreet());
		Place cloneP2 = getPlaseAfterTest(this.cloneDc, this.place2.getStreet());
		
		Place originalP3 = getPlaseAfterTest(this.originalDc, this.place3.getStreet());
		Place cloneP3 = getPlaseAfterTest(this.cloneDc, this.place3.getStreet());

		assertNotNull(originalP1.getStreet(), cloneP1.getStreet());
		assertNotNull(originalP2.getStreet(), cloneP2.getStreet());
		assertNotNull(originalP3.getStreet(), cloneP3.getStreet());
		
		assertNotEquals("originalP1 & cloneP1 Id is Equal",originalP1.getId(), cloneP1.getId());
		assertNotEquals("originalP2 & cloneP2 Id is Equal", originalP2.getId(), cloneP2.getId());
		assertNotEquals("originalP3 & cloneP3 Id is Equal", originalP3.getId(), cloneP3.getId());
	}
	
	@Test
	public void testComparePlace(){
		
		
		Place originalP1 = getPlaseAfterTest(this.originalDc, this.place1.getStreet());
		Place cloneP1 = getPlaseAfterTest(this.cloneDc, this.place1.getStreet());
		
		Place originalP2 = getPlaseAfterTest(this.originalDc, this.place2.getStreet());
		Place cloneP2 = getPlaseAfterTest(this.cloneDc, this.place2.getStreet());
		
		Place originalP3 = getPlaseAfterTest(this.originalDc, this.place3.getStreet());
		Place cloneP3 = getPlaseAfterTest(this.cloneDc, this.place3.getStreet());

		assertNotNull(originalP1.getStreet(), cloneP1.getStreet());
		assertNotNull(originalP2.getStreet(), cloneP2.getStreet());
		assertNotNull(originalP3.getStreet(), cloneP3.getStreet());
		
		assertNotEquals("originalP1 & cloneP1 Id is Equal",originalP1.getId(), cloneP1.getId());
		assertNotEquals("originalP2 & cloneP2 Id is Equal", originalP2.getId(), cloneP2.getId());
		assertNotEquals("originalP3 & cloneP3 Id is Equal", originalP3.getId(), cloneP3.getId());
	}
	
	@Test
	public void testComparePlaceDistrict(){
		
		
		Place originalP1 = getPlaseAfterTest(this.originalDc, this.place1.getDistrict());
		Place cloneP1 = getPlaseAfterTest(this.cloneDc, this.place1.getDistrict());
		
		Place originalP2 = getPlaseAfterTest(this.originalDc, this.place2.getDistrict());
		Place cloneP2 = getPlaseAfterTest(this.cloneDc, this.place2.getDistrict());
		
		Place originalP3 = getPlaseAfterTest(this.originalDc, this.place3.getDistrict());
		Place cloneP3 = getPlaseAfterTest(this.cloneDc, this.place3.getDistrict());
		
		assertNotNull(originalP1.getDistrict(), cloneP1.getDistrict());
		assertNotNull(originalP2.getDistrict(), cloneP2.getDistrict());
		assertNotNull(originalP3.getDistrict(), cloneP3.getDistrict());

		assertNotEquals("originalP1 & cloneP1 Id is Equal",originalP1.getId(), cloneP1.getId());
		assertNotEquals("originalP2 & cloneP2 Id is Equal", originalP2.getId(), cloneP2.getId());
		assertNotEquals("originalP3 & cloneP3 Id is Equal", originalP3.getId(), cloneP3.getId());
	}
	

	@Test
	public void testComparePlaceParish(){
		
		
		Place originalP1 = getPlaseAfterTest(this.originalDc, this.place1.getParish());
		Place cloneP1 = getPlaseAfterTest(this.cloneDc, this.place1.getParish());
		
		Place originalP2 = getPlaseAfterTest(this.originalDc, this.place2.getParish());
		Place cloneP2 = getPlaseAfterTest(this.cloneDc, this.place2.getParish());
		
		Place originalP3 = getPlaseAfterTest(this.originalDc, this.place3.getParish());
		Place cloneP3 = getPlaseAfterTest(this.cloneDc, this.place3.getParish());
		
		assertNotNull(originalP1.getParish(), cloneP1.getParish());
		assertNotNull(originalP2.getParish(), cloneP2.getParish());
		assertNotNull(originalP3.getParish(), cloneP3.getParish());

		assertNotEquals("originalP1 & cloneP1 Id is Equal",originalP1.getId(), cloneP1.getId());
		assertNotEquals("originalP2 & cloneP2 Id is Equal", originalP2.getId(), cloneP2.getId());
		assertNotEquals("originalP3 & cloneP3 Id is Equal", originalP3.getId(), cloneP3.getId());
	}

	private Place getPlaseAfterTest(DataCollection dc, String value){
		
		for(Place p : dc.getPlaceList()){
			
			if(p.getStreet().equals(value)){
				return p;
			}else if(p.getDistrict().equals(value)){
				return p;
			}else if(p.getParish().equals(value)){
				return p;
			}
		}
		return null;
		
	}
	
	@AfterClass
	public static void endTheTest(){
		System.out.println();
		System.out.println("Data Collection Place Clone Test DONE");
	}

}
