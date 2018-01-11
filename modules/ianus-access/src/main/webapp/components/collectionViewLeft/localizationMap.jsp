<%@page import="de.ianus.access.web.utils.MDUtils"%>
<%@page import="de.ianus.metadata.utils.RetrievalUtils"%>
<%@page import="de.ianus.metadata.bo.utils.TextAttribute"%>
<%@page import="de.ianus.metadata.bo.DataCollection"%>
<%@page import="de.ianus.metadata.bo.utils.Place"%>

<%@page import="java.util.Set"%>

<jsp:useBean id="sessionBean" class="de.ianus.access.web.SessionBean" scope="session" />
<jsp:setProperty name="sessionBean" property="pageContext" value="${pageContext}" />



    
<%
DataCollection dc = sessionBean.getDc();
MDUtils util = new MDUtils(dc, sessionBean);

String places = util.getGeometries();



if(places != null && !places.equals("")) {%>
	
	<div class="item">
		<h2>Lokalisierung</h2>
		
		<div class="map" id="localization"></div>
		<a href="javascript: void(0);" onclick="downloadGeometries();">Geodaten downloaden</a>
		
		<script type="text/javascript">
			var areaMap = L.map('localization', {
				scrollWheelZoom: false
			});
			areaMap.setView([50.936047, 6.952903], 10);
			areaMap.on('click', function() {
		  		if(!areaMap.scrollWheelZoom.enabled()) {
		  			areaMap.scrollWheelZoom.enable();
		    	}
		  	});
			areaMap.on('mouseout', function() {
		  		areaMap.scrollWheelZoom.disable();
		  	});
			L.tileLayer('http://api.tiles.mapbox.com/v3/webmasterdai.map-mzpowsr9/{z}/{x}/{y}.png').addTo(areaMap);
			L.control.fullscreen().addTo(areaMap);
			var wicket = new Wkt.Wkt();
			var features = L.featureGroup();
			
			var places = <%= places %>;
			
			for(var i = 0; places.length > i; i++) {
				wicket.read(places[i].wkt);
				var feature = wicket.toObject();
				feature.setStyle({
					color: "#164194",
					weight: 5
				});
				
				if(typeof places[i].type != 'undefined' && places[i].type != "")
					feature.bindPopup(places[i].type);
				
				features.addLayer(feature);
			}
			
			areaMap.addLayer(features);
			areaMap.fitBounds(features.getBounds(), {padding: [20, 20]});
			
			function downloadGeometries() {
				var text = <%= places %>;
				var link = document.createElement('a');
				link.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(JSON.stringify(text, null, 4)));
				link.setAttribute('download', 'geodata.json');
				if (document.createEvent) {
					var event = document.createEvent('MouseEvents');
					event.initEvent('click', true, true);
					link.dispatchEvent(event);
				}else{
					link.click();
				}
				return false;
			}
			
			
		</script>
		
	</div>
<%}%>

