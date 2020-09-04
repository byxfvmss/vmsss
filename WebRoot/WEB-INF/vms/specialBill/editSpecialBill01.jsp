<!--file: <%=request.getRequestURI()%> -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../../page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<title>增值税管理平台</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/validator.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/editview.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/XmlHttp.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/vms.js"></script>
<!-- MessageBox -->
<script src="<%=bopTheme%>/js/MessageBox/js/messageBox.js" type="text/javascript"></script>
<link href="<%=bopTheme%>/js/MessageBox/css/messageBox.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
<link href="<%=bopTheme2%>/css/editView.css" type="text/css" rel="stylesheet">
<style type="text/css" rel="stylesheet">
.tbl_query_text {
	width: 90%
}
</style>
<script language="javascript" type="text/javascript">
	$(
			function() {
				$("#btnSave").click(function() {
					if (checkForm()) {
						submitForm("updateSpecialBill01.action");
					}

				});
				$("#btnBack").click(function() {
					submitForm("listSpecialBill.action");

				});

				function checkForm() {

					/* $("#tbl_context .tbl_query_text").each(function(result) {
						var msg = $(this).parent().prev().html();
						msg = msg.replace(":","");
					if (fucCheckNull(this, msg+"不能为空")) {
						
						return false;
					}
					}) */
					var list = $("#tbl_context .tbl_query_text");
					for (i = 0; i < list.length; i++) {
						var msg = $(list[i]).parent().prev().html();
						msg = msg.replace(":", "");
						if (false == fucCheckNull(list[i], msg + "不能为空")) {
							return false;
						}
					}
					return true;

				}
				function submitForm(actionUrl) {
					var form = $("#main");
					var oldAction = form.attr("action");
					form.attr("action", actionUrl);
					form.submit();
					form.attr("action", oldAction);
				}

				var isEdit = $("#isEdit").val();
				function initPage() {
					$("#billNo").attr("readonly", "true").addClass(
							"tbl_query_text_readonly")
					if ('false' == isEdit) {
						$("#tbl_context .tbl_query_text").attr("readonly",
								"true").addClass("tbl_query_text_readonly");
						$("#btnSave").attr("style", "display:none");
						$("#remark").attr("readonly", "true").addClass(
								"tbl_query_text_readonly").height(78);
					} else {
						$("#belongDataS,#belongDataE,#payData,#writeData")
								.click(function() {
									WdatePicker({
										dateFmt : 'yyyy-MM-dd'
									})
								});

					}

				}
				initPage();
			})
</script>
<style type="text/css">
.style1 {
	width: 242px;
}
</style>
</head>
<body>
	<form id="main" method="post" action="updateSpecialBill01.action">
		<input type="hidden" id="isEdit" value="<s:property value='isEdit'/>">
		<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
			<tr>
				<td class="centercondition">
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" />
						<span class="current_status_menu">当前位置：</span>
						<span class="current_status_submenu1">VMS进项税管理</span>
						<span class="current_status_submenu">其他票据</span>
						<span class="current_status_submenu">代扣代缴票据导入</span>
						<span class="current_status_submenu">数据编辑</span>
					</div>
					</br>
					<table id="tbl_context" cellspacing="0" width="100%" align="center" cellpadding="0">
						<tr class="row1">
							<td align="right" width="20%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">税票票号:&nbsp;&nbsp;&nbsp;</td>
							<td align="left" width="30%">
								<input type="text" class="tbl_query_text" name="withholdBill.billNo" id="billNo" value="<s:property value='withholdBill.billNo'/>" />
							</td>
							<td align="right" width="20%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">征收机关:&nbsp;&nbsp;&nbsp;</td>
							<td align="left" width="30%">
								<input type="text" class="tbl_query_text" name="withholdBill.taxGov" id="taxGov" value="<s:property value='withholdBill.taxGov'/>" />
							</td>
						</tr>
						<tr class="row1">
							<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">缴款单位代码:&nbsp;&nbsp;&nbsp;</td>
							<td align="left">
								<input type="text" class="tbl_query_text" name="withholdBill.taxNo" id="taxNo" value="<s:property value='withholdBill.taxNo'/>"
							</td>
							<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">缴款单位全称:&nbsp;&nbsp;&nbsp;</td>
							<td align="left">
								<input type="text" class="tbl_query_text" name="withholdBill.taxInstChn" id="taxInstChn" value="<s:property value='withholdBill.taxInstChn'/>"
							</td>
						</tr>
						<tr class="row1">
							<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">缴款单位银行:&nbsp;&nbsp;&nbsp;</td>
							<td align="left">
								<input type="text" class="tbl_query_text" name="withholdBill.bankandname" id="bankandname" value="<s:property value='withholdBill.bankandname'/>" />
							</td>
							<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">缴款单位账号:&nbsp;&nbsp;&nbsp;</td>
							<td align="left">
								<input type="text" class="tbl_query_text" name="withholdBill.bankandaccount" id="bankandaccount" value="<s:property value='withholdBill.bankandaccount'/>" />
							</td>
						</tr>
						<tr class="row1">
							<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">预算科目编码:&nbsp;&nbsp;&nbsp;</td>
							<td align="left">
								<input type="text" class="tbl_query_text" name="withholdBill.subjectId" id="subjectId" value="<s:property value='withholdBill.subjectId'/>" />
							</td>
							<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">预算科目名称:&nbsp;&nbsp;&nbsp;</td>
							<td align="left">
								<input type="text" class="tbl_query_text" name="withholdBill.subjectName" id="subjectName" value="<s:property value='withholdBill.subjectName'/>" />
							</td>
						</tr>
						<tr class="row1">
							<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">预算科目级次:&nbsp;&nbsp;&nbsp;</td>
							<td align="left">
								<input type="text" class="tbl_query_text" name="withholdBill.subjectClass" id="subjectClass" value="<s:property value='withholdBill.subjectClass'/>" />
							</td>
							<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">&nbsp;&nbsp;&nbsp;</td>
							<td align="left"></td>
						</tr>
						<tr class="row1">
							<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">收款国库:&nbsp;&nbsp;&nbsp;</td>
							<td align="left">
								<input type="text" class="tbl_query_text" name="withholdBill.nationalTre" id="nationalTre" value="<s:property value='withholdBill.nationalTre'/>" />
							</td>
							<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">填发日期:&nbsp;&nbsp;&nbsp;</td>
							<td>
								<input type="text" class="tbl_query_text" name="withholdBill.writeData" id="writeData" value="<s:property value='withholdBill.writeData'/>" />
							</td>
						</tr>
						<tr class="row1">
							<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">税款开始时间:&nbsp;&nbsp;&nbsp;</td>
							<td align="left">
								<input type="text" class="tbl_query_text" name="withholdBill.belongDataS" id="belongDataS" value="<s:property value='withholdBill.belongDataS'/>" />
							</td>
							<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">税款结束时间:&nbsp;&nbsp;&nbsp;</td>
							<td align="left">
								<input type="text" class="tbl_query_text" name="withholdBill.belongDataE" id="belongDataE" value="<s:property value='withholdBill.belongDataE'/>" />
							</td>
						</tr>
						<tr class="row1">
							<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">税款缴限日期:&nbsp;&nbsp;&nbsp;</td>
							<td align="left">
								<input type="text" class="tbl_query_text" name="withholdBill.payData" id="payData" value="<s:property value='withholdBill.payData'/>" />
							</td>
							<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">合计税额:&nbsp;&nbsp;&nbsp;</td>
							<td>
								<input type="text" class="tbl_query_text" name="withholdBill.taxAmtSum" id="taxAmtSum" value="<s:property value='withholdBill.taxAmtSum'/>" />
							</td>
						</tr>
						<tr class="row1">
							<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">抵扣状态:&nbsp;&nbsp;&nbsp;</td>
							<td align="left">
								<input type="hidden" class="tbl_query_text " name="withholdBill.dataStatus" id="dataStatus" value="<s:property value='withholdBill.dataStatus'/>" />
								<select disabled="disabled" class="tbl_query_text tbl_query_text_readonly" style="width:100px">
									<option>不可抵扣</option>
								</select>
							</td>
							<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">&nbsp;&nbsp;&nbsp;</td>
							<td align="left"></td>
						</tr>
						<tr class="row1">
							<td align="right" style="background-color:#F0F0F0;font-weight:bold;color:#727375;">备注:&nbsp;&nbsp;&nbsp;</td>
							<td align="left" colspan="3">
								<textarea id="remark" style="width: 72%;height:78px" rows="3" name="withholdBill.remark" id="remark"><s:property value='withholdBill.remark' /></textarea>
							</td>
						</tr>
					</table>
					</br>
					</br>
					<div id="ctrlbutton" class="ctrlbutton" style="border:0px">
						<input type="button" class="tbl_query_button" value="保存" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" name="btnSave" id="btnSave" />
						<input type="button" class="tbl_query_button" value="返回" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" name="btnBack" id="btnBack" />
					</div>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>