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
<link href="<%=webapp%>/page/subWindow.css" type="text/css" rel="stylesheet">
<title><s:if test="operType=='add' ">新增税控盘基本信息</s:if><s:elseif test="operType=='edit'">编辑税控盘基本信息</s:elseif></title>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script type="text/javascript" src="<%=bopTheme%>/js/validator.js"></script>
<!-- MessageBox -->
<script src="<%=bopTheme%>/js/MessageBox/js/messageBox.js" type="text/javascript"></script>
<link href="<%=bopTheme%>/js/MessageBox/css/messageBox.css" rel="stylesheet" type="text/css" />
<style>
.tbl_text {
  font-size: 12px;
  border: 1px solid #B7B7B7;
  height: 21px;
  width: 250px;
}
.tbl_small_text {
  font-size: 12px;
  border: 1px solid #B7B7B7;
  height: 21px;
  width: 150px;
}
</style>
<script type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript">
//标识页面是否已提交
var subed = false;
function checkForm(){

    //验证是否重复提交
    if (subed == true){
       alert("信息正在发送给服务器，请不要重复提交信息！");
       return false;
    }
    //税控盘号是否为空
    if(fucCheckNull(document.getElementById("taxDiskNo"),"请输入税控盘号")==false){
       return false;
    }
  	//开票机号是否为空
    if(fucCheckNull(document.getElementById("billMachineNo"),"请输入开票机号")==false){
       return false;
    }
    //税控盘版本号是否为空
    if(fucCheckNull(document.getElementById("taxDiskVersion"),"请输入税控盘版本号")==false){
       return false;
    }
    //税控盘时钟是否为空
    if(fucCheckNull(document.getElementById("taxDiskDate"),"请输入税控盘时钟")==false){
         return false;
    }
    //纳税人识别号是否为空
    if(fucCheckNull(document.getElementById("taxpayerNo"),"请选择纳税人识别号")==false){
         return false;
    }
    if(document.getElementById("operType").value=="add" && CountTaxDiskInfo() == false){
		alert("相同的税控盘号和纳税人识别号已存在");
		return false;
	}
    //纳税人名称是否为空
    if(fucCheckNull(document.getElementById("taxpayerNam"),"请输入纳税人名称")==false){
         return false;
    }
    //税控盘口令是否为空
//     if(fucCheckNull(document.getElementById("taxDiskPsw"),"请输入税控盘口令")==false){
//          return false;
//     }
    //证书口令是否为空
//     if(fucCheckNull(document.getElementById("taxCertPsw"),"请输入证书口令")==false){
//          return false;
//     }
    //税务机关代码是否为空
    if(fucCheckNull(document.getElementById("taxBureauNum"),"请输入税务机关代码")==false){
         return false;
    }
    //税务机关名称是否为空
    if(fucCheckNull(document.getElementById("taxBureauNam"),"请输入税务机关名称")==false){
         return false;
    }
    //发票类型代码是否为空
    if(fucCheckNull(document.getElementById("diskBillType"),"请输入发票类型代码")==false){
         return false;
    }
    //企业类型是否为空
    if(fucCheckNull(document.getElementById("diskCustType"),"请输入企业类型")==false){
         return false;
    }
    //保留信息是否为空
    if(fucCheckNull(document.getElementById("retainInfo"),"请输入保留信息")==false){
         return false;
    }
    //启用时间是否为空
    if(fucCheckNull(document.getElementById("enableDt"),"请选择启用时间")==false){
         return false;
    }
    subed=true;
    return true;
}

function submitForm(){
	
	if(true == checkForm()){
		//var taxpayerNam = document.getElementById("taxpayerNam").value;
		//?taxpayerNam="+encodeURI(encodeURI(taxpayerNam));
		document.forms[0].action = "<%=webapp%>/saveTaxDiskInfo.action";
		document.forms[0].submit();
	}
}
function settaxperNameList(taxperNo){
		//转action查询信息
		$.ajax({url: 'getTaxperNameByTaxperNo.action',
			type: 'POST',
			async:false,
			data:{taxperNo:taxperNo},
			dataType: 'text',
			timeout: 1000, 
			error: function(){return false;},
			success: function(result){
			    if(result != null && result !='')
			    {
					document.getElementById("taxpayerNam").value=result;}
			    else
			    {
			    	document.getElementById("taxpayerNam").value="";
			    }
			}
		});
	}
function CountTaxDiskInfo(){
	var countNum = 0;
	$.ajax({
		url: "<%=webapp%>/CountTaxDiskInfo.action",
		type: 'POST',
		async:false,
		data:{operType:'<s:property value="operType" />',taxDiskNo:$("#taxDiskNo").val(),taxpayerNo:$("#taxpayerNo").val()},
		error: function(){return false;},
		success: function(result){
			countNum = result;
		}
	});
	if (countNum>0){
		return false;
	}
	return true;
}
</script>
</head>
<body scroll="no" style="overflow:hidden;">
<div class="showBoxDiv">

<form id="frm" action="<c:out value='${webapp}'/>/savePaperInvoice.action" method="post">
<div id="editsubpanel" class="editsubpanel">
<div style="overflow: auto; width: 100%;" >  
	<input type="hidden" id="operType" name="operType" value="<s:property value="operType" />" />
	<s:if test='operType=="edit"'>
		<input type="hidden" id="taxDiskNo" name="taxDiskNo" value="<s:property value="taxDiskInfo.taxDiskNo" />" />
		<input type="hidden" id="taxpayerNo" name="taxpayerNo" value="<s:property value="taxDiskInfo.taxpayerNo" />" />
		<input type="hidden" id="taxpayerNam" name="taxpayerNam" value="<s:property value="taxDiskInfo.taxpayerNam" />" />
	</s:if>
<table id="contenttable" class="lessGrid" cellspacing="0"
	width="100%" align="center" cellpadding="0">
	<tr class="header">
		<th colspan="4"><s:if test="operType=='add' ">新增税控盘基本信息</s:if><s:elseif
			test="operType=='edit'">编辑税控盘基本信息</s:elseif></th>
	</tr>
	<tr>
		<td width="30%" style="text-align: right" class="listbar">税控盘号:</td>
		<td width="70%">
			<input id="taxDiskNo" name="taxDiskNo"
			type="text" class="tbl_small_text" value="<s:property value="taxDiskInfo.taxDiskNo" />" maxlength="12" <s:if test='operType=="edit"'>disabled="disabled"</s:if>/>
			<span class="spanstar">*</span>
		</td>
	</tr>
	<tr>
		<td width="30%" style="text-align: right" class="listbar">开票机号:</td>
		<td width="70%">
			<input id="billMachineNo" name="billMachineNo"
			type="text" class="tbl_small_text" value="<s:property value="taxDiskInfo.billMachineNo" />" maxlength="5" />
			<span class="spanstar">*</span>
		</td>
	</tr>
	<tr>
		<td width="30%" style="text-align: right" class="listbar">税控盘版本号:</td>
		<td width="70%">
			<input id="taxDiskVersion" name="taxDiskVersion"
			type="text" class="tbl_small_text" value="<s:property value="taxDiskInfo.taxDiskVersion" />" maxlength="10" />
			<span class="spanstar">*</span>
		</td>
	</tr>
	<tr>
		<td width="30%" style="text-align: right" class="listbar">税控盘时钟:</td>
		<td width="70%">
			<input id="taxDiskDate" name="taxDiskDate"
			type="text" class="tbl_small_text" value="<s:property value="taxDiskInfo.taxDiskDate" />" maxlength="14" />
			<span class="spanstar">*</span>
		</td>
	</tr>
	<tr>
		<td width="30%" style="text-align: right" class="listbar">纳税人识别号:</td>
		<td width="70%">
			<s:if test='operType=="edit"'>
			<s:if test="taxperList != null && taxperList.size > 0">
				<s:select name="taxpayerNo" list="taxperList" listKey='taxperNumber' listValue='taxperNumber' headerKey="" headerValue="所有" disabled="true"/>
			</s:if>
			</s:if>
			<s:else>
				<s:select name="taxpayerNo" list="taxperList" listKey='taxperNumber' listValue='taxperNumber' headerKey="" headerValue="所有" onchange="settaxperNameList(this.value)"/>
			</s:else>
			<span class="spanstar">*</span>
		</td>
	</tr>
	<tr>
		<td width="30%" style="text-align: right" class="listbar">纳税人名称:</td>
		<td width="70%">
			<input id="taxpayerNam" name="taxpayerNam"
				type="text" class="tbl_text" value="<s:property value="taxDiskInfo.taxpayerNam" />" maxlength="80"  readonly="readonly"/>
			<span class="spanstar">*</span>
		</td>
	</tr>
<!-- 	<tr> -->
<!-- 		<td width="30%" style="text-align: right" class="listbar">税控盘口令:</td> -->
<!-- 		<td width="70%"> -->
<!-- 			<input id="taxDiskPsw" name="taxDiskPsw" -->
<!-- 			type="text" class="tbl_small_text" value="<s:property value="taxDiskInfo.taxDiskPsw" />" maxlength="8" /> -->
<!-- 			<span class="spanstar">*</span> -->
<!-- 		</td> -->
<!-- 	</tr> -->
<!-- 	<tr> -->
<!-- 		<td width="30%" style="text-align: right" class="listbar">证书口令:</td> -->
<!-- 		<td width="70%"> -->
<!-- 			<input id="taxCertPsw" name="taxCertPsw" -->
<!-- 			type="text" class="tbl_small_text" value="<s:property value="taxDiskInfo.taxCertPsw" />" maxlength="8" /> -->
<!-- 			<span class="spanstar">*</span> -->
<!-- 		</td> -->
<!-- 	</tr> -->
	<tr>
		<td width="30%" style="text-align: right" class="listbar">税务机关代码:</td>
		<td width="70%">
			<input id="taxBureauNum" name="taxBureauNum"
			type="text" class="tbl_small_text" value="<s:property value="taxDiskInfo.taxBureauNum" />" maxlength="11" />
			<span class="spanstar">*</span>
		</td>
	</tr>
	<tr>
		<td width="30%" style="text-align: right" class="listbar">税务机关名称:</td>
		<td width="70%">
			<input id="taxBureauNam" name="taxBureauNam"
			type="text" class="tbl_text" value="<s:property value="taxDiskInfo.taxBureauNam" />" maxlength="80" />
			<span class="spanstar">*</span>
		</td>
	</tr>
	<tr>
		<td width="30%" style="text-align: right" class="listbar">发票类型代码:</td>
		<td width="70%">
			<input id="diskBillType" name="diskBillType"
			type="text" class="tbl_text" value="<s:property value="taxDiskInfo.diskBillType" />" maxlength="30" />
			<span class="spanstar">*</span>
		</td>
	</tr>
	<tr>
		<td width="30%" style="text-align: right" class="listbar">企业类型:</td>
		<td width="70%">
			<input id="diskCustType" name="diskCustType"
			type="text" class="tbl_small_text" value="<s:property value="taxDiskInfo.diskCustType" />" maxlength="3" />
			<span class="spanstar">*</span>
		</td>
	</tr>
	<tr>
		<td width="30%" style="text-align: right" class="listbar">保留信息:</td>
		<td width="70%">
			<input id="retainInfo" name="retainInfo"
			type="text" class="tbl_small_text" value="<s:property value="taxDiskInfo.retainInfo" />" maxlength="16" />
			<span class="spanstar">*</span>
		</td>
	</tr>
	<tr>
		<td width="30%" style="text-align: right" class="listbar">启用时间:</td>
		<td width="70%">
			<input id="enableDt" name="enableDt" type="text"  value="<s:property value='taxDiskInfo.enableDt' />" class="tbl_query_time"	 onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
			<span class="spanstar">*</span>
		</td>
	</tr>
</table>
</div>
<div id="ctrlbutton" class="ctrlbutton" style="border:0px">
<s:if test='paperinvoicestock==null || paperinvoicestock.distributeFlag=="0"'>
	<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="submitForm();" name="BtnSave" value="保存" id="BtnSave"/>
</s:if>
	<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="CloseWindow();" name="BtnReturn" value="关闭" id="BtnReturn"/>
</div>

</form>
</div>
</body>

</html>