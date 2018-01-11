<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="sessionBean" class="de.ianus.access.web.SessionBean" scope="session" />
<jsp:setProperty name="sessionBean" property="pageContext" value="${pageContext}" />
<% sessionBean.loadInformationPackage(); %>

<t:mainTemplate>

	<jsp:attribute name="header">
      	<jsp:include page="../components/headerCollection.jsp" />
    </jsp:attribute>
    
    <jsp:attribute name="footer">
      	<jsp:include page="../components/footer.jsp" />
    </jsp:attribute>
    
    

    <jsp:body>
    	<jsp:include page="../components/collectionNavbar.jsp"/>
    	
    	<div class="row" id="collectionView">
    		<!-- empty div for the tab content, provided via AJAX -->
    	</div>
    </jsp:body>
    
</t:mainTemplate>