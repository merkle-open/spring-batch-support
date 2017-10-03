(function ($) {
	// replace elements html: each ##key## by { key: value}, then return result
	$.fn.templateReplace = function (names) {
		var temp = $(this).html(),
			key;

		for (key in names) {
			temp = temp.replace(new RegExp('##' + key + '##', 'g'), names[key]);
		}
//console.log(names);
		return temp;
	};
})(Tc.$);