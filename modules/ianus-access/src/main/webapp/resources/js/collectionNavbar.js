

///////////////////////////////////////////////////////////////
// tabbing - ajax loading, opening by hash-URL, smoothscroll //
///////////////////////////////////////////////////////////////

function toggleTab(tab) {
	$('a[href="#collectionOverview"]').parent().removeClass("active");
	$('a[href="#collectionMetadata"]').parent().removeClass("active");
	$('a[href="#collectionFiles"]').parent().removeClass("active");
	$('a[href="#' + tab + '"]').parent().addClass("active");
}


function gup(name) {
    var url = location.href;
    name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
    var regexS = "[\\?&]"+name+"=([^&#]*)";
    var regex = new RegExp( regexS );
    var results = regex.exec( url );
    return results == null ? null : results[1];
}


/**
 * This method is attached to the tabs. 
 * But it is also invoked on pageload, thus it has to handle other 
 * and secondary hash-strings safely.
 */
function loadTab(safeHash) {
	var primary, secondary;
	primary = safeHash.primary;
	secondary = safeHash.secondary;
	
	var wfipId = gup("dipId");
	var paramName = "dipId";
	if(wfipId == null) {
		wfipId = gup("sipId");
		paramName = "sipId";
	}
	
	// don't reload the current tab
	if(location.hash == primary) return false;
	
	toggleTab(primary)
	
	$.ajax(
		"../components/" + primary + ".jsp?" + paramName + "=" + wfipId, {
			type: 'GET',
            contentType: "html",
	        success: function(data, textStatus, jqXHR){
	        	$("#collectionView").html(data);
	        	showUsagePolicy(primary);
	        	addScrollToHandlers();
	        	if(secondary != null) scrollTo(secondary);
	        },
	        error: function(jqXHR, textStatus, errorThrown){
	        	alert('Error: Page not found.');
	        }
        });
	return true;
}


function addScrollToHandlers() {
	$('#collectionView a[href*="#"]:not(a[href="#"], #pagenav a)').on("click", function(event) {
		// only attach this behavior to internal links
		if(	!this.getAttribute("href").startsWith("http")
		||	this.getAttribute().indexOf(window.location.hostname) != -1
		) {
			
			var safeHash = getSafeHash(this.getAttribute("href"));
			location.hash = safeHash.primary;
			event.preventDefault();
			if(!loadTab(safeHash)) {
				scrollTo(safeHash.secondary);
			}
			return false;
		}
	});
}


function scrollTo(identifier) {
	if(typeof identifier != 'undefined' && identifier != null && identifier != "")
		$('html,body').animate({
			scrollTop: $("#" + identifier).offset().top - ($("#pagenavtabs").height() + 10)
		}, 1000);
}


function getSafeHash(href) {
	var hash = location.hash;
	if(typeof href != 'undefined' && href != "" && href.indexOf("#") != -1)
		hash = href;
	
	if(typeof hash == 'undefined' || hash == "")
		hash = "#collectionOverview";
	//if(location.href.indexOf("collectionFiles.jsp") != -1)
		//hash = "#collectionFiles";
	
	var primary = null;
	var secondary = null;
	
	if(hash.indexOf("#") != -1) {
		var split = hash.split("#");
		if(typeof split[1] != 'undefined' && split[1] != "")
			primary = split[1];
		if(typeof split[2] != 'undefined' && split[2] != "")
			secondary = split[2];
	}
	
	if(!primary in {
	"collectionOverview":"",
	"collectionMetadata":"",
	"collectionFiles":""}) {
		secondary = primary;
		primary = "collectionOverview";
	}
	
	return {"primary": primary, "secondary": secondary};
}


function showUsagePolicy(primary) {
	if(primary != "collectionFiles") return;
	
	var cookie = Cookies.get('IANUS-DIP.' + dipId + '.usagePolicy');
	if(typeof(cookie) === 'undefined') {
		console.log();
		$.fancybox.open({
			type   : 'inline',
			src    : '#usageAgreement',
			opts : {
				showCloseButton: false,
				modal: true,	// disable navigation & closing by escape key or click outside
				afterLoad: function() {
					// apply some special styling
					var content = $('.fancybox-content');
					var height = content.height() - 85;
					var width = content.width() - 30;
					$('.fancybox-content').css({
						background: '#f9f9f9 none repeat scroll 0 0',
					    borderRadius: 4,
					    padding: 15,
					    color: '#444',
					    position: 'relative'
					});
					$('.fancybox-content object').css({
						height: height,
						width: width,
						position: 'absolute',
						zIndex: 50,
						bottom:15,
						borderBottom: '3px solid #444',
						borderRight: '2px solid #444',
						borderRadius: 4
					});
					$('.fancybox-close-small').css({
						display: 'none'
					});
				}
			}
		});
	}
}

function acceptUsagePolicy() {
	Cookies.set('IANUS-DIP.' + dipId + '.usagePolicy', 'accepted', {expires: 7});
	$.fancybox.close();
}






$(document).ready(function () {
	loadTab(getSafeHash());
    
	$("#pagenav a:not(.direct)").on('click', function(event) {
		event.preventDefault();
		location.hash = this.getAttribute("href").split("#")[1];
		loadTab(getSafeHash());
		return false;
	});
	
	addScrollToHandlers();
});




/////////////////////////////////////////////////////////////
// stacking the collection-navigation bar on resize events //
/////////////////////////////////////////////////////////////

// requires the responsive bootstrap toolkit:
(function($, viewport){
	var resizeCallback = function() {
		if(window.currentViewport == 'xs' && viewport.is('>=sm')) {
			$('#pagenavtabs').removeClass('nav-stacked',0);
		}
		else if(viewport.is('xs') && currentViewport != 'xs') {
			$('#pagenavtabs').addClass('nav-stacked',0);
		}
	}
	
	$(document).ready(function() {
		window.currentViewport = viewport.current();
	});
	$(window).resize(
		viewport.changed(function(){
			resizeCallback();
			window.currentViewport = viewport.current();
		})
	);
})($, ResponsiveBootstrapToolkit);

//Stack menu when collapsed
$('#pagenav').on('shown.bs.collapse', function() {
	$('#pagenavtabs').css('display', 'none');
	$('#pagenavtabs').addClass('nav-stacked');
	$('#pagenavtabs').slideDown(300);
});
//Unstack menu when not collapsed
$('#pagenav').on('hidden.bs.collapse', function() {
	$('#pagenavtabs').removeClass('nav-stacked');
	$('#pagenavtabs').attr('style', '');
});




//////////////////////////////////////////////////////////
// switching the menubar from scrolling to fixed & back //
//////////////////////////////////////////////////////////

(function( menubar, $, undefined ) {

	// Implementation of parallax scrolling by Andy Shora - http://andyshora.com/parallax.html
	// the menu is initialized inside the onload element, due to rendering order
	menubar.parallaxElements = [];
	menubar.windowHeight = 0;
	// set an initial value, we're going for the real thing in domready:
	menubar.headerHeight = 226;
	
	menubar.parallax = function(scrollTop) {
		//var toolbarMargin = 0;
		//if(jQuery('body').hasClass('toolbar')) toolbarMargin = 30;
		for (var id in menubar.parallaxElements) {
			// distance of element from top of viewport
			if(scrollTop >= menubar.headerHeight) {
				// element is now active, fix the position so when we scroll it stays fixed
				var speedMultiplier = menubar.parallaxElements[id].speed || 1;
				var pos = (menubar.windowHeight - menubar.headerHeight);
				jQuery(menubar.parallaxElements[id].elm).css({
					position: 'fixed',
					left: '8px'
				});
			}else if (scrollTop < menubar.headerHeight) {
				// scrolled up back past the start value (headerHeight), reset position
				jQuery(menubar.parallaxElements[id].elm).css({
					position: 'absolute',
					left: '0'
				});
			}
		}
	}
	
	
}( window.menubar = window.menubar || {}, jQuery ));




$(document).ready(function() {
	// the menu javascripts
	// Implementation of parallax scrolling by Andy Shora - http://andyshora.com/parallax.html
	menubar.windowHeight = $(window).height();
	$('html,body').scrollTop(1); // auto scroll to top
	// touch event check stolen from Modernizr
	menubar.touchSupported = (('ontouchstart' in window) || window.DocumentTouch && document instanceof DocumentTouch);
	// if touch events are supported, tie our animation to the position to these events as well
	if(menubar.touchSupported) {
		$(window).bind('touchmove', function(e) {
			var val = e.currentTarget.scrollY;
			menubar.parallax(val);
		});
	}
	$(window).bind('scroll', function(e) {
		var val = $(this).scrollTop();
		menubar.parallax(val);
	});
	// update vars used in parallax calculations on window resize
	$(window).resize(function() {
		menubar.windowHeight = $(this).height();
		menubar.headerHeight = getHeaderHeight();
		for (var id in menubar.parallaxElements) {
			menubar.parallaxElements[id].initialOffsetY = $(menubar.parallaxElements[id].elm).offset().top;
			menubar.parallaxElements[id].height = $(menubar.parallaxElements[id].elm).height();
		}
	});
	menubar.headerHeight = getHeaderHeight();	// add the ribbon height
	// get parallax elements straight away as they wont change
	// this will minimise DOM interactions on scroll events
	$('.parallax').each(function(){
		var $elm = $(this);
		var id = $elm.data('id');
		// use data-id as key
		menubar.parallaxElements[id] = {
			id: $elm.data('id'),
			start: $elm.data('start'),
			//stop: $elm.data('stop'),	// the stop property is not used by the menu bar
			speed: $elm.data('speed'),
			elm: $elm[0],
			initialOffsetY: $elm.offset().top,
			height: $elm.height(),
			width: $elm.outerWidth()
		};
	});
});


function getHeaderHeight() {
	return $('#header').outerHeight() + $('#account').outerHeight(); 
}



