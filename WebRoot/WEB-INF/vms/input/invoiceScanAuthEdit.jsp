<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../../page/include.jsp"%>
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
<link href="<%=webapp%>/page/common.css" type="text/css" rel="stylesheet">

<!-- MessageBox -->
<script src="<%=bopTheme%>/js/MessageBox/js/messageBox.js" type="text/javascript"></script>
<link href="<%=bopTheme%>/js/MessageBox/css/messageBox.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=bopTheme%>/js/validator.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/search.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
<meta http-equiv="Pragma" content="no-cache" />
<title>进项税管理</title>

<style type="text/css">
.detailInfoDIV {
	border: 1px solid green;
	background-color: khaki;
	top: 110px;
	left: 150px;
	width: 450px;
	height: 300px;
	position: absolute;
	z-index: 2;
	filter: alpha(opacity =     90);
	-moz-opacity: 0.9;
	display: none;
}
 
.unSelectBox { 
text-align: center; 
vertical-align:middle; 
height: 39px; 
overflow: hidden; 
moz-user-select: -moz-none; 
-moz-user-select: none; 
-o-user-select:none; 
-khtml-user-select:none; /* you could also put this in a class */ 
-webkit-user-select:none;/* and add the CSS class here instead */ 
-ms-user-select:none; 
user-select:none;/**禁止选中文字*/ 
} 

</style>
<script type="text/javascript">
	//标识页面是否已提交
	var subed = false;
	function checkForm(){
	    //验证是否重复提交
	    if (subed == true){
	       alert("信息正在发送给服务器，请不要重复提交信息！");
	       return false;
	    }
	    if(fucCheckNull(document.getElementById("payee"),"收款人不能为空")==false){
	       return false;
	    }
	    
	    if(fucCheckNull(document.getElementById("reviewer"),"审核人不能为空")==false){
	       return false;
	    }
	    if(fucCheckNull(document.getElementById("drawer"),"开票人不能为空")==false){
	       return false;
	    }
	    
	    if(!/^[\u4e00-\u9fa5]+$/gi.test(document.getElementById("payee").value)){
		    var m = new MessageBox(document.getElementById("payee"));
		   	m.Show("收款人仅允许输入中文汉字!");	
		   	obj.focus();
	    	return false;
	    }
	    if(!/^[\u4e00-\u9fa5]+$/gi.test(document.getElementById("reviewer").value)){
	    	var m = new MessageBox(document.getElementById("reviewer"));
		   	m.Show("审核人仅允许输入中文汉字!");	
		   	obj.focus();
	    	return false;
	    }
	    if(!/^[\u4e00-\u9fa5]+$/gi.test(document.getElementById("drawer").value)){
	    	var m = new MessageBox(document.getElementById("drawer"));
		   	m.Show("开票人仅允许输入中文汉字!");	
		   	obj.focus();
	    	return false;
	    }
	    <s:if test='authPassFlag=="0"'>
	    if(fucCheckNull(document.getElementById("form_datastatus"),"认证状态不能为空")==false){
	       return false;
	    }
	    </s:if>
	    subed=true;
	    return true;
}
function submitForm(actionUrl){
	var form = document.getElementById("main");
	form.action=actionUrl;
	form.submit();
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
function checkLength(){
    var value = document.getElementById("remark").value;
    if(value.length>125){
        document.getElementById("remark").value=document.getElementById("remark").value.substr(0, 125);
    }
}
function checkPositiveNumber(value){
  // 验证正整数包括0 
  var bool= /^(0|[1-9][0-9]*)$/.test(value);
  return bool;

}
function checkValueIs(obj, msg){
	var re = /^[0-9]+.?[0-9]*$/; //判断字符串是否为数字 //判断正整数 /^[1-9]+[0-9]*]*$/ 
　　var nubmer = obj.value;
　　if (!re.test(nubmer)) {
		obj.focus();
		var m = new MessageBox(obj);
		m.Show(msg);
		return false;
	}
	if(parseFloat(nubmer) <= 0){
		obj.focus();
		var m = new MessageBox(obj);
		m.Show(msg);
		return false;
	}
}
function itemInfoOperCheck(index){
   	if(fucCheckNull(document.getElementById("goodsName"+index),"商品名称不能为空")==false){
       return false;
    }
   	if(fucCheckNull(document.getElementById("specandmodel"+index),"规格型号不能为空")==false){
       return false;
    }
   	if(fucCheckNull(document.getElementById("goodsNo"+index),"商品数量不能为空")==false){
       return false;
    }
   	if(fucIsNoUnsignedFloat(document.getElementById("goodsNo"+index),"商品单价格式不正确")==false){
       return false;
    }

   	if(fucCheckNull(document.getElementById("goodsPrice"+index),"商品单价不能为空")==false){
       return false;
    }
   	if(fucIsNoUnsignedFloat(document.getElementById("goodsPrice"+index),"商品单价格式不正确")==false){
       return false;
    }

   	if(fucCheckNull(document.getElementById("amt"+index),"金额不能为空")==false){
       return false;
    }
    if(fucIsNoUnsignedFloat(document.getElementById("amt"+index),"金额格式不正确")==false){
       return false;
    }
    
   	if(fucCheckNull(document.getElementById("taxRate"+index),"税率不能为空")==false){
       return false;
    }
    if(fucIsNoUnsignedFloat(document.getElementById("taxRate"+index),"税率格式不正确")==false){
       return false;
    }
   	if(fucCheckNull(document.getElementById("taxAmt"+index),"税额不能为空")==false){
       return false;
    }
    if(fucIsNoUnsignedFloat(document.getElementById("taxAmt"+index),"税额格式不正确")==false){
       return false;
    }
    
    if(checkValueIs(document.getElementById("goodsNo"+index),"商品数据必须为大于0的数字") == false){
		return false;	
    }
    if(checkValueIs(document.getElementById("goodsPrice"+index),"商品单价必须为大于0的数字") == false){
		return false;	
    }
    if(checkValueIs(document.getElementById("amt"+index),"金额必须为大于0的数字") == false){
		return false;	
    }
    if(checkValueIs(document.getElementById("taxRate"+index),"税率必须为大于0的数字") == false){
		return false;	
    }
    if(checkValueIs(document.getElementById("taxAmt"+index),"税额必须为大于0的数字") == false){
		return false;	
    }
    return true;
}
function saveInvoiceItem(index){
	if(itemInfoOperCheck(index) == false){
		return;
	}
	var billItemId = document.getElementById("billItemId"+index).value
	var billId = document.getElementById("o_bill_id").value;
	var goodsName = document.getElementById("goodsName"+index).value;
	var specandmodel = document.getElementById("specandmodel"+index).value;
	var goodsNo = new Number(document.getElementById("goodsNo"+index).value).toFixed(2);
	var goodsPrice = new Number(document.getElementById("goodsPrice"+index).value).toFixed(2);
	var amt = new Number(document.getElementById("amt"+index).value).toFixed(2);
	var taxRate = new Number(document.getElementById("taxRate"+index).value).toFixed(2);
	var taxAmt = new Number(document.getElementById("taxAmt"+index).value).toFixed(2);
	
	$.ajax({url: 'updateInvoiceItemInfo.action',
			type: 'POST',
			async:false,
			data:{billId:billId,billItemId:billItemId,goodsName:goodsName,specandmodel:specandmodel,goodsNo:goodsNo,goodsPrice:goodsPrice,amt:amt,taxRate:taxRate,taxAmt:taxAmt},
			dataType: 'text',
			timeout: 1000,
			error: function(){
				alert("保存失败，请修改数据后重试。");			
				return false;
			},
			success: function(result){
				alert("保存成功！");
				document.getElementById("billItemId"+index).value = result;
				document.getElementById("goodsNo"+index).value = goodsNo;
				document.getElementById("goodsPrice"+index).value = goodsPrice;
				document.getElementById("amt"+index).value = amt;
				document.getElementById("taxRate"+index).value = taxRate;
				document.getElementById("taxAmt"+index).value = taxAmt;
				return true;
			}
	});
}
function deleteInvoiceItem(index){
	var billItemId = document.getElementById("billItemId"+index).value
	if(billItemId == ""){
		setInvoiceItem(index);
		return;
	}
	var billId = document.getElementById("o_bill_id").value;
	var tresult = false;
	$.ajax({url: 'deleteInvoiceItemInfo.action',
			type: 'POST',
			async:false,
			data:{billId:billId,billItemId:billItemId},
			dataType: 'text',
			timeout: 1000,
			error: function(){
				return false;
			},
			success: function(result){
				alert("商品信息删除成功！");
				setInvoiceItem(index);
				return;
			}
	});
}
function setInvoiceItem(index){
	document.getElementById("billItemId"+index).value = "";
	document.getElementById("goodsName"+index).value = "";
	document.getElementById("specandmodel"+index).value = "";
	document.getElementById("goodsNo"+index).value = "";
	document.getElementById("goodsPrice"+index).value = "";
	document.getElementById("amt"+index).value = "";
	document.getElementById("taxRate"+index).value = "";
	document.getElementById("taxAmt"+index).value = "";
}
</script>
</head>
<body>
<form id="main" action="<c:out value='${webapp}'/>/updateInvoiceScanAuth.action" method="post">
<input type="hidden" name="o_bill_id" value="<s:property value='o_bill_id'/>"/>
<input type="hidden" name="billDate" value="<s:property value='billDate'/>"/>
<input type="hidden" name="customerName" value="<s:property value='customerName'/>"/>
<input type="hidden" name="datastatus" value="<s:property value='datastatus'/>"/>
<input type="hidden" name="instId" value="<s:property value='instId'/>"/>
<input type="hidden" name="billCode" value="<s:property value='billCode'/>"/>
<input type="hidden" name="billNo" value="<s:property value='billNo'/>"/>
<input type="hidden" name="fapiaoType" value="<s:property value='fapiaoType'/>"/>
<input type="hidden" name="identifyDate" value="<s:property value='identifyDate'/>"/>
<input type="hidden" name="paginationList.currentPage" value="<s:property value='currentPage'/>"/>

<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
	<tr>
		<td align="left">
			<div id="tbl_current_status">
				<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> 
				<span class="current_status_menu">当前位置：</span> 
				<span class="current_status_submenu1">进项税管理</span>
				<span class="current_status_submenu">认证管理</span>
				<span class="current_status_submenu">扫描认证</span>
				<span class="current_status_submenu">扫描编辑</span>
			</div>
			<table id="tbl_tools" width="100%" border="0" >
				<tr style="margin-top:10px;">
					<td align="left" ><input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 
					onclick="submitForm('listInvoiceScanAuth.action');"  name="cmdDelbt" value="返回" id="cmdDelbt" />
					</td>
				</tr>
			</table>
			<div id="whitebox">
				<div class="boxline">基本信息编辑</div>
				<table  id="tbl_context" cellspacing="0" width="100%" align="center" cellpadding="0">
					<tr>
						<td width="11%"  align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">发票代码:&nbsp;&nbsp;&nbsp;</td> 
						<td width="15%" >
							<s:property value='inputInvoiceInfo.billCode'/>
						</td>
						<td width="10%"  align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">发票号码:&nbsp;&nbsp;&nbsp;</td> 
						<td>
						<s:property value='inputInvoiceInfo.billNo'/>
						</td>
						<td width="11%"  align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">开票日期:&nbsp;&nbsp;&nbsp;</td> 
						<td width="15%" >
							<s:property value='inputInvoiceInfo.billDate'/>
						</td>
					</tr>
					<tr>
						<td width="11%"  align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">供应商中文名称:&nbsp;&nbsp;&nbsp;</td> 
						<td width="15%" >
							<s:property value='inputInvoiceInfo.vendorName'/>
						</td>
						<td width="10%"  align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">供应商纳税人识别号:&nbsp;&nbsp;&nbsp;</td> 
						<td>
						<s:property value='inputInvoiceInfo.vendorTaxno'/>
						</td>
						<td width="11%"  align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">供应商电话:&nbsp;&nbsp;&nbsp;</td> 
						<td width="15%" >
							<s:property value='inputInvoiceInfo.vendorPhone'/>
						</td>
					</tr>
					<tr>
						<td width="10%"  align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">供应商地址:&nbsp;&nbsp;&nbsp;</td> 
						<td>
						<s:property value='inputInvoiceInfo.vendorAddress'/>
						</td>
						<td width="11%"  align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">供应商银行账号:&nbsp;&nbsp;&nbsp;</td> 
						<td width="15%" >
						<s:property value='inputInvoiceInfo.vendorBankandaccount'/>
						</td>
						<td width="10%"  align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">价税合计金额:&nbsp;&nbsp;&nbsp;</td> 
						<td>
						<s:property value='inputInvoiceInfo.sumAmt'/>
						</td>
					</tr>
					<tr>
						<td width="11%"  align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">合计金额:&nbsp;&nbsp;&nbsp;</td> 
						<td width="15%" >
							<s:property value='inputInvoiceInfo.amtSum'/>
						</td>
						<td width="10%"  align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">合计税额:&nbsp;&nbsp;&nbsp;</td> 
						<td>
						<s:property value='inputInvoiceInfo.taxAmtSum'/>
						</td>
						<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
							认证状态:&nbsp;&nbsp;&nbsp;
						</td>
						<td>
							<s:if test='authPassFlag=="1"'>
								<select id="" name="" disabled >
									<option value="" <s:if test='inputInvoiceInfo.datastatus==""'>selected</s:if><s:else></s:else>>全部</option>
									<s:iterator value="mapDatastatus" id="entry">  
									<option value="<s:property value="key"/>" <s:if test='inputInvoiceInfo.datastatus==#entry.key'>selected</s:if><s:else></s:else>><s:property value="value"/></option>
							       </s:iterator>
								</select>
								<input type="hidden" name="inputInvoiceInfo.datastatus"  id="datastatus"  value="<s:property value='inputInvoiceInfo.datastatus'/>" />
							</s:if>
							<s:else>
								<select id="form_datastatus" name="inputInvoiceInfo.datastatus"  >
									<option value="" <s:if test='inputInvoiceInfo.datastatus==""'>selected</s:if><s:else></s:else>>全部</option>
									<s:iterator value="mapDatastatus" id="entry">  
									<option value="<s:property value="key"/>" <s:if test='inputInvoiceInfo.datastatus==#entry.key'>selected</s:if><s:else></s:else>><s:property value="value"/></option>
							       </s:iterator>
								</select>
							</s:else>
						</td>
					</tr>
					<tr>
						<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
							收款人 :&nbsp;&nbsp;&nbsp;
						</td>
						<td>
							<input type="text" class="tbl_query_text" name="inputInvoiceInfo.payee"  id="payee"  maxlength="30" 
							onkeyup="value=value.replace(/[\d]/g,'') "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[\d]/g,''))" 
							value="<s:property value='inputInvoiceInfo.payee'/>" />
						</td>
						<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
							审核人 :&nbsp;&nbsp;&nbsp;
						</td>
						<td>
							<input type="text" class="tbl_query_text" name="inputInvoiceInfo.reviewer"  id="reviewer"  maxlength="30" 
							onkeyup="value=value.replace(/[\d]/g,'') "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[\d]/g,''))" 
							value="<s:property value='inputInvoiceInfo.reviewer'/>" />
						</td>
						<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
							开票人 :&nbsp;&nbsp;&nbsp;
						</td>
						<td>
							<input type="text" class="tbl_query_text" name="inputInvoiceInfo.drawer"  id="drawer"  maxlength="30" 
							onkeyup="value=value.replace(/[\d]/g,'') "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[\d]/g,''))" 
							value="<s:property value='inputInvoiceInfo.drawer'/>" />
						</td>
					</tr>
					<tr>
						<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
							备注:&nbsp;&nbsp;&nbsp;
						</td>
						<td colspan="4">
							<textarea  rows="3" cols="20" id="remark" name="remark"  name="inputInvoiceInfo.remark" 
							onKeyDown="checkLength()"  onKeyUp="checkLength()" onPaste="checkLength()"
							 ><s:property value='inputInvoiceInfo.remark'/></textarea>
						</td>
						<td><input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 
						onclick="" name="cmdEditSavebt" value="保存" id="cmdEditSavebt" /></td>
					</tr>
				</table>
				<div class="boxline">商品信息编辑</div>
				<table  id="goods_tab" class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0"  style="border-collapse: collapse;width: 100%;">
					<tr class="lessGrid head good_head">
						<th style="text-align: center;">序号</th>
						<th style="text-align: center;width:15%">商品名称</th>
						<th style="text-align: center">规格型号</th>
						<th style="text-align: center">商品数量</th>
						<th style="text-align: center">商品单价</th>
						<th style="text-align: center">金额</th>
						<th style="text-align: center">税率</th>
						<th style="text-align: center">税额</th>
						<th style="text-align: center">操作</th>
					</tr>
					<s:iterator value="lstInputInvoiceItem" id="iList" status="stuts">
					<tr class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
						<td style="text-align: center;width:3%"><s:property value="#stuts.index+1"/><input type="hidden" id="billItemId<s:property value='#stuts.index'/>" name="billItemId<s:property value='#stuts.index'/>" value="<s:property value='#iList.billItemId'/>"s/></td>
						<td style="text-align: center;width:30%"><input type="text" class="tbl_query_text" id="goodsName<s:property value='#stuts.index'/>" name="goodsName<s:property value='#stuts.index'/>" value="<s:property value='#iList.goodsName'/>" style="width:100%;"  maxlength="30"/></td>
						<td style="text-align: center;width:20%"><input type="text" class="tbl_query_text" id="specandmodel<s:property value='#stuts.index'/>" name="specandmodel<s:property value='#stuts.index'/>" value="<s:property value='#iList.specandmodel'/>" style="width:100%;"  maxlength="30"/></td>
						<td style="text-align: center;width:8%"><input type="text" class="tbl_query_text" id="goodsNo<s:property value='#stuts.index'/>" name="goodsNo<s:property value='#stuts.index'/>" value="<s:property value='#iList.goodsNo'/>" style="width:100%;" maxlength="15" onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')"/></td>
						<td style="text-align: center;width:8%"><input type="text" class="tbl_query_text" id="goodsPrice<s:property value='#stuts.index'/>" name="goodsPrice<s:property value='#stuts.index'/>" value="<s:property value='#iList.goodsPrice'/>" style="width:100%;" maxlength="15"  onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')"/></td>
						<td style="text-align: center;width:8%"><input type="text" class="tbl_query_text" id="amt<s:property value='#stuts.index'/>" name="amt<s:property value='#stuts.index'/>" value="<s:property value='#iList.amt'/>" style="width:100%;" maxlength="15"  onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')"/></td>
						<td style="text-align: center;width:8%"><input type="text" class="tbl_query_text" id="taxRate<s:property value='#stuts.index'/>" name="taxRate<s:property value='#stuts.index'/>" value="<s:property value='#iList.taxRate'/>" style="width:100%;" maxlength="15"  onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')"/></td>
						<td style="text-align: center;width:8%"><input type="text" class="tbl_query_text" id="taxAmt<s:property value='#stuts.index'/>" name="taxAmt<s:property value='#stuts.index'/>" value="<s:property value='#iList.taxAmt'/>" style="width:100%;" maxlength="15"  onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')"/></td>
						<td style="text-align: center;">
							<a href="javascript:void();" onclick="saveInvoiceItem('<s:property value='#stuts.index'/>')" class="cmdItemEditBt">保存</a>
							<a href="javascript:void();" onclick="deleteInvoiceItem('<s:property value='#stuts.index'/>')" class="cmdItemDelBt">删除</a>
						</td>
					</tr>
					</s:iterator>
				</table>
				<div class="boxline">票据交易数据</div>
			<div style="border-bottom:#DDD solid 1px;">
				<iframe id="subTableFrame1" scrolling="auto" src="listInputTransItem.action?billId=<s:property value='o_bill_id'/>" height="160px" width="100%" frameborder="0"></iframe>
			</div>
				<!-- <div class="boxline">关联交易信息</div>
				<div id="lessGridList12" style="overflow: auto; width: 100%;">
					<table  class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0"  style="width: 100%;">
						<tr class="lessGrid head">
							<th style="text-align: center;width:15%">交易编号</th>
							<th style="text-align: center">金额_人民币</th>
							<th style="text-align: center">税额_人民币</th>
							<th style="text-align: center">供应商</th>
							<th style="text-align: center">交易发生机构</th>
						</tr>
						<tr class="lessGrid rowA">
							<s:if test='inputTransInfo == null'>
								<td colspan="5">该发票没有对应的交易信息！</td>
							</s:if>
							<s:else>
								<td style="text-align: center"><s:property value='inputTransInfo.dealNo'/></td>
								<td style="text-align: center"><s:property value='inputTransInfo.amtCny'/></td>
								<td style="text-align: center"><s:property value='inputTransInfo.taxAmtCny'/></td>
								<td style="text-align: center"><s:property value='inputTransInfo.vendorName'/></td>
								<td style="text-align: center"><s:property value='inputTransInfo.bankName'/></td>
							</s:else>
						</tr>
					</table> 
				</div>-->
			</div>			
		</td>
	</tr>
</table>

</form>
</body>
<script>
var hightValue = screen.availHeight - 180;
document.getElementById("tbl_main").height=hightValue;

$(function(){
	$("#cmdEditSavebt").click(function(){
		if(checkForm()==true){
			submitForm("updateInvoiceScanAuth.action");
		}
	});
	
	$addGoodNum=0;
	$("#proAddbt").click(function (){
		if($addGoodNum == '8'){
			alert("商品最多只能添加8项，请验证！");
			return;
		}
		$tr_good='<tr>';
		$tr_good+='	<td style="text-align: center;padding:0px 0px 0px 0px;"><input type="text" class="tbl_query_text" id="goodsName'+$addGoodNum+'" name="goodsName'+$addGoodNum+'" value="" style="width:120px;"/></td>';
		$tr_good+='	<td style="text-align: center;padding:0px 0px 0px 0px;"><input type="text" class="tbl_query_text" id="specandmodel'+$addGoodNum+'" name="specandmodel'+$addGoodNum+'" value="" style="width:140px;"/></td>';
		$tr_good+='	<td style="text-align: center;padding:0px 0px 0px 0px;"><input type="text" class="tbl_query_text" id="goodsNo'+$addGoodNum+'" name="goodsNo'+$addGoodNum+'" value="" style="width:140px;"/></td>';
		$tr_good+='	<td style="text-align: center;padding:0px 0px 0px 0px;"><input type="text" class="tbl_query_text" id="goodsPrice'+$addGoodNum+'" name="goodsPrice'+$addGoodNum+'" value="" style="width:140px;"/></td>';
		$tr_good+='	<td style="text-align: center;padding:0px 0px 0px 0px;"><input type="text" class="tbl_query_text" id="amt'+$addGoodNum+'" name="amt'+$addGoodNum+'" value="" style="width:70px;"/></td>';
		$tr_good+='	<td style="text-align: center;padding:0px 0px 0px 0px;"><input type="text" class="tbl_query_text" id="taxRate'+$addGoodNum+'" name="taxRate'+$addGoodNum+'" value="" style="width:70px;"/></td>';
		$tr_good+='	<td style="text-align: center;padding:0px 0px 0px 0px;"><input type="text" class="tbl_query_text" id="taxAmt'+$addGoodNum+'" name="taxAmt'+$addGoodNum+'" value="" style="width:70px;"/></td>';
		$tr_good+='</tr>';
		$("#goods_tab").append($tr_good);
		$addGoodNum=$addGoodNum+1;
		$("#addGoodNum").val($addGoodNum);
	} );
});

</script>
</html>