<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.io.IOException"%>
<%@page import="java.nio.file.Paths"%>
<%@page import="java.nio.file.Files"%>
<%@page import="java.nio.file.Path"%>
<%@page import="de.ianus.ingest.core.bo.WorkflowIP"%>
<%@page import="de.ianus.ingest.core.Services"%>
<%@page import="de.ianus.access.web.utils.CFUtils"%>
<%@page import="org.springframework.mail.javamail.ConfigurableMimeFileTypeMap"%>
<%@page trimDirectiveWhitespaces="true" %>


<jsp:useBean id="sessionBean" class="de.ianus.access.web.SessionBean" scope="session" />
<jsp:setProperty name="sessionBean" property="pageContext" value="${pageContext}" />

<%
try{
	
	Long wfipId = sessionBean.getWfip().getId();
	
	String filePath = request.getParameter("filePath");
	filePath = CFUtils.stripSlashes(filePath);
	
	if(StringUtils.isNotEmpty(filePath)){
		WorkflowIP wfip = Services.getInstance().getDaoService().getWorkflowIP(wfipId, sessionBean.getWfipClass());
		
		if(wfip == null){
			throw new Exception("A WfIP with id=" + wfipId + "has not been found");
		}
		
		String absolutePath = wfip.getAbsolutePath() + "/" + filePath ;
	
		Path path = Paths.get(absolutePath);
		
		String mime = new ConfigurableMimeFileTypeMap().getContentType(absolutePath);
		response.setContentType(mime);
		
		response.setHeader("Content-Disposition", "attachment; filename=\"" + path.getFileName()  + "\"");
		
		byte[] data = Files.readAllBytes(path);
		response.getOutputStream().write(data);
	
	}
	
}catch(IOException ex){
	ex.printStackTrace();
}
%>
