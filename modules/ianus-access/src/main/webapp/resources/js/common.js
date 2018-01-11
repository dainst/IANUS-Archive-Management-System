

function getQuery () {
	var queryDict = {};
	location.search.substr(1).split("&").forEach(function(item) {queryDict[item.split("=")[0]] = item.split("=")[1]});
	return queryDict;
}

var dipId = null;
if(typeof getQuery().dipId != 'undefined') dipId = getQuery().dipId;