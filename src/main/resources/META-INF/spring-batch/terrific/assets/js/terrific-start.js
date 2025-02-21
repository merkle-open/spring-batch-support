if (!Array.prototype.filter) {
	Array.prototype.filter = function (fun /*, thisArg */) {
		"use strict";

		if (this === void 0 || this === null) {
			throw new TypeError();
		}

		var t = Object(this);
		var len = t.length >>> 0;
		if (typeof fun != "function") {
			throw new TypeError();
		}

		var res = [];
		var thisArg = arguments.length >= 2 ? arguments[1] : void 0;
		for (var i = 0; i < len; i++) {
			if (i in t) {
				var val = t[i];
				if (fun.call(thisArg, val, i, t)) {
					res.push(val);
				}
			}
		}

		return res;
	};
}

(function () {
	window.T = window.T || {};
	T.Utils = T.Utils || {};

	var _checkDomain = function (url) {
		if (url.indexOf('//') === 0) {
			url = location.protocol + url;
		}
		return url.toLowerCase().replace(/([a-z])?:\/\//, '$1').split('/')[0];
	};

	T.Utils.isExternal = function (url) {
		return ( ( url.indexOf(':') > -1 || url.indexOf('//') > -1 ) && _checkDomain(location.href) !== _checkDomain(url) );
	};

	T.Utils.getParam = function (name, url) {
		if (!url) {
			url = window.location.href;
		}
		name = name.replace(/[\[\]]/g, "\\$&");
		var regex = new RegExp("[#?&]" + name + "(=([^&#]*)|&|#|$)"),
			results = regex.exec(url);
		if (!results) {
			return null;
		}
		if (!results[2]) {
			return '';
		}
		return decodeURIComponent(results[2].replace(/\+/g, " "));
	};

})();

jQuery(document).ready(function ($) {
	var token = T.Utils.getParam("csrf"),
		header = T.Utils.getParam("csrf_header");

	if (token && header) {
		$(document).ajaxSend(function (e, xhr, settings) {
			if (!T.Utils.isExternal(settings.url)) {
				xhr.setRequestHeader(header, token);
			}
		});

		function csrfLinks() {
			$('a').each(function () {
				var $link = $(this);


				var url = $link.attr("href");

				if (url && url !== '#' && !T.Utils.isExternal(url)) {
					var existingToken = T.Utils.getParam("csrf", url),
						existingHeader = T.Utils.getParam("csrf_header", url);

					if (url.indexOf('#') <= 0) {
						url = url + "#";
					}
					if (!existingToken) {
						url = url + "&csrf=" + token;
					}
					if (!existingHeader) {
						url = url + "&csrf_header=" + header;
					}
					$link.attr('href', url);
				}
			});
		}

		$("body").bind("DOMNodeInserted", function () {
			csrfLinks();
		});

		csrfLinks();

	}
}(jQuery));

/** Initializer */
(function ($) {
	$(document).ready(function () {
		var start = function () {
			var $page = $('html');
			var application = new Tc.Application($page);
			application.registerModules();
			application.start();
		};

		var modulesToLoad = 0;
		var $modules = $('[data-module]');
		if ($modules.length > 0) {
			$modules.each(function () {
				modulesToLoad++;
				$module = $(this);
				var url = $module.data('module');
				$module.load(url, function () {
					$(this).contents().unwrap();
					modulesToLoad--;
					if (modulesToLoad <= 0) {
						start();
					}
				})
			});
		}
		else {
			start();
		}

	});
})(Tc.$);

