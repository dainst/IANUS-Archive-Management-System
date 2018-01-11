package de.ianus.metadata.bo.collectionFile;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

import de.ianus.metadata.bo.MDEntry;
import de.ianus.metadata.bo.utils.Language;

@Entity(name="PDFDocument")
public class PDFDocument extends MDEntry{

	@Column(name="originalFileName")
	private String originalFileName;

	@Column(name="creationSoftware")
	private String creationSoftware;
	
	@Transient
	private Set<Language> languageList = new LinkedHashSet<Language>();

	public Set<Language> getLanguageList() {
		return languageList;
	}

	public void setLanguageList(Set<Language> languageList) {
		this.languageList = languageList;
	}

	public String getOriginalFileName() {
		return originalFileName;
	}

	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

	public String getCreationSoftware() {
		return creationSoftware;
	}

	public void setCreationSoftware(String creationSoftware) {
		this.creationSoftware = creationSoftware;
	}
}
