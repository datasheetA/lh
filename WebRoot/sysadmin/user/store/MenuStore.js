Ext.define('App.sysadmin.user.store.MenuStore', {
			extend : 'Ext.data.TreeStore',

			constructor : function(cfg) {
				var me = this;
				cfg = cfg || {};
				me.callParent([Ext.apply({
							model : 'App.sysadmin.user.model.MenuModel',
							nodeParam : 'menuId',
							autoLoad : false,
							root : {
								text : '菜单列表',
								id : '0',
								expanded : false
							},
							proxy : {
								type : 'ajax',
								url : cfg.storeUrl,
								extraParams : {
									'menuId' : '0'
								},
								reader : {
									type : 'json',
									root : 'menuList'
								}
							}
						}, cfg)]);
			}
		});