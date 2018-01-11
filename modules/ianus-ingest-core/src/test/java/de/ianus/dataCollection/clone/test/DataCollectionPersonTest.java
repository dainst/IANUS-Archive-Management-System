package de.ianus.dataCollection.clone.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.ianus.ingest.core.Services;
import de.ianus.metadata.bo.DataCollection;
import de.ianus.metadata.bo.actor.Actor;
import de.ianus.metadata.bo.actor.Person;



/**
 * This test class is going to test the Person DataCollection Clone. 
 *
 * @author MR
 *
 */
public class DataCollectionPersonTest {
	
	
	DataCollection originalDc;
	DataCollection H2Dc;
	DataCollection cloneDc;
	
	Person person1;
	Person person2;
	Person person3;

	@BeforeClass
	public static void startTheTest(){
		System.out.println("Data Collection Person Clone Test START");
		System.out.println();
	}
	
	/**
	 * Create the Object, initialize the test value and store the value in the H2 database and make it clone.  
	 *
	 */
	@Before
	public void beforeTest() throws Exception{
		
		this.person1 = new Person();
		this.person2 = new Person();
		this.person3 = new Person();

		this.originalDc = new DataCollection();
		
		Services.getInstance().getMDService().saveDataCollection(originalDc);
		
		person1.setFirstName("Jorge");
		person1.setLastName("Urzua");
		person1.setCity("Berlin");
		person1.setCountry("Chili");
		person1.setInstitutionName("University of Dresden");
		
		person2.setFirstName("Hendrik");
		person2.setLastName("Schmeer");
		person2.setCity("Berlin");
		person2.setCountry("German");
		person2.setInstitutionName("Colone University");
		
		person3.setFirstName("Mostafizur");
		person3.setLastName("Rahman");
		person3.setCity("Rajshahi");
		person3.setCountry("Bangladesh");
		person3.setInstitutionName("Hannover University");
		
		person1.setSource(originalDc);
		person2.setSource(originalDc);
		person3.setSource(originalDc);
		
		
		Services.getInstance().getMDService().saveEntry(person1);
		Services.getInstance().getMDService().saveEntry(person2);
		Services.getInstance().getMDService().saveEntry(person3);
		
		
		this.originalDc.getActorMap().put(this.person1.getId(), this.person1);
		this.originalDc.getActorMap().put(this.person2.getId(), this.person2);
		this.originalDc.getActorMap().put(this.person3.getId(), this.person3);

		
		H2Dc = Services.getInstance().getMDService().getDataCollection(originalDc.getId());
		
		cloneDc = Services.getInstance().getMDService().cloneDataCollection0(originalDc).getDc();
		cloneDc = Services.getInstance().getMDService().getDataCollection(cloneDc.getId());
		
		
	}
	
	@Test
	public void testSize(){
		
		assertEquals("Original and H2 Text Attribute Size Does not match: ", originalDc.getPrincipalInvestigatorList().size(), H2Dc.getPrincipalInvestigatorList().size());
		
		assertEquals("Original and Clone Text Attribute Size Does not match: ", originalDc.getPrincipalInvestigatorList().size(), cloneDc.getPrincipalInvestigatorList().size());
		
	}
	
	@Test
	public void testComparePersonFirstName(){
		
		
		Person originalP1 = getPersonAfterTest(this.originalDc, this.person1.getFirstName());
		Person cloneP1 = getPersonAfterTest(this.cloneDc, this.person1.getFirstName());
		
		Person originalP2 = getPersonAfterTest(this.originalDc, this.person2.getFirstName());
		Person cloneP2 = getPersonAfterTest(this.cloneDc, this.person2.getFirstName());
		
		Person originalP3 = getPersonAfterTest(this.originalDc, this.person3.getFirstName());
		Person cloneP3 = getPersonAfterTest(this.cloneDc, this.person3.getFirstName());

		assertEquals("Person1 First Name Not Equal", originalP1.getFirstName(), cloneP1.getFirstName());
		assertEquals("Person2 First Name Not Equal", originalP2.getFirstName(), cloneP2.getFirstName());
		assertEquals("Person3 First Name Not Equal", originalP3.getFirstName(), cloneP3.getFirstName());

		assertNotEquals("originalP1 & cloneP1 Id is Equal",originalP1.getId(), cloneP1.getId());
		assertNotEquals("originalP2 & cloneP2 Id is Equal", originalP2.getId(), cloneP2.getId());
		assertNotEquals("originalP3 & cloneP3 Id is Equal", originalP3.getId(), cloneP3.getId());
	}

	
	@Test
	public void testComparePersonLastName(){
		
		
		Person originalP1 = getPersonAfterTest(this.originalDc, this.person1.getLastName());
		Person cloneP1 = getPersonAfterTest(this.cloneDc, this.person1.getLastName());
		
		Person originalP2 = getPersonAfterTest(this.originalDc, this.person2.getLastName());
		Person cloneP2 = getPersonAfterTest(this.cloneDc, this.person2.getLastName());
		
		Person originalP3 = getPersonAfterTest(this.originalDc, this.person3.getLastName());
		Person cloneP3 = getPersonAfterTest(this.cloneDc, this.person3.getLastName());

		assertEquals("Person1 Last Name Not Equal", originalP1.getLastName(), cloneP1.getLastName());
		assertEquals("Person2 Last Name Not Equal", originalP2.getLastName(), cloneP2.getLastName());
		assertEquals("Person3 Last Name Not Equal", originalP3.getLastName(), cloneP3.getLastName());

		assertNotEquals("originalP1 & cloneP1 Id is Equal",originalP1.getId(), cloneP1.getId());
		assertNotEquals("originalP2 & cloneP2 Id is Equal", originalP2.getId(), cloneP2.getId());
		assertNotEquals("originalP3 & cloneP3 Id is Equal", originalP3.getId(), cloneP3.getId());
	}


	@Test
	public void testComparePersonCity(){
		
		
		Person originalP1 = getPersonAfterTest(this.originalDc, this.person1.getCity());
		Person cloneP1 = getPersonAfterTest(this.cloneDc, this.person1.getCity());
		
		Person originalP2 = getPersonAfterTest(this.originalDc, this.person2.getCity());
		Person cloneP2 = getPersonAfterTest(this.cloneDc, this.person2.getCity());
		
		Person originalP3 = getPersonAfterTest(this.originalDc, this.person3.getCity());
		Person cloneP3 = getPersonAfterTest(this.cloneDc, this.person3.getCity());

		assertEquals("Person1 City Not Equal", originalP1.getCity(), cloneP1.getCity());
		assertEquals("Person2 City Not Equal", originalP2.getCity(), cloneP2.getCity());
		assertEquals("Person3 City Not Equal", originalP3.getCity(), cloneP3.getCity());

		assertNotEquals("originalP1 & cloneP1 Id is Equal",originalP1.getId(), cloneP1.getId());
		assertNotEquals("originalP2 & cloneP2 Id is Equal", originalP2.getId(), cloneP2.getId());
		assertNotEquals("originalP3 & cloneP3 Id is Equal", originalP3.getId(), cloneP3.getId());
	}

	
	@Test
	public void testComparePersonCountry(){
		
		
		Person originalP1 = getPersonAfterTest(this.originalDc, this.person1.getCountry());
		Person cloneP1 = getPersonAfterTest(this.cloneDc, this.person1.getCountry());
		
		Person originalP2 = getPersonAfterTest(this.originalDc, this.person2.getCountry());
		Person cloneP2 = getPersonAfterTest(this.cloneDc, this.person2.getCountry());
		
		Person originalP3 = getPersonAfterTest(this.originalDc, this.person3.getCountry());
		Person cloneP3 = getPersonAfterTest(this.cloneDc, this.person3.getCountry());

		assertEquals("Person1 Country Not Equal", originalP1.getCity(), cloneP1.getCity());
		assertEquals("Person2 Country Not Equal", originalP2.getCity(), cloneP2.getCity());
		assertEquals("Person3 Country Not Equal", originalP3.getCity(), cloneP3.getCity());

		assertNotEquals("originalP1 & cloneP1 Id is Equal",originalP1.getId(), cloneP1.getId());
		assertNotEquals("originalP2 & cloneP2 Id is Equal", originalP2.getId(), cloneP2.getId());
		assertNotEquals("originalP3 & cloneP3 Id is Equal", originalP3.getId(), cloneP3.getId());
	}

	
	@Test
	public void testComparePersonInstitutionName(){
		
		
		Person originalP1 = getPersonAfterTest(this.originalDc, this.person1.getInstitutionName());
		Person cloneP1 = getPersonAfterTest(this.cloneDc, this.person1.getInstitutionName());
		
		Person originalP2 = getPersonAfterTest(this.originalDc, this.person2.getInstitutionName());
		Person cloneP2 = getPersonAfterTest(this.cloneDc, this.person2.getInstitutionName());
		
		Person originalP3 = getPersonAfterTest(this.originalDc, this.person3.getInstitutionName());
		Person cloneP3 = getPersonAfterTest(this.cloneDc, this.person3.getInstitutionName());

		assertEquals("Person1 InstitutionName Not Equal", originalP1.getInstitutionName(), cloneP1.getInstitutionName());
		assertEquals("Person2 InstitutionName Not Equal", originalP2.getInstitutionName(), cloneP2.getInstitutionName());
		assertEquals("Person3 InstitutionName Not Equal", originalP3.getInstitutionName(), cloneP3.getInstitutionName());

		assertNotEquals("originalP1 & cloneP1 Id is Equal",originalP1.getId(), cloneP1.getId());
		assertNotEquals("originalP2 & cloneP2 Id is Equal", originalP2.getId(), cloneP2.getId());
		assertNotEquals("originalP3 & cloneP3 Id is Equal", originalP3.getId(), cloneP3.getId());
	}

	
	private Person getPersonAfterTest(DataCollection dc, String value){
		
		for(Actor a : dc.getActorMap().values()){
			
			Person p = (Person)a;
			
			if(p.getFirstName().equals(value)){
				
				return p;
				
			}else if(p.getLastName().equals(value)){
				
				return p;
			}
			else if(p.getCity().equals(value)){
				
				return p;
				
			}else if(p.getCountry().equals(value)){
				
				return p;
				
			}else if(p.getInstitutionName().equals(value)){
				
				return p;
			}
		}
		return null;
		
	}
	

	@AfterClass
	public static void endTheTest(){
		System.out.println();
		System.out.println("Data Collection Person Clone Test DONE");
	}


}
