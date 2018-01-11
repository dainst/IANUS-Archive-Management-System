<%@page import="de.ianus.access.web.utils.MDUtils"%>
<%@page import="de.ianus.metadata.bo.utils.TextAttribute"%>
<%@page import="de.ianus.metadata.bo.utils.ElementOfList"%>
<%@page import="java.util.Set" %>

<jsp:useBean id="sessionBean" class="de.ianus.access.web.SessionBean" scope="session" />
<jsp:setProperty name="sessionBean" property="pageContext" value="${pageContext}" />

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<% MDUtils mdUtil = new MDUtils(sessionBean.getDc(), sessionBean); %>



	
<%
Set<ElementOfList> list = sessionBean.getDc().getClassificationList();
if(list != null && list.size() > 0) {
	%>
	<div>
	<h2>Informationen beziehen sich auf</h2>
	<%
	int i = 0;
	for(ElementOfList item : list) {
		if(i > 0) {%> · <%}%>
		<%= MDUtils.escape(item.getValue()) %>
		<% i++;
	}
	%>
	</div><%
}
%>



<% if(mdUtil.hasTextAttribute(TextAttribute.ContentType.summary)) { %>
	<h2>Zusammenfassung</h2>
	<div class="whitespace">
		<%= mdUtil.getTextAttributeValue(TextAttribute.ContentType.summary) %>
	</div>
<% } %>


<jsp:include page="../../components/collectionViewRight/keywords.jsp" />


<% if(mdUtil.hasTextAttribute(TextAttribute.ContentType.projectDescription)) { %>
	<h2>Beschreibung Projekt</h2>
	<div class="whitespace">
		<%= MDUtils.escape(mdUtil.getTextAttributeValue(TextAttribute.ContentType.projectDescription)) %>
	</div>
<% } %>

<% if(mdUtil.hasTextAttribute(TextAttribute.ContentType.dataCollectionDescription)) { %>
	<h2>Beschreibung Datensammlung</h2>
	<div class="whitespace">
		<%= MDUtils.escape(mdUtil.getTextAttributeValue(TextAttribute.ContentType.dataCollectionDescription)) %>
	</div>
<% } %>

<jsp:include page="../../components/collectionViewRight/publications.jsp" /> 


<% if(mdUtil.hasTextAttribute(TextAttribute.ContentType.motivation)) { %>
	<h2>Kurz-Statement des Datengebers</h2>
	<div class="italic whitespace">
		"<%= MDUtils.escape(mdUtil.getTextAttributeValue(TextAttribute.ContentType.motivation)) %>"
	</div>
<% } %>


        