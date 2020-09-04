<!--file: <%=request.getRequestURI()%> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal" import="com.opensymphony.util.BeanUtils"
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
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/editview.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/ylb.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/XmlHttp.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/vms.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/lhb.js"></script>
<link href="<%=bopTheme2%>/css/main.css" type="text/css"
	rel="stylesheet">
<link href="<%=bopTheme2%>/css/editView.css" type="text/css"
	rel="stylesheet">
<script language="javascript" type="text/javascript">
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
		function reverse(transId){
			transId = document.getElementById("transId").value;
			var transSelectedId;
			var	statNum ;
			if(checkChkBoxesSelected("selectTransIds")){
				var transSelectedIds = document.getElementsByName("selectTransIds")
				var statNums = document.getElementsByName("statNums")
				var j = 0;
				for(var i = 0; i< transSelectedIds.length;i++){
					if(transSelectedIds[i].checked){
						transSelectedId =	transSelectedIds[i].value;
						statNum = statNums[i].value;
						j++;
					}if(j>1){
						alert("请选择单条记录进行冲账！");
						return false;
					}
				}
				if(statNum != 1){
					alert("该交易已在开票处理流程中,不能进行冲账处理!");
					return false;
				}
				submitAction(document.forms[0], "reverseTrans2.action?transSelectedId="+transSelectedId+"&transId="+transId);
			}else{
				alert("请选择冲账交易记录");
				return false;
			}
		}
		function revoke(){
			submitAction(document.forms[0], "listTransReverse.action?fromFlag=list");
		}
		function selectList(){
			submitAction(document.forms[0], "transToReverse1.action?fromFlag=list2");
		}
	</script>
</head>

<body onLoad="javascript:tips()" onmousemove="MM(event)"
	onmouseout="MO(event)">
<form name="Form1" method="post" action="transToReverse1.action" id="Form1">
	<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
		<tr>
			<td class="centercondition">
			<div id="tbl_current_status">
				<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> 
				<span class="current_status_menu">当前位置：</span> 
				<span class="current_status_submenu1">销项税管理</span>
				<span class="current_status_submenu">交易冲账</span>
				<span class="current_status_submenu">交易冲账</span>
				<span class="current_status_submenu">执行冲账</span>
			</div>
			</br>
			<div style="overflow: auto; width: 100%; height: 100px">
				<table class="lessGrid" cellspacing="0"
					rules="all" border="1" display="none" width="100%">
					<tr class="lessGrid head">
						<th style="text-align: center">交易时间</th>
						<th style="text-align: center">客户名称</th>
						<th style="text-align: center">纳税人类型</th>
						<th style="text-align: center">交易类型</th>
						<s:if test='configCustomerFlag.equals("KBC")'>
						<th style="text-align: center">数据来源</th>
						</s:if>
						<th style="text-align: center">交易金额</th>
						<th style="text-align: center">税率</th>
						<th style="text-align: center">税额</th>
						<th style="text-align: center">收入</th>
						<th style="text-align: center">价税合计</th>
						<th style="text-align: center">未开票金额</th>
						<th style="text-align: center">发票类型</th>
						<th style="text-align: center">状态</th>
					</tr>
					<%
						List transEditList = (ArrayList) request
								.getAttribute("transEditList");
						if (transEditList != null && transEditList.size() > 0) {
							for (int i = 0; i < transEditList.size(); i++) {
								TransInfoYS trans = (TransInfoYS) transEditList.get(i);
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
						<td align="center"><%=trans.getTransDate()%></td>
						<td align="center"><%=trans.getCustomerName()%></td>
						<td align="center"><%=DataUtil.getTaxpayerTypeCH(trans
					.getTaxpayerType())%></td>
						<td align="center"><%=trans.getBusinessCname()%></td>
						<s:if test='configCustomerFlag.equals("KBC")'>
						<td align="center"></td>
						</s:if>
						<td align="center"><%=NumberUtils.format(trans.getAmtCny(), "", 2)%></td>
						<td align="right"><%=NumberUtils.format(trans.getTaxRate(), "", 4)%></td>
						<td align="right"><%=NumberUtils.format(trans.getTaxAmtCny(), "", 2)%></td>
						<td align="right"><%=NumberUtils.format(trans.getInComeCny(), "", 2)%></td>
						<td align="right"><%=trans.getInComeCny() == null || "".equals(trans.getInComeCny()) || trans.getTaxAmtCny()==null || "".equals(trans.getTaxAmtCny()) ? "" : NumberUtils.format((trans.getInComeCny().add(trans.getTaxAmtCny())),"", 2)%></td>
						<td align="right"><%=trans.getBalance()%></td>
						<td align="center"><%=DataUtil.getFapiaoTypeCH(trans.getFapiaoType())%></td>
						<!--状态-->
						<td align="center"><%=DataUtil.getDataStatusCH(trans.getDataStatus(),"TRANS")%></td>
					</tr>
					<%
						}
						} else {
					%>
					<tr>
						<td colspan="100">无记录</td>
					</tr>
					<%
						}
					%>
				</table>
			</div>
			<table id="tbl_query" cellpadding="0" cellspacing="0" width="80%"
				border="0">
				<tr>
					<td align="left">交易时间</td>
					<td><input class="tbl_query_time" id="transBeginDate"
						type="text" name="transInfoYS.transBeginDate"
						value="<s:property value='transInfoYS.transBeginDate'/>"
						onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'transEndDate\')}'})"
						size='11' /> &nbsp;&nbsp;&nbsp;--&nbsp;&nbsp;&nbsp; <input class="tbl_query_time" id="transEndDate"
						type="text" name="transInfoYS.transEndDate"
						value="<s:property value='transInfoYS.transEndDate'/>"
						onfocus="WdatePicker({minDate:'#F{$dp.$D(\'transBeginDate\')}'})"
						size='11' /></td>
					<td align="left">交易类型</td>
					<td><input type="text" class="tbl_query_text" name="transInfoYS.businessCname"
						value="<s:property value='transInfoYS.businessCname'/>" /></td>
						
					<s:if test='configCustomerFlag.equals("KBC")'>
					<td align="left">客户号</td>
					<td><input type="text" class="tbl_query_text" name="transInfoYS.customerId"
						value="<s:property value='transInfoYS.customerId'/>" /></td>
					</s:if>
				</tr>
				<tr>
					<td align="left">交易金额</td>
					<td><input type="text" class="tbl_query_text" name="transInfoYS.amtMinStr"
						value="<s:property value='transInfoYS.amtMin'/>" maxlength="20" />
						- <input type="text" class="tbl_query_text" name="transInfoYS.amtMaxStr"
						value="<s:property value='transInfoYS.amtMax'/>" maxlength="20" />
					</td>
					<s:if test='configCustomerFlag.equals("KBC")'>
					<td align="left">数据来源</td>
			 		  <td>
						<s:select id="transInfoYS.dataSources" name="transInfoYS.dataSources" list="kbcDataSouceList" headerKey="" headerValue="所有" listKey='value' listValue='text' />
					  </td>
					</s:if>
					<td colspan="2"><input type="button"
					class="tbl_query_button" value="查询"
					onMouseMove="this.className='tbl_query_button_on'"
					onMouseOut="this.className='tbl_query_button'" name="BtnView"
					id="BtnView" onclick="selectList()" /></td>
				</tr>
			</table>
			<div id="lessGridList8" style="overflow: auto; width: 100%;">
		<table class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0" style="border-collapse: collapse; width: 100%;">
<!--			<div id="dataList1" style="overflow: auto; width: 100%; height: 250px">-->
<!--				<table id="lessGridList1" class="lessGrid" cellspacing="0"	rules="all" border="1" display="none" width="100%">-->
					<tr class="lessGrid head">
						<th style="text-align: center"><input
							style="width: 13px; height: 13px;" id="CheckAll"
							type="checkbox" onclick="cbxselectall(this,'selectTransIds')" /></th>
						<th style="text-align: center">序号</th>
						<th style="text-align: center">交易时间</th>
						<th style="text-align: center">客户名称</th>
						<s:if test='configCustomerFlag.equals("KBC")'>
						<th style="text-align: center">客户号</th>
						</s:if>
						<th style="text-align: center">纳税人类型</th>
						<th style="text-align: center">交易类型</th>
						<s:if test='configCustomerFlag.equals("KBC")'>
						<th style="text-align: center">数据来源</th>
						</s:if>
						<th style="text-align: center">交易金额</th>
						<th style="text-align: center">税率</th>
						<th style="text-align: center">税额</th>
						<th style="text-align: center">收入</th>
						<th style="text-align: center">价税合计</th>
						<th style="text-align: center">未开票金额</th>
						<th style="text-align: center">发票类型</th>
						<th style="text-align: center">状态</th>
					</tr>
					<%
						List normalTransList = (ArrayList) request
								.getAttribute("normalTransList");
						if (normalTransList != null && normalTransList.size() > 0) {
							for (int i = 0; i < normalTransList.size(); i++) {
								TransInfoYS normalTrans = (TransInfoYS) normalTransList
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
						<td align="center"><input
							style="width: 13px; height: 13px;" type="checkbox"
							name="selectTransIds"
							value="<%=BeanUtils.getValue(normalTrans, "transId")%>" /></td>
						<td align="center"><%=i + 1%></td>
						<td align="center"><%=normalTrans.getTransDate()%></td>
						<td align="center"><%=normalTrans.getCustomerName()%></td>
						<s:if test='configCustomerFlag.equals("KBC")'>
						<td align="center"><%=normalTrans.getCustomerId()%></td>
						</s:if>
						<td align="center"><%=DataUtil.getTaxpayerTypeCH(normalTrans
					.getTaxpayerType())%></td>
						<td align="center"><%=normalTrans.getBusinessCname()%></td>
						<s:if test='configCustomerFlag.equals("KBC")'>
						<td align="center"></td>
						</s:if>
						<td align="left"><%=NumberUtils.format(normalTrans.getAmtCny(), "", 2)%></td>
						<td align="left"><%=NumberUtils.format(normalTrans.getTaxRate(), "",
					4)%></td>
						<td align="left"><%=NumberUtils.format(normalTrans.getTaxAmtCny(),
					"", 2)%></td>
						<td align="left"><%=NumberUtils.format(normalTrans.getInComeCny(),
					"", 2)%></td>
						<td align="left"><%=normalTrans.getInComeCny() == null || "".equals(normalTrans.getInComeCny()) || normalTrans.getTaxAmtCny()==null || "".equals(normalTrans.getTaxAmtCny()) ? "" : NumberUtils.format((normalTrans.getInComeCny().add(normalTrans.getTaxAmtCny())), "", 2)%></td>
						<td align="left"><%=normalTrans.getBalance()%></td>
						<td align="center"><%=DataUtil.getFapiaoTypeCH(normalTrans
					.getFapiaoType())%></td>
						<!--状态-->
						<td align="center"><%=DataUtil.getDataStatusCH(
					normalTrans.getDataStatus(), "TRANS")%> <input type="hidden"
							name="statNums" value="<%=normalTrans.getDataStatus()%>"></td>
					</tr>
					<%
						}
						} else {
					%>
					<tr>
						<td colspan="100">无记录</td>
					</tr>
					<%
						}
					%>
				</table>
			</div>
		<table id="tbl_tools" width="100%" border="0">
			<tr>
				<td align="left">
				<input type="hidden" name="reverseTransId" value="<s:property value="reverseTransId"/>">
				<input type="hidden" id="transId" name="transId" value="<s:property value="transId"/>">
				<input type="button" class="tbl_query_button" value="冲账"
				onMouseMove="this.className='tbl_query_button_on'"
				onMouseOut="this.className='tbl_query_button'" name="btReverse"
				id="btReverse" onclick="reverse('<s:property value="transId"/>');" />
				<input type="button" class="tbl_query_button" value="返回"
				onMouseMove="this.className='tbl_query_button_on'"
				onMouseOut="this.className='tbl_query_button'" name="btCancel"
				id="btCancel" onclick="revoke()" />
				</td>
			</tr>
		</table>
		<script language="javascript" type="text/javascript">
	<%if (normalTransList == null || normalTransList.size() == 0) {%>
		document.getElementById("btReverse").disabled = true;
	<%}%>
	</script>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
