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
<title>税控盘监控信息-新增</title>

<script language="javascript" type="text/javascript">
//标识页面是否已提交
var subed = false;
function checkForm(){
    //验证是否重复提交
    if (subed == true){
       alert("信息正在发送给服务器，请不要重复提交信息！");
       return false;
    }
    if(fucCheckNull(document.getElementById("taxDiskNo"),"税控盘号不能为空")==false){
       return false;
    }
    if(fucCheckNull(document.getElementById("fapiaoType"),"发票类型不能为空")==false){
       return false;
    }
    if(fucCheckNull(document.getElementById("billEndDateS"),"开票截止日期不能为空")==false){
       return false;
    }
    if(fucCheckNull(document.getElementById("dataRepStrDateS"),"数据报送起始日期不能为空")==false){
       return false;
    }
    if(fucCheckNull(document.getElementById("dataRepEndDateS"),"数据报送终止日期不能为空")==false){
       return false;
    }
    if(fucCheckNull(document.getElementById("billLimitAmtS"),"单张发票开票金额限额不能为空")==false){
       return false;
    }
    if(fucIsInteger(document.getElementById("billLimitAmtS"),"单张发票开票金额限额必须为数字")==false){
       return false;
    }
    
    if(fucCheckNull(document.getElementById("billLimitAmtPS"),"正数发票累计金额限额不能为空")==false){
       return false;
    }
    if(fucIsInteger(document.getElementById("billLimitAmtPS"),"正数发票累计金额限额必须为数字")==false){
       return false;
    }
    
    if(fucCheckNull(document.getElementById("billLimitAmtNS"),"负数发票累计金额限额不能为空")==false){
       return false;
    }
    if(fucIsInteger(document.getElementById("billLimitAmtNS"),"负数发票累计金额限额必须为数字")==false){
       return false;
    }
    
    if(fucCheckNull(document.getElementById("nBillFlgS"),"负数发票标志不能为空")==false){
       return false;
    }
    if(fucCheckNull(document.getElementById("nBilDayS"),"负数发票天数不能为空")==false){
       return false;
    }
    if(fucCheckNull(document.getElementById("newReportDateS"),"最新报税日期不能为空")==false){
       return false;
    }
    if(fucCheckNull(document.getElementById("residualCapacityS"),"剩余容量不能为空")==false){
       return false;
    }
    if(fucIsInteger(document.getElementById("residualCapacityS"),"剩余容量必须为数字")==false){
       return false;
    }
    
    if(fucCheckNull(document.getElementById("uploadDeadlineS"),"上传截止日期不能为空")==false){
       return false;
    }
    if(fucCheckNull(document.getElementById("limitFunctionS"),"限定功能标识不能为空")==false){
       return false;
    }
    if(fucCheckNull(document.getElementById("offLineDayS"),"离线开票时长不能为空")==false){
       return false;
    }
    if(fucIsInteger(document.getElementById("offLineDayS"),"离线开票时长必须为数字")==false){
       return false;
    }
    
    if(fucCheckNull(document.getElementById("offLineBillS"),"离线开票张数不能为空")==false){
       return false;
    }
    if(fucIsInteger(document.getElementById("offLineBillS"),"离线开票张数必须为数字")==false){
       return false;
    }
    
    if(fucCheckNull(document.getElementById("offLineAmtPS"),"离线正数累计金额不能为空")==false){
       return false;
    }
    if(fucIsInteger(document.getElementById("offLineBillS"),"离线正数累计金额必须为数字")==false){
       return false;
    }
    
    if(fucCheckNull(document.getElementById("offLineAmtNS"),"离线负数累计金额不能为空")==false){
       return false;
    }
    if(fucIsInteger(document.getElementById("offLineBillS"),"离线负数累计金额必须为数字")==false){
       return false;
    }
    
    if(fucCheckNull(document.getElementById("offLineOtsS"),"离线扩展信息不能为空")==false){
       return false;
    }
//     if(fucIsFloat(document.getElementById("vatOutProportion"),"请输入正确的转出比例")==false){
//        return false;
//     }
//    	if(CheckSurtaxRate(document.getElementById("vatOutProportion"),"4","转出比例必须是大于0.01小于1的小数")==false){
// 		return false;
// 	}
    subed=true;
    return true;
}

function submitForm(){
	if(true == checkForm()){
	$action= '<c:out value='${webapp}'/>/checkTaxDiskMonitor.action';
		$post_data=$("#doAddTaxDiskMonitorForm").serialize();
		ajax_post($action,$post_data,function(json){
			$json_obj=eval('('+json+')');
			if($json_obj.msg=='ok'){
				document.getElementById("doAddTaxDiskMonitorForm").action = '<c:out value='${webapp}'/>/saveTaxDiskMonitor.action';
				document.getElementById("doAddTaxDiskMonitorForm").submit();
			}else{
				subed=false;
				alert("您添加的数据系统中已经存在!");
				return;
			}
		});
		
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
function ajax_post(the_url,the_param,succ_callback){
	$.ajax({
		type:'POST',
		url:the_url,
		data:the_param,
		success:succ_callback,
		error:function(html){
			alert("提交数据失败，代码:" +html.status+ "，请稍候再试");
		}
	});
}
</script>
</head>
<body>
<div class="showBoxDiv">

<form id="doAddTaxDiskMonitorForm" action="<c:out value='${webapp}'/>/saveTaxDiskMonitor.action" method="post">
<div id="editpanel">
<div id="editsubpanel" class="editsubpanel">
<div style="overflow: auto; width: 100%;" >  
<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
	<tr>
		<th colspan="4">新增税控盘监控信息</th>
	</tr>
	<tr>
		<td  style="text-align: right; width:20%" class="listbar">税控盘号:</td>
		<td style="text-align: left; width:50%">
			<input type="text" class="tbl_query_text"  name="taxDiskMonitorInfo.taxDiskNo"  id="taxDiskNo"  
			onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"
			value="<s:property value='taxDiskMonitorInfo.taxDiskNo'/>" maxlength="12"/>
		</td>
	</tr>
	<tr>
		<td  style="text-align: right" class="listbar">发票类型:</td>
		<td>
			<select id="fapiaoType" name="taxDiskMonitorInfo.fapiaoType" ><option value="" <s:if test='taxDiskMonitorInfo.fapiaoType==""'>selected</s:if><s:else></s:else>>全部</option>
				<s:iterator value="mapVatType" id="entry">  
				<option value="<s:property value="key"/>" <s:if test='taxDiskMonitorInfo.fapiaoType==#entry.key'>selected</s:if><s:else></s:else>><s:property value="value"/></option>
		       </s:iterator>
			</select>
		</td>
	</tr>
	<tr>
		<td  style="text-align: right" class="listbar">开票截止日期:</td>
		<td >
			<input type="text" class="tbl_query_text"  name="taxDiskMonitorInfo.billEndDateS"  id="billEndDateS"  value="<s:property value='taxDiskMonitorInfo.billEndDateS'/>" class="tbl_query_time"	 onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
		</td>
	</tr>
	<tr>
		<td  style="text-align: right" class="listbar">数据报送起始日期:</td>
		<td>
			<input type="text" class="tbl_query_text"  name="taxDiskMonitorInfo.dataRepStrDateS"  id="dataRepStrDateS"  value="<s:property value='taxDiskMonitorInfo.dataRepStrDateS'/>" class="tbl_query_time"	 onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
    	</td>
	</tr>
	<tr>
		<td  style="text-align: right" class="listbar">数据报送终止日期:</td>
		<td >
			<input type="text" class="tbl_query_text"  name="taxDiskMonitorInfo.dataRepEndDateS"  id="dataRepEndDateS" 
			 value="<s:property value='taxDiskMonitorInfo.dataRepEndDateS'/>" class="tbl_query_time"	 onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
		</td>
	</tr>
	<tr>
		<td  style="text-align: right" class="listbar">单张发票开票金额限额:</td>
		<td>
		<input type="text" class="tbl_query_text"  name="taxDiskMonitorInfo.billLimitAmtS"  id="billLimitAmtS"  
		onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"
		value="<s:property value='taxDiskMonitorInfo.billLimitAmtS'/>" maxlength="12"/>元
		</td>
	</tr>
	<tr>
		<td  style="text-align: right" class="listbar">正数发票累计金额限额:</td>
		<td >
			<input type="text" class="tbl_query_text"  name="taxDiskMonitorInfo.billLimitAmtPS"  id="billLimitAmtPS"  
			onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"
			value="<s:property value='taxDiskMonitorInfo.billLimitAmtPS'/>" maxlength="12"/>元
		</td>
	</tr>
	<tr>
		<td  style="text-align: right" class="listbar">负数发票累计金额限额:</td>
		<td>
		&nbsp;&nbsp;-&nbsp;&nbsp;<input type="text" class="tbl_query_text"  name="taxDiskMonitorInfo.billLimitAmtNS"  id="billLimitAmtNS" 
		onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"
		 value="<s:property value='taxDiskMonitorInfo.billLimitAmtNS'/>" maxlength="11" />元
		</td>
	</tr>
	<tr>
		<td  style="text-align: right" class="listbar">负数发票标志:</td>
		<td>
			<select id="nBillFlgS" name="taxDiskMonitorInfo.nBillFlgS" >
				<option value="1">1:在盘内</option>
				<option value="2">2:不在盘内</option>
			</select>
<!--		<input type="text" class="tbl_query_text"  name="taxDiskMonitorInfo.nBillFlgS"  id="nBillFlgS"  value="<s:property value='taxDiskMonitorInfo.nBillFlgS'/>" maxlength="12"/>-->
		</td>
	</tr>
	<tr>
		<td  style="text-align: right" class="listbar">负数发票天数:</td>
		<td>
		&nbsp;&nbsp;-&nbsp;&nbsp;<input type="text" class="tbl_query_text"  name="taxDiskMonitorInfo.nBilDayS"  id="nBilDayS"  
		onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"
		value="<s:property value='taxDiskMonitorInfo.nBilDayS'/>" maxlength="3" />
		</td>
	</tr>
	<tr>
		<td  style="text-align: right" class="listbar">最新报税日期:</td>
		<td>
		<input type="text" class="tbl_query_text"  name="taxDiskMonitorInfo.newReportDateS"  id="newReportDateS"  value="<s:property value='taxDiskMonitorInfo.newReportDateS'/>" class="tbl_query_time"	 onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
		</td>
	</tr>
	<tr>
		<td  style="text-align: right" class="listbar">剩余容量:</td>
		<td>
		<input type="text" class="tbl_query_text"  name="taxDiskMonitorInfo.residualCapacityS"  id="residualCapacityS"  
		onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"
		value="<s:property value='taxDiskMonitorInfo.residualCapacityS'/>" maxlength="12" />
		</td>
	</tr>
	<tr>
		<td  style="text-align: right" class="listbar">上传截止日期:</td>
		<td>
		<input type="text" class="tbl_query_text"  name="taxDiskMonitorInfo.uploadDeadlineS"  id="uploadDeadlineS"  value="<s:property value='taxDiskMonitorInfo.uploadDeadlineS'/>" class="tbl_query_time"	 onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
		</td>
	</tr>
	<tr>
		<td  style="text-align: right" class="listbar">限定功能标识:</td>
		<td>
		<input type="text" class="tbl_query_text"  name="taxDiskMonitorInfo.limitFunctionS"  id="limitFunctionS"  
		onkeyup="value=value.replace(/[^\w\.\/]/ig,'')"
		value="<s:property value='taxDiskMonitorInfo.limitFunctionS'/>" maxlength="2" />
		</td>
	</tr>
	<tr>
		<td  style="text-align: right" class="listbar">离线开票时长:</td>
		<td>
		<input type="text" class="tbl_query_text"  name="taxDiskMonitorInfo.offLineDayS"  id="offLineDayS"  
		onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"
		value="<s:property value='taxDiskMonitorInfo.offLineDayS'/>" maxlength="5" />
		</td>
	</tr>
	<tr>
		<td  style="text-align: right" class="listbar">离线开票张数:</td>
		<td>
		<input type="text" class="tbl_query_text"  name="taxDiskMonitorInfo.offLineBillS"  id="offLineBillS"  
		onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"
		value="<s:property value='taxDiskMonitorInfo.offLineBillS'/>" maxlength="10" />
		</td>
	</tr>
	<tr>
		<td  style="text-align: right" class="listbar">离线正数累计金额:</td>
		<td>
		<input type="text" class="tbl_query_text"  name="taxDiskMonitorInfo.offLineAmtPS"  id="offLineAmtPS"  
		onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"
		value="<s:property value='taxDiskMonitorInfo.offLineAmtPS'/>" maxlength="12" />
		</td>
	</tr>
	<tr>
		<td  style="text-align: right" class="listbar">离线负数累计金额:</td>
		<td>
		&nbsp;&nbsp;-&nbsp;&nbsp;<input type="text" class="tbl_query_text"  name="taxDiskMonitorInfo.offLineAmtNS"  
		onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"
		id="offLineAmtNS"  value="<s:property value='taxDiskMonitorInfo.offLineAmtNS'/>" maxlength="11" />
		</td>
	</tr>
	<tr>
		<td  style="text-align: right" class="listbar">离线扩展信息:</td>
		<td>
		<input type="text" class="tbl_query_text"  name="taxDiskMonitorInfo.offLineOtsS"  id="offLineOtsS"  
		value="<s:property value='taxDiskMonitorInfo.offLineOtsS'/>" maxlength="160" />
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