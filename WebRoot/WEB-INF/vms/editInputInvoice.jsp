<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.vms.trans.model.BillInfo"
	import="com.cjit.vms.input.model.InputVatInfo"
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
//		function save(){
//			if(issave){
//				alert("数据保存中，请稍候...");
//				return false;
//			}
//			var isblank=true;
//			var oo = document.getElementById("Form1").getElementsByTagName("input");
//			for(var k = 0, len = oo.length; k < len; k++){
//				var t = oo[k].type;
//				if(t=="text" ){
//					var v=oo[k].value;
//					if(v&&v!="" &&v.replace(/(^\s*)|(\s*$)/g, "")!=""){
//						isblank=false;
//						break;
//					}
//				}
//			}
//			if (document.getElementById('submitFlag').value=='S'){
//				if (!submitBillCheck()){
//					return false;
//				}
//			}
//			if(!isblank){
//				document.getElementById("Form1").submit();
//				issave = true;
//			}else{
//				alert("表单没有输入数据,无需保存。 \n\n如要退出，请点击'返回'按钮");
//			}
//		}
		function save(){
			document.getElementById("Form1").submit();
		}
		function back(){
			submitAction(document.forms[0], "listInputInvoice.action?fromFlag=edit");
		}
		function loadOurInfo(instCode){
			var webroot = '<%=webapp%>';
			var url = webroot + "/loadOurInfo.action";
			var param = "instCode=" + document.getElementById("instCode").value;
			var result = sendXmlHttpPost(url, param);
			var vOurInfos = result.split("###");
			if (vOurInfos.length == 5){
				if (document.getElementById("inputVatInfo.name") != null){
					document.getElementById("inputVatInfo.name").value = vOurInfos[0];
				}
				if (document.getElementById("inputVatInfo.taxno") != null){
					document.getElementById("inputVatInfo.taxno").value = vOurInfos[1];
				}
				if (document.getElementById("inputVatInfo.addressandphone") != null){
					document.getElementById("inputVatInfo.addressandphone").value = vOurInfos[2] + " " + vOurInfos[3];
				}
				if (document.getElementById("inputVatInfo.bankandaccount") != null){
					document.getElementById("inputVatInfo.bankandaccount").value = vOurInfos[4];
				}
			}
		}
		function loadBillAmtInfo(){
			var webroot = '<%=webapp%>';
			var url = webroot + "/loadBillAmtInfo.action";
			var inVatId = document.forms[0].elements['inputVatInfo.inVatId'].value;
			if (inVatId == null || inVatId == ''){
				return false;
			}
			var result = sendXmlHttpPost(url, "billId=" + inVatId);
			var vAmtInfos = result.split("###");
			if (vAmtInfos.length == 3){
				if (document.getElementById("inputVatInfo.amt") != null){
					document.getElementById("inputVatInfo.amt").value = vAmtInfos[0];
				}
				if (document.getElementById("inputVatInfo.taxAmt") != null){
					document.getElementById("inputVatInfo.taxAmt").value = vAmtInfos[1];
				}
				if (document.getElementById("inputVatInfo.sumAmt") != null){
					document.getElementById("inputVatInfo.sumAmt").value = vAmtInfos[2];
				}
			}
		}
		// 计算税价合计
		function countSumAmt(){
			var vSumAmt = "0.00";
			if (document.getElementById("inputVatInfo.amt") != null
				&& document.getElementById("inputVatInfo.amt").value != ""){
				var vAmt = document.getElementById("inputVatInfo.amt").value;
				vSumAmt = eval(vSumAmt) + eval(vAmt);
			}
			if (document.getElementById("inputVatInfo.taxAmt") != null
				&& document.getElementById("inputVatInfo.taxAmt").value != ""){
				var vTaxAmt = document.getElementById("inputVatInfo.taxAmt").value;
				vSumAmt = eval(vSumAmt) + eval(vTaxAmt);
			}
			document.getElementById("inputVatInfo.sumAmt").value = vSumAmt.toFixed(2);
		}
	</script>
	<style type="text/css">
		.style1{
			width: 242px;
		}
	</style>
</head>
<body onLoad="javascript:tips()" onmousemove="MM(event)" onmouseout="MO(event)">
	<form name="Form1" method="post" action="saveInputInvoice.action" id="Form1">
	<input type="hidden" name="billId" value="<s:property value='inputInvoice.billId'/>"/>
	<input type="hidden" name="submitFlag" id="submitFlag" value="U"/>
	<table id="tbl_main" cellpadding=0 cellspacing=0>
	<tr>
		<td align="left">
		<table id="tbl_current_status" >
		<tr style="margin-left:-100px">
			<td>
				<img src="<%=bopTheme2%>/img/jes/icon/home.png" />
				<span class="current_status_menu">当前位置：</span>
				<span class="current_status_submenu">进项税管理
					<span class="actionIcon">-&gt;</span>进项税编辑
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
		<td align="left" width="15%">
			<input readonly class="readonly_input" type="text" name="inputInvoice.billCode" id="inputInvoice.billCode" value="<s:property value='inputInvoice.billCode'/>" />
		</td>
		<td align="right" width="10%">
			发票号码:&nbsp;
		</td>
		<td align="left" width="15%">
			<input readonly class="readonly_input" type="text" name="inputInvoice.billNo" id="inputInvoice.billNo" value="<s:property value='inputInvoice.billNo'/>" />
		</td>
		<td align="right" width="10%">
			开票日期:&nbsp;
		</td>
		<td align="left" width="15%">
			<input readonly class="readonly_input" class="tbl_query_time" id="inputInvoice.billDate" type="text" name="inputInvoice.billDate"
				value="<s:property value='inputInvoice.billDate'/>"/>
		</td>
		<td align="right" width="10%">
			购方纳税人名称:&nbsp;
		</td>
		<td align="left" width="15%">
			<input readonly class="readonly_input" type="text" name="inputInvoice.name" id="inputInvoice.name" value="<s:property value='inputInvoice.name'/>"/>
		</td>
	</tr>
	<tr class="row1">
		<td align="right">
			购方纳税人识别号:&nbsp;
		</td>
		<td align="left" width="15%">
			<input readonly class="readonly_input" type="text" name="inputInvoice.taxNo" id="inputInvoice.taxNo" value="<s:property value='inputInvoice.taxNo'/>"/>
		</td>
		<td align="right">
			购方地址电话:&nbsp;
		</td>
		<td align="left" width="15%">
			<input readonly class="readonly_input" type="text" name="inputInvoice.addressAndPhone" id="inputInvoice.addressAndPhone" value="<s:property value='inputInvoice.addressAndPhone'/>"/>
		</td>
		<td align="right">
			购方银行账号:&nbsp;
		</td>
		<td align="left" width="15%">
			<input readonly class="readonly_input" type="text" name="inputInvoice.bankAndAccount" id="inputInvoice.bankAndAccount" value="<s:property value='inputInvoice.bankAndAccount'/>"/>
		</td>
		<td align="right">
			合计金额:&nbsp;
		</td>
		<td align="left">
			<input type="text" name="inputInvoice.amtSum" id="inputInvoice.amtSum" value="<s:property value='inputInvoice.amtSum'/>"
				onkeypress="checkkey(value);" onblur="formatAmount(this,2,0,'true');"/>
		</td>
	</tr>
	<tr class="row1">
		<td align="right">
			合计税额:&nbsp;
		</td>
		<td align="left">
			<input type="text" name="inputInvoice.taxAmtSum" id="inputInvoice.taxAmtSum" value="<s:property value='inputInvoice.taxAmtSum'/>"
				onkeypress="checkkey(value);" onblur="formatAmount(this,2,0,'true');"/>
		</td>
		<td align="right">
			价税合计:&nbsp; 
		</td>
		<td align="left">
			<input type="text" name="inputInvoice.sumAmt" id="inputInvoice.sumAmt" value="<s:property value='inputInvoice.sumAmt'/>"
				onkeypress="checkkey(value);" onblur="formatAmount(this,2,0,'true');"/>
		</td>
		<td align="right">
			备注:&nbsp;
		</td>
		<td align="left">
			<input  type="text" name="inputInvoice.remark" id="inputInvoice.remark" value="<s:property value='inputInvoice.remark'/>"/>
		</td>
		<td align="right">
			开票人:&nbsp;
		</td>
		<td align="left">
			<input type="text" class="readonly_input" name="inputInvoice.drawer" id="inputInvoice.drawer" value="<s:property value='inputInvoice.drawer'/>"/>
		</td>
	</tr>
		<tr class="row1">
		<td align="right">
			复核人:&nbsp;
		</td>
		<td align="left">
			<input readonly class="readonly_input" type="text" name="inputInvoice.reivewer" id="inputInvoice.reivewer" value="<s:property value='inputInvoice.reivewer'/>"/>
		</td>
		<td align="right">
			收款人:&nbsp;
		</td>
		<td align="left">
			<input readonly class="readonly_input" type="text" name="inputInvoice.payee" id="inputInvoice.payee" value="<s:property value='inputInvoice.payee'/>"/>
		</td>
		<td align="right">
			销方纳税人名称:&nbsp;
		</td>
		<td>
			<input type="text" name="inputInvoice.vendorName" id="inputInvoice.vendorName" value="<s:property value='inputInvoice.vendorName'/>"/>
		</td>
		<td align="right">
			销方纳税人识别号:&nbsp;
		</td>
		<td>
			<input type="text" name="inputInvoice.vendorTaxNo" id="inputInvoice.vendorTaxNo" value="<s:property value='inputInvoice.vendorTaxNo'/>"/>
		</td>
	</tr>
	<tr class="row1">
		<td align="right">
			销方地址电话:&nbsp;
		</td>
		<td align="left">
			<input type="text" name="inputInvoice.vendorAddressAndPhone" id="inputInvoice.vendorAddressAndPhone" value="<s:property value='inputInvoice.vendorAddressAndPhone'/>"/>
		</td>
		<td align="right">
			销方银行账号:&nbsp;
		</td>
		<td align="left">
			<input type="text" name="inputInvoice.vendorBankAndAccount" id="inputInvoice.vendorBankAndAccount" value="<s:property value='inputInvoice.vendorBankAndAccount'/>"/>
		</td>
		<td align="right">
			所属机构:&nbsp;
		</td>
		<td align="left">
			<input type="text" name="inputInvoice.instCode" id="inputInvoice.instCode" value="<s:property value='inputInvoice.instCode'/>"/>
		</td>
		<td align="right">
			通知单编号:&nbsp;
		</td>
		<td align="left">
			<input type="text" name="inputInvoice.noticeNo" id="inputInvoice.noticeNo" value="<s:property value='inputInvoice.noticeNo'/>"/>
		</td>
	</tr>
	<tr class="row1">
		 <td align="right">
			认证通过日期:&nbsp;
		</td>
		<td align="left" width="15%">
			<input readonly class="readonly_input" class="tbl_query_time" id="inputInvoice.identifyDate" type="text" name="inputInvoice.identifyDate"
				value="<s:property value='inputInvoice.identifyDate'/>"/>
		</td>
		<td align="right">
			操作说明:&nbsp;
		</td>
		<td align="left">
			<input type="text" name="inputInvoice.description" id="inputInvoice.description" value="<s:property value='inputInvoice.description'/>"/>
		</td>
		<td align="right">
			转出比例:&nbsp;
		</td>
		<td align="left">
			<input type="text" name="inputInvoice.vatOutProportion" id="inputInvoice.vatOutProportion" value="<s:property value='inputInvoice.vatOutProportion'/>"/>
		</td>
		<td align="right">
			转出金额:&nbsp;
		</td>
		<td align="left">
			<input type="text" name="inputInvoice.vatOutAmt" id="inputInvoice.vatOutAmt" value="<s:property value='inputInvoice.vatOutAmt'/>"
				onkeypress="checkkey(value);" onblur="formatAmount(this,2,0,'true');"/>
		</td>
	</tr>
	<tr class="row1">
		<td align="right">
			是否勾稽:&nbsp;
		</td>
		<td>
			<s:select id="inputInvoice.conformFlg" name="inputInvoice.conformFlg" headerKey="" headerValue="请选择"
				list="#{'1':'勾稽','2':'不勾稽'}" listKey="key" listValue="value" 
				value='inputInvoice.conformFlg'/>
		</td>
		
		<td align="right">
			发票余额:&nbsp;
		</td>
		<td align="left">
			<input type="text" name="inputInvoice.balance" id="inputInvoice.balance" onkeypress="checkkey(value);" 
			value="<s:property value='inputInvoice.balance'/>"/>
		</td>
		<td align="right">
			发票类型:&nbsp;
		</td>
		<td>
			<s:select id="inputInvoice.faPiaoType" name="inputInvoice.faPiaoType" headerKey="" headerValue="请选择"
				list="#{'0':'增值税专用发票','1':'增值税普通发票'}" listKey="key" listValue="value" 
				value='inputInvoice.faPiaoType'/>
		</td>
		<td align="right">
			扫描日期:&nbsp;
		</td>
		<td align="left" width="15%">
			<input readonly class="readonly_input" class="tbl_query_time" id="inputInvoice.scanDate" type="text" name="inputInvoice.scanDate"
				value="<s:property value='inputInvoice.scanDate'/>"/>
		</td>
	</tr>
	<tr class="row1">
		<td align="right">
			扫描人:&nbsp;
		</td>
		<td align="left">
			<input type="text" class="readonly_input"  name="inputInvoice.scanOperator" id="inputInvoice.scanOperator" onkeypress="checkkey(value);" 
			value="<s:property value='inputInvoice.scanOperator'/>"/>
		</td>
		<td>
			</td>
		<td>
		</td>
		<td>
		</td>
		<td>
		</td>
		<td>
		</td>
		<td>
		</td>
	</tr>
	</table>
	<div id="ctrlbutton" class="ctrlbutton" style="border:0px">
		<input type="button" class="tbl_query_button" value="保存"
			onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
			name="btSave" id="btSave" onclick="save()" />
		<input type="button" class="tbl_query_button" value="返回"
			onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
			name="btBack" id="btBack" onclick="back()" />
	</div>
	<script language="javascript" type="text/javascript">
		//var vDataStatus = document.forms[0].elements['inputVatInfo.dataStatus'].value;
		//if (vDataStatus == "<%=DataUtil.BILL_STATUS_2%>" || vDataStatus == "<%=DataUtil.BILL_STATUS_4%>"){
		//	document.getElementById("btSave").disabled = true;
		//	document.getElementById("btSubmit").disabled = true;
		//	document.getElementById("btCancel").disabled = true;
		//}
	</script>
	</form>
</body>
</html>