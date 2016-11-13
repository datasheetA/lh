// 编辑窗口
Ext.define('App.sysadmin.menu.view.EditPanel', {
			extend : 'Ext.panel.Panel',
			alias : 'widget.editpanel',

			layout : 'fit',
			border : 0,
			region : 'center',

			initComponent : function() {
				this.form = Ext.create('Ext.form.Panel', {
							bodyPadding : '5 5 5 5',
							defaults : {
								xtype : 'textfield',
								labelWidth : 65,
								labelAlign : 'right',
								margin: 2,
								maxLength : 30,
								allowBlank : true
							},
							frame : true,
							border : false,
							layout : 'column',
							items : [{
										xtype : 'hidden',
										id : 'operFlag',
										name : 'operFlag'
									}, {
										xtype : 'hidden',
										id : 'menuId',
										name : 'menuId'
									}, {
										columnWidth : 1,
										height : 20,
										xtype : 'label',
										text : '菜单信息：'
									}, {
										columnWidth : .5,
										fieldLabel : '菜单名称<font color=red>*</font>',
										id : 'menuName',
										name : 'menuName',
										allowBlank : false,
										blankText : '菜单名称必须填写',
										minLength : 2,
										maxLength : 100
									}, {
										columnWidth : .5,
										xtype : 'combotree',
										id : 'parentId',
										name : 'parentId',
										rootId : 0,
										rootText : '菜单列表',
										storeUrl : appPath + '/sysadmin/menu/getMenuTreeCtrl.do',
										nodeParam : 'menuId',
										nodeList : 'menuList',
										treeHeight : 300,
										allowSelectedRoot : true,
										allowSelectBranch : true,
										expandRoot : false,
										maxLength : 1000,
										fieldLabel : '上级菜单<font color=red>*</font>',
										allowBlank : false,
										validateOnChange : false,
										validateOnBlur : false,
										blankText : '上级菜单必须填写',
										listeners : {
											'afterrender' : function(obj, eOpts) {
												obj.getTreePanel().expandAll();
											}
										}
									}, {
										columnWidth : 1,
										fieldLabel : '菜单URL',
										name : 'indexUrl',
										maxLength : 120
									}, {
										columnWidth : .5,
										xtype : 'combo',
										id : 'status',
										name : 'status',
										store : Ext.create('App.common.store.EditStatus'),
										fieldLabel : '状态<font color=red>*</font>',
										valueField : 'id',
										displayField : 'name',
										editable : false,
										allowBlank : false,
										forceSelection : true,
										triggerAction : 'all'
									}, {
										columnWidth : .5,
										fieldLabel : '序号',
										name : 'siteNo',
										maxLength : 20
									}, {
										columnWidth : 1,
										xtype : 'textarea',
										fieldLabel : '菜单所用到的URL(多个URL以回车键分割)',
										name : 'otherUrl',
										maxLength : 2000,
										height : 150
									}, {
										columnWidth : 1,
										xtype : 'textarea',
										fieldLabel : '备注',
										name : 'remark',
										maxLength : 120,
										height : 50
									}]
						});
				this.items = [this.form];
				this.buttons = [{
							text : '保存',
							action : 'btnSave',
							scope : this
						}, {
							text : '清空',
							action : 'btnClear',
							scope : this
						}, {
							text : '删除',
							action : 'btnDelete',
							scope : this
						}];
				this.callParent();
			}

		});