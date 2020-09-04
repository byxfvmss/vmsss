<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.vms.trans.model.BillInfo"
	import="com.cjit.vms.trans.model.BillItemInfo"
	import="com.cjit.vms.trans.model.TransInfo"
	import="com.cjit.vms.trans.model.EmsInfo"
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
				if (!submitEmsCheck()){
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
			submitAction(document.forms[0], "billEmsMessage.action?fromFlag=menu");
		}
	</script>
	<style type="text/css">
		.style1{
			width: 242px;
		}
	</style>
</head>
<body onLoad="javascript:tips()" onmousemove="MM(event)" onmouseout="MO(event)">
	<form name="Form1" method="post" action="saveEmsMessage.action?flag=add" id="Form1">
	<input type="hidden" name="billIds" value="<s:property value='selectBillIds'/>"/>
	<input type="hidden" name="billInfo.billId" value="<s:property value='billInfo.billId'/>"/>
	<input type="hidden" name="billInfo.dataStatus" value="<s:property value='billInfo.dataStatus'/>"/>
	<input type="hidden" name="submitFlag" id="submitFlag" value="U"/>
	<input type="hidden" name="emsId" value="<s:property value='emsId'/>"/>
	<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
	<tr>
		<td class="centercondition">
		<div id="tbl_current_status">
			<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> 
			<span class="current_status_menu">当前位置：</span> 
			<span class="current_status_submenu1">销项税管理</span>
			<span class="current_status_submenu">开票管理</span>
			<span class="current_status_submenu">发票快递</span>
			<span class="current_status_submenu">新增快递</span>
		</div>
		</br>
		</br>
	<table id="tbl_context" cellspacing="0" width="100%" align="center" cellpadding="0">
	<%
		ArrayList billInfoList = (ArrayList) request.getAttribute("billInfoList");
		if (billInfoList != null && billInfoList.size() > 0){
			for (int i = 0; i < billInfoList.size(); i++){
				BillInfo billInfo = (BillInfo)billInfoList.get(i);
	%>
	<tr class="row1">	
		<td align="right" width="13%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
			开票日期:&nbsp;&nbsp;&nbsp;
		</td>
		<td align="left" width="20%">
			<input type="text" class="tbl_query_text2" name="emsInfo.billDate" id="emsInfo.billDate" value="<%=billInfo.getBillDate()%>" onfocus="this.blur();" style="background-color: #CCCCCC;" />
		</td>
		<td align="right" width="13%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
			发票代码:&nbsp;&nbsp;&nbsp;
		</td>
		<td align="left" width="20%">
			<input type="text" class="tbl_query_text2" name="emsInfo.billCode" id="emsInfo.billCode" value="<%=billInfo.getBillCode()%>" onfocus="this.blur();" style="background-color: #CCCCCC;" />
		</td>
		<td align="right" width="13%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
			发票号码:&nbsp;&nbsp;&nbsp;
		</td>
		<td align="left" width="21%">
			<input type="text" class="tbl_query_text2" name="emsInfo.billNo" id="emsInfo.billNo" value="<%=billInfo.getBillNo()%>" onfocus="this.blur();" style="background-color: #CCCCCC;" />
		</td>
	</tr>
	<tr class="row1">
		<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
			客户纳税人名称:&nbsp;&nbsp;&nbsp;
		</td>
		<td align="left">
			<input type="text" class="tbl_query_text2" name="emsInfo.customerName" id="emsInfo.customerName" value="<%=billInfo.getCustomerName()%>" onfocus="this.blur();" style="background-color: #CCCCCC;" />
		</td>
		<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
			客户纳税人识别号:&nbsp;&nbsp;&nbsp;
		</td>
		<td align="left" colspan="3">
			<input type="text" class="tbl_query_text2" name="emsInfo.customerTaxno" id="emsInfo.customerTaxno" value="<%=billInfo.getCustomerTaxno()%>" onfocus="this.blur();" style="background-color: #CCCCCC;" />
		</td>
	</tr>
	<%
			if((i+1) == billInfoList.size()){
	%>
	<tr class="row1">
		<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
			客户联系人:&nbsp;&nbsp;&nbsp;
		</td>
		<td align="left">
			<input type="text" class="tbl_query_text2" name="emsInfo.customerLinkman" id="emsInfo.customerLinkman" value="<%=billInfo.getCustomerLinkman()==null?"":billInfo.getCustomerLinkman()%>" />
		</td>
		<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
			客户收件人:&nbsp;&nbsp;&nbsp;
		</td>
		<td align="left">
			<input type="text" class="tbl_query_text2" name="emsInfo.addressee" id="emsInfo.addressee" value="<%=billInfo.getAddressee()==null?"":billInfo.getAddressee()%>" /><span style="color: red">*</span>
		</td>
		<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
			客户收件人电话:&nbsp;&nbsp;&nbsp;
		</td>
		<td align="left">
			<input type="text" class="tbl_query_text2" name="emsInfo.addresseePhone" id="emsInfo.addresseePhone" value="<%=billInfo.getAddresseePhone()==null?"":billInfo.getAddresseePhone()%>" /><span style="color: red">*</span>
		</td>
		
	</tr>
	<tr class="row1">
		<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
			客户收件邮编:&nbsp;&nbsp;&nbsp;
		</td>
		<td align="left" >
			<input type="text" class="tbl_query_text2" name="emsInfo.addresseeZipcode" id="emsInfo.addresseeZipcode" value="<%=billInfo.getAddresseeZipcode()==null?"":billInfo.getAddresseeZipcode()%>" />
		</td>
		<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
			收件地址:&nbsp;&nbsp;&nbsp;
		</td>
		<td align="left" colspan="3">
			<input type="text" class="tbl_query_text2" name="emsInfo.addresseeAddress" id="emsInfo.addresseeAddress" value="<%=billInfo.getAddresseeAddress()==null?"":billInfo.getAddresseeAddress()%>" size="76" /><span style="color: red">*</span>
		</td>
	</tr>
	<tr class="row1">
		<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
			寄件人:&nbsp;&nbsp;&nbsp;
		</td>
		<td>
			<input type="text" class="tbl_query_text2" name="emsInfo.sender" id="emsInfo.sender" value="<%=billInfo.getSender()==null?"":billInfo.getSender()%>"/><span style="color: red">*</span>
		</td>
		<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
			详细收件地址:&nbsp;&nbsp;&nbsp;
		</td>
		<td align="left" colspan="3">
			<input type="text" class="tbl_query_text2" name="emsInfo.addresseeAddressdetail" id="emsInfo.addresseeAddressdetail" value="<%=billInfo.getAddresseeAddressdetail()==null?"":billInfo.getAddresseeAddressdetail()%>" size="76" /><span style="color: red">*</span>
		</td>
	</tr>
	<tr class="row1">
		<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
			快递公司:&nbsp;&nbsp;&nbsp;
		</td>
		<td>
			<input type="text" class="tbl_query_text2" name="emsInfo.fedexExpress" id="emsInfo.fedexExpress" value="<%=billInfo.getFedexExpress()==null?"":billInfo.getFedexExpress()%>"/><span style="color: red">*</span>
		</td>
		<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
			快递单号:&nbsp;&nbsp;&nbsp;
		</td>
		<td>
			<input type="text" class="tbl_query_text2" name="emsInfo.emsNo" id="emsInfo.emsNo" value="<%=billInfo.getEmsNo()==null?"":billInfo.getEmsNo()%>"/><span style="color: red">*</span>
		</td>
		<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
			快递状态:&nbsp;&nbsp;&nbsp;
		</td>
		<td>
			<select id="emsInfo.emsStatus" name="emsInfo.emsStatus" >
					<option value="1" <s:if test='emsInfo.emsStatus=="1"'>selected</s:if><s:else></s:else>>打印已快递</option>
					<!-- <option value="2" <s:if test='emsInfo.emsStatus=="2"'>selected</s:if><s:else></s:else>>打印未快递</option> -->
					<option value="3" <s:if test='emsInfo.emsStatus=="3"'>selected</s:if><s:else></s:else>>已签收</option></select></td>
		</td>
		
	</tr>
	<%		
		
	%>
	
	<%
			}
		}
	}
	%>
	</table>
	<table id="tbl_tools" width="100%" border="0">
		<tr>
			<td align="left">
			<input type="button" class="tbl_query_button" value="保存"
			onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
			name="btWriteOff" id="btWriteOff" onclick="document.getElementById('submitFlag').value='S';save()" />
			<input type="button" class="tbl_query_button" value="返回"
			onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
			name="btCancel" id="btCancel" onclick="revoke()" />
			</td>
		</tr>
	</table>
			</td>
	</tr>
	</table>
	</form>
</body>
</html>