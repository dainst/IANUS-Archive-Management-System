<%@page import="de.ianus.access.web.utils.MDUtils"%>
<%@page import="de.ianus.metadata.bo.utils.TextAttribute"%>
<%@page import="de.ianus.metadata.bo.DataCollection"%>


<jsp:useBean id="sessionBean" class="de.ianus.access.web.SessionBean" scope="session" />
<jsp:setProperty name="sessionBean" property="pageContext" value="${pageContext}" />

<%
DataCollection dc = sessionBean.getDc();
MDUtils util = new MDUtils(dc, sessionBean);

%>


<div id="header" class="container">
	
	<div class="ribbon wide">
		<div>
			<a href="<%= sessionBean.getAppBean().getContext() %>/pages/information.jsp#version-info" id="logo-prototyp">Version 0.2</a>
		</div>
	</div>
	
	<a href="" id="top" title="Projektübersicht"></a>
	<div class="row headerCollection">
		<img src="<%= sessionBean.getAppBean().getContext() 
				+ "/components/getCollectionPreview.jsp?filePath=" 
				+ sessionBean.getWfip().getWebAssetsFolder() 
				+ "/header.png" %>"
			alt="preview image"
			class="img-responsive collectionImage">
		<div class="col-lg-6 col-md-8 col-sm-10 col-xs-10 collectionTitle">
			<h1>
				<a href="" title="Projektübersicht">
					<%= util.getTextAttributeValue(TextAttribute.ContentType.title) %>
				</a>
			</h1>
			<p class="collectionDate"><%= util.getPresentationDate() %></p>
		</div>
	</div>
	
	<jsp:include page="../components/submenu.jsp" />

	<div class="ribbon narrow">
		<div></div>
	</div>
	
</div>