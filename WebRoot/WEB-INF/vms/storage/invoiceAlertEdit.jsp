<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html> 
<head>
<base target="_self">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../../page/include.jsp"%>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
<!-- MessageBox -->
<script src="<%=bopTheme%>/js/MessageBox/js/messageBox.js" type="text/javascript"></script>
<link href="<%=bopTheme%>/js/MessageBox/css/messageBox.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="<%=bopTheme%>/js/validator.js"></script>
<link href="<%=webapp%>/page/subWindow.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<base target="_self">
<title>发票库存预警重置</title>

<script language="javascript" type="text/javascript">
//标识页面是否已提交
var subed = false;
function checkForm(){
    //验证是否重复提交
    if (subed == true){
       alert("信息正在发送给服务器，请不要重复提交信息！");
       return false;
    }
    //领用张数
    if(fucCheckNull(document.getElementById("alert_num"),"请输入预警值")==false){
         return false;
    }else if(fucIsInteger(document.getElementById("alert_num"),"预警值应该为整数")==false){
	   return false;
	}else{
		//校验发票代码是否存在
		
	}
    subed=true;
    return true;
}

function submitForm(){
	var alert_num=document.getElementById("alert_num").value;
	if(alert_num==null||alert_num==''||alert_num<0)
	{
		alert("预警值不能为负数！");
	}
	else
		{
			if(true == checkForm()){
				document.getElementById("doRestForm").action = '<c:out value='${webapp}'/>/saveInstAlert.action';
				document.getElementById("doRestForm").submit();
			}
		}
	}
	</script>
</head>
<body>
<div class="showBoxDiv">

<form id="doRestForm" action="<c:out value='${webapp}'/>/saveInstAlert.action" method="post">
<div id="editpanel">
<div id="editsubpanel" class="editsubpanel">
<div style="overflow: auto; width: 100%;" >  
<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
	<input type="hidden" name="inst_id"  value="<s:property value="inst_id" />"/>
	<input type="hidden" name="invoice_type"  value="<s:property value="invoice_type" />"/>
	<tr>
		<th colspan="4">发票库存预警重置</th>
	</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">机构名称:</td>
		<td width="35%">
			<s:property value="instInvoiceAlert.instName" />
		</td>
	</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">发票类型:</td>
		<td width="35%">
			 <s:if test='invoice_type=="0"'>增值税专用发票</s:if><s:elseif test='invoice_type=="1"'>增值税普通发票</s:elseif><s:else></s:else>
		</td>
	</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">预警值:</td>
		<td width="35%">
			<input type="text" name="alert_num"  id="alert_num"  value="<s:property value="instInvoiceAlert.alertNum" />"/><span class="spanstar">*</span>
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