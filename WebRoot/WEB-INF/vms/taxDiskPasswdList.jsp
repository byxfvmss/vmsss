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
<form id="main" action="<c:out value='${webapp}'/>/taxDiskPasswdList.action" method="post">
<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
	<tr>
		<td class="centercondition">
		<div id="tbl_current_status">
			<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> 
			<span class="current_status_menu">当前位置：</span> 
			<span class="current_status_submenu1">参数管理</span>
			<span class="current_status_submenu">税控设备管理</span>
			<span class="current_status_submenu">税控盘密码管理</span>
		</div>
		<div class="widthauto1">
		<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td>纳税人识别号</td> 
				<td>
<!-- 					<s:if test="taxperLists != null && taxperLists.size > 0"> -->
<!-- 					<s:select name="taxPerNumber" list="taxperList" listKey='tanNo' listValue='tanNo' headerKey="" headerValue="所有" onChange="getAjaxTaxperName(this.value, document.getElementById('taxperName'))"/> -->
<!-- 					</s:if> -->
<!-- 					<s:if test="taxperLists == null || taxperLists.size == 0"> -->
<!-- 					<select name="taxPerNumber" > -->
<!-- 					<option value="">请分配机构权限</option> -->
<!-- 					</select> -->
<!-- 					</s:if> -->
					
					<s:if test="taxperLists != null && taxperLists.size > 0">
					<s:select style="width:150px" name="taxPerNumber" list="taxperLists"
					 listKey='tanNo' listValue='tanNo' headerKey="" headerValue="所有" 
					 onChange="getAjaxTaxperName(this.value, document.getElementById('taxperName'))" />
					</s:if>
					<s:if test="taxperLists == null || taxperLists.size == 0">
						<select style="width:150px" name="taxPerNumber" >
						<option value="">请分配机构权限</option>
						</select>
						</s:if>
				</td>
				<td>纳税人名称</td> 
				<td>
					<input id="taxperName" name="taxperName" type="text" class="tbl_query_text" value="<s:property value='taxperName' />"  />&nbsp;&nbsp;&nbsp;
				</td>
				<td>税控盘编号</td> 
				<td>
					<input id="taxDiskNo" name="taxDiskNo" type="text" class="tbl_query_text" value="<s:property value='taxDiskNo' />"  />&nbsp;&nbsp;&nbsp;
				</td>
				<td>&nbsp;&nbsp;&nbsp;</td> 
				<td>
					<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" 
					onMouseOut="this.className='tbl_query_button'"  onclick="submitForm('taxDiskPasswdList.action');" 
					name="cmdQueryBtn" value="查询" id="cmdQueryBtn" />		
				</td>
			</tr>
		</table>
		</div>
<!-- 		<table id="tbl_query" cellpadding="1" cellspacing="0"> -->
<!-- 			<tr align="left"> -->
<!-- 				<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td> -->
<!-- 				<td style="text-align:left"> -->
<!-- 				<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'"  -->
<!-- 				onMouseOut="this.className='tbl_query_button'"  onclick=""  -->
<!-- 				name="cmdAddBtn" value="增加" id="cmdAddBtn" />				 -->
<!-- 				</td> -->
<!-- 				<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td> -->
<!-- 				<td style="text-align:left"> -->
<!-- 				<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"  -->
<!-- 				onclick="" name="cmdDelbt" value="删除" id="cmdDelbt" />				 -->
<!-- 				</td> -->
<!-- 	         </tr> -->
<!-- 		</table> -->

<div id="lessGridList" style="overflow: auto; width: 100%;">
<table  class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0"  style="border-collapse: collapse;width: 100%;">
	<tr class="lessGrid head">
<!-- 	<th width="3%" style="text-align:center"> -->
<!-- 	<input id="CheckAll" style="width:13px;height:13px;" type="checkbox" onClick="checkAll(this,'checked_tax_disk_no')" /> -->
<!-- 	</th> -->
		<th style="text-align: center">序号</th>
		<th style="text-align: center">纳税人识别号</th>
		<th style="text-align: center">纳税人名称</th>
		<th style="text-align: center">税控盘编号</th>
		<th style="text-align: center">操作</th>
	</tr>
	<s:iterator value="paginationList.recordList" id="iList" status="stuts">
		
		<tr align="center"class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
<!-- 		    <td> -->
<!-- 		    <input type="checkbox" style="width:13px;height:13px;" name="checked_tax_disk_no" value="<s:property value="#iList.taxDiskNo"/>,<s:property value="#iList.taxpayerNo"/>"/> -->
<!-- 		    </td> -->
			<td align="center"><s:property value='#stuts.count' /></td>		
			<td><s:property value='#iList.taxpayerNo'/></td>
			<td><s:property value="#iList.taxpayerNam"/></td>
			<td align="center"><s:property value='#iList.taxDiskNo' /></td>		
			<td>
				<a href="javascript:;" class="edit_tax_disk_info_bt"  tdn="<s:property value="#iList.taxDiskNo"/>"  tpn="<s:property value="#iList.taxpayerNo"/>" >
				<img src ="<c:out value="${bopTheme}"/>/themes/images/icons/icon1005.png" title="编辑" style="border-width: 0px;" /></a>
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
		$("#cmdAddBtn").click(function(){
			OpenModalWindow("<c:out value='${webapp}'/>/initAddTaxDiskPasswd.action",600,200,true);
		});
		$(".edit_tax_disk_info_bt").click(function(){
			taxDiskNo=$(this).attr("tdn");
			taxPerNumber=$(this).attr("tpn");
			OpenModalWindow("<c:out value='${webapp}'/>/editTaxDiskPasswd.action?taxDiskNo="+taxDiskNo+"&taxPerNumber="+taxPerNumber,600,300,true);
		});
		$("#cmdDelbt").click(function(){
			$del_items=$("input[name='checked_tax_disk_no']:checked");
			if($del_items.size()==0){
				alert("请选择您要删除的数据！");
				return false;
			}
			if(confirm("您确定要删除吗？")){
				submitForm('deleteTaxDiskPasswd.action')
			}
		});
	});
</script>
</html>