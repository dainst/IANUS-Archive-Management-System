<%@page import="java.io.IOException"%>
<%@page import="java.nio.file.Paths"%>
<%@page import="java.nio.file.Files"%>
<%@page import="java.nio.file.Path"%>
<%@page trimDirectiveWhitespaces="true" %>
<%@page import="org.apache.commons.lang.StringUtils"%>


<jsp:useBean id="sessionBean" class="de.ianus.access.web.SessionBean" scope="session" />
<jsp:setProperty name="sessionBean" property="pageContext" value="${pageContext}" />

<%
response.setContentType("image/png");
response.setHeader("Content-Disposition", "inline");

try{
	
	String filePath = request.getParameter("filePath");
	if(StringUtils.isNotEmpty(filePath)){
		Path path = Paths.get(filePath);
		byte[] data = Files.readAllBytes(path);
		response.getOutputStream().write(data);
	}
	
}catch(IOException ex){
	ex.printStackTrace();
}
%>