<%@page import="de.ianus.metadata.bo.resource.RelatedResource"%>
<%@page import="de.ianus.metadata.bo.utils.Identifier"%>
<%@page import="de.ianus.access.web.utils.MDUtils"%>
<%@page import="de.ianus.metadata.bo.DataCollection"%>
<%@page import="java.util.HashSet" %>
<%@page import="java.util.Set" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<jsp:useBean id="sessionBean" class = "de.ianus.access.web.SessionBean" scope="session"/>
<jsp:setProperty name = "sessionBean" property="pageContext" value ="${pageContext}"/>

<%
DataCollection dc = sessionBean.getDc();
MDUtils mdUtil = new MDUtils(dc, sessionBean);

String uri = request.getRequestURI();
String pageName = uri.substring(uri.lastIndexOf("/")+1, uri.length() - 4);



Set<RelatedResource> refList = dc.getRelatedResourceList();
if(refList != null && refList.size() > 0) {
	
	%><h2>Zugehörige Quellen</h2><%

	for(RelatedResource reference : refList){
		%>
		<p>
			<% //TODO: multilingual %>
			<%= reference.getDescription() %>
			<%
			String id = MDUtils.getLongIdentifierLink(reference.getIdentifier());
			if(!MDUtils.empty(id)) {%>
				<br>
				<%= id %>
			<%} %>
			
			<%
			if(!MDUtils.empty(reference.getLocation())) {%>
				<br>
				<%= reference.getLocation() %>
			<%}%>
		</p>
		<%
	}
}
%>




