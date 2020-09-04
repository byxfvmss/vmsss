<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.util.PaginationList"
	import="com.cjit.vms.filem.util.FileUtil"
	import="com.cjit.vms.trans.model.BillInfo"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../../page/include.jsp"%>
<OBJECT ID=ocxObj CLASSID="clsid:003BD8F2-A6C3-48EF-9B72-ECFD8FC4D49F" codebase="NISEC_SKSCX.ocx#version=1,0,0,1" style='display: none'></OBJECT>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<title>增值税管理平台</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
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
	
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/billinterface/billInterfaceAjax.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/billinterface/bw_servlet.js"></script>
	<script type="text/javascript">
	var billInterface = new BillInterface();
	billInterface.init({});
	function createBillPrint(){
		var billIds = document.getElementsByName("selectBillIds");
		var  fapiaoType=document.getElementById("billInfo.fapiaoType").value;
		var ids="";
			for(var i=0;i<billIds.length;i++){
				if (billIds[i].checked){
					ids = ids === "" ? billIds[i].value : ids + "," + billIds[i].value;
				}
			}
			if(ids==''){
				alert("请选择票据记录");
				return false;
				
			}
		 billInterface.createBillPrint({ids:ids,fapiaoType:fapiaoType});
		submitAction(document.forms[0], "listBillPrint.action??paginationList.showCount="+"false");
	}
	/*** 
 * 顶层类 打印字符串
****/
function showOcxSelver(ids,faPiaoType,printLimitValue,fileName){
			var a="success";
			$.ajax({
							   type: "POST",
							   async:false,
							   cache:false,
							   url: 'showOCXstring.action',
							   data:{billIds:ids,faPiaoType:faPiaoType,printLimitValue:printLimitValue},
							   dataType: 'html',
							   success : function(result){
							   if(result=='printValueError'){
							   	 alert("单次打印超过限定的值"+printLimitValue+"请重新选择");
							   	 a= "error";
							    }
							  var arr= new Array();
							  //alert(result);
							arr = result.split("+");
					    	//alert(arr[0]);
					    	if(arr[0]==0){
					    		alert(arr[1]);
					    		return false;
					    	}
					    	var ocxString=arr[1];
					    	var billNo=arr[2];
					   		var sum=arr[3];
					    	if(confirm(billNo+"\n是否打印这些发票?")){
    							ocxString=ocxString;
    							//alert(ocxString);
    							//return false;
    							dataSelverutil(ocxString,fileName,faPiaoType)
					    		
					    		//updateBillStatus(statas);
    							}
							   },
							   error:function(){
							      alert("获取组装传进OCX的字符串过程出现异常!");
							   }
							 });
			
		}
		function dataSelverutil(ocxString,fileName,faPiaoType){
			alert(ocxString);
			var ar=new Array();
			ar=ocxString.split("|");
			//alert(ar.length)
			var a=ar.length==1?1:ar.length-1;
			var j=0;
			//alert(a);
			for(var i=0;i<a;i++){
				var arr=new Array();
				arr=ar[i].split("^");
				var billId=arr[1];
				var billCode=arr[2];
				alert(billCode);
				var billNo=arr[3];
				//alert(j);
				//alert(billNo);
				//return false;
				j=dataprintBase(billId,faPiaoType,billCode,billNo,fileName,j,a-1,i)
				if(j=="error"){
				break;
				}
			}
		}
		/***TAXPARAMETERS
		createBillPrintXml 创建打印的xml 文件
		
		*/
		function dataprintBase(billId,fapiaoType,billCode,billNo,fileName,j,a,i){
		//	alert("11");
			alert(billCode);
			$.ajax({
					   async:false,
					   type: "POST",
					   cache:false,
					   url: "createBillPrintXml.action",
					   data:{fapiaoType:fapiaoType,billCode:billCode,billNo:billNo,fileName:fileName},
					   dataType:"text",
					   error: function(){
							return false;},
					   success : function(result){
									//alert(result);
								try
							    {
							var ret = sk.Operate(result);
							
								//alert(ret)
							 j= updateBillServerStatus(ret,billId,fileName,j,a,i);
							    }
							catch(e)
							    {
							alert(e.message + ",errno:" + e.number);
							    }	
								
					  }
					});
			return j;
			
		}
	function updateBillServerStatus(statas,billId,fileName,j,a,i){
		//alert(statas);
		$.ajax({
							   type: "POST",
							   async:false, 
							   cache:false,
							   url: 'updatePrintServerResult.action',
							   data:{data:statas,billId:billId,fileName:fileName},
							   dataType: "html",
							   success : function(result){
								   if(result=="success"){
									   j++;
								   }
								 	 if(result!="success"){
									   alert(result);
									   j="error";
							   	submitAction(document.forms[0], "listBillPrint.action??paginationList.showCount="+"false");
							   	 document.forms[0].action="listBillPrint.action?paginationList.showCount="+"false";
							   	 return j;
								 }else{
								  if(a==i){
									  
									    a=a+1;
								alert("打印"+a+"条!打印成功"+j+"条");
							submitAction(document.forms[0], "listBillPrint.action??paginationList.showCount="+"false");
							   document.forms[0].action="listBillPrint.action?paginationList.showCount="+"false";
									
								  } 
								  }
							   },
							   error:function(){
							      alert("根据OCX返回结果更新发票状态过程出现异常!");
							   }
							 })
							 return j;
							
	}
	
	</script>
	<script type="text/javascript">
				var faPiaoType=0;
		// [查询]按钮
		function submitQuery(){
		
				submitAction(document.forms[0], "listBillPrint.action??paginationList.showCount="+"false");
							   	 document.forms[0].action="listBillPrint.action?paginationList.showCount="+"false";
			
		}
		// [打印发票]按钮
		function print(){
			if(checkChkBoxesSelected("selectBillIds")){
				submitAction(document.forms[0], "printBill.action");
				document.getElementById('BtnView').disabled=true;
			}else{
				alert("请选择发票记录!");
			}
		}
		
		function printNew(){
			
				if (!checkChkBoxesSelected("selectBillIds")) {
				alert("请选择票据记录");
				return false;
			}
			checkDiskAndSelver("",printNewSelver())
		}
				/** 税控服务器版****/
		function printNewSelver(){
			try{
			var printLimitValue=document.getElementById("printLimitValue").value;
			 var  faPiaoType=document.getElementById("billInfo.fapiaoType").value;
			 var fileName="发票打印.xml";
			 	var billIds = document.getElementsByName("selectBillIds");
					var ids = "";
					var selectNum=0;
				for (var i = 0; i < billIds.length; i++){
					if (billIds[i].checked){
						ids = ids === "" ? billIds[i].value : ids + "," + billIds[i].value;
						selectNum++;
					}
				}
				showOcxSelver(ids,faPiaoType,printLimitValue,fileName)
			}catch(e){
			
			alert(e.message + ",errno:" + e.number);

			
			}}
		function printNewDisk(){
			var printLimitValue=document.getElementById("printLimitValue").value;
			 var  faPiaoType=document.getElementById("billInfo.fapiaoType").value;
				
			if (checkChkBoxesSelected("selectBillIds")) {
				try{
				var taxDiskNo = DocCenterCltObj.FunGetPara('','taxDiskNo');
				// alert(taxDiskNo);
				var arr=new Array();
				arr=taxDiskNo.split("|");
				if(arr[0]==0){
					alert("请连接税控盘");
					return false;
				}
			
				taxDiskNo=arr[1];
				//alert(taxDiskNo);
				//return false;
				
					var billIds = document.getElementsByName("selectBillIds");
					var ids = "";
					var selectNum=0;
				for (var i = 0; i < billIds.length; i++){
					if (billIds[i].checked){
						ids = ids === "" ? billIds[i].value : ids + "," + billIds[i].value;
						selectNum++;
					}
				}
				 $.ajax({
					   async:false,
					   type: "POST",
					   cache:false,
					   url: "showLock.action",
					   data:{taxDiskNo:taxDiskNo,fapiaoType:faPiaoType},
					   dataType:"text",
					   error: function(){
							return false;},
					   success : function(result){
								if (result == ''){
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
							} else{
							
								 showOcxString(result,ids,faPiaoType,printLimitValue);
							}
					  }
					});
					document.forms[0].action="listBillPrint.action?paginationList.showCount="+"false";
				}catch(e){
					alert("请安装税控盘插件！");
				}
				 } else {
				alert("请选择票据记录！");
			}
		}
		function showOcxString(result1,ids,faPiaoType,printLimitValue){
			$.ajax({
							   type: "POST",
							   async:false,
							   cache:false,
							   url: 'showOCXstring.action',
							   data:{billIds:ids,faPiaoType:faPiaoType,printLimitValue:printLimitValue},
							   dataType: 'html',
							   success : function(result){
							   if(result=='printValueError'){
							   	 alert("单次打印超过限定的值"+printLimitValue+"请重新输入");
							   	 return false;
							    }
							  var arr= new Array();
							arr = result.split("+");
					    	var ocxString=arr[0];
					    	var billNo=arr[1];
					   		var sum=arr[2];
					    	if(confirm(billNo+"\n是否打印这些发票?")){
    							ocxString=result1+'|'+sum+'|'+ocxString;
    							///alert(ocxString);
					    		var statas=DocCenterCltObj.FunGetPara(ocxString,'printInvoice');
					    		//alert(statas);
					    		updateBillStatus(statas);
    							}
							   },
							   error:function(){
							      alert("获取组装传进OCX的字符串过程出现异常!");
							   }
							 });
			document.forms[0].action="listBillPrint.action?paginationList.showCount="+"false";
		}
	function updateBillStatus(statas){
		$.ajax({
							   type: "POST",
							   async:false, 
							   cache:false,
							   url: 'updatePrintResult.action',
							   data:{resultOCX:statas},
							   dataType: "html",
							   success : function(result){
							
							   	  // clearInterval(intervalID);
							    	alert(result);
							   	 location.reload();
							 
							  
							   },
							   error:function(){
							      alert("根据OCX返回结果更新发票状态过程出现异常!");
							   }
							 });
							 document.forms[0].action="listBillPrint.action?paginationList.showCount="+"false";
	}
		// 增值税专用发票    货物运输业增值税专用发票  机动车销售统一发票  XML导出
		function billsToXml(obj){
			var findz = document.getElementById('findz').value;
			var findp = document.getElementById('findp').value;
			if(obj=="1" && findz =="1"){
				submitAction(document.forms[0], "billsToXml.action?billType="+obj);
				document.forms[0].action="listBillPrint.action";
			}else if(obj=="4" && findp =="1"){
				submitAction(document.forms[0], "billsToXml.action?billType="+obj);
				document.forms[0].action="listBillPrint.action";
			}else{
				alert("无记录!");
			}
		}
		function toExcel(){
			submitAction(document.forms[0], "xxsToExcel.action");
			document.forms[0].action="listBillPrint.action";
		}
		
		function toExcelNew(){
			submitAction(document.forms[0], "xxsToExcelNew.action");
			document.forms[0].action="listBillPrint.action";
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

		}
	
		function queryYupiaoTest(){
		//	a)	操作代码：surplusInvoice
//b)	输入：注册码|税控盘口令|证书口令|发票类型-0（专）1（普）
				//taxDiskNo;taxDiskPas;certificatePas;faPiaoType;
		var taxDiskNo = DocCenterCltObj.FunGetPara('','taxDiskNo');
		alert(taxDiskNo);
		 var  faPiaoType=document.getElementById("billInfo.fapiaoType").value;
		 $.ajax({
					   async:false,
					   type: "POST",
					   cache:false,
					   url: "getInvoiceNum.action",
					   data:{taxDiskNo:taxDiskNo,fapiaoType:faPiaoType},
					   dataType:"text",
					   error: function(){
							return false;},
					   success : function(result){
								if (result == ''){
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
							} else{
								alert(result);
							var statas=DocCenterCltObj.FunGetPara(result,'surplusInvoice');
								alert(statas);
			
							}
					  }
					});
					document.forms[0].action="listBillPrint.action?paginationList.showCount="+"false";
	
			
			
			
		}
		
	
	
		function monitorData(){
			//纳税人识别号|税控盘号|税控盘口令|发票类盘型(0:专,1:普)
			var taxNo='500102010003760';
			var inputdata=taxNo+'|'+'499000135701'+'|'+taxDiskPas+'|'+0;
			alert(inputdata);
				var statas=DocCenterCltObj.FunGetPara(inputdata,'monitorDataQuery');
				alert(statas);
		}//500102010003760|499000135701|88888888|0
		function taxDiskinfo(){
			var inputdata='88888888';
				var statas=DocCenterCltObj.FunGetPara(inputdata,'DataQuery');
				alert(statas);
			
			
		}
		function taxItems(){
		
		//	b)	输入：纳税人识别号|税控盘号|税控盘口令|发票类型 b)	输入：纳税人识别号|税控盘号|税控盘口令|发票类型
				var taxNo='500102010003760';
				var inputdata=taxNo+'|'+'499000135701'+'|'+taxDiskPas+'|'+faPiaoType;
				alert(inputdata);
				var statas=DocCenterCltObj.FunGetPara(inputdata,'taxesQuery');
				alert(statas);
				//c)	输出：发票类型|税种税目索引号^税种税目代码^税率^税种名称^税目名称^|
		}
		function buyInvoice(){// 纳税人识别号|税控盘号|税盘口令|发票类型  
				//uyInvoiceMsgb)	输入：注册码|税控盘口令|证书口令|发票类型-0（专）1（普）
	var taxDiskNo = DocCenterCltObj.FunGetPara('','taxDiskNo');
		alert(taxDiskNo);
		 var  faPiaoType=document.getElementById("billInfo.fapiaoType").value;
		 $.ajax({
					   async:false,
					   type: "POST",
					   cache:false,
					   url: "getInvoiceNum.action",
					   data:{taxDiskNo:taxDiskNo,fapiaoType:faPiaoType},
					   dataType:"text",
					   error: function(){
							return false;},
					   success : function(result){
								if (result == ''){
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
							} else{
								alert(result);
						var statas=DocCenterCltObj.FunGetPara(result,'buyInvoiceMsg');
								alert(statas);
			
							}
					  }
					});
					document.forms[0].action="listBillPrint.action?paginationList.showCount="+"false";
				
			//	b)	输入：纳税人识别号|税控盘号|税盘口令|发票类型
//c)	输出：发票类型|当前发票代码|当前发票号码|总剩余份数|发票代码^发票起始号码^发票终止号码^发票领购份数^剩余份数^领购日期^领购人员^|
				
		}
		function configPrint(){
			var topMar='3';
			var leftMar='-8';
				var inputdata=faPiaoType+'|'+topMar+'|'+leftMar;
				var statas=DocCenterCltObj.FunGetPara(inputdata,'configPrint');
				alert(statas);
		}
		
	</script>
	<style type="text/css">
		.style1{
			width: 242px;
		}
	</style>
</head>
<body onkeydow="enterkey(event)">
	<form name="Form1" method="post"  id="Form1">
	<input type="hidden" name="taxParam" id="taxParam" value="<s:property value='taxParam'/>"/>
	<%
		String currMonth = (String) request.getAttribute("currMonth");
		String projectCode = (String) request.getAttribute("projectCode");
		String SKPXXCX = FileUtil.createXmlStringForSKPXXCX(FileUtil.skpkl);
		String res = "";
		System.out.println("SKPXXCX:");
		System.out.println(SKPXXCX);
		
		String findz = (String) request.getAttribute("zbillInfo");
		String findp = (String) request.getAttribute("pbillInfo");
	%>
	<input type="hidden" id="printLimitValue" name="printLimitValue" value="<s:property value="printLimitValue"/>">
	<input type="hidden" name="currMonth" id="currMonth" value="<%=currMonth%>"/>
	<input type="hidden" name="reasionInfo" id="reasionInfo" value=""/>
	<input type="hidden" name=findz id="findz" value="<%=findz%>"/>
	<input type="hidden" name="findp" id="findp" value="<%=findp%>"/>
	<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
	<tr>
		<td class="centercondition">
		<s:component template="rocketMessage" />
		<div id="tbl_current_status">
			<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> 
			<span class="current_status_menu">当前位置：</span> 
			<span class="current_status_submenu1">销项税管理</span>
			<span class="current_status_submenu">发票管理</span>
			<span class="current_status_submenu">发票打印</span>
		</div>
		<div class="widthauto1">
		<table id="tbl_query" cellpadding="0" cellspacing="0" width="80%" border="0">
		<tr>
			<td align="left">
				开票日期
			</td>
			<td>
				<input class="tbl_query_time" type="text" name="billInfo.billBeginDate"
					value="<s:property value='billInfo.billBeginDate'/>"
					onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'billInfo.billEndDate\')}'})"
					size='11'"/>
				<input class="tbl_query_time" type="text" name="billInfo.billEndDate"
					value="<s:property value='billInfo.billEndDate'/>"
					onfocus="WdatePicker({minDate:'#F{$dp.$D(\'billInfo.billBeginDate\')}'})"
					size='11'"/>
			</td>
			
			<td align="left">
				客户名称
			</td>
			<td>
				<input type="text" class="tbl_query_text" name="billInfo.customerName" value="<s:property value='billInfo.customerName'/>" maxlength="100"/>
			</td>
			
			<td align="left">
				发票类型
			</td>
			<td>
				<select id="billInfo.fapiaoType" name="billInfo.fapiaoType"  style="width:120px">
				<option value="0" <s:if test='billInfo.fapiaoType=="0"'>selected</s:if><s:else></s:else>>增值税专用发票</option>
				<option value="1" <s:if test='billInfo.fapiaoType=="1"'>selected</s:if><s:else></s:else>>增值税普通发票</option>
			</select>
			</td>
			
		</tr>
		<tr>
			<td align="left">
				是否手工录入
			</td>
			<td>
				<select id="billInfo.isHandiwork" name="billInfo.isHandiwork"  style="width:120px">
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
				<select id="billInfo.issueType" name="billInfo.issueType"  style="width:120px">
				<option value="" <s:if test='billInfo.issueType==""'>selected</s:if><s:else></s:else>>全部</option>
				<option value="1" <s:if test='billInfo.issueType=="1"'>selected</s:if><s:else></s:else>>单笔</option>
				<option value="2" <s:if test='billInfo.issueType=="2"'>selected</s:if><s:else></s:else>>合并</option>
				<option value="3" <s:if test='billInfo.issueType=="3"'>selected</s:if><s:else></s:else>>拆分</option>
			</select>
			</td>
			
			<td align="left">
				状态
			</td>
			<td>
				<select id="billInfo.dataStatus" name="billInfo.dataStatus"  style="width:120px">
				<option value="" <s:if test='billInfo.dataStatus==""'>selected</s:if><s:else></s:else>>全部</option>
				<option value="5" <s:if test='billInfo.dataStatus=="5"'>selected</s:if><s:else></s:else>>已开具未上传</option>
				<option value="6" <s:if test='billInfo.dataStatus=="6"'>selected</s:if><s:else></s:else>>已开具已上传</option>
				<option value="9" <s:if test='billInfo.dataStatus=="9"'>selected</s:if><s:else></s:else>>打印失败</option>
			</select>
			</td>
			<td style="width:80px;" align="right">
				<input type="button" class="tbl_query_button" value="查询"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="BtnView" id="BtnView" onclick="submitQuery()" />
			</td>
		</tr>
		</table>
		</div>
		<table id="tbl_tools" width="100%" border="0">
		<tr>
			<td align="left">
				<a href="#" name="BtnView" id="BtnView" onclick="createBillPrint()"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1021.png"/>打印发票</a>
				<%--<a href="#" name="BtnView" id="BtnView" onclick="queryYupiaoTest()"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1022.png"/>剩余票数查询</a>
				<a href="#" name="BtnView" id="BtnView" onclick="queryTexDiskNo()"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1023.png"/>税控盘号查询</a>
				<a href="#" name="BtnView" id="BtnView" onclick="monitorData()" ><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1024.png"/>监控数据查询</a>
				<a href="#" name="BtnView" id="BtnView" onclick="taxDiskinfo()"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1025.png"/>稅控盘信息查询</a>
				<a href="#" name="BtnView" id="BtnView" onclick="taxItems()" ><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1026.png"/>税种税目查询</a>
				<a href="#" name="BtnView" id="BtnView" onclick="buyInvoice()"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1027.png"/>购票信息查询</a>
				<a href="#" name="BtnView" id="BtnView" onclick="configPrint()"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1028.png"/>边距调整</a>
				--%><!-- <input type="button" class="tbl_query_button" value="打印发票"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="BtnView" id="BtnView" onclick="printNew()" />
				<input type="button" class="tbl_query_button" value="剩余票数查询"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="BtnView" id="BtnView" onclick="queryYupiaoTest()" />
				<input type="button" class="tbl_query_button" value="税控盘号查询"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="BtnView" id="BtnView" onclick="queryTexDiskNo()" />
				<input type="button" class="tbl_query_button" value="监控数据查询"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="BtnView" id="BtnView" onclick="monitorData()" />
				<input type="button" class="tbl_query_button" value="稅控盘信息查询"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="BtnView" id="BtnView" onclick="taxDiskinfo()" />
				<input type="button" class="tbl_query_button" value="税种税目查询"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="BtnView" id="BtnView" onclick="taxItems()" />
				<input type="button" class="tbl_query_button" value="购票信息查询"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="BtnView" id="BtnView" onclick="buyInvoice()" />
				<input type="button" class="tbl_query_button" value="边距调整"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="BtnView" id="BtnView" onclick="configPrint()" />  -->
				
			<%--<%
				if ("MUFG".equals(projectCode)){
			%>
					onclick="toExcel()" />
					
					<input type="button" class="tbl_query_button" value="增值税专票导出"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="BtnView" id="BtnView"  onclick="billsToXml('1')" />
			 
					<input type="button" class="tbl_query_button" value="增值税普票导出"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="BtnView" id="BtnView" onclick="billsToXml('4')" />
					
			<%
				} else if ("HSBC".equals(projectCode)){
			%>
					onclick="printNew()" />
					
					<input type="button" class="tbl_query_button" value="导出EXCEL"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="BtnView" id="BtnView" onclick="toExcelNew()" />
			<%
				}
			%>--%>
			</td>
		</tr>
		</table>
	
	<div id="lessGridList4" style="width: 100%; height:360px;overflow: auto;">
		<table class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0" style="border-collapse: collapse; width: 100%;">
		<tr class="lessGrid head">
			<th style="text-align:center">
				<input style="width:13px;height:13px;" id="CheckAll" type="checkbox" onclick="cbxselectall(this,'selectBillIds')" />
			</th>
			<th style="text-align:center">序号</th>
			<th style="text-align:center">开票日期</th>
			<th style="text-align:center">客户名称</th>
			<th style="text-align:center">客户纳税人识别号</th>
			<th style="text-align:center">发票代码</th>
			<th style="text-align:center">发票号码</th>
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
					String billAuditStatus = "";
	%>
		<td align="center">
			<input style="width:13px;height:13px;" type="checkbox" id="selectBillIds" name="selectBillIds" value="<%=BeanUtils.getValue(bill,"billId")%>" />
			<input type="hidden" name="selectBillDates" value="<%=bill.getBillDate()%>"/>
			<input type="hidden" name="billId" value="<%=bill.getBillId()%>"/>
		</td>
		<td align="left"><%=i+1%></td>
		<td align="left"><%=bill.getBillDate()%></td>
		<td align="left"><%=bill.getCustomerName()%></td>
		<td align="left"><%=bill.getCustomerTaxno()%></td>
		<td align="left"><%=bill.getBillCode()%></td>
		<td align="left"><%=bill.getBillNo()%></td>
		<td align="left"><%=NumberUtils.format(bill.getAmtSum(),"",2)%></td>
		<td align="left"><%=NumberUtils.format(bill.getTaxAmtSum(),"",2)%></td>
		<td align="left"><%=NumberUtils.format(bill.getSumAmt(),"",2)%></td>
		<td align="left"><%=(bill.getIsHandiwork()==null)?"":bill.getIsHandiwork().equals("1")?"自动开票":bill.getIsHandiwork().equals("2")?"人工审核":"人工开票"%></td>
		<td align="left"><%=(bill.getIssueType()==null)?"":bill.getIssueType().equals("1")?"单笔":bill.getIssueType().equals("2")?"合并":"拆分"%></td>
		<td align="left"><%=(bill.getFapiaoType()==null)?"":bill.getFapiaoType().equals("0")?"增值税专用发票":"增值税普通发票"%></td>
		<td align="left"><%=(bill.getDataStatus()==null)?"":bill.getDataStatus().equals("5")?"已开具":bill.getDataStatus().equals("6")?"已开具":"打印失败"%></td> 
		<td align="center">
				<a href='<%=webapp%>/inputBillTrans.action?billId=<%=bill.getBillId()%>'>
			    <img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1014.png" title="查看" style="border-width: 0px;"/></a>
			    <a href="javascript:void(0);" 
			onClick="OpenModalWindowSubmit('viewImgFromBillPrint.action?billId=<%=bill.getBillId()%>',1000,650,true)">
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
	<tr><input type="hidden" name="paginationList.showCount" value="false">
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