Ext.Loader.setConfig({
			enabled : true
		});
		
Ext.Loader.setPath('Ext.ux', appPath + extjsPath +'/examples/ux');

Ext.require(['Ext.ux.form.SearchField']);

Ext.application({
			name : 'App.sysadmin.role',
			appFolder : appPath + '/sysadmin/role',
			controllers : ['RoleCtrl'],
			launch : function() {
				Ext.create('Ext.container.Viewport', {
							layout : 'fit',
							items : [{
										xtype : 'rolelistgrid'
									}]
						});
			}
		});
