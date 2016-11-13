Ext.define('App.sysadmin.role.store.MenuStore', {
			extend : 'Ext.data.TreeStore',
			model : 'App.sysadmin.role.model.MenuModel',
			nodeParam : 'menuId',
			autoLoad : false,
			root : {
				text : '菜单列表',
				id : '0',
				expanded : false
			},
			proxy : {
				type : 'ajax',
				url : appPath + '/sysadmin/role/getRoleAuthTreeCtrl.do',
				extraParams : {
					'menuId' : '0'
				},
				reader : {
					type : 'json',
					root : 'menuList'
				}
			}
		});