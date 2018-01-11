package de.ianus.ingest.core.utils;

/**
 * @author Jorge Urzua
 *
 */
public class HibernateUtil {
	/*
    private static SessionFactory sessionFactory;
    
    private static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            // loads configuration and mappings
            Configuration configuration = new Configuration().configure();
            ServiceRegistry serviceRegistry
                = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();
             
            // builds a session factory from the service registry
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);           
        }
         
        return sessionFactory;
    }
    
    public static Session getSession(){
    	Session session;
    	try {
    	    session = getSessionFactory().getCurrentSession();
    	} catch (org.hibernate.HibernateException he) {
    	    session = getSessionFactory().openSession();
    	}
    	return session;
    }*/
}
