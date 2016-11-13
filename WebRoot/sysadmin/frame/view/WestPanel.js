// 页面左边panel
Ext.define('App.sysadmin.frame.view.WestPanel', {
			extend : 'Ext.tree.Panel',
			alias : 'widget.westpanel',

			title : '系统管理菜单',
			split : true,
			collapsible : true,
			padding : '2 0 2 0',
			width : 200,
			region : 'west',

			initComponent : function() {
				this.store = Ext.create('App.sysadmin.frame.store.MenuStore',{
					
					autoLoad : true
				});
				this.dockedItems = [{
							dock : 'top',
							xtype : 'toolbar',
							layout : 'column',
							items : [{
										iconCls : 'arrow_out',
										text : '展开',
										handler : function(btn) {
											btn.up('treepanel').collapseAll();
											btn.up('treepanel').expandAll();
										}
									}, {
										iconCls : 'arrow_in',
										text : '折叠',
										handler : function(btn) {
											btn.up('treepanel').collapseAll();
										}
									}, {
										iconCls : 'arrow_refresh',
										text : '刷新',
										handler : function(btn) {
											var tpanel = btn.up('treepanel');
											var delNode;
											while (delNode = tpanel.getRootNode().childNodes[0]) {
												tpanel.getRootNode().removeChild(delNode);
											}
											tpanel.getStore().load({
														scope : this,
														callback : function(records, operation, success) {
															tpanel.getRootNode().expand();
														}
													});
										}
									}]
						}];
				this.listeners = {};

				this.callParent();

//				this.on('afterrender', function(obj, eOpts) {
//							//obj.getRootNode().expand();
//							//obj.expandAll();//点击某个节点，将展开所有节点
//						}, this);
				// this.on('itemmousedown', function(selModel, record) {
				// this.loadMenu(selModel, record);
				// }, this);
				// this.on('itemclick', function(view, rec, node) {
				// view.expand(rec);
				// }, this);
			}
		});