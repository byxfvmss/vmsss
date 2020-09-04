<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<%@ include file="../../../page/include.jsp"%>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
<link href="<c:out value="${bopTheme2}"/>/css/main.css" rel="stylesheet" type="text/css" />
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/window.js"></script>
<script charset="gb2312" language="javascript" type="text/javascript" src="<%=bopTheme%>/js/validator.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script src="<c:out value="${bopTheme}"/>/js/MessageBox/js/messageBox.js" type="text/javascript"></script>
<link href="<c:out value="${bopTheme}"/>/js/MessageBox/css/messageBox.css" rel="stylesheet" type="text/css" />
<title>客户信息明细</title>
<link type="text/css" href="<c:out value="${bopTheme2}"/>/css/subWindow.css" rel="stylesheet" />
</head>
<body>
	<div class="windowtitle" style="background:#004C7E; height:45px; line-height:45px; text-align:center; color:#FFF; font-weight:bold; font-size:14px;">查看供应商</div>
	<div class="windowtitle" style="background:#004C7E; height:30px; line-height:30px; text-align:left; color:#FFF; font-size:12px; margin-top: 10px;">供应商基本信息</div>
	<form name="formBusiness" id="formBusiness" action="" method="post">
		<div id="editsubpanel" class="editsubpanel">
			<div style="overflow: auto; width: 100%;">
				<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
					<tr>
						<td width="15%" align="left">供应商信息审核-明细</td>
					</tr>
					<tr>
						<input type="hidden" id="vendorId" value="<s:property value='vendorInfo.vendorId'/>" />
						<input type="hidden" id="taxNo" value="<s:property value='vendorInfo.vendorTaxNo'/>" />
						<table id="tbl_context" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
							<tr>
								<td width="15%" align="right">数据操作标志:&nbsp;&nbsp;&nbsp;</td>
								<td width="35%">
									<s:if test='vendorInfo.dataOperationLabel=="1"'>
										<input type="text" class="tbl_query_text2" readonly="readonly" id="" value="新增" />
									</s:if>
									<s:if test='vendorInfo.dataOperationLabel=="2"'>
										<input type="text" class="tbl_query_text2" readonly="readonly" id="" value="修改" />
									</s:if>
									<s:if test='vendorInfo.dataOperationLabel=="3"'>
										<input type="text" class="tbl_query_text2" readonly="readonly" id="" value="删除" />
									</s:if>
									<input id="dataOperationLabel" type="hidden" value="<s:property value='vendorInfo.dataOperationLabel'/>">
								<td width="15%" align="right">数据审核状态:&nbsp;&nbsp;&nbsp;</td>
								<td width="35%">
									<s:if test='vendorInfo.dataAuditStatus=="1"'>
										<input type="text" class="tbl_query_text2" readonly="readonly" id="" value="审核通过" />
									</s:if>
									<s:if test='vendorInfo.dataAuditStatus=="2"'>
										<input type="text" class="tbl_query_text2" readonly="readonly" id="" value="审核不通过" />
									</s:if>
									<s:if test='vendorInfo.dataAuditStatus=="0"'>
										<input type="text" class="tbl_query_text2" readonly="readonly" id="" value="待审核" />
									</s:if>
								</td>
							</tr>
							<tr>
								<td width="15%" align="right">供应商中文名称:&nbsp;&nbsp;&nbsp;</td>
								<td width="35%">
									<input type="text" class="tbl_query_text2" readonly="readonly" id="" value="<s:property value='vendorInfo.vendorCName'/>" />
								</td>
								<td width="15%" align="right">供应商英文名称:&nbsp;&nbsp;&nbsp;</td>
								<td width="35%">
									<input type="text" class="tbl_query_text2" readonly="readonly" id="" value="<s:property value='vendorInfo.vendorEName'/>" />
								</td>
							</tr>
							<tr>
								<td align="right">供应商纳税人识别号:&nbsp;&nbsp;&nbsp;</td>
								<td>
									<input type="text" class="tbl_query_text2" readonly="readonly" id="" value="<s:property value='vendorInfo.vendorTaxNo'/>" />
								</td>
								<td align="right">供应商账号:&nbsp;&nbsp;&nbsp;</td>
								<td>
									<input type="text" class="tbl_query_text2" readonly="readonly" id="" value="<s:property value='vendorInfo.vendorAccount'/>" />
								</td>
							</tr>
							<tr>
								<td align="right">供应商开户银行中文名称:&nbsp;&nbsp;&nbsp;</td>
								<td>
									<input type="text" class="tbl_query_text2" readonly="readonly" id="" value="<s:property value='vendorInfo.vendorCBank'/>" />
								</td>
								<td align="right">供应商开户银行英文名称:&nbsp;&nbsp;&nbsp;</td>
								<td>
									<input type="text" class="tbl_query_text2" readonly="readonly" id="" value="<s:property value='vendorInfo.vendorEBank'/>" />
								</td>
							</tr>
							<tr>
								<td align="right">供应商联系人:&nbsp;&nbsp;&nbsp;</td>
								<td>
									<input type="text" class="tbl_query_text2" readonly="readonly" id="" value="<s:property value='vendorInfo.vendorLinkman'/>" />
								</td>
								<td align="right">供应商电话:&nbsp;&nbsp;&nbsp;</td>
								<td>
									<input type="text" class="tbl_query_text2" readonly="readonly" id="" value="<s:property value='vendorInfo.vendorPhone'/>" />
								</td>
							</tr>
							<tr>
								<td align="right">供应商邮箱地址:&nbsp;&nbsp;&nbsp;</td>
								<td>
									<input type="text" class="tbl_query_text2" readonly="readonly" id="" value="<s:property value='vendorInfo.vendorEmail'/>" />
								</td>
								<td align="right">供应商地址:&nbsp;&nbsp;&nbsp;</td>
								<td>
									<input type="text" class="tbl_query_text2" readonly="readonly" id="" value="<s:property value='vendorInfo.vendorAddress'/>" />
								</td>
							</tr>
							<tr>
								<td align="right">供应商类别:&nbsp;&nbsp;&nbsp;</td>
								<td colspan="3">
									<s:if test='vendorInfo.taxpayerType=="S"'>
										<input type="text" class="tbl_query_text2" readonly="readonly" id="" value="小规模纳税人" />
									</s:if>
									<s:elseif test='vendorInfo.taxpayerType=="G"'>
										<input type="text" class="tbl_query_text2" readonly="readonly" id="" value="一般纳税人" />
									</s:elseif>
									<s:elseif test='vendorInfo.taxpayerType=="I"'>
										<input type="text" class="tbl_query_text2" readonly="readonly" id="" value="个体纳税人" />
									</s:elseif>
									<s:elseif test='vendorInfo.taxpayerType=="O"'>
										<input type="text" class="tbl_query_text2" readonly="readonly" id="" value="其他" />
									</s:elseif>
									<s:else></s:else>
								</td>
							</tr>
						</table>
						<div class="windowtitle" style="background:#004C7E; height:30px; line-height:30px; text-align:left; color:#FFF; font-size:12px;">供应商快递信息</div>
						<table id="tbl_context" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
							<tr>
								<td width="23%" align="right">收件人:&nbsp;&nbsp;&nbsp;</td>
								<td width="27%">
									<input type="text" class="tbl_query_text2" readonly="readonly" id="" value="<s:property value='vendorInfo.addressee'/>" />
								</td>
								<td width="23%" align="right">收件人电话:&nbsp;&nbsp;&nbsp;</td>
								<td width="27%">
									<input type="text" class="tbl_query_text2" readonly="readonly" id="" value="<s:property value='vendorInfo.addresseePhone'/>" />
								</td>
							</tr>
							<tr>
								<td align="right">收件人邮编:&nbsp;&nbsp;&nbsp;</td>
								<td>
									<input type="text" class="tbl_query_text2" readonly="readonly" id="" value="<s:property value='vendorInfo.addresseeZipcode'/>" />
								</td>
								<td align="right">收件地址:&nbsp;&nbsp;&nbsp;</td>
								<td>
									<input type="text" class="tbl_query_text2" readonly="readonly" id="" value="<s:property value='vendorInfo.addresseeAddress'/>" />
								</td>
							</tr>
							<tr>
								<td align="right">详细收件地址:&nbsp;&nbsp;&nbsp;</td>
								<td colspan="3">
									<input type="text" class="tbl_query_text2" readonly="readonly" id="" value="<s:property value='vendorInfo.addresseeAddressdetail'/>" />
								</td>
							</tr>
						</table>
						<div id="ctrlbutton" class="ctrlbutton" style="border:0px"></div>
						<div id="ctrlbutton" class="ctrlbutton" style="border:0px">
							<table id="tbl_tools" width="100%" border="0">
								<tr>
									<td align="left" width="400">
										<s:if test='vendorInfo.dataAuditStatus=="0"'>
											<a href="#" onclick="return audit(1);" name="cmdP" id="cmdP">
												<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1017.png" />
												审核通过
											</a>
											<a href="#" onclick="return audit(2);" name="cmdR" id="cmdR">
												<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1018.png" />
												审核不通过
											</a>
										</s:if>
										<input type="button" class="tbl_query_button" value="关闭" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" name="btCancel" id="btCancel"
											onclick="window.close()"
										/>
									</td>
								</tr>
							</table>
						</div>
						</form>
</body>
<script>
	function audit(auditsudits) {
		var t = document.getElementById('vendorId').value;
		//
		var dataOperationLabel = document.getElementById("dataOperationLabel").value;
		//alert(dataOperationLabel);
		var addAarray;
		var updateAarray;
		var deleteAarray;
		if (dataOperationLabel == 1) {
			addAarray = t;
		}
		if (dataOperationLabel == 2) {
			updateAarray = t;
		}
		if (dataOperationLabel == 3) {
			deleteAarray = t;
		}

		$.ajax({
			url : 'auditVendorInfo.action',
			type : 'POST',
			async : false,
			data : {
				addAarray : addAarray,
				updateAarray : updateAarray,
				deleteAarray : deleteAarray,
				auditsudits : auditsudits
			},
			dataType : 'text',
			error : function() {
				return false;
			},
			success : function(result) {
				alert(result);
				document.forms[0].submit();
				window.dialogArguments.saveVendorAuditSuccess();
				window.close();

			}
		});

	}
</script>
</html>