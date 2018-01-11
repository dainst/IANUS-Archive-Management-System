package de.ianus.ingest.web.jsf;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import de.ianus.ingest.core.bo.InformationPackage;
import de.ianus.ingest.web.pages.FilesPage.FolderContent;

/**
 * <p>
 * This class is used to represent the content of an element (file or folder) in the FilesPage (files.xhtml).
 * A node in the tree of the FilesPage is represented by a TreeNode (a class of primefaces). The extended the class TreeNode creating TreeFile.
 * TreeFile has always an object called data. The object data should contain the relevant information to be able to work with the nodes.
 * This data object is represented by this class. This should be serialized, because it is same in the servlet context. 
 * </p>
 * 
 * @author Jorge Urzua
 *
 */
public class TreeFileData implements Serializable{
	private static final long serialVersionUID = 1200135792862316945L;
	
	private String fileName;
	private String relativePath;
	private Long ipId;
	private String ipPrefix;
	private int filesContent = 0;
	private int foldersContent = 0; 
	private String type;
	
	public TreeFileData(InformationPackage ip, String type, String fileName, String fileAbsolutePath, FolderContent folderContent) throws Exception{
		this.ipId = ip.getId();
		this.ipPrefix = ip.getPrefix();
		this.fileName = fileName;
		this.relativePath = getRelativePath(fileAbsolutePath, ip, fileName);
		this.type = type;
		if(folderContent != null){
			this.filesContent = folderContent.files;
			this.foldersContent = folderContent.folders;	
		}
	}
	
	public boolean isDirectory(){
		return StringUtils.equals("folder", this.type);
	}
	
	/**
	 * this method extracts the absolute path of the IP from the absolute path of the file.
	 * The name of the file is included in the returned path. 
	 * 
	 * @param fileAbsolutePath
	 * @param ip
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	private static String getRelativePath(String fileAbsolutePath, InformationPackage ip, String fileName) throws Exception{
		String relativePath = fileAbsolutePath.replace(ip.getAbsolutePath(), "");
		return relativePath;
		//return relativePath.replace(fileName, "");
	}
	
	public String getLabel() {
		String label = new String(fileName);
		if(StringUtils.equals("folder", this.type)){
			label += " (" + this.foldersContent + " Ordner | " + this.filesContent + " Dateien)";
		}
		return label;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

	public Long getIpId() {
		return ipId;
	}

	public void setIpId(Long ipId) {
		this.ipId = ipId;
	}

	public String getIpPrefix() {
		return ipPrefix;
	}

	public void setIpPrefix(String ipPrefix) {
		this.ipPrefix = ipPrefix;
	}

	public int getFilesContent() {
		return filesContent;
	}

	public void setFilesContent(int filesContent) {
		this.filesContent = filesContent;
	}

	public int getFoldersContent() {
		return foldersContent;
	}

	public void setFoldersContent(int foldersContent) {
		this.foldersContent = foldersContent;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
