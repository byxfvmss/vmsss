<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.vms.trans.model.BillInfo"
	import="com.cjit.vms.trans.model.BillItemInfo"
	import="com.cjit.vms.trans.model.TransInfo"
	import="com.cjit.vms.trans.util.DataUtil"
	import="com.cjit.vms.trans.model.EmsInfo"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<title>增值税管理平台</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/editview.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/XmlHttp.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/vms.js"></script>
	<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
	<link href="<%=bopTheme2%>/css/editView.css" type="text/css" rel="stylesheet">
	<script language="javascript" type="text/javascript">
		var issave = false;
		var msg = '<s:property value="message"/>';
		function closeup(){
			submitAction(document.forms[0], "billEmsMessage.action?fromFlag=menu");
		}
	</script>
</head>
<body onmousemove="MM(event)" onmouseout="MO(event)">
	<form name="Form1" method="post" action="billEmsMessage.action" id="Form1">
	<input type="hidden" name="billId" value="<s:property value='billInfo.billId'/>"/>
	<input type="hidden" name="billInfo.billId" value="<s:property value='billInfo.billId'/>"/>
	<input type="hidden" name="billInfo.dataStatus" value="<s:property value='billInfo.dataStatus'/>"/>
	<input type="hidden" name="submitFlag" id="submitFlag" value="U"/>
	<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
	<tr>
		<td class="centercondition">
		<div id="tbl_current_status">
			<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> 
			<span class="current_status_menu">当前位置：</span> 
			<span class="current_status_submenu1">销项税管理</span>
			<span class="current_status_submenu">开票管理</span>
			<span class="current_status_submenu">发票快递 </span>
			<span class="current_status_submenu">快递查看</span>
		</div>
	<table id="tbl_tools" width="100%" border="0">
		<tr>
			<td align="left">
			<input type="button" class="tbl_query_button" value="返回"
			onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
			name="btCancel" id="btCancel" onclick="closeup()" />
			</td>
		</tr>
	</table>
	<table id="tbl_context" cellspacing="0" width="100%" align="center" cellpadding="0" >
	<tr class="row1">
		<td align="right" width="13%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;"	>
			开票日期:&nbsp;&nbsp;&nbsp;
		</td>
		<td align="left" width="20%">
			<input type="text" class="tbl_query_text2" name="emsInfo.billDate" id="emsInfo.billDate" value="<s:property value='billInfo.billDate'/>" onfocus="this.blur();" />
		</td>
		<td align="right" width="13%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;"	>
			发票代码:&nbsp;&nbsp;&nbsp;
		</td>
		<td align="left" width="20%">
			<input type="text" class="tbl_query_text2" name="emsInfo.billCode" id="emsInfo.billCode" value="<s:property value='billInfo.billCode'/>" onfocus="this.blur();" />
		</td>
		<td align="right" width="13%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;"	>
			发票号码:&nbsp;&nbsp;&nbsp;
		</td>
		<td align="left" width="21%">
			<input type="text" class="tbl_query_text2" name="emsInfo.billNo" id="emsInfo.billNo" value="<s:property value='billInfo.billNo'/>" onfocus="this.blur();" />
		</td>
	</tr>
	<tr class="row1">
		<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;"	>
			客户纳税人名称:&nbsp;&nbsp;&nbsp;
		</td>
		<td align="left">
			<input type="text" class="tbl_query_text2" name="emsInfo.customerName" id="emsInfo.customerName" value="<s:property value='billInfo.customerName'/>" onfocus="this.blur();" />
		</td>
		<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;"	>
			客户纳税人识别号:&nbsp;&nbsp;&nbsp;
		</td>
		<td colspan="3">
			<input type="text" class="tbl_query_text2" name="emsInfo.customerTaxno" id="emsInfo.customerTaxno" value="<s:property value='billInfo.customerTaxno'/>" onfocus="this.blur();" />
		</td>
	</tr>
	<tr class="row1">
		<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;"	>
			客户联系人:&nbsp;&nbsp;&nbsp;
		</td>
		<td align="left">
			<input type="text" class="tbl_query_text2" name="emsInfo.customerLinkman" id="emsInfo.customerLinkman" value="<s:property value='billInfo.customerLinkman'/>" onfocus="this.blur();"  />
		</td>
		<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;"	>
			客户收件人:&nbsp;&nbsp;&nbsp;
		</td>
		<td align="left">
			<input type="text" class="tbl_query_text2" name="emsInfo.addressee" id="emsInfo.addressee" value="<s:property value='billInfo.addressee'/>" onfocus="this.blur();"  />
		</td>
		<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;"	>
			客户收件人电话:&nbsp;&nbsp;&nbsp;
		</td>
		<td align="left">
			<input type="text" class="tbl_query_text2" name="emsInfo.addresseePhone" id="emsInfo.addresseePhone" value="<s:property value='billInfo.addresseePhone'/>" onfocus="this.blur();"  />
		</td>
	</tr>
	<tr class="row1">
		<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;"	>
			客户收件邮编:&nbsp;&nbsp;&nbsp;
		</td>
		<td align="left">
			<input type="text" class="tbl_query_text2" name="emsInfo.addresseeZipcode" id="emsInfo.addresseeZipcode" value="<s:property value='billInfo.addresseeZipcode'/>" onfocus="this.blur();" />
		</td>
		<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;"	>
			收件地址:&nbsp;&nbsp;&nbsp;
		</td>
		<td align="left"  colspan="3">
			<input type="text" class="tbl_query_text2" name="emsInfo.addresseeAddress" id="emsInfo.addresseeAddress" value="<s:property value='billInfo.addresseeAddress'/>" size="76" onfocus="this.blur();" />
		</td>
	</tr>
	<tr class="row1">
		<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;"	>
			寄件人:&nbsp;&nbsp;&nbsp;
		</td>
		<td>
			<input type="text" class="tbl_query_text2" name="emsInfo.sender" id="emsInfo.sender" value="<s:property value='emsInfo.sender'/>" onfocus="this.blur();" />
		</td>
		<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;"	>
			详细收件地址:&nbsp;&nbsp;&nbsp;
		</td>
		<td align="left" colspan="3">
			<input type="text" class="tbl_query_text2" name="emsInfo.addresseeAddressdetail" id="emsInfo.addresseeAddressdetail" value="<s:property value='billInfo.addresseeAddressdetail'/>" size="76" onfocus="this.blur();" />
		</td>
	</tr>
	<tr class="row1">
		<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;"	>
			快递公司:&nbsp;&nbsp;&nbsp;
		</td>
		<td>
			<input type="text" class="tbl_query_text2" name="emsInfo.fedexExpress" id="emsInfo.fedexExpress" value="<s:property value='emsInfo.fedexExpress'/>" onfocus="this.blur();" />
		</td>
		<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;"	>
			快递单号:&nbsp;&nbsp;&nbsp;
		</td>
		<td>
			<input type="text" class="tbl_query_text2" name="emsInfo.emsNo" id="emsInfo.emsNo" value="<s:property value='emsInfo.emsNo'/>" onfocus="this.blur();" />
		</td>
		<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;"	>
			快递状态:&nbsp;&nbsp;&nbsp;
		</td>
		<td>
			<select id="emsInfo.emsStatus" name="emsInfo.emsStatus" disabled="disabled">
					<option value="1" <s:if test='emsInfo.emsStatus=="1"'>selected</s:if><s:else></s:else>>打印已快递</option>
					<!-- <option value="2" <s:if test='emsInfo.emsStatus=="2"'>selected</s:if><s:else></s:else>>打印未快递</option> -->
					<option value="3" <s:if test='emsInfo.emsStatus=="3"'>selected</s:if><s:else></s:else>>已签收</option></select></td>
		</td>
	</tr>
	</table>
	<div id="lessGridList8" style="overflow: auto; width: 100%;">
		<table class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0" style="border-collapse: collapse; width: 100%;">
		<tr class="lessGrid head">
			<%--<th>交易类型</th>
			--%><th>商品名称</th>
			<th>规格型号</th>
			<th>商品数量</th>
			<th>商品单价</th>
			<th>含税标志</th>
			<th>金额</th>
			<th>税率</th>
			<th>税额</th>
			<th>折扣率</th>
		</tr>
	<%
		ArrayList billItemList = (ArrayList) request.getAttribute("billItemList");
		if (billItemList != null && billItemList.size() > 0){
			for (int i = 0; i < billItemList.size(); i++){
				BillItemInfo billItem = (BillItemInfo)billItemList.get(i);
	%>
		<tr>
			<input type="hidden" name="billItemId" value="<%=billItem.getBillItemId()%>" />
			<input type="hidden" name="oriGoodsNo" value="<%=billItem.getGoodsNo()%>" />
			<input type="hidden" name="discountRate" value="<%=billItem.getDiscountRate()==null?"1":billItem.getDiscountRate()%>" />
			<!--交易类型--><%--
			<td align="center"><%=billItem.getTransTypeName()==null?"":billItem.getTransTypeName()%></td>
			--%><!--商品名称-->
			<td align="center"><%=billItem.getGoodsName()%></td>
			<!--规格型号-->
			<td align="center"><%=billItem.getSpecandmodel()%></td>
			<!--商品数量-->
			<td align="center"><%=billItem.getGoodsNo()%></td>
			<!--商品单价-->
			<td align="center"><%=NumberUtils.format(billItem.getGoodsPrice(),"",2)%></td>
			<!--含税标志-->
			<td align="center"><%=billItem.getTaxFlag()%></td>
			<!--金额-->
			<td align="center"><%=NumberUtils.format(billItem.getAmt(),"",2)%></td>
			<!--税率-->
			<td align="center"><%=NumberUtils.format(billItem.getTaxRate(),"",4)%></td>
			<!--税额-->
			<td align="center"><%=NumberUtils.format(billItem.getTaxAmt(),"",2)%></td>
			<!-- 折扣率 -->
			<td align="center"><%=DataUtil.showPercent(billItem.getDiscountRate())%></td>
		</tr>
	<%
			}
		}else{
	%>
		<tr>
			<td colspan="100">
				无商品记录
			</td>
		</tr>
	<%
		}
	%>
		</table>
			</td>
	</tr>
	</table>
	</form>
</body>
</html>