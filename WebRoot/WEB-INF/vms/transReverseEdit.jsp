<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.vms.trans.model.BillInfo"
	import="com.cjit.vms.trans.model.TransInfo"
	import="com.cjit.vms.trans.model.TransInfoYS"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<title>增值税管理平台</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/editview.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/ylb.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/XmlHttp.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/vms.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/lhb.js"></script>
	<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
	<link href="<%=bopTheme2%>/css/editView.css" type="text/css" rel="stylesheet">
	<script language="javascript" type="text/javascript">
		function submitForm(actionUrl){
			document.forms[0].action = actionUrl;
			document.forms[0].submit();
			document.forms[0].action = '';
		}
		var issave = false;
		var msg = '<s:property value="message"/>';
		if (msg != ""){
		}
		function reverse1(){
			if(checkChkBoxesSelected("selectTransIds")){
				submitAction(document.forms[0], "reverseTrans.action?oper=reverse");
			}else{
				alert("请选择冲账交易记录");
				return false;
			}
		}
		function reverse(reverseTransId,transId){
			var	statNum = document.getElementById('statNum').value;
			
			if(checkChkBoxesSelected("selectTransIds")){
				if(statNum != 1){
					alert("该交易已在开票处理流程中,不能进行冲账处理!");
					return false;
				}
				submitAction(document.forms[0], "reverseTrans2.action?reverseTransId="+reverseTransId+"&transId="+transId);
			}else{
				alert("请选择冲账交易记录");
				return false;
			}
		}
		function revoke(){
			window.history.back();
		}
	</script>
</head>
<body onmousemove="MM(event)" onmouseout="MO(event)">
	<form name="Form1" method="post" action="transEdit.action" id="Form1">
	<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
	<tr>
		<td class="centercondition">
		<div id="tbl_current_status">
			<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> 
			<span class="current_status_menu">当前位置：</span> 
			<span class="current_status_submenu1">销项税管理</span>
			<span class="current_status_submenu">交易冲账</span>
			<span class="current_status_submenu">交易冲账</span>
			<span class="current_status_submenu">冲账明细</span>
		</div>
		</br>
		<div id="lessGridList" style="overflow: auto; width: 100%;">
		<table class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0" style="border-collapse: collapse; width: 100%;">
<!--		<div id="dataList0" style="overflow:auto;width:100%;">-->
<!--		<table id="lessGridList" width="100%" class="lessGrid" cellspacing="0" rules="all" border="0" cellpadding="0" display="none" style="border-collapse: collapse;">-->
			<tr class="lessGrid head">
			<th style="text-align:center">交易时间</th>
			<th style="text-align:center">客户名称</th>
			<s:if test='configCustomerFlag.equals("KBC")'>
			<th style="text-align: center">客户号</th>
			</s:if>
			<th style="text-align:center">纳税人类型</th>
			<th style="text-align:center">交易类型</th>
			<s:if test='configCustomerFlag.equals("KBC")'>
			<th style="text-align: center">数据来源</th>
			</s:if>
			<th style="text-align:center">交易金额</th>
			<th style="text-align:center">税率</th>
			<th style="text-align:center">税额</th>
			<th style="text-align:center">收入</th>
			<th style="text-align:center">价税合计</th>
			<th style="text-align:center">未开票金额</th>
			<th style="text-align:center">发票类型</th>
			<th style="text-align:center">状态</th>
		</tr>
	<%
		List transEditList = (ArrayList) request.getAttribute("transEditList");
		if (transEditList != null && transEditList.size() > 0){
			for (int i = 0; i < transEditList.size(); i++){
				TransInfoYS trans = (TransInfoYS)transEditList.get(i);
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
			<td align="center"><%=trans.getTransDate()%></td>
			<td align="left"><%=trans.getCustomerName()%></td>
			<s:if test='configCustomerFlag.equals("KBC")'>
			<td align="center"><%=trans.getCustomerId()%></td>
			</s:if>
			<td align="left">
				<%=DataUtil.getTaxpayerTypeCH(trans.getTaxpayerType())%>
			</td>
			<td align="left"><%=trans.getBusinessCname()%></td>
			<s:if test='configCustomerFlag.equals("KBC")'>
			<td align="center"></td>
			</s:if>
			<td align="left"><%=NumberUtils.format(trans.getAmtCny(),"",2)%></td>
			<td align="right"><%=NumberUtils.format(trans.getTaxRate(),"",4)%></td>
			<td align="right"><%=NumberUtils.format(trans.getTaxAmtCny(),"",2)%></td>
			<td align="right"><%=NumberUtils.format(trans.getInComeCny(),"",2)%></td>
			<td align="right"><%=NumberUtils.format((trans.getInComeCny().add(trans.getTaxAmtCny())),"",2)%></td>
			<td align="right"><%=trans.getBalance()%></td>
			<td style="text-align:center"><%=DataUtil.getFapiaoTypeCH(trans.getFapiaoType())%></td>
			<!--状态-->
			<td style="text-align:center"><%=DataUtil.getDataStatusCH(trans.getDataStatus(),"TRANS")%>
				<input id="statNum" type="hidden" name="statNum" value="<%=trans.getDataStatus()%>">
			</td>
			
		</tr>
	<%
			}
		}else{
	%>
		<tr>
			<td colspan="100">
				无记录
			</td>
		</tr>
	<%
		}
	%>
		</table>
		</div>
		<div id="anpBoud" align="Right" style="overflow:auto;width:100%;">
			<table width="100%" cellspacing="0" border="0">
				<tr>
					<td align="right"><s:component template="pagediv"/></td>
				</tr>
			</table>
		</div>
		<table id="tbl_tools" width="100%" border="0">
			<tr>
				<td align="left">
				<input type="button" class="tbl_query_button" value="返回"
						onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
						name="btCancel" id="btCancel" onclick="submitForm('listTransReverse.action')" />							 
				<input type="hidden" name="reverseTransId" value="<s:property value="reverseTransId"/>"> 
				<input type="hidden" name="transId" value="<s:property value="transId"/>"> 
				</td>
			</tr>
		</table>
	</td>
	</tr>
	</table>
	</form>
</body>
</html>
