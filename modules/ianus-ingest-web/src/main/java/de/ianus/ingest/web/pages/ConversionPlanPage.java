package de.ianus.ingest.web.pages;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.conversionRoutine.bo.ActionTool;
import de.ianus.ingest.core.conversionRoutine.bo.ConversionAction;
import de.ianus.ingest.core.conversionRoutine.bo.ConversionRoutine;
import de.ianus.ingest.web.AbstractBean;

public class ConversionPlanPage  extends AbstractBean{
	
	public static String PAGE_NAME = "conversionPlan";
	
	private static final String conversion_actions = "conversionPlan/ianus_actions.json";
	private static final String conversion_routines = "conversionPlan/ianus_routines.json";
	
	private static final Logger logger = LogManager.getLogger(ConversionPlanPage.class);
	
	private ConversionRoutine routine;
	private Long actionId;
	
	private String pronomInput;
	private String pronomOutput;
	
	public void load(ConversionRoutine routine){
		this.routine = routine;
	}
	
	public String actionEditAction(){
		ConversionAction action = (ConversionAction)getRequestBean("action");
		getSession().getActionPage().loadAction(action, this.routine);
		return ActionPage.PAGE_NAME;
	}
	
	public String actionCreateAction(){
		getSession().getActionPage().loadAction(new ConversionAction(), this.routine);
		return ActionPage.PAGE_NAME;
	}
	
	public String actionAddInputPronom(){
		if(this.routine.getInputPronomList().contains(this.pronomInput)){
			addMsg("The routine contains already the pronom: " + this.pronomInput + " as input");
		}else{
			this.routine.getInputPronomList().add(this.pronomInput);
			this.pronomInput = null;
		}
		return PAGE_NAME;
	}
	
	public String actionAddOutputPronom(){
		if(this.routine.getOutputPronomList().contains(this.pronomOutput)){
			addMsg("The routine contains already the pronom: " + this.pronomOutput + " as output");
		}else{
			this.routine.getOutputPronomList().add(this.pronomOutput);
			this.pronomOutput = null;
		}
		return PAGE_NAME;
	}
	
	public String actionNewRoutine(){
		this.routine = new ConversionRoutine();
		return PAGE_NAME;
	}
	
	public String actionCancelEditionRoutine(){
		this.routine = null;
		return PAGE_NAME;
	}
	
	public String actionSaveRoutine(){
		try {
			Services.getInstance().getDaoService().saveDBEntry(this.routine);
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	public String actionSaveRoutineAndClose(){
		try {
			Services.getInstance().getDaoService().saveDBEntry(this.routine);
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	public String actionEditRoutine(){
		this.routine = (ConversionRoutine)getRequestBean("routine");
		return PAGE_NAME;
	}
	
	public String actionDeleteRoutine(){
		addMsg("TODO implement");
		return PAGE_NAME;
	}
	
	public String actionDeletePronomInput(){
		String pronomInput = (String)getRequestBean("pronomInput");
		this.routine.getInputPronomList().remove(pronomInput);
		return PAGE_NAME;
	}
	
	public String actionDeletePronomOutput(){
		String pronomOutput = (String)getRequestBean("pronomOutput");
		this.routine.getOutputPronomList().remove(pronomOutput);
		return PAGE_NAME;
	}
	
	public List<ConversionRoutine> getConversionRoutineList(){
		return Services.getInstance().getDaoService().getConversionRoutineList((x1, x2) -> x1.getName().compareTo(x2.getName()));
	}
	
	public List<ConversionAction> getConversionActionList(){
		return Services.getInstance().getDaoService().getConversionActionList(
				(x1, x2) -> 
				{	if(StringUtils.isNotEmpty(x1.getName()) && StringUtils.isNoneEmpty(x2.getName())){
						return x1.getName().compareTo(x2.getName());
					}else if(StringUtils.isNotEmpty(x1.getName())){
						return -1;
					}else if(StringUtils.isNotEmpty(x2.getName())){
						return 1;
					}else{
						return 0;
					}
				});
	}
	
	public List<SelectItem> getAction4RoutineEdition(){
		List<ConversionAction> actions = getConversionActionList();
		
		final String currentScope = 
				(StringUtils.isNoneEmpty(this.routine.getScope())) ? 
						this.routine.getScope() : ConversionRoutine.SIP_AIP;
		List<SelectItem> list = actions.stream()
			.filter(action -> currentScope.equals(action.getScope()))
			.map(action -> new SelectItem(action.getId(), action.getName() + " actor=" + action.getActor()))
			.collect(Collectors.toList());
		return list;
	}
	
	public String actionRemoveAction(){
		try {
			ConversionAction action = (ConversionAction)getRequestBean("action");
			Services.getInstance().getDaoService().deleteDBEntry(action);
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	public String actionRemoveActionFromRoutine(){
		try {
			ConversionAction action = (ConversionAction)getRequestBean("action");
			this.routine.getActionList().remove(action);
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	private  List<ConversionAction> loadConversionActions() throws Exception{
		
		List<ConversionAction> list = new ArrayList<ConversionAction>();
		
		Path path = Paths.get(getClass().getClassLoader().getResource(conversion_actions).toURI());
		
		JsonParser parser = new JsonParser();
        String content = new String (Files.readAllBytes(path), Charset.forName("UTF-8"));
        Object obj = parser.parse(content);
        JsonElement e = (JsonElement) obj;
        JsonArray array = e.getAsJsonArray();
        
        int count = 0;
		Iterator<JsonElement> it = array.iterator();
		while(it.hasNext()){
			JsonObject j = it.next().getAsJsonObject();
			
			JsonArray softwareArray = j.get("software").getAsJsonArray();
			Iterator<JsonElement> softwareIt = softwareArray.iterator();
			while(softwareIt.hasNext()){
				
				ConversionAction ca = new ConversionAction(
						j.get("name").getAsString(), 
						j.get("actor").getAsString(), 
						j.get("scope").getAsString(),
						j.get("description").getAsString());
				count++;
				System.out.println(count + "\t" + ca.toString());
				
				//loading the sequence of tool using for this action
				JsonElement softwareElement = softwareIt.next();
				JsonArray toolsInActionArray = softwareElement.getAsJsonArray();
				Iterator<JsonElement> toolsInActionIt = toolsInActionArray.iterator();
				
				while(toolsInActionIt.hasNext()){
					JsonObject toolInActionJson = toolsInActionIt.next().getAsJsonObject();
					ActionTool actionTool = 
							new ActionTool(
									toolInActionJson.get("name").getAsString(),
									toolInActionJson.get("params").getAsString(),
									toolInActionJson.get("url").getAsString());
					ca.getToolList().add(actionTool);
				}
				
				list.add(ca);
				//System.out.println(ca.toLongString());
			}
			
		}
		
		return list;
	}
	
	public String actionAddAction2Routine(){
		
		if(this.actionId != null &&
				this.routine.getActionList().stream().filter(a -> a.getId().equals(this.actionId)).count() > 0){
			this.addMsg("This action has been added already");
		}else if(this.actionId != null){
			this.routine.getActionList().add(Services.getInstance().getDaoService().getConversionAction(this.actionId));
		}
		
		return PAGE_NAME;
	}
	
	private List<ConversionRoutine> loadConversionRoutines() throws URISyntaxException, IOException{
		
		List<ConversionRoutine> list = new ArrayList<ConversionRoutine>();
		
		//Path path = Paths.get(getClass().getClassLoader().getResource(conversion_routines).toURI());
		Path path = Paths.get(getClass().getClassLoader().getResource(conversion_routines).toURI());
		
		JsonParser parser = new JsonParser();
        String content = new String (Files.readAllBytes(path), Charset.forName("UTF-8"));
        Object obj = parser.parse(content);
        JsonElement e = (JsonElement) obj;
        JsonArray array = e.getAsJsonArray();
        
		Iterator<JsonElement> it = array.iterator();
		while(it.hasNext()){
			JsonObject j = it.next().getAsJsonObject();
			
			String name = j.get("name").getAsString();
			String actionName = j.get("actionName").getAsString();
			String scope = j.get("scope").getAsString();
			String type =  (j.has("type")) ? j.get("type").getAsString() : "";
			
			List<String> inputPronomList = new ArrayList<String>();
			List<String> outputPronomList = new ArrayList<String>();
			
			List<String> inputExtensionList = new ArrayList<String>();
			List<String> outputExtensionList = new ArrayList<String>();
			
			JsonArray inputPid = j.get("inputPid").getAsJsonArray();
			Iterator<JsonElement> itInputPid = inputPid.iterator();
			while(itInputPid.hasNext()){
				inputPronomList.add(itInputPid.next().getAsString());
			}
			
			JsonArray outputPid = j.get("outputPid").getAsJsonArray();
			Iterator<JsonElement> itOutputPid = outputPid.iterator();
			while(itOutputPid.hasNext()){
				outputPronomList.add(itOutputPid.next().getAsString());
			}
			
			ConversionRoutine cr = new ConversionRoutine(
					name, 
					actionName,	
					scope,
					type,
					inputPronomList, 
					outputPronomList,
					inputExtensionList,
					outputExtensionList);
			list.add(cr);
		}
		
		return list;
	}
	
	public String actionDeleteAllRoutines(){
		
		for(ConversionRoutine routine : getConversionRoutineList()){
			Services.getInstance().getDaoService().deleteDBEntry(routine);
		}
		
		for(ConversionAction action : Services.getInstance().getDaoService().getConversionActionList((x1, x2) -> 0)){
			Services.getInstance().getDaoService().deleteDBEntry(action);
		}
		
		return PAGE_NAME;
	}
	
	public String actionInitConversionPlan(){
		
		if(getConversionRoutineList().isEmpty()){
			try {
				
				List<ConversionAction> actions = this.loadConversionActions();
				List<ConversionRoutine> routineList = this.loadConversionRoutines();
				
				for(ConversionAction action : actions){
					Services.getInstance().getDaoService().saveDBEntry(action);
				}
				
				for(ConversionRoutine routine : routineList){
					System.out.println(routine.toLongString());
					Services.getInstance().getDaoService().saveDBEntry(routine);
					routine.setActionList(Services.getInstance().getDaoService().getConversionAction(routine.getActionName()));
					Services.getInstance().getDaoService().saveDBEntry(routine);
				}
				
			} catch (Exception e) {
				addInternalError(e);
			}
		}else{
			addMsg("The conversion plan exists already.");
		}
		return PAGE_NAME;
	}

	public ConversionRoutine getRoutine() {
		return routine;
	}

	public void setRoutine(ConversionRoutine routine) {
		this.routine = routine;
	}

	public String getPronomInput() {
		return pronomInput;
	}

	public void setPronomInput(String pronomInput) {
		this.pronomInput = pronomInput;
	}

	public String getPronomOutput() {
		return pronomOutput;
	}

	public void setPronomOutput(String pronomOutput) {
		this.pronomOutput = pronomOutput;
	}

	public Long getActionId() {
		return actionId;
	}

	public void setActionId(Long actionId) {
		this.actionId = actionId;
	}
}
