package de.ianus.ingest.core.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


public class CoreProperties {
	
	private static final Logger logger = LogManager.getLogger(CoreProperties.class);
	
	private Properties properties = new Properties();
	
	private static final String PROPERTIES_FILE = "ianus-ingest-core.properties";
	
	
	public CoreProperties() {
		this.loadProperties();
	}
	
	
	
	private void loadProperties(){
		try {
			InputStream in = this.getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE);
			this.properties.load(in);
			in.close();
			
			StringBuilder sb = new StringBuilder("Property's List");
			for(Object key : properties.keySet())
				sb.append("\n" + key + "=\t" + properties.getProperty(key.toString()));
			sb.append("\n");
			logger.info(sb.toString());
			
		}catch (IOException e) {
			logger.error("Properties file \'" + PROPERTIES_FILE + " not found.", e);
			e.printStackTrace();
		}
	}
	
	public String get(String key) {
		return this.properties.getProperty(key);
	}
	
	
}
