// 菜单panel
Ext.define('App.sysadmin.menu.view.MenuPanel', {
			extend : 'Ext.tree.Panel',
			alias : 'widget.menupanel',

			title : '系统菜单',
			split : true,
			// collapsible : true,
			padding : '0 0 0 0',
			width : 300,
			region : 'west',

			initComponent : function() {
				this.store = Ext.create('App.sysadmin.menu.store.MenuStore');
				this.tools = [{
							type : 'refresh'
						}];
				this.dockedItems = [{
							dock : 'top',
							xtype : 'toolbar',
							layout : 'column',
							items : [{
										iconCls : 'arrow_out',
										text : '展开',
										action : 'expand'
									}, {
										iconCls : 'arrow_in',
										text : '折叠',
										action : 'collapse'
									}, {
										iconCls : 'arrow_refresh',
										text : '刷新',
										action : 'refresh'
									}, {
										text : '添加',
										iconCls : 'add',
										action : 'add'
									}, {
										iconCls : 'delete',
										text : '删除',
										action : 'delete'
									}]
						}];
				this.listeners = {};

				this.callParent();

				// this.on('afterrender', function(obj, eOpts) {
				// // obj.getRootNode().expand();
				// //obj.expandAll();
				// }, this);
			},
			getStore : function() {
				return this.store;
			}
		});