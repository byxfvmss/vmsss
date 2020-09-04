// 票据提交时，对界面字段进行校验
function submitBillCheck(){
	var message = "";
	if (document.getElementById("billInfo.customerName").value == ''){
		message += "客户纳税人名称不能为空！\n";
	}
	if (document.getElementById("billInfo.customerTaxno").value == ''){
		message += "客户纳税人识别号不能为空！\n";
	}
	if (document.getElementById("billInfo.customerAddressandphone").value == ''){
		message += "客户地址电话不能为空！\n";
	}
	if (document.getElementById("billInfo.customerBankandaccount").value == ''){
		message += "客户银行账号不能为空！\n";
	}
	if (document.getElementById("billInfo.amtSum").value == ''){
		message += "合计金额不能为空！\n";
	}
	if (document.getElementById("billInfo.sumAmt").value == ''){
		message += "价税合计不能为空！\n";
	}else{
		if (document.getElementById("billInfo.amtSum").value != ''){
			if (document.getElementById("billInfo.taxAmtSum").value != '' && eval(document.getElementById("billInfo.taxAmtSum").value) != 0.00){
				var vSumAmt = eval(document.getElementById("billInfo.amtSum").value) + eval(document.getElementById("billInfo.taxAmtSum").value);
				if (document.getElementById("billInfo.sumAmt").value != vSumAmt){
					//message += "价税合计应等于合计金额与合计税额之和！\n";
				}
			}else{
				if (document.getElementById("billInfo.sumAmt").value != document.getElementById("billInfo.amtSum").value){
					//message += "当合计税额为零时，价税合计应等于合计金额！\n";
				}
			}
		}
	}
	if (document.getElementById("billInfo.drawer").value == ''){
		message += "开票人不能为空！\n";
	}
	if (document.getElementById("billInfo.name").value == ''){
		message += "我方纳税人名称不能为空！\n";
	}
	if (document.getElementById("billInfo.taxno").value == ''){
		message += "我方纳税人识别号不能为空！\n";
	}
	if (document.getElementById("billInfo.addressandphone").value == ''){
		message += "我方地址电话不能为空！\n";
	}
	if (document.getElementById("billInfo.bankandaccount").value == ''){
		message += "我方银行账号不能为空！\n";
	}
	if (message != ''){
		alert(message);
		return false;
	}else{
		return true;
	}
}
//票据预开页面商品明细之校验

function submitBillCheckList(){
	var message="";
	var grid  = $("#billItemPreListYS").find("tr");
	var param = "";
	for(var i=1;i<grid.length;i++){
		var td = $(grid[i]).find("td input");
		var a = $(grid[i]).find("td a");
		var count = $(a[0]).text();
		var select = $(grid[i]).find("td select");
		
		if($(select[0]).val()==""||$(select[0]).val()==null){
			message += "第"+ count +"行 商品名称不可为空! \n";
		}
		if($(td[1]).val()==""||$(td[1]).val()==null){
			message += "第"+ count +"行 规格型号不可为空! \n";
		}
		if($(td[2]).val()==""||$(td[2]).val()==null){
			message += "第"+ count +"行 单位不可为空! \n";
		}
		if($(td[3]).val()==""||$(td[3]).val()==null||isNaN($(td[3]).val())){
			message += "第"+ count +"行 价税合计必须为数字并不可为空! \n";
		}
		if($(td[4]).val()==""||$(td[4]).val()==null||isNaN($(td[4]).val())){
			message += "第"+ count +"行 金额必须为数字并不可为空! \n";
		}
		if($(td[5]).val()==""||$(td[5]).val()==null||isNaN($(td[5]).val())){
			message += "第"+ count +"行 数量必须为数字并不可为空! \n";
		}
	}
	if(message!=""){
		alert(message);
		return false;
	}else{
		return true;
	}
	
}
// 票据明细保存时，对界面字段进行校验
function saveBillItemCheck(){
	var message = "";
	if (document.getElementById("billItem.transType").value == ''){
		message += "请选择交易类型！\n";
	}
	if (document.getElementById("billItem.goodsName").value == ''){
		message += "商品名称不能为空！\n";
	}
	//if (document.getElementById("billItem.specandmodel").value == ''){
	//	message += "规格型号不能为空！\n";
	//}
	//if (document.getElementById("billItem.goodsNo").value == ''){
	//	message += "商品数量不能为空！\n";
	//}
	//if (document.getElementById("billItem.goodsPrice").value == ''){
	//	message += "商品单价不能为空！\n";
	//}
	if (message != ''){
		alert(message);
		return false;
	}else{
		return true;
	}
}
// 进项税信息保存时，对界面字段进行校验
function saveInputVatCheck(){
	var message = "";
	if (document.getElementById("inputVatInfo.billCode").value == ''){
		message += "发票代码不能为空！\n";
	}
	if (document.getElementById("inputVatInfo.billNo").value == ''){
		message += "发票号码不能为空！\n";
	}
	if (message != ''){
		alert(message);
		return false;
	}else{
		return true;
	}
}

//EMS快递信息提交时，对界面字段进行校验
function submitEmsCheck(){
	var message = "";
	if (document.getElementById("emsInfo.addressee").value == ''){
		message += "客户收件人不能为空！\n";
	}
	if (document.getElementById("emsInfo.addresseePhone").value == ''){
		message += "客户收件人电话不能为空！\n";
	}
	if (document.getElementById("emsInfo.addresseeAddress").value == ''){
		message += "收件地址不能为空！\n";
	}
	if (document.getElementById("emsInfo.addresseeAddressdetail").value == ''){
		message += "详细收件地址不能为空！\n";
	}
	if (document.getElementById("emsInfo.fedexExpress").value == ''){
		message += "快递公司不能为空！\n";
	}
	if (document.getElementById("emsInfo.emsNo").value == ''){
		message += "快递单号不能为空！\n";
	}
	if (document.getElementById("emsInfo.sender").value == ''){
		message += "寄件人不能为空！\n";
	}
	if (message != ''){
		alert(message);
		return false;
	}else{
		return true;
	}
}

/**
 * Abel:大都会grid字段多,做自适应表格
 */
$(function(){
	var ifrm = parent.document.getElementById("frame");
	$("#rDiv").attr("style","width:0px;height:auto;");
	var lessGridList = $("#rDiv").parent().attr("id");
	var width = document.getElementById(lessGridList).offsetWidth;
	var table = $("#rDiv table");
	if(ifrm.cols!="230,10,*" || Number(table.width()) < Number(width)){
		$("#rDiv").attr("style","iddle:" + Number(width) * 0.956 + "px;height:auto;");
	}
});

function mess(flag) {
	var lessGridList = $("#rDiv").parent().attr("id");
	if (flag == 2) {
		$("#rDiv").attr("style","width:0px;height:auto;");
		var width = document.getElementById(lessGridList).offsetWidth;
		$("#rDiv").attr("style","width:" + Number(width) * 0.98 + "px;height:auto;");
	} else {
		var width = document.getElementById(lessGridList).offsetWidth;
		$("#rDiv").attr("style","width:" + Number(width) * 0.98 + "px;height:auto;");
	}
}
