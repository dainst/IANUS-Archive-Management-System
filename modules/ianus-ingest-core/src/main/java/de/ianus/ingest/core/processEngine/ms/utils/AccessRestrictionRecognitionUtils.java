package de.ianus.ingest.core.processEngine.ms.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;
import org.jopendocument.dom.spreadsheet.SpreadSheet;
import org.odftoolkit.simple.TextDocument;

import com.itextpdf.text.exceptions.BadPasswordException;
import com.itextpdf.text.pdf.PdfReader;

import de.ianus.ingest.core.bo.ActivityOutput;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

/**
 * This class checks all files, if the file is password protected or not! 
 * In this class, we are considering (PDF, Doc[.doc, .docx], Excel[.xls, .xlsx], open office (.odt, .ods)) and .zip file format. 
 * 
 * @author MR
 *
 */

public class AccessRestrictionRecognitionUtils {
	
	//private static String source = "/Users/mr/Desktop/Test_Data/input";
	private static Logger logger = Logger.getLogger(AccessRestrictionRecognitionUtils.class);
	
	private String inputData;
	private ActivityOutput output = null;
	
	
	public AccessRestrictionRecognitionUtils(String input){
		this.inputData = input;	
	}
	
	public AccessRestrictionRecognitionUtils(String input, ActivityOutput output){
		this.inputData = input;	
		this.output = output;
	}
	
	/**
	 * Scan the files from the dataFolder and Return the class ResultList_RestrictiveFiles level valueFile List 
	 * 
	 * @param str
	 * @return ResultList_RestrictiveFiles valueList
	 */
	public Map<String, String> scanFiles() throws Exception{
		
		Map<String, String> allValueList = new HashMap<String, String>();
		
		File dataFolder = new File(this.inputData);
		
		this.checkRecursively(dataFolder, allValueList);
		
		return allValueList;
	}
	
	/**
	 * Void method to get back the Map value of the file List 
	 * 
	 * @param str
	 * @return 
	 */
	private void checkRecursively(File input, Map<String, String> fileList) throws Exception{
		if(input.exists() && input.canRead()) {
			if(input.isFile()){
				String result = this.checkTheFileType(input);
				if(result != null)
					fileList.put(input.getAbsolutePath(), result);
			}else if(input.isDirectory()){
				for(File child : input.listFiles()){
					this.checkRecursively(child, fileList);
				}
			}
		}
	}
	
	/**
	 * Check the File type and return the respective string result.  
	 * 
	 * @param str
	 * @return String value 
	 */
	private String checkTheFileType(File file) throws Exception {
		
		logger.info("\t" + file.getAbsoluteFile());
		
		if (file == null || !file.isFile() || !file.exists()) {
			System.out.println("Source file '" + file + "' cannot be found.");
	    }
		
		String extension = FilenameUtils.getExtension(file.getName()).toLowerCase();
		
		try {
			if(extension.equals("pdf")){
				return this.checkPdfFile(file) ? "PROTECTED" : "FREE";
			}else if(extension.equals("docx") || extension.equals("xlsx")){
				return checkDocxAndXlsxFile(file) ? "PROTECTED" : "FREE";
			}else if(extension.equals("doc")){
				return checkDocFile(file) ? "PROTECTED" : "FREE";
			}else if(extension.equals("xls")){
				return checkXlsFile(file) ? "PROTECTED" : "FREE";
			}else if(extension.equals("ods")){
				return checkOdsFile(file) ? "PROTECTED" : "FREE";
			}else if(extension.equals("odt")){
				return checkOdtFile(file) ? "PROTECTED" : "FREE";
			}else if(extension.equals("zip")){
				return checkZipFile(file) ? "PROTECTED" : "FREE";
			}
		}catch(Exception e) {
			if(this.output != null) 
				this.output.print("Exception occured: " + e.getMessage() + ", file: " + file.getAbsolutePath());
		}
		
		return  "UNABLE TO CHECK";
	}
	
	/**
	 * Check the PDF File using the iText API
	 * 
	 * @param str
	 * @return boolean result 
	 * @throws IOException 
	 */
	private boolean checkPdfFile(File file) throws Exception{
		
		boolean isValidPdf = true;
		InputStream stream = new FileInputStream(file);
		PdfReader reader = null;
		
		try {
			reader = new PdfReader(stream);
			isValidPdf = reader.isOpenedWithFullPermissions();
		}catch(BadPasswordException e) {
			stream.close();
			return true;	// invalid
		}
		
		if(reader != null) reader.close();
		stream.close();
		
		return !isValidPdf;
		
	}
	
	/**
	 * Check the Microsoft .docx and Excel .xlsx File using the Apache POI API
	 * 
	 * @param str
	 * @return boolean result 
	 * @throws IOException 
	 */
	private static boolean checkDocxAndXlsxFile(File file) throws Exception {

		FileInputStream stream = null;
		
	    try {
	        try {
	        	stream = new FileInputStream(file);
	        	POIFSFileSystem poi = new POIFSFileSystem(stream);
	        	poi.close();
	        } catch (IOException e) {
	        	// if the file can be opened, we would expect the OfficeXmlFileException to occur next?
	        	e.printStackTrace();
	        }
	        stream.close();
	        return true;
	        
	    } catch (OfficeXmlFileException e) {
	    	stream.close();
	        return false;
	        
	    }
	}
	
	/**
	 * Check the Microsoft Excel .xls File using the Apache POI API
	 * 
	 * @param str
	 * @return boolean result 
	 */
	private static boolean checkXlsFile(File file) throws Exception {
		Workbook wb = null;
		FileInputStream stream = new FileInputStream(file);
		
		try {
			wb = new HSSFWorkbook(stream);
		}catch (EncryptedDocumentException e) {
			stream.close();
			if(wb != null) wb.close();
			return true;
		}
		
		stream.close();
		if(wb != null) wb.close();
		return false;
		
	}
	
	/**
	 * Check the .doc File using the Apache POI API
	 * 
	 * @param str
	 * @return boolean result 
	 */
	private  boolean checkDocFile(File file) throws Exception {
	
		FileInputStream stream = new FileInputStream(file);
		
		try {
			HWPFDocument document = new HWPFDocument(stream);
		}catch (EncryptedDocumentException e) {
			stream.close();
			return true;
		}
		
		stream.close();
		return false;
	}
	
	/**
	 * Check the Open Office Excel (.ods) File  using the JODConverter/Apache odf toolkit API
	 * 
	 * @param str
	 * @return boolean result 
	 */
	private static boolean checkOdsFile(File file) throws Exception {
		
		try {
			SpreadSheet sheet = SpreadSheet.createFromFile(file);
		}catch (Exception e) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Check the Open Office text (.odt) File using the Apache odf toolkit API
	 * 
	 * @param str
	 * @return boolean result 
	 */
	private static boolean checkOdtFile(File file) throws Exception{
		
		TextDocument doc = null;
		
		try {
			 doc = TextDocument.loadDocument(file);
			 
		}catch (Exception e) {
			if(doc != null) doc.close();
			return true;
		}
		
		if(doc != null) doc.close();
		return false;
	}
	
	/**
	 * Check the Zip file (.zip) File using the Zip4j API
	 * 
	 * @param str
	 * @return boolean result 
	 * @throws ZipException 
	 */
	private static boolean checkZipFile(File file) throws Exception{
		
		return new ZipFile(file).isEncrypted();
	}
	
	
	


}
