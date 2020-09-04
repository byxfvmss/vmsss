<!--file: <%=request.getRequestURI()%> -->
<%@page import="com.cjit.vms.system.model.GoodsInfo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../../page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<title>增值税管理平台</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/editview.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/XmlHttp.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/vms.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/lhb.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/ylb.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
<link href="<c:out value="${bopTheme2}"/>/css/subWindow.css" type="text/css" rel="stylesheet">
<link href="<%=bopTheme2%>/css/editView.css" type="text/css" rel="stylesheet">
<style type='text/css'>
.lessGrids th {
    background: rgba(0, 0, 0, 0) url("<c:out value='${bopTheme}'/>/themes/images/listtitlebg.png") repeat-x scroll center bottom;
    color: #252525;
    cursor: default;
    font-weight: bold;
    line-height: 35px;
    padding-top: 7px;
	white-space: nowrap;
}
.red1 {
	border-color:red;
}

.black_overlay {
	display: none;
	position: absolute;
	top: 0%;
	left: 0%;
	width: 100%;
	height: 100%;
	background-color: black;
	z-index: 1001;
	-moz-opacity: 0.8;
	opacity: .50;
	filter: alpha(opacity = 80);
}
.white_content {
	display: none;
	position: absolute;
	top: 25%;
	left: 30%;
	width: 30%;
	height: 40%;
	padding: 16px;
	background-color: white;
	z-index: 1002;
	overflow: scroll;
}
</style>
<script type="text/javascript">
	var goodsEditFlag = false; //全局变量，判断是否展示商品编辑栏
	function init(){
		var billId = $("#billInfoBillId").val();
		if(billId==""||billId==null){
			$("[name=instCode] option:first").attr("selected","selected");
			$("[name = billInfo.name]").val("");
			$("[name = billInfo.taxno]").val("");
			$("[name = billInfo.addressandphone]").val("");
			$("[name = billInfo.bankandaccount]").val("");
		}
	}
	function loadSelfInfo(instCode){
		var param = "instCode=" + document.getElementById("instCode").value;
		$.ajax({
			type: "Post",
		    url: "loadSelfInfo.action?"+param,
		    dataType: "json", //可以是text，如果用text，返回的结果为字符串；如果需要json格式的，可是设置为json
		    success: function (data) {
				if(null == data){
					$("[name = billInfo.name]").val("");
					$("[name = billInfo.taxno]").val("");
					$("[name = billInfo.addressandphone]").val("");
					$("[name = billInfo.bankandaccount]").val("");
				}else{
					$("[name = billInfo.name]").val(data.taxperName);
					$("[name = billInfo.taxno]").val(data.taxperNumber);
					$("[name = billInfo.addressandphone]").val(data.taxAddress+" "+data.taxTel);
					$("[name = billInfo.bankandaccount]").val(data.taxBank+" "+data.account);
				}
			},
		    error: function (msg) {
		    	alert(" 数据加载失败！" + msg);
		    }
		 });
	}
	function save(){
		
		/* customerId=document.getElementById("billInfo.customerId").value; */
		
		if(goodsEditFlag){
			alert("数据保存中，请稍候...");
			return false;
		}
		if(!validation()){
			alert("输入信息有误，请检查...");
			return false;
		}
		//$("[name = billPreOpenItem]:first").remove();
		submitForm("savePreBill.action");
		goodsEditFlag = true;
	}
	
	function submitForm(actionUrl){
		document.forms[0].action = actionUrl;
		document.forms[0].submit();
		document.forms[0].action = '';
	}
	
	function validation(){
		//我方信息列
		var name = $("[name = billInfo.name]").val();
		var taxno = $("[name = billInfo.taxno]").val();
		var addressandphone = $("[name = billInfo.addressandphone]").val();
		var bankandaccount = $("[name = billInfo.bankandaccount]").val();
		//对方信息列
		var customerName = $("[name = billInfo.customerName]").val();
		var customerTaxno = $("[name = billInfo.customerTaxno]").val();
		var customerAddressandphone = $("[name = billInfo.customerAddressandphone]").val();
		var customerBankandaccount = $("[name = billInfo.customerBankandaccount]").val();
		//发票类型
		var fapiaoType = $("[name = billInfo.fapiaoType]").val();
		//专票校验
		if(fapiaoType == 0){
			if(customerTaxno==null||customerTaxno==""){
				return false;
			}
			if(customerAddressandphone==null||customerAddressandphone==""){
				return false;
			}
			if(customerBankandaccount==null||customerBankandaccount==""){
				return false;
			}
		}
		//普票和其他校验
		if(customerName==null||customerName==""){
				return false;
			}
		if(name==null||name==""){
				return false;
			}
		if(taxno==null||taxno==""){
				return false;
			}
		if(addressandphone==null||addressandphone==""){
				return false;
			}
		if(bankandaccount==null||bankandaccount==""){
				return false;
			}
		//all clear
		return true;
	}
	function rowDefination(){
		$("[name = billPreOpenItem]:odd").attr("class","lessGrid rowA");
		$("[name = billPreOpenItem]:even").attr("class","lessGrid rowB");
	}
	
	var getGoodsListFlag = true;
	function addGoods(){
		var goodsCount = $("[name=billPreOpenItem]").length;
		if(goodsCount == 1&&getGoodsListFlag){
			var param = $("[name = billInfo.taxno]").val();
			$.ajax({
				async: false,
				type: "Post",
		    	url: "getGoodsList.action?billInfo.taxno="+param,
		        
		    	dataType: "json", //可以是text，如果用text，返回的结果为字符串；如果需要json格式的，可是设置为json
		    	success: function (data) {
					getGoodsListFlag = false;
					var option = $("#goodsNo option:first");
					for(var i = 0; i < data.length; i++){
						var goodsOption = option.clone();
						var goodsInfo = data[i];
						goodsOption.html(goodsInfo.goodsFullName);
						goodsOption.val(goodsInfo.goodsNo);
						goodsOption.data("goodsInfo",goodsInfo);
						option.after(goodsOption);
					}
					$("#goodsNo option:first").attr('selected', 'true');
				},
		    	error: function (msg) {
		    		alert(" 数据加载失败！" + msg);
		    	}
			});
		}
		if(goodsCount == 9){
			alert("商品数量不能大于8条!");
			return false;
		}
		var template = $("[name=billPreOpenItem]:first").clone(true,true);
		template.attr("style","");
		var grid = $("[name=billPreOpenItem]:last");
		grid.after(template);
		rowDefination();
	}
	function removeGoods(goods){
		$(goods).closest("tr").remove();
	}
	function change(select,value){
		var optionData = $("option:selected",select).data("goodsInfo");
		var tr = $(select).closest("tr");
		tr.find("[name = income]").val("");
		tr.find("[name = goodsNum]").val("");
		tr.find("[name = sumAmt]").val("");
		tr.find("[name = taxAmt]").val("");
		tr.find("[name = goodsName]").val("");
		if(optionData==null||optionData == ""){
			tr.find("[name = specandmodel]").val("");
			tr.find("[name = goodsUnit]").val("");
			tr.find("[name = taxRate]").val("");
			tr.find("[name = goodsName]").val("");
			tr.find("[name = income]").attr("readonly","readonly");
			tr.find("[name = sumAmt]").attr("readonly","readonly");
			tr.find("[name = goodsNum]").attr("readonly","readonly");
		}else{
			tr.find("[name = specandmodel]").val(optionData.model);
			tr.find("[name = goodsUnit]").val(optionData.unit);
			tr.find("[name = taxRate]").val(optionData.taxRate);
			tr.find("[name = goodsName]").val(optionData.goodsName);
			tr.find("[name = income]").removeAttr("readonly");
			tr.find("[name = goodsNum]").removeAttr("readonly");
			tr.find("[name = sumAmt]").removeAttr("readonly");
		}
	}
</script>
</head>
<body onmousemove="MM(event)" onmouseout="MO(event)" onload="init()">
	<form name="Form1" method="post" action="" id="Form1">
		<input type="hidden" name="faPiaoType" value="<s:property value='faPiaoType'/>" />
		<input type="hidden" name="fromFlag" id="fromFlag" value="<s:property value='fromFlag'/>" />
		<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
			<tr>
				<td class="centercondition">
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" />
						<span class="current_status_menu">当前位置：</span>
						<span class="current_status_submenu1">销项税管理</span>
						<span class="current_status_submenu">开票管理</span>
						<span class="current_status_submenu">票据预开</span>
					</div>
					<div id="fade" class="black_overlay"></div>
					<div id="whitebox">
						<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0" >
							<tr>
								<td align="right" width="10%">客户纳税人识别号</td>
								<td align="left" width="25%">
									<input type="text" class="tbl_query_text2" name="customerTaxno" id="customerTaxno" value="<s:property value='customerTaxno'/>" size="100" />
								</td>
								<td align="left" width="10%">
									<input type="button" class="tbl_query_button" value="获取详细" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" name="etInfo" id="getInfo" onclick="submitForm('getCustomerInfo.action?flag=0');"/>
								</td>
								<td width="25%"></td>
								<td width="10%"></td>
								<td width="25%"></td>
							</tr>
						</tr>
						<tr>
							<td align="right">发票类型</td>
								<td align="left">
								
									<%--  <s:if test="billInfo.fapiaoType == 1"> 
										<s:select disabled="true" id="billInfo.fapiaoType" name="billInfo.fapiaoType" list="fapiaoTypeMap" listKey="key" listValue="value">
										</s:select>
										<s:hidden id="billInfo.fapiaoType" name="billInfo.fapiaoType" value="1"/>
									 </s:if> --%>
									
									<%-- <s:else> --%>
										<s:select id="billInfo.fapiaoType" name="billInfo.fapiaoType" list="fapiaoTypeMap" listKey="key" listValue="value">
										</s:select>
									<%-- </s:else> --%>
								</td>
								
							<td align="right">
								我方开票机构
							</td>
							<td >
								<s:if test="authInstList != null && authInstList.size > 0">
									<s:if test="billInfo.billId!=null&&billInfo.billId!=''">
										<s:select disabled="true" headerKey="" headerValue="请选择..." name="instCode" list="authInstList" 
											listKey='instId' listValue='instName' onchange="loadSelfInfo(this.value)"/>
										<s:hidden id="instCode" name="instCode"></s:hidden>
									</s:if>
									<s:else>
										<s:select headerKey="" headerValue="请选择..." name="instCode" list="authInstList" 
											listKey='instId' listValue='instName' onchange="loadSelfInfo(this.value)"/>
									</s:else>
								</s:if>
								<s:if test="authInstList == null || authInstList.size == 0">
									<select name="instCode" class="readOnlyText">
										<option value="">请分配机构权限</option>
									</select>
								</s:if>
							</td>
							<td></td>
							<td></td>
						</tr>
						<tr class="row1" style="height: 21px;">
							<td style="height: 21px;" align="right">
								客户纳税人名称
							</td>
							<td style="height: 21px;" align="left">
								<input type="text" style="height: 21px;" class="tbl_query_text_readonly" name="billInfo.customerName" id="billInfo.customerName" value="<s:property value='billInfo.customerName'/>" size="100" readonly class="readonly"/>
							</td>
							<td style="height: 21px;" align="right">
								我方纳税人名称
							</td>
							<td style="height: 21px;" align="left">
								<input type="text" style="height: 21px;" class="tbl_query_text_readonly" name="billInfo.name" id="billInfo.name" value="<s:property value='billInfo.name'/>" size="100" readonly class="readonly"/>
							</td>
							<td style="height: 21px;"></td>
							<td style="height: 21px;"></td>
						</tr>
						<tr style="height: 21px;" class="row1">
							<td style="height: 21px;" align="right">
								客户号
							</td>
							<td style="height: 21px;" align="left">
								<input style="height: 21px;" type="text" class="tbl_query_text_readonly" name="customerId" id="customerId" value="<s:property value='billInfo.customerId'/>" size="100" readonly class="readonly"/>
							</td>
							<td style="height: 21px;" align="right">
								我方纳税人识别号
							</td>
							<td style="height: 21px;" align="left">
								<input style="height: 21px;" type="text" class="tbl_query_text_readonly" name="billInfo.taxno" id="billInfo.taxno" value="<s:property value='billInfo.taxno'/>" size="100" readonly class="readonly"/>
							</td>
							<td style="height: 21px;"></td>
							<td style="height: 21px;"></td>
						</tr>
						<tr style="height: 21px;" class="row1">
							<td style="height: 21px;" align="right">
								客户地址电话
							</td>
							<td style="height: 21px;" align="left">
								<input type="text" style="height: 21px;" class="tbl_query_text_readonly" name="billInfo.customerAddressandphone" id="billInfo.customerAddressandphone" value="<s:property value='billInfo.customerAddressandphone'/>" size="100" readonly class="readonly"/>
							</td>
							<td style="height: 21px;" align="right">
								我方地址电话
							</td>
							<td style="height: 21px;" align="left">
								<input style="height: 21px;" type="text" class="tbl_query_text_readonly" name="billInfo.addressandphone" id="billInfo.addressandphone" value="<s:property value='billInfo.addressandphone'/>" size="100" readonly class="readonly"/>
							</td>
							<td style="height: 21px;"></td>
							<td style="height: 21px;"></td>
						</tr>
						<tr style="height: 21px;" class="row1">
							<td style="height: 21px;" align="right">
								客户银行账号
							</td>
							<td style="height: 21px;" align="left">
								<input style="height: 21px;" type="text" class="tbl_query_text_readonly" name="billInfo.customerBankandaccount" id="billInfo.customerBankandaccount" value="<s:property value='billInfo.customerBankandaccount'/>" size="100" readonly class="readonly"/>
							</td>
							<td style="height: 21px;" align="right">
								我方银行账号
							</td>
							<td style="height: 21px;" align="left">
								<input style="height: 21px;" type="text" class="tbl_query_text_readonly" name="billInfo.bankandaccount" id="billInfo.bankandaccount" value="<s:property value='billInfo.bankandaccount'/>" size="100" readonly class="readonly"/>
							</td>
							<td style="height: 21px;"></td>
							<td style="height: 21px;"></td>
						</tr style="height: 21px;">
						<tr style="height: 21px;" class="row1">
						<td style="height: 21px;" align="right">
							价税合计
						</td>
						<td style="height: 21px;">
							<input style="height: 21px;" type="text" class="tbl_query_text_readonly1" name="billInfo.sumAmtStr" id="billInfo.sumAmt" value="<s:property value='billInfo.sumAmt'/>" readonly class="readonly"/>
						</td>
						<td style="height: 21px;" align="right">
							合计收入
						</td>
						<td style="height: 21px;">
							<input style="height: 21px;" type="text" class="tbl_query_text_readonly1" name="billInfo.amtSumStr" id="billInfo.amtSum" value="<s:property value='billInfo.amtSum'/>" readonly class="readonly"/>
						</td>
						<td style="height: 21px;" align="right">
							合计税额
						</td>
						<td style="height: 21px;">
							<input style="height: 21px;" type="text" class="tbl_query_text_readonly1" name="billInfo.taxAmtSumStr" id="billInfo.taxAmtSum" value="<s:property value='billInfo.taxAmtSum'/>" readonly class="readonly"/>
						</td>
					</tr style="height: 21px;">
						<tr style="height: 21px;" class="row1">
							<td style="height: 21px;" align="right">
								收款人
							</td>
							<td style="height: 21px;">
								<input style="height: 21px;" type="text" maxlength="10" class="tbl_query_text" name="billInfo.payee" id="billInfo.payee" size="16" value="<s:property value='billInfo.payee'/>" />
							</td>
							<td style="height: 21px;" align="right">
								复核人
							</td>
							<td style="height: 21px;">
								<input style="height: 21px;" type="text" maxlength="10" class="tbl_query_text" name="billInfo.reviewer" id="billInfo.reviewer" size="16" value="<s:property value='billInfo.reviewer'/>"/>
							</td>
							<td style="height: 21px;" align="right">
								开票人
							</td>
							<td style="height: 21px;">
								<input style="height: 21px;" type="text" class="tbl_query_text_readonly1" name="billInfo.drawer" id="billInfo.drawer" value="<s:property value='billInfo.drawer'/>" readonly class="readonly"/>
							</td>
						</tr>
						<tr>
							<td align="right">
								备注
							</td>
							<td style="height: 21px;">
								<textarea cols="50" rows="1" name="billInfo.remark" id="billInfo.remark" 
									style="height: 21px;width: 252px;"><s:property value='billInfo.remark'/></textarea>
							</td>
						</tr>
						<tr class="row1">
							<td valign="top" colspan="6">
								<table id="tbl_tools" width="100%" border="0">
									<tr>
										<td align="left">
												<s:if test="billInfo.billId!=null&&billInfo.billId!=''">
									  			<a href="#" name="btSave" id="btSave" onClick="addGoods()">
								     		 		<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon16.png"/>新增
								      			</a>
								      		 	<a href="#"  id="bussIds">
								     	 			<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon16.png"/>交易流水号
								      			</a>
											</s:if>
										</td>
										<td width="80%"></td>
											<td>
												<s:if test="billInfo.billId == '' || billInfo.billId == null"><input type="button" class="tbl_query_button" value="保存" type=<s:if test="billInfo.billId != '' && billInfo.billId != null">"hidden"</s:if> onMouseMove="this.className='tbl_query_button_on'"
													onMouseOut="this.className='tbl_query_button'" name="btSave" id="btSave" onclick="save()"/>
												</s:if>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr class="row1">
								<td valign="top" colspan="6">
			 						<div style="width: 100%;height: 167px;overflow:scroll;" align="center">
										<table class="lessGrid" cellspacing="0" rules="all" border="0" cellpadding="0" display="none" id="billItemPreListYS" style="border-collapse: collapse; width: 99%;">
											<tr class="lessGrids">
												<th style="text-align: center;width: 15%;background-color: #D4D4D4;" >服务名称</th>
												<th style="text-align: center;width: 13%;" >规格型号</th>
												<th style="text-align: center;width: 7%;" >单位</th>
												<th style="text-align: center;width: 5%;" >数量</th>
												<th style="text-align: center;width: 13%;" >单价</th>
												<th style="text-align: center;width: 13%;" >金额</th>
												<th style="text-align: center;width: 5%;" >税率</th>
												<th style="text-align: center;width: 13%;" >税额</th>
												<th style="text-align: center;width: 11%;" >价税合计</th>
												<th style="text-align: center;width: 5%;" >删除</th>
											</tr>
											<input type="hidden" id="billInfoBillId" name="billInfo.billId" value="<s:property value='billInfo.billId'/>"> 
											<!--预置商品明细模板 start-->
											<tr name="billPreOpenItem" align="center" class="lessGrid rowA" style="display: none" >
												<td style='width: 15%;'>
													<select id="goodsNo" name="goodsFullName"  class="tbl_query_text5" onchange='change(this,this.value);'>  
														<option value='' >请选择...</option>
													</select>
													<span class='spanstar'>*</span>
												</td>
												<td style='width: 13%;'>
													<s:hidden name="goodsName" />
													<input type='text' name='specandmodel' class='tbl_query_text3' value="<s:property value="specandmodel"/>" style='text-align: left;' readonly="readonly"/>
												</td>
												<td style='width: 7%;'>
													<input type='text' name='goodsUnit' class='tbl_query_text6'  value="<s:property value="goodsUnit"/>"  style='text-align: left;' readonly="readonly" />
												</td>
												<td style='width: 5%;'>
													<input type='text' name='goodsNum' readonly="readonly" class='tbl_query_text4'  value="<s:property value="goodsNum"/>"  onkeyup='keyGoodsNum(this,this.value);' style='text-align: right;' />
												</td>
												<td style='width: 13%;'>
													<input type='text' name='goodsPrice'  class='tbl_query_text3' value="<s:property value="goodsPrice"/>"
													 style='text-align: right;' onkeyup='keyGoodsPrice(this,this.value);'/>     
												</td>
												<td style='width: 13%;'>
													<input type='text' name='income' readonly="readonly" class='tbl_query_text3'  value="<s:property value="income"/>" 
													style='text-align: right;' onkeyup='keyIncome(this,this.value);' />
												</td>
												<td style='width: 5%;'>
													<input type='text' name='taxRate' class='tbl_query_text4' value="<s:property value="taxRate"/>"  style='text-align: right;' onkeyup='keyTaxRate(this,this.value)'/>
												</td>
												
												<td style='width: 13%;'>
													<input type='text' name='taxAmt' readonly="readonly" class='tbl_query_text3'  value="<s:property value="taxAmt"/>"  style='text-align: right;' />
												</td>
												
												<td style='width: 11%'>
													<input type='text' name='sumAmt' readonly="readonly" class='tbl_query_text3'  value="<s:property value="sumAmt"/>" style='text-align: right;' onkeyup='keySumAmt(this,this.value);' />
												</td>
												
												<td  style='width: 5%;text-align: center'>
													<a onclick='removeGoods(this);'> <img src='<%=bopTheme2%>/img/jes/icon/delete.png' title='删除' style='border-width: 0px;' /></a>
												</td>
											</tr>
											<!--预置商品明细行 end-->
											<!-- 如果是预开修改页面，可用iterator循环，参考如下 -->
											<s:if test="billInfo.billId != '' && billInfo.billId != null ">
												<s:iterator value="billItemList" id="iList" status="stuts">
													<tr align="center" class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
														<!-- 此处可参考预制商品明细模板 -->
														<!-- 商品明细List获取可参考sqlmap-vms-billPreOpen中的id="findBillItemInfoForPre" -->
													</tr>
												</s:iterator>
											</s:if>
											<!-- 预开修改end -->
										</table>
									</div>
								</td>
							</tr>
						</table>
					</div>
					<table id="tbl_tools" width="100%" border="0">
						<tr>
							<td align="right">
								<div class="ctrlbuttonnew">
									<s:if test="billInfo.billId!=null&&billInfo.billId!=''">
									<input type="button" class="tbl_query_button" value="全部提交" <s:if test="billInfo.billId == '' || billInfo.billId == null">disabled</s:if> onMouseMove="this.className='tbl_query_button_on'"
										onMouseOut="this.className='tbl_query_button'" name="btSubmit" id="btSubmit" onclick="saveAll();"
									/>
									</s:if>
								</div>
							</td>
							<td align="right">
								<div class="ctrlbuttonnew">
									<input type="button" class="tbl_query_button" value="返回" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
										onclick="submitForm('billPreInvoice.action?type=back')">
								</div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<!-- 交易流水号弹框  -->
					<div id="light" class="white_content">
						<div >
							<table id="busIdsTable" class="lessGrid">
								<tr class="header">
									<th colspan="4">
										<span>发票交易流水号</span>
									</th>
								</tr>
								<s:if test="null!=trnasBusIds&&trnasBusIds.length>0">
									<s:iterator value="trnasBusIds" status="status" id="trnasBusId">
										<tr >
											<td style="width: 80%;text-align: right;">
												<s:textfield name="trnasBusIds" value="%{trnasBusId}" cssClass="tbl_query_text2"></s:textfield>
											</td>
											<td width="20%">
												<a title="追加" name="add" href="#">
													<img src="<c:out value="${bopTheme}"/>/themes/images/icons/add.png" alt="追加" />
												</a>
												<a title="删除" name="del" href="#">
													<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1020.png" alt="删除" />
												</a>
											</td>
										</tr>
									</s:iterator>
								</s:if>
							<s:else>
							<tr>
								<td style="width: 80%;text-align: right;">
									<s:textfield name="trnasBusIds"  cssClass="tbl_query_text2"></s:textfield>
								</td>
								<td width="20%">
								<a title="追加" name="add" href="#">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/add.png" alt="追加" />
								</a>
								<a title="删除" name="del" href="#">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1020.png" alt="删除" />
								</a>
							</td>
						</tr>
					</s:else>
				</table>
				<div style="padding-top: 20px">
					<input type="button" class="tbl_query_button" value="确定"	onclick="closeDiv()"/>
				</div>
			</div>
		</div>
	</form>
</body>
<script type="text/javascript">
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
	function keyGoodsPrice(t,value){
		var tr = $(t).closest("tr");
		var taxRate = tr.find("[name=taxRate]").val();
		if(Number(value)<=0){
			$(t).css("color","red");
			tr.find("[name=taxAmt]").val("");
			tr.find("[name=sumAmt]").val("");
			tr.find("[name=income]").val("");
		}else if(isNaN(Number(value))){
			$(t).css("color","red");
			tr.find("[name=taxAmt]").val("");
			tr.find("[name=sumAmt]").val("");
			tr.find("[name=income]").val("");
		}else{
			$(t).css("color","");
			
			var goodsNum = tr.find("[name=goodsNum]").val();
			if(!(goodsNum==null||goodsNum==""||Number(goodsNum)==0||isNaN(Number(goodsNum)))){
				
				var income=Number(value*goodsNum).toFixed(2);
				tr.find("[name=income]").val(income);
				tr.find("[name=income]").css("color","");
				
				var taxAmt = Number(value*goodsNum*(taxRate*1)).toFixed(2);
				tr.find("[name=taxAmt]").val(taxAmt);
				tr.find("[name=taxAmt]").css("color","");
				
				var sumAmt = Number(income*1+taxAmt*1).toFixed(2);
				tr.find("[name=sumAmt]").val(sumAmt);
				tr.find("[name=sumAmt]").css("color","");
				
				
			}
		}
	}
	
	
	
	function keyIncome(t,value){
		var tr = $(t).closest("tr");
		var taxRate = tr.find("[name=taxRate]").val();
		if(Number(value)<=0){
			$(t).css("color","red");
			tr.find("[name=taxAmt]").val("");
			tr.find("[name=sumAmt]").val("");
			tr.find("[name=goodsPrice]").val("");
		}else if(isNaN(Number(value))){
			$(t).css("color","red");
			tr.find("[name=taxAmt]").val("");
			tr.find("[name=sumAmt]").val("");
			tr.find("[name=goodsPrice]").val("");
		}else{
			$(t).css("color","");
			var taxAmt = Number(value*(taxRate*1)).toFixed(2);
			tr.find("[name=taxAmt]").val(taxAmt);
			tr.find("[name=taxAmt]").css("color","");
			var sumAmt = Number(value*1+taxAmt*1).toFixed(2);
			tr.find("[name=sumAmt]").val(sumAmt);
			tr.find("[name=sumAmt]").css("color","");
			var goodsNum = tr.find("[name=goodsNum]").val();
			if(!(goodsNum==null||goodsNum==""||Number(goodsNum)==0||isNaN(Number(goodsNum)))){
				var goodsPrice = Number(value/(goodsNum*1)).toFixed(2);
				tr.find("[name=goodsPrice]").val(goodsPrice);
				tr.find("[name=goodsPrice]").css("color","");
			}
		}
	}
	function keyGoodsNum(t,value){
		var tr = $(t).closest("tr");
		var income = tr.find("[name=income]").val(); 
		if(Number(value)<=0){
			$(t).css("color","red");
			tr.find("[name=goodsPrice]").val("");
			
		}else if(isNaN(Number(value))){
			$(t).css("color","red");
			tr.find("[name=goodsPrice]").val("");
		}else{
			$(t).css("color","");
			var goodsPrice = Number(income/(value*1)).toFixed(2);
			tr.find("[name=goodsPrice]").val(goodsPrice);
			tr.find("[name=goodsPrice]").css("color","");
		}
	}
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
	}
	var submitFlag = false;
	function saveAll(){
		var goodsInfoList = $("[name = billPreOpenItem]");
		var goodsNoList = goodsInfoList.find("[name = goodsFullName] option:selected");
		for(var i = 1; i < goodsNoList.size(); i++){
			var goodsNoInfo = $(goodsNoList[i]);
			var goodsNo = goodsNoInfo.val();
			var matchCount = $("[name = goodsFullName] option:selected[value="+goodsNo+"]").size();
			if(matchCount > 1){
				alert("商品重复");
				return;
			}
			var goodsInfo = $(goodsNoInfo).closest("tr");
			var inputList = goodsInfo.find("input");
			for(var i = 0; i < inputList.size(); i++){
				var input = $(inputList[i]).val();
				var css = $(inputList[i]).css("color");
				var name = $(inputList[i])[0].name;
				if(name!="specandmodel"&&name!="goodsUnit"){
					var input = $(inputList[i]).val();
					var css = $(inputList[i]).css("color");
					if(input==null||input==""||css == "red"){
					alert("请输入完整并正确的商品信息");
					return;
					}
				}
			}
		}
		if(!submitFlag){
			submitFlag = true;
			$("form").attr("action","saveGoodsList.action").submit();		
		}else{
			alert("数据提交中，请稍后...");
		}
	}
	function showDiv() {
		document.getElementById('light').style.display = 'block';
		document.getElementById('fade').style.display = 'block';
	}
	function closeDiv() {
		document.getElementById('light').style.display = 'none';
		document.getElementById('fade').style.display = 'none';
	}
	$(function(){
		//打开交易流水号画面
		$("#bussIds").click(function(){
			showDiv();
		});
		//交易流水号画面增加按钮
		$("#busIdsTable [name='add']").live("click",function(){
			var varTr = $(this).closest("tr").clone();
			varTr.insertAfter($(this).closest("tr"));
			$("[name='trnasBusIds']",varTr).val("");
		});
		//交易流水号画面删除按钮
		$("#busIdsTable [name='del']").live("click",function(){
			if($("#busIdsTable [name='del']").size()==1){
				alert("已经是最后一条");
				return false;
			}
			var varTr = $(this).closest("tr").remove();
		});
	})
</script>
</html>