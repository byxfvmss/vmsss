<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../page/include.jsp"%>
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">

<script type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script type="text/javascript" src="<%=bopTheme%>/js/validator.js"></script>
<!-- MessageBox -->
<script src="<%=bopTheme%>/js/MessageBox/js/messageBox.js" type="text/javascript"></script>
<link href="<%=bopTheme%>/js/MessageBox/css/messageBox.css" rel="stylesheet" type="text/css" />
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
<meta http-equiv="Pragma" content="no-cache" />
<title>发票勾稽</title>

<script type="text/javascript">
	function submitForm(actionUrl){
		document.forms[0].action = actionUrl;
		document.forms[0].submit();
		document.forms[0].action = "billInfoCheckList.action";
	}
	function checkAll(obj,itemName){
      var inputs=document.getElementsByName(itemName);	      
      for(var i=0;i<inputs.length;i++){
      	if(inputs[i].disabled == false){
     	    inputs[i].checked=obj.checked; 
      	}else{
      	inputs[i].checked=false; 
      	}
     }
  }
</script>
</head>
<body>
<form id="main" action="<c:out value='${webapp}'/>/billInfoCheckList.action" method="post">
<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
	<tr>
		<td class="centercondition">
		<div id="tbl_current_status">
			<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> 
			<span class="current_status_menu">当前位置：</span> 
			<span class="current_status_submenu1">销项税管理</span>
			<span class="current_status_submenu">发票勾稽</span>
			<span class="current_status_submenu">发票勾稽</span>
		</div>
		<div class="widthauto1">
		<table id="tbl_query" cellpadding="0" cellspacing="0" border="0" width="100%" >
			<tr>
				<td>申请开票日期</td>
				<td>
				<input id="startDate" name="startDate" type="text"  value="<s:property value='startDate' />" class="tbl_query_time"	 onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}'})" />&nbsp;--&nbsp;
				<input id="endDate" name="endDate" type="text"  value="<s:property value='endDate' />" class="tbl_query_time"	 onFocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}'})" />&nbsp;&nbsp;&nbsp;
				</td>
				<td>客户名称</td> 
				<td><input id="customerName" class="tbl_query_text" class="tbl_query_text" name="customerName" type="text" value="<s:property value='customerName' />" style="width:150px"/></td>
				<td>发票类型</td> 
				<td>
					<select id="fapiaoType" name="fapiaoType" ><option value="" <s:if test='fapiaoType==""'>selected</s:if><s:else></s:else>>所有</option>
						<s:iterator value="mapVatType" id="entry">  
						<option value="<s:property value="key"/>" <s:if test='fapiaoType==#entry.key'>selected</s:if><s:else></s:else>><s:property value="value"/></option>
				       </s:iterator>
					</select>
				</td>
			</tr>
			<tr>
				<td>状态</td> 
				<td>
					<select id="datastatus" name="dataStatus" ><option value="" <s:if test='dataStatus==""'>selected</s:if><s:else></s:else>>所有</option>
						<s:iterator value="mapDataStatus" id="entry">  
						<option value="<s:property value="key"/>" <s:if test='dataStatus==#entry.key'>selected</s:if><s:else></s:else>><s:property value="value"/></option>
				       </s:iterator>
					</select>
				</td>
				<td>勾稽状态</td> 
				<td>
					<select id="gjType" name="gjType" >
						<option value="1" <s:if test='gjType=="1"'>selected</s:if>>未勾稽</option>
						<option value="0" <s:if test='gjType=="0"'>selected</s:if>>已勾稽</option>
					</select>
				</td>
				<s:if test='configCustomerFlag.equals("KBC")'>
				<td>客户号</td> 
				<td><input id="customerId" class="tbl_query_text" class="tbl_query_text" name="customerId" type="text" value="<s:property value='customerId' />" style="width:150px"/></td>
				</s:if>
				<td>
					<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 
					onclick="submitForm('billInfoCheckList.action');" name="cmdSelect" value="查询" id="cmdSelect" />			
				</td>
				</tr>
				<!-- <td>
					<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 
					onclick="submitForm('billInfoCheckExportExcel.action');" name="cmdExcel" value="导出EXCEL" id="cmdExcel" />					
				</td> -->
		</table>
 		<table id="tbl_tools" width="100%" border="0">
			<tr>
				<td align="left">
				<a href="#" onclick="submitForm('billInfoCheckExportExcel.action');"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png"/>导出</a>
				</td>
			</tr>
		</table>
<div id="lessGridList4" style="overflow: auto; width: 100%;">
<table  class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0"  style="border-collapse: collapse;width: 100%;">
	<tr class="lessGrid head">
	<th width="3%" style="text-align:center"><input id="CheckAll" style="width:13px;height:13px;" type="checkbox" onClick="checkAll(this,'selectInfos')" /></th>
		<th style="text-align: center">序号</th>
		<th style="text-align: center">申请开票日期</th>
		<th style="text-align: center">开票日期</th>
		<th style="text-align: center">客户名称</th>
		<s:if test='configCustomerFlag.equals("KBC")'>
		<th style="text-align: center">客户号</th>
		</s:if>
		<th style="text-align: center">客户纳税人识别号</th>
		<th style="text-align: center">合计金额</th>
		<th style="text-align: center">合计税额</th>
		<th style="text-align: center">价税合计</th>
		<th style="text-align: center">开具类型</th>
		<th style="text-align: center">发票类型</th>
		<th style="text-align: center">状态</th>
		<th style="text-align: center">操作
		</th>
	</tr>
	<s:iterator value="paginationList.recordList" id="iList" status="stuts">
		
		<tr align="center" class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
		    <td><input type="checkbox" style="width:13px;height:13px;" name="selectInfos" value="<s:property value="#iList.billId"/>"/></td>
			<td align="center"><s:property value='#stuts.count' /></td>		
			<td><s:property value='applyDate'/></td>
			<td><s:property value='billDate'/></td>
			<td><s:property value='customerName'/></td>
			<s:if test='configCustomerFlag.equals("KBC")'>
			<td><s:property value='customerId'/></td>
			</s:if>
			<td><s:property value='customerTaxno'/></td>
			<td><s:property value='amtSum'/></td>
			<td><s:property value='taxAmtSum'/></td>
			<td><s:property value='sumAmt'/></td>
			<td>
				<s:iterator value="mapIssueType" id="entry">  
					<s:if test='issueType==#entry.key'><s:property value="value"/></s:if>
				</s:iterator>
			</td>
			<td>
				<s:iterator value="mapVatType" id="entry">  
					<s:if test='fapiaoType==#entry.key'><s:property value="value"/></s:if>
				</s:iterator>
			</td>
			<td><s:property value='mapDataStatus[#iList.dataStatus]'/></td>
			<td style="width:60px;"> 
			<s:if test='%{#request.gjType=="0"}'>
				<a href="javascript:void(0)" 
				onClick="OpenModalWindow('billInfoViewData.action?billId=<s:property value="#iList.billId" />',900,700,'view') ">
			    <img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1014.png" title="查看数据" style="border-width: 0px;"/></a>
			</s:if>
				<a href="javascript:void(0)" 
				onClick="OpenModalWindow('billInfoCheckExe.action?billId=<s:property value="#iList.billId" />',1000,700,'view') ">
			    <img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1005.png" title="发票勾稽" style="border-width: 0px;"/></a>
			    <a href="javascript:void(0)" 
				onClick="OpenModalWindow('billInfoViewImg.action?billId=<s:property value="#iList.billId" />',900,650,'view') ">
			    <img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1015.png" title="查看票据" style="border-width: 0px;"/></a>
			</td>
		</tr>
	</s:iterator> 
</table>
</div>
<div id="anpBoud" align="Right" style="width:100%;">
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
		var msgHight = document.getElementById("anpBoud").offsetHeight;
		var tophight = document.getElementById("tbl_query").offsetHeight;
	document.getElementById("lessGridList1").style.height = screen.availHeight -310-msgHight-tophight
</script>
</html>















