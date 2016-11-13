Ext.define('App.sysadmin.role.model.UserModel', {
			extend : 'Ext.data.Model',
			fields : [{
						name : 'user_id',
						mapping : 'user_id'
					}, {
						name : 'user_account',
						mapping : 'user_account'
					}, {
						name : 'user_name',
						mapping : 'user_name'
					}]
		});
