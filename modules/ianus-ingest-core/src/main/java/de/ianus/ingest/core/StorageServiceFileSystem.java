package de.ianus.ingest.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import de.ianus.ingest.core.bo.ArchivalIP;
import de.ianus.ingest.core.bo.DisseminationIP;
import de.ianus.ingest.core.bo.InformationPackage;
import de.ianus.ingest.core.bo.Location;
import de.ianus.ingest.core.bo.LocationPurpose;
import de.ianus.ingest.core.bo.SubmissionIP;
import de.ianus.ingest.core.bo.TransferP;
import de.ianus.ingest.core.bo.WorkflowIP;
import de.ianus.ingest.core.bo.files.FileInstance;
import de.ianus.ingest.core.bo.files.IANUSFileUtils;
import de.ianus.ingest.core.processEngine.ms.utils.ZipUtils;
import de.ianus.ingest.core.utils.MemoryUtil;
import de.ianus.metadata.bo.actor.Actor;

/**
 * 
 * 
 * @author Jorge Urzua
 *
 */
public class StorageServiceFileSystem {

	private static final Logger logger = LogManager.getLogger(StorageServiceFileSystem.class);
	
	public Location assignLocation(InformationPackage ip){
		if(ip instanceof TransferP){
			return getLocation(LocationPurpose.TIP_STORAGE);
		}else if(ip instanceof SubmissionIP){
			return getLocation(LocationPurpose.SIP_STORAGE);
		}else if(ip instanceof ArchivalIP){
			return getLocation(LocationPurpose.AIP_STORAGE);
		}else if(ip instanceof DisseminationIP){
			return getLocation(LocationPurpose.DIP_STORAGE);
		}
		return null;
	}
	
	public void cloneActorLogos(WorkflowIP wfip, Map<Actor, Actor> actorMap) throws Exception{
		String actorLogosFolder = wfip.getActorLogosFolder();
		for (Map.Entry<Actor, Actor> entry : actorMap.entrySet()) {
			copyActorLogo(actorLogosFolder, entry.getKey(), entry.getValue());
		}
	}
	
	/**
	 * This method is used when cloning a DataCollection.
	 * For each actor, if the actor has a logo associated, it will be rename with the id of the cloned actor.
	 * 
	 * @param ip
	 * @param actorSource: the original actor
	 * @param actorTarget: the cloned actor
	 * @throws Exception 
	 */
	private void copyActorLogo(String actorLogosFolder, Actor originalActor, Actor clonedActor) throws Exception{
		String originalLogoPath = actorLogosFolder + "/" + originalActor.getId() + ".png";
		File originalLogoFile = new File(originalLogoPath);
		if(originalLogoFile.exists()){
			File clonedLogoFile = new File(actorLogosFolder + "/" + clonedActor.getId() + ".png");
			FileUtils.moveFile(originalLogoFile, clonedLogoFile);
		}
	}
	
	public void addSubFolder(InformationPackage ip, String relativePath, String newFolderName) throws Exception{
		
		String path = (ip.getAbsolutePath().endsWith("/")) ? ip.getAbsolutePath() : ip.getAbsolutePath() + "/";
		path += (relativePath.endsWith("/")) ? relativePath : relativePath + "/";
		path += newFolderName;
		
		logger.info(ip.toString() + "\nrelativePath=" + relativePath + ", newFolderName=" + newFolderName + "\n" + path);
		
		File childFolder = new File(path);
		if(!childFolder.exists()){
			childFolder.mkdirs();
		}
	}
	
	/**
	 * This method ...
	 * @param ip
	 * @param relativePath
	 * @param fileName
	 * @throws Exception You get exceptions when a file or directory cannot be deleted. (java.io.File methods returns a boolean)
	 */
	public void deleteFile(InformationPackage ip, String relativePath) throws Exception{
		String path = (ip.getAbsolutePath().endsWith("/")) ? ip.getAbsolutePath() : ip.getAbsolutePath() + "/";
		path += (relativePath.endsWith("/")) ? relativePath : relativePath + "/";
		
		logger.info(ip.toString() + "\nrelativePath=" + relativePath + "\n" + path);
		
		File fileToDelete = new File(path);
		DAOService dao = Services.getInstance().getDaoService();
		
		// first collect and delete the belonging FileInstanceObjects
		if(fileToDelete.isFile()){
			FileInstance fileInstance = dao.getFileInstance(ip, fileToDelete);
			dao.deleteFileInstance(fileInstance);
		}else if(fileToDelete.isDirectory()) {
			List<FileInstance> list = dao.getFileInstancesInFolder(ip, fileToDelete);
			if(list != null) for(FileInstance fi : list) {
				dao.deleteFileInstance(fi);
			}
		}
		
		// ...then delete the physical file!
		// otherwise, there is no chance to do the isFile/isDir check
		FileUtils.forceDelete(fileToDelete);
	}
	
	/**
	 * <p>
	 * This method is used to move a file to a new place but always within a certain IP.
	 * </p>
	 * @param ip
	 * @param fileSourceRelativePath
	 * @param folderTargetRelativePath
	 * @throws Exception
	 */
	public void moveFile(InformationPackage ip,  String fileSourceRelativePath, String folderTargetRelativePath) throws Exception{
		//logger.info(ip.toString() + "[source="+ fileSourceRelativePath +", target=" + folderTargetRelativePath + "]");
		String ipPath = (ip.getAbsolutePath().endsWith("/")) ? ip.getAbsolutePath() : ip.getAbsolutePath() + "/";
		String sourceAbsolutePath = ipPath + fileSourceRelativePath;
		String targetAbsolutePath = ipPath + folderTargetRelativePath;
		File source = new File(sourceAbsolutePath);
		File target = new File(targetAbsolutePath + "/" + source.getName());
		logger.info(ip.toString() + "[source="+ source.getAbsolutePath() +", target=" + target.getAbsolutePath() + "]");
		FileUtils.moveFile(source, target);
		IANUSFileUtils.updateFile(ip, source, target);
	}
	
	public void moveDirectory(InformationPackage ip,  String fileSourceRelativePath, String folderTargetRelativePath) throws Exception{
		//logger.info(ip.toString() + "[source="+ fileSourceRelativePath +", target=" + folderTargetRelativePath + "]");
		String ipPath = (ip.getAbsolutePath().endsWith("/")) ? ip.getAbsolutePath() : ip.getAbsolutePath() + "/";
		String sourceAbsolutePath = ipPath + fileSourceRelativePath;
		String targetAbsolutePath = ipPath + folderTargetRelativePath;
		File source = new File(sourceAbsolutePath);
		File target = new File(targetAbsolutePath + "/" + source.getName());
		
		logger.info("[" + ip.getClass().getSimpleName() + ", id=" + ip.getId() + "] [source="+ source.getAbsolutePath() +", target=" + target.getAbsolutePath() + "]");
		
		List<FileInstance> fileInstanceList = IANUSFileUtils.getFileInstancesFromDirectory(ip, source);
		FileUtils.moveDirectory(source, target);
		
		//updating the new location in the fileInstances associated to the files of the directory source
		IANUSFileUtils.updateDirectory(ip, fileInstanceList, FileInstance.calculateLocation(ip, source), FileInstance.calculateLocation(ip, target));
	}
	
	/**
	 * <p>This method is used to modify the name of a file without modify its localization.</p>
	 * @param ip
	 * @param relativePath
	 * @param oldFileName
	 * @param newFileName
	 * @throws Exception
	 */
	public void renameFile(InformationPackage ip, String relativePath, String oldFileName, String newFileName) throws Exception{
		String ipPath = (ip.getAbsolutePath().endsWith("/")) ? ip.getAbsolutePath() : ip.getAbsolutePath() + "/";
		
		File source = new File(ipPath + relativePath);
		File target = new File(ipPath + removeLastPartOfPath(relativePath) + newFileName);
		
		logger.info("[" + ip.getClass().getSimpleName() + ", id=" + ip.getId() + "] [source="+ source.getAbsolutePath() +", target=" + target.getAbsolutePath() + "]");
		
		if(source.isFile()){
			FileUtils.moveFile(source, target);
			IANUSFileUtils.updateFile(ip, source, target);
		}else{
			List<FileInstance> fileInstanceList = IANUSFileUtils.getFileInstancesFromDirectory(ip, source);
			FileUtils.moveDirectory(source, target);
			//updating the new location in the fileInstances associated to the files of the directory source
			IANUSFileUtils.updateDirectory(ip, fileInstanceList, FileInstance.calculateLocation(ip, source), FileInstance.calculateLocation(ip, target));
		}
	}
	
	/**
	 * Returns the location that has more space
	 * @return Location
	 */
	public Location getProcessingLocation(){
		return getLocation(LocationPurpose.PROCESSING_STORAGE);
	}
	
	public void deleteUploadedBagItFolderInIP(InformationPackage ip, String BagitFileName) throws Exception{
		// FILE Upload + / + name
		// FOLDER data + / + name (ohne zip) + /
		// FOLDER logs + / + name (ohne zip) + /
		// FOLDER metadata/metadata-upload + / + name (ohne zip) + /
		String folderName = BagitFileName.substring(0, BagitFileName.length()-4);
		
		File uploadedFile = new File(ip.getAbsolutePath() + "/" + "upload/" + BagitFileName);
		File dataFolder = new File(ip.getAbsolutePath() + "/" + "data/" + folderName);
		File logsFolder = new File(ip.getAbsolutePath() + "/" + "logs/" + folderName);
		File metadataFolder = new File(ip.getAbsolutePath() + "/" + "metadata/metadata-upload/" + folderName);


		if(uploadedFile.exists()){
			FileUtils.forceDelete(uploadedFile);
			logger.info("The folder in the IP has been deleted [id=" + ip.getId() + "] " + uploadedFile.getAbsolutePath());
		}
		if(dataFolder.exists()){
			FileUtils.forceDelete(dataFolder);
			logger.info("The folder in the IP has been deleted [id=" + ip.getId() + "] " + dataFolder.getAbsolutePath());
		}
		if(logsFolder.exists()){
			FileUtils.forceDelete(logsFolder);
			logger.info("The folder in the IP has been deleted [id=" + ip.getId() + "] " + logsFolder.getAbsolutePath());
		}
		if(metadataFolder.exists()){
			FileUtils.forceDelete(metadataFolder);
			logger.info("The folder in the IP has been deleted [id=" + ip.getId() + "] " + metadataFolder.getAbsolutePath());
		}
	}
	
	@Deprecated
	public void deleteFolderInIP(InformationPackage ip, String relativeFolderPath) throws Exception{
		File folder = new File(ip.getAbsolutePath() + "/" + relativeFolderPath);
		if(folder.exists()){
			FileUtils.forceDelete(folder);
			logger.info("The folder in the IP has been deleted [id=" + ip.getId() + "] " + folder.getAbsolutePath());
		}
	}
	
	public boolean isFolderEmpty(InformationPackage ip, String relativeFolderPath) throws Exception{
		File folder = new File(ip.getAbsolutePath() + "/" + relativeFolderPath);
		if(folder.exists())
			return folder.list().length == 0;
		return true;
	}
	
	
	public void deletePhysicalInformationPackage(InformationPackage ip){
		try {
			String absolutePath = getAbsolutePath(ip);
			File ipFolder = new File(absolutePath);
			if(ipFolder.exists()){
				FileUtils.forceDelete(ipFolder);
				logger.info("The folder of the IP has been deleted [id=" + ip.getId() + "] " + absolutePath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the location that has more space filtered by propose.
	 * @return Location
	 */
	public Location getLocation(LocationPurpose purpose){
		List<Location> list = Services.getInstance().getDaoService().getLocations();
		
		Location output = null;
		for(Location loc : list){
			if(loc.getPurpose().equals(purpose)){
				if(output == null || loc.getFreeSpaceInBytes() > output.getFreeSpaceInBytes()){
					output = loc;
				}
			}
		}
		return output;
	}
	
	public File getFile(String pathname){
		return new File(pathname);
	}
	
	/**
	 * Saves ZipInputStream objects as files in given path. 
	 * 
	 * @param ZipInputStream that should be saved in order to unpack an zip file.
	 * @param String an absolute URL giving the base location of the upload folder
	 * @param String the name for the uploaded file
	 * @throws Exception
	 * 
	 * @return String containing the information where the Stream is saved as file.
	 * 
	 * @author zoes
	 * @see ZipUtils.java
	 */
	
	public String saveFile(ZipInputStream zis, String absoluteOutputFolder, String fileName) throws IOException{
		
		// This String contains the information about new (unziped) files.
		String result = "";
		
		byte[] buffer = new byte[1024];
		String filePath = absoluteOutputFolder + File.separator + fileName;
		File newFile = new File(filePath);

		result += "Saving unziped file : "+ newFile.getAbsoluteFile() + "\n";
             
        //create all non existing folders
        //else you will hit FileNotFoundException for compressed folder
        new File(newFile.getParent()).mkdirs();
           
        FileOutputStream fos = new FileOutputStream(newFile);             

        int len;
        while ((len = zis.read(buffer)) > 0) {
        	fos.write(buffer, 0, len);
        }
     	fos.close();
     	return result;
	}
	
	public void moveToLocation(InformationPackage ip, LocationPurpose locationPurpose) throws Exception{
		
		Location storageLocation = getLocation(locationPurpose);
		if(storageLocation == null){
			throw new Exception("There is no location of type: " + locationPurpose + " necessary for the execution of this method");
		}
		
		File source = new File(getAbsolutePath(ip));
		ip.setLocation(storageLocation);
		File target = new File(getAbsolutePath(ip));
		
		if(target.exists()){
			FileUtils.forceDelete(target);
		}
		FileUtils.moveDirectory(source, target);
	}
	
	public void copyToLocation(InformationPackage ip, Location locationTarget) throws Exception{
		
		File source = new File(getAbsolutePath(ip));
		File target = new File(locationTarget.getAbsolutePath());
		
		if(target.exists()){
			FileUtils.forceDelete(target);
		}
		FileUtils.copyDirectory(source, target);
	}
	
	public String getAbsolutePath(InformationPackage ip) throws Exception{
		String rootFolder = ip.getLocation().getAbsolutePath();
		rootFolder = (rootFolder.endsWith("/")) ? rootFolder : rootFolder + "/"; 
		rootFolder += ip.getPackageFolderName();
		
		File file = new File(rootFolder);
		if(!file.exists()){
			this.createRootFolders(ip);
		}
		return rootFolder;
	}
	
	/**
	 * Returns an array of strings naming directories in the data folder.
	 * @param ip
	 * @return
	 * @throws Exception
	 */
	public List<String> getFoldersNameInDataFolder(InformationPackage ip) throws Exception{
		
		List<String> folders = new ArrayList<String>();
		
		File dataFolder = new File(ip.getDataFolder());
		for(String file : dataFolder.list()){
			File f0 = new File(ip.getDataFolder() + "/" + file);
			if(f0.isDirectory()){
				folders.add(file);
			}
		}
		return folders;
	}
	
	/**
	 * Returns an array of strings naming directories in the upload folder.
	 * @param ip
	 * @return
	 * @throws Exception
	 */
	public List<String> getFilesNameInUploadFolder(InformationPackage ip) throws Exception{
		List<String> files = new ArrayList<String>();
		
		File dataFolder = new File(ip.getUploadFolder());
		if(dataFolder != null && dataFolder.exists()) for(String file : dataFolder.list()){
			File f0 = new File(ip.getUploadFolder() + "/" + file);
			if(!f0.isDirectory()){
				files.add(file);
			}
		}
		return files;
	}
	
	public List<String> getFilesNameInMetadataFolder(InformationPackage ip) throws Exception{
		List<String> files = new ArrayList<String>();
		
		File dataFolder = new File(ip.getMetadataFolder());
		if(dataFolder != null && dataFolder.exists()) for(String file : dataFolder.list()){
			File f0 = new File(ip.getMetadataFolder() + "/" + file);
			if(!f0.isDirectory()){
				files.add(file);
			}
		}
		return files;
	}
	
	/**
	 * Returns a list of all files in the metadata-upload folder belonging to a specific BagIt file. 
	 * 
	 * @param InformationPackage 
	 * @param String the name for the uploaded-metadata folder of the specific BagIt file
	 * @throws Exception
	 * 
	 * @return a list of all files in metadata-upload folder belonging to a specific BagIt file.
	 * 
	 * @author zoes
	 * @see ZipUtils.java
	 */
	
	public List<String> getFilesNameInMetadataUploadFolder(InformationPackage ip, String unpackedFileNameAsFolder) throws Exception{
		List<String> files = new ArrayList<String>();
		
		File dataFolder = new File(ip.getMetadataUploadFolder() + "/" + unpackedFileNameAsFolder);
		if(dataFolder != null && dataFolder.exists()) for(String file : dataFolder.list()){
			File f0 = new File(ip.getMetadataUploadFolder() + "/" + unpackedFileNameAsFolder + "/" + file);
			if(!f0.isDirectory()){
				files.add(file);
			}
		}
		return files;
	}
	
	/**
	 * Returns the relative path of a file in the temporal folder.
	 * Example:
	 * For this path
	 * /XXXX/YYYY-MM-SS_SIP/data/YYYY-MM-SS_TMP/docs/inform.doc
	 * this
	 *  method returns:
	 * YYYY-MM-SS_TMP/docs/inform.doc
	 * @param sip
	 * @return
	 * @throws Exception
	 */
	public List<String> getRecursiveFilePathInTmpDataFolder(SubmissionIP sip) throws Exception{
		File currentFolder = new File(sip.getTmpFolderInData());
		return getRecursiveFilePathInTmpDataFolder0(sip, currentFolder, new ArrayList<String>());
	}
	
	public List<String> getRecursiveFilePathInDataFolder(InformationPackage ip, String unpackedFileNameAsFolder) throws Exception{
		//String folderName = "_TP";
		//Services.getInstance().getStorageService().createFolder(ip, "/data/" + unpackedFileNameAsFolder + "/" + folderName);
		
		String DataFolderPath = ip.getDataFolder() + "/" + unpackedFileNameAsFolder;
		File currentFolder = new File(DataFolderPath);
		return getRecursiveFilePathInTmpDataFolder0(ip, currentFolder, new ArrayList<String>(), DataFolderPath);
	}
	
	
	private List<String> getRecursiveFilePathInTmpDataFolder0(InformationPackage ip, File currentFolder, List<String> rs, String DataFolderPath) throws Exception{
		
		if(currentFolder != null && currentFolder.exists()) for(String fileName : currentFolder.list()){
			File file = new File( currentFolder.getAbsolutePath() + "/" + fileName);
			if(file.isDirectory()){
				getRecursiveFilePathInTmpDataFolder0(ip, file, rs, DataFolderPath);
			}else if(file.isFile()){
				String relativePath = file.getAbsolutePath().replace(DataFolderPath + "/", "");
				rs.add(relativePath);
			}
		}
		
		return rs;
	}
	
	
	
	private List<String> getRecursiveFilePathInTmpDataFolder0(SubmissionIP sip, File currentFolder, List<String> rs) throws Exception{
		
		if(currentFolder != null && currentFolder.exists()) for(String fileName : currentFolder.list()){
			File file = new File( currentFolder.getAbsolutePath() + "/" +fileName);
			if(file.isDirectory()){
				getRecursiveFilePathInTmpDataFolder0(sip, file, rs);
			}else if(file.isFile()){
				String relativePath = file.getAbsolutePath().replace(sip.getTmpFolderInData() + "/", "");
				rs.add(relativePath);
			}
		}
		
		return rs;
	}
	
	public List<String> getFilesNameInTmpDataFolder(SubmissionIP sip) throws Exception{
		List<String> files = new ArrayList<String>();
		
		File dataFolder = new File(sip.getTmpFolderInData());
		if(dataFolder != null && dataFolder.exists()) for(String file : dataFolder.list()){
			File f0 = new File(sip.getTmpFolderInData() + "/" + file);
			if(!f0.isDirectory()){
				files.add(file);
			}
		}
		return files;
	}

		/**
		 * This method returns all folder names in temporal folder.
		 *
		 * @param sip
		 * @return
		 * @throws Exception
		 *
		 * @author zoes
		 */
		public List<String> getFolderNamesInTmpDataFolder(SubmissionIP sip) throws Exception{
				List<String> folders = new ArrayList<String>();

				File dataFolder = new File(sip.getTmpFolderInData());
				for(String folder : dataFolder.list()){
						File f0 = new File(sip.getTmpFolderInData() + "/" + folder);
						if(f0.isDirectory()){
								folders.add(folder);
						}
				}
				return folders;
		}
	
	private void createRootFolders(InformationPackage ip) throws Exception{
		createFolder(ip, "/data");
		createFolder(ip, "/metadata");
		createFolder(ip, "/logs");
		createFolder(ip, "/upload");
		createFolder(ip, "/tmp");
		if(ip instanceof SubmissionIP || ip instanceof DisseminationIP){
			createFolder(ip, "/web_derivatives");
			createFolder(ip, "/web_assets/");
			createFolder(ip, "/web_assets/actor_logos");
		}else if(ip instanceof ArchivalIP){
			createFolder(ip, "/data/" + ((ArchivalIP)ip).getArchivalFolderName());
		}
	}
	
	public boolean exists(String filePathName){
		File file = new File(filePathName);
		return file.exists();
	}
	
	/**
	 * 
	 * Create a folder or a a path on the top of the information package.
	 * 
	 * @param ip
	 * @param relativeFolder
	 * @throws Exception
	 */
	public void createFolder(InformationPackage ip, String relativeFolder) throws Exception{
		String absolutePath = ip.getLocation().getAbsolutePath(); 
		
		absolutePath = absolutePath.endsWith("/") ? absolutePath : absolutePath + "/";
		
		String rootFolder = absolutePath + ip.getPackageFolderName();
		if(!new File(rootFolder).exists()){
			new File(rootFolder).mkdirs();	
		}
		String SLASH = (!rootFolder.endsWith("/") && !relativeFolder.startsWith("/")) ? "/" : "";
		String newFolder = rootFolder + SLASH + relativeFolder;
		logger.info("Creating new folder: " + newFolder);
		if(!new File(newFolder).mkdirs()){
			throw new Exception("The folder " + newFolder + " could not be created");
		}
	}
	
	public Long getFreeSpaceInBytes(Location loc){
		File file = new File(loc.getAbsolutePath());
		return file.getFreeSpace();
	}
	
	public boolean getCanConnectToLocation(Location loc){
		File file = new File(loc.getAbsolutePath());
		return file.exists() && file.isDirectory();
	}
	
	public String getIPSizeFormatted(InformationPackage ip){
		// 1 Kilobyte = 1,024 Bytes
		// 1 Megabyte = 1,048,576 Bytes
		// 1 Gigabyte = 1,073,741,824 Bytes
		// 1 Terabyte = 1,099,511,627,776 Bytes
		
		try {
			File directory = new File(ip.getAbsolutePath());
			long bytes = FileUtils.sizeOfDirectory(directory);
			
			return MemoryUtil.formatBytes(bytes);	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getDataSizeFormatted(InformationPackage ip){
		try {
			File directory = new File(ip.getAbsolutePath() + "/data");
			long bytes = FileUtils.sizeOfDirectory(directory);
			
			return MemoryUtil.formatBytes(bytes);	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Integer getDataFilesAmount(InformationPackage ip){
		try {
			return getFilesAmount(new File(ip.getAbsolutePath() + "/data"));	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Integer getFilesAmount(File root){
		Integer counter = 0;
		for(File file : root.listFiles()){
			if(file.isDirectory()){
				counter += getFilesAmount(file);
			}else{
				counter++;
			}
		}
		return counter;
	}
	
	public Long getDataSize(InformationPackage ip) {
		File directory = null;
		try {
			directory = new File(ip.getAbsolutePath() + "/data");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(directory.exists())
			return FileUtils.sizeOfDirectory(directory);
		return (long) 0;
	}
	
	public File getFolder(InformationPackage ip) throws Exception{
		File file = new File(ip.getAbsolutePath());
		return file;
	}
	
	public List<File> getFiles(File file) throws Exception{
		
		if(!file.exists()){
			throw new Exception("Trying to get files from a folder that does not exist: " + file.getAbsolutePath());
		}
		if(!file.isDirectory()){
			throw new Exception("Trying to get files from a file that is not a directory: " + file.getAbsolutePath());
		}
		
		File[] files = file.listFiles();
		return Arrays.asList(files);
	}
	
	private static String removeLastPartOfPath(String input){
		String result = new String();
		
		System.out.println("After\t" + input);
		input = input.endsWith("/") && input.length() > 1 ? input.substring(0, input.length() - 2) : input;
		System.out.println("Before\t" + input);
		
		String[] array = input.split("/");
		
		for(int i=1; i<array.length-1; i++){
			result+= "/" + array[i];
		}
		
		return result + "/";
	}
}