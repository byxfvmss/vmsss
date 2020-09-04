<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.vms.trans.model.BillInfo"
	import="com.cjit.vms.trans.model.BillItemInfo"
	import="com.cjit.vms.trans.model.TransInfo"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="cjit.crms.util.StringUtil"%>
<%@ include file="../../page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<title>客户票据明细信息</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/editview.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/XmlHttp.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/vms.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/lhb.js"></script>
	<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
	
	<link href="<%=bopTheme2%>/css/editView.css" type="text/css" rel="stylesheet">
	<script language="javascript" type="text/javascript">
		
		function goBack(){
			if (document.getElementById("fromFlag").value == "listTrans"){
				document.getElementById("fromFlag").value = "menu";
				document.forms[0].action = "listTrans.action?paginationList.showCount="+"false"";
			} else if (document.getElementById("fromFlag").value == "listBillModify"){
				document.getElementById("fromFlag").value = "menu";
				document.forms[0].action = "listBillModify.action?flag=modify";
			}
			document.forms[0].submit();
		}
	</script>
</head>
<%
	TransInfo transInfo = (TransInfo) request.getAttribute("transInfo");
%>
<body onkeydow="enterkey(event)" scroll="no" style="overflow:hidden;">
<form name="Form1" method="post" action="mainQuery.action" id="Form1">
	<!--<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
	 <tr>
		<td align="left">
		<s:component template="rocketMessage" />
		<table id="tbl_current_status">
		<tr>
			<td>
				<img src="<%=bopTheme2%>/img/jes/icon/home.png" />
				<span class="current_status_menu">当前位置：</span>
				<span class="current_status_submenu">销项税管理
					<span class="actionIcon">-&gt;</span>综合查询
					<span class="actionIcon">-&gt;</span>交易查询
					<span class="actionIcon">-&gt;</span>查看详情
				</span>
			</td>
		</tr>
		</table>
		</td>
	</tr> -->
	</br>
	<table id="tbl_context" cellspacing="0" width="100%" align="center" cellpadding="0">
		<tr class="row1">
			<td align="right" width="10%"  style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
				客户名称:&nbsp;&nbsp;&nbsp;
			</td>
			<td align="left" width="20%">
				<input type="text" class="tbl_query_text2" value="<s:property value='transInfo.customerName'/>" readonly="readonly"/>
			</td>
			<td align="right" width="10%"  style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
				客户账号:&nbsp;&nbsp;&nbsp;
			</td>
			<td align="left" width="20%">
				<input type="text" class="tbl_query_text2" value="<%=transInfo.getCustomerAccount()==null?"":transInfo.getCustomerAccount() %>" readonly="readonly"/>
			</td>
			<td align="right" width="10%"  style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
				交易时间:&nbsp;&nbsp;&nbsp;
			</td>
			<td align="left" width="20%">
				<input type="text" class="tbl_query_text2" value="<s:property value='transInfo.transDate'/>" readonly="readonly"/>
			</td>
		</tr>
		<tr class="row1">
			<td align="right"  style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
				纳税人类型:&nbsp;&nbsp;&nbsp;
			</td>
			<td align="left">
				<input type="text" class="tbl_query_text2" value="<%=DataUtil.getTaxpayerTypeCH(transInfo.getCustomerTaxPayerType()) %>" readonly="readonly"/>
			</td>
			<td align="right"  style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
				客户纳税人识别号:&nbsp;&nbsp;&nbsp;
			</td>
			<td align="left">
				<input type="text" class="tbl_query_text2" value="<%=transInfo.getCustomerTaxno()==null?"":transInfo.getCustomerTaxno() %>" readonly="readonly"/>
			</td>
			<td align="right"  style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
				交易类型:&nbsp;&nbsp;&nbsp;
			</td>
			<td align="left">
				<input type="text" class="tbl_query_text2" value="<%=transInfo.getTransType() %>" readonly="readonly"/>
			</td>
		</tr>
		<tr class="row1">
			<td align="right"  style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
				交易标志:&nbsp;&nbsp;&nbsp;
			</td>
			<td align="left">
				<input type="text" class="tbl_query_text2" value="<%=DataUtil.getTransFlag(transInfo.getTransFlag()) %>" readonly="readonly"/>
			</td>
			<td align="right"  style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
				是否打票:&nbsp;&nbsp;&nbsp;
			</td>
			<td align="left">
				<input type="text" class="tbl_query_text2" value="<%=DataUtil.getFapiaoFlagCH(transInfo.getTransFapiaoFlag())%>" readonly="readonly"/>
			</td>
			<td align="right"  style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
				状态:&nbsp;&nbsp;&nbsp;
			</td>
			<td align="left">
				<input type="text" class="tbl_query_text2" value="<%=DataUtil.getDataStatusCH(transInfo.getDataStatus(), "TRANS") %>" readonly="readonly"/>
			</td>
		</tr>
		<tr class="row1">
			<td align="right"  style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
				交易金额:&nbsp;&nbsp;&nbsp;
			</td>
			<td align="left">
				<input type="text" class="tbl_query_text2" value="<%=NumberUtils.format(transInfo.getAmt(),"",2) %>" readonly="readonly"/>
			</td>
			<td align="right"  style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
				发票类型:&nbsp;&nbsp;&nbsp;
			</td>
			<td align="left">
				<input type="text" class="tbl_query_text2" value="<%=DataUtil.getFapiaoTypeCH(transInfo.getFapiaoType()) %>" readonly="readonly"/>
			</td>
			<td align="right"  style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
				是否冲账:&nbsp;&nbsp;&nbsp;
			</td>
			<td align="left">
				<input type="text" class="tbl_query_text2" value="<%=DataUtil.getYOrNCH(transInfo.getIsReverse()) %>" readonly="readonly"/>
			</td>
		</tr>
		<tr class="row1">
			<td align="right"  style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
				税率:&nbsp;&nbsp;&nbsp;
			</td>
			<td align="left">
				<input type="text" class="tbl_query_text2" value="<%=NumberUtils.format(transInfo.getTaxRate(),"",4) %>" readonly="readonly"/>
			</td>
			<td align="right"  style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
				税额:&nbsp;&nbsp;&nbsp;
			</td>
			<td align="left">
				<input type="text" class="tbl_query_text2" value="<%=NumberUtils.format(transInfo.getTaxAmt(),"",2)%>" readonly="readonly"/>
			</td>
			<td></td>
			<td></td>
		</tr>
		<tr class="row1">
			<td align="right"  style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
				收入:&nbsp;&nbsp;&nbsp;
			</td>
			<td align="left">
				<input type="text" class="tbl_query_text2" value="<%=NumberUtils.format(transInfo.getIncome(),"",2) %>" readonly="readonly"/>
			</td>
			<td align="right"  style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
				未开票金额:&nbsp;&nbsp;&nbsp;
			</td>
			<td align="left">
				<input type="text" class="tbl_query_text2" value="<%=NumberUtils.format(transInfo.getBalance(),"",2) %>" readonly="readonly"/>
			</td>
			<td></td>
			<td></td>
		</tr>
		<tr class="row1">
			<td align="right"  style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
				价税合计:&nbsp;&nbsp;&nbsp;
			</td>
			<td align="left" colspan="7">
				<input type="text" class="tbl_query_text2" value="<%=NumberUtils.format(transInfo.getAmt(),"",2) %>" readonly="readonly"/>
			</td>
		</tr>
	</table>

<!--	<div style="overflow:auto;width:100%;" id="list1">-->
<!--	<table id="lessGridList" width="100%" class="lessGrid" cellspacing="0" rules="all" border="0" cellpadding="0" display="none" style="border-collapse: collapse;">-->
<!--	<tr>-->
<!--		<td width="15%" align="left" colspan="4">票据信息</td>-->
<!--	</tr>-->
  	<div id="lessGridList8" style="overflow: auto; width: 100%;">
		<table class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0" style="border-collapse: collapse; width: 100%;">
		    <tr class="lessGrid head">
				<th style="text-align:center">序号</th>
				<th style="text-align:center">申请开票日期</th>
				<th style="text-align:center">开票日期</th>
				<th style="text-align:center">客户名称</th>
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
			</tr>
			<%		List billInfoList = (List) request.getAttribute("billInfoList");
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
				<td align="center"><%=i + 1%></td>
				<td align="center"><%=bill.getApplyDate()==null ? "" : bill.getApplyDate()%></td>
				<td align="center"><%=bill.getBillDate()==null ? "" : bill.getBillDate()%></td>
				<td align="left"><%=bill.getCustomerName()%></td>
				<td align="left"><%=bill.getCustomerTaxno()%></td>
				<td align="center"><%=bill.getBillCode()%></td>
				<td align="center"><%=bill.getBillNo()%></td>
				<td align="right"><%=NumberUtils.format(bill.getAmtSum(),"",2)%></td>
				<td align="right"><%=NumberUtils.format(bill.getTaxAmtSum(),"",2)%></td>
				<td align="right"><%=NumberUtils.format(bill.getSumAmt(),"",2)%></td>
				<td align="center"><%=DataUtil.getIsHandiworkCH(bill.getIsHandiwork())%></td>
				<td align="center"><%=DataUtil.getIssueTypeCH(bill.getIssueType())%></td>
				<td align="center"><%=DataUtil.getFapiaoTypeCH(bill.getFapiaoType())%></td>
				<td align="center"><%=DataUtil.getDataStatusCH(bill.getDataStatus(), "BILL")%></td>
			</tr>
			<%
					}
				}
			%>
			</tr>
		</table>
	</div>
	<!-- <div id="ctrlbutton" class="ctrlbutton" style="border:0px">
		<input type="button" class="tbl_query_button" value="关闭"
			onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
			name="btCancel" id="btCancel" onclick="window.close()" />
	</div> -->
	<table id="tbl_tools" width="100%" border="0">
		<tr>
			<td align="left">
			<input type="button" class="tbl_query_button" value="关闭"
			onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
			name="btCancel" id="btCancel" onclick="window.close()" />
			</td>
		</tr>
	</table>
	</form>
</body>
</html>
