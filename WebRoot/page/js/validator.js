/*******************************************************************************
 * Validator v1.03 code by 我佛山人 wfsr@msn.com
 ******************************************************************************/
Validator = {	Require : /.+/,
	Email : /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/,
	Phone : /^((\(\d{3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/,
	PhoneMobileOfficePhone :/((^\d{11}$)|^((\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$)/,
	OfficePhone : /^(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/,
	Mobile : /^((\(\d{3}\))|(\d{3}\-))?13\d{9}$/,
	Url : /^http:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*$/,
	IdCard : /(^\d{15}$)|(^\d{17}([0-9]|X|x)$)/,
	Currency : /^\d+(\.\d+)?$/,
	Number : /^\d+$/,
	NumberLimit : /^\d{0,10}$/,
	Zip : /^[1-9]\d{5}$/,
	QQ : /^[1-9]\d{4,8}$/,
	Integer : /^[-\+]?\d+$/,
	Double : /^[-\+]?\d+(\.\d+)?$/,
	English : /^[A-Za-z]+$/,
	Chinese : /^[\u0391-\uFFE5]+$/,
	Username : /^[a-z]\w{3,}$/i,
	UnSafe : /^(([A-Z]*|[a-z]*|\d*|[-_\~!@#\$%\^&\*\.\(\)\[\]\{\}<>\?\\\/\'\"]*)|.{0,5})$|\s/,
	IsSafe : function(str) {
		return !this.UnSafe.test(str);
	},
	SafeString : "this.IsSafe(value)",
	Limit : "this.limit(value.length,getAttribute('min'), getAttribute('max'))",
	LimitB : "this.limit(this.LenB(value), getAttribute('min'), getAttribute('max'))",
	Date : "this.IsDate(value, getAttribute('min'), getAttribute('format'))",
	Repeat : "value == document.getElementsByName(getAttribute('to'))[0].value",
	Range : "getAttribute('min') < (value|0) && (value|0) < getAttribute('max')",
	Compare : "this.compare(value,getAttribute('operator'),getAttribute('to'))",
	Custom : "this.Exec(value, getAttribute('regexp'))",
	Group : "this.MustChecked(getAttribute('name'), getAttribute('min'), getAttribute('max'))",
	IP : "this.IsIP(value)",
	NoneNegativeInt : "this.IsNoneNegativeInt(value)",
	ErrorItem : [ document.forms[0] ],
	ErrorMessage : [ "以下原因导致提交失败：\t\t\t\t" ],
	Validate : function(theForm, mode) {
		var obj = theForm || event.srcElement;
		var count = obj.elements.length;
		this.ErrorMessage.length = 1;
		this.ErrorItem.length = 1;
		this.ErrorItem[0] = obj;
		for (var i = 0; i < count; i++) {
			with (obj.elements[i]) {
				var _dataType = getAttribute("dataType");
				if (typeof (_dataType) == "object"
						|| typeof (this[_dataType]) == "undefined")
					continue;
				this.ClearState(obj.elements[i]);
				value = this.fullTrim(value);
				if (getAttribute("require") == "false" && value == "")
					continue;
				value = this.htmlEncode(value);
				switch (_dataType) {
				case "Date":
				case "Repeat":
				case "Range":
				case "Compare":
				case "Custom":
				case "Group":
				case "Limit":
				case "LimitB":
				case "SafeString":
				case "IP":
				case "NoneNegativeInt":
					if (!eval(this[_dataType])) {
						this.AddError(i, getAttribute("msg"));
					}
					break;
				default:
					if (!this[_dataType].test(value)) {
						this.AddError(i, getAttribute("msg"));
					}
					break;
				}
			}
		}
		if (this.ErrorMessage.length > 1) {
			mode = mode || 1;
			var errCount = this.ErrorItem.length;
			switch (mode) {
			case 2:
				for (var i = 1; i < errCount; i++)
					this.ErrorItem[i].style.color = "red";
			case 1:
				alert(this.ErrorMessage.join("\n"));
				this.ErrorItem[1].focus();
				break;
			case 3:
				for (var i = 1; i < errCount; i++) {
					try {
						var span = document.createElement("SPAN");
						span.id = "__ErrorMessagePanel";
						span.style.color = "red";
						this.ErrorItem[i].parentNode.appendChild(span);
						span.innerHTML = this.ErrorMessage[i].replace(/\d+:/,
								"*");
					} catch (e) {
						alert(e.description);
					}
				}
				this.ErrorItem[1].focus();
				break;
			case 4://add by wangyang  需配合messageBox使用
				for (var i = 1; i < errCount; i++) {
					try {
						var m = new MessageBox(this.ErrorItem[i]);
						m.Show(this.ErrorMessage[i].replace(/\d+:/,""));
						break;
					} catch (e) {
						alert(e.description);
					}
				}
				this.ErrorItem[1].focus();
				break;
			default:
				alert(this.ErrorMessage.join("\n"));
				break;
			}
			return false;
		}
		return true;
	},
	limit : function(len, min, max) {
		min = min || 0;
		max = max || Number.MAX_VALUE;
		return min <= len && len <= max;
	},
	LenB : function(str) {
		return str.replace(/[^\x00-\xff]/g, "**").length;
	},
	ClearState : function(elem) {
		with (elem) {
			if (style.color == "red")
				style.color = "";
			var lastNode = parentNode.childNodes[parentNode.childNodes.length - 1];
			if (lastNode.id == "__ErrorMessagePanel")
				parentNode.removeChild(lastNode);
		}
	},
	AddError : function(index, str) {
		this.ErrorItem[this.ErrorItem.length] = this.ErrorItem[0].elements[index];
		this.ErrorMessage[this.ErrorMessage.length] = this.ErrorMessage.length
				+ ":" + str;
	},
	Exec : function(op, reg) {
		return new RegExp(reg, "g").test(op);
	},
	compare : function(op1, operator, op2) {
		switch (operator) {
		case "NotEqual":
			return (op1 != op2);
		case "GreaterThan":
			return (op1 > op2);
		case "GreaterThanEqual":
			return (op1 >= op2);
		case "LessThan":
			return (op1 < op2);
		case "LessThanEqual":
			return (op1 <= op2);
		default:
			return (op1 == op2);
		}
	},
	MustChecked : function(name, min, max) {
		var groups = document.getElementsByName(name);
		var hasChecked = 0;
		min = min || 1;
		max = max || groups.length;
		for (var i = groups.length - 1; i >= 0; i--)
			if (groups[i].checked)
				hasChecked++;
		return min <= hasChecked && hasChecked <= max;
	},
	IsDate : function(op, formatString) {
		formatString = formatString || "ymd";
		var m, year, month, day;
		switch (formatString) {
		case "ymd":
			m = op.match(new RegExp(
					"^((\\d{4})|(\\d{2}))([-./])(\\d{1,2})\\4(\\d{1,2})$"));
			if (m == null)
				return false;
			day = m[6];
			month = m[5] * 1;
			year = (m[2].length == 4) ? m[2] : GetFullYear(parseInt(m[3], 10));
			break;
		case "dmy":
			m = op.match(new RegExp(
					"^(\\d{1,2})([-./])(\\d{1,2})\\2((\\d{4})|(\\d{2}))$"));
			if (m == null)
				return false;
			day = m[1];
			month = m[3] * 1;
			year = (m[5].length == 4) ? m[5] : GetFullYear(parseInt(m[6], 10));
			break;
		default:
			break;
		}
		if (!parseInt(month))
			return false;
		month = month == 0 ? 12 : month;
		var date = new Date(year, month - 1, day);
		return (typeof (date) == "object" && year == date.getFullYear()
				&& month == (date.getMonth() + 1) && day == date.getDate());
		function GetFullYear(y) {
			return ((y < 30 ? "20" : "19") + y) | 0;
		}
	},
	IsIP : function(strIP) {
		var re = /^(\d+)\.(\d+)\.(\d+)\.(\d+)$/g // 匹配IP地址的正则表达式
		if (re.test(strIP)) {
			if (RegExp.$1 < 256 && RegExp.$2 < 256 && RegExp.$3 < 256
					&& RegExp.$4 < 256)
				return true;
		}
		return false;
	},
	IsNoneNegativeInt : function(intValue) {

		if (intValue == '') {
			intValue = 0;
		}

		// 保证长度在非负整数范围内
		var re = /^\d+$/g; // 非负整数正则表达式
		if (re.test(intValue) == false) {
			return false;
		}
		// 判断是否为整数
		if (intValue > 2147483647 || intValue < 0) {
			return false;
		}
		return true;
	},
	htmlEncode : function(strS) {
		return (strS.replace(/"/g, "＂").replace(/</g, "&lt;").replace(/>/g,
				"&gt;"));
	},
	fullTrim : function(strS) {
		return strS.replace(/^\s*/, '').replace(/\s*$/, '');
	}

}