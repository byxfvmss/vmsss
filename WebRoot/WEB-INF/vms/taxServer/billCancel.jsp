<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.util.PaginationList"
	import="com.cjit.vms.trans.model.BillCancelInfo"
	import="com.cjit.vms.trans.model.BillInfo"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.cjit.vms.trans.model.billInvalid.BillCancelInfo" %>
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
	<script type="text/javascript">
		var msg = '<s:property value="message" escape="false" />';
		if (msg != null && msg != ''){
			alert(msg);
		}
		// [查询]按钮
		function query(){
			//document.forms[0].submit();
			submitAction(document.forms[0], "listBillCancel.action");
			document.forms[0].action="listBillCancel.action";
		}
		// 作废按钮
		function cancelBill(){
			if(checkChkBoxesSelected("selectBillIds")){
				try{
				var chkBoexes= document.getElementsByName("selectBillIds");
				var j = 0;
				var billId="";
				for(i=0;i<chkBoexes.length;i++){
					if(chkBoexes[i].checked){
						j++;
						billId = chkBoexes[i].value;
					}
					if(j>1){
						alert("请选择单条记录进行操作！");
						return false;
					}
				}
				
				var taxDiskNo = DocCenterCltObj.FunGetPara('','taxDiskNo');
				//alert(taxDiskNo);
				var arr=new Array();
				arr=taxDiskNo.split("|");
				if(arr[0]==0){
					alert("请连接税控盘");
					return false;
				}
			
				taxDiskNo=arr[1];
				if(!confirm("确定将选中票据进行作废处理？")){
					return false;
				}
				//转action查询信息
				$.ajax({url: 'billCancelDispose.action',
						type: 'POST',
						async:false,
						data:{taxDiskNo:taxDiskNo,billId:billId},
						dataType: 'text',
						timeout: 1000,
						error: function(){return false;},
						success: function(result){
							//alert(result);
							if (result == ''){
								alert("查询信息失败。");
								return false;
							} else if (result == 'registeredInfoError') {
								alert("获取税控盘注册码失败。");
								return false;
							} else if (result == 'taxPwdError') {
								alert("获取税控盘口令和证书口令失败。");
								return false;
							} else  {
								var cancelRes = DocCenterCltObj.FunGetPara(result,'concelInvoice');
								updateCancelBillResult(cancelRes);
							}
						}
					});
					document.forms[0].action="listBillCancel.action";
				}catch(e){
					alert("请安装税控盘控件！");
				}
			}else{
				alert("请选择票据记录！");
			}
		}
		
		function updateCancelBillResult(cancelRes) {
			$.ajax({url: 'updateCancelBillResult.action',
					type: 'POST',
					async:false,
					data:{cancelRes:cancelRes},
					dataType: 'text',
					timeout: 1000,
					error: function(){return false;},
					success: function(result){
						alert(result);
				submitAction(document.forms[0], "listBillCancel.action");
				document.forms[0].action="listBillCancel.action";
					}
				});
		}
		
		// 撤销按钮
		function revokeBill(){
			if(checkChkBoxesSelected("selectBillIds")){
				var billIds = document.Form1.selectBillIds;
				if(billIds[0]==undefined){
					if(!confirm("确定将选中票据进行撤销处理？")){
						return false;
					}
					if(!deletedYNcheck()){
						alert("已作废发票不可撤销,请重新选择！");
					}
					else{
						submitAction(document.forms[0], "revokeBill.action");//撤销
						document.forms[0].action="listBillCancel.action";
					}
				}else{
					if (!isNaN(billIds.length)){
						if(!confirm("确定将选中票据进行撤销处理？")){
							return false;
						}
						if(!deletedYNcheck()){
							alert("已作废发票不可撤销,请重新选择！");
						}
						else{
							submitAction(document.forms[0], "revokeBill.action");//撤销
							document.forms[0].action="listBillCancel.action";
						}
					}
				}
			}else{
				alert("请选择交易记录！");
			}
		}
		
		function deletedYNcheck()
		{
			var chkBoxes= document.getElementsByName("selectBillIds");
			var selStatus= document.getElementsByName("selectDataStatus");
			for (var i=0; i<chkBoxes.length; i++) {
				if(chkBoxes[i].checked && selStatus[i].value == 15)
				{
					return false;
				}
			}
			return true;
		}
		
		// 导出按钮
		function exportCancelBill(){
			submitAction(document.forms[0], "cancelBillToExcel.action?reqExportSource=billCancel");
			document.forms[0].action="listBillCancel.action";
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
	<style type="text/css">
		.style1{
			width: 242px;
		}
	</style>
</head>
<body onkeydow="enterkey(event)">
	<form name="Form1" method="post" action="listBillCancel.action" id="Form1">
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
			<span class="current_status_submenu">作废管理</span>
			<span class="current_status_submenu">发票作废</span>
		</div>
		<div class="widthauto1">
		<table id="tbl_query" cellpadding="0" cellspacing="0" width="80%" border="0">
		<tr>
<!-- 							<td> -->
<!-- 							投保单号 -->
<!-- 							</td> -->
<!-- 							<td> -->
<!-- 							<input type="text" class="tbl_query_text"  name="billCancelInfo.vtiTtmprcon" -->
<!-- 								value="<s:property value='billCancelInfo.vtiTtmprcon'/>" /> -->
<!-- 							</td> -->
<!-- 							<td align="left"> -->
<!-- 							保单号 -->
<!-- 							</td> -->
<!-- 							<td> -->
<!-- 							<input type="text" class="tbl_query_text"  -->
<!-- 								name="billCancelInfo.vtiCherNum" -->
<!-- 								value="<s:property value='billCancelInfo.vtiCherNum'/>" /> -->
<!-- 							</td> -->
							<td>发票代码</td>
							<td><input type="text" class="tbl_query_text" name="billCancelInfo.billCode"
								maxlength="10"
								value="<s:property value='billCancelInfo.vtiBillCode'/>" /></td>
							<td>发票号码</td>
							<td><input type="text" class="tbl_query_text" name="billCancelInfo.billNo"
								maxlength="10"
								value="<s:property value='billCancelInfo.billNo'/>" /></td>
							<td align="left">开票日期</td>
							<td><input class="tbl_query_time" id="billBeginDate"
								type="text" name="billCancelInfo.billBeginDate"
								value="<s:property value='billCancelInfo.billBeginDate'/>"
								onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'billEndDate\')}'})"
								size='11' "/> -- <input class="tbl_query_time" id="billEndDate"
								type="text" name="billCancelInfo.billEndDate"
								value="<s:property value='billCancelInfo.billEndDate'/>"
								onfocus="WdatePicker({minDate:'#F{$dp.$D(\'billBeginDate\')}'})"
								size='11' "/></td>
						</tr>
						<tr>
<!-- 							<td>渠道</td> -->
<!-- 							<td><input type="text" class="tbl_query_text"  name="billCancelInfo.vtiChannel" -->
<!-- 								value="<s:property value='billCancelInfo.vtiChannel'/>" /></td> -->
<!-- 							<td align="left">客户名称</td> -->
<!-- 							<td><input type="text" class="tbl_query_text"  -->
<!-- 								name="billCancelInfo.customerName" -->
<!-- 								value="<s:property value='billCancelInfo.customerName'/>" /> -->
<!-- 							</td> -->
<!-- 							<td align="left">承保日期</td> -->
<!-- 							<td><input class="tbl_query_time" id="billBeginDate" -->
<!-- 								type="text" name="billCancelInfo.vtiHissDteBegin" -->
<!-- 								value="<s:property value='billCancelInfo.vtiHissDteBegin'/>" -->
<!-- 								onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'billEndDate\')}'})" -->
<!-- 								size='11' "/> -- <input class="tbl_query_time" id="billCancelInfo.hissDteEnd" -->
<!-- 								type="text" name="billCancelInfo.vtiHissDteEnd" -->
<!-- 								value="<s:property value='billCancelInfo.vtiHissDteEnd'/>" -->
<!-- 								onfocus="WdatePicker({minDate:'#F{$dp.$D(\'billBeginDate\')}'})" -->
<!-- 								size='11' "/></td> -->
						</tr>
<!-- 						<tr> -->
<!-- 							<td align="left">缴费频率</td> -->
<!-- 							<td><input type="text" class="tbl_query_text"  -->
<!-- 								name="billCancelInfo.vtiBillFreq" -->
<!-- 								value="<s:property value='billCancelInfo.vtiBillFreq'/>" /></td> -->
<!-- 							<td align="left">数据来源</td> -->
<!-- 							<td><input type="text" class="tbl_query_text"  -->
<!-- 								name="billCancelInfo.vtiDSource" -->
<!-- 								value="<s:property value='billCancelInfo.vtiDSource'/>" /> -->
<!-- 							</td> -->
<!-- 							<td align="left">费用类型</td> -->

<!-- 							<td><input type="text" class="tbl_query_text" name="billCancelInfo.vtiFeeTyp" -->
<!-- 								maxlength="10" -->
<!-- 								value="<s:property value='billCancelInfo.vtiFeeTyp'/>" /></td> -->
<!-- 						</tr> -->
						<tr>
							<td align="left">开具类型</td>
							<td><select id="billCancelInfo.issueType" name="billCancelInfo.issueType"  style="width:120px">
								<option value="" <s:if test='billCancelInfo.issueType==""'>selected</s:if><s:else></s:else>>全部</option>
								<option value="1" <s:if test='billCancelInfo.issueType=="1"'>selected</s:if><s:else></s:else>>单笔</option>
								<option value="2" <s:if test='billCancelInfo.issueType=="2"'>selected</s:if><s:else></s:else>>合并</option>
								<option value="3" <s:if test='billCancelInfo.issueType=="3"'>selected</s:if><s:else></s:else>>拆分</option></td>

							<td align="left">发票类型</td>
							<td><select id="billCancelInfo.fapiaoType"
								name="billCancelInfo.fapiaoType">
									<option value="">全部</option>
									<option value="0"
										<s:if test='billCancelInfo.fapiaoType=="0"'>selected</s:if>
										<s:else></s:else>>增值税专用发票</option>
									<option value="1"
										<s:if test='billCancelInfo.fapiaoType=="1"'>selected</s:if>
										<s:else></s:else>>增值税普通发票</option>
							</select>
							</td>
							<td align="left">客户名称</td>
							<td><input type="text" class="tbl_query_text" 
								name="billCancelInfo.customerName"
								value="<s:property value='billCancelInfo.customerName'/>" />
							</td>
						</tr>
						<tr>
							<td align="left">
								是否手工录入
							</td>
							<td><s:select list="#{'1':'全部','2':'人工审核','3':'人工开票'}" name="billCancelInfo.isHandiwork"label="abc" listKey="key" listValue="value" />
							</td>
							<td>
<!-- 							发票号码 -->
							</td>
							<td>
<!-- 							<input type="text" class="tbl_query_text" name="billCancelInfo.billNo" -->
<!-- 								maxlength="10" -->
<!-- 								value="<s:property value='billCancelInfo.billNo'/>" /> -->
							</td>
							<td></td>
							<td ><input type="button"
								class="tbl_query_button" value="查询"
								onMouseMove="this.className='tbl_query_button_on'"
								onMouseOut="this.className='tbl_query_button'" name="BtnView"
								id="BtnView" onclick="query()" /></td>
						</tr>
		</table>
		</div>
	<!-- <table id="tbl_query" cellpadding="1" cellspacing="0">
		<tr align="left">
			<td>
				 <input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" 
				 onMouseOut="this.className='tbl_query_button'" value="作废" onClick="cancelBill()"/>
			</td>
			<td>
				 <input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" 
				 onMouseOut="this.className='tbl_query_button'" value="撤销" onClick="revokeBill()"/>
			</td>
			<td>
				 <input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" 
				 onMouseOut="this.className='tbl_query_button'" value="导出" 
				 onClick="exportCancelBill()"/>
			</td>
        </tr>
	</table> -->
 	<table id="tbl_tools" width="100%" border="0">
		<tr>
			<td align="left">
			<a href="#" onClick="cancelBill()"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1031.png"/>作废</a>
			<a href="#" onClick="revokeBill()"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1032.png"/>撤销</a>
			<a href="#" onClick="exportCancelBill()"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png"/>导出</a>
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
					<th style="text-align: center">投保单号</th>
					<th style="text-align: center">保单号</th>
<!-- 					<th style="text-align: center">旧保单号</th> -->
					<th style="text-align: center">开票日期</th>
					<th style="text-align: center">客户名称</th>
					<th style="text-align: center">发票代码</th>
					<th style="text-align: center">发票号码</th>
					<th style="text-align: center">合计金额</th>
					<th style="text-align: center">合计税额</th>
					<th style="text-align: center">价税合计</th>
					<th style="text-align: center">是否手工录入</th>
					<th style="text-align: center">开具类型</th>
					<th style="text-align: center">发票类型</th>
					<th style="text-align: center">状态</th>
<!-- 					<th style="text-align: center">渠道</th> -->
<!-- 					<th style="text-align: center">费用类型</th> -->
<!-- 					<th style="text-align: center">承保日期</th> -->
<!-- 					<th style="text-align: center">缴费频率</th> -->
<!-- 					<th style="text-align: center">数据来源</th> -->
					<th style="text-align: center">操作</th>
	</tr>
	<%
		PaginationList paginationList = (PaginationList)request.getAttribute("paginationList");
		if (paginationList != null){
			List billCancelInfoList = paginationList.getRecordList();
			if (billCancelInfoList != null && billCancelInfoList.size() > 0){
				for(int i=0; i<billCancelInfoList.size(); i++){
					BillCancelInfo billCancel = (BillCancelInfo)billCancelInfoList.get(i);
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
			<input style="width:13px;height:13px;" type="checkbox" name="selectBillIds" value="<%=BeanUtils.getValue(billCancel,"billId")%>" />
			<input type="hidden" name="selectDataStatus" value="<%=billCancel.getDataStatus()%>"/>
		</td>
					<td align="center"><%=i+1%></td>
					<td align="center"><%=billCancel.getVtiTtmprcon() %></td>
					<td align="center"><%=billCancel.getVtiCherNum() %></td>
<!-- 					<td align="center"><%=billCancel.getVtiRepNum() %></td> -->
					<td align="center"><%=billCancel.getBillDate()!=null?billCancel.getBillDate():""%></td>
					<td align="center"><%=billCancel.getCustomerName()%></td>
					<td align="center"><%=billCancel.getBillCode()%></td>
					<td align="center"><%=billCancel.getBillNo()%></td>
					<td align="right"><%=NumberUtils.format(billCancel.getAmtSum(),"",2)%></td>
					<td align="right"><%=NumberUtils.format(billCancel.getTaxAmtSum(),"",2)%></td>
					<td align="right"><%=NumberUtils.format(billCancel.getSumAmt(),"",2)%></td>
					<td align="left"><%=(billCancel.getIsHandiwork()==null)?"":billCancel.getIsHandiwork().equals("1")?"自动开票":billCancel.getIsHandiwork().equals("2")?"人工审核":"人工开票"%></td>
					<td align="left"><%=(billCancel.getIssueType()==null)?"":billCancel.getIssueType().equals("1")?"单笔":billCancel.getIssueType().equals("2")?"合并":"拆分"%></td>
					<td align="right"><%=DataUtil.getFapiaoTypeCH(billCancel.getFapiaoType()) %></td>
					<td align="right"><%=DataUtil.getDataStatusCH(billCancel.getDatastatus(), "BILL") %></td>
					<td align="center"><%=billCancel.getVtiChannel() %></td>
					<td align="center"><%=billCancel.getVtiFeeTypCh() %></td>
					<td align="center"><%=billCancel.getVtiHissDte() %></td>
					<td align="center"><%=billCancel.getVtiBillFreqCh() %></td>
					<td align="center"><%=billCancel.getVtiDsouRceCh() %></td>
		<td align="center">
			<a href="seeTransWithBill.action?reqSource=billCancel&billId=<%=BeanUtils.getValue(billCancel,"billId")%>">
			<img src ="<c:out value="${bopTheme}"/>/themes/images/icons/icon1014.png" title="查看交易" style="border-width: 0px;" /></a>
			<a href="javascript:void(0);" 
			onClick="OpenModalWindowSubmit('viewImgFromBillForBillCancel.action?billId=<%=billCancel.getBillId()%>',1000,650,true)">
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