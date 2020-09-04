<!-- file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.util.PaginationList"
	import="com.cjit.vms.system.model.AccTitle"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../../../page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>

<script language="javascript" type="text/javascript" src="<%=bopTheme %>/js/main.js"></script>
<link href="<c:out value="${bopTheme2}"/>/css/main.css" rel="stylesheet" type="text/css" />
<script language="javascript" type="text/javascript" src="<%=bopTheme %>/js/window.js"></script>
<script charset="gbk"  language="javascript" type="text/javascript" src="<%=bopTheme%>/js/validator.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<!-- MessageBox -->
<script src="<c:out value="${bopTheme}"/>/js/MessageBox/js/messageBox.js" type="text/javascript"></script>
<link href="<c:out value="${bopTheme}"/>/js/MessageBox/css/messageBox.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/My97DatePicker/WdatePicker.js"></script>
<meta http-equiv="Pragma" content="no-cache"/>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<title>客户管理</title>
<script type="text/javascript">
	function submitForm(actionUrl){
		var form = document.getElementById("main");
		form.action=actionUrl;
		form.submit();
		form.action = "listAccTitle.action";
		
	}
	function toExcel(){
		var form = document.getElementById("main");
		form.action="exportAccTitle.action";
		form.submit();
		form.action = "listAccTitle.action";
	}
	
</script>
</head>
<body scroll="no" style="overflow:hidden;">
<form id="main" action="listAccTitle.action" method="post" enctype="multipart/form-data">
<input type="hidden" name="accTitle.parentAccTitleCode" value="<s:property value='accTitle.parentAccTitleCode' />" />
<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
	<tr>
		<td class="centercondition">
		<div class="widthauto1">
		<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
			<tr>
			    <td>科目编号</td>
				<td><input id="accTitle.accTitleCode" name="accTitle.accTitleCode" type="text" class="tbl_query_text" value="<s:property value='accTitle.accTitleCode' />" /></td>
			    <td>科目名称</td>
				<td><input id="accTitle.accTitleName"  name="accTitle.accTitleName" type="text" class="tbl_query_text" value="<s:property value='accTitle.accTitleName' />" /></td>

				<td style="text-align:left">
				<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" 
				onMouseOut="this.className='tbl_query_button'" onclick="submitForm('listAccTitle.action');" name="cmdFilter" value="查询" id="cmdFilter" />				
				</td>
			</tr>
		</table>
		</div>
		 	<table id="tbl_tools" width="100%" border="0">
			<tr>
				<td align="left" width="200">
				<a href="#" onClick="OpenModalWindowSubmit('<c:out value="${webapp}"/>/toSaveAccPage.action?editType=add',500,300,true,'add')"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon16.png"/>新增</a>
				<a href="#" onClick="beforeDeleteAccTitle('<c:out value="${webapp}"/>/deleteAccTitle.action')"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1019.png"/>删除</a>
				</td>
				<td width="255">
				<s:file name="attachmentAccTitle" size="30" style="height:26px;"></s:file>
				</td><td>
				<a href="#" onclick="submitForm('importAccTitle.action');"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1007.png"/>导入</a>
				<a href="#" onClick="toExcel();"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png"/>导出</a>
				</td>
			</tr>
			</table>
			<div id="lessGridList1" style="overflow: auto; width: 100%;">
			<table  class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0"  style="border-collapse: collapse;width: 100%;">
				<tr class="lessGrid head">
				    <th width="3%" style="text-align:center"><input id="CheckAll" style="width:13px;height:13px;" type="checkbox" onClick="checkAll(this,'accTitleIds')" /></th>
				    <th width="3%" style="text-align: center">序号</th>
				    <th width="10%" style="text-align: center">科目编号</th>
				    <th width="5%" style="text-align: center">科目名称</th>
				    <th width="10%" style="text-align: center">父科目编号</th>
				    <th width="5%" style="text-align: center">父科目名称</th>
					<th width="5%" style="text-align: center">修改</th>
				</tr>
				
				<s:iterator value="paginationList.recordList" id="iList" status="stuts">
					<tr align="center"
						class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>"
						>
						<td><input type="checkbox" style="width:13px;height:13px;" name="accTitleIds" value="<s:property value="accTitleId"/>" /></td>
						<td align="center"><s:property value='#stuts.count' /></td>	
						<td style="display: none"><input id = 'accTitleId' name='accTitleId' value='<s:property value='accTitleId'/>'/></td> 
						<td ><s:property value='accTitleCode'/></td> 
						<td><s:property value='accTitleName'/></td> 
						<td ><s:property value='parentAccTitleCode'/></td> 
						<td><s:property value='parentAccTitleName'/></td> 
					
						<%-- <td style="display: none"><s:property value='customerId'/></td> --%>
						<td align="center">
							<a href="javascript:void(0)" onClick="OpenModalWindowSubmit('<c:out value="${webapp}"/>/toUpdateAccPage.action?accTitleId=<s:property value="accTitleId"/>',500,300,'view') ">
						    <img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1005.png" title="修改" style="border-width: 0px;"/></a>
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

<script language="javascript">	
function OpenModalWindowSubmit(newURL,width,height,needReload,s) {
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
		retData = showModalDialog(newURL, 
				  window, 
				  "dialogWidth:" + width
				+ "px;dialogHeight:" + height
				+ "px;center=1;scroll=1;help=0;status=0;");
		if(needReload && retData){
			//window.document.forms[0].submit();
			submitAction(document.forms[0],"listAccTitle.action?newdate="+new Date());
		}
}
function beforeDeleteCustomerlk(actionName) {
	var t = "";
	var inputs = document.getElementsByName('customerIdList');
	for ( var i = 0; i < inputs.length; i++) {
		if (inputs[i].checked == true) {
			t += inputs[i].value + ",";
		}
	}
	if (t.length == 0) {
		alert("请先选择要删除的客户！");
		return;
	}
   
				if(confirm('确认删除所选客户？')){
					document.forms[0].action = actionName;
					document.forms[0].submit();
				}
			
	
}
function beforeDeleteAccTitle(actionName){
	var t = "";
	var inputs = document.getElementsByName('accTitleIds');
	for ( var i = 0; i < inputs.length; i++) {
		if (inputs[i].checked == true) {
			t += inputs[i].value + ",";
		}
	}
	if (t.length == 0) {
		alert("请先选择要删除的科目！");
		return;
	}
	
				if(confirm('确认删除所选科目？')){
					//document.forms[0].action = actionName;
					deletebefore(t);
					/* submitAction(document.forms[0],"listAccTitle.action"); */
				}
			
}
function deletebefore(t){
	$.ajax({
		url: 'deleteAccTitle.action',
	
					type: 'POST',
					async:false,
					data:{accTitleIds:t},
					dataType: 'text',
				//	timeout: 1000,
					error: function(){
						return false;},
					success: function(result){
					if(result=="success"){
						alert("删除成功");
					document.forms[0].submit();
					}else{
						alert(result);
					}
							
			}			
				});
}

</script>
</html>