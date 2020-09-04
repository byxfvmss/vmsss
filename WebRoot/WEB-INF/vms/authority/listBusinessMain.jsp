<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="com.cjit.common.constant.Constants"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@page import="com.cjit.gjsz.cache.CacheManager,org.apache.commons.lang.StringUtils" import="java.util.*"
	import="java.math.BigDecimal"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.util.PaginationList"
	import="com.cjit.vms.trans.model.BillInfo"
	import="com.cjit.vms.system.model.Business"
	import="com.cjit.vms.trans.util.DataUtil"%>
	
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<%@ include file="../../../page/include.jsp"%>
	<script language="javascript" type="text/javascript" src="<%=bopTheme %>/js/main.js"></script>
	<link href="<%=bopTheme2 %>/css/main.css" rel="stylesheet" type="text/css" />
	<script language="javascript" type="text/javascript" src="<%=bopTheme %>/js/window.js"></script>
	<script charset="gb2312"  language="javascript" type="text/javascript" src="<%=bopTheme %>/js/validator.js"></script>
	<script language="javascript" type="text/javascript" src="<%=bopTheme %>/js/main.js" charset="GBK"></script>
	<link href="<%=bopTheme2 %>/css/main.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="<%=bopTheme %>/js/SimpleTree/js/ajax.js"></script>
	<script language="javascript" type="text/javascript" src="<%=bopTheme %>/js/window.js" charset="UTF-8"></script>
	<script language="javascript" type="text/javascript" src="<%=bopTheme %>/js/validator.js" charset="GBK"></script>
</script>
<title>交易认定设置</title>

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
	filter: alpha(opacity = 90);
	-moz-opacity: 0.9;
	display: none;
}
</style>
<script type="text/javascript">
	var msg = '<s:property value="message" escape="false"/>';
	if (msg != null && msg != ''){
		alert(msg);
		window.parent.frames["leftFrame"].location.reload();
	}
	function submitForm(actionUrl){
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
<form name="Form1" method="post" action="" id="Form1">
	<table id="tbl_query" cellpadding="1" cellspacing="0">
			<tr align="left">
			<td>
				 <input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" 
				 onMouseOut="this.className='tbl_query_button'" value="新增" 
				 onClick="OpenModalWindowSubmit('<%=webapp%>/createBusiness.action?editType=add',600,410,true,'add')"/>
				 <input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" 
				 onMouseOut="this.className='tbl_query_button'" value="新增子节点" onClick="OpenModalWindowSubmit('<%=webapp%>/createBusinessSup.action?editType=addSub',600,410,true,'addSub')"/>
				 <input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'"
				 onMouseOut="this.className='tbl_query_button'" value="删除" onClick="beforeDeleteInst('<%=webapp%>/deleteBusiness.action');" name="BtnReturn" id="BtnReturn"/>
			
			</td>
	        </tr>
		</table>
<div id="lessGridList" style="overflow: auto; width: 100%;" id="list1">
<table  class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0"  style="border-collapse: collapse;width: 100%;">
	<tr class="lessGrid head">
	    <th width="5%" style="text-align:center"><input id="CheckAll" style="width:13px;height:13px;" type="checkbox" onClick="checkAll(this,'ids')" /></th>
	    <th width="10%" style="text-align: center">序号</th>
		<th width="20%" style="text-align: center">交易码</th>
		<th width="20%" style="text-align: center">交易名称</th>
		<th width="15%" style="text-align: center">税率(%)</th>
		<th width="10%" style="text-align: center">是否自动打印</th>
		<th width="10%" style="text-align: center">操作</th>
	</tr>
	<%
	PaginationList paginationList = (PaginationList)request.getAttribute("paginationList");
	if (paginationList != null){
		List businessInfoList = paginationList.getRecordList();
		if (businessInfoList != null && businessInfoList.size() > 0){
			for(int i=0; i<businessInfoList.size(); i++){
				Business business = (Business)businessInfoList.get(i);
				if(i%2==0){
	%>
	<tr class="lessGrid rowA">
	<%
					}else{
	%>
	<tr class="lessGrid rowB">
	<%
					}
				if(business.getHasTax() != null){
					if(business.getHasTax().equals("1")){
						business.setHasTax("是");
					}else if (business.getHasTax().equals("0")){
						business.setHasTax("否");
					}
				}else{
					business.setHasTax("未知");
				}
				if(business.getAutoPrint() != null){
					if(business.getAutoPrint().equals("1")){
						business.setAutoPrint("是");
					}else if (business.getAutoPrint().equals("0")){
						business.setAutoPrint("否");
					}
				}else{
					business.setAutoPrint("未知");
				}
					
	%>
		<td align="center">
			<input style="width:13px;height:13px;" type="checkbox" name="ids" value="<%=BeanUtils.getValue(business,"businessCode")%>" />
		</td>
		<td align="center"><%=i+1%></td>
		<td align="center"><%=business.getBusinessCode()%></td>
		<td align="center"><%=business.getBusinessCName()%></td>
		<td align="center"><%=business.getTaxRate()%></td>
		<td align="center"><%=business.getAutoPrint()%></td>
		<td align="center">
				<a href="javascript:void(0)" onClick="OpenModalWindow('<%=webapp%>/editBusi.action?businessCode=<%=business.getBusinessCode()%>',650,600,'view') ">
			    <img src="<%=bopTheme2%>/img/jes/icon/edit.png" title="修改" style="border-width: 0px;"/></a>
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
	</form>
</body>
<script type="text/javascript">
	document.getElementById("lessGridList").style.height = screen.availHeight - 375;
</script>

<script language="javascript">
function beforeDeleteInst(actionName) {
	var t = "";
	var inputs = document.getElementsByName('ids');
	for ( var i = 0; i < inputs.length; i++) {
		if (inputs[i].checked == true) {
			t += inputs[i].value + ",";
		}
	}
	if (t.length == 0) {
		alert("请先选择要删除的机构！");
		return;
	}
   
				if(confirm('确认删除所选机构？')){
					document.forms[0].action = actionName;
					document.forms[0].submit();
				}
			
	
}

function OpenModalWindowSubmit(newURL,width,height,needReload,s) {

	try {
    var node=window.parent.frames["leftFrame"].window.instTree.selectedNode;
		 if(node==null&&s=='addSub'){
	        alert("请先选择新增机构的上级机构");
	        return;
	    }
	   var retData = false;

		if(typeof(width) == 'undefined'){
			width 	= screen.width * 0.9;
		}
		if(typeof(height) == 'undefined'){
			height 	= screen.height * 0.9;
		}
		if(typeof(needReload) == 'undefined'){
			needReload 	= false;
		}
		if(s=='root'){
		   newURL+='&isRoot=true'
		}
		retData = showModalDialog(newURL, 
				  window, 
				  "dialogWidth:" + width
				+ "px;dialogHeight:" + height
				+ "px;center=1;scroll=1;help=0;status=0;");

		if(needReload && retData){
			window.document.forms[0].submit();
		}
		} catch (err) {
	alert(err)
	}
}

</script>

</html>
