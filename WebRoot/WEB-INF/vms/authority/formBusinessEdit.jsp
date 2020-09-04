<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.util.PaginationList"
	import="com.cjit.vms.trans.model.billInvalid.BillCancelInfo"
	import="com.cjit.vms.trans.model.BillInfo"
	import="com.cjit.vms.system.model.Business"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../../page/modalPage.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@ include file="../../../page/include.jsp"%>
	<title>修改交易</title>
	<link type="text/css" href="<%=bopTheme2%>/css/subWindow.css" rel="stylesheet">
	<link rel="stylesheet" type="text/css" href="<%=bopTheme%>/css/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=bopTheme%>/css/icon.css">
	<link href="<%=bopTheme%>/js/MessageBox/css/messageBox.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="<%=bopTheme%>/js/jquery-1.4.4.min.js"></script>
	<script type="text/javascript" src="<%=bopTheme%>/js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=webapp%>/page/js/jquery/region.js" charset="GBK"></script>
	<script type="text/javascript" src="<%=bopTheme%>/js/validator.js"></script>
	<script type="text/javascript" src="<%=bopTheme%>/js/MessageBox/js/messageBox.js"></script>
	<script language="javascript" type="text/javascript">
		//标识页面是否已提交
		var subed = false;
		var msg = '<s:property value="message" escape="false"/>';
		if (msg != null && msg != ''){
			alert(msg);
		}
		function findOutSubmit() {		
			if(fucCheckNull(document.getElementById("business.math"),"请输入公式")==false) {		
				return false;
			}
			var form = document.getElementById("formBusiness");
			form.action='saveBusiness.action';
			form.submit();
		}
	</script>
</head>
<body scroll="no" style="overflow:hidden;">
<div class="showBoxDiv">
<form name="formBusiness" id="formBusiness" action="saveBusiness.action" method="post">
<div id="editsubpanel" class="editsubpanel">
<div style="overflow: auto; width: 100%;" >
<input type="hidden" name="editType" id="editType" value="edit" />
<%-- <input type="hidden" name="business.businessLayer"  value="<s:property value='business.businessLayer'/>" /> 
<input type="hidden" name="business.orderNum" id="editType" value="<s:property value='business.orderNum'/>" /> 
<input type="hidden" name="business.isHead" id="editType" value="<s:property value='business.isHead'/>" /> 
<input type="hidden" name="business.isUse" id="editType" value="<s:property value='business.isUse'/>" /> 
    --%>
<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0" >
	<tr class="header">
		<th colspan="7" >
			修改交易认定种类
		</th>
	</tr>
<tr>
		<td align="right" class="listbar">编号:</td>
		<td >
				<input type="text" class="tbl_query_text" maxlength="50" value="<s:property value="business.serialNo"/>" disabled="disabled"/>&nbsp; 
            <span class="spanstar">*</span>
		</td>
		<td align="right" class="listbar">税率:</td>
		<td><input maxlength="20" value="<s:property value="business.taxRate"/>" disabled="disabled" type="text" class="tbl_query_text"/>
		<span class="spanstar">*</span></td>
		<td align="right" class="listbar">公式:</td>
		<td><input id="business.math" type="text" class="tbl_query_text" name="business.math" maxlength="20" value="<s:property value="business.math"/>"/>
		<span class="spanstar">*</span></td>
	</tr>	
		<tr style="display: none" ><input type="hidden" id="business.taxRateId" name="business.taxRateId" value="<s:property value="business.taxRateId"/>"/> </tr>
		<tr style="display: none" ><input type="hidden" id="business.taxRate" name="business.taxRate" value="<s:property value="business.taxRate"/>"/> </tr>
		<tr style="display: none" ><input type="hidden" id="business.serialNo" name="business.serialNo" value="<s:property value="business.serialNo"/>"/> </tr>
</table>
</div>
</div>
	<div id="ctrlbutton" class="ctrlbutton" style="border:0px">		
	<a></a>
	<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 
	onclick="findOutSubmit()" name="BtnSave" value="保存" id="BtnSave"/>
	<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 
	onclick="window.close()" name="BtnReturn" value="关闭" id="BtnReturn"/>	
	</div>
	<script language="javascript" type="text/javascript">
	$(document).ready(function(){
			$("#autoPrint").attr("value",$("#busiautoPrint").val());
			$("#hasTax").attr("value",$("#busihasTax").val());
			
		});
		
	 $(document).ready(function(){
	    $("#btn1").click(function(){
	        $("#test").append($("#btn1").val());
	    });
	     $("#btn2").click(function(){
	        $("#test").append($("#btn2").val());
	    });
	      $("#btn3").click(function(){
	        $("#test").append($("#btn3").val());
	    });
	      $("#btn4").click(function(){
	        $("#test").append($("#btn4").val());
	    });
	      $("#btn5").click(function(){
	        $("#test").append($("#btn5").val());
	    });
	       $("#btn6").click(function(){
	        $("#test").append($("#btn6").val());
	    });
	       $("#btn7").click(function(){
	        $("#test").append($("#btn7").val());
	    });
	       $("#btn8").click(function(){
	        $("#test").append($("#btn8").val());
	    });
	       $("#btn9").click(function(){
	        $("#test").append($("#btn9").val());
	    });
	    
	       $("#p.index").click(function(){
	        var text = $("#test").val();
	        var ss = $("#p.index").val();
	        text += ss;
	        $("#test").val(text);
	    });
   });
   
   function show(name){
   	$("#test").append(name);
   }
	</script>

	<table>
	<tr>
		<td ><input type="button" id = "btn1" value=" + " style="height:30px;width:50px;">
		<input type="button" id = "btn2" value=" - " style="height:30px;width:50px;">
		<input type="button" id = "btn3" value=" * " style="height:30px;width:50px;">
		<input type="button" id = "btn4" value=" % " style="height:30px;width:50px;">
		<input type="button" id = "btn5" value=" and " style="height:30px;width:50px;">
		<input type="button" id = "btn6" value=" or " style="height:30px;width:50px;">
		<input type="button" id = "btn7" value=" = " style="height:30px;width:50px;">
		<input type="button" id = "btn8" value=" ( " style="height:30px;width:50px;">
		<input type="button" id = "btn9" value=" ) " style="height:30px;width:50px;">
		</td>
	</tr>
	<tr>
		<s:iterator value="infoList" status="p" >
			<s:if test="#p.index%4==0">
			</tr><tr>
			<td style="float: left">
			</s:if>
				<input type="button" value="<s:property value="comments"/>" onclick="show('<s:property value="column_Name"/>')" style="height:30px;width:150px;" />
		</s:iterator>
		</td>
	</tr>
	<tr>
		<td align="right" class="listbar">
		<textarea rows="7" cols="70" id ="test" name="business.sql"><s:if test="business.sql!=null"><s:property value="business.sql"/></s:if><s:else>select TRANS_TYPE from VMS_TRANS_INFO where 1=1</s:else> </textarea></td>
	</tr>
	
	</table>
	</form>
</div>
</body>
</html>