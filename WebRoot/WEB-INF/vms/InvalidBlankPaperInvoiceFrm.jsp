<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html> 
<head>
<base target="_self">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../page/include.jsp"%>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<title>作废空白发票</title>
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
<link href="<%=webapp%>/page/subWindow.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=bopTheme%>/js/validator.js"></script>
<!-- MessageBox -->
<script src="<%=bopTheme%>/js/MessageBox/js/messageBox.js" type="text/javascript"></script>
<link href="<%=bopTheme%>/js/MessageBox/css/messageBox.css" rel="stylesheet" type="text/css" />

<script language="javascript" type="text/javascript">
//标识页面是否已提交
var subed = false;
function checkForm(){
    //发票代码是否为空
    if(fucCheckNull(document.getElementById("invoiceCode"),"请输入发票代码")==false){
         return false;
    }else if(fucIsInteger(document.getElementById("invoiceCode"),"发票代码应该为整数")==false){
	   return false;
	}else if(fucInvoiceCheckLength(document.getElementById("invoiceCode"),"10","发票代码长度必须是10位")==false){
		return false;
	}
    
    //发票号码是否为空
    if(fucCheckNull(document.getElementById("invoiceNo"),"请输入发票号码")==false){
         return false;
    }else if(fucIsInteger(document.getElementById("invoiceNo"),"发票号码应该为整数")==false){
	   return false;
	}else if(fucInvoiceCheckLength(document.getElementById("invoiceNo"),"8","发票号码长度必须是8位")==false){
		return false;
	}
    
    return true;
}

function chkPaper(invoiceCode, invoiceNo){
	var vv = "0";
	$.ajax({url: 'chkPaperInvoiceUseDetailCnt.action',
	type: 'POST',
	async:false,
	data:{invoiceCode:invoiceCode,invoiceNo:invoiceNo},
	dataType: 'html',
	timeout: 1000,
	error: function(){return false;},
	success: function(result){
		vv = result;
	}
	});
	if (vv == "0"){
		return false;
	}
	return true;
}

function updPaper(invoiceCode, invoiceNo, invalidReason){
	var vv = "";
	$.ajax({url: 'updInvalidBlankPaperInvoice.action',
	type: 'POST',
	async:false,
	data:{invoiceCode:invoiceCode,invoiceNo:invoiceNo,invalidReason:invalidReason},
	dataType: 'html',
	timeout: 1000,
	error: function(){return false;},
	success: function(result){
		vv = result;
	}
	});
	if (vv == "ng"){
		return false;
	}
	return true;
}

function submitForm(){
    //验证是否重复提交
    if (subed == true){
       alert("信息正在发送给服务器，请不要重复提交信息！");
       return false;
    }
    subed = true;
	// 入力check
	if(checkForm() == false){
	    subed = false;
		return false;
	}
	
	// 发票的存在性check
	if(chkPaper(document.getElementById("invoiceCode").value, document.getElementById("invoiceNo").value) == false){
		var obj = document.getElementById("invoiceCode")
		var m = new MessageBox(obj);
		m.Show("请确认输入的发票代码和发票号码是否正确");	
		obj.focus();
	    subed = false;
		return false;
	}
	
	// 空白发票作废页面 纸质发票使用明细表更新
	if(updPaper(document.getElementById("invoiceCode").value, document.getElementById("invoiceNo").value, document.getElementById("invalidReason").value) == false){
		var obj = document.getElementById("invoiceCode")
		var m = new MessageBox(obj);
		m.Show("空白发票作废失败");	
		obj.focus();
	    subed = false;
		return false;
	}
    CloseWindow();
}
function fucInvoiceCheckLength(obj,len,strAlertMsg)
{
	strTemp=obj.value;
	if(strTemp.length!=len)
	{
	  //超出了约束的最大字符长度
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
</head>
<body scroll="no" style="overflow:hidden;">
<div class="showBoxDiv">

<form id="frm" action="<c:out value='${webapp}'/>/savePaperInvoice.action" method="post">
<div id="editpanel">
<div id="editsubpanel" class="editsubpanel">
<div style="overflow: auto; width: 100%;" >  
<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
	<tr>
		<th colspan="4">作废空白发票</th>
	</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">发票代码</td>
		<td width="35%"><input id="invoiceCode" name="invoiceCode" type="text" class="tbl_query_text" maxlength="10" value="" style="ime-mode: disabled" />
			<span class="spanstar">*</span></td>
		<td width="15%" style="text-align: right" class="listbar">发票号码</td>
		<td><input id="invoiceNo" name="invoiceNo" type="text" class="tbl_query_text" maxlength="8" value="" style="ime-mode: disabled" /><span class="spanstar">*</span></td>
	</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">作废原因</td>
		<td width="35%" colspan=3><textarea id="invalidReason" name="invalidReason" rows="3" cols="20"></textarea></td>
	</tr>
</table>
</div>
</div>
<div id="ctrlbutton" class="ctrlbutton" style="border:0px">
	<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="return submitForm();" name="BtnSave" value="作废" id="BtnSave"/>
	<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="CloseWindow();" name="BtnReturn" value="关闭" id="BtnReturn"/>
</div>

</form>
</div>
</body>

</html>