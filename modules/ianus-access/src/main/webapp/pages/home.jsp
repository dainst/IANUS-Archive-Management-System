<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>





<t:mainTemplate>

	<jsp:attribute name="header">
      	<jsp:include page="../components/headerHome.jsp" />
    </jsp:attribute>
    
    <jsp:attribute name="footer">
      	<jsp:include page="../components/footer.jsp" />
    </jsp:attribute>
	
	
    
    <jsp:body>
    	<jsp:include page="../components/collectionsList.jsp" />
    </jsp:body>
    
</t:mainTemplate>