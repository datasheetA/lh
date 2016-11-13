// 编辑窗口
Ext.define('App.sysadmin.user.view.UserEditWin', {
			extend : 'Ext.window.Window',
			alias : 'widget.usereditwin',

			title : '用户编辑',
			layout : 'fit',
			width : 800,
			height : 475,
			closeAction : 'hide',
			modal:true,

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
							//autoScroll : true,
							layout : 'column',
							items : [{
										xtype : 'hidden',
										id : 'operFlag',
										name : 'operFlag'
									}, {
										xtype : 'hidden',
										id : 'userId',
										name : 'userId'
									}, {
										columnWidth : 1,
										height : 20,
										xtype : 'label',
										text : '请在下面输入用户信息：'
									}, {
										columnWidth : .5,
										vtype : 'useraccount',
										fieldLabel : '用户账号<font color=red>*</font>',
										id : 'userAccount',
										name : 'userAccount',
										allowBlank : false,
										blankText : '用户账号必须填写',
										minLength : 4,
										maxLength : 20
									}, {
										columnWidth : .5,
										fieldLabel : '用户名称',
										name : 'userName',
										maxLength : 20
									}, {
										columnWidth : .5,
										vtype : 'password',
										fieldLabel : '密码<font color=red>*</font>',
										id : 'passwd',
										name : 'passwd',
										inputType : 'password',
										allowBlank : false,
										blankText : '密码必须填写',
										minLength : 4,
										maxLength : 20
									}, {
										columnWidth : .5,
										vtype : 'password',
										fieldLabel : '密码确认<font color=red>*</font>',
										id : 'passwd1',
										name : 'passwd1',
										inputType : 'password',
										initialPassField : 'passwd',
										allowBlank : false,
										blankText : '密码确认必须填写',
										minLength : 4,
										maxLength : 20
									}, {
										columnWidth : .5,
										xtype : "datefield",
										fieldLabel : '出生日期',
										name : 'birthday',
										editable : false,
										format : 'Y-m-d'
									}, {
										columnWidth : .5,
										xtype : 'combo',
										id : 'sex',
										name : 'sex',
										store : Ext.create('App.common.store.EditSex'),
										fieldLabel : '性别',
										valueField : 'id',
										displayField : 'name',
										editable : false,
										forceSelection : true,
										triggerAction : 'all'
									}, {
										columnWidth : .5,
										xtype : 'combo',
										id : 'certType',
										name : 'certType',
										store : Ext.create('App.common.store.EditCertType'),
										fieldLabel : '证件类型',
										valueField : 'id',
										displayField : 'name',
										editable : false,
										forceSelection : true,
										triggerAction : 'all'
									}, {
										columnWidth : .5,
										fieldLabel : '证件号码',
										name : 'certNumber'
									}, {
										columnWidth : .25,
										xtype : 'combo',
										id : 'province',
										name : 'province',
										store : Ext.create('App.common.store.EditDistrict'),
										fieldLabel : '省',
										valueField : 'id',
										displayField : 'name',
										editable : false,
										forceSelection : true,
										triggerAction : 'all',
										listeners : {
											select : function(combo, record, index) {
												try {
													Ext.getCmp('city').clearValue();
													Ext.getCmp('county').clearValue();
													Ext.getCmp('district').clearValue();

													Ext.getCmp('city').getStore().removeAll();
													Ext.getCmp('county').getStore().removeAll();
													Ext.getCmp('district').getStore().removeAll();

													Ext.getCmp('city').getStore().load({
																params : {
																	"parentId" : this.value
																}
															});
												}
												catch (ex) {
													Ext.MessageBox.alert("错误", "数据加载失败。");
												}
											}
										}
									}, {
										columnWidth : .25,
										xtype : 'combo',
										id : 'city',
										name : 'city',
										store : Ext.create('App.common.store.EditDistrict'),
										fieldLabel : '市',
										valueField : 'id',
										displayField : 'name',
										labelWidth : 25,
										editable : false,
										forceSelection : true,
										queryMode : 'local',
										triggerAction : 'all',
										listeners : {
											select : function(combo, record, index) {
												try {
													Ext.getCmp('county').clearValue();
													Ext.getCmp('district').clearValue();

													Ext.getCmp('county').getStore().removeAll();
													Ext.getCmp('district').getStore().removeAll();

													Ext.getCmp('county').getStore().load({
																params : {
																	"parentId" : this.value
																}
															});
												}
												catch (ex) {
													Ext.MessageBox.alert("错误", "数据加载失败。");
												}
											}
										}
									}, {
										columnWidth : .25,
										xtype : 'combo',
										id : 'county',
										name : 'county',
										store : Ext.create('App.common.store.EditDistrict'),
										fieldLabel : '县',
										valueField : 'id',
										displayField : 'name',
										editable : false,
										forceSelection : true,
										queryMode : 'local',
										triggerAction : 'all',
										listeners : {
											select : function(combo, record, index) {
												try {
													Ext.getCmp('district').clearValue();

													Ext.getCmp('district').getStore().removeAll();

													Ext.getCmp('district').getStore().load({
																params : {
																	"parentId" : this.value
																}
															});
												}
												catch (ex) {
													Ext.MessageBox.alert("错误", "数据加载失败。");
												}
											}
										}
									}, {
										columnWidth : .25,
										xtype : 'combo',
										id : 'district',
										name : 'district',
										store : Ext.create('App.common.store.EditDistrict'),
										fieldLabel : '区',
										valueField : 'id',
										displayField : 'name',
										labelWidth : 25,
										editable : false,
										forceSelection : true,
										queryMode : 'local',
										triggerAction : 'all'
									}, {
										columnWidth : .5,
										fieldLabel : '地址',
										name : 'address',
										maxLength : 120
									}, {
										columnWidth : .5,
										xtype : 'numberfield',
										fieldLabel : '邮政编码',
										name : 'postalcode',
										allowDecimals : false,
										hideTrigger : true,
										keyNavEnabled : false,
										mouseWheelEnabled : false,
										minLength : 6,
										maxLength : 6
									}, {
										columnWidth : .5,
										xtype : 'combotree',
										id : 'birthplace',
										name : 'birthplace',
										rootId : 0,
										rootText : '全部地区',
										storeUrl : appPath + '/sysadmin/user/getBirthplaceCtrl.do',
										nodeParam : 'parentId',
										nodeList : 'zdList',
										showRoot : true,
										expandRoot : true,
										treeHeight : 200,
										allowSelectBranch : false,
										maxLength : 60,
										fieldLabel : '出生地'
									}, {
										columnWidth : .5,
										xtype : 'combotree',
										id : 'workArea',
										name : 'workArea',
										rootId : 0,
										rootText : '全部地区',
										storeUrl : appPath + '/sysadmin/user/getWorkAreaCtrl.do',
										nodeParam : 'parentId',
										nodeList : 'zdList',
										showRoot : true,
										expandRoot : false,
										treeHeight : 200,
										allowSelectBranch : false,
										fieldLabel : '工作地',										
										maxLength : 1000
									}, {
										columnWidth : .5,
										vtype : 'tel',
										fieldLabel : '电话',
										name : 'tel'
									}, {
										columnWidth : .5,
										vtype : 'mobile',
										fieldLabel : '手机号',
										name : 'mobile',
										maxLength : 11
									}, {
										columnWidth : .5,
										xtype : 'numberfield',
										fieldLabel : 'QQ',
										name : 'qq',
										allowDecimals : false,
										hideTrigger : true,
										keyNavEnabled : false,
										mouseWheelEnabled : false,
										maxLength : 15
									}, {
										columnWidth : .5,
										vtype : 'email',
										fieldLabel : 'Msn',
										name : 'msn'
									}, {
										columnWidth : .5,
										vtype : 'email',
										fieldLabel : 'Email',
										name : 'email'
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
										columnWidth : 1,
										xtype : 'filefield',
										fieldLabel : '头像',
										id : 'portrait',
										name : 'portrait',
										buttonText : '选择头像',
										maxLength : 200
									},{
										columnWidth : 1,
										xtype : 'combotree',
										id : 'roleIds',
										name : 'roleIds',
										rootId : 0,
										rootText : '全部角色',
										storeUrl : appPath + '/sysadmin/user/getUserRoleCtrl.do',
										nodeParam : 'parentId',
										nodeList : 'zdList',
										showRoot : true,
										expandRoot : false,
										treeHeight : 200,
										allowSelectBranch : false,
										fieldLabel : '角色',										
										maxLength : 1000
									},  {
										columnWidth : 1,
										xtype : 'textarea',
										fieldLabel : '简介',
										name : 'intro',
										maxLength : 200,
										height : 50
									}]
						});
				this.items = [this.form];
				this.buttons = [{
							text : '保存',
							action : 'btnSave',
							scope : this
						}, {
							text : '关闭',
							scope : this,
							handler : function(btn) {
								this.hide();
							}
						}];
				this.listeners = {}
				this.callParent();
			}

		});