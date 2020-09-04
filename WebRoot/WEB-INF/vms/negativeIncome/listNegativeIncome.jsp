<!--file: <%=request.getRequestURI()%> -->
<%@ page language="java" import="java.util.*"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.PaginationList"
	import="com.cjit.vms.trans.util.DataUtil"
	import="com.cjit.vms.trans.model.TransInfo"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
		<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
		<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
		<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/search.js"></script>
		<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/window.js"></script>
		<script language="javascript" type="text/javascript" charset="UTF-8"></script>
		<script type="text/javascript">
function submitForm(actionUrl) {
	submitAction(document.forms[0], actionUrl);
	document.forms[0].action="listNegativeIncome.action";
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

function loadCustomerName(customerTaxno) {
	if (customerTaxno == ""){
		document.getElementById("customerName").value = "";
		document.getElementById("customerName").removeAttribute("readOnly");
		return false;
	}
	var webroot = '<%=webapp%>';
	var url = webroot + "/loadCustomerName.action";
	var param = "customerTaxno=" + customerTaxno;
	var result = sendXmlHttpPost(url, param);
	if (result != null){
		document.getElementById("customerName").value = result;
		document.getElementById("customerName").setAttribute("readOnly",'true')
	} 
}

function offsetTrans(actionName) {
	if(checkChkBoxesSelected("selectInstCodes")){
		var chkBoexes= document.getElementsByName("selectInstCodes");
		var customerIds= document.getElementsByName("selectCustomerIds");
		var billingTimes= document.getElementsByName("selectBillingTimes");
		var j = 0;
		var instCode = "";
		var customerId = "";
		var billingTime = "";
		for(i=0;i<chkBoexes.length;i++){
			if(chkBoexes[i].checked){
				j++;
				instCode = chkBoexes[i].value;
				customerId = customerIds[i].value;
				billingTime = billingTimes[i].value;
			}
			if(j>1){
				alert("请选择单条记录进行操作！");
				return false;
			}
		}
		var conStr = "";
		if (actionName == "offsetTrans.action"){
			conStr = "确定冲抵吗？";
		} else {
			conStr = "确定撤回吗？";
		}
		if(!confirm(conStr)){
			return false;
		}
		submitAction(document.forms[0], actionName + "?instCode=" + instCode + "&customerId=" + customerId + "&billingTime=" + billingTime);
		document.forms[0].action="listNegativeIncome.action";
	}else{
		alert("请选择冲抵数据！");
	}
}
</script>
</head>
<body onkeydow="enterkey(event)">
<form name="Form1" method="post" action="listNegativeIncome.action" id="Form1">
<%
	String currMonth = (String) request.getAttribute("currMonth");
%>
<input type="hidden" name="currMonth" id="currMonth" value="<%=currMonth%>"/>
<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
	<tr>
		<td class="centercondition">
			<s:component template="rocketMessage" />
			<div id="tbl_current_status">
				<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> 
				<span class="current_status_menu">当前位置：</span> 
				<span class="current_status_submenu1">销项税管理</span>
				<span class="current_status_submenu">开票管理</span>
				<span class="current_status_submenu">负数收入</span>
			</div>
			<div class="widthauto1">
			<table id="tbl_query" cellpadding="0" cellspacing="0" width="60%"
				border="0">
				<tr>
					<td align="left">机构</td>
					<td>
						<s:if test="authInstList != null && authInstList.size > 0">
							<s:select name="transInfo.instCode" list="authInstList"
								listKey='id' listValue='name' headerKey="" headerValue="全部"
								value='transInfo.instCode' />
						</s:if>
						<s:if test="authInstList == null || authInstList.size == 0">
							<select name="transInfo.instCode" class="readOnlyText">
								<option value="">
									请分配机构权限
								</option>
							</select>
						</s:if>
					</td>
					<td align="left">客户纳税人识别号</td>
					<td>
						<s:if test="customerTaxnoList != null && customerTaxnoList.size > 0">
							<s:select name="transInfo.customerTaxno" list="customerTaxnoList" onchange="loadCustomerName(this.value)"
								listKey='text' listValue='value' headerKey="" headerValue="全部"
								value='transInfo.customerTaxno' />
						</s:if>
						<s:if test="customerTaxnoList == null || customerTaxnoList.size == 0">
							<select name="transInfo.customerTaxno" class="readOnlyText">
								<option value="">
									请添加客户信息
								</option>
							</select>
						</s:if>
					</td>
					<td>
					</td>
				</tr>
				<tr>
					<td align="left">客户纳税人名称</td>
					<td>
						<input type="text" class="tbl_query_text" id="customerName" name="transInfo.customerName" value="<s:property value='transInfo.customerName'/>"/>
					</td>
					<td align="left">账单日期</td>
					<td>
						<input class="tbl_query_time" id="billingBeginTime" type="text"
							name="transInfo.billingBeginTime"
							value="<s:property value='transInfo.billingBeginTime'/>"
							onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'billingEndTime\')}'})"
							size='11' "/>
						--
						<input class="tbl_query_time" id="billingEndTime" type="text"
							name="transInfo.billingEndTime"
							value="<s:property value='transInfo.billingEndTime'/>"
							onfocus="WdatePicker({minDate:'#F{$dp.$D(\'billingBeginTime\')}'})"
							size='11' "/>
					</td>
					<td>
						<input type="button" class="tbl_query_button" value="查询"
							onMouseMove="this.className='tbl_query_button_on'"
							onMouseOut="this.className='tbl_query_button'" name="BtnView"
							id="BtnView" onclick="submitForm('listNegativeIncome.action');" />
					</td>
				</tr>
			</table>
		</div>
		<table id="tbl_tools" width="100%" border="0">
			<tr>
				<td align="left">
				<a href="#" onClick="offsetTrans('offsetTrans.action')" ><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1030.png"/>冲抵</a>
				<a href="#" onClick="offsetTrans('cancelNegativeIncome.action')" ><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1030.png"/>撤回</a>
				<a href="#" onClick="submitForm('exportNegativeIncome.action');"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png"/>导出</a>
				</td>
			</tr>
		</table>
		<!-- <table id="tbl_query" cellpadding="1" cellspacing="0">
				<tr align="left">
					<td>
						<input type="button" class="tbl_query_button"
							onMouseMove="this.className='tbl_query_button_on'"
							onMouseOut="this.className='tbl_query_button'" value="冲抵"
							onClick="offsetTrans()" />
					</td>
					<td>
						<input type="button" class="tbl_query_button"
							onMouseMove="this.className='tbl_query_button_on'"
							onMouseOut="this.className='tbl_query_button'" value="导出"
							onClick="submitForm('exportNegativeIncome.action');" />
					</td>
				</tr>
			</table> -->
  	<div id="lessGridList4" style="overflow: auto; width: 100%;">
		<table class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0" style="border-collapse: collapse; width: 100%;">
					<tr class="lessGrid head">
						<th style="text-align: center">
							<input style="width: 13px; height: 10px;" id="CheckAll" type="checkbox" onclick="cbxselectall(this,'selectInstCodes')" />
						</th>
						<th style="text-align: center">序号</th>
						<th style="text-align: center">机构</th>
						<th style="text-align: center">客户纳税人名称</th>
						<th style="text-align: center">客户纳税人识别号</th>
						<th style="text-align: center">客户类型</th>
						<th style="text-align: center">收入</th>
						<th style="text-align: center">税额</th>
						<th style="text-align: center">金额</th>
						<th style="text-align: center">冲抵成本ENTRY</th>
						<th style="text-align: center">VAT-output GL</th>
						<th style="text-align: center">GHO Class</th>
						<th style="text-align: center">账单日期</th>
						<th style="text-align: center">明细</th>
					</tr>
					<%
						PaginationList paginationList = (PaginationList) request.getAttribute("paginationList");
						if (paginationList != null) {
							List negativeIncomeList = paginationList.getRecordList();
							if (negativeIncomeList != null && negativeIncomeList.size() > 0) {
								for (int i = 0; i < negativeIncomeList.size(); i++) {
									TransInfo trans = (TransInfo) negativeIncomeList.get(i);
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
								<input style="width: 13px; height: 13px;" type="checkbox" name="selectInstCodes" value="<%=trans.getInstCode()%>" />
								<input type="hidden" name="selectCustomerIds" value="<%=trans.getCustomerId() %>"/>
								<input type="hidden" name="selectBillingTimes" value="<%=trans.getBillingTime() %>"/>
							</td>
							<td align="center"><%=i + 1%></td>
							<td align="center"><%=trans.getInstCode()%></td>
							<td align="left"><%=trans.getCustomerName()%></td>
							<td align="center"><%=trans.getCustomerTaxno()%></td>
							<td align="center"><%=DataUtil.getCustomerTypeCH(trans.getCustomerType())%></td>
							<td align="right"><%=NumberUtils.format(trans.getIncome(),"",2)%></td>
							<td align="right"><%=NumberUtils.format(trans.getTaxAmt(),"",2)%></td>
							<td align="right"><%=NumberUtils.format(trans.getAmt(),"",2)%></td>
							<td align="right"><%=NumberUtils.format(trans.getTaxAmt(),"",2)%></td>
							<td align="right"><%=NumberUtils.format(trans.getTaxAmt(),"",2)%></td>
							<td align="center"><%=trans.getGhoClass()==null ? "" : trans.getGhoClass()%></td>
							<td align="center"><%=trans.getBillingTime()==null ? "" : trans.getBillingTime()%></td>
							<td align="center">
								<a href="javascript:void(0);"
									onClick="OpenModalWindowSubmit('viewTransFromNegativeIncome.action?instCode=<%=trans.getInstCode()%>&customerId=<%=trans.getCustomerId() %>&billingTime=<%=trans.getBillingTime() %>',1040,600,true)">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1014.png" title="查看交易明细" style="border-width: 0px;" />
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
			<div id="anpBoud" align="Right" style="overflow: auto; width: 100%;">
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