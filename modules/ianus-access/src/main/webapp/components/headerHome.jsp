
<jsp:useBean id="sessionBean" class="de.ianus.access.web.SessionBean" scope="session" />

<div id="header" class="container">
	
	<div class="ribbon wide">
		<div>
			<a href="<%= sessionBean.getAppBean().getContext() %>/pages/information.jsp#version-info" id="logo-prototyp">Version 0.2</a>
		</div>
	</div>
	
	<div class="container headerHome">
		
		<a class="image-link" title="IANUS Startseite" href="http://www.ianus-fdz.de">Home</a>
		
		<div class="header-right">
			<h1><a href="http://datenportal.ianus-fdz.de">Datenportal</a></h1>
			<h2><a href="http://datenportal.ianus-fdz.de">Digitaler Forschungsdaten aus
				Arch&auml;ologie &amp; Altertumswissenschaften</a></h2>
		</div>
		
		<div class="main-menu">
			<ul>
				<li><a href="http://www.ianus-fdz.de" target="_blank">Forschungsdatenzentrum</a></li>
				<li><a href="http://datenportal.ianus-fdz.de" class="selected">Datenportal</a></li>
				<li><a href="http://www.ianus-fdz.de/it-empfehlungen" target="_blank">IT-Empfehlungen</a></li>		
			</ul>
		</div>
		
	</div>
	
	<jsp:include page="../components/submenu.jsp" />
	
	<div class="ribbon narrow">
		<div></div>
	</div>
	
</div>