<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.vms.trans.model.BillInfo"
	import="com.cjit.vms.trans.model.BillItemInfo"
	import="com.cjit.vms.trans.model.TransInfo"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../../page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<s:if test="exchangeRate.exchangeRateId != null && exchangeRate.exchangeRateId != '' ">
		<title>汇率管理--修改汇率</title>
		
		
	</s:if>
	<s:else>
		<title>汇率管理--增加汇率</title>
	</s:else>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/calendar_notime.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/editview.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/XmlHttp.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/vms.js"></script>
	<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
	<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
	<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
	<script type="text/javascript" src="<%=bopTheme%>/js/validator.js"></script>
	<!-- MessageBox -->
	<script src="<%=bopTheme%>/js/MessageBox/js/messageBox.js" type="text/javascript"></script>
	<link href="<%=bopTheme%>/js/MessageBox/css/messageBox.css" rel="stylesheet" type="text/css" />
	<script language="javascript" type="text/javascript">
	
		function check(){
			//验证时间是否为空
			if(fucCheckNull(document.getElementById("exchangeRate.dataDt"),"请输入数据日期")==false){
				return false;
			}
			//判断时间是否合法
			if(fucCheckDateFormat(document.getElementById("exchangeRate.dataDt"),"数据日期格式不合法")==false){
				return false;
			}
			//验证基准币种是否为空
			if(fucCheckNull(document.getElementById("exchangeRate.basicCcy"),"请输入基准币种")==false){
				return false;
			}
			//描述超出指定长度,最大3字节
			if(fucCheckLength(document.getElementById("exchangeRate.basicCcy"),3,"基准币种超出指定长度,最大3字符")==false){
				return false;
			}
			//验证时间是否为空
			if(fucCheckNull(document.getElementById("exchangeRate.ccyDate"),"请输入汇率日期")==false){
				return false;
			}
			//判断时间是否合法
			if(fucCheckDateFormat(document.getElementById("exchangeRate.ccyDate"),"汇率日期格式不合法")==false){
				return false;
			}
			//验证折算币种是否为空
			if(fucCheckNull(document.getElementById("exchangeRate.forwardCcy"),"请输入折算币种")==false){
				return false;
			}
			//描述超出指定长度,最大3字节
			if(fucCheckLength(document.getElementById("exchangeRate.forwardCcy"),3,"折算币种超出指定长度,最大3字符")==false){
				return false;
			}
			//验证折算类型是否为空
			if(fucCheckNull(document.getElementById("exchangeRate.convertTyp"),"请输入折算类型")==false){
				return false;
			}
			//验证汇率是否为空
			if(fucCheckNull(document.getElementById("exchangeRate.ccyRate"),"请输入汇率")==false){
				return false;
			}
			//验证汇率是否为 空 或 非负数
			//if(fucIsFloat(document.getElementById("exchangeRate.ccyRate"),"汇率格式不合法")==false){
			//	return false;
			//}
			return true;
		}
		
		function findOutSubmit() {
			if(check()) {
				var method = document.getElementById("method").value;
				var exchangeRateId = document.getElementById("exchangeRateId").value;
				var dataDt = document.getElementById("exchangeRate.dataDt").value;
				var basicCcy = document.getElementById("exchangeRate.basicCcy").value;
				var ccyDate = document.getElementById("exchangeRate.ccyDate").value;
				var forwardCcy = document.getElementById("exchangeRate.forwardCcy").value;
				var convertTyp = document.getElementById("exchangeRate.convertTyp").value;
				var ccyRate = document.getElementById("exchangeRate.ccyRate").value;
				$.ajax({url: 'editExchangeRate.action',
						type: 'POST',
						async:false,
						data:{dataDt:dataDt, basicCcy:basicCcy, ccyDate:ccyDate, forwardCcy:forwardCcy,
					          convertTyp:convertTyp, ccyRate:ccyRate,exchangeRateId:exchangeRateId,method:method},
						dataType: 'html',
						timeout: 1000,
						error: function(){return false;},
						success: function(result){
							msg = result;
						}
						});
				if (msg == "wrongRate"){
					alert("汇率格式不合法");
					return false;
				} else if (msg == "afterNow"){
					alert("汇率日期应该不能晚于系统日期。");
					return false;
				} else if (msg == "repeatRate"){
					alert("该汇率已存在。");
					return false;
				} else if (msg == "repeatCcy"){
					alert("基准币种与折算币种相同，汇率应该为1，请确认。");
					return false;
				} else if (msg == "addSuccess"){
					alert("汇率新增成功。");
					window.dialogArguments.saveExchangeSuccess();
					window.close();
					return true;
				} else if (msg == "editSuccess"){
					alert("汇率修改成功。");
					window.dialogArguments.saveExchangeSuccess();
					window.close();
					return true;
				} else if (msg == "addError"){
					alert("汇率新增失败。");
					return false;
				} else  if (msg == "editError"){
					alert("汇率修改失败。");
					return false;
				} 
				return false;
			}
		}
		
		var msg = '<s:property value="message" escape="false"/>';
		if (msg != null && msg != ''){
			alert(msg);
			if (msg == '汇率新增成功。' || msg == '汇率修改成功。') {
				window.opener.location.reload();
				window.close();
			} 
		}
	</script>
</head>
<body  onmousemove="MM(event)" onmouseout="MO(event)" scroll="no" style="overflow:hidden;">
	<form name="Form1" action="editExchangeRate.action" method="post"  id="Form1">
	<input type="hidden" id="method" value="<%=request.getAttribute("method") %>"/>
	<input type="hidden" id="exchangeRateId" value="<s:property value='exchangeRate.exchangeRateId'/>"/>
	<div class="windowtitle" style="background:#004C7E; height:45px; line-height:45px; text-align:center; color:#FFF; font-size:14px; font-weight:bold;">
		<s:if test="exchangeRate.exchangeRateId != null && exchangeRate.exchangeRateId != '' ">
			汇率管理--修改汇率
		</s:if>
		<s:else>
			汇率管理--增加汇率
		</s:else>
	</div>
	<table id="tbl_context" cellspacing="0" width="100%" align="center" cellpadding="0" >
	<tr class="row1">
		<td width="15%" align="right"  style="background-color:#F0F0F0;font-weight:bold;color:#727375;"	>数据日期:&nbsp;&nbsp;&nbsp;</td>
		<td width="35%">
			<input class="tbl_query_time" type="text" name="exchangeRate.dataDt" id="exchangeRate.dataDt" value="<s:property value='exchangeRate.dataDt'/>"	onfocus="WdatePicker()"	size='20'" <s:if test="exchangeRate.exchangeRateId != null && exchangeRate.exchangeRateId != '' "> disabled="disabled" </s:if> />
			&nbsp;<span style="color:red;">*</span>
		</td>
	</tr>
	<tr class="row1">
		<td width="15%" align="right"  style="background-color:#F0F0F0;font-weight:bold;color:#727375;"	>基准币种:&nbsp;&nbsp;&nbsp;</td>
		<td width="35%" <s:if test="exchangeRate.exchangeRateId != null && exchangeRate.exchangeRateId != '' "> disabled="disabled" </s:if> >
			<s:select id="exchangeRate.basicCcy" name="exchangeRate.basicCcy" style="width:155px" value="exchangeRate.basicCcy"
					list="currencyList" listKey='value' listValue='text' />
			<span style="color:red;">*</span>
		</td>
	</tr>
	<tr class="row1">
		<td width="15%" align="right"  style="background-color:#F0F0F0;font-weight:bold;color:#727375;"	>汇率日期:&nbsp;&nbsp;&nbsp;</td>
		<td width="35%" <s:if test="exchangeRate.exchangeRateId != null && exchangeRate.exchangeRateId != '' "> disabled="disabled" </s:if> >
			<input class="tbl_query_time" type="text" name="exchangeRate.ccyDate" id="exchangeRate.ccyDate" value="<s:property value='exchangeRate.ccyDate'/>"	onfocus="WdatePicker()"	size='20'" w/> 
			&nbsp;<span style="color:red;">*</span>
		</td>
	</tr>
	<tr class="row1">
		<td width="15%" align="right"  style="background-color:#F0F0F0;font-weight:bold;color:#727375;"	>折算币种:&nbsp;&nbsp;&nbsp;</td>
		<td width="35%" <s:if test="exchangeRate.exchangeRateId != null && exchangeRate.exchangeRateId != '' "> disabled="disabled" </s:if> >
			<s:select id="exchangeRate.forwardCcy" name="exchangeRate.forwardCcy"  style="width:155px" value="exchangeRate.forwardCcy"
					list="currencyList" listKey='value' listValue='text'/>
			<span style="color:red;">*</span>
		</td>
	</tr>
	<tr class="row1">
		<td width="15%" align="right"  style="background-color:#F0F0F0;font-weight:bold;color:#727375;"	>折算类型:&nbsp;&nbsp;&nbsp;
		<td width="35%" <s:if test="exchangeRate.exchangeRateId != null && exchangeRate.exchangeRateId != '' "> disabled="disabled" </s:if> >
			<select name="exchangeRate.convertTyp" style="width:155px">
					<option value="M" <s:if test='exchangeRate.convertTyp=="M"'>selected</s:if><s:else></s:else>>M:乘</option>
					<option value="D" <s:if test='exchangeRate.convertTyp=="D"'>selected</s:if><s:else></s:else>>D:除</option>
				</select>
			&nbsp;<span style="color:red;">*</span>
		</td>
	</tr>
	<tr class="row1">
		<td width="15%" align="right"  style="background-color:#F0F0F0;font-weight:bold;color:#727375;"	>汇率:&nbsp;&nbsp;&nbsp;</td>
		<td width="35%">
			<input type="text" class="tbl_query_text" name="ccyRate" id="exchangeRate.ccyRate" value="<s:property value='exchangeRate.ccyRate'/>" maxlength="19" />
			<span style="color:red;">*</span>
		</td>
	</tr>
	</table>
	<div id="ctrlbutton" class="ctrlbutton" style="border:0px; padding-top: 10px" align="center">
		<input type="button" class="tbl_query_button" value="保存"
			onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
			name="btSave" id="btSave" onclick="findOutSubmit()" />
		<input type="button" class="tbl_query_button" value="关闭"
			onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
			name="btCancel" id="btCancel" onclick="window.close()" />
	</div>
	</form>
</body>
<script type="text/javascript">
	document.getElementById("exchangeRate.ccyRate").focus();
</script>
</html>