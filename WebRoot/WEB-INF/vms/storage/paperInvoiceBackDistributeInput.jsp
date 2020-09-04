<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html> 
<head>
<base target="_self">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../../page/include.jsp"%>
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
<link href="<%=webapp%>/page/subWindow.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script type="text/javascript" src="<%=bopTheme%>/js/validator.js"></script>

<!-- MessageBox -->
<script src="<%=bopTheme%>/js/MessageBox/js/messageBox.js" type="text/javascript"></script>
<link href="<%=bopTheme%>/js/MessageBox/css/messageBox.css" rel="stylesheet" type="text/css" />
<base target="_self">
<title>纸质发票退还</title>
<script language="javascript" type="text/javascript">
//标识页面是否已提交
var subed = false;
function checkForm(){
    //验证是否重复提交
    if (subed == true){
       alert("信息正在发送给服务器，请不要重复提交信息！");
       return false;
    }
//发票代码1是否为空
    if(fucCheckNull(document.getElementById("invoiceCode"),"请输入发票代码")==false){
         return false;
    }else if(fucIsInteger(document.getElementById("invoiceCode"),"发票代码应该为整数")==false){
	   return false;
	}
	else if(fucInvoiceCheckLength(document.getElementById("invoiceCode"),"10","发票代码长度必须是10位")==false){
		return false;
	}
    //发票起始号码1是否为空
   // if(fucCheckNull(document.getElementById("invoiceBeginNo"),"请输入发票起始号码")==false){
       //  return false;
    //}else if(fucIsInteger(document.getElementById("invoiceBeginNo"),"发票起始号码应该为整数")==false){
	//   return false;
	//}else if(fucInvoiceCheckLength(document.getElementById("invoiceBeginNo"),"8","发票起始号码长度必须是8位")==false){
	//	return false;
	//}
    //发票终止号码1是否为空
    if(fucCheckNull(document.getElementById("invoiceEndNo"),"请输入发票终止号码")==false){
         return false;
    }else if(fucIsInteger(document.getElementById("invoiceEndNo"),"发票终止号码应该为整数")==false){
	   return false;
	}else if(fucInvoiceCheckLength(document.getElementById("invoiceEndNo"),"8","发票终止号码长度必须是8位")==false){
		return false;
	}
	if(document.getElementById("invoiceEndNo").value<document.getElementById("invoiceBeginNo").value){
		alert("发票终止号码不能小于发票起始号码");
		document.getElementById("invoiceEndNo").focus();
		return false;
	}
	
	maxBackNum=parseInt(document.getElementById("maxBackNum").value);
	wantBackNum=parseInt(document.getElementById("invoiceEndNo").value)-parseInt(document.getElementById("invoiceBeginNo").value)+1;
	if(wantBackNum>maxBackNum){
			alert("您输入的退还发票信息有误！");
			return false;
	}
    //subed=true;
    return true;
}
function fucInvoiceCheckLength(obj,len,strAlertMsg){
	strTemp=obj.value;
	if(strTemp.length!=len){
	  //字符长度不正确
	   var m = new MessageBox(obj);
	   m.Show(strAlertMsg);	
	   obj.focus();
	   return false;
	}else	{
	   return true;
	}	
}
function submitForm(){
	if(true == checkForm()&&checknum()==true){
		if(checknumbyNum()==false){
			return false;
		}
		document.getElementById("doBackDistributeForm").action = '<c:out value='${webapp}'/>/doBackDistribute.action';
							document.getElementById("doBackDistributeForm").submit();
							subed=true;
		<%--$action= '<c:out value='${webapp}'/>/checkBackReceiver.action';
		$post_data=$("#doBackDistributeForm").serialize();
		ajax_post($action,$post_data,function(json){
			$json_obj=eval('('+json+')');
			if($json_obj.msg=='ok'){
				mayBackNum=$json_obj.mayBackNum;
				if(mayBackNum==0){
					alert("您输入的退还发票信息有误！");
					subed=false;
					return;
				}
				wantBackNum=parseInt(document.getElementById("invoiceEndNo").value)-parseInt(document.getElementById("invoiceBeginNo").value)+1;
				if(wantBackNum>mayBackNum){
					alert("您输入的退还发票信息有误！");
					subed=false;
					return;
				}
				if($json_obj.count==1){
						$obj=$json_obj.data;
						if(confirm("请确认退还人的身份为["+decodeURI($obj.receiveInstId)+"]下的["+decodeURI($obj.receiveUserId)+"]")){
							document.getElementById("doBackDistributeForm").action = '<c:out value='${webapp}'/>/doBackDistribute.action';
							document.getElementById("doBackDistributeForm").submit();
						}else{
							subed=false;
							alert("请您再次确认退还发票信息是否有误！");
							return;
						}
				}else{
					if($json.count>1){
						subed=false;
						alert("您输入的退还发票信息包含多个领用人,请确认您输入的退还发票信息！");
						return;
					}
					if($json.count==0){
						subed=false;
						alert("您输入的退还发票信息有误！");
						return;
					}
				}
			}else{
			subed=false;
				alert("系统发生错误,请您稍后再次尝试!");
				return;
			}
			subed=false;
		});--%>
// 		document.getElementById("doBackDistributeForm").action = '<c:out value='${webapp}'/>/doReceiveDistribute.action';
// 		document.getElementById("doBackDistributeForm").submit();
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
function fucCheckNum(obj, strAlertMsg) {
		strAddress = obj.value;
		strAddress = strAddress.replace(/^(\s)*|(\s)*$/g, "");//去掉字符串两边的空格

		//匹配规则：
		//只允许以字母开头，用a-z,A-Z,0-9以及下划线组成的email名
		//email后面的域名只允许字母或下划线开头,至少一个.,以字母或下划线结束
		var newPar = /^[0-9]*$/;

		var tflag = newPar.test(strAddress);
		var balanceNum =document.getElementById("balanceNum").value;
		//alert(balanceNum);
	
		if (strAddress.length > 0 && tflag == false) {
			var m = new MessageBox(obj);
		m.Show(strAlertMsg);
		//obj.focus();
			return false;
		}else if(parseInt(balanceNum)<parseInt(strAddress)){
			var m = new MessageBox(obj);
			//alert(balanceNum);
			m.Show("退还数量大于领取剩余数量"+balanceNum);
		//	obj.focus();
			return false;
		}
		else {
			return true;
		}
	}


function checknumbyNum(){
	var num=document.getElementById("num").value; 
	var balanceNum =document.getElementById("balanceNum").value;
	if(fucCheckNum(document.getElementById("num"),"数量只能为数字")==false){
		return false;
	}else if(parseInt(balanceNum)<parseInt(num)){
					var m = new MessageBox(document.getElementById("num"));
						m.Show("退还数量大于剩余领用数量"+result.balanceNum);
						return false;
						//document.getElementById("num").focus();
			}else if(fucCheckNull(document.getElementById("num"),"请输入数量")==false){
			return false;
		}else if(fucCheckNum(document.getElementById("num"),"数量只能为数字")==false){
		return false;
	}
	return true;
	
}
function getBillNo(){
	var a=true;
	var people =document.getElementById("people").value;
	var num=document.getElementById("num").value; 
	var paperInvoiceDistributeId=document.getElementById("paperInvoiceDistributeId").value;
	 $.ajax({
                url: 'checkNumByReceverName.action',
                type: 'POST',
                async: false,
                data: {people:people,paperInvoiceDistributeId:paperInvoiceDistributeId},
                dataType: 'json',
                //	timeout: 1000,
                error: function () {
                    return false;
                },
                success: function (result) {
	$("#balanceNum").attr("value",result.balanceNum);
	$("#invoiceBeginNo").attr("value",result.invoiceBeginNo);
	},
            });
	return a;
	
}
function checknum(){
	if(parseInt($("#balanceNum").val())==0){
                		var m = new MessageBox(document.getElementById("people"));
						m.Show("该领取人不存在");
                    return false;
                	}
	return true;
}
	</script>
</head>
<body scroll="no" style="overflow:hidden;">
<div class="showBoxDiv">

<form id="doBackDistributeForm" action="<c:out value='${webapp}'/>/savePaperInvoice.action" method="post">
<div id="editpanel">
<div id="editsubpanel" class="editsubpanel">
<div style="overflow: auto; width: 100%;" >  
<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
	<input type="hidden" name="paperInvoiceRbHistory.paperInvoiceDistributeId" id="paperInvoiceDistributeId" value="<s:property value="paperInvoiceDistribute.paperInvoiceDistributeId" />"/>
	<input type="hidden" name="paperInvoiceRbHistory.paperInvoiceStockId" id="paperInvoiceStockId" value="<s:property value="paperInvoiceDistribute.paperInvoiceStockId" />"/>
	<input type="hidden" name="paperInvoiceRbHistory.receiveInstId" id="paperInvoiceStockId" value="<s:property value="paperInvoiceDistribute.receiveInstId" />"/>
	<input type="hidden" id="maxBackNum" value="<s:property value="paperInvoiceDistribute.hasReceiveNum" />"/> 
	<tr>
		<th colspan="4">纸质发票退还</th>
	</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">发票代码:</td>
		<td width="35%">
			<input type="text" class="tbl_query_text" name="paperInvoiceRbHistory.invoiceCode"  id="invoiceCode"  value="<s:property value="paperInvoiceDistribute.invoiceCode" />" readonly="readonly"/><span class="spanstar">*</span>
		</td>
			<td width="15%" style="text-align: right" class="listbar">发票起始号码:</td>
		<td width="35%">
			<input type="text" class="tbl_query_text bl_query_text_readonly" name="paperInvoiceRbHistory.invoiceBeginNo"  id="invoiceBeginNo"value="<s:property value="paperInvoiceDistribute.invoiceBeginNo" />"  readonly="readonly" style="background:#E6E6E6!important"/><span class="spanstar">*</span>
		</td>				
	</tr>
	<tr>
	<td width="15%" style="text-align: right" class="listbar">退还人:</td>
		<td width="35%">
			<input type="text" class="tbl_query_text" name="paperInvoiceRbHistory.receiveUserId"  id="people" onkeyup="getBillNo()" />
			<span class="spanstar">*</span>
		</td>
		
				<input type="hidden" class="tbl_query_text" name="currentbillNo"  id="currentbillNo"  value="<s:property value="paperInvoiceDistribute.currentbillNo" />"/><span class="spanstar">*</span>
	
	
		<td width="15%" style="text-align: right" class="listbar">发票终止号码:</td>
		<td>
			<input type="text" class="tbl_query_text  bl_query_text_readonly" name="paperInvoiceRbHistory.invoiceEndNo"  id="invoiceEndNo"  value="<s:property value="paperInvoiceDistribute.invoiceEndNo" />" readonly="readonly" style="background:#E6E6E6!important"/><span class="spanstar">*</span>
    	</td>
		
	</tr>
	<tr>
	<td width="15%" style="text-align: right" class="listbar">退换张数:</td>
		<td width="35%">
			<input type="text" class="tbl_query_text"   name="paperInvoiceRbHistory.returnNum"  id="num"  />
			<input type="hidden" class="tbl_query_text"  id="balanceNum" value="<s:property value="paperInvoiceDistribute.balanceNum" />" />
			<span class="spanstar">*</span>
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
		<%
			String retry=request.getParameter("retry");
			if(org.apache.commons.lang.StringUtils.isNotEmpty(retry)){ 
		%>
			alert("领取失败，请您重新再试！");
		<%
			}
		%>
	});
</script>

</html>