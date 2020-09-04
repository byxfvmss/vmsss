<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../page/include.jsp"%>
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
<link href="<%=webapp%>/page/common.css" type="text/css" rel="stylesheet">

<script src="<%=bopTheme%>/js/MessageBox/js/messageBox.js" type="text/javascript"></script>
<link href="<%=bopTheme%>/js/MessageBox/css/messageBox.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/search.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
<meta http-equiv="Pragma" content="no-cache" />
<title>附加税管理</title>

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
	// 打开空白发票作废页面
	function invalidBlankPaperInvoice(){
		var url = 'invalidBlankPaperInvoice.action';
		OpenModalWindow(url,650,250,true);
	}
	function submitForm(actionUrl){
		var form = document.getElementById("main");
		form.action=actionUrl;
		form.submit();
		form.action="listInSurtax.action";
	}
	
	function searchForm(actionUrl){
		if(CheckSurtaxRate(document.getElementById("surtaxRate"),"附加税税率必须是大于等于0小于1的小数")==false){
			return false;
		}
		var form = document.getElementById("main");
		form.action=actionUrl;
		form.submit();
		form.action="listInSurtax.action";
	}
	function CheckSurtaxRate(obj,strAlertMsg)
	{
		var strValue=obj.value;
		//if(strValue!="" && ((isNaN(strValue) || strValue>=1 || strValue<=0)
		//		||(!/^[0-9]+\.[0-9]{2}$/.test(strValue)))){	
		if(strValue!="" && (isNaN(strValue) || strValue>=1 || strValue<0)){	
			var m = new MessageBox(obj);
			m.Show(strAlertMsg);	
			obj.focus();
			return false;
		}else
		{
		   return true;
		}	
		
	}
	
	function deleteTransBatch(actionUrl){
	
		var form = document.getElementById("main");
		form.action=actionUrl;
		form.submit();
		form.action="listInSurtax.action";
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
<form id="main" action="<c:out value='${webapp}'/>/listInSurtax.action" method="post">
<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
	<tr>
		<td class="centercondition">
		<div id="tbl_current_status">
			<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> 
			<span class="current_status_menu">当前位置：</span> 
			<span class="current_status_submenu1">附加税管理</span>
			<span class="current_status_submenu">附加税管理</span>
			<span class="current_status_submenu">进项附加税</span>
		</div>

		<div class="widthauto1">
		<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td>纳税人识别号</td> 
				<td>
					<s:if test="taxperList != null && taxperList.size > 0">
					<s:select name="taxPerNumber" list="taxperList" listKey='taxperNumber' listValue='taxperNumber' headerKey="" headerValue="所有" onChange="getAjaxTaxperName(this.value, document.getElementById('taxperName'))"/>
					</s:if>
					<s:if test="taxperList == null || taxperList.size == 0">
					<select name="taxPerNumber" class="readOnlyText">
					<option value="">请分配机构权限</option>
					</select>
					</s:if>
				</td>
				<td>纳税人名称</td> 
				<td><input id="taxperName" class="tbl_query_text" name="taxperName" type="text" value="<s:property value='taxperName' />" />
					<div id="searchInstResult" style="display: none;"/></td>
				<td>附加税税率</td>
				<td><input id="surtaxRate" maxlength="4" name="surtaxRate" type="text" class="tbl_query_text" value="<s:property value='surtaxRate' />" class=""	 />&nbsp;&nbsp;&nbsp;</td>
			</tr>
			<tr>
				<td>附加税税种</td> 
				<td><select id="surtaxType" name="surtaxType" ><option value="" <s:if test='surtaxType==""'>selected</s:if><s:else></s:else>>全部</option>
					<s:iterator value="mapSurtaxAmtType" id="entry">  
					<option value="<s:property value="key"/>" <s:if test='surtaxType==#entry.key'>selected</s:if><s:else></s:else>><s:property value="value"/></option>
			       </s:iterator>
				</select>
				</td>
				<td>申报周期</td>
				<td><input id="applyPeriod" name="applyPeriod" type="text"  value="<s:property value='applyPeriod' />" class="tbl_query_time"	 onFocus="WdatePicker({dateFmt:'yyyy-MM'})" />&nbsp;&nbsp;&nbsp;</td>
				<td colspan="2"><input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" 
				onMouseOut="this.className='tbl_query_button'"  onclick="searchForm('listInSurtax.action');" 
				name="cmdDistribute" value="查询" id="cmdDistribute" />	</td>
			</tr>
		</table>
		</div>
		<!-- <table id="tbl_query" cellpadding="1" cellspacing="0">
			<tr align="left">
				<td style="text-align:left">
				<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" 
				onMouseOut="this.className='tbl_query_button'" onclick="reset('listInSurtax.action');" name="cmdFilter" value="重置" id="cmdFilter" />				
				</td>
				<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td style="text-align:left">
				<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" 
				onMouseOut="this.className='tbl_query_button'"  onclick="searchForm('listInSurtax.action');" 
				name="cmdDistribute" value="查询" id="cmdDistribute" />				
				</td>
				<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td style="text-align:left">
				<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 
				onclick="submitForm('inSurtaxExcel.action');" name="cmdExcel" value="导出" id="cmdExcel" />				
				</td>
	         </tr>
		</table> -->
			<table id="tbl_tools" width="100%" border="0">
				<tr>
					<td align="left">
					<a href="#" onclick="submitForm('inSurtaxExcel.action');"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png"/>导出</a>
					</td>
				</tr>
			</table>

<div id="lessGridList4" style="overflow: auto; width: 100%;">
<table  class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0"  style="border-collapse: collapse;width: 100%;">
	<tr class="lessGrid head">
	<th width="3%" style="text-align:center"><input id="CheckAll" style="width:13px;height:13px;" type="checkbox" onClick="checkAll(this,'batchIdList')" /></th>
		<th style="text-align: center">序号</th>
		<th style="text-align: center">纳税人识别号</th>
		<th style="text-align: center">纳税人名称</th>
		<th style="text-align: center">附加税类型</th>
<!-- 		<th style="text-align: center">附加税名称</th> -->
		<th style="text-align: center">附加税税率</th>
		<th style="text-align: center">进项税</th>
		<th style="text-align: center">汇总附加税</th>
		<th style="text-align: center">明细附加税</th>
		<th style="text-align: center">附加税差异</th>
		<th style="text-align: center">机构名称</th>
	</tr>
	<s:iterator value="paginationList.recordList" id="iList" status="stuts">
		
		<tr align="center"class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
		    <td><input type="checkbox" style="width:13px;height:13px;" name="paper_invoice_stock_id" value="<s:property value="#iList.paperInvoiceId"/>"/></td>
			<td align="center"><s:property value='#stuts.count' /></td>		
			<td><s:property value='taxPerNumber'/></td>
			<td><s:property value='taxPerName'/></td>
<!-- 			<td><s:property value='surtaxType'/></td> -->
			<td><s:property value='surtaxName'/></td>
			<td style="text-align: right"><s:property value='surtaxRate'/></td>
			<td style="text-align: right"><s:property value='taxAmtCny'/></td>
			<td style="text-align: right"><s:property value='gatherSurtax'/></td>
			<td style="text-align: right"><s:property value='surtaxAmt'/></td>
			<td style="text-align: right"><s:property value='diffSurtax'/></td>
			<td style="text-align: center"><s:property value='instName'/></td>
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
</html>















