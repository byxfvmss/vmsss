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
		// [返回]按钮
		function back(){
			submitAction(document.forms[0], "listInputRedReceipt.action?type=back");
		};
		// [提交]按钮
		function commit(){
			submitAction(document.forms[0], "inputRedReceiptSpecialSave.action?type=commit");
		};
		function exportApply(){
			var billId = '<s:property value='applyInfo.billId'/>';
			submitAction(document.forms[0], "exportWord.action?billId="+billId);
		}
		function exportXML(){
			var billId = '<s:property value='applyInfo.billId'/>';
			submitAction(document.forms[0], "exportXML.action?billId="+billId);
		}
	</script>
	<style type="text/css">
		.style1{
			width: 242px;
		}
	</style>
</head>
<body onkeydow="enterkey(event)">
	<form name="Form1" method="post" action="inputRedReceiptSpecialSave.action" id="Form1">

	<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
	<tr>
		<td class="centercondition">
		<s:component template="rocketMessage" />
		<div id="tbl_current_status">
			<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> 
			<span class="current_status_menu">当前位置：</span> 
			<span class="current_status_submenu1">进项税管理</span>
			<span class="current_status_submenu">红冲管理</span>
			<span class="current_status_submenu">发票红冲</span>
			<span class="current_status_submenu">查看数据</span>
		</div>
		<div id="lessGridList16" style="overflow: auto; width: 100%;">
			<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
				<tr>
					<td width="15%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">销售方名称:&nbsp;&nbsp;&nbsp;</td>
					<td style="background-color:#F0F0F0;color:#727375;">
						<s:property value='applyInfo.name'/>
					</td>
					<td width="15%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">购买方名称:&nbsp;&nbsp;&nbsp;</td>
					<td style="background-color:#F0F0F0;color:#727375;"><s:property value='inputInvoiceInfo.billNo'/>
						<s:property value='applyInfo.vendorName'/>
					</td>
				</tr>
				<tr>
					<td width="15%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">销售方纳税人识别号:&nbsp;&nbsp;&nbsp;</td>
					<td style="background-color:#F0F0F0;color:#727375;">
						<s:property value='applyInfo.taxno'/>
					</td>
					<td width="15%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">购买方纳税人识别号:&nbsp;&nbsp;&nbsp;</td>
					<td style="background-color:#F0F0F0;color:#727375;"><s:property value='inputInvoiceInfo.billNo'/>
						<s:property value='applyInfo.vendorTaxno'/>
					</td>
				</tr>
			</table>
			<div class="boxline">商品列表</div>
			<div>
				<iframe id="subTableFrame" scrolling="auto" src="listInputItem.action?billId=<s:property value='applyInfo.billId'/>" height="160px" width="100%" frameborder="0"></iframe>
			</div>
			<div class="boxline">票据交易数据</div>
			<div style="border-bottom:#DDD solid 1px;">
				<iframe id="subTableFrame1" scrolling="auto" src="listInputTransItem.action?billId=<s:property value='applyInfo.billId'/>" height="160px" width="100%" frameborder="0"></iframe>
			</div>
			<table class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0" rules="all">
				<tr>
					<td>
						<s:if test="specialTicket.level1Option == 0">
							<input type="radio" value="0" name="specialTicket.level1Option" onclick="noneDiv()" checked="checked" />已抵扣
						</s:if>
						<s:else>
							<input type="radio" value="0" name="specialTicket.level1Option" onclick="noneDiv()" />已抵扣
						</s:else>
							<br>
						<s:if test="specialTicket.level1Option == 1">
							<input type="radio" value="0" name="specialTicket.level1Option" onclick="showDiv()" checked="checked" />未抵扣
						</s:if>
						<s:else>
							<input type="radio" value="1" id="radioF" name="specialTicket.level1Option" onclick="showDiv()" />未抵扣
						</s:else>
					</td>
					<td colsapn="3">
						<s:if test="specialTicket.level2Option == 0">
							<input type="radio" value="0" name="specialTicket.level2Option" checked="checked" />无法认证<br/>
						</s:if>
						<s:else>
							<input type="radio" value="0" name="specialTicket.level2Option" />无法认证<br/>
						</s:else>
						<s:if test="specialTicket.level2Option == 1">
							<input type="radio" value="1" name="specialTicket.level2Option" checked="checked" />纳税人识别号认证不符<br/>
						</s:if>
						<s:else>
							<input type="radio" value="1" name="specialTicket.level2Option" />纳税人识别号认证不符<br/>
						</s:else>
						<s:if test="specialTicket.level2Option == 2">
							<input type="radio" value="2" name="specialTicket.level2Option" checked="checked" />增值税专用发票代码、号码认证不符<br/>
						</s:if>
						<s:else>
							<input type="radio" value="2" name="specialTicket.level2Option" />增值税专用发票代码、号码认证不符<br/>
						</s:else>
						<s:if test="specialTicket.level2Option == 3">
							<input type="radio" value="3" name="specialTicket.level2Option" checked="checked" />所购货物或劳务、服务不属于增值税扣税项目范围<br/>
						</s:if>
						<s:else>
							<input type="radio" value="3" name="specialTicket.level2Option" />所购货物或劳务、服务不属于增值税扣税项目范围<br/>
						</s:else>
					</td>
				</tr>
				<tr>
					<td width="15%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">对应蓝字专用发票的代码:&nbsp;&nbsp;&nbsp;</td>
					<td style="background-color:#F0F0F0;color:#727375;">
						<s:property value='applyInfo.billCode'/>
					</td>
					<td width="15%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">对应蓝字专用发票的号码:&nbsp;&nbsp;&nbsp;</td>
					<td style="background-color:#F0F0F0;color:#727375;"><s:property value='inputInvoiceInfo.billNo'/>
						<s:property value='applyInfo.billNo'/>
					</td>
				</tr>
			</table>
			<input type="hidden" name="specialTicket.billId" value="<s:property value='applyInfo.BillId'/>"/>
			<input type="hidden" name="specialTicket.billNo" value="<s:property value='applyInfo.BillNo'/>"/>
			<input type="hidden" name="specialTicket.billCode" value="<s:property value='applyInfo.BillCode'/>"/>
		</div>
		<div  align="Right" style="width:100%; float: right;">
			<table width="100%" cellspacing="0" border="0">
			<tr>
				
				<input type="button" class="tbl_query_button" value="返回"
							onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
							name="BtnView" id="BtnView" onclick="back()" />			
				<input type="button" class="tbl_query_button mleft5" value="导出Word"
							onMouseMove="this.className='tbl_query_button_on mleft5'" onMouseOut="this.className='tbl_query_button mleft5'"
							name="BtnView" id="BtnView" onclick="exportApply()" />
				<input type="button" class="tbl_query_button mleft5" value="导出XML"
							onMouseMove="this.className='tbl_query_button_on mleft5'" onMouseOut="this.className='tbl_query_button mleft5'"
							name="BtnView" id="BtnView" onclick="exportXML()" />
			</tr>
			</table>		
		</div>
		</td>
	</tr>
	</table>	
	<script language="javascript" type="text/javascript" charset="GBK">
		function showDiv(){
			var div = document.getElementById("div");
			div.style.display="block";
		}
		function noneDiv(){
			var div = document.getElementById("div");
			div.style.display="none";
		}
	</script>
	</form>
</body>
</html>