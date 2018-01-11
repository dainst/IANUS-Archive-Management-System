package de.ianus.metadata.bo.utils;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public class SimpleDate {

	@Column(name="dataCollectionId")
	private Long dataCollectionId;
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="date")
    private Date date;

	public Long getDataCollectionId() {
		return dataCollectionId;
	}

	public void setDataCollectionId(Long dataCollectionId) {
		this.dataCollectionId = dataCollectionId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	
	
}
