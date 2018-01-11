package de.ianus.ingest.core.bo.files;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import de.ianus.ingest.core.bo.DBEntry;

@Entity
@Table(name="Agent")
public class Agent extends DBEntry{
	
	//name either of microservice or user name. 
	@Column(name="agentName")
	private String agentName;
	
	//person, micro-service
	@Column(name="agentType")
	private String agentType = AgentType.software.toString();
	
	public enum AgentType {
		// taken from http://id.loc.gov/vocabulary/preservation/agentType.html
		hardware, organization, person, software
	}
	
	public Agent(){}
	
	
	public Agent(String name, AgentType type) {
		this.agentName = name;
		this.agentType = type.toString();
	}

	public String getAgentName() {
		return agentName;
	}
	
	public void setAgentName(String name) {
		this.agentName = name;
	}

	public String getAgentType() {
		return agentType;
	}
	
	public void setAgentType(String type) {
		this.agentType = type;
	}
}
