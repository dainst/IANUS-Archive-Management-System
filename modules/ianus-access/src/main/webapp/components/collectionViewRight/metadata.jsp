<%@page import="de.ianus.access.web.utils.MDUtils"%>
<%@page import="de.ianus.metadata.bo.utils.Identifier"%>
<%@page import="de.ianus.metadata.bo.actor.Person"%>
<%@page import="de.ianus.metadata.bo.DataCollection"%>
<%@page import="java.util.List"%>
<%@page import="de.ianus.metadata.bo.actor.Actor"%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<jsp:useBean id="sessionBean" class="de.ianus.access.web.SessionBean" scope="session" />
<jsp:setProperty name="sessionBean" property="pageContext" value="${pageContext}" />

<%
DataCollection dc = sessionBean.getDc();
MDUtils mdUtil = new MDUtils(dc, sessionBean);
%>




<jsp:include page="../../components/collectionViewRight/keywords.jsp" />




<%
String actors = mdUtil.getActorsList();
if(!MDUtils.empty(actors)) { %>
	<h2>Akteure</h2>
	<div class="actors">
		<%= actors %>
	</div>
<% } %>


<% if(!MDUtils.empty(mdUtil.getExternalIdentifierList())) { %>
	<h2 id="foreignIdentifications">Externe Kennungen</h2>
	<div class="md-output">
		<%= mdUtil.getExternalIdentifierList() %>
	</div>
<% } %>


<jsp:include page="../../components/collectionViewRight/publications.jsp" />

<jsp:include page="../../components/collectionViewRight/relatedResources.jsp" />

<jsp:include page="../../components/collectionViewRight/statistic.jsp" />



<div>
	<a href="#collectionFiles">zu den Daten</a>
</div>
	