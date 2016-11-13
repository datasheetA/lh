Ext.Loader.setConfig({
			enabled : true
		});

Ext.application({
			name : 'App.sysadmin.frame',
			appFolder : appPath + '/sysadmin/frame',
			// autoCreateViewport : true,//如果为ture,则查找view目录下的Viewport.js文件
			controllers : ['FrameCtrl'],
			launch : function() {// 开始
				Ext.create('Ext.container.Viewport', {
							layout : 'border',
							items : [{
										xtype : 'northpanel'
									}, {
										xtype : 'westpanel'
									}, {
										xtype : 'centertabpanel'
									},{
										xtype : 'southpanel'
									}]
						});
			}
		});
