<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires",0);
%>
<%@ include file="../../../page/modalPage.jsp"%>
<%@ include file="../../../page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<!-- <title><s:if test="user.userId != null && user.userId != '' ">修改交易种类</s:if><s:else>新增交易种类</s:else></title> -->
	<title>进项税综合查询信息明细</title>
	<!-- <link type="text/css" href="<c:out value="${bopTheme}"/>/css/subWindow.css" rel="stylesheet"> -->
	<link href="<%=bopTheme2%>/css/subWindow.css" rel="stylesheet">
	<link href="<%=bopTheme2%>/css/inputInvoice.css" rel="stylesheet">
<body onload="load()">
<div class="showBoxDiv">
<form name="formBusiness" id="formBusiness">

<div id="editsubpanel" class="editsubpanel">
<div style="overflow: auto; width: 100%;">    
<input type="hidden" name="editType" id="editType" value="add" />
<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0" >
	<tr class="header">
		<th colspan="4">
			票据查看
		</th>
	</tr>
	
	<tr>
		<td align="right" class="listbar">发票代码:</td>
		<td><s:property value='inputBills.billCode'/></td>
		<td align="right" class="listbar">发票号码:</td>
		<td><s:property value='inputBills.billNo'/></td>
	</tr>
	
	<tr>
	<td align="right" class="listbar">开票日期:</td>
		<td><s:property value='inputBills.billDate'/></td>
	</tr>
	<tr>
		<td width="15%" align="right" class="listbar">供应商中文名:</td>
		<td>
			<s:property value='inputBills.vendorName'/> 
			
		</td>
		<td width="15%" align="right" class="listbar">供应商纳税人识别号:</td>
		<td width="35%">
				<s:property value='inputBills.vendorTaxNo'/> 
           
		</td>
	</tr>
	<tr>
		<td width="15%" align="right" class="listbar">供应商电话:</td>
		<td>
			<s:property value='inputBills.vendorName'/> 
			
		</td>
		<td width="15%" align="right" class="listbar">供应商地址:</td>
		<td width="35%">
				<s:property value='inputBills.vendorTaxNo'/> 
           
		</td>
	</tr>
	<tr>
		<td width="15%" align="right" class="listbar">供应商银行账号:</td>
		<td>
			<s:property value='inputBills.vendorName'/> 
			
		</td>
		<td width="15%" align="right" class="listbar">价税合计金额:</td>
		<td width="35%">
				<s:property value='inputBills.vendorTaxNo'/> 
           
		</td>
	</tr>
	<tr>
		<td align="right" class="listbar">合计金额:</td>
		<td><s:property value='inputBills.amtCny'/>
		</td>
		<td align="right" class="listbar">合计税额:</td>
		<td><s:property value='inputBills.taxAmtCny'/>
		    </td>
	</tr>
	
	<tr class="header">
		<th colspan="4">
			票据信息
		</th>
	</tr>
	<tr>
		<td align="right" class="listbar">发票代码:</td>
		<td><s:property value='inputBills.billCode'/></td>
		<td align="right" class="listbar">发票号码:</td>
		<td><s:property value='inputBills.billNo'/></td>
	</tr>
	
	<tr>
		
		<td align="right" class="listbar">发票类型:</td>
		<td>
			<s:if test='inputBills.faPiaoType =="0"'>增值税专用发票</s:if><s:else>增值税普通发票</s:else>
		</td>
		<td align="right" class="listbar">开票日期:</td>
		<td><s:property value='inputBills.billDate'/></td>
	</tr>
	 
	
</table>
</div>
</div>
	<div id="ctrlbutton" class="ctrlbutton" style="border:0px">		

	<input type="button" class="tbl_query_button" 
		onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 
		onclick="window.close()" name="BtnReturn" value="关闭" id="BtnReturn"/>	
	</div>
	<script language="javascript" type="text/javascript">
	</script>
</form>
</div>
</body>
</html>