<!--file: <%=request.getRequestURI()%> -->
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<title>增值税管理平台</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
<script type="text/javascript">
	// [开具]按钮
	function opener() {
		if (checkChkBoxesSelected("selectBillIds")) {
			var billIds = document.Form1.selectBillIds;
			var billDates = document.Form1.selectBillDates;
			var canCancel = true;

			var chkBoexes = document.getElementsByName("selectBillIds");
			var j = 0;
			var ids = "";
			for (i = 0; i < chkBoexes.length; i++) {
				if (chkBoexes[i].checked) {
					ids += "," + chkBoexes[i].value;
				}
			}
			if (!confirm("确定将选中票据进行开具处理？")) {
				return false;
			}
			submitAction(document.forms[0],
					"redReceiptReleaseTrans.action?ids=" + ids + "&result=18");
		} else {
			alert("请选择交易记录！");
		}
	}

	function checkReverseTrans() {
		var reverseTransIdList = $("[name=selectBillIds]").closest("tr").find(
				".reverseTransId");
		var result = true;
		reverseTransIdList.each(function() {
			var isReversed = $(this).html();
			isReversed = isReversed.replace(/^(\s)*|(\s)*$/g, "");
			if (isReversed == "是") {
				var isNotChecked = $(this).closest("tr").find(
						"[name=selectBillIds]:checked").size() == 0 ? true
						: false;
				if (isNotChecked) {
					result = false;
					alert("冲账交易必须选择");
					return false;
				}

			}
		})
		return result;

	}
	function commit() {
		var checkedList = $("[name=selectBillIds]:checked");
		if (checkedList.size() == 0) {
			alert("请选择数据");
			return false;
		}

		if (!checkReverseTrans()) {
			return false;
		}

		var checkedRadioSize = $("[name=specialTicket.level1Option]:checked").size();
		var fapiaoTypeHD = $("#fapiaoTypeHD").val();
		if("0"==fapiaoTypeHD&&0==checkedRadioSize){
			alert("请选择发票状态");
			return false;
		}
		var billId = document.getElementById("billId").value;
		$("#BtnView").attr("disabled", "disabled");
		submitAction(document.forms[0], "redReceiptApply.action");
	}
	function cancel() {
		self.location = 'listRedReceiptApply.action?back=back';
		//window.history.back(-1);
	}
	function allIn() {
		var billId = document.getElementById("billId").value;
		var flag = document.getElementById("flag").value;
		submitAction(document.forms[0], "billInfoAndTransList.action?billId="
				+ billId + "&type=all&ticket=" + flag + "&fromFlag=first");
	}
	function add() {
		var billId = document.getElementById("billId").value;
		var flag = document.getElementById("flag").value;
		submitAction(document.forms[0], "redReceiptTransList.action?billId="
				+ billId + "&ticket=" + flag + "&fromFlag=first");
	}
	function del() {
		if (checkChkBoxesSelected("selectBillIds")) {
			var billIds = document.Form1.selectBillIds;
			var billDates = document.Form1.selectBillDates;
			var canCancel = true;
			var flag = document.getElementById("flag").value;
			var billId = document.getElementById("billId").value;
			var chkBoexes = document.getElementsByName("selectBillIds");
			var j = 0;
			var ids = "";
			for (i = 0; i < chkBoexes.length; i++) {
				if (chkBoexes[i].checked) {
					ids += "," + chkBoexes[i].value;
					j++;
				}
			}
			if (!confirm("确定删除选中的" + j + "笔数据")) {
				return false;
			}
			submitAction(document.forms[0], "billInfoAndTransList.action?ids="
					+ ids + "&billId=" + billId + "&type=del&ticket=" + flag);
		}
	}

	function pageEvent() {
		var billId = document.getElementById("billId").value;
		var flag = document.getElementById("flag").value;
		submitAction(document.forms[0], "billInfoAndTransList.action?billId="
				+ billId + "&type=all&ticket=" + flag);
	}
</script>
<style type="text/css">
.style1 {
	width: 242px;
}

#list1 {
	padding: 0 5 0 5;
}

#list1 span {
	line-height: 35px;
	font-weight: bold;
	color: #252525;
	white-space: nowrap;
	cursor: default;
}

.lessGridListPri {
	padding: 0 7px 0 7px;
	margin-bottom: 20px; cursor : default;
	border: #DDDDDD solid 1px;
	background: #FFF;
	overflow: auto;
	width: 98%;
	cursor: default;
}
</style>
</head>
<body onkeydow="enterkey(event)">
	<!-- <form name="Form1" id="Form1" method="post" action="pageEvent()" > -->
	<form name="Form1" id="Form1" method="post" action="billInfoAndTransList.action">
		<input type="hidden" id="type" name="type" value="<s:property value='#request.type' />" />
		<input type="hidden" id="ids" name="ids" value="<s:property value='#request.ids' />" />
		<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
			<tr>
				<td class="centercondition">
					<s:component template="rocketMessage" />
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" />
						<span class="current_status_menu">当前位置：</span>
						<span class="current_status_submenu1">销项税管理</span>
						<span class="current_status_submenu">红冲管理</span>
						<span class="current_status_submenu">红冲申请</span>
						<span class="current_status_submenu">申请红冲</span>
					</div>
					</br>
					<div style="padding: 0 0 20px 0;">
						<div class="lessGridListPri">
							<table width="100%" class="lessGrid" cellspacing="0" rules="all" border="0" cellpadding="0" display="none" style="border-collapse: collapse;">
								<tr class="lessGrid head">
									<th style="text-align: center">开票日期</th>
									<th style="text-align: center">客户名称</th>
									<s:if test='configCustomerFlag.equals("KBC")'>
									<th style="text-align: center">客户号</th>
									</s:if>
									<th style="text-align: center">客户纳税人识别号</th>
									<!-- <th style="text-align: center">商品名称</th> -->
									<th style="text-align: center">合计金额</th>
									<th style="text-align: center">合计税额</th>
									<th style="text-align: center">价税合计</th>
									<th style="text-align: center">发票类型</th>
									<th style="text-align: center">状态</th>
								</tr>
								<tr class="lessGrid rowA">
									<input type="hidden" value="<s:property value="redReceiptApplyInfo.billId"/>" id="billId" name="billId" />
									<td align="center">
										<s:property value="redReceiptApplyInfo.billDate" />
									</td>
									<td align="center">
										<s:property value="redReceiptApplyInfo.customerName" />
									</td>
									<s:if test='configCustomerFlag.equals("KBC")'>
									<td align="center">
										<s:property value="redReceiptApplyInfo.customerId" />
									</td>
									</s:if>
									<td align="center">
										<s:property value="redReceiptApplyInfo.customerTaxno" />
									</td>
									<%-- <td align="center">
									<s:property value="redReceiptApplyInfo.goodsName" />
								</td> --%>
									<td align="center">
										<s:property value="redReceiptApplyInfo.amtSum" />
									</td>
									<td align="center">
										<s:property value="redReceiptApplyInfo.taxAmtSum" />
									</td>
									<td align="center">
										<s:property value="redReceiptApplyInfo.sumAmt" />
									</td>
									<td align="center">
										<%-- <s:if test="redReceiptApplyInfo.fapiaoType == 0">
										增值税专用发票
									</s:if>
									<s:else>
										增值税普通发票
									</s:else> --%>
										<s:hidden id="fapiaoTypeHD" name="redReceiptApplyInfo.fapiaoType" disabled="disabled"></s:hidden>
										<s:property value="@com.cjit.vms.trans.util.DataUtil@getFapiaoTypeCH(redReceiptApplyInfo.fapiaoType)" />
									</td>
									<td align="center">
										<%-- 		<s:if test="redReceiptApplyInfo.datastatus == 8">
							已打印
							</s:if>
									<s:elseif test="redReceiptApplyInfo.datastatus == 12">
							已抄报
							</s:elseif>
									<s:else>
							已开具
							</s:else> --%>
										<s:property value="@com.cjit.vms.trans.util.DataUtil@getDataStatusCH(redReceiptApplyInfo.datastatus,'BILL')" />
									</td>
								</tr>
							</table>
						</div>
						<!-- <div id="anpBoud" align="Right" style="overflow: auto; width: 100%; padding: 30px 0 0 0;">
							<table width="100%" cellspacing="0" border="0">
								<tr>
									<input type="button" class="tbl_query_button" value="全部红冲" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" name="BtnView" id="BtnView"
										onclick="allIn()"
									/>
									<input type="button" class="tbl_query_button" value="添加" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" name="BtnView" id="BtnView"
										onclick="add()"
									/>
									<input type="button" class="tbl_query_button" value="删除" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" name="BtnView" id="BtnView"
										onclick="del()"
									/>
								</tr>
								</div>
							</table>
						</div> -->
						<!-- <div style="overflow: auto; width: 100%;" id="list1">
				<table id="lessGridList" width="100%" class="lessGrid"
					cellspacing="0" rules="all" border="0" cellpadding="0"
					display="none" style="border-collapse: collapse;"> -->
						<div id="lessGridList5" style="overflow: auto; width: 100%;height: 300px;">
							<table class="lessGrid" cellspacing="0" rules="all" border="0" cellpadding="0" style="border-collapse: collapse; width: 100%;">
								<tr class="lessGrid head">
									<th style="text-align: center">
										<input style="width: 13px; height: 13px;" id="CheckAll" type="checkbox" onclick="cbxselectall(this,'selectBillIds')" />
									</th>
									<th style="text-align: center">序号</th>
									<th style="text-align: center">交易时间</th>
									<th style="text-align: center">客户名称</th>
									<s:if test='configCustomerFlag.equals("KBC")'>
									<th style="text-align: center">客户号</th>
									</s:if>
									<!-- <th style="text-align: center">客户纳税人识别号</th> -->
									<th style="text-align: center">客户账号</th>
									<th style="text-align: center">交易类型</th>
									<th style="text-align: center">收入</th>
									<th style="text-align: center">税率</th>
									<th style="text-align: center">税额</th>
									<th style="text-align: center">价税合计</th>
									<th style="text-align: center">交易发生机构</th>
									<th style="text-align: center">是否冲账</th>
									<th style="text-align: center">冲账金额</th>
									<th style="text-align: center">发票类型</th>
									<th style="text-align: center">交易标志</th>
									<th style="text-align: center">是否打票</th>
									<th style="text-align: center">状态</th>
								</tr>
								<s:iterator value="paginationList.recordList" id="iList" status="stuts">
									<tr align="center" class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
										<td align="center" style="width:30px;">
											<input type="checkbox" name="selectBillIds" value="<s:property value="transId"/>" />
										</td>
										<td>
											<s:property value="#stuts.index+1" />
										</td>
										<td>
											<s:property value="transDate" />
										</td>
										<td>
											<s:property value="customerName" />
										</td>
										<s:if test='configCustomerFlag.equals("KBC")'>
										<td>
											<s:property value="customerId" />
										</td>
										</s:if>
										<%-- <td>
											<s:property value="customerTaxno" />
										</td> --%>
										<td>
											<s:property value="customerAccount" />
										</td>
										<td>
											<s:property value="transType" />
										</td>
										<td>
											<fmt:formatNumber value="${amtCny}" pattern="#,##0.00"></fmt:formatNumber>
										</td>
										<td>
											<fmt:formatNumber value="${taxRate}" pattern="#,##0.00"></fmt:formatNumber>
										</td>
										<td>
											<fmt:formatNumber value="${taxAmtCny}" pattern="#,##0.00"></fmt:formatNumber>
										</td>
										<td>
											<!--EL相加没了小数  所以使用ognl 相加再用EL格式化 -->
											<s:set value="amtCny+taxAmtCny" var="amtCnyPage"></s:set>
											<fmt:formatNumber value="${amtCnyPage}" maxFractionDigits="2" pattern="#,##0.00"></fmt:formatNumber>
										</td>
										<td>
											<s:property value="bankCode" />
										</td>
										<td>
											<span class="reverseTransId">
												<s:property value="reverseTransId==null?'否':'是'" />
											</span>
										</td>
										<td>
											<s:set value="reverseAmtCny==null?0:reverseAmtCny" id="reverseAmtCnyPage"></s:set>
											<fmt:formatNumber value="${reverseAmtCnyPage}" pattern="#,##0.00"></fmt:formatNumber>
										</td>
										<td>
											
											<s:property value="@com.cjit.vms.trans.util.DataUtil@getFapiaoTypeCH(fapiaoType)" />
										</td>
										<td>
											<s:property value="@com.cjit.vms.trans.util.DataUtil@getTransFlag(transFlag)" />
										</td>
										<td>
											<s:property value="@com.cjit.vms.trans.util.DataUtil@getIsHandiworkCH(isHandiwork)" />
										</td>
										<td>
											<s:property value="@com.cjit.vms.trans.util.DataUtil@getDataStatusCH(datastatus,'TRANS')" />
										</td>
									</tr>
								</s:iterator>
							</table>
							
						</div>
						<input type="hidden" value="<s:property value="flag"/>" id="flag" name="flag" />
						<div id="anpBoud" align="Right" style="overflow: auto; width: 100%;">
							<table width="100%" cellspacing="0" border="0">
								<tr>
									<td align="right">
										<s:component template="pagediv" />
									</td>
								</tr>
							</table>
						</div>
						<s:if test="flag == 0">
							<div style="overflow: auto; width: 100%; padding: 0px 0 10px 0;" id="list1">
								<table width="100%" cellspacing="0" border="0">
									<tr>
										<td>
											<s:if test="specialTicket.level1Option == 0">
												<input type="radio" value="1" name="specialTicket.level1Option" checked="checked" />
												<span>购买方拒收发票 </span>
											</s:if>
											<s:else>
												<input type="radio" value="1" name="specialTicket.level1Option" />
												<span>购买方拒收发票</span>
											</s:else>
											<s:if test="specialTicket.level1Option == 1">
												<input type="radio" value="2" name="specialTicket.level1Option" checked="checked" />
												<span>发票尚未交付</span>
											</s:if>
											<s:else>
												<input type="radio" value="2" name="specialTicket.level1Option" />
												<span>发票尚未交付</span>
											</s:else>
										</td>
									</tr>
								</table>
							</div>
						</s:if>
						<table id="tbl_tools" width="100%" border="0">
							<tr>
								<td align="left">
									<input type="button"   class="tbl_query_button" value="提交" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" name="BtnView" id="BtnView"
										onclick="commit()"
									/>
									<input type="button" class="tbl_query_button" value="返回" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" name="BtnView" id="BtnView"
										onclick="cancel()"
									/>
								</td>
							</tr>
						</table>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>