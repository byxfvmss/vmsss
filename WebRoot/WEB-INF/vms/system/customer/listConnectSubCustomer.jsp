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
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/ylb.js"></script>


<!-- MessageBox -->
<script src="<c:out value="${bopTheme}"/>/js/MessageBox/js/messageBox.js" type="text/javascript"></script>
<link href="<c:out value="${bopTheme}"/>/js/MessageBox/css/messageBox.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/My97DatePicker/WdatePicker.js"></script>
<meta http-equiv="Pragma" content="no-cache" />
<title>关联客户</title>
<script type="text/javascript">
function submitBill(){
		}
	function submitForm(){
		//var customerCode=document.getElementById("customerCode").value;
		//var transId=document.getElementById("transId").value;
		 var newUrl = "<%=webapp%>/connectCustomer.action?flag=search";
		submitAction(document.forms[0],newUrl);
	}
	function toExcel(){
		var form = document.getElementById("main");
		form.action="exportCustomer.action";
		form.submit();
		form.action = "";
	}
	function updateConnectCustomer(){
	   if(checkChkBoxesSelected("customerIdList")){
			if(checkChkBoxesSelectedOne("customerIdList")==1){
				if (!confirm("确定对选中交易进行关联客户吗？")){
					return false;
				}
				var customerIds=document.getElementsByName("customerIdList");
				var orgCustomerId = document.getElementById("orgCustomerId");
				var customerId;
				for(var i=0;i<customerIds.length;i++){
					if(customerIds[i].checked){
				 		customerId=customerIds[i].value;
					}
				}
				var transIds=document.getElementById("transIds").value;
				 var newUrl = "<%=webapp%>/updateConnectSubCustomer.action?connCust.currCustomerId="
				     +customerId;
				submitAction(document.forms[0],newUrl);
			}else{
				alert("请选择一条客户记录");
			}
			
		}else{
				alert("请选择客户记录！");
			}
		
		}
		
		function checkChkBoxesSelectedOne(chkBoexName){
			var j=0;
			var chkBoexes= document.getElementsByName(chkBoexName);
			for(i=0;i<chkBoexes.length;i++){
				if(chkBoexes[i].checked){
					j++;
				}
			}
			return j;
}
</script>
</head>
<body>
<form id="main" action="connectCustomer.action" method="post" enctype="multipart/form-data">
<table id="tbl_main" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td align="left">
		<table id="tbl_current_status">
			<tr>
				<td><img
					src="<c:out value="${bopTheme2}"/>/img/jes/icon/home.png" /> 
					<span class="current_status_menu">当前位置：</span>
					<span class="current_status_submenu">
						销项税管理
						<span class="actionIcon">-&gt;</span>开票申请
						<span class="actionIcon">-&gt;</span>关联客户(子公司)
					</span></td>
			</tr>
		</table>
		</td>
	</tr>
	</table>
		<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
		<input type="hidden" name="connCust.transIds" value="<s:property value='connCust.transIds'/>" id="transIds"/>
		<input type="hidden" name="connCust.orgCustomerId" value="<s:property value='connCust.orgCustomerId'/>" id="orgCustomerId"/>
			<tr>
			    <td>客户编号:</td>
				<td>
				<!-- <input id="customerCode" class="tbl_query_text" name="customerCode" type="text" value="<%=request.getAttribute("customerCode") %>" /> -->
				<s:textfield id="customerCode" name="connCust.customerCode" cssClass="tbl_query_text" /></td>
				<td style="text-align:left">
				<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" 
				onMouseOut="this.className='tbl_query_button'" onclick="submitForm('listCustomer.action');" name="cmdFilter" value="查询" id="cmdFilter" />				
				<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" 
				onMouseOut="this.className='tbl_query_button'" onclick="updateConnectCustomer()" name="cmdFilter" value="关联" id="cmdFilter" />				
				<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" 
				onMouseOut="this.className='tbl_query_button'" onclick="window.history.back()" name="cmdFilter" value="返回" id="cmdFilter" />				
				</td>
			</tr>
			<tr>
				<td>备注</td>
				<td>
					<s:textarea id="conCustremark" name="connCust.conCustremark" cols="40"/>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>

<div id="lessGridList" style="overflow: auto; width: 100%;">
<table  class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0"  style="border-collapse: collapse;width: 100%;">
	<tr class="lessGrid head">
	    <th width="3%" style="text-align:center">
	       <input id="CheckAll" style="width:13px;height:13px;" type="checkbox" onClick="checkAll(this,'customerIdList')" />
	    </th>
	    <th>序号</th>
	    <th>子公司ID</th>
	    <th>子公司名称</th>
	    <th>子公司识别号</th>
	    <th>子公司地址</th>
	    <th>子公司电话</th>
	    <th>子公司邮箱</th>
	    <th>子公司开户行</th>
	    <th>子公司开户行账号</th>
	</tr>
	<s:if test="paginationList.recordList != null && !paginationList.recordList.isEmpty()">
	   <s:iterator value="paginationList.recordList" id="iList" status="stuts">
	     <tr align="center" class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
			<td><input type="checkbox" style="width:13px;height:13px;" name="customerIdList" value="<s:property value="#iList.subCustomerId"/>" /></td>
			<td align="center"><s:property value='#stuts.count' /></td>	
			<td align="center"><s:property value='subCustomerId' /></td>
			<td align="center"><s:property value='subCustomerName' /></td>
			<td align="center"><s:property value='subCustomerTaxno' /></td>
			<td align="center"><s:property value='subCustomerAddressand' /></td>
			<td align="center"><s:property value='subCustomerPhone' /></td>
			<td align="center"><s:property value='subCustomerEmail' /></td>
			<td align="center"><s:property value='subCustomerBankand' /></td>
			<td align="center"><s:property value='subCustomerAccount' /></td>
		 </tr>
		</s:iterator>
	  </s:if>
	  <s:if test="paginationList.recordList == null || paginationList.recordList.isEmpty()">
	     <tr>
			<td align="center" colspan="9">对不起,你查的数据不存在</td>
		 </tr>
	  </s:if>
</table>
</div>
    <input type="hidden" name="orgCustomerType" value="<%=request.getAttribute("orgCustomerType")%>">
    <input type="hidden" name="customerCode" value="<%=request.getAttribute("customerCode")%>">
    <input type="hidden" name="orgCustomerId" value="<%=request.getAttribute("customerId")%>">
    <input type="hidden" name="transIds" value="<%=request.getAttribute("transIds")%>">
    <input type="hidden" name="paginationList.showCount" value="false">
<div id="anpBoud" align="Right" style="width:100%;">
        <table width="100%" cellspacing="0" border="0">
            <tr>           
                <td align="right"><s:component template="pagediv"/></td>
            </tr>
        </table>
</div>
</form>
</body>
<script type="text/javascript">
		var msgHight = document.getElementById("anpBoud").offsetHeight;
		var tophight = document.getElementById("tbl_query").offsetHeight;
	/* document.getElementById("lessGridList").style.height = screen.availHeight -310-msgHight-tophight */
</script>
<script language="javascript">	
function OpenModalWindowSubmit(newURL,width,height,needReload,s) {
		var retData = false;
		if(typeof(width) == 'undefined'){
			width 	= screen.width * 0.9;
		}
		if(typeof(height) == 'undefined'){
			height 	= screen.height * 0.9;
		}
		if(typeof(needReload) == 'undefined'){
			needReload 	= false;
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
</html>