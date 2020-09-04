<%@ page language="java" import="java.util.*"
	import="com.cjit.vms.trans.model.BillInfo"
	import="com.cjit.vms.trans.model.BillItemInfo"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires",0);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html> 
<head>
<base target="_self">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../page/include.jsp"%>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
<link href="<%=webapp%>/page/subWindow.css" type="text/css" rel="stylesheet">
<title>票据预开-<s:if test="updFlg==0">新增</s:if><s:elseif test="updFlg==1">编辑</s:elseif>明细</title>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script type="text/javascript" src="<%=bopTheme%>/js/validator.js"></script>
<!-- MessageBox -->
<script src="<%=bopTheme%>/js/MessageBox/js/messageBox.js" type="text/javascript"></script>
<link href="<%=bopTheme%>/js/MessageBox/css/messageBox.css" rel="stylesheet" type="text/css" />
	
<script type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript">
//标识页面是否已提交
var subed = false;
function checkForm(){

    //验证是否重复提交
    if (subed == true){
       alert("信息正在发送给服务器，请不要重复提交信息！");
       return false;
    }
    
    // 商品名称
	if(fucCheckNull(document.getElementById("billItem.goodsName"),"请选择商品名称。")==false){
		return false;
	}
    
	// 数量
	if(fucCheckNull(document.getElementById("billItem.goodsNo"),"请输入商品数量。")==false){
		return false;
	}
    if (/^\d+$/.test(document.getElementById("billItem.goodsNo").value) == false){
    	alert("请输入正确的商品数量。");
		return false;
    }
    
    // 金额
	if(fucCheckNull(document.getElementById("billItem.amt"),"请输入金额。")==false){
		return false;
	}
    if (/^-?\d+\.?\d{0,2}$/.test(document.getElementById("billItem.amt").value) == false){
    	alert("请输入正确的金额。最多保留2位小数");
		return false;
    }
	
    subed=true;
    return true;
}

function submitForm(){
	var billItemId = document.getElementById("billItem.billItemId").value;
	if(true == checkForm()){
		if(billItemId!=""&&billItemId!=null){
			document.forms[0].action = "<%=webapp%>/savePreBillItem.action?open=open";
		}else{
			document.forms[0].action = "<%=webapp%>/savePreBillItem.action";
		}
			document.forms[0].submit();
	}else {
	    subed = false;
	}
}
function setPrice(){
	// 单价         =  金额/数量，保留6位小数
	var amt = document.getElementById("billItem.amt").value;
	var goodsNo = document.getElementById("billItem.goodsNo").value;
	//var taxFlag = document.getElementById("billItem.taxFlag").value;
	var taxFlag = $(".taxFlag option:selected").val();
	if (amt == "" || goodsNo == ""){
		document.getElementById("billItem.goodsPrice").value = "0";
	}else {
		if ("Y" == taxFlag) {
			var taxAmt = document.getElementById("billItem.taxAmt").value;
			document.getElementById("billItem.goodsPrice").value = changeTwoDecimal((amt-taxAmt) / goodsNo, 1000000);
		}else{
			document.getElementById("billItem.goodsPrice").value = changeTwoDecimal(amt / goodsNo, 1000000);
		}
		//document.getElementById("billItem.goodsPrice").value = changeTwoDecimal(amt/goodsNo, 1000000);
	}
}
function setTaxRate(){
	// 税率   =   金额*税率，保留2位小数
	var amt = document.getElementById("billItem.amt").value;
	var taxRate = document.getElementById("taxRate").value;
	if (amt == "" || taxRate == ""){
		document.getElementById("billItem.taxRate").value = "0";
	}else {
		//document.getElementById("billItem.taxRate").value = changeTwoDecimal(amt*taxRate, 100);
		document.getElementById("billItem.taxRate").value = changeTwoDecimal(taxRate,100);
	}
}
function setTaxAmt(){
	// 保留2位小数			
	//      是否含税为Y时，取值为[金额/(1+税率)]*税率
	//      是否含税为N时，取值为金额*税率
	var amt = document.getElementById("billItem.amt").value;
	var taxRate = document.getElementById("taxRate").value;
	var taxFlag = document.getElementById("billItem.taxFlag").value;

	if (taxFlag == "Y"){
		if (amt == "" || taxRate == ""){
			document.getElementById("billItem.taxAmt").value = "0";
		}else {
			document.getElementById("billItem.taxAmt").value = changeTwoDecimal((amt/(1+taxRate*1))*taxRate, 100);
		}
	}else if (taxFlag == "N"){
		if (amt == "" || taxRate == ""){
			document.getElementById("billItem.taxAmt").value = "0";
		}else {
			document.getElementById("billItem.taxAmt").value = changeTwoDecimal(amt*taxRate, 100);
		}
	}
}
function changeTwoDecimal(x,y){
	var f_x = parseFloat(x);
	if (isNaN(f_x)){
		return 0;
	}
	f_x = Math.round(f_x *y)/y;
	return f_x;
}
</script>
</head>
<body scroll="no" style="overflow:hidden;">
<div class="showBoxDiv">

<form id="frm" action="<c:out value='${webapp}'/>/savePreBillItem.action" method="post">
<%
	BillItemInfo billItem = (BillItemInfo)request.getAttribute("billItem");
	String billId = (String)request.getAttribute("billItem.billId");
	String billItemId = (String)request.getAttribute("billItem.billItemId");
	String taxno = (String)request.getAttribute("taxno");
	String fapiaoType = (String)request.getAttribute("fapiaoType");
%>
<input type="hidden" id="billItem.billId" name="billItem.billId" value="<%=billId%>" />
<input type="hidden" id="billId" name="billId" value="<%=billId%>" />
<input type="hidden" id="billItem.billItemId" name="billItem.billItemId" value="<%=billItemId%>" />
<input type="hidden" name="updFlg" id="updFlg" value="<s:property value="updFlg" />" />
<input type="hidden" name="taxno" id="taxno" value="<%=taxno%>" />
<input type="hidden" name="fapiaoType" id="fapiaoType" value="<%=fapiaoType%>" />
<input type="hidden" name="taxId" id="taxId" value="<s:property value="taxId" />" />
<input type="hidden" name="taxRate" id="taxRate" value="<s:property value="taxRate" />" />
<div id="editsubpanel" class="editsubpanel">
<div style="overflow: auto; " >  
<table id="contenttable" class="lessGrid" cellspacing="0"
	width="100%" align="center" cellpadding="0">
	<tr class="header">
		<th colspan="10">
			票据明细信息
		</th>
	</tr>
<%--	<tr>--%>
<%--		<td class="contnettable-subtitle" colspan="10">发票基本信息<s:property value="testv" /></td>--%>
<%--	</tr>--%>
	<tr>
		<td width="6%" style="text-align: right" class="listbar">商品名称</td>
		<td width="12%">
			<select id="billItem.goodsName" name="billItem.goodsName" >
				<%
				Map mapGoodsList = (Map)request.getAttribute("mapGoodsList");
				if (mapGoodsList != null && mapGoodsList.size() > 0){
					
	 				Set keySet = mapGoodsList.keySet();
	 				for(Iterator iterator = keySet.iterator(); iterator.hasNext();){
	 					String key = (String) iterator.next();
						%>
						<option value="<%=mapGoodsList.get(key) %>" <%if (mapGoodsList.get(key).equals(billItem.getGoodsName())) {%>selected<%} %> ><%=mapGoodsList.get(key) %></option>
						<%
					}
				}
				%>
			</select>
		</td>
		<td width="6%" style="text-align: right" class="listbar">规格型号</td>
		<td width="12%">
			<input type="text" class="tbl_query_text" id="billItem.specandmodel" name="billItem.specandmodel" value="<%if (billItem.getSpecandmodel() != null){%><%=billItem.getSpecandmodel()%><%}%>" maxlength="36" size="36" />
		</td>
		</tr><tr>
		<td width="5%" style="text-align: right" class="listbar">单位</td>
		<td width="12%">
			<input type="text" class="tbl_query_text" id="billItem.goodsUnit" name="billItem.goodsUnit" value="<%if (billItem.getGoodsUnit() != null){%><%=billItem.getGoodsUnit()%><%}%>" maxlength="14" size="14" />
		</td>
		<td width="6%" style="text-align: right" class="listbar">数量</td>
		<td width="12%">
			<input type="text" class="tbl_query_text" id="billItem.goodsNo" name="billItem.goodsNo" value="<%if (billItem.getGoodsNo() != null){%><%=billItem.getGoodsNo()%><%}%>" size="16" maxlength="16" onChange="setPrice();" />
		</td>
		</tr><tr>
		<td width="5%" style="text-align: right" class="listbar">单价</td>
		<td width="12%">
			<input type="text" class="tbl_query_text" id="billItem.goodsPrice" name="billItem.goodsPrice" value="<%if (billItem.getGoodsPrice() != null){%><%=billItem.getGoodsPrice()%><%}%>" readonly class="readonly" />
		</td>
		<td width="6%" style="text-align: right" class="listbar">金额</td>
		<td width="12%">
			<input type="text" class="tbl_query_text" id="billItem.amt" name="billItem.amt" value="<%if (billItem.getAmt() != null){%><%=billItem.getAmt()%><%}%>" size="17" maxlength="17" onChange="setTaxRate();setTaxAmt();setPrice();"/>
		</td>
		</tr>
		<tr>
		<td width="6%" style="text-align: right" class="listbar">税目</td>
		<td width="12%">
			<input type="text" class="tbl_query_text" id="billItem.taxItem" name="billItem.taxItem" value="<%if (billItem.getTaxItem() != null){%><%=billItem.getTaxItem()%><%}%>" readonly class="readonly" />
		</td>
		<td width="5%" style="text-align: right" class="listbar">税率</td>
		<td width="12%">
			<input type="text" class="tbl_query_text" id="billItem.taxRate" name="billItem.taxRate" value="<%if (billItem.getTaxRate() != null){%><%=billItem.getTaxRate()%><%}%>" readonly class="readonly" />
		</td>
		</tr>
		<tr>
		<td width="5%" style="text-align: right" class="listbar">税额</td>
		<td width="12%">
			<input type="text" class="tbl_query_text" id="billItem.taxAmt" name="billItem.taxAmt" value="<%if (billItem.getTaxAmt() != null){%><%=billItem.getTaxAmt()%><%}%>" readonly class="readonly" />
		</td>
		<td></td>
		<td></td>
	</tr>
</table>
</div>
<div id="ctrlbutton" class="ctrlbutton" style="border:0px">
	<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="submitForm();" name="BtnSave" value="保存" id="BtnSave"/>
<!--	<input type="reset" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" name="BtnReset" value="重置" id="BtnReset"/>-->
	<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="CloseWindow();" name="BtnReturn" value="关闭" id="BtnReturn"/>
</div>

</form>
</div>
</body>

</html>
