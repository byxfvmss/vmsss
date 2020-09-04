<!--file: <%=request.getRequestURI()%> -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
%>

<%@include file="../../../../page/modalPage.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<%@ include file="../../../../page/include.jsp"%>


<html>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/editview.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/XmlHttp.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/vms.js"></script>
	<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
	<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
	<link href="<%=bopTheme2%>/css/editView.css" type="text/css" rel="stylesheet">
	<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
	<script type="text/javascript" src="<%=bopTheme%>/js/validator.js"></script>
	<!-- MessageBox -->
	<script src="<%=bopTheme%>/js/MessageBox/js/messageBox.js" type="text/javascript"></script>
	<link href="<%=bopTheme%>/js/MessageBox/css/messageBox.css" rel="stylesheet" type="text/css" />


	<!-- MessageBox -->
	<script src="<%=bopTheme%>/js/MessageBox/js/messageBox.js" type="text/javascript"></script>
	<link href="<%=bopTheme%>/js/MessageBox/css/messageBox.css" rel="stylesheet" type="text/css" />
<style>
.showStar {
	display: inline !important;
}

.hiddenStar {
	display: none !important;
}
</style>

<script language="javascript" type="text/javascript">

//标识页面是否已提交
var subed = false;
$(function(){
	checkFapiaoType();
});

function fucCheckTMail(obj, strAlertMsg) {
	strAddress = obj.value;
	strAddress = strAddress.replace(/^(\s)*|(\s)*$/g, "");//去掉字符串两边的空格

	//匹配规则：
	//只允许以字母开头，用a-z,A-Z,0-9以及下划线组成的email名
	//email后面的域名只允许字母或下划线开头,至少一个.,以字母或下划线结束
	//var newPar=/^[a-zA-Z](\w*)@\w+\.(\w|.)*\w+$/
	var newPar = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;

	if (strAddress.length > 0 && newPar.test(strAddress) == false) {

		return false;
	} else {
		return true;
	}
}
function fucCheckTPhone(obj, strAlertMsg) {
	strAddress = obj.value;
	strAddress = strAddress.replace(/^(\s)*|(\s)*$/g, "");//去掉字符串两边的空格

	//匹配规则：
	//只允许以字母开头，用a-z,A-Z,0-9以及下划线组成的email名
	//email后面的域名只允许字母或下划线开头,至少一个.,以字母或下划线结束
	//var newPar=/^[a-zA-Z](\w*)@\w+\.(\w|.)*\w+$/
	var regMobile = /^0?1[3|4|5|8][0-9]\d{8}$/;
	var regTel = /^0[\d]{2,3}-[\d]{7,8}$/;
	var mflag = regMobile.test(strAddress);
	var tflag = regTel.test(strAddress);
	if (strAddress.length > 0 && mflag == false && tflag == false) {

		return false;
	} else {
		return true;
	}
}
function fucCheckCName(obj,strAlertMsg){

	strAddress=obj.value;
	strAddress=strAddress.replace(/^(\s)*|(\s)*$/g,"");//去掉字符串两边的空格
			
	var fapiaoType = $("#fapiaoType",$("#formBusiness")).val();
	
	if(fapiaoType&&fapiaoType=='0'){//专用发票
	
		//中文校验
		var newPar = /^[\u4e00-\u9fa5]+$/;

		var tflag = newPar.test(strAddress);
		
		
		if (strAddress.length<=0||(strAddress.length > 0 && tflag == false)) {

			return false;
		} else {
			return true;
		}
	
	}
}
function fucCheckAccount(obj,strAlertMsg) {
		strAddress=obj.value;
		strAddress=strAddress.replace(/^(\s)*|(\s)*$/g,"");//去掉字符串两边的空格
				
		//匹配规则：
		//只允许以字母开头，用a-z,A-Z,0-9以及下划线组成的email名
		//email后面的域名只允许字母或下划线开头,至少一个.,以字母或下划线结束
		var newPar=/^[1-9]*[1-9][0-9]*$/;
		 	 
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
function fucCheckTaxNo(obj,strAlertMsg){
		strAddress=obj.value;
		strAddress=strAddress.replace(/^(\s)*|(\s)*$/g,"");//去掉字符串两边的空格
				
		//匹配规则：
		//只允许以字母开头，用a-z,A-Z,0-9以及下划线组成的email名
		//email后面的域名只允许字母或下划线开头,至少一个.,以字母或下划线结束
		var newPar=/^[0-9a-zA-Z]*$/g;
		 	 
		    var tflag = newPar.test(strAddress);
				if(strAddress.length>0 && tflag==false)
				{
				  
				   return false;	
				 }else if(strAddress.length>0&&strAddress.length<15){
					 return false;
				 }
				 else
				 {
				    return true;
				 }
		 }
function fucChecTNull(obj,strAlertMsg)
		{
		    strTemp=obj.value;
		    // 去掉字符串两边的空格
			strTemp=strTemp.replace(/^(\s)*|(\s)*$/g,"");
			if (strTemp.length<1)
				{
					//var m = new MessageBox(obj);
					//m.Show(strAlertMsg);
					//obj.focus();
					return false;
				}else{
				
					return true;
				}
		}
function fucCheckZip(obj,strAlertMsg)
 			{
		strAddress=obj.value;
		strAddress=strAddress.replace(/^(\s)*|(\s)*$/g,"");//去掉字符串两边的空格
				
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
function funCheckTname(){
		//	if(fucChecTNull(document.getElementById("customerCName"),"请输入客户中文名称")==false) {
				
			//	return false;
			//}
			if(fucCheckCName(document.getElementById("customerCName"),"")==false) {	
				document.getElementById("name").style.display='inline';
				document.getElementById("name1").style.display='none';
				return false;
			}else{
				document.getElementById("name").style.display='none';
				document.getElementById("name1").style.display='none';
				return true;
			}
}
function funCheckgTaxNo(){
			if(fucCheckTaxNo(document.getElementById("customerTaxno"),"")==false){
				document.getElementById("taxNo").style.display='inline';
				document.getElementById("taxNo1").style.display='none';
				return false;
			}else{
				document.getElementById("taxNo").style.display='none';
				document.getElementById("taxNo1").style.display='none';
								return true;
				
			}
			
		}
		
function funCheckAccount(){
			if(fucCheckAccount(document.getElementById("customerAccount"),"")==false) {		
				document.getElementById("account").style.display='inline';
				document.getElementById("account1").style.display='none';
				return false;
			}else{
				document.getElementById("account").style.display='none';
				document.getElementById("account1").style.display='none';
			
							return true;
			
			}
			
		}
function funCheckphone(){
			
			if(fucCheckTPhone(document.getElementById("customerPhone"),"电话号码不正确")==false) {		
				document.getElementById("phone").style.display='inline';
				document.getElementById("phone1").style.display='none1';
				return false;
			}else{
				document.getElementById("phone").style.display='none';
				document.getElementById("phone1").style.display='none';
							return true;
			
			}
				
			
		}
function funCheckemail(){
			if(fucCheckTMail(document.getElementById("customerEmail"),"请按正确的邮箱地址格式输入")==false) {		
				document.getElementById("email").style.display='inline';
				document.getElementById("email1").style.display='none';
				return false;
			}else{
				document.getElementById("email").style.display='none';
				document.getElementById("email1").style.display='none';
						return true;
		
			}
		}
function funCheckLinkphone(){
			
			if(fucCheckTPhone(document.getElementById("linkPhone"),"电话号码不正确")==false) {		
				document.getElementById("ph").style.display='inline';
				return false;
			}else{
				document.getElementById("ph").style.display='none';
							return true;
			
			}
				
			
		}
function funCheckLinkZip(){
			
			if(fucCheckZip(document.getElementById("customerZipCode"),"不正确")==false) {		
				document.getElementById("zip").style.display='inline';
				return false;
			}else{
				document.getElementById("zip").style.display='none';
							return true;
			
			}
				
			
		}

function findOutSubmit() {

	document.getElementById("fapiaoType").removeAttribute("disabled");
	
	
	var $form = $("#formBusiness");
	$("select[name='customerNationality']",$form).attr("disabled",false);
		
	var customerNationality = $("select[name='customerNationality']",$form).val();//国籍，专用发票必须为中国	
	
	var fapiaoType = $("#fapiaoType",$form).val();
	if(fapiaoType&&fapiaoType=='0'){
	
		//if($("#customerTaxno1",$form)&&$("#customerTaxno1",$form).val().trim().length<=0){//客户识别码
		if($("#customerTaxno",$form)&&!fucChecTNull($("#customerTaxno",$form)[0])){//客户识别码
			$("#taxNo1",$form).css("display","inline");
			$("#taxNo",$form).css("display","none");
		 	return false;
		
		}else{
			$("#taxNo1",$form).css("display","none");
			$("#taxNo",$form).css("display","none");
		}
		if($("#customerAddress",$form)&&!fucChecTNull($("#customerAddress",$form)[0])){//地址
			$("#address1",$form).css("display","inline");
		 	return false;
		
		}else{
			$("#address1",$form).css("display","none");
		}
		
		if($("#customerPhone",$form)&&!fucChecTNull($("#customerPhone",$form)[0])){//电话
			$("#phone1",$form).css("display","inline");
			$("#phone",$form).css("display","none");
		 	return false;
		
		}else{
			$("#phone1",$form).css("display","none");
			$("#phone",$form).css("display","none");
		}
		
		if($("#customerEmail",$form)&&!fucChecTNull($("#customerEmail",$form)[0])){//邮箱
			$("#email1",$form).css("display","inline");
			$("#email",$form).css("display","none");
		 	return false;
		
		}else{
			$("#email1",$form).css("display","none");
			$("#email",$form).css("display","none");
		}
		
		if($("#customerCBank",$form)&&!fucChecTNull($("#customerCBank",$form)[0])){//开户行
			$("#bank1",$form).css("display","inline");
			$("#bank",$form).css("display","none");
		 	return false;
		
		}else{
			$("#bank1",$form).css("display","none");
			$("#bank",$form).css("display","none");
		}
		
		if($("#customerAccount",$form)&&!fucChecTNull($("#customerAccount",$form)[0])){//开户账号
			$("#account1",$form).css("display","inline");
			$("#account",$form).css("display","none");
		 	return false;
		
		}else{
			$("#account1",$form).css("display","none");
			$("#account",$form).css("display","none");
		}
		
		if(customerNationality!='CHN'){
		 	alert("国籍请选择中国");
		 	return false;
		}
		
	}
	
	
	
	if(funCheckemail()&&funCheckTname()&&funCheckphone()&&funCheckAccount()&&funCheckgTaxNo()&&funCheckLinkphone()&&funCheckLinkZip()){
			var taxpayerType=document.getElementById("taxpayerType").value;
		if(fucChecTNull(document.getElementById("customerCName"),"")==false) {		
				document.getElementById("name1").style.display='inline';
				return false;
		}else{
			document.getElementById("name1").style.display='none';
		}
		if(taxpayerType=='G'){
		
		/*	if(fucChecTNull(document.getElementById("customerTaxno"),"")==false) {
				document.getElementById("taxNo1").style.display='inline';
				return false;
			}else{
				document.getElementById("taxNo1").style.display='none';
			}
			
			if(fucChecTNull(document.getElementById("customerCBank"),"")==false) {
				document.getElementById("bank1").style.display='inline';
				return false;
			}else{
				document.getElementById("bank1").style.display='none';
			}
			if(fucChecTNull(document.getElementById("customerAccount"),"")==false) {
				document.getElementById("account1").style.display='inline';
				return false;
			}else{
				document.getElementById("account1").style.display='none';
			}
			if(fucChecTNull(document.getElementById("customerAddress"),"")==false) {
				document.getElementById("address1").style.display='inline';
				return false;
			}else{
				document.getElementById("address1").style.display='none';
			}
		
			if(fucChecTNull(document.getElementById("customerPhone"),"")==false) {
				document.getElementById("phone1").style.display='inline';
				return false;
			}else{
				document.getElementById("phone1").style.display='none';
			}
			
			
			if(fucChecTNull(document.getElementById("customerEmail"),"")==false) {
					document.getElementById("email1").style.display='inline';
					return false;
				}else{
				document.getElementById("email1").style.display='none';
			}*/
		}
		var form = document.getElementById("formBusiness");
		form.action='saveEditCustomer.action';
		form.submit();
		document.getElementById("btn_edit").disabled=true;
	
}else{
	return false;
}
}
function limit(a){
	if(a!='G'){
	document.getElementById("fapiaoType").value='1';
	document.getElementById('fapiaoType').disabled="disabled"; 



	}else{
			document.getElementById('fapiaoType').disabled=false; 
		
	}
			
}


function checkFapiaoType(){
	
	var fapiaoType = $("#fapiaoType",$("#formBusiness")).val();
	if(fapiaoType&&fapiaoType=='0'){
		$("span.checkFapiaoType",$("#formBusiness")).removeClass("hiddenStar").addClass("showStar");
		$("select[name='customerNationality']",$("#formBusiness")).val("CHN");
		$("select[name='customerNationality']",$("#formBusiness")).attr("disabled","disabled");
	}else{
		$("span.checkFapiaoType",$("#formBusiness")).removeClass("showStar").addClass("hiddenStar");
	}
	
}
</script>
</head>
	<body>
		<form name="formBusiness" id="formBusiness" method="post">
			<div class="windowtitle"
				style="background: #004C7E; height: 45px; line-height: 45px; text-align: center; color: #FFF; font-weight: bold; font-size: 14px;">
				编辑客户
			</div>
			<div class="windowtitle"
				style="background: #004C7E; height: 30px; line-height: 30px; text-align: left; color: #FFF; font-size: 12px; margin-top: 10px;">
				客户基本信息
			</div>
			<div id="editsubpanel" class="editsubpanel">
				<div style="overflow: auto; width: 100%;">
					<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
						<tr>

						</tr>
						<tr>
							<td width="15%" align="right" class="listbar">
								客户名称:
							</td>
							<td width="35%">
								<input size="30" id="customerCName" class="tbl_query_text" name="customerCName" type="text" maxlength="20"
									value="<s:property value='customer.customerCName'/>" onblur="funCheckTname()" />
								&nbsp;
								<span style="color: red;">*</span>
								<label id="name" style="display: none; color: #FF0000; font-size: 14">
									请输入中文
								</label>
								<label id="name1" style="display: none; color: #FF0000; font-size: 14">
									请输入
								</label>
							</td>
							<td width="15%" align="right" class="listbar">
								纳税人类型:
							</td>
							<td width="35%">
								<select id="taxpayerType" name="taxpayerType" onchange="limit(this.value)">
									<option value="G" <s:if test='customer.taxPayerType=="G"'>selected</s:if> <s:else></s:else>>
										一般纳税人
									</option>
									<option value="S" <s:if test='customer.taxPayerType=="S"'>selected</s:if> <s:else></s:else>>
										小规模纳税人
									</option>
									<option value="I" <s:if test='customer.taxPayerType=="I"'>selected</s:if> <s:else></s:else>>
										个体纳税人
									</option>
									<option value="O" <s:if test='customer.taxPayerType=="O"'>selected</s:if> <s:else></s:else>>
										其他
									</option>
								</select>
							</td>
						</tr>


						<tr>
							<td width="15%" align="right" class="listbar">
								客户纳税人识别号:
							</td>
							<td width="35%">
								<input id="customerTaxno1" name="customerTaxno1" type="hidden"
									value="<s:property value='customer.customerTaxno'/>" />
								<input size="30" id="customerTaxno" class="tbl_query_text" name="customerTaxno" type="text"
									value="<s:property value='customer.customerTaxno'  />" onblur="funCheckgTaxNo()" maxlength="18" />
								&nbsp;
								<span style="color: red;" class="checkFapiaoType hiddenStar">*</span>
								<label id="taxNo" style="display: none; color: #FF0000; font-size: 14">
									纳税人的格式有误
								</label>
								<label id="taxNo1" style="display: none; color: #FF0000; font-size: 14">
									请输入
								</label>
							</td>
							<td width="15%" align="right" class="listbar">
								国籍:
							</td>
							<td width="35%">
								<s:select id="customer.customerNationality" name="customerNationality" list="countrys"
									listKey='customerNationality' listValue='countrySName' headerKey="" value='customer.customerNationality'
									headerValue="所有" />
							</td>
						</tr>

						<tr>

							<td width="15%" align="right" class="listbar">
								地址:
							</td>
							<td width="35%">
								<input size="40" id="customerAddress" class="tbl_query_text" name="customerAddress" type="text" maxlength="20"
									value="<s:property value='customer.customerAddress'/>" />
								&nbsp;
								<span style="color: red;" class="checkFapiaoType hiddenStar">*</span>
								<label id="address1" style="display: none; color: #FF0000; font-size: 14">
									请输入
								</label>
							</td>
							<td width="15%" align="right" class="listbar">
								电话:
							</td>
							<td width="35%">
								<input size="30" id="customerPhone" class="tbl_query_text" name="customerPhone" type="text" maxlength="20"
									value="<s:property value='customer.customerPhone'/>" onblur="funCheckphone()" />
								&nbsp;
								<span style="color: red;" class="checkFapiaoType hiddenStar">*</span>
								<label id="phone" style="display: none; color: #FF0000; font-size: 14">
									电话格式错误
								</label>
								<label id="phone1" style="display: none; color: #FF0000; font-size: 14">
									请输入
								</label>
							</td>


						</tr>
						<tr>
							<td width="15%" align="right" class="listbar">
								邮箱:
							</td>
							<td width="35%">
								<input size="30" id="customerEmail" class="tbl_query_text" name="customerEmail" type="text"
									value="<s:property value='customer.customerEMail'/>" onblur="funCheckemail()" />
								&nbsp;
								<span style="color: red;" class="checkFapiaoType hiddenStar">*</span>
								<label id="email" style="display: none; color: #FF0000; font-size: 14">
									邮箱格式错误
								</label>
								<label id="email1" style="display: none; color: #FF0000; font-size: 14">
									请输入
								</label>
							</td>
							<td width="15%" align="right" class="listbar">
								开户银行
							</td>
							<td width="35%">
								<input size="30" id="customerCBank" class="tbl_query_text" name="customerCBank" type="text"
									value="<s:property value='customer.customerCBank'/>" />
								&nbsp;
								<span style="color: red;" class="checkFapiaoType hiddenStar">*</span>
								<label id="bank" style="display: none; color: #FF0000; font-size: 14">
									请输入字母或数字
								</label>
								<label id="bank1" style="display: none; color: #FF0000; font-size: 14">
									请输入
								</label>
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="listbar">
								开户账号:
							</td>
							<td width="35%">
								<input size="30" id="customerAccount" class="tbl_query_text" name="customerAccount" type="text" maxlength="20"
									value="<s:property value='customer.customerAccount'/>" onblur="funCheckAccount()" />
								&nbsp;
								<span style="color: red;" class="checkFapiaoType hiddenStar">*</span>
								<label id="account" style="display: none; color: #FF0000; font-size: 14">
									请输入数字
								</label>
								<label id="account1" style="display: none; color: #FF0000; font-size: 14">
									请输入
								</label>
							</td>
							<td width="15%" align="right" class="listbar">
								客户类型:
							</td>
							<td width="35%">
								<select id="customerType" name="customerType">
									<option value="I" <s:if test='customer.customerType=="I"'>selected</s:if> <s:else></s:else>>
										私人客户
									</option>
									<option value="C" <s:if test='customer.customerType=="C"'>selected</s:if> <s:else></s:else>>
										公司客户
									</option>
								</select>
							</td>

						</tr>

						<tr>


							<td width="15%" align="right" class="listbar">
								发票类型:
							</td>
							<td width="35%">
								<select id="fapiaoType" name="fapiaoType" <s:if test='customer.taxPayerType!="G"'>disabled</s:if> onchange="checkFapiaoType()">
									发票类型
									<option value="0" <s:if test='customer.fapiaoType=="0"'>selected</s:if> <s:else></s:else>>
										增值税专用发票
									</option>
									<option value="1" <s:if test='customer.fapiaoType=="1"'>selected</s:if> <s:else></s:else>>
										增值税普通发票
									</option>
								</select>
							<td width="15%" align="right" class="listbar">
								是否打票:
							</td>
							<td width="35%">
								<select id="customerFapiaoFlag" name="customerFapiaoFlag">
									<option value="A" <s:if test='customer.customerFapiaoFlag=="A"'>selected</s:if> <s:else></s:else>>
										自动打印
									</option>
									<option value="M" <s:if test='customer.customerFapiaoFlag=="M"'>selected</s:if> <s:else></s:else>>
										手动打印
									</option>
									<option value="N" <s:if test='customer.customerFapiaoFlag=="N"'>selected</s:if> <s:else></s:else>>
										永不打印
									</option>
								</select>

							</td>
						</tr>
						<tr>
							<td class="listbar" align="right">
								客户编号
							</td>
							<td>
								<s:property value='customer.customerID' />
								<input type="hidden" value="<s:property value='customer.customerID'/>" name="customerId">
							</td>
							<td width="15%" align="right" class="listbar">
								数据来源
							</td>
							<td width="35%">
								<s:select list="dataSourceList" name="datasSource" id="datasSource" headerKey="1" listKey="value"
									listValue="text" disabled="true"></s:select>

							</td>
						</tr>

						<%--<tr>
		<td width="15%" align="right" class="listbar">GHO类</td>
		<td width="35%"><input type="text" name="ghoClass" id="ghoClass"  class="tbl_query_text" value="<s:property value="customer.ghoClass"/>"/></td>
		
	</tr>
	--%>
					</table>
					<div class="windowtitle"
						style="background: #004C7E; height: 30px; line-height: 30px; text-align: left; color: #FFF; font-size: 12px;">
						客户快递信息
					</div>
					<table id="tbl_context" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">

						<tr>
							<td width="15%" align="right" class="listbar">
								联系人
							</td>
							<td width="35%">
								<input size="40" id="linkName" class="tbl_query_text" name="linkName" type="text" maxlength="30"
									value="<s:property value='customer.linkName'/>" />
								&nbsp;
							</td>
							<td width="15%" align="right" class="listbar">
								联系人电话
							</td>
							<td width="35%">
								<input size="40" id="linkPhone" class="tbl_query_text" name="linkPhone" type="text" maxlength="15"
									value="<s:property value='customer.linkPhone'/>" onblur="funCheckLinkphone()" />
								&nbsp;
								<label id="ph" style="display: none; color: #FF0000; font-size: 14">
									电话格式错误
								</label>
							</td>
						</tr>
						<tr>

						</tr>
						<tr>
							<td width="15%" align="right" class="listbar">
								联系人地址
							</td>
							<td width="35%">
								<input size="40" id="linkAddress" class="tbl_query_text" name="linkAddress" type="text" maxlength="50"
									value="<s:property value='customer.linkAddress'/>" />
								&nbsp;
							</td>
							<td width="15%" align="right" class="listbar">
								客户邮编
							</td>
							<td width="35%">
								<input size="40" id="customerZipCode" class="tbl_query_text" name="customerZipCode" type="text" maxlength="50"
									value="<s:property value='customer.customerZipCode'/>" onblur="funCheckLinkZip()" />
								&nbsp;
								<label id="zip" style="display: none; color: #FF0000; font-size: 14">
									邮编格式错误
								</label>

							</td>
						</tr>




					</table>
					<div id="ctrlbutton" class="ctrlbutton" style="border: 0px">
						<input type="button" class="tbl_query_button" id="btn_edit" onMouseMove="this.className='tbl_query_button_on'"
							onMouseOut="this.className='tbl_query_button'" onclick="findOutSubmit()" name="BtnSave" value="保存" id="BtnSave" />
						<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'"
							onMouseOut="this.className='tbl_query_button'" onclick="window.close()" name="BtnReturn" value="取消"
							id="BtnReturn" />
					</div>
		</form>
	</body>
</html>