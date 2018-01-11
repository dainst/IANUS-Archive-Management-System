function checksum() {
	var elements = $(".someClassName");
	if (elements[0].files[0]) {
		var file = elements[0].files[0];
		alert(file.name);
	} else {
		alert('File no found');
	}
}

$(document).on("pfAjaxSend", function(event, xhr, options) {
	var source = options.source;
	if (source == 'uploadForm:uploadComponent') {
		alert(source);
	}
});



function processFiles(files) {
	var resultMap = {};
	var len = files.length;
	for(var i=0; i < len; i++){
		var file = files[i];
		var checksum = calculateChecksum(file);
		resultMap[file.name] = checksum;
	}
	return resultMap;
}

function calculateChecksum(file){
	return "XXX";
}