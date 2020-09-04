<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires",0);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html> 
<head>
<base target="_self">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../page/include.jsp"%>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
<link href="<%=webapp%>/page/subWindow.css" type="text/css" rel="stylesheet">
<title>查看商品管理</title>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script type="text/javascript" src="<%=bopTheme%>/js/validator.js"></script>
<!-- MessageBox -->
<script src="<%=bopTheme%>/js/MessageBox/js/messageBox.js" type="text/javascript"></script>
<link href="<%=bopTheme%>/js/MessageBox/css/messageBox.css" rel="stylesheet" type="text/css" />
	
<script type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
</head>
<body scroll="no">
<div class="showBoxDiv">

<form id="frm" action="<c:out value='${webapp}'/>/addOrUpdGoodsInfo.action" method="post">
<input type="hidden" id="taxnoBak" name="taxnoBak" value="<s:property value="taxno" />" />
<input type="hidden" id="goodsNameBak" name="goodsNameBak" value="<s:property value="goodsName" />" />
<input type="hidden" id="transTypeBak" name="transTypeBak" value="<s:property value="transType" />" />
<div id="editsubpanel" class="editsubpanel">
<div style="overflow: auto; width: 100%;" >  
	<input type="hidden" name="updFlg" id="updFlg" value="<s:property value="updFlg" />" />
<table id="contenttable" class="lessGrid" cellspacing="0"
	width="100%" align="center" cellpadding="0">
	<tr class="header">
		<th colspan="4">
			查看商品管理
		</th>
	</tr>
<%--	<tr>--%>
<%--		<td class="contnettable-subtitle" colspan="10">发票基本信息<s:property value="testv" /></td>--%>
<%--	</tr>--%>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">商品名称:</td>
		<td>
			<input id="goodsName" name="goodsName" type="text" class="tbl_query_text2" value="<s:property value="goodsName" />"  maxlength="50" style="ime-mode: disabled" disabled="disabled" />
		</td>
		</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">发票商品编号:</td>
		<td width="35%">
			<input id="goodsNo1" name="goodsNo1" type="text" class="tbl_query_text2" value="<s:property value="goodsNo" />"  maxlength="20" style="ime-mode: disabled" disabled="disabled"/>
		</td>
	</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">纳税人识别号:</td>
		<td width="35%">
		<s:select name="taxno" list="taxperList" listKey='taxperNumber' listValue='taxperNumber' disabled='true'/>
	</tr>
	
	<%--<tr class="lessGrid head">
		<th style="text-align:center">选择</th>
		<th style="text-align:center">交易编码</th>
		<th style="text-align:center">交易名称</th>
	</tr>
	<s:iterator value="paginationList.recordList" id="iList" status="stuts">
						<tr align="center"class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
							<td align="center" >
							<input type="checkbox" style="width: 13px; height: 13px;" name="selectGoodsNos" value="<s:property value="goodsNo"/>" checked='checked' onclick="return false;"/>
							<input type="checkbox" style="display:none" name="selectTransTypes" value="<s:property value="transType" />"/>
							</td>
							<td align="center">
								<s:property value="transType" />
							</td>
							<td align="center">
								<s:property value="transName" />
							</td>
						</tr>
					</s:iterator>
	--%>
	<tr class="row1">
		<td valign="top" colspan="10" style="border-top:#BCBCBC solid 1px;">
			<iframe id="subTableFrame" scrolling="auto" src="getBusiList.action?taxno=<s:property value="taxno"/>&goodsNo=<s:property value="goodsNo"/>" height="390px" width="100%" frameborder="0"></iframe>
		</td>
	</tr>
	
	
		<%-- <tr>
		<td width="15%" style="text-align: right" class="listbar">交易类型:</td>
		<td>
			<s:select id="transType" name="transType" headerKey="" headerValue="请选择" 
				list="businessList" listKey='businessCode' listValue='businessCName' />
		</td>
	</tr> --%>
</table>
</div>
<div id="ctrlbutton" class="ctrlbutton" style="border:0px">
	<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="CloseWindow();" name="BtnReturn" value="返回" id="BtnReturn"/>
</div>

</form>
</div>
</body>

</html>