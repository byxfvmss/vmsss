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
<title>附加税税种维护</title>

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
	function openWindows(url){
		OpenModalWindow(url,650,360,true);
	}
	// 打开空白发票作废页面
	function invalidBlankPaperInvoice(){
		var url = 'invalidBlankPaperInvoice.action';
		OpenModalWindow(url,650,250,true);
	}
	function submitForm(actionUrl){
		if(CheckSurtaxRate(document.getElementById("surtaxRate"),"附加税税率必须是大于等于0小于1的两位小数")==false){
			return false;
		}

		var form = document.getElementById("main");
		form.action=actionUrl;
		form.submit();
		form.action="listSurtaxType.action";
	}
	
	function CheckSurtaxRate(obj,strAlertMsg)
	{
		var strValue=obj.value;
	//	if(strValue!="" && ((isNaN(strValue) || strValue>=1 || strValue<=0)
	//			||(!/^[0-9]+\.[0-9]{2}$/.test(strValue)))){	
		if(strValue!="" && (isNaN(strValue) || strValue>=1 || strValue<0)){	
			var m = new MessageBox(obj);
			m.Show(strAlertMsg);	
			obj.focus();
			return false;
		}else{
		   return true;
		}	
		
	}
	
	function deleteTransBatch(actionUrl){
	
		var form = document.getElementById("main");
		form.action=actionUrl;
		form.submit();
		form.action="listSurtaxType.action";
		
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
	$(function(){
		$("#cmdDel").click(function(){
			$checkedStore=$("input[name='surtax_ids']:checked");
			if($checkedStore.size()==0){
				alert("请选择要删除的记录");
				return;
			}
			if(confirm("确定要删除选择的记录吗？")){
				$selectedIds="";
				for($idx=0;$idx<$checkedStore.size();$idx++){
					if(($idx+1)!=$checkedStore.size()){
						$selectedIds+=$($checkedStore[$idx]).val()+",";
					}else{
						$selectedIds+=$($checkedStore[$idx]).val();
					}
				}
				submitForm("delSurtaxTypeInfo.action?selectedIds="+$selectedIds);
			}
		});
	});

</script>
</head>
<body>
<form id="main" action="<c:out value='${webapp}'/>/listSurtaxType.action" method="post">
<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
	<tr>
		<td class="centercondition">
		<div id="tbl_current_status">
			<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> 
			<span class="current_status_menu">当前位置：</span> 
			<span class="current_status_submenu1">附加税管理</span>
			<span class="current_status_submenu">附加税管理</span>
			<span class="current_status_submenu">税种维护</span>
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
				<td><input id="taxperName" class="tbl_query_text" name="taxperName" type="text" class="tbl_query_text" value="<s:property value='taxperName' />" />
					<div id="searchInstResult" style="display: none;"/></td>
				<td>附加税税种</td> 
				<td><select id="surtaxType" name="surtaxType" >
						<option value="" <s:if test='surtaxType==""'>selected</s:if><s:else></s:else>>所有</option>
						<option value="1" <s:if test='surtaxType=="1"'>selected</s:if><s:else></s:else>>城市建设维护税</option>
						<option value="2" <s:if test='surtaxType=="2"'>selected</s:if><s:else></s:else>>教育费附加税</option>
						<option value="3" <s:if test='surtaxType=="3"'>selected</s:if><s:else></s:else>>地方教育费附加税</option>
						<option value="4" <s:if test='surtaxType=="4"'>selected</s:if><s:else></s:else>>其他地方附加税</option>
					</select></td>
				<td>附加税税率</td>
				<td><input id="surtaxRate" name="surtaxRate" type="text" class="tbl_query_text" maxlength="4" value="<s:property value='surtaxRate' />" class="" style="ime-mode: disabled"/>&nbsp;&nbsp;&nbsp;</td>
			</tr>
			<tr>
				<td>生效日</td>
				<td><input id="surtaxStrDt" name="surtaxStrDt" type="text" value="<s:property value='surtaxStrDt'/>" class="tbl_query_time" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'surtaxEndDt\')}'})" />&nbsp;&nbsp;&nbsp;</td>
			
				<td>终止日</td>
				<td><input id="surtaxEndDt" name="surtaxEndDt" type="text"  value="<s:property value='surtaxEndDt' />" class="tbl_query_time" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'surtaxStrDt\')}'})"/>&nbsp;&nbsp;&nbsp;</td>
 				<td colspan="4">
 				<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 
 				onclick="submitForm('listSurtaxType.action');" name="cmdSelect" value="查询" id="cmdSelect" />
 				</td>
			</tr>
		</table>
		</div>
		<!-- <table id="tbl_query" cellpadding="1" cellspacing="0">
			<tr align="left">
				<td style="text-align:left">
				<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" 
				onMouseOut="this.className='tbl_query_button'" onclick="openWindows('surtaxTypeAddOrUpdInit.action?updFlg=0');" name="cmdAdd" value="新增" id="cmdAdd" />				
				</td>
				<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td style="text-align:left">
				<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 
				name="cmdDel" value="删除" id="cmdDel" />				
				</td>
	         </tr>
		</table> -->
		<table id="tbl_tools" width="100%" border="0">
			<tr>
				<td align="left">
				<a href="#" onclick="openWindows('surtaxTypeAddOrUpdInit.action?updFlg=0');"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon16.png"/>新增</a>
				<a href="#" name="cmdDel" id="cmdDel"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1019.png"/>删除</a>
				</td>
			</tr>
		</table>
<div id="lessGridList4" style="overflow: auto; width: 100%;">
<table  class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0"  style="border-collapse: collapse;width: 100%;">
	<tr class="lessGrid head">
	<th width="3%" style="text-align:center"><input id="CheckAll" style="width:13px;height:13px;" type="checkbox" onClick="checkAll(this,'surtax_ids')" /></th>
		<th style="text-align: center">序号</th>
		<th style="text-align: center">纳税人识别号</th>
		<th style="text-align: center">纳税人名称</th>
		<th style="text-align: center">附加税类型</th>
		<th style="text-align: center">附加税名称</th>
		<th style="text-align: center">附加税税率</th>
		<th style="text-align: center">生效日</th>
		<th style="text-align: center">终止日</th>
 		<th style="text-align: center">编辑</th>
 		<th style="text-align: center; display: none">操作状态</th>
	</tr>
	<s:iterator value="paginationList.recordList" id="iList" status="stuts">
		
		<tr align="center"class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
		    <td><input type="checkbox" style="width:13px;height:13px;" name="surtax_ids" value="<s:property value="taxpayerId"/>:<s:property value="surtaxType"/>"/></td>
			<td align="center"><s:property value='#stuts.count' /></td>		
			<td><s:property value='taxpayerId'/></td>
			<td><s:property value='taxperName'/></td>
			<td><s:property value='surtaxType'/></td>
			<td><s:property value='surtaxName'/></td>
			<td><s:property value='surtaxRate'/></td>
			<td><s:property value='surtaxStrDt'/></td>
			<td><s:property value='surtaxEndDt'/></td>
 			<td>
 				<a href="javascript:void(0);" onClick="openWindows('surtaxTypeAddOrUpdInit.action?updFlg=1&taxPerNumber=<s:property value="taxpayerId" />&surtaxType=<s:property value="surtaxType" />');">
 				<img src ="<c:out value="${bopTheme}"/>/themes/images/icons/icon1005.png" title="编辑" style="border-width: 0px;" /></a>
 			</td>
 			<td style="display: none"><s:property value='batchId'/></td>
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
