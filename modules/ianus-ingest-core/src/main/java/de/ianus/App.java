package de.ianus;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.InformationPackage;
import de.ianus.ingest.core.bo.files.FileConcept;
import de.ianus.ingest.core.bo.files.FileGroup;
import de.ianus.ingest.core.bo.files.FileInstance;

/**
 * Hello world!
 *
 */
public class App{
	
	public void createFileSection(InformationPackage ip) throws Exception{
		
		File dataFolder = new File(ip.getDataFolder());
		
		for(File child : dataFolder.listFiles()){
			if(child.isDirectory()){
				//TODO recursive call
			}else if(child.isFile()){
				FileInstance fileInstance = Services.getInstance().getDaoService().getFileInstance(ip, child);
				//TODO create mets:file element
				String fileId = "File_" + fileInstance.getId();
			}
		}
	}
	
	public void createLogicalStructMap(InformationPackage ip){
		
		//TYPE="representation_versions"
		List<FileConcept> list1 = Services.getInstance().getDaoService().getFileConceptList(ip);
		for(FileConcept fileConcept : list1){
			//TODO create mets:div element
			String divId = "LOGIC_" + fileConcept.getId();
			for(FileInstance fileInstance : fileConcept.getFileInstanceList()){
				//TODO create mets:fptr element
				String fptrId = "File_" + fileInstance.getId();
				String type="representation_versions";
			}
		}
		
		//TYPE="file_dependencies"
		List<FileGroup> list2 = Services.getInstance().getDaoService().getFileGroupList(ip);
		for(FileGroup fileGroup : list2){
		//TODO create mets:div element
		String divId = "LOGIC_" + fileGroup.getId();
		for(FileInstance fileInstance : fileGroup.getFileInstanceList()){
			//TODO create mets:fptr element
			String fptrId = "File_" + fileInstance.getId();
			String type="file_dependencies";
			}
		}
	}
	
	public void createPhysicalStructMap(InformationPackage ip) throws Exception{
		
		File dataFolder = new File(ip.getDataFolder());
		
		for(File child : dataFolder.listFiles()){
			if(child.isDirectory()){
				//TODO create mets:div element. id not given 
				
			}else if(child.isFile()){
				FileInstance fileInstance = Services.getInstance().getDaoService().getFileInstance(ip, child);
				//TODO create mets:fptr element
				String fptrId = "File_" + fileInstance.getId();
			}
		}
	}
	
	
	private static Date yesterday() {
	    final Calendar cal = Calendar.getInstance();
	    cal.add(Calendar.DATE, -1);
	    return cal.getTime();
	}
	
    public static void main( String[] args ){
    	
    	String a = new String("a");
    	String b = new String("b");
    	String c = new String("c");
    	
    	Date now = new Date();
    	Date yesterday = yesterday();
    	
    	System.out.println(now.compareTo(yesterday));
    	
    	
    	
    	/*
    	SubmissionIP sip = Services.getInstance().getDaoService().getSip(new Long(8783));
    	List<FileConcept> conceptList = Services.getInstance().getDaoService().getFileConceptList(sip);
    	
    	for(FileConcept concept : conceptList){
    		System.out.println(concept);
    	}*/
    	
    }
}
