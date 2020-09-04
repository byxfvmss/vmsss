<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../page/include.jsp"%>
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">

<script type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
<meta http-equiv="Pragma" content="no-cache" />
<title>税控钥匙信息管理</title>

<script type="text/javascript">
	function openTaxKeyInfo(url){
		OpenModalWindow(url,500,550,true);
	}
	function submitForm(actionUrl){
		var form = document.getElementById("main");
		form.action=actionUrl;
		form.submit();
		form.action="taxKeyInfoList.action";
	}
	
	function submitQueryForm(actionUrl){
		var form = document.getElementById("main");
		form.action=actionUrl;
		form.submit();
		form.action="taxKeyInfoList.action";
	}
	function submitForms(actionUrl,value){
		$("#type").val(1);
		var form = document.getElementById("main");
		form.action=actionUrl;
		form.submit();
		form.action="taxKeyInfoList.action";
	}
	

	
	$(function(){
		$("#cmdDelbt").click(function(){
			$del_items=$("input[name='selectTaxKeys']:checked");
			if($del_items.size()==0){
				alert("请选择您要删除的数据！");
				return false;
			}
			if(confirm("您确定要删除吗？")){
				submitForm('deleteTaxKeyInfo.action')
			}
		});
	});
</script>
</head>
<body>
<!-- 开始 -->
<form id="main" action="<c:out value='${webapp}'/>/taxKeyInfoList.action" method="post">
<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
	<tr>
		<td class="centercondition">
		<div id="tbl_current_status">
			<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> 
			<span class="current_status_menu">当前位置：</span> 
			<span class="current_status_submenu1">参数管理</span>
			<span class="current_status_submenu">税控设备管理</span>
			<span class="current_status_submenu">税控钥匙信息管理</span>
		</div>
		<div class="widthauto1">
			<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td width="50">税控钥匙编号</td>
					<td width="130">
						<input type="text" class="tbl_query_text" id="taxKeyNo" name="taxKeyNo" value='<s:property value="taxKeyNo"/>' />
					</td>
					<td width="100">纳税人识别号</td>
					<td width="130">
						<input type="text" class="tbl_query_text" id="taxNo" name="taxNo" value='<s:property value="taxNo"/>' >
					</td>
					<td style="text-align:left">
					<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 
					onclick="submitQueryForm('taxKeyInfoList.action?type=2');" name="cmdSelect" value="查询" id="cmdSelect" />				
					</td>
				</tr>
			</table>
		</div>
		<table id="tbl_tools" width="100%" border="0">
			<tr>
				<td align="left">
				<a href="#" onclick="openTaxKeyInfo('addTaxKeyInfo.action');" name="cmdFilter" id="cmdFilter"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon16.png"/>新增</a>
				<a href="#" name="cmdDelbt" id="cmdDelbt" ><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1019.png"/>删除</a>
				</td>
			</tr>
		</table>
		<div id="lessGridList1" style="width: 100%; height:360px;overflow: auto;">
		<table  class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0"  style="border-collapse: collapse;width: 100%; margin-top:5px;">
			<tr class="lessGrid head gridbr">
				<th width="3%" style="text-align:center"><input id="CheckAll" style="width:13px;height:13px;" type="checkbox" onClick="checkAll(this,'selectTaxKeys')" /></th>
				<th style="text-align: center">序号</th>
				<th style="text-align: center">税控钥匙编号</th>
				<th style="text-align: center">纳税人识别号</th>
				<th style="text-align: center">开票终端标示</th>
				<th style="text-align: center">ip地址</th>
				<th style="text-align: center">服务端口</th>
				<th style="text-align: center">操作</th>
				
			</tr>
			<s:iterator value="paginationList.recordList" id="iList" status="stuts"><tr align="center"class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
			    <td><input type="checkbox" style="width:13px;height:13px;" name="selectTaxKeys" value="<s:property value="#iList.taxKeyNo"/>,<s:property value="#iList.taxNo"/>"/></td>
				<td align="center"><s:property value='#stuts.count' /></td>		
				<td><s:property value='taxKeyNo'/></td>
				<td><s:property value='taxNo'/></td>
				<td><s:property value='bilTerminalFlag'/></td>
				<td><s:property value='ipAddress'/></td>
				<td><s:property value='servletPort'/></td>
				<td>
					<a href="javascript:void(0);" onClick="openTaxKeyInfo('editTaxKeyInfo.action?taxKeyNo=<s:property value="#iList.taxKeyNo" />&taxNo=<s:property value="#iList.taxNo" />');"><img src ="<c:out value="${bopTheme}"/>/themes/images/icons/icon1005.png" title="编辑" style="border-width: 0px;" /></a>
				</td>
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