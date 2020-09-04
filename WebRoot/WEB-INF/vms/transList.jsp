<!-- file: <%=request.getRequestURI()%> -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<title>增值税管理平台</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/calendar_notime.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/editview.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/XmlHttp.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/search.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/ylb.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/search.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript">
	
		jQuery.extend({checkTransDate:function(){
			//提示逻辑
			return true;
		}});
		
		// [查询]按钮
		function submit(){
			document.forms[0].submit();
		}

		// [开票]按钮
		function transToEachBill(){
			if(checkChkBoxesSelected("selectTransIds")){
				submitAction(document.forms[0], "transToEachBill.action?paginationList.showCount="+"false");
				document.forms[0].action="listTrans.action?paginationList.showCount="+"false";
			}else{
				alert("请选择交易记录！");
			}
		}
		function transtoCustmer(){
			if (!confirm("确定对选中交易进行关联客户吗？")){
				return false;
			}
			var flag = false;
			var count = 1;
			var checkboxes = document.getElementsByName("selectTransIds");
			var selectedIds = "";
			var customerIds = document.getElementsByName("selectCustomers");
			var customerId = "";
			for(var i = 0; i < checkboxes.length; i++){
				var box = checkboxes[i];
				if(box.checked){
					flag = true;
					if(count == 1){
						customerId = customerIds[i].value;
					}
					if(i == checkboxes.length - 1){
						selectedIds = selectedIds + box.value;
					}else{
						selectedIds = selectedIds + box.value + ",";
					}
					if(document.Form1.selectDataStatuses[i].value!='1'){
						alert("所选部分交易已经开票，不能再修改客户信息！");
				 		return false;
					}
					if(document.Form1.selectIncome[i].value!=document.Form1.selectBalance[i].value){
				 		alert("所选部分交易已经做过拆分开票，不能再修改客户信息！");
				 		return false;
				 	}
					if(customerId != customerIds[i].value){
						alert("请选择相同客户所发生的交易");
						return false;
					}
					count = count + 1;
				}
			}
			if(!flag){
				alert("请选择至少一条交易记录");
				return false;
			}
			var newUrl = "connectCustomer.action?transIds="+selectedIds+"&orgCustomerId="+customerId;
			submitAction(document.forms[0],newUrl);
			document.forms[0].action="listTrans.action?paginationList.showCount="+"false";
		}
		
		function checkChkBoxesSelectedOne(chkBoexName){
			var j=0;
			var chkBoexes= document.getElementsByName(chkBoexName);
			for(i=0;i<chkBoexes.length;i++){
				if(chkBoexes[i].checked){
					j++;
				}
			}
			return j;
	}
		
		function transToOneBill(){

			var selectTransIds = "";
			var selectCustomers = "";
			var transIds = document.getElementsByName("selectTransIds");
			var customers = document.getElementsByName("selectCustomers");
			for(var i =0;i<customers.length;i++){
				if(transIds[i].checked){
					selectCustomers += customers[i].value + ",";
				}
			}
			for(var i=0;i<transIds.length;i++){
				if(transIds[i].checked){
					selectTransIds += transIds[i].value + ",";
				}
			}
			$.ajax({url: "selectTransToOneBill.action",
				type: 'POST',
				async:false,
				data:{
					selectTransIds:selectTransIds.substring(0,selectTransIds.length-1),
					selectCustomers:selectCustomers.substring(0,selectCustomers.length-1)
					},
				dataType: 'text',
				error: function(){return false;},
				success: function(result){
					if(result=="error"){
						alert("系统未知异常，请联系管理员");
						return;
					}
					if(result=="N"){
						submitAction(document.forms[0], "transToOneBill.action");
						return;
					}
					if(confirm(result)){
						submitAction(document.forms[0], "transToOneBill.action?paginationList.showCount="+"false");
						document.forms[0].action="listTrans.action?paginationList.showCount="+"false";
					}
				}
			});
		}
		// [合并开票]按钮
		function transToOneBills(){
			if(checkChkBoxesSelected("selectTransIds")){
				var transIds = document.Form1.selectTransIds;
				var customers = document.Form1.selectCustomers;
				var fapiaoTypes = document.Form1.selectFapiaoTypes;
				var customer = "";
				var fapiaoType = "";
				var selectTransIds = "";
				if (!isNaN(transIds.length)){
					for (var i = 0; i < transIds.length; i++){
						if (document.Form1.selectTransIds[i].checked){
								selectTransIds += document.Form1.selectCustomers[i].value+",";
							if (customer == ""){
								customer = document.Form1.selectCustomers[i].value;
							} else if (customer != document.Form1.selectCustomers[i].value){
								alert("所选交易属于不同客户，不能合并开票！");
								return false;
							}
							if (fapiaoType == ""){
								fapiaoType = document.Form1.selectFapiaoTypes[i].value;
							} else if (fapiaoType != document.Form1.selectFapiaoTypes[i].value){
								alert("所选交易之发票类型不一致，不能合并开票！");
								return false;
							}
						}
					}
				}else{
					// 列表中仅一笔可供选交易记录
				}
				/* if (!confirm("确定将选中交易合并开票？")){
					return false;
				}
 				submitAction(document.forms[0], "transToOneBill.action?paginationList.showCount="+"false");
 				document.forms[0].action="listTrans.action?paginationList.showCount="+"false"; */
 				transToOneBill()
			}else{
				alert("请选择交易记录！");
			}
		}
		// [拆分开票]按钮
		function transToManyBill(){
			if(checkChkBoxesSelected("selectTransIds")){
				var transIds = document.Form1.selectTransIds;
				var selectTransId = "";
				if (!isNaN(transIds.length)){
					var selectCount = 0;
					for (var i = 0; i < transIds.length; i++){
						if (document.Form1.selectTransIds[i].checked){
							if (selectCount == 1){
								alert("只可选择一笔交易进行拆分！");
								return false;
							} else {
								selectCount = selectCount + 1;
								selectTransId = document.Form1.selectTransIds[i].value;
							}
						}
					}
				}else{
					selectTransId = document.Form1.selectTransIds.value;
					//判断汇率是否为零
					if(document.Form1.selectTaxRate.value==0.0||document.Form1.selectTaxRate.value==""){
					alert("汇率不存在,不可以拆分");
					return;
					}
				}
				
				/*if(!jQuery.checkTransDate()){
					if (!confirm("此保单仍在犹豫期内，是否确认开票")){
						return false;
					}
				}	*/		
				var newUrl = "splitTrans.action?selectTransIds="+selectTransId;
				OpenModalWindowSubmit(newUrl, 600, 400, true);
				document.forms[0].action="listTrans.action?paginationList.showCount="+"false";
				
			}else{
				alert("请选择交易记录！");
			}
		}
		function OpenModalWindowSubmit(newURL,width,height,needReload){
			var retData = false;
			if(typeof(width) == 'undefined'){
				width = screen.width * 0.9;
			}
			if(typeof(height) == 'undefined'){
				height = screen.height * 0.9;
			}
			if(typeof(needReload) == 'undefined'){
				needReload = false;
			}
			retData = showModalDialog(newURL, 
						window, 
						"dialogWidth:" + width
							+ "px;dialogHeight:" + height
							+ "px;center=1;scroll=1;help=0;status=0;");
			if(needReload && retData){
				window.document.forms[0].submit();
			}
		}
		// 刷新本页面
		function splitTransSuccess(){
			alert("拆分开票保存成功！");
			document.forms[0].submit();
		}
		
		// [交易冲账]按钮
		function transToReverse(){
			if(checkChkBoxesSelected("selectTransIds")){
				var transIds = document.Form1.selectTransIds;
				var customers = document.Form1.selectCustomers;
				if (!isNaN(transIds.length)){
					var customer = "";
					var transId = "";
					for (var i = 0; i < transIds.length; i++){
						if (document.Form1.selectTransIds[i].checked){
							if (customer == ""){
								customer = document.Form1.selectCustomers[i].value;
								transId = document.Form1.selectTransIds[i].value.substring(0,44);
							} else if (customer != document.Form1.selectCustomers[i].value){
								alert("所选交易属于不同客户，不能一同冲账！");
								return false;
							} else {
								var thisTransId = document.Form1.selectTransIds[i].value.substring(0,44);
								if (transId != thisTransId){
									alert("所选交易流水号不通，不能一同冲账！");
									return false;
								}
							}
						}
					}
				}
				if(!confirm("确定对选中交易进行冲账？")){
					return false;
				}
				submitAction(document.forms[0], "transToReverse.action?paginationList.showCount="+"false");
				document.forms[0].action="listTrans.action?paginationList.showCount="+"false";
			}else{
				alert("请选择交易记录！");
			}
		}
		//var msg = '<s:property value="message"/>';
		//if (msg != null && msg != ''){
		//	alert(msg);
		//}
	<%-- <%
		String message = (String)request.getAttribute("message");
		if (message != null && !"".equals(message)){
			message = message.replaceAll("AmtOverFull", "存在交易金额超出单张发票开票金额限额的交易，请做拆分开票。");
			message = message.replaceAll("NotExistsTaxRate", "存在税目信息表中无对应税率信息的交易，不能进行开票。");
			message = message.replaceAll("NotExistsGoods", "存在无对应发票商品的交易信息，不能进行开票。");
			message = message.replaceAll("NotExistsTrans", "不存在对应交易交易信息，不能进行开票。");
			message = message.replaceAll("TransStatusError", "交易信息不是未开票状态，不能进行开票。");
	%>
			alert('<%=message%>');
	<%
		}
	%> --%>
	
		$(function(){
			function showMsg(){
				var message = $("#message").val();
				message = message.replace(/^\s*/, '').replace(/\s*$/, '');
				if("AmtOverFull"==message){
					message = "存在交易金额超出单张发票开票金额限额的交易，请做拆分开票。";
				}
				if("NotExistsTaxRate"==message){
					message = "存在税目信息表中无对应税率信息的交易，不能进行开票。";
				}
				if("NotExistsGoods"==message){
					message = "存在无对应发票商品的交易信息，不能进行开票。";
				}
				if("NotExistsTrans"==message){
					message = "不存在对应交易交易信息，不能进行开票。";
				}
				if("TransStatusError"==message){
					message = "交易信息不是未开票状态，不能进行开票。";
				}
				
				if(null!=message&&''!==message){
					alert(message);
				}
				
			}
			showMsg();
		})
</script>
</head>
<body onkeydow="enterkey(event)">
	<form name="Form1" method="post" action="listTrans.action" id="Form1">
		<s:hidden id="message" value="%{message}"></s:hidden>
		<input name="fromFlag" type="hidden" value="list" />
		<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
			<tr>
				<td class="centercondition">
					<s:component template="rocketMessage" />
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" />
						<span class="current_status_menu">当前位置：</span>
						<span class="current_status_submenu1">销项税管理</span>
						<span class="current_status_submenu">开票管理</span>
						<span class="current_status_submenu">开票申请</span>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" width="100%" border="0">
							<tr>
								<td align="left">交易日期</td>
								<td>
									<input class="tbl_query_time" id="transInfo.transDate" type="text" name="transInfo.transDate" value="<s:property value='transInfo.transDate'/>" onfocus="WdatePicker()" size='11' />
								</td>
								<td align="left">机构</td>
								<td width="130">
									<input type="hidden" id="inst_id" name="transInfo.instCode" value='<s:property value="transInfo.instCode"/>' />
									<input type="text" class="tbl_query_text" id="inst_Name" name="transInfo.instName" value='<s:property value="transInfo.instName"/>' onclick="setOrg(this);" readonly="readonly" />
								</td>
								<td align="left">客户名称</td>
								<td>
									<input type="text" class="tbl_query_text" name="transInfo.customerName" value="<s:property value='transInfo.customerName'/>" maxlength="100" />
								</td>
							</tr>

							<tr>
								<td>交易状态</td>
								<td>
									<s:select id="transInfo.dataStatus" name="transInfo.dataStatus" list="transDataStatusList" headerKey="" headerValue="所有" listKey='value' listValue='text' />
								</td>
								<td align="left">交易种类</td>
								<td>
									<input type="text" class="tbl_query_text" name="transInfo.transTypeName" value="<s:property value='transInfo.transTypeName'/>" maxlength="50" />
								</td>
								<td></td>
								<td colspan="2">
									<input type="button" class="tbl_query_button" value="查询" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" name="btSubmit" id="btSubmit"
										onclick="submit()"
									/>
								</td>
							</tr>
						</table>
					</div>
					<table id="tbl_tools" width="100%" border="0">
						<tr>
							<td align="left">
								<a href="#" onclick="transToEachBill()">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1010.png" />
									开票
								</a>
								<a href="#" onclick="transToOneBills()">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1011.png" />
									合并开票
								</a>
								<a href="#" onclick="transToManyBill()">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1012.png" />
									拆分开票
								</a>
								<a href="#" onclick="transtoCustmer()">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1013.png" />
									关联客户
								</a>
							</td>
						</tr>
					</table>
					<div id="lessGridList4" style="overflow: auto; width: 100%;">
						<table class="lessGrid" cellspacing="0" rules="all" border="0" cellpadding="0" style="border-collapse: collapse; width: 100%;">
							<tr class="lessGrid head">
								<th style="text-align:center">
									<input style="width:13px;height:13px;" id="CheckAll" type="checkbox" onclick="cbxselectall(this,'selectTransIds')" />
								</th>
								<th style="text-align:center">序号</th>
								<th style="text-align:center">交易日期</th>
								<th style="text-align:center">客户名称</th>
								<th style="text-align:center">交易机构</th>
								<th style="text-align:center">客户纳税人类别</th>
								<th style="text-align:center">交易种类</th>
								<th style="text-align:center">交易金额</th>
								<th style="text-align:center">税率</th>
								<th style="text-align:center">税额</th>
								<th style="text-align:center">收入</th>
								<th style="text-align:center">价税合计</th>
								<th style="text-align:center">未开票金额</th>
								<th style="text-align:center">交易状态</th>
								<th style="text-align:center">明细</th>
							</tr>
							<s:iterator value="paginationList.recordList" id="iList" status="stuts">
								<tr align="center" id='<s:property value="transId"/>' class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
									<s:set value="@com.cjit.vms.trans.util.DataUtil@TRANS_STATUS_2" id="status2"></s:set>
									<s:set value="#status2 eq dataStatus?true:false" id="isDataStatus2"></s:set>
									<s:set value="balance==0?true:false" id="isEmptyBalance"></s:set>
									<td align="center">
										<input style="width:13px;height:13px;" class="selectTransIds" type="checkbox" name="selectTransIds" value='<s:property value="transId"/>'
											<s:property value="#isDataStatus2&&#isEmptyBalance?'disabled':''"/>
										/>
										<s:hidden name="selectCustomers" value="%{customerId}"></s:hidden>
										<s:hidden name="selectFapiaoTypes" value="%{fapiaoType}"></s:hidden>
										<s:hidden name="selectDataStatuses" value="%{dataStatus}"></s:hidden>
										<s:hidden name="selectIncome" value="%{income}"></s:hidden>
										<s:hidden name="selectBalance" value="%{balance}"></s:hidden>
										<input name="selectTaxRate" type="hidden" value="<fmt:formatNumber value="${taxRate}" pattern="0.0000"></fmt:formatNumber>">
									</td>
									<s:property value="" />
									<td align="center">
										<s:property value="#stuts.index+1" />
									</td>
									<td align="center" class="transDate">
										<s:property value="transDate" />
									</td>
									<td align="left">
										<s:property value="customerName" />
									</td>
									<td align="left">
										<s:property value="instName" />
									</td>
									<td align="center">
										<s:property value="@com.cjit.vms.trans.util.DataUtil@getTaxpayerTypeCH(customerTaxPayerType)" />
									</td>
									<td align="center">
										<s:property value="transTypeName" />
									</td>
									<td align="right">
										<fmt:formatNumber value="${amt}" pattern="#,##0.00" />
									</td>
									<td align="right">
										<fmt:formatNumber value="${taxRate}" pattern="#,##0.0000" />
									</td>
									<td align="right">
										<fmt:formatNumber value="${taxAmt}" pattern="#,##0.00" />
									</td>
									<td align="right">
										<fmt:formatNumber value="${income}" pattern="#,##0.00" />
									</td>
									<!-- 价税合计=税额+收入 2位 transInfo.getAmt().add(transInfo.getTaxRate())-->
									<td align="right">
										<s:set value="income+taxAmt" id="amtSumPage"></s:set>
										<fmt:formatNumber value="${amtSumPage}" pattern="#,##0.00" />
									</td>
									<td align="right">
										<fmt:formatNumber value="${balance}" pattern="#,##0.00" />
									</td>
									<td>
										<s:set value="reverseTransId == null?dataStatus:6" id="dataStatusPage"></s:set>
										<s:property value="@com.cjit.vms.trans.util.DataUtil@getDataStatusCH(#dataStatusPage,'TRANS')" />
									</td>
									<td align="center">
										<a href="#" onclick="goToPage('transDetail.action?transId=<s:property value="transId"/> ');">
											<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1014.png" title="查看" style="border-width: 0px;" />
										</a>
									</td>
								</tr>
							</s:iterator>
						</table>
					</div>
					<input type="hidden" name="paginationList.showCount" value="false">
					<div id="anpBoud" align="Right" style="overflow:auto;width:100%;">
						<table width="100%" cellspacing="0" border="0">
							<tr>
								<td align="right">
									<s:component template="pagediv" />
								</td>
							</tr>
						</table>
					</div>
				</td>
			</tr>
		</table>
	</form>
	<c:import url="${webapp}/page/webctrl/instTree/tree_include.jsp" charEncoding="UTF-8">
		<c:param name="treeType" value="radio" />
		<c:param name="bankName_tree" value="inst_Name" />
		<c:param name="bankId_tree" value="inst_id" />
		<c:param name="taskId_tree" value="" />
		<c:param name="task_tree" value="task_tree" />
		<c:param name="webapp" value="${webapp}" />
	</c:import>
</body>
</html>
