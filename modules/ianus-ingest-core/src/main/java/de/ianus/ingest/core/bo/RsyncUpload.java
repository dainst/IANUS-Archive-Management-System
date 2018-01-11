package de.ianus.ingest.core.bo;

import java.io.IOException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;



@Entity
@Table(name="RsyncUpload")
public class RsyncUpload extends DBEntry {
	
	/**
	 * The user is supposed to replenish files found foulty and removed by ClamAV.
	 * As infected files are automatically removed, the object can always be closed. 
	 */
	// waiting 		-> virus_scan		->	scan_ok			->		waiting ||	->		closed
	// 										scan_errors		->		waiting ||	->		closed
		
	public enum State {
		waiting, virus_scan, scan_ok, scan_errors, closed
	}
	
	
	
	@Column(name="wfipId")
	private Long wfipId;
	
	@Column(name="wfipType")
	private String wfipType;
	
	/**
	 * An absolute path!
	 * The user is supposed to upload data to wfip-data directory, but due to the fact that they are using rsync,
	 * we do not have effective control about that. 
	 * The displayed instructions with the complete rsync command (being copypasta for the user) 
	 * will also incorporate the full absolute path.
	 */
	@Column(name="target")
	private String target;
	
	@Column(name="userName")
	private String userName;
	
	@Enumerated(EnumType.STRING)
	@Column(name="status")
	private State status = State.waiting;
	
	// PID of a currently running process on the uploaded data (virus scan)
	@Column(name="pid")
	private Long scanPid;
	
	@Column(name="preScanFileCount")
	private Integer preScanFileCount;
	
	@Column(name="scannedFiles")
	private Integer scannedFiles;
	
	@Column(name="scanStartTime")
	private Long scanStartTime;
	
	
	
	
	public RsyncUpload(String target, InformationPackage ip, String userName) {
		this.target = target;
		this.wfipType = ip.getClass().getName();
		this.wfipId = ip.getId();
		this.userName = userName;
	}
	
	public RsyncUpload() {}
	
	
	
	public void setStatus(State state) {
		this.status = state;
	}
	
	public State getStatus() {
		return this.status;
	}
	
	public String getStatusInfo() throws IOException, InterruptedException {
		String tmp = this.getScanStatus(); 
		if(tmp != null) return this.status.toString() + " " + tmp;
		return this.status.toString();
	}
	
	private String getScanStatus() throws IOException, InterruptedException {
		if(this.status == RsyncUpload.State.virus_scan) {
			String cmd = "ps -p " + this.scanPid;
			Process process = Runtime.getRuntime().exec(cmd);
			if(process.waitFor() == 0) {
				return this.getScanProgress();
			}else{
				return "<br>Scan status unknown, lost? <br>[PID: " + this.scanPid + "]";
			}
		}
		return null;
	}
	
	// scannedFiles will be updated by ClamAVOutputWriter regularly
	public String getScanProgress() {
		if(this.scannedFiles == null || this.scannedFiles == 0)
			return null;
		String result = "";
		Integer scanned = this.scannedFiles;
		Integer counted = this.preScanFileCount;
		Long elapsed = System.currentTimeMillis() - this.scanStartTime;
		if(counted > 0 && counted >= scanned) {
			double madeGood = 100 * scanned / counted;
			Long remaining = (long) ((100 - (madeGood)) * elapsed / madeGood);
			Integer hours = Math.round((remaining / 1000) / 3600);
			Integer minutes = Math.round(((remaining / 1000) % 3600) / 60);
			Integer seconds = Math.round(((remaining / 1000) % 3600) % 60);
			result += Math.round(madeGood) + "%, <br>";
			result += "remaining time: " + String.format("%02d", hours);
			result += ":" + String.format("%02d", minutes);
			result += ":" + String.format("%02d", seconds) + ", <br>";
		}
		result += this.scannedFiles.toString() + " of " + this.preScanFileCount.toString() + " files.";
		return result;
	}
	
	public String getUserName() {
		return this.userName;
	}
	
	public String getTarget() {
		return this.target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public Long getWfipId() {
		return this.wfipId;
	}

	public void setWfipId(Long id) {
		this.wfipId = id;
	}
	
	public String getVirusLogFileName(){
		return "rsync_upload_virus_scan_report_" + this.getId() + ".txt";
	}

	public void setScanPid(Long pid) {
		this.scanPid = pid;
	}
	
	public Long getScanPid() {
		return this.scanPid;
	}
	
	public void setPreScanFileCount(Integer count) {
		this.preScanFileCount = count;
	}
	
	public Integer getPreScanFileCount() {
		return this.preScanFileCount;
	}
	
	public Integer getScannedFiles() {
		return this.scannedFiles;
	}
	
	public void setScannedFiles(Integer n) {
		this.scannedFiles = n;
	}
	
	public Long getScanStartTime() {
		return this.scanStartTime;
	}
	
	public void setScanStartTime(Long startTime) {
		this.scanStartTime = startTime;
	}
}
