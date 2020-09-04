<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../../page/include.jsp"%>
<link href="<%=bopTheme2%>/css/main.css" type="text/css"
	rel="stylesheet">
<script type="text/javascript"
	src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript"
	src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/ylb.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/lhb.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/search.js"></script>
<meta http-equiv="Pragma" content="no-cache" />
<title>价税分离查询</title>
<script type="text/javascript">
	function submitForm(actionUrl) {
		var form = document.getElementById("conditionForm");
		form.action = actionUrl;
		form.submit();
		form.action = "listBusinessInfo.action";
	}

	function openWindows(url) {
		OpenModalWindow(encodeURI(url), 500, 400, true);
	}
	
	function delbusinessInfo(){
		if(checkChkBoxesSelected("selectBusinessIds")){
			if(!confirm("确定要删除选择的记录吗？")){
				return false;
			}
 			submitForm("delBusinessInfo.action");
		}else{
			alert("请选择要删除的记录");
		}
	}
	
	// [导入]按钮
		function importData(actionUrl){
			var fileId = document.getElementById("fileId");
			if(fileId.value.length > 0){
				if(fileId.value.lastIndexOf(".XLS") > -1||fileId.value.lastIndexOf(".xls")>-1){
					submitForm(actionUrl)
				}else{
					alert("文件格式不对，请上传Excel文件。");
				}
			}else{
				alert("请先选择要上传的文件。");
			}
		}
</script>
</head>
<body>
<form id="conditionForm" method="post" enctype="multipart/form-data">
	<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
		<tr>
			<td class="centercondition">
			<div id="tbl_current_status">
				<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> 
				<span class="current_status_menu">当前位置：</span> 
				<span class="current_status_submenu1">参数管理</span>
				<span class="current_status_submenu">业务参数管理</span>
				<span class="current_status_submenu">交易种类</span>
			</div>
			<div class="widthauto1">
				<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td><span>交易码</span></td>
<!--						<td><s:textfield name="business.businessCode" class="tbl_query_text2"></s:textfield></td>-->
						<td><input id="business.businessCode" class="tbl_query_text" name="business.businessCode" type="text"  value="<s:property value='business.businessCode' />" /></td>
						<td><span>交易名称</span></td>
<!--						<td><s:textfield name="business.businessCName" class="tbl_query_text2"></s:textfield></td>-->
						<td><input id="business.businessCName" class="tbl_query_text" name="business.businessCName" type="text"  value="<s:property value='business.businessCName' />" /></td>
						<td><input type="button" class="tbl_query_button"
							onMouseMove="this.className='tbl_query_button_on'"
							onMouseOut="this.className='tbl_query_button'"
							onclick="submitForm('listBusinessInfo.action');"
							name="cmdFilter" value="查询" id="cmdFilter" /></td>
					</tr>
				</table>
				</div>
				<!-- <table id="tbl_main" cellpadding="0" cellspacing="0">
					<tr>
						<td><input type="button" class="tbl_query_button"
							onMouseMove="this.className='tbl_query_button_on'"
							onMouseOut="this.className='tbl_query_button'" value="新增"
							onClick="openWindows('initBusinessInfo.action');" />

						<input type="button" class="tbl_query_button"
							onMouseMove="this.className='tbl_query_button_on'"
							onMouseOut="this.className='tbl_query_button'"
							onclick="delbusinessInfo();" name="cmdDel" value="删除" id="cmdDel" />
						<input type="button" class="tbl_query_button"
							onMouseMove="this.className='tbl_query_button_on'"
							onMouseOut="this.className='tbl_query_button'"
							onclick="importData('impBisinessinfo.action');"
							name="cmdFilter" value="导入" id="cmdFilter" />
							<s:file name="attachment" id="fileId"></s:file></td>
					</tr>
				</table> -->
		 		<table id="tbl_tools" width="100%" border="0">
					<tr>
						<td align="left" width="200">
						<a href="#" onClick="openWindows('initBusinessInfo.action');" ><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon16.png"/>新增</a>
						<a href="#" onclick="delbusinessInfo();"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1019.png"/>删除</a>
						</td><td width="255">
						<s:file name="attachment" id="fileId" size="30" style="height:26px;"></s:file>
						</td><td>
						<a href="#" onclick="importData('impBisinessinfo.action');"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1007.png"/>导入</a>
						</td>
					</tr>
				</table>
				<div id="lessGridList1" style="overflow: auto; width: 100%;">
					<table class="lessGrid" cellspacing="0" rules="all" border="0"
						cellpadding="0" style="border-collapse: collapse;width: 100%;">
						<tr class="lessGrid head">
							<th style="text-align:center"><input id="CheckAll"
								style="width: 13px; height: 13px;" type="checkbox"
								onClick="checkAll(this,'selectBusinessIds')" /></th>
							<th style="text-align: center;width:">序号</th>
							<th style="text-align: center">交易码</th>
							<th style="text-align: center">交易名称</th>
							<th style="text-align: center">描述</th>
							<th style="text-align: center">编辑</th>
			
						</tr>
						<s:iterator value="paginationList.recordList" id="iList"
							status="stuts">
							<tr align="center"
								class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
								<td style="width: 5%"><input type="checkbox"
									style="width: 13px; height: 13px;" name="selectBusinessIds"
									value="<s:property value="businessId"/>" /></td>
								<td style="width: 10%"><span><s:property
											value='#stuts.count' /></span></td>
								<td style="width: 15%"><span><s:property
											value='businessCode' /></span></td>
								<td style="width: 15%"><span><s:property
											value='businessCName' /></span></td>
								<td style="width: 45%;white-space: pre-line;"><span  style="width:100%;display:block;"><s:property
											value='businessNote' /></span></td>
								<td style="width: 10%"><a href="#"
									onClick="openWindows('initBusinessInfo.action?business.businessId=<s:property value="businessId" />')">
										<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1005.png" title="编辑"
										style="border-width: 0px;" />
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