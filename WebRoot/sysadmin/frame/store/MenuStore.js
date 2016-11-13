Ext.define('App.sysadmin.frame.store.MenuStore', {
			extend : 'Ext.data.TreeStore',
			model : 'App.sysadmin.frame.model.MenuModel',// 不能省略包路径
			nodeParam : 'menuId',
			root : {
				text : '菜单列表',
				id : '0'

			},

			proxy : {
				type : 'ajax',
				url : appPath + '/sysadmin/frame/getFrameMenuCtrl.do',
				// actionMethods : {create: "POST", read: "GET", update: "POST",destroy: "POST"},
				extraParams : {
					'menuId' : '0'
				},
				reader : {
					type : 'json',
					root : 'menuList'
				}
			}
			// listeners : {
			// beforeload : function(ds, opration, opt) {
			// opration.params.menuId = opration.node.data.id;
			// }
			// },
//			sorters : [{
//						property : 'siteNo',
//						direction : 'ASC'
//					}]
		});
