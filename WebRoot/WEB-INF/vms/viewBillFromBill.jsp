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
		function revoke(){
			//var billId = document.forms[0].elements['billInfo.billId'].value;
			submitAction(document.forms[0], "listBillTrack.action?fromFlag=menu&flag=track");
		}
	</script>
	<style type="text/css">
		.style1{
			width: 242px;
		}
	</style>
</head>
<body onLoad="javascript:tips()" onmousemove="MM(event)" onmouseout="MO(event)">
	<form name="Form1" method="post" action="backBill.action" id="Form1">
	<input type="hidden" name="billInfo.billId" value="<s:property value='billInfo.billId'/>"/>
	<input type="hidden" name="flag" id="flag" value="<s:property value='flag'/>"/>
	<table id="tbl_main" cellpadding=0 cellspacing=0>
	<tr>
		<td align="left">
		<table id="tbl_current_status" >
		<tr style="margin-left:-100px">
			<td>
				<img src="<%=bopTheme2%>/img/jes/icon/home.png" />
				<span class="current_status_menu">当前位置：</span>
				<span class="current_status_submenu">销项税管理
					<span class="actionIcon">-&gt;</span>发票跟踪
					<span class="actionIcon">-&gt;</span>查看票据
				</span>
			</td>
		</tr>
		</table>
		</td>
	</tr>
	</table>
	<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0" >
	<tr class="row1">
		<td align="right">
			客户纳税人名称:&nbsp;
		</td>
		<td align="left" colspan="5">
			<s:property value='billInfo.customerName'/>
		</td>
	</tr>
	<tr class="row1">
		<td align="right">
			客户纳税人识别号:&nbsp;
		</td>
		<td align="left" colspan="5">
			<s:property value='billInfo.customerTaxno'/>
		</td>
	</tr>
	<tr class="row1">
		<td align="right">
			客户地址电话:&nbsp;
		</td>
		<td align="left" colspan="5">
			<s:property value='billInfo.customerAddressandphone'/>
		</td>
	</tr>
	<tr class="row1">
		<td align="right">
			客户银行账号:&nbsp;
		</td>
		<td align="left" colspan="5">
			<s:property value='billInfo.customerBankandaccount'/>
		</td>
	</tr>
	<tr class="row1">
		<td valign="top" colspan="6">
		<table id="lessGridList" class="lessGrid" cellspacing="0" rules="all" border="1" display="none" width="100%">
		<tr class="fixedrowheader head">
			<th>商品名称</th>
			<th>规格型号</th>
			<th>商品数量</th>
			<th>商品单价</th>
			<th>含税标志</th>
			<th>金额</th>
			<th>税率</th>
			<th>税额</th>
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
			<!--商品名称-->
			<td><%=billItem.getGoodsName()%></td>
			<!--规格型号-->
			<td><%=billItem.getSpecandmodel()%></td>
			<!--商品数量-->
			<td><%=billItem.getGoodsNo()%></td>
			<!--商品单价-->
			<td><%=NumberUtils.format(billItem.getGoodsPrice(),"",2)%></td>
			<!--含税标志-->
			<td><%=DataUtil.getYOrNCH(billItem.getTaxFlag())%></td>
			<!--金额-->
			<td><%=NumberUtils.format(billItem.getAmt(),"",2)%></td>
			<!--税率-->
			<td><%=NumberUtils.format(billItem.getTaxRate(),"",4)%></td>
			<!--税额-->
			<td><%=NumberUtils.format(billItem.getTaxAmt(),"",2)%></td>
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
	<tr class="row1">
		<td align="right">
			价税合计:&nbsp;
		</td>
		<td width="20%">
			<s:property value='billInfo.sumAmt'/>
		</td>
		<td align="right">
			合计金额:&nbsp;
		</td>
		<td width="20%">
			<s:property value='billInfo.amtSum'/>
		</td>
		<td align="right">
			合计税额:&nbsp;
		</td>
		<td width="20%">
			<s:property value='billInfo.taxAmtSum'/>
		</td>
	</tr>
	<tr class="row1">
		<td align="right">
			我方开票机构:&nbsp;
		</td>
		<td colspan="3">
			<s:if test="authInstList != null && authInstList.size > 0">
			<s:select name="instCode" list="authInstList" listKey='id' listValue='name' onchange="loadOurInfo(this.value)" disabled="true"/>
			</s:if>
			<s:if test="authInstList == null || authInstList.size == 0">
			<select name="instCode" class="readOnlyText">
			<option value="">请分配机构权限</option>
			</select>
			</s:if>
		</td>
		<td align="right" rowspan="5">
			备注:&nbsp;
		</td>
		<td rowspan="5">
			<textarea name="remark" id="remark" cols="50" rows="7" disabled="disabled"><s:property value='billInfo.remark'/></textarea>
		</td>
	</tr>
	<tr class="row1">
		<td align="right">
			我方纳税人名称:&nbsp;
		</td>
		<td colspan="3">
			<s:property value='billInfo.name'/>
		</td>
	</tr>
	<tr class="row1">
		<td align="right">
			我方纳税人识别号:&nbsp;
		</td>
		<td colspan="3">
			<s:property value='billInfo.taxno'/>
		</td>
	</tr>
	<tr class="row1">
		<td align="right">
			我方地址电话:&nbsp;
		</td>
		<td colspan="3">
			<s:property value='billInfo.addressandphone'/>
		</td>
	</tr>
	<tr class="row1">
		<td align="right">
			我方银行账号:&nbsp;
		</td>
		<td colspan="3">
			<s:property value='billInfo.bankandaccount'/>
		</td>
	</tr>
	<tr class="row1">
		<td align="right">
			收款人:&nbsp;
		</td>
		<td>
			<s:property value='billInfo.payee'/>
		</td>
		<td align="right">
			复核人:&nbsp;
		</td>
		<td>
			<s:property value='billInfo.reviewerName'/>
		</td>
		<td align="right">
			开票人:&nbsp;
		</td>
		<td>
			<s:property value='billInfo.drawerName'/>
		</td>
	</tr>
	</table>
	<table id="tbl_tools" width="100%" border="0">
		<tr>
			<td align="left">
				<input type="button" class="tbl_query_button" value="关闭"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="btCancel" id="btCancel" onclick="revoke()" />
			</td>
		</tr>
	</table>
	<script language="javascript" type="text/javascript">
	</script>
	</form>
</body>
</html>