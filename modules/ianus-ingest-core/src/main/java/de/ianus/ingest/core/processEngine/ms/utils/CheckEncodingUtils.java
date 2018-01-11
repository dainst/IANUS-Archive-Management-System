package de.ianus.ingest.core.processEngine.ms.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author MR
 *
 */
public class CheckEncodingUtils {

	/**
	 * @param dataFolderPathName
	 * @return a list of file's and folder's name that have a wrong encoding (differ from UTF8)
	 * @throws Exception
	 * 
	 */
	public static List<String> scanFileFolderNameEncoding(String dataFolderPathName) throws Exception{
		
		List<String> wrongEncodingList = new ArrayList<String>();
		
		File dataFolder = new File(dataFolderPathName);
		
		wrongEncodingList = pullDirectoryContents(dataFolder, wrongEncodingList);
		
		return wrongEncodingList;
	}
	
	/**
	 * This method recursively checks the sub-folders and files
	 * @param dir
	 * @param fileFolderList
	 * @throws IOException
	 */
	private static List<String> pullDirectoryContents(File dataFolder, List<String> wrongEncodingList) throws IOException{
		File [] files = dataFolder.listFiles();
		
		if(null == files){
			return wrongEncodingList;
		}
		
		for(File child : dataFolder.listFiles()){
			if(!encodingUTF8(child.getName())){
				wrongEncodingList.add(child.getAbsolutePath());
			}
			if(child.isDirectory()){
				pullDirectoryContents(child, wrongEncodingList);
			}
		}
		return wrongEncodingList;
	}
	
	/**
	 * This method returns true, if the string is encoded in UTF-8. In other case, it returns false.
	 * @param value
	 * @return boolean result
	 * throws IOException
	 */
	private static boolean encodingUTF8(String value) throws IOException{
		Charset charset = Charset.forName("UTF-8");
		byte [] encode = value.getBytes(charset);
		InputStream stream = new ByteArrayInputStream(encode);
		String decode = decodeFromStream(stream, charset);
		
		return value.equals(decode) ? true : false;
	}
	
	/**
	 * Helper method, decode the String with respect to Charset encoding. 
	 * @param stream
	 * @param encoding
	 * @return 
	 * @throws IOException 
	 */
	private static String decodeFromStream(InputStream stream, Charset encoding) throws IOException{
		StringBuilder builder = new StringBuilder();
		byte[] buffer = new byte[1];
		while(true){
			int r = stream.read(buffer);
			if(r < 0){
				break;
			}
			String data = new String(buffer, 0, r, encoding);
			builder.append(data);
		}
		return builder.toString();
	}
	
	/**
	 * Execute from CheckEncoding.class to the package: de.ianus.ingest.core.processEngine.ms;
	 * 
	 */
//	public static List<String> execute(InformationPackage ip) throws Exception{
//		return scanFileFolderNameEncoding(ip.getAbsolutePath());
//	}
	
	// Execute from main method using local folder value
	public static void main (String ...args) throws Exception{
		List<String> wrongEncodingList = scanFileFolderNameEncoding("/Users/mostafizur/Desktop/test");
		for(String eco : wrongEncodingList){
			System.out.println(eco.toString());
		}
	}
}
