<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%@include file="../../page/include.jsp"%>
<base target="_self">
<meta http-equiv='pragma' content='no-cache'>
<meta http-equiv='cache-control' content='no-cache'>  
<META HTTP-EQUIV="Expires" CONTENT="0">
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
	<title>拆分开票</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
	<script type="text/javascript" src="<c:out value="${webapp}"/>/page/js/jquery/jquery_1.42.js"></script>
	<script type="text/javascript">
	function save(){
		//获取tr数
		var trNum = $("#tbody tr").length;
		var myAllMoney = "";
		var myMoney = "";
		var myMoneyNum = 0;
		for(var i=0;i<trNum;i++){
			myMoney = document.getElementById("money"+(i+1));
			if($.trim(myMoney.value)==""){
				alert("金额" + "不能为空");
				//url.focus();
				return false;
			}
			
			if(isNaN($.trim(myMoney.value))){
				alert("请输入数值型！");
				//url.focus();
				return false;
			}
			
			if($.trim(myMoney.value)<=0){
				alert("金额" + "必须大于0");
				//url.focus();
				return false;
			}
            var reg = new RegExp("^[0-9]+(.[0-9]{0,2})?$", "g");
            if (!reg.test($.trim(myMoney.value))) {
				alert("金额" + "最多只能有两位小数");
				//url.focus();
				return false;
			}
			myMoneyNum += parseFloat(myMoney.value);
			myAllMoney += myMoney.value+"_";
		}
		document.getElementById("allMoney").value = myAllMoney;
		var moneyTotal = document.getElementById("moneyTotal").value;
		moneyTotal = parseFloat(moneyTotal);
		if(myMoneyNum.toFixed(2) - moneyTotal.toFixed(2) > 0){
			alert("输入金额不能超过总金额！");
			return false;
		}else if(myMoneyNum.toFixed(2) < moneyTotal.toFixed(2)){
			//alert("输入金额不等于总金额,请继续输入金额！");
			//return false;
		}
		window.form1.submit();
	}
	function addLine(){
	    var trNumPre = $("#tbody tr").length;
	    var preMoney = $("#tbody .tbl_query_text2:last");
	    //当前行金额不为空才可加行
	    if($.trim(preMoney.val())!=""){
			var trNum =  $("#tbody tr").length+1;
			var newTr = "<tr id='tr" + trNum + "'><td align='center' style='background-color:#ffffff;'>" 
			    + trNum + "</td><td align='center' style='background-color:#ffffff;'> <input type='text' class='tbl_query_text2' id='money" + trNum
			    + "' name='money' value='' onkeypress='checkkey(this);' onkeyup='changeMoney(this);' > </td><td style='background-color:#ffffff;'>&nbsp;</td></tr>";
			$("#tbody").append(newTr);
		}
	}
	
	function subtractLine(){
		// 得到最后一行tr 从最后一行逐行往上删除并重新计算金额
		var trNum = $("#tbody tr").length;
		if(trNum==1){
			alert("已经为最后一行," + "不能删除");
			//url.focus();
			return false;
		}
	    $("#tbody").find("tr:last").remove();
	    changeMoney();
	}
	
	
	function checkkey(v){
		var kc=event.keyCode;
		//只允许输入“+”“-”“.”“回车”“0-9”等字符。
		if (kc==43 || kc ==45 || kc ==46 || (kc >47 && kc <58)){
			if(kc==46 && v.indexOf('.') >-1){
				//不允许输入多个小数点
				event.returnValue = false; 
			}
			if(kc==43){
				//不允许输入多个正号，不允许在数字中间输入正号 
				if (v.length>0 && v.indexOf('+')>-1){
				//event.returnValue = false;
				}
			}else if (kc==45){
				//不允许输入多个负号，不允许在数字中间输入负号 
				if (v.length>0 && v.indexOf('-')>-1){
					//event.returnValue = false;
				}
			}
		}else{
			event.returnValue = false;
			alert("请输入数值类型");
		}
	}
	function changeMoney(obj){
		var moneyTotal = document.getElementById("moneyTotal").value;
		//获取tr数 从而获取所有格子的金额值
		var trNum = $("#tbody tr").length;
		var myMoney = "";
		var myMoneyNum = 0;
		for(var i=0;i<trNum;i++){
			myMoney = document.getElementById("money"+(i+1));
			if($.trim(myMoney.value)!=""){
				// 相加各个金额
				myMoneyNum += parseFloat(myMoney.value);
			}
		}
		moneyTotal = parseFloat(moneyTotal);
		var moneySy = moneyTotal - myMoneyNum;
		document.getElementById("moneySy").innerHTML = moneySy.toFixed(2);
	}
	<%
		String message = (String)request.getAttribute("message");
		if (message != null && !"".equals(message)){
		    message = message.replaceAll("MoneyError", "拆分金额格式不对，请输入小数位最多2位的正数数字。");
			message = message.replaceAll("AmtOverFull", "存在交易金额超出单张发票开票金额限额的交易，请做拆分开票。");
			message = message.replaceAll("NotExistsTaxRate", "存在税目信息表中无对应税率信息的交易，不能进行开票。");
			message = message.replaceAll("NotExistsGoods", "存在无对应发票商品的交易信息，不能进行开票。");
			message = message.replaceAll("NotExistsTrans", "不存在对应交易交易信息，不能进行开票。");
			message = message.replaceAll("TransStatusError", "交易信息不是未开票状态，不能进行开票。");
	%>
			alert('<%=message%>');
	<%
		}
	%>
	</script>
	</head>
	<body scroll="no" style="overflow:hidden;">
	<%
		String transId = (String)request.getAttribute("transId");
		String userId = (String)request.getAttribute("userId");
		String amt = (String)request.getAttribute("amt");
		String saveFlag = (String)request.getAttribute("saveFlag");
		if ("1".equals(saveFlag)){
	%>
	<script type="text/javascript">
	</script>
	<%
		}
	%>
	<div class="showBoxDiv">
	<form name="form1" id="form1" action="transToManyBill.action" method="post">
	<div id="editsubpanel" class="editsubpanel">
	<div style="overflow: auto; width: 100%;" >  
		<input type="hidden" name="transId" id="transId" value="<%=transId%>" />
		<input type="hidden" name="userId" id="userId" value="<%=userId%>" />
		<input type="hidden" name="moneyTotal" id="moneyTotal" value="<%=amt%>" />
		<input type="hidden" name="allMoney" id="allMoney" value=""/>
	<div class="windowtitle" style="background:#004C7E; height:45px; line-height:45px; text-align:center; color:#FFF; font-size:14px; font-weight:bold;">拆分开票</div>
	<div style="width:100%;align:center;" >
		<table cellpadding="0" cellspacing="5" width="100%" border="0" id="mTable">
			<tr>
				<td align="right" style="width:15%;">总金额:&nbsp;&nbsp;</td>
				<td style="width:45%"><%=amt%></td>
				<td align="left">
					<input name="add" type="button" id="add" value="+" onclick="addLine()" class="tbl_query_button"/>
				</td>
				<td align="left">
					<input name="subtract" type="button" id="subtract" value="-" onclick="subtractLine()" class="tbl_query_button"/>
				</td>
			</tr>
			<tr>
				<td align="right">剩余金额:&nbsp;&nbsp;</td>
				<td align="left" id="moneySy"><%=amt%></td>
			</tr>
		</table>
	</div>
	<div style="width: 100%;align:center;" id="lessGridList">
		<table class="lessGrid" cellspacing="0" rules="all" border="0" cellpadding="0" style="border-collapse: collapse; width: 100%;">
			<tr class="lessGrid head">
				<th width="15%" style="text-align: center;background-color:#00A9DA;">序号</th>
				<th width="65%" style="text-align: center;background-color:#00A9DA;">金额</th>
				<th width="20%" style="text-align: center;background-color:#00A9DA;">备注</th>
			</tr>
			<tbody id="tbody">
				<tr id="tr1">
					<td align="center"><s:property value="1"/></td>
					<td align="center" >
						<input type="text"  class="tbl_query_text2"  id="money1" name="money" value="" onkeypress="checkkey(this);" onkeyup="changeMoney(this);">
					</td>
					<td >金额最多只能有两位小数。</td>
				</tr>
			</tbody>
		</table>
		<table cellpadding="0" cellspacing="5" width="100%" border="0" id="mTable">
			<tr>
				<td colspan="2" align="left">
					<input type="button" class="tbl_query_button" onclick="save()" name="BtnView" value="保存" id="BtnView"/>
				</td>
			</tr>
		</table>
	</div>
	</div>
	</div>
</form>
</div>
</body>
</html>