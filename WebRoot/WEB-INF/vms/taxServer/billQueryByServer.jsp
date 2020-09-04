<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.vms.trans.model.BillInfo"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../../page/include.jsp"%>
<OBJECT ID=sk CLASSID="clsid:003BD8F2-A6C3-48EF-9B72-ECFD8FC4D49F" codebase="NISEC_SKSCX.ocx#version=1,0,0,1" style="display: none"></OBJECT>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<title>增值税管理平台</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/calendar_notime.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/XmlHttp.js"></script>
	<!-- <script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/search.js"></script> -->
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/ylb.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/lhb.js"></script>
	<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/search.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/taxSelver/validatorTax.js"></script>
	<script type="text/javascript">
		// [查询]按钮
		function query(){
			//checkDiskAndSelver("",billQueryByServer);
			billQueryByServer();
		}
		// 作废按钮
		function billQueryByServer(){
			var queryType = document.getElementById("queryType").value;
			var fapiaoType = document.getElementById("fapiaoType").value;
			var billBeginDate = document.getElementById("billBeginDate").value;
			var billEndDate = document.getElementById("billEndDate").value;
			var billCode = document.getElementById("billCode").value;
			var billBeginNo = document.getElementById("billBeginNo").value;
			var billEndNo = document.getElementById("billEndNo").value;
			if (queryType == '1') {
				if (billBeginDate == '') {
					alert("开票起始日期不能为空！");
					return false;
				}
				if (billEndDate == '') {
					alert("开票结束日期不能为空！");
					return false;
				}
			} else {
				if (billCode == '') {
					alert("发票代码不能为空！");
					return false;
				}
				if (billBeginNo == '') {
					alert("发票起始号码不能为空！");
					return false;
				}
				if (billEndNo == '') {
					alert("发票结束号码不能为空！");
					return false;
				}
			}
			$.ajax({url: 'billQueryByServer.action',
						type: 'POST',
						async:false,
						data:{queryType:queryType,fapiaoType:fapiaoType,billBeginDate:billBeginDate,billEndDate:billEndDate,
								billCode:billCode,billBeginNo:billBeginNo,billEndNo:billEndNo},
						dataType: 'text',
						timeout: 1000,
						error: function(){return false;},
						success: function(result){
							alert(result);
							//var ret = sk.Operate(result);
							var ret = "";
							parseBillQueryServerReturnXml(ret,fapiaoType);
						}
					});
			document.forms[0].action="billQueryByTax.action";
			document.forms[0].submit();
				
		}
		
		function parseBillQueryServerReturnXml(ret, fapiaoType) {
			$.ajax({url: 'parseBillQueryServerReturnXml.action',
					type: 'POST',
					async:false,
					data:{ret:ret, fapiaoType:fapiaoType},
					dataType: 'text',
					timeout: 1000,
					error: function(){return false;},
					success: function(result){
						//alert(result);
					}
				});
		}
		
		//更换查询条件
		function changeQuery() {
			var queryType = document.getElementById("queryType").value;
			if (queryType == '1') {
				$(".date").show();
				$(".bill").hide();
			} else {
				$(".date").hide();
				$(".bill").show();
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
	<form name="Form1" method="post" action="" id="Form1">
		<input type="hidden" name="taxParam" id="taxParam" value="<s:property value='taxParam'/>"/>
	
	<%
		String currMonth = (String) request.getAttribute("currMonth");
	%>
	<input type="hidden" name="submitFlag" id="submitFlag" value=""/>
	<input type="hidden" name="currMonth" id="currMonth" value="<%=currMonth%>"/>
	<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
	<tr>
		<td class="centercondition">
		<s:component template="rocketMessage" />
		<div id="tbl_current_status">
			<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> 
			<span class="current_status_menu">当前位置：</span> 
			<span class="current_status_submenu1">销项税管理</span>
			<span class="current_status_submenu">综合查询</span>
			<span class="current_status_submenu">票据查询(税)</span>
		</div>
		<div class="widthauto1">
		<table id="tbl_query" cellpadding="0" cellspacing="0" width="80%" border="0">
		<tr>
			<td align="left">
				查询方式
			</td>
			<td><select id="queryType" onchange="changeQuery()">
					<option value="0" <s:if test='queryType=="0"'>selected</s:if><s:else></s:else>>按发票号码段查询</option>
					<option value="1" <s:if test='queryType=="1"'>selected</s:if><s:else></s:else>>按时间段查询</option>
				</select>
			</td>
			<td align="left" style="display: none;" class="date">
				开票日期
			</td>
			<td style="display: none;" class="date">
				<input class="tbl_query_time" id="billBeginDate" type="text" name="billBeginDate"
					value="<s:property value='billBeginDate'/>"
					onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'billEndDate\')}'})"
					size='11'"/>
				--
				<input class="tbl_query_time" id="billEndDate" type="text" name="billEndDate"
					value="<s:property value='billEndDate'/>"
					onfocus="WdatePicker({minDate:'#F{$dp.$D(\'billBeginDate\')}'})"
					size='11'"/>
			</td>
			<td style="display: none;" class="date">
			</td>
			<td style="display: none;" class="date">
			</td>
			<td class="bill">
				发票代码
			</td>
			<td class="bill">
				<input type="text" class="tbl_query_text" id="billCode" name="billCode" value="<s:property value='billCode'/>" size="38"/>
			</td>
			<td align="left" class="bill">
				发票号码
			</td>
			<td class="bill">
				<input type="text" class="tbl_query_text" name="billBeginNo" value="<s:property value='billBeginNo'/>" size="38"/>
				--
				<input type="text" class="tbl_query_text" name="billEndNo" value="<s:property value='billEndNo'/>" size="38"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				发票类型
			</td>
			<td><select id="fapiaoType" name="fapiaoType" >
					<option value="0" <s:if test='fapiaoType=="0"'>selected</s:if><s:else></s:else>>增值税专用发票</option>
					<option value="1" <s:if test='fapiaoType=="1"'>selected</s:if><s:else></s:else>>增值税普通发票</option>
				</select>
			</td>
			
			<td colspan="2">
				<input type="button" class="tbl_query_button" value="查询"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="BtnView" id="BtnView" onclick="query()" />
			</td>
		</tr>	
		</table>
		</div>
  	<div id="lessGridList4" style="overflow: auto; width: 100%;">
		<table class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0" style="border-collapse: collapse; width: 100%;">
	<tr class="lessGrid head">
		<th style="text-align:center">序号</th>
		<th style="text-align:center">开票日期</th>
		<th style="text-align:center">发票代码</th>
		<th style="text-align:center">发票号码</th>
		<th style="text-align:center">客户纳税人名称</th>
		<th style="text-align:center">客户纳税人识别号</th>
		<th style="text-align:center">合计金额</th>
		<th style="text-align:center">合计税额</th>
		<th style="text-align:center">价税合计</th>
		<th style="text-align:center">发票类型</th>
		<th style="text-align:center">状态</th>
	</tr>
	<%
		List billList = (List) request.getAttribute("billList");
		if (billList != null){
			if (billList != null && billList.size() > 0){
				for(int i=0; i<billList.size(); i++){
					System.out.println("----"+i);
					BillInfo bill = (BillInfo)billList.get(i);
					if(i%2==0){
						System.out.println(bill.getBillDate());	
	%>
	<tr class="lessGrid rowA">
	<%
					}else{
	%>
	<tr class="lessGrid rowB">
	<%
					}
	%>
		<td align="center"><%=i+1%></td>
		<td align="center"><%=bill.getBillDate()%></td>
		<td align="center"><%=bill.getBillCode()%></td>
		<td align="center"><%=bill.getBillNo()%></td>
		<td align="center"><%=bill.getCustomerName()%></td>
		<td align="center"><%=bill.getCustomerTaxno()%></td>
		<td align="center"><%=bill.getAmtSum()%></td>
		<td align="center"><%=bill.getTaxAmtSum()%></td>
		<td align="center"><%=bill.getSumAmt()%></td>
		<td align="center"><%=bill.getFapiaoType()%></td>
		<td align="center"><%=bill.getDataStatus()%></td>
	</tr>
	<%
				}
			}
		}
	%>
	</table>
	</div>
		</td>
	</tr>
	</table>
	</form>
</body>
</html>