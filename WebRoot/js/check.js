Ext.apply(Ext.form.field.VTypes, {
	password : function(val, field) {
		if (field.initialPassField) {
			var pwd = Ext.getCmp(field.initialPassField);
			return (val == pwd.getValue());
		}
		return true;
	},
	passwordText : '两次输入的密码不一致',

	chinese : function(val, field) {
		try {
			if (val == '' || /^[\u4e00-\u9fa5]+$/i.test(val)) {
				return true;
			}
			return false;
		}
		catch (e) {
			return false;
		}
	},
	chineseText : '请输入中文',

	alpha : function(val, field) {
		try {
			if (val == '' || /^[a-zA-Z]+$/.test(val)) {
				return true;
			}
			return false;
		}
		catch (e) {
			return false;
		}
	},
	alphaText : '请输入英文字母',

	alphanum : function(val, field) {
		try {
			if (val == '' || /^[a-zA-Z0-9]+$/.test(val)) {
				return true;
			}
			return false;
		}
		catch (e) {
			return false;
		}
	},
	alphanumText : '请输入英文字母或数字,其它字符不允许',

	alphanumunderline : function(val, field) {
		try {
			if (val == '' || /^[a-zA-Z0-9_]+$/.test(val)) {
				return true;
			}
			return false;
		}
		catch (e) {
			return false;
		}
	},
	alphanumunderlineText : '请输入英文字母、数字或下滑线,其它字符不允许',

	useraccount : function(val, field) {
		try {
			if (val == '' || /^[a-z0-9_]+$/.test(val)) {
				return true;
			}
			return false;
		}
		catch (e) {
			return false;
		}
	},
	useraccountText : '请输入小写字母、数字或下滑线,其它字符不允许',

	integer : function(val, field) {
		try {
			if (val == '' || /^[-+]?[\d]+$/.test(val)) {
				return true;
			}
			return false;
		}
		catch (e) {
			return false;
		}
	},
	integerText : '请输入整数',

	plus : function(val, field) {
		try {
			if (val == '' || /^[\d]+$/.test(val)) {
				return true;
			}
			return false;
		}
		catch (e) {
			return false;
		}
	},
	plusText : '请输入正整数',

	max : function(val, field) {
		try {
			if (parseFloat(val) <= parseFloat(field.max)) {
				return true;
			}
			return false;
		}
		catch (e) {
			return false;
		}
	},
	maxText : '超过最大值',

	min : function(val, field) {
		try {
			if (parseFloat(val) >= parseFloat(field.min)) {
				return true;
			}
			return false;
		}
		catch (e) {
			return false;
		}
	},
	minText : '小于最小值',

	datecn : function(val, field) {
		try {
			if (val == '') {
				return true;
			}

			var regex = /^(\d{4})-(\d{2})-(\d{2})$/;
			if (!regex.test(val)) {
				return false;
			}
			var d = new Date(val.replace(regex, '$1/$2/$3'));
			return (parseInt(RegExp.$2, 10) == (1 + d.getMonth())) && (parseInt(RegExp.$3, 10) == d.getDate())
					&& (parseInt(RegExp.$1, 10) == d.getFullYear());
		}
		catch (e) {
			return false;
		}
	},
	datecnText : '请使用这样的日期格式: yyyy-mm-dd. 例如:2001-01-01.',

	age : function(val, field) {
		try {
			if (parseInt(val) >= 18 && parseInt(val) <= 200) {
				return true;
			}
			return false;
		}
		catch (err) {
			return false;
		}
	},
	ageText : '年龄输入有误',

	url : function(val, field) {
		try {
			if (val == '' || /^(http|https|ftp):\/\/(([A-Z0-9][A-Z0-9_-]*)(\.[A-Z0-9][A-Z0-9_-]*)+)(:(\d+))?\/?/i.test(val)) {
				return true;
			}
			return false;
		}
		catch (e) {
			return false;
		}
	},
	urlText : '请输入有效的URL地址.',

	ip : function(val, field) {
		try {
			if (val == '' || /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/.test(val)) {
				return true;
			}
			return false;
		}
		catch (e) {
			return false;
		}
	},
	ipText : '请输入正确的IP地址',

	tel : function(val, field) {
		try {
			if (val == '' || /^(\d{2,5}-)?\d{2,5}-\d{5,12}$/.test(val)) {
				return true;
			}
			return false;
		}
		catch (e) {
			return false;
		}
	},
	telText : '请输入正确的固定电话号码,如:010-12345678',

	mobile : function(val, field) {
		try {
			if (val == '' || /(^0?[1][23456789][0-9]{9}$)/.test(val)) {
				return true;
			}
			return false;
		}
		catch (e) {
			return false;
		}
	},
	mobileText : '请输入正确的手机号码',

	email : function(val, field) {
		try {
			if (val == '' || /^(\w+)([\-+.][\w]+)*@(\w[\-\w]*\.){1,5}([A-Za-z]){1,6}$/.test(val)) {
				return true;
			}
			return false;
		}
		catch (e) {
			return false;
		}
	},
	emailText : '请输入正确的email',

	postcode : function(val, field) {
		try {
			if (val == '' || /^[-+]?[\d]+$/.test(val)) {
				return true;
			}
			return false;
		}
		catch (e) {
			return false;
		}
	},
	postcodeText : '请输入正确的邮政编码',

	money : function(val, field) {
		try {
			if (val == '' || /^\d+(\.\d{1,2})?$/.test(val)) {
				return true;
			}
			return false;
		}
		catch (e) {
			return false;
		}
	},
	moneyText : '请输入正确的金额',

	idcard : function(val, field) {
		try {
			if (val == ''
					|| /^([1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}|([1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])((\d{4})|\d{3}[A-Z])))$/
							.test(v)) {
				return true;
			}
			return false;
		}
		catch (e) {
			return false;
		}
	},
	idcardText : '请输入正确的身份证号码',

	specialChar : function(val, field) {
		try {
			if (val == '' || /^[^`+=?|\\\'"%;<>]{1,}$/.test(val)) {
				return true;
			}
			return false;
		}
		catch (e) {
			return false;
		}
	},
	specialCharText : "不能输入(`,#,+,=,?,|,\,'," + '",%,;,<,>)特殊字符!',

	daterange : function(val, field) {
		var date = field.parseDate(val);
		if (!date) {
			return;
		}
		if (field.startDateField && (!this.dateRangeMax || (date.getTime() != this.dateRangeMax.getTime()))) {
			var start = Ext.getCmp(field.startDateField);
			start.setMaxValue(date);
			start.validate();
			this.dateRangeMax = date;
		}
		else if (field.endDateField & (!this.dateRangeMin || (date.getTime() != this.dateRangeMin.getTime()))) {
			var end = Ext.getCmp(field.endDateField);
			end.setMinValue(date);
			end.validate();
			this.dateRangeMin = date;
		}
		return true;
	},

	filetypecheck : function(v, field) {
		this.filetypecheckText = '文件类型必须是: ' + field.fileType;
		var strFileType = field.fileType.split('|');
		var strFtypeVal = "";
		if (v.lastIndexOf(".") != -1) {
			strFtypeVal = v.substring(v.lastIndexOf(".") + 1).toUpperCase();
		}
		for (var a = 0; a < strFileType.length; a++) {
			if (strFtypeVal == strFileType[a].toUpperCase()) {
				return true;
			}
		}
		return false;
	},
	filetypecheckText:'请选择正确的文件类型！'
});
