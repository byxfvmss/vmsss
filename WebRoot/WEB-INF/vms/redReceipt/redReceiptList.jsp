<!--file: <%=request.getRequestURI()%> -->
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<title>增值税管理平台</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/calendar_notime.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/XmlHttp.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/search.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/ylb.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/lhb.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/search.js"></script>
<style>
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
	opacity: .80;
	filter: alpha(opacity = 80);
}

.white_content {
	display: none;
	position: absolute;
	top: 25%;
	left: 25%;
	width: 40%;
	height: 40%;
	padding: 16px;
	background-color: white;
	z-index: 1002;
	overflow: auto;
}
</style>
<script type="text/javascript">
	// [查询]按钮
	function search() {
		//document.forms[0].submit();
		submitAction(document.forms[0], "listRedReceipt.action");
		document.forms[0].action = "listRedReceipt.action";
	}
	function exportExcel() {
		submitAction(document.forms[0],
				"redReceiptBillToExcel.action?type=redReceiptExcel");
		document.forms[0].action = "listRedReceipt.action";
	}
	// 红冲按钮
	function cancel(result) {
		if (checkChkBoxesSelected("selectBillIds")) {
			var billIds = document.Form1.selectBillIds;
			var billDates = document.Form1.selectBillDates;
			var canCancel = true;

			var chkBoexes = document.getElementsByName("selectBillIds");
			var j = 0;
			var billId = "";
			for (i = 0; i < chkBoexes.length; i++) {
				if (chkBoexes[i].checked) {
					j++;
					billId = chkBoexes[i].value;
				}
				if (j > 1) {
					alert("请选择单条记录进行操作！");
					return false;
				}
			}

			if (result == "18") {
				if (!confirm("确定将选中票据进行红冲处理？")) {
					return false;
				}
				showDiv();
				//window.open("redReceiptReleaseTrans.action?billId="+billId+"&result=18",'newwindow',screen.width * 0.8,screen.height * 0.8);
			} else {
				submitAction(document.forms[0],
						"cancelRedReceipt.action?billId=" + billId);
				document.forms[0].action = "listRedReceipt.action";
			}
		} else {
			alert("请选择交易记录！");
		}
	}
	function commit() {
		var notic = document.getElementById("notic").value;

		var reg = /^\d{16}$/;
		//专票必须要通知单号  普票可为空
		if (!reg.test(notic)) {
			var checkedFapiaoType = $("[name=selectBillIds]:checked").closest(
					"tr").find("[name=fapiaoType]").val();
			//专票提示
			if (0 == checkedFapiaoType) {
				alert("请输入16位数字通知单号");
				return false;
			}

		}
		var chkBoexes = document.getElementsByName("selectBillIds");
		var billId = "";
		for (i = 0; i < chkBoexes.length; i++) {
			if (chkBoexes[i].checked) {
				billId = chkBoexes[i].value;
			}
		}
		document.getElementById('light').style.display = 'none';
		document.getElementById('fade').style.display = 'none';
		submitAction(document.forms[0], "redReceiptReleaseTrans.action?billId="
				+ billId + "&result=22&notic=" + notic);
		//document.forms[0].action = "listRedReceipt.action";

	}
	function showDiv() {
		document.getElementById('light').style.display = 'block';
		document.getElementById('fade').style.display = 'block';
	}
	function closeDiv() {
		document.getElementById('light').style.display = 'none';
		document.getElementById('fade').style.display = 'none';
	}
	var msg = '<s:property value="message" escape="false"/>';
	if (msg != null && msg != '') {
		alert(msg);
	}

	function exportWord() {
		if (checkChkBoxesSelected("selectBillIds")) {
			var billIds = document.Form1.selectBillIds;
			var billDates = document.Form1.selectBillDates;
			var canCancel = true;

			var chkBoexes = document.getElementsByName("selectBillIds");
			var j = 0;
			var billId = "";

			for (i = 0; i < chkBoexes.length; i++) {
				if (chkBoexes[i].checked) {
					j++;
					billId = chkBoexes[i].value;
				}
				if (j > 1) {
					alert("请选择单条记录进行提交！");
					return false;
				}

			}
			var ticket = document.getElementById("bill" + billId).value;
			if (ticket == "0") {
				//alert(billId);
				submitAction(document.forms[0], "exportToWord.action?billId="
						+ billId);
				document.forms[0].action = "listRedReceipt.action";
			} else {
				alert("您所选票据为普票,没有申请信息！");
				return false;
			}

		} else {
			alert("请选择要导出的记录！");
		}
	}
</script>
</head>
<body onkeydow="enterkey(event)">
	<form name="Form1" method="post" action="listRedReceipt.action" id="Form1">
		<div id="light" class="white_content">
			<div style="margin: 50px 25px 50px 150px;">
				<table>
					<tr>
						<td>红字通知单号:</td>
						<td align="left">
							<input style="width: 200px;" class="tbl_query_text" type="text" id="notic" name="notic" maxlength="16">
						</td>
					</tr>
				</table>
				<br />
				<br />
				<input type="button" class="tbl_query_button" value="确定" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" name="BtnView" id="BtnView"
					onclick="commit()"
				/>
				<input type="button" class="tbl_query_button" value="取消" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" name="BtnView" id="BtnView"
					onclick="closeDiv()"
				/>
			</div>
		</div>
		<div id="fade" class="black_overlay"></div>
		<input type="hidden" name="submitFlag" id="submitFlag" value="" />
		<input type="hidden" name="currMonth" id="currMonth" value="<s:property value='currMonth'/>" />
		<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
			<tr>
				<td class="centercondition">
					<s:component template="rocketMessage" />
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" />
						<span class="current_status_menu">当前位置：</span>
						<span class="current_status_submenu1">销项税管理</span>
						<span class="current_status_submenu">红冲管理</span>
						<span class="current_status_submenu">发票红冲</span>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" width="80%" border="0">
							<tr>
								<td align="left">申请开票日期</td>
								<td>
									<input class="tbl_query_time" id="billApplyBeginDate" type="text" name="redReceiptApplyInfo.billApplyBeginDate" value="<s:property value='redReceiptApplyInfo.billApplyBeginDate'/>"
										onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'billApplyEndDate\')}'})" size='11'
									"/>
									--
									<input class="tbl_query_time" id="billApplyEndDate" type="text" name="redReceiptApplyInfo.billApplyEndDate" value="<s:property value='redReceiptApplyInfo.billApplyEndDate'/>"
										onfocus="WdatePicker({minDate:'#F{$dp.$D(\'billApplyBeginDate\')}'})" size='11'
									"/>
								</td>
								<td align="left">客户纳税人名称</td>
								<td>
									<input type="text" class="tbl_query_text" id="customerName" name="redReceiptApplyInfo.customerName" value="<s:property value='redReceiptApplyInfo.customerName'/>" />
								</td>
								<td align="left">发票类型</td>
								<td>
									<select id="fapiaoType" name="redReceiptApplyInfo.fapiaoType">
										<option value="">全部</option>
										<option value="0" <s:if test='redReceiptApplyInfo.fapiaoType=="0"'>selected</s:if> <s:else></s:else>>增值税专用发票</option>
										<option value="1" <s:if test='redReceiptApplyInfo.fapiaoType=="1"'>selected</s:if> <s:else></s:else>>增值税普通发票</option>
									</select>
								</td>
								<td style="width:80px;" align="right">
									<input type="button" class="tbl_query_button" value="查询" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" name="BtnView" id="BtnView"
										onclick="search()"
									/>
								</td>
							</tr>
						</table>
					</div>
					<table id="tbl_tools" width="100%" border="0">
						<tr>
							<td align="left">
								<!-- <input type="button" class="tbl_query_button" value="红冲"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="BtnView" id="BtnView" onclick="cancel(18)" />
				<input type="button" class="tbl_query_button" value="导出"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="BtnView" id="BtnView" onclick="exportExcel()" />
				<input type="button" class="tbl_query_button" value="撤销"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="BtnView" id="BtnView" onclick="cancel(16)" /> -->
								<a href="#" onclick="cancel(18)">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1033.png" />
									红冲
								</a>
								<a href="#" onclick="exportExcel()">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />
									导出
								</a>
								<a href="#" onclick="cancel(16)">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1032.png" />
									撤销
								</a>
								<a href="#" onclick="exportWord()">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1034.png" />
									导出申请WORD表
								</a>
								<a href="#" onclick="commit1()">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1035.png" />
									导出申请XML表
								</a>
							</td>
						</tr>
					</table>
					<div id="lessGridList1" style="overflow: auto; width: 100%;">
						<table class="lessGrid" cellspacing="0" rules="all" border="0" cellpadding="0" style="border-collapse: collapse; width: 100%;">
							<tr class="lessGrid head">
								<th style="text-align:center">
									<input style="width:13px;height:13px;" id="CheckAll" type="checkbox" onclick="cbxselectall(this,'selectBillIds')" />
								</th>
								<th style="text-align:center">序号</th>
<!-- 								metlife begin -->
								<th style="text-align: center">投保单号</th>
								<th style="text-align: center">保单号</th>
								<th style="text-align: center">保全受理号</th>
<!-- 								metlife end -->
								<th style="text-align:center">申请开票日期</th>
								<th style="text-align:center">客户纳税人名称</th>
								<th style="text-align:center">合计金额</th>
								<th style="text-align:center">合计税额</th>
								<th style="text-align:center">价税合计</th>
								<th style="text-align:center">发票类型</th>
								<th style="text-align:center">状态</th>
<!-- 								metlife begin -->
								<th style="text-align: center">渠道</th>
								<th style="text-align: center">费用类型</th>
								<th style="text-align: center">承保日期</th>
								<th style="text-align: center">缴费频率</th>
								<th style="text-align: center">数据来源</th>
<!-- 								metlift end -->
							</tr>
							<s:iterator value="paginationList.recordList" id="iList" status="stuts">
								<tr align="center" class="<s:if test='#stuts.odd'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
									<td align="center">
										<input style="width:13px;height:13px;" type="checkbox" name="selectBillIds" value="<s:property value="billId"/>" />
										<s:hidden name="selectBillDates" value="%{billDate}"></s:hidden>
									</td>
									<td align="center">
										<s:property value="#stuts.index+1" />
									</td>
<!-- 									metlife  begin -->
									<td align="center">
										<s:property value="ttmprcno" />
									</td>
									<td align="center">
										<s:property value="insureId" />
									</td>
									<td align="center">
										<s:property value="repnum" />
									</td>
<!-- 									metlife end -->
									<td align="center">
										<s:property value="applyDate" />
									</td>
									<td align="center">
										<s:property value="customerName" />
									</td>
									<td align="right">
										<fmt:formatNumber value="${amtSum}" pattern="#,##0.0"></fmt:formatNumber>
									</td>
									<td align="right">
										<fmt:formatNumber value="${taxAmtSum}" pattern="#,##0.0"></fmt:formatNumber>
									</td>
									<td align="right">
										<fmt:formatNumber value="${sumAmt}" pattern="#,##0.0"></fmt:formatNumber>
									</td>
									<td align="center">
										<s:property value="@com.cjit.vms.trans.util.DataUtil@getFapiaoTypeCH(fapiaoType)" />
										<s:hidden id="bill%{billId}" name="fapiaoType"></s:hidden>
									</td>
									<td align="center">
										<s:property value="@com.cjit.vms.trans.util.DataUtil@getDataStatusCH(datastatus,'BILL')" />
									</td>
									<!-- 									metlife begin -->
									<td align="center">
										<s:property value="channelch" />
									</td>
									<td align="center">
										<s:property value="feeTypCh" />
									</td>
									<td align="center">
										<s:property value="hissDte" />
									</td>
									<td align="center">
										<s:property value="billFreq" />
									</td>
									<td align="center">
										<s:property value="dSource" />
									</td>
<!-- 									metlife  end -->
								</tr>
							</s:iterator>
							</tr>
						</table>
					</div>
					<div id="anpBoud" align="Right" style="overflow:auto;width:100%;">
						<table width="100%" cellspacing="0" border="0">
							<tr>
								<td align="right">
									<s:component template="pagediv" />
								</td>
							</tr>
						</table>
					</div>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>