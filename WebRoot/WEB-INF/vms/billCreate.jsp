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
		function revoke(){
			var billId = document.forms[0].elements['billInfo.billId'].value;
			submitAction(document.forms[0], "revokeBill.action?billId=" + billId);
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
				if (document.getElementById("billInfo.amtSumStr") != null){
					document.getElementById("billInfo.amtSumStr").value = vAmtInfos[0];
				}
				if (document.getElementById("billInfo.taxAmtSumStr") != null){
					document.getElementById("billInfo.taxAmtSumStr").value = vAmtInfos[1];
				}
				if (document.getElementById("billInfo.sumAmtStr") != null){
					document.getElementById("billInfo.sumAmtStr").value = vAmtInfos[2];
				}
			}
		}

		function check(){
			var id = document.getElementById("customerId").value;
			alert(id);
			submitAction(document.forms[0], "findCustomerById.action?customerId="+id);
			
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
	<input type="hidden" name="billInfo.billId" value="<s:property value='billInfo.billId'/>"/>
	<input type="hidden" name="billInfo.dataStatus" value="<s:property value='billInfo.dataStatus'/>"/>
	<input type="hidden" name="billInfo.isHandiwork" value="<s:property value='billInfo.isHandiwork'/>"/>
	<input type="hidden" name="flag" id="flag" value="<s:property value='flag'/>"/>
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
					<s:if test="flag == 'create'">
					<span class="actionIcon">-&gt;</span>票据添加
					</s:if>
					<s:else>
					<span class="actionIcon">-&gt;</span>票据编辑
					</s:else>
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
			客户号:&nbsp;
		</td>
		<td align="left" width="90%" colspan="5">
			<input type="text" name="billInfo.customerId" id="customerId" value="<s:property value='billInfo.customerId'/>" size="100"/>
			<%--<div id="tipDiv"style="border: 1px #74c0f9 solid; height: 60px; width: 635px; background: #FFF; display: none"></div>--%>
		
		<input type="button" class="tbl_query_button" value="检验"
			onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
			name="btSubmit" id="btSubmit" onclick="check()" />
		</td>
	</tr>
	<tr class="row1">
		<td align="right" width="10%">
			客户纳税人名称:&nbsp;
		</td>
		<td align="left" width="90%" colspan="5">
			<input type="text" name="billInfo.customerName" id="billInfo.customerName" value="<s:property value='billInfo.customerName'/>" size="100"/>
		</td>
	</tr>
	<tr class="row1">
		<td align="right" width="10%">
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
		<table cellspacing="0" border="0" cellpadding="0" width="100%" style="BORDER:#D6D6D6 1px solid" height="100%">
		<tr>
			<td>
				<iframe id="subTableFrame" scrolling="auto" src="listBillItemYS.action?billId=<s:property value='billInfo.billId'/>&isHandiwork=<s:property value='billInfo.isHandiwork'/>" height="110px" width="100%" frameborder="0"></iframe>
			</td>
		</tr>
		</table>
		</td>
	</tr>
	<tr class="row1">
		<td align="right">
			价税合计:&nbsp;
		</td>
		<td>
			<input type="text" name="billInfo.sumAmtStr" id="billInfo.sumAmtStr" value="<s:property value='billInfo.sumAmt'/>"
				onkeypress="loadBillAmtInfo();checkkey(value);" onblur="formatAmount(this,2,0,'true');"/>
		</td>
		<td align="right">
			合计金额:&nbsp;
		</td>
		<td>
			<input type="text" name="billInfo.amtSumStr" id="billInfo.amtSumStr" value="<s:property value='billInfo.amtSum'/>"
				onkeypress="loadBillAmtInfo();checkkey(value);" onblur="formatAmount(this,2,0,'true');"/>
		</td>
		<td align="right">
			合计税额:&nbsp;
		</td>
		<td>
			<input type="text" name="billInfo.taxAmtSumStr" id="billInfo.taxAmtSumStr" value="<s:property value='billInfo.taxAmtSum'/>"
				onkeypress="loadBillAmtInfo();checkkey(value);" onblur="formatAmount(this,2,0,'true');"/>
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
			<textarea name="billInfo.remark" id="billInfo.remark" cols="60" rows="7"><s:property value='billInfo.remark'/></textarea>
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
		<input type="button" class="tbl_query_button" value="保存"
			onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
			name="btSave" id="btSave" onclick="save()" />
		<input type="button" class="tbl_query_button" value="提交"
			onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
			name="btSubmit" id="btSubmit" onclick="document.getElementById('submitFlag').value='S';save()" />
		<input type="button" class="tbl_query_button" value="取消"
			onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
			name="btCancel" id="btCancel" onclick="revoke()" />
	</div>
	<script language="javascript" type="text/javascript">
		var vDataStatus = document.forms[0].elements['billInfo.dataStatus'].value;
		if (vDataStatus == "<%=DataUtil.BILL_STATUS_2%>" || vDataStatus == "<%=DataUtil.BILL_STATUS_4%>"){
			document.getElementById("btSave").disabled = true;
			document.getElementById("btSubmit").disabled = true;
			document.getElementById("btCancel").disabled = true;
		}
	</script>
	</form>
</body>
</html>