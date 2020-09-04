<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.vms.trans.model.BillInfo"
	import="com.cjit.vms.trans.model.BillItemInfo"
	import="com.cjit.vms.trans.model.TransInfo"
	import="com.cjit.vms.system.model.Business"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../../../page/modalPage.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@ include file="../../../page/include.jsp"%>
	<title><s:if test="inst.instId != null && inst.instId != '' ">修改机构</s:if><s:else>新增机构</s:else></title>
	<link type="text/css" href="<%=bopTheme2%>/css/subWindow.css" rel="stylesheet">
	<link rel="stylesheet" type="text/css" href="<%=bopTheme%>/css/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=bopTheme%>/css/icon.css">
	<link href="<%=bopTheme%>/js/MessageBox/css/messageBox.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="<%=bopTheme%>/js/validator.js"></script>
	<script type="text/javascript" src="<%=bopTheme%>/js/MessageBox/js/messageBox.js"></script>
	<script type="text/javascript" src="<%=bopTheme%>/js/jquery-1.4.4.min.js"></script>
	<script type="text/javascript" src="<%=bopTheme%>/js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=webapp%>/js/jquery/region.js" charset="GBK"></script>
	<script language="javascript" type="text/javascript">
		//标识页面是否已提交
		var subed = false;
		function findOutSubmit() {		
			if(fucCheckNull(document.getElementById("business.businessCode"),"请输入交易码")==false) {		
				return false;
			}
			if(fucCheckNull(document.getElementById("business.businessCName"),"请输入交易名称")==false) {		
				return false;
			}
			var taxRate = document.getElementById("taxRate");
			if(fucCheckNull(taxRate,"请输入税率")==false) {		
				return false;
			}else if(fucIsNoUnsignedFloat(taxRate,"请输入非负数")==false){
				return false;
			}
			var form = document.getElementById("formBusiness");
			form.action='saveBusiness.action';
			form.submit();
		}
		//初始化多选框
		$(document).ready(function(){
		//if('<s:property value="editType"/>'=='addSub'){
		    var node=window.dialogArguments.parent.frames["leftFrame"].window.instTree.GetSelectedTreeNodes(); 
		    if(node!=null){
		       $('#parentInstName').val(node.getAttribute("name"));
		       $('#parentInstId').val(node.getAttribute("id"));
		       $('#taxRate').val(node.getAttribute("class"));
		    }
		//}
		});
		
	</script>
</head>
<body>
<div class="showBoxDiv">
<form name="formBusiness" id="formBusiness" action="saveBusiness.action" method="post">
<input type="hidden" name="editType" id="editType" value="add" />    
<div id="editsubpanel" class="editsubpanel">
<div style="overflow: auto; width: 100%;" >    
<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0" >
	<tr class="header">
		<th colspan="4" >
			新增交易种类
		</th>
	</tr>
	<tr>
		<td width="15%" align="right" class="listbar">交易码1:</td>
		<td width="35%">
				<input id="business.businessCode" name="business.businessCode" type="text"/>&nbsp; 
            <span class="spanstar">*</span>
		</td>
		<td width="15%" align="right" class="listbar">交易名称:</td>
		<td>
			<input id="business.businessCName" name="business.businessCName" type="text" maxlength="20" /> 
			<span class="spanstar">*</span>
		</td>
	</tr>
	<tr>
		<td width="15%" align="right" class="listbar">上级交易码:</td>
		<td width="35%">
			
		    <input id="parentInstId" name="business.businessParentCode" type="hidden" value="<s:property value="inst.parentInstId" />"/>
		    <input id="parentInstName" readonly name="business.businessParentName" type="text" class="tbl_query_text_readonly"/>
            <span class="spanstar">*</span>
		</td>
		
	</tr>
	<tr>
		<td align="right" class="listbar">税率(%):</td>
		<td><input id="taxRate" type="text" name="business.taxRate" maxlength="20"/>
		<span class="spanstar">*</span></td>
		<td align="right" class="listbar">是否自动打印:</td>
		<td><s:select list="#{0:'否'}" label="abc" listKey="key" listValue="value"
					  headerKey="1" headerValue="是" name="business.autoPrint" maxlength="20"/>
			<span class="spanstar">*</span></td>
	</tr>
</table>
</div>
</div>
	<div id="ctrlbutton" class="ctrlbutton" style="border:0px">		
	<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 
	onclick="findOutSubmit()" name="BtnSave" value="保存" id="BtnSave"/>
	<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 
	onclick="window.close()" name="BtnReturn" value="关闭" id="BtnReturn"/>	
	</div>
	<script language="javascript" type="text/javascript">
	</script>
</form>
</div>
</body>
</html>
