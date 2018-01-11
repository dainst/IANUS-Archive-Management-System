package de.ianus.ingest.web.jsf;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import de.ianus.ingest.core.bo.InformationPackage;
import de.ianus.ingest.web.pages.FilesPage.FolderContent;

/**
 * <p>
 * In the class FilesPage, the content (digital object) of an IP is represented as a tree. 
 * For that, we use the DefaultTreeNode implementation of Primefaces. This class is extended here by the TreeFile.
 * This extension is necessary to add all the relevant information related to the files and folders.
 * </p>
 * 
 * @author Jorge Urzua
 *
 */
public class TreeFile extends DefaultTreeNode{
	private static final long serialVersionUID = -7878193220844905112L;

	public TreeFile(Object data, TreeNode parent) {
		super(data, parent);
	}

	public TreeFile(String type, Object data, TreeNode parent) {
		super(type, data, parent);
	}
	
	public static TreeFile getDirectoryNode(String fileAbsolutePath, InformationPackage ip, String label, TreeNode parent, FolderContent folderContent) throws Exception{
		TreeFileData data = new TreeFileData(ip, "folder", label, fileAbsolutePath, folderContent);
		TreeFile node = new TreeFile(data, parent);
		return node;
	}
	
	public static TreeFile getFileNode(String fileAbsolutePath, InformationPackage ip, String label, TreeNode parent) throws Exception{
		TreeFileData data = new TreeFileData(ip, "file", label, fileAbsolutePath, null);
		TreeFile node = new TreeFile("document", data, parent);
		return node;
	}
}
