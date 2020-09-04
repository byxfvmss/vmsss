<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.util.PaginationList"
	import="com.cjit.vms.trans.model.BillInfo"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/lhb.js"></script>
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/search.js"></script>
	<script type="text/javascript">
		// [查询]按钮
		function submit(){
			document.forms[0].submit();
		}
	</script>
	<style type="text/css">
		.style1{
			width: 242px;
		}
	</style>
</head>
<body>
<form id="main" action="<c:out value='${webapp}'/>/listTransType.action" method="post">
<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
	<tr>
		<td class="centercondition">
		<div id="tbl_current_status">
			<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> 
				<span class="current_status_menu">当前位置：</span> 
				<span class="current_status_submenu1">参数管理</span>
				<span class="current_status_submenu">业务参数管理</span>
				<span class="current_status_submenu">交易类型设置</span>
		</div>
		<div class="widthauto1">
		<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
			<tr>
			<td align="left">
				交易类型编号:
			</td>
			<td>
				<input class="tbl_query_text" id="transTypeInfo.transName" type="text" name="transTypeInfo.transName"
					value="<s:property value='transTypeInfo.transName'/>"/>
			</td>
			<td align="left">
				交易名称:
			</td>
			<td>
				<input class="tbl_query_text" name="transTypeInfo.verificationName" value="<s:property value='transTypeInfo.verificationName'/>" />
			</td>
		</tr>
		<tr>
			<td>科目编号</td>
			<td>
				<input class="tbl_query_text" name="transTypeInfo.verificationName" value="<s:property value='transTypeInfo.verificationName'/>" />
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td style="text-align:left">
				<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 
				onclick="submit();" name="cmdSelect" value="查询" id="cmdSelect" />				
			</td>
		</tr>
		</table>
		<table id="tbl_tools" width="100%" border="0">
			<tr>
				<td align="left">
				<a href="#" onclick="openTaxDiskInfo('addTaxDiskInfo.action');" name="cmdFilter" id="cmdFilter"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon16.png"/>新增</a>
				<a href="#" name="cmdDelbt" id="cmdDelbt" ><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1019.png"/>删除</a>
				<a href="#" id="cmdExpbt" name="cmdExpbt" onClick="submitForm('exportTaxDiskInfo.action');"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png"/>导出</a>
				</td>
			</tr>
		</table>
	</div>
	<div id="lessGridList4" style="overflow: auto; width: 100%;">
	<table  class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0"  style="border-collapse: collapse;width: 100%;">
	<tr class="lessGrid head">
		<!-- <th style="text-align:center">
			<input style="width:13px;height:13px;" id="CheckAll" type="checkbox" onclick="cbxselectall(this,'selectBillIds')" />
		</th> -->
		<th style="text-align:center">科目编号</th>
		<th style="text-align:center">科目名称</th>
		<th style="text-align:center">交易认定编号</th>
		<th style="text-align:center">交易认定名称</th>
		<th style="text-align:center">纳税主体类型</th>
		<th style="text-align:center">备注</th>
	</tr>
	<s:iterator value="paginationList.recordList" id="iList" status="stuts">
		
	<tr align="center"class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
		<!--
		<td align="center"><%//=bill.getBillNo()%></td>
		-->
		<td align="center"><s:property value='transId'/></td>
		<td align="center"><s:property value='transName'/></td>
		<td align="center"><s:property value='verificationId'/></td>
		<td align="center"><s:property value='verificationName'/></td>
		<td align="center"><s:property value='taxTypeName'/></td>
		<td align="center"><s:property value='remark'/></td>
		</tr>
	</s:iterator> 
	</table>
	</div>
	<div id="anpBoud" align="Right" style="width:100%;">
        <table width="100%" cellspacing="0" border="0">
            <tr>           
                <td align="right"><s:component template="pagediv"/></td>
            </tr>
        </table>
	</div>
	</td>
	</tr>
	</table>
	</form>
	</body>
</html>