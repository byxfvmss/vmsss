<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<base target="_self">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../../page/include.jsp"%>
<script type="text/javascript"
	src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<link href="<%=bopTheme2%>/css/main.css" type="text/css"
	rel="stylesheet">
<link href="<%=webapp%>/page/subWindow.css" type="text/css"
	rel="stylesheet">
<link href="<c:out value="${sysTheme}"/>/css/subWindow.css"
	type="text/css" rel="stylesheet">
<title><s:if test="operType=='add' ">新增发票申请信息</s:if><s:elseif
		test="operType=='edit'">编辑发票申请信息</s:elseif></title>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script type="text/javascript" src="<%=bopTheme%>/js/validator.js"></script>
<script type="text/javascript"
	src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript">
//标识页面是否已提交
var subed = false;
function checkForm(){

    //验证是否重复提交
    if (subed == true){
       alert("信息正在发送给服务器，请不要重复提交信息！");
       return false;
    }
    
  	//申请日期
    if(fucCheckNull(document.getElementById("applyTime"),"请选择申请日期")==false){
       return false;
    }
	//领取日期是否为空
    if(fucCheckNull(document.getElementById("productType"),"请选择应税服务")==false){
       return false;
    } 
    subed=true;
    return true;
}

function submitForm(){
	    checkForm();
		document.forms[0].action = "<%=webapp%>/saveInvoiceInquerity.action";
		document.forms[0].submit();

}

</script>
</head>
<body scroll="no" style="overflow: hidden;">
	<div class="showBoxDiv">
		<form id="frm"
			action="<c:out value='${webapp}'/>/saveInvoiceInquerity.action"
			method="post">
			<div id="editsubpanel" class="editsubpanel">
				<div style="overflow: auto; width: 100%;">
					<input type="hidden" name="operType"
						value="<s:property value="operType" />" /> <input type="hidden"
						id="applicationFormId" name="applicationFormId"
						value="<s:property value="invoiceInquerityListInfo.applicationFormId" />" />
					<table id="contenttable" class="lessGrid" cellspacing="0"
						width="100%" align="center" cellpadding="0">
						<tr>
							<th colspan="4"><s:if test="operType=='add' ">新增发票申请信息</s:if>
								<s:elseif test="operType=='edit'">编辑发票申请信息</s:elseif></th>
						</tr>
						<tr>
							<td width="15%" style="text-align: right" class="listbar">申请日期:</td>
							<td width="35%"><input id="applyTime"
								name="applyTime" type="text" readonly="true"
								value="<s:property value="invoiceInquerityListInfo.receiveInvoiceTime" />"
								class="tbl_query_time"
								onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"
								style="width: 155px;">
								<span class="spanstar">*</span></td>
							<td width="15%" style="text-align: right" class="listbar">应税服务名称:</td>
							<td width="35%"><select id="productType" name="productType">
									<s:iterator value="mapVatType" id="entry">
										<option value="<s:property value="key"/>"
											<s:if test='invoiceInquerityListInfo.productType==#entry.key'>selected</s:if>
											<s:else></s:else> >
											<s:property value="value" />
										</option>
									</s:iterator>
							</select> <span class="spanstar">*</span></td>
						</tr>
						<tr>
						 <td width="15%" style="text-align: right" class="listbar">金额:</td>
							<td><input id="amount" name="amount" type="text"
								class="tbl_query_text"
								value="<s:property value="invoiceInquerityListInfo.amount" />"
								maxlength="20"/>
							    <span class="spanstar">*</span>
							</td>	
							<td width="15%" style="text-align: right" class="listbar">价税合计:</td>
							<td><input id="totalPriceandTax" name="totalPriceandTax" type="text"
								class="tbl_query_text"
								value="<s:property value="invoiceInquerityListInfo.totalPriceandTax" />"
								maxlength="20"/>
							</td>
													
						</tr>
						<tr>							
							<td width="15%" style="text-align: right" class="listbar">税率:</td>
							<td><input id="taxRate" name="taxRate" type="text"
								class="tbl_query_text"
								value="<s:property value="invoiceInquerityListInfo.taxRate" />" onkeyup='keyTaxRate(this,this.value);'
								maxlength="20"/>
							</td>
							<td width="15%" style="text-align: right" class="listbar">税额:</td>
							<td><input id="amountTax" name="amountTax" type="text"
								class="tbl_query_text"
								value="<s:property value="invoiceInquerityListInfo.amountTax" />"
								maxlength="20"/>
						   </td>
						</tr>
					</table>
				</div>
				<div id="ctrlbutton" class="ctrlbutton" style="border: 0px">	
			       <input type="button" class="tbl_query_button"
						onMouseMove="this.className='tbl_query_button_on'"
						onMouseOut="this.className='tbl_query_button'"
						onclick="submitForm();" name="BtnSave" value="保存" id="BtnSave" />
					<input type="button" class="tbl_query_button"
						onMouseMove="this.className='tbl_query_button_on'"
						onMouseOut="this.className='tbl_query_button'"
						onclick="CloseWindow();" name="BtnReturn" value="关闭"
						id="BtnReturn" />
				</div>
			</div>
		</form>
	</div>
</body>
<script type="text/javascript">
/*  计算税率*/
function keyTaxRate(t,value){
		var tr = $(t).closest("tr");
		var income = tr.find("[name=income]").val(); 
		if(isNaN(Number(value))){
			$(t).css("color","red");
			tr.find("[name=taxAmt]").val("");
		}else if(value>1||value<0){
			$(t).css("color","red");
			tr.find("[name=taxAmt]").val("");
		}else{
			$(t).css("color","");
			var taxAmt = Number(income*(value*1)).toFixed(2);
			tr.find("[name=taxAmt]").val(taxAmt);
			tr.find("[name=taxAmt]").css("color","");
			var amt = taxAmt*1+income*1
			tr.find("[name=sumAmt]").val(amt);
			tr.find("[name=sumAmt]").css("color","");
		}
		function keySumAmt(t,value){
			var tr = $(t).closest("tr");
			var taxRate = tr.find("[name=taxRate]").val();
			if(Number(value)<=0){
				$(t).css("color","red");
				tr.find("[name=taxAmt]").val("");
				tr.find("[name=income]").val("");
				tr.find("[name=goodsPrice]").val("");
			}else if(isNaN(Number(value))){
				$(t).css("color","red");
				tr.find("[name=taxAmt]").val("");
				tr.find("[name=income]").val("");
				tr.find("[name=goodsPrice]").val("");
			}else{
				$(t).css("color","");
				var taxAmt = Number(value/(1+taxRate*1)*taxRate*1).toFixed(2);
				tr.find("[name=taxAmt]").val(taxAmt);
				tr.find("[name=taxAmt]").css("color","");
				var income = Number(value-taxAmt).toFixed(2);
				tr.find("[name=income]").val(income);
				tr.find("[name=income]").css("color","");
				var goodsNum = tr.find("[name=goodsNum]").val();
				if(!(goodsNum==null||goodsNum==""||Number(goodsNum)==0||isNaN(Number(goodsNum)))){
					var goodsPrice = Number(income/(goodsNum*1)).toFixed(2);
					tr.find("[name=goodsPrice]").val(goodsPrice);
					tr.find("[name=goodsPrice]").css("color","");
				}
			}
		}
		function keySumAmt(t,value){
			var tr = $(t).closest("tr");
			var taxRate = tr.find("[name=taxRate]").val();
			if(Number(value)<=0){
				$(t).css("color","red");
				tr.find("[name=taxAmt]").val("");
				tr.find("[name=income]").val("");
				tr.find("[name=goodsPrice]").val("");
			}else if(isNaN(Number(value))){
				$(t).css("color","red");
				tr.find("[name=taxAmt]").val("");
				tr.find("[name=income]").val("");
				tr.find("[name=goodsPrice]").val("");
			}else{
				$(t).css("color","");
				var taxAmt = Number(value/(1+taxRate*1)*taxRate*1).toFixed(2);
				tr.find("[name=taxAmt]").val(taxAmt);
				tr.find("[name=taxAmt]").css("color","");
				var income = Number(value-taxAmt).toFixed(2);
				tr.find("[name=income]").val(income);
				tr.find("[name=income]").css("color","");
				var goodsNum = tr.find("[name=goodsNum]").val();
				if(!(goodsNum==null||goodsNum==""||Number(goodsNum)==0||isNaN(Number(goodsNum)))){
					var goodsPrice = Number(income/(goodsNum*1)).toFixed(2);
					tr.find("[name=goodsPrice]").val(goodsPrice);
					tr.find("[name=goodsPrice]").css("color","");
				}
			}
		}
	</script>
</html>