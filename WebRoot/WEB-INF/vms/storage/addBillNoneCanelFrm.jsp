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
<link href="<%=webapp%>/page/subWindow.css" type="text/css" rel="stylesheet">
<link href="<c:out value="${sysTheme}"/>/css/subWindow.css" type="text/css" rel="stylesheet">
<title>新增作废发票信息和红冲发票</title>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script type="text/javascript" src="<%=bopTheme%>/js/validator.js"></script>
<!-- MessageBox -->
<script src="<%=bopTheme%>/js/MessageBox/js/messageBox.js" type="text/javascript"></script>
<link href="<%=bopTheme%>/js/MessageBox/css/messageBox.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript">
//标识页面是否已提交
var subed = false;
function checkForm(){

 //  验证是否重复提交
    if (subed == true){
       alert("信息正在发送给服务器，请不要重复提交信息！");
       return false;
    }
	
    subed=true;
    return true;
}

function onBlurs() {
	var invoiceCode = $("#invoiceCode").val();
	var invoiceNo = $("#invoiceNo").val();
	if (invoiceCode == "" || invoiceCode == null) {
		return alert("发票代码不可为空");
	} else if (invoiceNo == "" || invoiceNo == null) {

		return alert("发票号码不可为空");
	}
	$.ajax({
		url : 'selectLianDong.action',
		type : 'POST',
		async : false,
		data : {
			invoiceCode : invoiceCode,
			invoiceNo : invoiceNo
		},
		dataType : 'text',
		error : function() {
			return false;
		},
		success : function(result) {
			result = eval(result);
			if (result[0] == "0") {
				$("#invoiceTypes").val("增值税专用发票");
				$("#invoiceType").val(result[0]);
			} else if (result[0] == "1") {
				$("#invoiceTypes").val("增值税普通发票");
				$("#invoiceType").val(result[0]);
			} else if (result[0] == "" || result[0] == null) {
				$("#invoiceTypes").val("");
				$("#invoiceType").val(result[0]);
				return alert("未找到此发票信息,请重新输入");
			}
			if (result[1] != '') {
				alert("该发票已作废");
				return false;
			}
			
		}
	});
 }
//发票作废、空白发票作废、发票红冲	
function submitOpert(opertType) {
	var invoiceCode = $("#invoiceCode").val();
	var invoiceType = $("#invoiceType").val();
	var invoiceTypes = $("#invoiceTypes").val();
	var invoiceNo = $("#invoiceNo").val();
	var opert = opertType;
	if (invoiceType == "" || invoiceType == null && invoiceTypes == ""
			|| invoiceTypes == null) {
		return alert("发票类型不可为空!");
	}
	if (!getInvoiceStatusInfo(invoiceCode, invoiceNo,opertType)) {
			return false;
		} 
	$.ajax({
		url : "updateBillNoneCancel.action",
		type : 'POST',
		async : false,
		data : {
			invoiceCode : invoiceCode,
			invoiceNo : invoiceNo,
			invoiceType : invoiceType,
			invalidReason : $("#invalidReason").val(),
			opertType : opert
		},
		error : function() {
			return false;
		},
		success : function(result) {
			if (result == '') {
				alert("操作成功!");
				window.dialogArguments.saveVendorSuccess();
				window.close();
				CloseWindow();
			} else {
				alert("操作失败");
			}
		}

	});
}
	//发票代码,发票号码联动发票类型
 

 	function getInvoiceStatusInfo(invoiceCode, invoiceNo,type) {
		if (invoiceCode == "" || invoiceCode == null) {
			return alert("发票代码不可为空");
		} else if (invoiceNo == "" || invoiceNo == null) {
			return alert("发票号码不可为空");
		}
		  var returnData=true;
		  var opertType=type;
		$.ajax({
			url : 'selectLianDong.action',
			type : 'POST',
			async : false,
			data : {
				invoiceCode : invoiceCode,
				invoiceNo : invoiceNo
			},
			dataType : 'text',
			error : function() {
				return false;
			},
			success : function(result) {
				result = eval(result);             
				if(opertType=="cancel"){
					if (result[3] != '0') {
						alert("该发票已使用，无法进行空白发票作废！");
						returnData = false;						
						}
					}
				if(opertType=="redHush"){
					if (result[3] != '1') {
						alert("不是已开具发票，不可以红冲！");
						returnData = false;						
						}
					}
				if(opertType=="cancelnvoice"){
					if (result[3] != '1') {
						alert("不是已开具发票，不可以进行发票作废！");
						returnData = false;						
						}				
					}				
			}
		});
		return returnData;

	}
</script>
</head>
<body scroll="no" style="overflow:hidden;">
<div class="showBoxDiv">

<form id="frm" action="<c:out value='${webapp}'/>/savePaperInvoice.action" method="post">
<div id="editsubpanel" class="editsubpanel">
<div style="overflow: auto; width: 100%;" >  
	<input type="hidden" name="operType" value="<s:property value="operType" />" />
	<input type="hidden" id="paperInvoiceStockId" name="paperInvoiceStockId" value="<s:property value="paperinvoicestock.paperInvoiceStockId" />" /> 

<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
	<tr>
		<th colspan="9">新增发票处理信息</th>
	</tr>
	<tr>
		<td style="text-align: right;">发票代码：</td>
		<td colspan="2"><input id="invoiceCode" name="invoiceCode" class="tbl_query_text" type="text" maxlength="10"/></td>
		<td style="text-align: right;">发票号码：</td>
		<td colspan="2">
		<input id="invoiceNo" name="invoiceNo" class="tbl_query_text" 
		type="text" maxlength="8" onblur="onBlurs();"/></td>
		<td style="text-align: right;">发票类型：</td>
		<td colspan="2">
			<input readonly="readonly" id="invoiceTypes" name="invoiceTypes" 
			class="tbl_query_text" type="text" disabled="disabled">
			<input readonly="readonly" id="invoiceType" name="invoiceType"
			 class="tbl_query_text" type="hidden">
		</td>
	</tr>
	<tr>
		<td style="text-align: right;top: 20px;">操作原因：</td>
		<td colspan="8"></td>
	</tr>
	<tr>
	<td></td>
	<td rowspan="4" colspan="8">
		<textarea name="invalidReason" id="invalidReason" cols="60" rows="7" size="184"></textarea>
	</td>
	</tr>
</table>
</div>
<div id="ctrlbutton" class="ctrlbutton" style="border:0px">
	<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="submitOpert('cancel');" name="cancel" value="空白作废" id="cancel"/>
	<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="submitOpert('cancelnvoice');" name="cancelnvoice" value="作废发票" id="cancelnvoice"/>
	<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="submitOpert('redHush');" name="redHush" value="红冲" id="redHush"/>	
	<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="CloseWindow();" name="BtnReturn" value="关闭" id="BtnReturn"/>
</div>
</div>
</form>
</div>
</body>

</html>