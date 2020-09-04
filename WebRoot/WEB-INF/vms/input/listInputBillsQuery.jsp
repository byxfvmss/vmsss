<%@ page language="java" import="java.util.*" import="java.math.BigDecimal" import="com.opensymphony.util.BeanUtils" import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils" import="com.cjit.common.util.PaginationList" import="com.cjit.vms.input.model.InformationBills" import="com.cjit.vms.trans.util.DataUtil"
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
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/editview.js"></script>
<script type="text/javascript">
	// [查询]按钮
	function submitBill() {
		document.forms[0].submit();
		document.forms[0].action="listInputBillsQuery.action";
	}

	function submitExport() {
		submitAction(document.forms[0], "exportInformationBills.action");
		document.forms[0].action="listInputBillsQuery.action";
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
				
		//if(needReload && retData){
		//	window.document.forms[0].submit();
		//}
	}
</script>
<style type="text/css">
.style1 {
	width: 242px;
}
</style>
</head>
<body onkeydow="enterkey(event)">
	<form name="Form1" method="post" action="listInputBillsQuery.action" id="Form1" enctype="multipart/form-data">
		<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
			<tr>
				<td class="centercondition">
					<s:component template="rocketMessage" />
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" />
						<span class="current_status_menu">当前位置：</span>
						<span class="current_status_submenu1">进项税管理</span>
						<span class="current_status_submenu">综合查询</span>
						<span class="current_status_submenu">票据查询</span>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" width="100%" border="0">
							<tr>
								<td align="left">开票日期</td>
								<td>
									<input class="tbl_query_time" type="text" name="billStartDate_s" id="billStartDate_s" value="<s:property value='informationBills.billStartDate'/>"
										onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'billStartDate_s\')}'})" size='11'
									"/>
									--
									<input class="tbl_query_time" type="text" name="billEndDate_s" id="billEndDate_s" value="<s:property value='informationBills.billEndDate'/>"
										onfocus="WdatePicker({minDate:'#F{$dp.$D(\'billEndDate_s\')}'})" size='11'
									"/>
								</td>
								<td>所属机构</td>
								<td>
									<input type="hidden" id="inst_id" name="instCode_s" value='<s:property value="informationBills.instCode"/>'>
									<input type="text" class="tbl_query_text" id="inst_Name" name="instName_s" value='<s:property value="informationBills.instName"/>' onclick="setOrg(this);" readonly="readonly">
									<%-- <s:if test="authInstList != null && authInstList.size > 0">
										<s:select name="instCode_s" list="authInstList" listKey='id' listValue='name' headerKey="" headerValue="所有" value='informationBills.instCode' />
									</s:if>
									<s:if test="authInstList == null || authInstList.size == 0">
										<select name="instCode_s" class="readOnlyText">
											<option value="">请分配机构权限</option>
										</select>
									</s:if> --%>
								</td>
								<td>发票状态</td>
								<td>
									<select id="dataStatus" name="dataStatus_s">
										<option value="" <s:if test='informationBills.dataStatus==""'>selected</s:if> <s:else></s:else>>所有</option>
										<s:iterator value="mapDataStatus" id="entry">
											<option value="<s:property value="key"/>" <s:if test='informationBills.dataStatus==#entry.key'>selected</s:if> <s:else></s:else>><s:property value="value" /></option>
										</s:iterator>
									</select>
								</td>
							</tr>
							<tr>
								<td>发票代码</td>
								<td>
									<input type="text" class="tbl_query_text" name="billCode_s" value="<s:property value='informationBills.billCode'/>" maxlength="10" onkeypress="checkkey(value);" />
								</td>
								<td>发票号码</td>
								<td>
									<input type="text" class="tbl_query_text" name="billNo_s" value="<s:property value='informationBills.billNo'/>" maxlength="8" onkeypress="checkkey(value);" />
								</td>
								<td>发票类型</td>
								<td>
									<select id="faPiaoType" name="faPiaoType_s">
										<option value="" <s:if test='informationBills.faPiaoType==""'>selected</s:if> <s:else></s:else>>所有</option>
										<s:iterator value="mapVatType" id="entry">
											<option value="<s:property value="key"/>" <s:if test='informationBills.faPiaoType==#entry.key'>selected</s:if> <s:else></s:else>><s:property value="value" /></option>
										</s:iterator>
									</select>
								</td>
							</tr>
							<tr>
								<td>供应商纳税人识别号</td>
								<td>
									<input type="text" class="tbl_query_text" name="vendorTaxNo_s" value="<s:property value='informationBills.vendorTaxNo'/>" />
								</td>
								<td>供应商名称</td>
								<td>
									<input type="text" class="tbl_query_text" name="vendorName_s" value="<s:property value='informationBills.vendorName'/>" maxlength="100" />
								</td>
								<td colspan="2" align="right">
									<input type="button" class="tbl_query_button" value="查询" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" name="BtnView" id="BtnView"
										onclick="submitBill()"
									/>
								</td>
								<!--			<td style="width:80px;" align="right">-->
								<!--					<input type="button" class="tbl_query_button" value="导出"-->
								<!--					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" -->
								<!--					name="upLoad" id="upLoad" onclick="submitExport()"/>-->
								<!--			</td>-->
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
					<div id="lessGridList3" style="overflow: auto; width: 100%;">
						<table class="lessGrid" cellspacing="0" rules="all" border="0" cellpadding="0" style="border-collapse: collapse; width: 100%;">
							<tr class="lessGrid head">
								<th style="text-align:center">
									<input style="width:13px;height:13px;" id="CheckAll" type="checkbox" onclick="cbxselectall(this,'selectInputBillCodes')" />
								</th>
								<th style="text-align:center">序号</th>
								<th style="text-align:center">发票代码</th>
								<th style="text-align:center">发票号码</th>
								<th style="text-align:center">开票日期</th>
								<th style="text-align:center">所属机构</th>
								<th style="text-align:center">金额</th>
								<th style="text-align:center">税额</th>
								<th style="text-align:center">发票种类</th>
								<th style="text-align:center">供应商名称</th>
								<th style="text-align:center">供应商纳税人识别号</th>
								<th style="text-align:center">发票状态</th>
								<th style="text-align:center">认证日期</th>
								<th style="text-align:center">扫描时间</th>
								<th style="text-align:center">明细</th>
							</tr>
							<s:iterator value="paginationList.recordList" id="iList" status="stuts">
								<tr align="center" class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
									<td>
										<input type="checkbox" style="width:13px;height:13px;" name="selectInfos" value="<s:property value="#iList.billId"/>" />
									</td>
									<td align="center">
										<s:property value='#stuts.count' />
									</td>
									<td>
										<s:property value='billCode' />
									</td>
									<td>
										<s:property value='billNo' />
									</td>
									<td>
										<s:property value='billDate' />
									</td>
									<td>
										<s:property value='instName' />
									</td>
									<td>
										<s:property value='amtSum' />
									</td>
									<td>
										<s:property value='taxAmtSum' />
									</td>
									<td>
										<s:iterator value="mapVatType" id="entry">
											<s:if test='faPiaoType==#entry.key'>
												<s:property value="value" />
											</s:if>
										</s:iterator>
									</td>
									<td>
										<s:property value='vendorName' />
									</td>
									<td>
										<s:property value='vendorTaxNo' />
									</td>
									<td>
										<s:property value='mapDataStatus[#iList.dataStatus]' />
									</td>
									<td>
										<s:property value='identifyDate' />
									</td>
									<td>
										<s:property value='scanDate' />
									</td>
									<td style="width:60px;">
										<a href="javascript:void(0)" onClick="OpenModalWindow('<%=webapp%>/inputInvoiceInfoViewData.action?billId=<s:property value="#iList.billId" />',600,650,'view') ">
											<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1014.png" title="查看" style="border-width: 0px;" />
										</a>
										<a href="javascript:void(0);" onClick="OpenModalWindowSubmit('inputInvoiceInfoViewImg.action?billId=<s:property value="#iList.billId" />',800,600,true)">
											<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1015.png" title="查看票样" style="border-width: 0px;" />
										</a>
									</td>
								</tr>
							</s:iterator>
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