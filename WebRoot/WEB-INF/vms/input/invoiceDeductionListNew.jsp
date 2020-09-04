<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../../page/include.jsp"%>
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
<link href="<%=webapp%>/page/common.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
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
		form.action = "listInvoiceDeductionNew.action";
	}

	function deducNew(){
		var ids = document.getElementsByName("billCode");
		var selectedIds = null;
		if (ids.length > 0) {
			for (var i = 0; i < ids.length; i++) {
				var id = ids[i];
				var j = 0;
				if (id.checked) {
					if (selectedIds != null) {
						selectedIds = selectedIds + "," + id.value;
					} else {
						selectedIds = id.value;
					}
					j++;
				}
			}
		}
		if (selectedIds == null) {
			alert("请选择要抵扣的记录");
		}else
		if(confirm("确认要抵扣该记录吗？")){
		submitAction(document.forms[0], "InvoiceDeductionNew.action?selectedIds="
				+ selectedIds);
		}
				document.forms[0].action="listInvoiceDeductionNew.action";
	}

	function deleteTransBatch(actionUrl) {

		var form = document.getElementById("main");
		form.action = actionUrl;
		form.submit();
		form.action = "listInvoiceDeduction.action";
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
		var elTable = document.getElementById("lessGridList"); //这里的page1 是包含表格的Div层的ID
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
	<form id="main" action="listInvoiceDeduction.action" method="post" enctype="multipart/form-data">
		<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
			<tr>
				<td class="centercondition">
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" />
						<span class="current_status_menu">当前位置：</span>
						<span class="current_status_submenu1">进项税管理</span>
						<span class="current_status_submenu">认证管理</span>
						<span class="current_status_submenu">进项抵扣</span>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td>开票日期</td>
								<td>
									<input type="text" class="tbl_query_text" name="billDate" id="billDate" value="<s:property value='billDate'/>" class="tbl_query_time" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
								</td>
								<td>供应商名称</td>
								<td>
									<input type="text" class="tbl_query_text" name="vendorName" id="vendorName" value="<s:property value='vendorName'/>" />
								</td>
								<td>所属机构</td>
								<!-- <td>
									&nbsp;
									<input type="hidden" id="inst_id" name="instId" value='<s:property value="instId"/>'>
									<input type="text" class="tbl_query_text" id="inst_Name" name="instName" value='<s:property value="instName"/>' onclick="setOrg(this);" readonly="readonly">
									
								</td> -->
								<td><s:select id="industryType"
                                          name="industryType" list="industryTypeList"
                                          headerKey="" headerValue="全部" listKey='code' listValue='name'
                                          cssClass="tbl_query_text"/>
                            </td>
								
							</tr>
							<tr>
								<td>发票代码</td>
								<td>
									<input type="text" class="tbl_query_text" name="billCode" id="billCode" maxlength="10" onkeypress="checkkey(value);" />
								</td>
								<td>发票号码</td>
								<td>
									<input type="text" class="tbl_query_text" name="billNo" id="billNo" maxlength="8" onkeypress="checkkey(value);" />
								</td>
								<td width="15%" align="right" class="listbar">发票状态:</td>
				<td width="35%">
					<select id="dataStatus" name="dataStatus">
						
						<option value="1" <s:if test='dataStatus=="1"'> selected </s:if>>已认证</option>
						<option value="3" <s:if test='dataStatus=="3"'>selected </s:if>>已抵扣</option>
					</select>
				</td>
								<%-- <td>发票种类:&nbsp;&nbsp;&nbsp;</td> 
						<td>
							<select id="fapiaoType" name="fapiaoType" ><option value="" <s:if test='fapiaoType==""'>selected</s:if><s:else></s:else>>全部</option>
								<s:iterator value="mapVatType" id="entry">  
								<option value="<s:property value="key"/>" <s:if test='fapiaoType==#entry.key'>selected</s:if><s:else></s:else>><s:property value="value"/></option>
						       </s:iterator>
							</select>
						</td> --%>
								<td colspan='4'>
									<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
										onclick="submitForm('listInvoiceDeductionNew.action');" name="cmdQueryBtn" value="查询" id="cmdQueryBtn"
									/>
								</td>
							</tr>
						</table>
					</div>
					<table id="tbl_tools" width="100%" border="0">
						<tr>
							<td align="left">
								<!--					<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" -->
								<!--					onclick="submitForm('exportInvoiceDeduction.action')" name="cmdImportBt" value="导出EXCEL" id="cmdImportBt" />	-->
								<a href="#" onclick="submitForm('exportInvoiceDeductionNew.action')" name="cmdImportBt" id="cmdImportBt">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />
									导出
								</a>
								<a href="#" onclick="deducNew()" name="cmdDeducBt" id="cmdDeducBt">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/down.png" />
									抵扣
								</a>
								<!--					<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" -->
								<!--					onclick="" name="cmdRollbackBt" value="撤回数据" id="cmdRollbackBt" />	
								<a href="#" name="cmdRollbackBt" id="cmdRollbackBt">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1032.png" />
									撤回数据
								</a>  -->
								<!-- 				<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'"  -->
								<!-- 				onMouseOut="this.className='tbl_query_button'"  onclick=""  -->
								<!-- 				name="cmdAuthSubmitBtn" value="认证提交" id="cmdAuthSubmitBtn" />				 -->
							</td>
						</tr>
					</table>
					<div id="lessGridList4" style="overflow: auto; width: 100%;">
						<table class="lessGrid" cellspacing="0" rules="all" border="0" cellpadding="0" style="border-collapse: collapse;width: 100%;">
							<tr class="lessGrid head">
								<th width="3%" style="text-align:center">
									<input id="CheckAll" style="width:13px;height:13px;" type="checkbox" onClick="cbxselectall(this,'billCode')" />
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
								<th style="text-align: center;">操作</th>
							</tr>
							<s:iterator value="paginationList.recordList" id="iList" status="stuts">
								<tr align="center" class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
									<td>
										<input type="checkbox" <s:if test='#iList.datastatus=="3"'>disabled</s:if> style="width:13px;height:13px;" name="billCode" value="<s:property value="#iList.billCode"/><s:property value="#iList.billNo"/>" />
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
									</td>
									<td>
										<s:property value='#iList.vendorName' />
									</td>
									<td>
										<s:property value="#iList.vendorTaxno" />
									</td>
									<td>
										<s:property value='mapDatastatus[#iList.datastatus]' />
									</td>
									
									<td>
										
										<a href="#" onclick = "OpenModalWindow('<%=webapp%>/invoiceDeductionListNew.action?billNoAndCode=<s:property value='#iList.billCode'/><s:property value='#iList.billNo'/>',800,600,'view')" class="cmdViewBt" >
											<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1014.png" title="查看" style="border-width: 0px;" />
										</a>
										<!-- 
										<a href="javascript:;" class="cmdEditBt" rel="<s:property value="#iList.BillId"/>">
											<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1005.png" title="编辑" style="border-width: 0px;" />
										</a>
										<a href="javascript:void(0)" onClick="OpenModalWindow('<%=webapp%>/inputInformationViewImg.action?dealNo=#iList.dealNo',850,600,'view') ">
											<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1015.png" title="票样" style="border-width: 0px;" />  -->
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
							//submitForm("<c:out value='${webapp}'/>/editInvoiceDeduction.action");
							OpenModalWindow(encodeURI("editInvoiceDeduction.action?o_bill_id="+$("#o_bill_id").val()), 1000, 700, true);
						});
			$("#cmdRollbackBt")
				.click(
						function() {
							$items = $("input[name='billId']:checked");
							if ($items.size() == 0) {
								alert("请选择您要撤回的数据！");
								return false;
							}
							submitForm("<c:out value='${webapp}'/>/rollbackInvoiceDeduction.action");
						});
	});
</script>
</html>