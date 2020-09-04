<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../page/include.jsp"%>
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
<link href="<%=webapp%>/page/subWindow.css" type="text/css" rel="stylesheet">
<title>进项税转出比例/金额管理-查看</title>
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
<input type="hidden" name="bill_id" value='<s:property value="bill_id"/>'/>
<div id="editpanel">
<div id="editsubpanel" class="editsubpanel">
<div style="overflow: auto; width: 100%;" >  
<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
	<tr>
		<th colspan="4">进项税转出比例/金额管理-查看</th>
	</tr>
	<!-- 
	<tr>
		<td class="contnettable-subtitle" colspan="4">领用基本信息</td>
	</tr>
	 -->
	<tr>
		<td width="15%" style="text-align: right" class="listbar">数据时间:</td>
		<td width="35%"> <s:property value='paramInSurtaxInfo.dataDt'/>
<!--			<input type="text" name="dataDt"  id="dataDt"  value="<s:property value='paramInSurtaxInfo.dataDt'/>" disabled/>-->
		</td>
		<td width="15%" style="text-align: right" class="listbar">机构</td>
		<td><s:property value='paramInSurtaxInfo.instName'/>
<!--		<input type="text" name="instName"  id="instName"  value="<s:property value='paramInSurtaxInfo.instName'/>" disabled/>-->
		</td>
	</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">纳税人识别号:</td>
		<td width="35%"><s:property value='paramInSurtaxInfo.taxpayerId'/>
<!--			<input type="text" name="taxPerNumber'"  id="taxPerNumber'"  value="<s:property value='paramInSurtaxInfo.taxpayerId'/>" disabled/>-->
		</td>
		<td width="15%" style="text-align: right" class="listbar">纳税人名称:</td>
		<td><s:property value='paramInSurtaxInfo.taxpayerName'/>
<!--			<input type="text" name="taxpayerName"  id="taxpayerName"  value="<s:property value='paramInSurtaxInfo.taxpayerName'/>" disabled/>-->
    	</td>
	</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">免税收入:</td>
		<td width="35%"><s:property value='paramInSurtaxInfo.taxfreeIncome'/>
<!--			<input type="text" name="taxfreeIncome"  id="taxfreeIncome"  value="<s:property value='paramInSurtaxInfo.taxfreeIncome'/>" disabled/>-->
		</td>
		<td width="15%" style="text-align: right" class="listbar">征税收入:</td>
		<td><s:property value='paramInSurtaxInfo.assessableIncome'/>
<!--		<input type="text" name="assessableIncome"  id="assessableIncome"  value="<s:property value='paramInSurtaxInfo.assessableIncome'/>" disabled/>-->
		</td>
	</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">转出比例:</td>
		<td width="35%"><s:property value='paramInSurtaxInfo.vatOutProportion'/>
<!--			<input type="text" name="vatOutProportion"  id="vatOutProportion"  value="<s:property value='paramInSurtaxInfo.vatOutProportion'/>"  disabled />-->
		</td>
		<td width="15%" style="text-align: right" class="listbar">转出金额:</td>
		<td><s:property value='paramInSurtaxInfo.vatOutAmt'/>
<!--		<input type="text" name="vatOutAmt"  id="vatOutAmt"  value="<s:property value='paramInSurtaxInfo.vatOutAmt'/>" disabled/>-->
		</td>
	</tr>
</table>
</div>
</div>
<div id="ctrlbutton" class="ctrlbutton" style="border:0px;width:100%;padding-right:0px;margin-right:10px;">
	<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" 
	onMouseOut="this.className='tbl_query_button'" onclick="window.close();" value="关闭" />
</div>

</form>
</div>
</body>
<script>
$(function(){
	$("#vatOutAmt").change(function(){
		if($(this).val()==""){
			alert("转出金额不能为空");
			return;
		}
		$vatOutProportion=parseFloat($(this).val())/parseFloat($("#sumAmt").val())
		$("#vatOutProportion").val(($vatOutProportion*100).toFixed(2));
	});
	
	$("#vatOutProportion").change(function(){
		if($(this).val()==""){
			alert("转出比例不能为空");
			return;
		}
		$vatOutAmt=parseFloat($(this).val())*parseFloat($("#sumAmt").val())/100
		$("#vatOutAmt").val(($vatOutAmt).toFixed(2));
	});
})
</script>
</html>