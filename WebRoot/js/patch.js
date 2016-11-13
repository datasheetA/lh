if (Ext.getVersion().getMinor() == 0) {
	Ext.override(Ext.form.Field, {// ext4.0.x增加setFieldLabel()函数
		setFieldLabel : function(newLabel) {
			if (this.rendered) {
				Ext.get(this.labelEl.id).update(newLabel);
			}
			this.fieldLabel = newLabel;
		}
	});
	
	Ext.override(Ext.LoadMask, {// ext4.0.x combobox 第二次 load时 loadmask不消失问题 
            onHide: function() {
                this.callParent();
            }
        }); 
	
}





