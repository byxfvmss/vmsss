<!--file: <%=request.getRequestURI()%> -->
<%@ page language="java" import="java.util.*" import="java.math.BigDecimal" import="com.opensymphony.util.BeanUtils" import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils" import="com.cjit.vms.trans.model.BillInfo" import="com.cjit.vms.trans.model.BillItemInfo" import="com.cjit.vms.trans.model.TransInfo"
	import="com.cjit.vms.system.model.Business" import="com.cjit.vms.trans.util.DataUtil" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%>
<%
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
%>
<%@ include file="../../../page/modalPage.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<%@ include file="../../../page/include.jsp"%>
<!-- <title><s:if test="user.userId != null && user.userId != '' ">修改交易种类</s:if><s:else>新增交易种类</s:else></title> -->
<title>新增交易认定种类</title>
<link type="text/css" href="<%=bopTheme2%>/css/subWindow.css" rel="stylesheet">
<link href="<%=bopTheme%>/js/MessageBox/css/messageBox.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<%=bopTheme%>/js/validator.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/editview.js"></script>
<script type="text/javascript" src="<%=bopTheme%>/js/MessageBox/js/messageBox.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
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
	//标识页面是否已提交
	var subed = false;
	function checkForm() {
		//验证是否重复提交
		if (subed == true) {
			alert("信息正在发送给服务器，请不要重复提交信息！");
			return false;
		}
		/**if(fucCheckNull(document.getElementById("payee"),"开票人不能为空")==false){
		   return false;
		}
		if(fucCheckNull(document.getElementById("reviewer"),"审核人不能为空")==false){
		   return false;
		}
		if(fucCheckNull(document.getElementById("drawer"),"开票人不能为空")==false){
		   return false;
		}
		if(fucCheckNull(document.getElementById("e_datastatus"),"认证状态不能为空")==false){
		   return false;
		}*/
		subed = true;
		return true;
	}
	function submitForm(actionUrl) {
		var form = document.getElementById("main");
		form.action = actionUrl;
		form.submit();
	}
</script>
</head>
<body>
	<form id="main"  action="updateInvoiceDeduction.action"  method="post">
		<input type="hidden" name="o_bill_id" value="<s:property value='o_bill_id'/>" />
		<input type="hidden" name="billDate" value="<s:property value='billDate'/>" />
		<input type="hidden" name="vendorName" value="<s:property value='vendorName'/>" />
		<input type="hidden" name="datastatus" value="<s:property value='datastatus'/>" />
		<input type="hidden" name="billCode" value="<s:property value='billCode'/>" />
		<input type="hidden" name="fapiaoType" value="<s:property value='fapiaoType'/>" />
		<input type="hidden" name="billNo" value="<s:property value='billNo'/>" />
		<input type="hidden" name="instId" value="<s:property value='instId'/>" />
		<input type="hidden" name="paginationList.currentPage" value="<s:property value='currentPage'/>" />
		<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
			<tr>
				<td class="centercondition">
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" />
						<span class="current_status_menu">当前位置：</span>
						<span class="current_status_submenu1">进项税管理</span>
						<span class="current_status_submenu">认证管理</span>
						<span class="current_status_submenu">进项抵扣</span>
						<span class="current_status_submenu">抵扣编辑</span>
					</div>
					<!--		<div class="windowtitle" style="background:#004C7E; height:30px; line-height:30px; text-align:left; color:#FFF; font-size:12px; margin-top: 15px;">抵扣编辑</div>-->
					<div class="widthauto2">
						<table width="100%">
							<tr>
								<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">备注:&nbsp;&nbsp;&nbsp;</td>
								<td>
									<textarea col="20" row="3" name="inputInvoiceInfo.remark"><s:property value='inputInvoiceInfo.remark' /></textarea>
								</td>
								<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">抵扣状态:&nbsp;&nbsp;&nbsp;</td>
								<td>
									<%-- 	<select id="e_datastatus" name="inputInvoiceInfo.datastatus" ><option value="" <s:if test='inputInvoiceInfo.datastatus==""'>selected</s:if><s:else></s:else>>全部</option>
						<s:iterator value="mapDatastatus" id="entry">  
						<option value="<s:property value="key"/>" <s:if test='inputInvoiceInfo.datastatus==#entry.key'>selected</s:if><s:else></s:else>><s:property value="value"/></option>
				       </s:iterator>
					</select> --%>
									<s:select name="inputInvoiceInfo.datastatus" list="mapDatastatus" listKey="key" listValue="value"></s:select>
									<!-- 							<input type="text" name="inputInvoiceInfo.datastatus"  id="datastatus"  value="<s:property value='inputInvoiceInfo.datastatus'/>" /> -->
								</td>
								<td colspan="2">
									<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="" name="cmdEditSavebt" value="保存"
										id="cmdEditSavebt"
									/>
									<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="window.close()" name="cmdDelbt"
										value="返回" id="cmdDelbt"
									/>
								</td>
							</tr>
						</table>
					</div>
					<div id="whitebox1">
						<div class="boxline">基本信息</div>
						<table id="tbl_context" cellpadding="0" cellspacing="0" border="0" width="100%">
							<tr>
								<td width="20%" align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">发票代码:&nbsp;&nbsp;&nbsp;</td>
								<td width="30%">
									<s:property value='inputInvoiceInfo.billCode' />
								</td>
								<td width="20%" align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">发票号码:&nbsp;&nbsp;&nbsp;</td>
								<td>
									<s:property value='inputInvoiceInfo.billNo' />
								</td>
							</tr>
							<tr>
								<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">开票日期:&nbsp;&nbsp;&nbsp;</td>
								<td>
									<s:property value='inputInvoiceInfo.billDate' />
								</td>
								<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">价税合计金额:&nbsp;&nbsp;&nbsp;</td>
								<td>
									<s:property value='inputInvoiceInfo.sumAmt' />
								</td>
							</tr>
							<tr>
								<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">供应商中文名称:&nbsp;&nbsp;&nbsp;</td>
								<td>
									<s:property value='inputInvoiceInfo.vendorName' />
								</td>
								<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">供应商纳税人识别号:&nbsp;&nbsp;&nbsp;</td>
								<td>
									<s:property value='inputInvoiceInfo.vendorTaxno' />
								</td>
							</tr>
							<tr>
								<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">供应商电话:&nbsp;&nbsp;&nbsp;</td>
								<td>
									<s:property value='inputInvoiceInfo.vendorPhone' />
								</td>
								<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">供应商地址:&nbsp;&nbsp;&nbsp;</td>
								<td>
									<s:property value='inputInvoiceInfo.vendorAddress' />
								</td>
							</tr>
							<tr>
								<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">合计金额:&nbsp;&nbsp;&nbsp;</td>
								<td>
									<s:property value='inputInvoiceInfo.amtSum' />
								</td>
								<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">合计税额:&nbsp;&nbsp;&nbsp;</td>
								<td>
									<s:property value='inputInvoiceInfo.taxAmtSum' />
								</td>
							</tr>
							<tr>
								<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">收款人:&nbsp;&nbsp;&nbsp;</td>
								<td>
									<s:property value='inputInvoiceInfo.payee' />
									<input type="hidden" name="inputInvoiceInfo.payee" id="payee" value="<s:property value='inputInvoiceInfo.payee'/>" />
								</td>
								<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">供应商银行账号:&nbsp;&nbsp;&nbsp;</td>
								<td>
									<s:property value='inputInvoiceInfo.vendorBankandaccount' />
								</td>
							</tr>
							<tr>
								<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">审核人:&nbsp;&nbsp;&nbsp;</td>
								<td>
									<s:property value='inputInvoiceInfo.reviewer' />
									<input type="hidden" name="inputInvoiceInfo.reviewer" id="reviewer" value="<s:property value='inputInvoiceInfo.reviewer'/>" />
								</td>
								<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">开票人:&nbsp;&nbsp;&nbsp;</td>
								<td>
									<s:property value='inputInvoiceInfo.drawer' />
									<input type="hidden" name="inputInvoiceInfo.drawer" id="drawer" value="<s:property value='inputInvoiceInfo.drawer'/>" />
								</td>
							</tr>
						</table>
						<div class="boxline">商品数据列表</div>
						<div>
							<iframe id="subTableFrame" scrolling="auto" src="listInputItem.action?billId=<s:property value='o_bill_id'/>" height="160px" width="100%" frameborder="0"></iframe>
						</div>
						<div class="boxline">票据交易数据</div>
						<div style="border-bottom:#DDD solid 1px;">
							<iframe id="subTableFrame1" scrolling="auto" src="listInputTransItem.action?billId=<s:property value='o_bill_id'/>" height="160px" width="100%" frameborder="0"></iframe>
						</div>
						<!-- <div class="boxline">关联商品</div>
			<table  class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0"  style="border-collapse: collapse;width: 100%;">
				<tr class="lessGrid head">
					<th style="text-align: center;width:15%">商品名称</th>
					<th style="text-align: center">规格型号</th>
					<th style="text-align: center">商品数量</th>
					<th style="text-align: center">商品单价</th>
					<th style="text-align: center">金额</th>
					<th style="text-align: center">税率</th>
					<th style="text-align: center">税额</th>
				</tr>
				<s:if test='lstInputInvoiceItem == null || lstInputInvoiceItem.size() == 0'>
					<tr><td colspan="7">该发票没有对应的商品信息！</td></tr>
				</s:if>
				<s:else>
					<s:iterator value="lstInputInvoiceItem" id="iList" status="stuts">
						<tr>
							<td style="text-align: center"><s:property value='goodsName'/></td>
							<td style="text-align: center"><s:property value='specandmodel'/></td>
							<td style="text-align: center"><s:property value='goodsNo'/></td>
							<td style="text-align: center"><s:property value='goodsPrice'/></td>
							<td style="text-align: center"><s:property value='amt'/></td>
							<td style="text-align: center"><s:property value='taxRate'/></td>
							<td style="text-align: center"><s:property value='taxAmt'/></td>
						</tr>
					</s:iterator>
				</s:else>
			</table>
			<div class="boxline">关联交易</div>
			<table  class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0"  style="border-collapse: collapse;width: 100%;">
				<tr class="lessGrid head">
					<th style="text-align: center;width:15%">交易编号</th>
					<th style="text-align: center">金额_人民币</th>
					<th style="text-align: center">税额_人民币</th>
					<th style="text-align: center">供应商</th>
					<th style="text-align: center">交易发生机构</th>
				</tr>
				
				<tr>
				<s:if test='inputTransInfo == null'>
					<td colspan="5">该发票没有对应的交易信息！</td>
				</s:if>
				<s:else>
					<td style="text-align: center"><s:property value='inputTransInfo.dealNo'/></td>
					<td style="text-align: center"><s:property value='inputTransInfo.amtCny'/></td>
					<td style="text-align: center"><s:property value='inputTransInfo.taxAmtCny'/></td>
					<td style="text-align: center"><s:property value='inputTransInfo.vendorName'/></td>
					<td style="text-align: center"><s:property value='inputTransInfo.bankName'/></td>
				</s:else>
				</tr>
			</table> -->
					</div>
				</td>
			</tr>
		</table>
	</form>
</body>
<script>
	$(function() {
		$("#cmdEditSavebt").click(function() {
			if (checkForm() == true) {
				submitForm("updateInvoiceDeduction.action");
			}
		});
	});
</script>
</html>