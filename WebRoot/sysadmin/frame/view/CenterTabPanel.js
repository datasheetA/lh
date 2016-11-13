// 页面中间tabpanel
Ext.define('App.sysadmin.frame.view.CenterTabPanel', {
			extend : 'Ext.tab.Panel',
			alias : 'widget.centertabpanel',

			id : 'centerTabPanel',
			region : 'center',
			defaults : {
				autoScroll : true,
				bodyPadding : 0
			},
			padding : '2 0 2 0',
			activeTab : 0,

			initComponent : function() {
				
				this.items = [{
							id : 'homepage',
							title : '欢迎页',
							 iconCls : 'home',
							xtype : 'container',
							layout : 'fit',
							loader : {
								url : appPath + '/sysadmin/welcome.html',
								contentType : 'html',
								autoLoad : true,
								scripts : true
							}
						}];
				

				
				this.callParent();
			}
		});
