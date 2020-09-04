<!--file: <%=request.getRequestURI()%> -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<title>增值税管理平台</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
<link href="<%=bopTheme2%>/css/editView.css" type="text/css" rel="stylesheet">
<style type="text/css">
.tdHead {
	text-align: right;
	background-color: #F0F0F0;
	font-weight: bold;
	color: #727375;
	font-weight: bold;
}

.tdValue {
	text-align: left;
}

#tbl_context td span {
	padding: 5px;
}
</style>
<script language="javascript" type="text/javascript">
	$(function() {

		$("#savaBtn").click(function() {

			if (!checkForm()) {
				return false;
			}
			submitForm("saveBillExpress.action");
		});

		function checkForm() {

			if ("0" == $("#receiveType").val()) {
				if (0 == $("#customerAddressId option").size()) {
					alert("请先维护客户收件地址");
					return false;
				}
			} else {
				if (0 == $("#customerReceiverId option").size()) {
					alert("请先维护客户收件人");
					return false;
				}
			}

			return true;
		}
		$("#tbl_context tr:odd").attr("class", "row1");
		$("#tbl_context tr:even").attr("class", "row1");
		$("#tbl_context td:even").attr("class", "tdHead");
		$("#tbl_context td:odd").attr("class", "tdValue");

		$("#receiveType").change(function() {
			initView();
			initStatusOption();
		})

		var addressList;
		var receiverList;
		var receiveStatusList;
		function loadAddressList() {
			var customerId = $("#customerId").val();
			var url = "listCustomerAddressAJAX.action";
			url += "?customerId=" + customerId;
			url += "&X=" + (new Date()).getTime();

			$
					.ajax({
						type : "Post",
						url : url,
						async : false,
						dataType : "json", //可以是text，如果用text，返回的结果为字符串；如果需要json格式的，可是设置为json
						success : function(data) {
							addressList = data;
							var customerAddressId = $("#customerAddressId");
							var customerAddressIdVal = $(
									"#customerAddressIdVal").val();
							$(data).each(
									function() {
										var optionStr = "<option value='"
												+ this.id + "'";
										if (customerAddressIdVal == this.id) {
											optionStr += " selected=selected "
										}
										optionStr += ">" + this.addressTag
												+ "</option>"
										var option = $(optionStr);
										option.data("address", this)
										customerAddressId.append(option);
									});
						},
						error : function(msg) {
							alert(" 数据加载失败！" + msg);
						}
					});
		}

		function loadReceiverList() {
			var customerId = $("#customerId").val();
			var url = "listCustomerReceiverAJAX.action";
			url += "?customerId=" + customerId;
			url += "&X=" + (new Date()).getTime();
			$.ajax({
				type : "Post",
				url : url,
				async : false,
				dataType : "json", //可以是text，如果用text，返回的结果为字符串；如果需要json格式的，可是设置为json
				success : function(data) {
					receiverList = data;
				},
				error : function(msg) {
					alert(" 数据加载失败！" + msg);
				}
			});
		}

		function loadReceiveStatusList() {
			var customerId = $("#customerId").val();
			var url = "listReceiveStatusAJAX.action";
			url += "?X=" + (new Date()).getTime();
			$.ajax({
				type : "Post",
				url : url,
				async : false,
				dataType : "json", //可以是text，如果用text，返回的结果为字符串；如果需要json格式的，可是设置为json
				success : function(data) {
					receiveStatusList = data;
				},
				error : function(msg) {
					alert(" 数据加载失败！" + msg);
				}
			});
		}

		/* ajax加载地址和联系人 */
		loadAddressList();
		loadReceiverList();
		loadReceiveStatusList();
		/* 地址选择时加载地址详细信息 */
		$("#customerAddressId").click(function() {
			loadAddressOptionDetail();
		});
		function loadAddressOptionDetail() {
			var address = $("#customerAddressId option:selected").data(
					"address");
			if (null != address) {
				$("#contactPerson").html(address.contactPerson);
				$("#contactPhone").html(address.contactPhone);
				$("#contactEmail").html(address.contactEmail);
				$("#receiver").html(address.receiver);
				$("#receiverPhone").html(address.receiverPhone);
				$("#receiverAddress").html(address.receiverAddress);
				$("#postCode").html(address.postCode);
				$("#addressRemark").html(address.addressRemark);
			}

		}
		/* 收件人改变时加载详细信息 */
		$("#customerReceiverId").click(function() {
			loadReceiverOptionDtail();
		});

		function loadReceiverOptionDtail() {
			var receiver = $("#customerReceiverId option:selected").data(
					"receiver");
			if (null != receiver) {
				$("#documentsTypeName").html(receiver.documentsTypeName);
				$("#documentsCode").html(receiver.documentsCode);
				$("#receiverName").html(receiver.receiverName);
				$("#receiverRemark").html(receiver.remark);
			}

		}
		/* 收件类型改变  实现 */
		function initView() {
			initStatusOption();
			var receiveType = $("#receiveType option:selected").val();
			if ("0" == receiveType) {
				//加载详细信息
				loadAddressOptionDetail();
				$("#addressDiv").show(1000, "swing");
				$("#receiverDiv").hide(1000, "swing");

			} else {
				/* 重构收件人option */
				initReceiverOption(receiveType);
				//加载详细信息
				loadReceiverOptionDtail();
				$("#addressDiv").hide(1000, "swing");
				$("#receiverDiv").show(1000, "swing");
			}

		}

		function initStatusOption() {
			var receiveType = $("#receiveType option:selected").val();
			var receiveStatus = $("#receiveStatus");
			receiveStatus.empty();
			var receiveStatusVal = $("#receiveStatusVal").val();
			$(receiveStatusList).each(
					function() {
						if (this.valueBank.lastIndexOf(receiveType) != -1) {
							var optionStr = "<option value='"
									+ this.valueStandardLetter + "'";
							if (receiveStatusVal == this.valueStandardLetter) {
								optionStr += " selected=selected "
							}
							optionStr += ">" + this.name + "</option>";
							receiveStatus.prepend(optionStr);
						}

					})

		}

		/* 跟据收件类型改变收件人下拉框option  */
		function initReceiverOption(receiveType) {
			var customerReceiverId = $("#customerReceiverId");
			customerReceiverId.empty();
			var customerReceiverIdVal = $("#customerReceiverIdVal").val();
			for (var i = 0; i < receiverList.length; i++) {
				var receiver = receiverList[i];

				if (receiveType == receiver.receiverType) {

					var optionStr = "<option value='" + receiver.id + "'";
					if (customerReceiverIdVal == receiver.id) {
						optionStr += " selected=selected "
					}
					optionStr += ">" + receiver.receiverName + "</option>"
					var option = $(optionStr);
					option.data("receiver", receiver)
					customerReceiverId.append(option);
				}
			}
		}
		initView();

		function submitForm(actionUrl) {
			var form = $("#main");
			var oldAction = form.attr("action");
			form.attr("action", actionUrl);
			form.submit();
			form.attr("action", oldAction);
		}

		$("#returnBtn").click(function() {
			var url = "listBillExpress.action";
			window.location.href = url;
		});
	});
</script>
</head>
<body>
	<form name="Form1" method="post" action="saveBillExpress.action" id="main">
		<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
			<tr>
				<td class="centercondition">
					<div id="tbl_current_status" style="margin-bottom: 50px">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" />
						<span class="current_status_menu">当前位置：</span>
						<span class="current_status_submenu1">销项税管理</span>
						<span class="current_status_submenu">开票管理</span>
						<span class="current_status_submenu">发票快递</span>
						<span class="current_status_submenu">信息编辑</span>
						<s:hidden id="customerId" name="customerId"></s:hidden>
						<s:iterator value="checkedlineNo" id="id">
							<s:hidden name="checkedlineNo" value="%{#id}"></s:hidden>
						</s:iterator>
					</div>
					<table id="tbl_context" cellspacing="0" width="100%" align="center" cellpadding="0" style="margin-top: 20px">
						<tr>
							<td style="background-color: #F0F0F0;width: 10%">取票方式 :</td>
							<td width="20%">
								<s:select id="receiveType" cssStyle="width:200px" name="billExpressForm.receiveType" list="receiveTypeList" listKey="valueStandardLetter" listValue="name"></s:select>
							</td>
							<td style="background-color: #F0F0F0;width: 10%"></td>
							<td style="width: 20%"></td>
							<td style="background-color: #F0F0F0;width: 10%"></td>
							<td style="width: 20%"></td>
						</tr>
					</table>
					<div id="addressDiv">
						<table id="tbl_context" cellspacing="0" width="100%" align="center" cellpadding="0">
							<tr>
								<td style="background-color: #F0F0F0;width: 10%">
									<span>收件地址标签:</span>
								</td>
								<td style="width: 20%">
									<s:hidden id="customerAddressIdVal" value="%{billExpressForm.customerAddressId}"></s:hidden>
									<select id="customerAddressId" name="billExpressForm.customerAddressId" style="width:200px"></select>
								</td>
								<td style="background-color: #F0F0F0;width: 10%">收件地址:</td>
								<td style="width: 20%">
									<span id="receiverAddress"></span>
								</td>
								<td style="background-color: #F0F0F0;width: 10%"></td>
								<td style="width: 20%"></td>
							</tr>
							<tr>
								<td style="background-color: #F0F0F0;width: 10%">收件人:</td>
								<td style="width: 20%">
									<span id="receiver"></span>
								</td>
								<td style="background-color: #F0F0F0;width: 10%">收件人电话:</td>
								<td style="width: 20%">
									<span id="receiverPhone"></span>
								</td>
								<td style="background-color: #F0F0F0;width: 10%">邮编:</td>
								<td style="width: 20%">
									<span id="postCode"></span>
								</td>
							</tr>
							<tr>
								<td style="background-color: #F0F0F0;width: 10%">联系人:</td>
								<td style="width: 20%">
									<span id="contactPerson"></span>
								</td>
								<td style="background-color: #F0F0F0;width: 10%">联系人电话:</td>
								<td style="width: 20%">
									<span id="contactPhone"></span>
								</td>
								<td style="background-color: #F0F0F0;width: 10%">联系人邮箱:</td>
								<td style="width: 20%">
									<span id="contactEmail"></span>
								</td>
							</tr>
							<tr>
								<td style="background-color: #F0F0F0;width: 10%;height: 100px;">备注:</td>
								<td colspan="6">
									<span id="addressRemark"></span>
								</td>
							</tr>
						</table>
					</div>
					<div id="receiverDiv">
						<table id="tbl_context" cellspacing="0" width="100%" align="center" cellpadding="0">
							<tr>
								<td style="background-color: #F0F0F0;width: 10%">收件人:</td>
								<td style="width: 20%">
									<%-- <s:select id="customerReceiverId" name="billExpressForm.customerReceiverId" list="customerReceiverList" listKey="id" listValue="receiverName"></s:select> --%>
									<s:hidden id="customerReceiverIdVal" value="%{billExpressForm.customerReceiverId}"></s:hidden>
									<select id="customerReceiverId" name="billExpressForm.customerReceiverId" style="width:200px"></select>
								</td>
								<td style="background-color: #F0F0F0;width: 10%"></td>
								<td style="width: 20%"></td>
								<td style="background-color: #F0F0F0;width: 10%"></td>
								<td style="width: 20%"></td>
							</tr>
							<tr>
								<td style="background-color: #F0F0F0;width: 10%">证件类型:</td>
								<td style="width: 20%">
									<span id="documentsTypeName"></span>
								</td>
								<td style="background-color: #F0F0F0;width: 10%">证件号码</td>
								<td style="width: 20%">
									<span id="documentsCode"></span>
								</td>
								<td style="background-color: #F0F0F0;width: 10%">收件人名:</td>
								<td style="width: 20%">
									<span id="receiverName"></span>
								</td>
							</tr>
						</table>
					</div>
					<div>
						<table id="tbl_context" cellspacing="0" width="100%" align="center" cellpadding="0">
							<tr>
								<td style="background-color: #F0F0F0;width: 10%">状态</td>
								<td style="width: 20%">
									<s:hidden id="receiveStatusVal" value="%{billExpressForm.receiveStatus}"></s:hidden>
									<select id="receiveStatus" name="billExpressForm.receiveStatus" style="width:200px"></select>
								</td>
								<td style="background-color: #F0F0F0;width: 10%"></td>
								<td style="width: 20%"></td>
								<td style="background-color: #F0F0F0;width: 10%"></td>
								<td style="width: 20%"></td>
							</tr>
						</table>
					</div>
					<table id="tbl_tools" width="100%" border="0">
						<tr>
							<td align="left">
								<input id="savaBtn" type="button" class="tbl_query_button" value="保存" />
								<input id="returnBtn" type="button" class="tbl_query_button" value="返回" />
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</form>
	<s:debug></s:debug>
</body>
</html>