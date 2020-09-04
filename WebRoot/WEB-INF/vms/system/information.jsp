<!-- file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.vms.input.model.InformationInput"
	import="com.cjit.vms.input.model.InputInvoice"
	import="com.cjit.vms.input.model.InputInvoiceItem"
	import="com.cjit.vms.input.model.InformationBills"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
	<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
	<link href="<%=bopTheme2%>/css/subWindow.css" rel="stylesheet">
	<link href="<%=bopTheme2%>/css/inputInvoice.css" rel="stylesheet">
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
<body onload="load()">
<div class="showBoxDiv">
<form name="formBusiness" id="formBusiness" style="background-color:#fff">

<div id="editsubpanel" class="editsubpanel" > 
<input type="hidden" name="editType" id="editType" value="add" />
<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0" >
	<tr class="header">
		<th colspan="14">
			交易信息
		</th>
	</tr>
	<tr>
		<td width="30%" align="center" class="listbar">交易时间:</td>
		<td >
				<s:property value='informationInput.transDate'/> 
           
		</td>
		<td colspan="4"></td>
		<td width="30%" align="center" class="listbar">交易机构:</td>
		<td>
			<s:property value='informationInput.bankCode'/> 
			
		</td>
		
	</tr>
	<tr>
		<td width="30%" align="center" class="listbar">交易金额:</td>
		<td><s:property value='informationInput.amtCny'/>
		</td>
		<td colspan="4"></td>
		<td width="30%" align="center" class="listbar">交易编号:</td>
		<td>
			<s:property value='informationInput.dealNo'/> 
		</td>
	</tr>
	
	<tr>
	<td align="center" class="listbar">税额:</td>
		<td><s:property value='informationInput.taxAmtCny'/></td>
	</tr>
	<!-- 
	<tr class="header">
		<th colspan="14">
			票据信息
		</th>
	</tr>
	 -->
	<tr>
	
	
	
	<%
		List billsList = (List)request.getAttribute("billsList");
		List billItemsList = (List)request.getAttribute("billItemsList");
		if(billItemsList!=null&&billItemsList.size()>1){
	%>	
					<tr>
						<th style="text-align:center">商品名称</th>
						<th style="text-align:center">规格型号</th>
						<th style="text-align:center">单位</th>
						<th style="text-align:center">商品数量</th>
						<th style="text-align:center">商品单价</th>
						<th style="text-align:center">金额</th>
						<th style="text-align:center">税率</th>
						<th style="text-align:center">税额</th>
						<th style="text-align:center">票据行性质</th>
						<th style="text-align:center">折扣率</th>
					</tr>
			<%
						for(int i=0; i<billItemsList.size(); i++){
							InputInvoiceItem invoiceItems = (InputInvoiceItem)billItemsList.get(i);
							if(i%2==0){
			%>
			<tr class="lessGrid rowA">
			<%
							}else if(i%2!=0){
			%>
			<tr class="lessGrid rowB" >
			<%
							}
			%>
				<td align="center"><%=(invoiceItems.getGoodsName()==null)?"":invoiceItems.getGoodsName()%></td>
				<td align="center"><%=(invoiceItems.getSpecandmodel()==null)?"":invoiceItems.getSpecandmodel()%></td>
				<td align="center"><%=(invoiceItems.getGoodsUnit()==null)?"":invoiceItems.getGoodsUnit()%></td>
				<td align="center"><%=(invoiceItems.getGoodsNo()==null)?"":invoiceItems.getGoodsNo()%></td>
				<td align="center"><%=(invoiceItems.getGoodsPrice()==null)?"":invoiceItems.getGoodsPrice()%></td>
				<td align="center"><%=(invoiceItems.getAmt()==null)?"":invoiceItems.getAmt()%></td>
				<td align="center"><%=(invoiceItems.getTaxRate()==null)?"":invoiceItems.getTaxRate()%></td>
				<td align="center"><%=(invoiceItems.getTaxAmt()==null)?"":invoiceItems.getTaxAmt()%></td>
				<td align="center"><%=invoiceItems.getRowNature()==null?"":invoiceItems.getRowNature().equals("0")?"正常行":invoiceItems.getRowNature().equals("1")?"折扣行":"被折扣"%></td>
				<td align="center"><%=invoiceItems.getDiscountRate()==null?"":invoiceItems.getDiscountRate()%></td>
			</tr>
			<%
						}
		}else{
			if (billsList != null && billsList.size() > 0){
	%>
			<tr>
				<th style="text-align:center">发票代码</th>
				<th style="text-align:center">发票号码</th>
				<th style="text-align:center">开票日期</th>
				<th style="text-align:center">所属机构</th>
				<th style="text-align:center">金额</th>
				<th style="text-align:center">税额</th>
				<th style="text-align:center">发票种类</th>
				<th style="text-align:center">供应商名称</th>
				<th style="text-align:center">供应商纳税人识别号</th>
				<th style="text-align:center">认证日期</th>
				<th style="text-align:center">扫描时间</th>
			</tr>
	<%
				for(int i=0; i<billsList.size(); i++){
					InformationBills bills = (InformationBills)billsList.get(i);
					if(i%2==0){
	%>
	<tr class="lessGrid rowA">
	<%
					}else if(i%2!=0){
	%>
	<tr class="lessGrid rowB" >
	<%
					}
	%>
		<td align="center"><%=(bills.getBillCode()==null)?"":bills.getBillCode()%></td>
		<td align="center"><%=(bills.getBillNo()==null)?"":bills.getBillNo()%></td>
		<td align="center"><%=(bills.getBillDate()==null)?"":bills.getBillDate()%></td>
		<td align="center"><%=(bills.getInstCode()==null)?"":bills.getInstCode()%></td>
		<td align="center"><%=(bills.getAmtSum()==null)?"":NumberUtils.format(bills.getAmtSum(),"",2)%></td>
		<td align="center"><%=(bills.getTaxAmtSum()==null)?"":NumberUtils.format(bills.getTaxAmtSum(),"",2)%></td>
		<td align="center"><%=bills.getFaPiaoType()==null?"":bills.getFaPiaoType().equals("0")?"增值税专用发票":"增值税普通发票"%></td>
		<td align="center"><%=(bills.getVendorName()==null)?"":bills.getVendorName()%></td>
		<td align="center"><%=(bills.getVendorTaxNo()==null)?"":bills.getVendorTaxNo()%></td>
		<td align="center"><%=bills.getIdentifyDate()==null?"":bills.getIdentifyDate()%></td>
		<td align="center"><%=bills.getScanDate()==null?"":bills.getScanDate()%></td>
	</tr>
	<%
				}
			}
		}
	%>
	
	
</table>
</div>
	<div id="ctrlbutton" class="ctrlbutton" style="border:0px" width="120%">		

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