<!-- file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.util.PaginationList"
	import="com.cjit.vms.input.model.InputVatInfo"
	import="com.cjit.vms.input.model.InputInvoice"
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
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/window.js"></script>
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
					document.forms[0].action = webroot+"/importInputInvoice.action";
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
	<form name="Form1" method="post" action="listInputInvoice.action" id="Form1" enctype="multipart/form-data">
	<table id="tbl_main" cellpadding=0 cellspacing=0>
	<tr>
		<td align="left">
		<s:component template="rocketMessage" />
		<table id="tbl_current_status">
		<tr>
			<td>
				<img src="<%=bopTheme2%>/img/jes/icon/home.png" /> 
				<span class="current_status_menu">当前位置：</span> 
				<span class="current_status_submenu">进项税管理
					<span class="actionIcon">-&gt;</span>数据处理
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
				开票日期:
			</td>
			<td>
				<input class="tbl_query_time" id="billBeginDate" type="text" name="billBeginDate"
					value="<s:property value='billBeginDate'/>"
					onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'billEndDate\')}'})"
					size='11'"/>
				至
				<input class="tbl_query_time" id="billEndDate" type="text" name="billEndDate"
					value="<s:property value='billEndDate'/>"
					onfocus="WdatePicker({minDate:'#F{$dp.$D(\'billBeginDate\')}'})"
					size='11'"/>
			</td>
			<td>
				发票号码:
			</td>
			<td>
				<input type="text" name="billNo" value="<s:property value='billNo'/>" />
			</td>
			<td>
				发票代码:
			</td>
			<td>
				<input type="text" name="billCode" value="<s:property value='billCode'/>" />
			</td>
			
			<td>
				购方纳税人识别号:
			</td>
			<td>
				<input type="text" name="taxNo" value="<s:property value='taxNo'/>" />
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
			<!--
				<input type="button" class="tbl_query_button" value="新增"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="BtnView" id="BtnView" />
				<input type="button" class="tbl_query_button" value="删除"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="BtnView" id="BtnView" />  -->
				<input type='file' name='theFile' id='fileId' size='25'/>
	        	<input type="button" class="tbl_query_button" value="导入"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 
					name="upLoad" id="upLoad" onclick="importData('<%=webapp%>')"/>
	        	<!--<input type="button" class="tbl_query_button" value="导出"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 
					name="upLoad" id="upLoad" /> -->
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
			<input style="width:13px;height:13px;" id="CheckAll" type="checkbox" onclick="cbxselectall(this,'selectInputInvoiceIds')" />
		</th>
		<th style="text-align:center">编辑</th>
		<th style="text-align:center">发票代码</th>
		<th style="text-align:center">发票号码</th>
		<th style="text-align:center">开票日期</th>
		<th style="text-align:center">合计金额</th>
		<th style="text-align:center">合计税额</th>
		<th style="text-align:center">价税合计</th>
		<th style="text-align:center">购方纳税人名称</th>
		<th style="text-align:center">购方纳税人识别号</th>
		<th style="text-align:center">转出比例</th>
		<th style="text-align:center">转出金额</th>
		<th style="text-align:center">是否勾稽</th>
		<th style="text-align:center">发票类型</th>
		<th style="text-align:center">认证通过日期</th>
		<th style="text-align:center">抵扣剩余天数</th>
		<th style="text-align:center">状态</th>
		<th style="text-align:center">明细</th>
	</tr>
	<%
		PaginationList paginationList = (PaginationList)request.getAttribute("paginationList");
		if (paginationList != null){
			List inputInvoiceList = (List)paginationList.getRecordList();
			if (inputInvoiceList != null && inputInvoiceList.size() > 0){
				for(int i=0; i<inputInvoiceList.size(); i++){
					InputInvoice inputInvoice = (InputInvoice)inputInvoiceList.get(i);
					if(i%2==0&&inputInvoice.getDeductedAlartFlag().equals("Y")){
	%>
	<tr class="lessGrid rowA" style="background:yellow">
	<%
					}else if(i%2==0&&inputInvoice.getDeductedAlartFlag().equals("N")){
	%>
	<tr class="lessGrid rowA">
	<%
					}else if(i%2!=0&&inputInvoice.getDeductedAlartFlag().equals("Y")){
	%>
	<tr class="lessGrid rowB"  style="background:yellow">
	<%
					}else{
	%>
	<tr class="lessGrid rowB">
	<%
					}
	%>
		<td align="center">
			<input style="width:13px;height:13px;" type="checkbox" name="selectInputInvoiceIds" value="<%=BeanUtils.getValue(inputInvoice,"billId")%>" />
		</td>
		<td align="center">
			<a href="editInputInvoice.action?curPage=<s:property 
			value='paginationList.currentPage'/>&billId=<%=inputInvoice.getBillId()%>&fromFlag=editList">
			<img src ="<%=bopTheme2%>/img/jes/icon/edit.png" title="编辑票据" 
			style="border-width: 0px;" /></a>
		</td>
		<td align="center"><%=(inputInvoice.getBillCode()==null)?"":inputInvoice.getBillCode()%></td>
		<td align="center"><%=(inputInvoice.getBillNo()==null)?"":inputInvoice.getBillNo()%></td>
		<td align="center"><%=(inputInvoice.getBillDate()==null)?"":inputInvoice.getBillDate()%></td>
		<td align="center"><%=(inputInvoice.getAmtSum()==null)?"":inputInvoice.getAmtSum()%></td>
		<td align="center"><%=NumberUtils.format(inputInvoice.getTaxAmtSum(),"",2)%></td>
		<td align="center"><%=NumberUtils.format(inputInvoice.getSumAmt(),"",2)%></td>
		<td align="center"><%=(inputInvoice.getName()==null)?"":inputInvoice.getName()%></td>
		<td align="center"><%=(inputInvoice.getTaxNo()==null)?"":inputInvoice.getTaxNo()%></td>
		<td align="center"><%=(inputInvoice.getVatOutProportion()==null)?"":inputInvoice.getVatOutProportion()%></td>
		<td align="center"><%=(inputInvoice.getVatOutAmt()==null)?"":inputInvoice.getVatOutAmt()%></td>
		<td align="center"><%=inputInvoice.getConformFlg().equals("1")?"勾稽":"不勾稽"%></td>
		<td align="center"><%=inputInvoice.getFaPiaoType()%></td>
		<td align="center"><%=inputInvoice.getIdentifyDate()%></td>
		<td align="center"><%=DataUtil.getDeductedDaysFlag(inputInvoice.getDeductedDays())%></td>
		<td align="center"><%=inputInvoice.getDataStatus().equals("1")?"已扫描未认证":""%></td>
		<td align="center">
				<a href="javascript:void(0)" 
				onClick="OpenModalWindow('<%=webapp%>/inputInvoiceDetail.action?billId=<%=inputInvoice.getBillId()%>',650,400,'view') ">
			    <img src="<%=bopTheme%>/theme/default//img/jes/icon/view.png" title="查看" style="border-width: 0px;"/></a>
		</td>
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