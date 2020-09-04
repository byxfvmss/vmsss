<!--file: <%=request.getRequestURI() %> -->
<%@page import="com.cjit.vms.trans.model.RedReceiptApplyInfo"%>
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.util.PaginationList"
	import="com.cjit.vms.trans.model.BillInfo"
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
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/lhb.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/comm.js"></script>
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/search.js"></script>
	<script type="text/javascript">
		// [保存]按钮
		function save(){
			document.forms[0].submit();
		};
		// [保存]按钮
		function commit(){
			submitAction(document.forms[0], "redReceiptSpecialSave.action?commit=commit");
		};
		function exportApply(){
			submitAction(document.forms[0], "exportApplyInfo.action");
		}
	</script>
</head>
<body onkeydow="enterkey(event)">
	<form name="Form1" method="post" action="redReceiptSpecialSave.action" id="Form1">

	<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
	<tr>
		<td class="centercondition">
		<s:component template="rocketMessage" />
		<div id="tbl_current_status">
			<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> 
			<span class="current_status_menu">当前位置：</span> 
			<span class="current_status_submenu1">销项税管理</span>
			<span class="current_status_submenu">红冲管理</span>
			<span class="current_status_submenu">红冲申请</span>
		</div>
		<div class="widthauto1">
		<table id="tbl_query" cellpadding="0" cellspacing="0" width="80%" border="0">
		<tr>
			
			<td align="left">
				销售方名称:
			</td>
			<td>
				<input type="text" name="applyInfo.name" value="<s:property value='applyInfo.name'/>" class="readonly" readonly="readonly"/>
			</td>
			
			<td align="left">
				购买方名称:
			</td>
			<td>
				<input type="text" name="applyInfo.customerName" value="<s:property value='applyInfo.customerName'/>" class="readonly" readonly="readonly"/>
			</td>
			<td align="left">
				销售方纳税人识别号:
			</td>
			<td>
				<input type="text" name="applyInfo.taxno" value="<s:property value='applyInfo.taxno'/>" class="readonly" readonly="readonly"/>
			</td>
			
			<td align="left">
				购买方纳税人识别号:
			</td>
			<td>
				<input type="text" name="applyInfo.customerTaxno" value="<s:property value='applyInfo.customerTaxno'/>" class="readonly" readonly="readonly"/>
			</td>

		</tr>
		</table>
		</div>
		</td>
	</tr>
	<tr class="lessGrid head">
		<td>
			<iframe id="subTableFrame" scrolling="auto" src="listBillItem1.action?billId=<s:property value='applyInfo.billId'/>&isHandiwork=<s:property value='billInfo.isHandiwork'/>" height="110px" width="100%" frameborder="0"></iframe>
		</td
	</tr>
	</table>
	<div style="overflow:auto;width:100%;" id="list1">
	<table id="lessGridList" width="100%" class="lessGrid" cellspacing="0" rules="all" border="0" cellpadding="0" display="none" style="border-collapse: collapse;">
	
	
	<tr>
		<td align="left">
				概要信息
			</td>
			<td>
		<s:if test="specialTicket.level1Option == 0">
			<input type="radio" value="0" name="specialTicket.level1Option" checked="checked"/>购买方拒收发票
			
		</s:if>
		<s:else>
				<input type="radio" value="0" name="specialTicket.level1Option" />购买方拒收发票
		</s:else>
		<br/>
		<s:if test="specialTicket.level1Option == 1">
			<input type="radio" value="1" name="specialTicket.level1Option" checked="checked"/>发票尚未交付
		</s:if>
		<s:else>
				<input type="radio" value="1" name="specialTicket.level1Option"/>发票尚未交付
		</s:else>
		<input type="hidden" name="specialTicket.billId" value="<s:property value='applyInfo.BillId'/>"/>
		<input type="hidden" name="specialTicket.billNo" value="<s:property value='applyInfo.BillNo'/>"/>
		<input type="hidden" name="specialTicket.billCode" value="<s:property value='applyInfo.BillCode'/>"/>
		</td>
	</tr>
	<tr>
	<td align="left">
				对应蓝字专用发票的代码:
			</td>
			<td>
				<input type="text" name="applyInfo.customerName" value="<s:property value='applyInfo.billCode'/>" class="readonly" readonly="readonly"/>
			</td>
			
			<td align="left">
				对应蓝字专用发票的号码:
			</td>
			<td>
				<input type="text" name="applyInfo.customerName" value="<s:property value='applyInfo.billNo'/>" class="readonly" readonly="readonly"/>
			</td>
			
			<td align="left">
				红字发票信息表编号:
			</td>
			<td>
				<input type="text" name="" value="" />
			</td>
		</tr>	
	</table>
	</div>
	<div id="anpBoud" align="Right" style="overflow:auto;width:100%;">
	<table width="100%" cellspacing="0" border="0">
	<tr>
		<td align="right"><s:component template="pagediv"/></td>
	</tr>
	<tr>
		<input type="button" class="tbl_query_button" value="保存"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="BtnView" id="BtnView" onclick="save()" />
		<input type="button" class="tbl_query_button" value="提交"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="BtnView" id="BtnView" onclick="commit()" />			
		<input type="button" class="tbl_query_button" value="导出word"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="BtnView" id="BtnView" onclick="exportApply()" />
	</tr>
	</table>
	
	</div>
	<script type="text/javascript">
		var msgHight = document.getElementById("anpBoud").offsetHeight;
		document.getElementById("list1").style.height = screenHeight - 340 - msgHight;
	</script>
	<script language="javascript" type="text/javascript" charset="GBK">
	
	</script>
	</form>
</body>
</html>