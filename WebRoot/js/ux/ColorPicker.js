/**
 * @class App.ux.ColorPicker
 * @extends Ext.container.Container 定义颜色选取类
 */

Ext.define('App.ux.ColorPicker', {
			extend : 'Ext.form.field.Trigger',
			alias : 'widget.appcolorpicker',
			triggerTip : 'Please select a color.',
			initComponent : function() {
				var me = this;
				this.listeners = {
					afterrender : function(obj) {
						me.inputEl.setStyle({
									backgroundImage : 'none'
								});
						if (!Ext.isEmpty(this.value)) {
							me.inputEl.setStyle({
										backgroundColor : me.value
									});
						}
						else {
							me.inputEl.setStyle({
										backgroundColor : '#ffffff'
									});
						}
					}
				}

				this.callParent(arguments);
			},
			onTriggerClick : function() {
				var me = this;
				picker = Ext.create('Ext.picker.Color', {
							pickerField : this,
							ownerCt : this,
							renderTo : document.body,
							floating : true,
							hidden : true,
							focusOnShow : true,
							style : {
								backgroundColor : '#ffffff'
							},
							listeners : {
								scope : this,
								select : function(field, value, opts) {
									me.setValue('#' + value);
									me.inputEl.setStyle({
												backgroundColor : value
											});
									picker.hide();
								},
								show : function(field, opts) {
									field.getEl().monitorMouseLeave(500, field.hide, field);
								}
							}
						});
				picker.alignTo(me.inputEl, 'tl-bl?');
				picker.show(me.inputEl);
			},
			setDefaultValue : function(value) {
				this.setValue(value);
				if (this.rendered) {
					if (!Ext.isEmpty(value)) {
						this.inputEl.setStyle({
									backgroundColor : value
								});
					}
					else {
						this.inputEl.setStyle({
									backgroundColor : '#ffffff'
								});
					}
				}
			}
		});