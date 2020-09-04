<!-- file: <%=request.getRequestURI()%> -->
<%@ page language="java" import="java.util.*" import="java.math.BigDecimal" import="com.cjit.common.constant.ScopeConstants" import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.util.PaginationList" import="com.cjit.vms.input.model.InputInvoiceNew" import="com.cjit.vms.trans.util.DataUtil" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%>

<%@page import="com.opensymphony.util.BeanUtils"%>
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
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript" src="<c:out value='${webapp}'/>/page/js/window.js" charset="UTF-8"></script>
<script src="<%=bopTheme%>/js/MessageBox/js/messageBox.js" type="text/javascript"></script>
<link href="<%=bopTheme%>/js/MessageBox/css/messageBox.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=bopTheme%>/js/validator.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/search.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/editview.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>

<script type="text/javascript">
	// [查询]按钮
	function submit() {
		document.forms[0].submit();
	}
	//[导出]按钮
	function exportInputTrans() {
		submitAction(document.forms[0], "exportInputInvoiceNew.action");
		document.forms[0].action="listInputInvoiceNew.action";
	}
	// [新增]按钮
	function createInVat() {
		submitAction(document.forms[0], "createInputVat.action");
		document.forms[0].action="listInputInvoiceNew.action";
	}
	// [删除]按钮
	function deleteInputTrans() {
		var ids = document.getElementsByName("selectInputTransIds");
		var selectedIds = null;
		if (ids.length > 0) {
			for (var i = 0; i < ids.length; i++) {
				var id = ids[i];
				var j = 0;
				if (id.checked) {
					if (selectedIds != null) {
						selectedIds = selectedIds + "," + id.value;
					} else {
						selectedIds = id.value;
					}
					j++;
				}
			}
		}
		if (selectedIds == null) {
			alert("请选择要删除的记录");
		}
		submitAction(document.forms[0], "deleteInputTrans.action?selectedIds="
				+ selectedIds);
				document.forms[0].action="listInputInvoiceNew.action";
	}

	//删除-加
	function deleteInputTransNew() {
		var ids = document.getElementsByName("selectInputTransIds");
		var selectedIds = null;
		if (ids.length > 0) {
			for (var i = 0; i < ids.length; i++) {
				var id = ids[i];
				var j = 0;
				if (id.checked) {
					if (selectedIds != null) {
						selectedIds = selectedIds + "," + id.value;
					} else {
						selectedIds = id.value;
					}
					j++;
				}
			}
		}
		if (selectedIds == null) {
			alert("请选择要删除的记录");
		}else
		if(confirm("确认要删除该记录吗？")){
		submitAction(document.forms[0], "deleteInputTransNew.action?selectedIds="
				+ selectedIds);
		}
				document.forms[0].action="listInputInvoiceNew.action";
	}
	
	// [导入]按钮
	function importData(webroot) {
		var fileId = document.getElementById("fileId");
		if (fileId.value.length > 0) {
			if (fileId.value.lastIndexOf(".XLS") > -1
					|| fileId.value.lastIndexOf(".xls") > -1) {
				document.forms[0].action = webroot + "/importInputInvoiceNew.action";
				document.forms[0].submit();
				//document.forms[0].action = 'listInputVat.action';
				document.forms[0].action="listInputInvoiceNew.action";
			} else {
				alert("文件格式不对，请上传Excel文件。");
			}
		} else {
			alert("请先选择要上传的文件。");
		}
	}

	function addInputTrans(){

		OpenModalWindow('<%=webapp%>/inputTransDetail.action%>',650,400,'view') ;
        
        
	}

	$(function() {
		$("#cmdImportBt").click(
				function() {
					if (fucCheckNull(document.getElementById("theFile"),
							"导入文件不能为空") == false) {
						return false;
					}
					submitForm('importInputInvoiceNew.action');
					
				});
	});

	function submitForm(actionUrl) {
		var form = document.getElementById("Form1");
		form.action = actionUrl;
		form.submit();
			}
</script>
</head>
<body onkeydow="enterkey(event)">
	<form name="Form1" method="post" action="listInputInvoiceNew.action" id="Form1" enctype="multipart/form-data">
		<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
			<%
				String currMonth = (String) request.getAttribute("currMonth");
			%>
			<input type="hidden" name="currMonth" id="currMonth" value="<%=currMonth%>" />
			<tr>
				<td class="centercondition">
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" />
						<span class="current_status_menu">当前位置：</span>
						<span class="current_status_submenu1">进项税管理</span>
						<span class="current_status_submenu">认证管理</span>
						<span class="current_status_submenu">发票管理</span>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" border="0" width="100%">
						 <tr>
                            <td align="left">开票日期</td>
                            <td width="280"><input class="tbl_query_time" id="deducBeginDate" 
                            type="text" name="inputInvoiceNew.deducBeginDate" value="<s:property value='inputInvoiceNew.deducBeginDate'/>"
										onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'deducBeginDate\')}'})" size='11'
									"/>
							-
							<input class="tbl_query_time" id="deducEndDate" type="text" name="inputInvoiceNew.deducEndDate" value="<s:property value='inputInvoiceNew.deducEndDate'/>" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'deducEndDate\')}'})"
										size='11'
									"/>
                            </td>
						    <td align="left">发票代码</td>
                            <td><input type="text" class="tbl_query_text"
                                       name="inputInvoiceNew.billCode"
                                        maxlength="10"
                                       onkeypress="checkkey(value);"/></td>
                            <td align="left">发票号码</td>
                            <td><input type="text" class="tbl_query_text"
                                       name="inputInvoiceNew.billNo"
                                        maxlength="8"
                                       onkeypress="checkkey(value);"/></td>
                        </tr>
						<tr>
                            <!-- <td align="left">机构名称</td>
							<td width="280">
									<input type="hidden" id="inst_id" name="inputInvoiceNew.instId" value='<s:property value="inputInvoiceNew.instId"/>'>
									<input type="text" class="tbl_query_text" id="inst_Name" name="inputInvoiceNew.instName" value='<s:property value="inputInvoiceNew.instName"/>' onclick="setOrg(this);" readonly="readonly">
						    </td> -->
						    
						    <td align="left">机构名称</td>
                            <td><s:select id="industryType"
                                          name="inputInvoiceNew.industryType" list="industryTypeList"
                                          headerKey="" headerValue="全部" listKey='code' listValue='name'
                                          cssClass="tbl_query_text"/>
                            </td>
                           
                            <td >
									<input type="button" class="tbl_query_button" value="查询" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" name="BtnView" id="BtnView"
										onclick="submit()"
									/>
								</td>
						</tr>
						</table>
					</div>
					<table id="tbl_tools" width="100%" border="0">
						<tr>
						<td align="left" width="120">
							<!-- <a href="#" onClick="OpenModalWindow('<%=webapp%>/InputInvoiceNewAdd.action',800,600,'view') " >
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1044.png" />
									新增
								</a> -->
								<a href="InputInvoiceNewAdd.action?curPage=<s:property 
					value='paginationList.currentPage'/>&fromFlag=addList  ">
										<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1044.png" title="新增" style="border-width: 0px;" />
									新增</a>
							</td>
							<td align="left" width="200">
								<!--
				<input type="button" class="tbl_query_button" value="新增"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="BtnView" id="BtnView"  /> -->
					
								<a href="#" onclick="deleteInputTransNew()">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1019.png" />
									删除
								</a>
							</td>
							<td width="234">
								<input type='file' name='theFile' id='theFile' size='25' style="height:26px;" />
							</td>
							<td >
								<a href="<%=webapp%>/template/INPUT_INVOICENEW_IMP.xls">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/upload.png" />
									下载模板
								</a>
							</td>
							<td>
								<a href="javascript:;" id="cmdImportBt" name="cmdImportBt">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1007.png" />
									导入
								</a>
								<a href="#" onclick="exportInputTrans()">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />
									导出
								</a>
							</td>
						</tr>
					</table>
					<div id="lessGridList4" style="overflow: auto; width: 100%;">
						<table class="lessGrid" cellspacing="0" rules="all" border="0" cellpadding="0" style="border-collapse: collapse; width: 100%;">
							<tr class="lessGrid head">
								<th style="text-align:center">
									<input style="width:13px;height:13px;" id="CheckAll" type="checkbox" onclick="cbxselectall(this,'selectInputTransIds')" />
								</th>
								<th style="text-align:center">机构代码</th>
								<th style="text-align:center">机构名称</th>
								<th style="text-align:center">发票代码</th>
								<th style="text-align:center">发票号码</th>
								<th style="text-align:center">供应商名称</th>
								<th style="text-align:center">供应商纳税人识别号</th>
								<th style="text-align:center">金额</th>
								<th style="text-align:center">税额</th>
								<th style="text-align:center">开票日期</th>
								<th style="text-align:center">票据状态</th>
								<th style="text-align:center" colspan="2">操作</th>
							</tr>
							<s:iterator value="paginationList.recordList" id="iList" status="stuts">
							
							<tr id="<s:property value="#iList.billCode"/>"  align="center" class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
								<td align="center">
									<input style="width:13px;height:13px;" type="checkbox" name="selectInputTransIds" value='<s:property value="#iList.billNo"/><s:property value="#iList.billCode"/>' />
								</td>
								<td align="left"><s:property value='#iList.instId' /></td>
								<td align="left"><s:property value='#iList.instName' /></td>
								<td align="left"><s:property value='#iList.billCode' /></td>
								<td align="left"><s:property value='#iList.billNo' /></td>
								<td align="center"><s:property value='#iList.vendorName' /></td>
								<td align="center"><s:property value='#iList.taxNo' /></td>
								<td align="center"><s:property value='#iList.amt' /></td>
								<td align="center"><s:property value='#iList.taxAmt' /></td>
								<td align="left"><s:property value='#iList.billDate' /></td>
								<td align="left"><s:property value='#iList.dataStatus' /></td>
								
								<td align="center">
									<a href="editInputTransNew.action?curPage=<s:property 
					value='paginationList.currentPage'/>&id='<s:property value="#iList.billNo"/>'&fromFlag=editList &billNoAndCode =<s:property value="#iList.billNo"/><s:property value="#iList.billCode"/> ">
										<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1005.png" title="编辑" style="border-width: 0px;" />
									</a>
								</td>
								<td align="center">
									<a href="#" onclick = "OpenModalWindow('<%=webapp%>/InputInvoiceNewList.action?billNoAndCode=<s:property value="#iList.billNo"/><s:property value="#iList.billCode"/>',800,600,'view')">
										<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1014.png" title="查看" style="border-width: 0px;" />
									</a>
								</td>
								
								<td align="center">
									<a href="#" onclick = "OpenModalWindow('<%=webapp%>/InputInvoiceEditList.action?billNoAndCode=<s:property value="#iList.billNo"/><s:property value="#iList.billCode"/>',800,600,'view')">
										<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1014.png" title="修改记录" style="border-width: 0px;" />
									</a>
								</td>
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
				</td>
			</tr>
		</table>
	</form>
	<c:import url="${webapp}/page/webctrl/instTree/tree_include.jsp" charEncoding="UTF-8">
		<c:param name="treeType" value="radio" />
		<c:param name="bankName_tree" value="inst_Name" />
		<c:param name="bankId_tree" value="inst_id" />
		<c:param name="taskId_tree" value="" />
		<c:param name="task_tree" value="task_tree" />
		<c:param name="webapp" value="${webapp}" />
	</c:import>
</body>
</html>