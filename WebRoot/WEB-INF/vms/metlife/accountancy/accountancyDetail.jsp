<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<!--
	createTime:2016.2.
	author:沈磊
	content:销项会计分录 metlife
  -->

<html>
<head>
<title>增值税管理平台</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../../../page/include.jsp"%>

<link href="<%=bopTheme2%>/css/main.css" type="text/css"
	rel="stylesheet">
<link href="<%=webapp%>/page/common.css" type="text/css"
	rel="stylesheet">
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
<script type="text/javascript"
	src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<meta http-equiv="Pragma" content="no-cache" />
<script type="text/javascript">
	function querylist1(AccountingPeriod,a,b){
	alert(1);
	document.forms[0].action="accountingEntriesInfo.action?type=1&accountingPeriod="+AccountingPeriod+"&la5Plan="+a+"&la10Branch="+b;
	document.forms[0].submit();
	document.forms[0].action="accountingEntriesInfo.action";
	}
	
	function querylist(){
		document.forms[0].action="accountingEntriesInfo.action";
		document.forms[0].submit();
	}
	function create(){
		OpenModalWindowSubmit("openwindow.action",750,100,false);
// 		document.forms[0].action="createAccountingEntriesInfo.action";
// 		document.forms[0].submit();
	}
	function toExcel(){
		OpenModalWindowSubmit("openwindow1.action",750,100,true);
	}
	function OpenModalWindowSubmit(newURL, width, height, needReload) {
		var retData = false;
		if (typeof (width) == 'undefined') {
			width = screen.width * 0.9;
		}
		if (typeof (height) == 'undefined') {
			height = screen.height * 0.9;
		}
		if (typeof (needReload) == 'undefined') {
			needReload = false;
		}
		retData = showModalDialog(newURL, window, "dialogWidth:" + width
				+ "px;dialogHeight:" + height
				+ "px;center=1;scroll=0;help=0;status=0;");
		if (needReload && retData) {
			window.document.forms[0].submit();
		}
	}
	
</script>
</head>
<body>
	<form name="Form1" method="post"
		action="incomeAccountingEntries.action" id="Form1"
		enctype="multipart/form-data">
		<table id="tbl_main" cellpadding="0" cellspacing="0"
			class="tablewh100">
			<tr>
				<td class="centercondition">
				<s:component
						template="rocketMessage" />
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> <span
							class="current_status_menu">当前位置：</span> <span
							class="current_status_submenu1">VMS销项管理</span> <span
							class="current_status_submenu">会计分录</span> <span
							class="current_status_submenu">会计分录明细</span>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" width="80%"
							border="0">
							<tr>
								<td align="left">财务月</td>
								<td>
									<input  class="tbl_query_text"  type="text"
                                       name="accountingEntriesInfo.accountingPeriod"
                                       value="<s:property value='accountingEntriesInfo.accountingPeriod'/>" />
								</td>
								<td align="left">会计科目</td>
								<td><input type="text" class="tbl_query_text"
									name='accountingEntriesInfo.accountCode'
									value='<s:property value="accountingEntriesInfo.accountCode"/>' /></td>
								<td>T2</td>
								<td>
								<s:select name="accountingEntriesInfo.la2Channel"
										list="chanNelList" listKey="key" listValue='value'
										cssClass="tbl_query_text5" headerKey="" headerValue="请选择" />
								</td>
								<td align="left">T5</td>
								<td>
									<input type="text" class="tbl_query_text"
									name='accountingEntriesInfo.la5Plan'
									value='<s:property value="accountingEntriesInfo.la5Plan"/>' />
								</td>

							</tr>
							<tr>
								<td>T6</td>
								<td>
								<input type="text" class="tbl_query_text"
									name='accountingEntriesInfo.la6District'
									value='<s:property value="accountingEntriesInfo.la6District"/>' />
								</td>
								<td>T10</td>
								<td>
								<s:if
										test="authInstList != null && authInstList.size > 0">
										<s:select name="accountingEntriesInfo.la10Branch" list="authInstList"
											listKey="instId" listValue='instName'
											cssClass="tbl_query_text5" headerKey="" headerValue="全部" />
									</s:if> <s:if test="authInstList == null || authInstList.size == 0">
										<select name="accountingEntriesInfo.la10Branch" class="readOnlyText">
											<option value="">请分配机构权限</option>
										</select>
									</s:if>
								</td>
								<td>保单号</td>
								<td>
								<input type="text" class="tbl_query_text"
									name='accountingEntriesInfo.cherNum'
									value='<s:property value="accountingEntriesInfo.cherNum"/>' maxlength="20" />
								</td>
								<td>分录类别</td>
								<td>
								<s:select name="accountingEntriesInfo.vsadFlg"
										list="flglist" listKey="key" listValue='value'
										cssClass="tbl_query_text5" headerKey="" headerValue="请选择" />
								</td>
							</tr>
							<tr>
								<td></td>
								<td>
								
								</td>
								<td></td>
								<td></td>
								<td></td>
								<td>
								</td>
								<td></td>
								<td><input type="button" class="tbl_query_button"
									value="查询" onMouseMove="this.className='tbl_query_button_on'"
									onMouseOut="this.className='tbl_query_button'" name="BtnView"
									id="BtnView" onclick="querylist()" /></td>
							</tr>
						</table>
					</div>
					<table id="tbl_tools" width="100%" border="0">
						<tr>
							<td align="center" width="20%"><a href="#" name="upLoad"
								id="upLoad" onclick="create()"> <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />
									生成待备案产品分录 </a></td>
							<td align="center" width="20%"><a href="#" name="upLoad"
								id="upLoad" onclick="toExcel()"> <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />
									导出待备案产品分录 </a></td>
							<td align="center" width="5%"></td>
							<td align="center" width="5%"></td>
							<td align="center" width="5%"></td>
							
							<td align="center" width="35%"></td>
						</tr>
					</table>
						<div style="width: 100%;">
					<div id="lessGridList3" style="overflow: auto; width: 100%;">
							<table class="lessGrid" cellspacing="0" rules="all" border="0"
								cellpadding="0" style="border-collapse: collapse;width: 100%;">
								<tr class="lessGrid head">
									<th width="3%" style="text-align:center"><input
										id="CheckAll" style="width:13px;height:13px;" type="checkbox"
										onClick="checkAll(this,'ruleId')" /></th>
									<th style="text-align:center">保单号</th>
									<th style="text-align:center">AccountCode</th>
									<th style="text-align:center">AccountingPeriod</th>
									<th style="text-align:center">T1</th>
									<th style="text-align:center">T2</th>
									<th style="text-align:center">T3</th>
									<th style="text-align:center">T5</th>
									<th style="text-align:center">T6</th>
									<th style="text-align:center">T7</th>
									<th style="text-align:center">T10</th>
									<th style="text-align:center">BaseAmount</th>
									<th style="text-align:center">CurrencyCode</th>
									<th style="text-align:center">DebitCredit</th>
									<th style="text-align:center">Description</th>
									<th style="text-align:center">JournalSource</th>
									<th style="text-align:center">TransactionAmount</th>
									<th style="text-align:center">TransactionDate</th>
									<th style="text-align:center">TransactionReference</th>
								</tr>
								<s:iterator value="paginationList.recordList" id="iList"
									status="stuts">
									<tr id="<s:property value="num"/>">
										<td style="text-align:center"><input type="checkbox"
											style="width:13px;height:13px;" name="ruleId"
											value="<s:property value="vcvId"/>" />
										</td>
										<td><s:property value='cherNum' />
										</td>
										<td><s:property value='accountCode' />
										</td>
										<td><s:property value='accountingPeriod' />
										</td>
										<td><s:property value='la1Fund' />
										</td>
										<td><s:property value='la2Channelch' />
										</td>
										<td><s:property value='la3Category' />
										</td> 
										<td><s:property value='la5Plan' />
										</td>
										<td><s:property value='la6District' />
										</td>
										<td><s:property value='la7Unit' />
										</td>
										<td><s:property value='la10Branch' />
										</td>
										<td><s:property value='baseAmount' />
										</td>
										<td><s:property value='currency' />
										</td>
										<td><s:property value='dc' />
										</td>
										<td><s:property value='transactiondescription' />
										</td>
										<td><s:property value='journalSource' />
										</td>
										<td><s:property value='transactionAmount' />
										</td>
										<td><s:property value='transactionDate' />
										</td>
										<td><s:property value='transactionReference' />
										</td> 
									</tr>
								</s:iterator>
							</table>
						</div>
					</div>
					<div id="anpBoud" align="Right" style="overflow:auto;width:100%;">
						<table width="100%" cellspacing="0" border="0">
							<tr>
								<td align="right"><s:component template="pagediv" /></td>
							</tr>
						</table>
					</div>
				</td>	
			</tr>
		</table>
	</form>
</body>
</html>