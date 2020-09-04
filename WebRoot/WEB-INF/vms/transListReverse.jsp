<!-- file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.util.PaginationList"
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
	<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/calendar_notime.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/XmlHttp.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/search.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/ylb.js"></script>
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/search.js"></script>
	<script type="text/javascript">
		var msg = '<s:property value="message"/>';
		if (msg != ""){
		}
		// [查询]按钮  [导出]按钮
		function submitForm(actionUrl){
			submitAction(document.forms[0], actionUrl);
			document.forms[0].action="listTransReverse.action";
		}
		// [交易冲账]按钮
		function transToReverse(){
			if(checkChkBoxesSelected("reverseTransId")){
				if(!confirm("确定对选中交易进行冲账？")){
					return false;
				}
				submitAction(document.forms[0], "transToReverse.action?reverseTransId=" + transId);
				document.forms[0].action="listTransReverse.action";
			}else{
				alert("请选择交易记录！");
			}
		}
		function goToPage(reverseTransID,transID){
			submitAction(document.forms[0], "transToReverse1.action?fromFlag=list&reverseTransId=" + reverseTransID+"&transId="+transID);
			document.forms[0].action="listTransReverse.action";
		}
	</script>
</head>
<body onkeydow="enterkey(event)">
	<form name="Form1" method="post" action="listTransReverse.action" id="Form1">
	<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
	<tr>
		<td class="centercondition">
		<s:component template="rocketMessage" />
		<div id="tbl_current_status">
			<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> 
			<span class="current_status_menu">当前位置：</span> 
			<span class="current_status_submenu1">销项税管理</span>
			<span class="current_status_submenu">交易冲账</span>
			<span class="current_status_submenu">交易冲账</span>
		</div>
		<div class="widthauto1">
		<table id="tbl_query" cellpadding="0" cellspacing="0" width="80%" border="0">
		<tr>
			<td align="left">
				交易时间
			</td>
			<td>
				<input class="tbl_query_time" id="transBeginDate" type="text" name="transBeginDate"
					value="<s:property value='transBeginDate'/>"
					onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'transEndDate\')}'})"
					size='11'/>
				--
				<input class="tbl_query_time" id="transEndDate" type="text" name="transEndDate"
					value="<s:property value='transEndDate'/>"
					onfocus="WdatePicker({minDate:'#F{$dp.$D(\'transBeginDate\')}'})"
					size='11'/>
			</td>
			<td align="left">
				客户名称
			</td>
			<td>
				<input type="text" class="tbl_query_text" name="transInfoYS.customerName" value="<s:property value='transInfoYS.customerName'/>" />
			</td>
			
			<td align="left">
				纳税人类型
			</td>
			<td>
				<s:select id="transInfoYS.taxpayerType" name="transInfoYS.taxpayerType" list="custTaxPayerTypeList" headerKey="" headerValue="所有"
					listKey='value' listValue='text'/>
			</td>
			
		</tr>
		<tr>
			<td align="left">
				交易金额
			</td>
			<td>
				<input type="text" class="tbl_query_text" name="transInfoYS.amtMinStr" value="<s:property value='transInfoYS.amtMin'/>" maxlength="20"/>
				-
				<input type="text" class="tbl_query_text" name="transInfoYS.amtMaxStr" value="<s:property value='transInfoYS.amtMax'/>" maxlength="20"/>
			</td>
			<td align="left">
				未开票金额
			</td>
			<td>
				<input type="text" class="tbl_query_text" name="transInfoYS.balanceMinStr" value="<s:property value='transInfoYS.balanceMin'/>" maxlength="20"/>
				-
				<input type="text" class="tbl_query_text" name="transInfoYS.balanceMaxStr" value="<s:property value='transInfoYS.balanceMax'/>" maxlength="20"/>
			</td>
			<td align="left">
				交易类型
			</td>
			<td>
				<input type="text" class="tbl_query_text" name="transInfoYS.businessCname" value="<s:property value='transInfoYS.businessCname'/>" />
			</td>
			<td>
				<input type="button" class="tbl_query_button" value="查询"
						onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
						name="BtnView" id="BtnView" onclick="submitForm('listTransReverse.action')" />
			</td>
			<!-- <td style="width:80px;" align="right">
			    <input type="button" class="tbl_query_button" value="导出"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="BtnView" id="BtnView" onclick="submitForm('transInfoQueryToExcel.action')" />		
			</td> -->
		</tr>
		<s:if test='configCustomerFlag.equals("KBC")'>
		<tr>
			<td align="left">
				客户号
			</td>
			<td>
				<input type="text" class="tbl_query_text" name="transInfoYS.customerId" value="<s:property value='transInfoYS.customerId'/>" />
			</td>
			<td align="left">数据来源</td>
			 <td>
				<s:select id="transInfo.dataSources" name="transInfo.dataSources" list="kbcDataSouceList" headerKey="" headerValue="所有" listKey='value' listValue='text' />
			 </td>
			
		</tr>
		</s:if>
		</table>
		</div>
 	<table id="tbl_tools" width="100%" border="0">
			<tr>
				<td align="left">
				<a href="#" onclick="submitForm('transInfoQueryToExcel.action')" ><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png"/>导出</a>
				</td>
			</tr>
		</table>
  	<div id="lessGridList4" style="overflow: auto; width: 100%;">
		<table class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0" style="border-collapse: collapse; width: 100%;">
	<tr class="lessGrid head">
		<th width="3%" style="text-align: center">序号</th>
		<th style="text-align:center">冲账</th>
		<th style="text-align:center">交易时间</th>
		<th style="text-align:center">客户名称</th>
		<s:if test='configCustomerFlag.equals("KBC")'>
		<th style="text-align:center">客户号</th>
		</s:if>
		<th style="text-align:center">纳税人类型</th>
		<th style="text-align:center">交易类型</th>
		<s:if test='configCustomerFlag.equals("KBC")'>
		<th style="text-align:center">数据来源</th>
		</s:if>
		<th style="text-align:center">交易金额</th>
		<th style="text-align:center">税率</th>
		<th style="text-align:center">税额</th>
		<th style="text-align:center">收入</th>
		<th style="text-align:center">价税合计</th>
		<th style="text-align:center">未开票金额</th>
		<th style="text-align:center">发票类型</th>
		<th style="text-align:center">状态</th>
		<th style="text-align:center">明细</th>
	</tr>
	<%
		PaginationList paginationList = (PaginationList)request.getAttribute("paginationList");
		if (paginationList != null){
			List transInfoList = paginationList.getRecordList();
			if (transInfoList != null && transInfoList.size() > 0){
				for(int i=0; i<transInfoList.size(); i++){
					TransInfoYS trans = (TransInfoYS)transInfoList.get(i);
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
		<td align="center"><%=i+1%></td>
		<td align="center">
			<a onclick="goToPage('<%=trans.getReverseTransId()==null?"":trans.getReverseTransId()%>','<%=trans.getTransId()%>');">
				<img src ="<c:out value="${bopTheme}"/>/themes/images/icons/icon1005.png" title="冲账" style="border-width: 0px;" />
			</a>
		</td>
		<td align="center"><%=trans.getTransDate()%></td>
		<td align="center"><%=trans.getCustomerName()==null?"":trans.getCustomerName()%></td>
		<s:if test='configCustomerFlag.equals("KBC")'>
		<td align="center"><%=trans.getCustomerId()==null?"":trans.getCustomerId()%></td>
		</s:if>
		<td align="center">
			<%=DataUtil.getTaxpayerTypeCH(trans.getTaxpayerType())%>
		</td>
		<td align="center"><%=trans.getBusinessCname()%></td>
		<s:if test='configCustomerFlag.equals("KBC")'>
		<td align="center"></td>
		</s:if>
		<td align="center"><%=NumberUtils.format(trans.getAmtCny(),"",2)%></td>
		<td align="center"><%=NumberUtils.format(trans.getTaxRate(),"",4)%></td>
		<td align="center"><%=NumberUtils.format(trans.getTaxAmtCny(),"",2)%></td>
		<td align="center"><%=NumberUtils.format(trans.getInComeCny(),"",2)%></td>
		<td align="center"><%=trans.getInComeCny() == null || "".equals(trans.getInComeCny()) || trans.getTaxAmtCny()==null || "".equals(trans.getTaxAmtCny()) ? "" : NumberUtils.format((trans.getInComeCny().add(trans.getTaxAmtCny())),"",2)%></td>
		<td align="center"><%=trans.getBalance()%></td>
		<td align="center"><%=DataUtil.getFapiaoTypeCH(trans.getFapiaoType())%></td>
		<!--状态-->
		<td align="center"><%=DataUtil.getDataStatusCH(trans.getDataStatus(),"TRANS")%>
			<input id="statNum" type="hidden" name="statNum" value="<%=trans.getDataStatus()%>">
		</td>
		<td align="center">
			<a href="transEdit.action?transId=<%=BeanUtils.getValue(trans,"transId")%>">
			<img src ="<c:out value="${bopTheme}"/>/themes/images/icons/icon1014.png" title="查看明细" style="border-width: 0px;" /></a>
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
		<td align="right"><s:component template="pagediv"/></td>
	</tr>
	</table>
	</div>
		</td>
	</tr>
	</table>
	</form>
</body>
</html>
