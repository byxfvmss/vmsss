<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html> 
<head>
<base target="_self">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../page/include.jsp"%>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
<link href="<%=webapp%>/page/subWindow.css" type="text/css" rel="stylesheet">
<title><s:if test="operType=='add' ">新增税控盘注册信息</s:if><s:elseif test="operType=='edit'">编辑税控盘注册信息</s:elseif></title>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script type="text/javascript" src="<%=bopTheme%>/js/validator.js"></script>
<!-- MessageBox -->
<script src="<%=bopTheme%>/js/MessageBox/js/messageBox.js" type="text/javascript"></script>
<link href="<%=bopTheme%>/js/MessageBox/css/messageBox.css" rel="stylesheet" type="text/css" />
<style>
.tbl_text {
  font-size: 12px;
  border: 1px solid #B7B7B7;
  height: 21px;
  width: 250px;
}
.tbl_small_text {
  font-size: 12px;
  border: 1px solid #B7B7B7;
  height: 21px;
  width: 150px;
}
</style>
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
    //税控盘号是否为空
    if(fucCheckNull(document.getElementById("taxDiskNo"),"请输入税控盘号")==false){
       return false;
    }
    //纳税人识别号是否为空
    if(fucCheckNull(document.getElementById("taxpayerNo"),"请选择纳税人识别号")==false){
         return false;
    }
    if(document.getElementById("operType").value=="add" && CountTaxDiskInfo() == false){
		alert("相同的税控盘号和纳税人识别号已存在");
		return false;
	}
    //注册码信息
    if(fucCheckNull(document.getElementById("registeredInfo"),"请输入注册码信息")==false){
         return false;
    }
    
	
    subed=true;
    return true;
}

function submitForm(){

	if(true == checkForm()){
		document.forms[0].action = "<%=webapp%>/saveDiskRegInfo.action";
		document.forms[0].submit();
	}
}
function CountTaxDiskInfo(){
	var countNum = 0;
	$.ajax({
		url: "<%=webapp%>/CountDiskRegInfo.action",
		type: 'POST',
		async:false,
		data:{operType:'<s:property value="operType" />',taxDiskNo:$("#taxDiskNo").val(),taxpayerNo:$("#taxpayerNo").val()},
		error: function(){return false;},
		success: function(result){
			countNum = result;
		}
	});
	if (countNum>0){
		return false;
	}
	return true;
}
</script>
</head>
<body scroll="no" style="overflow:hidden;">
<div class="showBoxDiv">
<form id="frm" action="<c:out value='${webapp}'/>/saveDiskRegInfo.action" method="post">
<div id="editsubpanel" class="editsubpanel">
<div style="overflow: auto; width: 100%;" >  
	<input type="hidden" id="operType" name="operType" value="<s:property value="operType" />" />
	<s:if test='operType=="edit"'>
		<input type="hidden" id="taxDiskNo" name="taxDiskNo" value="<s:property value="diskRegInfo.taxDiskNo" />" />
		<input type="hidden" id="taxpayerNo" name="taxpayerNo" value="<s:property value="diskRegInfo.taxpayerNo" />" />
	</s:if>
<table id="contenttable" class="lessGrid" cellspacing="0"
	width="100%" align="center" cellpadding="0">
	<tr class="header">
		<th colspan="4"><s:if test="operType=='add' ">新增税控盘注册信息</s:if><s:elseif
			test="operType=='edit'">编辑税控盘注册信息</s:elseif></th>
	</tr>
	<tr>
		<td width="30%" style="text-align: right" class="listbar">税控盘号:</td>
		<td width="70%">
			<input id="taxDiskNo" name="taxDiskNo"
			type="text" class="tbl_small_text" value="<s:property value="diskRegInfo.taxDiskNo" />" maxlength="12" <s:if test='operType=="edit"'>disabled="disabled"</s:if>/>
			<span class="spanstar">*</span>
		</td>
	</tr>
	<tr>
		<td width="30%" style="text-align: right" class="listbar">纳税人识别号:</td>
		<td width="70%">
			<s:if test='operType=="edit"'>
			<s:if test="taxperList != null && taxperList.size > 0">
				<s:select name="taxpayerNo" list="taxperList" listKey='taxperNumber' listValue='taxperNumber' headerKey="" headerValue="所有" disabled="true"/>
			</s:if>
			</s:if>
			<s:else>
				<s:select name="taxpayerNo" list="taxperList" listKey='taxperNumber' listValue='taxperNumber' headerKey="" headerValue="所有"/>
			</s:else>
			<span class="spanstar">*</span>
		</td>
	</tr>
	<tr>
		<td width="30%" style="text-align: right" class="listbar">注册码信息:</td>
		<td width="70%">
			<textarea id="registeredInfo" name="registeredInfo"
			 rows="8" cols="35" ><s:property value="diskRegInfo.registeredInfo" /></textarea>
			<span class="spanstar">*</span>
		</td>
	</tr>
</table>
</div>
<div id="ctrlbutton" class="ctrlbutton" style="border:0px">
<s:if test='paperinvoicestock==null || paperinvoicestock.distributeFlag=="0"'>
	<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="submitForm();" name="BtnSave" value="保存" id="BtnSave"/>
</s:if>
	<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="CloseWindow();" name="BtnReturn" value="关闭" id="BtnReturn"/>
</div>

</form>
</div>
</body>

</html>