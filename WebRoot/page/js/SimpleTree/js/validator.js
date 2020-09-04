/*****************************************************************************
函数名称	：fucCheckNull
函数功能	：检查是否为空
参数		：obj，要检查的对象
参数		：strAlertMsg 要显示的提示信息
返回    	：消息提示框  true/false
日期	   	：2009-06-22
作者    	：jz_guo
修改人  	：
修改日  	：
******************************************************************************/
function fucCheckNull(obj,strAlertMsg)
{
    strTemp=obj.value;
    // 去掉字符串两边的空格
	strTemp=strTemp.replace(/^(\s)*|(\s)*$/g,"");
	if (strTemp.length<1)
		{
			var m = new MessageBox(obj);
			m.Show(strAlertMsg);
			obj.focus();
			return false;
		}
}


function fucCheckNullBoth(chanNum,repNum){
	var repNumView = repNum;
	chanNum=chanNum.value;
	repNum=repNum.value;
	// 去掉字符串两边的空格
	chanNum=chanNum.replace(/^(\s)*|(\s)*$/g,"");
	repNum=repNum.replace(/^(\s)*|(\s)*$/g,"");
	if (chanNum.length<1&&repNum.length<1){
		var repNum = new MessageBox(repNumView);
		repNum.Show("保单号和旧保单号不可同时\n为空,请输入任意一个");
		repNumView.focus();
		return false;
	}
}
function fucCheckNullLength(obj,length,regxt,strAlertNullMsg,strAlertLengthMsg,strTestMsg) {
    strTemp=obj.value;
    // 去掉字符串两边的空格
	strTemp=strTemp.replace(/^(\s)*|(\s)*$/g,"");
	if (strTemp.length<1){
			var m = new MessageBox(obj);
			m.Show(strAlertNullMsg);
			obj.focus();
			return false;
	}

	if(regxt==false){
		var m = new MessageBox(obj);
		m.Show(strTestMsg);
		obj.focus();
		return false;
	}

	if(strTemp.length!=length){
		var m = new MessageBox(obj);
		m.Show(strAlertLengthMsg);
		obj.focus();
		return false;
	}
}
/*****************************************************************************
函数名称	：fucCheckObjAndNull
函数功能	：检查两个对象中的值不同时为空
参数		：strObj1 对象的ID
参数		：strObj2  对象的ID
参数		：strAlertMsg 要显示的提示信息
返回    	：消息提示框  true/false
日期		：2009-06-22
作者    	：jz_guo
修改人  	：
修改日  	：
******************************************************************************/
function fucCheckObjAndNull(strObj1,strObj2,strAlertMsg)
{
	var obj1=document.all[strObj1];
	var strTmp1="";		
	if(obj1!=null)
	{
	  strTmp1=obj1.value;
	}
	
	var obj2=document.all[strObj2];
	var strTmp2="";
	if(obj2!=null)
	{
	  strTmp2=obj2.value;
	}
	
	//检查不同时为空
	strTemp1=strTemp1.replace(/^(\s)*|(\s)*$/g,"");//去掉字符串两边的空格
	strTemp2=strTemp2.replace(/^(\s)*|(\s)*$/g,"");//去掉字符串两边的空格
	if ((strTemp1.length<1) && (strTemp2.length<1))
	{
	    strObj1.focus();
		var m = new MessageBox(strObj1);
		m.Show(strAlertMsg);
		return false;
	}
	else
	{
	  return true;
	}
}

/*****************************************************************************
函数名称	：fucIsInteger
函数功能	：检查是否为 空 或 整数
参数		：obj,要验证的数值
参数		：strAlertMsg, 出错时要显示的提示信息
返回    	：消息提示框  true/false
日期		：2009-06-22
作者    	：jz_guo
修改人  	：
修改日  	：
******************************************************************************/
function fucIsInteger(obj,strAlertMsg)
 {
        strInteger=obj.value;//要验证的数值
        strInteger=strInteger.replace(/^(\s)*|(\s)*$/g,"");//去掉字符串两边的空格
        
		//验证规则：整数
		var newPar=/^(-|\+)?\d+$/
		if(strInteger.length>0 && newPar.test(strInteger)==false)
		{
		   obj.focus();
			var m = new MessageBox(obj);
			m.Show(strAlertMsg);	
		   return false;	
		 }
		 else
		 {
		    return true;
		 }
 }
		
/*****************************************************************************
函数名称	：fucIsFloat
函数功能	：检查是否为 空 或 有效数值（实数）
参数		：obj,要验证的数值
参数		：strAlertMsg, 出错时要显示的提示信息
返回    	：消息提示框  true/false
日期		：2009-06-22
作者    	：jz_guo
修改人  	：
修改日  	：
******************************************************************************/
function fucIsFloat(obj,strAlertMsg)
 {
        strFloat=obj.value;//要验证的数值
        strFloat=strFloat.replace(/^(\s)*|(\s)*$/g,"");//去掉字符串两边的空格
        
		//验证规则：整数
		var newPar=/^(-|\+)?\d*.?\d+$/
		if(strFloat.length>0 && newPar.test(strFloat)==false)
		{
		   obj.focus();
			var m = new MessageBox(obj);
			m.Show(strAlertMsg);		
		   return false;	
		 }
		 else
		 {
		    return true;
		 }
 }
 
/*****************************************************************************
函数名称	：fucIsUnsignedInteger
函数功能	：检查是否为 空 或 正整数
参数		：obj,要验证的数值
参数		：strAlertMsg, 出错时要显示的提示信息
返回    	：消息提示框  true/false
日期		：2009-06-22
作者    	：jz_guo
修改人  	：
修改日  	：
******************************************************************************/
function fucIsUnsignedInteger(obj,strAlertMsg)
 {
		strInteger=obj.value;//要验证的数值
        strInteger=strInteger.replace(/^(\s)*|(\s)*$/g,"");//去掉字符串两边的空格
        
		//验证规则：正整数
		var newPar=/^\d*[123456789]\d*$/
		if(strInteger.length>0 && newPar.test(strInteger)==false)
		{
		   obj.focus();
		   var m = new MessageBox(obj);
		   m.Show(strAlertMsg);	
		   return false;	
		 }
		 else
		 {
		    return true;
		 }
 }
 
 /*****************************************************************************
函数名称	：fucIsUnsignedInteger
函数功能	：检查是否为 空 或 非负数
参数		：obj,要验证的数值
参数		：strAlertMsg, 出错时要显示的提示信息
返回    	：消息提示框  true/false
日期		：2009-06-22
作者    	：jz_guo
修改人  	：
修改日  	：
******************************************************************************/
function fucIsNoUnsignedInteger(obj,strAlertMsg)
 {
		strInteger=obj.value;//要验证的数值
        strInteger=strInteger.replace(/^(\s)*|(\s)*$/g,"");//去掉字符串两边的空格
        
		//验证规则：非负数
		var newPar=/^\d+$/
		if(strInteger.length>0 && newPar.test(strInteger)==false)
		{
		   obj.focus();
		   var m = new MessageBox(obj);
		   m.Show(strAlertMsg);		
		   return false;	
		 }
		 else
		 {
		    return true;
		 }
 }

/*****************************************************************************
函数名称	：fucCheckMail
函数功能	：检查对象的值是否为Email Address
参数		：obj,要检查的对象
返回    	：消息提示框  true/false
日期		：2009-06-22
作者    	：jz_guo
修改人  	：
修改日  	：
******************************************************************************/
function fucCheckMail(obj,strAlertMsg)
 {
		strAddress=obj.value;
		strAddress=strAddress.replace(/^(\s)*|(\s)*$/g,"");//去掉字符串两边的空格
				
		//匹配规则：
		//只允许以字母开头，用a-z,A-Z,0-9以及下划线组成的email名
		//email后面的域名只允许字母或下划线开头,至少一个.,以字母或下划线结束
		//var newPar=/^[a-zA-Z](\w*)@\w+\.(\w|.)*\w+$/
		var newPar=/^([\S])+[@]{1}([\S])+[.]{1}(\S)+$/;
		if(strAddress.length>0 && newPar.test(strAddress)==false)
		{
		   var m = new MessageBox(obj);
		   m.Show(strAlertMsg);	
		   obj.focus();	
		   return false;	
		 }
		 else
		 {
		    return true;
		 }
 }

/*****************************************************************************
函数名称	：fucCompareValue
函数功能	：检查两个对象的值是否一致
参数		：obj1,对象1
参数		：obj2,对象2
参数		：strAddress,提示信息
返回    	：消息提示框  true/false
日期		：2009-06-22
作者    	：jz_guo
修改人  	：
修改日  	：
******************************************************************************/
function fucCompareValue(obj1,obj2,strAlertMsg)
 {
		strValue1=obj1.value;
		strValue2=obj2.value;
		strValue1=strValue1.replace(/^(\s)*|(\s)*$/g,"");//去掉字符串两边的空格
		strValue2=strValue2.replace(/^(\s)*|(\s)*$/g,"");//去掉字符串两边的空格
				
		if(strValue1!=strValue2)
		{
		   var m = new MessageBox(obj1);
		   m.Show(strAlertMsg);	
		   obj1.focus();	
		   return false;	
		 }
		 else
		 {
		    return true;
		 }
 }
 
/*****************************************************************************
函数名称	：fucCheckLength
函数功能	：判断字符串的长度是否已经超出制定的范围
参数		：obj,要检查的对象
参数		：iStrMax,字符串约束的最大长度
参数		：strAlertMsg 要显示的提示信息
返回    	：消息提示框  true/false
日期		：2009-06-22
作者    	：jz_guo
修改人  	：
修改日  	：
******************************************************************************/
function fucCheckLength(obj,iStrMax,strAlertMsg)
{
	strTemp=obj.value;
	strTemp=strTemp.replace(/"/,"&quot");
	strTemp=strTemp.replace(/</g,"&lt");
	strTemp=strTemp.replace(/>/g,"&gt");
	strTemp=strTemp.replace(/'/g,"''");
	strTemp=strTemp.replace(/\n/g,"<br>");
	var i,sum;
	sum=0;
	for(i=0;i<strTemp.length;i++)
	{
		//如果是标准字符，占一个字符长度
		if ((strTemp.charCodeAt(i)>=0) && (strTemp.charCodeAt(i)<=255))
			sum=sum+1;
		else  //如果是非标准字符（汉字），占两个字符长度
			sum=sum+2;
	}
	
	if(sum>iStrMax)
	{
	  //超出了约束的最大字符长度
	   var m = new MessageBox(obj);
	   m.Show(strAlertMsg);	
	   obj.focus();
	   return false;
	}
	else
	{
	   return true;
	}	
}
 
/*****************************************************************************
函数名称	：fucCheckDateFormat
函数功能	：验证输入日期的格式是否正确,如2003-09-01 或 空
参数		：obj,要检查的对象
参数		：strAlertMsg, 出错时要显示的提示信息
返回    	：消息提示框  true/false
日期		：2009-06-22
作者    	：jz_guo
修改人  	：
修改日  	：
******************************************************************************/
function fucCheckDateFormat(obj,strAlertMsg)
{
    strDate=obj.value;
    strDate=strDate.replace(/^(\s)*|(\s)*$/g,"");//去掉字符串两边的空格
    
   //验证规则：长日期格式，不足用0补齐，如2003-09-01
    var newPar=/^\d{4}\-\d{2}\-\d{2}$/
    if(strDate.length>0 && newPar.test(strDate)==false)
    {
        obj.focus();
		var m = new MessageBox(obj);
		m.Show(strAlertMsg);	
	    return false;
	}
	else
	{
	   return true;
	}	
}

/*****************************************************************************
函数名称	：fucCheckTimeFormat
函数功能	：验证输入日期的格式是否正确,如 hh:ss 或 空
参数		：strTime,要检查的对象
参数		：strAlertMsg, 出错时要显示的提示信息
返回    	：消息提示框  true/false
日期		：2009-06-22
作者    	：jz_guo
修改人  	：
修改日  	：
******************************************************************************/
function fucCheckTimeFormat(obj,strAlertMsg)
{
    strTime=obj.value;//日期字符串
    strTime=strDate.replace(/^(\s)*|(\s)*$/g,"");//去掉字符串两边的空格
    
   //验证规则：时间格式(只到分) hh:mm
    var newPar=/^([0,1][0-9])|[2][0-3]:[0-5][0-9]$/
    if(strTime.length>0 && newPar.test(strTime)==false)
    {
        obj.focus();
		var m = new MessageBox(obj);
		m.Show(strAlertMsg);
	    return false;
	}
	else
	{
	   return true;
	}	
}

/*****************************************************************************
函数名称	：fucCheckDateOrder
函数功能	：验证开始日期必须在结束日期之后(比较的日期格式：2003-09-01)
参数		：strDate,开始日期字符串
参数		：strEDate,结束日期字符串
返回    	：消息提示框  true/false
日期		：2009-06-22
作者    	：jz_guo
修改人  	：
修改日  	：
******************************************************************************/
function fucCheckDateOrder(objSDate,objEDate,strMsg)
{
    strSDate=objSDate.value;//日期字符串
    strSDate=strSDate.replace(/^(\s)*|(\s)*$/g,"");//去掉字符串两边的空格
    
    strEDate=objEDate.value;//日期字符串
    strEDate=strEDate.replace(/^(\s)*|(\s)*$/g,"");//去掉字符串两边的空格
    
    strSDate=strSDate.replace(/\-/,"\/");
    strEDate=strEDate.replace(/\-/,"\/");
    
    if(strMsg==""||strMsg==null)
    {
       strMsg="fff";
    }
   
   //比较时间
    if(new Date(strSDate).getTime()>new Date(strEDate).getTime())
    {
        objEDate.focus();
		var m = new MessageBox(objSDate);
		m.Show(strMsg);
        return false;
     }
     else
     {
        return true;
     }
}


/*****************************************************************************
函数名称	：getNowTime
函数功能	：获取当前时间 
返回    	：当前日期:yyyy-mm-dd hh:mm:ss
日期		：2009-06-22
作者    	：jz_guo
修改人  	：
修改日  	：
******************************************************************************/
function getNowTime()
{
    var today=new Date();					//获取当前的日期实例
	var year = today.getUTCFullYear();		//年
	var month =today.getUTCMonth()+1;		//月
	var day = today.getUTCDate();			//日
	var hour=today.getHours();				//时
	var minute=today.getMinutes();			//分
	var second=today.getSeconds();			//秒
	if (month <= 9) month = "0" + month; 
	if (day <= 9) day = "0" + day;
	if (hour <= 9) hour = "0" + hour;
	if (minute <= 9) minute = "0" + minute;
	if (second <= 9) second = "0" + second;
	var clocktext =year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second;
	return clocktext;
}

/*****************************************************************************
函数名称	：selectall
函数功能	：复选框全选 
返回    	：
日期		：2009-06-22
作者    	：sz
修改人  	：
修改日  	：
******************************************************************************/
function selectall(listID) {
	var i;
	var obj = document.getElementsByName(listID);
	if (obj) {
		for (i = 0; i < obj.length; i++) {
			if (obj[i].type.toLowerCase() == 'checkbox') {
				if (obj[i].checked == false) {
					obj[i].checked = true;
				}
			}
		}
	}
}
/*****************************************************************************
函数名称	：unselectall
函数功能	：列表对象取消全选 
返回    	：
日期		：2009-06-22
作者    	：sz
修改人  	：
修改日  	：
******************************************************************************/
function unselectall(listID) {
	var i;
	var obj = document.getElementsByName(listID);
	if (obj) {
		for (i = 0; i < obj.length; i++) {
			if (obj[i].type.toLowerCase() == 'checkbox') {
				if (obj[i].checked == true) {
					obj[i].checked = false;
				}
			}
		}
	}
}
/*****************************************************************************
函数名称	：cbxselectall
函数功能	：根据CheckBox 设置是否全部选择
返回    	：
日期		：2009-06-22
作者    	：sz
修改人  	：
修改日  	：
******************************************************************************/
function cbxselectall(obj,listID){
	if(obj.checked){
		selectall(listID);
	}else{
		unselectall(listID);
	}
}
/*****************************************************************************
函数名称	：unselectall
函数功能	：检查复选框是否勾上
返回    	：
日期		：2009-06-22
作者    	：sz
修改人  	：
修改日  	：
******************************************************************************/
function check(vals) {
	var ids = document.getElementsByName(vals);
	var isOK = false;
	for (i = 0; i < ids.length; i++) {
		var obj = ids[i];
		if (obj.checked) {
			isOK = true;
			break;
		}
	}
	if (isOK) {
		return true;
	} else {
		alert("请您先选择要操作的记录！");
		return false;
	}
}
/*****************************************************************************
函数名称	：beforeDelete
函数功能	：删除之前确认框
返回    	：
日期		：2009-06-22
作者    	：sz
修改人  	：
修改日  	：
******************************************************************************/
function beforeDelete(actionName, ids) {
	if (check(ids)) {
		if (confirm('确定要删除当前选中所有项吗？')) {
			document.forms[0].action = actionName;
			document.forms[0].submit();
		}
	}

}


/** 检查输入的内容是否符合长度限制 author: GongRunLin */
function fnLengthCheck(str, max, min) {
	var len = 0;
	for(var i=0; i < str.length; i++) {
		var ech = escape(str.charAt(i));
		if (ech.length > 4)
			len++;
		len++;
	}
	if (len > max || (min && len < min)) {
		return false;
	}
	return true;
}
// 匹配大小写字母、数字、下划线
var _REG_N = /^\w*$/; 
// 匹配大小写字母、数字、下划线、“!@#$”四个特殊字符的四种任意组合8到30位长度
var _REG_P = /^[\w!@#$]*$/; 
/**
 * 密码合法性验证验证
 * author: GongRunLin
 */
function pwdCheck(obj, obj2) {
	if (obj.value == "") {
		var m = new MessageBox(obj);
		m.Show("密码不能为空");
		return false;
	}

	if (!_REG_P.test(obj.value)) {
		var m = new MessageBox(obj);
		m.Show("密码中只能包含大小写字母、数字和“_!@#$”等字符");
		return false;
	}

	if (obj.value != obj2.value) {
		var m = new MessageBox(obj);
		m.Show("两次输入的密码不一样");
		return false;
	}
	return true;
}

/** 用户名合法性验证 author: GongRunLin */
function nameCheck(obj) {
	if (obj.value == "") {
		var m = new MessageBox(obj);
		m.Show("用户登录名不能为空");
		return false;
	}
	if (!_REG_N.test(obj.value)) {
		var m = new MessageBox(obj);
		m.Show("用户名中只能包含大小写字母、数字和“_”等字符");
		return false;
	}
	if (!fnLengthCheck(obj.value, 20)) {
		var m = new MessageBox(obj);
		m.Show("用户名长度不能超过20个字符");
		return false;
	}
	return true;
}
