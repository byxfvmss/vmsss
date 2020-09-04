<!--file: <%=request.getRequestURI()%> -->
<%@ page language="java" import="java.util.*" import="java.math.BigDecimal" import="com.opensymphony.util.BeanUtils" import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils" import="com.cjit.vms.trans.model.BillInfo" import="com.cjit.vms.trans.model.BillItemInfo" import="com.cjit.vms.trans.model.TransInfo"
	import="com.cjit.vms.system.model.Business" import="com.cjit.vms.trans.util.DataUtil" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%>
<%
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
%>
<%@ include file="../../../../page/modalPage.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<%@ include file="../../../../page/include.jsp"%>
<!-- <title><s:if test="user.userId != null && user.userId != '' ">修改交易种类</s:if><s:else>新增交易种类</s:else></title> -->
<title>关联商品</title>
<link type="text/css" href="<%=bopTheme2%>/css/subWindow.css" rel="stylesheet">
<link href="<%=bopTheme%>/js/MessageBox/css/messageBox.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<%=bopTheme%>/js/validator.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/editview.js"></script>
<script type="text/javascript" src="<%=bopTheme%>/js/MessageBox/js/messageBox.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script language="javascript" type="text/javascript">
	$(function() {
	
		$("#BtnView[value='确定']").click(function(){
			alert("sfa");
		});
		
		$("#BtnSave").click(function() {
			if (checkForm()) {
				//$("#leftIds option").attr("selected", "selected");
				$("#rightIds option").attr("selected", "selected");
				submitForm("saveGoodConfig.action");
			}

		});
		
		$("#BtnSearch").click(function() {
			//if (checkForm()) {
				//$("#leftIds option").attr("selected", "selected");
				//$("#rightIds option").attr("selected", "selected");
			//}
			//alert($("#inst_id").val());
			//alert($("#tax_no").val());
			var ids = $("#transTypeIds").val();
			if($("#inst_id").val()==""||$("#inst_id").val()==null){
				alert("请选择机构");
				return;
			}
			if($("#tax_no").val()==""||$("#tax_no").val()==null){
				alert("当前所选机构并非开票主体");
				return;
			}
			submitForm("relTrans2Goods.action?ids="+ids);

		});
		
		function checkForm() {

			var list = $("#contenttable .tbl_query_text2");
			for (i = 0; i < list.length; i++) {
				var msg = $(list[i]).closest("tr").find("td:first").html();
				msg = msg.replace(":", "");
				if (false == fucCheckNull(list[i], msg + "不能为空")) {
					return false;
				}
			}
			var taxType = $("#taxType option:selected").val();
			var rightIds = $("#rightIds option");
			for (i = 0; i < rightIds.length; i++) {
				var optionVal = $(rightIds[i]).html();
				if (optionVal.indexOf(taxType + " - ") != 0) {
					$(rightIds[i]).attr("selected", "selected");
					alert("请选择于纳税人类型一致的交易类型！");
					return false;
				}
			}

			return true;

		}

	})

	function submitForm(actionUrl) {
		var form = $("#main");
		var oldAction = form.attr("action");
		form.attr("action", actionUrl);
		form.submit();
		form.attr("action", oldAction);
	}
</script>
</head>
<body scroll="no" style="overflow:hidden;">
	<div class="showBoxDiv">
		<form id="main" name="formBusiness" method="post" action="relTrans2Goods.action">
			<div id="editsubpanel" class="editsubpanel">
				<div style="overflow: auto; width: 100%;">
					<input type="hidden" name="transTypeIds" id="transTypeIds" value='<s:property value="transTypeIds"/>' />
					<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
						<tr class="header">
							<th colspan="5">
								关联商品
							</th>
						</tr>
						<tr>
							<td align="right" class="listbar">机构名称:</td>
							<td>
								<input type="hidden" id="inst_id" name="instId" value='<s:property value="relCondition.instCode"/>'>
								<input type="hidden" id="tax_no" name="taxNo" value='<s:property value="relCondition.taxNo"/>'>
								<input type="text" class="tbl_query_text2" id="inst_Name" name="instName" 
								value='<s:property value="relCondition.instName"/>' onclick="setOrg(this);" readonly="readonly">
							</td>
							<td align="right" class="listbar">商品名称:</td>
							<td>
								<input type="text" class="tbl_query_text2" id="goodsName" name="relCondition.goodsName" 
									maxlength="50" value="<s:property value="relCondition.goodsName"/>" />
							</td>
							<td class="listbar">
								<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" 
								onMouseOut="this.className='tbl_query_button'"  name="BtnSearch" value="查询" id="BtnSearch" />
							</td>
						</tr>
						<tr>
							<td align="right" class="listbar">商品类型:</td>
							<td class="listbar"  colspan="4">
								<s:optiontransferselect label="交易类型" name="leftIds" leftTitle="可选交易类型" 
									list="leftList" listKey="goodsId" listValue="goodsIdAndName" multiple="true"
									headerKey="headerKey" emptyOption="false" allowUpDownOnLeft="false" cssStyle="width:200px;height:300px;padding:5px" 
									rightTitle="已选交易类型" doubleList="rightList" doubleListKey="goodsId"
									doubleListValue="goodsIdAndName" doubleName="rightIds" doubleHeaderKey="doubleHeaderKey" 
									doubleEmptyOption="false" doubleMultiple="true" allowUpDownOnRight="false"
									doubleCssStyle="width:200px;height:300px;padding:5px"
								/>
							</td>
						</tr>
					</table>
				</div>
			</div>
			<div id="ctrlbutton" class="ctrlbutton" style="border:0px">
				<s:if test="!showOnly">
					<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" name="BtnSave" value="保存" id="BtnSave" />
				</s:if>
				<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="window.close()" value="关闭" id="BtnReturn" />
			</div>
		</form>
		<c:import url="${webapp}/page/webctrl/instTree/tree_include.jsp" charEncoding="UTF-8">
			<c:param name="treeType" value="radio" />
			<c:param name="bankName_tree" value="inst_Name" />
			<c:param name="bankId_tree" value="inst_id" />
			<c:param name="taskId_tree" value="" />
			<c:param name="task_tree" value="task_tree" />
			<c:param name="webapp" value="${webapp}" />
		</c:import>
	</div>
</body>
</html>