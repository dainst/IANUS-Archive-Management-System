
function getQuery () {
	var queryDict = {};
	location.search.substr(1).split("&").forEach(function(item) {queryDict[item.split("=")[0]] = item.split("=")[1]});
	return queryDict;
}

var dipId = null;
if(typeof getQuery().dipId != 'undefined') dipId = getQuery().dipId;


// set searchTerm as Get-Parameter:
function setSearchTermAsGetParameter() {
   
	// get searchTerm from SearchField:
	var searchTerm = $('#searchField').val();
	
	// redirect to /ianus-access with searchTerm as GET-Parameter:
	var currentLocation = window.location.href.split('?')[0];
	window.location.replace(currentLocation + "?q=" + searchTerm);
}

// enter-key-submit for searchField:
$(document).keyup(function(event) {
    if ($("#searchField").is(":focus") && event.key == "Enter") {
    	setSearchTermAsGetParameter();
    }
});