<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.util.PaginationList"
	import="com.cjit.vms.input.model.InputInvoiceInfo"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.cjit.vms.input.model.InputTrans"%>
<%@ include file="../../../page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<title>增值税管理平台</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
	
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/calendar_notime.js"></script>
	<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/XmlHttp.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/search.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/ylb.js"></script>
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/search.js"></script>
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/window.js"></script>
	<script type="text/javascript">
		// [查询]按钮
		function submitBill(){
				submitAction(document.forms[0], "listInputCompare.action");
		}
		
		function submitExport(){
				submitAction(document.forms[0], "exportinputInvoiceInfo.action");
		}
		 
		function submitdelte(a,b,c){
				$.ajax({
				type: "POST",
				url:"deleteInnovation.action?billId="+a+"&dealNo="+b+"&vendId="+c,
				success: function(data) {
				window.parent.submitForm();
				//window.parent.rel();
				}
				});
				//submitAction(document.forms[0], "deleteInnovation.action?billId="+a+"&dealNo="+b+"&vendId="+c);
		}
		
	</script>
	<style type="text/css">
		.style1{
			width: 242px;
		}
	</style>
</head>
	<body class="white" onLoad="" onmousemove="MM(event)" onmouseout="MO(event)">
    <form name="Form1" method="post" action="listDatasInner.action" id="Form1" enctype="multipart/form-data">
	<%
		List billItemList = (List)request.getAttribute("inputTransList");
		
		
	%>
	<div id="lessGridList17" style="overflow: auto; width: 100%;">
	<table class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0" style="border-collapse: collapse; width: 100%;">
	<tr class="lessGrid head">
		<th>序号</th>
		<th>交易编号</th>
		<th>金额</th>
		<th>税额</th>
		<th>供应商纳税人识别号</th>
		<th>交易发生机构</th>
		<s:if test="flag=='edit'" ><th>撤销勾稽</th></s:if>
		
	</tr>
	<%
		for(int i = 0; billItemList != null && i < billItemList.size(); i++){
			InputTrans inputTrans = (InputTrans) billItemList.get(i);
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
		<td align="center"><%=i+1 %></td>
		<td align="center"><%=inputTrans.getDealNo()%></td>
		<td align="center"><%=inputTrans.getAmtCny()%></td>
		<td align="center"><%=inputTrans.getTaxAmtCny()%></td>
		<td align="center"><%=inputTrans.getVendorId()%></td>
		<td align="center"><%=inputTrans.getBankCode()%></td>
		<s:if test="flag=='edit'">
		<td align="center"><a  href="javascript:void(0);"
		       onclick="submitdelte('<s:property value='billId'/>','<%=inputTrans.getDealNo()%>','<%=inputTrans.getVendorId()%>')">
		       撤销
		       							<!-- 
									    	<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1020.png"
									    	title="单击撤销" style="border-width: 0px;" />
		       							 -->
								    	</a></td>
								    	</s:if>
	</tr>
	<%
		}
	%>
	</table>
    </div> 
    </form>
</body>
</html>