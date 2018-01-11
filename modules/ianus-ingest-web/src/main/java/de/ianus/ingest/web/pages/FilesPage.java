package de.ianus.ingest.web.pages;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.primefaces.event.TreeDragDropEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.InformationPackage;
import de.ianus.ingest.core.bo.WorkflowIP;
import de.ianus.ingest.core.bo.files.FileConcept;
import de.ianus.ingest.core.bo.files.FileGroup;
import de.ianus.ingest.core.bo.files.FileInstance;
import de.ianus.ingest.core.bo.files.FileInstanceProperty;
import de.ianus.ingest.web.jsf.TreeFile;
import de.ianus.ingest.web.jsf.TreeFileData;
import de.ianus.ingest.web.pages.component.IpComponentPage;
import de.ianus.ingest.web.servlet.IANUSServlet;

/**
 * 
 * <p>This class is used to represent files and folder of a IP in the web.
 * The files and folder are represented as a tree, whose root can be the folder of the IP or a sub-folder like: data, upload or metadata.</p>
 * <p>This class exists in the session scope of the ingest-module.</p> 
 * <p>When we want to display a IP, we should call the method load(InformationPackage ip, ViewLevel viewLevel).
 * This method has two parameters:
 * InformationPackage ip: it is the IP that we want to deploy.
 * ViewLevel vl: this parameters indicates which folder will the root of the tree. This class supports four different level: 
 * project that shows the whole project and the sub-folders upload, data, metadata and upload.
 * </p>
 * 
 * @author Jorge Urzua
 *
 */
public class FilesPage extends IpComponentPage {

	private static final Logger logger = LogManager.getLogger(FilesPage.class);
	
	public static final String PAGE_NAME = "files";
	
	//project, data, metadata, upload
	private ViewLevel viewLevel;
	private TreeNode root;
	private String newFileName;
	private String childFolderName;
	private List<Right> rightList = new ArrayList<FilesPage.Right>();
	
	
	private TreeFileData selectedNode;
	private FileItem selectedFile;
	private List<FileItem> selectedDirectoryList;
	
	
	private FileGroup selectedFileGroup = null;
	private Set<FileInstance> tmpFileInstanceListInGroup = new HashSet<FileInstance>();
	
	/**
	 * Always when we want to represent the contain of a IP (files and folders) as a tree in the web, 
	 * we must call this method with the instance of the desired IP. 
	 * 
	 * @param ip the information package that will be displayed
	 * @param viewLevel this parameters indicates the folder that will be the root of the tree representing the project in the web.
	 * @throws Exception if the folder does not exist or the folder can not be opened.
	 */
	public void load(WorkflowIP ip, ViewLevel viewLevel, Right ... rights ) throws Exception{
		super.load(ip);
		this.rightList = Arrays.asList(rights);
		this.load0(ip, viewLevel);
	}
	
	private void load0(WorkflowIP ip, ViewLevel viewLevel) throws Exception{
		long startTime = System.currentTimeMillis();
		this.viewLevel = viewLevel;
		this.ip = ip;
		this.loadRootFile();
		this.selectedNode = null;
		this.newFileName = null;
		this.childFolderName = null;
		this.selectedDirectoryList = null;
		this.selectedFile = null;
		
		//reset group's variables
		this.selectedFileGroup = null;
		this.tmpFileInstanceListInGroup = new HashSet<FileInstance>();
		
		long diff = System.currentTimeMillis() - startTime;
		logger.info("Tree generation Execution time [ms]: " + diff);
	}
	
	public void loadRootFile() throws Exception{
		if(this.ip != null){
			root = new DefaultTreeNode(ip.getId(), null);
			File file = null;
			if(ViewLevel.project.equals(this.viewLevel)){
				file = new File(ip.getAbsolutePath());
			}else if(ViewLevel.data.equals(this.viewLevel)){
				file = new File(ip.getDataFolder());
			}else if(ViewLevel.metadata.equals(this.viewLevel)){
				file = new File(ip.getMetadataFolder());
			}else if(ViewLevel.upload.equals(this.viewLevel)){
				file = new File(ip.getUploadFolder());
			}
			this.loadFolder(root, file);
		}
	}
	
	/**
	 * <p>
	 * This method is called, when we drop a selected file on a folder.
	 * Internally, this action will move the selected file into the new folder.
	 * The movement of the file is executed by the StorageService
	 * </p> 
	 * @param event
	 */
	public void onDragDrop(TreeDragDropEvent event) {
 
		TreeNode dragNode = event.getDragNode();
        TreeNode dropNode = event.getDropNode();
        int dropIndex = event.getDropIndex();
        
        if(((TreeFileData)dropNode.getData()).isDirectory()){
        	//TODO moving folders???
        	TreeFileData source = ((TreeFileData)dragNode.getData());
        	TreeFileData target = ((TreeFileData)dropNode.getData());
        	
        	String sourceRelativePath = source.getRelativePath();
            String targetDirectoryRelativePath = target.getRelativePath();
            
        	try {
        		if(source.isDirectory()){
        			Services.getInstance().getStorageService().moveDirectory(
                    		this.ip, sourceRelativePath, targetDirectoryRelativePath);
        		}else{
        			Services.getInstance().getStorageService().moveFile(
                    		this.ip, sourceRelativePath, targetDirectoryRelativePath);
        		}
                this.load0(this.ip, this.viewLevel);
    		} catch (Exception e) {
    			addInternalError(e);
    		}
            
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Dragged " + dragNode.getData(), "Dropped on " + dropNode.getData() + " at " + dropIndex);
            FacesContext.getCurrentInstance().addMessage(null, message);	
            addMsg("The source " + source.getFileName() + " was moved");
            addMsg("from " + source.getRelativePath().replace(source.getFileName(), ""));
            addMsg("to " + target.getRelativePath());
        }else{
        	addMsg("No executed!");
        	addMsg("The target should be a directory");
        }
        try {
			this.load0(this.ip, viewLevel);
		} catch (Exception e) {
			addInternalError(e);
		}
    }	
	
	public String actionAddSubFolder(){
		try {
			if(this.selectedNode != null && StringUtils.isNotEmpty(this.childFolderName)){
				Services.getInstance().getStorageService().addSubFolder(this.ip, this.selectedNode.getRelativePath(), this.childFolderName);
				this.load0(this.ip, this.viewLevel);	
			}else{
				addMsg("The new name can not be empty");
			}
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	/**
	 * <p>
	 * This method renames the selected file. The rename is executed by the StorageService.
	 * </p>
	 * @return
	 */
	public String actionRenameFile(){
		try {
			if(this.selectedNode != null && StringUtils.isNotEmpty(this.newFileName)){
				Services.getInstance().getStorageService().renameFile(this.ip, this.selectedNode.getRelativePath(), this.selectedNode.getFileName(), this.newFileName);
				this.load0(this.ip, this.viewLevel);	
			}else{
				addMsg("The new name can not be empty");
			}
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	public String actionDeleteFile(){
		try {
			if(this.selectedNode != null){
				// this method will check if the selected node is a file or directory and delete all nested objects and belonging FileInstances recursively
				Services.getInstance().getStorageService().deleteFile(ip, this.selectedNode.getRelativePath());
				this.load0(this.ip, this.viewLevel);
			}else{
				addMsg("No file selected");
			}
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	public String gotoUploadPage(){
		getSession().getUploadPage().load(this.getIp());
		return UploadPage.PAGE_NAME;
	}
	
	/**
	 * 
	 * Lazy loading method.
	 * This method is not recursive, because a IP can be very big.
	 * 
	 * @param rootNode
	 * @param rootFile
	 * @throws Exception
	 */
	private void loadFolder(TreeNode rootNode, File rootFile) throws Exception{
		List<File> files = Services.getInstance().getStorageService().getFiles(rootFile);
		Collections.sort(files);
		for(File file : files){
			if(file.isDirectory()){
				FolderContent folderContent = getContent(file);
				//TreeFile.getDirectoryNode(file.getAbsolutePath(), ip, file.getName(), rootNode, folderContent);
				TreeFile folderNode = TreeFile.getDirectoryNode(file.getAbsolutePath(), ip, file.getName(), rootNode, folderContent);
				this.loadFolder(folderNode, file);
			}else if(file.isFile()){
				TreeFile.getFileNode(file.getAbsolutePath(), ip, file.getName(), rootNode);
			}
		}
	}
	
	private static FolderContent getContent(File rootFile) throws Exception{
		FolderContent content = new FolderContent();
		List<File> files = Services.getInstance().getStorageService().getFiles(rootFile);
		for(File file : files){
			if(file.isDirectory()){
				content.folders++;
				FolderContent contentChild = getContent(file);
				content.folders += contentChild.folders;
				content.files += contentChild.files;
			}else if(file.isFile()){
				content.files++;
			}
		}
		return content;
	}
	
	public static class FolderContent{
		public int files = 0;
		public int folders = 0;
	}
	
	public void listenerSelectFile(ActionEvent event){
		Object obj = getRequestBean("fileNode");
		this.newFileName = "";
		this.selectedNode = (TreeFileData)obj;
		this.selectedFile = null;
		this.selectedDirectoryList = null;
		
		this.selectFile0();
	}
	
	private void selectFile0(){
		try {
			
			String fileAbsolutePath = ip.getAbsolutePath() + this.selectedNode.getRelativePath();
			File selectedFile = new File(fileAbsolutePath);
			
			if(this.selectedNode.getRelativePath().startsWith("/data")){
				if(selectedFile.isFile()){
					loadFile(selectedFile);
				}else if(selectedFile.isDirectory()){
					loadDirectory(selectedFile);
				}
			}
			 
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	public void listenerSelectFileItem(ActionEvent event){
		this.selectedFile = (FileItem)getRequestBean("fileItem");
	}
	
	public void listenerEditGroup(ActionEvent event){
		try {
			FileItem fileItem = (FileItem)getRequestBean("fileItem");
			this.tmpFileInstanceListInGroup = new HashSet<FileInstance>();
			
			if(fileItem.group == null){
				this.selectedFileGroup = new FileGroup();
				this.tmpFileInstanceListInGroup.add(fileItem.instance);
			}else{
				this.selectedFileGroup = fileItem.group;
				for(FileInstance instanceInGroup : this.selectedFileGroup.getFileInstanceList()){
					this.tmpFileInstanceListInGroup.add(instanceInGroup);
				}
			}
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	public void listenerEditGroupFromInstanceDetails(ActionEvent event){
		try {
			this.tmpFileInstanceListInGroup = new HashSet<FileInstance>();
			
			if(this.selectedFile.group == null){
				this.selectedFileGroup = new FileGroup();
				this.tmpFileInstanceListInGroup.add(this.selectedFile.instance);
			}else{
				this.selectedFileGroup = this.selectedFile.group;
				for(FileInstance instanceInGroup : this.selectedFileGroup.getFileInstanceList()){
					this.tmpFileInstanceListInGroup.add(instanceInGroup);
				}
			}
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	public String actionCancelGroupEdition(){
		this.selectedFileGroup = null;
		return PAGE_NAME;
	}
	
	public void listenerAddInstanceToGroup(ActionEvent event){
		FileItem fileItem = (FileItem)getRequestBean("fileItem");
		this.tmpFileInstanceListInGroup.add(fileItem.instance);
	}
	
	public void listenerAddInstanceFromDetailsToGroup(ActionEvent event){
		this.tmpFileInstanceListInGroup.add(this.selectedFile.instance);
	}
	
	public void listenerRemoveInstanceFromGroup(){
		try {
			FileInstance groupInstance = (FileInstance)getRequestBean("groupInstance");
			this.tmpFileInstanceListInGroup.remove(groupInstance);
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	public boolean isEditingGroup(){
		return this.selectedFileGroup != null;
	}
	
	public boolean isShowFilesMetadataPanel(){
		return this.selectedDirectoryList != null || this.selectedFileGroup != null || this.selectedFile != null;
	}
	
	public String actionSaveGroup(){
		try {
			
			this.selectedFileGroup.setIpId(getIp().getId());
			
			if(this.selectedFileGroup.isPersistent()){
				logger.info("Removing old instances from group " + this.selectedFileGroup);
				List<FileInstance> oldInstancesInGroup = Services.getInstance().getDaoService().getFileInstances(this.selectedFileGroup);
				for(FileInstance oldInstance : oldInstancesInGroup){
					if(!listContainsInstance(tmpFileInstanceListInGroup, oldInstance)){
						oldInstance.setFileGroupId(null);
						oldInstance.setFileGroup(null);
						 Services.getInstance().getDaoService().saveDBEntry(oldInstance);
					}
				}
			}
			
			logger.info("Adding instances to group " + this.selectedFileGroup);
			//If the group does not have file instances associated, it should be removed
			if(this.tmpFileInstanceListInGroup.isEmpty()){
				Services.getInstance().getDaoService().deleteDBEntry(this.selectedFileGroup);
				this.selectedFileGroup = null;
			}else{
				Services.getInstance().getDaoService().saveDBEntry(this.selectedFileGroup);
				for(FileInstance fileInstance : this.tmpFileInstanceListInGroup){
					fileInstance.setFileGroup(this.selectedFileGroup);
					Services.getInstance().getDaoService().saveDBEntry(fileInstance);
				}
			}
			
			this.selectFile0();
			
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	private boolean listContainsInstance(Set<FileInstance> list, FileInstance instance){
		for(FileInstance i0 : list){
			if(i0.getId().equals(instance.getId())){
				return true;
			}
		}
		return false;
	}
	
	public void listenerCloseFileInstancePanel(ActionEvent event){
		this.selectedFile = null;
	}
	
	private void loadFile(File file) throws Exception{
		this.selectedFile = new FileItem(ip, file);
	}
	
	private void loadDirectory(File directory) throws Exception{
		this.selectedDirectoryList = new ArrayList<FilesPage.FileItem>();
		for(File child : directory.listFiles()){
			if(child.isFile()){
				this.selectedDirectoryList.add(new FileItem(ip, child));
			}
		}
		Collections.sort(this.selectedDirectoryList);
	}
	
	public String getLinkDisplayFile(){
		if(this.selectedNode != null){
			String link = getAppBean().getContextRoot() + "/rest/" + IANUSServlet.METHOD_RETRIEVE + "/" + this.selectedNode.getIpPrefix() + "/" + 
					this.selectedNode.getIpId() + this.selectedNode.getRelativePath();
			return link;
		}
		return null;
	}
	
	public String getFormatName() {
		List<FileInstanceProperty> list = this.selectedFile.getInstance().getProperties("formatName");
		String result = "";
		int i = 0;
		if(list != null) for(FileInstanceProperty prop : list) {
			if(i > 0) result += ", ";
			result += prop.getValue();
			i++;
		}
		return result;
	}
	
	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}
	
	public TreeFileData getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(TreeFileData selectedNode) {
		this.selectedNode = selectedNode;
	}
	public String getNewFileName() {
		return newFileName;
	}

	public void setNewFileName(String newFileName) {
		this.newFileName = newFileName;
	}

	public String getChildFolderName() {
		return childFolderName;
	}

	public void setChildFolderName(String childFolderName) {
		this.childFolderName = childFolderName;
	}
	
	public boolean isCanMove(){
		return (this.rightList.contains(Right.move));
	}

	public boolean isCanDownload(){
		return (this.rightList.contains(Right.download));
	}
	
	public boolean isCanDelete(){
		return (this.rightList.contains(Right.delete));
	}
	
	public boolean isCanUpdate(){
		return (this.rightList.contains(Right.update));
	}
	
	public String getRightsAsString(){
		String text = new String();
		if(this.rightList != null) {
			for(Right item : this.rightList){
				if(StringUtils.isNotEmpty(text)){
					text += ", ";
				}
				text += item.toString();
			}
		}
		return text;
	}
	
	public boolean isInProgress(){
		return (StringUtils.equals(this.getWfip().getState(), WorkflowIP.IN_PROGRESS));
	}

	public FileItem getSelectedFile() {
		return selectedFile;
	}

	public void setSelectedFile(FileItem selectedFile) {
		this.selectedFile = selectedFile;
	}

	public List<FileItem> getSelectedDirectoryList() {
		return selectedDirectoryList;
	}

	public void setSelectedDirectoryList(List<FileItem> selectedDirectoryList) {
		this.selectedDirectoryList = selectedDirectoryList;
	}

	
	public Set<FileInstance> getTmpFileInstanceListInGroup() {
		return tmpFileInstanceListInGroup;
	}

	public void setTmpFileInstanceListInGroup(Set<FileInstance> tmpFileInstanceListInGroup) {
		this.tmpFileInstanceListInGroup = tmpFileInstanceListInGroup;
	}

	public class FileItem implements Comparable<FileItem>{
		
		FileInstance instance;
		FileConcept concept;
		FileGroup group;
		
		public FileItem(InformationPackage ip, File file) throws Exception{
			this.instance = Services.getInstance().getDaoService().getFileInstance(ip, file);
			if(instance != null){
				this.concept = Services.getInstance().getDaoService().getFileConcept(this.instance.getFileConceptId());
				this.group = Services.getInstance().getDaoService().getFileGroup(this.instance.getFileGroupId());
			}
		}

		public FileInstance getInstance() {
			return instance;
		}

		public FileConcept getConcept() {
			return concept;
		}

		public FileGroup getGroup() {
			return group;
		}
		
		@Override
		public String toString(){
			return "FileItem [" + this.instance + "]";
		}

		@Override
		public int compareTo(FileItem o) {
			if(StringUtils.isNotEmpty(instance.getFileName()) && StringUtils.isNotEmpty(o.instance.getFileName())){
				return instance.getFileName().compareTo(o.instance.getFileName());
			}
			return 0;
		}
	}	

	public FileGroup getSelectedFileGroup() {
		return selectedFileGroup;
	}

	public void setSelectedFileGroup(FileGroup selectedFileGroup) {
		this.selectedFileGroup = selectedFileGroup;
	}

	public enum Right{
		upload, download, move, delete, update
	}

	public enum ViewLevel{
		project, data, metadata, upload
	}
	
	
	
	
	
}
