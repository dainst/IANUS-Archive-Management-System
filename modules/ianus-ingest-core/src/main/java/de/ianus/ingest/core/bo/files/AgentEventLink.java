package de.ianus.ingest.core.bo.files;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import de.ianus.ingest.core.bo.DBEntry;

@Entity
@Table(name="AgentEventLink")
public class AgentEventLink extends DBEntry{
	
	@Column(name="agentId")
	private Long agentId;
	
	@Column(name="eventId")
	private Long eventId;
	
	@Column(name="agentRole")
	private String agentRole = AgentRole.executing_program.toString();
	
	
	public enum AgentRole {
		// taken from http://id.loc.gov/vocabulary/preservation/eventRelatedAgentRole.html
		authorizer, executing_program, implementer, validator
	}
	
	
	public AgentEventLink() {}
	
	public AgentEventLink(Agent agent, Event event, AgentRole role){
		this.agentId = agent.getId();
		this.agentRole = role.toString();
		this.eventId = event.getId();
	}
	
	/**
	 * clone constructor
	 * @param agentId
	 * @param eventId
	 * @param agentRole
	 */
	public AgentEventLink(Long agentId, Long eventId, String agentRole) {
		this.agentId = agentId;
		this.eventId = eventId;
		this.agentRole = agentRole;
	}
	
	public AgentEventLink clone() {
		return new AgentEventLink(this.agentId, this.eventId, this.agentRole);
	}

	
	
	public Long getAgentId() {
		return agentId;
	}

	public Long getEventId() {
		return eventId;
	}

	public String getAgentRole() {
		return this.agentRole;
	}
	
	
	
}
