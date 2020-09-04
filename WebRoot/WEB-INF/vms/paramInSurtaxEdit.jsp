<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html> 
<head>
<base target="_self">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../page/include.jsp"%>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
<!-- MessageBox -->
<script src="<%=bopTheme%>/js/MessageBox/js/messageBox.js" type="text/javascript"></script>
<link href="<%=bopTheme%>/js/MessageBox/css/messageBox.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="<%=bopTheme%>/js/validator.js"></script>
<link href="<%=webapp%>/page/subWindow.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<base target="_self">
<title>进项税转出比例/金额管理-编辑</title>

<script language="javascript" type="text/javascript">
//标识页面是否已提交
var subed = false;
function checkForm(){
    //验证是否重复提交
    if (subed == true){
       alert("信息正在发送给服务器，请不要重复提交信息！");
       return false;
    }
//     if(fucCheckNull(document.getElementById("vatOutAmt"),"转出金额不能为空")==false){
//        return false;
//     }
//     if(fucIsFloat(document.getElementById("vatOutAmt"),"请输入正确的转出金额")==false){
//        return false;
//     }
    
    if(fucCheckNull(document.getElementById("vatOutProportion"),"转出比例不能为空")==false){
       return false;
    }
    if(fucIsFloat(document.getElementById("vatOutProportion"),"请输入正确的转出比例")==false){
       return false;
    }
   	if(CheckSurtaxRate(document.getElementById("vatOutProportion"),"4","转出比例必须是大于0.01小于1的小数")==false){
		return false;
	}
    subed=true;
    return true;
}

function submitForm(){
	if(true == checkForm()){
		document.getElementById("doEditParamInSurtaxForm").action = '<c:out value='${webapp}'/>/saveParamInSurtax.action';
		document.getElementById("doEditParamInSurtaxForm").submit();
	}
}
	function CheckSurtaxRate(obj,len,strAlertMsg)	{
		var strValue=obj.value;
		if(strValue!="" && (isNaN(strValue) || strValue>=1 || strValue<=0)){
			var m = new MessageBox(obj);
			m.Show(strAlertMsg);	
			obj.focus();
			return false;
		}else		{
		   return true;
		}	
	}
	</script>
</head>
<body>
<div class="showBoxDiv">

<form id="doEditParamInSurtaxForm" action="<c:out value='${webapp}'/>/saveParamInSurtax.action" method="post">
<input type="hidden" name="taxPerNumber" value='<s:property value="taxPerNumber"/>'/>
<div id="editpanel">
<div id="editsubpanel" class="editsubpanel">
<div style="overflow: auto; width: 100%;" >  
<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
	<tr>
		<th colspan="4">进项税转出比例/金额管理-编辑</th>
	</tr>
	<!-- 
	<tr>
		<td class="contnettable-subtitle" colspan="4">领用基本信息</td>
	</tr>
	 -->
	<tr>
		<td width="15%" style="text-align: right" class="listbar">数据时间:</td>
		<td width="35%">
			<input type="text" class="tbl_query_text" name="dataDt"  id="dataDt"  value="<s:property value='paramInSurtaxInfo.dataDt'/>" disabled/>
		</td>
		<td width="15%" style="text-align: right" class="listbar">机构</td>
		<td><input type="text" class="tbl_query_text" name="instName"  id="instName"  value="<s:property value='paramInSurtaxInfo.instName'/>" disabled/></td>
	</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">纳税人识别号:</td>
		<td width="35%">
			<input type="text" class="tbl_query_text" name="taxpayerId'"  id="taxpayerId'"  value="<s:property value='paramInSurtaxInfo.taxpayerId'/>" disabled/>
		</td>
		<td width="15%" style="text-align: right" class="listbar">纳税人名称:</td>
		<td>
			<input type="text" class="tbl_query_text" name="taxpayerName"  id="taxpayerName"  value="<s:property value='paramInSurtaxInfo.taxpayerName'/>" disabled/>
    	</td>
	</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">免税收入:</td>
		<td width="35%">
			<input type="text" class="tbl_query_text" name="taxfreeIncome"  id="taxfreeIncome"  value="<s:property value='paramInSurtaxInfo.taxfreeIncome'/>" disabled/>
		</td>
		<td width="15%" style="text-align: right" class="listbar">征税收入:</td>
		<td>
		<input type="text" class="tbl_query_text" name="assessableIncome"  id="assessableIncome"  value="<s:property value='paramInSurtaxInfo.assessableIncome'/>" disabled/>
		</td>
	</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">转出比例:</td>
		<td width="35%">
			<input type="text" class="tbl_query_text" name="vatOutProportion"  id="vatOutProportion"  value="<s:property value='paramInSurtaxInfo.vatOutProportion'/>" />
		</td>
		<td width="15%" style="text-align: right" class="listbar">转出金额:</td>
		<td>
		<input type="text" class="tbl_query_text" name="vatOutAmt"  id="vatOutAmt"  value="<s:property value='paramInSurtaxInfo.vatOutAmt'/>" disabled/>
		</td>
	</tr>
</table>
</div>
</div>
<div id="ctrlbutton" class="ctrlbutton" style="border:0px;width:100%;padding-right:0px;margin-right:10px;">
	<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="submitForm();" name="BtnSave" value="保存" id="BtnSave"/>
	<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="CloseWindow();" name="BtnReturn" value="关闭" id="BtnReturn"/>
</div>

</form>
</div>
</body>
<script>
$(function(){
	$("#vatOutProportion").change(function(){
		if($(this).val()==""){
			alert("转出比例不能为空");
			return;
		}
		$vatOutProportion=parseFloat($(this).val()).toFixed(2)
		$("#vatOutProportion").val($vatOutProportion);
	});
})
</script>
</html>