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
	<base target="_self">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
	<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
	<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
	<script type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
	<script type="text/javascript">	
		function submitForm(actionUrl){
		document.forms[0].action = actionUrl;
		document.forms[0].submit();
	}
		
		
		function goY(t){
		var tr = $(t).parents('tr:first').attr("id");
		var len_y = $(window.parent.document).find("#table_y tr").length;
		var rowA_class = "lessGrid rowA";
		var rowB_class = "lessGrid rowB";
		var trHTML = "<tr id='"+tr+"' align='center' ";
		if(len_y%2 ==0){
			trHTML += "class='"+rowB_class+"'>";
		}else{
			trHTML += "class='"+rowA_class+"'>";
		}
		valArr=[];

		$("#"+tr).find("td").each(function(index,element){
			if(index==0){
				n = len_y-1;
				trHTML += "<td>"+n+"</td>";
			}
			else if(index==6){
				trHTML += "<td><a href='javascript:void(0);' onClick='goN(this)'>撤销</a></td>";
			}
			else{
				trHTML += "<td>"+$.trim($(this).text())+"</td>";
			}
			//valArr.push($.trim($(this).text()));//.text()获取td的文本内容，$.trim()去空格
		});
		$(window.parent.document).find("#table_y").append(trHTML);
		$(t).parents('tr:first').remove();
		var len_n = $("#table_n tr").length;
		for (i = 0; i < len_n; i++) {
             $("#table_n tr").eq(i).children("td").eq(0).html(i);  //给每行的第一列重写赋值
        }
	}
</script>
	<style type="text/css">
		.style1{
			width: 242px;
		}
	</style>
</head>
	<body scroll="no" style="overflow:hidden; background:#FFF;">
<form id="main" action="listInputnoTransByVen.action" method="post">
<table  class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
		<tr>
			<td align="left" colspan='14'>
			<input type="hidden" id="billId" name="billId" value="<%=request.getParameter("billId")%>"/>
			<input type="hidden" id="vendorId" name="vendorId" value="<%=request.getParameter("vendorId")%>"/>
			<table id="tbl_query" cellpadding="0" cellspacing="0" border="0" width="100%" >
			<tr>
				<td>交易编号:</td> 
				<td><input id="dealNo" class="tbl_query_text" name="dealNo" type="text" /></td>
				<td>
				<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 
				onclick="submitForm('listInputnoTransByVen.action');" name="cmdSelect" value="查询" id="cmdSelect" /></td>
			</tr>
		</table>
		</td>
	</tr>
</table>

<div style="overflow: auto; height:220px;">
<table id="table_n"  class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0" >
	<tr class="lessGrid head">
		<th> 序号</th>
		<th>交易编号</th>
		<th>金额</th>
		<th>税额</th>
		<th>供应商纳税人识别号</th>
		<th>交易发生机构</th>
		<th>确人钩稽</th>
		</tr>
		<s:iterator value="inputTransList" id="iList" status="stuts">
		<tr id="<s:property value='dealNo' />" align="center" class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
			<td align="center"><s:property value='#stuts.count' /></td>		
			<td align="center"><s:property value='dealNo'/></td>
			<td align="center"><s:property value='amtCny'/></td>
			<td align="center"><s:property value='taxAmtCny'/></td>
			<td align="center"><s:property value='vendorId '/></td>
			<td align="center"><s:property value='bankCode'/></td>
			<td align="center">
				<a href="javascript:void(0);" onClick='goY(this)'>勾稽</a>
			</td>
		</tr>
		</s:iterator>  
</table>
</div>
<div id="anpBoud" align="Right" style="width:100%;">
        <table width="100%" cellspacing="0" border="0">
            <tr>           
                <td align="right"><s:component template="pagediv"/></td>
            </tr>
		</table>
</div>
</form>
</body>
</html>