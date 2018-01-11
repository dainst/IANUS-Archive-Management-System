<%@page import="de.ianus.metadata.bo.utils.ElementOfList"%>
<%@page import="de.ianus.access.web.utils.MDUtils"%>
<%@page import="de.ianus.metadata.bo.utils.Time"%>
<%@page import="de.ianus.metadata.bo.DataCollection" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<jsp:useBean id="sessionBean" class="de.ianus.access.web.SessionBean" scope="session" />
<jsp:setProperty name="sessionBean" property="pageContext" value="${pageContext}" />

<%
DataCollection dc = sessionBean.getDc();
MDUtils mdUtil = new MDUtils(dc, sessionBean);

String uri = request.getRequestURI();
String pageName = uri.substring(uri.lastIndexOf("/")+1, uri.length() - 4);
Integer limit = (pageName.equals("collectionMetadata")) ? null : 1;
%>


<h2 id="keywords">Schlagwörter</h2>
<div class="keywords">
	
	<!-- ###### Discipline List Creation ####### -->	
	<h3>Fachdisziplinen</h3>
	
	<%
	String disciplines0 = MDUtils.implode(MDUtils.getElementOfListList(dc.getMainDisciplineList()));
	if(!MDUtils.empty(disciplines0)) { %>
		<p><span>Allgemein: </span>
			<%= disciplines0 %>
		</p>
	<% } %>
	
	<%
	String disciplines1 = MDUtils.implode(MDUtils.getElementOfListList(dc.getSubDisciplineList()));
	if(pageName.equals("collectionMetadata") && !MDUtils.empty(disciplines1)) { %>
		<p><span>Speziell: </span>
			<%= disciplines1 %>
		</p>
	<% } %>
	 
	<!-- ###### Content List Creation ####### -->
	<h3>Gegenstand</h3>
	<%
	String content0 = MDUtils.implode(MDUtils.getElementOfListList(dc.getMainContentList()));
	if(!MDUtils.empty(content0)) { %>
		<p><span>Allgemein: </span>
			<%= content0 %>
		</p>
	<% } %>
	
	<%
	String content1 = MDUtils.implode(MDUtils.getElementOfListList(dc.getSubContentList()));
	if(pageName.equals("collectionMetadata") && !MDUtils.empty(content1)) { %>
		<p><span>Speziell: </span>
			<%= content1 %>
		</p>
	<% } %>
	
	<!-- ###### Time Period (Main, Sub) List Creation ####### -->
	<h3>Zeitstellung</h3>
	
	<% if(!MDUtils.empty(mdUtil.getMainPeriod())) { %>
		<p><span>Allgemein: </span>
			<%= mdUtil.getMainPeriod() %>
		</p>
	<% } %>
	
	<% if(pageName.equals("collectionMetadata") && !MDUtils.empty(mdUtil.getSubPeriod())) { %>
		<p><span>Speziell: </span>
			<%= mdUtil.getSubPeriod() %>
		</p>
	<% } %>
	
	<% if(!MDUtils.empty(mdUtil.getProjectDuration())) { %>
		<p><span>Projektdauer: </span>
			<%= mdUtil.getProjectDuration() %>
		</p>
	<% } %>
	
	<% if(!MDUtils.empty(mdUtil.getDataGeneration())) { %>
		<p><span>Datenerzeugung: </span>
			<%= mdUtil.getDataGeneration() %>
		</p>
	<% } %>
	
	<!-- ###### Method List Creation ####### -->
	<h3>Methode</h3>
	<%
	String method0 = MDUtils.implode(MDUtils.getElementOfListList(dc.getMainMethodList()));
	if(!MDUtils.empty(method0)) {%>
		<p><span>Allgemein: </span>
			<%= method0 %>
		</p>
	<%} %>
	
	<%
	String method1 = MDUtils.implode(MDUtils.getElementOfListList(dc.getSubMethodList()));
	if(pageName.equals("collectionMetadata") && !MDUtils.empty(method1)) { %>
		<p><span>Speziell: </span>
			<%= method1 %>
		</p>
	<% } %>
	
	
	<% if(pageName.equals("collectionOverview")) { %>
		<a href="#collectionMetadata#keywords" class="more-link">Mehr</a>
	<% } %>
</div>



<% if(!MDUtils.empty(mdUtil.getLocalization(limit))) { %>
	<h2 id="localization">Lokalisierung</h2>
	<%= mdUtil.getLocalization(limit) %>
<% } %>
	
	



