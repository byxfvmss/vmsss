<!--file: <%=request.getRequestURI()%> -->
<%@ page language="java" import="java.util.*" import="java.math.BigDecimal" import="com.opensymphony.util.BeanUtils" import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils" import="com.cjit.vms.trans.model.BillInfo" import="com.cjit.vms.trans.model.BillItemInfo" import="com.cjit.vms.trans.model.TransInfo"
	import="com.cjit.vms.system.model.Business" import="com.cjit.vms.trans.util.DataUtil" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%>
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
<link href="<%=bopTheme%>/js/MessageBox/css/messageBox.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<%=bopTheme%>/js/validator.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/editview.js"></script>
<script type="text/javascript" src="<%=bopTheme%>/js/MessageBox/js/messageBox.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script language="javascript" type="text/javascript">
	/* 	function load() {
	 var id = document.getElementById("businessId").value;
	 if ("" != id) {
	 document.getElementById("businessCode").readOnly = true;
	 document.getElementById("businessCname").readOnly = true;
	 document.getElementById("businessCode").style.cssText = "background-color:#EAEBED";
	 document.getElementById("businessCname").style.cssText = "background-color:#EAEBED";
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
	 */
	$(function() {
		$("#BtnSave").click(function() {
			if (checkForm()) {
				submitForm("saveTransVerification.action");
			}

		});

		function checkForm() {

			var list = $("#contenttable .tbl_query_text2");
			for (i = 0; i < list.length; i++) {
				var msg = $(list[i]).closest("tr").find("td:first").html();
				msg = msg.replace(":", "");
				if (false == fucCheckNull(list[i], msg + "不能为空")) {
					return false;
				}
			}

			return true;

		}

	})

	function submitForm(actionUrl) {
		var form = $("#main");
		var oldAction = form.attr("action");
		form.attr("action", actionUrl);
		form.submit();
		form.attr("action", oldAction);
	}
</script>
</head>
<body scroll="no" style="overflow:hidden;">
	<div class="showBoxDiv">
		<form id="main" name="formBusiness" method="post" action="saveTransVerification.action">
			<div id="editsubpanel" class="editsubpanel">
				<div style="overflow: auto; width: 100%;">
					<input type="hidden" name="editType" id="editType" value="add" />
					<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
						<tr class="header">
							<th colspan="4">
								<s:if test="transVerification.id != null && transVerification.id != '' ">修改交易认定类型</s:if>
								<s:else>新增交易认定类型</s:else>
								<input type="hidden" name="transVerification.id" value="<s:property value="transVerification.id"/>">
							</th>
						</tr>
						<tr>
							<td width="25%" align="right" class="listbar">交易认定类型:</td>
							<td>
								<input type="text" class="tbl_query_text2" id="verificationtype" name="transVerification.verificationType" maxlength="20" value="<s:property value="transVerification.verificationType"/>" />
								&nbsp;
								<span class="spanstar">*</span>
							</td>
						</tr>
						<tr>
							<td align="right" class="listbar">交易名称:</td>
							<td>
								<input type="text" class="tbl_query_text2" id="verificationName" name="transVerification.verificationName" maxlength="20" value="<s:property value="transVerification.verificationName"/>" />
								&nbsp;
								<span class="spanstar">*</span>
							</td>
						</tr>
						<%-- <tr>
							<td align="right" class="listbar">商品编号:</td>
							<td>
								<input type="text" class="tbl_query_text2" id="goodsNo" name="transVerification.goodsNo" maxlength="20" value="<s:property value="transVerification.goodsNo"/>" />
								&nbsp;
								<span class="spanstar">*</span>
							</td>
						</tr>
						<tr>
							<td align="right" class="listbar">商品名称:</td>
							<td>
								<input type="text" class="tbl_query_text2" id="goodsName" name="transVerification.goodsName" maxlength="20" value="<s:property value="transVerification.goodsName"/>" />
								&nbsp;
								<span class="spanstar">*</span>
							</td>
						</tr> --%>
						<tr>
							<td align="right" class="listbar">交易发生地:</td>
							<td>
								<%-- 	<input type="text" class="tbl_query_text2" id="inlandflag" name="transVerification.inlandFlag" maxlength="20" value="<s:property value="transVerification.inlandFlag"/>" /> --%>
								<s:select name="transVerification.inlandFlag" list="inlandFlagList" listKey='value' listValue='text' style="width:90%"></s:select>
								&nbsp;
								<span class="spanstar">*</span>
							</td>
						</tr>
						<tr>
							<td align="right" class="listbar">纳税人类型:</td>
							<td>
								<%-- <input type="text" class="tbl_query_text2" id="taxrate" name="transVerification.taxRate" maxlength="20" value="<s:property value="transVerification.taxRate"/>" /> --%>
								<s:if test="null == transVerification.id||''==transVerification.id">
									<s:select name="transVerification.taxType" list="taxTypeList" listKey='value' listValue='text' style="width:90%"></s:select>
								</s:if>
								<s:else>
									<s:select name="transVerification.taxType" list="taxTypeList" listKey='value' listValue='text' style="width:90%" disabled="true"></s:select>
									<input  type="hidden" name="transVerification.taxType" value="<s:property value='transVerification.taxType'/>" >
								</s:else>
								&nbsp;
								<span class="spanstar">*</span>
							</td>
						</tr>
						<tr>
							<td align="right" class="listbar">税率:</td>
							<td>
								<%-- <input type="text" class="tbl_query_text2" id="taxrate" name="transVerification.taxRate" maxlength="20" value="<s:property value="transVerification.taxRate"/>" /> --%>
								<s:select name="transVerification.taxRate" list="taxRateList" listKey='value' listValue='text' style="width:90%"></s:select>
								&nbsp;
								<span class="spanstar">*</span>
							</td>
						</tr>
						<tr>
							<td align="right" class="listbar">备注:</td>
							<td>
								<s:textarea id="ramark" rows="6" cols="30" cssClass="width:100%" name="transVerification.ramark">
								</s:textarea>
							</td>
						</tr>
					</table>
				</div>
			</div>
			<div id="ctrlbutton" class="ctrlbutton" style="border:0px">
				<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" name="BtnSave" value="保存" id="BtnSave" />
				<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="window.close()" value="关闭" id="BtnReturn" />
			</div>
		</form>
	</div>
</body>
</html>