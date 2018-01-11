package de.ianus.ingest.core.processEngine.ms.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.lang.StringUtils;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.InformationPackage;

/**
 * ## Javaclass ZipUtils Helper to unzip BagIt files
 * 
 * 
 * ### Summary   
 * Unpack the ziped BagIt into the folders of the Transferpackage:
 * data goes to data/bagItxyz/ 
 * and all other BagIt files go to metadata/metadata-upload/bagItxyz/
 * 
 * ### Methods 
 * 	Unpack the compressed file: public static String unZipBagIt(InformationPackage ip, String unpackedFileName)
 * 
 * @param InformationPackage
 * @param String the name for the uploaded BagIt which is also the name for the folder for saving the data
 * @throws Exception
 * 
 * @return String containing the log information of the unpacking process
 * 
 * @author jurzua
 * @author zoes
 * @see BagitUploadProcessor.java // Unpacking BagIt files during upload process
 * @since 0.1
 *
 * package de.ianus.ingest.core.processEngine.ms.utils;
 * 
 */

public class ZipUtils {

	public static String unZipBagIt(InformationPackage ip, String unpackedFileName) throws Exception{
	
		// String the name for the uploaded BagIt which is also the name for the folder for saving the data
		String unpackedFileNameAsFolder = unpackedFileName.substring(0, unpackedFileName.length() - 4);
		// String containing the log information of the unpacking process
		String result = "";
		
		// Get ziped BagIt file in upload folder of the ip
		File bagitZip = Services.getInstance().getStorageService().getFile(ip.getUploadFolder() + "/" + unpackedFileName);

		// Starting the process of unpacking the zip
		ZipInputStream zis = new ZipInputStream(new FileInputStream(bagitZip));
		result += "Starting to unzip the BagIt: "+ unpackedFileName + "\n in the folder: " + ip.getUploadFolder() + "/" + unpackedFileName +  "\n";
		//get the ziped file list entry
    	ZipEntry ze = zis.getNextEntry();
    	while(ze!=null){
    		String filePathInZip = ze.getName();
    		result += "Found this file Path in zip: "+ filePathInZip + "\n";
     	   	
     	   	String[] array = filePathInZip.split("/");
     	   	String fileName = array[array.length-1];
     	   	
     	   	
     	   	//All files in data folder of the BagIt should be moved to data/bagitxyz/...
      	   	if(array.length >= 2 && StringUtils.equals("data", array[1])){
     	   		String newRelativeFilePath = filePathInZip.replace(array[0] + "/" + array[1] + "/", unpackedFileNameAsFolder + "/");
     	   		result += Services.getInstance().getStorageService().saveFile(zis, ip.getDataFolder(), newRelativeFilePath);
     	   	}else{
         	   	//All other files should be moved to metadata/metadata-upload/bagitxyz/...
     	   		result += Services.getInstance().getStorageService().saveFile(zis, ip.getMetadataUploadFolder(), unpackedFileNameAsFolder + "/" + fileName);
     	   	}
     	
     	   	ze = zis.getNextEntry();
     	}
     	
        zis.closeEntry();
     	zis.close();
     	result += "Finished unzip process.";
     	return result;
	}
	
	
}
