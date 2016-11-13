Ext.define('App.ux.PagingToolbarWithPagesize', {
			extend : 'Object',
			pageSize : defaultPageSize,
			constructor : function(config) {
				if (config) {
					Ext.apply(this, config);
				}
			},
			init : function(pbar) {
				var self = this;
				var nowArray = [[10, 10], [20, 20], [30, 30], [50, 50], [100, 100], [200, 200]];
				for (i = 0; i < nowArray.length; i++) {
					if (self.pageSize < nowArray[i][0]) {
						nowArray.push([self.pageSize, self.pageSize]);
						break;
					}
					if (self.pageSize == nowArray[i][0]) {
						break;
					}
					if (i == nowArray.length - 1) {
						nowArray.push([self.pageSize, self.pageSize]);
					}
				}
				var cobm = new Ext.form.ComboBox({
							width : 50,
							height : 18,
							store : new Ext.data.SimpleStore({
										fields : ["key", "value"],
										data : nowArray.sort(function(o1, o2) {
													return o1[0] - o2[0];
												})
									}),
							mode : 'local',
							value : self.pageSize,
							valueField : "key",
							displayField : "value",
							forceSelection : true,
							editable : false,
							triggerAction : 'all',
							loadPages : function() {
								pbar.store.pageSize = this.getValue();
								pbar.store.loadPage(1);
							},
							listeners : {
								'select' : function() {
									this.loadPages();
								}
							}
						});

                var addCombSite = pbar.items.indexOf(pbar.child("#refresh")) + 1;
				pbar.insert(addCombSite, cobm);
				pbar.insert(addCombSite, '每页显示条数');
				pbar.insert(addCombSite, '-');
			}

		});
