<jsp:useBean id="sessionBean" class="de.ianus.access.web.SessionBean" scope="session" />
<% request.setAttribute("context", sessionBean.getAppBean().getContext()); %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<div id="submenu">
	<ul>
		<li><a href="${context}/pages/information.jsp">Über das Datenportal</a></li>
		
		<li>
			<c:choose>
				<c:when test="${ sessionBean.isHome(request.getRequestURI()) }">
					<a class="selected" href="${context}">Startseite</a>
				</c:when>
				<c:otherwise>
					<a href="${context}">Startseite</a>
				</c:otherwise>
			</c:choose>
		</li>
		
	</ul>
</div>

