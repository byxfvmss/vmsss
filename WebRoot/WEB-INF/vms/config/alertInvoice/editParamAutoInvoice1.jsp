<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@ include file="/page/include.jsp"%>
		<%@ include file="/page/modalPage.jsp"%>
		<link href="<%=bopTheme2%>/css/main.css" type="text/css"
			rel="stylesheet">
		<link href="<%=webapp%>/page/common.css" type="text/css"
			rel="stylesheet">
		<script src="<%=bopTheme%>/js/MessageBox/js/messageBox.js"
			type="text/javascript">
</script>
		<link href="<%=bopTheme%>/js/MessageBox/css/messageBox.css"
			rel="stylesheet" type="text/css" />
		<script type="text/javascript"
			src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js">
</script>
		<script type="text/javascript"
			src="<%=webapp%>/page/js/jquery/jquery_1.42.js">
</script>
		<script type="text/javascript" src="<%=webapp%>/page/js/window.js">
</script>
		<script type="text/javascript" src="<%=webapp%>/page/js/search.js">
</script>
		<script language="javascript" type="text/javascript"
			src="<%=bopTheme%>/js/main.js">
</script>
		<meta http-equiv="Pragma" content="no-cache" />
		<title>交易认定管理</title>
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
	filter: alpha(opacity =         90);
	-moz-opacity: 0.9;
	display: none;
}
</style>
		<script type="text/javascript">
$(function() {
	$("#btnEdit").click(function(){
		//alert("ok");
		submitForm("alertRateEdit.action");//81
	});
	$("#btnSearch").click(function() {
		submitForm("commitEdit.action");
		parent.frames["leftFrame"].init();
	});
	function submitForm(actionUrl) {
		var form = $("#main");
		var oldAction = form.attr("action");
		form.attr("action", actionUrl);
		form.submit();
		form.attr("action", oldAction);
	}
	$("#CheckAll").click(function() {
		$("[name=checkedlineNo]").attr("checked", $(this).attr("checked"))
	});
})
function CheckNotNull(){
	var StrText = document.getElementById(alertNum).value;
	if(StrText==""||StrText==null){
	alert("请作正确的修改");
	return;
	}
}

</script>
	</head>
	<body>
		<form id="main" action="commitEdit.action" method="post"
			enctype="multipart/form-data">
			<table id="tbl_main" cellpadding="0" cellspacing="0"
				class="tablewh100">
				<p></p>
					
				<tr>
					<td class="centercondition">
				
						<div class="widthauto1">
							<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
								<tr>
								 <input type="button" name="cmdDistribute" id="btnSearch"  value="修改" />
									</td>
								</tr>
							</table>
						</div>
						<table id="tbl_tools" width="100%" border="0">
						</table>
						<div id="lessGridList1" style="overflow: auto; width: 100%;">
							<table class="lessGrid" cellspacing="0" rules="all" border="0"
								cellpadding="0" style="border-collapse: collapse; width: 100%;">
								<tr class="lessGrid head">
									<th>
										发票类型
									</th>
									<th>
										存量
									</th>
									<th>
										预警百分比
									</th>
									 
								</tr>
								<s:iterator value="paginationList.recordList" id="iList"
									status="stuts">
									<tr align="center"
										class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
										<td>
										<input type="hidden" name="invoiceType" value="<s:property  value='invoiceType' />"/>
										<input type="text"  readonly="readonly" value="<s:property  value="@com.cjit.vms.trans.util.DataUtil@getFapiaoTypeCH(invoiceType)" />">
										<input type="hidden" id="instCode" name="instCode" value="<s:property  value='instCode' />"/> 		
										</td>
										<td>
										<input type="text" id="alertNum" name="alertNum" value="<s:property  value='alertNum' />" />
										</td>
										<td>
										<input type="text" readonly="readonly" name="invoicePercent" value="<s:property value='invoicePercent' />"/>%
										</td>
										 
									</tr>
								</s:iterator>

							</table>
						</div>
						<!--<div id="anpBoud" align="Right" style="width: 100%;">
							<table width="100%" cellspacing="0" border="0">
								<tr>
									<td align="right">
										<s:component template="pagediv" />
									</td>
								</tr>
							</table>
						</div>-->
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>
