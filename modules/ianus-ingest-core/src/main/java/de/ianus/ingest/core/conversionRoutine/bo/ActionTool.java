package de.ianus.ingest.core.conversionRoutine.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import de.ianus.ingest.core.bo.DBEntry;

@Entity
@Table(name = "ActionTool")
public class ActionTool extends DBEntry implements Serializable{
	private static final long serialVersionUID = -29257661182380783L;
	
	@Column(name="software")
	private String software;
	
	@Column(name="version")
	private String version;
	
	@Column(name="params")
	private String params;

	@Column(name="url")
	private String url;
	
	public ActionTool(){}
	
	public ActionTool(String software, String params, String url){
		this.software = software;
		this.params = params;
		this.url = url;
	}
	
	public String getSoftware() {
		return software;
	}

	public void setSoftware(String software) {
		this.software = software;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	@Override
	public String toString(){
		return "ActionTool [id=" + getId() + ", software=" + software + ", params=" + params + ", url=" + url + "]";
	}
}
