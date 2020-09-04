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
		function save(){
			if(issave){
				alert("数据保存中，请稍候...");
				return false;
			}
			var isblank=true;
			var oo = document.getElementById("Form1").getElementsByTagName("input");
			for(var k = 0, len = oo.length; k < len; k++){
				var t = oo[k].type;
				if(t=="text" ){
					var v=oo[k].value;
					if(v&&v!="" &&v.replace(/(^\s*)|(\s*$)/g, "")!=""){
						isblank=false;
						break;
					}
				}
			}
			if (document.getElementById('submitFlag').value=='S'){
				if (!submitBillCheck()){
					return false;
				}
			}
			if(!isblank){
				document.getElementById("Form1").submit();
				issave = true;
			}else{
				alert("表单没有输入数据,无需保存。 \n\n如要退出，请点击'返回'按钮");
			}
		}
		function back(){
			submitAction(document.forms[0], "listInputVat.action?fromFlag=menu");
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
	<form name="Form1" method="post" action="saveInputVat.action" id="Form1">
	<input type="hidden" name="inputVatInfo.inVatId" value="<s:property value='inputVatInfo.inVatId'/>"/>
	<input type="hidden" name="submitFlag" id="submitFlag" value="U"/>
	<table id="tbl_main" cellpadding=0 cellspacing=0>
	<tr>
		<td align="left">
		<table id="tbl_current_status" >
		<tr style="margin-left:-100px">
			<td>
				<img src="<%=bopTheme2%>/img/jes/icon/home.png" />
				<span class="current_status_menu">当前位置：</span>
				<span class="current_status_submenu">查询
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
			<input type="text" name="inputVatInfo.billCode" id="inputVatInfo.billCode" value="<s:property value='inputVatInfo.billCode'/>" maxlength="10"/>
		</td>
		<td align="right" width="10%">
			发票号码:&nbsp;
		</td>
		<td align="left" width="15%">
			<input type="text" name="inputVatInfo.billNo" id="inputVatInfo.billNo" value="<s:property value='inputVatInfo.billNo'/>" maxlength="8"/>
		</td>
		<td align="right" width="10%">
			交易日期:&nbsp;
		</td>
		<td align="left" width="15%">
			<input class="tbl_query_time" id="inputVatInfo.valueDate" type="text" name="inputVatInfo.valueDate"
				value="<s:property value='inputVatInfo.valueDate'/>" onfocus="WdatePicker()" style="width:155px;"/>
		</td>
		<td align="right" width="10%">
			记账日期:&nbsp;
		</td>
		<td align="left" width="15%">
			<input class="tbl_query_time" id="inputVatInfo.bookingDate" type="text" name="inputVatInfo.bookingDate"
				value="<s:property value='inputVatInfo.bookingDate'/>" onfocus="WdatePicker()" style="width:155px;"/>
		</td>
	</tr>
	<tr class="row1">
		<td align="right">
			发票种类:&nbsp;
		</td>
		<td align="left">
			<s:select id="inputVatInfo.billType" name="inputVatInfo.billType" list="billTypeList" headerKey="" headerValue="请选择"
				listKey='valueStandardLetter' listValue='name'/>
		</td>
		<td align="right">
			合计金额:&nbsp;
		</td>
		<td align="left">
			<input type="text" name="inputVatInfo.amt" id="inputVatInfo.amt" value="<s:property value='inputVatInfo.amt'/>"
				onblur="countSumAmt();" onkeypress="checkkey(value);" onblur="formatAmount(this,2,0,'true');"/>
		</td>
		<td align="right">
			合计税额:&nbsp;
		</td>
		<td align="left">
			<input type="text" name="inputVatInfo.taxAmt" id="inputVatInfo.taxAmt" value="<s:property value='inputVatInfo.taxAmt'/>"
				onblur="countSumAmt();" onkeypress="checkkey(value);" onblur="formatAmount(this,2,0,'true');"/>
		</td>
		<td align="right">
			价税合计:&nbsp;
		</td>
		<td align="left">
			<input type="text" name="inputVatInfo.sumAmt" id="inputVatInfo.sumAmt" value="<s:property value='inputVatInfo.sumAmt'/>"
				onfocus="countSumAmt();" onkeypress="checkkey(value);" onblur="formatAmount(this,2,0,'true');"/>
		</td>
	</tr>
	<tr class="row1">
		<td align="right">
			供应商名称:&nbsp;
		</td>
		<td align="left">
			<input type="text" name="inputVatInfo.suppName" id="inputVatInfo.suppName" value="<s:property value='inputVatInfo.suppName'/>"/>
		</td>
		<td align="right">
			供应商税务登记号:&nbsp;
		</td>
		<td align="left">
			<input type="text" name="inputVatInfo.suppTaxNo" id="inputVatInfo.suppTaxNo" value="<s:property value='inputVatInfo.suppTaxNo'/>"/>
		</td>
		<td align="right">
			供应商银行:&nbsp;
		</td>
		<td align="left">
			<input type="text" name="inputVatInfo.suppBank" id="inputVatInfo.suppBank" value="<s:property value='inputVatInfo.suppBank'/>"/>
		</td>
		<td align="right">
			供应商账号:&nbsp;
		</td>
		<td align="left">
			<input type="text" name="inputVatInfo.suppAccount" id="inputVatInfo.suppAccount" value="<s:property value='inputVatInfo.suppAccount'/>"/>
		</td>
	</tr>
	<tr class="row1">
		<td align="right">
			供应商地址:&nbsp;
		</td>
		<td align="left">
			<input type="text" name="inputVatInfo.suppAddress" id="inputVatInfo.suppAddress" value="<s:property value='inputVatInfo.suppAddress'/>"/>
		</td>
		<td align="right">
			供应商电话:&nbsp;
		</td>
		<td align="left">
			<input type="text" name="inputVatInfo.suppPhone" id="inputVatInfo.suppPhone" value="<s:property value='inputVatInfo.suppPhone'/>"/>
		</td>
		<td align="right">
			记账科目:&nbsp;
		</td>
		<td align="left">
			<input type="text" name="inputVatInfo.bookingCourse" id="inputVatInfo.bookingCourse" value="<s:property value='inputVatInfo.bookingCourse'/>"/>
		</td>
		<td align="right">
			业务凭证编号:&nbsp;
		</td>
		<td align="left">
			<input type="text" name="inputVatInfo.bussVouchersCode" id="inputVatInfo.bussVouchersCode" value="<s:property value='inputVatInfo.bussVouchersCode'/>"/>
		</td>
	</tr>
	<tr class="row1">
		<td align="right">
			合同管理编号:&nbsp;
		</td>
		<td align="left">
			<input type="text" name="inputVatInfo.contractCode" id="inputVatInfo.contractCode" value="<s:property value='inputVatInfo.contractCode'/>"/>
		</td>
		<td align="right">
			抵扣联管理编号:&nbsp;
		</td>
		<td align="left">
			<input type="text" name="inputVatInfo.deductionCode" id="inputVatInfo.deductionCode" value="<s:property value='inputVatInfo.deductionCode'/>"/>
		</td>
		<td align="right">
			认证结果:&nbsp;
		</td>
		<td align="left">
			<s:select id="inputVatInfo.authenticationFlag" name="inputVatInfo.authenticationFlag" list="authenticationFlagList"
				listKey='valueStandardLetter' listValue='name'/>
		</td>
		<td align="right">
			认证日期:&nbsp;
		</td>
		<td align="left">
			<input class="tbl_query_time" id="inputVatInfo.authenticationDate" type="text" name="inputVatInfo.authenticationDate"
				value="<s:property value='inputVatInfo.authenticationDate'/>" onfocus="WdatePicker()" style="width:155px;"/>
		</td>
	</tr>
	<tr class="row1">
		<td align="right">
			备注:&nbsp;
		</td>
		<td colspan="7">
			<input type="text" name="inputVatInfo.remark" id="inputVatInfo.remark" value="<s:property value='inputVatInfo.remark'/>" size="160"/>
		</td>
	</tr>
	<tr class="row1">
		<td align="right">
			开票人:&nbsp;
		</td>
		<td>
			<input type="text" name="inputVatInfo.drawer" id="inputVatInfo.drawer" value="<s:property value='inputVatInfo.drawer'/>"/>
		</td>
		<td align="right">
			复核人:&nbsp;
		</td>
		<td>
			<input type="text" name="inputVatInfo.reviewer" id="inputVatInfo.reviewer" value="<s:property value='inputVatInfo.reviewer'/>"/>
		</td>
		<td align="right">
			收款人:&nbsp;
		</td>
		<td>
			<input type="text" name="inputVatInfo.payee" id="inputVatInfo.payee" value="<s:property value='inputVatInfo.payee'/>"/>
		</td>
		<td align="right">
			转出金额:&nbsp;
		</td>
		<td align="left">
			<input type="text" name="inputVatInfo.outAmt" id="inputVatInfo.outAmt" value="<s:property value='inputVatInfo.outAmt'/>"
				onkeypress="checkkey(value);" onblur="formatAmount(this,2,0,'true');"/>
		</td>
	</tr>
	</table>
	<div id="ctrlbutton" class="ctrlbutton" style="border:0px">
		<input type="button" class="tbl_query_button" value="保存"
			onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
			name="btSave" id="btSave" onclick="if(saveInputVatCheck()){save();}" />
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