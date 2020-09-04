<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html> 
<head>
<base target="_self">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../page/include.jsp"%>
<script type="text/javascript" src="<%= webapp%>/page/js/jquery/jquery_1.42.js"></script>
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
<link href="<%=webapp%>/page/subWindow.css" type="text/css" rel="stylesheet">
<link href="<c:out value="${sysTheme}"/>/css/subWindow.css" type="text/css" rel="stylesheet">
<title><s:if test="operType=='add' ">新增空白发票批次</s:if><s:elseif test="operType=='edit'">编辑空白发票批次</s:elseif></title>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script type="text/javascript" src="<%=bopTheme%>/js/validator.js"></script>
<!-- MessageBox -->
<script src="<%=bopTheme%>/js/MessageBox/js/messageBox.js" type="text/javascript"></script>
<link href="<%=bopTheme%>/js/MessageBox/css/messageBox.css" rel="stylesheet" type="text/css" />
	
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
    
    //机构是否为空
    if(fucCheckNull(document.getElementById("instId"),"请选择机构")==false){
       return false;
    }

  	//领取日期是否为空
    if(fucCheckNull(document.getElementById("receiveInvoiceTime"),"请选择领取日期")==false){
       return false;
    }
    
    //领取人员是否为空
    if(fucCheckNull(document.getElementById("userId"),"请输入领取人员")==false){
       return false;
    }
    //发票类型是否为空
    if(fucCheckNull(document.getElementById("invoiceType"),"请选择发票类型")==false){
         return false;
    }
    /* //最高金额是否为空
    if(fucCheckNull(document.getElementById("maxMoney"),"请输入单张发票最高金额")==false){
         return false;
    }else if(fucIsFloat(document.getElementById("maxMoney"),"单张发票最高金额应该为整数")==false){
	   return false;
	} */
    //发票代码1是否为空
    if(fucCheckNull(document.getElementById("invoiceCode1"),"请输入发票代码1")==false){
         return false;
    }else if(fucIsInteger(document.getElementById("invoiceCode1"),"发票代码1应该为整数")==false){
	   return false;
	}else if(fucInvoiceCheckLength(document.getElementById("invoiceCode1"),"10","发票代码1长度必须是10位")==false){
		return false;
	}
    //发票起始号码1是否为空
    if(fucCheckNull(document.getElementById("invoiceBeginNo1"),"请输入发票起始号码1")==false){
         return false;
    }else if(fucIsInteger(document.getElementById("invoiceBeginNo1"),"发票起始号码1应该为整数")==false){
	   return false;
	}else if(fucInvoiceCheckLength(document.getElementById("invoiceBeginNo1"),"8","发票起始号码1长度必须是8位")==false){
		return false;
	}
    //发票终止号码1是否为空
    if(fucCheckNull(document.getElementById("invoiceEndNo1"),"请输入发票终止号码1")==false){
         return false;
    }else if(fucIsInteger(document.getElementById("invoiceEndNo1"),"发票终止号码1应该为整数")==false){
	   return false;
	}else if(fucInvoiceCheckLength(document.getElementById("invoiceEndNo1"),"8","发票终止号码1长度必须是8位")==false){
		return false;
	}
	if(document.getElementById("invoiceEndNo1").value<document.getElementById("invoiceBeginNo1").value){
		alert("发票终止号码1不能小于发票起始号码1");
		document.getElementById("invoiceEndNo1").focus();
		return false;
	}
	// 判断代码1和代码号码是否已经存在
	if(checkInvoiceCode(document.getElementById("invoiceCode1").value,document.getElementById("invoiceBeginNo1").value,document.getElementById("invoiceEndNo1").value)==false){
		alert("发票代码1中的发票号码段包含已存在的发票号码，请更正");
		document.getElementById("invoiceBeginNo1").focus();
		return false;
	}
	
	//发票代码2组合check
	var invoiceCode2_v = document.getElementById("invoiceCode2").value;
	var invoiceBeginNo2_v = document.getElementById("invoiceBeginNo2").value;
	var invoiceEndNo2_v = document.getElementById("invoiceEndNo2").value;
	if(invoiceCode2_v!="" || invoiceBeginNo2_v!="" || invoiceEndNo2_v!=""){
		if(invoiceCode2_v==document.getElementById("invoiceCode1").value){
			alert("发票代码1和发票代码2不能相同，请重新输入");
			return false;
		}
		//发票代码2是否为空
	    if(fucCheckNull(document.getElementById("invoiceCode2"),"请输入发票代码2")==false){
	         return false;
	    }else if(fucIsInteger(document.getElementById("invoiceCode2"),"发票代码2应该为整数")==false){
		   return false;
		}else if(fucInvoiceCheckLength(document.getElementById("invoiceCode2"),"10","发票代码2长度必须是10位")==false){
			return false;
		}
	    //发票起始号码2是否为空
	    if(fucCheckNull(document.getElementById("invoiceBeginNo2"),"请输入发票起始号码2")==false){
	         return false;
	    }else if(fucIsInteger(document.getElementById("invoiceBeginNo2"),"发票起始号码2应该为整数")==false){
		   return false;
		}else if(fucInvoiceCheckLength(document.getElementById("invoiceBeginNo2"),"8","发票起始号码2长度必须是8位")==false){
			return false;
		}
	    //发票终止号码2是否为空
	    if(fucCheckNull(document.getElementById("invoiceEndNo2"),"请输入发票终止号码2")==false){
	         return false;
	    }else if(fucIsInteger(document.getElementById("invoiceEndNo2"),"发票终止号码2应该为整数")==false){
		   return false;
		}else if(fucInvoiceCheckLength(document.getElementById("invoiceEndNo2"),"8","发票终止号码2长度必须是8位")==false){
			return false;
		}
		if(document.getElementById("invoiceEndNo2").value<document.getElementById("invoiceBeginNo2").value){
			alert("发票终止号码2不能小于发票起始号码2");
			document.getElementById("invoiceEndNo2").focus();
			return false;
		}
		// 判断代码1和代码号码是否已经存在
		if(checkInvoiceCode(document.getElementById("invoiceCode2").value,document.getElementById("invoiceBeginNo2").value,document.getElementById("invoiceEndNo2").value)==false){
			alert("发票代码2中的发票号码段包含已存在的发票号码，请更正");
			document.getElementById("invoiceBeginNo2").focus();
			return false;
		}
	}
	
    subed=true;
    return true;
}

function submitForm(){
	if(true == checkForm()){
		document.forms[0].action = "<%=webapp%>/savePaperInvoice.action";
		document.forms[0].submit();
	}
}
function checkInvoiceCode(invoiceCode,beginNo,endNo){
	var resultDate=true;
 $.ajax({
		url: "<%=webapp%>/CountPaperInvoiceCode.action",
		type: 'POST',
		async:false,
		data:{ 
			   paperInvoiceStockId:$("#paperInvoiceStockId").val(),
			   invoiceCode:invoiceCode,
			   beginNo:beginNo,
			   endNo:endNo		   
			   },
		dataType : 'text',
		error: function(){return false;},
		success: function(result){
			if (result=="N"){
				resultDate= false;
			}
			if(result=="Y"){
				resultDate= true;
				}
		}
	}); 
	return resultDate;
}
function fucInvoiceCheckLength(obj,len,strAlertMsg)
{
	strTemp=obj.value;
	if(strTemp.length!=len)
	{
	  //字符长度不正确
	   var m = new MessageBox(obj);
	   m.Show(strAlertMsg);	
	   obj.focus();
	   return false;
	}
	else
	{
	   return true;
	}	
}
</script>
</head>
<body scroll="no" style="overflow:hidden;">
<div class="showBoxDiv">

<form id="frm" action="<c:out value='${webapp}'/>/savePaperInvoice.action" method="post">
<div id="editsubpanel" class="editsubpanel">
<div style="overflow: auto; width: 100%;" >  
	<input type="hidden" name="operType" value="<s:property value="operType" />" />
	<input type="hidden" id="paperInvoiceStockId" name="paperInvoiceStockId" value="<s:property value="paperinvoicestock.paperInvoiceStockId" />" /> 

<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
	<tr>
		<th colspan="4"><s:if test="operType=='add' ">新增空白发票批次</s:if><s:elseif
			test="operType=='edit'">编辑空白发票批次</s:elseif></th>
	</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">机构:</td>
		<td width="35%">
				<s:if test="authInstList != null && authInstList.size > 0">
				<s:if test='paperinvoicestock.distributeFlag!="0"'>
				<s:select name="instId" list="authInstList" listKey='id' listValue='name' headerKey="" headerValue="请选择" disabled="true"/>
				</s:if>
				<s:else>
				<s:select name="instId" list="authInstList" listKey='id' listValue='name' headerKey="" headerValue="请选择"/>
				</s:else>
				</s:if>
				<s:if test="authInstList == null || authInstList.size == 0">
				<select name="instId" class="readOnlyText">
				<option value="">请分配机构权限</option>
				</select>
				</s:if>
			<span class="spanstar">*</span></td>
		<td width="15%" style="text-align: right" class="listbar"></td>
		<td colspan="2"></td>
	</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">领取日期:</td>
		<td width="35%"><input id="receiveInvoiceTime" name="receiveInvoiceTime" type="text" readonly="true" value="<s:property value="paperinvoicestock.receiveInvoiceTime" />" class="tbl_query_time" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:155px;" <s:if test='paperinvoicestock.distributeFlag!="0"'>disabled="disabled"</s:if>><span class="spanstar">*</span></td>
		<td width="15%" style="text-align: right" class="listbar">领购人员:</td>
		<td><input id="userId" name="userId" type="text" class="tbl_query_text" value="<s:property value="paperinvoicestock.userId" />"  maxlength="20" <s:if test='paperinvoicestock.distributeFlag!="0"'>disabled="disabled"</s:if>/><span class="spanstar">*</span></td>
	</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">发票类型:</td>
		<td width="35%">
			<select id="invoiceType" name="invoiceType" <s:if test='paperinvoicestock.distributeFlag!="0"'>disabled="true"</s:if>>
				<s:iterator value="mapVatType" id="entry">  
				<option value="<s:property value="key"/>" <s:if test='paperinvoicestock.invoiceType==#entry.key'>selected</s:if><s:else></s:else>><s:property value="value"/></option>
				</s:iterator>
			</select>
		<!--  input id="invoiceType"
			name="paperinvoicestock.invoiceType" type="text"
			value="<s:property value="paperinvoicestock.invoiceType" />" /> --><span class="spanstar">*</span></td>
		 <td width="15%" style="text-align: right" class="listbar"></td>
		<%-- <td><input id="maxMoney" name="maxMoney"
			type="text" class="tbl_query_text" value="<s:property value="paperinvoicestock.maxMoney" />"  
			onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" 
			maxlength="14" <s:if test='paperinvoicestock.distributeFlag!="0"'>disabled="disabled"</s:if>/><span class="spanstar">*</span></td>
 --%>	</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">发票代码1:</td>
		<td width="35%"><input id="invoiceCode1"
			name="invoiceCode1" type="text" class="tbl_query_text" 
			value="<s:property value="paperinvoicestockdetail1.invoiceCode" />" 
			onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" 
			maxlength="10" <s:if test='paperinvoicestock.distributeFlag!="0"'>disabled="disabled"</s:if>/><span class="spanstar">*</span></td>
		<td width="15%" style="text-align: right" class="listbar"></td>
		<td></td>
	</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">发票起始号码1:</td>
		<td width="35%"><input id="invoiceBeginNo1"
			name="invoiceBeginNo1" type="text" class="tbl_query_text" 
			value="<s:property value="paperinvoicestockdetail1.invoiceBeginNo" />" 
			onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" 
			maxlength="8" <s:if test='paperinvoicestock.distributeFlag!="0"'>disabled="disabled"</s:if>/><span class="spanstar">*</span></td>
		<td width="15%" style="text-align: right" class="listbar">发票终止号码1:</td>
		<td><input id="invoiceEndNo1" name="invoiceEndNo1"
			type="text" class="tbl_query_text" value="<s:property value="paperinvoicestockdetail1.invoiceEndNo" />" 
			onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" 
			maxlength="8" <s:if test='paperinvoicestock.distributeFlag!="0"'>disabled="disabled"</s:if>/><span class="spanstar">*</span></td>
	</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">发票代码2:</td>
		<td width="35%"><input id="invoiceCode2"
			name="invoiceCode2" type="text" class="tbl_query_text" 
			value="<s:property value="paperinvoicestockdetail2.invoiceCode" />" 
			onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" 
			maxlength="10" <s:if test='paperinvoicestock.distributeFlag!="0"'>disabled="disabled"</s:if>/></td>
		<td width="15%" style="text-align: right" class="listbar"></td>
		<td></td>
	</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">发票起始号码2:</td>
		<td width="35%"><input id="invoiceBeginNo2"
			name="invoiceBeginNo2" type="text" class="tbl_query_text" 
			value="<s:property value="paperinvoicestockdetail2.invoiceBeginNo" />" 
			onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" 
			maxlength="8" <s:if test='paperinvoicestock.distributeFlag!="0"'>disabled="disabled"</s:if>/></td>
		<td width="15%" style="text-align: right" class="listbar">发票终止号码2:</td>
		<td><input id="invoiceEndNo2" name="invoiceEndNo2"
			type="text" class="tbl_query_text"  value="<s:property value="paperinvoicestockdetail2.invoiceEndNo" />" 
			onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" 
			maxlength="8" <s:if test='paperinvoicestock.distributeFlag!="0"'>disabled="disabled"</s:if>/></td>
	</tr>
</table>
</div>
<div id="ctrlbutton" class="ctrlbutton" style="border:0px">
<s:if test='paperinvoicestock==null || paperinvoicestock.distributeFlag=="0"'>
	<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="submitForm();" name="BtnSave" value="保存" id="BtnSave"/>
</s:if>
	<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="CloseWindow();" name="BtnReturn" value="关闭" id="BtnReturn"/>
</div>
</div>
</form>
</div>
</body>

</html>