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
	$(function() {
		$("#BtnSave").click(function() {
			if (checkForm()) {
				//$("#leftIds option").attr("selected", "selected");
				$("#rightIds option").attr("selected", "selected");
				submitForm("saveGoodConfig.action");
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
			var taxType = $("#taxType option:selected").val();
			var rightIds = $("#rightIds option");
			for (i = 0; i < rightIds.length; i++) {
				var optionVal = $(rightIds[i]).html();
				if (optionVal.indexOf(taxType + " - ") != 0) {
					$(rightIds[i]).attr("selected", "selected");
					alert("请选择于纳税人类型一致的交易类型！");
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
								<s:if test="goodsConfig.goodsNo != null && goodsConfig.goodsNo != '' ">修改商品</s:if>
								<s:else>新增商品</s:else>
								<input type="hidden" id="goodsNo" name="goodsConfig.goodsNo" maxlength="20" value="<s:property value="goodsConfig.goodsNo"/>" />
							</th>
						</tr>
						<tr>
							<td align="right" class="listbar">商品名称:</td>
							<td>
								<input type="text" class="tbl_query_text2" id="goodsName" name="goodsConfig.goodsName" maxlength="50" value="<s:property value="goodsConfig.goodsName"/>" />
								&nbsp;
								<span class="spanstar">*</span>
							</td>
						</tr>
						<tr>
							<td align="right" class="listbar">纳税人类型:</td>
							<td>
								<%-- <input type="text" class="tbl_query_text2" id="taxrate" name="transVerification.taxRate" maxlength="20" value="<s:property value="transVerification.taxRate"/>" /> --%>
								<s:if test="null == goodsConfig||'' == goodsConfig.goodsNo">
								   
									<s:select id="taxType" name="goodsConfig.taxType" list="taxTypeList" listKey='value' listValue='text' style="width:90%"></s:select>
								</s:if>
								<s:if test="null != goodsConfig.goodsNo&&''!= goodsConfig.goodsNo">
									<s:select id="taxType" name="goodsConfig.taxType" list="taxTypeList" listKey='value' listValue='text' style="width:90%" disabled="true"></s:select>
									<input type="hidden" name="goodsConfig.taxType" value="<s:property value='goodsConfig.taxType'/>">
								</s:if>
								&nbsp;
								<span class="spanstar">*</span>
							</td>
						</tr>
						<tr>
							<td align="right" class="listbar">交易类型:</td>
							<td class="listbar">
								<s:optiontransferselect label="交易类型" name="leftIds" leftTitle="可选交易类型" list="transVerificationSelList" listKey="id" listValue="verificationFullName" multiple="true"
									headerKey="headerKey" emptyOption="false" allowUpDownOnLeft="false" cssStyle="width:200px;height:300px;padding:5px" rightTitle="已选交易类型" doubleList="transVerificationList" doubleListKey="id"
									doubleListValue="verificationFullName" doubleName="rightIds" doubleHeaderKey="doubleHeaderKey" doubleEmptyOption="false" doubleMultiple="true" allowUpDownOnRight="false"
									doubleCssStyle="width:200px;height:300px;padding:5px"
								/>
							</td>
						</tr>
						<%-- <tr class="row1">
							<td valign="top" colspan="10">
								<table cellspacing="0" border="0" cellpadding="0" width="100%" style="BORDER:#D6D6D6 1px solid" height="290px;">
									<tr>
										<td>
											<iframe id="subTableFrame1" scrolling="auto" src="getBusitoList.action?taxno=<s:property value="taxno"/>&goodsNo=<s:property value="goodsNo"/>" height="290px" width="100%" frameborder="0"></iframe>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr class="row1">
							<td valign="top" colspan="10">
								<table cellspacing="0" border="0" cellpadding="0" width="100%" style="BORDER:#D6D6D6 1px solid" height="290px;">
									<tr>
										<td>
											<iframe id="subTableFrame2" scrolling="auto" src="getBusitoList.action?taxno=<s:property value="taxno"/>&goodsNo=<s:property value="goodsNo"/>" height="290px" width="100%" frameborder="0"></iframe>
										</td>
									</tr>
								</table>
							</td>
						</tr> --%>
					</table>
				</div>
			</div>
			<div id="ctrlbutton" class="ctrlbutton" style="border:0px">
				<s:if test="!showOnly">
					<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" name="BtnSave" value="保存" id="BtnSave" />
				</s:if>
				<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="window.close()" value="关闭" id="BtnReturn" />
			</div>
		</form>
	</div>
	
</body>
</html>