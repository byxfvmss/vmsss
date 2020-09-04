<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/page/include.jsp"%>
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
<link href="<%=webapp%>/page/common.css" type="text/css" rel="stylesheet">
<script src="<%=bopTheme%>/js/MessageBox/js/messageBox.js" type="text/javascript"></script>
<link href="<%=bopTheme%>/js/MessageBox/css/messageBox.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/search.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
<meta http-equiv="Pragma" content="no-cache" />
<title>交易认定管理</title>
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

	$(
			function() {

				/**$("#btnConfrim").click(function() {
					if ($("[name=checkedlineNo]:checked").length > 0) {
						submitForm("confrimSpecialBill01.action");
					} else {
						alert("请选择明细");
					}

				});*/
				$("#btnAdd").click(function() {
					var url = "editTransVerification.action";
					OpenModalWindow(encodeURI(url), 500, 450, true);
				});
				$("#btnDel").click(function() {
					var obj = $("[name=checkedlineNo]:checked");
					if (obj.size() == 0) {
						alert("请选择一条明细");
						return;
					}
					submitForm("deleteTransVerification.action");
				});

				$(".aEdit").click(function() {
					var id = $(this).closest("td").find(".editId").val()
					var url = "editTransVerification.action?id=" + id;
					OpenModalWindow(encodeURI(url), 500, 450, true);
				});
				$("#btnExport").click(function() {
					submitForm("exportTransVerification.action");
				});
				$("#btnImport")
						.click(
								function() {
									var dir = $("#fileId").val();
									if (dir.length > 0) {
										if (dir.lastIndexOf(".XLS") > -1
												|| dir.lastIndexOf(".xls") > -1) {
											submitForm("importTransVerification.action");
										} else {
											alert("文件格式不对，请上传Excel文件。");
										}
									} else {
										alert("请先选择要上传的文件。");
									}
								});
				$("#btnSearch").click(function() {
					submitForm("listTransVerification.action");
				});

				/* 	$(".aEdit").click(function(){
						var billNo = $(this).closest("tr").find("[name=checkedlineNo]").val();
						var actionStr = "listSpecialBill01.action?editBillNo="+billNo;
						submitForm(actionStr);
					}); */
				function submitForm(actionUrl) {
					var form = $("#main");
					var oldAction = form.attr("action");
					form.attr("action", actionUrl);
					form.submit();
					form.attr("action", oldAction);
				}
				$("#CheckAll").click(
						function() {
							$("[name=checkedlineNo]").attr("checked",
									$(this).attr("checked"))
						});
			})
</script>
</head>
<body>
	<form id="main" action="listTransVerification.action" method="post" enctype="multipart/form-data">
		<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
			<tr>
				<td class="centercondition">
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" />
						<span class="current_status_menu">当前位置：</span>
						<span class="current_status_submenu1">VMS进项税管理</span>
						<span class="current_status_submenu">业务参数管理</span>
						<span class="current_status_submenu">交易认定管理</span>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td>交易认定类型</td>
								<td>
									<s:textfield name="form.verificationType" cssClass="tbl_query_text"></s:textfield>
									<%-- <input class="tbl_query_text" name="form.verificationType" value="<s:property value='form.verificationType' />"> --%>
								</td>
								<td>交易发生地</td>
								<td>
									<s:select name="form.inlandFlag" list="inlandFlagList" listKey='value' listValue='text' headerKey="" headerValue="全部" style="width:126px"></s:select>
									<%-- <input class="tbl_query_text" name="form.inlandFlag" type="text" value="<s:property value='form.inlandFlag' />" /> --%>
								</td>
								<td>
									<!-- 商品名称 -->
								</td>
								<td>
									<%-- <input class="tbl_query_text" name="form.goodsName" type="text" value="<s:property value='form.goodsName' />" /> --%>
								</td>
							</tr>
							<tr>
								<td>纳税人类型</td>
								<td>
									<s:select name="form.taxType" list="taxTypeList" listKey='value' listValue='text' headerKey="" headerValue="全部" style="width:126px"></s:select>
								</td>
								<td>税率</td>
								<td>
									<%-- <input id="payData" name="form.taxRate" type="text" value="<s:property value='form.taxRate' />" class="tbl_query_text"> --%>
									<s:select name="form.taxRate" list="taxRateList" listKey='value' listValue='text' headerKey="" headerValue="全部" style="width:126px"></s:select>
								</td>
								<td>
									<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" name="cmdDistribute" value="查询" id="btnSearch" />
								</td>
							</tr>
						</table>
					</div>
					<table id="tbl_tools" width="100%" border="0">
						<tr>
							<td width="250">
								<a href="#" id="btnAdd">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon16.png" />
									新增
								</a>
								<a href="#" id="btnDel">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1019.png" />
									删除
								</a>
							</td>
							<td width="234">
								<input type='file' name='theFile' id='fileId' size='25' style="height:26px;" />
							</td>
							<td width="100">
								<a href="#" id="btnImport">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1007.png" />
									导入
								</a>
							</td>
							<td>
								<a href="#" id="btnExport">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />
									导出
								</a>
							</td>
						</tr>
					</table>
					<div id="lessGridList4" style="overflow: auto; width: 100%;">
						<table class="lessGrid" cellspacing="0" rules="all" border="0" cellpadding="0" style="border-collapse: collapse;width: 100%;">
							<tr class="lessGrid head">
								<th width="3%" style="text-align:center">
									<input id="CheckAll" style="width:13px;height:13px;" type="checkbox" />
								</th>
								<th style="text-align: center">序号</th>
								<th style="text-align: center">交易认定类型</th>
								<th style="text-align: center">交易名称</th>
								<th style="text-align: center">交易发生地</th>
								<!-- <th style="text-align: center">商品编号</th>
								<th style="text-align: center">商品名称</th> -->
								<th style="text-align: center">纳税人类型</th>
								<th style="text-align: center">税率</th>
								<th style="text-align: center">操作</th>
							</tr>
							<s:iterator value="paginationList.recordList" id="iList" status="stuts">
								<tr align="center" class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
									<td>
										<input type="checkbox" style="width:13px;height:13px;" name="checkedlineNo" value="<s:property value="id"/>" />
									</td>
									<td align="center">
										<s:property value='#stuts.count' />
									</td>
									<td>
										<s:property value='verificationType' />
									</td>
									<td>
										<s:property value='verificationName' />
									</td>
									<td>
										<s:property value='inlandFlagName' />
									</td>
									<%-- <td>
										<s:property value='goodsNo' />
									</td>
									<td>
										<s:property value='goodsName' />
									</td> --%>
									<td>
										<s:property value='taxTypeName' />
									</td>
									<td>
										<s:property value='taxRateName' />
									</td>
									<td>
										<%-- 	<a href="" onclick="OpenModalWindow(encodeURI('editTransVerification.action?id=<s:property value='id'/>'), 500, 400, false);"> --%>
										<a class="aEdit">
											<input class="editId" type="hidden" value="<s:property value='id'/>">
											<!-- <a href="" onclick="OpenModalWindowSubmit('<c:out value="${webapp}"/>/editTransVerification.action?id=<s:property value='id'/>',500,400,'view') "> -->
											<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1005.png" title="编辑" style="border-width: 0px;" />
									</td>
								</tr>
							</s:iterator>
						</table>
					</div>
					<div id="anpBoud" align="Right" style="width:100%;">
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
