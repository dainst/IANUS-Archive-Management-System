package de.ianus.ingest.core.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name="BagitFile")
public class BagitFile extends DBEntry{
	
	@Column(name="tpId")
	private Long ipId;

	@Column(name="fileName")
	private String fileName;
	
	@Enumerated(EnumType.STRING)
	@Column(name="unpack")
	private State unpack = State.no_started;
	
	@Column(name="checksumBagitFileClient")
	private String checksumBagitFileClient;
	
	@Column(name="checksumBagitFileServer")
	private String checksumBagitFileServer; 
	
	@Enumerated(EnumType.STRING)
	@Column(name="checksumEvaluation")
	private State checksumEvaluation = State.no_started;
	
	@Enumerated(EnumType.STRING)
	@Column(name="virusScan")
	private State virusScan = State.no_started;
	
	@Enumerated(EnumType.STRING)
	@Column(name="bagitConsistency")
	private State bagitConsistency = State.no_started;
	
	@Enumerated(EnumType.STRING)
	@Column(name="conceptualFiles")
	private State conceptualFiles = State.no_started;
	
	public String getRelativePathVirusLogFile(){
		String unpackedFileNameAsFolder = this.getFileName().substring(0, this.getFileName().length() - 4);
		return "/logs/" + unpackedFileNameAsFolder + "/virus_scan_output.txt";
	}
	
	public String getBagitValidationLogFile(){
		String unpackedFileNameAsFolder = this.getFileName().substring(0, this.getFileName().length() - 4);
		return "/logs/" + unpackedFileNameAsFolder + "/bagit-validation_output.txt";
	}
	
	public String getBagitConceptFilesLogFile(){
		String unpackedFileNameAsFolder = this.getFileName().substring(0, this.getFileName().length() - 4);
		return "/logs/" + unpackedFileNameAsFolder + "/concept-files_output.txt";
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public Long getIpId() {
		return ipId;
	}

	public void setIpId(Long ipId) {
		this.ipId = ipId;
	}

	public State getUnpack() {
		return unpack;
	}

	public void setUnpack(State unpack) {
		this.unpack = unpack;
	}	

	public State getChecksumEvaluation() {
		return checksumEvaluation;
	}

	public void setChecksumEvaluation(State checksumEvaluation) {
		this.checksumEvaluation = checksumEvaluation;
	}

	public State getVirusScan() {
		return virusScan;
	}

	public void setVirusScan(State virusScan) {
		this.virusScan = virusScan;
	}

	public State getBagitConsistency() {
		return bagitConsistency;
	}

	public void setBagitConsistency(State bagitConsistency) {
		this.bagitConsistency = bagitConsistency;
	}

	public String getChecksumBagitFileClient() {
		return checksumBagitFileClient;
	}

	public void setChecksumBagitFileClient(String checksumBagitFileClient) {
		this.checksumBagitFileClient = checksumBagitFileClient;
	}


	public String getChecksumBagitFileServer() {
		return checksumBagitFileServer;
	}

	public void setChecksumBagitFileServer(String checksumBagitFileServer) {
		this.checksumBagitFileServer = checksumBagitFileServer;
	}

	public State getConceptualFiles() {
		return conceptualFiles;
	}

	public void setConceptualFiles(State conceptualFiles) {
		this.conceptualFiles = conceptualFiles;
	}

	public boolean isVirusScanFinished(){
		return this.virusScan == State.finished_ok || this.virusScan == State.finished_errors;
	}
	
	public boolean isBagitConsistencyFinished(){
		return this.bagitConsistency == State.finished_ok || this.bagitConsistency == State.finished_errors;
	}
	
	public boolean isFinished(){
		return isVirusScanFinished() && isBagitConsistencyFinished();
	}

	public enum State{
		no_started, in_progress, finished_ok, finished_errors, no_required
	}
}
