package de.ianus.ingest.core.processEngine.ms.utils;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;



/**
 * This class checks directory file each other. If there are duplicate content
 * and name, those files address will be print.
 * 
 * @author MR
 *
 */
public class CheckFileContentUtils {

	private static final Logger logger = Logger.getLogger(CheckFileContentUtils.class);
	
	/**
	 * Method to scan folder and compare files
	 * 
	 * @param dataFolderPathName
	 * @return List of folder containing duplicate files
	 * @throws Exception
	 */
	public static ResultSetDuplicate scanDuplicateFiles(String dataFolderPathName) throws Exception {
	
		ResultSetDuplicate rs = new ResultSetDuplicate();
		
		File dataFolder = new File(dataFolderPathName);

		long startTime = System.currentTimeMillis();
		
		compareRecursively(dataFolder, rs.rsFileContentComparison, "content");
		compareRecursively(dataFolder, rs.rsFileNameWithExtensionComparison, "name_with_extension");
		compareRecursively(dataFolder, rs.rsFileNameWithoutExtensionComparison, "name_without_extension");
		
		printLog(startTime);
		
		return rs;
	}
	
	/**
	 * Method to store compare files
	 * 
	 * @param 
	 * @return List of folder containing duplicate files
	 * @throws Exception
	 */
	public static class ResultSetDuplicate {
		
		public Map<String, List<String>> rsFileContentComparison = new HashMap<String, List<String>>();
		public Map<String, List<String>> rsFileNameWithoutExtensionComparison = new HashMap<String, List<String>>();
		public Map<String, List<String>> rsFileNameWithExtensionComparison = new HashMap<String, List<String>>();

	}
	
	/**
	 * Method check the time and JVM status
	 * @param startTime, fileNumber
	 * @param folderFileMap
	 * @return
	 * @throws
	 */
	public static void printLog(long startTime){
		
		Runtime runtime = Runtime.getRuntime();

		NumberFormat format = NumberFormat.getInstance();

		StringBuilder sb = new StringBuilder("\tTotal Time to Compare All Files ::: > " + (System.currentTimeMillis() - startTime) / 1000 + " secs\n");
		long maxMemory = runtime.maxMemory();
		long allocatedMemory = runtime.totalMemory();
		long freeMemory = runtime.freeMemory();

		sb.append("\tfree memory: " + format.format(freeMemory / 1024) + "\n");
		sb.append("\tallocated memory: " + format.format(allocatedMemory / 1024) + "\n");
		sb.append("\tmax memory: " + format.format(maxMemory / 1024) + "\n");
		sb.append("\ttotal free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024) + "\n");
		
		System.out.println(sb.toString());
	}
	
	/**
	 * Method recursively scans Directory/SubDirectory for files
	 * 
	 * @param dataFolder
	 * @param folderFileMap
	 * @return folderFileMap Map List
	 * @throws IOException
	 */
	private static void compareRecursively(File dataFolder, Map<String, List<String>> folderFileMap, String algorithmType) throws Exception {
		
		for(File child : dataFolder.listFiles()){
			if(child.isDirectory()){
				compareRecursively(child, folderFileMap, algorithmType);
			}else{
				//String fileEncodeString = encodeFileToMD5Apache(child);
				String fileEncodeString = getEncode(child, algorithmType);
				List<String> fileList = folderFileMap.get(fileEncodeString);
				if(null == fileList){
					fileList = new LinkedList<>();
				}
				fileList.add(child.getAbsolutePath());
				folderFileMap.put(fileEncodeString, fileList);
			}
		}
		
	}
	
	/**
	 * Method to redirect the specific algorithm checking
	 * 
	 * @param file and algorithmType
	 * @param folderFileMap
	 * @return
	 * @throws IOException
	 */
	public static String getEncode(File file, String algorithmType) throws Exception{
		
		if(StringUtils.equals(algorithmType, "content")){
			return encodeFileToMD5ApacheForFileContent(file);
		}else if(StringUtils.equals(algorithmType, "name_with_extension")){
			return encodeFileToMD5ApacheFileNameWithExtenstion(file);
		}else if(StringUtils.equals(algorithmType, "name_without_extension")){
			return encodeFileToMD5ApacheFileNameWithoutExtenstion(file);
		}
		return null;
	}
	
	/**
	 * Method writing the algorithm to build the md5 string respect of the file name with considering the file extension
	 * 
	 * @param file
	 * @return md5 string of the file
	 * @throws IOException
	 */
	public static String encodeFileToMD5ApacheFileNameWithExtenstion(File file) throws Exception{
		
		String fis = file.getName().toLowerCase();
		
		byte data[] = DigestUtils.md5(fis);
		char md5Chars[] = Hex.encodeHex(data);
		String md5 = String.valueOf(md5Chars);
		
		return md5.toString();
	}

	/**
	 * Method writing the algorithm to to build the md5 string respect of the file name without the file extension
	 * 
	 * @param file
	 * @return md5 string of the file
	 * @throws IOException
	 */
	public static String encodeFileToMD5ApacheFileNameWithoutExtenstion(File file) throws Exception{
		
		String f1 = file.getName().toLowerCase();

		String filename = FilenameUtils.getBaseName(f1);
		
		byte data[] = DigestUtils.md5(filename);
		char md5Chars[] = Hex.encodeHex(data);
		String md5 = String.valueOf(md5Chars);
		return md5.toString();
	}
	
	/**
	 * Method writing the algorithm to build the md5 string respect of the file content
	 * 
	 * @param file
	 * @return md5 string of the file
	 * @throws IOException
	 */
	public static String encodeFileToMD5ApacheForFileContent(File file) throws Exception{
		
		FileInputStream fis = new FileInputStream(file);
		byte data[] = DigestUtils.md5(fis);
		char md5Chars[] = Hex.encodeHex(data);
		String md5 = String.valueOf(md5Chars);
		fis.close();
		
		return md5.toString();
	}
	
	
}
