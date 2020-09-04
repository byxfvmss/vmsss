<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<title>预警编辑</title>
<base target="_self">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/page/include.jsp"%>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
<style type="text/css">
.errorAmt {
	color: red;
}

.errorTr {
	background: yellow;
}
</style>
<script type="text/javascript">
	$(function() {
		//使用jquery绑定取消按钮事件 新加原属也将自动绑定此方法
		$(".action").live("click", function() {
			deleteRelate(this);
			checkItemsMaxRelateAmt();
		})

		//默认选中一个商品
		$("[name=goodsId]:first").attr("checked", "checked");
		//加载框架
		loadIframepage();

		$("[name=goodsId]").change(function() {
			loadIframepage()
		});

		//钩稽金额输入框校验事件绑定
		$("#relatedTable tr:gt(0) [name=relateAmts]").live("keyup", function() {
			checkFormHighLight(this);
		});

	});

	function submitForm() {
		if (checkFormHighLightAndAlertMsg()) {
			document.forms[0].action = "saveBillTransRelate.action";
			document.forms[0].submit();
		}

	}

	//校验表单不合法高亮并输出MSG
	function checkFormHighLightAndAlertMsg() {
		if (!hasErrorAmt()) {
			alert("请输入合法的金额");
			return false;
		}

		if (!checkItemsMaxRelateAmt()) {
			alert("钩稽交易金额必须等于商品金额");
			return false;
		}
		return true;
	}

	//校验表单不合法高亮
	function checkFormHighLight(checkObj) {
		if (checkValIsNum(checkObj)) {
			checkTransMaxAmt(checkObj);
			checkItemsMaxRelateAmt();
		}
	}

	//校验是否为数字 不合法高亮显示
	function checkValIsNum(taget) {
		var format = /^-?\d+\.?\d{0,2}$/;
		if (format.test($(taget).val())) {
			setOrMemoveColorRed(taget, false)
			return true;
		} else {
			setOrMemoveColorRed(taget, true)
			return false;

		}
	};

	function checkTransAllMaxAmt() {
		var checkResult = true;
		$("#relatedTable tr:gt(0) [name=relateAmts]").each(function() {
			if (checkTransMaxAmt(this)) {
				checkResult = false;
				return false;
			}
		})

		return checkResult;
	}

	//校验输入框的最大金额不可大于balance   
	//（已钩稽的balance=交易balance + 已钩稽amt【trans_bill  amt】）
	function checkTransMaxAmt(target) {
		var amt = $(target).val() * 1;
		var blance = $(target).closest("tr").find("[name = balance]").val();
		if (amt > blance) {
			setOrMemoveColorRed(target, true)
			return false;
		}
		setOrMemoveColorRed(target, false);
		return true;
	}

	//set True move false
	function setOrMemoveColorRed(taget, flag) {
		if (flag) {
			$(taget).addClass("errorAmt");
			$(taget).css("color", "red");
		} else {
			$(taget).removeClass("errorAmt");
			$(taget).css("color", "");
		}
	}

	//是否存在不合法金额
	function hasErrorAmt() {

		var relateAmtObj = $(".errorAmt[name=relateAmts]");

		if (relateAmtObj.size() > 0) {
			$(relateAmtObj[0]).focus();
			return false;
		}

		return true;
	}

	//校验所有商品的交易信息合计金额   不合法高亮显示
	function checkItemsMaxRelateAmt() {
		//循环商品列表
		var checkResult = true;
		$("#relateBillItemList tr:gt(0)").each(function() {
			var trObj = $(this).closest("tr");
			var goodsId = $("[name=goodsId]", trObj).val();
			var goodsAmt = $("[name=goodsAmt]", trObj).val() * 1;
			var goodsTaxAmt = $("[name=goodsTaxAmt]", trObj).val() * 1;
			var goodsSumAmt = goodsAmt + goodsTaxAmt;
			goodsSumAmt = goodsSumAmt.toFixed(2)
			if (!checkItemMaxRelateAmt(goodsId, goodsSumAmt)) {
				setBackGroundColorByGoodsId(goodsId, true);
				checkResult = false;
				return false;
			} else {
				setBackGroundColorByGoodsId(goodsId, false);
			}
		});
		return checkResult;
	}

	//设置指定商品的商品信息和交易信息加上或移除背景
	function setBackGroundColorByGoodsId(goodsId, flag) {
		var goodsSelecterStr = "#relateBillItemList tr:gt(0) [name=goodsId][value="
				+ goodsId + "]";
		var transSelecterStr = "#relatedTable tr:gt(0) [name=transGoodsId][value="
				+ goodsId + "]";
		var goodsTrObj = $(goodsSelecterStr).closest("tr");
		var transTrObj = $(transSelecterStr).closest("tr");
		if (flag) {

			goodsTrObj.add(transTrObj).addClass("errorTr");
			goodsTrObj.add(transTrObj).css("background", "yellow");
		} else {
			goodsTrObj.add(transTrObj).removeClass("errorTr");
			goodsTrObj.add(transTrObj).css("background", "");
		}
	}

	//校验指定商品的交易信息合计金额不能大于商品最大金额
	function checkItemMaxRelateAmt(goodsId, goodsSumAmt) {
		var selectStr = "#relatedTable [name=transGoodsId][value=" + goodsId
				+ "]";
		var relateSumAmt = 0;
		$(selectStr).each(function() {
			var trObj = $(this).closest("tr");
			relateSumAmt += $("[name=relateAmts]", trObj).val() * 1;

		});
		if (relateSumAmt - goodsSumAmt != 0) {
			return false;
		}

		return true;
	}
	//切换商品时刷新可钩稽列表
	function loadIframepage() {

		var billId = $("[name=relateBillId]").val();
		var customerId = $("#customerId").val();
		var goodsId = $("[name=goodsId]:checked").val();
		var itemId = $("[name=goodsId]:checked").siblings("[name=billItemId]")
				.val();
		var urlStr = "listBillTransNotRelate.action";
		urlStr += "?billInfoSearch.billId=" + billId;
		urlStr += "&relateItemId=" + itemId;
		urlStr += "&relateGoodsId=" + goodsId;
		var iframepageObj = $("#iframepage");
		iframepageObj.attr("src", urlStr);
	}

	//删除钩稽时删除数据库数据  成功后移除当前行、刷新可钩稽列表
	function deleteRelate(target) {

		if ($(".action", target) == "取消选择") {
			deleteTRAndReloadIframe(trObj);
			return;
		}

		var trObj = $(target).closest("tr");
		var relateBillIdStr = $("[name=relateBillId]").val();
		var relateTransIdStr = $("[name=relateTransIds]", trObj).val();
		var relateItemIdStr = $("[name=relateItemIds]", trObj).val();
		var url = "deleteBillTransRelate.action";
		url += "?relateBillId=" + relateBillIdStr;
		url += "&relateTransId=" + relateTransIdStr;
		url += "&relateItemId=" + relateItemIdStr;

		$.ajax({
			type : "Post",
			url : url,
			async : false,
			dataType : "text", //可以是text，如果用text，返回的结果为字符串；如果需要json格式的，可是设置为json
			success : function(data) {
				//移除当前行、刷新可钩稽列表
				deleteTRAndReloadIframe(trObj)
			},
			error : function(msg) {
				alert("取消钩稽异常");
			}
		});
	}
	////移除当前行、刷新可钩稽列表
	function deleteTRAndReloadIframe(trObj) {
		$(trObj).remove();
		loadIframepage();
	}
</script>
</head>
<body style=" background:#FFF; padding-right:17px;overflow-x:hidden!important;">
	<div class="showBoxDiv">
		<form id="frm" method="post" action="saveBillTransRelate.action">
			<div style=" width: 100%; ">
				<div class="windowtitle" style="background:#004C7E; height:45px; line-height:45px; text-align:center; color:#FFF; font-size:14px; font-weight:bold;">
					票据查看--
					<s:property value="relateBillInfo.billId" />
					<s:hidden name="relateBillId" value="%{relateBillInfo.billId}"></s:hidden>
					<s:hidden id="customerId" value="%{relateBillInfo.customerId}"></s:hidden>
				</div>
				<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0" style="background:#FFF;">
					<tr>
						<td width="15%" style="text-align: right" class="listbar">申请开票日期:</td>
						<td width="35%">
							<s:property value="relateBillInfo.applyDate" />
						</td>
						<td width="15%" style="text-align: right" class="listbar">开票日期:</td>
						<td>
							<s:property value="relateBillInfo.billDate" />
						</td>
					</tr>
					<tr>
						<td width="15%" style="text-align: right" class="listbar">发票类型:</td>
						<td width="35%">
							<s:property value="@com.cjit.vms.trans.util.DataUtil@getFapiaoTypeCH(relateBillInfo.fapiaoType)" />
						</td>
						<td width="15%" style="text-align: right" class="listbar">客户名称</td>
						<td width="35%">
							<s:property value="relateBillInfo.customerName" />
						</td>
					</tr>
					<tr>
						<td width="15%" style="text-align: right" class="listbar">客户纳税人识别号:</td>
						<td width="35%">
							<s:property value="relateBillInfo.customerTaxno" />
						</td>
						<td width="15%" style="text-align: right" class="listbar">客户地址电话:</td>
						<td>
							<s:property value="relateBillInfo.customerAddressandphone" />
						</td>
					</tr>
					<tr>
						<td width="15%" style="text-align: right" class="listbar">客户开户行及账号:</td>
						<td width="35%">
							<s:property value="relateBillInfo.customerBankandaccount" />
						</td>
						<td width="15%" style="text-align: right" class="listbar">合计金额:</td>
						<td id="amtSum">
							<s:property value="relateBillInfo.amtSum" />
						</td>
					</tr>
					<tr>
						<td width="15%" style="text-align: right" class="listbar">合计税额:</td>
						<td width="35%">
							<s:property value="relateBillInfo.taxAmtSum" />
						</td>
						<td width="15%" style="text-align: right" class="listbar">价税合计:</td>
						<td id="sumAmt">
							<s:property value="relateBillInfo.sumAmt" />
						</td>
					</tr>
				</table>
				<table class="lessGrid" id="relateBillItemList" cellspacing="0" rules="all" border="0" cellpadding="0" style="border-collapse: collapse;width: 100%;">
					<tr>
						<th style="text-align: center"></th>
						<th style="text-align: center">商品名称</th>
						<th style="text-align: center">规格型号</th>
						<th style="text-align: center">单位</th>
						<th style="text-align: center">商品数量</th>
						<th style="text-align: center">商品单价</th>
						<th style="text-align: center">金额</th>
						<th style="text-align: center">税率</th>
						<th style="text-align: center">税额</th>
					</tr>
					<s:iterator value="relateBillItemList" id="iList" status="stuts">
						<tr align="center" class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
							<td>
								<input type="radio" name="goodsId" value="<s:property value='goodsId' />" />
								<s:hidden name="billItemId"></s:hidden>
							</td>
							<td>
								<s:property value='goodsName' />
							</td>
							<td>
								<s:property value='specandmodel' />
							</td>
							<td>
								<s:property value='goodsUnit' />
							</td>
							<td>
								<s:property value='goodsNo' />
							</td>
							<td>
								<s:property value='goodsPrice' />
							</td>
							<td>
								<s:property value='amt' />
								<s:hidden name="goodsAmt" value="%{amt}"></s:hidden>
							</td>
							<td>
								<s:property value='taxRate' />
							</td>
							<td>
								<s:property value='taxAmt' />
								<s:hidden name="goodsTaxAmt" value="%{taxAmt}"></s:hidden>
							</td>
						</tr>
					</s:iterator>
				</table>
				<div class="windowtitle" style=" background:#004C7E; height:30px; line-height:30px; padding-left:10px; color:#FFF;">已勾稽的数据</div>
				<div style="overflow: auto; height:232px;">
					<table id="relatedTable" id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
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
							<th style="text-align: center">钩稽金额</th>
							<th style="text-align: center">可钩稽金额</th>
							<th style="text-align: center">发票类型</th>
							<th style="text-align: center">交易状态</th>
							<th style="text-align: center">撤销勾稽</th>
						</tr>
						<s:iterator value="relatedBillTransList" id="iList" status="stuts">
							<tr id="<s:property value='transId' />" align="center" class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
								<td align="center">
									<%-- <s:property value='#stuts.count' />
									<s:hidden name="relateTransIds" value="%{transId}" />
									<s:hidden name="relateTaxRates" value="%{taxRate}" />
									<s:hidden name="relateItemIds" value="%{relateBillItemId}"></s:hidden>
									<s:hidden name="balance"></s:hidden>
									<s:hidden name="taxCnyBalance"></s:hidden> --%>
									<span class="index">
										<s:property value='#stuts.count' />
									</span>
									<s:hidden name="relateTransIds" value="%{transId}" />
									<s:hidden name="relateTaxRates" value="%{taxRate}" />
									<s:hidden name="relateItemIds" value="%{relateItemId}"></s:hidden>
									<s:hidden name="transGoodsId" value="%{goodsId}"></s:hidden>
									<s:hidden name="balance" value="%{relateBalance}"></s:hidden>
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
								<td>
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
										<s:property value='dataSources' />
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
								<td>
									<s:textfield name="relateAmts" cssClass="tbl_query_text" value="%{relateAmt}"></s:textfield>
								</td>
								<td>
									<s:property value="relateBalance" />
								</td>
								<td>
									<s:property value="@com.cjit.vms.trans.util.DataUtil@getFapiaoTypeCH(fapiaoType)" />
								</td>
								<td>
									<s:property value="@com.cjit.vms.trans.util.DataUtil@getDataStatusCH(#iList.dataStatus,'TRANS')" />
								</td>
								<td>
									<a href="javascript:void(0);" class="action">取消钩稽</a>
								</td>
							</tr>
						</s:iterator>
					</table>
				</div>
				<div class="windowtitle" style=" background:#004C7E; height:30px; line-height:30px; padding-left:10px; color:#FFF;">未勾稽的数据</div>
				<iframe id="iframepage" name="iframepage" frameborder="0" marginheight="0" style="overflow: auto; width: 100%; height:310px; background:#FFF;"> </iframe>
			</div>
			<div id="ctrlbutton" class="ctrlbutton" style="border:0px">
				<input type="button" class="tbl_query_button" onclick="submitForm();" name="BtnSave" value="提交" id="BtnSave" />
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button" class="tbl_query_button" onclick="CloseWindow();" name="BtnReturn" value="取消" id="BtnReturn" />
			</div>
		</form>
	</div>
</body>
</html>