<!-- file: <%=request.getRequestURI()%> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal" import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.util.PaginationList"
	import="com.cjit.vms.trans.model.billInvalid.BillCancelInfo"
	import="com.cjit.vms.trans.model.BillInfo"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../../page/include.jsp"%>
<OBJECT id='DocCenterCltObj' width=100 height=100
	classid="clsid:1E25664C-EA53-4F39-8575-684737626D6F"
	style="display: none;"></OBJECT>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<title>增值税管理平台</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%=bopTheme2%>/css/main.css" type="text/css"
	rel="stylesheet">
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/calendar_notime.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/XmlHttp.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/search.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/ylb.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/lhb.js"></script>
<script type="text/javascript"
	src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/search.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script type="text/javascript">
	var msg = '<s:property value="message" escape="false" />';
	if (msg != null && msg != '') {
		alert(msg);
	}
	function openPaperInvoice(url) {
		OpenModalWindow(url, 650, 350, true);
		document.forms[0].action = "SelectNoneAction";
	}
	function saveVendorSuccess() {
		document.forms[0].submit();
	}
	// [查询]按钮
	function query() {
		submitAction(document.forms[0], "SelectNoneAction.action");
	}

	// 撤销按钮
	function revokeBill() {
		var t = "";
		var inputs = document.getElementsByName("selectBillIds");
		for (var i = 0; i < inputs.length; i++) {
			if (inputs[i].checked == true) {
				t += inputs[i].value + ",";
			}
		}
		if (t.length == 0) {
			alert("请选择票据记录！");
			return;
		}
		if (confirm("确定将选中票据进行撤销处理？")) {
			$.ajax({
				url : 'revokeBillNoneCancel.action',
				type : 'POST',
				async : false,
				data : {
					invoiceId : t.substring(0, t.length - 1)
				},
				dataType : 'text',
				error : function() {
					return false;
				},
				success : function(result) {
					if (result == '1') {
						alert("撤销成功!");
						document.forms[0].submit();
					} else {
						alert("撤销失败!");
					}
				}
			});
		} else {
			return;
		}
	}

	// 导出按钮
	function exportCancelBill() {
		submitAction(document.forms[0], "cancelNoneBillToExcel.action");
		document.forms[0].action = "SelectNoneAction";
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
			document.forms[0].action = "SelectNoneAction";
		}
	}
</script>
<style type="text/css">
.style1 {
	width: 242px;
}
</style>
</head>
<body onkeydow="enterkey(event)">
	<form name="Form1" method="post" action="SelectNoneAction.action"
		id="Form1">
		<%
			String currMonth = (String) request.getAttribute("currMonth");
		%>
		<table id="tbl_main" cellpadding="0" cellspacing="0"
			class="tablewh100">
			<tr>
				<td class="centercondition"><s:component
						template="rocketMessage" />
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> <span
							class="current_status_menu">当前位置：</span> <span
							class="current_status_submenu1">销项税管理</span> <span
							class="current_status_submenu">库存管理</span> <span
							class="current_status_submenu">发票处理</span>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td style="text-align: right;" width="8%">领购日期</td>
								<td width="25%"><input class="tbl_query_time"
									id="startTime" type="text" name="startTime"
									value="<s:property value='billBeginDate'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endTime\')}'})"
									size='11' "/>--<input class="tbl_query_time" id="endTime"
									type="text" name="endTime"
									value="<s:property value='billEndDate'/>"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startTime\')}'})"
									size='11' "/></td>
								<td style="text-align: right;" width="8%">发票号码</td>
								<td><input id="invoiceNo" class="tbl_query_text"
									name="invoiceNo" type="text"
									value="<s:property value='invoiceNo' />" /></td>
								<td style="text-align: right;" width="10%">发票类型：</td>
								<td width="20%"><select style="width: 150px;"
									id="invoiceType" name="invoiceType">
										<option value="">全部</option>
										<option value="0"
											<s:if test='invoiceType=="0"'>selected</s:if>
											<s:else></s:else>>增值税专用发票</option>
										<option value="1"
											<s:if test='invoiceType=="1"'>selected</s:if>
											<s:else></s:else>>增值税普通发票</option>
								</select></td>
								<td></td>		
								<td width="10%">
							<input type="button"
									class="tbl_query_button" value="查询"
									onMouseMove="this.className='tbl_query_button_on'"
									onMouseOut="this.className='tbl_query_button'" name="BtnView"
									id="BtnView" onclick="query()" /></td>										
							   </tr>
							<tr>
								<td style="text-align: right;" width="8%">发票状态</td>
								<td width="20%"><select style="width: 150px;"
									id="invoiceStatus" name="invoiceStatus">
										<option value="">全部</option>
										<option value="2"
											<s:if test='invoiceStatus=="2"'>selected</s:if>
											<s:else></s:else>>发票作废</option>
										<option value="3"
											<s:if test='invoiceStatus=="3"'>selected</s:if>
											<s:else></s:else>>发票红冲</option>
											<option value="5"
											<s:if test='invoiceStatus=="5"'>selected</s:if>
											<s:else></s:else>>空白作废</option>
				
								</select></td>
			
							
							</tr>
						</table>
					</div>
					<table id="tbl_tools" width="100%" border="0">
						<tr>
							<td>&nbsp;&nbsp;</td>
							<td align="left"><a href="#"
								onClick="openPaperInvoice('addBillNoneCancel.action');"> <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon16.png" />
									新增
							</a> <a href="#" onClick="revokeBill()"> <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1032.png" />
									撤销
							</a> <%-- <a href="#" onClick="exportCancelBill()"> <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />
									导出
							</a> --%></td>
						</tr>
					</table>
					<div id="lessGridList4" style="overflow: auto; width: 100%;">
						<table class="lessGrid" cellspacing="0" rules="all" border="0"
							cellpadding="0" style="border-collapse: collapse; width: 100%;">
							<tr class="lessGrid head">
								<th style="text-align: center"><input
									style="width: 13px; height: 13px;" id="CheckAll"
									type="checkbox" onclick="cbxselectall(this,'selectBillIds')" />
								</th>
								<th style="text-align: center">序号</th>
								<th style="text-align: center">领购日期</th>
								<th style="text-align: center">领购人员</th>
								<th style="text-align: center">发票类型</th>
								<th style="text-align: center">发票代码</th>
								<th style="text-align: center">发票号码</th>
								<th style="text-align: center">状态</th>
								<th style="text-align: center">操作时间</th>
								<th style="text-align: center">备注</th>
							</tr>
							<s:iterator value="paginationList.recordList" id="iList"
								status="stuts">
								<tr align="center"
									class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
									<td><input type="checkbox"
										style="width: 13px; height: 13px;" name="selectBillIds"
										value="<s:property value="#iList.invoiceId"/>"</td>


									<td align="center"><s:property value='#stuts.count' /></td>
									<td><s:property value="receiveInvoiceTime" /></td>
									<td><s:if test="userName==null||userName==''">
											<s:property value="userId" />
										</s:if> <s:else>
											<s:property value="userName" />
										</s:else></td>
									<td readonly="readonly"><s:if test="invoiceType==0">增值税专用发票</s:if>
										<s:elseif test="invoiceType==1">增值税普通发票</s:elseif></td>
									<td><s:property value="invoiceCode" /></td>
									<td><s:property value="invoiceNo" /></td>
									<td readonly="readonly"><s:if test="invoiceStatus==5">空白作废</s:if>
										<s:elseif test="invoiceStatus==3">已红冲</s:elseif> <s:elseif
											test="invoiceStatus==2">已作废</s:elseif></td>
									<td><s:property value="changeTime" /></td>
									<td><a href="javascript:void(0);"
										onclick="openPaperInvoice('addBillNoneInvoiceReason.action?invoiceId=<s:property value="#iList.invoiceId"/>&invoiceCode=<s:property value="#iList.invoiceCode"/>&invoiceNo=<s:property value="#iList.invoiceNo"/>');">
											<img
											src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1014.png"
											title="查看备注" style="border-width: 0px;" />
									</a></td>
								</tr>
							</s:iterator>
						</table>
					</div>
					<div id="anpBoud" align="Right" style="width:100%;">
                   	<table width="100%" cellspacing="0" border="0">
							<tr>
								<input type="hidden" name="paginationList.showCount"
									value="false" />
								<td align="right"><s:component template="pagediv" /></td>
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