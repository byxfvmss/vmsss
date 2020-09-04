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
		var issave = false;
		var msg = '<s:property value="message"/>';
		function writeOff(){
			var vNoticeNo = document.forms[0].elements['noticeNo'].value;
			if (vNoticeNo == null || vNoticeNo == ''){
				alert("请填写通知单编号！");
				return false;
			}
			var vBillItemIds = document.forms[0].elements['billItemId'];
			var vOriGoodsNos = document.forms[0].elements['oriGoodsNo'];
			var vCancelGoodsNos = document.forms[0].elements['cancelGoodsNo'];
			var vDisRates = document.forms[0].elements['discountRate'];
			if (!isNaN(vBillItemIds.length)){
				for (var i = 0; i < vBillItemIds.length; i++){
					if (eval(vCancelGoodsNos[i].value) > eval(vOriGoodsNos[i].value)){
						alert("第" + (i+1) + "行有误，红冲数量应小于或等于原商品明细数量！");
					}
				}
			}else{
				if (vCancelGoodsNos.value > vOriGoodsNos.value){
					alert("红冲数量应小于或等于原商品明细数量！");
				}
			}
			submitAction(document.forms[0], "writeOffBill.action");
		}
		function revoke(){
			//var billId = document.forms[0].elements['billInfo.billId'].value;
			submitAction(document.forms[0], "listBillTrack.action?fromFlag=menu&flag=track");
		}
		function loadOurInfo(instCode){
			var webroot = '<%=webapp%>';
			var url = webroot + "/loadOurInfo.action";
			var param = "instCode=" + document.getElementById("instCode").value;
			var result = sendXmlHttpPost(url, param);
			var vOurInfos = result.split("###");
			if (vOurInfos.length == 6){
				if (document.getElementById("billInfo.name") != null){
					document.getElementById("billInfo.name").value = vOurInfos[0];
				}
				if (document.getElementById("billInfo.taxno") != null){
					document.getElementById("billInfo.taxno").value = vOurInfos[1];
				}
				if (document.getElementById("billInfo.addressandphone") != null){
					document.getElementById("billInfo.addressandphone").value = vOurInfos[2] + " " + vOurInfos[3];
				}
				if (document.getElementById("billInfo.bankandaccount") != null){
					document.getElementById("billInfo.bankandaccount").value = vOurInfos[4] + " " + vOurInfos[5];
				}
			}
		}
		function loadBillAmtInfo(){
			var webroot = '<%=webapp%>';
			var url = webroot + "/loadBillAmtInfo.action";
			var billId = document.forms[0].elements['billInfo.billId'].value;
			if (billId == null || billId == ''){
				return false;
			}
			var result = sendXmlHttpPost(url, "billId=" + billId);
			var vAmtInfos = result.split("###");
			if (vAmtInfos.length == 3){
				if (document.getElementById("billInfo.amtSum") != null){
					document.getElementById("billInfo.amtSum").value = vAmtInfos[0];
				}
				if (document.getElementById("billInfo.taxAmtSum") != null){
					document.getElementById("billInfo.taxAmtSum").value = vAmtInfos[1];
				}
				if (document.getElementById("billInfo.sumAmt") != null){
					document.getElementById("billInfo.sumAmt").value = vAmtInfos[2];
				}
			}
		}
	</script>
	<style type="text/css">
		.style1{
			width: 242px;
		}
	</style>
</head>
<body onLoad="javascript:tips()" onmousemove="MM(event)" onmouseout="MO(event)">
	<form name="Form1" method="post" action="saveBill.action" id="Form1">
	<input type="hidden" name="billId" value="<s:property value='billInfo.billId'/>"/>
	<input type="hidden" name="billInfo.billId" value="<s:property value='billInfo.billId'/>"/>
	<input type="hidden" name="billInfo.dataStatus" value="<s:property value='billInfo.dataStatus'/>"/>
	<input type="hidden" name="submitFlag" id="submitFlag" value="U"/>
	<table id="tbl_main" cellpadding=0 cellspacing=0>
	<tr>
		<td align="left">
		<table id="tbl_current_status" >
		<tr style="margin-left:-100px">
			<td>
				<img src="<%=bopTheme2%>/img/jes/icon/home.png" />
				<span class="current_status_menu">当前位置：</span>
				<span class="current_status_submenu">销项税管理
					<span class="actionIcon">-&gt;</span>发票红冲
				</span>
			</td>
		</tr>
		</table>
		</td>
	</tr>
	</table>
	<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0" >
	<tr class="row1">
		<td align="right" width="10%">
			发票代码:&nbsp;
		</td>
		<td align="left">
			<s:property value='billInfo.billCode'/>
		</td>
		<td align="right" width="10%">
			开票日期:&nbsp;
		</td>
		<td align="left">
			<s:property value='billInfo.billDate'/>
		</td>
		<td align="right" width="10%">
			发票号码:&nbsp;
		</td>
		<td align="left">
			<s:property value='billInfo.billNo'/>
		</td>
	</tr>
	<tr class="row1">
		<td align="right" width="10%">
			客户纳税人名称:&nbsp;
		</td>
		<td align="left" colspan="3">
			<input type="text" name="billInfo.customerName" id="billInfo.customerName" value="<s:property value='billInfo.customerName'/>" size="100"/>
		</td>
		<td align="right" width="10%">
			通知单编号:&nbsp;
		</td>
		<td align="left" width="20%">
			<input type="text" name="noticeNo" id="noticeNo" value="<s:property value='billInfo.noticeNo'/>" maxlength="16" size="40"/>
		</td>
	</tr>
	<tr class="row1">
		<td align="right">
			客户纳税人识别号:&nbsp;
		</td>
		<td align="left" colspan="5">
			<input type="text" name="billInfo.customerTaxno" id="billInfo.customerTaxno" value="<s:property value='billInfo.customerTaxno'/>" size="100"/>
		</td>
	</tr>
	<tr class="row1">
		<td align="right">
			客户地址电话:&nbsp;
		</td>
		<td align="left" colspan="5">
			<input type="text" name="billInfo.customerAddressandphone" id="billInfo.customerAddressandphone" value="<s:property value='billInfo.customerAddressandphone'/>" size="100"/>
		</td>
	</tr>
	<tr class="row1">
		<td align="right">
			客户银行账号:&nbsp;
		</td>
		<td align="left" colspan="5">
			<input type="text" name="billInfo.customerBankandaccount" id="billInfo.customerBankandaccount" value="<s:property value='billInfo.customerBankandaccount'/>" size="100"/>
		</td>
	</tr>
	<tr class="row1">
		<td valign="top" colspan="6">
		<table id="lessGridList" class="lessGrid" cellspacing="0" rules="all" border="1" display="none" width="100%">
		<tr class="fixedrowheader head">
			<th>交易类型</th>
			<th>商品名称</th>
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
			<!--交易类型-->
			<td><%=billItem.getTransTypeName()%></td>
			<!--商品名称-->
			<td><%=billItem.getGoodsName()%></td>
			<!--规格型号-->
			<td><%=billItem.getSpecandmodel()%></td>
			<!--商品数量-->
			<td>
				<input type="text" name="cancelGoodsNo" value="<%=billItem.getGoodsNo()%>" />
			</td>
			<!--商品单价-->
			<td><%=NumberUtils.format(billItem.getGoodsPrice(),"",2)%></td>
			<!--含税标志-->
			<td><%=billItem.getTaxFlag()%></td>
			<!--金额-->
			<td><%=NumberUtils.format(billItem.getAmt(),"",2)%></td>
			<!--税率-->
			<td><%=NumberUtils.format(billItem.getTaxRate(),"",4)%></td>
			<!--税额-->
			<td><%=NumberUtils.format(billItem.getTaxAmt(),"",2)%></td>
			<!-- 折扣率 -->
			<td><%=DataUtil.showPercent(billItem.getDiscountRate())%></td>
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
			合计金额:&nbsp;
		</td>
		<td>
			<input type="text" name="billInfo.amtSum" id="billInfo.amtSum" value="<s:property value='billInfo.amtSum'/>"
				onfocus="loadBillAmtInfo();" onkeypress="checkkey(value);" onblur="formatAmount(this,2,0,'true');"/>
		</td>
		<td align="right">
			合计税额:&nbsp;
		</td>
		<td>
			<input type="text" name="billInfo.taxAmtSum" id="billInfo.taxAmtSum" value="<s:property value='billInfo.taxAmtSum'/>"
				onfocus="loadBillAmtInfo();" onkeypress="checkkey(value);" onblur="formatAmount(this,2,0,'true');"/>
		</td>
		<td align="right">
			价税合计:&nbsp;
		</td>
		<td>
			<input type="text" name="billInfo.sumAmt" id="billInfo.sumAmt" value="<s:property value='billInfo.sumAmt'/>"
				onfocus="loadBillAmtInfo();" onkeypress="checkkey(value);" onblur="formatAmount(this,2,0,'true');"/>
		</td>
	</tr>
	<tr class="row1">
		<td align="right">
			我方开票机构:&nbsp;
		</td>
		<td colspan="3">
			<s:if test="authInstList != null && authInstList.size > 0">
			<s:select name="instCode" list="authInstList" listKey='id' listValue='name' onchange="loadOurInfo(this.value)"/>
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
			<textarea name="remark" id="remark" cols="60" rows="7"><s:property value='billInfo.remark'/></textarea>
		</td>
	</tr>
	<tr class="row1">
		<td align="right">
			我方纳税人名称:&nbsp;
		</td>
		<td colspan="3">
			<input type="text" name="billInfo.name" id="billInfo.name" value="<s:property value='billInfo.name'/>" size="100"/>
		</td>
	</tr>
	<tr class="row1">
		<td align="right">
			我方纳税人识别号:&nbsp;
		</td>
		<td colspan="3">
			<input type="text" name="billInfo.taxno" id="billInfo.taxno" value="<s:property value='billInfo.taxno'/>" size="100"/>
		</td>
	</tr>
	<tr class="row1">
		<td align="right">
			我方地址电话:&nbsp;
		</td>
		<td colspan="3">
			<input type="text" name="billInfo.addressandphone" id="billInfo.addressandphone" value="<s:property value='billInfo.addressandphone'/>" size="100"/>
		</td>
	</tr>
	<tr class="row1">
		<td align="right">
			我方银行账号:&nbsp;
		</td>
		<td colspan="3">
			<input type="text" name="billInfo.bankandaccount" id="billInfo.bankandaccount" value="<s:property value='billInfo.bankandaccount'/>" size="100"/>
		</td>
	</tr>
	<tr class="row1">
		<td align="right">
			收款人:&nbsp;
		</td>
		<td>
			<input type="text" name="billInfo.payee" id="billInfo.payee" value="<s:property value='billInfo.payee'/>"/>
		</td>
		<td align="right">
			复核人:&nbsp;
		</td>
		<td>
			<s:property value='billInfo.reviewerName'/>
			<input type="hidden" name="billInfo.reviewer" id="billInfo.reviewer" value="<s:property value='billInfo.reviewer'/>"/>
		</td>
		<td align="right">
			开票人:&nbsp;
		</td>
		<td>
			<s:property value='billInfo.drawerName'/>
			<input type="hidden" name="billInfo.drawer" id="billInfo.drawer" value="<s:property value='billInfo.drawer'/>"/>
		</td>
	</tr>
	</table>
	<div id="ctrlbutton" class="ctrlbutton" style="border:0px">
		<input type="button" class="tbl_query_button" value="红冲"
			onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
			name="btWriteOff" id="btWriteOff" onclick="writeOff()" />
		<input type="button" class="tbl_query_button" value="取消"
			onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
			name="btCancel" id="btCancel" onclick="revoke()" />
	</div>
	<script language="javascript" type="text/javascript">
	</script>
	</form>
</body>
</html>