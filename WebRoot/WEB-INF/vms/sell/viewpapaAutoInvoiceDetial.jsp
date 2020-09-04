<!-- file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.util.PaginationList"
	import="com.cjit.vms.trans.model.storage.PaperAutoInvoice"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="cjit.crms.util.date.DateUtil"%>
<%@page import="com.cjit.common.util.DateUtils"%>
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
	
<link href="<c:out value="${bopTheme2}"/>/css/main.css" rel="stylesheet" type="text/css" />
<script language="javascript" type="text/javascript" src="<%=bopTheme %>/js/window.js"></script>
<script charset="gbk"  language="javascript" type="text/javascript" src="<%=bopTheme%>/js/validator.js"></script>
<script type="text/javascript" src="js/My97DatePicker/WdatePicker.js"></script>
	<script>  
	function revoke(){
		submitAction(document.forms[0], "listBillTrack.action?fromFlag=menu&flag=track");
	}
	</script>
	
	<style type="text/css">
		.style1{
			width: 242px;
		}
	</style>
</head>
<body onkeydow="enterkey(event)">
	<table id="tbl_main" cellpadding=0 cellspacing=0>
	<tr>
		<td align="left">
		<table id="tbl_current_status" >
		<tr style="margin-left:-100px">
			<td>
				<img src="<%=bopTheme2%>/img/jes/icon/home.png" />
				<span class="current_status_menu">当前位置：</span>
				<span class="current_status_submenu">销项税管理
					<span class="actionIcon">-&gt;</span>库存管理
					<span class="actionIcon">-&gt;</span>发票管理
					<span class="actionIcon">-&gt;</span>查看详情
				</span>
			</td>
		</tr>
		</table>
		</td>
	</tr>
	</table>		</table>

	
	
	<form name="Form1" method="post" action="" id="Form1">
	<div style="overflow:auto;width:100%;" id="lessGridList">
<table  class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0"  style="border-collapse: collapse;width: 100%;">
	<tr class="lessGrid head">
        <th style="text-align:center">序号</th>
		<th style="text-align:center">纳税人识别号</th>
		<th style="text-align:center">税控盘号</th>
		<th style="text-align:center">领购日期</th>
		<th style="text-align:center">领购人员</th>
		<th style="text-align:center">发票类型</th>
		<th style="text-align:center">当前发票代码</th>
		<th style="text-align:center">发票代码</th>
		<th style="text-align:center">当前发票号码		</th>
		<th style="text-align:center">发票代码</th>
		<th style="text-align:center">发票起始号码</th>
		<th style="text-align:center">发票终止号码	</th>
		<th style="text-align:center">发票份数</th>
		<th style="text-align:center">剩余份数</th>
		
	</tr>
	<%
		PaginationList paginationList = (PaginationList)request.getAttribute("paginationList");
		if (paginationList != null){
			List transInfoList = paginationList.getRecordList();
			if (transInfoList != null && transInfoList.size() > 0){
				for(int i=0; i<transInfoList.size(); i++){
					PaperAutoInvoice trans = (PaperAutoInvoice)transInfoList.get(i);
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
		<td align="center"><%=trans.getTaxpayerNo()%></td>
		<td align="center"><%=trans.getTaxDiskNo()%></td>
		<td align="center"><%=DateUtils.toString((DateUtils.stringToDate(trans.getReceiveInvoiceTime(),"yyyyMMdd")),"yyyyMMdd")%></td>
		<td align="center"><%=trans.getUserId()%></td>
		<td align="center"><%=DataUtil.getFapiaoTypeCH(trans.getInvoiceType())%></td>
		<td align="center"><%=trans.getCurrentInvoiceCode()%></td>
		<td align="center"><%=trans.getInvoiceCode()%></td>
		<td align="center"><%=trans.getCurrentInvoiceNo()%></td>
		<td align="center"><%=trans.getInvoiceCode()%></td>
		<td align="center"><%=trans.getInvoiceBeginNo()%></td>
		<td align="center"><%=trans.getInvoiceEndNo()%></td>
		<td align="center"><%=trans.getInvoiceNum()%></td>
		<td align="center"><%=trans.getSurplusNum()%></td>
		
		
	</tr>
	<%
				}
			}
		}
	%>
	</tr>
	</table>
	</div>
	<table id="tbl_tools" width="100%" border="0">
		<tr>
			<td align="left">
				<input type="button" class="tbl_query_button" value="关闭"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="btCancel" id="btCancel" onclick="window.history.back(-1);" />
			</td>
		</tr>
	</table>
	<div id="anpBoud" align="Right" style="overflow:auto;width:100%;">
	<table width="100%" cellspacing="0" border="0">
	<tr>
		<td align="right"><s:component template="pagediv"/></td>
	</tr>
	</table>
	</div>
	<script type="text/javascript">
	var msgHight = document.getElementById("anpBoud").offsetHeight;
	document.getElementById("list1").style.height = screenHeight - 355 - msgHight;
	</script>
	<script language="javascript" type="text/javascript" charset="GBK">
	
	</script>
	</form>
</body>
</html>