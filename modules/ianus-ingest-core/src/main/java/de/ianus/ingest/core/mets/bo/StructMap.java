package de.ianus.ingest.core.mets.bo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.InformationPackage;
import de.ianus.ingest.core.bo.WorkflowIP;
import de.ianus.ingest.core.bo.files.FileConcept;
import de.ianus.ingest.core.bo.files.FileGroup;
import de.ianus.ingest.core.bo.files.FileInstance;
import de.ianus.metadata.bo.DataCollection;
import de.ianus.metadata.xml.XMLObject;

/*
 * structMap: Structural Map. The structural map is the heart of a METS document, 
 * defining the hierarchical arrangement of a primary source document which has been digitized. 
 * This hierarchy is encoded as a tree of 'div' elements. 
 * Any given 'div' can point to another METS document via the 'mptr' element, or to a single file, 
 * to a group of files, or to segments of individual files or groups of files through the 'fptr' and subsidiary elements.
 */

public class StructMap extends XMLObject {

	// The main Element <structMap> is the root element and needed to create a mets document.
	// It may contain <div>
	public StructMap(WorkflowIP ip, DataCollection dc, String type) throws Exception {
		super("mets:structMap");
		

		// ______Attributes for <mets:structMap> Element__________
		
		// ID: an optional XML ID value
		// ID: xsd:ID   optional  
		//this.addAttribute("ID", null);
	
		this.addAttribute("TYPE", type);
		
		//LABEL: an optional attribute providing a title/text string identifying the document for users.
		this.addAttribute("LABEL", type + " file map");

		if (type.equals("physical")) {
			getPhysicalMap(new File(ip.getDataFolder()), this, ip);
		}
		else if (type.equals("logical")) {
			for (XMLObject Elements : getLogicalMap(ip)) {
				this.addDescendant(Elements);
			}	
		}
	}


	
	/**
	 * Helper Method to create XML (Physical StructMap) from Data folder
	 * 
	 * @param String absolutePath 	- the string removed from an absolute path to get the relative path
	 * @param String parentId 		- the parent folder element id
	 * @param File directory 		- the directory to iterate
	 * @param XMLObject result		- the resulting XML
	 * @param InformationPackage ip	- the information package
	 * @return XMLObject
	 * @throws Exception
	 */
	private static void getPhysicalMap(File item, XMLObject result, InformationPackage ip) throws Exception{
		if(item != null && item.exists()) {
			if(item.isDirectory()) {
				// Create XML object representing folder
				XMLObject directory = new XMLObject("div");
					directory.addAttribute("LABEL", item.getName());
					directory.addAttribute("TYPE", "directory");
				result.addDescendant(directory);
				
				for(File descendant : item.listFiles()) {
					// recurse
					getPhysicalMap(descendant, directory, ip);
				}
			}
			else if(item.isFile()) {
				FileInstance fileInstance = Services.getInstance().getDaoService().getFileInstance(ip, item);
				XMLObject file = new XMLObject("div");
					file.addAttribute("LABEL", item.getName());
					file.addAttribute("TYPE", "file");
					XMLObject fptr = new XMLObject("mets:fptr");
						fptr.addAttribute("FILEID", "FILE_" + fileInstance.getId());
					file.addDescendant(fptr);
				result.addDescendant(file);
			}
		}
	}
	
	
	
	/**
	 * Helper Method to create XML (Logical StructMap)
	 * 
	 * @param InformationPackage ip	- the information package
	 * @return ArrayList<XMLObject>
	 */	
	
	private static ArrayList<XMLObject> getLogicalMap(InformationPackage ip) {
		ArrayList<XMLObject> result = new ArrayList<XMLObject>();
		
		List<FileConcept> concepts = Services.getInstance().getDaoService().getFileConceptList(ip);
		for (FileConcept fileConcept : concepts) {
			XMLObject file = new XMLObject("div");
				file.addAttribute("ID", "CONCEPT_" + fileConcept.getId());
				file.addAttribute("TYPE", "representation_versions");
			
				List<FileInstance> fileInstanceList = Services.getInstance().getDaoService().getFileInstances(fileConcept);
				for (FileInstance fileInstance : fileInstanceList) {
					XMLObject fptr = new XMLObject("mets:fptr");
						fptr.addAttribute("FILEID", "FILE_" + fileInstance.getId());
					file.addDescendant(fptr);
				}
			result.add(file);
		}

		List<FileGroup> groups = Services.getInstance().getDaoService().getFileGroupList(ip);
		for (FileGroup fileGroup : groups) {
			XMLObject files = new XMLObject("div");
				files.addAttribute("ID", "GROUP_" + fileGroup.getId());
				files.addAttribute("TYPE", "file_dependencies");
			
				List<FileInstance> fileInstanceList = Services.getInstance().getDaoService().getFileInstances(fileGroup);
				for (FileInstance fileInstance : fileInstanceList) {
					XMLObject fptr = new XMLObject("mets:fptr");
						fptr.addAttribute("FILEID", "FILE_" + fileInstance.getId());
					files.addDescendant(fptr);
				}
			result.add(files);
		}
		
		return result;
	}
	
	
	
	
	
	
	

}