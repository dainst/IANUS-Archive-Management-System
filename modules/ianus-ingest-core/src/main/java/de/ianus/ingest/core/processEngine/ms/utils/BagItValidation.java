package de.ianus.ingest.core.processEngine.ms.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.InformationPackage;
import de.ianus.ingest.core.processEngine.ms.utils.ChecksumManifest.VerificationResult;

/**
 * ## Javaclass BagItValidation Helper to validate BagIt files
 * 
 * 
 * ### Summary   
 * Validates the BagIt files. Therefore the checksum of all files within the BagIt must be checked and
 * the path of all files are controlled
 * 
 * ### Methods 
 * 	Validate the BagIt file: public static String validate(InformationPackage ip, String unpackedFileName)
 * 
 * @param InformationPackage
 * @param String the name for the uploaded BagIt which is also the name for the folder for saving the data
 * @throws Exception
 * 
 * @return String containing the log information of the unpacking process
 * 
 * @author zoes
 * @see ChecksumManifest.java
 * @see BagitUploadProcessor.java // Validating BagIt files during upload process
 * @since 0.1
 *
 * package de.ianus.ingest.core.processEngine.ms.utils;
 * 
 */

public class BagItValidation {

	public static String validate(InformationPackage ip, String unpackedFileName) throws Exception {
		
		// String the name for the uploaded BagIt which is also the name for the folder for saving the data
		String unpackedFileNameAsFolder = unpackedFileName.substring(0, unpackedFileName.length() - 4);
		String result = "";

		// A list of all files in "metadata-upload/bagitName/* 
		List<File> manifestFileList = new ArrayList<File>(); 
		List<String> fileNameList = Services.getInstance().getStorageService().getFilesNameInMetadataUploadFolder(ip, unpackedFileNameAsFolder);

		for (String fileName : fileNameList) {
			// * starting with "manifest-"
			if (StringUtils.startsWith(fileName, "manifest-")) {
				File file = new File(ip.getMetadataUploadFolder() + "/" + unpackedFileNameAsFolder + "/" + fileName);
				manifestFileList.add(file);
			}
		}
		

		// second list
		List<ChecksumManifest> manifestList = new ArrayList<ChecksumManifest>();

		
		// Check 
		for (File file : manifestFileList) {
			ChecksumManifest cm = new ChecksumManifest(file, ip, unpackedFileNameAsFolder);
			if (cm.verifyConsistency()) {
				manifestList.add(cm);
			} else {
				result += "Manifest file: " + cm.getManifestFile().getName() + " is not consistent \n";

			}
		}
		
		// Check 2

		String DataFolder = ip.getDataFolder() + "/" + unpackedFileNameAsFolder;

		for(String fileRelativePath : Services.getInstance().getStorageService().getRecursiveFilePathInDataFolder(ip, unpackedFileNameAsFolder)){

			File file = Services.getInstance().getStorageService().getFile(DataFolder + "/" + fileRelativePath);

			for(ChecksumManifest manifest : manifestList){
				String relativePath0 = "data/" + fileRelativePath;
				VerificationResult resultS = manifest.verifyFile(file, relativePath0);
				result += resultS.toString() + "\n";
			}
			

		
	}
		return result;
	}}
