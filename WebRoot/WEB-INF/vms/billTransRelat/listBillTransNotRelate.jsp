<%@ page language="java" import="com.cjit.vms.trans.util.DataUtil" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<title>票据查看</title>
<base target="_self">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
<script type="text/javascript">


	//画面初始化
	$(function() {
		//使用jquery绑定事件
		$(".action").live("click",function() {
			doRelate(this);
			//锁定已钩稽的交易
			lockCheckedTR(); 
		})
		//锁定已钩稽的行
		lockCheckedTR(); 
		
	})
	
	function lockCheckedTR(){
		//取得父窗口所有已选择的交易ID（避免选择后翻页交易可复选）
		var parentTableObj = $("#relatedTable [name=relateTransIds]", window.parent.document);
		
		$(parentTableObj).each(function(){
			var transIdStr = $(this).val();
			var selecterStr = "#notRelatedTable tr:gt(0) [name=relateTransIds][value="+transIdStr+"]";
		    $(selecterStr).closest("tr").find(".action").html("已选择").removeClass("action");
		})
	}
	
	//选择交易
	function doRelate(target) {
		//取得行
		var trObj = $(target).closest("tr");
		//创建新行
		var trCloneObj = copyAndProcessTr(trObj);
		//取得已钩稽table
		var parentTableObj = $("#relatedTable", window.parent.document);
		//钩稽table追加行
		parentTableObj.append(trCloneObj);
		//父窗口校验方法70行选择后立即校验金额
		window.parent.checkFormHighLight($("[name=relateAmts]",trCloneObj));
		
		//钩稽table重载css  index
		reloadTRClass(parentTableObj);
		reloadTRIndex(parentTableObj);

	}

	function copyAndProcessTr(trObj) {
		//复制行
		var trCloneObj = trObj.clone();
		//创建输入框
		var newINPUT = $("<input type='text' />");
		newINPUT.addClass("tbl_query_text");
		newINPUT.attr("name", "relateAmts");
		//设置初始值为balance
		newINPUT.val($("[name=balance]", trCloneObj).val());
		trCloneObj.find(".action").html("取消选择");

		var newTD = $("<td/>")

		newTD.append(newINPUT);
		//插入一个输入框
		$(".balanceTr", trCloneObj).after(newTD);

		return trCloneObj
	}

	function reloadTRClass(table) {
		$("tr:odd", table).attr("class", "lessGrid rowA");
		$("tr:even", table).attr("class", "lessGrid rowB");
	}
	function reloadTRIndex(table) {
		var i = 1;
		$("tr:gt(0)", table).each(function() {
			$(".index", this).html(i);
			i++;
		});
	}

	function submitForm(url) {
		document.forms[0].action = url;
		document.forms[0].submit();
	}

    
	/* function lockTR(){
		var parentTableObj = $("#relatedTable", window.parent.document);
		var notRelatedTableObj = $("#notRelatedTable");
		$("tr:gt(0)",parentTableObj).each(function(){
			var relatedTransId = $("[name=relateTransIds]",this).val();
			$("tr:gt(0)",notRelatedTableObj).each(function(){
				var transId =  $("[name=relateTransIds]",this).val();
				if(relatedTransId==transId){
					$(".action",this).html("钩稽锁定中");
					$(".action",this).die("click");
				}
			});
		})
	} */
</script>
</head>
<body>
	<form id="main" action="transInfoCheckNQuery.action" method="post">
		<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
			<tr>
				<td class="centercondition">
					<table class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
						<tr>
							<td align="left" colspan='14'>
								
								<table id="tbl_query" cellpadding="0" cellspacing="0" border="0" width="100%">
									<tr>
										<s:hidden name="billInfoSearch.billId"></s:hidden>
										<s:hidden name="relateGoodsId"></s:hidden>
										<s:hidden name="relateItemId"></s:hidden>
										<s:set value="relateItemId" id="relateBillItemId"></s:set>
										<td>交易业务编号:</td>
										<td>
											<s:textfield name="relateTransId" cssClass="tbl_query_text"></s:textfield>
										</td>
										<%-- <td>客户编号:</td>
										<td>
											<s:textfield name="billInfoSearch.customerId" cssClass="tbl_query_text"></s:textfield>
										</td> --%>
										<td>交易发生机构:</td>
										<td>
											<s:select name="billInfoSearch.instcode" list="authInstList" listKey='id' listValue='name' headerKey="" headerValue="所有"></s:select>
										</td>
										<td>
											<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
												onclick="submitForm('listBillTransNotRelate.action');" name="cmdSelect" value="查询" id="cmdSelect"
											/>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
					
					<div class="lessGridList4" style="overflow: auto; height:220px;">
						<table id="notRelatedTable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
							<tr>
								<th style="text-align: center">序号</th>
								<th style="text-align: center">交易业务编号</th>
								<th style="text-align: center">交易时间</th>
								<th style="text-align: center">客户编号</th>
								<th style="text-align: center">客户名称</th>
								<th style="text-align: center">交易类型</th>
								<th style="text-align: center">交易金额</th>
								<s:if test='configCustomerFlag.equals("KBC")'>
									<th style="text-align: center">数据来源</th>
								</s:if>
								<th style="text-align: center">是否含税</th>
								<th style="text-align: center">税率</th>
								<th style="text-align: center">税额</th>
								<th style="text-align: center">收入</th>
								<th style="text-align: center">价税合计</th>
								<th style="text-align: center">未开票金额</th>
								<th style="text-align: center">可钩稽金额</th>
								<th style="text-align: center">发票类型</th>
								<th style="text-align: center">交易状态</th>
								<th style="text-align: center">确认勾稽</th>
							</tr>
							<s:iterator value="paginationList.recordList" id="iList" status="stuts">
								<tr align="center" class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
									<td align="center">
										<span class="index">
											<s:property value='#stuts.count' />
										</span>
										<s:hidden name="relateTransIds" value="%{transId}" />
										<s:hidden name="relateTaxRates" value="%{taxRate}" />
										<!--画面87行定义  -->
										<s:hidden name="relateItemIds" value="%{relateBillItemId}"></s:hidden>
										<s:hidden name="transGoodsId" value="%{goodsId}"></s:hidden>
										<s:hidden name="balance"></s:hidden>
									</td>
									<td>
										<s:property value='transId' />
									</td>
									<td>
										<s:property value='transDate' />
									</td>
									<td>
										<s:property value='customerId' />
									</td>
									<td>
										<s:property value='customerName' />
									</td>
									<td>
										<s:property value='transTypeName' />
									</td>
									<td class="transAmt" style="text-align: right">
										<!--原始交易金额  -->
										<s:if test='taxFlag eq "N"'>
											<fmt:formatNumber value="${income}" pattern="#,##0.00" />
										</s:if>
										<s:else>
											<fmt:formatNumber value="${amt}" pattern="#,##0.00" />
										</s:else>
									</td>
									<s:if test='configCustomerFlag.equals("KBC")'>
										<td>
											<s:property value="@com.cjit.vms.trans.util.DataUtil@getKbcDataSourcesCH(dataSources)" />
										</td>
									</s:if>
									<td>
										<s:property value="@com.cjit.vms.trans.util.DataUtil@getTaxFlagCH(taxFlag)" />
									</td>
									<td style="text-align: right">
										<fmt:formatNumber value="${taxRate}" pattern="#,##0.0000" />
									</td>
									<td style="text-align: right">
										<fmt:formatNumber value="${taxAmt}" pattern="#,##0.00" />
									</td>
									<td style="text-align: right">
										<fmt:formatNumber value="${income}" pattern="#,##0.00" />
									</td>
									<td style="text-align: right">
										<fmt:formatNumber value="${amt}" pattern="#,##0.00" />
									</td>
									<td class="balanceTr" style="text-align: right">
										<fmt:formatNumber value="${balance}" pattern="#,##0.00" />
									</td>
									<td  style="text-align: right">
										<fmt:formatNumber value="${balance}" pattern="#,##0.00" />
									</td>
									<td>
										<s:property value="@com.cjit.vms.trans.util.DataUtil@getFapiaoTypeCH(fapiaoType)" />
									</td>
									<td>
										<s:property value="@com.cjit.vms.trans.util.DataUtil@getDataStatusCH(#iList.dataStatus,'TRANS')" />
									</td>
									<td>
										<a href="javascript:void(0);" class="action">选择</a>
									</td>
								</tr>
							</s:iterator>
						</table>
					</div>
					<div id="anpBoud" align="Right" style="overflow:auto;width:100%;">
						<table width="100%" cellspacing="0" border="0">
							<tr>
								<td align="right">
									<s:component template="pagediv" />
								</td>
							</tr>
						</table>
					</div>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>