var imageRegular = /\.([jJ][pP][gG]){1}$|\.([jJ][pP][eE][gG]){1}$|\.([gG][iI][fF]){1}$|\.([pP][nN][gG]){1}$|\.([bB][mM][pP]){1}$/

function showProps(obj) {
	// 用来显示所有的属性名称和值
	var props = "";
	// 开始遍历
	for (var p in obj) {
		// 方法
		if (typeof(obj[p]) == " function ") {
			obj[p]();
		}
		else {
			// p 为属性名称，obj[p]为对应属性的值
			props += p + " = " + obj[p] + " \t ";
		}
	}
	// 最后显示所有的属性
	alert(props);
}

var rendererDate = function(val) {
	try {
		var JsonDateValue;
		if (Ext.isEmpty(val)) {
			return '';
		}
		else if (Ext.isEmpty(val.time)) {
			JsonDateValue = new Date(val);
		}
		else {
			JsonDateValue = new Date(val.time);
		}

		return Ext.util.Format.date(JsonDateValue, 'Y-m-d');
	}
	catch (e) {
	}
	return '';
}

var rendererDateTime = function(val) {
	try {
		var JsonDateValue;
		if (Ext.isEmpty(val)) {
			return '';
		}
		else if (Ext.isEmpty(val.time)) {
			JsonDateValue = new Date(val);
		}
		else {
			JsonDateValue = new Date(val.time);
		}

		return Ext.util.Format.date(JsonDateValue, 'Y-m-d H:i:s');
	}
	catch (e) {
	}
	return '';
}

Ext.define('App.common.model.ZdModel', {
			extend : 'Ext.data.Model',
			fields : ['id', 'name']
		});

Ext.define('App.common.model.Sex', {
			extend : 'Ext.data.Model',
			fields : ['id', 'name']
		});

Ext.define('App.common.model.CertType', {
			extend : 'Ext.data.Model',
			fields : ['id', 'name']
		});

Ext.define('App.common.model.District', {
			extend : 'Ext.data.Model',
			fields : ['id', 'name']
		});

Ext.define('App.common.store.QueryStatus', {
			extend : 'Ext.data.Store',
			model : 'App.common.model.ZdModel',
			data : [{
						id : '',
						name : '全部'
					}, {
						id : '1',
						name : '有效'
					}, {
						id : '0',
						name : '冻结'
					}],
			proxy : {
				type : 'memory',
				reader : {
					type : 'json'
				}
			}
		});

Ext.define('App.common.store.EditStatus', {
			extend : 'Ext.data.Store',
			model : 'App.common.model.ZdModel',
			data : [{
						id : '1',
						name : '有效'
					}, {
						id : '0',
						name : '冻结'
					}],
			proxy : {
				type : 'memory',
				reader : {
					type : 'json'
				}
			}
		});

Ext.define('App.common.store.QueryYesNo', {
			extend : 'Ext.data.Store',
			model : 'App.common.model.ZdModel',
			data : [{
						id : '',
						name : '全部'
					}, {
						id : '1',
						name : '是'
					}, {
						id : '0',
						name : '否'
					}],
			proxy : {
				type : 'memory',
				reader : {
					type : 'json'
				}
			}
		});

Ext.define('App.common.store.QueryAudit', {
			extend : 'Ext.data.Store',
			model : 'App.common.model.ZdModel',
			data : [{
						id : '',
						name : '全部'
					}, {
						id : '0',
						name : '未审核'
					}, {
						id : '1',
						name : '已通过'
					}, {
						id : '2',
						name : '未通过'
					}],
			proxy : {
				type : 'memory',
				reader : {
					type : 'json'
				}
			}
		});

Ext.define('App.common.store.EditYesNo', {
			extend : 'Ext.data.Store',
			model : 'App.common.model.ZdModel',
			data : [{
						id : '1',
						name : '是'
					}, {
						id : '0',
						name : '否'
					}],
			proxy : {
				type : 'memory',
				reader : {
					type : 'json'
				}
			}
		});

Ext.define('App.common.store.EditSex', {
			extend : 'Ext.data.Store',
			model : 'App.common.model.Sex',
			proxy : {
				type : 'ajax',
				url : appPath + '/sysadmin/zd/getZdSexCtrl.do',
				reader : {
					type : 'json',
					root : 'zdList'
				}
			}
		});

Ext.define('App.common.store.EditCertType', {
			extend : 'Ext.data.Store',
			model : 'App.common.model.CertType',
			proxy : {
				type : 'ajax',
				url : appPath + '/sysadmin/zd/getZdCertTypeCtrl.do',
				reader : {
					type : 'json',
					root : 'zdList'
				}
			}
		});

Ext.define('App.common.store.EditDistrict', {
			extend : 'Ext.data.Store',
			model : 'App.common.model.District',
			autoLoad : false,
			proxy : {
				type : 'ajax',
				url : appPath + '/sysadmin/zd/getZdDistrictCtrl.do',
				extraParams : {
					'parentId' : 0
				},
				reader : {
					type : 'json',
					root : 'zdList'
				}
			}
		});
