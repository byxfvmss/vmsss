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
<%@ include file="../../../../page/include.jsp"%>
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
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/window.js"></script>
	<script type="text/javascript">
		// [查询]按钮
		function setBusiList(){
				var taxno = document.getElementById("taxno").value;
				var goodsNo = document.getElementById("goodsNo").value;
				submitAction(document.forms[0], "getBusitoList.action?taxno="+taxno+"&goodsNo="+goodsNo);
		}
		
		function submitExport(){
				submitAction(document.forms[0], "exportinputInvoiceInfo.action");
		}
		 
		function submitdelte(a,b,c){
				submitAction(document.forms[0], "deleteInnovation.action?billId="+a+"&dealNo="+b+"&vendId="+c);
			 parent.location.reload();
				
		}
	function b(){
		var selectTransTypes=document.getElementsByName("selectTransTypes");
		var ids="";
		for (var i = 0; i < selectTransTypes.length; i++){
					if (selectTransTypes[i].checked){
						ids = ids === "" ? selectTransTypes[i].value : ids + "," + selectTransTypes[i].value;
						
					}
				}
		
		return ids;
	}
	</script>
	
</head>
<body scroll="no" style="overflow:hidden;">
<form id="main" action="getBusitoList.action" method="post" enctype="multipart/form-data">
<input id ="taxno" name ="taxno" type="hidden" value="<s:property value="taxno" />"/>
<input id ="goodsNo" name ="goodsNo" type="hidden" value="<s:property value="goodsNo" />"/>
<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
	<tr>
	<td align="left">
		交易编码:
	</td>
	<td>
		<input type="text" class="tbl_query_text" id="transType" name="transType" value="<s:property value='transType'/>" maxlength="20" />
	</td>
	<td align="left">
		交易名称:
	</td>
	<td>
		<input type="text" class="tbl_query_text" id="transName" name="transName" value="<s:property value='transName'/>" maxlength="100"/>
	</td>
	<td style="width:200px;" colspan="4">
		<input type="button" class="tbl_query_button" value="查询"
			onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
			name="BtnView" id="BtnView" onclick="setBusiList()" />
	</td>
	</tr>
</table>
<div style="height:197px; overflow:auto; border-top:#D6D6D6 solid 1px;">
<table  class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0"  style="border-collapse: collapse;width: 100%;">
	<tr class="lessGrid head">
	    <th width="4%" style="text-align:center">
			<input id="CheckAll" style="width: 13px; height: 13px;" type="checkbox" onClick="checkAll(this,'selectTransTypes')" />
		</th>
		<th style="text-align:center">交易编码</th>
		<th style="text-align:center">交易名称</th>
	</tr>
	<s:iterator value="paginationList.recordList" id="iList" status="stuts">
		<tr align="center"class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
			<td align="center" >
			<input type="checkbox" style="width: 13px; height: 13px;" name="selectTransTypes" value="<s:property value="transType"/>" />
			<%-- <input type="checkbox" style="display:none" name="selectTransTypes" value="<s:property value="transType" />"/> --%>
			</td>
			<td align="center">
				<s:property value="transType" />
			</td>
			<td align="center">
				<s:property value="transName" />
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
	
<script type="text/javascript">
</script>
</html>