package de.ianus.access.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import de.ianus.access.web.servlet.PermanentURLServlet;
import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.DisseminationIP;
import de.ianus.ingest.core.bo.WorkflowIP;

public class ApplicationBean {

	private List<DisseminationIP> dipList = new ArrayList<DisseminationIP>();
	
	private static String PROPERTIES_FILES = "ianus-access.properties";
	
	public Properties props = new Properties();
	
	private static final Logger logger = LogManager.getLogger(PermanentURLServlet.class);
	
	
	public ApplicationBean(){
		this.dipList = Services.getInstance().getDaoService().getDipList(WorkflowIP.FINISHED, 10);
		this.loadProperties();
	}
	
	
	public String getContext(){
		return props.getProperty("home.url");
	}
	
	
	public String getStorageDirectory() {
		return props.getProperty("content.path");
	}


	public List<DisseminationIP> getDipList() {
		return dipList;
	}


	public void setDipList(List<DisseminationIP> dipList) {
		this.dipList = dipList;
	}
	
	private static ApplicationBean app;
	
	public static ApplicationBean getInstance(){
	
		if(app == null){
			app = new ApplicationBean();
		}
		return app;
	}
	
	
	public String getVersionedUrl(String path) {
		File file = new File(props.getProperty("document.root") + path);
		if(file.exists()) {
			return getContext() + path + "?v=" + file.lastModified();
		}
		return getContext() + path;
	}
	
	
	private void loadProperties(){
		
		InputStream input = null;
		
		try {
			 input = this.getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILES);
			 
			 if (input == null) {
				System.out.println("Sorry, unable to find " + PROPERTIES_FILES);
				return;
			}
			props.load(input);

			StringBuilder sb = new StringBuilder("Property's List");
			
			for(Object key : props.keySet()){
				sb.append("\n" + key + "=\t" + props.getProperty(key.toString()));
			}
			
			sb.append("\n");
			
			logger.info(sb.toString());
		
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
