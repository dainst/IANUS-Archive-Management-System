package de.ianus.ingest.core.bo.ums;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import de.ianus.ingest.core.bo.DBEntry;

@Entity
@Table(name = "User")
public class User extends DBEntry implements Serializable{
	private static final long serialVersionUID = 1228626170824397638L;

	@Column(name="firstName")
	private String firstName;
	
	@Column(name="lastName")
	private String lastName;
	
	@Column(name="userName", unique=true)
	private String userName;
	
	@Column(name="sshAccount")
	private String sshAccount;
	
	@Column(name="email")
	private String email;

	@Column(name="password")
	private String password;
	
	@Column(name="role")
	private String role = UserRole.curator.toString();
	
	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSshAccount() {
		return this.sshAccount;
	}

	public void setSshAccount(String account) {
		this.sshAccount = account;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public enum UserRole{
		admin, curator, guest
	}
	
	@Override
	public String toString(){
		return "User [userName=" + this.userName + ", role=" + role + ", firstName=" + this.firstName + ", lastName=" + this.lastName + "]";
	}
}
