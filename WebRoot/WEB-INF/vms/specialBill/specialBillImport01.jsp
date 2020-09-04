<%@ page language="java" import="java.util.*" import="java.math.BigDecimal" import="com.opensymphony.util.BeanUtils" import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils" import="com.cjit.common.util.PaginationList" import="com.cjit.vms.input.model.SpecialBillWithhold" import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<title>增值税管理平台</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../../page/include.jsp"%>
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
	$(
			function() {

				$("#btnConfrim").click(function() {
					if ($("[name=checkedlineNo]:checked").length > 0) {
						submitForm("confrimSpecialBill01.action");
					} else {
						alert("请选择明细");
					}

				});
				$("#btnExport").click(function() {
					submitForm("exportSpecialBill01.action");
				});
				$("#btnImport").click(
						function() {
							var dir = $("#fileId").val();
							if (dir.length > 0) {
								if (dir.lastIndexOf(".XLS") > -1
										|| dir.lastIndexOf(".xls") > -1) {
									submitForm("importSpecialBill01.action");
								} else {
									alert("文件格式不对，请上传Excel文件。");
								}
							} else {
								alert("请先选择要上传的文件。");
							}
						});
				$("#btnSearch").click(function() {
					submitForm("listSpecialBill01.action");
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
					form.attr("action", "listSpecialBill.action");
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
	<form id="main" action="listSpecialBill.action" method="post" enctype="multipart/form-data">
		<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
			<tr>
				<td class="centercondition">
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" />
						<span class="current_status_menu">当前位置：</span>
						<span class="current_status_submenu1">VMS进项税管理</span>
						<span class="current_status_submenu">其他票据</span>
						<span class="current_status_submenu">代扣代缴票据导入</span>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td>税票票号</td>
								<td><input class="tbl_query_text" name="billNo" value="<s:property value='billNo' />"></td>
								<td>填发日期</td>
								<td><input id="writeData" class="tbl_query_text" name="writeData" type="text" value="<s:property value='writeData' />" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" /></td>
								<td>科目名称</td>
								<td><input id="subjectName" class="tbl_query_text" name="subjectName" type="text" value="<s:property value='subjectName' />" /></td>
								<td>票据类型</td>
								<td><s:select class="tbl_query_text" list="billClassList" name="billType" listKey="billType" listValue="billName">
									</s:select></td>
							</tr>
							<tr>
								<td>缴款期限日期</td>
								<td><input id="payData" name="payData" type="text" value="<s:property value='payData' />" class="tbl_query_text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})"></td>
								<td>缴款单位全称</td>
								<td><input id="taxInstChn" class="tbl_query_text" name="taxInstChn" type="text" value="<s:property value='taxInstChn' />" /></td>
								<td>缴款单位代码</td>
								<td><input id="taxNo" class="tbl_query_text" name="taxNo" type="text" value="<s:property value='taxNo' />" /></td>
								<td><input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" name="cmdDistribute" value="查询"
										id="btnSearch"
									/></td>
							</tr>
						</table>
					</div>
					<table id="tbl_tools" width="100%" border="0">
						<tr>
							<td width="150"><a href="#" id="btnConfrim"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />确认提交</a></td>
							<td width="234"><input type='file' name='theFile' id='fileId' size='25' style="height:26px;" /></td>
							<td width="100"><a href="#" id="btnImport"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1007.png" />导入</a></td>
							<td><a href="#" id="btnExport"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />导出</a></td>
						</tr>
					</table>
					<div id="lessGridList4" style="overflow: auto; width: 100%;">
						<table class="lessGrid" cellspacing="0" rules="all" border="0" cellpadding="0" style="border-collapse: collapse;width: 100%;">
							<tr class="lessGrid head">
								<th width="3%" style="text-align:center"><input id="CheckAll" style="width:13px;height:13px;" type="checkbox" /></th>
								<th style="text-align: center">序号</th>
								<th style="text-align: center">税票票号</th>
								<th style="text-align: center">缴款单位代码</th>
								<th style="text-align: center">缴款单位全称</th>
								<th style="text-align: center">科目编码</th>
								<th style="text-align: center">科目名称</th>
								<th style="text-align: center">填发日期</th>
								<th style="text-align: center">税款开始时间</th>
								<th style="text-align: center">税款结束时间</th>
								<th style="text-align: center">税款限缴日期</th>
								<th style="text-align: center">实缴金额</th>
								<th style="text-align: center">操作</th>
							</tr>
							<s:iterator value="paginationList.recordList" id="iList" status="stuts">
								<tr align="center" class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
									<td><input type="checkbox" style="width:13px;height:13px;" name="checkedlineNo" value="<s:property value="billNo"/>" /></td>
									<td align="center"><s:property value='#stuts.count' /></td>
									<td><s:property value='billNo' /></td>
									<td><s:property value='taxNo' /></td>
									<td><s:property value='taxInstChn' /></td>
									<%-- <td><s:property value='bankandname' /></td> --%>
									<%-- <td><s:property value='bankandaccount' /></td> --%>
									<td><s:property value='subjectId' /></td>
									<td><s:property value='subjectName' /></td>
									<%-- <td><s:property value='subjectClass' /></td>
								<td><s:property value='nationalTre' /></td> --%>
									<td><s:property value='writeData' /></td>
									<td><s:property value='belongDataS' /></td>
									<td><s:property value='belongDataE' /></td>
									<td><s:property value='payData' /></td>
									<td><s:property value='taxAmtSum' /></td>
									<%-- <td><s:property value='dataStatus' /></td>
								<td><s:property value='remark' /></td> --%>
									<td><a href="editSpecialBill01.action?editBillNo=<s:property value='billNo'/>&isEdit=true"> <img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1005.png" title="编辑"
												style="border-width: 0px;"
											/> <a href="editSpecialBill01.action?editBillNo=<s:property value='billNo'/>&isEdit=false"> <img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1014.png" title="查看"
													style="border-width: 0px;"
												/>
										</a></td>
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
