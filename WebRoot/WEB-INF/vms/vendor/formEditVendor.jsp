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
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<%@ include file="../../../page/include.jsp"%>
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
			var vendorId = document.getElementById("vendorId").value;
			var vendorCName = document.getElementById("vendorCName").value;
			var vendorEName = document.getElementById("vendorEName").value;
			var vendorAccount = document.getElementById("vendorAccount").value;
			var vendorCBank = document.getElementById("vendorCBank").value;
			var vendorEBank = document.getElementById("vendorEBank").value;
			var vendorLinkman = document.getElementById("vendorLinkman").value;
			var vendorPhone = document.getElementById("vendorPhone").value;
			var vendorTaxNo = document.getElementById("vendorTaxNo").value;
			var vendorEmail = document.getElementById("vendorEmail").value;
			var vendorAddress = document.getElementById("vendorAddress").value;
			var taxpayerType = document.getElementById("taxpayerType").value;
			var addressee = document.getElementById("addressee").value;
			var addresseePhone = document.getElementById("addresseePhone").value;
			var addresseeZipcode = document.getElementById("addresseeZipcode").value;
			var addresseeAddress = document.getElementById("addresseeAddress").value;
			var addresseeAddressdetail = document.getElementById("addresseeAddressdetail").value;
			var vendorTaxNo1 = document.getElementById("vendorTaxNo1").value;
			
			$.ajax({url: 'saveEditVendor.action',
					type: 'POST',
					async:false,
					data:{vendorId:vendorId, vendorCName:vendorCName, vendorEName:vendorEName, vendorAccount:vendorAccount,
				          vendorCBank:vendorCBank, vendorEBank:vendorEBank, vendorLinkman:vendorLinkman, vendorPhone:vendorPhone,vendorTaxNo:vendorTaxNo,vendorTaxNo1:vendorTaxNo1,
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
			if(aa[0]=='1'){
				alert(aa[1]);
				window.dialogArguments.saveVendorSuccess();
				window.close();
				return true;
			}else{
				alert(aa[1]);
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
<%--
private String vendorId;// vendor_id varchar2(50)
	private String ;// vendor_cname varchar2(100) not null,/*供应商中文名称*/
	private String ;// vendor_ename varchar2(100) not null,/*供应商英文名称*/
	private String ;// vendor_taxno varchar2(25) not null,/*供应商纳税人识别号，即税务登记号*/
	private String ;// vendor_account varchar2(50) not null,/*账号，主键*/
	private String ;// vendor_cbank varchar2(100) not null,/*供应商开户银行中文名称*/
	private String ;// vendor_ebank varchar2(100) not null,/*供应商开户银行英文名称*/
	private String ;// vendor_phone varchar2(50) not null,/*供应商电话*/
	private String ;// vendor_email varchar2(100),/*供应商地址*/
	private String ;// vendor_address varchar2(100),/*供应商地址*/
	private String ;// vendor_linkman varchar2(100) not null,/*供应商联系人*/
	private String ;// addressee varchar2(100),/*供应商收件人*/
	private String ;// addressee_phone varchar2(50),/*供应商收件人电话*/
	private String ;// addressee_address varchar2(100),/*供应商收件地址*/
	private String ;// addressee_addressdetail varchar2(100),/*供应商详细收件地址*/
	private String ;// addressee_zipcode varchar2(100),/*供应商收件邮编*/
	private String ;  //供应商纳税人类别  S/G/O/I    S-小规模纳税人    G-一般纳税人    O-其他    I-个体纳税人
	private String dataOperationLabel;//DATA_OPERATION_LABEL	VARCHAR2(1)	N			数据操作标志
	private String dataAuditStatus;//DATA_AUDIT_STATUS	VARCHAR2(1)	N			数据审核状态
--%>
<body >
	<form name="Form1" method="post" action="saveEditVendor.action" id="Form1">
	<input type="hidden" id="vendorId" value="<s:property value='vendorInfo.vendorId'/>"/>
	<div class="windowtitle" style="background:#004C7E; height:45px; line-height:45px; text-align:center; color:#FFF; font-weight:bold; font-size:14px;">编辑供应商</div>
	<div class="windowtitle" style="background:#004C7E; height:30px; line-height:30px; text-align:left; color:#FFF; font-size:12px; margin-top: 10px;">供应商基本信息</div>
	<table id="tbl_context" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0" >
	<tr >
		<td width="15%" align="right">供应商中文名称:&nbsp;&nbsp;&nbsp;</td>
		<td width="35%">
			<input type="text" class="tbl_query_text" id="vendorCName" name="vendorCName" maxlength="100" value="<s:property value='vendorInfo.vendorCName'/>"/>&nbsp; 
			<span class="spanstar">*</span>
		</td>
		<td width="15%" align="right">供应商英文名称:&nbsp;&nbsp;&nbsp;</td>
		<td width="35%">
			<input type="text" class="tbl_query_text" id="vendorEName" name="vendorEName" maxlength="50" value="<s:property value='vendorInfo.vendorEName'/>"/>
		</td>
	</tr>
	<tr >
		<td align="right">供应商纳税人识别号:&nbsp;&nbsp;&nbsp;</td>
		<td >
			<input type="text" class="tbl_query_text" id="vendorTaxNo" maxlength="18" name="vendorTaxNo" value="<s:property value='vendorInfo.vendorTaxNo'/>" readonly="readonly"/>&nbsp; 
			<input type="hidden" class="tbl_query_text" id="vendorTaxNo1" maxlength="18" name="vendorTaxNo1" value="<s:property value='vendorInfo.vendorTaxNo'/>" readonly="readonly"/>&nbsp; 
			<span class="spanstar">*</span>
		</td>
		<td align="right">供应商账号:&nbsp;&nbsp;&nbsp;</td>
		<td >
			<input type="text" class="tbl_query_text" id="vendorAccount" maxlength="20"  name="vendorAccount" value="<s:property value='vendorInfo.vendorAccount'/>"/>
		</td>
	</tr>
	<tr >
		<td align="right">供应商开户银行中文名称:&nbsp;&nbsp;&nbsp;</td>
		<td >
			<input type="text" class="tbl_query_text" id="vendorCBank" maxlength="50"name="vendorCBank" value="<s:property value='vendorInfo.vendorCBank'/>"/>
		</td>
		<td align="right">供应商开户银行英文名称:&nbsp;&nbsp;&nbsp;</td>
		<td >
			<input type="text" class="tbl_query_text" id="vendorEBank" maxlength="20"  name="vendorEBank" value="<s:property value='vendorInfo.vendorEBank'/>"/>
		</td>
	</tr>
	<tr >
		<td align="right">供应商联系人:&nbsp;&nbsp;&nbsp;</td>
		<td >
			<input type="text" class="tbl_query_text" id="vendorLinkman" maxlength="50"  name="vendorLinkman" value="<s:property value='vendorInfo.vendorLinkman'/>"/>
		</td>
		<td align="right">供应商电话:&nbsp;&nbsp;&nbsp;</td>
		<td>
			<input type="text" class="tbl_query_text" id="vendorPhone" maxlength="20"  name="vendorPhone" value="<s:property value='vendorInfo.vendorPhone'/>"/>
		</td>
	</tr>
	<tr >
		<td align="right">供应商邮箱地址:&nbsp;&nbsp;&nbsp;</td>
		<td >
			<input type="text" class="tbl_query_text" id="vendorEmail" maxlength="50" name="vendorEmail" value="<s:property value='vendorInfo.vendorEmail'/>"/>
		</td>
		<td align="right">供应商地址:&nbsp;&nbsp;&nbsp;</td>
		<td >
			<input type="text" class="tbl_query_text" id="vendorAddress" maxlength="50" name="vendorAddress" value="<s:property value='vendorInfo.vendorAddress'/>"/>
		</td>
	</tr>
	<tr >
		<td align="right">供应商类别:&nbsp;&nbsp;&nbsp;</td>
		<td colspan="3">
			<select id="taxpayerType" style="width:155px" name="">
				<option value="" <s:if test='vendorInfo.taxpayerType==""'>selected</s:if><s:else></s:else>>全部</option>
				<option value="S" <s:if test='vendorInfo.taxpayerType=="S"'>selected</s:if><s:else></s:else>>小规模纳税人</option>
				<option value="G" <s:if test='vendorInfo.taxpayerType=="G"'>selected</s:if><s:else></s:else>>一般纳税人</option>
				<option value="I" <s:if test='vendorInfo.taxpayerType=="I"'>selected</s:if><s:else></s:else>>个体纳税人</option>
				<option value="O" <s:if test='vendorInfo.taxpayerType=="O"'>selected</s:if><s:else></s:else>>其他</option>
			</select>
		</td>
	</tr>
	<tr>
	</tr>
	</table>
	<div class="windowtitle" style="background:#004C7E; height:30px; line-height:30px; text-align:left; color:#FFF; font-size:12px;">供应商快递信息</div>
	<table id="tbl_context" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0" >
	<tr >
		<td width="21%" align="right">收件人:&nbsp;&nbsp;&nbsp;</td>
		<td width="29%">
			<input type="text" class="tbl_query_text" id="addressee" name="addressee" maxlength="50" value="<s:property value='vendorInfo.addressee'/>"/>
		</td>
		<td width="21%" align="right">收件人电话:&nbsp;&nbsp;&nbsp;</td>
		<td width="29%">
			<input type="text" class="tbl_query_text" id="addresseePhone" name="addresseePhone" maxlength="20" value="<s:property value='vendorInfo.addresseePhone'/>"/>
		</td>
	</tr>
	<tr >
		<td align="right">收件人邮编:&nbsp;&nbsp;&nbsp;</td>
		<td >
			<input type="text" class="tbl_query_text" id="addresseeZipcode" name="addresseeZipcode" maxlength="20" value="<s:property value='vendorInfo.addresseeZipcode'/>"/>
		</td>
		<td align="right">收件地址:&nbsp;&nbsp;&nbsp;</td>
		<td >
			<input type="text" class="tbl_query_text" id="addresseeAddress"  name="addresseeAddress" maxlength="50" value="<s:property value='vendorInfo.addresseeAddress'/>"/>
		</td>
	</tr>
	<tr >
		<td align="right">详细收件地址:&nbsp;&nbsp;&nbsp;</td>
		<td colspan="3">
			<input type="text" class="tbl_query_text" id="addresseeAddressdetail" name="addresseeAddressdetail" maxlength="100" value="<s:property value='vendorInfo.addresseeAddressdetail'/>"/>
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