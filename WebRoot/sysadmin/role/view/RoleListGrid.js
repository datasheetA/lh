// 列表
Ext.define('App.sysadmin.role.view.RoleListGrid', {
			extend : 'Ext.grid.Panel',
			alias : 'widget.rolelistgrid',

			border : false,
			selModel : Ext.create('Ext.selection.CheckboxModel', {
						checkOnly : true
					}),
			viewConfig : {
				stripeRows : true
			},

			initComponent : function() {
				// this.store = 'RoleStore';
				this.store = Ext.create('App.sysadmin.role.store.RoleStore', {
							autoLoad : true
						});

				this.columns = [{
							header : '角色编号',
							dataIndex : 'role_id',
							hidden : true
						}, {
							header : '角色名称',
							dataIndex : 'role_name',
							align : 'center',
							width : 150
						}, {
							header : '状态',
							dataIndex : 'status',
							renderer : this.renderStatus,
							align : 'center',
							width : 150
						}, {
							header : '备注',
							dataIndex : 'remark',
							align : 'center',
							width : 300
						}, {
							header : '操作',
							id : 'oper',
							align : 'center',
							sortable : false,
							columns : [{
										id : 'see',
										align : 'center',
										width : 36,
										style : {
											display : 'none'
										},
										renderer : this.renderOperateSee,
										sortable : false
									}, {
										id : 'update',
										align : 'center',
										width : 36,
										style : {
											display : 'none'
										},
										renderer : this.renderOperateUpdate,
										sortable : false
									}, {
										id : 'delete',
										align : 'center',
										width : 36,
										style : {
											display : 'none'
										},
										renderer : this.renderOperateDelete,
										sortable : false
									}, {
										id : 'grant',
										align : 'center',
										width : 36,
										style : {
											display : 'none'
										},
										renderer : this.renderOperateGrant,
										sortable : false
									}]

						}];
				this.tbar = [{
							xtype : 'button',
							text : '添加',
							iconCls : 'add',
							action : 'btnAdd'
						}, '-', {
							xtype : 'button',
							text : '修改',
							iconCls : 'edit',
							action : 'btnUpdate'
						}, '-', {
							xtype : 'button',
							text : '删除',
							iconCls : 'delete',
							action : 'btnDelete'
						}, '-', {
							xtype : 'button',
							text : '查看',
							iconCls : 'see',
							action : 'btnSee'
						}, '-', {
							xtype : 'button',
							text : '查询',
							iconCls : 'find',
							action : 'btnQuery'
						}, '->', {
							xtype : 'searchfield',
							id : 'searchFieldRoleName',
							name : 'searchFieldRoleName',
							fieldLabel : '搜索',
							labelWidth : 38,
							labelAlign : 'right',
							store : this.store,
							width : 180
						}];
				this.bbar = Ext.create('Ext.PagingToolbar', {
							id : 'pagingToolBar',
							store : this.store,
							displayInfo : true,
							displayMsg : '显示 {0} - {1} 条，共计 {2} 条',
							emptyMsg : "没有数据"
						});
				this.listeners = {}
				this.callParent();
			},
			renderOperateSee : function(value, metaData, record) {
				return '<a href="#" class="aoper">查看</a>';
			},
			renderOperateUpdate : function(value, metaData, record) {
				return '<a href="#" class="aoper">修改</a>';
			},
			renderOperateDelete : function(value, metaData, record) {
				return '<a href="#" class="aoper">删除</a>';
			},
			renderOperateGrant : function(value, metaData, record) {
				return '<a href="#" class="aoper">授权</a>';
			},
			renderStatus : function(value, metaData, record) {
				return (value == 1) ? "有效" : "冻结";
			}
		});