<!--file: <%=request.getRequestURI()%> -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../../page/include.jsp"%>
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
<meta http-equiv="Pragma" content="no-cache" />
<title>商品管理</title>
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
					var url = "editGoodConfig.action";
					OpenModalWindow(encodeURI(url), 600, 550, true);
				});
				$("#btnDel").click(function() {
					var obj = $("[name=checkedlineNo]:checked");
					if (obj.size() == 0) {
						alert("请选择一条明细");
						return;
					}
					submitForm("delGoodConfigs.action");
				});

				$(".aEdit").click(function() {
					var id = $(this).closest("td").find(".editId").val()
					var url = "editGoodConfig.action?goodsNo=" + id;
					OpenModalWindow(encodeURI(url), 600, 550, true);
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
					submitForm("listGoodConfig.action");
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
	<%--		<form id="main" action="<c:out value='${webapp}'/>/listGoodsInfo.action" method="post">--%>
	<form id="main" action="listGoodsInfo.action" method="post" enctype="multipart/form-data">
		<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
			<tr>
				<td class="centercondition">
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" />
						<span class="current_status_menu">当前位置：</span>
						<span class="current_status_submenu1">参数管理</span>
						<span class="current_status_submenu">业务参数管理</span>
						<span class="current_status_submenu">商品管理</span>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="1" cellspacing="0">
							<tr>
								<td align="right">商品编号</td>
								<td>
									<input name="goodsNo" id="goodsNo" type="text" class="tbl_query_text" value="<s:property value="goodsNo"/>" style="width: 150;" maxlength="20" />
								</td>
								<td align="right">商品名称</td>
								<td>
									<input name="goodsName" id="goodsName" type="text" class="tbl_query_text" value="<s:property value="goodsName"/>" style="width: 150;" maxlength="50" />
								</td>
								<td>
									<input id="btnSearch" type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" value="查询" />
								</td>
							</tr>
						</table>
					</div>
					<table id="tbl_tools" width="100%" border="0">
						<tr>
							<td align="left" width="200">
								<a href="#" id="btnAdd">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon16.png" />
									增加
								</a>
								<a href="#" id="btnDel">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1019.png" />
									删除
								</a>
							</td>
							<%-- <td align="left" width="255">
								<s:file name="attachmentTaxItem" size="30" style="height:26px;"></s:file>
							</td>
							<td>
								<a href="#" onclick="submitForm('importGoodsInfo.action');">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1007.png" />
									导入
								</a>
								<a href="#" onclick="toExcel();">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />
									导出
								</a>
							</td> --%>
						</tr>
					</table>
					<div style="overflow: auto; width: 100%; margin-left:5px;" id="lessGridList1">
						<table class="lessGrid" cellspacing="0" rules="all" border="0" cellpadding="0" display="none" style="border-collapse: collapse; width: 100%;">
							<tr class="lessGrid head" style="height:20px;">
								<th width="4%" style="text-align:center">
									<input id="CheckAll" style="width: 13px; height:13px;" type="checkbox" onClick="checkAll(this,'selectGoodsNos')" />
								</th>
								<th width="6%" style="text-align: center">序号</th>
								<th width="30%" style="text-align:center" nowrap>商品编号</th>
								<th width="30%" style="text-align:center" nowrap>商品名称</th>
								<th width="10%" style="text-align:center" nowrap>纳税人类型</th>
								<th style="text-align:center" >操作</th>
							</tr>
							<s:iterator value="paginationList.recordList" id="iList" status="stuts">
								<tr align="center" class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
									<td align="center">
										<input type="checkbox" style="width: 13px; height: 13px;" name="checkedlineNo" value="<s:property value="goodsNo"/>" />
									</td>
									<td align="center">
										<s:property value='#stuts.count' />
									</td>
									<td align="center">
										<s:property value="goodsNo" />
									</td>
									<td align="center">
										<s:property value="goodsName" />
									</td>
									<td align="center">
										<s:property value="taxName" />
									</td>
									<td align="center">
										<%-- <a href="#" onClick="openWindows('goodsInfoView.action?updFlg=1&taxno=<s:property value="taxNo" />&goodsNo=<s:property value="goodsNo" />&transType=<s:property value="transType" />')">
											<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1014.png" title="查看" style="border-width: 0px;" />
										</a> --%>
										<a href="#" class="aEdit">
											<input class="editId" type="hidden" value="<s:property value='goodsNo' />">
											<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1005.png" title="编辑" style="border-width: 0px;" />
										</a>
									</td>
								</tr>
							</s:iterator>
						</table>
					</div>
					<div id="anpBoud" align="Right" style="width:100%;vlign=top;">
						<table width="100%" cellspacing="0" border="0">
							<tr>
								<td align="left"></td>
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
</html>
