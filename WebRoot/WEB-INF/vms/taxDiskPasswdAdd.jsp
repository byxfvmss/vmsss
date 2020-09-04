<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html> 
<head>
<base target="_self">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../page/include.jsp"%>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
<!-- MessageBox -->
<script src="<%=bopTheme%>/js/MessageBox/js/messageBox.js" type="text/javascript"></script>
<link href="<%=bopTheme%>/js/MessageBox/css/messageBox.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="<%=bopTheme%>/js/validator.js"></script>
<link href="<%=webapp%>/page/subWindow.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/search.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
<base target="_self">
<title>税控盘密码管理-添加</title>

<script language="javascript" type="text/javascript">
//标识页面是否已提交
var subed = false;
function checkForm(){
    //验证是否重复提交
    if (subed == true){
       alert("信息正在发送给服务器，请不要重复提交信息！");
       return false;
    }
    if(fucCheckNull(document.getElementById("taxpayerNo"),"纳税人识别号不能为空")==false){
       return false;
    }
    if(fucCheckNull(document.getElementById("taxpayerNam"),"纳税人名称不能为空")==false){
       return false;
    }
    
    if(fucCheckNull(document.getElementById("taxDiskNo"),"税控盘编号不能为空")==false){
       return false;
    }
    
    if(fucCheckLength(document.getElementById("taxDiskNo"),"12","税控盘编号长度要小于等于12位")==false){
       return false;
    }
    if(fucCheckNull(document.getElementById("taxDiskPsw"),"税控盘口令不能为空")==false){
       return false;
    }
    
    if(fucCheckLength(document.getElementById("taxDiskPsw"),"8","税控盘口令长度要小于等于8位")==false){
       return false;
    }
    
    if(fucCheckNull(document.getElementById("taxCertPsw"),"证书口令不能为空")==false){
       return false;
    }
    
    if(fucCheckLength(document.getElementById("taxCertPsw"),"8","证书口令长度要小于等于8位")==false){
       return false;
    }
    subed=true;
    return true;
}

function submitForm(){
	if(true == checkForm()){
		$action= '<c:out value='${webapp}'/>/checkTaxDiskInfo.action';
		$post_data=$("#doAddTaxDiskForm").serialize();
		ajax_post($action,$post_data,function(json){
			$json_obj=eval('('+json+')');
			if($json_obj.msg=='ok'){
				document.getElementById("doAddTaxDiskForm").action = '<c:out value='${webapp}'/>/saveTaxDiskPasswd.action';
				document.getElementById("doAddTaxDiskForm").submit();
			}else{
				subed=false;
				alert("您添加的数据系统中已经存在!");
				return;
			}
		});
		
	}
}
function fucCheckLengthEqv(obj,len,strAlertMsg){
	strTemp=obj.value;
	if(strTemp.length!=len)	{
//	  字符长度不正确
	   var m = new MessageBox(obj);
	   m.Show(strAlertMsg);	
	   obj.focus();
	   return false;
	}else{
	   return true;
	}	
}
function ajax_post(the_url,the_param,succ_callback){
	$.ajax({
		type:'POST',
		url:the_url,
		data:the_param,
		success:succ_callback,
		error:function(html){
			alert("提交数据失败，代码:" +html.status+ "，请稍候再试");
		}
	});
}
</script>
</head>
<body>
<div class="showBoxDiv">

<form id="doAddTaxDiskForm" action="<c:out value='${webapp}'/>/saveTaxDiskPasswd.action" method="post">
<div id="editpanel">
<div id="editsubpanel" class="editsubpanel">
<div style="overflow: auto; width: 100%;" >  
<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
	<tr>
		<th colspan="4">税控盘密码管理-添加</th>
	</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">纳税人识别号:</td>
		<td width="35%">
			<s:if test="taxperList != null && taxperList.size > 0">
			<s:select id="taxpayerNo" name="taxDiskInfo.taxpayerNo" list="taxperList" listKey='taxperNumber' listValue='taxperNumber' headerKey="" headerValue="所有" onChange="getAjaxTaxperName(this.value, document.getElementById('taxpayerNam'))"/>
			</s:if>
			<s:if test="taxperList == null || taxperList.size == 0">
			<select  id="taxpayerNo" name="taxDiskInfo.taxPerNumber" class="readOnlyText">
			<option value="">请分配机构权限</option>
			</select>
			</s:if>
		</td>
		<td width="15%" style="text-align: right" class="listbar">纳税人名称:</td>
		<td>
			<input id="taxpayerNam" name="taxDiskInfo.taxpayerNam" type="text"  value="<s:property value='taxDiskInfo.taxpayerNam' />"  />
		</td>
	</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">税控盘编号:</td>
		<td width="35%">
			<input type="text" name="taxDiskInfo.taxDiskNo"  id="taxDiskNo"  value="<s:property value='taxDiskInfo.taxDiskNo'/>" />
		</td>
		<td width="15%" style="text-align: right" class="listbar">税控盘口令:</td>
		<td>
			<input type="text" name="taxDiskInfo.taxDiskPsw"  id="taxDiskPsw"  value="<s:property value='taxDiskInfo.taxDiskPsw'/>" maxlength="8"/>
    	</td>
	</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">证书口令:</td>
		<td width="35%">
			<input type="text" name="taxDiskInfo.taxCertPsw"  id="taxCertPsw"  value="<s:property value='taxDiskInfo.taxCertPsw'/>"  maxlength="8"/>
		</td>
		<td width="15%" style="text-align: right" class="listbar">&nbsp;</td>
		<td>
		&nbsp;
		</td>
	</tr>
</table>
</div>
</div>
<div id="ctrlbutton" class="ctrlbutton" style="border:0px;width:100%;padding-right:0px;margin-right:10px;">
	<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="submitForm();" name="BtnSave" value="保存" id="BtnSave"/>
	<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="CloseWindow();" name="BtnReturn" value="关闭" id="BtnReturn"/>
</div>

</form>
</div>
</body>
</html>