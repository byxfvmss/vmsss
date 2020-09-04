<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.util.PaginationList"
	import="com.cjit.vms.trans.model.billInvalid.BillCancelInfo"
	import="com.cjit.vms.trans.model.BillInfo"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../../page/include.jsp"%>
<OBJECT ID=sk CLASSID="clsid:003BD8F2-A6C3-48EF-9B72-ECFD8FC4D49F" codebase="NISEC_SKSCX.ocx#version=1,0,0,1" style="display: none"></OBJECT>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<title>增值税管理平台</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/calendar_notime.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/XmlHttp.js"></script>
	<!-- <script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/search.js"></script> -->
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/ylb.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/lhb.js"></script>
	<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/search.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/taxSelver/validatorTax.js"></script>
	
	<script type="text/javascript">
		var msg = '<s:property value="message" escape="false" />';
		// alert(msg);
		if (msg != null && msg != ''){
			alert(msg);
		}
		
		function submitForm(){
			var lvalue=document.getElementById("lvalue").value;
			var tvalue=document.getElementById("tvalue").value;
			if(!/^-?\d+(?:\.\d+)?$/.test(lvalue)){  
					 alert("页边距只能是数字"); 
					 return false;
					}
				if(!/^-?\d+(?:\.\d+)?$/.test(tvalue)){  
					 alert("页边距只能是数字"); 
					 return false;
					}
				var aa=checkDiskAndSelver("",Pageselver(tvalue,lvalue,0));
				if(aa==false){
					return false;
				}
				 aa=checkDiskAndSelver("",Pageselver(tvalue,lvalue,1));
				submitAction(document.forms[0], "savePageMargins.action");
				submitAction(document.forms[0], "listPageList.action");
				
				
		}
		function Pageselver(top,left,fapiaoType){
			 $.ajax({
					   async:false,
					   type: "POST",
					   cache:false,
					   url: "getPrintMargin.action",
					   data:{top:top,left:left,fapiaoType:fapiaoType},
					   dataType:"text",
					   error: function(){
							return false;},
					   success : function(result){
								try
								
								    {
								ret = sk.Operate(result);
								//alert(ret);
								    }
								catch(e)
								    {
								
								    }	
					  }
					});
			}
	</script>
	<style type="text/css">
		.style1{
			width: 242px;
		}
	</style>
</head>
<body onkeydow="enterkey(event)">
	<form name="Form1" method="post" action="savePageMargins.action" id="Form1">
<input type="hidden" name="taxParam" id="taxParam" value="<s:property value='taxParam'/>"/>
       
	<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
	<tr>
		<td class="centercondition">
		<s:component template="rocketMessage" />
		<div id="tbl_current_status">
			<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> 
			<span class="current_status_menu">当前位置：</span> 
			<span class="current_status_submenu1">参数管理</span>
			<span class="current_status_submenu">页边距设置</span>
		</div>
		

 
	
  	<div id="lessGridList1" style="overflow: auto; width: 100%;">
		<table class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0" style="border-collapse: collapse; width: 100%;">
			<tr>
				<td align="right">左边距：</td>
				<td><input type="text" id="lvalue"  class="tbl_query_text" name="lvalue" value="<s:property value='ul.selectedValue'/>"/>
					<input type="hidden" name="lparamId" value="<s:property value='ul.paramId'/>" >
				</td>
				
			</tr>
			<tr>
				<td align="right">上边距：</td>
				<td><input type="text" id="tvalue" name="tvalue" class="tbl_query_text" value="<s:property value='ut.selectedValue'/>"/>
					<input type="hidden" name="tparamId" value="<s:property value='ut.paramId'/>" >
				</td>
				
			</tr>
	</table>
	</div>
	<table id="tbl_tools" width="100%" border="0">
		<tr>
			<td align="left">
			<input type="button" class="tbl_query_button" value="保存"
			onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
			name="btWriteOff" id="btWriteOff" onclick="submitForm()" />
			</td>
		</tr>
	</table>
</table>
	</form>
</body>
</html>