package de.ianus.ingest.core.utils;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import de.ianus.ingest.core.DAOService;
import de.ianus.ingest.core.StorageServiceFileSystem;
import de.ianus.ingest.core.bo.Location;
import de.ianus.ingest.core.bo.LocationPurpose;

public class CoreInitialization {

	private static final Logger logger = LogManager.getLogger(CoreInitialization.class);
	//private static String dataFolder = "/data/ianus/";
	private static String[] StorageFolders = { "/sip_storage/", "/aip_storage/", "/dip_storage/", "/tip_storage/",
			"/processing/" };
	private static LocationPurpose[] purposes = { LocationPurpose.SIP_STORAGE, LocationPurpose.AIP_STORAGE,
			LocationPurpose.DIP_STORAGE, LocationPurpose.TIP_STORAGE, LocationPurpose.PROCESSING_STORAGE };
	//private DAOService daoService;

	public static void initializeCore(DAOService daoService, StorageServiceFileSystem storageService, String dataFolder) throws Exception {
		int size = StorageFolders.length;
		for (int i = 0; i < size; i++) {
			createStorageFolder(StorageFolders[i], dataFolder);
			createLocations(daoService, storageService, StorageFolders[i], purposes[i], dataFolder);
		}
	}

	private static void createStorageFolder(String folderName, String dataFolder) throws Exception {

		if (!new File(dataFolder + folderName).exists()) {
			new File(dataFolder + folderName).mkdirs();
		}
		String newFolder = dataFolder + folderName;
		logger.info("Creating new folder: " + newFolder);
		//(new File(newFolder)).mkdirs();
		File file = new File(newFolder); 
		FileUtils.forceMkdir(file);
	}

	private static void createLocations(DAOService daoService, StorageServiceFileSystem storageService, String folderName, LocationPurpose purpose, String dataFolder)
			throws Exception {
		Location loc = new Location();
		loc.setAbsolutePath(dataFolder + folderName);
		loc.setPurpose(purpose);
		if (storageService.getLocation(purpose) == null) {
			daoService.saveDBEntry(loc);
			logger.info("Created location: " + loc.toString());
		}
	}

}
