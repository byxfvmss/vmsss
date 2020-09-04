<%@ page language="java" import="java.util.*" import="java.math.BigDecimal" import="com.cjit.common.constant.ScopeConstants" import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.util.PaginationList" import="com.cjit.vms.input.model.InputInvoiceNew" import="com.cjit.vms.trans.util.DataUtil" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%>
<%
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<%@ include file="/page/include.jsp"%>

<%@page import="com.opensymphony.util.BeanUtils"%>

<html>
<base target="_self">
<meta http-equiv='pragma' content='no-cache'>
<meta http-equiv='cache-control' content='no-cache'>  
<META    HTTP-EQUIV="Expires"    CONTENT="0">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/calendar_notime.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/ylb.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/editview.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/XmlHttp.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/vms.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
<link href="<%=bopTheme2%>/css/editView.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script type="text/javascript" src="<%=bopTheme%>/js/validator.js"></script>
<link href="<c:out value="${bopTheme2}"/>/css/main.css" rel="stylesheet" type="text/css" />
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/window.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script charset="gb2312" language="javascript" type="text/javascript" src="<%=bopTheme%>/js/validator.js"></script>
<!-- MessageBox -->
<script src="<%=bopTheme%>/js/MessageBox/js/messageBox.js" type="text/javascript"></script>
<link href="<%=bopTheme%>/js/MessageBox/css/messageBox.css" rel="stylesheet" type="text/css" />
<title>新增发票信息</title>

<style>

.showStar{
display:inline !important;
}
.hiddenStar{
display:none !important;
}

</style>
<script language="javascript" type="text/javascript"><!--


//标识页面是否已提交
var subed = false;

function findOutSubmit(){

	
	var TaxNo = document.getElementById("TaxNo").value;
	var len = TaxNo.length;
	if(len != 15 && len != 18){
		alert("纳税人识别号应为15或18位");
	       return false;
	}
	if(document.getElementById("industryType").value == "" || document.getElementById("industryType").value == null){
		alert("机构不能为空");
	       return false;
	}
	if(document.getElementById("billCode").value == "" || document.getElementById("billCode").value == null){
		alert("发票代码不能为空");
	       return false;
	}

	if(document.getElementById("billNo").value == "" || document.getElementById("billNo").value == null){
		alert("发票号码不能为空");
	       return false;
	}
	if(document.getElementById("amt").value == "" || document.getElementById("amt").value == null){
		alert("金额不能为空");
	       return false;
	}
	if(document.getElementById("amtRate").value == "" || document.getElementById("amtRate").value == null){
		alert("税率不能为空");
	       return false;
	}

	if(document.getElementById("amtTaxAmt").value == "" || document.getElementById("amtTaxAmt").value == null){
		alert("发票总额不能为空");
	       return false;
	}
	if(document.getElementById("billDate").value == "" || document.getElementById("billDate").value == null){
		alert("开票日期不能为空");
	       return false;
	}

	if(document.getElementById("directionId").value == "" || document.getElementById("directionId").value == null){
		alert("费用明细科目不能为空");
	       return false;
	}

	if(document.getElementById("trans_item").value == "" || document.getElementById("trans_item").value == null){
		alert("进项转出科目不能为空");
	       return false;
	}

	var amt = document.getElementById("amt").value;
	var taxAmtSum = document.getElementById("amtRate").value;

	var taxAmt = parseFloat(amt) * parseFloat(taxAmtSum) /100;

	taxAmt = Math.round(taxAmt*100)/100;
	

    if(document.getElementById("taxAmt").value != taxAmt){
        alert("税额计算不正确");
        return false;
     }

    var sumTaxAmt = parseFloat(amt) + parseFloat(taxAmt);
	
    if(document.getElementById("amtTaxAmt").value != sumTaxAmt){
    	alert("价税合计金额计算不正确");
        return false;
    }

var form = document.getElementById("formInvoice");
form.action = 'saveInvoiceNew.action';
form.submit();
//document.getElementById("btn_save").disabled = true;

}

function back(){
	submitAction(document.forms[0], "listInputInvoiceNew.action?fromFlag=menu");
}

</script>
</head>
<body>
	<form name="formInvoice" id="formInvoice" action="saveInvoiceNew.action" method="post">
		<div class="windowtitle" style="background:#004C7E; height:45px; line-height:45px; text-align:center; color:#FFF; font-weight:bold; font-size:14px;">新增票据</div>
		<div class="windowtitle" style="background:#004C7E; height:30px; line-height:30px; text-align:left; color:#FFF; font-size:12px; margin-top: 10px;">票据基本信息</div>
		<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
			<%--<tr class="header">
		<th colspan="4" >
			新增进项税信息
		</th>
	</tr>
	--%>
			<tr>
				<td align="right">机构名称</td>
							<td width="280">
									<s:select id="industryType"
                                          name="industryType" list="industryTypeList"
                                          headerKey="" headerValue="全部" listKey='code' listValue='name'
                                          cssClass="tbl_query_text"/>
						    </td>
				<td width="15%" align="right" class="listbar">用途:</td>
				<td width="35%">
					<select id="purposeCode" name="purposeCode">
						<option value="Y01" selected>应税</option>
						<option value="Y02">进项转出</option>
						<option value="Y03">视同销售</option>
					</select>
				</td>
			</tr>
			<tr>
				<td width="15%" align="right" class="listbar">发票代码:</td>
				<td width="35%">
					<input size="30" id="billCode" class="tbl_query_text" name="billCode" type="text" maxlength="20"/>
					&nbsp;
					<span style="color:red;" id="nm" >*</span>
				</td>
				<td width="15%" align="right" class="listbar">发票号码:</td>
				<td width="35%">
					<input size="30" id="billNo" class="tbl_query_text" name="billNo" type="text" maxlength="20"/>
					&nbsp;
					<span style="color:red;" id="nm" >*</span>
				</td>
			</tr>
			<tr>
				
				<td width="15%" align="right" class="listbar">供应商名称:</td>
				<td width="35%">
					<input size="30" id="vendorName" class="tbl_query_text" name="vendorName" type="text" maxlength="20"/>
					<span style="color:red;" id="nm" >*</span>
				</td>
				 <td width="15%" align="right" class="listbar">供应商纳税人识别号:</td>
				<td width="35%">
					<input size="30" id="TaxNo" class="tbl_query_text" name="TaxNo" type="text" maxlength="20"/>
					<span style="color:red;" id="nm" >*</span>
				</td>
			</tr>
			<tr>
			   
				<td width="15%" align="right" class="listbar">金额:</td>
				<td width="35%">
					<input size="40" id="amt" class="tbl_query_text" name="amt" type="text" maxlength="100" />
					&nbsp;
					<span style="color:red;" id="nm" >*</span>
				
				</td>
				<td width="15%" align="right" class="listbar">税率:</td>
				<td width="35%">
					<input size="30" id="amtRate" class="tbl_query_text" name="amtRate" type="text" maxlength="20" />
					&nbsp;
					<span style="color:red;" id="nm" >*</span>
				</td>
			</tr>
			<tr>
				
				
				<td width="15%" align="right" class="listbar">税额:</td>
				<td width="35%">
					<input size="30" id="taxAmt" class="tbl_query_text" name="taxAmt" type="text" maxlength="20" />
					&nbsp;
					<span style="color:red;" id="nm">*</span>
				</td>
				<td width="15%" align="right" class="listbar">发票总额:</td>
				<td width="35%">
					<input size="30" id="amtTaxAmt" class="tbl_query_text" name="amtTaxAmt" type="text" maxlength="20" />
					&nbsp;
					<span style="color:red;" id="nm">*</span>
				</td>
			</tr>
			<tr>
			 <td width="15%" align="right" >开票日期:</td>
				<td width="35%">
					<input id="billDate" name="billDate" type="text"  value="<s:property value='inputInvoiceNew.billDate' />" class="tbl_query_time1"	 
						onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'billDate\')}'})" />
					&nbsp;<span style="color:red;" id="nm">*</span>
				</td>
			    <td width="15%" align="right" class="listbar">发票状态:</td>
				<td width="35%">
					<select id="dataStatus" name="dataStatus">
						<option value="0" selected>待认证</option>
						<option value="1">已认证</option>
					</select>
				</td>
				
			</tr>
			
			<tr>
			   
				 <td width="15%" align="right" >费用明细编码:</td>
				<td width="35%">
				    <input size="30" id="directionId" class="tbl_query_text" name="directionId" type="text" maxlength="20"/>
					&nbsp;<span style="color:red;" id="nm">*</span>
				</td>
				<td width="15%" align="right" >进项转出科目明细编码:</td>
				<td width="35%">
					<input size="30" id="trans_item" class="tbl_query_text" name="trans_item" type="text" maxlength="20"/>
					&nbsp;<span style="color:red;" id="nm">*</span>
				</td>
				
			</tr>
			<tr>
			<td width="15%" align="right" class="listbar">发票类型:</td>
				<td width="35%">
					<select id="billType" name="billType">
						<option value="0" >专票</option>
						<option value="1" >普票</option>
					</select>
				</td>
			</tr>
			<div id="anpBoud" align="Right" style="overflow:auto;width:100%;">
						<table width="100%" cellspacing="0" border="0">
							<tr>
								<td align="right">
									<s:component template="pagediv" />
								</td>
							</tr>
						</table>
					</div>
			
		</table>
		
		<div id="ctrlbutton" class="ctrlbutton" style="border:0px">
			<input type="button" id="btn_save" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="findOutSubmit()"
				name="BtnSave" value="保存" id="BtnSave"
			/>
			<input type="button" class="tbl_query_button" value="返回"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="btBack" id="btBack" onclick="back()" />
		</div>
	</form>

</body>
</html>