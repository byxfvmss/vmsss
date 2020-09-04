<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../page/include.jsp"%>
<link href="<%=bopTheme2%>/css/main.css" type="text/css"
	rel="stylesheet">
<link href="<%=webapp%>/page/common.css" type="text/css"
	rel="stylesheet">

<script src="<%=bopTheme%>/js/MessageBox/js/messageBox.js"
	type="text/javascript"></script>
<link href="<%=bopTheme%>/js/MessageBox/css/messageBox.css"
	rel="stylesheet" type="text/css" />
<script type="text/javascript"
	src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript"
	src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/search.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/main.js"></script>
<meta http-equiv="Pragma" content="no-cache" />
<title>附加税管理</title>

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
</style>
<script type="text/javascript">
	$(function() {
		$("#btnExport").click(function() {
			submitForm("exportSurtaxList.action");
		});
		$("#btnSearch").click(function() {
			submitForm("listSurtax.action");
		});
		$("#btnReSet").click(function() {
			$("#taxperName,#surtaxRate").val("");
			$("#tbl_query select option:selected").attr("selected", "");
			$("#tbl_query select option:first").attr("selected", "selected");
			var now = new Date();
			var year = now.getFullYear();
			var month = now.getMonth();
			if(0==month){
			  month = 12;
			  year = year-1;
			}
			var applyPeriod = year + "-" + month;
			$("#applyPeriod").val(applyPeriod);
		});
		function submitForm(actionUrl) {
			var form = $("#main");
			var oldAction = form.attr("action");
			form.attr("action", actionUrl);
			form.submit();
			form.attr("action", "listSurtax.action");
		}
		$("#CheckAll").click(function(){
			$("[name=checkedlineNo]").attr("checked",$(this).attr("checked"))
		});
	})
	
	
</script>
</head>
<body>
	<form id="main" action="listSurtax.action" method="post">
		<table id="tbl_main" cellpadding="0" cellspacing="0"
			class="tablewh100">
			<tr>
				<td class="centercondition">
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> <span
							class="current_status_menu">当前位置：</span> <span
							class="current_status_submenu1">附加税管理</span> <span
							class="current_status_submenu">附加税管理</span> <span
							class="current_status_submenu">附加税</span>
					</div>

					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td>纳税人识别号</td>
								<td><s:if test="taxperList != null && taxperList.size > 0">
										<s:select id="taxPerNumber" name="taxPerNumber"
											list="taxperList" listKey='taxperNumber'
											listValue='taxperNumber' headerKey="" headerValue="所有"
											onChange="getAjaxTaxperName(this.value, document.getElementById('taxperName'))" />
									</s:if> <s:if test="taxperList == null || taxperList.size == 0">
										<select name="taxPerNumber" class="readOnlyText">
											<option value="">请分配机构权限</option>
										</select>
									</s:if></td>
								<td>纳税人名称</td>
								<td><input id="taxperName" class="tbl_query_text"
									name="taxperName" type="text"
									value="<s:property value='taxperName' />" />
									<div id="searchInstResult" style="display: none;" /></td>
								<%--
				 <td>附加税税率</td>
				 <td><input id="surtaxRate" maxlength="4" name="surtaxRate" type="text" class="tbl_query_text" value="<s:property value='surtaxRate' />" class=""	 />&nbsp;&nbsp;&nbsp;</td>
		 		--%>
								<td>申报周期</td>
								<td><input id="applyPeriod" name="applyPeriod" type="text"
									value="<s:property value='applyPeriod' />"
									class="tbl_query_text"
									onclick="WdatePicker({dateFmt:'yyyy-MM'})"></td>

							</tr>
							<tr>
								<td>附加税税种</td>
								<td><select id="surtaxType" name="surtaxType"><option
											value="" <s:if test='surtaxType==""'>selected</s:if>
											<s:else></s:else>>全部</option>
										<s:iterator value="mapSurtaxAmtType" id="entry">
											<option value="<s:property value="key"/>"
												<s:if test='surtaxType==#entry.key'>selected</s:if>
												<s:else></s:else>><s:property value="value" /></option>
										</s:iterator>
								</select></td>
								<td></td>
								<td></td>
								<td colspan="2"><input type="button"
									class="tbl_query_button"
									onMouseMove="this.className='tbl_query_button_on'"
									onMouseOut="this.className='tbl_query_button'" value="重置"
									id="btnReSet" /> <input type="button" class="tbl_query_button"
									onMouseMove="this.className='tbl_query_button_on'"
									onMouseOut="this.className='tbl_query_button'"
									name="cmdDistribute" value="查询" id="btnSearch" /></td>
							</tr>
						</table>
					</div>

					<table id="tbl_tools" width="100%" border="0">
						<tr>
							<td align="left"><a href="#" id="btnExport"><img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />导出</a>
							</td>
						</tr>
					</table>

					<div id="lessGridList4" style="overflow: auto; width: 100%;">
						<table class="lessGrid" cellspacing="0" rules="all" border="0"
							cellpadding="0" style="border-collapse: collapse;width: 100%;">
							<tr class="lessGrid head">
								<th width="3%" style="text-align:center"><input
									id="CheckAll" style="width:13px;height:13px;" type="checkbox"
									 /></th>
								<th style="text-align: center">序号</th>
								<th style="text-align: center">纳税人识别号</th>
								<th style="text-align: center">纳税人名称</th>
								<th style="text-align: center">附加税类型</th>
								<th style="text-align: center">销项附加税税额</th>
								<th style="text-align: center">进项附加税税额</th>
								<th style="text-align: center">应缴附加税税额</th>
								<th style="text-align: center">机构名称</th>
							</tr>
							<s:iterator value="paginationList.recordList" id="iList"
								status="stuts">

								<tr align="center"
									class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
									<td><input type="checkbox" style="width:13px;height:13px;"
										name="checkedlineNo" id = "checkedlineNo"
										value="<s:property value="#stuts.count-1"/>" /></td>
									<td align="center"><s:property value='#stuts.count' /></td>
									<td><s:property value='taxpernumber' /></td>
									<td><s:property value='taxpername' /></td>
									<td><s:property value='surtax_name' /></td>
									<td style="text-align: right"><s:property
											value='surtaxAMT' /></td>
									<td style="text-align: right"><s:property
											value='inSurtaxAMT' /></td>
									<td style="text-align: right"><s:property
											value='payurtaxAMT' /></td>
									<td><s:property value='inst_name' /></td>

								</tr>
							</s:iterator>
						</table>

					</div>
					<div id="anpBoud" align="Right" style="width:100%;">
						<table width="100%" cellspacing="0" border="0">
							<tr>
								<td align="right"><s:component template="pagediv" /></td>
							</tr>
						</table>
					</div>
				</td>
			</tr>
		</table>
	</form>

</body>
</html>















