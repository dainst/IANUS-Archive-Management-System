<%@page import="de.ianus.access.web.utils.MDUtils"%>
<%@page import="de.ianus.metadata.bo.utils.TextAttribute"%>
<%@page import="de.ianus.metadata.bo.actor.Actor"%>
<%@page import="de.ianus.metadata.bo.actor.Person"%>
<%@page import="de.ianus.metadata.bo.actor.Institution"%>

<jsp:useBean id="sessionBean" class="de.ianus.access.web.SessionBean" scope="session" />
<jsp:setProperty name="sessionBean" property="pageContext" value="${pageContext}" />

<%
MDUtils util = new MDUtils(sessionBean.getDc(), sessionBean);
Long wfipId = sessionBean.getWfip().getId();
String filePath = "web_assets/actor_logos/%actorID%.png";
String actorID = null;
%>

<div class="item">
    
    <h2>Kontaktperson(en)</h2>
    
   	<%
   	for(Actor actor : sessionBean.getDc().getContactPersonList()) {
   		
   		if(actor instanceof Person) {
   			Person person = (Person)actor;
   			%>
   			<div class="clearfix">
		   		<%
		   		String logo = util.getDataUriImage(wfipId, filePath.replace("%actorID%", Long.toString(actor.getId())), "Logo Ansprechpartner");
		   		if(logo != null && !logo.equals("")) {
		   			if(!MDUtils.empty(actor.getUrl())) {%><a href="<%= actor.getUrl() %>" class="image-link" target="_blank"><%= logo %></a><%}
		   			else{%><span class="image-link"><%= logo %></span><%}
		   		}
		   		%>
			    <div class="details">
			    	<%= MDUtils.getPerson(person) %>
			    </div>
			</div>
			
   			<%
   		}else if(actor instanceof Institution){
   			Institution inst = (Institution)actor;
   			%>
   			
   			<div class="clearfix">
		   		<%
		   		String logo = util.getDataUriImage(wfipId, filePath.replace("%actorID%", Long.toString(actor.getId())), "Logo Ansprechpartner");
		   		if(logo != null && !logo.equals("")) {
		   			%><a href="<%= actor.getUrl() %>" class="image-link" target="_blank"><%= logo %></a><%
		   		}
		   		%>
			    <div class="details">
				   <%= MDUtils.getInstitution(inst) %>
			    </div>
			</div>
   			<%
   		}
   	}
   	%>
   	
</div>