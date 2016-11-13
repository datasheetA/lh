Ext.define('App.sysadmin.user.store.UserStore', {
			extend : 'Ext.data.Store',
			model : 'App.sysadmin.user.model.UserModel',
			
			pageSize : defaultPageSize,
			remoteSort : true,
			autoLoad : false,
			autoDestroy : true,
			proxy : {
				type : 'ajax',
				url : appPath + '/sysadmin/user/getUserListCtrl.do',
				extraParams : {},
				reader : {
					type : 'json',
					root : 'userList',
					totalProperty : 'totalProperty'
				},
				simpleSortMode : true
			},
			sorters : [{
						property : 'user_account',
						direction : 'ASC'
					}],
			listeners : {}

		});