package de.ianus.metadata.bo.utils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import de.ianus.metadata.bo.Element;


@Entity
@Table(name="Software")
public class Software extends Element{
	
	//Software
	@Column(name="name")
	private String name;
	
	//Software_Version
	@Column(name="version")
	private String version;
	
	//Hardware_Typ
	@Column(name="hardwareType")
	private String hardwareType;
	
	//Hardware_Spezifikation
	@Column(name="hardwareSpecification")
	private String hardwareSpecification;
	
	//Betriebssystem
	@Column(name="operatingSystem")
	private String 	operatingSystem;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getHardwareType() {
		return hardwareType;
	}

	public void setHardwareType(String hardwareType) {
		this.hardwareType = hardwareType;
	}

	public String getHardwareSpecification() {
		return hardwareSpecification;
	}

	public void setHardwareSpecification(String hardwareSpecification) {
		this.hardwareSpecification = hardwareSpecification;
	}

	public String getOperatingSystem() {
		return operatingSystem;
	}

	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}
}
