$(function() {
    $.ajax({
        async : true,
        type : "GET",
        url : contextURL + "/components/fileBrowser/getDirectoryTree.jsp?dipId=" + dipId,
        dataType : "json",
        data: 	{
        	id : ('id'),
        	parent: ('parent'),
        	text: ('text')
        },
        success : function(data) {
            createJSTree(data);
        },
        error : function(xhr, ajaxOptions, thrownError) {
            alert(xhr.status + ", " + thrownError);
        }
    });
});    

function resize() {
	// substract navbar height and spacing
	var h = Math.min($(window).height() - 95, $('.jstree-container-ul').height());
	$('#fileTree').height(h);
}

function createJSTree(jsonData) {
	
	$(window).resize(function () {
		resize();
	})
	
    $("#fileTree").jstree({
    	'plugins' : ['state','dnd','sort','types','unique'],
	    'core': {
	    	"animation" : 0,
	        "check_callback" : true,
	        "themes" : { "stripes" : true },
	        'data': jsonData
	    },
	    'sort' : function(a, b) {
			return this.get_type(a) === this.get_type(b) ? (this.get_text(a) > this.get_text(b) ? 1 : -1) : (this.get_type(a) >= this.get_type(b) ? 1 : -1);
		},
	    'types' : {
			'default' : { 'icon': contextURL + '/resources/fonts/fileIcon/folder-icon.png' },
		},
		'unique' : {
			'duplicate' : function (name, counter) {
				return name + ' ' + counter;
			}
		}
    }).on('open_node.jstree', function (e, data) { 
    	data.instance.set_icon(data.node, contextURL + '/resources/fonts/fileIcon/folder-open.png');
    	resize();
    })
    .on('close_node.jstree', function (e, data) { 
    	data.instance.set_icon(data.node, contextURL + '/resources/fonts/fileIcon/folder-icon.png');
    	resize();
    })
    .on('ready.jstree', function(e, data) {
    	var rootNode = $('#fileTree > ul > li.jstree-node:first-child');
    	if($('#fileTree').jstree(true).get_parent(rootNode) == '#') {
    	    // node is a root node
    		$('#fileTree').jstree("select_node", rootNode);
    		$('#fileTree').jstree("open_node", rootNode); // order matters: select after open might close the node again
    	}
    })
    .bind("select_node.jstree", function(e, data)  {
    	return data.instance.toggle_node(data.node);    
    });
}    

$('#fileTree').on("changed.jstree", function(e, data) {
	getFileList(data.selected);
});




function getFileList(selectedFolder) {
	var url = contextURL + "/components/fileBrowser/getFileList.jsp?dipId=" + dipId + "&directory=" + selectedFolder;
	$.ajax({
        async : true,
        type : "GET",
        url : url,
        dataType : "html",
        success : function(data) {
        	$("#fileList").html(data);
        	$(".licensed").on('click', function(event) {
        		showLicense(event, this);
        	});
        	$('[data-fancybox^="group"]').fancybox({
        		closeClickOutside	: true
        	});
        },
        error : function(xhr, ajaxOptions, thrownError) {
            alert(xhr.status + ", " + thrownError);
        }
    });
}


function showLicense(event, element) {
	var cookie = Cookies.get('IANUS-DIP.' + dipId + '.license');
	if(typeof(cookie) === 'undefined') {
		event.preventDefault();
		acceptLicense();
		$.fancybox.open({
			type: 'inline',
			src : '#downloadLicense',
			opts: {
				closeBtn			: true, 	// here is a bug: closeBtn: false seems not to work for inline content
				closeClickOutside	: true,
				beforeClose			: function () {		// another bug: afterClose callback never fires
					// redirect to download the file
					window.location = element.href;
				}
			}
		});
	}
}

function acceptLicense() {
	Cookies.set('IANUS-DIP.' + dipId + '.license', 'accepted', {expires: 7});
	$.fancybox.close();
}

// set client width to cookie for preview size selection
$(document).ready(function() {
	document.cookie = "clientWidth=" + $(window).width() + "; path=/";
});






