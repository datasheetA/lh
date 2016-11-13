Ext.define('App.sysadmin.role.view.DDRoleGrid', {
			extend : 'Ext.Panel',
			alias : 'widget.ddrolegrid',
			layout : {
				type : 'hbox',
				align : 'stretch'
			},
			defaults : {
				flex : 1
			},
			frame : true,
			border : false,
			initComponent : function() {
				this.items = [{
							xtype : 'grid',
							id : 'gridUsableUser',
							store : Ext.create('App.sysadmin.role.store.UsableRoleUserStore', {
										autoLoad : false
									}),
							title : '可选用户',
							// selModel : Ext.create('Ext.selection.CheckboxModel', {
							// checkOnly : true
							// }),
							columns : [{
										header : '用户编号',
										dataIndex : 'user_id',
										sortable : false,
										hidden : true
									}, {
										header : '用户账号',
										dataIndex : 'user_account',
										sortable : false,
										flex : true
									}],
							multiSelect : true,
							viewConfig : {
								plugins : {
									ptype : 'gridviewdragdrop',
									dragText : '选择用户',
									dragGroup : 'firstGridDDGroup',
									dropGroup : 'secondGridDDGroup'
								}
							},
							listeners : {}
						}, {
							xtype : 'container',
							layout : 'vbox',
							padding : '75 10 0 10',
							border : false,
							maxWidth : 50,
							items : [{
										xtype : 'button',
										text : '>>',
										action : 'btnAddSelected'
									}, {
										height : 10,
										xtype : 'label',
										text : ''
									}, {
										xtype : 'button',
										text : '<<',
										action : 'btnRemoveSelected'
									}]
						}, {
							xtype : 'grid',
							id : 'gridSelectedUser',
							store : Ext.create('App.sysadmin.role.store.SelectedRoleUserStore', {
										autoLoad : false
									}),
							title : '已选用户',
							// selModel : Ext.create('Ext.selection.CheckboxModel', {
							// checkOnly : true
							// }),
							columns : [{
										header : '用户编号',
										dataIndex : 'user_id',
										sortable : false,
										hidden : true
									}, {
										header : '用户账号',
										dataIndex : 'user_account',
										sortable : false,
										flex : true
									}],
							multiSelect : true,
							viewConfig : {
								plugins : {
									ptype : 'gridviewdragdrop',
									dragText : '删除用户',
									dragGroup : 'secondGridDDGroup',
									dropGroup : 'firstGridDDGroup'
								}
							}
						}];

				this.listeners = {}
				this.callParent();
			}
		});