<!--file: <%=request.getRequestURI()%> -->
<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<%@ include file="/page/modalPage.jsp"%>
<%@ include file="/page/include.jsp"%>
<title>新增交易认定种类</title>
<link type="text/css" href="<%=bopTheme2%>/css/subWindow.css" rel="stylesheet">
<link href="<%=bopTheme%>/js/MessageBox/css/messageBox.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<%=bopTheme%>/js/validator.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/editview.js"></script>
<script type="text/javascript" src="<%=bopTheme%>/js/MessageBox/js/messageBox.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script language="javascript" type="text/javascript">
	$(function() {
		$("#saveBtn").click(function() {
			if (checkForm()) {
				//$("#leftIds option").attr("selected", "selected");
				//$("#rightIds option").attr("selected", "selected");
				submitForm("saveCustomerReceiver.action");
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

			/*var taxType = $("#taxType option:selected").val();
			var rightIds = $("#rightIds option");
			for (i = 0; i < rightIds.length; i++) {
				var optionVal = $(rightIds[i]).html();
				if (optionVal.indexOf(taxType + " - ") != 0) {
					$(rightIds[i]).attr("selected", "selected");
					alert("请选择于纳税人类型一致的交易类型！");
					return false;
				}
			}
			 */
			return true;

		}

		

	})

	function submitForm(actionUrl) {
		var form = $("#main");
		if (!Validator.Validate(form[0], 4)) {
			return false;
		}
		var oldAction = form.attr("action");
		form.attr("action", actionUrl);
		form.submit();
		form.attr("action", oldAction);
	}
</script>
</head>
<body scroll="no" style="overflow:hidden;">
	<div class="showBoxDiv">
		<form id="main" name="formBusiness" method="post" action="saveCustomerReceiver.action">
			<div id="editsubpanel" class="editsubpanel">
				<div style="overflow: auto; width: 100%;">
					<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
						<tr class="header">
							<th colspan="4">
								<s:if test="!createFlag">新增收件人信息</s:if>
								<s:else>修改收件人信息</s:else>
								<s:hidden name="customerReceiver.id"></s:hidden>
								<s:if test="!customerReceiver.customerId">
									<s:hidden name="customerReceiver.customerId" ></s:hidden>
								</s:if>
								<s:else>
									<input type="hidden" name="customerReceiver.customerId" value='<s:property value="customerReceiverSearch.customerId"/>'>
									
								</s:else>
								
							</th>
						</tr>
						<tr>
							<td align="right" class="listbar">姓名:</td>
							<td>
								<s:textfield name="customerReceiver.receiverName" cssClass="tbl_query_text2"></s:textfield>
								<span class="spanstar">*</span>
							</td>
						</tr>
						<tr>
							<td align="right" class="listbar">收件人类型:</td>
							<td>
								<%-- <s:textfield name="customerReceiver.receiverType" cssClass="tbl_query_text2"></s:textfield> --%>
								<s:select name="customerReceiver.receiverType" list="receiverTypeList" listKey="valueStandardLetter" listValue="name" cssClass="tbl_query_text2"></s:select>
								<span class="spanstar">*</span>
							</td>
						</tr>
						<tr>
							<td align="right" class="listbar">证件类型:</td>
							<td>
								<%-- <s:textfield name="customerReceiver.documentsType" cssClass="tbl_query_text2"></s:textfield> --%>
								<s:select name="customerReceiver.documentsType" list="documentsTypeList" listKey="valueStandardLetter" listValue="name" cssClass="tbl_query_text2"></s:select>
								<span class="spanstar">*</span>
							</td>
						</tr>
						
						<tr>
							<td align="right" class="listbar">证件号码:</td>
							<td>
								<s:textfield name="customerReceiver.documentsCode" cssClass="tbl_query_text2"></s:textfield>
								<span class="spanstar">*</span>
							</td>
						</tr>
						
						<tr>
							<td align="right" class="listbar">备注:</td>
							<td>
								<s:textarea name="customerReceiver.remark"></s:textarea>
							</td>
						</tr>
					</table>
				</div>
			</div>
			<div id="ctrlbutton" class="ctrlbutton" style="border:0px">
				<input type="button" class="tbl_query_button" value="保存" id="saveBtn" />
				<input type="button" class="tbl_query_button" onclick="window.close()" value="关闭" id="BtnReturn" />
			</div>
		</form>
	</div>
</body>
</html>