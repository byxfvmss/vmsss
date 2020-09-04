<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<!--
	createTime:2016.2.
	author:沈磊
	content:进项会计分录 metlife
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
	function querylist(){
		document.forms[0].action="incomeAccountingEntries.action";
		document.forms[0].submit();
	}
	function upload(){
	var fileId = document.getElementById("fileId");
		if (fileId.value.length > 0) {
			if (fileId.value.lastIndexOf(".XLS") > -1
					|| fileId.value.lastIndexOf(".xls") > -1) {
				document.forms[0].action="importExcelIncomeAccountingEntries.action";
				document.forms[0].submit();
				//document.forms[0].action = 'listInputVat.action';
			} else {
				alert("文件格式不对，请上传Excel文件。");
			}
		} else {
			alert("请先选择要上传的文件。");
		}
		
	}
	function toReport(){
	
		OpenModalWindow("openwindow2.action",750,100,true);
	
// 		document.forms[0].action="accountingToReport.action";
// 		document.forms[0].submit();
// 		document.forms[0].action="incomeAccountingEntries.action";
	}
	function toSun(){
		document.forms[0].action="accountingToSun.action";
		document.forms[0].submit();
		document.forms[0].action="incomeAccountingEntries.action";
	}
	function sunToExecel(){
		document.forms[0].action="accountingSunToExecel.action";
		document.forms[0].submit();
		document.forms[0].action="incomeAccountingEntries.action";
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
				<s:component template="rocketMessage" />
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> <span
							class="current_status_menu">当前位置：</span> <span
							class="current_status_submenu1">VMS进项管理</span> <span
							class="current_status_submenu">会计分录</span> <span
							class="current_status_submenu">会计分录明细</span>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" width="80%"
							border="0">
							<tr>
								<td align="left">会计科目</td>
								<td><input type="text" class="tbl_query_text"
									name='accountingEntriesInfo.accountCode'
									value='<s:property value="accountingEntriesInfo.accountCode"/>' /></td>
								<td>分录标识</td>
								<td><input type="text" class="tbl_query_text"
									name='accountingEntriesInfo'
									value='<s:property value=""/>' />
								</td>
								<td align="left">T1</td>
								<td>
									<input type="text" class="tbl_query_text"
									name='accountingEntriesInfo.la1Fund'
									value='<s:property value="accountingEntriesInfo.la1Fund"/>' />
								</td>
								<td align="left">T10</td>
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
							</tr>
							<tr>
								<td align="left">T2</td>
								<td>
									<s:select name="accountingEntriesInfo.la2Channel"
										list="chanNelList" listKey="key" listValue='value'
										cssClass="tbl_query_text5" headerKey="" headerValue="请选择" />
								</td>
								<td align="left">T3</td>
								<td>
									<input type="text" class="tbl_query_text"
									name='accountingEntriesInfo.la3Category'
									value='<s:property value="accountingEntriesInfo.la3Category"/>' />
								</td>
								<td align="left">T5</td>
								<td>
									<input type="text" class="tbl_query_text"
									name='accountingEntriesInfo.la5Plan'
									value='<s:property value="accountingEntriesInfo.la5Plan"/>' />
								</td>
								<td align="left">T6</td>
								<td>
									<input type="text" class="tbl_query_text"
									name='accountingEntriesInfo.la6District'
									value='<s:property value="accountingEntriesInfo.la6District"/>' />
								</td>
							</tr>
							<tr>
								<td align="left">T7</td>
								<td>
									<input type="text" class="tbl_query_text"
									name='accountingEntriesInfo.la7Unit'
									value='<s:property value="accountingEntriesInfo.la7Unit"/>' />
								</td>
								<td></td>
								<td>
									
								</td>
								<td></td>
								<td>
								</td>
								<td></td>
								<td style="text-align:center"><input type="button" class="tbl_query_button"
									value="查询" onMouseMove="this.className='tbl_query_button_on'"
									onMouseOut="this.className='tbl_query_button'" name="BtnView"
									id="BtnView" onclick="querylist()" /></td>
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
								<a href="#" onclick="sunToExecel();" name="cmdFilter" id="cmdFilter">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1007.png" />
									生成sun导入文件
								</a>
							</td>
							<td align="center" >
							<a href="#" onclick="toReport()" name="cmdFilter" id="cmdFilter">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1007.png" />
									预算报表生成
								</a>
							</td>
							<td align="center" ></td>
							<td align="center" ></td>
							<td align="center" ></td>
							<td align="center" width="35%"></td>
						</tr>
					</table>
					<div id="lessGridList3" style="width: 100%;">
						<table class="lessGrid" cellspacing="0" rules="all" border="0" cellpadding="0" style="border-collapse: collapse; width: 100%;">
							<tr class="lessGrid head">
									<th width="3%" style="text-align:center"><input
										id="CheckAll" style="width:13px;height:13px;" type="checkbox"
										onClick="checkAll(this,'ruleId')" /></th>
									<th style="text-align:center">基准日</th>
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
<!-- 									<th style="text-align:center">标识</th> -->
<!-- 									<th style="text-align:center">匹配状态</th> -->
<!-- 									<th style="text-align:center">抵扣额</th> -->
								</tr>
								<s:iterator value="paginationList.recordList" id="iList"
									status="stuts">
									<tr id="<s:property value="num"/>">
										<td style="text-align:center"><input type="checkbox"
											style="width:13px;height:13px;" name="ruleId"
											value="<s:property value="vcvId"/>" />
										</td>
										<td><s:property value='vidiDate' />
										</td>
										<td><s:property value='accountCode' />
										</td>
										<td><s:property value='accountingPeriod' />
										</td>
										<td><s:property value='la1Fu nd' />
										</td>
										<td><s:property value='la2Channel' />
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