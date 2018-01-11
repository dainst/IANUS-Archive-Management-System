<%@page import="de.ianus.metadata.bo.DataCollection"%>
<%@page import="de.ianus.access.web.utils.MDUtils"%>
<%@page import="de.ianus.metadata.bo.utils.TextAttribute"%>





<jsp:useBean id="sessionBean" class="de.ianus.access.web.SessionBean" scope="session" />
<jsp:setProperty name="sessionBean" property="pageContext" value="${pageContext}" />

<%
DataCollection dc = sessionBean.getDc();
MDUtils util = new MDUtils(dc, sessionBean);

if(sessionBean.getWfip().hasFiles()) {
%>

<div class="item citation">
    <h2>Identifikation &amp; Zitation</h2>
    <div>
        <img src="<%= sessionBean.getAppBean().getContext() %>/resources/images/zitierhinweis.png" 
        alt="Zitierhinweis">
        <p>
        	IANUS Sammlung <%= util.getCollectionNumber() %><br>
        	DOI <%= util.getDOI() %>
        </p>
	</div>
    
    <a data-fancybox 
    	data-src="#citationOverlay" 
    	href="javascript:;">Zitierhinweis</a>
    
</div>

<div style="display:none">
    <div id="citationOverlay">
        <h2>Identifikation &amp; Zitation</h2>
        <p>
        	Die Sammlung hat in IANUS die Kennung: <%= util.getCollectionNumber() %>
        </p>
        		
        <p>
        	Die Sammlung kann permanent mit dem DOI <%= util.getDOI() %>
        	referenziert werden: <br>
        	<a href="http://dx.doi.org/<%= util.getDOI() %>"
        		target="_blank">http://dx.doi.org/<%= util.getDOI() %></a>
        </p>	
        <p>
        	<b>Als vollst&auml;ndiges Zitat empfehlen wir:</b><br>
        	<%= util.getAbbreviatedAuthorList(" - ") %>,
        	<%= util.getTextAttributeValue(TextAttribute.ContentType.title) %>.
        	(<%= util.getPresentationDate() %>), Datensammlung hrsg. v. IANUS.
        	<br>
        	DOI: <%= util.getDOI() %>
        </p>
        <p>
        	<b>F&uuml;r die Referenzierung einer Einzeldatei empfehlen wir in Analogie 
        	zur Zitationen von Artikeln in einem Sammelband:</b>
        	<br>
        	Dateiname.Dateiendung, im Ordner X &gt; Ordner Y &gt; Ordner Z,
        	in:<br>
        	<%= util.getAbbreviatedAuthorList(" - ") %>,
        	<%= util.getTextAttributeValue(TextAttribute.ContentType.title) %>.
        	(<%= util.getPresentationDate() %>), Datensammlung hrsg. v. IANUS. 
        	<br>
        	DOI: <%= util.getDOI() %>
        </p>
        		
	</div>
</div>

<% } %>