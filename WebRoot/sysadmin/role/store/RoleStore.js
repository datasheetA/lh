Ext.define('App.sysadmin.role.store.RoleStore', {
			extend : 'Ext.data.Store',
			model : 'App.sysadmin.role.model.RoleModel',
			
			pageSize : defaultPageSize,
			remoteSort : true,
			autoLoad : false,
			autoDestroy : true,
			proxy : {
				type : 'ajax',
				url : appPath + '/sysadmin/role/getRoleListCtrl.do',
				extraParams : {},
				reader : {
					type : 'json',
					root : 'roleList',
					totalProperty : 'totalProperty'
				},
				simpleSortMode : true
			},
			sorters : [{
						property : 'role_name',
						direction : 'ASC'
					}],
			listeners : {}

		});