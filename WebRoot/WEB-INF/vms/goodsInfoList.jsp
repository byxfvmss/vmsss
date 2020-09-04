<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../page/include.jsp"%>
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">

<script type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
<meta http-equiv="Pragma" content="no-cache" />
<title>商品管理</title>
<script type="text/javascript">
	function openWindows(url){
		OpenModalWindow(encodeURI(url),900,900,true);
		document.forms[0].action = "listGoodsInfo.action";
	}
	

	function toExcel(){
		document.forms[0].action = 'goodsInfoToExcel.action';  
		document.forms[0].submit();
		document.forms[0].action = "listGoodsInfo.action";
	}
	
	// 打开空白发票作废页面
	function invalidBlankPaperInvoice(){
		var url = 'invalidBlankPaperInvoice.action';
		OpenModalWindow(url,650,250,true);
	}
	function submitForm(actionUrl){
		document.forms[0].action = actionUrl;
		document.forms[0].submit();
		document.forms[0].action = "listGoodsInfo.action";
	}
	
	function deleteTransBatch(actionUrl){
	
		var form = document.getElementById("main");
		form.action=actionUrl;
		form.submit();
		document.forms[0].action = "listGoodsInfo.action";
	}
	
<%--	$(function(){--%>
<%--		$("#cmdDel").click(function(){--%>
<%--			$checkedStore=$("input[name='selectedIds']:checked");--%>
<%--			if($checkedStore.size()==0){--%>
<%--				alert("请选择要删除的记录");--%>
<%--				return;--%>
<%--			}--%>
<%--			if(confirm("确定要删除选择的记录吗？")){--%>
<%--				$selectedIds="";--%>
<%--				for($idx=0;$idx<$checkedStore.size();$idx++){--%>
<%--					if(($idx+1)!=$checkedStore.size()){--%>
<%--						$selectedIds+=$($checkedStore[$idx]).val()+",";--%>
<%--					}else{--%>
<%--						$selectedIds+=$($checkedStore[$idx]).val();--%>
<%--					}--%>
<%--				}--%>
<%--				submitForm("delTaxItemInfo.action?selectedIds="+$selectedIds);--%>
<%--			}--%>
<%--		});--%>
<%--	});--%>
	// 删除商品信息
	function delGoodsInfo(){
		if(checkChkBoxesSelected("selectGoodsNos")){
			if(!confirm("确定要删除选择的记录吗？")){
				return false;
			}
			submitForm("delGoodsInfo.action");
		}else{
			alert("请选择要删除的记录");
		}
	}
	//检查多选框集是否至少有一个被选中
	function checkChkBoxesSelected(chkBoexName){
		var flg = false;
		var chkBoexes= document.getElementsByName(chkBoexName);
		for(i=0;i<chkBoexes.length;i++){
			if(chkBoexes[i].checked){
				flg = true;
				document.getElementsByName("selectTaxNos")[i].checked = true;
				document.getElementsByName("selectGoodsNames")[i].checked = true;
				document.getElementsByName("selectTransTypes")[i].checked = true;
			}else{
				document.getElementsByName("selectTaxNos")[i].checked = false;
				document.getElementsByName("selectGoodsNames")[i].checked = false;
				document.getElementsByName("selectTransTypes")[i].checked = false;
			}
		}
		return flg;
	}

</script>
	</head>
	<body>
<%--		<form id="main" action="<c:out value='${webapp}'/>/listGoodsInfo.action" method="post">--%>
		<form name="main" action="listGoodsInfo.action" method="post" enctype="multipart/form-data">
		    <table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
			<tr>
				<td class="centercondition">
				<div id="tbl_current_status">
					<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> 
					<span class="current_status_menu">当前位置：</span> 
					<span class="current_status_submenu1">参数管理</span>
					<span class="current_status_submenu">业务参数管理</span>
					<span class="current_status_submenu">商品管理</span>
				</div>
				<div class="widthauto1">
					<table id="tbl_query" cellpadding="1" cellspacing="0">
						<tr>
							<td align="right">纳税人识别号</td>
							<td>
								<s:if test="taxperList != null && taxperList.size > 0">
								<s:select name="taxno" list="taxperList" listKey='taxperNumber' listValue='taxperNumber' headerKey="" headerValue="所有"/>
								</s:if>
								<s:if test="taxperList == null || taxperList.size == 0">
								<select name="taxno" class="readOnlyText">
								<option value="">请分配机构权限</option>
								</select>
								</s:if>
							</td>
							<td align="right">发票商品编号</td>
							<td><input name="goodsNo" id="goodsNo" type="text"  class="tbl_query_text" value="<s:property value="goodsNo"/>" style="width: 150;" maxlength="20" /></td>
							<td align="right">商品名称</td>
							<td><input name="goodsName" id="goodsName" type="text"  class="tbl_query_text" value="<s:property value="goodsName"/>" style="width: 150;" maxlength="50"/></td>	
							<td><input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
							           onclick="submitForm('listGoodsInfo.action');" name="BtnView" value="查询" id="BtnView" /></td>	
						</tr>
					</table>
			</div>
			<!-- <table id="tbl_tools" cellpadding="1" cellspacing="0">
				<tr>
					<td align="left" style="padding: 1px 0px 0px 5px;">
						<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 
						    value="增加"	onClick="openWindows('goodsInfoAddOrUpdInit.action?updFlg=0');" />
						<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 
							onclick="return delGoodsInfo();" name="cmdDel" value="删除" id="cmdDel" />	
						<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 
						    onclick="toExcel();" name="delBtn" value="导出" id="export" />
                        <input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" 
	                        onMouseOut="this.className='tbl_query_button'" onclick="submitForm('importGoodsInfo.action');" name="cmdFilter2" value="导入" id="cmdFilter2" />
	                    <s:file name="attachmentTaxItem"></s:file>
					</td>
				</tr>
			</table> -->
	
			 <table id="tbl_tools" width="100%" border="0">
				<tr>
					<td align="left" width="200">
					<a href="#" onClick="openWindows('goodsInfoAddOrUpdInit.action?updFlg=0');" ><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon16.png"/>增加</a>
					<a href="#" onclick="return delGoodsInfo();" ><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1019.png"/>删除</a>
					</td><td align="left" width="255">
					<s:file name="attachmentTaxItem" size="30" style="height:26px;"></s:file>
					</td><td>
					<a href="#" onclick="submitForm('importGoodsInfo.action');"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1007.png"/>导入</a>
					<a href="#" onclick="toExcel();" ><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png"/>导出</a>
					</td>
				</tr>
			</table>
			<div style="overflow: auto; width: 100%; margin-left:5px;" id="lessGridList1">
				<table class="lessGrid" cellspacing="0" rules="all" border="0"	cellpadding="0" display="none" style="border-collapse: collapse; width: 100%;">
					<tr class="lessGrid head" style="height:20px;">
						<th width="4%" style="text-align:center">
							<input id="CheckAll" style="width: 13px; height:13px;" type="checkbox" onClick="checkAll(this,'selectGoodsNos')" />
						</th>
						<th width="6%" style="text-align: center">序号</th>
						<th width="30%"  style="text-align:center" nowrap>
							商品名称
						</th>
						<th width="25%" style="text-align:center" nowrap>
							发票商品编号
						</th>
						<th width="30%"  style="text-align:center" nowrap>
							纳税人识别号
						</th>
						<th style="text-align:center" nowrap colspan="2">
							操作
						</th>
					</tr>
					<s:iterator value="paginationList.recordList" id="iList" status="stuts">
						<tr align="center"class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
							<td align="center" >
							<input type="checkbox" style="width: 13px; height: 13px;" name="selectGoodsNos" value="<s:property value="goodsNo"/>" />
							<input type="checkbox" style="display:none" name="selectTaxNos" value="<s:property value="taxNo" />"/>
							<input type="checkbox" style="display:none" name="selectGoodsNames" value="<s:property value="goodsName" />"/>
							<input type="checkbox" style="display:none" name="selectTransTypes" value="<s:property value="transType" />"/>
							</td>
							<td align="center"><s:property value='#stuts.count' /></td>
							<td align="center">
								<s:property value="goodsName" />
							</td>
							<td align="center">
								<s:property value="goodsNo" />
							</td>
							<td align="center">
								<s:property value="taxNo" />
							</td>
							<td align="center">
								<a href="#"
									onClick="openWindows('goodsInfoView.action?updFlg=1&taxno=<s:property value="taxNo" />&goodsNo=<s:property value="goodsNo" />&transType=<s:property value="transType" />')">
									<img src ="<c:out value="${bopTheme}"/>/themes/images/icons/icon1014.png" title="查看" style="border-width: 0px;" />
								</a>
							</td>
							<td align="center">
								<a href="#"
									onClick="openWindows('goodsInfoAddOrUpdInit.action?updFlg=1&taxno=<s:property value="taxNo" />&goodsNo=<s:property value="goodsNo" />&transType=<s:property value="transType" />')">
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
</html>
