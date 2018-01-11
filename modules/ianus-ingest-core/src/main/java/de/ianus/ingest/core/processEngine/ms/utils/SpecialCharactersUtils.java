package de.ianus.ingest.core.processEngine.ms.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import de.ianus.ingest.core.DAOService;
import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.ActivityOutput;
import de.ianus.ingest.core.bo.ArchivalIP;
import de.ianus.ingest.core.bo.InformationPackage;
import de.ianus.ingest.core.bo.SubmissionIP;
import de.ianus.ingest.core.bo.WorkflowIP;
import de.ianus.ingest.core.bo.files.FileInstance;


/**
 * This class is responsible for special character recognition and/or replacing in folder an file names.
 * Special characters (and the change values) will be read from tsv file
 *
 * @author zoes
 * @author jurzua
 * @author hendrik
 */

public class SpecialCharactersUtils {
	
	private static final Logger logger = LogManager.getLogger(SpecialCharactersUtils.class);
	
	private static Map<String, String> suggestionMap = new HashMap<String, String>();
	private static Map<String, String> forbiddenMap = new HashMap<String, String>();
	
	private DAOService dao;
	private ActivityOutput output;
	
	static{
		
		String NUMBER_SIGN = "\u002D";	// -
		String LOW_LINE = "\u005F";		// _
		
		
		suggestionMap.put("\u0023", NUMBER_SIGN);	// #	
		suggestionMap.put("\u0028", LOW_LINE);		// (
		suggestionMap.put("\u0029", LOW_LINE);
		suggestionMap.put("\u002B", LOW_LINE);
		suggestionMap.put("\u0040", "\u0061\u0074");
		suggestionMap.put("\u005E", "");
		suggestionMap.put("\u0060", "");
		suggestionMap.put("\u009E", "\u0078");
		suggestionMap.put("\u008E", "\u0041\u0065");
		suggestionMap.put("\u0099", "\u004F\u0065");
		suggestionMap.put("\u009A", "\u0055\u0065");
		suggestionMap.put("\u0084", "\u0061\u0065");
		suggestionMap.put("\u0094", "\u006F\u0065");
		suggestionMap.put("\u0081", "\u0075\u0065");
		suggestionMap.put("\u00DF", "\u0073\u0073");
		suggestionMap.put("\u00A0", "\u0061");
		suggestionMap.put("\u0082", "\u0065");
		suggestionMap.put("\u00A1", "\u0069");
		suggestionMap.put("\u00A2", "\u006F");
		suggestionMap.put("\u00A3", "\u0075");
		suggestionMap.put("\u0085", "\u0061");
		suggestionMap.put("\u008A", "\u0065");
		suggestionMap.put("\u008D", "\u0069");
		suggestionMap.put("\u0095", "\u006F");
		suggestionMap.put("\u0097", "\u0075");
		suggestionMap.put("\u00c4", "\u0041\u0065"); // for Ä suggestion Ae
		suggestionMap.put("\u00d6", "\u004f\u0065"); // for Ö suggestion Oe
		suggestionMap.put("\u00dc", "\u0055\u0065"); // for Ü suggestion Ue
		suggestionMap.put("\u00e4", "\u0061\u0065"); // for ä suggestion ae
		suggestionMap.put("\u00f6", "\u006f\u0065"); // for ö suggestion oe
		suggestionMap.put("\u00fc", "\u0075\u0065"); // for ü suggestion ue
		suggestionMap.put("\u00a7", LOW_LINE); 		// for § suggestion _
		
		
		
		forbiddenMap.put("\u0020", LOW_LINE);
		forbiddenMap.put("\u0021", LOW_LINE);
		forbiddenMap.put("\\u0022", LOW_LINE);	// double quote
		forbiddenMap.put("\u0024", LOW_LINE);
		forbiddenMap.put("\u0025", LOW_LINE);
		forbiddenMap.put("\u0026", "\u002B");
		forbiddenMap.put("\u0027", LOW_LINE);
		forbiddenMap.put("\u002A", LOW_LINE);
		forbiddenMap.put("\u002C", LOW_LINE);
		//forbiddenMap.put("\u002E", LOW_LINE);	// .	(full stop - allowed!)
		//forbiddenMap.put("\u002F", LOW_LINE);	// /	forward slash - allowed because part of pathnames
		forbiddenMap.put("\u003A", LOW_LINE);
		forbiddenMap.put("\u003B", LOW_LINE);
		forbiddenMap.put("\u003C", LOW_LINE);
		forbiddenMap.put("\u003D", LOW_LINE);
		forbiddenMap.put("\u003E", LOW_LINE);
		forbiddenMap.put("\u003F", LOW_LINE);
		forbiddenMap.put("\u005B", LOW_LINE);	
		forbiddenMap.put("\\u005C", LOW_LINE);	// [
		forbiddenMap.put("\u005D", LOW_LINE);
		forbiddenMap.put("\u007C", LOW_LINE);
		forbiddenMap.put("\u007B", LOW_LINE);
		forbiddenMap.put("\u007D", LOW_LINE);
		forbiddenMap.put("\u007E", LOW_LINE);
		
	}
	
	public SpecialCharactersUtils(ActivityOutput output) {
		this.dao = Services.getInstance().getDaoService();
		this.output = output;
	}
	
	public static List<SpecialCharacterResult> recognize(InformationPackage ip) throws Exception{
		List<SpecialCharacterResult> rs = new ArrayList<SpecialCharacterResult>();
		
		File root = new File(ip.getDataFolder());
		rs = recognize0(root, ip.getDataFolder(), rs);
		
		return rs;
	}
	
	public void replaceForbidden(WorkflowIP ip, File file) throws Exception{
		if(file == null) {
			file = new File(ip.getDataFolder());
			if(ip instanceof ArchivalIP) file = new File(ip.getArchiveDataFolder());
		}
		
		if(file.isDirectory()){
			for(File child : file.listFiles()){
				if(child != null) this.replaceForbidden(ip, child);
			}
			if(file.listFiles().length == 0) {
				file.delete();
			}
		}
		else if(file.isFile()) {
			String fileName = file.getAbsolutePath();
			String newName = transformString(fileName, forbiddenMap);
			if(!StringUtils.equals(fileName, newName)){
				this.output.print("\nRenaming file: " + fileName + "\n to: " + newName);
				
				File newFile = new File(newName);
				String path = newFile.getAbsolutePath().replace(newFile.getName(), "");
				new File(path).mkdirs();
				file.renameTo(newFile);
				file.delete();
				
				//update the file instance with the new name
				FileInstance fileInstance = dao.getFileInstance(ip, file);
				if(fileInstance != null) {
					logger.info("Updating FileInstance...");
					fileInstance.setFileName(newFile.getName());
					fileInstance.setLocation(FileInstance.calculateLocation(ip, newFile));
					fileInstance.save(dao);
				}
			}
		}
	}
	
	
	private static List<SpecialCharacterResult> recognize0(File root, String dataFolderAbsolutePath, List<SpecialCharacterResult> rs){
		for(File file : root.listFiles()){
			
			String fileName = file.getName();
			boolean hasSuggestedCharacters = containsCharacters(fileName, suggestionMap.keySet());
			boolean hasForbiddenCharacters = containsCharacters(fileName, forbiddenMap.keySet());
			
			String suggestedFileName = fileName;
			if(hasSuggestedCharacters){
				suggestedFileName = transformString(suggestedFileName, suggestionMap);
			}
			if(hasForbiddenCharacters){
				suggestedFileName = transformString(suggestedFileName, forbiddenMap);
			}
			if(hasSuggestedCharacters || hasForbiddenCharacters){
				String relativePath = file.getAbsolutePath().replace(dataFolderAbsolutePath, "");
				SpecialCharacterResult item = new SpecialCharacterResult(
						fileName, relativePath, suggestedFileName, 
						hasSuggestedCharacters, hasForbiddenCharacters, file.isDirectory());
				rs.add(item);
			}
			
			if(file.isDirectory()){
				recognize0(file, dataFolderAbsolutePath, rs);
			}
		}
		return rs;
	}
	
	public static class SpecialCharacterResult{
		public String fileName;
		public String relativePath;
		public String suggestedFileName;
		public boolean hasSuggestedCharacters;
		public boolean hasForbiddenCharacters;
		public boolean isDirectory;
		
		SpecialCharacterResult(String fileName, String relativePath, String suggestedFileName, boolean hasSuggestedCharacters, boolean hasForbiddenCharacters, boolean isDirectory){
			this.fileName = fileName;
			this.relativePath = relativePath;
			this.suggestedFileName = suggestedFileName;
			this.hasForbiddenCharacters = hasForbiddenCharacters;
			this.hasSuggestedCharacters = hasSuggestedCharacters;
			this.isDirectory = isDirectory;
		}
		
		public String toHtml(){
			StringBuilder sb = new StringBuilder();
			sb.append("<h3>" + fileName + " [" + this.relativePath + "]</h3><br/>");
			
			sb.append((hasForbiddenCharacters) ? "The file name must be changed." : "The file name should be changed<br/>");
			sb.append(" Suggested new name = " + suggestedFileName + "<br/>");
			
			return sb.toString();
		}
	}
	
	private static String transformString(String fileName, Map<String, String> map){
		String suggestedFileName = fileName;
		for(Entry<String, String> entry : map.entrySet()){
			suggestedFileName = suggestedFileName.replace(entry.getKey(), entry.getValue());
		}
		return suggestedFileName;
	}
	
	private static boolean containsCharacters(String fileName, Set<String> set){
		for(String s : set){
			if(fileName.contains(s)){
				return true;
			}
		}
		return false;
	}
	
	
	
	
	

		// All file names in datafolder
		private List<String> fileNames = null;
		Map<String, String> specialCharacterInFileNames = new HashMap<String, String>();

		// All folder names in datafolder
		List<String> folderNames = null;
		Map<String, String> specialCharacterInFolderNames = new HashMap<String, String>();

		// Map of special characters and the appropriate replacements (from SpecialCharactersFile)
		Map<String, String> charactersMap = null;

		// Boolean: true if special characters are found
		boolean containsSC = false;

		// Path to temporal Data Folder -> needed to automatically rename files and folders
		File dataFolder = null;

		// Map of renamed files and folders
		Map<String, String> specialCharacterReplaced = new HashMap<String, String>();
		Map<String, String> specialCharacterFolderReplaced = new HashMap<String, String>();


		// List of Relative File Paths
		List<String> relativeFileNames = null;

		/**
		 * This constructor method creates an object to identify special characters in file and folder names
		 *
		 * @param sip
		 * @param
		 * @throws Exception
		 */
		public SpecialCharactersUtils(SubmissionIP sip, Map<String, String> charactersMap) throws Exception {

				this.fileNames = Services.getInstance().getStorageService().getFilesNameInTmpDataFolder(sip);
				this.folderNames = Services.getInstance().getStorageService().getFolderNamesInTmpDataFolder(sip);
				this.charactersMap = charactersMap;

				this.dataFolder = new File(sip.getTmpFolderInData());
				this.relativeFileNames = Services.getInstance().getStorageService()
						.getRecursiveFilePathInTmpDataFolder(sip);

				this.specialCharacterInFileNames = checkSpecialCharacters(this.fileNames);
				this.specialCharacterInFolderNames = checkSpecialCharacters(this.folderNames);

				//this.specialCharacterReplaced = replaceSpecialCharacters(this.fileNames);
				//this.specialCharacterFolderReplaced = replaceSpecialCharacters(this.folderNames);

		}

		/**
		 * This helper method is used while initializing an object. It returns a list of identified special characters in Stings
		 */
		private Map<String, String> checkSpecialCharacters(List<String> names) throws IOException {

				Map<String, String> specialCharacterInName = new HashMap<String, String>();

				//Nur die Sonderzeichen aus d. Hashmap
				String s = "";
				String FIND = "";
				for (String key : this.charactersMap.keySet()) {

						FIND = s + key;
						s = FIND;
				}


				String n = "";
				for (String name : names) {
						Pattern pattern = Pattern.compile("([" + FIND + "])");
						Matcher m = pattern.matcher(name);
						while (m.find()) {
								// Name in hashmap und Special character als value
								if (!specialCharacterInName.isEmpty()) {
										String o = specialCharacterInName.get(name);
										n = o + ", " + m.group();
								} else {
										n = m.group();
								}
								specialCharacterInName.put(name, n);
								this.containsSC = true;

						}

				}
				return specialCharacterInName;
		}

		/**
		 * This method replaces special characters automatically
		 * Wird nur aufgerufen, wenn es Special Characters gibt, welche automatisch ersetzt werden sollen
		 * Was ist mit der manifest Datei? Pfad auch ersetzen
		 */

		public Map<String, String> replaceSpecialCharacters(List<String> names) throws IOException {

				// Return map with Special Characters and depending file/folder names
				Map<String, String> replace = new HashMap<String, String>();

				// Name in hashmap und Special character als value
				//Nur die Sonderzeichen aus d. Hashmap
				String s = "";
				String FIND = "";
				for (String key : this.charactersMap.keySet()) {

						FIND = s + key;
						s = FIND;
				}

				//Nur die Ersetzungen
				String[] REPLACE = this.charactersMap.values().toArray(new String[charactersMap.size()]);

				for (String name : names) {
						Pattern pattern = Pattern.compile("([" + FIND + "])");

						//relativer Pfad und Dateiname
						String oldPathwithName = name;
						String oldFileName = "";
						//Nur den filename
						String fileName = "";
						String oldPath ="";

						try {
								oldFileName = oldPathwithName.substring(name.lastIndexOf('/') + 1, name.length());
								oldPath = oldPathwithName.substring(0, oldPathwithName.lastIndexOf('/'));
						} catch (Exception e) {
								oldFileName = name;
						}

						Matcher m = pattern.matcher(oldFileName);

						if (m.find()) {
								StringBuffer buffer = new StringBuffer(fileName.length());
								do {
										m.appendReplacement(buffer, REPLACE[FIND.indexOf(m.group())]);
								}

								while (m.find());
								m.appendTail(buffer);
								fileName = buffer.toString();
						}

						//create source File object = new File(dataFolder.toString() + "/" + name);
						File oldName = new File(dataFolder.toString() + "/" + oldPathwithName);

						//create destination File object
						File newFile = new File(dataFolder.toString() + oldPath + "/" + fileName);
						replace.put(oldFileName, fileName);

						if (oldName.renameTo(newFile)) {
								System.out.println("renamed");
						} else {
								System.out.println("Error");
						}

				}

				return replace;

		}

		/**
		 * Setter & Getter
		 */
		public Map<String, String> getSpecialCharacterInFolderNames() {
				return specialCharacterInFolderNames;
		}

		public Map<String, String> getSpecialCharacterInFileNames() {
				return specialCharacterInFileNames;
		}

		public boolean isContainsSC() {
				return containsSC;
		}

		public Map<String, String> getSpecialCharacterReplaced() {
				return specialCharacterReplaced;
		}

		public List<String> getFileNames() {
				return fileNames;
		}

		public List<String> getFolderNames() {
				return folderNames;
		}

		public void setSpecialCharacterReplaced(Map<String, String> specialCharacterReplaced) {
				this.specialCharacterReplaced = specialCharacterReplaced;
		}

		public void setSpecialCharacterFolderReplaced(Map<String, String> specialCharacterFolderReplaced) {
				this.specialCharacterFolderReplaced = specialCharacterFolderReplaced;
		}
}


