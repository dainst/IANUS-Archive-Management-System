package de.ianus.access.web;

import javax.servlet.jsp.PageContext;

import org.apache.commons.lang.StringUtils;

public class JSPWrapper {
	
	//private HttpServletRequest request;
	//private HttpServletResponse response;
	private PageContext pageContext;
	
	public static String SESSION_BEAN = "sessionBean";
	private static ApplicationBean appBean;
	
	public SessionBean getSessionBean(){
		return (SessionBean)getSessionBean(SESSION_BEAN);
	}
	
	public ApplicationBean getAppBean() {
		if(appBean == null){
			appBean = new ApplicationBean();
		}
		return appBean; 
	}
	
	
	private Object getSessionBean(String beanName) {
		return this.getPageContext().getSession().getAttribute(beanName);
	}
	
	/*
	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		System.out.println(response);
		this.response = response;
	}*/
	
	public void init(){
		System.out.println("THIS METHOD SHOULD BE IMPLEMENTED!!!!");
	}

	public PageContext getPageContext() {
		return pageContext;
	}

	public boolean isHome(String requestUri){
		String contextPath = pageContext.getServletContext().getContextPath() + "/";
		String homeRelativePath = "pages/home.jsp";
		return StringUtils.equals(requestUri, contextPath) || StringUtils.equals(requestUri, contextPath + homeRelativePath);
	}
	
	public void setPageContext(PageContext pageContext) {
		this.pageContext = pageContext;
	}
}
