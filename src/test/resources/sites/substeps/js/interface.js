$(function() {
	var hash = window.location.hash,
		subtitle = $('.subtitle[id="' + hash.replace('#', '') + '"]');

	$('.subtitle').each(function () {
		$(this)
			.nextAll()
			.hide();
	}).on('click', function () {
		$(this)
			.toggleClass('open')
			.nextAll()
			.slideToggle();
	});

	subtitle
    	.toggleClass('open')
    	.nextAll()
    	.show();

	$('a[rel=external]').each(function () {
		if(this.href.indexOf(location.hostname) == -1) {
			$(this).attr({
				target: '_blank',
				title: 'External: ' + $(this).attr('title') + ' (opens in a new window)'
			});
		}
	});
});