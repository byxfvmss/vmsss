<!--file: <%=request.getRequestURI()%> -->
<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<%@ include file="../../../page/include.jsp"%>
	<%@ include file="../../../page/modalPage.jsp"%>
	<title>新增交易类型</title>
	<link type="text/css" href="<%=bopTheme2%>/css/subWindow.css" rel="stylesheet">
	<link href="<%=bopTheme%>/js/MessageBox/css/messageBox.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="<%=bopTheme%>/js/validator.js"></script>
	<script type="text/javascript" src="<%=bopTheme%>/js/MessageBox/js/messageBox.js"></script>
	<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/editview.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/calendar_notime.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/search.js"></script>
	<script language="javascript" type="text/javascript">
		$(function() {
			$("#BtnSave").click(function() {
				if (checkForm()) {
					submitForm("saveParamAutoInvoice.action");
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
<form id="main" name="formBusiness" method="post" action="saveParamAutoInvoice.action">
	<div id="editsubpanel" class="editsubpanel">
		<div style="overflow: auto; width: 100%;">
		<input type="hidden" name="editType" id="editType" value="add" />
		<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
		<tr class="header">
			<th colspan="4">
				<s:if test="!createFlag">修改自动开票配置</s:if>
				<s:else>新增自动开票配置</s:else>
				<s:hidden name="createFlag"></s:hidden>
			</th>
		</tr>
		<tr>
			<td align="right" class="listbar" style="width:120px">客户纳税识别号:</td>
			<td>
				<s:if test="createFlag">
				<s:textfield name="autoInvoiceParam.custTaxNo" cssClass="tbl_query_text" maxlength="50"></s:textfield>
				</s:if>
				<s:else>
				<s:textfield name="autoInvoiceParam.custTaxNo" cssClass="tbl_query_text_readonly" readonly="true" ></s:textfield>
				</s:else>
			</td>
		</tr>
		<tr>
			<td align="right" class="listbar">业务种类:</td>
			<td>
				<s:select name="autoInvoiceParam.bussType" list="bussTypeList" listKey='valueStandardNum' listValue='name' />
			</td>
		</tr>
		<tr>
			<td align="right" class="listbar">费用类型:</td>
			<td>
				<s:select name="autoInvoiceParam.costType" list="costTypeList" listKey='valueStandardNum' listValue='name' />
			</td>
		</tr>
		<tr>
			<td align="right" class="listbar">缴费频率:</td>
			<td>
				<s:select name="autoInvoiceParam.payFreq" list="payFreqList" listKey='valueStandardNum' listValue='name' />
			</td>
		</tr>
		<tr>
			<td align="right" class="listbar">发票类型:</td>
			<td>
				<s:select name="autoInvoiceParam.invoiceType" list="invoiceTypeList" listKey='valueStandardNum' listValue='name' />
			</td>
		</tr>
		<tr>
			<td align="right" class="listbar">是否启用周年日:</td>
			<td>
				<s:select name="autoInvoiceParam.weekYearDay" list="wydList" listKey='value' listValue='text' />
			</td>
		</tr>
		<tr>
			<td align="right" class="listbar">年度:</td>
			<td>
				<s:select name="autoInvoiceParam.annual" list="annualList" listKey='valueStandardNum' listValue='name' />
			</td>
		</tr>
		<tr>
			<td align="right" class="listbar">期数:</td>
			<td>
				<s:select name="autoInvoiceParam.periods" list="periodsList" listKey='valueStandardNum' listValue='name' />
			</td>
		</tr>
		<tr>
			<td align="right" class="listbar">特殊标记:</td>
			<td>
				<s:select name="autoInvoiceParam.specialMark" list="specialMarkList" listKey='valueStandardNum' listValue='name'
					headerKey="" headerValue="" />
			</td>
		</tr>
		<tr>
			<td align="right" class="listbar">备注:</td>
			<td>
				<s:textarea name="autoInvoiceParam.remark"></s:textarea>
			</td>
		</tr>
		<!-- 
		<tr>
			<td align="right" class="listbar">生效日期范围:</td>
			<td>
				<input class="tbl_query_time" type="text" id="beginDate" 
					name="autoInvoiceParam.beginDate" value="<s:property value='autoInvoiceParam.beginDate'/>" 
					onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}'})" size='11' />
				 ～ 
				<input class="tbl_query_time" type="text" id="endDate"
					name="autoInvoiceParam.endDate" value="<s:property value='autoInvoiceParam.endDate'/>"
					onfocus="WdatePicker({minDate:'#F{$dp.$D(\'beginDate\')}'})" size='11' />
			</td>
		</tr>
		 -->
		</table>
		</div>
	</div>
	<div id="ctrlbutton" class="ctrlbutton" style="border:0px">
		<s:if test="!showOnly">
			<input type="button" class="tbl_query_button" 
				onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 
				name="BtnSave" value="保存" id="BtnSave" />
		</s:if>
		<input type="button" class="tbl_query_button" 
			onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 
			onclick="window.close()" value="关闭" id="BtnReturn" />
	</div>
</form>
</div>
</body>
</html>