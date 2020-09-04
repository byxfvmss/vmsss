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
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/comm.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/search.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/editview.js"></script>
<script type="text/javascript">
	// [查询]按钮
	function search() {
		document.forms[0].submit();
	}
	// [红冲]按钮
	function redReceipt() {
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
					alert("请选择单条记录进行申请！");
					return false;
				}

			}
			if (!confirm("确定将选中票据进行红冲申请？")) {
				return false;
			}
			submitAction(document.forms[0],
					"inputRedReceiptSpecialApply.action?billId=" + billId);
					document.forms[0].action="listInputRedReceiptApply.action";
			//window.open("inputRedReceiptSpecialApply.action?billId="+billId,'newWindow',screen.width * 0.8,screen.height * 0.8);
		} else {
			alert("请选择交易记录！");
		}
	}

	// [红冲]按钮
	function commit() {
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
					alert("请选择单条记录进行申请！");
					return false;
				}

			}
			if (!confirm("确定将选中票据进行红冲申请？")) {
				return false;
			}
			submitAction(document.forms[0], "commitRedReceipt.action?billId="
					+ billId);
					document.forms[0].action="listInputRedReceiptApply.action";
			//window.open("inputRedReceiptSpecialApply.action?billId="+billId,'newWindow',screen.width * 0.8,screen.height * 0.8);
		} else {
			alert("请选择交易记录！");
		}
	}
	function exportExcel() {
		submitAction(document.forms[0], "exportExcel.action");
		document.forms[0].action = "listInputRedReceiptApply.action";
	}
	var msg = '<s:property value="message" escape="false"/>';
	if (msg != null && msg != '') {
		alert(msg);
	}
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
			document.forms[0].action="listInputRedReceiptApply.action";
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
	<form name="Form1" method="post" action="listInputRedReceiptApply.action" id="Form1">
		<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
			<tr>
				<td class="centercondition">
					<s:component template="rocketMessage" />
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" />
						<span class="current_status_menu">当前位置：</span>
						<span class="current_status_submenu1">进项税管理</span>
						<span class="current_status_submenu">红冲管理</span>
						<span class="current_status_submenu">红冲申请</span>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" width="100%" border="0">
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
									<%-- 	<s:select id="applyInfo.instcode" name="applyInfo.instcode" list="instCodeList" headerKey="" headerValue="全部"
							listKey='id' listValue='name' style="width:135px"/> --%>
								</td>
								<td align="left">供应商名称</td>
								<td>
									<input type="text" class="tbl_query_text" name="applyInfo.vendorName" value="<s:property value='applyInfo.vendorName'/>" maxlength="100" />
								</td>
								<td align="left">数据状态</td>
								<td>
									<s:select id="applyInfo.datastatus" name="applyInfo.datastatus" list="billDataStatusList" headerKey="" headerValue="全部" listKey='value' listValue='text' style="width:135px" />
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
								<td align="right">
									<input type="button" class="tbl_query_button" value="查询" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" name="BtnView" id="BtnView"
										onclick="search()"
									/>
								</td>
						</table>
					</div>
					<table id="tbl_tools" width="100%" border="0">
						<tr>
							<td align="left">
								<a href="#" ame="BtnView" id="BtnView" onclick="redReceipt()">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1033.png" />
									红冲申请
								</a>
								<!--					<input type="button" class="tbl_query_button" value="红冲申请"-->
								<!--						onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"-->
								<!--						name="BtnView" id="BtnView" onclick="redReceipt()" />-->
								<a href="#" name="BtnView" id="BtnView" onclick="commit()">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1016.png" />
									红冲提交
								</a>
								<!--					<input type="button" class="tbl_query_button" value="红冲提交"-->
								<!--						onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"-->
								<!--						name="BtnView" id="BtnView" onclick="commit()" />-->
								<a href="#" name="BtnView" id="BtnView" onclick="exportExcel()">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />
									导出
								</a>
								<!--					<input type="button" class="tbl_query_button" value="导出"-->
								<!--						onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"-->
								<!--						name="BtnView" id="BtnView" onclick="exportExcel()" />-->
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
								</td>
								<td align="center">
									<%=(i + 1)%>
								</td>
								<td align="center"><%=bill.getBillCode() == null ? "" : bill
								.getBillCode()%></td>
								<td align="center"><%=bill.getBillNo() == null ? "" : bill
								.getBillNo()%></td>
								<td align="center"><%=bill.getBillDate() == null ? "" : bill
								.getBillDate()%></td>
								<td align="center"><%=bill.getInstcode() == null ? "" : bill
								.getInstcode()%></td>
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
								<td align="center"><%=bill.getVendorName() == null ? "" : bill
								.getVendorName()%></td>
								<td align="center"><%=bill.getVendorTaxno() == null ? "" : bill
								.getVendorTaxno()%></td>
								<td align="center">
									<%
										if (bill.getDatastatus().equals("3")) {
									%>
									首次认证通过
									<%
										} else if (bill.getDatastatus().equals("5")) {
									%>
									再次认证通过
									<%
										} else if (bill.getDatastatus().equals("7")) {
									%>
									税务局当场认证通过
									<%
										} else if (bill.getDatastatus().equals("10")) {
									%>
									已抵扣
									<%
										} else if (bill.getDatastatus().equals("11")) {
									%>
									不可抵扣
									<%
										}
									%>
								</td>
								<td align="center"><%=NumberUtils.format(bill.getVatOutAmt(), "", 2)%></td>
								<td align="center"><%=bill.getIdentifyDate() == null ? "" : bill
								.getIdentifyDate()%></td>
								<td align="right">
									<a href="viewSpecialApply.action?billId=<%=BeanUtils.getValue(bill, "billId")%>">
										<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1014.png" title="查看数据" style="border-width: 0px;" />
									</a>
									<a href="inputRedReceiptSpecialApply.action?billId=<%=BeanUtils.getValue(bill, "billId")%>">
										<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1005.png" title="编辑数据" style="border-width: 0px;" />
									</a>
									<a href="javascript:void(0);" onClick="OpenModalWindowSubmit('inputApplyListToCancelReason.action?billId=<%=BeanUtils.getValue(bill, "billId")%>',500,350,true)">
										<img src="<c:out value="${bopTheme}"/>/themes/images/icons/info.png" title="查看退回原因" style="border-width: 0px;" />
									</a>
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