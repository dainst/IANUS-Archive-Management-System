package de.ianus.ingest.core.conversionRoutine.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import de.ianus.ingest.core.bo.DBEntry;
import de.ianus.ingest.core.utils.StringListConverter;

/**
 * 
 * @author jurzua
 *
 */
@Entity
@Table(name = "ConversionRoutine")
public class ConversionRoutine extends DBEntry implements Serializable{

	private static final long serialVersionUID = 2915228361276149417L;
	
	public static final String SIP_AIP = "SIP-AIP";
	public static final String AIP_DIP = "AIP-DIP";
	
	//präferiert/akzeptiert
	//prefered/accepted
	@Column(name="type0")
	private String type = Type.accepted.toString();
	
	@Column(name="name")
	private String name;
	
	@Column(name="actionName")
	private String actionName;
	
	//pre-ingest (SIP->AIP), ingest (AIP->DIP)
	@Column(name="scope0")
	private String scope = SIP_AIP;
	
	@Column(name="condition0")
	private String condition;
	
	@Column(name="inputPronoms")
	@Convert(converter = StringListConverter.class)
	private List<String> inputPronomList;
	
	@Column(name="outputPronoms")
	@Convert(converter = StringListConverter.class)
	private List<String> outputPronomList;
	
	@Column(name="inputExtensions")
	@Convert(converter = StringListConverter.class)
	private List<String> inputExtensionsList;
	
	@Column(name="outputExtensions")
	@Convert(converter = StringListConverter.class)
	private List<String> outputExtensionsList;
	
	//@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy="actions")
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE}, fetch = FetchType.EAGER)
	@JoinTable(
	        name = "Routine_Action", 
	        joinColumns = { @JoinColumn(name = "routine_id") }, 
	        inverseJoinColumns = { @JoinColumn(name = "action_id") }
	 )
    private List<ConversionAction> actionList = new ArrayList<ConversionAction>();
	
	public ConversionRoutine(){}
	
	public ConversionRoutine(String name, String actionName, String scope, String type, 
			List<String> inputPronomList, 
			List<String> outputPronomList,
			List<String> inputExtensionList,
			List<String> outputExtensionList){
		this.name = name;
		this.actionName = actionName;
		this.scope = scope;
		this.type = type;
		
		this.inputPronomList = inputPronomList;
		this.outputPronomList = outputPronomList;
		
		this.inputExtensionsList = inputExtensionList;
		this.outputExtensionsList = outputExtensionList;
	}
	
	public boolean containsInputPronom(String pronom){
		return this.inputPronomList.contains(pronom);
	}
	
	public boolean containsOutputPronom(String pronom){
		return this.outputPronomList.contains(pronom);
	}
	

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}
	
	public List<String> getInputPronomList() {
		return inputPronomList;
	}

	public void setInputPronomList(List<String> inputPronomList) {
		this.inputPronomList = inputPronomList;
	}

	public List<String> getOutputPronomList() {
		return outputPronomList;
	}

	public void setOutputPronomList(List<String> outputPronomList) {
		this.outputPronomList = outputPronomList;
	}

	public List<ConversionAction> getActionList() {
		return actionList;
	}

	public void setActionList(List<ConversionAction> actionList) {
		this.actionList = actionList;
	}
	
	public List<String> getInputExtensionsList() {
		return inputExtensionsList;
	}

	public void setInputExtensionsList(List<String> inputExtensionsList) {
		this.inputExtensionsList = inputExtensionsList;
	}

	public List<String> getOutputExtensionsList() {
		return outputExtensionsList;
	}

	public void setOutputExtensionsList(List<String> outputExtensionsList) {
		this.outputExtensionsList = outputExtensionsList;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String toLongString(){
		StringBuilder sb = new StringBuilder(this.toString());
		for(ConversionAction action : this.actionList){
			sb.append("\t" + action.toString() + "\n");
			for(ActionTool tool : action.getToolList()){
				sb.append("\t\t" + tool.toString() + "\n");
			}
		}
		return sb.toString();
	}
	
	@Override
	public String toString(){
		return "ConversionRoutine [id=" + id +
				", actionName=" + actionName + 
				", name=" + name + 
				", scope=" + scope+ 
				", type=" + type + "]";
	}
	
	public enum Type{
		prefered, accepted
	}
}
