package de.ianus.ingest.web.pages;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.apache.commons.lang.StringUtils;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.ums.User;
import de.ianus.ingest.web.AbstractBean;

public class UserManagerPage extends AbstractBean{
	
	public static String PAGE_NAME = "userManager";
	
	private boolean changePassword = false;
	private User currentUser = null;
	
	private String newPassword;
	private String repeatPassword;
	
	
	public String actionDelete(){
		try {
			User tmp = (User)getRequestBean("user");
			Services.getInstance().getDaoService().saveDBEntry(tmp);
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	public String actionEditUser(){
		try {
			this.currentUser = (User)getRequestBean("user");
			this.changePassword = false;
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	public String actionEditPassword(){
		try {
			this.currentUser = (User)getRequestBean("user");
			this.changePassword = true;
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	public boolean isPasswordEdition(){
		return this.changePassword && this.currentUser != null;
	}
	
	public boolean isuserEdition(){
		return !this.changePassword && this.currentUser != null;
	}
	
	public String actionCreateUser(){
		try {
			this.currentUser = new User();
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	public String actionCancelEdition(){
		this.currentUser = null;
		return PAGE_NAME;
	}
	
	public String actionSavePassword(){
		try {
			
			if(StringUtils.equals(this.newPassword, this.repeatPassword)){
				
				this.currentUser.setPassword(newPassword);
				Services.getInstance().getDaoService().saveDBEntry(this.currentUser);
				addMsg("The password for the user " + this.currentUser.getUserName() + " has been changed");
				
				this.currentUser = null;
				this.newPassword = null;
				this.repeatPassword = null;
				this.changePassword = false;
				
			}else{
				addMsg("The new password and the repeat password are not equals");
			}
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	public String actionSaveUser(){
		try {
			
			if(!this.currentUser.isPersistent() && StringUtils.equals(this.currentUser.getPassword(), repeatPassword) || 
				this.currentUser.isPersistent()){
				
					Services.getInstance().getDaoService().saveDBEntry(this.currentUser);
					
					addMsg("The user " + this.currentUser.getUserName() + " has been saved");
					
					this.currentUser = null;
					this.repeatPassword = null;
					
			}else if(!this.currentUser.isPersistent() && StringUtils.equals(this.currentUser.getPassword(), repeatPassword)){
				addMsg("The new password and the repeat password are not equals");
			}
			
		} catch (ConstraintViolationException e) {
			addMsg("There exists already an account with the userName = " + this.currentUser.getUserName());
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}

	public List<User> getUserList() {
		return Services.getInstance().getDaoService().getUserList();
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getRepeatPassword() {
		return repeatPassword;
	}

	public void setRepeatPassword(String repeatPassword) {
		this.repeatPassword = repeatPassword;
	}
}
