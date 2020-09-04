<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires",0);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html> 
<head>
<base target="_self">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../page/include.jsp"%>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
<link href="<%=webapp%>/page/subWindow.css" type="text/css" rel="stylesheet">
<title>附加税税种维护-<s:if test="updFlg==0">新增</s:if><s:elseif test="updFlg==1">修改</s:elseif></title>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/search.js"></script>
<script type="text/javascript" src="<%=bopTheme%>/js/validator.js"></script>
<!-- MessageBox -->
<script src="<%=bopTheme%>/js/MessageBox/js/messageBox.js" type="text/javascript"></script>
<link href="<%=bopTheme%>/js/MessageBox/css/messageBox.css" rel="stylesheet" type="text/css" />
	
<script type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript">
//标识页面是否已提交
var subed = false;
function checkForm(){

    //验证是否重复提交
    if (subed == true){
       alert("信息正在发送给服务器，请不要重复提交信息！");
       return false;
    }
    
	// 纳税人识别号
	if(fucCheckNull(document.getElementById("taxPerNumber"),"请选择纳税人识别号")==false){
		return false;
	}

  	// 纳税人名称
    if(fucCheckNull(document.getElementById("taxperName"),"请输入纳税人名称")==false){
       return false;
    }
    
	// 附加税税种
	if(fucCheckNull(document.getElementById("surtaxType"),"请选择附加税税种")==false){
		return false;
	}

  	// 附加税税率
    if(fucCheckNull(document.getElementById("surtaxRate"),"请输入附加税税率")==false){
       return false;
    }
	if(CheckSurtaxRate(document.getElementById("surtaxRate"), "附加税税率必须是大于等于0小于1，保留两位小数")==false){
		return false;
	}

  	// 生效日
    if(fucCheckNull(document.getElementById("surtaxStrDt"),"请输入生效日")==false){
       return false;
    }

  	// 终止日
    if(fucCheckNull(document.getElementById("surtaxEndDt"),"请输入终止日")==false){
       return false;
    }
    
	// 生效日长度10的判断
    if(fucInvoiceCheckLength(document.getElementById("surtaxStrDt"),"10","请输入正确的生效日")==false){
		return false;
	}
    
	// 终止日长度10的判断
    if(fucInvoiceCheckLength(document.getElementById("surtaxEndDt"),"10","请输入正确的终止日")==false){
		return false;
	}

    if(fucCheckDateOrder1(document.getElementById("surtaxStrDt"),document.getElementById("surtaxEndDt"),"终止日必须大于生效日期")==false){
       return false;
    }
    
    if (document.getElementById("updFlg").value == "0"){
		// 附加税税率维护表的存在性check
		if(chkSurtaxInfo(document.getElementById("taxPerNumber").value, document.getElementById("surtaxType").value) == false){
			var obj = document.getElementById("taxPerNumber")
			var m = new MessageBox(obj);
			m.Show("纳税人识别号和附加税税种已经存在，请重新输入。");	
			obj.focus();
			return false;
		}
    }
	
    subed=true;
    return true;
}
function clearNoNum(obj)  
{  
  obj.value = obj.value.replace(/[^\d.]/g,"");  //清除“数字”和“.”以外的字符  
  obj.value = obj.value.replace(/^\./g,"");  //验证第一个字符是数字而不是.  
  obj.value = obj.value.replace(/\.{2,}/g,"."); //只保留第一个. 清除多余的  
  obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");  
  obj.value=obj.value.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3');//只能输入两个小数  
}  
function submitForm(){
	if(true == checkForm()){
		document.forms[0].action = "<%=webapp%>/addOrUpdSurtaxTypeInfo.action";
		document.forms[0].submit();
	}else {
	    subed = false;
	}
}
function CheckSurtaxRate(obj,strAlertMsg){
	strValue=obj.value;
	//if(strValue!="" && ((isNaN(strValue) || Number(strValue) >= 1 || Number(strValue) <= 0)
	//		||(!/^[0-9]+\.[0-9]{2}$/.test(strValue)))){
	if(strValue!="" && (isNaN(strValue) || Number(strValue) >= 1 || Number(strValue) < 0)){
		var m = new MessageBox(obj);
		m.Show(strAlertMsg);
		obj.focus();
		return false;
	}else {
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
function fucCheckDateOrder1(objSDate,objEDate,strMsg)
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
    if(new Date(strSDate).getTime()>=new Date(strEDate).getTime())
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
function chkSurtaxInfo(taxPerNumber, surtaxType){
	var vv = "0";
	$.ajax({url: 'chkSurtaxTypeInfo.action',
	type: 'POST',
	async:false,
	data:{taxPerNumber:taxPerNumber,surtaxType:surtaxType},
	dataType: 'html',
	timeout: 1000,
	error: function(){return false;},
	success: function(result){
		vv = result;
	}
	});
	if (vv == "0"){
		return true;
	}
	return false;
}
function fucInvoiceCheckLength(obj,len,strAlertMsg)
{
	strTemp=obj.value;
	if(strTemp.length!=len)
	{
	  //字符长度不正确
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
</script>
</head>
<body scroll="no" style="overflow:hidden;">
<div class="showBoxDiv">

<form id="frm" action="<c:out value='${webapp}'/>/addOrUpdSurtaxTypeInfo.action" method="post">
<div id="editsubpanel" class="editsubpanel">
<div style="overflow: auto; width: 100%;" >  
	<input type="hidden" name="updFlg" id="updFlg" value="<s:property value="updFlg" />" />
<table id="contenttable" class="lessGrid" cellspacing="0"
	width="100%" align="center" cellpadding="0">
	<tr class="header">
		<th colspan="4">
			<s:if test="updFlg==0">新增</s:if><s:elseif test="updFlg==1">修改</s:elseif>附加税税种维护
		</th>
	</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">纳税人识别号:</td>
		<td width="35%">
				<s:if test="taxperList != null && taxperList.size > 0">
				<s:if test='updFlg==1'>
					<s:select name="taxPerNumber" list="taxperList" listKey='taxperNumber' listValue='taxperNumber' headerKey="" headerValue="请选择" disabled="true"/>
					<input type="hidden" id="taxPerNumber" name="taxPerNumber" value="<s:property value="taxPerNumber"/>" />
				</s:if>
				<s:else>
					<s:select name="taxPerNumber" list="taxperList" listKey='taxperNumber' listValue='taxperNumber' headerKey="" headerValue="请选择" onChange="getAjaxTaxperName(this.value, document.getElementById('taxperName1'));getAjaxTaxperName(this.value, document.getElementById('taxperName'))"/>
				</s:else>
				</s:if>
				<s:if test="taxperList == null || taxperList.size == 0">
				<select name="taxPerNumber" class="readOnlyText">
				<option value="">请分配机构权限</option>
				</select>
				</s:if>
			<span class="spanstar">*</span></td>
		<td width="15%" style="text-align: right" class="listbar">纳税人名称:</td>
		<td width="35%">
			<input id="taxperName1" name="taxperName1" type="text" class="tbl_query_text" value="<s:property value="taxperName" />"  maxlength="20" disabled="disabled"/><span class="spanstar">*</span>
			<input type="hidden" id="taxperName" name="taxperName" value="<s:property value="taxperName"/>" />
		</td>
	</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">附加税税种:</td>
		<td width="35%">
			<s:if test='updFlg==1'>
				<select id="surtaxType1" name="surtaxType1" disabled="disabled">
					<option value="" <s:if test='surtaxType==""'>selected</s:if><s:else></s:else>></option>
					<option value="1" <s:if test='surtaxType=="1"'>selected</s:if><s:else></s:else>>城市建设维护税</option>
					<option value="2" <s:if test='surtaxType=="2"'>selected</s:if><s:else></s:else>>教育费附加税</option>
					<option value="3" <s:if test='surtaxType=="3"'>selected</s:if><s:else></s:else>>地方教育费附加税</option>
					<option value="4" <s:if test='surtaxType=="4"'>selected</s:if><s:else></s:else>>其他地方附加税</option>
				</select>
				<input type="hidden" id="surtaxType" name="surtaxType" value="<s:property value="surtaxType"/>" />
			</s:if>
			<s:else>
				<select id="surtaxType" name="surtaxType">
					<option value="" <s:if test='surtaxType==""'>selected</s:if><s:else></s:else>></option>
					<option value="1" <s:if test='surtaxType=="1"'>selected</s:if><s:else></s:else>>城市建设维护税</option>
					<option value="2" <s:if test='surtaxType=="2"'>selected</s:if><s:else></s:else>>教育费附加税</option>
					<option value="3" <s:if test='surtaxType=="3"'>selected</s:if><s:else></s:else>>地方教育费附加税</option>
					<option value="4" <s:if test='surtaxType=="4"'>selected</s:if><s:else></s:else>>其他地方附加税</option>
				</select>
			</s:else>
			<span class="spanstar">*</span>
		</td>
		<td width="15%" style="text-align: right" class="listbar">附加税税率:</td>
		<td>
			<input id="surtaxRate" name="surtaxRate" type="text" class="tbl_query_text" value="<s:property value="surtaxRate" /><s:if test='surtaxRate==0'>00</s:if><s:elseif test="surtaxRate.length()==3">0</s:elseif>"  maxlength="20" onkeyup="clearNoNum(this)"  style="ime-mode: disabled"/><span class="spanstar">*</span>
		</td>
	</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">生效日:</td>
		<td>
			<input id="surtaxStrDt" name="surtaxStrDt" type="text"  value="<s:property value='surtaxStrDt' />" class="tbl_query_time" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
			<span class="spanstar">*</span>
		</td>
		<td width="15%" style="text-align: right" class="listbar">终止日:</td>
		<td>
			<input id="surtaxEndDt" name="surtaxEndDt" type="text"  value="<s:property value='surtaxEndDt' />" class="tbl_query_time" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
			<span class="spanstar">*</span>
		</td>
	</tr>
</table>
</div>
<div id="ctrlbutton" class="ctrlbutton" style="border:0px">
	<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="submitForm();" name="BtnSave" value="保存" id="BtnSave"/>
<!--	<input type="reset" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" name="BtnReset" value="重置" id="BtnReset"/>-->
	<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="CloseWindow();" name="BtnReturn" value="关闭" id="BtnReturn"/>
</div>

</form>
</div>
</body>

</html>