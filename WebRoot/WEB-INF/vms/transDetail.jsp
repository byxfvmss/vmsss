<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="cjit.crms.util.StringUtil"
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
		};
		// [返回]按钮
		function back(){
			submitAction(document.forms[0], "listTransBack.action");
		};
		function loadOurInfo(instCode){
			var webroot = '<%=webapp%>';
			var url = webroot + "/loadOurInfo.action";
			var param = "instCode=" + document.getElementById("instCode").value;
			var result = sendXmlHttpPost(url, param);
			var vOurInfos = result.split("###");
			if (vOurInfos.length == 5){
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
					document.getElementById("billInfo.bankandaccount").value = vOurInfos[4];
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
	</script>
	<style type="text/css">
		.style1{
			width: 242px;
		}
	</style>
</head>
<body onLoad="javascript:tips()" onmousemove="MM(event)" onmouseout="MO(event)">
	<form name="Form1" method="post" action="saveBill.action" id="Form1">
	<%
		TransInfo trans = (TransInfo)request.getAttribute("transInfo");
		List billList = null;
		if (trans != null){
			billList = trans.getBillList();
		}else{
			trans = new TransInfo();
		}
	%>
	<input type="hidden" name="transId" value="<%=trans.getTransId()%>"/>
	<input type="hidden" name="billInfo.dataStatus" value="<s:property value='billInfo.dataStatus'/>"/>
	<input type="hidden" name="flag" id="flag" value="<s:property value='flag'/>"/>
	<input type="hidden" name="submitFlag" id="submitFlag" value="U"/>
	<table id="tbl_main" cellpadding="0" cellspacing="0">
	<tr>
		<td class="centercondition">
		<div id="tbl_current_status">
			<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> 
			<span class="current_status_menu">当前位置：</span> 
			<span class="current_status_submenu1">销项税管理</span>
			<span class="current_status_submenu">开票管理</span>
			<span class="current_status_submenu">开票申请</span>
			<span class="current_status_submenu">交易详情</span>
		</div>
		</td>
	</tr>
	</table>
	<div id="whitebox">
		<table id="tbl_context" cellspacing="0" width="100%" align="center" cellpadding="0" >
		<tr class="row1">
			<td align="right" width="10%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;color:#727375;">
				客户名称:&nbsp;&nbsp;&nbsp;
			</td>
			<td align="left" width="20%">
				<%=trans.getCustomerName()%>
			</td>
			<td align="right" width="10%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
				客户纳税人识别号:&nbsp;&nbsp;&nbsp;
			</td>
			<td align="left" width="20%">
				<%=trans.getCustomerTaxno() == null ? "" : trans.getCustomerTaxno()%>
			</td>
			<td align="right" width="10%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
				纳税人类型:&nbsp;&nbsp;&nbsp;
			</td>
			<td align="left" width="20%">
				<%=trans.getCustomerTaxPayerType() == null ? "" : DataUtil.getCustomerTaxPayerTypeCh(trans.getCustomerTaxPayerType()) %>
			</td>
		</tr>
		<tr class="row1">
			<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
				客户银行账号:&nbsp;&nbsp;&nbsp;
			</td>
			<td>
			    <%=trans.getCustomerAccount() == null ? "" : trans.getCustomerAccount()%>
			</td>
			<s:if test='configCustomerFlag.equals("KBC")'>
			<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
				客户地址电话:&nbsp;&nbsp;&nbsp;
			</td>
			<td>
				<%=trans.getCustomerAddress() == null || "null".equals(trans.getCustomerAddress()) || "".equals(trans.getCustomerAddress()) ? "" : trans.getCustomerAddress() + " " + trans.getCustomerTel() == null ? "" : trans.getCustomerTel()%>
			</td>
			<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
				客户号:&nbsp;&nbsp;&nbsp;
			</td>
			<td>
			    <%=trans.getCustomerId() == null ? "" : trans.getCustomerId()%>
			</td>
			</s:if>
			<s:else>
			<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
				客户地址电话:&nbsp;&nbsp;&nbsp;
			</td>
			<td colspan="3">
				<%=trans.getCustomerAddress() == null || "null".equals(trans.getCustomerAddress()) || "".equals(trans.getCustomerAddress()) ? "" : trans.getCustomerAddress() + " " + trans.getCustomerTel() == null ? "" : trans.getCustomerTel()%>
			</td>
			</s:else>
		</tr>
		<tr class="row1">
		    <td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
				交易时间:&nbsp;&nbsp;&nbsp;
			</td>
			<td>
				<%=trans.getTransDate() == null ? "" : trans.getTransDate()%>
			</td>
			<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
				交易类型:&nbsp;&nbsp;&nbsp;
			</td>
			<td>
				<%=trans.getTransTypeName() == null ? "" : trans.getTransTypeName()%>
			</td>
			<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
				发票类型:&nbsp;&nbsp;&nbsp;
			</td>
			<td>
				<%=trans.getFapiaoType() == null ? "" : DataUtil.getFapiaoTypeName(trans.getFapiaoType())%>
			</td>
			<s:if test='configCustomerFlag.equals("KBC")'>
			<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
				数据来源:&nbsp;&nbsp;&nbsp;
			</td>
			<td>
				<%=trans.getDataSources() == null ? "" : trans.getDataSources()%>
			</td>
			</s:if>
		</tr>
		<tr class="row1">
			<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
				交易金额:&nbsp;&nbsp;&nbsp;
			</td>
			<td>
				<%=NumberUtils.format(trans.getAmt(),"",2)%>
			</td>
		</tr>
		<tr class="row1">
			<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
				税率:&nbsp;&nbsp;&nbsp;
			</td>
			<td>
				<%=NumberUtils.format(trans.getTaxRate(),"",4)%>
			</td>
			<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
				税额:&nbsp;&nbsp;&nbsp;
			</td>
			<td>
				<%=NumberUtils.format(trans.getTaxAmt(),"",2)%>
			</td>
			<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
				收入:&nbsp;&nbsp;&nbsp;
			</td>
			<td>
				<%=NumberUtils.format(trans.getIncome(),"",2)%>
			</td>
		</tr>
		<tr class="row1">
			<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
				价税合计:&nbsp;&nbsp;&nbsp;
			</td>
			<td>
				<%=NumberUtils.format(trans.getAmt(),"",2)%>
			</td>
			<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
				未开票金额:&nbsp;&nbsp;&nbsp;
			</td>
			<td>
				<%=NumberUtils.format(trans.getBalance(),"",2)%>
			</td>
			<td></td>
			<td></td>
		</tr>
		<tr class="row1">
		    <td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
				是否冲账:&nbsp;&nbsp;&nbsp;
			</td>
			<td>
				<%=trans.getIsReverse()==null?"否":DataUtil.getFlagDesc(trans.getIsReverse())%>
			</td>
			<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
				状态:&nbsp;&nbsp;&nbsp;
			</td>
			<td>
				<%=DataUtil.getDataStatusCH(trans.getDataStatus(), "TRANS")%>
			</td>
			<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
				备注:&nbsp;&nbsp;&nbsp;
			</td>
			<td>
				<%=trans.getRemark() == null ? "" : trans.getRemark() %>
			</td>
		</tr>
		</table>
	</div>
	
	<div style="overflow:auto;" id="list1">
	<table id="contenttable" width="100%" class="lessGrid" cellspacing="0" rules="all" border="0" cellpadding="0" display="none" style="border-collapse: collapse;">
	<%
		if (billList != null && billList.size() > 0){
			for (int i = 0; i < billList.size(); i++){
				BillInfo bill = (BillInfo)billList.get(i);
				String billTitle = "";
				if (billList.size() > 1){
					billTitle = "第" + (i+1) + "次";
				}
	%>
	<tr class="row1">
		<td align="right" width="10%"></td>
		<td align="left" width="20%"></td>
		<td align="right" width="10%"></td>
		<td align="left" width="30%"></td>
		<td align="right" width="10%"></td>
		<td align="left" width="20%"></td>
	</tr>
	<tr class="row1">
		<td align="right">
			<%=billTitle%>开票日期
		</td>
		<td>
			<%=bill.getBillDate()%>
		</td>
		<td align="right">
			票据代码
		</td>
		<td>
			<%=bill.getBillCode()%>
		</td>
		<td align="right">
			票据号码
		</td>
		<td>
			<%=bill.getBillNo()%>
		</td>
	</tr>
	<tr class="row1">
		<td align="right">
			票据状态
		</td>
		<td>
			<%=DataUtil.getDataStatusCH(bill.getDataStatus(),"BILL")%>
		</td>
		<td align="right">
			开具类型
		</td>
		<td>
		    <%=DataUtil.getIssueTypeName(bill.getIssueType())%>
		</td>
		<td align="right">
			发票类型
		</td>
		<td>
		    <%=DataUtil.getFapiaoTypeName(bill.getFapiaoType())%>
		</td>
	</tr>
	<tr class="row1">
		<td align="right">
			票据合计金额
		</td>
		<td>
			<%=NumberUtils.format(bill.getAmtSum(),"",2)%>
		</td>
		<td align="right">
			票据合计税额
		</td>
		<td>
			<%=NumberUtils.format(bill.getTaxAmtSum(),"",2)%>
		</td>
		<td align="right">
			票据价税合计
		</td>
		<td colspan=3>
			<%=NumberUtils.format(bill.getSumAmt(),"",2)%>
		</td>
	</tr>
	<tr class="row1">
		<td align="right">
			开票人
		</td>
		<td>
			<%=bill.getDrawer()%>
		</td>
		<td align="right">
			复核人
		</td>
		<td>
			<%=bill.getReviewer()%>
		</td>
		<td align="right">
			收款人
		</td>
		<td>
			<%=bill.getPayee()%>
		</td>
	</tr>
	<%
				//if (StringUtil.isNotEmpty(bill.getCancelFlag()) || DataUtil.BILL_STATUS_0.equals(bill.getDataStatus())){
				//	// 存在撤销记录
				//	String cancelFlagDesc = "";
				//	if ("F".equals(bill.getCancelFlag())){
				//		cancelFlagDesc = "废票";
				//	} else if ("H".equals(bill.getCancelFlag())){
				//		cancelFlagDesc = "红冲";
				//	}
	%>
	<%--
	<tr class="row1">
		<td align="right">
			撤销标识:&nbsp;
		</td>
		<td>
			<%//=cancelFlagDesc%>
		</td>
		<td align="right">
			撤销日期:&nbsp;
		</td>
		<td>
			<%//=bill.getReviewer()%>
		</td>
		<td align="right">
			撤销发起人:&nbsp;
		</td>
		<td>
			<%//=bill.getCancelInitiator()%>
		</td>
	</tr>
	--%>
	<%
				//}
			}
		}
	%>
	</table>
	</div>

	<table id="tbl_tools" width="100%" border="0">
		<tr>
			<td align="right">
				<div id="ctrlbutton" class="ctrlbutton mtop10" style="border:0px">
					<input type="button" class="tbl_query_button" value="返回"
						onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
						name="btCancel" id=""btCancel"" onclick="window.history.back()" />
				</div>
			</td>
		</tr>
	</table>

	<script language="javascript" type="text/javascript">
	    var msgHight = document.getElementById("tbl_tools").offsetHeight;
		document.getElementById("list1").style.height = screenHeight - 450 - msgHight;
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
