<jsp:useBean id="sessionBean" class="de.ianus.access.web.SessionBean" scope="session" />

<div id="pagenavtabs" class="parallax" data-start="250" data-speed="1" style="position: absolute; left: 0px;">
	
	<div class="container">
		<button type="button"
			class="navbar-toggle collapsed" 
			data-toggle="collapse"
			data-target="#pagenav" 
			aria-expanded="false" 
			aria-controls="navbar"
		>
			<span class="sr-only">Toggle navigation</span>
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>
			Seitennavigation
		</button>
	
		
		<div id="pagenav" class="collapse navbar-collapse">
		
			<ul class="nav nav-tabs" role="tablist">
				
				<li role="presentation">
					<a href="#collectionOverview">Projektübersicht</a>
				</li>
				<li role="presentation">
					<a href="#collectionMetadata">Metadaten</a>
				</li>
				<li role="presentation">
					<a href="#collectionFiles">Daten</a>
				</li>
			</ul>
		</div>
	
	</div>
		
</div>


<!-- bootstrap toolkit is for nav stacking on small screens -->
<script type="text/javascript" src="<%= sessionBean.getAppBean().getContext() %>/resources/js/bootstrap/bootstrap-toolkit.min.js"></script>

<!-- main script -->
<script type="text/javascript" src="<%= sessionBean.getAppBean().getContext() %>/resources/js/collectionNavbar.js"></script>