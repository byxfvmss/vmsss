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
<title><s:if test="updFlg==0">新增</s:if><s:elseif test="updFlg==1">修改</s:elseif>税目类型</title>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
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
	if(fucCheckNull(document.getElementById("taxno"),"请选择纳税人识别号")==false){
		return false;
	}

	// 税类
	if(fucCheckNull(document.getElementById("fapiaoType"),"请选择税类")==false){
		return false;
	}
	
  	// 税目编号
  	if (document.getElementById("updFlg").value == "0"){
	    if(fucCheckNull(document.getElementById("taxId"),"请输入税目编号")==false){
	       return false;
	    }
	}
	
  	// 税率
    if(fucCheckNull(document.getElementById("taxRate"),"请输入税率")==false){
       return false;
    }
	if(CheckSurtaxRate(document.getElementById("taxRate"), "税率必须是大于等于0小于0.2的小数")==false){
		return false;
	}
	// 税目管理  税目ID存在性check chkVmsTaxInfoTaxId
	if(chkTaxIdInfo(document.getElementById("taxno").value, document.getElementById("fapiaoType").value, document.getElementById("taxId").value, document.getElementById("updFlg").value) == false){
		var obj = document.getElementById("taxId")
		var m = new MessageBox(obj);
		m.Show("税目编号已经存在，请重新输入。");	
		obj.focus();
		return false;
	}
	
	// 税目管理  税率存在性check
	/**if(chkTaxRateInfo(document.getElementById("taxno").value, document.getElementById("fapiaoType").value, document.getElementById("taxRate").value, document.getElementById("taxId").value, document.getElementById("updFlg").value) == false){
		var obj = document.getElementById("taxno")
		var m = new MessageBox(obj);
		m.Show("相同纳税人识别号的发票类型不能相同，请重新选择。");	
		obj.focus();
		return false;
	}*/
	
    subed=true;
    return true;
}

function submitForm(){
	var taxRate = document.getElementById("taxRate").value; 
	var taxRates = document.getElementById("taxRates").value; 
	if(taxRate!=""&&taxRates!=""&&taxRate==taxRates){
		subed = false;
		alert("修改成功!");
		CloseWindow();
		return;
	}
	if(true == checkForm()){
		document.forms[0].action = "<%=webapp%>/addOrUpdTaxItemInfo.action";
		document.forms[0].submit();
	}else {
	    subed = false;
	}
}
function CheckSurtaxRate(obj,strAlertMsg){
	strValue=obj.value;
	if(strValue!="" && (isNaN(strValue) || Number(strValue) >= 0.2 || Number(strValue) < 0)){
		var m = new MessageBox(obj);
		m.Show(strAlertMsg);
		obj.focus();
		return false;
	}else {
		return true;
	}
}
function chkTaxIdInfo(taxno, fapiaoType, taxId, updFlg){
	var vv = "0";
	$.ajax({url: 'chkVmsTaxInfoTaxId.action',
	type: 'POST',
	async:false,
	data:{taxno:taxno,fapiaoType:fapiaoType,taxId:taxId,updFlg:updFlg},
	dataType: 'html',
	timeout: 1000,
	error: function(){return false;},
	success: function(result){
		vv = result;
	}
	});
	// 0：新增，1：修改
	if (updFlg == "0"){
		if (vv == "0"){
			return true;
		}
	}else if (updFlg == "1"){
		if (vv == "1"){
			return true;
		}
	}
	return false;
}
function chkTaxRateInfo(taxno, fapiaoType, taxRate, taxId, updFlg){
	var vv = "0";
	$.ajax({url: 'chkVmsTaxInfoTaxRate.action',
	type: 'POST',
	async:false,
	data:{taxno:taxno,fapiaoType:fapiaoType,taxRate:taxRate,taxId:taxId,updFlg:updFlg},
	dataType: 'html',
	timeout: 1000,
	error: function(){return false;},
	success: function(result){
		vv = result;
	}
	});
	// 0：新增，1：修改
	if (updFlg == "0"){
		if (vv == "0"){
			return true;
		}
	}else if (updFlg == "1"){
		if (vv == "1"){
			return true;
		}
	}
	return false;
}
</script>
</head>
<body scroll="no" style="overflow:hidden;">
<div class="showBoxDiv">

<form id="frm" action="<c:out value='${webapp}'/>/addOrUpdTaxItemInfo.action" method="post">
<div id="editsubpanel" class="editsubpanel">
<div style="overflow: auto; width: 100%;" >  
	<input type="hidden" name="updFlg" id="updFlg" value="<s:property value="updFlg" />" />
<table id="contenttable" class="lessGrid" cellspacing="0"
	width="100%" align="center" cellpadding="0">
	<tr class="header">
		<th colspan="4">
			<s:if test="updFlg==0">新增</s:if><s:elseif test="updFlg==1">修改</s:elseif>税目类型
		</th>
	</tr>
<%--	<tr>--%>
<%--		<td class="contnettable-subtitle" colspan="10">发票基本信息<s:property value="testv" /></td>--%>
<%--	</tr>--%>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">纳税人识别号:</td>
		<td width="35%">
			<s:if test='updFlg==1'>
<!--				<s:select name="taxnoBak" list="taxperList" listKey='taxperNumber' listValue='taxperNumber' headerKey="" headerValue="请选择" disabled="true" />-->
				<input type="hidden" id="taxno" name="taxno" value="<s:property value="taxno" />" /><s:property value="taxno" />
			</s:if>
			<s:else>
				<s:if test="taxperList != null && taxperList.size > 0">
				<s:select name="taxno" list="taxperList" listKey='taxperNumber' listValue='taxperNumber' headerKey="" headerValue="请选择" />
				</s:if>
				<s:if test="taxperList == null || taxperList.size == 0">
				<select name="taxno" class="readOnlyText">
				<option value="">请分配机构权限</option>
				</select>
				</s:if>
			</s:else>
			<span class="spanstar">*</span></td>
		<td width="15%" style="text-align: right" class="listbar">税类:</td>
		<td width="35%">
			<s:if test='updFlg==1'>
<!--			<select id="fapiaoType1" name="fapiaoType1" disabled="true">-->
<!--				<option value="" <s:if test='fapiaoType==""'>selected</s:if><s:else></s:else>></option>-->
<!--				<option value="0" <s:if test='fapiaoType=="0"'>selected</s:if><s:else></s:else>>增值税专用发票</option>-->
<!--				<option value="1" <s:if test='fapiaoType=="1"'>selected</s:if><s:else></s:else>>增值税普通发票</option>-->
<!--			</select>-->
			<input type="hidden" id="fapiaoType" name="fapiaoType" value="<s:property value="fapiaoType" />" />
			<s:if test='fapiaoType=="0"'>专</s:if><s:else>普</s:else>
			</s:if>
			<s:else>
			<select id="fapiaoType" name="fapiaoType">
				<option value="" <s:if test='fapiaoType==""'>selected</s:if><s:else></s:else>></option>
				<option value="0" <s:if test='fapiaoType=="0"'>selected</s:if><s:else></s:else>>专</option>
				<option value="1" <s:if test='fapiaoType=="1"'>selected</s:if><s:else></s:else>>普</option>
			</select>
			</s:else>
			<span class="spanstar">*</span>
		</td>
	</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">税目编号:</td>
		<td>
			<s:if test='updFlg==1'>
<!--			<input id="taxId1" name="taxId1" type="text" class="tbl_query_text" value="<s:property value="taxId" />"  maxlength="20" style="ime-mode: disabled" disabled="disabled"/><span class="spanstar">*</span>-->
			<input type="hidden" id="taxId" name="taxId" value="<s:property value="taxId" />" />
			<s:property value="taxId" />
			</s:if>
			<s:else>
			<input id="taxId" name="taxId" type="text" class="tbl_query_text" value="<s:property value="taxId" />"  maxlength="20" style="ime-mode: disabled" /><span class="spanstar">*</span>
			</s:else>
		</td>
		<td width="15%" style="text-align: right" class="listbar">税率:</td>
		<td>
			<input id="taxRate" name="taxRate" type="text" class="tbl_query_text" value="<s:property value="taxRate" /><s:if test="taxRate==0">00</s:if><s:elseif test="taxRate.length()==3">0</s:elseif>"  maxlength="4" style="ime-mode: disabled"/><span class="spanstar">*</span>
			<input id="taxRates" name="taxRates" type="hidden" class="tbl_query_text" value="<s:property value="taxRate" /><s:if test="taxRate==0">00</s:if><s:elseif test="taxRate.length()==3">0</s:elseif>"  maxlength="20" style="ime-mode: disabled"/>
		</td>
	</tr>
</table>
</div>
<div id="ctrlbutton" class="ctrlbutton" style="border:0px">
	<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="submitForm();" name="BtnSave" value="保存" id="BtnSave"/>
<!--	<s:if test='updFlg==0'>-->
<!--		<input type="reset" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" name="BtnReset" value="重置" id="BtnReset"/>-->
<!--	</s:if>-->
	<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="CloseWindow();" name="BtnReturn" value="关闭" id="BtnReturn"/>
</div>

</form>
</div>
</body>

</html>
