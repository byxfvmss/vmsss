<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../page/include.jsp"%>
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
<link href="<%=webapp%>/page/common.css" type="text/css" rel="stylesheet">

<script type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/search.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
<meta http-equiv="Pragma" content="no-cache" />
<title>税控盘监控信息</title>

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
	function reset(url){
		//OpenModalWindow(url,650,350,true);
		location.href=url;
	}
	function submitForm(actionUrl){
		var form = document.getElementById("main");
		form.action=actionUrl;
		form.submit();
		form.action="listTaxDiskMonitor.action";
	}
	
	function deleteTransBatch(actionUrl){
	
		var form = document.getElementById("main");
		form.action=actionUrl;
		form.submit();
		form.action="listTaxDiskMonitor.action";
	}
	function setTaxperList(authInst){
		if(authInst==""){
			authInst = "";
		}
		//转action查询信息
		$.ajax({url: 'findInfoTaxNo.action',
			type: 'POST',
			async:false,
			data:{authInst:authInst},
			dataType: 'text',
			error: function(){return false;},
			success: function(result){
					document.getElementById("taxpayerNo").options.length=0;
					var p = new Array();
					p = result.split(",");
					for(var i=0;i<p.length;i++){
						if(result==""||result==null){
							document.getElementById("taxpayerNo").options.add(new Option("所有",""));
						}else{
							if(authInst==""){
								document.getElementById("taxpayerNo").options.add(new Option("所有",""));
							}
							document.getElementById("taxpayerNo").options.add(new Option(p[i]));
						}
					}
			}
		});
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
		oRangeRef1.execCommand( "Copy" );
		
		
		//粘贴 
		try{ 
		appExcel.Visible = true; 
		appExcel.Worksheets.Item(2).Paste(); 
		appExcel1 = null; 
		}catch(e){ 
		alert("使用此功能必须在浏览器中设置:Internet选项->安全->将本站加入“受信任的站点”。"); 
		} 
	}
	
	
</script>
</head>
<body>
<form id="main" action="<c:out value='${webapp}'/>/listTaxDiskMonitor.action" method="post">
<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
	<tr>
		<td class="centercondition">
		<div id="tbl_current_status">
			<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> 
			<span class="current_status_menu">当前位置：</span> 
			<span class="current_status_submenu1">参数管理</span>
			<span class="current_status_submenu">税控设备管理</span>
			<span class="current_status_submenu">税控盘监控信息管理</span>
		</div>
		<div class="widthauto1">
			<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td style="text-align: right;" width="10%">机构</td> 
					<td width="30%">
						<s:if test="authInstList != null && authInstList.size > 0">
					<s:select style="width:150px" id = "instId" name="instId" list="authInstList" 
					listKey='instId' listValue='instName' headerKey="" headerValue="所有" 
					onchange="setTaxperList(this.value)"/>
					</s:if>
					<s:if test="authInstList == null || authInstList.size == 0">
					<select style="width:150px" name="instId" class="readOnlyText">
					<option value="">请分配机构权限</option>
					</select>
					</s:if>
					</td>
					<td style="text-align: right;" width="10%">纳税人识别号</td> 
					<td width="30%">
						<s:if test="taxperLists != null && taxperLists.size > 0">
						<s:select style="width:150px" name="taxPerNumber" id = "taxpayerNo" list="taxperLists"
					 	listKey='tanNo' listValue='tanNo' headerKey="" headerValue="所有" />
						</s:if>
						<s:if test="taxperLists == null || taxperLists.size == 0">
						<select style="width:150px" name="taxPerNumber" >
						<option value="">所有</option>
						</select>
						</s:if>
					</td>
					<td width="20%">
						<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" 
						onMouseOut="this.className='tbl_query_button'"  onclick="submitForm('listTaxDiskMonitor.action');" 
						name="cmdQueryBtn" value="查询" id="cmdQueryBtn" />		
					</td>
				</tr>
			</table>
		</div>
		<table id="tbl_tools" width="100%" border="0">
			<tr>
				<td align="left">
				<a href="#" name="cmdAddBtn" id="cmdAddBtn"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon16.png"/>新增</a>
				<a href="#" name="cmdDelbt" id="cmdDelbt" ><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1019.png"/>删除</a>
				<a href="#" name="cmdExpbt" id="cmdExpbt"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png"/>导出</a>
				</td>
			</tr>
		</table>
		<div id="lessGridList1" style="overflow: auto; width: 100%;">
		<table  class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0"  style="border-collapse: collapse;width: 100%; margin-top:5px;">
			<tr class="lessGrid head gridbr">
			<th width="3%" style="text-align:center"><input id="CheckAll" style="width:13px;height:13px;" type="checkbox" onClick="checkAll(this,'tax_no_type_id')" /></th>
				<th style="text-align: center">序号</th>
				<th style="text-align: center;">操作</th>
				<th style="text-align: center">税控盘号</th>
				<th style="text-align: center">发票类型</th>
				<th style="text-align: center">开票截止日期</th>
				<th style="text-align: center">数据报送起始日期</th>
				<th style="text-align: center">数据报送终止日期</th>
				<th style="text-align: center">单张发票开票金额限额(元)</th>
				<th style="text-align: center">正数发票累计金额限额(元)</th>
				<th style="text-align: center">负数发票累计金额限额(元)</th>
				<th style="text-align: center;">负数发票标志</th>
				<th style="text-align: center;">负数发票天数</th>
				<th style="text-align: center;">最新报税日期</th>
				<th style="text-align: center;">剩余容量</th>
				<th style="text-align: center;">上传截止日期</th>
				<th style="text-align: center;">限定功能标识</th>
				<th style="text-align: center;">离线开票时长</th>
				<th style="text-align: center;">离线开票张数</th>
				<th style="text-align: center;">离线正数累计金额</th>
				<th style="text-align: center;">离线负数累计金额</th>
				<th style="text-align: center;">离线扩展信息</th>
			</tr>
			<s:iterator value="paginationList.recordList" id="iList" status="stuts">
				
				<tr align="center"class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>" >
				<td><input type="checkbox" style="width:13px;height:13px;" name="tax_no_type_id" value="<s:property value="#iList.taxDiskNo"/>_<s:property value="#iList.fapiaoType"/>"/></td>
				    <td align="center"><s:property value='#stuts.count' /></td>	
				    <td>
						<a href="javascript:;" class="edit_tax_disk_monitor_bt"  tdno="<s:property value="#iList.taxDiskNo"/>" fptype="<s:property value="#iList.fapiaoType"/>" >
							<img src ="<c:out value="${bopTheme}"/>/themes/images/icons/icon1005.png" title="编辑" style="border-width: 0px;" />
						</a>
					</td>
					<td align="center"><s:property value='#iList.taxDiskNo' /></td>		
					<td align="center"><s:property value='mapVatType[#iList.fapiaoType]' /></td>		
					<td><s:property value='#iList.billEndDateS'/></td>
					<td><s:property value="#iList.dataRepStrDateS"/></td>
					<td><s:property value='#iList.dataRepEndDateS'/></td>
					<td><s:property value='#iList.billLimitAmtS'/></td>
					<td><s:property value="billLimitAmtPS"/></td>
					<td><s:property value='billLimitAmtNS'/></td>
					<td><s:if test='#iList.nBillFlgS == 1'>在盘内</s:if><s:else>不在盘内</s:else></td>
					<td><s:property value='#iList.nBilDayS'/></td>
					<td><s:property value='#iList.newReportDateS'/></td>
					<td><s:property value='#iList.residualCapacityS'/></td>
					<td><s:property value='#iList.uploadDeadlineS'/></td>
					<td><s:property value='#iList.limitFunctionS'/></td>
					<td><s:property value='#iList.offLineDayS'/></td>
					<td><s:property value='#iList.offLineBillS'/></td>
					<td><s:property value='#iList.offLineAmtPS'/></td>
					<td><s:property value='#iList.offLineAmtNS'/></td>
					<td><s:property value='#iList.offLineOtsS'/></td>
				</tr>
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
<script type="text/javascript">
	$(function(){
		$("#cmdAddBtn").click(function(){
			OpenModalWindow("<c:out value='${webapp}'/>/initAddTaxDiskMonitor.action",600,620,true);
		});
		$(".edit_tax_disk_monitor_bt").click(function(){
			taxDiskNo=$(this).attr("tdno");
			fapiaoType=$(this).attr("fptype");
			OpenModalWindow("<c:out value='${webapp}'/>/editTaxDiskMonitor.action?taxDiskNo="+taxDiskNo+"&fapiaoType="+fapiaoType,600,620,true);
		});
		$("#cmdDelbt").click(function(){
			$del_items=$("input[name='tax_no_type_id']:checked");
			if($del_items.size()==0){
				alert("请选择您要删除的数据！");
				return false;
			}
			if(confirm("您确定要删除吗？")){
				submitForm('deleteTaxDiskMonitor.action');
			}
		});
		$("#cmdExpbt").click(function(){
			submitForm('expTaxDiskMonitor.action');
			//document.forms[0].action = 'expTaxDiskMonitor.action';
			//document.forms[0].submit();
		});
	});
</script>
</html>
