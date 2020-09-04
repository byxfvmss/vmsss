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
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/editview.js"></script>
<meta http-equiv="Pragma" content="no-cache" />
<title>进项税转出比例金额</title>
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

.ellipsis_div {
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
}
</style>
<script type="text/javascript">
	function reset(url) {
		//OpenModalWindow(url,650,350,true);
		location.href = url;
	}
	// 打开空白发票作废页面
	function invalidBlankPaperInvoice() {
		var url = 'invalidBlankPaperInvoice.action';
		OpenModalWindow(url, 650, 250, true);
	}
	function submitForm(actionUrl) {
		var form = document.getElementById("main");
		form.action = actionUrl;
		form.submit();
		form.action = "listInvoiceInSurtax.action";
	}

	function deleteTransBatch(actionUrl) {

		var form = document.getElementById("main");
		form.action = actionUrl;
		form.submit();
		form.action = "listInvoiceInSurtax.action";
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
	<form id="main" action="<c:out value='${webapp}'/>/listInvoiceInSurtax.action" method="post">
		<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
			<tr>
				<td class="centercondition">
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" />
						<span class="current_status_menu">当前位置：</span>
						<span class="current_status_submenu1">进项税管理</span>
						<span class="current_status_submenu">认证管理</span>
						<span class="current_status_submenu">进项转出</span>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td>开票日期</td>
								<td>
									<input class="tbl_query_text" id="billDate" name="billDate" type="text" value="<s:property value='billDate' />" class="tbl_query_time" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
									&nbsp;&nbsp;&nbsp;
								</td>
								<td>供应商纳名称</td>
								<td>
									<input class="tbl_query_text" id="vendorName" name="vendorName" type="text" value="<s:property value='vendorName'   />" maxlength="50" />
								</td>
								<!-- 				<td>发票状态:&nbsp;&nbsp;&nbsp;</td>  -->
								<!-- 				<td> -->
								<!-- 				<select id="datastatus" name="datastatus" ><option value="" <s:if test='datastatus==""'>selected</s:if><s:else></s:else>>全部</option> -->
								<!-- 						<s:iterator value="mapDatastatus" id="entry">   -->
								<!-- 						<option value="<s:property value="key"/>" <s:if test='datastatus==#entry.key'>selected</s:if><s:else></s:else>><s:property value="value"/></option> -->
								<!-- 				       </s:iterator> -->
								<!-- 					</select> -->
								<!-- 				</td> -->
								<td>所属机构</td>
								<td>
									<input type="hidden" id="inst_id" name="instId" value='<s:property value="instId"/>'>
									<input type="text" class="tbl_query_text" id="inst_Name" name="instName" value='<s:property value="instName"/>' onclick="setOrg(this);" readonly="readonly">
									<%-- <select id="instId" name="instId" ><option value="" <s:if test='instId==""'>selected</s:if><s:else></s:else>>全部</option>
						<s:iterator value="lstAuthInstId" id="org">  
						<option value="<s:property value="#org.id"/>" <s:if test='instId==#org.id'>selected</s:if><s:else></s:else>><s:property value="#org.name"/></option>
				       </s:iterator>
					</select> --%>
						<!-- 		</td>
								<td>转出金额</td>
								<td>
									<input type="text" class="tbl_query_text" name="vatOutAmt" id="vatOutAmt" value="<s:property value='vatOutAmt'/>" maxlength="10" onkeypress="checkkey(value);" />
								</td> -->
							</tr>
							<tr>
								<td>发票代码</td>
								<td>
									<input id="billCode" name="billCode" type="text" class="tbl_query_text" value="<s:property value='billCode' />" maxlength="10" onkeypress="checkkey(value);" />
									&nbsp;&nbsp;&nbsp;
								</td>
								<td>发票号码</td>
								<td>
									<input type="text" class="tbl_query_text" name="billNo" id="billNo" value="<s:property value='billNo'/>" maxlength="8" onkeypress="checkkey(value);" />
								</td>
								<%-- <td>发票种类:&nbsp;&nbsp;&nbsp;</td> 
				<td>
					<select id="fapiaoType" name="fapiaoType" ><option value="" <s:if test='fapiaoType==""'>selected</s:if><s:else></s:else>>全部</option>
						<s:iterator value="mapVatType" id="entry">  
						<option value="<s:property value="key"/>" <s:if test='surtaxType==#entry.key'>selected</s:if><s:else></s:else>><s:property value="value"/></option>
				       </s:iterator>
					</select>
				</td> --%>
								<td colspan="4">
									<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
										onclick="submitForm('listInvoiceInSurtax.action');" name="cmdQueryBtn" value="查询" id="cmdQueryBtn"
									/>
								</td>
							</tr>
						</table>
					</div>
					<table id="tbl_tools" width="100%" border="0">
						<tr align="left">
							<td style="text-align:left">
								<!-- 				<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'"  -->
								<!-- 				onMouseOut="this.className='tbl_query_button'" onclick="reset('listInSurtax.action');" name="cmdFilter" value="重置" id="cmdFilter" />				 -->
								<!--				<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" -->
								<!--				onMouseOut="this.className='tbl_query_button'"  onclick="" -->
								<!--				name="cmdRollOutSubmitBtn" value="转出提交" id="cmdRollOutSubmitBtn" />	-->
								<a href="#" id="cmdRollOutSubmitBtn" name="cmdRollOutSubmitBtn">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1036.png" />
									转出提交
								</a>
								<!--				<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" -->
								<!--				onMouseOut="this.className='tbl_query_button'"  onclick="" -->
								<!--				name="cmdBatchRollOutBtn" value="批量转出" id="cmdBatchRollOutBtn" />	-->
								<a href="#" id="cmdBatchRollOutBtn" name="cmdBatchRollOutBtn">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1037.png" />
									批量转出
								</a>
								<!--				<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" -->
								<!--				onMouseOut="this.className='tbl_query_button'"  onclick="" -->
								<!--				name="cmdRollBackBtn" value="撤回数据" id="cmdRollBackBtn" />	-->
								<a href="#" id="cmdRollBackBtn" name="cmdRollBackBtn">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1032.png" />
									撤回数据
								</a>
								<!--				<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" -->
								<!--				onclick="submitForm('invoiceInSurtaxExcel.action');" name="cmdExcel" value="导出EXCEL" id="cmdExcel" />		-->
								<a href="#" onclick="submitForm('invoiceInSurtaxExcel.action');" name="cmdExcel" id="cmdExcel">
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
									<input id="CheckAll" style="width:13px;height:13px;" type="checkbox" onClick="checkAll(this,'billId')" />
								</th>
								<th style="text-align: center">发票代码</th>
								<th style="text-align: center">发票号码</th>
								<th style="text-align: center">开票日期</th>
								<th style="text-align: center">金额</th>
								<th style="text-align: center">税额</th>
								<th style="text-align: center">发票种类</th>
								<th style="text-align: center">供应商名称</th>
								<th style="text-align: center">供应商纳税人识别号</th>
								<!-- 		<th style="text-align: center">业务凭证编号</th> -->
								<th style="text-align: center">认证结果</th>
								<th style="text-align: center">转出金额</th>
								<th style="text-align: center;">认证日期</th>
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
									<td>
										<s:property value='#iList.billNo' />
									</td>
									<td>
										<s:property value="#iList.billDate" />
									</td>
									<td>
										<s:property value='#iList.amtSum' />
									</td>
									<td>
										<s:property value='#iList.taxAmtSum' />
									</td>
									<td>
										<!-- 			    <s:if test='fapiaoType=="0"'>增值税专用发票</s:if><s:elseif test='fapiaoType=="1"'>增值税普通发票</s:elseif><s:else></s:else></td> -->
										<s:property value="mapVatType[#iList.fapiaoType]" />
									<td>
										<div class=ellipsis_div style="width:120px;" title="<s:property value='#iList.vendorName'/>">
											<s:property value='#iList.vendorName' />
										</div>
									</td>
									<td>
										<s:property value='vendorTaxno' />
									</td>
									<!-- 			<td><s:property value='userdNum'/></td> -->
									<td>
										<s:property value='#iList.datastatusName' />
									</td>
									<td>
										<s:property value='vatOutAmt' />
									</td>
									<td>
										<s:property value='verifyData' />
									</td>
									<td>
										<a href="javascript:;" class="edit_insurtax" rel="<s:property value="#iList.billId"/>">
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
		$(".edit_insurtax").click(
				function() {
					bill_id = $(this).attr("rel");
					OpenModalWindow(
							"<c:out value='${webapp}'/>/editInvoiceInSurtax.action?bill_id="
									+ bill_id, 800, 600, true);
				});
		//转出提交
		$("#cmdRollOutSubmitBtn").click(function() {
			$billIds = $("input[name='billId']:checked");
			if ($billIds.size() == 0) {
				alert("请选择您要转出的数据");
				return false;
			}
			submitForm('rollOutSubmitInvoiceInSurtax.action');
		});
		//撤回数据
		$("#cmdRollBackBtn").click(function() {
			$billIds = $("input[name='billId']:checked");
			if ($billIds.size() == 0) {
				alert("请选择您要撤回的数据");
				return false;
			}
			submitForm('batchRollBackInvoiceInSurtax.action');
		});
		//批量转出
		$("#cmdBatchRollOutBtn").click(function() {
			$billIds = $("input[name='billId']:checked");
			if ($billIds.size() == 0) {
				alert("请选择您要批量转出的数据");
				return false;
			}
			submitForm('batchRollOutInvoiceInSurtax.action');
		});
	});
</script>
</html>
