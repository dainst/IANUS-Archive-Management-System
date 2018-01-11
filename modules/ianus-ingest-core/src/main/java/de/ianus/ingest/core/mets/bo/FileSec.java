package de.ianus.ingest.core.mets.bo;

import java.io.File;
import java.util.ArrayList;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.WorkflowIP;
import de.ianus.ingest.core.bo.files.FileInstance;
import de.ianus.metadata.xml.XMLObject;

/*
 * ******************************************************************************************************************
 * The file section (<fileSec>) contains one or more <fileGrp> elements used to group together related files.		*
 * A <fileGrp> lists all of the files which comprise a single electronic version of the digital library object. 	*
 * For example, there might be separate <fileGrp> elements 															*
 * for the thumbnails, the master archival images, the pdf versions, the TEI encoded text versions, etc.			*
 * 																													*
 * The overall purpose of the content file section element <fileSec> is to provide an inventory of 					*
 * and the location for the content files that comprise the digital object being described in the METS document.	*
 * 																													*
 * For IANUS the relevant files are stored in "original" and "archive" folder										*																		
 * ******************************************************************************************************************
 */

public class FileSec extends XMLObject {

	// minOccurs="0"
	// The main Element <fileSec> is the root element and needed to create the file section.
	// It may contain <fileGrp>
	// contained within |	<mets>
	public FileSec(WorkflowIP ip) throws Exception {
		super("mets:fileSec");

		// ______Attributes for <mets:fileSec> Element__________
		
		// ID: an optional XML ID value
		// no need to set an empty ID
		//this.addAttribute("ID", null);
	
		// ______Elements within <mets:fileSec>__________
		
		// Add fileSec from folder data/xxx-original or archive
		this.addDescendant(getFileGroup(ip, "original"));
		this.addDescendant(getFileGroup(ip, "archive"));
		
		
		//this.addDescendant(getFileGroup(ip, "data"));
	}
	
	
	private XMLObject getFileGroup(WorkflowIP ip, String use) throws Exception {
		
		XMLObject ElementFileGrp = new XMLObject("mets:fileGrp");
		
		// ______Attributes <fileGrp>__________

		// ID: an optional XML ID
		// ID: xsd:ID optional
		ElementFileGrp.addAttribute("ID", "FileGrp-" + use);
		
		ElementFileGrp.addAttribute("USE", use);
		
		String root = null;
		switch(use) {
		case "original": root = ip.getOriginalDataFolder(); break;
		case "archive":  root = ip.getArchiveDataFolder();  break;
		}
		
		if(root != null) { 
			File rootDir = new File(root);
			// Generate XML representing all files in the folder	
			if(rootDir.exists() && rootDir.isDirectory()) {
				ElementFileGrp.addDescendants(getDirectoryTree(rootDir, ip));
			}
		}
		return ElementFileGrp;
	}
	
	
	private static ArrayList<XMLObject> getDirectoryTree(File root, WorkflowIP ip) throws Exception{
		ArrayList<XMLObject> result = new ArrayList<XMLObject>();
		for(File item : root.listFiles()) {
			if(item.isDirectory()) {
				ArrayList<XMLObject> tmp = getDirectoryTree(item, ip);
				if(tmp != null) result.addAll(tmp);
			}
			else if(item.isFile()) {
				FileInstance fi = Services.getInstance().getDaoService().getFileInstance(ip, item);
				if(fi != null) {
					XMLObject metsFile = new XMLObject("mets:file");
						metsFile.addAttribute("LABEL", fi.getFileName());
						metsFile.addAttribute("ID", "File_" + fi.getId());
						metsFile.addAttribute("CHECKSUM", fi.getChecksum());
						metsFile.addAttribute("CHECKSUMTYPE", "MD5");
						metsFile.addAttribute("SIZE", fi.getFileBytes().toString());
						if(fi.getMimeType() != null)
							metsFile.addAttribute("MIMETYPE", fi.getMimeType());
						if(fi.getFileCreated() != null)
							metsFile.addAttribute("CREATED", fi.getFileCreated());
						if(fi.getFileLastModified() != null)
							metsFile.addAttribute("lastmodified", fi.getFileLastModified());
						if(fi.getPuid() != null && !fi.getPuid().equals("CONFLICT"))
							metsFile.addAttribute("puid", fi.getPuid());
						// Create XML Object for flocat
						XMLObject fLocat = new XMLObject("mets:FLocat");
							fLocat.addAttribute("LOCTYPE", "OTHER");
							fLocat.addAttribute("OTHERLOCTYPE", "system");
							fLocat.addAttribute("xlink:href", fi.getLocation());
						metsFile.addDescendant(fLocat);
					result.add(metsFile);
				}else{
					throw new Exception("No FileInstance could be found. File: " + item.getAbsolutePath());
				}
			}
		}
		
		return result;
	}

	
	
	
}