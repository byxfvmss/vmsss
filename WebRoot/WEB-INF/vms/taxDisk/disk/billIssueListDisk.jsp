<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.util.PaginationList"
	import="com.cjit.vms.trans.model.BillInfo"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../../../page/include.jsp"%>
<OBJECT id='DocCenterCltObj' width=100 height=100 classid="clsid:1E25664C-EA53-4F39-8575-684737626D6F" style="display: none;"></OBJECT>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<title>增值税管理平台</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/calendar_notime.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/editview.js"></script>
	<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
	<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/XmlHttp.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/search.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/ylb.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/lhb.js"></script>
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/search.js"></script>
	<script type="text/javascript">
	
		
	
	</script>
		
	<script type="text/javascript">
		// [查询]按钮  [导出]按钮
		function submitForm(actionUrl){
			submitAction(document.forms[0], actionUrl);
			document.forms[0].action="listbillIssueDisk.action?paginationList.showCount="+"false";
		}
		
		//开具按钮
		function issueBill(){
						
			
			if (!checkChkBoxesSelected("selectBillIds")) {
				alert("请选泽票据记录");
				return false;
			}
			checkDiskAndSelver(issueBillDisk(),"");
			
		}
		//开具按钮
		function issueBillDisk() {
			if (checkChkBoxesSelected("selectBillIds")) {
				try{
				//数控盘查询盘号
				var taxDiskNo = DocCenterCltObj.FunGetPara('','taxDiskNo');
				// alert(taxDiskNo);
				var arr=new Array();
				arr=taxDiskNo.split("|");
				if(arr[0]==0){
					alert("请连接税控盘 and 以管理员身份运行");
					return false;
				}
			
				taxDiskNo=arr[1];
				//alert(taxDiskNo);
				//获取选中的billId、数量、发票类型
				var billIds = document.getElementsByName("selectBillIds");
				var fapiaoTypes = document.getElementsByName("fapiaoTypes");
				var invalidInvoiceNum = document.Form1.invalidInvoiceNum.value;
				//alert(invalidInvoiceNum);
				var fapiaoType = fapiaoTypes[0].value;
				//alert("fa")
				var ids = "";
				var selectNum = 0;
				for (var i = 0; i < billIds.length; i++){
					if (billIds[i].checked){
						ids = ids === "" ? billIds[i].value : ids + "," + billIds[i].value;
						selectNum++;
					}
				}
				//转action查询信息
				$.ajax({url: 'getInvoiceNum.action',
						type: 'POST',
						async:false,
						data:{taxDiskNo:taxDiskNo,fapiaoType:fapiaoType},
						dataType:'text',
					//	timeout: 1000,
						error: function(){
							return false;},
						success: function(result){
					//			alert(result);
							if (result ==''){
								alert("查询信息失败。");
								return false;
							} else if (result == 'lock') {
								alert("不能同时操作数据。");
								return false;
							} else if (result == 'registeredInfoError') {
								alert("获取税控盘注册码失败。");
								return false;
							} else if (result == 'taxPwdError') {
								alert("获取税控盘口令和证书口令失败。");
								return false;
							} else  {
							///	alert(result);
								//数控盘查询发票库存量surplusInvoice
							var invoiceStockNum = DocCenterCltObj.FunGetPara(result,'surplusInvoice');
				//				alert(invoiceStockNum);
								var arr=new Array();
								arr=invoiceStockNum.toString().split("|");
					//			alert(arr);
								if(arr[0]==0){
									alert("请检查注册码是否正确");
									return false;
								}
								invoiceStockNum=arr[1];
			//					alert(arr[1]);
								
								//return false;
								
								//alert(invoiceStockNum)
							var kucun;
							if(fapiaoType==0){
							kucun='增值税专业发票';	
							}else{
								kucun='增值税普通发票';
							}
							//compareStore(selectNum,invalidInvoiceNum,invoiceStockNum);
							//var mm=parseInt(invalidInvoiceNum)+selectNum;
							//alert("1");	
							//alert(invalidInvoiceNum<10)
							//alert(ParseInt0<1)pa
							//alert(invoiceStockNum<parseInt(mm))
							//	alert(invoiceStockNum-(selectNum+parseInt(invalidInvoiceNum)));
							//alert(selectNum+parseInt(invalidInvoiceNum));
						if (invoiceStockNum<(selectNum+parseInt(invalidInvoiceNum))) {
									alert("库存不足，无法开具。当前"+kucun+"库存数量"+invoiceStockNum);
									return false;
								}
						
								getIssueBillInfo(result, ids,fapiaoType);
							}
						}
					});
					document.forms[0].action="listIssueBill.action?paginationList.showCount="+"false";
				}catch(e){
					alert("请安装税控盘插件！");
				}
			} else {
				alert("请选择票据记录！");
			}
		}
		
		
		function getIssueBillInfo(preStr, ids,fapiaoType) {
			//获取选中的billId、数量、发票类型
			if(!confirm("是否确认开具？")){
				return false;
			}
			
			//转action查询信息
			$.ajax({url: 'issueBill.action',
					type: 'POST',
					async:false,
					data:{preStr:preStr,billIds:ids,fapiaoType:fapiaoType},
					dataType: 'text',
				//	timeout: 1000,
					error: function(){
						return false;},
					success: function(result){
						if (result == ''){
							alert("查询信息失败。");
							return false;
						} else if (result == 'billItemError') {
							alert("无法查询发票对应的商品。");
							return false;
						} else if (result == 'billItemNum') {
							alert("发票对应的商品数量不能超过9个。");
							return false;
						} else  {
							//alert(result);
							var issueRes = DocCenterCltObj.FunGetPara(result,'issueInvoice');
							//alert(issueRes);
							updateIssueResult(issueRes);
						}
					}
				});
			document.forms[0].action="listIssueBill.action?paginationList.showCount="+"false";
		}
		
		function updateIssueResult(issueRes) {
			$.ajax({url: 'updateIssueResult.action',
					type: 'POST',
					async:false,
					data:{issueRes:issueRes},
					dataType: 'text',
					timeout: 1000,
					error: function(){return false;},
					success: function(result){
						alert(result);
						submitAction(document.forms[0], "listIssueBill.action");
						document.forms[0].action="listIssueBill.action?paginationList.showCount="+"false";
					}
				});
				
		}
		
		// 撤销按钮
		function cancelBill(){
			if(checkChkBoxesSelected("selectBillIds")){
				var billIds = document.getElementsByName("selectBillIds");
				var isHandiworks = document.getElementsByName("isHandiworks");
				for (var i = 0; i < billIds.length; i++){
					if (billIds[i].checked){
						if ("1" == isHandiworks[i].value){
							alert("选中票据包含自动开票记录，不允许撤销");
							return false;
						}
					}
				}
				
				if(!confirm("是否确定对选中票据进行撤销处理？")){
					return false;
				}
				document.forms[0].action = 'revokeBillFromIssue.action';
				document.forms[0].submit();
				document.forms[0].action="listIssueBill.action?paginationList.showCount="+"false";
			}else{
				alert("请选择票据记录！");
			}
		}
		var msg = '<s:property value="message"/>';
		if (msg != null && msg != ''){
			alert(msg);
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
		
	</script>
	<script type="text/javascript">
	function billissue(){
		var billIds = document.getElementsByName("selectBillIds");
				var fapiaoTypes = document.getElementsByName("fapiaoTypes");
				var invalidInvoiceNum = document.Form1.invalidInvoiceNum.value;
				var fapiaoType = fapiaoTypes[0].value;
				var ids = "";
				var selectNum = 0;
				for (var i = 0; i < billIds.length; i++){
					if (billIds[i].checked){
						ids = ids === "" ? billIds[i].value : ids + "," + billIds[i].value;
						selectNum++;
					}
				}
				// 确库存的校验 待验证
				// 检验当前号码是否在库存内  ---缺失
				// 校验 纳税人识别号 是否相等 及纳税人名称
				$.ajax({url: 'checkTaxNoAndTaxName.action',
					type: 'POST',
					async:false,
					data:{issueRes:issueRes},
					dataType: 'text',
					timeout: 1000,
					error: function(){return false;},
					success: function(result){
						alert(result.StringXml);
						submitAction(document.forms[0], "listIssueBill.action");
						//document.forms[0].action="listIssueBill.action?paginationList.showCount="+"false";
					}
				});
				
				
	}
	function createQueryTaxDiskXml(){
		$.ajax({url:'createQueryTaxDiskXml.action',
					type: 'POST',
					async:false,
					dataType: 'json',
					timeout: 1000,
					error: function(){return false;
					},
					success: function(result){
						if(result.returnCode==0){
							
						}else if(result.returnCode==1){
							alert(result.returnMsg);
						}
						alert(result.stringXml);
						
						}
				});
	
		
	}
	function checkTaxNo(StringXml){
		$.ajax({url:'checkTaxDiskTaxNoandTaxNo.action',
					type: 'POST',
					async:false,
					dataType: 'json',
					timeout: 1000,
					error: function(){
						return false;
					},
					success: function(result){
						if(result!=null){
						alert(result);
						}else{
								
						}
						
						}
				});
	}
	
	 
        
	
	//function 
	</script>
	<style type="text/css">
		.style1{
			width: 242px;
		}
	</style>
</head>
<body onkeydow="enterkey(event)">
	<form name="Form1" method="post" action="listbillIssueDisk.action" id="Form1">
	<input type="hidden" name="taxParam" id="taxParam" value="<s:property value='taxParam'/>"/>
	<%
		String currMonth = (String) request.getAttribute("currMonth");
		Long invalidInvoiceNum = (Long) request.getAttribute("invalidInvoiceNum");
	%>
	<input type="hidden" name="submitFlag" id="submitFlag" value=""/>
	<input type="hidden" name="currMonth" id="currMonth" value="<%=currMonth%>"/>
	<input type="hidden" name="invalidInvoiceNum" id="invalidInvoiceNum" value="<%=invalidInvoiceNum %>"/>
	<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
	<tr>
		<td class="centercondition">
		<s:component template="rocketMessage" />
		<div id="tbl_current_status">
			<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> 
			<span class="current_status_menu">当前位置：</span> 
			<span class="current_status_submenu1">销项税管理</span>
			<span class="current_status_submenu">发票管理</span>
			<span class="current_status_submenu">发票开具</span>
		</div>
		<div class="widthauto1">
		<table id="tbl_query" cellpadding="0" cellspacing="0" width="100%" border="0">
		<tr>
			<td align="left">
				申请开票日期
			</td>
			<td>
				<input class="tbl_query_time" id="applyBeginDate" type="text" name="billInfo.applyBeginDate"
					value="<s:property value='billInfo.applyBeginDate'/>"
					onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'applyEndDate\')}'})"
					size='11'"/>
				-
				<input class="tbl_query_time" id="applyEndDate" type="text" name="billInfo.applyEndDate"
					value="<s:property value='billInfo.applyEndDate'/>"
					onfocus="WdatePicker({minDate:'#F{$dp.$D(\'applyBeginDate\')}'})"
					size='11'"/>
			</td>
			<td align="left">
				客户名称
			</td>
			<td>
				<input type="text" class="tbl_query_text" name="billInfo.customerName" value="<s:property value='billInfo.customerName'/>" maxlength="100"/>
			</td>
			<td align="left">
				客户纳税人识别号
			</td>
			<td>
				<input type="text" class="tbl_query_text" name="billInfo.customerTaxno" value="<s:property value='billInfo.customerTaxno'/>" maxlength="15" onkeypress="checkkey(value);"/>
			</td>
			<td align="left">
				状态
			</td>
			<td>
				<select name="billInfo.dataStatus" style="width:135px">
					<option value="" <s:if test='billInfo.dataStatus==""'>selected</s:if><s:else></s:else>>全部</option>
					<option value="3" <s:if test='billInfo.dataStatus=="1"'>selected</s:if><s:else></s:else>>审核通过</option>
					<option value="4" <s:if test='billInfo.dataStatus=="2"'>selected</s:if><s:else></s:else>>无需审核</option>
					<option value="7" <s:if test='billInfo.dataStatus=="3"'>selected</s:if><s:else></s:else>>开具失败</option>
				</select>
			</td> 
		</tr>
		<tr>
			<td align="left">
				是否手工录入
			</td>
			<td>
				<select name="billInfo.isHandiwork" style="width:135px">
					<option value="" <s:if test='billInfo.isHandiwork==""'>selected</s:if><s:else></s:else>>全部</option>
					<option value="1" <s:if test='billInfo.isHandiwork=="1"'>selected</s:if><s:else></s:else>>自动开票</option>
					<option value="2" <s:if test='billInfo.isHandiwork=="2"'>selected</s:if><s:else></s:else>>人工审核</option>
					<option value="3" <s:if test='billInfo.isHandiwork=="3"'>selected</s:if><s:else></s:else>>人工开票</option>
				</select>
			</td>
			<td align="left">
				开具类型
			</td>
			<td> 
				<select name="billInfo.issueType" style="width:135px">
					<option value="" <s:if test='billInfo.issueType==""'>selected</s:if><s:else></s:else>>全部</option>
					<option value="1" <s:if test='billInfo.issueType=="1"'>selected</s:if><s:else></s:else>>单笔</option>
					<option value="2" <s:if test='billInfo.issueType=="2"'>selected</s:if><s:else></s:else>>合并</option>
					<option value="3" <s:if test='billInfo.issueType=="3"'>selected</s:if><s:else></s:else>>拆分</option>
				</select>
			</td>
			<td align="left">
				发票类型
			</td>
			<td>
				<select name="billInfo.fapiaoType" style="width:135px">
					<option value="0" <s:if test='billInfo.fapiaoType=="0"'>selected</s:if><s:else></s:else>>增值税专用发票</option>
					<option value="1" <s:if test='billInfo.fapiaoType=="1"'>selected</s:if><s:else></s:else>>增值税普通发票</option>
				</select>
			</td>
			<td colspan="2">
				<input type="button" class="tbl_query_button" value="查询"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="BtnView" id="BtnView" onclick="submitForm('listIssueBill.action')" />
			</td>
		</tr>
		</table>
		</div>
		<table id="tbl_tools" width="100%" border="0">
		<tr>
			<td align="left">
				<a href="#" name="BtnView" id="BtnView" onclick="createQueryTaxDiskXml()"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1010.png"/>开具</a>
				<a href="#" name="BtnView" id="BtnView" onclick="submitForm('exportIssueBill.action')"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png"/>导出</a>
				<a href="#" name="BtnView" id="BtnView" onclick="cancelBill()" ><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1002.png"/>撤销</a>
			
			</td>
		</tr>
		</table>

  	<div id="lessGridList4" style="overflow: auto; width: 100%;">
		<table class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0" style="border-collapse: collapse; width: 100%;">
	<tr class="lessGrid head">
		<th style="text-align:center">
			<input style="width:13px;height:13px;" id="CheckAll" type="checkbox" onclick="cbxselectall(this,'selectBillIds')" />
		</th>
		<th style="text-align:center">序号</th>
		<th style="text-align:center">申请开票日期</th>
		<th style="text-align:center">客户名称</th>
		<th style="text-align:center">客户纳税人识别号</th>
		<th style="text-align:center">合计金额</th>
		<th style="text-align:center">合计税额</th>
		<th style="text-align:center">价税合计</th>
		<th style="text-align:center">是否手工录入</th>
		<th style="text-align:center">开具类型</th>
		<th style="text-align:center">发票类型</th>
		<th style="text-align:center">状态</th>
		<th style="text-align:center">操作</th>
	</tr>
	<%
		PaginationList paginationList = (PaginationList)request.getAttribute("paginationList");
		if (paginationList != null){
			List billInfoList = paginationList.getRecordList();
			if (billInfoList != null && billInfoList.size() > 0){
				for(int i=0; i<billInfoList.size(); i++){
					BillInfo bill = (BillInfo)billInfoList.get(i);
					if(i%2==0){
	%>
	<tr class="lessGrid rowA">
	<%
					}else{
	%>
	<tr class="lessGrid rowB">
	<%
					}
	%>
		<td align="center">
			<input style="width:13px;height:13px;" type="checkbox" name="selectBillIds" value="<%=BeanUtils.getValue(bill,"billId")%>" />
			<input type="hidden" name="fapiaoTypes" value="<%=bill.getFapiaoType() %>">
			<input type="hidden" name="isHandiworks" value="<%=bill.getIsHandiwork() %>">
		</td>
		<td align="center"><%=i+1%></td>
		<td align="center"><%=bill.getApplyDate()==null ? "" : bill.getApplyDate() %></td>
		<td align="left"><%=bill.getCustomerName()%></td>
		<td align="left"><%=bill.getCustomerTaxno()%></td>
		<td align="right"><%=NumberUtils.format(bill.getAmtSum(),"",2)%></td>
		<td align="right"><%=NumberUtils.format(bill.getTaxAmtSum(),"",2)%></td>
		<td align="right"><%=NumberUtils.format(bill.getSumAmt(),"",2)%></td>
		<td align="center"><%=DataUtil.getIsHandiworkCH(bill.getIsHandiwork())%></td>
		<td align="center"><%=DataUtil.getIssueTypeCH(bill.getIssueType())%></td>
		<td align="center"><%=DataUtil.getFapiaoTypeCH(bill.getFapiaoType())%></td>
		<td align="center"><%=DataUtil.getDataStatusCH(bill.getDataStatus(), "BILL")%></td>
		<td align="center">
			<a href="viewTrans.action?curPage=<s:property value='paginationList.currentPage'/>&billId=<%=BeanUtils.getValue(bill,"billId")%>">
			<img src ="<c:out value="${bopTheme}"/>/themes/images/icons/icon1014.png" title="查看交易" style="border-width: 0px;" /></a>
			<a href="javascript:void(0);" 
			onClick="OpenModalWindow('viewSample.action?billId=<%=bill.getBillId()%>',1000,650,true)">
			<img src ="<c:out value="${bopTheme}"/>/themes/images/icons/icon1015.png" title="查看票样" style="border-width: 0px;" /></a>
		</td>
	</tr>
	<%
				}
			}
		}
	%>
	</tr>
	</table>
	</div>
	
	<div id="anpBoud" align="Right" style="overflow:auto;width:100%;">
	<input type="hidden" name="paginationList.showCount" value="false">
	<table width="100%" cellspacing="0" border="0">
	<tr>
		<td align="right"><s:component template="pagediv"/></td>
	</tr>
	</table>
	</div>
			</td>
	</tr>
	</table>
	</form>
</body>
</html>