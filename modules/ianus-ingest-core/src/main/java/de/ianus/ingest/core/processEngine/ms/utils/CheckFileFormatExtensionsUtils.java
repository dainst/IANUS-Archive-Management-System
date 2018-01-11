package de.ianus.ingest.core.processEngine.ms.utils;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

/**
 * This class checks all files, compare extensions of files with list of formats. 
 * Output: list of files with formats which are preferred, accepted or not accepted 
 *
 * 
 * @author MR
 *
 */

public class CheckFileFormatExtensionsUtils {
	
	private static Logger logger = Logger.getLogger(CheckFileFormatExtensionsUtils.class);
	
	private String inputAddress;
	
	private final String[] ACCEPTED_FILE_FORMAT = { "pdf", "doc", "xls", "csv", "rtf", "sxw", "png", "jpeg", "jpg","gif", "bmp", "psd", "cpt", "jp2", "jpx", "nef", "crw", "cgm",
													  "dxf", "dwg", "ps", "eps", "ai", "indd", "dwf", "sxc", "json", "mdb", "accdb", "fp5", "fp7", "fmp12", "dbf", "bak", "db", 
													  "dmp", "odb", "mj2", "mp4", "mxf", "mpeg", "mpg", "mov", "asf", "wmv", "ogg", "ogv", "ogx", "ogm", "spx", "flv", "f4v", "mkv",
													  "wav", "acc", "mp3", "aiff", "aif", "wma", "ogg", "oga", "opus", "vrml", "avi", "u3d", "stl", "dxf", "maff", "htm", "html"};
	
	private final String[] PREFERED_FILE_FORMAT = { "odt", "docx" , "txt","xml", "sgml", "html", "dtd", "xsd", "tiff", "tif", "dng", "svg", "tsv", "ods", "xlsx", "siard", "sql",
													  "flac", "wav", "bwf", "x3d", "dae", "obj", "ply", "xhtml", "xht", "mhtml", "mth", "warc"};
	

	public CheckFileFormatExtensionsUtils(String input){
		
		this.inputAddress = input;
	}
	

	/**
	 * Assign the input data to the File and pass these file list to the checkRecursively method
	 * And return it the result with list of files 
	 * @param inputAddress
	 * @return ResultList of all files 
	 */
	public Map<String, List<String>> scanFileList() throws Exception{
		
		Map<String, List<String>> valueList = new HashMap<String, List<String>>();
		
		File dataFolder = new File(this.inputAddress);
		
		checkRecursively(dataFolder, valueList);
		
		return valueList;
		
	}
	
	/**
	 * Receive all files as input data along with an empty Map folder that stores the result of respective file list
	 * 
	 * @param data, fileListMap
	 * @return process and return map list all files and their result. 
	 */
	private void checkRecursively(File data, Map<String, List<String>> fileListMap) throws Exception{
		
		logger.info(data);
		
		String result = null;
		
		if(data.isFile()){
			
			result = checkFileExtenstionAndGiveTheDecision(data);
			
			mapWithDecision(result, fileListMap, data);
			
		}else{
		
			for(File child : data.listFiles()){
				
				if(child.isDirectory()){
					
					checkRecursively(child, fileListMap);
					
				}else{
					
					result = checkFileExtenstionAndGiveTheDecision(child);
					    
					mapWithDecision(result, fileListMap, child);
				}
			}
		}
	}

	/**
	 * Receive individual file as input and return the result of the respective file 
	 * 
	 * @param sourceFile
	 * @return process and return the decision of the file . 
	 */
	private String checkFileExtenstionAndGiveTheDecision(File sourceFile) throws Exception{
		
		logger.info("\t" + sourceFile.getAbsoluteFile());
		
		if (sourceFile == null || !sourceFile.isFile() || !sourceFile.exists()) {
			
			System.out.println("target file '" + sourceFile + "' cannot be found.");
			
	    }
		
		String file = sourceFile.getName().toLowerCase(); 
		
		String extension = FilenameUtils.getExtension(file);
		
		if(Arrays.asList(ACCEPTED_FILE_FORMAT).contains(extension))
			
			return "Files with accepted file formats";
		
		else if(Arrays.asList(PREFERED_FILE_FORMAT).contains(extension))
			
			return "Files with preferred file formats";

		else
			
			return "Files with not accepted file formats";
	}
	
	/**
	 * Take the particular file's result and the respective file as an input and also the empty Map folder to return the map with the result.  
	 * 
	 * @param resultValue, fileListMap, file
	 * @return map with the decision result and return it back.  
	 */
	private void mapWithDecision(String resultValue, Map<String, List<String>> fileListMap, File file) throws Exception{
		
		String testResult = resultValue;
		
		List<String> fileList = fileListMap.get(testResult);
		
		if(null == fileList){
			
			fileList = new LinkedList<>();
		}
		
		fileList.add(file.getAbsolutePath());
		
		fileListMap.put(testResult, fileList);
	}
	
	
	/**
	 * Main method to test the result.
	 * @param str
	 * @return print statement on the console 
	 */
//	public static void main(String[] args) throws Exception {
//		
//		String source = "src/test/resources/accessRestrictionTestFiles/dip_storage/dip_ID/data";
//		
//		CheckFileFormatExtensionsUtils generator = new CheckFileFormatExtensionsUtils(source);
//		
//		Map<String, List<String>> valueList = generator.scanFileList();
//		
//		if(!valueList.isEmpty()){
//		
//			for(String fileList : valueList.keySet()){
//				
//				String key = fileList.toString();
//				
//				System.out.println(key);
//				
//				for(String value : valueList.get(key)){
//					
//					System.out.println(value);
//					
//				}
//				System.out.println();	
//			}
//		}
//	}

}
