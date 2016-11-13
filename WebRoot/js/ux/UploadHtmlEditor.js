Ext.define('App.ux.UploadHtmlEditor', {
			extend : 'Ext.form.field.HtmlEditor',
			alias : 'widget.uploadhtmleditor',
			fontFamilies : ['宋体', '黑体', '隶书', 'Arial', 'Courier New', 'Tahoma', 'Times New Roman', 'Verdana'],
			defaultFont : '宋体',
			minWidth : 500,
			minHight : 300,
			curRange : null,
			curSelection : null,
			initComponent : function() {
				this.listeners = {};

				this.callParent(arguments);

				this.on({
							'afterrender' : function() {
								this.getToolbar().add(16, {
											itemId : 'htmlEditorUploadImage',
											iconCls : 'x-edit-insertimage',
											enableToggle : false,
											scope : this,
											handler : this.addUploadImage,
											tooltip : {
												title : '插入图片',
												text : '插入图片到编辑器',
												cls : Ext.baseCSSPrefix + 'html-editor-tip'
											}
										});

								this.getToolbar().add(17, {
											itemId : 'htmlEditorUploadAttach',
											iconCls : 'x-edit-insertattach',
											enableToggle : false,
											scope : this,
											handler : this.addUploadAttach,
											tooltip : {
												title : '插入附件',
												text : '插入附件到编辑器',
												cls : Ext.baseCSSPrefix + 'html-editor-tip'
											}
										});

								try {
									Ext.EventManager.removeListener(this.getDoc(), 'mouseup', Ext.emptyFn(), this);
									Ext.EventManager.removeListener(this.getDoc(), 'keyup', Ext.emptyFn(), this);
								}
								catch (e) {
								}

								Ext.EventManager.addListener(this.getDoc(), 'mouseup', this.onEditorEvent, this);
								Ext.EventManager.addListener(this.getDoc(), 'keyup', this.onEditorEvent, this);
								// Ext.EventManager.on(this.getDoc(), {
								// 'mouseup' : this.onEditorEvent,
								// 'keyup' : this.onEditorEvent,
								// buffer : 100,
								// scope : this
								// });
							}
						});
			},
			onEditorEvent : function() {
				if (Ext.isIE) {
					curSelection = this.getDoc().selection;
					curRange = curSelection.createRange();
				}
			},
			addUploadImage : function() {
				var editor = this;
				var uploadImageForm = Ext.create('Ext.form.Panel', {
							region : 'center',
							autoScroll : true,
							border : false,
							frame : true,
							items : [{
										xtype : 'filefield',
										fieldLabel : '选择图片',
										labelWidth : 60,
										labelAlign : 'right',
										id : 'uploadSelectedImage',
										name : 'uploadSelectedImage',
										buttonText : '选择...',
										allowBlank : false,
										blankText : '上传文件不能为空',
										msgTarget : 'under',
										vtype : 'filetypecheck',
										fileType : 'jpg|bmp|gif|png|jpeg',
										width : 500
									}],
							buttons : [{
										text : '上传',
										type : 'submit',
										handler : function() {
											var fm = uploadImageForm.getForm();
											if (fm.isValid()) {
												fm.submit({
															url : appPath + '/sysadmin/upload/uploadImageCtrl.do',
															method : 'post',
															type : 'ajax',
															waitTitle : '请等待',
															waitMsg : '正在保存...',
															success : function(form, action) {
																var data = Ext.JSON.decode(action.response.responseText);
																if (!Ext.isEmpty(data.url)) {
																	var element = document.createElement("img");
																	element.src = data.url;
																	if (Ext.isIE) {
																		editor.win.focus();
																		if (typeof(curSelection) == 'undefined') {
																			curSelection = editor.getDoc().selection;
																			curRange = curSelection.createRange();
																		}

																		if (typeof(curRange.text) != 'undefined') {
																			if (curRange.text != '') {
																				curRange.select();
																				curSelection.clear();
																			}
																			curRange.collapse(true);
																			curRange.pasteHTML(element.outerHTML);
																			editor.syncValue();
																		}
																		else {
																			curRange.select();
																			curSelection.clear();
																			curRange = curSelection.createRange();
																			curRange.collapse(true);
																			curRange.pasteHTML(element.outerHTML);
																			editor.syncValue();
																		}
																	}
																	else {
																		var selection = editor.win.getSelection();
																		if (!selection.isCollapsed) {
																			selection.deleteFromDocument();
																		}
																		selection.getRangeAt(0).insertNode(element);
																	}
																	form.findField('uploadSelectedImage').clearInvalid();
																	uploadImageWin.close();
																}
																else {
																	window.top.Ext.Msg.alert('错误', '上传图片出错！');
																	form.findField('uploadSelectedImage').clearInvalid();
																}
															},
															failure : pub_form_excepHandler
														});
											}
										}
									}, {
										text : '关闭',
										type : 'submit',
										handler : function() {
											uploadImageWin.close(this);
										}
									}]
						});

				var uploadImageWin = Ext.create('Ext.Window', {
							title : "上传图片",
							width : 550,
							height : 120,
							modal : true,
							border : false,
							layout : "fit",
							items : uploadImageForm
						});
				uploadImageWin.show();
			},
			addUploadAttach : function() {
				var editor = this;
				var uploadAttachForm = Ext.create('Ext.form.Panel', {
							region : 'center',
							autoScroll : true,
							border : false,
							frame : true,
							items : [{
										xtype : 'filefield',
										fieldLabel : '选择附件',
										labelWidth : 60,
										labelAlign : 'right',
										id : 'uploadSelectedAttach',
										name : 'uploadSelectedAttach',
										buttonText : '选择...',
										allowBlank : false,
										blankText : '上传文件不能为空',
										msgTarget : 'under',
										vtype : 'filetypecheck',
										fileType : 'gif|jpg|bmp|jpeg|jpe|png|chm|rar|zip|pdf|doc|xls|ppt|docs|xlss|ppts',
										width : 500
									}],
							buttons : [{
										text : '上传',
										type : 'submit',
										handler : function() {
											var fm = uploadAttachForm.getForm();
											if (fm.isValid()) {
												fm.submit({
															url : appPath + '/sysadmin/upload/uploadAttachCtrl.do',
															method : 'post',
															type : 'ajax',
															waitTitle : '请等待',
															waitMsg : '正在保存...',
															success : function(form, action) {
																var data = Ext.JSON.decode(action.response.responseText);
																if (!Ext.isEmpty(data.url)) {
																	var element = document.createElement("a");
																	element.href = data.url;
																	element.innerHTML = data.orgFileName;
																	if (Ext.isIE) {
																		editor.win.focus();
																		if (typeof(curSelection) == 'undefined') {
																			curSelection = editor.getDoc().selection;
																			curRange = curSelection.createRange();
																		}

																		if (typeof(curRange.text) != 'undefined') {
																			if (curRange.text != '') {
																				curRange.select();
																				curSelection.clear();
																			}
																			curRange.collapse(true);
																			curRange.pasteHTML(element.outerHTML);
																			editor.syncValue();
																		}
																		else {
																			curRange.select();
																			curSelection.clear();
																			curRange = curSelection.createRange();
																			curRange.collapse(true);
																			curRange.pasteHTML(element.outerHTML);
																			editor.syncValue();
																		}
																	}
																	else {
																		var selection = editor.win.getSelection();
																		if (!selection.isCollapsed) {
																			selection.deleteFromDocument();
																		}
																		selection.getRangeAt(0).insertNode(element);
																	}
																	form.findField('uploadSelectedAttach').clearInvalid();
																	uploadAttachWin.close();
																}
																else {
																	window.top.Ext.Msg.alert('错误', '上传附件出错！');
																	form.findField('uploadSelectedAttach').clearInvalid();
																}
															},
															failure : pub_form_excepHandler
														});
											}
										}
									}, {
										text : '关闭',
										type : 'submit',
										handler : function() {
											uploadAttachWin.close(this);
										}
									}]
						});

				var uploadAttachWin = Ext.create('Ext.Window', {
							title : "上传附件",
							width : 550,
							height : 120,
							modal : true,
							border : false,
							layout : "fit",
							items : uploadAttachForm
						});
				uploadAttachWin.show();
			}
		});
