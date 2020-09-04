<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../../page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<title>票据代扣代缴编辑</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/calendar_notime.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/XmlHttp.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/search.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/ylb.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/lhb.js"></script>
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/search.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/editview.js"></script>
	<script type="text/javascript">
		
	</script>
</head>
  <body>
   <div id="lessGridList4" style="overflow: auto; width: 100%;">
			<table class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0" style="border-collapse: collapse; width: 100%;">
			<tr class="lessGrid head">
				<th style="text-align:center">
					<input style="width:13px;height:13px;" id="CheckAll" type="checkbox" onclick="cbxselectall(this,'selectBillIds')" />
				</th>
				<th style="text-align:center">税票票号</th>
				<th style="text-align:center">缴款单位代码</th>
				<th style="text-align:center">缴款单位全称</th>
				<th style="text-align:center">科目编码</th>
				<th style="text-align:center">科目名称</th>
				<th style="text-align:center">填发日期</th>
				<th style="text-align:center">税款开始时间</th>
				<th style="text-align:center">税款结束时间</th>
				<th style="text-align:center">税款限缴日期</th>											
				<th style="text-align:center">实缴金额</th>
				<th style="text-align:center">操作</th>				
			</tr>
			</table>
			</div>
			<div id="anpBoud" align="Right" style="overflow:auto;width:100%;">
				<table width="100%" cellspacing="0" border="0">
					<tr>
						<td align="right"><s:component template="pagediv"/></td>
					</tr>
				</table>
			</div>
  </body>
</html>
