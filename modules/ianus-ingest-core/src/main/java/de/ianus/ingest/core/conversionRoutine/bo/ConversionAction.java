package de.ianus.ingest.core.conversionRoutine.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import de.ianus.ingest.core.bo.DBEntry;

@Entity
@Table(name = "ConversionAction")
public class ConversionAction extends DBEntry implements Serializable{
	private static final long serialVersionUID = 8131980238008228043L;
	
	@Column(name="name")
	private String name;

	//System/ Curator
	@Column(name="actor")
	private String actor;
	
	@Column(nullable = false, name="integrated", columnDefinition = "TINYINT", length = 1)
	private boolean integrated = false;
	
	@Column(name="scope0")
	private String scope;
	
	@Column(name="description")
	private String description;
	
	@ManyToMany(mappedBy = "actionList")
	private List<ConversionRoutine> routineList = new ArrayList<ConversionRoutine>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ActionTool> toolList = new ArrayList<ActionTool>();
	
	public ConversionAction(){}

	public ConversionAction(String name, String actor, String scope, String description){
		this.name = name;
		this.actor = actor;
		this.scope = scope;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getActor() {
		return actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}

	public boolean isIntegrated() {
		return integrated;
	}

	public void setIntegrated(boolean integrated) {
		this.integrated = integrated;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public List<ActionTool> getToolList() {
		return toolList;
	}

	public void setToolList(List<ActionTool> toolList) {
		this.toolList = toolList;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public List<ConversionRoutine> getRoutineList() {
		return routineList;
	}

	public void setRoutineList(List<ConversionRoutine> routineList) {
		this.routineList = routineList;
	}

	public String toLongString(){
		StringBuilder sb = new StringBuilder(this.toString());
		sb.append("\nTools in action:\n");
		for(ActionTool tool : this.toolList){
			sb.append("\t" + tool + "\n");
		}
		return sb.toString();
	}
	
	@Override
	public String toString(){
		return "ConversionAction [id=" + this.id + 
				", name=" + this.name + 
				", scope=" + this.scope +
				", integrated=" + this.integrated + "]";
	}
	
	public enum Actor{
		system, curator
	}
}
