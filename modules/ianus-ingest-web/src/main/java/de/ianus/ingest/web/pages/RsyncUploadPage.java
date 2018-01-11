package de.ianus.ingest.web.pages;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;

import javax.faces.event.ActionEvent;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import de.ianus.ingest.core.DAOService;
import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.InformationPackage;
import de.ianus.ingest.core.bo.RsyncUpload;
import de.ianus.ingest.core.bo.WorkflowIP;
import de.ianus.ingest.core.bo.ums.User;
import de.ianus.ingest.core.processEngine.ms.utils.RsyncUploadVirusScanProcessor;
import de.ianus.ingest.web.jsf.TreeFile;
import de.ianus.ingest.web.jsf.TreeFileData;
import de.ianus.ingest.web.pages.FilesPage.FolderContent;
import de.ianus.ingest.web.pages.FilesPage.ViewLevel;
import de.ianus.ingest.web.pages.component.IpComponentPage;

public class RsyncUploadPage extends IpComponentPage {
	
	
	private static final Logger logger = LogManager.getLogger(RsyncUploadPage.class);
	
	public static String PAGE_NAME = "rsyncUpload";
	
	
	private ViewLevel viewLevel;
	private TreeNode root;
	private TreeFileData selectedNode;

	private String path = null;
	
	private DAOService daoService = Services.getInstance().getDaoService();
	

	
	public TreeNode getRoot() {
		return this.root;
	}
	
	public String getPath() {
		return this.path;
	}
	
	public String getRemotePath() throws Exception {
		RsyncUpload upload = (RsyncUpload)this.getRequestBean("upload");
		String ssh = upload.getUserName() + "@" + this.getIpAdress();
		return ssh + ":" + this.getIp().getAbsolutePath() + upload.getTarget();
	}
	
	private String getIpAdress() throws UnknownHostException {
		return this.getAppBean().getProperty("server.ip");
		/*
		String str = InetAddress.getLocalHost().toString();
		String[] split = str.split("/");
		if(split.length == 2) str = split[1];
		return str;
		*/
	}
	
	public Boolean getNotEmptyPath() {
		return this.path != null && this.path != "";
	}
	
	
	
	
	
	
	/**
	 * Always when we want to represent the contain of a IP (files and folders) as a tree in the web, 
	 * we must call this method with the instance of the desired IP. 
	 * 
	 * @param ip the information package that will be displayed
	 * @param viewLevel this parameters indicates the folder that will be the root of the tree representing the project in the web.
	 * @throws Exception if the folder does not exist or the folder can not be opened.
	 */
	public void load(WorkflowIP ip, ViewLevel viewLevel) throws Exception{
		super.load(ip);
		long startTime = System.currentTimeMillis();
		this.viewLevel = viewLevel;
		this.ip = ip;
		this.loadRootFile();
		
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
	 * 
	 * Lazy loading method.
	 * This method is not recursive, because an IP can be very big.
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
				TreeFile folderNode = TreeFile.getDirectoryNode(file.getAbsolutePath(), ip, file.getName(), rootNode, folderContent);
				this.loadFolder(folderNode, file);
			}else if(file.isFile()){
				TreeFile.getFileNode(file.getAbsolutePath(), ip, file.getName(), rootNode);
			}
		}
	}
	
	private static FolderContent getContent(File root) throws Exception{
		FolderContent content = new FolderContent();
		List<File> files = Services.getInstance().getStorageService().getFiles(root);
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
	
	public void listenerSelectFile(ActionEvent event){
		Object obj = getRequestBean("fileNode");
		this.selectedNode = (TreeFileData)obj;
		
		try {
			this.path = this.selectedNode.getRelativePath();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<RsyncUpload> getUploadList() {
		return this.daoService.getOpenRsyncUploadList(this.ip);
	}
	
	
	private String getFileSystemAccountName() {
		User user = this.getSession().getCurrentUser();
		String name = user.getUserName();
		if(user.getSshAccount() != null && !user.getSshAccount().equals(""))
			name = user.getSshAccount();
		return name;
	}
	
	
	
	public void listenerStartRsyncUpload() throws Exception {
		// check for matching pathes, subpathes and parent pathes to prevent 
		// writing multiple users to the same directory
		Boolean block = false;
		List<RsyncUpload> list = this.daoService.getOpenRsyncUploadList(this.getIp());
		if(list != null) for(RsyncUpload item : list) {
			if(item.getTarget().contains(this.path)) {
				block = true;
				break;
			}
			String tmp = "";
			for(String frag : this.path.split("/")) {
				tmp += "/" + frag;
				if(tmp.equals(item.getTarget())) {
					block = true;
					break;
				}
			}
			if(block) break;
		}
		
		if(!block) { 
			RsyncUpload upload = new RsyncUpload(this.path, this.getIp(), this.getFileSystemAccountName());
			this.daoService.saveDBEntry(upload);
			
			this.grantPermissions(upload);
			
		}else{
			this.addMsg("A user has already opened an upload object with write access to the requested location."
					+ "Close the first upload prior to open a new one.");
		}
	}
	
	
	
	private void grantPermissions(RsyncUpload upload) throws Exception {
		String[] cmd = {
			"sudo",
			"/var/lib/tomcat8/webapps/ianus-ingest-web/WEB-INF/classes/rsyncUpload/rsync_upload_grant_permissions.sh", 
			"--storage-path=" + this.getIp().getLocation().getAbsolutePath(), 
			"--ip-root=" + this.getIp().getPackageFolderName(), 
			"--ip-target=" + upload.getTarget(), 
			"--username=" + upload.getUserName(), 
			"--upload-id=" + upload.getId()
		};
		
		logger.info("grant write permissions for user "
				+ this.getFileSystemAccountName()
				+ " at " + this.getIp().getAbsolutePath() + upload.getTarget());
		logger.info("Execute cmd: " + StringUtils.join(cmd, " "));
		
		Process proc = Runtime.getRuntime().exec(new String[]{
				"/bin/bash",
				"-c",
				StringUtils.join(cmd, " ")
		});
		proc.waitFor();
	    
	    BufferedReader stdInput = new BufferedReader(new 
		     InputStreamReader(proc.getInputStream()));
		BufferedReader stdError = new BufferedReader(new 
		     InputStreamReader(proc.getErrorStream()));
		// read the output from the command
		System.out.println("##### stdout:\n");
		String s = null;
		while ((s = stdInput.readLine()) != null) {
		    System.out.println(s);
		}
		// read any errors from the attempted command
		System.out.println("##### stderr:\n");
		while ((s = stdError.readLine()) != null) {
		    System.out.println(s);
	    }
	}
	
	
	private void revokePermissions(RsyncUpload upload) throws Exception {
		String[] cmd = {
			"sudo",
			"/var/lib/tomcat8/webapps/ianus-ingest-web/WEB-INF/classes/rsyncUpload/rsync_upload_revoke_permissions.sh", 
			"--storage-path=" + this.getIp().getLocation().getAbsolutePath(), 
			"--ip-root=" + this.getIp().getPackageFolderName(), 
			"--ip-target=" + upload.getTarget(), 
			"--username=" + upload.getUserName(), 
			"--upload-id=" + upload.getId()
		};
		
		logger.info("revoke write permissions for user "
				+ this.getFileSystemAccountName()
				+ " at " + this.getIp().getAbsolutePath() + upload.getTarget());
		logger.info("hand over control back to " + System.getProperty("user"));
		logger.info("Execute cmd: " + StringUtils.join(cmd, " "));
		
		Process proc = Runtime.getRuntime().exec(new String[]{
				"/bin/bash",
				"-c",
				StringUtils.join(cmd, " ")
		});
	    proc.waitFor();
	    
	    BufferedReader stdInput = new BufferedReader(new 
    	     InputStreamReader(proc.getInputStream()));
    	BufferedReader stdError = new BufferedReader(new 
    	     InputStreamReader(proc.getErrorStream()));
    	// read the output from the command
    	System.out.println("##### stdout:\n");
    	String s = null;
    	while ((s = stdInput.readLine()) != null) {
    	    System.out.println(s);
    	}
    	// read any errors from the attempted command
    	System.out.println("##### stderr:\n");
    	while ((s = stdError.readLine()) != null) {
    	    System.out.println(s);
    	}
	}
	
	
	
	public void listenerStartVirusScan() throws Exception {
		RsyncUpload upload = (RsyncUpload)this.getRequestBean("upload");
		InformationPackage wfip = this.getIp();
		
		if(upload.getStatus() == RsyncUpload.State.waiting) {
			this.revokePermissions(upload);
			
			upload.setPreScanFileCount(getContent(new File(this.getIp().getAbsolutePath() + upload.getTarget())).files);
			upload.setStatus((RsyncUpload.State.virus_scan));
			this.daoService.saveDBEntry(upload);
			
			Thread thread = new Thread(
				new RsyncUploadVirusScanProcessor(this.daoService, upload.getId(), upload.getWfipId(), wfip.getClass()));
			thread.start();
		}else{
			addMsg("Virus scan already running.");
		}
	}
	
	public void listenerReopen() throws Exception {
		RsyncUpload upload = (RsyncUpload)this.getRequestBean("upload");
		if(	upload.getStatus() == RsyncUpload.State.scan_ok
		||	upload.getStatus() == RsyncUpload.State.scan_errors
		||	upload.getStatus() == RsyncUpload.State.virus_scan) {
			logger.info("Resetting the RsyncUpload object " + upload.getId());
			upload.setScannedFiles(0);
			upload.setStatus(RsyncUpload.State.waiting);
			this.daoService.saveDBEntry(upload);
			this.grantPermissions(upload);
		}
	}
	
	public void listenerCloseRsyncUpload() throws Exception {
		RsyncUpload upload = (RsyncUpload)this.getRequestBean("upload");
		if(	upload.getStatus() == RsyncUpload.State.scan_ok
		||	upload.getStatus() == RsyncUpload.State.scan_errors
		||	upload.getStatus() == RsyncUpload.State.waiting) {
			logger.info("Closing the RsyncUpload object " + upload.getId());
			upload.setStatus(RsyncUpload.State.closed);
			this.daoService.saveDBEntry(upload);
		}
	}
}
