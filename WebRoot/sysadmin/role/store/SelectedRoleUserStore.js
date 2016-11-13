Ext.define('App.sysadmin.role.store.SelectedRoleUserStore', {
			extend : 'Ext.data.Store',
			model : 'App.sysadmin.role.model.UserModel',
			
			pageSize : defaultPageSize,
			remoteSort : true,
			autoLoad : false,
			autoDestroy : true,
			proxy : {
				type : 'ajax',
				url : appPath + '/sysadmin/role/getSelectedUserByRoleIdCtrl.do',
				extraParams : {},
				reader : {
					type : 'json',
					root : 'userList'
				},
				simpleSortMode : true
			},
			sorters : [{
						property : 'user_account',
						direction : 'ASC'
					}],
			listeners : {}

		});