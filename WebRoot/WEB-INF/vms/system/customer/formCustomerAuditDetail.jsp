<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires",0);
%>
<%@include file="../../../../page/modalPage.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
<%@ include file="../../../../page/include.jsp"%>

<script language="javascript" type="text/javascript" src="<%=bopTheme %>/js/main.js"></script>
<link href="<c:out value="${bopTheme2}"/>/css/main.css" rel="stylesheet" type="text/css" />
<script language="javascript" type="text/javascript" src="<%=bopTheme %>/js/window.js"></script>
<script charset="gb2312"  language="javascript" type="text/javascript" src="<%=bopTheme %>/js/validator.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>

<script src="<c:out value="${bopTheme}"/>/js/MessageBox/js/messageBox.js" type="text/javascript"></script>
<link href="<c:out value="${bopTheme}"/>/js/MessageBox/css/messageBox.css" rel="stylesheet" type="text/css" />	


	<title>客户信息明细</title>
</head>
<body>
<div class="showBoxDiv">
<form name="formBusiness" id="formBusiness" action="" method="post">
<div class="windowtitle" style="background:#004C7E; height:45px; line-height:45px; text-align:center; color:#FFF; font-weight:bold; font-size:14px;">查看客户</div>
<div class="windowtitle" style="background:#004C7E; height:30px; line-height:30px; text-align:left; color:#FFF; font-size:12px; margin-top: 10px;">客户基本信息</div>
<table id="contenttable" class="lessGrid" cellspacing="0"
							width="100%" align="center" cellpadding="0">
								<tr>
	</tr>
	
	<tr>
	<td width="15%" align="right" class="listbar">数据操作标志</td><td width="35%"><s:if test='customer.dataOperationLabel=="1"'>
	<input type="text" class="tbl_query_text2" readonly="readonly" id="" value="新增"/>
	</s:if>
			<s:if test='customer.dataOperationLabel=="2"'><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="修改"/></s:if>
			<s:if test='customer.dataOperationLabel=="3"'><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="删除"/></s:if></td><input type="hidden" value="<s:property value="customer.dataOperationLabel"/>" id="dataOperationLabel"/>
<td width="15%" align="right" class="listbar">数据审核状态</td><td width="35%"><s:if test='customer.dataAuditStatus=="0"'>
	<input type="text" class="tbl_query_text2" readonly="readonly" id="" value="待审核"/>
	</s:if>
			<s:if test='customer.dataAuditStatus=="1"'>
			<input type="text" class="tbl_query_text2" readonly="readonly" id="" value="审核通过"/>
			</s:if>
			<s:if test='customer.dataAuditStatus=="2"'>
			<input type="text" class="tbl_query_text2" readonly="readonly" id="" value="审核不通过"/>
			</s:if></td>
	</tr>

	<tr>
	<td width="15%" align="right" class="listbar">客户名称:</td>
		<td width="35%"><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="<s:property value='customer.customerCName'/>"/></td>		
	<td width="15%" align="right" class="listbar">纳税人类型:</td>
		
		<td><s:if test='customer.taxPayerType=="G"'><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="小规模纳税人"/></s:if>
		<s:if test='customer.taxPayerType=="S"'><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="小规模纳税人"/></s:if>
		<s:if test='customer.taxPayerType=="I"'><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="小规模纳税人"/></s:if>
		<s:if test='customer.taxPayerType=="O"'><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="小规模纳税人"/></s:if>
		</td>
	</tr>
	
	<tr>
	<td width="15%" align="right" class="listbar">客户纳税人识别号:</td>
		<td width="35%"><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="<s:property value='customer.customerTaxno'/>"/></td>
	<td width="15%" align="right" class="listbar">国籍:</td>
			<td width="35%"><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="<s:property value='customer.countrySName'/>"/></td>
	
	
		</tr>
	<tr>
			<td width="15%" align="right" class="listbar">地址:</td>
			<td width="35%"><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="<s:property value='customer.customerAddress'/>"/></td>
	<td width="15%" align="right" class="listbar">电话:</td>
		<td width="35%"><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="<s:property value='customer.customerPhone'/>"/></td>
	
	</tr>
	
	<tr>
		<td width="15%" align="right" class="listbar">邮箱:</td>
		<td><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="<s:property value='customer.customerEMail'/>"/></td>
	<td width="15%" align="right" class="listbar">开户银行:</td>
		<td width="35%"><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="<s:property value='customer.customerCBank'/>"/></td>
	
	</tr>
	
	<tr>
		<td width="15%" align="right" class="listbar">开户账号:</td>
		<td><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="<s:property value='customer.customerAccount'/>"/></td>
	<td width="15%" align="right" class="listbar">客户类型:</td>
		<td width="35%">
			<s:if test='customer.customerType=="I"'><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="私人客户"/></s:if><s:else>
			<s:if test='customer.customerType=="C"'><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="公司客户"/></s:if>
		</s:else>
		</td>
	</tr>
	
	<tr>
		
		<td width="15%" align="right" class="listbar">发票类型:</td>
		<td>
		<s:if test='customer.fapiaoType=="0"'><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="增值税专用发票"/></s:if><s:else>
		<s:if test='customer.fapiaoType=="1"'><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="增值税普通发票"/></s:if>
		</s:else>
		</td>
	<td width="15%" align="right" class="listbar">是否打票:</td>
		<td width="35%">  
			<s:if test='customer.customerFapiaoFlag=="A"'><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="自动打印"/></s:if>
			<s:if test='customer.customerFapiaoFlag=="M"'><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="手动打印"/></s:if>
			<s:if test='customer.customerFapiaoFlag=="N"'><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="永不打印"/></s:if>
			</td>
		
	</tr>
	<tr>
		
		
	</tr>
	<%--<tr>	
		<td width="15%" align="right" class="listbar">GHO类:</td>
		<td width="35%">  
				<s:property value="customer.ghoClass"/>
			</td>
		
	</tr>
	--%><tr>
		
		<td width="15%" align="right" class="listbar">数据来源:</td>
		<td width="35%">  
			<s:if test='customer.datasSource=="1"'><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="手工"/></s:if>
			<s:if test='customer.datasSource=="2"'><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="系统"/></s:if>
			</td>
		<td width="15%" align="right" class="listbar">客户编号:</td>
		<td><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="<s:property value='customer.customerID'/>"/></td>
		<input type="hidden" id="customerIdList" name="customerIdList" value="<s:property value='customer.customerID'/>"/>
		
	</tr>
	</table>
	<div class="windowtitle" style="background:#004C7E; height:30px; line-height:30px; text-align:left; color:#FFF; font-size:12px;">客户快递信息</div>
		<table id="tbl_context" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0" >
	
	<tr>
		<td width="15%" align="right" class="listbar">联系人:</td>
		<td><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="<s:property value='customer.linkName'/>"/></td>
		<td width="15%" align="right" class="listbar">联系人电话:</td>
		<td><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="<s:property value='customer.linkPhone'/>"/></td>
			
	</tr>
	
	<tr>
		<td width="15%" align="right" class="listbar">联系人地址:</td>
		<td><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="<s:property value='customer.linkAddress'/>"/></td>
	<td width="15%" align="right" class="listbar">客户邮编:</td>
		<td><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="<s:property value='customer.customerZipCode'/>"/></td>
		
	</tr>
	
</table>
</div>
</div>
	<div id="ctrlbutton" class="ctrlbutton" style="border:0px">	
	<table id="tbl_tools" width="100%" border="0">
	<tr>
		<td align="left" width="400">
		<s:if test='customer.dataAuditStatus=="0"'>
		<a href="#" onclick="return audit(1);" name="cmdP" id="cmdP"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1017.png"/>审核通过</a>
				<a href="#" onclick="return audit(2);" name="cmdR" id="cmdR"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1018.png"/>审核不通过</a></s:if>
	<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 

	onclick="window.close()" name="BtnReturn" value="关闭" id="BtnReturn"/>	
		</td></tr>
	 
		</table>	
	
	</div>
</form>
</div>
</body>
<script>
function audit(auditsudits){
	//addAarray:addAarray.toString(),updateAarray:updateAarray.toString(),deleteAarray:deleteAarray.toString()
	var  t = document.getElementById('customerIdList').value;
var dataOperationLabel=document.getElementById("dataOperationLabel").value;
	var addAarray;
	var updateAarray;
	var deleteAarray;
	if(dataOperationLabel==1){
		addAarray=t;
	}
	if(dataOperationLabel==2){
		updateAarray=t;
	}
	if(dataOperationLabel==3){
		deleteAarray=t;
	}
	submitAction(document.forms[0], "CustomerAudit.action?addAarray="+addAarray+"&&updateAarray="+updateAarray+"&&deleteAarray="+deleteAarray+"&&auditsudits="+auditsudits);
	
	
}
</script>
</html>