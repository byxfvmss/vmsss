<!-- file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.util.PaginationList"
	import="com.cjit.vms.system.model.TransAccInfo"
	import="com.cjit.vms.trans.model.ExchangeRate"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../../page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
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
		
	}
	function toExcel(){
		var form = document.getElementById("main");
		form.action="exportTransAcc.action";
		form.submit();
		form.action = "listTransAcc.action?paginationList.showCount="+"false";
	}
	
</script>
</head>
<body>
<form id="main" action="listTransAcc.action" method="post" enctype="multipart/form-data">
<table id="tbl_main" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td align="left">
		<div id="tbl_current_status">
			<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> 
			<span class="current_status_menu">当前位置：</span> 
			<span class="current_status_submenu1">参数管理</span>
			<span class="current_status_submenu">业务参数管理</span>
			<span class="current_status_submenu">交易分录查询</span>
		</div>
		<div class="widthauto1">
		<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
			<tr>
			    <td>交易流水号</td>
				<td><input id="transAccInfo.transId" name="transAccInfo.transId" type="text" class="tbl_query_text" value="<s:property value='transAccInfo.transId' />" maxlength="50"/></td>
			    <td>交易种类编码</td>
				<td><input id="transAccInfo.businessCode"  name="transAccInfo.businessCode" type="text" class="tbl_query_text" value="<s:property value='transAccInfo.businessCode' />" maxlength="50"/></td>
				<td>交易种类名称</td>
				<td><input id="transAccInfo.businessCName"  name="transAccInfo.businessCName" type="text" class="tbl_query_text" value="<s:property value='transAccInfo.businessCName' />" maxlength="100"/></td>
			</tr>
			<tr>
				<td>科目编码</td>
				<td><input id="transAccInfo.accTitleCode"  name="transAccInfo.accTitleCode" type="text" class="tbl_query_text" value="<s:property value='transAccInfo.accTitleCode' />" maxlength="50"/></td>
				<td>科目名称</td>
				<td><input id="transAccInfo.accTitleName"  name="transAccInfo.accTitleName" type="text" class="tbl_query_text" value="<s:property value='transAccInfo.accTitleName' />" maxlength="100"/></td>
				<td>借贷标识</td>
				<td>
					<select id="transAccInfo.cdFlag" name="transAccInfo.cdFlag" >
						<option value="" <s:if test='transAccInfo.cdFlag==""'>selected</s:if><s:else></s:else>>全部</option>
						<option value="D" <s:if test='transAccInfo.cdFlag=="D"'>selected</s:if><s:else></s:else>>借方</option>
						<option value="C" <s:if test='transAccInfo.cdFlag=="C"'>selected</s:if><s:else></s:else>>贷方</option>
					</select>
				</td>
			</tr>
			<tr>    
			    <td>冲账标识</td>
			    <td>
					<select id="transAccInfo.isReverse" name="transAccInfo.isReverse" >
						<option value="" <s:if test='transAccInfo.isReverse==""'>selected</s:if><s:else></s:else>>全部</option>
						<option value="Y" <s:if test='transAccInfo.isReverse=="Y"'>selected</s:if><s:else></s:else>>是</option>
						<option value="N" <s:if test='transAccInfo.isReverse=="N"'>selected</s:if><s:else></s:else>>否</option>
					</select>
				</td>
				<td>交易日期</td>
				<td>
				<input class="tbl_query_time" id="transAccInfo.transDate" type="text" name="transAccInfo.transDate"
							value="<s:property value='transAccInfo.transDate'/>" 
							onfocus="WdatePicker()" size='11'"/>
					</td>
				</td>
				<%-- <td><input id="transAccInfo.currency"  name="transAccInfo.currency" type="text" value="<s:property value='transAccInfo.currency' />" /></td> --%>
				<td colspan="2">
				<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" 
				onMouseOut="this.className='tbl_query_button'" onclick="submitForm('listTransAcc.action');" name="cmdFilter" value="查询" id="cmdFilter" />				
				</td>
			</tr>
		</table>
		</div>
		<!-- <table id="tbl_query" cellpadding="1" cellspacing="0">
			<tr align="left">
		<%-- 	<td>
				 <input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" 
				 onMouseOut="this.className='tbl_query_button'" value="新增" 
				 onClick="OpenModalWindowSubmit('<c:out value="${webapp}"/>/toSaveAccPage.action?editType=add',900,300,true,'add')"/>
			</td>
			<td>
				 <input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" 
				 onMouseOut="this.className='tbl_query_button'" value="删除" 
				 onClick="beforeDeleteAccTitle('<c:out value="${webapp}"/>/deleteAccTitle.action')"/>
			</td> --%>
			<td>
				 <input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" 
				 onMouseOut="this.className='tbl_query_button'" value="导出" onClick="toExcel();"/>
			</td>
			<%-- <td>
			    <input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" 
				onMouseOut="this.className='tbl_query_button'" onclick="submitForm('importAccTitle.action');" name="cmdFilter2" value="导入" id="cmdFilter2" />
				<s:file name="attachmentAccTitle"></s:file>
			</td> --%>
	        </tr>
		</table> -->
		 <table id="tbl_tools" width="100%" border="0">
			<tr>
				<td align="left">
				<a href="#" onClick="toExcel();"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png"/>导出</a>
				</td>
			</tr>
		</table>
<div id="lessGridList3" style="overflow: auto; width: 100%;">
<table  class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0"  style="border-collapse: collapse;width: 100%;">
	<tr class="lessGrid head">
	    <!-- <th style="text-align:center"><input id="CheckAll" style="width:13px;height:13px;" type="checkbox" onClick="checkAll(this,'accTitleIds')" /></th> -->
	    <th style="text-align: center">交易日期</th>
	    <th style="text-align: center">交易流水号</th>
	    <th style="text-align: center">交易种类编号</th>
	    <th style="text-align: center">交易种类名称</th>
	    <th style="text-align: center">冲账标识</th>
	    <th style="text-align: center">币种</th>
	    <th style="text-align: center">借贷标识</th>
	    <th style="text-align: center">科目编码</th>
	    <th style="text-align: center">科目名称</th>
	    <th style="text-align: center">金额</th>
	</tr>
	
	<s:iterator value="paginationList.recordList" id="iList" status="stuts">
		<tr align="center"
			class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>"
			>
			<%--<td><input type="checkbox" style="width:13px;height:13px;" name="accTitleIds" value="<s:property value="accTitleId"/>" /></td>
			<td align="center"><s:property value='#stuts.count' /></td>	
			 <td style="display: none"><input id = 'accTitleId' name='accTitleId' value='<s:property value='accTitleId'/>'/></td>  --%>
			<td><s:property value='transDate'/></td>
			<td><s:property value='transId'/></td> 
			<td><s:property value='businessCode'/></td> 
			<td><s:property value='businessCName'/></td> 
			<td><s:if test='isReverse=="Y"'>是</s:if><s:else>否</s:else></td>
			<%-- <td><input type="hidden" value="<s:property value='currency'/>" name='currency1'></td> --%>
			<td><s:property value='currencyName'/></td>
			<td><s:if test='cdFlag=="D"'>借方</s:if><s:else>贷方</s:else></td>
			<td><s:property value='accTitleCode'/></td>
			<td><s:property value='accTitleName'/></td>
			<td><s:property value='amt'/></td>
			<%-- <td style="display: none"><s:property value='customerId'/></td> 
			<td align="center">
				<a href="javascript:void(0)" onClick="OpenModalWindowSubmit('<c:out value="${webapp}"/>/toUpdateAccPage.action?accTitleId=<s:property value="accTitleId"/>',900,300,'view') ">
			    <img src="<c:out value="${bopTheme2}"/>/img/jes/icon/edit.png" title="修改" style="border-width: 0px;"/></a>
			</td>--%>
		</tr>
		</s:iterator> 
</table>
</div>
<div id="anpBoud" align="Right" style="width:100%;">
        <table width="100%" cellspacing="0" border="0">
            <tr>           
            	<input type="hidden" name="paginationList.showCount" value="false">
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