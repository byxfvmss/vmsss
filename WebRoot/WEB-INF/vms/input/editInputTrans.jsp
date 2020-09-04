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
<%@ include file="../../../page/include.jsp"%>
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
			submitAction(document.forms[0], "listInputTrans.action?fromFlag=menu");
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
	<form name="Form1" method="post" action="saveInputTrans.action" id="Form1">
	<input type="hidden" name="dealNo" value="<s:property value='inputTrans.dealNo'/>"/>
	<input type="hidden" name="submitFlag" id="submitFlag" value="U"/>
	<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
	<tr>
		<td class="centercondition">
			<div id="tbl_current_status">
				<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> 
				<span class="current_status_menu">当前位置：</span> 
				<span class="current_status_submenu1">进项税管理</span> 
				<span class="current_status_submenu">认证管理</span>
				<span class="current_status_submenu">数据导入 </span>
				<span class="current_status_submenu">数据编辑</span>
			</div>
			</br>
			<table id="tbl_context" cellspacing="0" width="100%" align="center" cellpadding="0" >
			<tr class="row1">
				<td align="right" width="20%"  style="background-color:#F0F0F0;font-weight:bold;color:#727375;"	>
					交易编号:&nbsp;&nbsp;&nbsp;
				</td>
				<td align="left" width="30%">
					<input type="text" class="tbl_query_text" name="inputTrans.dealNo" id="inputTrans.dealNo" value="<s:property value='inputTrans.dealNo'/>" disabled="disabled"/>
				</td>
				<td align="right" width="20%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;"	>
					交易发生机构:&nbsp;&nbsp;&nbsp;
				</td>
				<td align="left" width="30%">
					<input type="text" class="tbl_query_text" name="inputTrans.bankCode" id="inputTrans.bankCode" value="<s:property value='inputTrans.bankCode'/>" />
				</td>
			</tr>
			<tr class="row1">
				<td align="right"  style="background-color:#F0F0F0;font-weight:bold;color:#727375;"	>
					发票代码:&nbsp;&nbsp;&nbsp;
				</td>
				<td align="left" >
					<input type="text" class="tbl_query_text" name="inputTrans.billCode" id="inputTrans.billCode" value="<s:property value='inputTrans.billCode'/>" 
				</td>
			
				<td align="right"  style="background-color:#F0F0F0;font-weight:bold;color:#727375;"	>
					发票号码:&nbsp;&nbsp;&nbsp;
				</td>
				<td align="left">
					<input type="text" class="tbl_query_text" name="inputTrans.billNo" id="inputTrans.billNo" value="<s:property value='inputTrans.billNo'/>"
				</td>
			</tr>
			<tr class="row1">
				<td align="right"  style="background-color:#F0F0F0;font-weight:bold;color:#727375;"	>
					金额_人民币:&nbsp;&nbsp;&nbsp;
				</td>
				<td align="left">
					<input type="text" class="tbl_query_text" name="inputTrans.amtCny" id="inputTrans.amtCny" value="<s:property value='inputTrans.amtCny'/>"
						onblur="countSumAmt();" onkeypress="checkkey(value);" onblur="formatAmount(this,2,0,'true');"/>
				</td>
				<td align="right"  style="background-color:#F0F0F0;font-weight:bold;color:#727375;"	>
					税额_人民币:&nbsp;&nbsp;&nbsp;
				</td>
				<td align="left">
					<input type="text" class="tbl_query_text" name="inputTrans.taxAmtCny" id="inputTrans.taxAmtCny" value="<s:property value='inputTrans.taxAmtCny'/>"
					 onkeypress="checkkey(value);" onblur="formatAmount(this,2,0,'true');"/>
				</td>
			</tr>
			<tr class="row1">
				<td align="right"  style="background-color:#F0F0F0;font-weight:bold;color:#727375;"	>
					附加税1（城市建设）金额:&nbsp;&nbsp;&nbsp;
				</td>
				<td align="left">
					<input type="text" class="tbl_query_text" name="inputTrans.surtax1AmtCny" id="inputTrans.surtax1AmtCny" value="<s:property value='inputTrans.surtax1AmtCny'/>"
					  onkeypress="checkkey(value);" onblur="formatAmount(this,2,0,'true');" />
				</td>
				<td align="right"  style="background-color:#F0F0F0;font-weight:bold;color:#727375;"	>
					支出_人民币:&nbsp;&nbsp;&nbsp;
				</td>
				<td align="left">
					<input type="text" class="tbl_query_text" name="inputTrans.incomeCny" id="inputTrans.incomeCny" value="<s:property value='inputTrans.incomeCny'/>"
					 onkeypress="checkkey(value);" onblur="formatAmount(this,2,0,'true');"/>
				</td>
			</tr>
			<tr class="row1">
				<td align="right"  style="background-color:#F0F0F0;font-weight:bold;color:#727375;"	>
					附加税2（教育附加）金额:&nbsp;&nbsp;&nbsp;
				</td>
				<td align="left">
					<input type="text" class="tbl_query_text" name="inputTrans.surtax2AmtCny" id="inputTrans.surtax2AmtCny" value="<s:property value='inputTrans.surtax2AmtCny'/>"
					  onkeypress="checkkey(value);" onblur="formatAmount(this,2,0,'true');" />
				</td>
				<td align="right"  style="background-color:#F0F0F0;font-weight:bold;color:#727375;"	>
					交易日期:&nbsp;&nbsp;&nbsp;
				</td>
				<td>
					<input type="text" class="tbl_query_text" name="inputTrans.transDate" id="inputTrans.transDate" value="<s:property value='inputTrans.transDate'/>" disabled="disabled" />
				</td>
			</tr>
			<tr class="row1">
				<td align="right"  style="background-color:#F0F0F0;font-weight:bold;color:#727375;"	>
					附加税3（地方教育附加）金额:&nbsp;&nbsp;&nbsp;
				</td>
				<td align="left">
					<input type="text" class="tbl_query_text" name="inputTrans.surtax3AmtCny" id="inputTrans.surtax3AmtCny" value="<s:property value='inputTrans.surtax3AmtCny'/>"
					  onkeypress="checkkey(value);" onblur="formatAmount(this,2,0,'true');" />
				</td>
				<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;"	>
					备注:&nbsp;&nbsp;&nbsp;
				</td>
				<td align="left">
					<input type="text" class="tbl_query_text" name="inputTrans.remark" id="inputTrans.remark" value="<s:property value='inputTrans.remark'/>" />
				</td>
			</tr>
			<tr class="row1">
				<td align="right"  style="background-color:#F0F0F0;font-weight:bold;color:#727375;"	>
					附加税4（其他）金额:&nbsp;&nbsp;&nbsp;
				</td>
				<td align="left">
					<input type="text" class="tbl_query_text" name="inputTrans.surtax4AmtCny" id="inputTrans.surtax4AmtCny" value="<s:property value='inputTrans.surtax4AmtCny'/>"
					  onkeypress="checkkey(value);" onblur="formatAmount(this,2,0,'true');" />
				</td>
				<td align="right"  style="background-color:#F0F0F0;font-weight:bold;color:#727375;"	>
					供应商ID:&nbsp;&nbsp;&nbsp;
				</td>
				<td>
					<input type="text" class="tbl_query_text" name="inputTrans.vendorId" id="inputTrans.vendorId" value="<s:property value='inputTrans.vendorId'/>" disabled="disabled" />
				</td>
			</tr>
			</table>
			</br>
			</br>
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
			</td>
	</tr>
	</table>
	</form>
</body>
</html>