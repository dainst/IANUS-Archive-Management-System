package de.ianus.ingest.core.bo.files;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.log4j.Logger;

import de.ianus.ingest.core.DAOService;
import de.ianus.ingest.core.bo.DBEntry;


@Entity
@Table(name="FileInstanceProperty")
public class FileInstanceProperty extends DBEntry{
	
	
	private Long fileInstanceId;
	
	private String name;
	
	@Column(name="value", columnDefinition = "TEXT")
	private String value;
	
	private String tool;
	
	private String toolVersion;
	
	private String status;
	
	
	private static Logger logger = Logger.getLogger(FileInstanceProperty.class);
	
	
	public static enum ValueStatus {
		OK, CONFLICT, UNKNOWN;
		
		public static ValueStatus getByString(String str) {
			ValueStatus result = UNKNOWN;
			if(str == null) return result;
			switch(str.toLowerCase()) {
			case "ok":
			case "single_result": result = OK; break;
			case "conflict": result = CONFLICT; break;
			}
			return result;
		}
	}
	
	
	public FileInstanceProperty() {}
	
	public FileInstanceProperty(Long fid, String key, String value, 
			String tool, String version, ValueStatus status) {
		this.fileInstanceId = fid;
		this.name = key.trim();
		this.value = value.trim();
		this.tool = tool.trim();
		this.toolVersion = version.trim();
		this.status = status.name();
	}
	
	/**
	 * clone constructor
	 * @param fiId
	 * @param name
	 * @param status
	 * @param tool
	 * @param version
	 * @param value
	 */
	public FileInstanceProperty(Long fiId, String name, String status, String tool, String version, String value) {
		this.fileInstanceId = fiId;
		this.name = name;
		this.status = status;
		this.tool = tool;
		this.toolVersion = version;
		this.value = value;
	}
	
	public FileInstanceProperty clone(Long newFiId) {
		return new FileInstanceProperty(newFiId, this.name, this.status, this.tool, this.toolVersion, this.value);
	}
	
	/**
	 * Jhove produces irrelevant error messages, when it tries to evaluate invalid (xml-)files. 
	 * Thus, filter the attributes before saving
	 * 
	 * @param dao
	 * @throws Exception
	 */
	public void save(DAOService dao) throws Exception {
		if(this.name.equals("message") && this.tool.equals("Jhove")) {
			logger.info("not saving tool message output");
		}else{
			dao.saveDBEntry(this);
		}
	}
	
	
	public Long getFileInstanceId() {
		return this.fileInstanceId;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public String getTool() {
		return this.tool;
	}
	
	public String getToolVersion() {
		return this.toolVersion;
	}
	
	public String getStatus() {
		return this.status;
	}
}
