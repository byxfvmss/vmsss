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
	
	$("#btnInit").click(function() {
		//submitForm("showAlertList.action");   .src="itemRateTreeInst1.action"
		parent.frames["leftFrame"].location.href="itemRateTreeInst1.action";
		 alert(ok);
	});
	$("#btnPercent").click(function(){
		alert("弹出修改百分比页面进行修改");
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
	function saveparamSuccess(instCode){
	submitForm("listTransVerification.action?instCode="+instCode);
	}
</script>
	</head>
	<body><!-- listTransVerification.action -->
		<form id="main" action="listTransVerification.action" method="post"
			enctype="multipart/form-data">
			<table id="tbl_main" cellpadding="0" cellspacing="0"
				class="tablewh100">
				<tr>
					<td class="centercondition">
						<div class="widthauto1">
							<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
								<tr>
									<s:hidden id="itemCode" name="transTypeInfoPram.itemCode"></s:hidden>
									<input type="button" name="cmdDistribute" value="初始化" 	id="btnInit"  />
									<input type="button" name="cmdDistribute" value="预警百分比" 	id="btnPercent"  />
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
									<!-- <th>发票号</th> -->
									<th>
										发票类型
									</th>
									<th>
										存量
									</th>
									<th>
										预警百分比
									</th>
									<th>
										编辑
									</th>
								</tr>
								<s:iterator value="paginationList.recordList" id="iList"
									status="stuts">
									<tr align="center"
										class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
										<td>
										<input type="hidden" name="invoiceType" value="<s:property value='invoiceType'/>">
											<s:property  value="@com.cjit.vms.trans.util.DataUtil@getFapiaoTypeCH(invoiceType)" />	
										</td>
										<td>
											<s:property  value='alertNum' />	<!--存量-->
										</td>
										<td>
											<s:property value='invoicePercent' />%
										</td>
										<td>
										<a href="alertParamEdit.action?instCode=<s:property value='instCode'/>&invoiceType=<s:property value='invoiceType'/>" /> 
			    							<img title="预警参数设置" style="border-width: 0px;" src="http://localhost:8080/vms/themes/images/icons/icon1005.png">
			    						</a>
										</td>
									</tr>
								</s:iterator>

							</table>
						</div>
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>
