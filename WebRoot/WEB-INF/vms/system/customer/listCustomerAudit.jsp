<!-- file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.util.PaginationList"
	import="com.cjit.vms.system.model.Monitor"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../../../page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>

<script language="javascript" type="text/javascript" src="<%=bopTheme %>/js/main.js"></script>
<link href="<c:out value="${bopTheme2}"/>/css/main.css" rel="stylesheet" type="text/css" />
<script language="javascript" type="text/javascript" src="<%=bopTheme %>/js/window.js"></script>
<script charset="gbk"  language="javascript" type="text/javascript" src="<%=bopTheme%>/js/validator.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<!-- MessageBox -->
<script src="<c:out value="${bopTheme}"/>/js/MessageBox/js/messageBox.js" type="text/javascript"></script>
<link href="<c:out value="${bopTheme}"/>/js/MessageBox/css/messageBox.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/My97DatePicker/WdatePicker.js"></script>
<meta http-equiv="Pragma" content="no-cache"/>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<title>客户管理</title>
<script type="text/javascript">
	function submitForm(actionUrl){
		var form = document.getElementById("main");
		form.action=actionUrl;
		form.submit();
		form.action = "listCustomerAudit.action?paginationList.showCount="+"false";
	}
	function toExcel(){
		var form = document.getElementById("main");
		form.action="exportCustomer.action";
		form.submit();
		form.action = "listCustomer.action";
	}
	
</script>
</head>
<body>
<form id="main" action="listCustomerAudit.action" method="post" enctype="multipart/form-data">
<table id="tbl_main" cellpadding="0" cellspacing="0" border="0">
	<tr width="100%">
		<td align="left">
		<table id="tbl_current_status">
		<tr>
		<td class="centercondition">
		<s:component template="rocketMessage" />
		<div id="tbl_current_status">
			<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> 
			<span class="current_status_menu">当前位置：</span> 
			<span class="current_status_submenu1">销项税管理</span>
			<span class="current_status_submenu">客户管理</span>
			<span class="current_status_submenu">客户信息审核</span>
		</div>
	</tr>
			

	
	</table>
		<table id="tbl_query" cellpadding="0" cellspacing="0" border="0" width="100%">
			<tr>
			    <td>客户编号:</td>
				<td><input id="customerCode" class="tbl_query_text"  name="customerCode" type="text" value="<s:property value='customerCode' />" /></td>
			    <td>客户名称:</td>
				<td><input id="customerCName" class="tbl_query_text"  name="customerCName" type="text" value="<s:property value='customerCName' />" /></td>
				<td>客户纳税人识别号:</td>
				<td><input id="taxNo" class="tbl_query_text"  name="taxNo" type="text" value="<s:property value='taxNo' />" /></td>
					<td>纳税人类型:</td>
				
				<td>
					<select id="taxpayerType" name="taxpayerType" >
						<option value="" <s:if test='taxpayerType==""'>selected</s:if><s:else></s:else>>全部</option>
						<option value="S" <s:if test='taxpayerType=="S"'>selected</s:if><s:else></s:else>>小规模纳税人</option>
						<option value="G" <s:if test='taxpayerType=="G"'>selected</s:if><s:else></s:else>>一般纳税人</option>
						<option value="I" <s:if test='taxpayerType=="I"'>selected</s:if><s:else></s:else>>个体纳税人</option>
						<option value="O" <s:if test='taxpayerType=="O"'>selected</s:if><s:else></s:else>>其他</option>
					</select>
				</td>
				
				</tr>
				<tr>
					<%--<td>
					GHO类 
				</td>
				<td>
				<input type="text"  class="tbl_query_text"  name="ghoClass" value="<s:property value='ghoClass'/>">
				</td>
				--%><td>
					数据来源
				</td>
				<td>
				<s:select list="dataSourceList" name="datasSource" id="datasSource" headerKey="" headerValue="全部" listKey="value" listValue="text"></s:select>
				</td>
				
				
				<td align="right" class="listbar">数据操作标志:</td>
		<td>
				<s:select list="dataOperationLabelList" name="dataOperationLabel" id="dataOperationLabel" headerKey="" headerValue="全部" listKey="value" listValue="text"></s:select>
		
		
			
				
				
			
				</tr>
				
		</table>
		</td>
	</tr>
</table>



<table id="tbl_tools" width="100%" border="0">
	<tr>
		<td align="left" width="400">
		<a href="#" name="BtnView" id="BtnView" onclick="submitForm('listCustomerAudit.action');"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1010.png"/>查询</a>
		
		<a href="#" onclick="return audit(1);" name="cmdP" id="cmdP"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1017.png"/>审核通过</a>
				<a href="#" onclick="return audit(2);" name="cmdR" id="cmdR"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1018.png"/>审核不通过</a>
	
		</td><td align="right" width="255">
		
		</td></tr>

		</table>
<div id="lessGridList3" style="overflow: auto; width: 100%;">
<table  class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0"  style="border-collapse: collapse;width: 100%;">
	<tr class="lessGrid head">
	    <th width="3%" style="text-align:center"><input id="CheckAll" style="width:13px;height:13px;" type="checkbox" onClick="checkAll(this,'customerIdList')" /></th>
	    <th width="3%" style="text-align: center">序号</th>
	    <th width="10%" style="text-align: center">客户编号</th>
	    <th width="15%" style="text-align: center">客户名称</th>
	    <th width="10%" style="text-align: center">客户纳税人识别号</th>
		<th width="15%" style="text-align: center">纳税人类型</th>
		<%--<th width="10%" style="text-align: center">GHO类</th>
		--%><th width="10%" style="text-align: center">数据来源</th>
		<th width="15%" style="text-align: center">数据操作标志</th>
		<th width="15%" style="text-align: center">数据审核状态</th>
		<th width="5%" style="text-align: center">详情</th>
	</tr>
	
	<s:iterator value="paginationList.recordList" id="iList" status="stuts">
		<tr align="center"
			class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>"
			>
			<td><input type="checkbox" style="width:13px;height:13px;" name="customerIdList" 
			value="<s:property value="#iList.customerID"/>|<s:property value="#iList.dataOperationLabel"/>" <s:if test='#iList.dataAuditStatus != 0'>disabled</s:if>/></td>
			<td align="center"><s:property value='#stuts.count' /></td>	
			<td ><s:property value='customerID'/></td> 
			<td ><s:property value='customerCName'/></td> 
			<td ><s:property value='customerTaxno' /></td>
			<td>
				<s:if test='taxPayerType=="G"'>一般纳税人</s:if>
				<s:if test='taxPayerType=="S"'>小规模纳税人</s:if>
				<s:if test='taxPayerType=="I"'>个体纳税人</s:if>
				<s:if test='taxPayerType=="O"'>其他</s:if>
			</td><%--
					<td><s:property value="ghoClass"/></td>
			--%><td>
			<s:if test='datasSource=="1"'>手工</s:if>
			<s:if test='datasSource=="2"'>系统</s:if>
			</td>
			<td>
			<s:if test='dataOperationLabel=="1"'>新增</s:if>
			<s:if test='dataOperationLabel=="2"'>修改</s:if>
			<s:if test='dataOperationLabel=="3"'>删除</s:if>
			</td>
			<td>
			<s:if test='dataAuditStatus=="0"'>待审核</s:if>
			<s:if test='dataAuditStatus=="1"'>审核通过</s:if>
			<s:if test='dataAuditStatus=="2"'>审核不通过</s:if>
			</td>
		
			<td align="center">
				<a href="javascript:void(0)" onClick="OpenModalWindow('<c:out value="${webapp}"/>/viewCustomerAuditDetail.action?customerId=<s:property value="#iList.customerID"/>',700,500,'view') ">
			    <img src="<c:out value="${bopTheme2}"/>/img/jes/icon/view.png" alt="查看" style="border-width: 0px;"/></a>
			</td>
		</tr>
		</s:iterator> 
</table>
</div>

<div id="anpBoud" align="Right" style="width:100%;">
        <table width="100%" cellspacing="0" border="0">
            <tr>           
            	<input type="hidden" name="paginationList.showCount" value="false">
                <td align="right"><s:component template="pagediv"/></td>
            </tr>
        </table>
</div>
</form>

</body>
<script type="text/javascript">
		var msgHight = document.getElementById("anpBoud").offsetHeight;
		var tophight = document.getElementById("tbl_query").offsetHeight;
	document.getElementById("lessGridList").style.height = screen.availHeight -310-msgHight-tophight
</script>
<script language="javascript">	

function audit(auditsudits){
	var t = "";
	var inputs = document.getElementsByName('customerIdList');
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
		alert("请选择客户记录");
		return false;
	}
	
	;
	
	$.ajax({
		url: 'CustomerListAudit.action',
	
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
</script>
</html>