<!-- file: <%=request.getRequestURI()%> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal" 
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.util.PaginationList"
	import="com.cjit.vms.trans.model.TransInfo"
	import="com.cjit.vms.trans.util.DataUtil"
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
		<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
		<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/search.js"></script>
	</head>
	<body onkeydow="enterkey(event)" scroll="no" style="overflow:hidden;">
	<div class="showBoxDiv">
	<div id="editsubpanel" class="editsubpanel">
	<div style="overflow: auto; width: 100%;" >  
	<form name="Form1" method="post" action="" id="Form1">
	<div class="windowtitle" style="background:#004C7E; height:45px; line-height:45px; text-align:center; color:#FFF; font-size:14px; font-weight:bold;">拆分开票</div>
<!--		<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">-->
<!--			<tr>-->
<!--				<td class="centercondition">-->
<!--					<div id="tbl_current_status">-->
<!--						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> -->
<!--						<span class="current_status_menu">当前位置：</span> -->
<!--						<span class="current_status_submenu1">销项税管理</span>-->
<!--						<span class="current_status_submenu">开票管理</span>-->
<!--						<span class="current_status_submenu">负数收入</span>-->
<!--						<span class="current_status_submenu">查看交易</span>-->
<!--					</div>-->
  	<div id="lessGridList2" style="overflow: auto; width: 100%;">
		<table class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0" style="border-collapse: collapse; width: 100%;">
					<tr class="lessGrid head">
						<th style="text-align: center">序号</th>
						<th style="text-align: center">交易日期</th>
						<th style="text-align: center">客户纳税人名称</th>
						<th style="text-align: center">客户类型</th>
						<th style="text-align: center">交易类型</th>
						<th style="text-align: center">交易金额</th>
						<th style="text-align: center">税率</th>
						<th style="text-align: center">税额</th>
						<th style="text-align: center">收入</th>
						<th style="text-align: center">冲抵成本ENTRY</th>
						<th style="text-align: center">VAT-output GL</th>
						<th style="text-align: center">GHO Class</th>
						<th style="text-align: center">WSID</th>
						<th style="text-align: center">交易账号</th>
						<th style="text-align: center">账单日期</th>
						<th style="text-align: center">状态</th>
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
							<td align="center"><%=i + 1%></td>
							<td align="center"><%=trans.getTransDate()%></td>
							<td align="left"><%=trans.getCustomerName()%></td>
							<td align="center"><%=DataUtil.getCustomerTypeCH(trans.getCustomerType())%></td>
							<td align="center"><%=trans.getTransTypeName()%></td>
							<td align="right"><%=NumberUtils.format(trans.getAmt(), "", 2)%></td>
							<td align="right"><%=NumberUtils.format(trans.getTaxRate(), "", 4)%></td>
							<td align="right"><%=NumberUtils.format(trans.getTaxAmt(), "", 2)%></td>
							<td align="right"><%=NumberUtils.format(trans.getIncome(), "", 2)%></td>
							<td align="right"><%=NumberUtils.format(trans.getTaxAmt(), "", 2)%></td>
							<td align="right"><%=NumberUtils.format(trans.getTaxAmt(), "", 2)%></td>
							<td align="center"><%=trans.getGhoClass()%></td>
							<td align="center"><%=trans.getOrigCapWorkstation()==null ? "" : trans.getOrigCapWorkstation()%></td>
							<td align="center"><%=trans.getAssociateAccountNo()==null ? "" : trans.getAssociateAccountNo()%></td>
							<td align="center"><%=trans.getBillingTime()==null ? "" : trans.getBillingTime()%></td>
							<td align="center"><%=DataUtil.getDataStatusCH(trans.getDataStatus(), "TRANS")%></td>
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
			<table id="tbl_tools" width="100%" border="0">
				<tr>
					<td align="left">
						<input type="button" class="tbl_query_button" value="返回"
							onMouseMove="this.className='tbl_query_button_on'"
							onMouseOut="this.className='tbl_query_button'" name="btCancel"
							id="btCancel" onclick="window.close();" />
					</td>
				</tr>
			</table>
</div>
<!--			</td>-->
<!--			</tr>-->
<!--		</table>-->
		</form>
		</div>
		</div>
	</body>
</html>