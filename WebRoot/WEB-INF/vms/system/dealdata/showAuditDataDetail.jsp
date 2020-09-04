<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../../../page/include.jsp"%>
<link href="<%=bopTheme2%>/css/main.css" type="text/css"
	rel="stylesheet">
<script type="text/javascript"
	src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript"
	src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/ylb.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/lhb.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/search.js"></script>
<meta http-equiv="Pragma" content="no-cache" />
<title>数据导入审核</title>
<script type="text/javascript">
	function submitForm(actionUrl) {
		var form = document.getElementById("main");
		form.action = actionUrl;
		form.submit();
	}

	function showMessage(transId) {
		alert($('#message_' + transId).val());
	}
</script>
</head>
<body>
	<form id="main" action="showAuditDataDetail.action" method="post"
		enctype="multipart/form-data">
		<input type="hidden" id="impBatchId" name="impBatchId"
			value="<s:property value='impBatchId' />" /> <input type="hidden"
			id="impStatus" name="impStatus" value="02" />
		<table id="tbl_main" cellpadding="0" cellspacing="0"
			class="tablewh100">
			<tr>
				<td class="centercondition">
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> <span
							class="current_status_menu">当前位置：</span> <span
							class="current_status_submenu1">销项税管理</span> <span
							class="current_status_submenu">开票管理</span> <span
							class="current_status_submenu">数据导入审核</span> <span
							class="current_status_submenu">认定结果</span>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td>交易时间</td>
								<td><input id="startTime" name="startTime" type="text"
									value="<s:property value='startTime' />" class="tbl_query_time"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endTime\')||\'new Date()\'}'})" />
									&nbsp;&nbsp;&nbsp;--&nbsp;&nbsp;&nbsp; <input id="endTime"
									name="endTime" type="text"
									value="<s:property value='endTime' />" class="tbl_query_time"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startTime\')}',maxDate:new Date()})" />
								</td>
								<td>交易金额</td>
								<td><input name="transAmtStart" id="transAmtStart"
									type="text" class="tbl_query_text" style="width: 80;"
									value="<s:property value='transAmtStart' />" />
									&nbsp;&nbsp;--&nbsp;&nbsp; <input name="transAmtEnd"
									id="transAmtEnd" type="text" class="tbl_query_text"
									style="width: 80;" value="<s:property value='transAmtEnd' />" />
								</td>
								<td>客户号</td>
								<td><input name="customerId" id="customerId" type="text"
									class="tbl_query_text" style="width: 150;"
									value="<s:property value='customerId' />" /></td>
								<td style="text-align:left"></td>
							</tr>
							<tr>
								<td>交易类型</td>
								<td><input name="transType" id="transType" type="text"
									class="tbl_query_text" style="width: 150;"
									value="<s:property value='transType' />" /></td>
								<td style="text-align:left"><input type="button"
									class="tbl_query_button"
									onMouseMove="this.className='tbl_query_button_on'"
									onMouseOut="this.className='tbl_query_button'"
									onclick="submitForm('showAuditDataDetail.action');"
									name="cmdFilter" value="查询" id="cmdFilter" /></td>
							</tr>
						</table>
					</div>
					<div id="lessGridList19" style="overflow: auto; width: 100%;">
						<table class="lessGrid" cellspacing="0" rules="all" border="0"
							cellpadding="0" style="border-collapse: collapse;width: 100%;">
							<tr class="lessGrid head">
								<!-- 业务流水号 客户号 交易时间 交易类型 交易金额 是否含税 交易发生机构 发票类型 是否冲账 原始业务流水号 备注 -->
								<th style="text-align: center">序号</th>
								<th style="text-align: center">业务流水号</th>
								<th style="text-align: center">客户号</th>
								<th style="text-align: center">交易时间</th>
								<th style="text-align: center">交易类型</th>
								<th style="text-align: center">交易金额</th>
								<th style="text-align: center">是否含税</th>
								<th style="text-align: center">交易发生机构</th>
								<th style="text-align: center">发票类型</th>
								<th style="text-align: center">是否冲账</th>
								<th style="text-align: center">原始业务流水号</th>
								<th style="text-align: center">备注</th>
								<th style="text-align: center">状态</th>
							</tr>
							<s:iterator value="paginationList.recordList" id="iList"
								status="stuts">
								<tr align="center"
									class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
									<td
										style="<s:if test='impStatus=="03"'>background-color:#FFFF37;</s:if><s:else></s:else>"
										align="center"><s:property value='#stuts.count' /></td>
									<td
										style="<s:if test='impStatus=="03"'>background-color:#FFFF37;</s:if><s:else></s:else>">
										<s:property value='transId' /></td>
									<td
										style="<s:if test='impStatus=="03"'>background-color:#FFFF37;</s:if><s:else></s:else>">
										<s:property value='customerId' /></td>
									<td
										style="<s:if test='impStatus=="03"'>background-color:#FFFF37;</s:if><s:else></s:else>">
										<s:property value='transDate' /></td>
									<td
										style="<s:if test='impStatus=="03"'>background-color:#FFFF37;</s:if><s:else></s:else>">
										<s:property value='transType' /></td>
									<td
										style="<s:if test='impStatus=="03"'>background-color:#FFFF37;</s:if><s:else></s:else>">
										<s:property value='transAmt' /></td>
									<td
										style="<s:if test='impStatus=="03"'>background-color:#FFFF37;</s:if><s:else></s:else>">
										<s:if test='taxFlag=="Y"'>是</s:if> <s:if test='taxFlag=="N"'>否</s:if>
									</td>
									<td
										style="<s:if test='impStatus=="03"'>background-color:#FFFF37;</s:if><s:else></s:else>">
										<s:property value='bankCode' /></td>
									<td
										style="<s:if test='impStatus=="03"'>background-color:#FFFF37;</s:if><s:else></s:else>">
										<s:property value='' /> <%-- <s:if test='fapiaoType=="0"'>专用发票</s:if>
										 <s:if test='fapiaoType=="1"'>普通发票</s:if>  --%> <s:if
											test='fapiaoType=="N"'>专用发票</s:if> <s:if
											test='fapiaoType=="Y"'>普通发票</s:if>
									</td>
									<td
										style="<s:if test='impStatus=="03"'>background-color:#FFFF37;</s:if><s:else></s:else>">
										<s:if test='isReverse=="Y"'>是</s:if> <s:if
											test='isReverse=="N"'>否</s:if></td>
									<td
										style="<s:if test='impStatus=="03"'>background-color:#FFFF37;</s:if><s:else></s:else>">
										<s:property value='reverseTransId' /></td>
									<td
										style="<s:if test='impStatus=="03"'>background-color:#FFFF37;</s:if><s:else></s:else>">
										<s:property value='narrative1' /></td>
									<td
										style="<s:if test='impStatus=="03"'>background-color:#FFFF37;</s:if><s:else></s:else>">
										<input type="hidden"
										id="message_<s:property value='transId' />"
										value="<s:property value='message' />" /> <s:if
											test='impStatus=="01"'>未校验</s:if> <s:if
											test='impStatus=="02"'>通过校验</s:if> <s:if
											test='impStatus=="03"'>
											<a
												href="javascript:showMessage('<s:property value='transId' />');">未通过校验</a>
										</s:if></td>
								</tr>
							</s:iterator>
						</table>
					</div>
					<div id="anpBoud" align="Right" style="width:100%;">
						<table width="100%" cellspacing="0" border="0">
							<tr>
								<td align="right"><s:component template="pagediv" /></td>
							</tr>
						</table>
					</div>
					<div class="ctrlbuttonnew1">
						<input type="button" class="tbl_query_button" value="返回"
							onMouseMove="this.className='tbl_query_button_on'"
							onMouseOut="this.className='tbl_query_button'" name="btCancel"
							id="btCancel"
							onclick="javascript:goToPage('listAuditImpdata.action?statusList=1&statusList=2')" />
					</div></td>
			</tr>
		</table>
	</form>
</body>
</html>