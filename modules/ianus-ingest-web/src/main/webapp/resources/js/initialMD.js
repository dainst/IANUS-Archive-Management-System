/**
 * Function on() allows to attach handlers to descendant elements that are created later on 
 * after an ajax request.
 * Thus, the handlers are being "delegated" from #page as the updated form's parent element.
 */
/*
 * We don't need that, native jQuery code conflicts with the PF behaviors
$(document).ready(function() {
	$('#page').on('click', '.editAttribute', function() {
		// toggle the fieldset contained by the 3rd parent table
		var parents = $(this).parents('table');
		$(parents[2]).find('fieldset').toggle();
	});
});
*/


/**
 * We need to define an expander method, as PF toggle() would close 
 * the panel on a second click. We do not want that...
 */
function fieldsetExpand(widget) {
	if(widget.stateHolder.attr('value') === 'true') widget.toggle();
}

/**
 * Same for the collapse method...
 */
function fieldsetCollapse(widget) {
	if(widget.stateHolder.attr('value') === 'false') widget.toggle();
}



