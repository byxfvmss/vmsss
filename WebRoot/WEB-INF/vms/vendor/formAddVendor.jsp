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
	<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
	<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
	<link href="<%=bopTheme2%>/css/editView.css" type="text/css" rel="stylesheet">
	<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
	<script type="text/javascript" src="<%=bopTheme%>/js/validator.js"></script>
	<!-- MessageBox -->
	<script src="<%=bopTheme%>/js/MessageBox/js/messageBox.js" type="text/javascript"></script>
	<link href="<%=bopTheme%>/js/MessageBox/css/messageBox.css" rel="stylesheet" type="text/css" />
	<script language="javascript" type="text/javascript">
		function save(){
			var msg = "0";
			if(fucCheckNull(document.getElementById("vendorCName"),"请输入供应商中文名称")==false) {		
				return false;
			}
			if(fucCheckCName(document.getElementById("vendorCName"),"请输入正确的供应商中文名称")==false) {		
				return false;
			}
			if(fucCheckNull(document.getElementById("vendorTaxNo"),"请输入纳税人识别号")==false) {		
				return false;
			}
			if(fucCheckMail(document.getElementById("vendorEmail"),"请按正确的邮箱地址格式输入")==false) {		
				return false;
			}
			if(fucCheckTPhone(document.getElementById("vendorPhone"),"请按正确的电话号码格式输入")==false) {		
				return false;
			}
			var vendorCName = document.getElementById("vendorCName").value;
			var vendorEName = document.getElementById("vendorEName").value;
			var vendorTaxNo = document.getElementById("vendorTaxNo").value;
			var vendorAccount = document.getElementById("vendorAccount").value;
			var vendorCBank = document.getElementById("vendorCBank").value;
			var vendorEBank = document.getElementById("vendorEBank").value;
			var vendorLinkman = document.getElementById("vendorLinkman").value;
			var vendorPhone = document.getElementById("vendorPhone").value;
			var vendorEmail = document.getElementById("vendorEmail").value;
			var vendorAddress = document.getElementById("vendorAddress").value;
			var taxpayerType = document.getElementById("taxpayerType").value;
			var addressee = document.getElementById("addressee").value;
			var addresseePhone = document.getElementById("addresseePhone").value;
			var addresseeZipcode = document.getElementById("addresseeZipcode").value;
			var addresseeAddress = document.getElementById("addresseeAddress").value;
			var addresseeAddressdetail = document.getElementById("addresseeAddressdetail").value;
			$.ajax({url: 'saveNewVendor.action',
					type: 'POST',
					async:false,
					data:{vendorCName:vendorCName, vendorEName:vendorEName, vendorTaxNo:vendorTaxNo, vendorAccount:vendorAccount,
				          vendorCBank:vendorCBank, vendorEBank:vendorEBank, vendorLinkman:vendorLinkman, vendorPhone:vendorPhone,
				          vendorEmail:vendorEmail, vendorAddress:vendorAddress, taxpayerType:taxpayerType, addressee:addressee,
				          addresseePhone:addresseePhone, addresseeZipcode:addresseeZipcode, addresseeAddress:addresseeAddress, addresseeAddressdetail:addresseeAddressdetail},
					dataType: 'html',
					timeout: 1000,
					error: function(){return false;},
					success: function(result){
						msg = result;
					}
					});
			var aa=new Array();
			aa=msg.split("|");
			alert(aa[1]);
			if(aa[0]==1){
				window.dialogArguments.saveVendorSuccess();
				window.close();
				return true;
			}else{
				return false;
			}
			
		}
		function fucCheckTPhone(obj,strAlertMsg) {
			strAddress=obj.value;
			strAddress=strAddress.replace(/^(\s)*|(\s)*$/g,"");//去掉字符串两边的空格
					
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
			   var m = new MessageBox(obj);
			   m.Show(strAlertMsg);	
			   obj.focus();	
			   return false;	
			 }
			 else
			 {
			    return true;
			 }
		 }
		
		function fucCheckCName(obj,strAlertMsg)
 			{
		strAddress=obj.value;
		strAddress=strAddress.replace(/^(\s)*|(\s)*$/g,"");//去掉字符串两边的空格
				
		//匹配规则：
		//只允许以字母开头，用a-z,A-Z,0-9以及下划线组成的email名
		//email后面的域名只允许字母或下划线开头,至少一个.,以字母或下划线结束
		var newPar=/^[\u4e00-\u9fa5]+$/;
		 	 
		    var tflag = newPar.test(strAddress);
				if(strAddress.length>0 && tflag==false)
				{
				   var m = new MessageBox(obj);
				   m.Show(strAlertMsg);	
				   obj.focus();	
				   return false;	
				 }
				 else
				 {
				    return true;
				 }
		 }
		function fucCheckAccount(obj,strAlertMsg)
 			{
		strAddress=obj.value;
		strAddress=strAddress.replace(/^(\s)*|(\s)*$/g,"");//去掉字符串两边的空格
				
		//匹配规则：
		//只允许以字母开头，用a-z,A-Z,0-9以及下划线组成的email名
		//email后面的域名只允许字母或下划线开头,至少一个.,以字母或下划线结束
		var newPar=/^[1-9]*[1-9][0-9]*$/;
		 	 
		    var tflag = newPar.test(strAddress);
				if(strAddress.length>0 && tflag==false)
				{
				   var m = new MessageBox(obj);
				   m.Show(strAlertMsg);	
				   obj.focus();	
				   return false;	
				 }
				 else
				 {
				    return true;
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
<form name="Form1" method="post" action="saveNewVendor.action" id="Form1">
	<div class="windowtitle" style="background:#004C7E; height:45px; line-height:45px; text-align:center; color:#FFF; font-weight:bold; font-size:14px;">新增供应商</div>
	<div class="windowtitle" style="background:#004C7E; height:30px; line-height:30px; text-align:left; color:#FFF; font-size:12px; margin-top: 10px;">供应商基本信息</div>
	<table id="tbl_context" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0" >
	<tr>
		<td width="15%" align="right">供应商中文名称:&nbsp;&nbsp;&nbsp;</td>
		<td width="35%">
			<input type="text" class="tbl_query_text" id="vendorCName" maxlength="100"/>&nbsp; 
			<span style="color:red;">*</span>
		</td>
		<td width="15%" align="right">供应商英文名称:&nbsp;&nbsp;&nbsp;</td>
		<td width="35%">
			<input type="text" class="tbl_query_text" id="vendorEName" maxlength="50"/>
		</td>
	</tr>
	<tr>
		<td align="right">供应商纳税人识别号:&nbsp;&nbsp;&nbsp;</td>
		<td >
			<input type="text" class="tbl_query_text" id="vendorTaxNo" maxlength="18"/>&nbsp; 
			<span style="color:red;">*</span>
		</td>
		<td align="right">供应商账号:&nbsp;&nbsp;&nbsp;</td>
		<td >
			<input type="text" class="tbl_query_text" id="vendorAccount" maxlength="20"/>
		</td>
	</tr>
	<tr>
		<td align="right">供应商开户银行中文名称:&nbsp;&nbsp;&nbsp;</td>
		<td >
			<input type="text" class="tbl_query_text" id="vendorCBank" maxlength="50"/>
		</td>
		<td align="right">供应商开户银行英文名称:&nbsp;&nbsp;&nbsp;</td>
		<td >
			<input type="text" class="tbl_query_text" id="vendorEBank" maxlength="20"/>
		</td>
	</tr>
	<tr>
		<td align="right">供应商联系人:&nbsp;&nbsp;&nbsp;</td>
		<td >
			<input type="text" class="tbl_query_text" id="vendorLinkman" maxlength="50"/>
		</td>
		<td align="right">供应商电话:&nbsp;&nbsp;&nbsp;</td>
		<td >
			<input type="text" class="tbl_query_text" id="vendorPhone" maxlength="20"/>
		</td>
	</tr>
	<tr>
		<td align="right">供应商邮箱地址:&nbsp;&nbsp;&nbsp;</td>
		<td >
			<input type="text" class="tbl_query_text" id="vendorEmail" maxlength="50"/>
		</td>
		<td align="right">供应商地址:&nbsp;&nbsp;&nbsp;</td>
		<td >
			<input type="text" class="tbl_query_text" id="vendorAddress" maxlength="50"/>
		</td>
	</tr>
	<tr>
		<td align="right">供应商类别:&nbsp;&nbsp;&nbsp;</td>
		<td colspan="3">
			<select id="taxpayerType" style="width:155px">
				<option value="S">小规模纳税人</option>
				<option value="G">一般纳税人</option>
				<option value="I">个体纳税人</option>
				<option value="O">其他</option>
			</select>
		</td>
	</tr>
	<tr>
	</tr>
	</table>
	<div class="windowtitle" style="background:#004C7E; height:30px; line-height:30px; text-align:left; color:#FFF; font-size:12px;">供应商快递信息</div>
	<table id="tbl_context" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0" >
	<tr>
		<td width="22%" align="right">收件人:&nbsp;&nbsp;&nbsp;</td>
		<td width="27%">
			<input type="text" class="tbl_query_text" id="addressee" maxlength="50"/>
		</td>
		<td width="22%" align="right">收件人电话:&nbsp;&nbsp;&nbsp;</td>
		<td width="27%">
			<input type="text" class="tbl_query_text" id="addresseePhone" maxlength="20"/>
		</td>
	</tr>
	<tr>
		<td align="right">收件人邮编:&nbsp;&nbsp;&nbsp;</td>
		<td >
			<input type="text" class="tbl_query_text" id="addresseeZipcode" maxlength="20"/>
		</td>
		<td align="right">收件地址:&nbsp;&nbsp;&nbsp;</td>
		<td >
			<input type="text" class="tbl_query_text" id="addresseeAddress" maxlength="50"/>
		</td>
	</tr>
	<tr>
		<td align="right">详细收件地址:&nbsp;&nbsp;&nbsp;</td>
		<td colspan="3">
			<input type="text" class="tbl_query_text" id="addresseeAddressdetail" maxlength="100"/>
		</td>
	</tr>
	</table>
	<div id="ctrlbutton" class="ctrlbutton" style="border:0px">
		<input type="button" class="tbl_query_button" value="保存"
			onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
			name="btSave" id="btSave" onclick="save()" />
		<input type="button" class="tbl_query_button" value="关闭"
			onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
			name="btCancel" id="btCancel" onclick="window.close()" />
	</div>
	<script language="javascript" type="text/javascript">
	</script>
	</form>
</body>
</html>