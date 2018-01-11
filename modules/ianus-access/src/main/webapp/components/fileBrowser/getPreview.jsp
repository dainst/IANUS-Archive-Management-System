<%@page import="java.io.IOException"%>
<%@page import="java.nio.file.Paths"%>
<%@page import="java.nio.file.Files"%>
<%@page import="java.nio.file.Path"%>
<%@page import="de.ianus.access.web.utils.CFUtils"%>
<%@page import="de.ianus.ingest.core.bo.WorkflowIP"%>
<%@page import="de.ianus.ingest.core.Services"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="org.springframework.mail.javamail.ConfigurableMimeFileTypeMap"%>
<%@page trimDirectiveWhitespaces="true" %>


<jsp:useBean id="sessionBean" class="de.ianus.access.web.SessionBean" scope="session" />
<jsp:setProperty name="sessionBean" property="pageContext" value="${pageContext}" />

<%
response.setHeader("Content-Disposition", "inline");

try{
	
	Long wfipId = sessionBean.getWfip().getId();
	
	String filePath = request.getParameter("filePath");
	filePath = CFUtils.stripSlashes(filePath);
	
	if(StringUtils.isNotEmpty(filePath)){
		WorkflowIP wfip = Services.getInstance().getDaoService().getWorkflowIP(wfipId, sessionBean.getWfipClass());
		
		if(wfip == null){
			throw new Exception("An IP with id=" + wfipId + "has not been found");
		}
		
		String absolutePath = wfip.getAbsolutePath() + "/" + filePath ;
		
		String mime = new ConfigurableMimeFileTypeMap().getContentType(absolutePath);
		response.setContentType(mime);
	
		Path path = Paths.get(absolutePath);
		
		byte[] data = Files.readAllBytes(path);
		response.getOutputStream().write(data);
	
	}
	
}catch(IOException ex){
	ex.printStackTrace();
}
%>