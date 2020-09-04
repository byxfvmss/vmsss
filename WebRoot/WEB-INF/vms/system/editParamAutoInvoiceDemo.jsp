<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../../../page/include.jsp"%>
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
<meta http-equiv="Pragma" content="no-cache" />
<title>数据导入</title>
<script type="text/javascript">
	$(function() {
		$("#add").click(function() {
			var lastTr = $("#lineDemo").clone();
			lastTr.attr("style","");
			$("#detailTable").append(lastTr);
			reloadClass();
		});
		$(".delLine").live("click", function() {
			$(this).closest("tr").remove();
		});
		$(".runType").live("change",function(){
			
			if($(this).val()=="N"){
				$(this).closest("tr").find(".unitSpan").text("期");
			}else{
				$(this).closest("tr").find(".unitSpan").text("天");
			}
		});
		
		$("#save").click(function(){
			alert("保存成功");
		});
		function reloadClass() {
			$("#detailTable tr:even").attr("class", "lessGrid rowA");
			$("#detailTable tr:odd").attr("class", "lessGrid rowB");
		}
		reloadClass();
	})
</script>
</head>
<body>
	<form id="main" action="listImpdata.action" method="post" enctype="multipart/form-data">
		<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
			<tr>
				<td class="centercondition">
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" />
						<span class="current_status_menu">当前位置：</span>
						<span class="current_status_submenu1">参数管理</span>
						<span class="current_status_submenu">自动开票管理</span>
						<span class="current_status_submenu">自动开票管理</span>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td></td>
							</tr>
						</table>
					</div>
					<table id="tbl_tools" cellpadding="1" cellspacing="0">
						<tr align="left">
							<td align="left" width="138">
								<a href="#" id="add" name="cmdFilter" >
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon16.png" />
									增加
								</a>
							</td>
							<td align="left" width="138">
								<a href="#" id="save" name="cmdFilter" >
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1006.png" />
									保存
								</a>
							</td>
							<td align="left" width="280" class="pleft15p"></td>
							<td align="left"></td>
						</tr>
					</table>
					<div id="lessGridList1" style="overflow: auto; width: 100%;">
						<table id="detailTable" class="lessGrid" cellspacing="0" rules="all" border="0" cellpadding="0" style="border-collapse: collapse;width: 100%;">
							<tr class="lessGrid head">
								<th style="text-align: center">交易类型</th>
								<th style="text-align: center">开票频度</th>
								<th style="text-align: center">偏移</th>
								<th style="text-align: center">开票时点</th>
								<th style="text-align: center">开票方式</th>
								<th style="text-align: center">开票种类</th>
								<th style="text-align: center">票据状态</th>
								<th style="text-align: center;">操作</th>
							</tr>
							<tr align="center" class="lessGrid" id = "lineDemo" style="display:none">
								<td width="20%">
									<s:select cssStyle="width:90%" list="tansTypeList" listKey="value" listValue="text"></s:select>
								</td>
								<td width="10%">
									<s:select cssClass="runType" cssStyle="width:90%" list="runTypeList" listKey="value" listValue="text"></s:select>
								</td>
								<td width="10%">
									<input type="text" class="tbl_query_time1"">
									<span class="unitSpan">天</span>
								</td>
								<td>
									<input type="text" class="tbl_query_time1" onfocus="WdatePicker({dateFmt:'HH:mm:ss'})">
								</td>
								<td>
									<s:select cssStyle="width:90%" name="moaiWay" list="wayList" listKey='valueStandardNum' listValue='name' />
								</td>
								<td>
									<s:select cssStyle="width:90%" name="moaiKind" list="kindList" listKey='valueStandardNum' listValue='name' />
								</td>
								<td>
									<s:select cssStyle="width:90%" name="billStatus" list="statusList" listKey='value' listValue='text' />
								</td>
								<td>
									<a href="#" class="delLine">
										<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1020.png" title="删除" style="border-width: 0px;" />
									</a>
								</td>
							</tr>
							
							<tr align="center" class="lessGrid">
								<td width="20%">
									<s:select cssStyle="width:90%" list="tansTypeList" listKey="value" listValue="text"></s:select>
								</td>
								<td width="10%">
									<s:select  cssClass="runType" cssStyle="width:90%" list="runTypeList" listKey="value" listValue="text"></s:select>
								</td>
								<td width="10%">
									<input type="text" class="tbl_query_time1"">
									<span class="unitSpan">天</span>
								</td>
								<td>
									<input type="text" class="tbl_query_time1" onfocus="WdatePicker({dateFmt:'HH:mm:ss'})">
								</td>
								<td>
									<s:select cssStyle="width:90%" name="moaiWay" list="wayList" listKey='valueStandardNum' listValue='name' />
								</td>
								<td>
									<s:select cssStyle="width:90%" name="moaiKind" list="kindList" listKey='valueStandardNum' listValue='name' />
								</td>
								<td>
									<s:select cssStyle="width:90%" name="billStatus" list="statusList" listKey='value' listValue='text' />
								</td>
								<td>
									<a href="#" class="delLine">
										<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1020.png" title="删除" style="border-width: 0px;" />
									</a>
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