<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.text.*"
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
	<!-- <script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/search.js"></script> -->
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/ylb.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/lhb.js"></script>
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/search.js"></script>
	<script type="text/javascript">
		var msg = '<s:property value="message"/>';
		if (msg != null && msg != ''){
			alert(msg);
		}
		// [查询]按钮
		function query(){
			//document.forms[0].submit();
			submitAction(document.forms[0], "listBillCancelApply.action");
			document.forms[0].action="listBillCancelApply.action";
		}
		// 作废按钮
		function cancel(){
			if(checkChkBoxesSelected("selectBillIds")){
				var billId = "";
				var billDate = "";
				var cancelTime = 0;
				var billIds = document.Form1.selectBillIds;
				var billDates = document.Form1.selectBillDates;//开票日期
				var cancelTimes = document.Form1.selectCancelTimes;//作废时间流参数
				var canCancel = true;
				var count = 0;
				if (!isNaN(billIds.length)){
					for (var i = 0; i < billIds.length; i++){
						if (billIds[i].checked){
							billId = billId=='' ? billIds[i].value : billId+","+billIds[i].value;
							billDate = billDates[i].value;
							cancelTime = cancelTimes[i].value;
							count++;
							if (document.Form1.elements['submitFlag'].value == '<%=DataUtil.FEIPIAO%>'){
								if(count > 1){
									alert("请选择单条记录进行作废！");
									canCancel = false;
									break;
								}
							}
						}
					}
				} else {
					billId = billIds.value;
					billDate = billDates.value;
					cancelTime = cancelTimes.value;
				}
				if (canCancel == false){
					return false;
				}
				if (document.Form1.elements['submitFlag'].value == '<%=DataUtil.FEIPIAO%>'){
					var nowDate = new Date();
					var sjc = Math.floor((nowDate.getTime()-Date.parse(new Date(billDate.replace(/-/g, "/"))))/(3600*1000));
					if (sjc > cancelTime){
						if(!confirm("确定是否继续？")){
							return false;
						}
						alert("票据需要审核，请进入审核页面进行审核！");
						submitAction(document.forms[0], "updateBillCancelStatus.action?dataStatus=13");//走审核流程
						document.forms[0].action="listBillCancelApply.action";
						
					}else{
						if(!confirm("确定是否继续？")){
							return false;
						}
						alert("票据不需要审核，请直接进入废票页面操作！");
						submitAction(document.forms[0], "updateBillCancelStatus.action?dataStatus=14");//无需走审核流程
						document.forms[0].action="listBillCancelApply.action";
					}
				}
			}else{
				alert("请选择发票记录！");
			}
		}
		
		// 导出按钮
		function exportCancelBill(){
			submitAction(document.forms[0], "cancelBillToExcel.action?reqExportSource=billCancelApply");
			document.forms[0].action="listBillCancelApply.action";
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
				document.forms[0].action="listBillCancelApply.action";
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
			<span class="current_status_submenu">作废申请</span>
		</div>
		<div class="widthauto1">
		<table id="tbl_query" cellpadding="0" cellspacing="0" width="80%" border="0">
		<tr>
			<td align="left">
				开票日期
			</td>
			<td>
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
			<td align="left">
				客户纳税人名称
			</td>
			<td>
				<input type="text" class="tbl_query_text"  name="billCancelInfo.customerName" value="<s:property value='billCancelInfo.customerName'/>" size="38"/>
			</td>
			<td align="left">
				发票类型
			</td>
			<td><select id="billCancelInfo.fapiaoType" name="billCancelInfo.fapiaoType" >
					<option value="">全部</option>
					<option value="0" <s:if test='billCancelInfo.fapiaoType=="0"'>selected</s:if><s:else></s:else>>增值税专用发票</option>
					<option value="1" <s:if test='billCancelInfo.fapiaoType=="1"'>selected</s:if><s:else></s:else>>增值税普通发票</option>
				</select>
			</td>
			<td></td>
		</tr>
		<tr>
			<td align="left">
				发票代码
			</td>
			<td>
				<input type="text" class="tbl_query_text" name="billCancelInfo.billCode" value="<s:property value='billCancelInfo.billCode'/>" size="38"/>
			</td>
			<td align="left">
				发票号码
			</td>
			<td>
				<input type="text" class="tbl_query_text" name="billCancelInfo.billNo" value="<s:property value='billCancelInfo.billNo'/>" size="38"/>
			</td>
			<td align="left">
				状态
			</td>
			<td><select id="billCancelInfo.dataStatus" name="billCancelInfo.dataStatus" >
					<option value="">全部</option>
					<option value="5" <s:if test='billCancelInfo.dataStatus=="5"'>selected</s:if><s:else></s:else>>已开具</option>
					<option value="8" <s:if test='billCancelInfo.dataStatus=="8"'>selected</s:if><s:else></s:else>>已打印</option>
					<!-- <option value="9" <s:if test='billCancelInfo.dataStatus=="9"'>selected</s:if><s:else></s:else>>打印失败</option>
					<option value="19" <s:if test='billCancelInfo.dataStatus=="19"'>selected</s:if><s:else></s:else>>已收回</option> -->
				</select>
			</td>
			<td >
				<input type="button" class="tbl_query_button" value="查询"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="BtnView" id="BtnView" onclick="query()" />
			</td>
		</tr>
		</table>
		</div>
	<!-- <table id="tbl_query" cellpadding="1" cellspacing="0">
		<tr align="left">
			<td>
				 <input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" 
				 onMouseOut="this.className='tbl_query_button'" value="作废" onClick="document.getElementById('submitFlag').value='<%=DataUtil.FEIPIAO%>';cancel()"/>
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
				<a href="#" onClick="document.getElementById('submitFlag').value='<%=DataUtil.FEIPIAO%>';cancel()" ><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1031.png"/>作废</a>
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
					<th style="text-align: center">保全受理号</th> 
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
					<th style="text-align: center">渠道</th>
					<th style="text-align: center">费用类型</th>
					<th style="text-align: center">承保日期</th>
					<th style="text-align: center">缴费频率</th>
					<th style="text-align: center">数据来源</th>
		<th style="text-align:center">操作</th>
	</tr>
	<%
		PaginationList paginationList = (PaginationList)request.getAttribute("paginationList");
		if (paginationList != null){
			List billCancelInfoList = paginationList.getRecordList();
			if (billCancelInfoList != null && billCancelInfoList.size() > 0){
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date d = new Date();
				String time = d.toLocaleString();
				for(int i=0; i<billCancelInfoList.size(); i++){
					BillCancelInfo billCancel = (BillCancelInfo)billCancelInfoList.get(i);
					Date d1 = df.parse(time);
					Date d2 = df.parse(billCancel.getBillDate());
					long diff = d1.getTime() - d2.getTime();
					long days = diff / (1000 * 60 * 60);
					long cancelTime = Long.parseLong(billCancel.getCancelTime());
					if(days > cancelTime){
	%>
	<tr class="lessGrid rowD">
	<%
					}else{
	%>
	<tr class="lessGrid rowB">
	<%
					}
	%>
		<td align="center">
			<input style="width:13px;height:13px;" type="checkbox" name="selectBillIds" value="<%=BeanUtils.getValue(billCancel,"billId")%>" />
			<input type="hidden" name="selectBillDates" value="<%=billCancel.getBillDate()%>"/>
			<input type="hidden" name="selectCancelTimes" value="<%=billCancel.getCancelTime()==null?"":billCancel.getCancelTime()%>"/>
		</td>
		<td align="center"><%=i+1%></td>
		<td align="center"><%=billCancel.getTtmprcno() %></td>
					<td align="center"><%=billCancel.getInsureId() %></td>
					<td align="center"><%=billCancel.getRepnum() %></td>
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
					<td align="center"><%=billCancel.getChannel() %></td>
					<td align="center"><%=billCancel.getFeeTyp() %></td>
					<td align="center"><%=billCancel.getHissDte() %></td>
					<td align="center"><%=billCancel.getBillFreq()() %></td>
					<td align="center"><%=billCancel.getdSource()%></td>
		<td align="center">
			<a href="seeTransWithBill.action?reqSource=billCancelApply&billId=<%=BeanUtils.getValue(billCancel,"billId")%>">
			<img src ="<c:out value="${bopTheme}"/>/themes/images/icons/icon1014.png" title="查看交易" style="border-width: 0px;" /></a>
			<a href="javascript:void(0);" onClick="OpenModalWindowSubmit('RedReceiptApplyToCancelReason.action?billId=<%=BeanUtils.getValue(billCancel,"billId")%>',500,300,true)">
				<img src="<c:out value="${bopTheme}"/>/themes/images/icons/info.png" title="查看退回说明" style="border-width: 0px;" />
			</a> 
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