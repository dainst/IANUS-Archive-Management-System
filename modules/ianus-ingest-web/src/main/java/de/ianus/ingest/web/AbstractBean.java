package de.ianus.ingest.web;

import java.util.ArrayList;

import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

public abstract class AbstractBean {
	
	public static String BEAN_APP = "appBean";
	public static String BEAN_SESSION = "sessionBean";
	
	protected SessionBean getSession(){
		SessionBean session = 
			((SessionBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(BEAN_SESSION));
		
		if(session == null){
			session = new SessionBean();
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(BEAN_SESSION, session);
		}
		
		return session;
	}
	
	protected Object getRequestBean(String name){
		return FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get(name);
	}
	
	public void addMsg(String msg){
		if(getSession().getMsgList() == null)
			getSession().setMsgList(new ArrayList<String>());
		getSession().getMsgList().add(msg);
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('msgDialog').show();");
	}
	
	public ApplicationBean getAppBean(){
		return ApplicationBean.getInstance();
	}
	
	public void addInternalError(Exception e){
		e.printStackTrace();
		addMsg("Internal Error: ");
		
		StackTraceElement[] st = e.getStackTrace();
		addMsg(e.getClass().getName() + ": " + e.getMessage());
	    for (int i = 0; i < st.length && i < 5; i++) {
	    	addMsg("at " + st[i].toString());
	    }
	}
}
