<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../../page/include.jsp"%>
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
<link href="<%=webapp%>/page/common.css" type="text/css" rel="stylesheet">
<!-- MessageBox -->
<script src="<%=bopTheme%>/js/MessageBox/js/messageBox.js" type="text/javascript"></script>
<link href="<%=bopTheme%>/js/MessageBox/css/messageBox.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=bopTheme%>/js/validator.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/search.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/editview.js"></script>
<meta http-equiv="Pragma" content="no-cache" />
<title>进项税管理</title>
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
	function reset(url) {
		//OpenModalWindow(url,650,350,true);
		location.href = url;
	}
	function submitForm(actionUrl) {
		var form = document.getElementById("main");
		form.action = actionUrl;
		form.submit();
		form.action = "listInvoiceScanAuth.action";
	}

	function deleteTransBatch(actionUrl) {

		var form = document.getElementById("main");
		form.action = actionUrl;
		form.submit();
		form.action = "listInvoiceScanAuth.action";
	}

	function hideDetailInfoDIV() {
		document.getElementById("detailInfoDIV").style.display = 'none';
	}

	function showDetailInfoDIV(logID) {
		var currRow = window.event.srcElement.parentElement.parentElement;// 获取当前行

		document.getElementById("_td2").innerHTML = logID;

		for (var i = 3; i < 16; i++) {
			document.getElementById("_td" + i).innerHTML = currRow.cells[i - 1].title;
		}

		document.getElementById("detailInfoDIV").style.display = 'block';
	}

	function output() {
		//拷贝 
		var elTable = document.getElementById("lessGridList1"); //这里的page1 是包含表格的Div层的ID
		var oRangeRef = document.body.createTextRange();
		oRangeRef.moveToElementText(elTable);
		oRangeRef.execCommand("Copy");

		//粘贴 
		try {
			var appExcel = new ActiveXObject("Excel.Application");
			appExcel.Visible = true;
			appExcel.Workbooks.Add().Worksheets.Item(1).Paste();
			//appExcel = null; 
		} catch (e) {
			alert("使用此功能必须在浏览器中设置:Internet选项->安全->将本站加入“受信任的站点”。");
		}

		var elTable1 = document.getElementById("lessGridList");
		var oRangeRef1 = document.body.createTextRange();
		oRangeRef1.moveToElementText(elTable1);
		oRangeRef1.execCommand("Copy");

		//粘贴 
		try {
			appExcel.Visible = true;
			appExcel.Worksheets.Item(2).Paste();
			appExcel1 = null;
		} catch (e) {
			alert("使用此功能必须在浏览器中设置:Internet选项->安全->将本站加入“受信任的站点”。");
		}
	}
</script>
</head>
<body>
	<form id="main" action="<c:out value='${webapp}'/>/listInvoiceScanAuth.action" method="post" enctype="multipart/form-data">
		<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
			<tr>
				<td class="centercondition">
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" />
						<span class="current_status_menu">当前位置：</span>
						<span class="current_status_submenu1">进项税管理</span>
						<span class="current_status_submenu">认证管理</span>
						<span class="current_status_submenu">扫描认证</span>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" border="0" width="100%">
							<tr>
								<td>开票日期</td>
								<td>
									<input type="text" class="tbl_query_text" name="billDate" id="billDate" value="<s:property value='billDate'/>" class="tbl_query_time" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
								</td>
								<td>供应商名称</td>
								<td>
									<input type="text" class="tbl_query_text" name="customerName" id="customerName" value="<s:property value='customerName'/>" maxlength="100" />
								</td>
								<td>所属机构</td>
								<td>
									<input type="hidden" id="inst_id" name="instId" value='<s:property value="instId"/>'>
									<input type="text" class="tbl_query_text" id="inst_Name" name="instName" value='<s:property value="instName"/>' onclick="setOrg(this);" readonly="readonly">
									<%-- <select id="instId" name="instId">
										<option value="" <s:if test='instId==""'>selected</s:if> <s:else></s:else>>全部</option>
										<s:iterator value="lstAuthInstId" id="org">
											<option value="<s:property value="#org.id"/>" <s:if test='instId==#org.id'>selected</s:if> <s:else></s:else>><s:property value="#org.name" /></option>
										</s:iterator>
									</select> --%>
								</td>
							</tr>
							<tr>
								<td>发票代码</td>
								<td>
									<input type="text" class="tbl_query_text" name="billCode" id="billCode" value="<s:property value='billCode'/>" maxlength="10" onkeypress="checkkey(value);" />
								</td>
								<td>发票号码</td>
								<td>
									<input type="text" class="tbl_query_text" name="billNo" id="billNo" value="<s:property value='billNo'/>" maxlength="8" onkeypress="checkkey(value);" />
								</td>
								<td>认证日期</td>
								<td>
									<input type="text" class="tbl_query_text" name="identifyDate" id="identifyDate" value="<s:property value='identifyDate'/>" class="tbl_query_time" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
								</td>
							</tr>
							<tr>
								<td>发票状态</td>
								<td>
									<select id="datastatus" name="datastatus">
										<option value="" <s:if test='datastatus==""'>selected</s:if> <s:else></s:else>>全部</option>
										<s:iterator value="mapDatastatus" id="entry">
											<option value="<s:property value="key"/>" <s:if test='datastatus==#entry.key'>selected</s:if> <s:else></s:else>><s:property value="value" /></option>
										</s:iterator>
									</select>
								</td>
								<td>发票种类</td>
								<td>
									<select id="fapiaoType" name="fapiaoType">
										<option value="" <s:if test='fapiaoType==""'>selected</s:if> <s:else></s:else>>全部</option>
										<s:iterator value="mapVatType" id="entry">
											<option value="<s:property value="key"/>" <s:if test='fapiaoType==#entry.key'>selected</s:if> <s:else></s:else>><s:property value="value" /></option>
										</s:iterator>
									</select>
								</td>
								<td colspan=2>
									<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
										onclick="submitForm('listInvoiceScanAuth.action');" name="cmdQueryBtn" value="查询" id="cmdQueryBtn"
									/>
								</td>
							</tr>
						</table>
					</div>
					<table id="tbl_tools" width="100%" border="0">
						<tr>
							<td align="left" width="168">
								<a href="javascript:;" id="cmdAuthSubmitBtn" name="cmdAuthSubmitBtn">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1036.png" />
									认证提交
								</a>
								<!--					<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" -->
								<!--					onMouseOut="this.className='tbl_query_button'"  onclick="" -->
								<!--					name="cmdAuthSubmitBtn" value="认证提交" id="cmdAuthSubmitBtn" />				-->
							</td>
							<td width="234">
								<input type="file" name="theFile" id="theFile" style="height:26px;" />
							</td>
							<td>
								<a href="javascript:;" id="cmdImportBt" name="cmdImportBt">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1007.png" />
									导入
								</a>
								<!--					<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" -->
								<!--					onclick="" name="cmdImportBt" value="导入" id="cmdImportBt" />				-->
								<!--					<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" -->
								<!--					 onclick="submitForm('exportInvoiceScanAuth.action')" name="cmdDelbt" value="导出" id="cmdDelbt" />		-->
								<a href="javascript:void();" onclick="submitForm('exportInvoiceScanAuth.action')">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />
									导出
								</a>
							</td>
						</tr>
					</table>
					<div id="lessGridList3" style="overflow: auto; width: 100%;">
						<table class="lessGrid" cellspacing="0" rules="all" border="0" cellpadding="0" style="border-collapse: collapse;width: 100%;">
							<tr class="lessGrid head">
								<th width="3%" style="text-align:center">
									<input id="CheckAll" style="width:13px;height:13px;" type="checkbox" onClick="checkAll(this,'billId')" />
								</th>
								<th style="text-align: center">发票代码</th>
								<th style="text-align: center">发票号码</th>
								<th style="text-align: center">开票日期</th>
								<th style="text-align: center">所属机构</th>
								<th style="text-align: center">金额</th>
								<th style="text-align: center">税额</th>
								<th style="text-align: center">发票种类</th>
								<th style="text-align: center">供应商名称</th>
								<th style="text-align: center">供应商纳税人识别号</th>
								<th style="text-align: center;">发票状态</th>
								<th style="text-align: center;">认证日期</th>
								<th style="text-align: center;">扫描时间</th>
								<th style="text-align: center;">操作</th>
							</tr>
							<s:iterator value="paginationList.recordList" id="iList" status="stuts">
								<tr align="center" class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
									<td>
										<input type="checkbox" style="width:13px;height:13px;" name="billId" value="<s:property value="#iList.billId"/>" />
									</td>
									<td align="center">
										<s:property value='#iList.billCode' />
									</td>
									<td align="center">
										<s:property value='#iList.billNo' />
									</td>
									<td>
										<s:property value='#iList.billDate' />
									</td>
									<td>
										<s:property value="#iList.instName" />
									</td>
									<td>
										<s:property value='#iList.amtSum' />
									</td>
									<td>
										<s:property value='#iList.taxAmtSum' />
									</td>
									<td align="center">
										<s:property value='mapVatType[#iList.fapiaoType]' />
										<input type="hidden" id="fapiaoType_<s:property value="#iList.billId"/>" value="<s:property value='#iList.fapiaoType'/>">
									</td>
									<td>
										<s:property value='#iList.vendorName' />
									</td>
									<td>
										<s:property value="vendorTaxno" />
									</td>
									<td>
										<s:property value='mapDatastatus[#iList.datastatus]' />
										<input type="hidden" id="datastatus_<s:property value="#iList.billId"/>" value="<s:property value='#iList.datastatus'/>">
									</td>
									<%-- 			<td><s:property value='mapDatastatus[#iList.datastatus]' /></td> --%>
									<td>
										<s:property value='#iList.identifyDate' />
									</td>
									<td>
										<s:property value='#iList.scanDate' />
									</td>
									<td>
										<a href="javascript:;" class="cmdEditBt" rel="<s:property value="#iList.BillId"/>">
											<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1005.png" title="编辑" style="border-width: 0px;" />
										</a>
										<a href="javascript:;" class="cmdViewBt" rel="<s:property value="#iList.BillId"/>">
											<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1014.png" title="查看" style="border-width: 0px;" />
										</a>
										<a href="javascript:void(0)" onClick="OpenModalWindow('<%=webapp%>/inputInformationViewImg.action?dealNo=#iList.dealNo',850,600,'view') ">
											<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1015.png" title="票样" style="border-width: 0px;" />
										</a>
									</td>
								</tr>
							</s:iterator>
							<input type="hidden" name="currentPage" value="<s:property value="paginationList.currentPage"/>" />
							<input type="hidden" name="o_bill_id" id="o_bill_id" value="" />
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
	<c:import url="${webapp}/page/webctrl/instTree/tree_include.jsp" charEncoding="UTF-8">
		<c:param name="treeType" value="radio" />
		<c:param name="bankName_tree" value="inst_Name" />
		<c:param name="bankId_tree" value="inst_id" />
		<c:param name="taskId_tree" value="" />
		<c:param name="task_tree" value="task_tree" />
		<c:param name="webapp" value="${webapp}" />
	</c:import>
</body>
<script type="text/javascript">
	$(function() {
		$(".cmdEditBt")
				.click(
						function() {
							$o_bill_id = $(this).attr("rel");
							$("#o_bill_id").val($o_bill_id);
							submitForm("<c:out value='${webapp}'/>/editInvoiceScanAuth.action");
						});
		$(".cmdViewBt")
				.click(
						function() {
							$o_bill_id = $(this).attr("rel");
							$("#o_bill_id").val($o_bill_id);
							submitForm("<c:out value='${webapp}'/>/viewInvoiceScanAuth.action");
						});

		//		$(".cmdImportBt").click(function(){
		// 			$fileName=$(this).theFile.getFileName();
		// 			if(null==$fileName||$fileName.equals("")){
		// 				alert("文件路径不能为空!");
		// 				return false;
		// 			}
		//			submitForm('importInvoiceScanAuth.action')
		//		});
		$("#cmdAuthSubmitBtn")
				.click(
						function() {
							$items = $("input[name='billId']:checked");
							
							if ($items.size() == 0) {
								alert("请选择您要认证提交的数据！");
								return false;

							}
							
							for ($idx = 0; $idx < $items.size(); $idx++) {
								$item_datastatus = $(
										"#datastatus_" + $($items[$idx]).val())
										.val();
								if (!($item_datastatus == '3'
										|| $item_datastatus == '5' || $item_datastatus == '7')) {
									if ($items.size() == 1) {
										alert("认证未通过的票据，不可以进行认证提交!");
									} else {
										alert("选中列表中存在认证未通过的票据，认证未通过的票据，不可以进行认证提交!");
									}
									return false;
								}
								$item_fapiaoType = $(
										"#fapiaoType_" + $($items[$idx]).val())
										.val();
								if ($item_fapiaoType == '1') {
									alert("增值税普通发票不可以进行认证提交!");
									return false;
								}
							}

							// 			$items=$("input[name='billId']:checked");
							// 			for(var i:int=0;i<$items.size();i++)
							// 			if(#iList.datastatus)
							// #iList.billId

							// 			$items=$("input[name='billId']:checked");
							// 			if($items.size()==0){
							// 				alert("请选择您要认证提交的数据！");
							// 				return false;

							// 			}
							//			var billids=document.
							// 			for(var i:int=0;i<$items.size();i++){
							// 				if($items[i].datastatus=='3'){
							// 					alert("请选择您据！");
							// 					return false;
							// 				}
							// 			}

							for ($idx = 0; $idx < $items.size(); $idx++) {
								
								$item_datastatus = $(
										"#datastatus_" + $($items[$idx]).val())
										.val();
								if (!($item_datastatus == '3'
										|| $item_datastatus == '5' || $item_datastatus == '7')) {
									alert("当前选中的数据包含未认证通过的数据，请确认数据状态。");
									return false;
								}
							}
if(confirm("确认认证提交吗？")) {
									
							submitForm("<c:out value='${webapp}'/>/authSubmit.action");
								}
						});

		$("#cmdImportBt").click(
				function() {
					if (fucCheckNull(document.getElementById("theFile"),
							"导入文件不能为空") == false) {
						return false;
					}
					submitForm('importInvoiceScanAuth.action');
				});
	});
</script>
</html>