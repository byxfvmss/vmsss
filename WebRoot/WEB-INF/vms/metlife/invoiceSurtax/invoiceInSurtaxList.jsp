<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../../../page/include.jsp"%>
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/ylb.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/editview.js"></script>
<meta http-equiv="Pragma" content="no-cache" />
<title>进项税转出比例金额</title>
<style type="text/css">
.detailinputInvoiceInfoDIV {
	border: 1px solid green;
	background-color: khaki;
	top: 110px;
	left: 150px;
	width: 450px;
	height: 300px;
	position: absolute;
	z-index: 2;
	filter: alpha(opacity =     90);
	-moz-opacity: 0.9;
	display: none;
}

.ellipsis_div {
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
}
</style>
</head>
<body>
	<form id="main" action="listInvoiceInSurtax.action" method="post">
		<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
			<tr>
				<td class="centercondition">
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" />
						<span class="current_status_menu">当前位置：</span>
						<span class="current_status_submenu1">进项税管理</span>
						<span class="current_status_submenu">认证管理</span>
						<span class="current_status_submenu">进项转出</span>
					</div>
					<input hidden id="expenseDocnuma" name="expenseDocnuma">
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td>发票代码</td>
								<td>
									<input id="billCode" name="inputInvoiceInfo.billCode" type="text" class="tbl_query_text" value="<s:property value='inputInvoiceInfo.billCode'/>" maxlength="10" onkeypress="checkkey(value);" />
								</td>
								<td>发票号码</td>
								<td>
									<input type="text" class="tbl_query_text" name="inputInvoiceInfo.billNo" id="billNo" value="<s:property value='inputInvoiceInfo.billNo'/>" maxlength="8" onkeypress="checkkey(value);" />
								</td>
								<td>转出标识:&nbsp;&nbsp;&nbsp;</td> 
								<td>
								<select style="width:150px;"  name="inputInvoiceInfo.datastatus">
										<option value="15" <s:if test='inputInvoiceInfo.datastatus=="15"'>selected</s:if> <s:else></s:else>>未转出</option>
										<option value="1" <s:if test='inputInvoiceInfo.datastatus=="1"'>selected</s:if> <s:else></s:else>>已转出</option>
										
									</select>
								</td>
								<td>标的物</td>
								<td>
									<select style="width:150px;"  name="inputInvoiceInfo.subjectType">
										<option value="0" <s:if test='inputInvoiceInfo.subjectType=="0"'>selected</s:if> <s:else></s:else>>正常采购</option>
										<option value="1" <s:if test='inputInvoiceInfo.subjectType=="1"'>selected</s:if> <s:else></s:else>>固定资产</option>
										<option value="2" <s:if test='inputInvoiceInfo.subjectType=="2"'>selected</s:if> <s:else></s:else>>视同销售</option>
										<option value="3" <s:if test='inputInvoiceInfo.subjectType=="3"'>selected</s:if> <s:else></s:else>>佣金手续费</option>
									</select>
								</td>
								
								
							</tr>
							<tr>
								<td></td>
								<td>
								</td>
								<td></td>
								<td>
								</td>
								<td></td> 
								<td></td> 
								<td></td> 
								<td>
								<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
										onclick="submitForm('listInvoiceInSurtax.action');" name="cmdQueryBtn" value="查询" id="cmdQueryBtn"/>
								</td>
							</tr>
						</table>
					</div>
					<table id="tbl_tools" width="100%" border="0">
						<tr align="left">
							<td style="text-align:left">
								<a href="#" id="cmdRollOutSubmitBtn" name="cmdRollOutSubmitBtn" onclick="rollOut()">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1036.png" />
									转出
								</a>
<!-- 								<a href="#" onclick="submitForm('invoiceInSurtaxExcel.action');" name="cmdExcel" id="cmdExcel"> -->
<!-- 									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" /> -->
<!-- 									导出 -->
<!-- 								</a> -->
							</td>
						</tr>
					</table>
					<div id="lessGridList4" style="overflow: auto; width: 100%;">
						<table class="lessGrid" cellspacing="0" rules="all" border="0" cellpadding="0" style="border-collapse: collapse;width: 100%;">
							<tr class="lessGrid head">
								<th width="3%" style="text-align:center">
									<input id="CheckAll" style="width:13px;height:13px;" type="checkbox" onClick="checkAll(this,'billCode')" />
								</th>
								<th style="text-align: center">发票代码</th>
								<th style="text-align: center">发票号码</th>
								<th style="text-align: center">税额</th>
								<th style="text-align: center">转出额</th>
								<th style="text-align: center">转出标识</th>
							</tr>
							<s:iterator value="paginationList.recordList" id="iList" status="stuts">
								<tr id="<s:property value="#iList.billCode"/>"  align="center" class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
									<td>
										<input type="checkbox" style="width:13px;height:13px;" name="billCode" value="<s:property value="#iList.billCode"/>" />
									</td>
									<td align="center">
										<s:property value='#iList.billCode' />
									</td>
									<td>
										<s:property value='#iList.billNo' />
									</td>
									<td>
										<s:property value='#iList.taxAmtSum' />
									</td>
									<td>
										<s:property value='#iList.vatOutAmt' />
									</td>
									<td>
										<s:if test='datastatus=="15"'>未转出</s:if>
										<s:if test='datastatus=="1"'>已转出</s:if>
									</td>
									<td hidden>
									<s:property value='#iList.expenseDocNum' />
									</td>
								</tr>
							</s:iterator>
						</table>
					</div>
					<div id="anpBoud" align="Right" style="width:100%;">
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
<script type="text/javascript">
	function rollOut(){
		var flag=false;
		var res = "";
		var resError = "";
		if (checkChkBoxesSelected("billCode")) {
			var ruleId="";
			var num="";
			var code="";
			var status="";
			var type="";
			var channel="";
			var tax="";
            var inputs = document.getElementsByName("billCode");
			for ( var i = 0; i < inputs.length; i++) {
				if (inputs[i].checked == true) {
				  $("#" + inputs[i].value).find("td").each(function (index, element) {				  
                        if (index == 5) {
                           status=$.trim($(this).text()); 
                        }
                         if(index == 2){
                        	code+=$.trim($(this).text())+",";
                        }
                         if(index == 6){
                        	num+=$.trim($(this).text())+",";
                        }
                    });   
                        if(status=="未转出"){ 
                        		res="";
                        		flag=true;
                        }
                       
					if(!flag){
					 alert("数据状态必须为未转出!第"+(i+1)+"条数据为转出");
                     return;
					}
					//num += inputs[i].value + ",";
				}
			}
			num=num.substring(0,num.length-1);
			//document.forms[0].expenseDocnuma.value=num;
           // alert(num);            
			code=code.substring(0,code.length-1);
			$("#expenseDocnuma").val(num);
			alert($("#expenseDocnuma").val());
			tax=tax.substring(0,tax.length-1);
			if(confirm("是否确认转出?")){
			document.forms[0].action="rollOutInvoiceInSurtax.action?num="+num+"&code="+code;
			document.forms[0].submit();
			}else{
				return;
			}
		}else{
		alert("请选择数据!");
		}
	}
	function submitForm(url){
			document.forms[0].action=url;
			document.forms[0].submit();
	}
	
// 	$(function() {
// 		$(".edit_insurtax").click(
// 				function() {
// 					bill_id = $(this).attr("rel");
// 					OpenModalWindow(
// 							"<c:out value='${webapp}'/>/editInvoiceInSurtax.action?bill_id="
// 									+ bill_id, 800, 600, true);
// 				});
//		转出提交
// 		$("#cmdRollOutSubmitBtn").click(function() {
// 			$billIds = $("input[name='billId']:checked");
// 			if ($billIds.size() == 0) {
// 				alert("请选择您要转出的数据");
// 				return false;
// 			}
// 			submitForm('不.action');
// 		});
//		撤回数据
// 		$("#cmdRollBackBtn").click(function() {
// 			$billIds = $("input[name='billId']:checked");
// 			if ($billIds.size() == 0) {
// 				alert("请选择您要撤回的数据");
// 				return false;
// 			}
// 			submitForm('batchRollBackInvoiceInSurtax.action');
// 		});
//		批量转出
// 		$("#cmdBatchRollOutBtn").click(function() {
// 			$billIds = $("input[name='billId']:checked");
// 			if ($billIds.size() == 0) {
// 				alert("请选择您要批量转出的数据");
// 				return false;
// 			}
// 			submitForm('batchRollOutInvoiceInSurtax.action');
// 		});
// 	});
</script>
</html>
