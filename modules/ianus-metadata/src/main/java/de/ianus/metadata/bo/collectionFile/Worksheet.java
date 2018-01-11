package de.ianus.metadata.bo.collectionFile;

import javax.persistence.Column;

public class Worksheet {

	@Column(name="name")
	private String name;
	
	@Column(name="purpose", columnDefinition = "TEXT")
	private String purpose;
	
	@Column(name="columnNumber")
	private Integer columnNumber;
	
	@Column(name="rowNumber")
	private Integer rowNumber;
	
}
