package de.ianus.metadata.bo.collectionFile;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Transient;

public class Database {
	
	//Bei relationalen Datenbanken die Anzahl der Tabellen, bei dokumentenbasierten Datenbanken die Anzahl der Dokumente
	@Column(name="elementNumber")
	private Integer elementNumber;
	
	@Column(name="technicalArchitecture", columnDefinition = "TEXT")
	private String technicalArchitecture;
	
	@Transient
	private Set<DatabaseTable> tableList = new LinkedHashSet<DatabaseTable>();

	public Integer getElementNumber() {
		return elementNumber;
	}

	public void setElementNumber(Integer elementNumber) {
		this.elementNumber = elementNumber;
	}

	public String getTechnicalArchitecture() {
		return technicalArchitecture;
	}

	public void setTechnicalArchitecture(String technicalArchitecture) {
		this.technicalArchitecture = technicalArchitecture;
	}

	public Set<DatabaseTable> getTableList() {
		return tableList;
	}

	public void setTableList(Set<DatabaseTable> tableList) {
		this.tableList = tableList;
	}
}
