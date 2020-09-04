<!--file: <%=request.getRequestURI()%> -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<title>增值税管理平台</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%=bopTheme2%>/css/main.css" type="text/css"
	rel="stylesheet">
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/calendar_notime.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/XmlHttp.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/ylb.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/lhb.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/search.js"></script>
<script type="text/javascript"
	src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script type="text/javascript">
	function submitForm(actionUrl) {
		submitAction(document.forms[0], actionUrl);
		document.forms[0].action = "invoiceInquerityList.action?paginationList.showCount="
				+ "false";
	}
	function regTest() {
		var flag = true;
		var customerIds = document.getElementsByName("selectTransIds");
		for (i = 0; i < customerIds.length; i++) {
			if (customerIds[i].checked) {
				$("#" + customerIds[i].value).find("td").each(
						function(index, element) {
							if (index == 3) {
								alert(/^[\u4e00-\u9fa5]{0,}$/.test($.trim($(
										this).text())));
								if (!/^[\u4e00-\u9fa5]{0,}$/.test($
										.trim($(this).text()))
										|| $.trim($(this).text()) == null) {
									flag = false;
									return alert("客户名称必须为中文,请修改!");
									;
								}
							}
						});
			}
		}
		return flag;
	}
	// [提交/删除]按钮
	function submitInvoiceInquerity(value) {
		var t = "";
		var msg = "";
		var res = "";
		var resError = "";
		if (value == 'delete') {
			msg = "是否确认删除?";
			res = "删除成功";
			resError = "删除失败";
		}
		var inputs = document.getElementsByName("selectBillIds");
		for (var i = 0; i < inputs.length; i++) {
			if (inputs[i].checked == true) {
				t += inputs[i].value + ",";
			}
		}
		if (t.length == 0) {
			alert("请选择数据！");
			return;
		}
		if (confirm(msg)) {
			if (value == 'delete') {
				$.ajax({
					url : 'deleteInvoiceInquerity.action',
					type : 'POST',
					async : false,
					data : {
						applicationFormId : t.substring(0, t.length - 1)
					},
					dataType : 'text',
					error : function() {
						return false;
					},
					success : function(result) {
						if (result == 'Y') {
							alert(res);
							document.forms[0].submit();
						} else if (result == 'N') {
							alert(resError);
						}
					}
				});
			}
			document.forms[0].action = "invoiceInquerityList.action?paginationList.showCount="
					+ "false";
		} else {
			return;
		}
	}

	function OpenModalWindowSubmit(newURL, width, height, needReload) {
		var retData = false;
		if (typeof (width) == 'undefined') {
			width = screen.width * 0.9;
		}
		if (typeof (height) == 'undefined') {
			height = screen.height * 0.9;
		}
		if (typeof (needReload) == 'undefined') {
			needReload = false;
		}
		retData = showModalDialog(newURL, window, "dialogWidth:" + width
				+ "px;dialogHeight:" + height
				+ "px;center=1;scroll=1;help=0;status=0;");
		if (needReload && retData) {
			window.document.forms[0].submit();
		}
	}
	//新增
	function openInvoiceInquerity(url) {
		OpenModalWindow(url, 500, 300, true);
	}
</script>
<style type="text/css">
.style1 {
	width: 242px;
}
</style>
</head>
<body onkeydow="enterkey(event)">
	<form name="Form1" method="post" action="invoiceInquerityList.action"
		id="Form1">
		<%
			String currMonth = (String) request.getAttribute("currMonth");
		%>
		<input type="hidden" name="currMonth" id="currMonth"
			value="<%=currMonth%>" /> <input type="hidden" name="reasionInfo"
			id="reasionInfo" value="" />
		<table id="tbl_main" cellpadding="0" cellspacing="0"
			class="tablewh100">
			<tr>
				<td class="centercondition"><s:component
						template="rocketMessage" />
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> <span
							class="current_status_menu">当前位置：</span> <span
							class="current_status_submenu1">销项税管理</span> <span
							class="current_status_submenu">开票管理</span> <span
							class="current_status_submenu">发票申请表</span>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" width="80%"
							border="0">
							<tr>
								<td align="left">申请日期</td>
								<td align="left"><input class="tbl_query_time"
									id="applyTime" type="text" name="applyTime"
									value="<s:property value="applyTime"/>" onfocus="WdatePicker()"
									size='11' /></td>
								<td align="left">应税服务名称</td>
								<td>
								<select id="productType" name="productType" style="width: 133px">
							       	   <option
											value="" <s:if test='invoiceType==""'>selected</s:if>
											<s:else></s:else>>所有</option>
										<s:iterator value="mapVatType" id="entry">
										   <option value="<s:property value="key"/>" <s:if test='invoiceInquerityListInfo.productType==#entry.key'>selected</s:if>
											<s:else></s:else> >
											<s:property value="value"/>										   
										   </option>										
										</s:iterator>
								</select>
								</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>					
								<td style="width: 80px;" align="right"><input type="button"
									class="tbl_query_button" value="查询"
									onMouseMove="this.className='tbl_query_button_on'"
									onMouseOut="this.className='tbl_query_button'" name="BtnView"
									id="BtnView"
									onclick="submitForm('invoiceInquerityList.action')" /></td>
							</tr>
						</table>
					</div>
					<table id="tbl_tools" width="100%" border="0">
						<tr>
							<td align="left"><a href="#"
								onclick="openInvoiceInquerity('addInvoiceInquerity.action');"
								name="cmdFilter"> <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon16.png" />新增
							</a> <a href="#" name="btDelete" id="btDelete"
								onclick="submitInvoiceInquerity('delete')"> <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1019.png" />
									删除
							</a></td>

						</tr>
					</table>
					<div id="lessGridList4"
						style="width: 100%; height: 360px; overflow: auto;">
						<table class="lessGrid" cellspacing="0" rules="all" border="0"
							cellpadding="0" style="border-collapse: collapse; width: 100%;">
							<tr class="lessGrid head">
								<th style="text-align: center"><input
									style="width: 13px; height: 13px;" id="CheckAll"
									type="checkbox" onclick="cbxselectall(this,'selectBillIds')" />
								</th>
								<th style="text-align: center">序号</th>
								<th style="text-align: center">申请日期</th>
								<th style="text-align: center">应税服务名称</th>
								<!-- <th style="text-align: center">月度</th> -->
								<th style="text-align: center">金额</th>
								<th style="text-align: center">税率</th>
								<th style="text-align: center">税额</th>
								<th style="text-align: center">价税合计</th>
								<th style="text-align: center">状态</th>
							</tr>
							<s:iterator value="paginationList.recordList" id="iList"
								status="stuts">
								<tr align="center"
									class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>"
									id="<s:property value="#iList.applicationFormId"/>">
									<td><input type="checkbox"
										style="width: 13px; height: 13px;" name="selectBillIds"
										value="<s:property value="#iList.applicationFormId"/>"</td>
									<td align="center"><s:property value='#stuts.count' /></td>
									<td align="center"><s:property value="applyTime" /></td>
									<td align="center">
									    <s:iterator value="mapVatType" id="entry">
									       <s:if test="productType==#entry.key">
									           <s:property value="value" />
									       </s:if>									    						    
									    </s:iterator>									
									</td>	
									<%-- <td align="center"><s:property value="monthly" /></td>	 --%>
									<td align="center"><s:if test="amount==''||amount==null">0</s:if>
										<s:else>
											<s:property value="amount" />
										</s:else></td>
									<td align="center"><s:if test="taxRate==''||taxRate==null">0</s:if>
										<s:else>
											<s:property value="taxRate" />
										</s:else></td>
									<td align="center"><s:if
											test="amountTax==''||amountTax==null">0</s:if> <s:else>
											<s:property value="amountTax" />
										</s:else></td>
										<td align="center"><s:if
											test="totalPriceandTax==''||totalPriceandTax==null">0</s:if>
										<s:else>
											<s:property value="totalPriceandTax" />
										</s:else></td>
									<td align="center"><s:if test="formStatus==0">校验失败</s:if>
										<s:if test="formStatus==1">校验成功</s:if></td>
								</tr>
							</s:iterator>
						</table>
					</div>
					<div id="anpBoud" align="Right"
						style="overflow: auto; width: 100%;">
						<table width="100%" cellspacing="0" border="0">
							<tr>
								<input type="hidden" name="paginationList.showCount"
									value="false" />
								<td align="right"><s:component template="pagediv" /></td>
							</tr>
						</table>
					</div></td>
			</tr>
		</table>
	</form>
</body>
</html>