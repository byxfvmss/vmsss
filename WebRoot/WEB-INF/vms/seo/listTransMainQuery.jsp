<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<title>增值税管理平台</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%=bopTheme2%>/css/main.css" type="text/css"
	rel="stylesheet">
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/calendar_notime.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/editview.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/XmlHttp.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/search.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/ylb.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/search.js"></script>
<script type="text/javascript"
	src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script language="javascript" type="text/javascript"
	src="<c:out value='${webapp}'/>/page/js/window.js" charset="UTF-8"></script>
</head>
<body>
	<form name="Form1" action="businessQuery.action" method="post" id="Form1">
		<table id="tbl_main" cellpadding="0" cellspacing="0"
			class="tablewh100">
			<tr>
				<td class="centercondition">
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> <span
							class="current_status_menu">当前位置：</span> <span
							class="current_status_submenu1">销项税管理</span> <span
							class="current_status_submenu">综合查询</span> <span
							class="current_status_submenu">交易查询</span>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td width="50">交易时间</td>
								<td width="280"><input id="startTime" name="startTime"
									type="text" value="<s:property value='startTime' />"
									class="tbl_query_time1"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endTime\')||\'new Date()\'}'})" />
									&nbsp;&nbsp;&nbsp;-&nbsp;&nbsp;&nbsp; <input id="endTime"
									name="endTime" type="text"
									value="<s:property value='endTime' />" class="tbl_query_time1"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startTime\')}',maxDate:new Date()})" />
								</td>
								<td width="25">客户名称</td>
								<td width="130"><input type="hidden" id="inst_id"
									name="instId" value='<s:property value="instId"/>'> <input
									type="text" class="tbl_query_text" id="inst_Name"
									name="instName" value='<s:property value="instName"/>'
									onclick="setOrg(this);" readonly="readonly"></td>
								<td width="25">纳税人类型</td>
								<td width="130" width="50"><select id="status"
									name="status">
										<option value="" <s:if test='status==""'>selected</s:if>
											<s:else></s:else>>全部</option>
										<option value="0" <s:if test='status=="0"'>selected</s:if>
											<s:else></s:else>>未校验</option>
										<option value="1" <s:if test='status=="1"'>selected</s:if>
											<s:else></s:else>>未通过校验</option>
										<option value="2" <s:if test='status=="2"'>selected</s:if>
											<s:else></s:else>>通过校验</option>
										<option value="2" <s:if test='status=="2"'>selected</s:if>
											<s:else></s:else>>通过审核</option>
								</select></td>
								<td width="25">发票类型</td>
								<td width="130" width="50"><select id="status"
									name="status">
										<option value="" <s:if test='status==""'>selected</s:if>
											<s:else></s:else>>全部</option>
										<option value="0" <s:if test='status=="0"'>selected</s:if>
											<s:else></s:else>>未校验</option>
										<option value="1" <s:if test='status=="1"'>selected</s:if>
											<s:else></s:else>>未通过校验</option>
										<option value="2" <s:if test='status=="2"'>selected</s:if>
											<s:else></s:else>>通过校验</option>
										<option value="2" <s:if test='status=="2"'>selected</s:if>
											<s:else></s:else>>通过审核</option>
								</select></td>
							</tr>
							<tr>
								<td width="50">交易金额</td>
								<td width="280"><input id="startTime" name="startTime"
									type="text" value="<s:property value='startTime' />"
									class="tbl_query_time1"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endTime\')||\'new Date()\'}'})" />
									&nbsp;&nbsp;&nbsp;-&nbsp;&nbsp;&nbsp; <input id="endTime"
									name="endTime" type="text"
									value="<s:property value='endTime' />" class="tbl_query_time1"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startTime\')}',maxDate:new Date()})" />
								</td>
								<td width="50">未开票金额</td>
								<td width="280"><input id="startTime" name="startTime"
									type="text" value="<s:property value='startTime' />"
									class="tbl_query_time1"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endTime\')||\'new Date()\'}'})" />
									&nbsp;&nbsp;&nbsp;-&nbsp;&nbsp;&nbsp; <input id="endTime"
									name="endTime" type="text"
									value="<s:property value='endTime' />" class="tbl_query_time1"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startTime\')}',maxDate:new Date()})" />
								</td>
								<td width="25">交易类型</td>
								<td width="130" width="50"><select id="status"
									name="status">
										<option value="" <s:if test='status==""'>selected</s:if>
											<s:else></s:else>>全部</option>
										<option value="0" <s:if test='status=="0"'>selected</s:if>
											<s:else></s:else>>未校验</option>
										<option value="1" <s:if test='status=="1"'>selected</s:if>
											<s:else></s:else>>未通过校验</option>
										<option value="2" <s:if test='status=="2"'>selected</s:if>
											<s:else></s:else>>通过校验</option>
										<option value="2" <s:if test='status=="2"'>selected</s:if>
											<s:else></s:else>>通过审核</option>
								</select></td>
							</tr>
							<tr>
								<td width="25">交易标志</td>
								<td width="130" width="50"><select id="status"
									name="status">
										<option value="" <s:if test='status==""'>selected</s:if>
											<s:else></s:else>>全部</option>
										<option value="0" <s:if test='status=="0"'>selected</s:if>
											<s:else></s:else>>未校验</option>
										<option value="1" <s:if test='status=="1"'>selected</s:if>
											<s:else></s:else>>未通过校验</option>
										<option value="2" <s:if test='status=="2"'>selected</s:if>
											<s:else></s:else>>通过校验</option>
										<option value="2" <s:if test='status=="2"'>selected</s:if>
											<s:else></s:else>>通过审核</option>
								</select></td>
								<td width="25">状态</td>
								<td width="130" width="50"><select id="status"
									name="status">
										<option value="" <s:if test='status==""'>selected</s:if>
											<s:else></s:else>>全部</option>
										<option value="0" <s:if test='status=="0"'>selected</s:if>
											<s:else></s:else>>未校验</option>
										<option value="1" <s:if test='status=="1"'>selected</s:if>
											<s:else></s:else>>未通过校验</option>
										<option value="2" <s:if test='status=="2"'>selected</s:if>
											<s:else></s:else>>通过校验</option>
										<option value="2" <s:if test='status=="2"'>selected</s:if>
											<s:else></s:else>>通过审核</option>
								</select>
								</td>
								<td style="text-align:left"><input type="button"
									class="tbl_query_button"
									onMouseMove="this.className='tbl_query_button_on'"
									onMouseOut="this.className='tbl_query_button'"
									onclick="submitForm('listImpdata.action');" name="cmdFilter"
									value="查询" id="cmdFilter" /></td>
							</tr>
						</table>
					</div>
					<table id="tbl_tools" cellpadding="1" cellspacing="0">
						<tr align="left">
							<td><a href="#" name="btSubmit" id="btSubmit"
								onclick="saveInfo()"> <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1006.png" />
									导出</a>
							</td>
						</tr>
					</table>
					<div id="lessGridList1" style="overflow: auto; width: 100%;">
						<table class="lessGrid" cellspacing="0" rules="all" border="0"
							cellpadding="0" style="border-collapse: collapse;width: 100%;">
							<tr class="lessGrid head">
								<th width="3%" style="text-align:center"><input
									id="CheckAll" style="width:13px;height:13px;" type="checkbox"
									onClick="checkAll(this,'batchIdList')" /></th>
								<th style="text-align: center">序号</th>
								<th style="text-align: center">交易时间</th>
								<th style="text-align: center">客户名称</th>
								<th style="text-align: center">纳税人类型</th>
								<th style="text-align: center">交易类型</th>
								<th style="text-align: center">交易金额</th>
								<th style="text-align: center">是否含税</th>
								<th style="text-align: center">税率</th>
								<th style="text-align: center">税额</th>
								<th style="text-align: center">收入</th>
								<th style="text-align: center">价税合计</th>
								<th style="text-align: center">未开票金额</th>
								<th style="text-align: center">发票类型</th>
								<th style="text-align: center">是否打票</th>
								<th style="text-align: center">交易标志</th>
								<th style="text-align: center">状态</th>
								<th style="text-align: center">明细</th>
							</tr>
							<s:iterator value="paginationList.recordList" id="iList"
								status="stuts">
								<tr align="center"
									class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
									<td>
										<!-- |<s:property value="status"/> --> <input type="checkbox"
										style="width:13px;height:13px;" name="batchIdList"
										value="<s:property value="batchId"/>"
									</td>
									<td align="center"><s:property value='#stuts.count' /></td>
									<td><s:property value='impTime' /></td>
									<td><s:property value='impInst' /></td>
									<td><s:property value='impUser' /></td>
									<td><s:property value='count' /></td>
									<td><s:property value='passCount' /></td>
									<td><s:property value='unPassCount' /></td>
									<td id="<s:property value="batchId"/>"><s:if
											test='status=="0"'>未校验</s:if> <s:if test='status=="1"'>未通过校验</s:if>
										<s:if test='status=="2"'>通过校验</s:if> <s:if test='status=="3"'>通过审核</s:if>
									</td>
									<td align="center"
										class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
										<s:if test='status=="0"'>
											<a
												href="showDataDetailStr.action?impBatchId=<s:property value="batchId"/>">
												<img
												src="<c:out value="${bopTheme}"/>/themes/images/icons/folder-open.png"
												title="查看" style="border-width: 0px;" /> </a>
										</s:if></td>
								</tr>
							</s:iterator>
						</table>
					</div>
					<div id="anpBoud" align="Right" style="width:100%;">
						<table width="100%" cellspacing="0" border="0">
							<tr>
								<td align="right"><s:component template="pagediv" /></td>
							</tr>
						</table>
					</div></td>
			</tr>
		</table>
	</form>
</body>
</html>