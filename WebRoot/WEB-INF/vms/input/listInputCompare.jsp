<!--file: <%=request.getRequestURI()%> -->
<%@ page language="java" import="java.util.*" import="java.math.BigDecimal" import="com.opensymphony.util.BeanUtils" import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils" import="com.cjit.common.util.PaginationList" import="com.cjit.vms.input.model.InformationInput" import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%>
<%@ include file="../../../page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<title>增值税管理平台</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/calendar_notime.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/XmlHttp.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/search.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/ylb.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/search.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/window.js"></script>
<script type="text/javascript">
	// [查询]按钮
	function submitBill() {
		submitAction(document.forms[0], "listInputCompare.action");
		document.forms[0].action="listInputCompare.action";
	}

	function submitExport() {
		submitAction(document.forms[0], "exportInformationInput.action");
		document.forms[0].action="listInputCompare.action";
	}
</script>
<style type="text/css">
.style1 {
	width: 242px;
}
</style>
</head>
<body>
	<form name="main" method="post" action="listInputCompare.action" id="main">
		<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
			<tr>
				<td class="centercondition">
					<s:component template="rocketMessage" />
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" />
						<span class="current_status_menu">当前位置：</span>
						<span class="current_status_submenu1">进项税管理</span>
						<span class="current_status_submenu">综合查询</span>
						<span class="current_status_submenu">交易查询</span>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" width="100%" border="0">
							<tr>
								<td align="left">交易时间</td>
								<td>
									<input class="tbl_query_time" id="billBeginDate" type="text" name="billBeginDate" value="<s:property value='informationInput.transBeginDate'/>"
										onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'billBeginDate\')}'})" size='11'
									"/>
									--
									<input class="tbl_query_time" id="billEndDate" type="text" name="billEndDate" value="<s:property value='informationInput.transEndDate'/>"
										onfocus="WdatePicker({minDate:'#F{$dp.$D(\'billEndDate\')}'})" size='11'
									"/>
								</td>
								<td>供应商名称</td>
								<td>
									<input type="text" class="tbl_query_text" name="vendorName" value="<s:property value='informationInput.vendorName'/>" />
								</td>
								<td>交易编号</td>
								<td>
									<input type="text" class="tbl_query_text" name="dealNo" value="<s:property value='informationInput.dealNo'/>" />
								</td>
							</tr>
							<tr>
								<td>交易机构</td>
								<td>
									<input type="hidden" id="inst_id" name="bankCode" value='<s:property value="bankCode"/>'>
									<input type="text" class="tbl_query_text" id="inst_Name" name="bankName" value='<s:property value="informationInput.bankName"/>' onclick="setOrg(this);" readonly="readonly">
									<%-- 	<s:if test="authInstList != null && authInstList.size > 0">
					<s:select name="bankCode" list="authInstList" listKey='id' listValue='name' headerKey="" headerValue="所有" value='informationInput.bankCode'  />
					</s:if>
					<s:if test="authInstList == null || authInstList.size == 0">
					<select name="bankCode" class="readOnlyText">
					<option value="">请分配机构权限</option>
					</select>
				</s:if> --%>
								</td>
								<td>供应商纳税人识别号</td>
								<td>
									<input type="text" class="tbl_query_text" name="vendorTaxNo" value="<s:property value='informationInput.vendorTaxNo'/>" />
								</td>
								<td align="right" colspan="2">
									<input type="button" class="tbl_query_button" value="查询" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" name="BtnView" id="BtnView"
										onclick="submitBill()"
									/>
									<!--					<input type="button" class="tbl_query_button" value="导出"-->
									<!--					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" -->
									<!--					name="upLoad" id="upLoad" onclick="submitExport()"/>-->
								</td>
							</tr>
						</table>
					</div>
					<table id="tbl_tools" width="100%" border="0">
						<tr>
							<td align="left">
								<a href="#" name="upLoad" id="upLoad" onclick="submitExport()">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />
									导出
								</a>
							</td>
						</tr>
					</table>
					<div id="lessGridList4" style="overflow: auto; width: 100%;">
						<table class="lessGrid" cellspacing="0" rules="all" border="0" cellpadding="0" style="border-collapse: collapse; width: 100%;">
							<tr class="lessGrid head">
								<th style="text-align:center">
									<input style="width:13px;height:13px;" id="CheckAll" type="checkbox" onclick="cbxselectall(this,'selectInputTransIds')" />
								</th>
								<th style="text-align:center">序号</th>
								<th style="text-align:center">交易时间</th>
								<th style="text-align:center">交易编号</th>
								<th style="text-align:center">交易金额</th>
								<th style="text-align:center">税额</th>
								<th style="text-align:center">交易机构</th>
								<th style="text-align:center">供应商编号</th>
								<th style="text-align:center">供应商名称</th>
								<th style="text-align:center">供应商纳税人识别号</th>
								<th style="text-align:center">发票代码</th>
								<th style="text-align:center">发票号码</th>
								<th style="text-align:center">操作</th>
							</tr>
							<%
								PaginationList paginationList = (PaginationList) request
										.getAttribute("paginationList");
								if (paginationList != null) {
									List inputTransList = (List) paginationList.getRecordList();
									if (inputTransList != null && inputTransList.size() > 0) {
										for (int i = 0; i < inputTransList.size(); i++) {
											InformationInput inputTrans = (InformationInput) inputTransList
													.get(i);
											if (i % 2 == 0) {
							%>
							<tr class="lessGrid rowA">
								<%
									} else {
								%>
							
							<tr class="lessGrid rowB">
								<%
									}
								%>
								<td align="center">
									<input style="width:13px;height:13px;" type="checkbox" name="selectInputTransIds" value="<%=BeanUtils.getValue(inputTrans, "dealNo")%>" />
								</td>
								<td align="center"><%=i + 1%></td>
								<td align="center"><%=(inputTrans.getTransDate() == null) ? ""
								: inputTrans.getTransDate()%></td>
								<td align="center"><%=(inputTrans.getDealNo() == null) ? ""
								: inputTrans.getDealNo()%></td>
								<td align="center"><%=NumberUtils.format(inputTrans.getAmtCny(),
								"", 2)%></td>
								<td align="center"><%=NumberUtils.format(inputTrans.getTaxAmtCny(),
								"", 2)%></td>
								<td align="center"><%=(inputTrans.getBankCode() == null) ? ""
								: inputTrans.getBankName()%></td>
								<td align="center"><%=(inputTrans.getVendorId() == null) ? ""
								: inputTrans.getVendorId()%></td>
								<td align="center"><%=(inputTrans.getVendorName() == null) ? ""
								: inputTrans.getVendorName()%></td>
								<td align="center"><%=(inputTrans.getVendorTaxNo() == null) ? ""
								: inputTrans.getVendorTaxNo()%></td>
								<td align="center"><%=(inputTrans.getBillCode() == null) ? ""
								: inputTrans.getBillCode()%></td>
								<td align="center"><%=(inputTrans.getBillNo() == null) ? ""
								: inputTrans.getBillNo()%></td>
								<td align="center">
									<a href="javascript:void(0)" onClick="OpenModalWindow('<%=webapp%>/inputInformationDetail.action?dealNo=<%=inputTrans.getDealNo()%>',850,400,'view') ">
										<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1014.png" title="查看数据" style="border-width: 0px;" />
									</a>
									<a href="javascript:void(0)" onClick="OpenModalWindow('<%=webapp%>/inputInformationViewImg.action?dealNo=<%=inputTrans.getDealNo()%>',850,600,'view') ">
										<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1015.png" title="查看票据" style="border-width: 0px;" />
									</a>
								</td>
							</tr>
							<%
								}
									}
								}
							%>
							</tr>
						</table>
					</div>
					<div id="anpBoud" align="Right" style="overflow:auto;width:100%;">
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
</html>