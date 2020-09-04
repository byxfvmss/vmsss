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
<title>纸质发票领用</title>

<script language="javascript" type="text/javascript">
//标识页面是否已提交
var subed = false;
function checkForm(){
    //验证是否重复提交
    if (subed == true){
       alert("信息正在发送给服务器，请不要重复提交信息！");
       return false;
    }
    //机构是否为空
    if(fucCheckNull(document.getElementById("receiveInstId"),"请输入领用部门")==false){
       return false;
    }
    //领取人员是否为空
    if(fucCheckNull(document.getElementById("receiveUserId"),"请输入领用人")==false){
       return false;
    }
    //领用张数
    if(fucCheckNull(document.getElementById("receive_num"),"请输入领用张数")==false){
         return false;
    }else if(fucIsInteger(document.getElementById("receive_num"),"领用张数应该为整数")==false){
	   return false;
    }else if(fucIntegerValue(document.getElementById("receive_num"),"领用张数必须大于0")==false){
	   return false;
	}else if(fucInvoiceCheckReceiveNum(document.getElementById("receive_num"),<s:property value="paperInvoiceDistribute.distributeNum-paperInvoiceDistribute.hasReceiveNum" />,"领用张数超出数目")==false){
		return false;
	}else{
		//校验发票代码是否存在
		
	}
    subed=true;
    return true;
}

function submitForm(){
	if(true == checkForm()){
// 		$action= '<c:out value='${webapp}'/>/doReceiveDistribute.action';
// 		$post_data=$("#doReceiveDistributeForm").serialize();
// 		ajax_post($action,$post_data,function(json){
// 			$json_obj=eval('('+json+')');
// 			if($json_obj.msg=='ok'){
// 				try{
// 					CloseWindow(true);
// 				}catch(e){
// 				 	window.close();
// 				}
// 			}else{
// 				subed=false;
// 				alert("领用发票失败，请重新尝试！");
// 				window.location.reload();
// 			}
// 		});
		document.getElementById("doReceiveDistributeForm").action = '<c:out value='${webapp}'/>/doReceiveDistribute.action';
		document.getElementById("doReceiveDistributeForm").submit();
	}
}
function fucInvoiceCheckReceiveNum(obj,maxNum,strAlertMsg)
{
	num=parseInt(obj.value);
	if(num>maxNum)
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

function fucIntegerValue(obj,strAlertMsg){
	val=parseInt(obj.value);
	if(parseInt(val) <= 0)	{
	  //超出了约束的最大字符长度
	   var m = new MessageBox(obj);
	   m.Show(strAlertMsg);	
	   obj.focus();
	   return false;
	}	else	{
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
<body scroll="no" style="overflow:hidden;">
<div class="showBoxDiv">

<form id="doReceiveDistributeForm" action="<c:out value='${webapp}'/>/savePaperInvoice.action" method="post">
<div id="editpanel">
<div id="editsubpanel" class="editsubpanel">
<div style="overflow: auto; width: 100%;" >  
<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
	<input type="hidden" name="paper_invoice_distribute_id"  value="<s:property value="paperInvoiceDistribute.paperInvoiceDistributeId" />"/>
	<input type="hidden" name="distributeNum"  value="<s:property value="paperInvoiceDistribute.distributeNum" />"/>
	<input type="hidden" name="hasReceiveNum"  value="<s:property value="paperInvoiceDistribute.hasReceiveNum"/>" />
	
	<tr>
		<th colspan="4">纸质发票领用</th>
	</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">发票代码:</td>
		<td width="35%">
			<s:property value="paperInvoiceDistribute.invoiceCode" />
		</td>
		<td width="15%" style="text-align: right" class="listbar">未领用张数</td>
		<td>&nbsp;<s:property value="paperInvoiceDistribute.unReceiveNum" /></td>
	</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">发票起始号码:</td>
		<td width="35%">
			<s:property value="paperInvoiceDistribute.invoiceBeginNo" />
		</td>
		<td width="15%" style="text-align: right" class="listbar">发票终止号码:</td>
		<td>
			<s:property value="paperInvoiceDistribute.invoiceEndNo" />
    	</td>
	</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">领用张数:</td>
		<td width="35%">
			<input type="text" class="tbl_query_text" name="paperInvoiceRbHistory.receive_num"  id="receive_num"  value="0"/><span class="spanstar">*</span>
		</td>
		<td width="15%" style="text-align: right" class="listbar">&nbsp;</td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">领用部门:</td>
		<td width="35%">
			<input id="receiveInstId" name="paperInvoiceRbHistory.receiveInstId" type="text" class="tbl_query_text" value="<s:property value="paperInvoiceRbHistory.receiveInstId" />" /><span class="spanstar">*</span>
		</td>
		<td width="15%" style="text-align: right" class="listbar">领用人:</td>
		<td><input id="receiveUserId" name="paperInvoiceRbHistory.receiveUserId"
			type="text" class="tbl_query_text" value="<s:property value="paperInvoiceRbHistory.receiveUserId" />" /><span class="spanstar">*</span></td>
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
<script>
	$(function(){
		<%
			String retry=request.getParameter("retry");
			if(org.apache.commons.lang.StringUtils.isNotEmpty(retry)){ 
		%>
			alert("领取失败，请您重新再试！");
		<%
			}
		%>
	});
</script>

</html>