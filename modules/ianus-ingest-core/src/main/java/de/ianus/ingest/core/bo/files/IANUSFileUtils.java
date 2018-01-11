package de.ianus.ingest.core.bo.files;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.InformationPackage;

public class IANUSFileUtils {

	private static final Logger logger = LogManager.getLogger(IANUSFileUtils.class);
	
	/*
	@Deprecated
	public static void initConceptualFiles(InformationPackage ip) throws Exception{
		
		File dataFolder = new File(ip.getDataFolder()); 
		createConceptualFiles(dataFolder, ip);
	}
	
	private static void createConceptualFiles(File directory, InformationPackage ip) throws Exception{
		
		for(File child : directory.listFiles()){
			if(child.isDirectory()){
				createConceptualFiles(child, ip);
			}else{
				FileConcept cf1 = new FileConcept(ip);
				Services.getInstance().getDaoService().saveDBEntry(cf1);
				
				FileInstance if1 = new FileInstance(cf1, child, ip);
				Services.getInstance().getDaoService().saveDBEntry(if1);		
			}
		}
	}
	*/
	/**
	 * This method is used to to associate a set of files with a common conceptFile.
	 * All these files will have the same conceptFile and the older conceptFile will be deleted, 
	 * if they do not have more fileInstances.
	 * It is worth to mention that a fileInstance can be associated with only one conceptFile.
	 * 
	 * @param files
	 * @throws Exception
	 */
	public static void associateFileInstancesWithConcept(List<FileInstance> files) throws Exception{
		
		for(FileInstance instance : files){
			FileConcept concept = Services.getInstance().getDaoService().getFileConcept(instance.getFileConceptId());
			if(!hasConceptualFileMoreInstances(concept)){
				Services.getInstance().getDaoService().deleteDBEntry(concept);
			}
		}
		
		FileConcept newConcept = new FileConcept();
		Services.getInstance().getDaoService().saveDBEntry(newConcept);
		
		for(FileInstance instance : files){
			instance.setFileConcept(newConcept);
			Services.getInstance().getDaoService().saveDBEntry(instance);
		}
	}
	
	/**
	 * This method is used to to associate a set of files with a common FileGroup.
	 * All these files will have the same FileGroup and the older FileGroup will be deleted, 
	 * if they do not have more fileInstances.
	 * It is worth to mention that a fileInstance can be associated with only one FileGroup.
	 * 
	 * @param files
	 * @throws Exception
	 */
	public static void associateFileInstancesWithGroup(List<FileInstance> files) throws Exception{
		
		for(FileInstance instance : files){
			FileConcept concept = Services.getInstance().getDaoService().getFileConcept(instance.getFileConceptId());
			if(!hasConceptualFileMoreInstances(concept)){
				Services.getInstance().getDaoService().deleteDBEntry(concept);
			}
		}
		
		FileConcept newConcept = new FileConcept();
		Services.getInstance().getDaoService().saveDBEntry(newConcept);
		
		for(FileInstance instance : files){
			instance.setFileConcept(newConcept);
			Services.getInstance().getDaoService().saveDBEntry(instance);
		}
	}
	
	public static void derivateFilesInstance(FileInstance sourceFI, File target, InformationPackage ip) throws Exception{
		FileConcept conceptFile = Services.getInstance().getDaoService().getFileConcept(sourceFI.getFileConceptId());
		if(conceptFile == null){
			throw new Exception("The File instance has no Concept associated. " + sourceFI);
		}
		
		FileInstance targetFI = new FileInstance(conceptFile, target, ip);
		Services.getInstance().getDaoService().saveDBEntry(targetFI);
	}
	
	private static boolean hasConceptualFileMoreInstances(FileConcept concept){
		return (Services.getInstance().getDaoService().getFileInstances(concept).size() > 1);
	}
	
	public static List<FileInstance> getFileInstancesFromDirectory(InformationPackage ip, File directory) throws Exception{
		return collectFileInstances(ip, directory, new ArrayList<FileInstance>());
	}
	
	public static void updateDirectory(InformationPackage ip, List<FileInstance> instanceList, String oldRelativePath, String newRelativePath) throws Exception{
		logger.info("oldRelativePath=" + oldRelativePath + ", newRelativePath=" + newRelativePath);
		for(FileInstance instance : instanceList){
			String location = instance.getLocation().replace(oldRelativePath, newRelativePath);
			instance.setLocation(location);
			Services.getInstance().getDaoService().saveDBEntry(instance);
		}
	}
	
	private static List<FileInstance> collectFileInstances(InformationPackage ip, File directory, List<FileInstance> list) throws Exception{
		for(File child : directory.listFiles()){
			if(child.isDirectory()){
				list = collectFileInstances(ip, child, list);
			}else if(child.isFile()){
				FileInstance fileInstance = Services.getInstance().getDaoService().getFileInstance(ip, child);
				if(fileInstance == null){
					throw new Exception("No FileInstance found for " + child.getAbsolutePath());
				}
				list.add(fileInstance);
			}
		}
		return list;
	}
	
	/**
	 * <p>
	 * This method is used to update the fileInstance of a file whose name or position change. (used by rename and move).
	 * </p>
	 * @param ip
	 * @param source
	 * @param target
	 * @throws Exception
	 */
	public static void updateFile(InformationPackage ip, File source, File target) throws Exception{
		FileInstance fileInstance = Services.getInstance().getDaoService().getFileInstance(ip, source);
		logger.info("Updating the FileInstance= " + fileInstance);
		if(fileInstance != null){
			fileInstance.updateLocation(ip, target);
			Services.getInstance().getDaoService().saveDBEntry(fileInstance);
		}
	}
}
