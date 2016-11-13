Ext.define('App.sysadmin.menu.store.MenuStore', {
			extend : 'Ext.data.TreeStore',
			model : 'App.sysadmin.menu.model.MenuModel',
			nodeParam : 'menuId',
			root : {
				text : '菜单列表',
				id : '0',
				expanded : true
			},
			autoLoad : false,
			proxy : {
				type : 'ajax',
				url : appPath + '/sysadmin/menu/getMenuTreeCtrl.do',
				extraParams : {
					'menuId' : '0'
				},
				reader : {
					type : 'json',
					root : 'menuList'
				}
			},
			listeners : {
				load : function(ds, nd, eOpts) {
					nd.expandChildren();
				}
			}
		});