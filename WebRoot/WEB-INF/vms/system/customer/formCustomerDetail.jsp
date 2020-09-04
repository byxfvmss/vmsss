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

<script src="<c:out value="${bopTheme}"/>/js/MessageBox/js/messageBox.js" type="text/javascript"></script>
<link href="<c:out value="${bopTheme}"/>/js/MessageBox/css/messageBox.css" rel="stylesheet" type="text/css" />	
	<title>客户信息明细</title>
</head>
<body>
<form name="formBusiness" id="formBusiness" method="post">
	<div class="windowtitle" style="background:#004C7E; height:45px; line-height:45px; text-align:center; color:#FFF; font-weight:bold; font-size:14px;">查看客户</div>
	<div class="windowtitle" style="background:#004C7E; height:30px; line-height:30px; text-align:left; color:#FFF; font-size:12px; margin-top: 10px;">客户基本信息</div>
						<table id="contenttable" class="lessGrid" cellspacing="0"
							width="100%" align="center" cellpadding="0">
							<tr>
								
							</tr>
							<tr>
								<td width="15%" align="right" class="listbar">
									客户名称:
								</td>
								<td>
									<input size="30" id="customerCName" class="tbl_query_text2"  name="customerCName" type="text"
										maxlength="20"
										value="<s:property value='customer.customerCName'/>"
										readonly="readonly"/>
								
								</td>
								<td width="15%" align="right" class="listbar">
									纳税人类型:
								</td>
								<td width="35%">
								<s:if test='customer.taxPayerType=="S"'><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="小规模纳税人"/></s:if>
			<s:elseif test='customer.taxPayerType=="G"'><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="一般纳税人"/></s:elseif>
			<s:elseif test='customer.taxPayerType=="I"'><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="个体纳税人"/></s:elseif>
			<s:elseif test='customer.taxPayerType=="O"'><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="其他"/></s:elseif>
			<s:else></s:else>
									
								</td>
							</tr>
							
							
							<tr>
								<td width="15%" align="right" class="listbar">
									客户纳税人识别号:
								</td>
								<td width="35%">
									
									<input size="30" id="customerTaxno" class="tbl_query_text2"  name="customerTaxno" type="text"
										value="<s:property value='customer.customerTaxno'  />"
										 maxlength="18" readonly="readonly"/>
							
								</td>
								<td width="15%" align="right" class="listbar">
									国籍:
								</td>
								<td>
									<input size="30" class="tbl_query_text2"  value="<s:property value='customer.countrySName'/>" type="text" readonly="readonly"/>
								</td>
							</tr>
							
							<tr>

								<td width="15%" align="right" class="listbar">
									地址:
								</td>
								<td>
									<input size="40" id="customerAddress" class="tbl_query_text2"  name="customerAddress" type="text"
										maxlength="20"
										value="<s:property value='customer.customerAddress'/>" readonly="readonly"/>
								</td>
						<td width="15%" align="right" class="listbar">
									电话:
								</td>
								<td>
									<input size="30" id="customerPhone" class="tbl_query_text2" name="customerPhone" type="text"
										maxlength="20"
										value="<s:property value='customer.customerPhone'/>"
										 readonly="readonly"/>
							
								</td>


							</tr>
							<tr>
								<td width="15%" align="right" class="listbar">
									邮箱:
								</td>
								<td width="35%">
									<input size="30" id="customerEmail" class="tbl_query_text2"  name="customerEmail" type="text"
										value="<s:property value='customer.customerEMail'/>"
										readonly="readonly"/>
							
								</td>
					<td width="15%" align="right" class="listbar">
									开户银行
								</td>
								<td width="35%">
									<input size="30" id="customerCBank" class="tbl_query_text2"  name="customerCBank" type="text"
										value="<s:property value='customer.customerCBank'/>" readonly="readonly"/>
									&nbsp;
							
								</td>
							</tr>
							<tr>
								<td width="15%" align="right" class="listbar">
									开户账号:
								</td>
								<td>
									<input size="30" id="customerAccount" class="tbl_query_text2"  name="customerAccount" type="text"
										maxlength="20"
										value="<s:property value='customer.customerAccount'/>"
										readonly="readonly"/>
									&nbsp;
						
								<td width="15%" align="right" class="listbar">
									客户类型:
								</td>
								<td width="35%">
									
									
			<s:if test='customer.customerType=="I"'><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="私人客户"/></s:if>
			<s:elseif test='customer.customerType=="C"'><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="公司客户"/></s:elseif>
			<s:else><input type="text" class="tbl_query_text2" readonly="readonly" id="" value=""/></s:else>		
								</td>
								
							</tr>

							<tr>


								<td width="15%" align="right" class="listbar">
									发票类型:
								</td>
								<td>
								<s:if test='customer.fapiaoType=="0"'><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="增值税专用发票"/></s:if>
			<s:elseif test='customer.fapiaoType=="1"'><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="增值税普通发票"/></s:elseif>
			<s:else><input type="text" class="tbl_query_text2" readonly="readonly" id="" value=""/></s:else>
									
										<td width="15%" align="right" class="listbar">
									是否打票:
								</td>
								<td width="15%">
									<s:if test='customer.customerFapiaoFlag=="A"'><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="自动打印"/></s:if>
			<s:elseif test='customer.customerFapiaoFlag=="M"'><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="手动打印"/></s:elseif>
			<s:elseif test='customer.customerFapiaoFlag=="N"'><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="永不打印"/></s:elseif>
			<s:else><input type="text" class="tbl_query_text2" readonly="readonly" id="" value=""/></s:else>
								
								
								
								</td>
							</tr>
												<tr>
	<td class="listbar" align="right">客户编号</td>
<td><input type="text" class="tbl_query_text2" value="<s:property value='customer.customerID'/>" name="customerId"  readonly="readonly">
								</td>
								<td width="15%" align="right" class="listbar">数据来源</td>
		<td>	
			<s:if test='customer.datasSource=="1"'><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="手工"/></s:if>
			<s:elseif test='customer.datasSource=="2"'><input type="text" class="tbl_query_text2" readonly="readonly" id="" value="系统"/></s:elseif>
			<s:else><input type="text" class="tbl_query_text2" readonly="readonly" id="" value=""/></s:else>
										
		
</td>
							</tr>
          
	<%--<tr>
		<td width="15%" align="right" class="listbar">GHO类</td>
		<td width="35%"><input type="text" name="ghoClass" id="ghoClass"  class="tbl_query_text" value="<s:property value="customer.ghoClass"/>"/></td>
		
	</tr>
	--%>
</table>
<div class="windowtitle" style="background:#004C7E; height:30px; line-height:30px; text-align:left; color:#FFF; font-size:12px;">客户快递信息</div>
	<table id="tbl_context" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0" >
<tr>
    <td width="15%" align="right" class="listbar">联系人</td>
     <td width="35%" >    
       <input size="40" id="linkName" align="left" class="tbl_query_text2"   name="linkName" type="text" maxlength="30" value="<s:property value='customer.linkName'/>" readonly="readonly"/>&nbsp;  
     </td>
     <td width="15%" align="right" class="listbar">联系人电话</td>
		 <td width="35%" >		
		 	<input size="40" align="left" id="linkPhone" class="tbl_query_text2"  name="linkPhone" type="text" maxlength="15" value="<s:property value='customer.linkPhone'/>" readonly="readonly"/>&nbsp;  
		 </td>
  </tr>
  <tr>
    
	</tr>
	<tr>
		<td width="15%" align="right" class="listbar">联系人地址</td>
		 <td width="35%" >		
		 	<input size="40" align="left" id="linkAddress" class="tbl_query_text2"  name="linkAddress" type="text" maxlength="50" value="<s:property value='customer.linkAddress'/>" readonly="readonly"/>&nbsp;  
		 </td>
		 <td width="15%" align="right" class="listbar">客户邮编</td>
		 <td width="35%" >		
		 	<input size="40" align="left" id="customerZipCode" class="tbl_query_text2"  name="customerZipCode" type="text" maxlength="50" value="<s:property value='customer.customerZipCode'/>" readonly="readonly"/>&nbsp;  		 	
		 </td>
	</tr>
	



						</table>
				<div id="ctrlbutton" class="ctrlbutton" style="border: 0px">
					<input type="button" class="tbl_query_button"
						onMouseMove="this.className='tbl_query_button_on'"
						onMouseOut="this.className='tbl_query_button'"
						onclick="window.close()" name="BtnReturn" value="关闭"
						id="BtnReturn" />
				</div>
			</form>
</body>
</html>