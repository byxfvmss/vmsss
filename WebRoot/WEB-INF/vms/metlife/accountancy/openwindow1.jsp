<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<!--
	createTime:2016.3.
	author:沈磊
	content:弹窗 metlife
  -->
<html>
<head>
<title>增值税管理平台</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../../../page/include.jsp"%>
<%@ include file="../../../../page/modalPage.jsp"%>
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
<link href="<%=webapp%>/page/common.css" type="text/css" rel="stylesheet">
<link href="<%=bopTheme2%>/css/main.css" type="text/css"
	rel="stylesheet">
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/calendar_notime.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/XmlHttp.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/search.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/ylb.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/lhb.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/comm.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/search.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<meta http-equiv="Pragma" content="no-cache" />
<script type="text/javascript">
	
	function create(){
	if($("#accountingPeriod").val()!=""){
	if(confirm("是否确认导出?")){
		$("base").attr("target","");
        document.getElementById("Form1").action = "toExcelAccountingEntriesInfo.action";
        document.getElementById("Form1").submit();
        document.gevElementById("Form1").action = "openwindow1.action";
    	$("base").attr("target","_self");
	}
	}else{
	alert("不可为空");
	}
	}
	function cancel(){
	window.close();
	}
</script>
</head>
<body style="overflow:hidden">
<base target="_self">
	<form name="Form1" method="post" action="" id="Form1" >
	<input type="hidden" name="vdName" id="vdName" />
		<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh10">
			<tr height="400px;t">
				<td class="centercondition">
					<s:component template="rocketMessage" />
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" width="80%" border="0">
							<tr>
								<td align="left">财务月</td>
								<td>
									<input  class="tbl_query_text"  type="text" id="accountingPeriod"
                                       name="accountingEntriesInfo.accountingPeriod"
                                       value="<s:property value='accountingEntriesInfo.accountingPeriod'/>" />
								</td>
								<td align="left">T10</td>
								<td>
								<s:if
										test="authInstList != null && authInstList.size > 0">
										<s:select name="accountingEntriesInfo.la10Branch" list="authInstList"
											listKey="instId" listValue='instName'
											cssClass="tbl_query_text5" />
									</s:if> <s:if test="authInstList == null || authInstList.size == 0">
										<select name="accountingEntriesInfo.la10Branch" class="readOnlyText">
											<option value="">请分配机构权限</option>
										</select>
									</s:if>
								</td>
								<td></td>
								<td>
								</td>
							</tr>
						</table>
						<table id="tbl_tools" width="100%" border="0">
						<td width="80%"></td>
						<td width="20%" align="right">
						<input type="button" class="tbl_query_button" value="生成" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" name="BtnView" id="BtnView"
										onclick="create()"/>
						<input type="button" class="tbl_query_button" value="取消" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" name="BtnView" id="BtnView"
										onclick="cancel()"/>
						</td>
						</table>				
					</div>
			</tr>
		</table>
	</form>
</body>
</html>