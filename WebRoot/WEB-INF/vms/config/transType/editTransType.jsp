<!--file: <%=request.getRequestURI()%> -->
<%@ page language="java" pageEncoding="UTF-8"
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<%@ include file="/page/modalPage.jsp"%>
<%@ include file="/page/include.jsp"%>
<title>新增交易类型</title>
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
				//$("#rightIds option").attr("selected", "selected");
				submitForm("saveTransType.action");
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
			/* 
			if(false==fucIsUnsignedInteger($("#itemCode")[0],"科目号不合法")){
				return false;
			} */
		/*	var taxType = $("#taxType option:selected").val();
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
		<form id="main" name="formBusiness" method="post" action="saveTransType.action">
			<div id="editsubpanel" class="editsubpanel">
				<div style="overflow: auto; width: 100%;">
					<input type="hidden" name="editType" id="editType" value="add" />
					<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
						<tr class="header">
							<th colspan="4">
								<s:if test="!createFlag">修改交易类型</s:if>
								<s:else>新增交易类型</s:else>
								<s:hidden name="createFlag"></s:hidden>
							</th>
						</tr>
						<tr>
							<td align="right" class="listbar" style="width:120px" >交易类型编号:</td>
							<td>
								<s:if test="createFlag">
									<s:textfield id="itemCode" name = "transTypeCondition.transTypeId" cssClass="tbl_query_text2"  dataType="Custom" regexp="^[a-zA-Z\d]+$" maxlength="50" msg="编号只能包含数字和字母"></s:textfield>
								</s:if>
								<s:else>
									<s:textfield id="itemCode" name = "transTypeCondition.transTypeId" cssClass="tbl_query_text_readonly" readonly="true" ></s:textfield>
								</s:else>
								<span class="spanstar">*</span>
							</td>
						</tr>
						<tr>
							<td align="right" class="listbar">交易类型名称:</td>
							<td>
								<s:textfield name = "transTypeCondition.transTypeName" cssClass="tbl_query_text2" maxlength="50"></s:textfield>
								<span class="spanstar">*</span>
							</td>
						</tr>
						<%-- <tr>
							<td align="right" class="listbar">纳税人识别编号:</td>
							<td>
								<s:textfield name="transTypeCondition.taxNo" cssClass="tbl_query_text2"></s:textfield>
								<span class="spanstar">*</span>
							</td>
						</tr> 
						<tr>
							<td align="right" class="listbar">科目税率:</td>
							<td>
								<s:select name="transTypeCondition.taxRate" list="taxRateSelList" listKey='value' listValue='text' cssClass="tbl_query_text1"></s:select>
								<span class="spanstar">*</span>
							</td>
						</tr>--%>
						<tr>
							<td align="right" class="listbar">备注:</td>
							<td>
								<s:textarea name="transTypeCondition.remark"></s:textarea>
							</td>
						</tr>
						
						
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