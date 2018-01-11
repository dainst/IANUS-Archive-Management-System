package de.ianus.ingest.core;

import de.ianus.metadata.MetadataService;

public class Services {

	private DAOService daoService;
	private StorageServiceFileSystem storageService;
	private ProcessEngineService processEngineService;
	private Testing testing;
	private MetadataService mdService;
	
	
	protected static Services instance;
	
	public static Services getInstance(){
		if(instance == null){
			instance = new Services();
		}
		return instance;
	}
	
	protected Services(){
		this.daoService = new DAOService();
		this.storageService = new StorageServiceFileSystem();
		this.processEngineService = new ProcessEngineService();
		this.testing = new Testing();
		this.mdService = new MetadataService();
	}
	
	

	public DAOService getDaoService() {
		return daoService;
	}

	public StorageServiceFileSystem getStorageService() {
		return storageService;
	}

	public ProcessEngineService getProcessEngineService() {
		return processEngineService;
	}
	
	public Testing getTesting(){
		return testing;
	}
	
	public MetadataService getMDService(){
		return mdService;
	}
}
