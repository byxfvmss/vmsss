<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.util.PaginationList"
	import="com.cjit.vms.trans.model.BillInfo"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../page/include.jsp"%>
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
	<script type="text/javascript">
		// [查询]按钮
		function submit(){
			document.forms[0].submit();
		}
		// [废票/红冲]按钮
		function cancel(){
			if(checkChkBoxesSelected("selectBillIds")){
				var billIds = document.Form1.selectBillIds;
				var billDates = document.Form1.selectBillDates;
				var canCancel = true;
				if (!isNaN(billIds.length)){
					for (var i = 0; i < billIds.length; i++){
						if (billIds[i].checked){
							if (document.Form1.elements['submitFlag'].value == '<%=DataUtil.FEIPIAO%>'){
								// 废票处理，要求开票日期在当前月
								if (!billDates[i].value.startWith(document.Form1.elements['currMonth'].value)){
									alert("第" + (i + 1) + "行记录有误：废票处理，应选择开票日期在当前月的记录！");
									canCancel = false;
								}
							}else if (document.Form1.elements['submitFlag'].value == '<%=DataUtil.HONGCHONG%>'){
								// 红冲处理，要求开票日期非当前月
								if (billDates[i].value.startWith(document.Form1.elements['currMonth'].value)){
									alert("第" + (i + 1) + "行记录有误：红冲处理，应选择开票日期早于当前月的记录！");
									canCancel = false;
								}
							}
						}
					}
					if (canCancel == false){
						return false;
					}
				}else{
					if (document.Form1.elements['submitFlag'].value == '<%=DataUtil.FEIPIAO%>'){
						// 废票处理，要求开票日期在当前月
						if (!document.Form1.selectBillDates.value.startWith(document.Form1.elements['currMonth'].value)){
							alert("废票处理，应选择开票日期在当前月的记录！");
							return false;
						}
					}else if (document.Form1.elements['submitFlag'].value == '<%=DataUtil.HONGCHONG%>'){
						// 红冲处理，要求开票日期非当前月
						if (document.Form1.selectBillDates.value.startWith(document.Form1.elements['currMonth'].value)){
							alert("红冲处理，应选择开票日期早于当前月的记录！");
							return false;
						}
					}
				}
				if(!confirm("确定将选中票据进行废票/红冲处理？")){
					return false;
				}
				submitAction(document.forms[0], "cancelBill.action");
			}else{
				alert("请选择交易记录！");
			}
		}
		var msg = '<s:property value="message"/>';
		if (msg != null && msg != ''){
			alert(msg);
		}
	</script>
	<style type="text/css">
		.style1{
			width: 242px;
		}
	</style>
</head>
<body onkeydow="enterkey(event)">
	<form name="Form1" method="post" action="listBillCancel.action" id="Form1">
	<%
		String currMonth = (String) request.getAttribute("currMonth");
	%>
	<input type="hidden" name="submitFlag" id="submitFlag" value=""/>
	<input type="hidden" name="currMonth" id="currMonth" value="<%=currMonth%>"/>
	<table id="tbl_main" cellpadding=0 cellspacing=0>
	<tr>
		<td align="left">
		<s:component template="rocketMessage" />
		<table id="tbl_current_status">
		<tr>
			<td>
				<img src="<%=bopTheme2%>/img/jes/icon/home.png" /> 
				<span class="current_status_menu">当前位置：</span> 
				<span class="current_status_submenu">销项税管理
					<span class="actionIcon">-&gt;</span>发票跟踪
				</span>
			</td>
		</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td align="left">
		<table id="tbl_query" cellpadding="0" cellspacing="0" width="80%" border="0">
		<tr>
			<td align="left">
				开票日期:
			</td>
			<td>
				<input class="tbl_query_time" id="billBeginDate" type="text" name="billBeginDate"
					value="<s:property value='billBeginDate'/>"
					onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'billEndDate\')}'})"
					size='11'"/>
				至
				<input class="tbl_query_time" id="billEndDate" type="text" name="billEndDate"
					value="<s:property value='billEndDate'/>"
					onfocus="WdatePicker({minDate:'#F{$dp.$D(\'billBeginDate\')}'})"
					size='11'"/>
			</td>
			<td align="left">
				发票号码:
			</td>
			<td>
				<input type="text" name="billInfo.billNo" value="<s:property value='billInfo.billNo'/>" />
			</td>
			<td align="left">
				客户纳税人名称:
			</td>
			<td>
				<input type="text" name="billInfo.customerName" value="<s:property value='billInfo.customerName'/>" />
			</td>
			<td align="left">
				客户银行账号:
			</td>
			<td>
				<input type="text" name="billInfo.customerBankandaccount" value="<s:property value='billInfo.customerBankandaccount'/>" />
			</td>
			<td style="width:80px;" align="right">
				<input type="button" class="tbl_query_button" value="查询"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="BtnView" id="BtnView" onclick="submit()" />
			</td>
		</tr>
		</table>
		<table id="tbl_tools" width="100%" border="0">
		<tr>
			<td align="left">
				<input type="button" class="tbl_query_button" value="废票处理"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="BtnView" id="BtnView" onclick="document.getElementById('submitFlag').value='<%=DataUtil.FEIPIAO%>';cancel()" />
				<!-- 
				<input type="button" class="tbl_query_button" value="红冲处理"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="BtnView" id="BtnView" onclick="document.getElementById('submitFlag').value='H';cancel()" /> -->
			</td>
		</tr>
		</table>
		</td>
	</tr>
	</table>
	<div style="overflow:auto;width:100%;" id="list1">
	<table id="lessGridList" width="100%" class="lessGrid" cellspacing="0" rules="all" border="0" cellpadding="0" display="none" style="border-collapse: collapse;">
	<tr class="lessGrid head">
		<th style="text-align:center">
			<input style="width:13px;height:13px;" id="CheckAll" type="checkbox" onclick="cbxselectall(this,'selectBillIds')" />
		</th>
		<th style="text-align:center">红冲</th>
		<th style="text-align:center">票据编号</th>
		<th style="text-align:center">开票日期</th>
		<th style="text-align:center">客户纳税人名称</th>
		<th style="text-align:center">客户纳税人识别号</th>
		<th style="text-align:center">合计金额</th>
		<th style="text-align:center">合计税额</th>
		<th style="text-align:center">价税合计</th>
	</tr>
	<%
		PaginationList paginationList = (PaginationList)request.getAttribute("paginationList");
		if (paginationList != null){
			List billInfoList = paginationList.getRecordList();
			if (billInfoList != null && billInfoList.size() > 0){
				for(int i=0; i<billInfoList.size(); i++){
					BillInfo bill = (BillInfo)billInfoList.get(i);
					if(i%2==0){
	%>
	<tr class="lessGrid rowA">
	<%
					}else{
	%>
	<tr class="lessGrid rowB">
	<%
					}
	%>
		<td align="center">
			<input style="width:13px;height:13px;" type="checkbox" name="selectBillIds" value="<%=BeanUtils.getValue(bill,"billId")%>" />
			<input type="hidden" name="selectBillDates" value="<%=bill.getBillDate()%>"/>
		</td>
		<td align="center">
			<a href="billToWriteoff.action?curPage=<s:property value='paginationList.currentPage'/>&billId=<%=BeanUtils.getValue(bill,"billId")%>">
			<img src ="<%=bopTheme2%>/img/jes/icon/edit.png" title="红冲" style="border-width: 0px;" /></a>
		</td>
		<td align="center"><%=bill.getBillNo()%></td>
		<td align="center"><%=bill.getBillDate()%></td>
		<td align="left"><%=bill.getCustomerName()%></td>
		<td align="left"><%=bill.getCustomerTaxno()%></td>
		<td align="right"><%=NumberUtils.format(bill.getAmtSum(),"",2)%></td>
		<td align="right"><%=NumberUtils.format(bill.getTaxAmtSum(),"",2)%></td>
		<td align="right"><%=NumberUtils.format(bill.getSumAmt(),"",2)%></td>
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
		<td align="right"><s:component template="pagediv"/></td>
	</tr>
	</table>
	</div>
	<script type="text/javascript">
		var msgHight = document.getElementById("anpBoud").offsetHeight;
		document.getElementById("list1").style.height = screenHeight - 440 - msgHight;
	</script>
	<script language="javascript" type="text/javascript" charset="GBK">
	
	</script>
	</form>
</body>
</html>