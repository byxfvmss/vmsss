<!-- file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.util.PaginationList"
	import="com.cjit.vms.input.model.InputVatInfo"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../page/include.jsp"%>
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
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/search.js"></script>
	<script type="text/javascript">
		// [查询]按钮
		function submit(){
			document.forms[0].submit();
		}
		// [新增]按钮
		function createInVat(){
			submitAction(document.forms[0], "createInputVat.action");
		}
		// [删除]按钮
		function deleteInVat(){
			submitAction(document.forms[0], "deleteInputVat.action");
		}
		// [导入]按钮
		function importData(webroot){
			var fileId = document.getElementById("fileId");
			if(fileId.value.length > 0){
				if(fileId.value.lastIndexOf(".XLS") > -1||fileId.value.lastIndexOf(".xls")>-1){
					document.forms[0].action = webroot+"/importInputVat.action";
					document.forms[0].submit();
					document.forms[0].action='listInputVat.action';
				}else{
					alert("文件格式不对，请上传Excel文件。");
				}
			}else{
				alert("请先选择要上传的文件。");
			}
		}
	</script>
	<style type="text/css">
		.style1{
			width: 242px;
		}
	</style>
</head>
<body onkeydow="enterkey(event)">
	<form name="Form1" method="post" action="listInputVat.action" id="Form1" enctype="multipart/form-data">
	<table id="tbl_main" cellpadding=0 cellspacing=0>
	<tr>
		<td align="left">
		<s:component template="rocketMessage" />
		<table id="tbl_current_status">
		<tr>
			<td>
				<img src="<%=bopTheme2%>/img/jes/icon/home.png" /> 
				<span class="current_status_menu">当前位置：</span> 
				<span class="current_status_submenu">查询
					<span class="actionIcon">-&gt;</span>进项税管理
				</span>
			</td>
		</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td align="left">
		<table id="tbl_query" cellpadding="0" cellspacing="0" width="100%" border="0">
		<tr>
			<td align="left">
				交易日期:
			</td>
			<td>
				<input class="tbl_query_time" id="valueBeginDate" type="text" name="valueBeginDate"
					value="<s:property value='valueBeginDate'/>"
					onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'valueEndDate\')}'})"
					size='11'"/>
				至
				<input class="tbl_query_time" id="valueEndDate" type="text" name="valueEndDate"
					value="<s:property value='valueEndDate'/>"
					onfocus="WdatePicker({minDate:'#F{$dp.$D(\'valueBeginDate\')}'})"
					size='11'"/>
			</td>
			<td align="left">
				供应商名称:
			</td>
			<td>
				<input type="text" name="suppName" value="<s:property value='suppName'/>" />
			</td>
			<!-- 
			<td align="left">
				供应商账号:
			</td>
			<td>
				<input type="text" name="suppAccount" value="<s:property value='suppAccount'/>" />
			</td> -->
			<td align="left">
				机构选择:
			</td>
			<td>
				<s:if test="authInstList != null && authInstList.size > 0">
				<s:select name="instCode" list="authInstList" listKey='id' listValue='name' onchange="submit()" headerKey="" headerValue="全部"/>
				</s:if>
				<s:if test="authInstList == null || authInstList.size == 0">
				<select name="instCode" class="readOnlyText">
				<option value="">请分配机构权限</option>
				</select>
				</s:if>
			</td>
			<td colspan="2"></td>
		</tr>
		<tr>
			<td align="left">
				记账日期:
			</td>
			<td>
				<input class="tbl_query_time" id="bookingBeginDate" type="text" name="bookingBeginDate"
					value="<s:property value='bookingBeginDate'/>"
					onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'bookingEndDate\')}'})"
					size='11'"/>
				至
				<input class="tbl_query_time" id="bookingEndDate" type="text" name="bookingEndDate"
					value="<s:property value='bookingEndDate'/>"
					onfocus="WdatePicker({minDate:'#F{$dp.$D(\'bookingBeginDate\')}'})"
					size='11'"/>
			</td>
			<td>
				发票种类:
			</td>
			<td>
				<s:select id="billType" name="billType" list="billTypeList" headerKey="" headerValue="全部"
					listKey='valueStandardLetter' listValue='name'/>
			</td>
			<td>
				记账科目:
			</td>
			<td>
				<input type="text" name="bookingCourse" value="<s:property value='bookingCourse'/>" />
			</td>
			<td style="width:80px;" align="right">
				<input type="button" class="tbl_query_button" value="查询"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="BtnView" id="BtnView" onclick="submit()" />
			</td>
		</tr>
		</table>
		<table id="tbl_tools" width="100%" border="0">
		<tr>
			<td align="left">
				<input type="button" class="tbl_query_button" value="新增"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="BtnView" id="BtnView" onclick="createInVat()" />
				<input type="button" class="tbl_query_button" value="删除"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="BtnView" id="BtnView" onclick="deleteInVat()" />
				<input type='file' name='theFile' id='fileId' size='25'/>
	        	<input type="button" class="tbl_query_button" value="导入"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 
					name="upLoad" id="upLoad" onclick="importData('<%=webapp%>')"/>
	        	<input type="button" class="tbl_query_button" value="导出"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 
					name="upLoad" id="upLoad" onclick="importData('<%=webapp%>')"/>
			</td>
		</tr>
		</table>
		</td>
	</tr>
	</table>
	<div style="overflow:auto;width:100%;" id="list1">
	<table id="lessGridList" width="100%" class="lessGrid" cellspacing="0" rules="all" border="0" cellpadding="0" display="none" style="border-collapse: collapse;">
	<tr class="lessGrid head">
		<th style="text-align:center">
			<input style="width:13px;height:13px;" id="CheckAll" type="checkbox" onclick="cbxselectall(this,'selectInVatIds')" />
		</th>
		<th style="text-align:center">编辑</th>
		<th style="text-align:center">发票代码</th>
		<th style="text-align:center">发票号码</th>
		<th style="text-align:center">交易日期</th>
		<th style="text-align:center">记账日期</th>
		<th style="text-align:center">金额</th>
		<th style="text-align:center">税额</th>
		<th style="text-align:center">发票种类</th>
		<th style="text-align:center">供应商名称</th>
		<th style="text-align:center">供应商税务登记号</th>
		<th style="text-align:center">记账科目</th>
		<th style="text-align:center">业务凭证编号</th>
		<th style="text-align:center">认证结果</th>
		<th style="text-align:center">认证日期</th>
		<th style="text-align:center">转出金额</th>
		<!--<th style="text-align:center">明细</th>-->
	</tr>
	<%
		PaginationList paginationList = (PaginationList)request.getAttribute("paginationList");
		if (paginationList != null){
			List inputVatInfoList = paginationList.getRecordList();
			if (inputVatInfoList != null && inputVatInfoList.size() > 0){
				for(int i=0; i<inputVatInfoList.size(); i++){
					InputVatInfo inputVat = (InputVatInfo)inputVatInfoList.get(i);
					if(i%2==0){
	%>
	<tr class="lessGrid rowA">
	<%
					}else{
	%>
	<tr class="lessGrid rowB">
	<%
					}
	%>
		<td align="center">
			<input style="width:13px;height:13px;" type="checkbox" name="selectInVatIds" value="<%=BeanUtils.getValue(inputVat,"inVatId")%>" />
		</td>
		<td align="center">
			<a href="editInputVat.action?curPage=<s:property value='paginationList.currentPage'/>&inVatId=<%=inputVat.getInVatId()%>&fromFlag=editList"><img src ="<%=bopTheme2%>/img/jes/icon/edit.png" title="编辑票据" style="border-width: 0px;" /></a>
		</td>
		<td align="left"><%=inputVat.getBillCode()%></td>
		<td align="left"><%=inputVat.getBillNo()%></td>
		<td align="center"><%=inputVat.getValueDate()%></td>
		<td align="center"><%=inputVat.getBookingDate()%></td>
		<td align="right"><%=NumberUtils.format(inputVat.getAmt(),"",2)%></td>
		<td align="right"><%=NumberUtils.format(inputVat.getTaxAmt(),"",2)%></td>
		<td align="left"><%=inputVat.getBillTypeView()%></td>
		<td align="left"><%=inputVat.getSuppName()%></td>
		<td align="left"><%=inputVat.getSuppTaxNo()%></td>
		<td align="left"><%=inputVat.getBookingCourse()%></td>
		<td align="left"><%=inputVat.getBussVouchersCode()%></td>
		<td align="left"><%=inputVat.getAuthenticationFlagView()%></td>
		<td align="center"><%=inputVat.getAuthenticationDate()%></td>
		<td align="right"><%=NumberUtils.format(inputVat.getOutAmt(),"",2)%></td>
		<!-- 
		<td align="center">
			<a onclick="goToPage('<%//=webapp%>/transDetail.action?transId=<%//=inputVat.getInVatId()%>');">
				<img src="<%//=bopTheme2%>/img/jes/icon/view.png" title="查看" style="border-width: 0px;"/>
			</a>
		</td> -->
	</tr>
	<%
				}
			}
		}
	%>
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
	<script type="text/javascript">
		var msgHight = document.getElementById("anpBoud").offsetHeight;
		document.getElementById("list1").style.height = screenHeight - 375 - msgHight;
	</script>
	<script language="javascript" type="text/javascript" charset="GBK">
	</script>
	</form>
</body>
</html>