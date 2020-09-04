<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../../../page/include.jsp"%>
<link href="<%=bopTheme2%>/css/main.css" type="text/css"
	rel="stylesheet">
<script type="text/javascript"
	src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript"
	src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/main.js"></script>
<meta http-equiv="Pragma" content="no-cache" />
<title>数据导入审核</title>
<script type="text/javascript">
	function submitForm(actionUrl) {
		var form = document.getElementById("main");
		form.action = actionUrl;
		form.submit();
	}
	// 审核数据
	function checkData() {
		if (checkChkBoxesSelected("batchIdList")) {
			var batchIds="";
			var items = $('[name = "batchIdList"]:checkbox:checked'); 
			for ( var i = 0; i < items.length; i++) {
				// 如果i+1等于选项长度则取值后添加空字符串，否则为逗号
     			batchIds = (batchIds + items.get(i).value) + (((i + 1)== items.length) ? '':',');
			}
		
			submitForm("auditData.action?batchIds="+batchIds);
		} else {
			alert("请选择要审核拒绝的数据！");
		}
	}
	//检查多选框集是否至少有一个被选中
	function checkChkBoxesSelected(chkBoexName) {
		var flg = false;
		var chkBoexes = document.getElementsByName(chkBoexName);
		for (i = 0; i < chkBoexes.length; i++) {
			if (chkBoexes[i].checked) {
				return true;
			}
		}
		return false;
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
	
	function closeWindow(){
		$("#main").submit();
	}
	//审核拒绝
	function auditDataNo(){
		if (checkChkBoxesSelected("batchIdList")) {
			var batchIds="";
			var items = $('[name = "batchIdList"]:checkbox:checked'); 
			for ( var i = 0; i < items.length; i++) {
				// 如果i+1等于选项长度则取值后添加空字符串，否则为逗号
     			batchIds = (batchIds + items.get(i).value) + (((i + 1)== items.length) ? '':',');
     			
			}
		
			submitForm("auditDataNo.action?batchIds="+batchIds);
		} else {
			alert("请选择要审核拒绝的数据！");
		}
	}
</script>
</head>
<body>
	<form id="main" action="listAuditImpdata.action" method="post"
		enctype="multipart/form-data">
		<input type="hidden" name="statusList" value="1" /> <input
			type="hidden" name="statusList" value="2" />
		<table id="tbl_main" cellpadding="0" cellspacing="0"
			class="tablewh100">
			<tr>
				<td class="centercondition">
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> <span
							class="current_status_menu">当前位置：</span> <span
							class="current_status_submenu1">销项税管理</span> <span
							class="current_status_submenu">开票管理</span> <span
							class="current_status_submenu">数据导入审核</span>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td width="50">导入时间</td>
								<td width="280"><input id="startTime" name="impTransCondition.startTime"
									type="text" value="<s:property value='impTransCondition.startTime' />"
									class="tbl_query_time1"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endTime\')||\'new Date()\'}'})" />
									&nbsp;&nbsp;&nbsp;-&nbsp;&nbsp;&nbsp; <input id="endTime"
									name="impTransCondition.endTime" type="text"
									value="<s:property value='impTransCondition.endTime' />" class="tbl_query_time1"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startTime\')}',maxDate:new Date()})" />
								</td>
								
								<td style="text-align:left"><input type="button"
									class="tbl_query_button"
									onMouseMove="this.className='tbl_query_button_on'"
									onMouseOut="this.className='tbl_query_button'"
									onclick="submitForm('listAuditImpdata.action');"
									name="cmdFilter" value="查询" id="cmdFilter" />
								</td>
							</tr>
						</table>
					</div>
					<table id="tbl_tools" cellpadding="1" cellspacing="0">
						<tr align="left">
							<td align="left"><a href="#"
								onclick="return checkData();" name="cmdFilter" id="cmdFilter">
									<img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1006.png" />
									审核通过 </a> <a href="javascript:void(0);" onClick="auditDataNo()">
									<img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1018.png" />
									审核拒绝 </a></td>
						</tr>
					</table>
					<div id="lessGridList1" style="overflow: auto; width: 100%;">
						<table class="lessGrid" cellspacing="0" rules="all" border="0"
							cellpadding="0" style="border-collapse: collapse;width: 100%;">
							<tr class="lessGrid head">
								<th width="3%" style="text-align:center"><input
									id="CheckAll" style="width:13px;height:13px;" type="checkbox"
									onClick="checkAll(this,'batchIdList')" />
								</th>
								<th style="text-align: center">序号</th>
								<th style="text-align: center">导入日期</th>
								<th style="text-align: center">上传用户</th>
								<th style="text-align: center">通过总数</th>
								<th style="text-align: center">认定结果</th>
							</tr>
							<s:iterator value="paginationList.recordList" id="iList"
								status="stuts">
								<tr align="center"
									class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
									<td>
										<input type="checkbox" style="width:13px;height:13px;"
										name="batchIdList" value="<s:property value="batchId"/>"
									</td>
									<td align="center"><s:property value='#stuts.count' /></td>
									<td><s:property value='impTime' /></td>
									<td><s:property value='impUser' /></td>
									<td><s:property value='passCount' /></td>
									<td align="center"
										class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
										<a
										href="showDataDetail.action?impBatchId=<s:property value="batchId"/>&flag=audit">
											<img
											src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1009.png"
											title="认定结果" style="border-width: 0px;" /> </a></td>
								</tr>
							</s:iterator>
						</table>
					</div>
					<div id="anpBoud" align="Right" style="width:100%;">
						<table width="100%" cellspacing="0" border="0">
							<tr>
								<td align="right"><s:component template="pagediv" />
								</td>
							</tr>
						</table>
					</div>
				</td>
			</tr>
		</table>
	</form>
	<c:import url="${webapp}/page/webctrl/instTree/tree_include.jsp"
		charEncoding="UTF-8">
		<c:param name="treeType" value="radio" />
		<c:param name="bankName_tree" value="inst_Name" />
		<c:param name="bankId_tree" value="inst_id" />
		<c:param name="taskId_tree" value="" />
		<c:param name="task_tree" value="task_tree" />
		<c:param name="webapp" value="${webapp}" />
	</c:import>
</body>
</html>