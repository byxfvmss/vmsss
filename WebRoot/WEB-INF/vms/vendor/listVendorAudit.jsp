<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.util.PaginationList"
	import="com.cjit.vms.input.model.VendorInfo"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
		<%
		response.setHeader("Pragma","No-cache");
		response.setHeader("Cache-Control","no-cache");
		response.setDateHeader("Expires",0);
		%>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
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
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/lhb.js"></script>
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/search.js"></script>
		<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
	<script language="javascript" type="text/javascript" charset="UTF-8">
	</script>
	<script type="text/javascript">
		function submitForm(actionUrl, flag){
			submitAction(document.forms[0], actionUrl);
			document.forms[0].action="listVendorAudit.action";
		}
		
		function OpenModalWindowSubmit(newURL,width,height,needReload){
			var retData = false;
			if(typeof(width) == 'undefined'){
				width = screen.width * 0.9;
			}
			if(typeof(height) == 'undefined'){
				height = screen.height * 0.9;
			}
			if(typeof(needReload) == 'undefined'){
				needReload = false;
			}
			retData = showModalDialog(newURL, 
						window, 
						"dialogWidth:" + width
							+ "px;dialogHeight:" + height
							+ "px;center=1;scroll=1;help=0;status=0;");
			if(needReload && retData){
				window.document.forms[0].submit();
			}
		}
		
		function saveVendorSuccess(){
			document.forms[0].submit();
			document.forms[0].action="listVendorAudit.action";
		}
		
		function deleteVendor() {
			if(checkChkBoxesSelected("selectVendorIds")){
				var vendorIds = document.Form1.selectVendorIds;
				var ids = "";
				for (var i = 0; i < vendorIds.length; i++){
					if (vendorIds[i].checked){
						ids = ids == "" ? vendorIds[i].value : ids + "," + vendorIds[i].value;
					}
				}
				if(!confirm("确定将选中供应商删除？")){
					return false;
				}
				submitAction(document.forms[0], "deleteVendor.action?vendorIds=" + ids);
				document.forms[0].action="listVendorAudit.action";
			}else{
				alert("请选择需要删除的供应商！");
			}
		}
		
		// [导入]按钮
		function importData(webroot){
			var fileId = document.getElementById("fileId");
			if(fileId.value.length > 0){
				if(fileId.value.lastIndexOf(".XLS") > -1||fileId.value.lastIndexOf(".xls")>-1){
					document.forms[0].action = webroot+"/importVendor.action";
					document.forms[0].submit();
					//document.forms[0].action = 'listVendor.action';
					document.forms[0].action="listVendorAudit.action";
				}else{
					alert("文件格式不对，请上传Excel文件。");
				}
			}else{
				alert("请先选择要上传的文件。");
			}
		}
		
	var msg = '<s:property value="message" escape="false"/>';
		if (msg != null && msg != ''){
			alert(msg);
		}
		function saveVendorAuditSuccess(){
			document.forms[0].submit();
			document.forms[0].action="listVendorAudit.action";
		}
		
	</script>
	<style type="text/css">
		.style1{
			width: 242px;
		}
	</style>
</head>
<body onkeydow="enterkey(event)">
<form name="Form1" method="post" action="listVendorAudit.action" id="Form1" enctype="multipart/form-data">
<table id="tbl_main" cellpadding=0 cellspacing=0>
	<tr>
		<td class="centercondition">
		<s:component template="rocketMessage" />
				<div id="tbl_current_status">
					<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> 
					<span class="current_status_menu">当前位置：</span> 
					<span class="current_status_submenu1">进项税管理</span>
					<span class="current_status_submenu">供应商管理</span>
					<span class="current_status_submenu">供应商信息审核</span>
				</div>
				<div class="widthauto1">
				<table id="tbl_query" cellpadding="0" cellspacing="0" width="90%" border="0">
				<tr>
					<td align="left">
						供应商名称
					</td>
					<td>
						<input type="text" class="tbl_query_text" name="vendorInfo.vendorCName" value="<s:property value='vendorInfo.vendorCName'/>" />
					</td>
					<td align="left">
						账号
					</td>
					<td>
						<input type="text" class="tbl_query_text" name="vendorInfo.vendorAccount" value="<s:property value='vendorInfo.vendorAccount'/>" />
					</td>
					<td align="left">
						税务登记号
					</td>
					<td>
						<input type="text" class="tbl_query_text" name="vendorInfo.vendorTaxNo" value="<s:property value='vendorInfo.vendorTaxNo'/>" />
					</td>
					<td align="left">
						供应商类别
					</td>
					<td>
						<select name="vendorInfo.taxpayerType" style="width:135px">
							<option value="" <s:if test='vendorInfo.taxpayerType==""'>selected</s:if><s:else></s:else>>全部</option>
							<option value="S" <s:if test='vendorInfo.taxpayerType=="S"'>selected</s:if><s:else></s:else>>小规模纳税人</option>
							<option value="G" <s:if test='vendorInfo.taxpayerType=="G"'>selected</s:if><s:else></s:else>>一般纳税人</option>
							<option value="I" <s:if test='vendorInfo.taxpayerType=="I"'>selected</s:if><s:else></s:else>>个体纳税人</option>
							<option value="O" <s:if test='vendorInfo.taxpayerType=="O"'>selected</s:if><s:else></s:else>>其他</option>
						</select>
					</td>
						
				<td align="right" class="listbar">数据操作标志:</td>
			<td>
				<s:select list="dataOperationLabelList" name="vendorInfo.dataOperationLabel" id="vendorInfo.dataOperationLabel" headerKey="" headerValue="全部" listKey="value" listValue="text"></s:select>
		
				</tr>
				</table>
				</div>
				<table id="tbl_tools" width="100%" border="0">
				
					<tr>
		<td align="left" width="400">
		<a href="#" name="BtnView" id="BtnView" onclick="submitForm('listVendorAudit.action');"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1010.png"/>查询</a>
		
		<a href="#" onclick="return audit(1);" name="cmdP" id="cmdP"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1017.png"/>审核通过</a>
				<a href="#" onclick="return audit(2);" name="cmdR" id="cmdR"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1018.png"/>审核不通过</a>
	
		</td><td align="right" width="255">
		
		</td></tr>
				
				</table>
				<div id="lessGridList" style="overflow: auto; width: 100%;">
				<table class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0" style="border-collapse: collapse; width: 100%;">
				<tr class="lessGrid head">
					<th style="text-align:center">
						<input style="width:13px;height:10px;" id="CheckAll" type="checkbox" onclick="cbxselectall(this,'selectVendorIds')" />
					</th>
					<th style="text-align: center">序号</th>
					<th style="text-align: center">供应商名称</th>
					<th style="text-align: center">开户银行</th>
					<th style="text-align: center">账号</th>
					<th style="text-align: center">税务登记号</th>
					<th style="text-align: center">数据操作标志</th>
					<th style="text-align: center">供应商类别</th>
					<th style="text-align: center" >详情</th>
				</tr>
					<s:iterator value="paginationList.recordList" id="iList" status="stuts">
				<tr align="center"
			class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>"
			>
			
			
			<td><input type="checkbox" style="width:13px;height:13px;" name="selectVendorIds" 
			value="<s:property value="#iList.vendorId"/>|<s:property value='dataOperationLabel' />" /></td>
			<td align="center"><s:property value='#stuts.count' /></td>	
			<td align="center"><s:property value='vendorCName' /></td>	
			<td align="center"><s:property value='vendorCBank' /></td>	
			<td align="center"><s:property value='vendorAccount' /></td>	
			<td align="center"><s:property value='vendorTaxNo' /></td>	
			<td>
			<s:if test='dataOperationLabel=="1"'>新增</s:if>
			<s:if test='dataOperationLabel=="2"'>修改</s:if>
			<s:if test='dataOperationLabel=="3"'>删除</s:if>
			</td>
			<td><s:if test='taxpayerType=="G"'>一般纳税人</s:if>
				<s:if test='taxpayerType=="S"'>小规模纳税人</s:if>
				<s:if test='taxpayerType=="I"'>个体纳税人</s:if>
				<s:if test='taxpayerType=="O"'>其他</s:if></td>
				<td align="center">
				<a href="javascript:void(0)" onClick="OpenModalWindowSubmit('<c:out value="${webapp}"/>/auditVendorInfoDetial.action?vendorId=<s:property value="#iList.vendorId"/>',700,700,'view') ">
			    <img src="<c:out value="${bopTheme2}"/>/img/jes/icon/view.png" alt="查看" style="border-width: 0px;"/></a>
			</td>
				<%--<%
					PaginationList paginationList = (PaginationList)request.getAttribute("paginationList");
					if (paginationList != null){
						List vendorInfoList = paginationList.getRecordList();
						if (vendorInfoList != null && vendorInfoList.size() > 0){
							for(int i=0; i<vendorInfoList.size(); i++){
								VendorInfo vendor = (VendorInfo)vendorInfoList.get(i);
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
						<input style="width:13px;height:13px;" type="checkbox" name="selectVendorIds" value="<%=BeanUtils.getValue(vendor,"vendorId")%>" />
					</td>
					<td align="center"><%=i + 1%></td>
					<td align="center"><%=vendor.getVendorCName()==null ? "" :vendor.getVendorCName()%></td>
					<td align="center"><%=vendor.getVendorCBank()==null ? "" :vendor.getVendorCBank()%></td>
					<td align="center"><%=vendor.getVendorAccount()==null ? "" :vendor.getVendorAccount()%></td>
					<td align="center"><%=vendor.getVendorTaxNo()==null ? "" : vendor.getVendorTaxNo()%></td>
					<td align="center"><%=vendor.getDataOperationLabel()==null ? "":vendor.getDataOperationLabel()%></td>
					<td align="center"><%=DataUtil.getTaxpayerTypeCH(vendor.getTaxpayerType())%></td>
					<td align="center">
						<a href="javascript:void(0);" 
						onClick="OpenModalWindowSubmit('toEditVendor.action?vendorId=<%=vendor.getVendorId()%>',650,500,true)">
						<img src ="<c:out value="${bopTheme}"/>/themes/images/icons/icon1005.png" title="修改供应商" style="border-width: 0px;" /></a>
					</td>
					<td align="center">
						<a href="javascript:void(0);" 
						onClick="OpenModalWindowSubmit('viewVendorDetail.action?vendorId=<%=vendor.getVendorId()%>',650,500,true)">
						<img src ="<c:out value="${bopTheme}"/>/themes/images/icons/icon1014.png" title="查看明细" style="border-width: 0px;" /></a>
					</td>
				</tr>
				<%
							}
						}
					}
				%>
				</tr>
				--%>
				</tr>
				</s:iterator>
				</table>
				</div>
				<div id="anpBoud" align="Right" style="overflow:auto;width:100%;">
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
<script type="text/javascript">

function audit(auditsudits){

	var t = "";
	var inputs = document.getElementsByName('selectVendorIds');
	var deleteAarray=new Array();
	var updateAarray=new Array();
	var addAarray=new Array();
	var num=0;
	for ( var i = 0; i < inputs.length; i++) {
		if (inputs[i].checked == true) {
			num++;
		  	var aa=new Array();
		  	aa=inputs[i].value.split("|");
			if(aa[1]=='1'){
				addAarray.push(aa[0]);
			}if(aa[1]=='2'){
				updateAarray.push(aa[0]);
			
			}if(aa[1]=='3'){
				deleteAarray.push(aa[0]);
			}
			
		}
	}
	if(num==0){
		alert("请选择供应商记录");
		return false;
	}
	
	
	
	$.ajax({
		url: 'auditVendorInfo.action',
	
					type: 'POST',
					async:false,
					data:{addAarray:addAarray.toString(),updateAarray:updateAarray.toString(),deleteAarray:deleteAarray.toString(),auditsudits:auditsudits},
					dataType: 'text',
				//	timeout: 1000,
					error: function(){
						return false;},
					success: function(result){
							alert(result);
						document.forms[0].submit();
					
							
			}			
				});
}

		function OpenModalWindowSubmit(newURL,width,height,needReload){
			var retData = false;
			if(typeof(width) == 'undefined'){
				width = screen.width * 0.9;
			}
			if(typeof(height) == 'undefined'){
				height = screen.height * 0.9;
			}
			if(typeof(needReload) == 'undefined'){
				needReload = false;
			}
			retData = showModalDialog(newURL, 
						window, 
						"dialogWidth:" + width
							+ "px;dialogHeight:" + height
							+ "px;center=1;scroll=1;help=0;status=0;");
			if(needReload && retData){
				window.document.forms[0].submit();
			}
		}
		
</script>
<script type="text/javascript">
		var msgHight = document.getElementById("anpBoud").offsetHeight;
		var tophight = document.getElementById("tbl_query").offsetHeight;
	document.getElementById("lessGridList").style.height = screen.availHeight -310-msgHight-tophight
</script>
</html>