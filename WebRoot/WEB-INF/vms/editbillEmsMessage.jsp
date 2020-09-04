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
		var fl='<s:if test='emsInfo.emsStatus=="3"'>1</s:if><s:else>0</s:else>'
function submitEMsaFormt(){
	var messge="";
	 if(fucCheckTaPhone(document.getElementById("emsInfo.addresseePhone"),"")==false){
		 messge+="电话号码格式不正确！\n";
	 }
	 if(fucCheckZip(document.getElementById("emsInfo.addresseeZipcode"),"")==false){
		 messge+="邮编格式不正确！\n";
	 }
	 if (messge != ''){
		alert(messge);
		return false;
	}else{
		return true;
	}
}
		function fucCheckTaPhone(obj,strAlertMsg)
 			{
		var strAddress=obj.value;
		strAddress=strAddress;//去掉字符串两边的空格
				
		//匹配规则：
		//只允许以字母开头，用a-z,A-Z,0-9以及下划线组成的email名
		//email后面的域名只允许字母或下划线开头,至少一个.,以字母或下划线结束
		//var newPar=/^[a-zA-Z](\w*)@\w+\.(\w|.)*\w+$/
		 	 var regMobile=/^0?1[3|4|5|8][0-9]\d{8}$/;
		     var   regTel =/^0[\d]{2,3}-[\d]{7,8}$/;
		     var mflag = regMobile.test(strAddress); 
		    var tflag = regTel.test(strAddress);
				if(strAddress.length>0 && mflag==false && tflag==false)
				{
				 
				   return false;	
				 }
				 else
				 {
				    return true;
				 }
		 }
		function fucCheckZip(obj,strAlertMsg)
 			{
		var strAddress=obj.value;
		strAddress=strAddress;//去掉字符串两边的空格
				
		//匹配规则：
		//只允许以字母开头，用a-z,A-Z,0-9以及下划线组成的email名
		//email后面的域名只允许字母或下划线开头,至少一个.,以字母或下划线结束
		var newPar= /^[1-9][0-9]{5}$/;
		 	 
		    var tflag = newPar.test(strAddress);
				if(strAddress.length>0 && tflag==false)
				{
				  
				   return false;	
				 }
				 else
				 {
				    return true;
				 }
		 }
		function save(){
			if(fl=="1"){
			if(document.getElementById("emsInfo.emsStatus").value=='1')
				alert("已签收状态不可改为打印已快递");
				return;
			}
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
			if (!submitEMsaFormt()){
					return false;
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
<body onmousemove="MM(event)" onmouseout="MO(event)">
	<form name="Form1" method="post" action="saveEmsMessage.action?flag=edit" id="Form1">
		<input type="hidden" name="billId" value="<s:property value='billInfo.billId'/>" />
		<input type="hidden" name="billInfo.billId" value="<s:property value='billInfo.billId'/>" />
		<input type="hidden" name="billInfo.dataStatus" value="<s:property value='billInfo.dataStatus'/>" />
		<input type="hidden" name="submitFlag" id="submitFlag" value="U" />
		<input type="hidden" name="emsId" value="<s:property value='emsId'/>" />
		<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
			<tr>
				<td class="centercondition">
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" />
						<span class="current_status_menu">当前位置：</span>
						<span class="current_status_submenu1">销项税管理</span>
						<span class="current_status_submenu">开票管理</span>
						<span class="current_status_submenu">发票快递</span>
						<span class="current_status_submenu">信息编辑</span>
					</div>
					</br>
					</br>
					<table id="tbl_context" cellspacing="0" width="100%" align="center" cellpadding="0">
						<tr class="row1">
							<td align="right" width="13%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">开票日期:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
							<td align="left" width="20%">
								<input type="text" class="tbl_query_text2" name="emsInfo.billDate" id="emsInfo.billDate" value="<s:property value='billInfo.billDate'/>" onfocus="this.blur();"
									style="background-color: #CCCCCC;"
								/>
							</td>
							<td align="right" width="13%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">发票代码:&nbsp;&nbsp;&nbsp;</td>
							<td align="left" width="20%">
								<input type="text" class="tbl_query_text2" name="emsInfo.billCode" id="emsInfo.billCode" value="<s:property value='billInfo.billCode'/>" onfocus="this.blur();"
									style="background-color: #CCCCCC;"
								/>
							</td>
							<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">发票号码:&nbsp;&nbsp;&nbsp;</td>
							<td align="left" width="20%">
								<input type="text" class="tbl_query_text2" name="emsInfo.billNo" id="emsInfo.billNo" value="<s:property value='billInfo.billNo'/>" onfocus="this.blur();" style="background-color: #CCCCCC;" />
							</td>
						</tr>
						<tr class="row1">
							<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">客户纳税人名称:&nbsp;&nbsp;&nbsp;</td>
							<td align="left">
								<input type="text" class="tbl_query_text2" name="emsInfo.customerName" id="emsInfo.customerName" value="<s:property value='billInfo.customerName'/>" onfocus="this.blur();"
									style="background-color: #CCCCCC;"
								/>
							</td>
							<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">客户纳税人识别号:&nbsp;&nbsp;&nbsp;</td>
							<td align="left" colspan="3">
								<input type="text" class="tbl_query_text2" name="emsInfo.customerTaxno" id="emsInfo.customerTaxno" value="<s:property value='billInfo.customerTaxno'/>" onfocus="this.blur();"
									style="background-color: #CCCCCC;"
								/>
							</td>
						</tr>
						<tr class="row1">
							<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">客户联系人:&nbsp;&nbsp;&nbsp;</td>
							<td align="left">
								<input type="text" class="tbl_query_text2" name="emsInfo.customerLinkman" id="emsInfo.customerLinkman" value="<s:property value='billInfo.customerLinkman'/>" />
							</td>
							<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">客户收件人:&nbsp;&nbsp;&nbsp;</td>
							<td align="left">
								<input type="text" class="tbl_query_text2" name="emsInfo.addressee" id="emsInfo.addressee" value="<s:property value='billInfo.addressee'/>" />
								<span style="color: red">*</span>
							</td>
							<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">客户收件人电话:&nbsp;&nbsp;&nbsp;</td>
							<td align="left">
								<input type="text" class="tbl_query_text2" name="emsInfo.addresseePhone" id="emsInfo.addresseePhone" value="<s:property value='billInfo.addresseePhone' />" maxlength="11" />
								<span style="color: red">*</span>
							</td>
						</tr>
						<tr class="row1">
							<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">客户收件邮编:&nbsp;&nbsp;&nbsp;</td>
							<td align="left">
								<input type="text" class="tbl_query_text2" name="emsInfo.addresseeZipcode" id="emsInfo.addresseeZipcode" value="<s:property value='billInfo.addresseeZipcode'/>" maxlength="6" />
							</td>
							<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">收件地址:&nbsp;&nbsp;&nbsp;</td>
							<td align="left" colspan="3">
								<input type="text" class="tbl_query_text2" name="emsInfo.addresseeAddress" id="emsInfo.addresseeAddress" value="<s:property value='billInfo.addresseeAddress'/>" size="76" />
								<span style="color: red">*</span>
							</td>
						</tr>
						<tr class="row1">
							<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">寄件人:&nbsp;&nbsp;&nbsp;</td>
							<td>
								<input type="text" class="tbl_query_text2" name="emsInfo.sender" id="emsInfo.sender" value="<s:property value='emsInfo.sender'/>" />
								<span style="color: red">*</span>
							</td>
							<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">详细收件地址:&nbsp;&nbsp;&nbsp;</td>
							<td align="left" colspan="3">
								<input type="text" class="tbl_query_text2" name="emsInfo.addresseeAddressdetail" id="emsInfo.addresseeAddressdetail" value="<s:property value='billInfo.addresseeAddressdetail'/>" size="76" />
								<span style="color: red">*</span>
							</td>
						</tr>
						<tr class="row1">
							<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">快递公司:&nbsp;&nbsp;&nbsp;</td>
							<td>
								<input type="text" class="tbl_query_text2" name="emsInfo.fedexExpress" id="emsInfo.fedexExpress" value="<s:property value='emsInfo.fedexExpress'/>" />
								<span style="color: red">*</span>
							</td>
							<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">快递单号:&nbsp;&nbsp;&nbsp;</td>
							<td>
								<input type="text" class="tbl_query_text2" name="emsInfo.emsNo" id="emsInfo.emsNo" value="<s:property value='emsInfo.emsNo'/>" />
								<span style="color: red">*</span>
							</td>
							<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">快递状态:&nbsp;&nbsp;&nbsp;</td>
							<td>
								<select id="emsInfo.emsStatus" name="emsInfo.emsStatus">
									<option value="1" <s:if test='emsInfo.emsStatus=="1"'>selected</s:if> <s:else></s:else>>打印已快递</option>
									<!-- <option value="2" <s:if test='emsInfo.emsStatus=="2"'>selected</s:if><s:else></s:else>>打印未快递</option> -->
									<option value="3" <s:if test='emsInfo.emsStatus=="3"'>selected</s:if> <s:else></s:else>>已签收</option>
								</select>
							</td>
							</td>
						</tr>
					</table>
					<table id="tbl_tools" width="100%" border="0">
						<tr>
							<td align="left">
								<input type="button" class="tbl_query_button" value="保存" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" name="btWriteOff" id="btWriteOff"
									onclick="document.getElementById('submitFlag').value='S';save()"
								/>
								<input type="button" class="tbl_query_button" value="返回" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" name="btCancel" id="btCancel"
									onclick="revoke()"
								/>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>