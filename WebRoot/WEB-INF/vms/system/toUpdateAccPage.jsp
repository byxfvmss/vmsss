<%-- <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> --%>
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.util.PaginationList"
	import="com.cjit.vms.system.model.AccTitle"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires",0);
%>
<%@include file="../../../page/modalPage.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
<%@ include file="../../../page/include.jsp"%>

<script language="javascript" type="text/javascript" src="<%=bopTheme %>/js/main.js"></script>
<link href="<c:out value="${bopTheme2}"/>/css/main.css" rel="stylesheet" type="text/css" />
<script language="javascript" type="text/javascript" src="<%=bopTheme %>/js/window.js"></script>
<script charset="gb2312"  language="javascript" type="text/javascript" src="<%=bopTheme %>/js/validator.js"></script>

<!-- MessageBox -->
<script src="<c:out value="${bopTheme}"/>/js/MessageBox/js/messageBox.js" type="text/javascript"></script>
<link href="<c:out value="${bopTheme}"/>/js/MessageBox/css/messageBox.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="js/My97DatePicker/WdatePicker.js"></script>
	<title>修改科目</title>
	<link type="text/css" href="<c:out value="${bopTheme2}"/>/css/subWindow.css" rel="stylesheet">
	
	<%--
	<link type="text/css" href="<c:out value="${sysTheme}"/>/css/subWindow.css" rel="stylesheet">
	--%><script language="javascript" type="text/javascript">
		//标识页面是否已提交
		var subed = false;
			function fucCheckTMail(obj,strAlertMsg)
 {
		strAddress=obj.value;
		strAddress=strAddress.replace(/^(\s)*|(\s)*$/g,"");//去掉字符串两边的空格
				
		//匹配规则：
		//只允许以字母开头，用a-z,A-Z,0-9以及下划线组成的email名
		//email后面的域名只允许字母或下划线开头,至少一个.,以字母或下划线结束
		//var newPar=/^[a-zA-Z](\w*)@\w+\.(\w|.)*\w+$/
	 var newPar = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;

		if(strAddress.length>0 && newPar.test(strAddress)==false)
		{
		   
		   return false;	
		 }
		 else
		 {
		    return true;
		 }
 }
		function fucCheckTPhone(obj,strAlertMsg)
 			{
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
				//if(strAddress.length>0 && tflag==false)
				  if(false)
				{
				  
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
				  
				   return false;	
				 }
				 else
				 {
				    return true;
				 }
		 }
		function fucCheckAccTitleCode(obj,strAlertMsg)
 			{
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
				}
		}
	
		function funCheckTname(){
			if(fucCheckCName(document.getElementById("accTitleName"),"")==false) {	
				document.getElementById("accName").style.display='inline';
				document.getElementById("accName1").style.display='none';
				return false;
			}else{
				document.getElementById("accName").style.display='none';
				document.getElementById("accName1").style.display='none';
				return true;
			}
		}
		function funAccTitleCode(){
			if(fucCheckAccTitleCode(document.getElementById("accTitleCode"),"")==false){
				document.getElementById("accCode").style.display='inline';
				document.getElementById("accCode1").style.display='none';
				return false;
			}else{
				document.getElementById("accCode").style.display='none';
				document.getElementById("accCode1").style.display='none';
				return true;
				
			}
			
		}
		
		
		function findOutSubmit() {
			if(funAccTitleCode()&&funCheckTname()){
			if(fucChecTNull(document.getElementById("accTitleCode"),"")==false) {
				document.getElementById("accCode1").style.display='inline';
				return false;
			}else{
				document.getElementById("accCode1").style.display='none';
			}
			
			if(fucChecTNull(document.getElementById("accTitleName"),"")==false) {		
				document.getElementById("accName1").style.display='inline';
				return false;
			}else{
				document.getElementById("accName1").style.display='none';
			}
			
			
			var form = document.getElementById("formAccTitle");
			form.action='updateAccTitle.action';
			form.submit();
			document.getElementById("btn_save").disabled=true;
		}else{
			return false;
		}
		}
	</script>
</head>
<body scroll="no" style="overflow:hidden;">
<div class="showBoxDiv">
<form name="formAccTitle" id="formAccTitle" action="updateAccTitle.action" method="post">
<div id="editsubpanel" class="editsubpanel">
<div style="overflow: auto; width: 100%;" >    
<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0" >
	<tr class="header">
		<th colspan="4" >
			修改科目
		</th>
	</tr>
	<tr>
		<td width="25%" align="right" class="listbar">科目编码:</td>
		<td width="60%">
				<input id="accTitleCode" name="accTitleCode" type="text" class="tbl_query_text" onblur="funAccTitleCode()" disabled="true" value="<s:property value='accTitle.accTitleCode' />" />&nbsp; 
				<span class="spanstar">*</span>
				<label id="accCode" style="display:none;color:#FF0000;font-size:14">请输入字母或数字</label>
				<label id="accCode1" style="display:none;color:#FF0000;font-size:14">请输入</label>
		</td>
	</tr><tr>
		<td align="right" class="listbar" >科目名称:</td>
		<td>
			<input id="accTitleName" name="accTitleName" type="text" class="tbl_query_text" maxlength="100" onChange="funCheckTname()" value="<s:property value='accTitle.accTitleName' />"/>&nbsp; 
			<span class="spanstar">*</span>
			<label id="accName" style="display:none;color:#FF0000;font-size:14">请输入中文</label>
			<label id="accName1" style="display:none;color:#FF0000;font-size:14">请输入</label>
		</td>
		<td style="display: none"><input id = 'accTitleId' name='accTitleId' value='<s:property value='accTitle.accTitleId'/>'/></td>
	</tr>

</table>
</div>
</div>
	<div id="ctrlbutton" class="ctrlbutton" style="border:0px">		
	<input type="button" id="btn_save" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 
	onclick="findOutSubmit()" name="BtnSave" value="保存" id="BtnSave"/>
	<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 
	onclick="window.close()" name="BtnReturn" value="关闭" id="BtnReturn"/>	
	</div>
</form>
</div>
</body>
	</html>