<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
	function submitForm(actionUrl){
		var form = document.getElementById("main");
		form.action=actionUrl;
		form.submit();
	}	
</script>
</head>
<body>
<form id="main" action="<c:out value='${webapp}'/>/listParamInSurtax.action" method="post">
<input type="hidden" name="o_bill_id" value="<s:property value='o_bill_id'/>"/>
<input type="hidden" name="billDate" value="<s:property value='billDate'/>"/>
<input type="hidden" name="customerName" value="<s:property value='customerName'/>"/>
<input type="hidden" name="datastatus" value="<s:property value='datastatus'/>"/>
<input type="hidden" name="instId" value="<s:property value='instId'/>"/>
<input type="hidden" name="billCode" value="<s:property value='billCode'/>"/>
<input type="hidden" name="billNo" value="<s:property value='billNo'/>"/>
<input type="hidden" name="fapiaoType" value="<s:property value='fapiaoType'/>"/>
<input type="hidden" name="identifyDate" value="<s:property value='identifyDate'/>"/>
<input type="hidden" name="paginationList.currentPage" value="<s:property value='currentPage'/>"/>
<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
	<tr>
		<td align="left">
			<div id="tbl_current_status">
				<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> 
				<span class="current_status_menu">当前位置：</span> 
				<span class="current_status_submenu1">进项税管理</span>
				<span class="current_status_submenu">认证管理</span>
				<span class="current_status_submenu">扫描认证</span>
				<span class="current_status_submenu">扫描详细</span>
			</div>
			<table id="tbl_tools" width="100%" border="0" >
				<tr style="margin-top:10px;">
					<td align="left" ><input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 
				onclick="submitForm('listInvoiceScanAuth.action');"  name="cmdDelbt" value="返回" id="cmdDelbt" /></th> 
					</td>
				</tr>
			</table>
			<div id="whitebox">
				<div class="boxline">基本信息</div>
				<table id="tbl_context" cellspacing="0" width="100%" align="center" cellpadding="0">
					<tr>
						<td align="left">
						<table id="contenttable" cellpadding="0" cellspacing="0" border="0" width="100%" >
							<tr>
								<td width="10%"  align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">发票代码:&nbsp;&nbsp;&nbsp;</td> 
								<td width="15%" >
								<s:property value='inputInvoiceInfo.billCode'/>
								</td>
								<td width="10%"  align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">发票号码:&nbsp;&nbsp;&nbsp;</td> 
								<td>
								<s:property value='inputInvoiceInfo.billNo'/>
								</td>
								<td width="10%"  align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">开票日期:&nbsp;&nbsp;&nbsp;</td> 
								<td width="15%" >
									<s:property value='inputInvoiceInfo.billDate'/>
								</td>
							</tr>
							<tr>
								<td width="10%"  align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">供应商中文名称:&nbsp;&nbsp;&nbsp;</td> 
								<td width="15%" >
								<s:property value='inputInvoiceInfo.vendorName'/>
								</td>
								<td width="10%"  align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">供应商纳税人识别号:&nbsp;&nbsp;&nbsp;</td> 
								<td>
								<s:property value='inputInvoiceInfo.vendorTaxno'/>
								</td>
								<td width="10%"  align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">供应商电话:&nbsp;&nbsp;&nbsp;</td> 
								<td width="15%" >
									<s:property value='inputInvoiceInfo.vendorPhone'/>
								</td>
							</tr>
							<tr>
								<td width="10%"  align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">供应商地址:&nbsp;&nbsp;&nbsp;</td> 
								<td>
								<s:property value='inputInvoiceInfo.vendorAddress'/>
								</td>
								<td width="10%"  align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">供应商银行账号:&nbsp;&nbsp;&nbsp;</td> 
								<td width="15%" >
									<s:property value='inputInvoiceInfo.vendorBankandaccount'/>
								</td>
								<td width="10%"  align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">价税合计金额:&nbsp;&nbsp;&nbsp;</td> 
								<td>
								<s:property value='inputInvoiceInfo.sumAmt'/>  
								</td>
							</tr>
							<tr>
								<td width="10%"  align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">合计金额:&nbsp;&nbsp;&nbsp;</td> 
								<td width="15%" >
								<s:property value='inputInvoiceInfo.amtSum'/>
								</td>
								<td width="10%"  align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">合计税额:&nbsp;&nbsp;&nbsp;</td> 
								<td>
								<s:property value='inputInvoiceInfo.taxAmtSum'/>
								</td>
								<td  width="10%"  align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
									认证状态 :&nbsp;&nbsp;&nbsp;
								</td>
								<td>
									<select id="datastatus" name="inputInvoiceInfo.datastatus" disabled><option value="" <s:if test='inputInvoiceInfo.datastatus==""'>selected</s:if><s:else></s:else>>全部</option>
										<s:iterator value="mapDatastatus" id="entry">  
										<option value="<s:property value="key"/>" <s:if test='inputInvoiceInfo.datastatus==#entry.key'>selected</s:if><s:else></s:else>><s:property value="value"/></option>
								       </s:iterator>
									</select>
								</td>
							</tr>
							<tr>
								<td  width="10%"  align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
									收款人 :&nbsp;&nbsp;&nbsp;
								</td>
								<td>
									<s:property value='inputInvoiceInfo.payee'/>
								</td>
								<td  width="10%"  align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
									审核人  :&nbsp;&nbsp;&nbsp;
								</td>
								<td>
									<s:property value='inputInvoiceInfo.reviewer'/>
								</td>
								<td  width="10%"  align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
									开票人  :&nbsp;&nbsp;&nbsp;
								</td>
								<td>
									<s:property value='inputInvoiceInfo.drawer'/>
								</td>
							</tr>
							<tr>
								<td  width="10%"  align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">
									备注 :&nbsp;&nbsp;&nbsp;
								</td>
								<td colspan="5"><s:property value='inputInvoiceInfo.remark'/>
				<!--					<textarea  rows="3" cols="20" name="inputInvoiceInfo.remark" disabled><s:property value='inputInvoiceInfo.remark'/></textarea>-->
								</td>
							</tr>
						</table>
						</td>
					</tr>
				</table>
				<!-- <div class="boxline">商品信息</div>
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
						<td colspan="7">该发票没有对应的商品信息！</td>
					</s:if>
					<s:else>
					<s:iterator value="lstInputInvoiceItem" id="iList" status="stuts">
					<tr class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
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
				</table> -->
			<div class="boxline">商品数据列表</div>
			<div>
				<iframe id="subTableFrame" scrolling="auto" src="listInputItem.action?billId=<s:property value='o_bill_id'/>" height="160px" width="100%" frameborder="0"></iframe>
			</div>
			<div class="boxline">票据交易数据</div>
			<div style="border-bottom:#DDD solid 1px;">
				<iframe id="subTableFrame1" scrolling="auto" src="listInputTransItem.action?billId=<s:property value='o_bill_id'/>" height="160px" width="100%" frameborder="0"></iframe>
			</div>
				<!-- <div class="boxline">关联交易信息</div>
				<div id="lessGridList12" style="overflow: auto; width: 100%;">
					<table  class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0"  style="border-collapse: collapse;width: 100%;">
						<tr class="lessGrid head">
							<th style="text-align: center;width:15%">交易编号</th>
							<th style="text-align: center">金额_人民币</th>
							<th style="text-align: center">税额_人民币</th>
							<th style="text-align: center">供应商ID</th>
							<th style="text-align: center">交易发生机构</th>
						</tr>
						<tr class="lessGrid rowA">
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
					</table>
				</div>-->
			</div> 
			
			
			
			
			
			
		</td>
	</tr>
</table>
</form>
</body>
</html>