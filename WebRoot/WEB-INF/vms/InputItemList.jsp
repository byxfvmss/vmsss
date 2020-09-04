<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="cjit.crms.util.StringUtil"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.vms.input.model.InputTrans"
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
    <script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/ylb.js"></script>
    <script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
	<script>
	function submit(){
		document.forms[0].submit();
	}
	var page = {};
	page.submitForm = function(){
		var fileId = document.getElementById("fileId");
		if(fileId.value.length > 0){
			if(fileId.value.lastIndexOf(".XLS") > -1||fileId.value.lastIndexOf(".xls")>-1){
				document.forms[0].action = "importExcel.action?tableIdInner=<s:property value='tableIdInner'/>&businessId=<s:property value='businessId'/>";
				document.forms[0].submit();
			}else{
				alert("文件格式不对，请上传Excel文件。");
			}
		}else{
			alert("请先选择要上传的文件。");
		}
	}
	</script>
    <style type="text/css">
        .style1
        {
            width: 242px;
        }
    </style>
</head>
<body onLoad="loadBillAmtInfo();" onmousemove="MM(event)" onmouseout="MO(event)">
    <form name="Form1" method="post" action="listDatasInner.action" id="Form1" enctype="multipart/form-data">
	<%
		//Map innerCheckResult = (HashMap)session.getAttribute(ScopeConstants.CHECK_RESULT_INNER);
		List inputItemList = (ArrayList)request.getAttribute("inputItemList");
		
	%>
<div style="SCROLLBAR-FACE-COLOR: #B7B7B7;width:100%;height:100%" id="list1">
	<table id="lessGridList" class="lessGrid" cellspacing="0" rules="all" border="1" display="none" width="100%">
	<tr class="fixedrowheader head" height="30">
		<!-- <th style="text-align:center" width="1%">修改</th>
		<th style="text-align:center" width="1%">删除</th>
		<th style="text-align:center" width="1%">折扣</th> -->
		<th>商品名称</th>
		<th>规格型号</th>
		<th>商品数量</th>
		<th>商品单价</th>
		<th>金额</th>
		<th>税率</th>
		<th>税额</th>
	</tr>
	<%
		for(int i = 0; inputItemList != null && i < inputItemList.size(); i++){
			InputTrans inputItem = (InputTrans) inputItemList.get(i);
			if(i%2==0){
	%>
	<tr class="row1">
	<%
			}else{
	%>
	<tr class="row2">
	<%
			}
	%>
	
	
	
	
		</td>
		
		<td><%=inputItem.getGoodsName()==null?"":inputItem.getGoodsName()%></td>
	
		<td><%=inputItem.getSpecandmodel()%></td>
		<!--商品数量-->
		<td><%=inputItem.getGoodsNo()==null?"":inputItem.getGoodsNo()%></td>
		<!--商品单价-->
		<td><%=inputItem.getGoodsPrice()==null?"":inputItem.getGoodsPrice()%></td>
		<!--金额-->
		<td><%=inputItem.getAmt()%></td>
		<!--税率-->
		<td><%=inputItem.getTaxRate()==null?"":inputItem.getTaxRate()%></td>
		<!--税额-->
		<td><%=inputItem.getTaxAmt()%></td>
	</tr>
	<%
		}
	%>
	</table>
    </div> 
    </form>
</body>
</html>