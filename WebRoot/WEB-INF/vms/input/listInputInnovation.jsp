<!--file: <%=request.getRequestURI()%> -->
<%@ page language="java" import="java.util.*" import="java.math.BigDecimal" import="com.opensymphony.util.BeanUtils" import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils" import="com.cjit.common.util.PaginationList" import="com.cjit.vms.input.model.InputInvoiceInfo" import="com.cjit.vms.trans.util.DataUtil"
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
		submitAction(document.forms[0], "listInnovation.action");
		document.forms[0].action="listInnovation.action";
	}

	function submitExport() {
		submitAction(document.forms[0], "exportInnovation.action");
		document.forms[0].action="listInnovation.action";
	}
</script>
<style type="text/css">
.style1 {
	width: 242px;
}
</style>
</head>
<body onkeydow="enterkey(event)">
	<form name="Form1" method="post" action="listInnovation.action" id="Form1">
		<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
			<tr>
				<td class="centercondition">
					<s:component template="rocketMessage" />
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" />
						<span class="current_status_menu">当前位置：</span>
						<span class="current_status_submenu1">进项税管理</span>
						<span class="current_status_submenu">发票勾稽</span>
						<span class="current_status_submenu">发票勾稽</span>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" border="0" width="100%">
							<tr>
								<td align="left">开票日期</td>
								<td>
									<input class="tbl_query_time" id="inputInvoiceInfo.billStartDate" type="text" name="inputInvoiceInfo.billStartDate" value="<s:property value='inputInvoiceInfo.billStartDate'/>"
										onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'inputInvoiceInfo.billEndDate\')}'})" size='11'
									/>
									--
									<input class="tbl_query_time" id="inputInvoiceInfo.billEndDate" type="text" name="inputInvoiceInfo.billEndDate" value="<s:property value='inputInvoiceInfo.billEndDate'/>"
										onfocus="WdatePicker({minDate:'#F{$dp.$D(\'inputInvoiceInfo.billStartDate\')}'})" size='11'
									/>
								</td>
								<td align="right">所属机构</td>
								<td>
									<input type="hidden" id="inst_id" name="inputInvoiceInfo.instId" value='<s:property value="inputInvoiceInfo.instId"/>'>
									<input type="text" class="tbl_query_text" id="inst_Name" name="inputInvoiceInfo.instName" value='<s:property value="inputInvoiceInfo.instName"/>' onclick="setOrg(this);" readonly="readonly">
									<%-- <select id="inst_id" name="inputInvoiceInfo.instName">
										<option value="" <s:if test='inst_id==""'>selected</s:if> <s:else></s:else>>全部</option>
										<s:iterator value="lstAuthInstId" id="org">
											<option value="<s:property value="id"/>" <s:if test='inputInvoiceInfo.instName==#org.id'>selected</s:if> <s:else></s:else>><s:property value="name" /></option>
										</s:iterator>
									</select> --%>
								</td>
								<td align="right">发票种类</td>
								<td>
									<select id="inputInvoiceInfo.fapiaoType" name="inputInvoiceInfo.fapiaoType">
										<option value="" <s:if test='inputInvoiceInfo.fapiaoType==""'>selected</s:if> <s:else></s:else>>全部</option>
										<option value="1" <s:if test='inputInvoiceInfo.fapiaoType=="1"'>selected</s:if> <s:else></s:else>>增值税普通发票</option>
										<option value="0" <s:if test='inputInvoiceInfo.fapiaoType=="0"'>selected</s:if> <s:else></s:else>>增值税专用发票</option>
									</select>
								</td>
							</tr>
							<tr>
								<td align="right">供应商名称</td>
								<td>
									<input type="text" class="tbl_query_text" name="inputInvoiceInfo.vendorName" value="<s:property value='inputInvoiceInfo.vendorName'/>" maxlength="100" />
								</td>
								<td align="right">发票代码</td>
								<td>
									<input type="text" class="tbl_query_text" name="inputInvoiceInfo.billCode" value="<s:property value='inputInvoiceInfo.billCode'/>" maxlength="10" onkeypress="checkkey(value);" />
								</td>
								<td align="right">发票号码</td>
								<td>
									<input type="text" class="tbl_query_text" name="inputInvoiceInfo.billNo" value="<s:property value='inputInvoiceInfo.billNo'/>" maxlength="8" onkeypress="checkkey(value);" />
								</td>
							</tr>
							<tr>
								<td align="right">供应商纳税识别号</td>
								<td>
									<input type="text" class="tbl_query_text" name="inputInvoiceInfo.vendorTaxno" value="<s:property value='inputInvoiceInfo.vendorTaxno'/>" maxlength="20" />
								</td>
								<td align="right">发票状态</td>
								<td>
									<select id="inputInvoiceInfo.datastatus" name="inputInvoiceInfo.datastatus">
										<option value="" <s:if test='inputInvoiceInfo.datastatus==""'>selected</s:if> <s:else></s:else>>全部</option>
										<option value="1" <s:if test='inputInvoiceInfo.datastatus=="1"'>selected</s:if> <s:else></s:else>>已扫描未认证</option>
										<option value="2" <s:if test='inputInvoiceInfo.datastatus=="2"'>selected</s:if> <s:else></s:else>>认证未收到回执</option>
										<option value="3" <s:if test='inputInvoiceInfo.datastatus=="3"'>selected</s:if> <s:else></s:else>>首次认证通过</option>
										<option value="4" <s:if test='inputInvoiceInfo.datastatus=="4"'>selected</s:if> <s:else></s:else>>首次认证未通过</option>
										<option value="5" <s:if test='inputInvoiceInfo.datastatus=="5"'>selected</s:if> <s:else></s:else>>再次认证通过</option>
										<option value="6" <s:if test='inputInvoiceInfo.datastatus=="6"'>selected</s:if> <s:else></s:else>>再次认证未通过</option>
										<option value="7" <s:if test='inputInvoiceInfo.datastatus=="7"'>selected</s:if> <s:else></s:else>>税务局当场认证通过</option>
										<option value="8" <s:if test='inputInvoiceInfo.datastatus=="8"'>selected</s:if> <s:else></s:else>>税务局当场认证未通过</option>
										<option value="9" <s:if test='inputInvoiceInfo.datastatus=="9"'>selected</s:if> <s:else></s:else>>票退回</option>
										<option value="10" <s:if test='inputInvoiceInfo.datastatus=="10"'>selected</s:if> <s:else></s:else>>已抵扣</option>
										<option value="11" <s:if test='inputInvoiceInfo.datastatus=="11"'>selected</s:if> <s:else></s:else>>不可抵扣</option>
										<option value="12" <s:if test='inputInvoiceInfo.datastatus=="12"'>selected</s:if> <s:else></s:else>>红冲待审核</option>
										<option value="13" <s:if test='inputInvoiceInfo.datastatus=="13"'>selected</s:if> <s:else></s:else>>红冲已审核</option>
										<option value="14" <s:if test='inputInvoiceInfo.datastatus=="14"'>selected</s:if> <s:else></s:else>>已红冲</option>
										<option value="15" <s:if test='inputInvoiceInfo.datastatus=="15"'>selected</s:if> <s:else></s:else>>认证提交</option>
										<option value="16" <s:if test='inputInvoiceInfo.datastatus=="16"'>selected</s:if> <s:else></s:else>>转出提交</option>
									</select>
								</td>
								<td style="width:80px;" align="right">
									<input type="button" class="tbl_query_button" value="查询" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" name="BtnView" id="BtnView"
										onclick="submitBill()"
									/>
								</td>
								<!--					<td>-->
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
									<input style="width:13px;height:13px;" id="CheckAll" type="checkbox" onclick="cbxselectall(this,'selectBillIds')" />
								</th>
								<th style="text-align:center">序号</th>
								<th style="text-align:center">发票代码</th>
								<th style="text-align:center">发票号码</th>
								<th style="text-align:center">开票日期</th>
								<th style="text-align:center">金额所属机构</th>
								<th style="text-align:center">税额</th>
								<th style="text-align:center">发票类型</th>
								<th style="text-align:center">供应商名称</th>
								<th style="text-align:center">供应商纳税人识别号</th>
								<th style="text-align:center">发票状态</th>
								<th style="text-align:center">转出金额</th>
								<th style="text-align:center">认证日期</th>
								<th style="text-align:center">操作</th>
							</tr>
							<%
								PaginationList paginationList = (PaginationList) request
										.getAttribute("paginationList");
								if (paginationList != null) {
									List inputTransList = (List) paginationList.getRecordList();
									if (inputTransList != null && inputTransList.size() > 0) {
										for (int i = 0; i < inputTransList.size(); i++) {
											InputInvoiceInfo inputTrans = (InputInvoiceInfo) inputTransList
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
								<td>
									<input style="width:13px;height:13px;" type="checkbox" name="selectBillIds" value="<%=BeanUtils.getValue(inputTrans, "billId")%>" />
								</td>
								<td align="center"><%=i + 1%></td>
								<td align="center"><%=(inputTrans.getBillCode() == null) ? ""
								: inputTrans.getBillCode()%></td>
								<td align="center"><%=(inputTrans.getBillNo() == null) ? ""
								: inputTrans.getBillNo()%></td>
								<td align="center"><%=(inputTrans.getBillDate() == null) ? ""
								: inputTrans.getBillDate()%></td>
								<td align="center"><%=(inputTrans.getInstName() == null) ? ""
								: inputTrans.getInstName()%></td>
								<td align="center"><%=NumberUtils.format(inputTrans.getTaxAmtSum(),
								"", 2)%></td>
								<td align="center"><%=(inputTrans.getFapiaoType() == null) ? ""
								: (inputTrans.getFapiaoType().equals("0") ? "增值税专用发票"
										: "增值税普通发票")%></td>
								<td align="center"><%=(inputTrans.getVendorName() == null) ? ""
								: inputTrans.getVendorName()%></td>
								<td align="center"><%=(inputTrans.getVendorTaxno() == null) ? ""
								: inputTrans.getVendorTaxno()%></td>
								<td align="center"><%=(inputTrans.getDatastatusName() == null) ? ""
								: inputTrans.getDatastatusName()%></td>
								<td align="center"><%=NumberUtils.format(inputTrans.getVatOutAmt(),
								"", 2)%></td>
								<td align="center"><%=(inputTrans.getVerifyData() == null) ? ""
								: inputTrans.getVerifyData()%></td>
								<td align="center">
									<a href="<%=webapp%>/InnovationDetail.action?flag=query&billId=<%=inputTrans.getBillId()%>&vendorTaxno=<%=inputTrans.getVendorTaxno()%>">
										<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1014.png" title="查看" style="border-width: 0px;" />
									</a>
									<!-- 
									<a href="<%=webapp%>/editInnovation.action?flag=edit&billId=<%=inputTrans.getBillId()%>&vendorTaxno=<%=inputTrans.getVendorTaxno()%>">
									 -->
									 <a href="javascript:void(0)"  onClick="OpenModalWindow('editInnovation.action?flag=edit&billId=<%=inputTrans.getBillId()%>&vendorTaxno=<%=inputTrans.getVendorTaxno()%>',1000,700,'view') ">
										<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1043.png" title="发票钩稽" style="border-width: 0px;" />
									</a>
									<a href="javascript:void(0)" onClick="OpenModalWindow('<%=webapp%>/inputBill.action?filePath=<%=inputTrans.getUrlBillImage()%>',650,400,'view') ">
										<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1015.png" title="查看票样" style="border-width: 0px;" />
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