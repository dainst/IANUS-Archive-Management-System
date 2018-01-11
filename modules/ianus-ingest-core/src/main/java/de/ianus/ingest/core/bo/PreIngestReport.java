package de.ianus.ingest.core.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * This class is used to represent the report that is written by the curator about a information package (DIP) 
 * in a PreIngest workflow. 
 * 
 * @author jurzua
 *
 */
@Entity
@Table(name="PreIngestReport")
public class PreIngestReport extends DBEntry{

	@Column(name="sipId")
	public Long sipId;
	
	@Column(name="dataReadability")
	public String dataReadability = State.NO_CHECKED.toString();
	
	@Column(name="dataConsistency")
	public String dataConsistency = State.NO_CHECKED.toString();
	
	@Column(name="documentationConsistency")
	public String documentationConsistency = State.NO_CHECKED.toString();
	
	@Column(name="collectionMetadata")
	public String collectionMetadata = State.NO_CHECKED.toString();
	
	@Column(name="fileMetadata")
	public String fileMetadata = State.NO_CHECKED.toString();
	
	@Column(name="legalConstraints")
	public String legalConstraints = State.NO_CHECKED.toString();
	
	@Column(name="microservicesActions")
	public String microservicesActions = State.NO_CHECKED.toString();
	
	//the following field are used by the data curators to describe the checking process
	@Column(name="dataReadabilityText", columnDefinition = "TEXT")
	public String dataReadabilityText;
	
	@Column(name="dataConsistencyText", columnDefinition = "TEXT")
	public String dataConsistencyText;
	
	@Column(name="documentationConsistencyText", columnDefinition = "TEXT")
	public String documentationConsistencyText;
	
	@Column(name="collectionMetadataText", columnDefinition = "TEXT")
	public String collectionMetadataText;
	
	@Column(name="fileMetadataText", columnDefinition = "TEXT")
	public String fileMetadataText;
	
	@Column(name="legalConstraintsText", columnDefinition = "TEXT")
	public String legalConstraintsText;
	
	@Column(name="microservicesActionsText", columnDefinition = "TEXT")
	public String microservicesActionsText;
	
	public boolean isReporOk(){
		return 
				StringUtils.equals(State.OK.toString(), this.dataReadability) &&
				StringUtils.equals(State.OK.toString(), this.dataConsistency) &&
				StringUtils.equals(State.OK.toString(), this.documentationConsistency) &&
				StringUtils.equals(State.OK.toString(), this.collectionMetadata) &&
				StringUtils.equals(State.OK.toString(), this.fileMetadata) &&
				StringUtils.equals(State.OK.toString(), this.microservicesActions) &&
				StringUtils.equals(State.OK.toString(), this.legalConstraints);
	}
	
	public enum State {
		NO_CHECKED,
		OK,
		REVISION,
		ERRORS
	}

	public String getDataReadability() {
		return dataReadability;
	}

	public void setDataReadability(String dataReadability) {
		this.dataReadability = dataReadability;
	}

	public String getDataConsistency() {
		return dataConsistency;
	}

	public void setDataConsistency(String dataConsistency) {
		this.dataConsistency = dataConsistency;
	}

	public String getDocumentationConsistency() {
		return documentationConsistency;
	}

	public void setDocumentationConsistency(String documentationConsistency) {
		this.documentationConsistency = documentationConsistency;
	}

	public String getCollectionMetadata() {
		return collectionMetadata;
	}

	public void setCollectionMetadata(String collectionMetadata) {
		this.collectionMetadata = collectionMetadata;
	}

	public String getFileMetadata() {
		return fileMetadata;
	}

	public void setFileMetadata(String fileMetadata) {
		this.fileMetadata = fileMetadata;
	}

	public String getLegalConstraints() {
		return legalConstraints;
	}

	public void setLegalConstraints(String legalConstraints) {
		this.legalConstraints = legalConstraints;
	}

	public String getMicroservicesActions() {
		return microservicesActions;
	}

	public void setMicroservicesActions(String microservicesActions) {
		this.microservicesActions = microservicesActions;
	}

	public Long getSipId() {
		return sipId;
	}

	public void setSipId(Long sipId) {
		this.sipId = sipId;
	}

	public String getDataReadabilityText() {
		return dataReadabilityText;
	}

	public void setDataReadabilityText(String dataReadabilityText) {
		this.dataReadabilityText = dataReadabilityText;
	}

	public String getDataConsistencyText() {
		return dataConsistencyText;
	}

	public void setDataConsistencyText(String dataConsistencyText) {
		this.dataConsistencyText = dataConsistencyText;
	}

	public String getDocumentationConsistencyText() {
		return documentationConsistencyText;
	}

	public void setDocumentationConsistencyText(String documentationConsistencyText) {
		this.documentationConsistencyText = documentationConsistencyText;
	}

	public String getCollectionMetadataText() {
		return collectionMetadataText;
	}

	public void setCollectionMetadataText(String collectionMetadataText) {
		this.collectionMetadataText = collectionMetadataText;
	}

	public String getFileMetadataText() {
		return fileMetadataText;
	}

	public void setFileMetadataText(String fileMetadataText) {
		this.fileMetadataText = fileMetadataText;
	}

	public String getLegalConstraintsText() {
		return legalConstraintsText;
	}

	public void setLegalConstraintsText(String legalConstraintsText) {
		this.legalConstraintsText = legalConstraintsText;
	}

	public String getMicroservicesActionsText() {
		return microservicesActionsText;
	}

	public void setMicroservicesActionsText(String microservicesActionsText) {
		this.microservicesActionsText = microservicesActionsText;
	}
}
