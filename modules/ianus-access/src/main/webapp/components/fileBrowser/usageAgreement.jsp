
<jsp:useBean id="sessionBean" class="de.ianus.access.web.SessionBean" scope="session" />
<jsp:setProperty name="sessionBean" property="pageContext" value="${pageContext}" />






<div id="usageAgreement" class="fancybox-content" style="display: none">
	<div class="interface">
		<button class="close" type="button" 
			onclick="javascript:acceptUsagePolicy();">
			Akzeptieren
		</button>
	</div>
	<object data="<%= sessionBean.getAppBean().getContext() %>/resources/documents/Nutzungsbedingungen.pdf" type="application/pdf"
		width="100%" height="100%">
		<h2>Nutzungsbedingungen</h2>
		<p>
			Ihr Browser unterstützt keine eingebetteten Objekte. 
			Bitte ändern Sie ggf. die Anzeigeeinstellungen für PDF Dokumente zu "Anzeige im Browser"
			oder laden Sie die Nutzungsbedingungen herunter. <br><br>
			<a href="<%= sessionBean.getAppBean().getContext() %>/resources/documents/Nutzungsbedingungen.pdf" type="application/pdf">Nutzungsbedingungen Download</a>.
		</p>
	</object>
</div>