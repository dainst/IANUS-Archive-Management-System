package de.ianus.ingest.core.processEngine.ms.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;


/**
 * This micro service make a copy of TP that becomes new SIP then validate new copy with checksum 
 * modify the structure of copied TP according to SIP package definition (initial SIP)
 * 
 * @author MR
 *
 */
public class CreateInitialSIPUtils {
	
	
	private String inputAddress;
	private String outputAddress;

	private String documentationFolder = "documentation";
	private String metadataFolder = "metadata";
	private String seperator = File.separator;

	
	public CreateInitialSIPUtils(String input, String output){
		
		this.inputAddress = input;
		this.outputAddress = output;
	}
	
	
	/**
	 * This method recursively compare source folder and destination folder. 
	 * @param dir
	 * @param imputAddress, outputAddress
	 * @throws IOException
	 */
	public void recurse(String value) throws IOException{
		
		File sourceFolder = new File(inputAddress);
		File destinationFolder = new File(outputAddress);
		
		//FileUtils.copyDirectory(sourceFolder, destinationFolder);
		
		System.out.println("Folder Copy DONE.....");
		System.out.println();
		System.out.println("Compare RESULT.....");

		compareTwoFiles(sourceFolder, destinationFolder);
		
		createDirectroy(this.outputAddress);
		
	}	
	

	/**
	 * This method compare two folder recursively 
	 * @param dir
	 * @param source folder and destination folder
	 * @throws IOException
	 */
	public boolean compareTwoFiles(File d1, File d2) throws IOException{
		
		if(checkLengthAndName(d1, d2)){
			
			
			for(File fd1 : d1.listFiles()){
				
				if(fd1.getName().equals(".DS_Store"))
					continue;
				
				File fd2 = getFileByName(d2, fd1.getName());
				
				if(fd1.isDirectory()){
					
					compareTwoFiles(fd1, fd2);
					
				}else{
					
					String checksumResult1 = checksum(fd1);
					String checksumResult2 = checksum(fd2);
					
					if(!checksumResult1.equals(checksumResult2)){
						
						System.out.println( "File is different: " + fd2.getAbsolutePath() );
		
						System.out.println( "File is different: " + fd1.getAbsolutePath() );
						
					}else{
						
						System.out.println("File is identical: " + fd2.getAbsolutePath());
						
						System.out.println( "File is identical: " + fd1.getAbsolutePath() );
						System.out.println();
					}
					
				}
				
			}
			
		}
		
		return false;
	}
	
	
	/**
	 * This method creates addition directories as metadataFolder and documentationFolder 
	 * @param dir
	 * @param destination path
	 * @throws IOException
	 */
	private void createDirectroy(String path) throws IOException{
		
	
		String dir1 = path + seperator + metadataFolder;
		
		File file1 = new File(dir1);
		
		if(path != null){
			
			if(!file1.exists()){
				
				Files.createDirectories(Paths.get(dir1));
				
				String nextDir = dir1 + seperator + documentationFolder;
				
				File file2 = new File(nextDir);
				
				if(!file2.exists()){

					Files.createDirectories(Paths.get(nextDir));
				}
			}
		}
		System.out.println("Folder Creation DONE......");
	}
	
	

	/**
	 * This method checks the folder name and length
	 * @param dir
	 * @param File d1, d2
	 * @throws
	 */
	private boolean checkLengthAndName(File d1, File d2){
		
		if(d1.listFiles().length != d2.listFiles().length){
			
			return false;
		}
		
		
		for(File fd1 : d1.listFiles()){
			
			if(fd1.getName().equals(".DS_Store"))
				
				continue;
			
			if(getFileByName(d2, fd1.getName()) == null){
				
				return false;
			}
		}
		
		return true;
	}
	

	/**
	 * This method check the File name with corresponding other file name 
	 * @param dir
	 * @param First file name and second folder name
	 * @throws 
	 */
	private File getFileByName(File d2, String fd1Name){
		
		for(File fd2 : d2.listFiles()){
			
			if(fd2.getName().equals(fd1Name)){
				
				return fd2;
				
			}
		}
		return null;
	}
	


	/**
	 * This method works as a checksum with MD5 to check every file 
	 * @param dir
	 * @param file
	 * @throws IOException
	 */
	private String checksum(File file) throws IOException{
		
		try 
		{
		    InputStream fin = new FileInputStream(file);
		    java.security.MessageDigest md5er = MessageDigest.getInstance("MD5");
		    byte[] buffer = new byte[1024];
		    int read;
		    do 
		    {
		    	read = fin.read(buffer);
		    	if (read > 0)
		    		md5er.update(buffer, 0, read);
		    } while (read != -1);
		    fin.close();
		    byte[] digest = md5er.digest();
		    if (digest == null)
		      return null;
		    String strDigest = "0x";
		    for (int i = 0; i < digest.length; i++) 
		    {
		    	strDigest += Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1).toUpperCase();
		    }
		    return strDigest;
		} 
		catch (Exception e) 
		{
		    return null;
		}
		
	}
	
	// Execute from main method using local folder value
	public static void main(String[] args) {
		
		String inputAddress = "/Users/mostafizur/Desktop/Test_Data/original";
		String outputAddress = "/Users/mostafizur/Desktop/Test_Data/b";
	
		
		System.out.println("start the proces to create SIP and validate it.....");
		try {
			new CreateInitialSIPUtils(inputAddress, outputAddress).recurse(null);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
