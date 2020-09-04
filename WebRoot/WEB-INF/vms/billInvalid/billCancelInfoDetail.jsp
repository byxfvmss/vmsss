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
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<title>增值税管理平台</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/calendar_notime.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/XmlHttp.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/search.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/ylb.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/lhb.js"></script>
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/search.js"></script>
	<script type="text/javascript">
		var msg = '<s:property value="message"/>';
		if (msg != null && msg != ''){
			alert(msg);
		}
		function closeup(){
			var reqSource = document.Form1.elements['reqSource'].value;
			if(reqSource == "billCancelApply"){
				submitAction(document.forms[0], "listBillCancelApply.action?fromFlag=menu");
			}
			if(reqSource == "billCancelAuditing"){
				submitAction(document.forms[0], "listBillCancelAuditing.action?fromFlag=menu");
			}
		}
	</script>
	<style type="text/css">
		.style1{
			width: 242px;
		}
	</style>
</head>
<body onkeydow="enterkey(event)">
	<form name="Form1" method="post" action="listBillCancelApply.action" id="Form1">
	<%
		String currMonth = (String) request.getAttribute("currMonth");
		String reqSource = (String) request.getAttribute("reqSource");
	%>
	<input type="hidden" name="submitFlag" id="submitFlag" value=""/>
	<input type="hidden" name="currMonth" id="currMonth" value="<%=currMonth%>"/>
	<input type="hidden" name="reqSource" id="reqSource" value="<%=reqSource%>"/>
	<table id="tbl_main" cellpadding=0 cellspacing=0>
	<tr>
		<td align="left">
		<s:component template="rocketMessage" />
		<table id="tbl_current_status">
		<tr>
			<td>
				<img src="<%=bopTheme2%>/img/jes/icon/home.png" /> 
				<span class="current_status_menu">当前位置：</span> 
				<span class="current_status_submenu">销项税管理
					<span class="actionIcon">-&gt;</span>作废发票明细
				</span>
			</td>
		</tr>
		</table>
		</td>
	</tr>
	</table>
	<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0" >
	<tr class="row1">
		<td align="right" width="10%">
			交易时间:&nbsp;
		</td>
		<td align="left">
			<input type="text" name="billCancelInfo.transDate" id="billCancelInfo.transDate" value="<s:property value='billCancelInfo.transDate'/>" onfocus="this.blur();" />
		</td>
		<td align="right" width="10%">
			交易流水号:&nbsp;
		</td>
		<td align="left">
			<input type="text" name="billCancelInfo.transId" id="billCancelInfo.transId" value="<s:property value='billCancelInfo.transId'/>" onfocus="this.blur();" />
		</td>
		<td align="right" width="10%">
			开票日期:&nbsp;
		</td>
		<td align="left">
			<input type="text" name="billCancelInfo.billDate" id="billCancelInfo.billDate" value="<s:property value='billCancelInfo.billDate'/>" onfocus="this.blur();" />
		</td>
		<td align="right" width="10%">
			票据代码:&nbsp;
		</td>
		<td align="left">
			<input type="text" name="billCancelInfo.billCode" id="billCancelInfo.billCode" value="<s:property value='billCancelInfo.billCode'/>" onfocus="this.blur();" />
		</td>
		<td align="right" width="10%">
			票据号码:&nbsp;
		</td>
		<td align="left">
			<input type="text" name="billCancelInfo.billNo" id="billCancelInfo.billNo" value="<s:property value='billCancelInfo.billNo'/>" onfocus="this.blur();" />
		</td>
	</tr>
	<tr class="row1">
		<td align="right" width="10%">
			客户纳税人名称:&nbsp;
		</td>
		<td align="left">
			<input type="text" name="billCancelInfo.customerName" id="billCancelInfo.customerName" value="<s:property value='billCancelInfo.customerName'/>" onfocus="this.blur();" />
		</td>
		<td align="right" width="10%">
			客户纳税人识别号:&nbsp;
		</td>
		<td align="left">
			<input type="text" name="billCancelInfo.customerTaxno" id="billCancelInfo.customerTaxno" value="<s:property value='billCancelInfo.customerTaxno'/>" onfocus="this.blur();" />
		</td>
		<td align="right" width="10%">
			商品名称:&nbsp;
		</td>
		<td align="left">
			<input type="text" name="billCancelInfo.goodsName" id="billCancelInfo.goodsName" value="<s:property value='billCancelInfo.goodsName'/>" onfocus="this.blur();"  />
		</td>
		<td align="right" width="10%">
			规格型号:&nbsp;
		</td>
		<td align="left">
			<input type="text" name="billCancelInfo.specandmodel" id="billCancelInfo.specandmodel" value="<s:property value='billCancelInfo.specandmodel'/>" onfocus="this.blur();"  />
		</td>
		<td align="right" width="10%">
			单位:&nbsp;
		</td>
		<td align="left">
			<input type="text" name="billCancelInfo.goodsUnit" id="billCancelInfo.goodsUnit" value="<s:property value='billCancelInfo.goodsUnit'/>" onfocus="this.blur();"  />
		</td>
	</tr>
	<tr class="row1">
		<td align="right" width="10%">
			商品数量:&nbsp;
		</td>
		<td align="left">
			<input type="text" name="billCancelInfo.goodsNo" id="billCancelInfo.goodsNo" value="<s:property value='billCancelInfo.goodsNo'/>" onfocus="this.blur();" />
		</td>
		<td align="right" width="10%">
			合计金额:&nbsp;
		</td>
		<td align="left">
			<input type="text" name="billCancelInfo.amtSum" id="billCancelInfo.amtSum" value="<s:property value='billCancelInfo.amtSum'/>" onfocus="this.blur();" />
		</td>
		<td align="right" width="10%">
			合计税额:&nbsp;
		</td>
		<td align="left">
			<input type="text" name="billCancelInfo.taxAmtSum" id="billCancelInfo.taxAmtSum" value="<s:property value='billCancelInfo.taxAmtSum'/>" onfocus="this.blur();" />
		</td>
		<td align="right">
			价税合计:&nbsp;
		</td>
		<td>
			<input type="text" name="billCancelInfo.sumAmt" id="billCancelInfo.sumAmt" value="<s:property value='billCancelInfo.sumAmt'/>" onfocus="this.blur();" />
		</td>
		<td align="right">
			折扣率:&nbsp;
		</td>
		<td>
			<input type="text" name="billCancelInfo.discountRate" id="billCancelInfo.discountRate" value="<s:property value='billCancelInfo.discountRate'/>" onfocus="this.blur();" />
		</td>
	</tr>
	<tr class="row1">
		<td align="right">
			发票类型:&nbsp;
		</td>
		<td>
			<select id="billCancelInfo.fapiaoType" name="billCancelInfo.fapiaoType" disabled="disabled">
					<option value="1" <s:if test='billCancelInfo.fapiaoType=="0"'>selected</s:if><s:else></s:else>>专用发票</option>
					<option value="3" <s:if test='billCancelInfo.fapiaoType=="1"'>selected</s:if><s:else></s:else>>普通发票</option></select></td>
		</td>
		<td align="right">
			状态:&nbsp;
		</td>
		<td colspan="7">
			<select id="billCancelInfo.dataStatus" name="billCancelInfo.dataStatus" disabled="disabled">
					<option value="5" <s:if test='billCancelInfo.dataStatus=="5"'>selected</s:if><s:else></s:else>>已开具未上传</option>
					<option value="6" <s:if test='billCancelInfo.dataStatus=="6"'>selected</s:if><s:else></s:else>>已开具已上传</option>
					<option value="8" <s:if test='billCancelInfo.dataStatus=="8"'>selected</s:if><s:else></s:else>>已打印</option>
					<option value="9" <s:if test='billCancelInfo.dataStatus=="9"'>selected</s:if><s:else></s:else>>打印失败</option>
					<option value="19" <s:if test='billCancelInfo.dataStatus=="19"'>selected</s:if><s:else></s:else>>已收回</option>
					<option value="13" <s:if test='billCancelInfo.dataStatus=="13"'>selected</s:if><s:else></s:else>>作废待审核</option></select></td>
		</td>
	</tr>
	</table>
	<div id="ctrlbutton" class="ctrlbutton" style="border:0px">
		<input type="button" class="tbl_query_button" value="关闭"
			onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
			name="btCancel" id="btCancel" onclick="closeup()" />
	</div>
	<!-- 
	<script type="text/javascript">
		var msgHight = document.getElementById("anpBoud").offsetHeight;
		document.getElementById("list1").style.height = screenHeight - 440 - msgHight;
	</script>
	 -->
	</form>
</body>
</html>