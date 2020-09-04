<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/page/include.jsp"%>
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script type="text/javascript" src="<%=bopTheme%>/js/validator.js"></script>
<!-- MessageBox -->
<script src="<%=bopTheme%>/js/MessageBox/js/messageBox.js" type="text/javascript"></script>
<link href="<%=bopTheme%>/js/MessageBox/css/messageBox.css" rel="stylesheet" type="text/css" />
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
<meta http-equiv="Pragma" content="no-cache" />
<title>发票勾稽</title>
<script type="text/javascript">
	function submitForm(actionUrl) {
		document.forms[0].action = actionUrl;
		document.forms[0].submit();
		document.forms[0].action = "billInfoCheckList.action";
	}
	function checkAll(obj, itemName) {
		var inputs = document.getElementsByName(itemName);
		for (var i = 0; i < inputs.length; i++) {
			if (inputs[i].disabled == false) {
				inputs[i].checked = obj.checked;
			} else {
				inputs[i].checked = false;
			}
		}
	}
</script>
</head>
<body>
	<form id="main" action="listBillTransRelate.action" method="post">
		<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
			<tr>
				<td class="centercondition">
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" />
						<span class="current_status_menu">当前位置：</span>
						<span class="current_status_submenu1">销项税管理</span>
						<span class="current_status_submenu">发票勾稽</span>
						<span class="current_status_submenu">发票勾稽</span>
					</div>
					<div>
						<table id="tbl_query" class="widthauto1" cellpadding="0" cellspacing="0" border="0" width="100%">
							<tr>
								<td>申请开票日期</td>
								<td>
									<input id="applyDateStart" name="applyDateStart" type="text" value="<s:property value='applyDateStart' />" class="tbl_query_time"
										onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'applyDateEnd\')}'})"
									/>
									&nbsp;--&nbsp;
									<input id="applyDateEnd" name="applyDateEnd" type="text" value="<s:property value='applyDateEnd' />" class="tbl_query_time" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'applyDateStart\')}'})" />
									&nbsp;&nbsp;&nbsp;
								</td>
								<td>客户号</td>
								<td>
									<s:textfield name="billInfoSearch.customerId" cssClass="tbl_query_text"></s:textfield>
								</td>
								<td>客户名称</td>
								<td>
									<s:textfield name="billInfoSearch.customerName" cssClass="tbl_query_text"></s:textfield>
								</td>
								<td></td>
							</tr>
							<tr>
								<td>发票类型</td>
								<td>
									<%-- <s:set value="@com.cjit.vms.trans.util.DataUtil@getFapiaoTypeList()" id="fapiaoTypeList" />
									<s:select name="billInfoSearch.fapiaoType" list="fapiaoTypeList" listKey="value" listValue="text" headerKey="" headerValue="所有"></s:select>
							 --%>
									<select name="billInfoSearch.fapiaoType">
										<option value="0" <s:if test='billInfoSearch.fapiaoType=="0"'>selected</s:if>>增值税专用发票</option>
										<option value="1" <s:if test='billInfoSearch.fapiaoType=="1"'>selected</s:if>>增值税普通发票</option>
									</select>
								</td>
								<td>勾稽状态</td>
								<td>
									<%-- <s:set value="@com.cjit.vms.trans.util.DataUtil@getBillTransRelateStatusList()" id="relateStatusList" />
									<s:select name="relateStatus" list="relateStatusList" listKey="value" listValue="text"></s:select> --%>
									<select  name="relateStatus">
										<option value="0" <s:if test='relateStatus=="0'>selected</s:if>>未勾稽</option>
										<option value="1" <s:if test='relateStatus=="1"'>selected</s:if>>勾稽中</option>
										<option value="2" <s:if test='relateStatus=="2"'>selected</s:if>>勾稽完成</option>
									</select>
								</td>
								<td></td>
								<td></td>
								<td>
									<input type="button" class="tbl_query_button" onclick="submitForm('listBillTransRelate.action');" name="cmdSelect" value="查询" id="cmdSelect" />
								</td>
							</tr>
						</table>
						<table id="tbl_tools" width="100%" border="0">
							<%-- <tr>
								<td align="left">
									<a href="#" onclick="submitForm('billInfoCheckExportExcel.action');">
										<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />
										导出
									</a>
								</td>
							</tr> --%>
						</table>
						<div id="lessGridList4" style="overflow: auto; width: 100%;">
							<table class="lessGrid" cellspacing="0" rules="all" border="0" cellpadding="0" style="border-collapse: collapse;width: 100%;">
								<tr class="lessGrid head">
									<th width="3%" style="text-align:center">
										<input id="CheckAll" style="width:13px;height:13px;" type="checkbox" onClick="checkAll(this,'selectInfos')" />
									</th>
									<th style="text-align: center">序号</th>
									<th style="text-align: center">申请开票日期</th>
									<th style="text-align: center">开票日期</th>
									<th style="text-align: center">客户名称</th>
									<th style="text-align: center">客户号</th>
									<th style="text-align: center">客户纳税人识别号</th>
									<th style="text-align: center">合计金额</th>
									<th style="text-align: center">合计税额</th>
									<th style="text-align: center">价税合计</th>
									<th style="text-align: center">开具类型</th>
									<th style="text-align: center">发票类型</th>
									<th style="text-align: center">状态</th>
									<th style="text-align: center">操作</th>
								</tr>
								<s:iterator value="paginationList.recordList" id="iList" status="stuts">
									<tr align="center" class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
										<td>
											<input type="checkbox" style="width:13px;height:13px;" name="selectInfos" value="<s:property value="#iList.billId"/>" />
										</td>
										<td align="center">
											<s:property value='#stuts.count' />
										</td>
										<td>
											<s:property value='applyDate' />
										</td>
										<td>
											<s:property value='billDate' />
										</td>
										<td>
											<s:property value='customerName' />
										</td>
										<td>
											<s:property value='customerId' />
										</td>
										<td>
											<s:property value='customerTaxno' />
										</td>
										<td>
											<s:property value='amtSum' />
										</td>
										<td>
											<s:property value='taxAmtSum' />
										</td>
										<td>
											<s:property value='sumAmt' />
										</td>
										<td>
											<s:property value="@com.cjit.vms.trans.util.DataUtil@getIsHandiworkCH(isHandiwork)" />
										</td>
										<td>
											<s:property value="@com.cjit.vms.trans.util.DataUtil@getFapiaoTypeCH(fapiaoType)" />
										</td>
										<td>
											<s:property value="@com.cjit.vms.trans.util.DataUtil@getDataStatusCH(datastatus,'BILL')" />
										</td>
										<td>
											<a href="javascript:void(0)" onClick="OpenModalWindow('billTransRelate.action?relateBillId=<s:property value="#iList.billId" />',1200,730,'view') ">
												<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1005.png" title="发票勾稽" style="border-width: 0px;" />
											</a>
											<%-- <a href="billTransRelate.action?relateBillId=<s:property value="#iList.billId" />" target="_blank">
												<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1005.png" title="发票勾稽" style="border-width: 0px;" />
											</a> --%>
											<a href="javascript:void(0)" onClick="OpenModalWindow('billInfoViewImg.action?billId=<s:property value="#iList.billId" />',900,650,'view') ">
												<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1015.png" title="查看票据" style="border-width: 0px;" />
											</a>
										</td>
									</tr>
								</s:iterator>
							</table>
						</div>
						<div id="anpBoud" align="Right" style="width:100%;">
							<table width="100%" cellspacing="0" border="0">
								<tr>
									<td align="right">
										<s:component template="pagediv" />
									</td>
								</tr>
							</table>
						</div>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
