<!--file: <%=request.getRequestURI()%> -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<title>增值税管理平台</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script type="text/javascript">
	// [查询]按钮
	function submit() {
		document.forms[0].submit();
	}
	$(function() {
		$("#goodsRel").click(function() {
			var ids = $(".selectTransTypeIds:checked");
			var size = ids.size();
			var keys = "";
			if (size == 0) {
				alert("请选择交易类型");
				return;
			}
			for (var i = 0; i < size; i++) {
				if (i != size - 1) {
					keys = keys + ids[i].value + ",";
				} else {
					keys = keys + ids[i].value;
				}
			}
			var url = "relTrans2Goods.action" + "?ids=" + keys;
			OpenModalWindow(encodeURI(url), 600, 650, true);
		});

		$("#addBtn").click(function() {
			var url = "editTransType.action";
			OpenModalWindow(encodeURI(url), 500, 450, true);
		});
		$("#cmdDelbt").click(function() {
			submitForm("removeTransType.action");
		});

		function submitForm(actionUrl) {
			var form = $("#main");
			var oldAction = form.attr("action");
			form.attr("action", actionUrl);
			form.submit();
			form.attr("action", oldAction);
		}
	})
</script>
<style type="text/css">
.style1 {
	width: 242px;
}
</style>
</head>
<body>
	<form id="main" action="listTransType.action" method="post">
		<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
			<tr>
				<td class="centercondition">
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" />
						<span class="current_status_menu">当前位置：</span>
						<span class="current_status_submenu1">参数管理</span>
						<span class="current_status_submenu">业务参数管理</span>
						<span class="current_status_submenu">交易类型设置</span>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td align="left">交易类型编号:</td>
								<td>
									<input class="tbl_query_text" id="transTypeCondition.transTypeId" type="text" name="transTypeCondition.transTypeId" value="<s:property value='transTypeCondition.transTypeId'/>" />
								</td>
								<td align="left">交易类型名称:</td>
								<td>
									<input class="tbl_query_text" name="transTypeCondition.transTypeName" value="<s:property value='transTypeCondition.transTypeName'/>" />
								</td>
								<td style="text-align:left">
									<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="submit();" name="cmdSelect" value="查询"
										id="cmdSelect"
									/>
								</td>
							</tr>
							
						</table>
						<table id="tbl_tools" width="100%" border="0">
							<tr>
								<td align="left">
									<a href="#" id="addBtn" name="cmdFilter" id="cmdFilter">
										<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon16.png" />
										新增
									</a>
									<a href="#" name="cmdDelbt" id="cmdDelbt">
										<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1019.png" />
										删除
									</a>
									<!--  
									<a href="#" name="goodsRel" id="goodsRel">
										<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1013.png" />
										关联商品
									</a>
									<a href="#" name="itemRel" id="itemRel">
										<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1013.png" />
										关联科目
									</a>
									<a href="#" id="cmdExpbt" name="cmdExpbt" onClick="submitForm('exportTaxDiskInfo.action');">
										<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />
										导出
									</a>
									-->
								</td>
							</tr>
						</table>
				</div>
				<div id="lessGridList1" style="overflow: auto; width: 100%;">
					<table class="lessGrid" cellspacing="0" rules="all" border="0" cellpadding="0" style="border-collapse: collapse;width: 100%;">
						<tr class="lessGrid head">
							<th style="text-align:center;">
								<input id="CheckAll" type="checkbox" onclick="cbxselectall(this,'checkedlineNo')" />
							</th>
							<th style="text-align:center">交易类型编号</th>
							<th style="text-align:center">交易名称</th>
							<!-- <th style="text-align:center">科目编号</th>
								<th style="text-align:center">科目名称</th> -->
							<!--	<th style="text-align:center">商品名称</th> -->
							<!-- <th style="text-align:center">备注</th> -->
							<th style="text-align:center">操作</th>
						</tr>
						<s:iterator value="paginationList.recordList" id="iList" status="stuts">
							<tr align="center" class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
								<td align="center" style="width:30px;">
									<input style="width:13px;height:13px;" type="checkbox" class="selectTransTypeIds" name="checkedlineNo" value="<s:property value='transTypeId'/>" />
								</td>
								<td align="center">
									<s:property value='transTypeId' />
								</td>
								</td>
								<td align="center">
									<s:property value='transTypeName' />
								</td>
								<%-- <td align="center">
										<s:property value='itemCode' />
									</td>
									<td align="center">
										<s:property value='itemName' />
									</td> --%>
								<td style="width: 10%">
									<a href="#" onClick="OpenModalWindow('editTransType.action?transTypeCondition.transTypeId=<s:property value="transTypeId" />',500,450,true)">
										<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1005.png" title="编辑" style="border-width: 0px;" />
									</a>
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