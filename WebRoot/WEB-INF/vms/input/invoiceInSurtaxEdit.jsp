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
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/editview.js"></script>
<base target="_self">
<title>进项税发票转出</title>

<script language="javascript" type="text/javascript">
//标识页面是否已提交
var subed = false;
function checkForm(){
    //验证是否重复提交
    if (subed == true){
       alert("信息正在发送给服务器，请不要重复提交信息！");
       return false;
    }
    if(fucCheckNull(document.getElementById("vatOutAmt"),"转出金额不能为空")==false){
       return false;
    }
    if(fucIsFloat(document.getElementById("vatOutAmt"),"请输入正确的转出金额")==false){
       return false;
    }
    if(fucCheckNull(document.getElementById("vatOutProportion"),"转出比例不能为空")==false){
       return false;
    }
    if(fucIsFloat(document.getElementById("vatOutProportion"),"请输入正确的转出比例")==false){
       return false;
    }
    var vatOutAmtV = document.getElementById("vatOutAmt").value;
    var taxAmtSumV = document.getElementById("taxAmtSum").value;
    if(parseFloat(vatOutAmtV) > parseFloat(taxAmtSumV)){
		var m = new MessageBox(document.getElementById("vatOutAmt"));
	   	m.Show("转出金额不能大于合计税额");
	   	obj.focus();	
	   	return false;	
    }
    subed=true;
    return true;
}

function submitForm(){
	if(true == checkForm()){
		document.getElementById("doEditParamInSurtaxForm").action = '<c:out value='${webapp}'/>/uptSaveInvoiceInSurtax.action';
		document.getElementById("doEditParamInSurtaxForm").submit();
	}
}
	</script>
</head>
<body style="background:#FFF;">
<form id="doEditParamInSurtaxForm" action="<c:out value='${webapp}'/>/uptSaveInvoiceInSurtax.action" method="post">
<input type="hidden" name="bill_id" value='<s:property value="bill_id"/>'/> 

<div class="windowtitle" style="background:#004C7E; height:45px; line-height:45px; text-align:center; color:#FFF; font-size:14px; font-weight:bold;">进项税发票转出</div>
<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
	<tr>
		<td width="15%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">转出金额:&nbsp;&nbsp;&nbsp;</td>
		<td width="35%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: left;">
		<input type="text" class="tbl_query_text" name="vatOutAmt"  id="vatOutAmt"  value="<s:property value='inputInvoiceInfo.vatOutAmt'/>" maxlength="10" onkeypress="checkkey(value);"/></td>
		<td width="15%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">转出比例:&nbsp;&nbsp;&nbsp;</td>
		<td width="35%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: left;">
		<input type="text" class="tbl_query_text" name="vatOutProportion"  id="vatOutProportion"  value="<s:property value='inputInvoiceInfo.vatOutProportion'/>" maxlength="10" onkeypress="checkkey(value);"/></td>
	</tr>
	<tr>
		<td width="15%"  style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">
			备注:&nbsp;&nbsp;&nbsp;
		</td>
		<td width="35%" colspan="2" style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: left;">
			<textarea rows="2" cols="20" name="remark"></textarea>
		</td>
		<td align="left" style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: left;">
			<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="submitForm();" name="BtnSave" value="保存" id="BtnSave"/>
			<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="CloseWindow();" name="BtnReturn" value="关闭" id="BtnReturn"/>
		</td>
	</tr>
</table>
<div class="windowtitle" style="background:#004C7E; height:30px; line-height:30px; text-align:left; color:#FFF; font-size:12px; ">基本信息</div>
<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
	<tr>
		<td width="15%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">发票代码:&nbsp;&nbsp;&nbsp;</td>
		<td width="35%" style="background-color:#F0F0F0;color:#727375;"><s:property value='inputInvoiceInfo.billCode'/>
			<input type="hidden" name="billCode"  id="billCode"  value="<s:property value='inputInvoiceInfo.billCode'/>" disabled/>
		</td>
		<td width="15%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">发票号码:&nbsp;&nbsp;&nbsp;</td>
		<td style="background-color:#F0F0F0;color:#727375;"><s:property value='inputInvoiceInfo.billNo'/>
		<input type="hidden" name="billNo"  id="billNo"  value="<s:property value='inputInvoiceInfo.billNo'/>" disabled/></td>
	</tr>
	<tr>
		<td width="15%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">开票日期:&nbsp;&nbsp;&nbsp;</td>
		<td style="background-color:#F0F0F0;color:#727375;"><s:property value='inputInvoiceInfo.billDate'/>
			<input type="hidden" name="billDate"  id="billDate"  value="<s:property value='inputInvoiceInfo.billDate'/>" disabled/>
		</td>
		<td width="15%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">价税合计金额:&nbsp;&nbsp;&nbsp;</td>
		<td style="background-color:#F0F0F0;color:#727375;"><s:property value='inputInvoiceInfo.sumAmt'/>
			<input type="hidden" name="sumAmt"  id="sumAmt"  value="<s:property value='inputInvoiceInfo.sumAmt'/>" disabled/>
		</td>
	</tr>
	<tr>
		<td width="15%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">供应商中文名称:&nbsp;&nbsp;&nbsp;</td>
		<td width="35%" style="background-color:#F0F0F0;color:#727375;"><s:property value='inputInvoiceInfo.vendorName'/>
			<input type="hidden" name="vendorName"  id="vendorName"  value="<s:property value='inputInvoiceInfo.vendorName'/>" disabled/>
		</td>
		<td width="15%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">供应商纳税人识别号:&nbsp;&nbsp;&nbsp;</td>
		<td style="background-color:#F0F0F0;color:#727375;"><s:property value='inputInvoiceInfo.vendorTaxno'/>
		<input type="hidden" name="vendorTaxno"  id="vendorTaxno"  value="<s:property value='inputInvoiceInfo.vendorTaxno'/>" disabled/>
		</td>
	</tr>
	<tr>
		<td width="15%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">供应商电话:&nbsp;&nbsp;&nbsp;</td>
		<td width="35%" style="background-color:#F0F0F0;color:#727375;"><s:property value='inputInvoiceInfo.vendorPhone'/>
			<input type="hidden" name="vendorPhone"  id="vendorPhone"  value="<s:property value='inputInvoiceInfo.vendorPhone'/>" disabled/>
		</td>
		<td width="15%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">供应商地址:&nbsp;&nbsp;&nbsp;</td>
		<td style="background-color:#F0F0F0;color:#727375;"><s:property value='inputInvoiceInfo.vendorAddress'/>
		<input type="hidden" name="vendorAddress"  id="vendorAddress"  value="<s:property value='inputInvoiceInfo.vendorAddress'/>" disabled/>
		</td>
	</tr>
	<tr>
		<td width="15%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">供应商银行账号:&nbsp;&nbsp;&nbsp;</td>
		<td width="35%" style="background-color:#F0F0F0;color:#727375;"><s:property value='inputInvoiceInfo.vendorBankandaccount'/>
			<input type="hidden" name="vendorBankandaccount"  id="vendorBankandaccount"  value="<s:property value='inputInvoiceInfo.vendorBankandaccount'/>" disabled/>
		</td>
		<td width="15%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">合计金额:&nbsp;&nbsp;&nbsp;</td>
		<td width="35%" style="background-color:#F0F0F0;color:#727375;"><s:property value="inputInvoiceInfo.amtSum" />
			<input id="amtSum" name="amtSum" type="hidden" disabled	value="<s:property value="inputInvoiceInfo.amtSum" />" />
		</td>
	</tr>
	<tr>
		<td width="10%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">收款人:&nbsp;&nbsp;&nbsp;</td>
		<td style="background-color:#F0F0F0;color:#727375;"><s:property value='inputInvoiceInfo.payee'/>
			<input type="hidden" name="payee"  id="payee"  value="<s:property value='inputInvoiceInfo.payee'/>" disabled/>
		</td>
		<td width="15%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">合计税额:&nbsp;&nbsp;&nbsp;</td>
		<td style="background-color:#F0F0F0;color:#727375;"><s:property value='inputInvoiceInfo.taxAmtSum'/>
		<input id="taxAmtSum" name="taxAmtSum"	type="hidden" value="<s:property value='inputInvoiceInfo.taxAmtSum'/>" disabled/></td>
	</tr>
	<tr>
		<td width="10%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">审核人:&nbsp;&nbsp;&nbsp;</td>
		<td style="background-color:#F0F0F0;color:#727375;"><s:property value='inputInvoiceInfo.reviewer'/>
		<input type="hidden" name="reviewer"  id="reviewer"  value="<s:property value='inputInvoiceInfo.reviewer'/>" disabled/></td>
		<td width="10%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">开票人:&nbsp;&nbsp;&nbsp;</td>
		<td style="background-color:#F0F0F0;color:#727375;"><s:property value='inputInvoiceInfo.drawer'/>
		<input type="hidden" name="drawer"  id="drawer"  value="<s:property value='inputInvoiceInfo.drawer'/>" disabled/></td>
	</tr>
</table>
<div class="boxline">商品数据列表</div>
<div>
	<iframe id="subTableFrame" scrolling="auto" src="listInputItem.action?billId=<s:property value='bill_id'/>" height="160px" width="100%" frameborder="0"></iframe>
</div>
<div class="boxline">票据交易数据</div>
<div style="border-bottom:#DDD solid 1px;">
	<iframe id="subTableFrame1" scrolling="auto" src="listInputTransItem.action?billId=<s:property value='bill_id'/>" height="160px" width="100%" frameborder="0"></iframe>
</div>
<!-- <div class="windowtitle" style="background:#004C7E; height:30px; line-height:30px; text-align:left; color:#FFF; font-size:12px; ">关联商品</div>
<table class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0" style="border-collapse: collapse; width: 100%;">
	<tr class="lessGrid head">
		<th width="25%" style="text-align: center">商品名称</th> 
		<th width="25%" style="text-align: center">规格型号</th>
		<th width="10%" style="text-align: center">商品数量</th>
		<th width="10%" style="text-align: center">商品单价</th>
		<th width="10%" style="text-align: center">金额</th> 
		<th width="10%" style="text-align: center">税率</th> 
		<th width="10%" style="text-align: center">税额</th> 
	</tr>
	<s:if test='lstInputInvoiceItem == null || lstInputInvoiceItem.size() == 0'>
		<tr><td colspan="7">该发票没有对应的交易信息！</td></tr>
	</s:if>
	<s:else>
		<s:iterator value="lstInputInvoiceItem" id="iList" status="stuts">
		<tr class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>" width="100%">
			<td width="25%" style="text-align: center"><s:property value="goodsName" /></td> 
			<td width="25%" style="text-align: center"><s:property value="specandmodel" /></td>
			<td width="10%" style="text-align: center"><s:property value="goodsNo" /></td>
			<td width="10%" style="text-align: center"><s:property value="goodsPrice" /></td>
			<td width="10%" style="text-align: center"><s:property value="amt" /></td> 
			<td width="10%" style="text-align: center"><s:property value="taxRate" /></td> 
			<td width="10%" style="text-align: center"><s:property value="taxAmt" /></td> 
		</tr>
		</s:iterator>
	</s:else>
</table> -->

</form>
</body>
<script>
$(function(){
	$("#vatOutAmt").change(function(){
		if($(this).val()==""){
			alert("转出金额不能为空");
			return;
		}
		$vatOutProportion=parseFloat($(this).val())/parseFloat($("#taxAmtSum").val())
		$("#vatOutProportion").val(($vatOutProportion).toFixed(2));
	});
	
	$("#vatOutProportion").change(function(){
		if($(this).val()==""){
			alert("转出比例不能为空");
			return;
		}
		$vatOutAmt=parseFloat($(this).val())*parseFloat($("#taxAmtSum").val())
		$("#vatOutAmt").val(($vatOutAmt).toFixed(2));
	});
})
</script>
</html>