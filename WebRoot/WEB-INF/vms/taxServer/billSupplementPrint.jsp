<!--file: <%=request.getRequestURI()%> -->
<%@ page language="java" import="java.util.*" import="java.text.*"
	import="java.math.BigDecimal" import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.util.PaginationList"
	import="com.cjit.vms.trans.model.billInvalid.BillCancelInfo"
	import="com.cjit.vms.trans.model.BillInfo"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../../page/include.jsp"%>
<OBJECT ID=ocxObj CLASSID="clsid:003BD8F2-A6C3-48EF-9B72-ECFD8FC4D49F" codebase="NISEC_SKSCX.ocx#version=1,0,0,1" style='display: none'></OBJECT>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<title>增值税管理平台</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%=bopTheme2%>/css/main.css" type="text/css"
	rel="stylesheet">
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/calendar_notime.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/XmlHttp.js"></script>
<!-- <script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/search.js"></script> -->
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/ylb.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/lhb.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/search.js"></script>
<script type="text/javascript"
	src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>

<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/billinterface/billInterfaceAjax.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/billinterface/bw_servlet.js"></script>

<script type="text/javascript">
var billInterface = new BillInterface();
	billInterface.init({});
	function createBillPrint(){
		var billIds = document.getElementsByName("selectBillIds");
		var  fapiaoType=document.getElementById("billCancelInfo.fapiaoType").value;
		var ids="";
		var t=0;
			for(var i=0;i<billIds.length;i++){
				if (billIds[i].checked){
					ids = ids === "" ? billIds[i].value : ids + "," + billIds[i].value;
					t++;
				}
			}
			if(ids==''){
				alert("请选择票据记录");
				return false;
				
			}
			if(t<1){
				alert("请选择一条票据记录");
				return false;
			}
			
		 billInterface.createBillPrint({ids:ids,fapiaoType:fapiaoType});
	submitAction(document.forms[0],
				"listBillSupplementPrint.action??paginationList.showCount="
						+ "false");	}


	var msg = '<s:property value="message"/>';
	if (msg != null && msg != '') {
		alert(msg);
	}
	// [查询]按钮
	function query() {
		//document.forms[0].submit();
		submitAction(document.forms[0],
				"listBillSupplementPrint.action??paginationList.showCount="
						+ "false");
	}


	


	// 导出按钮
	function exportBillSupplement() {
		submitAction(document.forms[0],
				"billSupplementPrintToExcel.action?paginationList.showCount="
						+ "false");
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
	<form name="Form1" method="post"
		action="listBillSupplementPrint.action?paginationList.showCount=false"
		id="Form1">
		<input type="hidden" name="taxParam" id="taxParam"
			value="<s:property value='taxParam'/>" />
		<%
			String currMonth = (String) request.getAttribute("currMonth");
		%>
		<input type="hidden" name="submitFlag" id="submitFlag" value="" /> <input
			type="hidden" name="currMonth" id="currMonth" value="<%=currMonth%>" />
		<table id="tbl_main" cellpadding="0" cellspacing="0"
			class="tablewh100">
			<tr>
				<td class="centercondition"><s:component
						template="rocketMessage" />
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> <span
							class="current_status_menu">当前位置：</span> <span
							class="current_status_submenu1">销项税管理</span> <span
							class="current_status_submenu">发票管理</span> <span
							class="current_status_submenu">发票补打</span>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" width="100%"
							border="0">
							<tr>
								<td align="left">开票日期</td>
								<td><input class="tbl_query_time" id="billBeginDate"
									type="text" name="billBeginDate"
									value="<s:property value='billBeginDate'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'billEndDate\')}'})"
									size='11' /> -- <input class="tbl_query_time" id="billEndDate"
									type="text" name="billEndDate"
									value="<s:property value='billEndDate'/>"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'billBeginDate\')}'})"
									size='11' /></td>
								<td align="left">客户纳税人名称</td>
								<td><input type="text" class="tbl_query_text"
									name="billCancelInfo.customerName"
									value="<s:property value='billCancelInfo.customerName'/>" /></td>
								<td align="left">是否手工录入</td>
								<td><select id="billCancelInfo.isHandiwork"
									name="billCancelInfo.isHandiwork">
										<option value=""
											<s:if test='billCancelInfo.isHandiwork==""'>selected</s:if>
											<s:else></s:else>>所有</option>
										<s:iterator value="mapillIsHandiWorklist" id="entry">
											<option value="<s:property value="key"/>"
												<s:if test='billCancelInfo.isHandiwork==#entry.key'>selected</s:if>
												<s:else></s:else>>
												<s:property value="value" />
											</option>
										</s:iterator>
								</select></td>
							</tr>
							<tr>

								<td align="left">开具类型</td>
								<td><select name="billCancelInfo.issueType"
									style="width:133px">
										<option value=""
											<s:if test='billCancelInfo.issueType==""'>selected</s:if>
											<s:else></s:else>>全部</option>
										<option value="1"
											<s:if test='billCancelInfo.issueType=="1"'>selected</s:if>
											<s:else></s:else>>单笔</option>
										<option value="2"
											<s:if test='billCancelInfo.issueType=="2"'>selected</s:if>
											<s:else></s:else>>合并</option>
										<option value="3"
											<s:if test='billCancelInfo.issueType=="3"'>selected</s:if>
											<s:else></s:else>>拆分</option>
								</select></td>
								<td align="left">发票类型</td>
								<td><select id="billCancelInfo.fapiaoType"
									name="billCancelInfo.fapiaoType" style="width:133px">
										<option value="0"
											<s:if test='billCancelInfo.fapiaoType=="0"'>selected</s:if>
											<s:else></s:else>>增值税专用发票</option>
										<option value="1"
											<s:if test='billCancelInfo.fapiaoType=="1"'>selected</s:if>
											<s:else></s:else>>增值税普通发票</option>
								</select></td>
								<td align="left">状态</td>
								<td><select id="billCancelInfo.dataStatus"
									name="billCancelInfo.dataStatus" style="width:133px">
										<option value="">全部</option>
										<option value="8"
											<s:if test='billCancelInfo.dataStatus=="8"'>selected</s:if>
											<s:else></s:else>>已打印</option>
								</select></td>
								<s:if test='configCustomerFlag.equals("KBC")'>
								<td align="left">客户号</td>
								<td><input type="text" class="tbl_query_text"
									name="billCancelInfo.customerId"
									value="<s:property value='billCancelInfo.customerId'/>" />
								</td>
								</s:if>
								<td style="width:80px;" align="right"><input type="button"
									class="tbl_query_button" value="查询"
									onMouseMove="this.className='tbl_query_button_on'"
									onMouseOut="this.className='tbl_query_button'" name="BtnView"
									id="BtnView" onclick="query()" /></td>
								<td>
									<!-- <input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" 
				 onMouseOut="this.className='tbl_query_button'" value="打印" onClick="billSupplementPrint()"/> -->
								</td>
								<td>
									<!--<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" 
				 onMouseOut="this.className='tbl_query_button'" value="导出" 
				 onClick="exportBillSupplement()"/> --></td>
							</tr>

						</table>
					</div>
					<table id="tbl_tools" width="100%" border="0">
						<tr>
							<td align="left"><a href="#" onClick="createBillPrint()"><img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1021.png" />打印</a>
								<a href="#" onClick="exportBillSupplement()"><img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />导出</a>
							</td>
						</tr>
					</table>
					<div id="lessGridList4" style="width: 100%; height:360px;overflow: auto;">
						<table class="lessGrid" cellspacing="0" rules="all" border="0"
							cellpadding="0" style="border-collapse: collapse; width: 100%;">
							<tr class="lessGrid head">
								<th style="text-align:center"><input
									style="width:13px;height:13px;" id="CheckAll" type="checkbox"
									onclick="cbxselectall(this,'selectBillIds')" /></th>
								<th style="text-align:center">序号</th>
								<th style="text-align:center">开票日期</th>
								<th style="text-align:center">客户纳税人名称</th>
								<s:if test='configCustomerFlag.equals("KBC")'>
								<th style="text-align:center">客户号</th>
								</s:if>
								<th style="text-align:center">客户纳税人识别号</th>
								<th style="text-align:center">发票代码</th>
								<th style="text-align:center">发票号码</th>
								<th style="text-align:center">合计金额</th>
								<th style="text-align:center">合计税额</th>
								<th style="text-align:center">价税合计</th>
								<th style="text-align:center">是否手工录入</th>
								<th style="text-align:center">开具类型</th>
								<th style="text-align:center">发票类型</th>
								<th style="text-align:center">状态</th>
								<th style="text-align:center">操作</th>
							</tr>
							<%
								PaginationList paginationList = (PaginationList) request
										.getAttribute("paginationList");
								if (paginationList != null) {
									List billCancelInfoList = paginationList.getRecordList();
									if (billCancelInfoList != null && billCancelInfoList.size() > 0) {
										for (int i = 0; i < billCancelInfoList.size(); i++) {
											BillCancelInfo billCancel = (BillCancelInfo) billCancelInfoList
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
								<td align="center"><input style="width:13px;height:13px;"
									type="checkbox" name="selectBillIds"
									value="<%=BeanUtils.getValue(billCancel, "billId")%>" /></td>
								<td align="center"><%=i + 1%></td>
								<td align="center"><%=billCancel.getBillDate() == null ? ""
								: billCancel.getBillDate()%></td>
								<td align="center"><%=billCancel.getCustomerName() == null ? ""
								: billCancel.getCustomerName()%></td>
								<s:if test='configCustomerFlag.equals("KBC")'>
								<td align="center"><%=billCancel.getCustomerId() == null ? ""
								: billCancel.getCustomerId()%></td>
								</s:if>
								<td align="center"><%=billCancel.getCustomerTaxno() == null ? ""
								: billCancel.getCustomerTaxno()%></td>
								<td align="center"><%=billCancel.getBillCode() == null ? ""
								: billCancel.getBillCode()%></td>
								<td align="center"><%=billCancel.getBillNo() == null ? ""
								: billCancel.getBillNo()%></td>
								<td align="center"><%=billCancel.getAmtSum() == null ? ""
								: billCancel.getAmtSum()%></td>
								<td align="center"><%=billCancel.getTaxAmtSum() == null ? ""
								: billCancel.getTaxAmtSum()%></td>
								<td align="center"><%=billCancel.getSumAmt() == null ? ""
								: billCancel.getSumAmt()%></td>
								<td align="center"><%=DataUtil.getIsHandiworkCH(billCancel
								.getIsHandiwork())%></td>
								<td align="center"><%=DataUtil.getIssueTypeCH(billCancel
								.getIssueType())%></td>
								<td align="center"><%=DataUtil.getFapiaoTypeCH(billCancel
								.getFapiaoType())%></td>
								<td align="center"><%=DataUtil.getDataStatusCH(
								billCancel.getDataStatus(), "BILL")%></td>
								<td align="center"><a
									href="seeTransForBillSupplement.action?reqSource=billSupplement&billId=<%=BeanUtils.getValue(billCancel, "billId")%>">
										<img
										src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1014.png"
										title="查看交易" style="border-width: 0px;" /> </a> <a
									href="javascript:void(0);"
									onClick="OpenModalWindowSubmit('viewImgFromBillSupplement.action?billId=<%=billCancel.getBillId()%>',1000,650,true)">
										<img
										src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1015.png"
										title="查看票样" style="border-width: 0px;" /> </a> <a
									href="javascript:void(0);"
									onClick="OpenModalWindowSubmit('seeBillPrintHistory.action?billId=<%=billCancel.getBillId()%>',700,700,true)">
										<img
										src="<c:out value="${bopTheme}"/>/themes/images/icons/info.png"
										title="查看补打记录" style="border-width: 0px;" /> </a></td>
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
								<input type="hidden" name="paginationList.showCount"
									value="false">
								<td align="right"><s:component template="pagediv" />
								</td>
							</tr>
						</table>
					</div></td>
			</tr>
		</table>
	</form>
</body>
</html>