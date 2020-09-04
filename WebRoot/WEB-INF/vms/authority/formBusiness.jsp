<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.vms.trans.model.BillInfo"
	import="com.cjit.vms.trans.model.BillItemInfo"
	import="com.cjit.vms.trans.model.TransInfo"
	import="com.cjit.vms.system.model.Business"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires",0);
%>
<%@ include file="../../../page/modalPage.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<%@ include file="../../../page/include.jsp"%>
	<!-- <title><s:if test="user.userId != null && user.userId != '' ">修改交易种类</s:if><s:else>新增交易种类</s:else></title> -->
	<title>新增交易认定种类</title>
	<link type="text/css" href="<%=bopTheme2%>/css/subWindow.css" rel="stylesheet">
	<link href="<%=bopTheme%>/js/MessageBox/css/messageBox.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="<%=bopTheme%>/js/validator.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/editview.js"></script>
	<script type="text/javascript" src="<%=bopTheme%>/js/MessageBox/js/messageBox.js"></script>
	<script language="javascript" type="text/javascript">
		/* function load(){
			document.getElementById("business.businessCode").focus();
		} */
		//标识页面是否已提交
		var subed = false;
		function findOutSubmit() {		
			if(fucCheckNull(document.getElementById("business.serialNo"),"请输入编号")==false) {		
				return false;
			}
			if(fucCheckNull(document.getElementById("business.math"),"请输入公式")==false) {		
				return false;
			}
			var taxRate = document.getElementById("business.taxRate");
			if(fucCheckNull(taxRate,"请输入税率")==false) {		
				return false;
			}else if(fucIsNoUnsignedFloat(taxRate,"请输入非负数")==false){
				return false;
			}
			if(taxRate.value>1){
				alert("请输入0-1的数字");
				document.getElementById("business.taxRate").focus();
				return false;
			}
			document.formBusiness.action="saveBusiness.action";
				document.formBusiness.method="post";
				document.formBusiness.submit();
				document.getElementById('BtnSave').disabled=true;
			
			//var form = document.getElementById("formBusiness");
			//form.action='saveBusiness.action';
			//form.submit();
			//document.getElementById('BtnSave').disabled=true;
			
		}
		
		function changeMath(){
			var rate = document.getElementById("business.taxRate").value;
			if(rate>1){
				alert("请输入0-1的数字");
				document.getElementById("business.taxRate").focus();
				return false;
			}
			document.getElementById("business.math").value= "(1+"+rate+")*"+rate;
			
		}
</script>  
</head>
<body scroll="no" style="overflow:hidden;">
<div class="showBoxDiv">
<form name="formBusiness" id="formBusiness">
<div id="editsubpanel" class="editsubpanel">
<div style="overflow: auto; width: 100%;">    
<input type="hidden" name="editType" id="editType" value="add" />
<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0" >
	<tr class="header">
		<th colspan="4">
			新增交易种类
		</th>
	</tr>
	<tr>
		<td width="25%" align="right" class="listbar">编号:</td>
		<td width="45%">
				<input id="business.serialNo" name="business.serialNo" type="text" class="tbl_query_text" maxlength="50" value="<s:property value="business.serialNo"/>"/>&nbsp; 
            <span class="spanstar">*</span>
		</td>
	</tr>
	<tr>
		<td align="right" class="listbar">税率:</td>
		<td><input id="business.taxRate" type="text" class="tbl_query_text" name="business.taxRate" maxlength="20" value="<s:property value="business.taxRate"/>" onkeypress="checkkey(value);" onblur="changeMath();" />
		<span class="spanstar">*</span></td>
	</tr>
	<tr>
		<td align="right" class="listbar">公式:</td>
		<td><input id="business.math" type="text" name="business.math" class="tbl_query_text" maxlength="20" value="<s:property value="business.math"/>"/>
		<span class="spanstar">*</span></td>
	</tr>
</table>
</div>
</div>
<div id="ctrlbutton" class="ctrlbutton" style="border:0px">		
	<input type="button" class="tbl_query_button" 
		onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 
		onclick="findOutSubmit()" name="BtnSave" value="保存" id="BtnSave"/>
	<input type="button" class="tbl_query_button" 
		onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 
		onclick="window.close()" name="BtnReturn" value="关闭" id="BtnReturn"/>	
</div>
</form>
</div>
</body>
</html>