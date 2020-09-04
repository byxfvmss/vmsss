<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../../page/include.jsp"%>
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">

<script type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/editview.js"></script>
<meta http-equiv="Pragma" content="no-cache" />
<title>交易认定设置</title>
<script type="text/javascript">
	function openWindows(url){
		OpenModalWindow(encodeURI(url),750,600,true);
	}
	function openWindows1(url){
		OpenModalWindow(encodeURI(url),400,300,true);
	}
	function deleteTransBatch(actionUrl){
	
		var form = document.getElementById("main");
		form.action=actionUrl;
		form.submit();
	}
	
	function delMgtInfo(){
		if(checkChkBoxesSelected("selectSerialNos")){
			if(!confirm("确定要删除选择的记录吗？")){
				return false;
			}
			submitForm("deleteBusiness.action");
		}else{
			alert("请选择要删除的记录");
		}
	}
	
	function checkChkBoxesSelected(chkBoexName){
		var flg = false;
		var chkBoexes= document.getElementsByName(chkBoexName);
		for(i=0;i<chkBoexes.length;i++){
			if(chkBoexes[i].checked){
				flg = true;
				/* document.getElementsByName("selectTaxNos")[i].checked = true;
				document.getElementsByName("selectGoodsNames")[i].checked = true;
				document.getElementsByName("selectTransTypes")[i].checked = true; */
			}else{
				/* document.getElementsByName("selectTaxNos")[i].checked = false;
				document.getElementsByName("selectGoodsNames")[i].checked = false;
				document.getElementsByName("selectTransTypes")[i].checked = false; */
			}
		}
		return flg;
	}
	
	function submitForm(actionUrl){
		document.forms[0].action = actionUrl;
		document.forms[0].submit();
		document.forms[0].action = '';
	}

</script>
	</head>
	<body>
		<form name="main" action="listBusiness.action" method="post" enctype="multipart/form-data">
		    <table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
			<tr>
				<td class="centercondition">
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> 
						<span class="current_status_menu">当前位置：</span> 
						<span class="current_status_submenu1">参数管理</span>
						<span class="current_status_submenu">业务参数管理</span>
						<span class="current_status_submenu">交易认定管理</span>
					</div>
					<div class="widthauto1">
					<table id="tbl_query" cellpadding="1" cellspacing="0">
						<tr>
							<td align="right" width="10%">税率</td>
							<td width="30%"><input id="taxrate" class="tbl_query_text" name="taxrate" type="text" value="<s:property value='taxrate' />" onkeypress="checkkey(value);" /></td>
							<td width="60%"><input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
							           onclick="submitForm('listBusiinessHead.action');" name="BtnView" value="查询" id="BtnView" /></td>	
						</tr>
					</table>
					</div>
					<table id="tbl_tools" cellpadding="1" cellspacing="0">
						<tr>
							<td align="left" style="padding: 1px 0px 0px 5px;">
								<!-- <input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 
								    value="增加"	onClick="openWindows('createBusiness.action');" />
								<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 
									onclick="delMgtInfo();" name="cmdDel" value="删除" id="cmdDel" /> -->	
									
								<a href="#" onClick="openWindows1('createBusiness.action');" ><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon16.png"/>新增</a>
								<a href="#" onclick="delMgtInfo();" ><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1019.png"/>删除</a>
							</td>
						</tr>
					</table>

			<div style="overflow: auto; width: 100%; margin-left:5px;" id="lessGridList4">
				<table class="lessGrid" cellspacing="0" rules="all" border="0"
					cellpadding="0" display="none" style="border-collapse: collapse; width: 100%;">
					<tr class="lessGrid head">
						<th width="4%" style="text-align:center">
							<input id="CheckAll" style="width: 13px; height: 13px;" type="checkbox" onClick="checkAll(this,'selectSerialNos')" />
						</th> 
						<th width="6%" style="text-align: center">序号</th>
						<th width="30%"  style="text-align:center" nowrap>
							编号
						</th>
						<th width="25%" style="text-align:center" nowrap>
							税率
						</th>
						<th width="30%"  style="text-align:center" nowrap>
							公式
						</th>
						<th style="text-align:center" nowrap>
							编辑
						</th>
					</tr>
					<s:iterator value="paginationList.recordList" id="iList" status="stuts">
						<tr align="center"class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
							<td align="center" >
							<input type="checkbox" style="width: 13px; height: 13px;" name="selectSerialNos" value="<s:property value="taxRateId"/>" />
							<%-- <input type="checkbox" style="display:none" name="selectSerialNos" value="<s:property value="serialNo" />"/> --%>
							<%-- <input type="checkbox" style="display:none" name="selectGoodsNames" value="<s:property value="goodsName" />"/>
							<input type="checkbox" style="display:none" name="selectTransTypes" value="<s:property value="transType" />"/>
							 --%>
							 <input type="hidden"  name="taxRateId" value="<s:property value="taxRateId" />"/>
							 </td>
							<td align="center"><s:property value='#stuts.count' /></td>
							<td align="center">
								<s:property value="serialNo" />
							</td>
							<td align="center">
								<s:property value="taxRate" />
							</td>
							<td align="center">
								<s:property value="math" />
							</td>
							<td align="center">
								<a href="#"
									onClick="openWindows('editBusi.action?taxRateId=<s:property value="taxRateId" />')">
									<img src ="<c:out value="${bopTheme}"/>/themes/images/icons/icon1005.png" title="编辑" style="border-width: 0px;" />
								</a>
							</td>
						</tr> 
					</s:iterator>

				</table>
			</div>
			<div id="anpBoud" align="Right" style="width:100%;vlign=top;">
	        <table width="100%" cellspacing="0" border="0">
	            <tr>
	                <td align="left"></td>
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
