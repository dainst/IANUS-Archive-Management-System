package de.ianus.ingest.web.pages;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.conversionRoutine.bo.ActionTool;
import de.ianus.ingest.core.conversionRoutine.bo.ConversionAction;
import de.ianus.ingest.core.conversionRoutine.bo.ConversionRoutine;
import de.ianus.ingest.web.AbstractBean;

public class ActionPage extends AbstractBean{
	
	public static final String PAGE_NAME = "action";
	
	private ConversionAction action;
	private ConversionRoutine routine;
	private ActionTool tool = new ActionTool();
	
	public void loadAction(ConversionAction action, ConversionRoutine routine){
		this.action = action;
		this.routine = routine;
	}
	
	public String actionDeleteAction(){
		addMsg("TODO");
		return PAGE_NAME;
	}
	
	public String actionEditAction(){
		addMsg("TODO");
		return PAGE_NAME;
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
	
	public String actionSaveActionAndGotoConversionPlan(){
		try {
			if(StringUtils.isNotEmpty(this.action.getName())){
				Services.getInstance().getDaoService().saveDBEntry(action);
				if(!this.routine.getActionList().contains(action)){
					this.routine.getActionList().add(action);
				}
				getSession().getConversionPlanPage().load(routine);
			}else{
				addMsg("The name of the action can not be empty");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ConversionPlanPage.PAGE_NAME;
	}
	
	public String actionSaveAction(){
		try {
			if(StringUtils.isNotEmpty(this.action.getName())){
				Services.getInstance().getDaoService().saveDBEntry(action);
			}else{
				addMsg("The name of the action can not be empty");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return PAGE_NAME;
	}
	
	public String actionBackToConversionPlanePage(){
		getSession().getConversionPlanPage().load(routine);
		return ConversionPlanPage.PAGE_NAME;
	}

	public String actionEditTool(){
		this.tool = (ActionTool)getRequestBean("tool");
		return PAGE_NAME;
	}
	
	public String actionRemoveTool(){
		ActionTool tool = (ActionTool)getRequestBean("tool");
		this.action.getToolList().remove(tool);
		return PAGE_NAME;
	}
	
	public String actionAddTool(){
		if(StringUtils.isNoneEmpty(this.tool.getSoftware())){
			if(!this.action.getToolList().contains(tool)){
				this.action.getToolList().add(this.tool);
			}
			this.tool = new ActionTool();
		}else{
			addMsg("The name of the software can not be empty");
		}
		
		return PAGE_NAME;
	}
	
	public String actionCancelEditionTool(){
		this.tool = new ActionTool();
		return PAGE_NAME;
	}
	
	public ConversionAction getAction() {
		return action;
	}

	public void setAction(ConversionAction action) {
		this.action = action;
	}

	public ConversionRoutine getRoutine() {
		return routine;
	}

	public void setRoutine(ConversionRoutine routine) {
		this.routine = routine;
	}

	public ActionTool getTool() {
		return tool;
	}

	public void setTool(ActionTool tool) {
		this.tool = tool;
	}
}	
