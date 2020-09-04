<!--file: <%=request.getRequestURI()%> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal" import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.vms.trans.model.BillInfo"
	import="com.cjit.vms.trans.model.BillItemInfo"
	import="com.cjit.vms.trans.model.TransInfo"
	import="com.cjit.vms.system.model.Business"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
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
<link href="<%=bopTheme%>/js/MessageBox/css/messageBox.css"	rel="stylesheet" type="text/css">
<script type="text/javascript" src="<%=bopTheme%>/js/validator.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/editview.js"></script>
<script type="text/javascript"	src="<%=bopTheme%>/js/MessageBox/js/messageBox.js"></script>
<script language="javascript" type="text/javascript">
	function load() {
		var id = document.getElementById("businessId").value;
		if ("" != id) {
			document.getElementById("businessCode").readOnly = true;
			document.getElementById("businessCname").readOnly = true;
			document.getElementById("businessCode").style.cssText="background-color:#EAEBED";
			document.getElementById("businessCname").style.cssText="background-color:#EAEBED";
		}
	}
	//标识页面是否已提交
	var subed = false;
	var msg = '<s:property value="message" escape="false"/>';
	if (msg != null && msg != '') {
		alert(msg);
	}

	function findOutSubmit() {
		
		if (fucCheckNull(document.getElementById("businessCode"), "交易码不能为空") == false) {
			return false;
		}
		if (fucCheckNull(document.getElementById("businessCName"), "交易名称不能为空") == false) {
			return false;
		}
		if (fucCheckLength(document.getElementById("businessNote"), 200,
				"描述长度请在200字以内") == false) {
			return false;
		}
		document.formBusiness.action = "createBusinessInfo.action";
		document.formBusiness.method = "post";
		document.formBusiness.submit();
		document.getElementById('BtnSave').disabled = true;
	}
</script>
</head>
<body onload="load()" scroll="no" style="overflow:hidden;">
	<div class="showBoxDiv">
		<form name="formBusiness" id="formBusiness">
			<%
				String sss = (String) request.getAttribute("parentCode");
			%>
			<div id="editsubpanel" class="editsubpanel">
				<div style="overflow: auto; width: 100%;">
					<input type="hidden" name="editType" id="editType" value="add" />
					<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
						<tr class="header">
							<th colspan="4"><s:if test="business.businessId != null && business.businessId != '' ">修改交易种类</s:if><s:else>新增交易种类</s:else></th>
						</tr>
						<tr>
							<td width="25%" align="right" class="listbar">交易码:</td>
							<td><s:hidden id="businessId" name="business.businessId"></s:hidden>
<!--								<s:textfield id="businessCode" maxlength="20" name="business.businessCode"></s:textfield>-->
								<input type="text" class="tbl_query_text2" id="businessCode" name="business.businessCode" maxlength="20" value="<s:property value="business.businessCode"/>" />
								&nbsp;<span class="spanstar">*</span></td>
						</tr>
						<tr>
							<td align="right" class="listbar">交易名称:</td>
							<td>
								<input type="text" class="tbl_query_text2" id="businessCName" name="business.businessCName" maxlength="20" value="<s:property value="business.businessCName"/>" />
<!--								<s:textfield id="businessCName" maxlength="20" class="tbl_query_text" name="business.businessCName"></s:textfield>-->
								&nbsp;<span class="spanstar">*</span></td>
						</tr>
						<tr>
							<td align="right" class="listbar">描述:</td>
							<td><s:textarea id="businessNote" rows="10" cols="30" cssClass="width:100%" name="business.businessNote"></s:textarea></td>
						</tr>
					</table>
				</div>
			</div>
			<div id="ctrlbutton" class="ctrlbutton" style="border:0px">
				<input type="button" class="tbl_query_button"
					onMouseMove="this.className='tbl_query_button_on'"
					onMouseOut="this.className='tbl_query_button'"
					onclick="findOutSubmit()" name="BtnSave" value="保存" id="BtnSave" />
				<input type="button" class="tbl_query_button"
					onMouseMove="this.className='tbl_query_button_on'"
					onMouseOut="this.className='tbl_query_button'"
					onclick="window.close()" name="BtnReturn" value="关闭" id="BtnReturn" />
			</div>
		</form>
	</div>
</body>
</html>