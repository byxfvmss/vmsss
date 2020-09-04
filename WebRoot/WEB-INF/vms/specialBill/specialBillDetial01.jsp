<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.util.PaginationList"
	import="com.cjit.vms.input.model.SpecialBillWithhold"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<title>增值税管理平台</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../../page/include.jsp"%>
<link href="<%=bopTheme2%>/css/main.css" type="text/css"
	rel="stylesheet">
<link href="<%=webapp%>/page/common.css" type="text/css"
	rel="stylesheet">

<script src="<%=bopTheme%>/js/MessageBox/js/messageBox.js"
	type="text/javascript"></script>
<link href="<%=bopTheme%>/js/MessageBox/css/messageBox.css"
	rel="stylesheet" type="text/css" />
<script type="text/javascript"
	src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript"
	src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/search.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/main.js"></script>
<meta http-equiv="Pragma" content="no-cache" />
	<script type="text/javascript">
function closes(url){
	document.forms[0].action=url;
	document.forms[0].submit();
}
function editbill(){
	document.forms[0].action="edtieSpecialBill.action?billid=<s:property value='specialBillWithhold.billNo'/>"
	document.forms[0].submit();
}		
	</script>
</head>
<body>
<form name="Form1" method="post" action="listVendor.action" id="Form1" enctype="multipart/form-data">
<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
	<tr>
		<td class="centercondition">
			<s:component template="rocketMessage" />
			<div id="tbl_current_status">
				<img src="<%=bopTheme%>/themes/images/icons/icon13.png"/> 
				<span class="current_status_menu">当前位置：</span> 
				<span class="current_status_submenu1">进项税管理</span>
				<span class="current_status_submenu">其他票据</span>
				<span class="current_status_submenu">票据代缴代扣</span>
				<span class="current_status_submenu">编辑</span>
			</div>

		</td>	
	</tr>
	<tr>
		<td>
		<table id="tbl_query" cellpadding="0" cellspacing="0" width="100%" border="1">
			<tr>
				<td align="center">税票票号
				</td>
				<td align="left"><s:property value='specialBillWithhold.billNo'/>
				</td>
				<td align="center">征收机关
				</td>
				<td align="left"><s:property value='specialBillWithhold.taxGov'/>
				</td>
			</tr>
			<tr>
				<td align="center">缴款单位代码
				</td>
				<td align="left"><s:property value='specialBillWithhold.taxNo'/>
				</td>
				<td align="center">缴款单位全称
				</td>
				<td align="left"><s:property value='specialBillWithhold.taxInstChn'/>
				</td>
			</tr>
			<tr>
				<td align="center">缴款单位银行
				</td>
				<td align="left"><s:property value='specialBillWithhold.bankandname'/>
				</td>
				<td align="center">缴款单位账号
				</td>
				<td align="left"><s:property value='specialBillWithhold.bankandaccount'/>
				</td>
			</tr>
			<tr>
				<td align="center">预算科目编码
				</td>
				<td align="left"><s:property value='specialBillWithhold.subjectId'/>
				</td>
				<td align="center">预算科目名称
				</td>
				<td align="left"><s:property value='specialBillWithhold.subjectName'/>
				</td>
			</tr>
			<tr>
				<td align="center">收款国库
				</td>
				<td align="left"><s:property value='specialBillWithhold.nationalTre'/>
				</td>
				<td align="center">填发日期
				</td>
				<td align="left"><s:property value='specialBillWithhold.writeData'/>
				</td>
			</tr>
			<tr>
				<td align="center">税款开始时间
				</td>
				<td align="left"><s:property value='specialBillWithhold.belongDataS'/>
				</td>
				<td align="center">税款结束时间
				</td>
				<td align="left"><s:property value='specialBillWithhold.belongDataE'/>
				</td>
			</tr>
			<tr>
				<td align="center">税款缴限日期
				</td>
				<td align="left"><s:property value='specialBillWithhold.payData'/>
				</td>
				<td align="center">合计税额
				</td>
				<td align="left"><s:property value='specialBillWithhold.taxAmtSum'/>
				</td>
			</tr>
			<tr>
				<td align="center">抵扣状态
				</td>
				<td align="left">
				<s:property value='specialBillWithhold.dataStatus'/>
				</td>
				<td align="center">
				</td>
				<td align="center">
				</td>
			</tr>
			<tr>
				<td align="center" >备注
				</td>
				<td align="center" >
				</td>
				<td align="center">
				</td>
				<td align="center">
				</td>
			</tr>
			<tr>
				<td align="center" colspan="4" disabled="disabled">
				<textarea rows="6" cols="150" disabled="disabled"><s:property value='specialBillWithhold.remark'/></textarea>
				</td>
			</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td align="center" colspan="4">
		<div id="ctrlbutton" class="ctrlbutton" style="border:0px">
			<input type="button" class="tbl_query_button" value="关闭" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
			name="btCancel" id="btCancel" onclick="closes('inputSpecialbillDeduction.action?fromFlag=menu')" />
		</div>
		</td>
	</tr>
</table>
</form>
 </body>
</html>
