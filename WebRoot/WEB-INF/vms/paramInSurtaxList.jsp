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
<title>进项税转出比例金额</title>

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
		form.action="listParamInSurtax.action";
	}
	
	function deleteTransBatch(actionUrl){
	
		var form = document.getElementById("main");
		form.action=actionUrl;
		form.submit();
		form.action="listParamInSurtax.action";
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
<form id="main" action="<c:out value='${webapp}'/>/listParamInSurtax.action" method="post">
<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
	<tr>
		<td class="centercondition">
		<div id="tbl_current_status">
			<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> 
			<span class="current_status_menu">当前位置：</span> 
			<span class="current_status_submenu1">参数管理</span>
			<span class="current_status_submenu">业务参数管理</span>
			<span class="current_status_submenu">进项税转出比例管理</span>
		</div>
		<div class="widthauto1">
		<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td>数据时间</td> 
				<td>
					<input id="dataDt" name="dataDt" type="text"  value="<s:property value='dataDt' />"  class="tbl_query_time"	 onFocus="WdatePicker({dateFmt:'yyyy-MM'})"/>&nbsp;&nbsp;&nbsp;
				</td>
				<td>机构</td> 
				<td>
					<select id="instId" name="instId" ><option value="" <s:if test='instId==""'>selected</s:if><s:else></s:else>>全部</option>
						<s:iterator value="lstAuthInstId" id="org">  
						<option value="<s:property value="#org.id"/>" <s:if test='instId==#org.id'>selected</s:if><s:else></s:else>><s:property value="#org.name"/></option>
				       </s:iterator>
					</select>
				</td>
				<td>纳税人识别号</td> 
				<td>
<!--					<s:if test="taxperList != null && taxperList.size > 0">-->
<!--					<s:select name="taxPerNumber" list="taxperList" listKey='taxperNumber' listValue='taxperNumber' headerKey="" headerValue="所有" onChange="getAjaxTaxperName(this.value, document.getElementById('taxperName'))"/>-->
<!--					</s:if>-->
<!--					<s:if test="taxperList == null || taxperList.size == 0">-->
<!--					<select name="taxPerNumber" class="readOnlyText">-->
<!--					<option value="">请分配机构权限</option>-->
<!--					</select>-->
<!--					</s:if>-->
						<input id="taxPerNumber" class="tbl_query_text" name="taxPerNumber" type="text" class="tbl_query_text" value="<s:property value='taxPerNumber' />" />
				</td>
				<td>纳税人名称</td> 
				<td>
					<input id="taxperName" class="tbl_query_text" name="taxperName" type="text" class="tbl_query_text" value="<s:property value='taxperName' />" />
					<div id="searchInstResult" style="display: none;"/>
				</td>
				<td>
				<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" 
				onMouseOut="this.className='tbl_query_button'"  onclick="submitForm('listParamInSurtax.action');" 
				name="cmdQueryBtn" value="查询" id="cmdQueryBtn" />	
				</td>
			</tr>
		</table>
		<!-- <table id="tbl_query" cellpadding="1" cellspacing="0">
			<tr align="left">
				<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td style="text-align:left">
				<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 
				onclick="submitForm('paramInsurtaxExcel.action');" name="cmdExcel" value="导出" id="cmdExcel" />				
				</td>
	         </tr>
		</table> -->
		<table id="tbl_tools" width="100%" border="0">
			<tr>
				<td align="left">
				<a href="#" onclick="submitForm('paramInsurtaxExcel.action');"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png"/>导出</a>
				</td>
			</tr>
		</table>
<div id="lessGridList1" style="overflow: auto; width: 100%;">
<table  class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0"  style="border-collapse: collapse;width: 100%;">
	<tr class="lessGrid head">
	<th width="3%" style="text-align:center"><input id="CheckAll" style="width:13px;height:13px;" type="checkbox" onClick="checkAll(this,'batchIdList')" /></th>
		<th style="text-align: center">数据时间</th>
		<th style="text-align: center">机构</th>
		<th style="text-align: center">纳税人识别号</th>
		<th style="text-align: center">纳税人名称</th>
		<th style="text-align: center">免税收入</th>
		<th style="text-align: center">征税收入</th>
		<th style="text-align: center">转出比例</th>
		<th style="text-align: center">转出金额</th>
		<th style="text-align: center">标志</th>
		<th style="text-align: center;">操作</th>
	</tr>
	<s:iterator value="paginationList.recordList" id="iList" status="stuts">
		
		<tr align="center"class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
		    <td><input type="checkbox" style="width:13px;height:13px;" name="taxpayer_id" value="<s:property value="#iList.taxpayerId"/>"/></td>
			<td align="center"><s:property value='#iList.dataDt' /></td>		
			<td align="center"><s:property value='#iList.instName' /></td>		
			<td><s:property value='#iList.taxpayerId'/></td>
			<td><s:property value="#iList.taxpayerName"/></td>
			<td><s:property value='#iList.taxfreeIncome'/></td>
			<td><s:property value='#iList.assessableIncome'/></td>
			<td>
				<s:property value="vatOutProportion"/>
<!-- 			    <s:if test='fapiaoType=="0"'>增值税专用发票</s:if><s:elseif test='fapiaoType=="1"'>增值税普通发票</s:elseif><s:else></s:else></td> -->
			</td>
			<td><s:property value='vatOutAmt'/></td>
			<td><s:property value='#iList.proportionFlgName'/></td>
			<td>
				<a href="javascript:;" class="edit_insurtax" rel="<s:property value="#iList.taxpayerId"/>">
				<img src ="<c:out value="${bopTheme}"/>/themes/images/icons/icon1005.png" title="编辑" style="border-width: 0px;" /></a>
				<a href="javascript:;" class="view_insurtax" rel="<s:property value="#iList.taxpayerId"/>">
				<img src ="<c:out value="${bopTheme}"/>/themes/images/icons/icon1014.png" title="查看" style="border-width: 0px;" /></a>
				
			</td>
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
	
		$(".edit_insurtax").click(function(){
			taxpayerId=$(this).attr("rel");
			OpenModalWindow("<c:out value='${webapp}'/>/editParamInSurtax.action?taxPerNumber="+taxpayerId,500,300,true);
		});
		$(".view_insurtax").click(function(){
			taxpayerId=$(this).attr("rel");
			OpenModalWindow("<c:out value='${webapp}'/>/viewParamInSurtax.action?taxPerNumber="+taxpayerId,500,300,true);
		});
	});
</script>
</html>