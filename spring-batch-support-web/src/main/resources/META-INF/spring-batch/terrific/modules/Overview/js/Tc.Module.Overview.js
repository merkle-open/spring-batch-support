(function ($) {
	Tc.Module.Overview = Tc.Module.extend({

		url: '',
		tpl: null,
		$dialog: null,
		$dialogName: null,
		$dialogState: null,

		on: function (callback) {
			var self = this;
			if (self.$ctx.data('jobs-url')) {
				self.url = self.$ctx.data('jobs-url');
				self.tpl = doT.template(self.$ctx.find('.tpl-jobs').text());
				self._loadJobs(self);
			}
			else {
				callback();
			}

			self.$dialog = $('.bs-modal-detail', self.$ctx);
			self.$dialogName = $('.js-detail-name', self.$ctx);
			self.$dialogState = $('.js-detail-state', self.$ctx);

			self.$ctx.on('click', '.js-control-job', function (e) {

				var $this = $(this),
					url = $this.data('url');

				// ajax then reload, no errorhandling
				var jqxhr = $.ajax({
					url: url,
					type: 'post',
					timeout: 6000,
					success: function () {
						self._loadJobs(self);
					}
				});
				return false;
			});

			self.$ctx.on('click', '.js-status', function (e) {
				var $this = $(this),
					name = $this.data('name'),
					state = $this.data('state');

				$this.$dialog = $('.bs-modal-detail', $this.$ctx);
				$this.$dialogName = $('.js-detail-name', $this.$dialog);
				$this.$dialogState = $('.js-detail-state', $this.$dialog);

				$this.$dialog.modal('show');
				$this.$dialogName.text(name);
				$this.$dialogState.text(state);
			});

		},

		_loadJobs: function (self) {
			$.getJSON(self.url, function (data) {
				self.$ctx.find('.jobs').html(self.tpl(data));
				setTimeout(function () {
					self._loadJobs(self)
				}, 10000);
			});
		}

	});
})(Tc.$);