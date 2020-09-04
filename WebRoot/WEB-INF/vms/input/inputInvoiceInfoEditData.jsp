<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.vms.trans.model.BillInfo"
	import="com.cjit.vms.input.model.InputVatInfo"
	import="com.cjit.vms.trans.model.BillItemInfo"
	import="com.cjit.vms.trans.model.TransInfo"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../../page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<title>增值税管理平台</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/editview.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/XmlHttp.js"></script>
	<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/vms.js"></script>
	<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
	<link href="<%=bopTheme2%>/css/editView.css" type="text/css" rel="stylesheet">
	<script language="javascript" type="text/javascript">
		var issave = false;
		var msg = '<s:property value="message"/>';

	</script>
	<style type="text/css">
		.style1{
			width: 242px;
		}
	</style>
</head>
<body onLoad="javascript:tips()" onmousemove="MM(event)" onmouseout="MO(event)">
	<form name="Form1" method="post" action="" id="Form1">
	<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
	<tr>
		<td class="centercondition">
			<div id="tbl_current_status">
				<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> 
				<span class="current_status_menu">当前位置：</span> 
				<span class="current_status_submenu1">进项税管理</span> 
				<span class="current_status_submenu">发票管理</span>
				<span class="current_st atus_submenu">修改记录查看</span>

			</div>
			<div id="lessGridList4" style="overflow: auto; width: 100%;">
						<table class="lessGrid" cellspacing="0" rules="all" border="0" cellpadding="0" style="border-collapse: collapse; width: 100%;">
							<tr class="lessGrid head">
								<th style="text-align:center">
									<input style="width:13px;height:13px;" id="CheckAll" type="checkbox" onclick="cbxselectall(this,'selectInputTransIds')" />
								</th>
								<th style="text-align:center">用户编码</th>
								<th style="text-align:center">用户名称</th>
								<th style="text-align:center">修改时间</th>
								
							</tr>
							<s:iterator value="paginationList.recordList" id="iList" status="stuts">
							
							<tr id=""  align="center" class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
								<td align="center">
									<input style="width:13px;height:13px;" type="checkbox" name="selectInputTransIds"  />
								</td>
								<td align="left"><s:property value='#iList.userId' /></td>
								<td align="left"><s:property value='#iList.userName' /></td>
								<td align="left"><s:property value='#iList.createDate' /></td>
								
								</tr>
							</s:iterator>
			
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