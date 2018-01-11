<!-- Fuer Nutzer ohne Javascript, um Fehlermeldungen zu vermeiden
(function ($) {
	$("#top-link").hide();
	$(function () {
		$(window).scroll(function () {
			if ($(this).scrollTop() > 100) {
				$('#top-link').fadeIn('slow');
			} else {
				$('#top-link').fadeOut('slow');
			}
		});
	});
})(jQuery);
-->