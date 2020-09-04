<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<!--
	createTime:2016.3.
	author:沈磊
	content:转出比例 metlife
  -->
<html>
<head>
<title>增值税管理平台</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../../../page/include.jsp"%>

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
	
	function querylist(){
		document.forms[0].action="financeMonth.action";
		document.forms[0].submit();
	}
	function cancel(){
	if (checkChkBoxesSelected("ruleId")) {
			 var vtorIds = document.getElementsByName("ruleId");
			 var vtorId="";
                    for (i = 0; i < vtorIds.length; i++) {
                        if (vtorIds[i].checked == true) {
			
                           vtorId += vtorIds[i].value+",";
                        }
                    }
            vtorId=vtorId.substring(0,vtorId.length-1);
        document.forms[0].action="delFinanceMonth.action?vtorId="+vtorId;
		document.forms[0].submit();
	}else{
		alert("请选择数据!");
	}
	
	}
	function upload(){
	var fileId = document.getElementById("fileId");
		if (fileId.value.length > 0) {
			if (fileId.value.lastIndexOf(".XLS") > -1
					|| fileId.value.lastIndexOf(".xls") > -1) {
				document.forms[0].action="uploadFinanceMonth.action";
				document.forms[0].submit();
				//document.forms[0].action = 'listInputVat.action';
			} else {
				alert("文件格式不对，请上传Excel文件。");
			}
		} else {
			alert("请先选择要上传的文件。");
		}
		
	}
</script>
</head>
<body>
	<form name="Form1" method="post" action="financeMonth.action" id="Form1" enctype="multipart/form-data">
		<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
			<tr>
				<td class="centercondition">
					<s:component template="rocketMessage" />
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" />
						<span class="current_status_menu">当前位置：</span>
						<span class="current_status_submenu1">VMS进项管理</span>
						<span class="current_status_submenu">进项管理</span>
						<span class="current_status_submenu">财务月管理</span>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" width="80%" border="0">
							<tr>
								<td align="left">财务月</td>
								<td>
									<input  name="inputInvoiceInfo.accountPeriod" type="text" class="tbl_query_text" value="<s:property value='inputInvoiceInfo.accountPeriod' />"  />
								</td>
								<td align="left"></td>
								<td align="right">
								</td>
								<td align="center">
								</td>
								<td align="left">
								</td>
								<td></td>
								<td>
								<input type="button" class="tbl_query_button" value="查询" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" name="BtnView" id="BtnView"
										onclick="querylist()"/>
								</td>
							</tr>
						</table>
					</div>
					<table id="tbl_tools" width="100%" border="0">
						<tr>
							<td align="left" width="280" class="pleft15p"> 
							<input type='file' name='theFile' id='fileId' size='25' style="height:26px;" /> 
							</td> 
							<td align="left"> 
 								<a href="#" onclick="upload();" name="cmdFilter" id="cmdFilter">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1007.png" />
									上传 
								</a> 
							</td> 
							<td align="center" >
								<a href="#" name="upLoad" id="upLoad" onclick="cancel()">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />
									删除
								</a>
							</td>
							<td align="center" >
							</td>
							<td align="center" >
							</td>
							<td align="center" ></td>
							<td align="center" ></td>
							<td align="center" width="5%"></td>
						</tr>
					</table>
					<div id="lessGridList4" style="overflow: auto; width: 100%;">
						<table class="lessGrid" cellspacing="0" rules="all" border="0" cellpadding="0" style="border-collapse: collapse;width: 100%;">
							<tr class="lessGrid head">
								<th width="4%" style="align:center">
									<input id="CheckAll" style="width:13px;height:13px;" type="checkbox" onClick="checkAll(this,'ruleId')" />
								</th>			
								<th style="text-align:center" width="32%">财务月</th>
								<th style="text-align:center" width="32%">开始日期</th>
								<th style="text-align:center" width="32%">结束日期</th>				
							</tr>
							
							<s:iterator value="paginationList.recordList" id="iList" status="stuts">
								<tr>
									<td style="align:center;width:13px;" style="text-align:center">
										<input type="checkbox" style="width:13px;height:13px;" name="ruleId" value="<s:property value="accountPeriod"/>" />
									</td>
									<td style="text-align:center"><s:property value='accountPeriod' /></td>
									<td style="text-align:center"><s:property value='accountPeriodStrart' /></td>
									<td style="text-align:center"><s:property value='accountPeriodEnd' /></td>
								</tr>
							</s:iterator>	
						</table>
					</div>
					<div id="anpBoud" align="Right" style="overflow:auto;width:100%;">
						<table width="100%" cellspacing="0" border="0">
							<tr>
								<td align="right">
									<s:component template="pagediv" />
								</td>
							</tr>
						</table>
					</div>
			</tr>
		</table>
	</form>
</body>
</html>
