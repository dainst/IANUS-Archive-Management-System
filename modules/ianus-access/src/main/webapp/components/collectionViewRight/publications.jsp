<%@page import="de.ianus.metadata.bo.resource.Reference"%>
<%@page import="de.ianus.metadata.bo.resource.Publication"%>
<%@page import="de.ianus.ingest.core.Services"%>
<%@page import="de.ianus.access.web.utils.MDUtils"%>
<%@page import="de.ianus.metadata.bo.DataCollection"%>
<%@page import="java.util.Set" %>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<jsp:useBean id="sessionBean" class = "de.ianus.access.web.SessionBean" scope="session"/>
<jsp:setProperty name = "sessionBean" property="pageContext" value ="${pageContext}"/>

<%
DataCollection dc = sessionBean.getDc();
MDUtils mdUtil = new MDUtils(dc, sessionBean);

String uri = request.getRequestURI();
String pageName = uri.substring(uri.lastIndexOf("/")+1, uri.length() - 4);



Set<Reference> refList = dc.getReferenceList();
if(refList != null && refList.size() > 0) {%>

	<h2 id="publications">Publikationen</h2>
	<div class="publications">
		<%
		int i = 0;
		for(Reference reference : refList){
			if(pageName.equals("collectionOverview") && i >= 3) break;
			
			Publication pub = Services.getInstance().getMDService().getPublication(reference.getPublicationId());
			reference.setPublication(pub);
			%>
			<p>
				<%= MDUtils.escape(reference.getLabel()) %>
				<%= MDUtils.getPublicationPIDList(reference.getPublication().getPidList()) %>
			</p>
			<% i++; %>
		<%}%>
		
		<% if(pageName.equals("collectionOverview")) { %>
			<a href="#collectionMetadata#publications" class="more-link">Mehr</a>
		<%}%>
	</div>
<%}%>