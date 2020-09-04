
_POSTURL = "getRegion.action";
_CODETYPE='REGION';
_PROVICE_LEVEL='1';
_CITY_LEVEL='2';
_AREA_LEVEL='3';
_INPUT_NAME='region';
_HIDDEN_NAME='inst.instRegion';
$G = function (id) {
	return document.getElementById(id) || document.getElementsByName(id)[0];
};
var Location = function (provinceCtl, cityCtl, areaCtl, value) {
	this.provinceCtl = provinceCtl;
	this.cityCtl = cityCtl;
	this.areaCtl = areaCtl;
	this.value = value || "110000";
	this.value = (this.value.length > 1 && this.value.length < 6) ? "110000" : this.value;
	this.value = (this.value.length > 6) ? this.value.substring(0, 6) : this.value;
	this.province = this.value.substring(0, 2) + "0000";
	this.city = this.value.substring(0, 4) + "00";
	this.area = this.value;
};
var Class = {create:function () {
	return function () {
		this.initialize.apply(this, arguments);
	};
}};
var Region = Class.create();
Region.prototype = {location:null, initialize:function (location) {
	this.location = location;
}, init:function () {
	var oThis = this;
	this.initProvince();
	this.initCity();
	this.initArea();
	$G(this.location.provinceCtl).onchange = function (e) {
		oThis.getCity(e, oThis.location.cityCtl);
	};
	$G(this.location.cityCtl).onchange = function (e) {
		oThis.getArea(e, oThis.location.areaCtl);
	};
	$G(this.location.areaCtl).onchange = function (e) {
		var evt = e ? e : window.event;
		elm = evt.srcElement || evt.target;
		initInputAndHidden(elm.value);
	};
	initInputAndHidden(oThis.location.value);
}, initProvince:function () {
	var oThis = this;
	//oThis.addNull(oThis.location.provinceCtl,"省份");
	$.post(_POSTURL, {codeType:_CODETYPE, regionLevel:_PROVICE_LEVEL}, function (data) {
		$.each(eval(data), function (i, n) {
			oThis.createOption(oThis.location.provinceCtl, n, oThis.location.province);
		});
		if(adds == '1')
			oThis.initCityImmed($G(oThis.location.provinceCtl).value);
	});
}, initCity:function () {
	var oThis = this;
	if(adds != '1')
		$.post(_POSTURL, {codeType:_CODETYPE, regionLevel:_CITY_LEVEL, selected:oThis.location.province}, function (data) {
			$.each(eval(data), function (i, n) {
				oThis.createOption(oThis.location.cityCtl, n, oThis.location.city);
			});
		});
}, initCityImmed:function (val) {
	var oThis = this;
	$G(oThis.location.cityCtl).length = 0;
	$.post(_POSTURL, {codeType:_CODETYPE, regionLevel:_CITY_LEVEL, selected:val}, function (data) {
		$.each(eval(data), function (i, n) {
			oThis.createOption(oThis.location.cityCtl, n, "");
		});
		// change area
		oThis.initAreaImmed($G(oThis.location.cityCtl).value);
	});
}, initArea:function () {
	var oThis = this;
	$.post(_POSTURL, {codeType:_CODETYPE, regionLevel:_AREA_LEVEL, selected:oThis.location.area}, function (data) {
		$.each(eval(data), function (i, n) {
			oThis.createOption(oThis.location.areaCtl, n, oThis.location.area);
		});
	});
}, initAreaImmed:function (val) {
	var oThis = this;
	$G(oThis.location.areaCtl).length = 0;
	$.post(_POSTURL, {codeType:_CODETYPE, regionLevel:_AREA_LEVEL, selected:val}, function (data) {
		$.each(eval(data), function (i, n) {
			oThis.createOption(oThis.location.areaCtl, n, "");
		});
	});
}, getCity:function (e, cityName) {
	var oThis = this;
	var evt = e ? e : window.event;
	elm = evt.srcElement || evt.target;
	$G(cityName).length = 0;
	//oThis.addNull(cityName,"城市");
	$.post(_POSTURL, {codeType:_CODETYPE, regionLevel:_CITY_LEVEL, selected:elm.value}, function (data) {
		$.each(eval(data), function (i, n) {
			$G(cityName).add(new Option(n.name, n.value));
		});
		// change area
		oThis.initAreaImmed($G(cityName).value);
	});
	//$G(oThis.location.areaCtl).length = 0;
	//oThis.addNull(oThis.location.areaCtl,"区县");
	initInputAndHidden(elm.value);
}, getArea:function (e, areaName) {
	var oThis = this;
	var evt = e ? e : window.event;
	elm = evt.srcElement || evt.target;
	$G(areaName).length = 0;
	//oThis.addNull(areaName,"区县");
	$.post(_POSTURL, {codeType:_CODETYPE,  regionLevel:_AREA_LEVEL, selected:elm.value}, function (data) {
		$.each(eval(data), function (i, n) {
			$G(areaName).add(new Option(n.name, n.value));
		});
	});
	initInputAndHidden(elm.value);
}, createOption:function (optionName, row, selectedVal) {
	var temp = new Option();
	temp.value = row.value;
	temp.text = row.name;
	row.value == selectedVal ? temp.selected = true : temp.selected = false;
	$G(optionName).add(temp);
}, addNull:function (optionName,text) {
	var temp = new Option();
	temp.value = "";
	temp.text = text;
	$G(optionName).add(temp);
}};
var OptionCtl = {init:function (optionName, codeType, selectedVal) {
	var oThis = this;
	$.post(_POSTURL, {codeType:codeType, optionName:null, selected:null}, function (data) {
		$.each(eval(data), function (i, n) {
			oThis.createOption(optionName, n, selectedVal);
		});
	});
}, createOption:function (optionName, row, selectedVal) {
	var temp = new Option();
	temp.value = row.value;
	temp.text =  row.name;
	row.value == selectedVal ? temp.selected = true : temp.selected = false;
	$G(optionName).add(temp);
}};
function initInputAndHidden(val){
	$G(_INPUT_NAME).value = val;
	$G(_HIDDEN_NAME).value = val;
}
