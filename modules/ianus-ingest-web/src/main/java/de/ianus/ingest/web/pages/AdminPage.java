package de.ianus.ingest.web.pages;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.apache.commons.lang.StringUtils;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.ums.User;
import de.ianus.ingest.scripts.ScriptCloningRepair;
import de.ianus.ingest.scripts.ScriptGenerateCollectionTitles;
import de.ianus.ingest.web.AbstractBean;

public class AdminPage extends AbstractBean{
	public static String PAGE_NAME = "admin";
	
	private List<User> userList = new ArrayList<User>();
	private User currentUser = new User();
	private String password;
	
	public String actionLoadUserList(){
		try {
			this.userList = Services.getInstance().getDaoService().getUserList();
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	public String actionCloningRepair(){
		try {
			ScriptCloningRepair.execute();
			addMsg("Genaration of collection titles is finished!");
			
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	public String actionGenerateCollectionTitles(){
		try {
			ScriptGenerateCollectionTitles.execute();
			addMsg("Genaration of collection titles is finished!");
			
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	public String actionSaveUser(){
		try {
			
			if(StringUtils.equals(this.password, this.currentUser.getPassword())){
				Services.getInstance().getDaoService().saveDBEntry(this.currentUser);
				this.currentUser = new User();
				this.password = new String();
				this.userList = Services.getInstance().getDaoService().getUserList();
			}
			
		} catch (ConstraintViolationException e) {
			addMsg("There exists already an account with the userName = " + this.currentUser.getUserName());
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
