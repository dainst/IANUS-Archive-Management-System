

<jsp:useBean id="sessionBean" class="de.ianus.access.web.SessionBean" scope="session" />
<jsp:setProperty name="sessionBean" property="pageContext" value="${pageContext}" />

<%
if(sessionBean.getWfip().hasFiles()) {
	%>
	<script type="text/javascript" src="<%= sessionBean.getAppBean().getContext() %>/resources/js/collectionFiles.js"></script>
	
	<jsp:include page="../components/fileBrowser/usageAgreement.jsp" />
	
	<jsp:include page="../components/fileBrowser/downloadLicense.jsp"/>
	
	<div class="row">
		<div class="col-md-5 col-xs-4">
			<h2>Verzeichnisstruktur</h2>
			<div id=fileTree role="main"></div>
			
			<jsp:include page="collectionViewLeft/contact.jsp"/>
			<jsp:include page="collectionViewLeft/citation.jsp"/>
			<jsp:include page="collectionViewLeft/license.jsp"/>
			<jsp:include page="collectionViewLeft/hosting.jsp"/>
		</div>
	
		<div class="col-md-7 col-xs-8">
			<h2>Daten</h2>
			<div id="fileList"></div>
		</div>
	</div>
	
	<%
}else{
	
	%>
	<script type="text/javascript">
		Cookies.set('IANUS-DIP.' + dipId + '.usagePolicy', 'accepted', {expires: 365});
	</script>
	
	<div class="col-xs-4">
		<jsp:include page="collectionViewLeft/contact.jsp"/>
		<jsp:include page="collectionViewLeft/license.jsp"/>
		<jsp:include page="collectionViewLeft/hosting.jsp"/>
	</div>
	<div class="col-xs-8">
		<jsp:include page="collectionViewRight/relatedResources.jsp" />
	</div>
	<%
}
%>