(function ($) {
	Tc.Module.Detail = Tc.Module.extend({

		url: '',
		tpl: null,
		jobName: '',
		$dialog: null,
		$dialogName: null,
		$dialogState: null,
		$paramContent: null,


		on: function (callback) {
			var self = this;
			if (self.$ctx.data('job-url')) {
				self.jobName = self.getUrlVars()['job'];
				self.url = self.$ctx.data('job-url');
				self.tpl = doT.template(self.$ctx.find('.tpl-job').text());
				self._loadJob(self);
			}
			else {
				callback();
			}

			self.$ctx.on('click', '.js-control-job', function (e) {

				var $this = $(this),
					url = $this.data('url');

				// ajax then reload, no errorhandling
				var jqxhr = $.ajax({
					url: url,
					type: 'post',
					timeout: 6000,
					success: function () {
						self._loadJob(self);
					}
				});
				return false;
			});

			self.$ctx.on('click', '.js-confirm', function (e) {

				var $this = $(this),
					url = $this.data('url'),
					jobName = $this.data('jobName'),
					modal_id = '.bs-modal-start-with-param',
					$modal = $(modal_id);

				self.$paramContent = $('#paramContent');

				$modal
					.data('jobName', jobName)
					.modal({show: true});

				// replace
				$modal.find('.js-name').text(jobName);

				//bind add of param
				$('.js-add-param', $modal).off('click').on('click', function (e) {
					self.$paramContent.append('<tr class="param-row">' +
						'<td><input type="text" name="name" /></td>' +
						'<td><select name="paramType">' +
						'<option value="">String</option>' +
						'<option value="(double)">Double</option>' +
						'<option value="(long)">Long</option>' +
						'<option value="(date)">Date (yyyy/MM/dd)</option>' +
						'</select></td>' +
						'<td><input type="text" name="value" /></td>' +
						'<td class="text-right">' +
						' <a class="btn btn-default danger btn-xs js-remove-param"><span class="glyphicon glyphicon-remove"></span> Remove</a>' +
						'</td>' +
						'</tr>'
					);
				});


				// bind start of batch job
				$('.js-execute', $modal).off('click').on('click', function (e) {
					var params = "";
					self.$paramContent.find('.param-row').each(function (k, value) {
						params += $('[name="name"]', value).val() + $('[name="paramType"]', value).val() + '=' + $('[name="value"]', value).val() + ',';
					});
					var urlWithParam = url + "?jobParameters=" + encodeURIComponent(params);
					var jqxhr = $.ajax({
						url: urlWithParam,
						type: 'post',
						timeout: 6000
					}).done(function () {
						$modal.modal('hide');
						window.location.reload();
					}).fail(function (e, textStatus) {
						console.log(textStatus);
						alert("failed to start batch job.");
					});

					return false;

				});

				return false;
			});

			self.$ctx.on('click', '.js-remove-param', function (e) {
				var $this = $(this);
				$(this).closest('tr').remove();
				return false;
			});

			self.$ctx.on('click', '.js-clean-param', function (e) {
				var $this = $(this);
				$('#paramContent').html("");
				return false;
			});

		},
		_loadJob: function (self) {
			$.getJSON(self.url + self.jobName, function (data) {
				self.$ctx.find('.job').html(self.tpl(data));
				setTimeout(function () {
					self._loadJob(self)
				}, 10000);
			});
		},
		getUrlVars: function () {
			var vars = [], hash;
			var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
			for (var i = 0; i < hashes.length; i++) {
				hash = hashes[i].split('=');
				vars.push(hash[0]);
				vars[hash[0]] = hash[1];
			}
			return vars;
		}

	});
})(Tc.$);