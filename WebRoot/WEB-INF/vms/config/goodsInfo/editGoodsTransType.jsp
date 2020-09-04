<!--file: <%=request.getRequestURI()%> -->
<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<%@ include file="/page/modalPage.jsp"%>
<%@ include file="/page/include.jsp"%>
<title>商品交易类型</title>
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
				$("#selectedTransType option").attr("selected", "selected");
				submitForm("saveGoodsTransType.action");
			}

		});

		function checkForm() {

			/* var list = $("#contenttable .tbl_query_text2");
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
			 */
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
		<form id="main" method="post" action="saveGoodsTransType.action">
			<div id="editsubpanel" class="editsubpanel">
				<div style="overflow: auto; width: 100%;">
					<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
						<tr class="header">
							<th colspan="4">商品交易类型</th>
							<s:hidden name="goodsInfo.taxNo"></s:hidden>
						</tr>
						<tr>
							<td align="right" class="listbar" style="width:120px">商品编号:</td>
							<td>
								<s:textfield name="goodsInfo.goodsId" cssClass="tbl_query_text_readonly" readonly="true"></s:textfield>
							</td>
						</tr>
						<tr>
							<td align="right" class="listbar">商品名称:</td>
							<td>
								<s:textfield name="goodsInfo.goodsName" cssClass="tbl_query_text_readonly" readonly="true"></s:textfield>
							</td>
						</tr>
						<tr>
							<td align="right" class="listbar">交易类型:</td>
							<td class="listbar">
								<s:optiontransferselect label="交易类型" name="leftSel" leftTitle="可选交易类型" list="leftSel" listKey="transTypeId" listValue="transTypeFullName" multiple="true" headerKey="headerKey"
									emptyOption="false" allowUpDownOnLeft="false" rightTitle="已选交易类型" doubleList="rightSel" doubleListKey="transTypeId" doubleListValue="transTypeFullName" doubleName="selectedTransType"
									doubleHeaderKey="doubleHeaderKey" doubleEmptyOption="false" doubleMultiple="true" allowUpDownOnRight="false" cssStyle="width:200px;height:300px;padding:5px"
									doubleCssStyle="width:200px;height:300px;padding:5px"
								/>
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