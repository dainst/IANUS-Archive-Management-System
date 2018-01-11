package de.ianus.access.web.utils;


import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import de.ianus.access.web.ApplicationBean;
import de.ianus.access.web.SessionBean;
import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.WorkflowIP;

public class CFUtils extends CommonUtils {

	private WorkflowIP dip;
	
	
	public CFUtils(WorkflowIP dip, SessionBean session){
		super(session.getDc(), session);
		this.dip = dip;
	}
	
	
	
	/**
	 * Method to generate JSON Array from the Data Folder
	 * 
	 * @param wfipId
	 * @return JSON Array List
	 * @throws Exception
	 */
	public JSONArray getFolderJsonTree(Long wfipId) {
		JSONArray result = new JSONArray();
		try {
			WorkflowIP wfip = Services.getInstance().getDaoService().getWorkflowIP(wfipId, this.session.getWfipClass());
			File directory = new File(wfip.getDataFolder());
			result = getDirectoryJsonTree(wfip.getAbsolutePath(), "#", directory, result);
			
			//logger.error("This is error : " + dip);
			
		}catch(Exception e){
			
			e.printStackTrace();
		}

		return result;
	}

	
	/**
	 * Helper Method to create JSON Tree from the Data Array
	 * 
	 * @param String absolutePath 	- the string removed from an absolute path to get the relative path
	 * @param String parentId 		- the parent folder element id
	 * @param File directory 		- the directory to iterate
	 * @param JsonArray result		- the resulting JSON tree
	 * @return JSONArray 
	 * @throws Exception
	 */
	private static JSONArray getDirectoryJsonTree(String absolutePath, String parentId, File directory, JSONArray result){
	
		int file = 0;
		int folder = 0;
		
		for(File item : directory.listFiles()){
				
			if(item.isDirectory()){
				
				String folderId = item.getAbsolutePath().replace(absolutePath, "");
					
				JSONObject jsonFolder = new JSONObject();
				jsonFolder.put("id", folderId);
				jsonFolder.put("parent", parentId);
				//folder = folderCounter(child, folder);
				//file = fileCounter(child, file);
				jsonFolder.put("text", item.getName() + " ("+ folderCounter(item, folder) + " Ordner | " + fileCounter(item, file) +  " Dateien)");
				
				result.add(jsonFolder);
				
				getDirectoryJsonTree(absolutePath, folderId, item, result);
			}
		}
		
		return result;
	}
	
	/**
	 * Helper Method to count all Files in the child array
	 * 
	 * @param child, counter
	 * @return fileCount
	 * @throws 
	 */
	private static int fileCounter(File root, int initialFileCount){
		if(root != null) 
			for(File child: root.listFiles()){
				
				if(child.isFile()){
					
					initialFileCount++;
					
				}else if(child.isDirectory()){
					
					initialFileCount= fileCounter(child, initialFileCount);
				}

		}
		return initialFileCount;
	}
	
	public int getFileCount() {
		File root = null;
		try {
			root = new File(this.dip.getDataFolder());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileCounter(root, 0);
	}
	
	/**
	 * Helper Method to count all Folder in the child array
	 * 
	 * @param child, counter
	 * @return folderCount
	 * @throws 
	 */
	private static int folderCounter(File root, int initialFolderCount){
		
		for(File child : root.listFiles()){
			
			if(child.isDirectory()){
				
				initialFolderCount++;
				
				initialFolderCount = folderCounter(child, initialFolderCount);
			}
			
		}
		
		return initialFolderCount; 
	}
	
	
	
	/**
	 * 
	 * Helper method to build the download URL the file
	 * 
	 * @param child
	 * @return fileContent
	 * @throws IOException 
	 */
	public static String getFileURL(String dipFile) {
		return ApplicationBean.getInstance().getContext() + "/components/fileBrowser/downLoadFile.jsp?filePath=" + dipFile;
	}
	
	
	
	/**
	 * Helper method to get an array list of file inside a directory
	 * 
	 * @param directory
	 * @return filesData List
	 * @throws Exception 
	 */
	public List<File> getFileList(String directory) {
		List<File> fileList = new ArrayList<File>();
		File[] files = null;
		try {
			files = FileUtils.getFile(
					this.dip.getAbsolutePath() + "/" + directory).listFiles();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(files != null) for (File file : files) {
			if (!file.isDirectory())
				fileList.add(file);
		}
		
		Collections.sort(fileList);
		return fileList;
	}

	
	
	public static String stripSlashes(String path) {
		return path.replaceAll("^/+", "").replaceAll("/+$", "");
	}
	
	private static String removeRoot(String path) {
		path = stripSlashes(path);
		String _path = "/";
		// remove the root level folder
		if(path.indexOf('/') > 0)
			_path = path.substring(path.indexOf('/')) + "/";
		return _path;
	}
	
	
	
	public String getDataUri(String file) {
		try {
			if(!new File(this.dip.getAbsolutePath()+"/"+file).exists()) return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ApplicationBean.getInstance().getContext() + "/components/fileBrowser/getPreview.jsp?filePath=" + file;
	}
	
	
	
	/**
	 * Helper Method of getFilesInsideSelectedFolder to get the file icon when file is as a PDF format 
	 * 
	 * @param String dipPath	The absolute DIP path.
	 * @param Sring directory	The data path relative to the DIP.
	 * @param String filename	Full filename. 
	 * @return String html		HTML markup, containing the corresponding thumbnail as byte array.
	 * @throws Exception 
	 */
	public String getFileIcon(String directory, String filename) {
		String html = "";
		String uri = null;
		String dipPath = null;
		try {
			dipPath = this.dip.getAbsolutePath();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		directory = removeRoot(directory);
		
		// construct the correct file icon path
		String iconPath = "/web_derivatives" + directory + "thumb_" + filename + ".png";
		
		uri = this.getDataUri(iconPath);
		
		if(uri != null) {
			html = "<img src=\"" + uri + "\">";
		}else{
			// in case no file icon is available, provide a CSS-based icon (eg. if the MicroService failed)
			String fileExtension = getFileExtension(new File(dipPath+"/"+directory+"/"+filename));
			html = "<span class=\"icon\"><span class=\"corner\"></span><span class=\"ext\">" + fileExtension + "</span></span>";
		}
		
		return html;
	}
	
	
	public String getPreview(String directory, String filename, String clientWidth) {
		String html = "";
		String uri = null;
		String data = null;
		String dipPath = null;
		try {
			dipPath = this.dip.getAbsolutePath();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		directory = removeRoot(directory);
		if(clientWidth == null || clientWidth.equals("") || clientWidth.equals("0"))
			clientWidth = "1600";
		if(Integer.parseInt(clientWidth) <= 400) clientWidth = "400";
		if(Integer.parseInt(clientWidth) >= 1600) clientWidth = "1600";
		else clientWidth = "800";
		
		File file = new File(dipPath+"/"+directory+"/"+filename);
		String previewFile = null;
		
		String fileExtension = getFileExtension(file);
		
		switch(fileExtension) {
		case "pdf":
			previewFile = "/data" + directory + file.getName();
			data = getDataUri(previewFile);
			uri = getFileURL(previewFile);
			html = "<object data=\"" + data + "\" type=\"application/pdf\">"
					+ "<p>Your browser does not support embedded objects. Download the file <a href=\"" + uri + "\">here</a></p>."
				+ "</object>";
			break;
		// all video file formats will be converted to mp4 for web preview, but the archived file might be any of these: 
		case "mkv": case "mp4": case "mj2": case "mxf": case "mpg": case "mpeg":
		case "avi": case "mov": case "asf": case "wmv": case "ogg": case "ogv":
		case "ogx": case "ogm": case "spx": case "flv": case "fl4":
			previewFile = "/web_derivatives" + directory + "preview_" + clientWidth + "_" + filename + ".mp4";
			data = getDataUri(previewFile);
			uri = getFileURL("/data" + directory + file.getName());
			html = "<video width=\"" + clientWidth + "\" controls>"
					+ "<source src=\"" + data + "\" type=\"video/mp4\">"
					+ "<p>Your browser does not support MP4 video format. Download the file <a href=\"" + uri + "\">here</a></p>."
				+ "</video>";
			break;
		
		default: 
			// construct the correct path
			previewFile = "/web_derivatives" + directory + "preview_" + clientWidth + "_" + filename + ".png";
			uri = this.getDataUri(previewFile);
			if(uri != null) html = "<img src=\"" + uri + "\">";
		}
		
		// fetch the thumb if no preview is available
		if(!new File(dipPath + previewFile).exists()) {
			html = this.getFileIcon(directory, filename);
			html += "<p>No preview available</p>";
		}
		
		return html;
	}
	
	
	public static String getFileExtension(File file) {
		return FilenameUtils.getExtension(file.getName().toLowerCase());
	}
	
	
	
	public String getFileSystemStats() {
		List<String> result = new ArrayList<String>(); 
		
		Integer count = this.getFileCount();
		if(count > 0)
			result.add(NumberFormat.getNumberInstance(this.getLocale()).format(count) + " digitale Objekte");
		else if(count <= 0 && this.dc.getFileNumber() != null && this.dc.getFileNumber() != 0)
			result.add(NumberFormat.getNumberInstance(this.getLocale()).format(this.dc.getFileNumber()) + " digitale Objekte");
		
		Long size = this.session.getWfip().getDataSize();
		if(size > 0)
			result.add(getReadableFileSize(size));
		else if(!empty(this.dc.getMemorySize()))
			result.add(this.dc.getMemorySize());
		
		if(result.size() > 0)
			return "<p>" + implode(result) + "</p>";
		return "";
	}
	
	
	
	/**
	 * Helper Method to calculate a file size with respect of 1024 bytes
	 * 
	 * @param long size
	 * @return String fileSize
	 * @throws 
	 */
	public static String getReadableFileSize(File file) {
		return getReadableFileSize(file.length());
	}
	
	public static String getReadableFileSize(Integer size) {
		return getReadableFileSize(Long.valueOf(size));
	}
	
	public static String getReadableFileSize(Long size) {
		if(size <= 0) return "0";
	    
	    final String[] units = new String[] { "Byte", "KB", "MB", "GB", "TB", "PB", "EB" };
	    
	    int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
	    
	    return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}
	
}
