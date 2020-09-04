<!--file: <%=request.getRequestURI() %> -->
<%@page import="com.cjit.vms.trans.model.RedReceiptApplyInfo"%>
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.util.PaginationList"
	import="com.cjit.vms.trans.model.BillInfo"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../../page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<OBJECT id='DocCenterCltObj' width=100 height=100 classid="clsid:1E25664C-EA53-4F39-8575-684737626D6F" style="display: none;"></OBJECT>
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
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/window.js"></script>
	<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
	<script type="text/javascript">
		// [查询]按钮
		function search(){
			//document.forms[0].submit();
			submitAction(document.forms[0], "billRedPrintList.action");
			document.forms[0].action="billRedPrintList.action";
		}
		function exportExcel(){
			submitAction(document.forms[0], "redReceiptBillPriintToExcel.action?type=redReceiptExcel");
			document.forms[0].action="billRedPrintList.action";
		}
		// 红冲按钮
		function cancel(result){
			if(checkChkBoxesSelected("selectBillIds")){
				var billIds = document.Form1.selectBillIds;
				var billDates = document.Form1.selectBillDates;
				var canCancel = true;
				
				var chkBoexes= document.getElementsByName("selectBillIds");
				var j = 0;
				var billId="";
				for(i=0;i<chkBoexes.length;i++){
					if(chkBoexes[i].checked){
						j++;
						billId = chkBoexes[i].value;
					}if(j>1){
						alert("请选择单条记录进行操作！");
						return false;
					}
				}
				if(!confirm("确定将选中票据进行红冲处理？")){
					return false;
				}if(result == "18"){
					window.open("redReceiptTransList.action?billId="+billId,'newwindow',screen.width * 0.8,screen.height * 0.8);
					document.forms[0].action="billRedPrintList.action";
				}else{
					submitAction(document.forms[0], "cancelRedReceipt.action?billId="+billId);
					document.forms[0].action="billRedPrintList.action";
				}
			}else{
				alert("请选择交易记录！");
			}
		}
		var msg = '<s:property value="message" escape="false"/>';
		if (msg != null && msg != ''){
			alert(msg);
		}
		function billRedlplementPrint(){
			if (checkChkBoxesSelected("selectBillIds")) {
				<%--var taxDiskNo = DocCenterCltObj.FunGetPara('','taxDiskNo');
				if (taxDiskNo == null) {
					alert("请连接税控盘。");
					return false;
				}--%>
				var faPiaoType=document.getElementById("redReceiptApplyInfo.fapiaoType").value;
					var billIds = document.getElementsByName("selectBillIds");
					var ids = "";
					var selectNum = 1;
					var myBillNo=new Array();
					for (var i = 0; i < billIds.length; i++){
						if (billIds[i].checked){
							myBillNo.push(billIds[i].value);
						}
					}
					var ids=myBillNo[0];
					if(myBillNo.length>1){
						alert("请选择一条记录进行补打");
						return false;
					}
				var resultLock="";
				var resultOCX="";
				// 查看税控盘号
				//var DiskNo = DocCenterCltObj.FunGetPara("","taxDiskNo");
									
				//alert(ids);
				if(ids!=null){
						// 判断上锁标志，去后台中获取
					   $.ajax({
					   async:false,
					   type: "POST",
					   cache:false,
					   url: "showRedLock.action",
					   //data:"billIds="+selectBillIds,
					   dataType: "text",
					   success : function(result){
					   if(result!=null){
					   	 if(result=="lock"){
					   	 	resultLock=result;
					   	 	alert("数据已被上锁，其他用户操作中，请稍后刷新页面再试！");
					   	 	return;
					     }else{
					    //	 alert("确定要打印吗");
					     	resultLock=result;
					    } 	
					   }
					   },
					   error:function(){
					      alert("获取锁标识过程出现异常!");
					   }
					});
					
						//alert(resultLock);
					var ocxString="";
					// 没有被锁，可以继续下一步操作
					Timeajax();
					if(resultLock=="null"){
						$.ajax({
							   type: "POST",
							   async:false,
							   cache:false,
							   url: 'showOCXRedstring.action',
							   data:{billIds:ids,faPiaoType:faPiaoType},
							   //data:"billIds="+ids+"faPiaoType="+faPiaoType+"diskNo=",
							   dataType: 'html',
							   success : function(result){
							   if(result!=null){
							   	 alert("result="+result);
							   	 ocxString=result;
							    }
							   },
							   error:function(){
							      alert("获取组装传进OCX的字符串过程出现异常!");
							   }
							 });
						var arr= new Array();
						arr = ocxString.split("+");
						   
					    ocxString=arr[0];
					    var sum=1;
					    var billPrintId=arr[1];
					  
					  	var ocxString='1111'+"|"+faPiaoType+"|"+sum+"|"+ocxString;
					  	alert(ocxString);
	    				if(ocxString!=null){
    					if(confirm("\n是否打印这些发票?")){
    						var statas=DocCenterCltObj.FunGetPara(ocxString,'printInvoice');
    						alert(statas);
    					resultOCX=billPrintId;
    					alert(resultOCX);
    					$.ajax({
							   type: "POST",
							   cache:false,
							   url: 'updateRedPrintResult.action',
							   data:{resultOCX:resultOCX},
							   dataType: "html",
							   success : function(result){
							   if(result!=null&&result=="success"){
							   	 alert("result="+result+"发票状态已更新");
							   	 clearInterval(intervalID);
							   	 location.reload() 
							    }
							   },
							   error:function(){
							      alert("根据OCX返回结果更新发票状态过程出现异常!");
							   }
							 });
							 document.forms[0].action="billRedPrintList.action";
    					}
					   }
					} 
					
				if(resultOCX!=""){
					//alert("bbb");
					
					}
				}
			} else {
				alert("请选择票据记录！");
			}
		}
	</script>
</head>
<body onkeydow="enterkey(event)">
	<form name="Form1" method="post" action="listRedReceipt.action" id="Form1">
	<%
		String currMonth = (String) request.getAttribute("currMonth");
	%>
	<input type="hidden" name="flag" value="true">
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
			<span class="current_status_submenu">红冲管理</span>
			<span class="current_status_submenu">红票打印</span>
		</div>
		<div class="widthauto1">
		<table id="tbl_query" cellpadding="0" cellspacing="0" width="100%" border="0">
							<tr>
								<td>投保单号</td>
							<td><input type="text" class="tbl_query_text"  name="redReceiptApplyInfo.vtiTtmprcon"
								value="<s:property value='redReceiptApplyInfo.vtiTtmprcon'/>" /></td>
							<td align="left">保单号</td>
							<td><input type="text" class="tbl_query_text" 
								name="redReceiptApplyInfo.vtiCherNum"
								value="<s:property value='redReceiptApplyInfo.vtiCherNum'/>" />
							</td>
							<td align="left">缴费频率</td>
							<td>
								<s:select name="redReceiptApplyInfo.vtiBillFreq" list="billFreqlList" listKey="key"
									listValue='value' cssClass="tbl_query_text" headerKey=""
									headerValue="请选择" />
							</td>
							</tr>
							<tr>
								<td>渠道</td>
								<td>
									<s:select name="redReceiptApplyInfo.vtiChannel" list="chanNelList" listKey="key"
										listValue='value' cssClass="tbl_query_text" headerKey=""
										headerValue="请选择" />
								</td>
								<td align="left">数据来源</td>
								<td>
									<s:select name="redReceiptApplyInfo.vtiDSource" list="dsouRceList" listKey="key"
										listValue='value' cssClass="tbl_query_text" headerKey=""
										headerValue="请选择" />
								</td>
								<td align="left">费用类型</td>
								<td>
									<s:select name="redReceiptApplyInfo.vtiFeeTyp" list="feeTypList" listKey="key"
											  listValue='value' cssClass="tbl_query_text" headerKey=""
											  headerValue="请选择" />
								</td>
							</tr>
							<tr>
								<td align="left">开票日期</td>
								<td>
									<input class="tbl_query_time" id="billBeginDate" type="text" name="redReceiptApplyInfo.billBeginDate" value="<s:property value='redReceiptApplyInfo.billBeginDate'/>"
										onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'billEndDate\')}'})" size='11'
									"/>
									--
									<input class="tbl_query_time" id="billEndDate" type="text" name="redReceiptApplyInfo.billEndDate" value="<s:property value='redReceiptApplyInfo.billEndDate'/>"
										onfocus="WdatePicker({minDate:'#F{$dp.$D(\'billBeginDate\')}'})" size='11'
									"/>
								</td>
								<td align="left">发票代码</td>
								<td>
									<input type="text" class="tbl_query_text" name="redReceiptApplyInfo.billCode" maxlength="10" value="<s:property value='redReceiptApplyInfo.billCode'/>" />
								</td>
								<td align="left">发票号码</td>
								<td>
									<input type="text" class="tbl_query_text" name="redReceiptApplyInfo.billNo" maxlength="8" value="<s:property value='redReceiptApplyInfo.billNo'/>" />
								</td>
							</tr>
							<tr>
								<td align="left">客户名称</td>
								<td>
									<input type="text" class="tbl_query_text" name="redReceiptApplyInfo.customerName" value="<s:property value='redReceiptApplyInfo.customerName'/>" />
								</td>
								<td align="left">发票类型</td>
								<td>
									<select id="redReceiptApplyInfo.fapiaoType" name="redReceiptApplyInfo.fapiaoType">
										<option value="">全部</option>
										<option value="0" <s:if test='redReceiptApplyInfo.fapiaoType=="0"'>selected</s:if> <s:else></s:else>>专</option>
										<option value="1" <s:if test='redReceiptApplyInfo.fapiaoType=="1"'>selected</s:if> <s:else></s:else>>普</option>
									</select>
								</td>
								<s:if test='configCustomerFlag.equals("KBC")'>
								<td align="left">客户号</td>
								<td>
									<input type="text" class="tbl_query_text" name="redReceiptApplyInfo.customerId" value="<s:property value='redReceiptApplyInfo.customerId'/>" />
								</td>
								</s:if>
								<td align="left">承保日期</td>
								<td><input class="tbl_query_time" id="billBeginDate"
									type="text" name="redReceiptApplyInfo.vtiHissDteBegin"
									value="<s:property value='redReceiptApplyInfo.vtiHissDteBegin'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'billEndDate\')}'})"
									size='11' "/> -- <input class="tbl_query_time" id="redReceiptApplyInfo.hissDteEnd"
									type="text" name="redReceiptApplyInfo.vtiHissDteEnd"
									value="<s:property value='redReceiptApplyInfo.vtiHissDteEnd'/>"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'billBeginDate\')}'})"
									size='11' "/></td>
							</tr>
							<tr>
							<td>
							开具类型
							</td>
							<td>
								<s:select list="#{'':'全部','1':'单笔','2':'合并','3':'拆分'}" name="redReceiptApplyInfo.issueType"label="abc" listKey="key" listValue="value" />
							</td>
							<td align="left">
								是否手工录入
							</td>
							<td><s:select list="#{'1':'全部','2':'人工审核','3':'人工开票'}" name="redReceiptApplyInfo.isHandiwork"label="abc" listKey="key" listValue="value" />
							</td>
							<td></td>
							<td align="right">
									<input type="button" class="tbl_query_button" value="查询" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" name="BtnView" id="BtnView" onclick="search()"/>
							</td>
							</tr>
			</table>
	 	<table id="tbl_tools" width="100%" border="0">
			<tr>
				<td align="left">
				<a href="#" name="BtnView" id="BtnView" onclick="exportExcel()"  ><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png"/>导出</a>
				<a href="#" name="BtnView" id="BtnView" onclick="billRedlplementPrint()"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1021.png"/>打印</a>
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
<!-- 								metlife begin -->
		<th style="text-align: center">投保单号</th>
		<th style="text-align: center">保单号</th>
		<th style="text-align: center">保全受理号</th>
<!-- 								metlife end -->		
		<th style="text-align:center">开票日期</th>
		<th style="text-align:center">客户名称</th>
		<th style="text-align:center">客户纳税人识别号</th>
		<th style="text-align:center">合计金额</th>
		<th style="text-align:center">合计税额</th>
		<th style="text-align:center">价税合计</th>
		<th style="text-align:center">是否手工录入</th>
		<th style="text-align:center">开具类型</th>
		<th style="text-align:center">发票类型</th>
		<th style="text-align:center">状态</th>
<!-- 								metlife begin -->
								<th style="text-align: center">渠道</th>
								<th style="text-align: center">费用类型	</th>
								<th style="text-align: center">承保日期</th>
								<th style="text-align: center">缴费频率</th>
								<th style="text-align: center">数据来源</th>
<!-- 								metlift end -->
		<th style="text-align:center">操作</th>
		
	</tr>
	<%
		PaginationList paginationList = (PaginationList)request.getAttribute("paginationList");
		if (paginationList != null){
			List billInfoList = paginationList.getRecordList();
			if (billInfoList != null && billInfoList.size() > 0){
				for(int i=0; i<billInfoList.size(); i++){
					RedReceiptApplyInfo bill = (RedReceiptApplyInfo)billInfoList.get(i);
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
			<input type="hidden" name="selectBillDates" value="<%=bill.getBillDate()%>"/>
		</td>
		<td align="center">
			<%=(i+1)%>
		</td>
<!-- 									metlife  begin -->
		<td align="center"><%=bill.getTtmprcno()%></td>
		<td align="center"><%=bill.getInsureId()%></td>
		<td align="center"><%=bill.getRepnum()%></td>
<!-- 									metlife end -->
		<td align="center"><%=bill.getBillDate()%></td>
		<td align="center"><%=bill.getCustomerName()%></td>
		<td align="center"><%=bill.getCustomerTaxno()%></td>
		<td align="right"><%=NumberUtils.format(bill.getAmtSum(),"",2)%></td>
		<td align="right"><%=NumberUtils.format(bill.getTaxAmtSum(),"",2)%></td>
		<td align="right"><%=NumberUtils.format(bill.getSumAmt(),"",2)%></td>
		<td align="center"><%=DataUtil.getIsHandiworkCH(bill.getIsHandiwork())%></td>
		<td align="center"><%=DataUtil.getIssueTypeCH(bill.getIssueType())%></td>
		<td align="center"><%=DataUtil.getFapiaoTypeCH(bill.getFapiaoType())%></td>
		<td align="center"><%=DataUtil.getDataStatusCH(bill.getDatastatus(),"BILL")%></td>
<!-- 									metlife begin -->
		<td align="center"><%=bill.getChannel()%></td>
		<td align="center"><%=bill.getFeeTyp()%></td>
		<td align="center"><%=bill.getHissDte()%></td>
		<td align="center"><%=bill.getBillFreq()%></td>
		<td align="center"><%=bill.getdSource()%></td>
<!-- 									metlife  end -->
		<td>
				<a href="viewPrintTrans.action?curPage=<s:property value='paginationList.currentPage'/>&billId=<%=BeanUtils.getValue(bill,"billId")%>">
			<img src ="<c:out value="${bopTheme}"/>/themes/images/icons/icon1014.png" title="查看交易" style="border-width: 0px;" /></a>
			    
			    <a href="javascript:void(0);" 
			onClick="OpenModalWindow('viewImgFromBill.action?falg=print&billId=<%=bill.getBillId()%>',1000,650,true)">
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
