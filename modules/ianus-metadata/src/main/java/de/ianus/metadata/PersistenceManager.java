package de.ianus.metadata;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


/**Normally this PersistenceManager class executes the persistence.xml file which is located at 
ianus-ingest-web/src/main/resources/META-INF folder. But when run the DataCollection Clone integration test JUnit with H2 Database 
(ianus-ingest-core/src/test/java/de/ianus/dataCollection/clone/test/Testing.java) then it will automatically 
execute the local persistence.xml file which is located at ianus-ingest-core/src/test/resources/META-INF folder. 
Because both have the same persistence unit name (persistence-unit name="metadataApp").
*/

public class PersistenceManager {
	
	private static EntityManagerFactory emf = null;

	public static EntityManager getEntityManager() {
		return getEntityManagerFactory().createEntityManager();
	}

	public static EntityManagerFactory getEntityManagerFactory() {
		if (emf == null) {
			emf = Persistence.createEntityManagerFactory("metadataApp");
			return emf;
		} else {
			return emf;
		}
	}
}