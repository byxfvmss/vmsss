<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<!--
	createTime:2016.2.
	author:沈磊
	content:保单关联管理 metlife
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
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/calendar_notime.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/XmlHttp.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/search.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/ylb.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/lhb.js"></script>
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/search.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<meta http-equiv="Pragma" content="no-cache" />
<script type="text/javascript">
	function exelout(){
	submitAction(document.forms[0], "insureRelaToExcel.action");
	document.forms[0].action="insureRelationManage.action";
	}
	function querylist(){
	document.forms[0].action = "insureRelationManage.action";
	 document.forms[0].submit();
	}
	
</script>
</head>
<body>
	<form name="Form1" method="post" action="insureRelationManage.action?fromFlag=menu" id="Form1" enctype="multipart/form-data">
		<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
			<tr>
				<td class="centercondition">
					<s:component template="rocketMessage" />
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" />
						<span class="current_status_menu">当前位置：</span>
						<span class="current_status_submenu1">VMS单证管理</span>
						<span class="current_status_submenu">单证管理</span>
						<span class="current_status_submenu">保单关联管理</span>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" width="80%" border="0">
							<tr>
								<td align="left">投保单号</td>
								<td>
									<input type="text" name="documentManageInfo.appNum" value='<s:property value="documentManageInfo.appNum"/>' class="tbl_query_text" />
								</td>
								<td align="left">保险单证号</td>
								<td>
									<input type="text" name="documentManageInfo.formNum" value='<s:property value="documentManageInfo.formNum"/>' class="tbl_query_text" />
								</td>
								<td align="left"> 保单号</td>
								<td>
									<input type="text" name="documentManageInfo.policyNum" value='<s:property value="documentManageInfo.policyNum"/>' class="tbl_query_text" />
								</td>
								<td>
								基准日期
								</td>
								<td>
								 <input id="startDate" class="tbl_query_time"  type="text"
                                       name="documentManageInfo.beginDate"
                                       value="<s:property value='documentManageInfo.beginDate'/>"
                                       onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}'})"
                                       size='11'/>
                                --
                                <input id="endDate" class="tbl_query_time"  type="text" name="documentManageInfo.endDate"
                                       value="<s:property value='documentManageInfo.endDate'/>"
                                       onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}'})"
                                       size='11'/>
								</td>
								
							</tr>
							<tr>
							<td align="left">投保单号渠道</td>
								<td>
									<select style="width:150px;" id="documentManageInfo.channel" name="documentManageInfo.channel">
										<option value="" <s:if test='documentManageInfo.channel==""'>selected</s:if> <s:else></s:else>>全部</option>
										<option value="AGY-IMAP" <s:if test='documentManageInfo.channel=="AGY-IMAP"'>selected</s:if> <s:else></s:else>>AGY-IMAP</option>
										<option value="DM" <s:if test='documentManageInfo.channel=="DM"'>selected</s:if> <s:else></s:else>>DM </option>
										<option value="TM" <s:if test='documentManageInfo.channel=="TM"'>selected</s:if> <s:else></s:else>>TM</option>
										<option value="DGT" <s:if test='documentManageInfo.channel=="DGT"'>selected</s:if> <s:else></s:else>>DGT</option>
										<option value="BXS" <s:if test='documentManageInfo.channel=="BXS"'>selected</s:if> <s:else></s:else>>BXS</option>
										<option value="AGY-MAP" <s:if test='documentManageInfo.channel=="AGY-MAP"'>selected</s:if> <s:else></s:else>>AGY-MAP</option>
									</select>	
								</td>
								
								<td>保险单证号渠道</td>
								<td>
									<select style="width:150px;" id="documentManageInfo.channel" name="documentManageInfo.tepchannel">
										<option value="" <s:if test='documentManageInfo.channel==""'>selected</s:if> <s:else></s:else>>全部</option>
										<option value="AGY-IMAP" <s:if test='documentManageInfo.channel=="AGY-IMAP"'>selected</s:if> <s:else></s:else>>AGY-IMAP</option>
										<option value="DM" <s:if test='documentManageInfo.channel=="DM"'>selected</s:if> <s:else></s:else>>DM </option>
										<option value="TM" <s:if test='documentManageInfo.channel=="TM"'>selected</s:if> <s:else></s:else>>TM</option>
										<option value="DGT" <s:if test='documentManageInfo.channel=="DGT"'>selected</s:if> <s:else></s:else>>DGT</option>
										<option value="BXS" <s:if test='documentManageInfo.channel=="BXS"'>selected</s:if> <s:else></s:else>>BXS</option>
										<option value="AGY-MAP" <s:if test='documentManageInfo.channel=="AGY-MAP"'>selected</s:if> <s:else></s:else>>AGY-MAP</option>
									</select>	
								</td>
								<td>
								
								</td>
								<td>
								
								</td>
								<td>
								报错
								</td>
								<td>
								<input type="checkbox" name="documentManageInfo.vdmiError"  <s:if test='documentManageInfo.vdmiError=="on"'>checked</s:if> />
								</td>
							</tr>
							<tr>
								<td></td>
								<td>
									
								</td>
								<td></td>
								<td>
									
								</td>
								<td></td>
								<td>
								</td>
								<td>
									<input type="button" class="tbl_query_button" value="查询" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" name="BtnView" id="BtnView"
										onclick="querylist()"
									/>
								</td>
								<td></td>
							</tr>
						</table>
					</div>
					<table id="tbl_tools" width="100%" border="0">
						<tr>
							<td align="center" width="5%">
								<a href="#" name="upLoad" id="upLoad" onclick="exelout()">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />
									导出
								</a>
							</td>
							<td align="center" width="5%">
						
							</td>
							<td align="center" width="5%">
							
							</td>
							<td align="center" width="5%">
						
							</td>
						
							<td align="center" width="5%">
							
							</td>
							<td align="center" width="5%">
							
							</td>
							<td align="center" width="5%">
							
							</td>
							<td align="center" width="35%"></td>
						</tr>
					</table>
					<div id="lessGridList3" style="overflow: auto; width: 100%;" >
						<table class="lessGrid" cellspacing="0" rules="all" border="0" cellpadding="0" style="border-collapse: collapse;width: 100%;">
							<tr class="lessGrid head">
								<th width="3%" style="text-align:center">
									<input id="CheckAll" style="width:13px;height:13px;" type="checkbox" onClick="checkAll(this,'ruleId')" />
								</th>
								<th style="text-align:center" width="5%">基准日</th>
								<th style="text-align:center" width="5%">投保单号</th>
								<th style="text-align:center" width="5%">保险单证号</th>
								<th style="text-align:center" width="5%">保单号</th>
								<th style="text-align:center" width="5%">投保单号渠道</th>
								<th style="text-align:center" width="5%">保险单证号渠道</th>
								<th style="text-align:center" width="5%">投保单号初始状态</th>
								<th style="text-align:center" width="5%">保险单证号初始状态</th>
								<th style="text-align:center" width="5%">报错</th>
								
							</tr>
							
							<s:iterator value="paginationList.recordList" id="iList" status="stuts">
								<tr id="<s:property value="num"/>">
									<td style="text-align:center">
										<input type="checkbox" style="width:13px;height:13px;" name="ruleId" value="<s:property value="num"/>" />
									</td>
									
									<td><s:property value='vdmiDate' /></td>
									<td><s:property value='appNum' /></td>
									<td><s:property value='formNum' /></td>
									<td><s:property value='policyNum' /></td>
									<td><s:property value='channel' /></td>
									<td><s:property value='tepStatus' /></td>
									<td><s:property value='status' /></td>
									<td><s:property value='tepchannel' /></td>
									<td><s:property value='vdmiError' /></td>
									
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