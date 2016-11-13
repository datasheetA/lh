Ext.Loader.setConfig({
			enabled : true
		});

Ext.Loader.setPath('Ext.ux', appPath + extjsPath + '/examples/ux');
Ext.Loader.setPath('App.ux', appPath + '/js/ux');

Ext.require(['Ext.ux.form.SearchField', 'App.ux.ComboTree']);

Ext.application({
			name : 'App.sysadmin.user',
			appFolder : appPath + '/sysadmin/user',
			controllers : ['UserCtrl'],
			launch : function() {
				Ext.create('Ext.container.Viewport', {
							layout : 'fit',
							items : [{
										xtype : 'userlistgrid'
									}]
						});
			}
		});
