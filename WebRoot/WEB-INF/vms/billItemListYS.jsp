<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="cjit.crms.util.StringUtil"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.vms.trans.model.BillInfo"
	import="com.cjit.vms.trans.model.BillItemInfo"
	import="com.cjit.vms.trans.model.TransInfo"
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
<body onmousemove="MM(event)" onmouseout="MO(event)" class="white">
    <form name="Form1" method="post" action="listDatasInner.action" id="Form1" enctype="multipart/form-data">
	<%
		Map innerCheckResult = (HashMap)session.getAttribute(ScopeConstants.CHECK_RESULT_INNER);
		List billItemList = (ArrayList)request.getAttribute("billItemList");
		String billId = (String)request.getAttribute("billId");
		if (billId == null){
			billId = "";
		}
		String isHandiwork = (String)request.getAttribute("isHandiwork");
	%>
	<input type="hidden" name="infoTypeCodeInner" value="<s:property value='infoTypeCodeInner'/>"/>
	<input type="hidden" name="billId" value="<%=billId%>"/>
	<input type="hidden" name="isHandiwork" value="<%=isHandiwork%>"/>
	<table class="lessGrid" id="tbl_tools" width="100%" border="0">
	<%
		if (StringUtil.isNotEmpty(billId) && DataUtil.YES.equals(isHandiwork)){
	%>
	<tr class="row1">
		<td>
			<input type="button" class="tbl_query_button" value="添加明细" 
				onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
				name="btSave" id="btSave" onclick="javascript:goToPage('editBillItem.action?billId=<%=billId%>')" />
		</td>
	</tr>
	<%
		}
	%>
	</table>
	<div id="list1">
	<table class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0" style="border-collapse: collapse; width: 100%;">
	<tr class="lessGrid head">
		<th style="text-align:center">商品名称</th>
		<th style="text-align:center">规格型号</th>
		<th style="text-align:center">商品数量</th>
		<th style="text-align:center">商品单价</th>
		<th style="text-align:center">金额</th>
		<th style="text-align:center">税率</th>
		<th style="text-align:center">税额</th>
	</tr>
	<%
		for(int i = 0; billItemList != null && i < billItemList.size(); i++){
			BillItemInfo billItem = (BillItemInfo) billItemList.get(i);
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
	<!-- 
		<td>
			<a href="editBillItem.action?billId=<%=billId%>&billItemId=<%=billItem.getBillItemId()%>&flag=normal">
				<img src="<%=bopTheme2%>/img/jes/icon/edit.png" title="修改明细" style="border-width: 0px;" />
			</a>
		</td>
		<td>
			<a onclick="if(confirm('是否确认删除该笔票据明细？')){goToPage('<%=webapp%>/deleteBillItem.action?billId=<%=billId%>&billItemId=<%=billItem.getBillItemId()%>');}">
				<img src="<%=bopTheme2%>/img/jes/icon/delete.png" title="删除明细" style="border-width: 0px;" />
			</a>
		</td>
		<td>
	<%
			if ("0".equals(billItem.getRowNature())){
				// 只有正常行的明细记录可编辑折扣
	%>
			<a href="editBillItem.action?billId=<%=billId%>&billItemId=<%=billItem.getBillItemId()%>&flag=dis">
				<img src="<%=bopTheme2%>/img/jes/icon/nextC.png" title="编辑折扣" style="border-width: 0px;" />
			</a>
	<%
			}else{
				out.print("");
			}
	%>
		</td>
		 -->
		<%--<!--交易类型-->
		<td><%=billItem.getTransTypeName()==null?"":billItem.getTransTypeName()%></td>
		--%>
		<!--商品名称-->
		<td><%=billItem.getGoodsName()%></td>
		<!--规格型号-->
		<td><%=billItem.getSpecandmodel()%></td>
		<!--商品数量-->
		<td><%=billItem.getGoodsNo()==null?"":billItem.getGoodsNo()%></td>
		<!--商品单价-->
		<td><%=billItem.getGoodsPrice()==null?"":billItem.getGoodsPrice()%></td>
		<!--金额-->
		<td><%=billItem.getAmt()%></td>
		<!--税率-->
		<td><%=billItem.getTaxRate()==null?"":billItem.getTaxRate()%></td>
		<!--税额-->
		<td><%=billItem.getTaxAmt()%></td>
	</tr>
	<%
		}
	%>
	</table>
    </div> 
    </form>
</body>
</html>
