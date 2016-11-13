// 列表
Ext.define('App.sysadmin.user.view.UserListGrid', {
			extend : 'Ext.grid.Panel',
			alias : 'widget.userlistgrid',

			border : false,
			selModel : Ext.create('Ext.selection.CheckboxModel', {
						checkOnly : true
					}),
			viewConfig : {
				stripeRows : true
			},

			initComponent : function() {
				this.store = Ext.create('App.sysadmin.user.store.UserStore', {
							autoLoad : true
						});

				this.columns = [{
							header : '用户编号',
							dataIndex : 'user_id',
							hidden : true
						}, {
							header : '用户账号',
							dataIndex : 'user_account',
							align : 'center',
							width : 150
						}, {
							header : '用户名称',
							dataIndex : 'user_name',
							align : 'center',
							width : 150
						}, {
							header : '注册时间',
							dataIndex : 'register_time',
							renderer : rendererDateTime,
							align : 'center',
							width : 150
						}, {
							header : '电话号码',
							dataIndex : 'tel',
							align : 'center',
							width : 150
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
							id : 'searchFieldUserAccount',
							name : 'searchFieldUserAccount',
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
				this.listeners = {

				}
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
			}
		});