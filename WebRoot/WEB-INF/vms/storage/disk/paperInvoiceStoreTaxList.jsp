<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../../../page/include.jsp" %>
<OBJECT id='DocCenterCltObj' width=100 height=100 classid="clsid:1E25664C-EA53-4F39-8575-684737626D6F" style="display: none;"></OBJECT>
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">

<script type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>

<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/taxSelver/validatorTax.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/taxSelver/billbuyQuery.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/taxSelver/syncTaxSelvlet.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/taxSelver/taxKeyQuery.js"></script>
<meta http-equiv="Pragma" content="no-cache" />
<title>发票统计</title>

<style type="text/css">
.detailInfoDIV {
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
</style>
<script type="text/javascript">
	function openPaperInvoice(url){
		OpenModalWindow(url,650,350,true);
	}
	// 打开空白发票作废页面
	function invalidBlankPaperInvoice(){
		var url = 'invalidBlankPaperInvoice.action';
		OpenModalWindow(url,650,250,true);
	}
	function submitForm(actionUrl){
		var form = document.getElementById("main");
		form.action=actionUrl;
		form.submit();
		form.action="listPageTaxInvoice";
	}
	function deleteTransBatch(actionUrl){
	
		var form = document.getElementById("main");
		form.action=actionUrl;
		form.submit();
	}
  function hideDetailInfoDIV(){
		document.getElementById("detailInfoDIV").style.display='none';
	}

	function showDetailInfoDIV(logID){
	 	var currRow =  window.event.srcElement.parentElement.parentElement;// 获取当前行

	 	document.getElementById("_td2").innerHTML=logID;

	 	for(var i=3;i<16;i++){
	 		document.getElementById("_td"+i).innerHTML=currRow.cells[i-1].title;
	 	}
	 	
	 	document.getElementById("detailInfoDIV").style.display='block';
	}
	
	function output() {
		//拷贝 
		var elTable = document.getElementById("lessGridList"); //这里的page1 是包含表格的Div层的ID
		var oRangeRef = document.body.createTextRange();
		oRangeRef.moveToElementText( elTable );
		oRangeRef.execCommand("Copy");
		
		
		//粘贴 
		try{
			var appExcel = new ActiveXObject( "Excel.Application" ); 
			appExcel.Visible = true; 
			appExcel.Workbooks.Add().Worksheets.Item(1).Paste(); 
		//appExcel = null; 
		}catch(e){
			alert("使用此功能必须在浏览器中设置:Internet选项->安全->将本站加入“受信任的站点”。"); 
		}
		
		
		var elTable1 = document.getElementById("lessGridList"); 
		var oRangeRef1 = document.body.createTextRange(); 
		oRangeRef1.moveToElementText( elTable1 ); 
		oRangeRef1.execCommand("Copy");
		
		
		//粘贴 
		try{ 
		appExcel.Visible = true; 
		appExcel.Worksheets.Item(2).Paste(); 
		appExcel1 = null; 
		}catch(e){ 
		alert("使用此功能必须在浏览器中设置:Internet选项->安全->将本站加入“受信任的站点”。"); 
		} 
	}
	$(function(){
		$("#cmdDistribute").click(function(){
			$checkedStore=$("input[name='paper_invoice_stock_id']:checked");
			if($checkedStore.size()==0){
				alert("请选择您要分发的库存发票");
				return;
			}
			$store_ids="";
			for($idx=0;$idx<$checkedStore.size();$idx++){
				if(($idx+1)!=$checkedStore.size()){
					$store_ids+=$($checkedStore[$idx]).val()+",";
				}else{
					$store_ids+=$($checkedStore[$idx]).val();
				}
			}
			OpenModalWindow("<c:out value='${webapp}'/>/initDistrubute.action?paper_invoice_stock_ids="+$store_ids,800,600,true);
		});
	});
	/*删除未分发的票据信息*/
function deletePaperInvoice(){
			var billIds = document.getElementsByName("paper_invoice_stock_id");
			var ids="";
			for (var i = 0; i < billIds.length; i++){
					if (billIds[i].checked){
						ids = ids === "" ? billIds[i].value : ids + "," + billIds[i].value;
						
					}
				}
			alert(ids);
	$.ajax({url: 'deletePaperInvoice.action',
						type: 'POST',
						async:false,
						data:{billIds:ids},
						dataType: 'text',
					//	timeout: 1000,
						error: function(){
							return false;},
						success: function(result){
							if(result=="success"){
								alert("删除成功");
							}
							if(result=="error"){
								alert("只能删除未分发信息请检查信息");
							}
							if(result=="exception"){
								alert("系统异常 所选值为空");
							}
						}
					});
			
}
	function syncDiskTax(){
	checkDiskAndSelver(syncDiskTaxDisk(),"");
}
	function syncDiskTaxDisk(){
		// 稅控盘口令
		try{
				//数控盘查询盘号
				var taxDiskNo = DocCenterCltObj.FunGetPara('','taxDiskNo');
				// alert(taxDiskNo);
				var arr=new Array();
				arr=taxDiskNo.split("|");
				if(arr[0]==0){
					alert("请连接税控盘");
					return false;
				}
			
				taxDiskNo=arr[1];
			//	alert(taxDiskNo);
				$.ajax({url: 'getDiskInfo.action',
						type: 'POST',
						async:false,
						data:{taxDiskNo:taxDiskNo},
						dataType: 'text',
					//	timeout: 1000,
						error: function(){
							return false;},
						success: function(result){
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
							}else{
							//	alert(result);
								conncctionDiskTax(result,taxDiskNo);
							}
							}
					});
				
		}catch(e){
			alert("请安装税控盘插件！");
			}
		}
		function conncctionDiskTax(result,taxDiskNo){
		var arr=new Array();
		//alert(result);
		arr=result.split("|");
		//注册码
		var registeredInfo=arr[0];
		//税控盘口令
		var taxDiskpwd=arr[1];
		//证书口令
		var certpwd=arr[2];
		//alert(taxDiskpwd);
		//查询税控盘信息
		//税控盘编号|纳税人识别号|纳税人名称|税务机关代码|税务机关名称|发票类型代码|当前时钟|启用时间|版本号|开票机号|企业类型|保留信息|其它扩展信息
		//返回结果（0失败1成功）|返回信息（”成功”或者具体错误）|税控盘编号|纳税人识别号|纳税人名称|税务机关代码|税务机关名称|发票类型代码|当前时钟|启用时间|版本号|开票机号|企业类型|保留信息|其它扩展信息
		//	1						2							3			4				5	6					7		8			9		10		11	12			13		14		15			
		var DataQuery = DocCenterCltObj.FunGetPara(taxDiskpwd,'DataQuery');
		arr=DataQuery.split("|");
		if(arr[0]==0){
			alert(arr[1]);
			return false;
		}
		var diskNo=arr[2];
		var taxNo=arr[3];//b)	输入：注册码|纳税人识别号|税控盘号|税控盘口令|发票类型
		var dateTime=arr[8];
		var taxesQuery=registeredInfo+"|"+taxNo+"|"+diskNo+"|"+taxDiskpwd
		$.ajax({url: 'getTaxDiskInfo.action',
						type: 'POST',
						async:false,
						data:{DataQuery:DataQuery},
						dataType: 'text',
					//	timeout: 1000,
						error: function(){
							return false;},
						success: function(result1){
								if(result1=='success'){
									alert("税控盘信息保存成功");
									monTaxDisk(result,taxNo,dateTime,diskNo,taxesQuery);
									
								}else{
									alert(result1);
									return false;
								}
						}
							});
		//alert(certpwd);
		
	}
		//保存 库存信息
		
	//	a)	操作代码：buyInvoiceMsg
//b)	输入：注册码|税控盘口令|证书口令|发票类型-0（专）1（普）
//c)	输出：发票类型|当前发票代码|当前发票号码|总剩余份数|发票代码^发票起始号码^发票终止号码^发票领购份数^剩余份数^领购日期^领购人员^|
		function buyInvoiceMsg(result,taxNo,dateTime,diskNo,taxesQuery,maxamt0,maxamt1){
		var dataInfo0=result+"|"+0;
		var dataInfo1=result+"|"+1;
		var buyInvoiceMsg0 = diskNo+"|"+0+"|"+DocCenterCltObj.FunGetPara(dataInfo0,'buyInvoiceMsg');
		var buyInvoiceMsg1 = diskNo+"|"+1+"|"+DocCenterCltObj.FunGetPara(dataInfo1,'buyInvoiceMsg');
		
		//alert(dataInfo1)
		//alert(buyInvoiceMsg0);
		//alert(buyInvoiceMsg1);
	//注册码|纳税人识别号|税控盘号|税控盘口令|发票类盘型(0:专,1:普)
	$.ajax({url: 'saveTaxDiskStockInvoice.action',
						type: 'POST',
						async:false,
						data:{buyInvoiceMsg0:buyInvoiceMsg0,buyInvoiceMsg1:buyInvoiceMsg1,taxNo:taxNo,dateTime:dateTime,maxamt0:maxamt0,maxamt1:maxamt1},
						dataType: 'text',
					//	timeout: 1000,
						error: function(){
							return false;},
						success: function(result){
								//alert(result)
								if(result=='success'){
									alert("纸票库存信息保存成功");
									  taxeAQuery(taxesQuery,taxNo,diskNo);
								}else{
									alert(result);
									return false;
								}
						}
							});
		}
//保存税目信息
function taxeAQuery(taxesQuery,taxNo,diskNo){
		var data0= taxesQuery+"|"+0;
		var data1= taxesQuery+"|"+1;
	var taxesQuery0=taxNo+"|"+0+"|"+DocCenterCltObj.FunGetPara(data0,'taxesQuery');
	var taxesQuery1=taxNo+"|"+1+"|"+DocCenterCltObj.FunGetPara(data1,'taxesQuery');
	//alert(taxesQuery1);
		$.ajax({url: 'saveTaxInfo.action',
						type: 'POST',
						async:false,
						data:{taxesQuery0:taxesQuery0,taxesQuery1:taxesQuery1},
						dataType: 'text',
					//	timeout: 1000,
						error: function(){
							return false;},
						success: function(result){
								//alert(result)
								if(result=='success'){
									alert("税目信息保存成功");
									// monTaxDisk(taxesQuery,diskNo);
								}else{
									alert(result);
									return false;
								}
						}
							});
			 submitAction(document.forms[0], "listPageTaxInvoice.action");
		
		
	}
/*
 * 保存税控盘监控信息查询
 * */
 function monTaxDisk(result1,taxNo,dateTime,diskNo,taxesQuery){
	var data0=taxesQuery+"|"+"0";
	var data1=taxesQuery+"|"+"1";
	var mondata0=DocCenterCltObj.FunGetPara(data0,'monitorDataQuery');
	var mondata1=DocCenterCltObj.FunGetPara(data1,'monitorDataQuery');
	var maxamt0=getMaxAmt(mondata0);
	var maxamt1=getMaxAmt(mondata1);
	$.ajax({url: 'saveMonTaxDiskInfo.action',
						type: 'POST',
						async:false,
						data:{mondata0:mondata0,mondata1:mondata1,diskNo:diskNo},
						dataType: 'text',
					//	timeout: 1000,
						error: function(){
							return false;},
						success: function(result){
								//alert(result)
								if(result=='success'){
									alert("税控盘监控信息保存成功");
									// monTaxDisk(taxesQuery,diskNo);
									 buyInvoiceMsg(result1,taxNo,dateTime,diskNo,taxesQuery,maxamt0,maxamt1);
								}else{
									alert(result);
									return false;
								}
						}
							});
		
 }
function getMaxAmt(data){
	var arr=new Array();
	arr=data.split("|")
	if(arr[0]==1){
		return arr[6];
	}
} 
</script>
</head>
<body>
<form id="main" action="<c:out value='${webapp}'/>/listPageTaxInvoice.action" method="post">
<input type="hidden" name="taxParam" id="taxParam" value="<s:property value='taxParam'/>"/>
<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
	<tr>
		<td class="centercondition">
		<div id="tbl_current_status">
			<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> 
			<span class="current_status_menu">当前位置：</span> 
			<span class="current_status_submenu1">销项税管理</span>
			<span class="current_status_submenu">发票统计</span>
		</div>
		<div class="widthauto1">
			<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td width="72">纳税人识别号</td> 
					<td width="130">
						<input type="text" class="tbl_query_text2" name="paperListInfo.taxpayerNo" id="paperListInfo.taxpayerNo" value="<s:property value='paperListInfo.taxpayerNo'/>"/>
					</td>
					<td width="50">领购时间</td> 
					<td width="280"><input id="paperListInfo.receiveInvoiceTimeA" name="paperListInfo.receiveInvoiceTimeA" type="text"  value="<s:property value='paperListInfo.receiveInvoiceTimeA' />" class="tbl_query_time1"	 
					onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:document.getElementById('paperListInfo.receiveInvoiceEndTimeA').value})" />
					&nbsp;&nbsp;~&nbsp;&nbsp;
					<input id="paperListInfo.receiveInvoiceEndTimeA" name="paperListInfo.receiveInvoiceEndTimeA" type="text"  value="<s:property value='paperListInfo.receiveInvoiceEndTimeA' />" class="tbl_query_time1"	 
					onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:document.getElementById('paperListInfo.receiveInvoiceTimeA').value})" /></td>
					<td width="50">领购人员</td> 
					<td width="130"><input id="paperListInfo.receiveUserName" class="tbl_query_text2" name="paperListInfo.receiveUserName" type="text" value="<s:property value='paperListInfo.receiveUserName' />" /></td>
					<td width="50">发票类型</td> 
					<td width="130">
						<select id="paperListInfo.invoiceType" name="paperListInfo.invoiceType" ><option value="" <s:if test='paperListInfo.invoiceType==""'>selected</s:if><s:else></s:else>>所有</option>
							<s:iterator value="mapVatType" id="entry">  
							<option value="<s:property value="key"/>" <s:if test='paperListInfo.invoiceType==#entry.key'>selected</s:if><s:else></s:else>><s:property value="value"/></option>
					       </s:iterator>
						</select>
					</td>
					<td>
					<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 
					onclick="submitForm('listPageTaxInvoice.action');" name="cmdSelect" value="查询" id="cmdSelect" />				
					</td>
				</tr>
			</table>
		</div>
		<table id="tbl_tools" width="100%" border="0">
			<tr align="left">
				<td>
					<a href="#" onclick="syncDiskTax();" name="cmdInvalid" id="cmdInvalid"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1004.png"/>同步税控盘</a>
					<a href="#" onclick="submitForm('extortTaxInvoice.action');" name="cmdExcel" id="cmdExcel"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png"/>导出</a>
				</td>
	         </tr>
		</table><%-- 序号 	 管理机构代码 		 管理机构名称 	发票类型	发票号段	领购日期	 发放总量	 未使用 	 正常使用	 已使用作废 	 未使用作废	 红冲
		
		--%><div id="lessGridList1" style="overflow: auto; width: 100%;">
		<table  class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0"  style="border-collapse: collapse;width: 100%;">
			<tr class="lessGrid head">
			<th width="3%" style="text-align:center"><input id="CheckAll" style="width:13px;height:13px;" type="checkbox" onClick="checkAll(this,'batchIdList')" /></th>
				<th style="text-align: center">序号</th>
				<%--<th style="text-align: center">纳税人识别号</th>
				<th style="text-align: center">领购日期</th>
				<th style="text-align: center">领购人员</th>
				--%>
				<th style="text-align: center">管理机构代码</th>
				<th style="text-align: center">管理机构名称</th>
				<th style="text-align: center">发票类型</th>
				<th style="text-align: center">发票号码段</th>
				<th style="text-align: center">领购日期</th>
				<th style="text-align: center">发放总量</th>
				<th style="text-align: center">未使用</th>
				<th style="text-align: center">正常使用</th>
				<th style="text-align: center">已使用作废</th>
				<th style="text-align: center">未使用作废</th>
				<th style="text-align: center">红冲</th>
				<%--
				<%--
				<th style="text-align: center">已分发张数</th>
				
				<th style="text-align: center">总张数</th>
				<th style="text-align: center">已用张数</th>
				<th style="text-align: center">未用张数</th>// 数据库属性
	private String autoInvoiceId;	//	电子发票库存表ID
	private String taxpayerNo;//   纳税人识别号
	private String taxDiskNo;// 税控盘号
	private String	userId;//领购人员
	private String	receiveInvoiceTime;	//领购日期
	private String	invoiceType	;// 发票类型
	private String	currentInvoiceCode;//当前发票代码
	private String	currentInvoiceNo;//当前发票号码
	private String	invoiceCode;//发票代码
	private String	invoiceBeginNo;	//发票起始号码 
	private String	invoiceEndNo;//发票终止号码
	private String	invoiceNum;//发票份数
	private String	surplusNum;//剩余份数
	private String	instId;//机构id
	private String 	instName;//机构名字
	private String 	normalMakeInvoice;//正常开票数量
	private String 	blankWasteCancel;//空白作废数量
	private String 	issuedCancel;//开具作废数量
	private String 	redHedge;//红冲数量
				<%--<th style="text-align: center">已用百分比(%)</th>
				<th style="text-align: center">明细</th>
				--%><th style="text-align: center; display: none">操作状态</th>
			</tr>
			<s:iterator value="paginationList.recordList" id="iList" status="stuts">
				
				<tr align="center"class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
				    <td><input type="checkbox" name="paper_invoice_stock_id" value="<s:property value="#iList.paperInvoiceId"/>+','+<s:property value='distributeFlag'/>"/></td>
					<td align="center"><s:property value='#stuts.count' /></td>		
					<td><s:property value='instId'/></td>
					<td><s:property value='instName'/></td>
					<td>
						<s:iterator value="mapVatType" id="entry">  
							<s:if test='invoiceType==#entry.key'><s:property value="value"/></s:if>
						</s:iterator>
					</td>
					<td><s:property value='invoiceBeginNo'/>-<s:property value='invoiceEndNo'/></td> 
					<td><s:date name="receiveInvoiceTime" format="yyyy-MM-dd" /></td>
					<td><s:property value='invoiceNum'/></td>
					<td><s:property value='unUserdNum'/></td>
					<td><s:property value='normalMakeInvoice'/></td>
					<td><s:property value='issuedCancel'/></td>
					<td><s:property value='blankWasteCancel'/></td>
					<td><s:property value='redHedge'/></td>
					<%--<td>
						<s:iterator value="mapVatType" id="entry">  
							<s:if test='invoiceType==#entry.key'><s:property value="value"/></s:if>
						</s:iterator>
					</td>
					</td>
					<td><s:if test='paperListInfo.distributeNum==null'>0</s:if><s:else><s:property value='paperListInfo.distributeNum'/></s:else></td>
					<td><s:property value='invoiceNum'/></td>
					<td><s:property value='userdNum'/></td>
					<td><s:property value='unUserdNum'/></td>
				
					<td><s:if test='userRatioNum==null'></s:if><s:else><s:property value='userRatioNum'/>%</s:else></td>
					<td>
							<a href="viewpaperAutoInvoiceDetial.action?invoiceType=<s:property value="invoiceType"/>&taxDiskNo=<s:property value='taxDiskNo'/>&taxpayerNo=<s:property value='taxpayerNo'/>&invoiceCode=<s:property value="invoiceCode"/>&invoiceBeginNo=<s:property value="invoiceBeginNo"/>" ><img src ="<%=bopTheme2%>/img/jes/icon/view.png" alt="查看详情" style="border-width: 0px;" /></a>
					</td>
					<td style="display: none"><s:property value='batchId'/></td>
				--%></tr>
			</s:iterator> 
		</table>
		</div>
		<div id="anpBoud" align="Right" style="width:100%;">
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

