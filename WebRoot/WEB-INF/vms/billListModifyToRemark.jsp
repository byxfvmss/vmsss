<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.vms.trans.model.BillInfo"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<title>增值税管理平台</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
	<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
	<script language="javascript" type="text/javascript">
		function save(){
		    var strRemark = document.getElementById("remark").value;
		    var fapiaoType = document.getElementById("fapiaoType").value;
			var lenRemark = 0;
			if (strRemark == '') {
				alert("备注不能为空！");
				return;
			}
			//alert(strRemark);
		    for (var i=0; i<strRemark.length; i++) { 
			    var c = strRemark.charCodeAt(i); 
			    //单字节加1 
			    if ((c >= 0x0001 && c <= 0x007e) || (0xff60<=c && c<=0xff9f)) { 
			    	 lenRemark++; 
			    } else { 
			    	 lenRemark+=2; 
			    } 
		    } 
		    if(lenRemark>138&&fapiaoType=='1'||lenRemark>184&&fapiaoType=='0'){
		    	alert("备注过长！");
		    	return;
		    }
		    var billId = document.getElementById("billId").value;
		    $.ajax({url: 'updateRemark.action',
					type: 'POST',
					async:false,
					data:{remark:strRemark, billId:billId},
					dataType: 'html',
					timeout: 1000,
					error: function(){return false;},
					success: function(result){
						if (result == "success"){
							alert("修改成功！");
							window.close();
							return true;
						} else {
							alert("修改失败:" + result);
							return false;
						}
					}
					});
		}
	</script>
</head>
<body onLoad="javascript:tips()" onmousemove="MM(event)" onmouseout="MO(event)">
	<form name="Form1" method="post" action="updateRemark.action" id="Form1">
	<input type="hidden" id="billId" value="<s:property value='billInfo.billId'/>"/>
	<input type="hidden" id="fapiaoType" value="<s:property value='billInfo.fapiaoType'/>"/>
	<div id="tbl_current_status">
		<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> 
		<span class="current_status_menu">当前位置：</span> 
		<span class="current_status_submenu1">销项税管理</span>
		<span class="current_status_submenu">开票管理</span>
		<span class="current_status_submenu">票据编辑</span>
		<span class="current_status_submenu">修改备注</span>
	</div>
	<div class="centercondition">
		<!-- <div class="blankbox" id="blankbox" style="overflow:auto;height: 100%;" > -->
		<div id="bankbox" style="width: 100%;height: 170px;">
			<table id="contenttable" class="lessGridS"  cellspacing="0" width="100%" align="center" cellpadding="0" style="border-collapse: collapse;">
			<tr>
				<td align="right" width="20%">
					申请开票日期
				</td>
				<td align="left" width="50%" >
					<input type="text" class="tbl_query_text_readonly" name="billInfo.applyDate" id="billInfo.applyDate" value="<s:property value='billInfo.applyDate'/>" size="80" readonly />
				</td>
			</tr>
			<tr>
				<td align="right" width="20%">
					客户纳税人名称
				</td>
				<td align="left" width="50%">
					<input type="text" class="tbl_query_text_readonly" name="billInfo.customerName" id="billInfo.customerName" value="<s:property value='billInfo.customerName'/>" size="80" readonly />
				</td>
			</tr>
			<tr>
				<td align="right" width="20%">
					客户纳税人识别号
				</td>
				<td align="left">
					<input type="text" class="tbl_query_text_readonly" name="billInfo.customerTaxno" id="billInfo.customerTaxno" value="<s:property value='billInfo.customerTaxno'/>" size="80" readonly />
				</td>
			</tr>
			<tr>
				<td align="right" style="vertical-align: middle">
					备注
				</td>
				<td>
					<textarea name="billInfo.remark" id="remark" cols="80" rows="5"><s:property value='billInfo.remark'/></textarea>
				</td>
			</tr>
			</table>
		</div>
		<div class="bottombtn">
			<input type="button" class="tbl_query_button" value="保存"
			onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
			name="btSave" id="btSave" onclick="save()" />
			<input type="button" class="tbl_query_button" value="返回"
			onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
			name="btCancel" id="btCancel" onclick="window.close();" />
		</div>
	</div>
	</form>
<script language="javascript" type="text/javascript" charset="UTF-8">
window.onload = function(){
	var hightValue = screen.availHeight - 270;
	var hightValueStr = "height:"+ hightValue + "px";
	
	
	if (typeof(eval("document.all.blankbox"))!= "undefined"){
		document.getElementById("blankbox").setAttribute("style", hightValueStr);
	}
}
</script>
</body>
</html>