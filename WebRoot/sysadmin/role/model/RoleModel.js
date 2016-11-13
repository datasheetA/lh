Ext.define('App.sysadmin.role.model.RoleModel', {
			extend : 'Ext.data.Model',
			fields : [{
						name : 'role_id',
						mapping : 'roleId'
					}, {
						name : 'role_name',
						mapping : 'roleName'
					}, {
						name : 'status'
					}, {
						name : 'remark'
					}]
		});