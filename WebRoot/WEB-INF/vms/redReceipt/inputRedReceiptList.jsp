<!--file: <%=request.getRequestURI()%> -->
<%@page import="com.cjit.vms.input.model.InputRedReceiptApplyInfo"%>
<%@ page language="java" import="java.util.*" import="java.math.BigDecimal" import="com.opensymphony.util.BeanUtils" import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils" import="com.cjit.common.util.PaginationList" import="com.cjit.vms.trans.model.BillInfo" import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%>
<%@ include file="../../../page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<title>增值税管理平台</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/calendar_notime.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/XmlHttp.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/search.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/ylb.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/lhb.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/search.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/editview.js"></script>
<script type="text/javascript">
	// [查询]按钮
	function submit() {
		document.forms[0].submit();

	}
	// 红冲按钮
	function cancel(result) {
		if (checkChkBoxesSelected("selectBillIds")) {
			var billIds = document.Form1.selectBillIds;
			var billDates = document.Form1.selectBillDates;
			var canCancel = true;

			var chkBoexes = document.getElementsByName("selectBillIds");
			var j = 0;
			var billId = "";
			for (i = 0; i < chkBoexes.length; i++) {
				if (chkBoexes[i].checked) {
					j++;
					billId = chkBoexes[i].value;
				}
				if (j > 1) {
					alert("请选择单条记录进行操作！");
					return false;
				}
			}
			if (!confirm("确定将选中票据进行红冲处理？")) {
				return false;
			}
			submitAction(document.forms[0], "inputRedReceipt.action?billId="
					+ billId + "&result=" + result);
							document.forms[0].action="listInputRedReceipt.action";
			//window.open("redReceiptTransList.action?billId="+billId+"&result="+result,'newwindow',screen.width * 0.8,screen.height * 0.8);
			//
		} else {
			alert("请选择交易记录！");
		}
	}
	var msg = '<s:property value="message" escape="false"/>';
	if (msg != null && msg != '') {
		alert(msg);
	}

	//	查看票样
	function OpenModalWindowSubmit(newURL, width, height, needReload) {
		var retData = false;
		if (typeof (width) == 'undefined') {
			width = screen.width * 0.9;
		}
		if (typeof (height) == 'undefined') {
			height = screen.height * 0.9;
		}
		if (typeof (needReload) == 'undefined') {
			needReload = false;
		}
		retData = showModalDialog(newURL, window, "dialogWidth:" + width
				+ "px;dialogHeight:" + height
				+ "px;center=1;scroll=1;help=0;status=0;");
		if (needReload && retData) {
			window.document.forms[0].submit();
		}
	}
</script>
<style type="text/css">
.style1 {
	width: 242px;
}
</style>
</head>
<body onkeydow="enterkey(event)">
	<form name="Form1" method="post" action="listInputRedReceipt.action" id="Form1">
		<%
			String currMonth = (String) request.getAttribute("currMonth");
		%>
		<input type="hidden" name="submitFlag" id="submitFlag" value="" />
		<input type="hidden" name="currMonth" id="currMonth" value="<%=currMonth%>" />
		<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
			<tr>
				<td class="centercondition">
					<s:component template="rocketMessage" />
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" />
						<span class="current_status_menu">当前位置：</span>
						<span class="current_status_submenu1">进项税管理</span>
						<span class="current_status_submenu">红冲管理</span>
						<span class="current_status_submenu">发票红冲</span>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" width="80%" border="0">
							<tr>
								<td align="left">开票日期</td>
								<td>
									<input class="tbl_query_time" id="billBeginDate" type="text" name="applyInfo.billBeginDate" value="<s:property value='applyInfo.billBeginDate'/>"
										onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'billEndDate\')}'})" size='11'
									"/>
									--
									<input class="tbl_query_time" id="billEndDate" type="text" name="applyInfo.billEndDate" value="<s:property value='applyInfo.billEndDate'/>"
										onfocus="WdatePicker({minDate:'#F{$dp.$D(\'billBeginDate\')}'})" size='11'
									"/>
								</td>
								<td align="left">所属机构</td>
								<td>
									<input type="hidden" id="inst_id" name="applyInfo.instcode" value='<s:property value="applyInfo.instcode"/>'>
									<input type="text" class="tbl_query_text" id="inst_Name" name="applyInfo.instName" value='<s:property value="applyInfo.instName"/>' onclick="setOrg(this);" readonly="readonly">
									<%-- <s:select id="applyInfo.instcode" name="applyInfo.instcode" list="instCodeList" headerKey="" headerValue="全部"
					listKey='id' listValue='name' style="width:135px"/> --%>
								</td>
								<td align="left">供应商名称</td>
								<td>
									<input type="text" class="tbl_query_text" name="applyInfo.vendorName" value="<s:property value='applyInfo.vendorName'/>" maxlength="100" />
									<!-- </td>
				<td align="left">
				发票种类:
			</td>
			<td>
				<select name="applyInfo.fapiaoType" style="width:135px">
					<option value="" <s:if test='applyInfo.fapiaoType==""'>selected</s:if><s:else></s:else>>全部</option>
					<option value="0" <s:if test='applyInfo.fapiaoType=="0"'>selected</s:if><s:else></s:else>>增值税专用发票</option>
					<option value="1" <s:if test='applyInfo.fapiaoType=="1"'>selected</s:if><s:else></s:else>>增值税普通发票</option>
				</select>
			</td> -->
							</tr>
							<tr>
								<td align="left">发票代码</td>
								<td>
									<input type="text" class="tbl_query_text" name="applyInfo.billCode" maxlength="10" value="<s:property value='applyInfo.billCode'/>" onkeypress="checkkey(value);" />
								</td>
								<td align="left">供应商纳税人识别号</td>
								<td>
									<input type="text" class="tbl_query_text" name="applyInfo.vendorTaxno" value="<s:property value='applyInfo.vendorTaxno'/>" maxlength="20" />
								</td>
								<td align="left">发票号码</td>
								<td>
									<input type="text" class="tbl_query_text" name="applyInfo.billNo" maxlength="8" value="<s:property value='applyInfo.billNo'/>" onkeypress="checkkey(value);" />
								</td>
								<td></td>
								<td style="width:80px;" align="right">
									<input type="button" class="tbl_query_button" value="查询" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" name="BtnView" id="BtnView"
										onclick="submit()"
									/>
								</td>
							</tr>
						</table>
					</div>
					<table id="tbl_tools" width="100%" border="0">
						<tr>
							<td align="left">
								<a href="#" name="BtnView" id="BtnView" onclick="cancel(14)">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1033.png" />
									红冲
								</a>
								<a href="#" name="BtnView" id="BtnView" onclick="cancel(18)">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1032.png" />
									红冲撤回
								</a>
								<!--					<input type="button" class="tbl_query_button" value="红冲"-->
								<!--						onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"-->
								<!--						name="BtnView" id="BtnView" onclick="cancel(14)" />-->
								<!--					<input type="button" class="tbl_query_button" value="红冲撤回"-->
								<!--						onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"-->
								<!--						name="BtnView" id="BtnView" onclick="cancel(18)" />-->
							</td>
						</tr>
					</table>
					<div id="lessGridList4" style="overflow: auto; width: 100%;">
						<table class="lessGrid" cellspacing="0" rules="all" border="0" cellpadding="0" style="border-collapse: collapse; width: 100%;">
							<tr class="lessGrid head">
								<th style="text-align:center">
									<input style="width:13px;height:13px;" id="CheckAll" type="checkbox" onclick="cbxselectall(this,'selectBillIds')" />
								</th>
								<th style="text-align:center">序号</th>
								<th style="text-align:center">发票代码</th>
								<th style="text-align:center">发票号码</th>
								<th style="text-align:center">开票日期</th>
								<th style="text-align:center">所属机构</th>
								<th style="text-align:center">金额</th>
								<th style="text-align:center">税额</th>
								<th style="text-align:center">发票种类</th>
								<th style="text-align:center">供应商名称</th>
								<th style="text-align:center">供应商纳税人识别号</th>
								<th style="text-align:center">发票状态</th>
								<th style="text-align:center">转出金额</th>
								<th style="text-align:center">认证日期</th>
								<th style="text-align:center">操作</th>
							</tr>
							<%
								PaginationList paginationList = (PaginationList) request
										.getAttribute("paginationList");
								if (paginationList != null) {
									List billInfoList = paginationList.getRecordList();
									if (billInfoList != null && billInfoList.size() > 0) {
										for (int i = 0; i < billInfoList.size(); i++) {
											InputRedReceiptApplyInfo bill = (InputRedReceiptApplyInfo) billInfoList
													.get(i);
											if (i % 2 == 0) {
							%>
							<tr class="lessGrid rowA">
								<%
									} else {
								%>
							
							<tr class="lessGrid rowB">
								<%
									}
								%>
								<td align="center">
									<input style="width:13px;height:13px;" type="checkbox" name="selectBillIds" value="<%=BeanUtils.getValue(bill, "billId")%>" />
									<input type="hidden" name="selectBillDates" value="<%=bill.getBillDate()%>" />
								</td>
								<td align="center">
									<%=(i + 1)%>
								</td>
								<td align="center"><%=bill.getBillCode()%></td>
								<td align="center"><%=bill.getBillNo()%></td>
								<td align="center"><%=bill.getBillDate()%></td>
								<td align="center"><%=bill.getInstcode()%></td>
								<td align="center"><%=NumberUtils.format(bill.getAmtSum(), "", 2)%></td>
								<td align="center"><%=NumberUtils.format(bill.getTaxAmtSum(), "", 2)%></td>
								<td align="center">
									<%
										if (bill.getFapiaoType().equals("0")) {
									%>
									专票
									<%
										} else {
									%>
									普票
									<%
										}
									%>
								</td>
								<td align="center"><%=bill.getVendorName()%></td>
								<td align="center"><%=bill.getVendorTaxno()%></td>
								<td align="center">红冲已审核</td>
								<td align="center"><%=bill.getVatOutAmt()%></td>
								<td align="center"><%=bill.getIdentifyDate()%></td>
								<td align="right">
									<a href="viewSpecialReceipt.action?billId=<%=BeanUtils.getValue(bill, "billId")%>">
										<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1014.png" title="查看数据" style="border-width: 0px;" />
										<!-- 
					<a href="">
					<img src ="<c:out value="${bopTheme}"/>/themes/images/icons/icon1015.png" title="查看票样" style="border-width: 0px;" />
					 -->
										<a href="javascript:void(0);" onClick="OpenModalWindowSubmit('viewImgFromBillEdit1.action?billId=<%=bill.getBillId()%>',1000,650,true)">
											<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1015.png" title="查看票样" style="border-width: 0px;" />
										</a>
								</td>
							</tr>
							<%
								}
									}
								}
							%>
							</tr>
						</table>
					</div>
					<div id="anpBoud" align="Right" style="overflow:auto;width:100%;">
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
	<c:import url="${webapp}/page/webctrl/instTree/tree_include.jsp" charEncoding="UTF-8">
		<c:param name="treeType" value="radio" />
		<c:param name="bankName_tree" value="inst_Name" />
		<c:param name="bankId_tree" value="inst_id" />
		<c:param name="taskId_tree" value="" />
		<c:param name="task_tree" value="task_tree" />
		<c:param name="webapp" value="${webapp}" />
	</c:import>
</body>
</html>