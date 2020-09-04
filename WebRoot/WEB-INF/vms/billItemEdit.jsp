<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="cjit.crms.util.StringUtil"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.vms.trans.model.BillInfo"
	import="com.cjit.vms.trans.model.BillItemInfo"
	import="com.cjit.vms.trans.model.TransInfo"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
    <title>增值税管理平台</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
    <link href="<%=bopTheme2%>/css/editView.css" type="text/css" rel="stylesheet">
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/calendar_notime.js"></script>
    <script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/ylb.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/editview.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/XmlHttp.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/vms.js"></script>
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
	<script>
	 	var msg = '<s:property value="message"/>';
		if (msg != null){
			if (msg.indexOf("saveSuccess") > -1){
				msg = "保存数据成功！";
			} else if (msg.indexOf("checkFailure") > -1){
				msg = "数据逻辑校验未通过，请查看校验结果！";
			} else if (msg.indexOf("saveFailure") > -1){
				msg = "保存数据失败，请检查数据类型！";
			} else if (msg.indexOf("checkPass") > -1){
				msg = "数据逻辑校验通过！";
			}
		}
		var page = {};
		page.index = 0;
		page.start = true;
		page.count = 0;
		page.dispatch = function(){
			if(msg != null && msg.indexOf("数据逻辑校验通过") > -1){
				alert(msg);
				document.forms[0].action='listDatasInner.action';
				document.forms[0].submit();
			}else if(msg != null && msg.indexOf("保存数据成功") > -1){
				alert(msg);
				document.forms[0].action='listDatasInner.action';
				document.forms[0].submit();
			}else if(msg != null && msg.length>1){
				alert(msg);
			}
			else{}
		}
		page.test = function(){
			var str = "";
			var elements = document.forms[0].elements;
			for(var i=0; i<elements.length; i++){
				var obj = elements.elements(i);
				str += obj.id + ", " +  obj.type + "\r\n<p>";
			}
			document.write(str);
		}
		page.setIndex = function(){
			var elements = document.forms[0].elements;
			for(var i=0; i<elements.length; i++){
				var obj = elements.elements(i);
				if("text" == obj.type || "checkbox" ==obj.type){
					page.index = i-2;
					break;
				}
			}
			page.count = elements.length;
		}
		page.gogogo = function() {
			if(page.start){
				page.setIndex();
				page.start=false;
				//alert(page.index);
			}
  			if(event.keyCode==13) {
  				document.forms[0].submit()
  				return false;
  			}
  		}
  		document.onkeydown=page.gogogo;
		function loadTransTypeInfo(transType){
			var webroot = '<%=webapp%>';
			var url = webroot + "/loadTransTypeInfo.action";
			var param = "transType=" + transType;
			if (document.getElementById("billItem.goodsNo") != null){
				param += "&number=" + document.getElementById("billItem.goodsNo").value;
			}
			if (document.getElementById("billItem.goodsPrice") != null){
				param += "&price=" + document.getElementById("billItem.goodsPrice").value;
			}
			var result = sendXmlHttpPost(url, param);
			var vInfos = result.split(";");
			// 是否含税;税率;金额;税额
			if (vInfos.length == 4){
				if (document.getElementById("billItem.taxFlag") != null){
					document.getElementById("billItem.taxFlag").value = vInfos[0];
					if (vInfos[0] == '1'){
						document.getElementById('taxFlag').innerText = '含税';
					}else if (vInfos[0] == '0'){
						document.getElementById('taxFlag').innerText = '不含税';
					}
				}
				if (document.getElementById("billItem.taxRate") != null){
					document.getElementById("billItem.taxRate").value = vInfos[1];
					document.getElementById('taxRate').innerText = vInfos[1];
				}
				if (document.getElementById("billItem.amt") != null){
					document.getElementById("billItem.amt").value = vInfos[2];
					document.getElementById('amt').innerText = vInfos[2];
				}
				if (document.getElementById("billItem.taxAmt") != null){
					document.getElementById("billItem.taxAmt").value = vInfos[3];
					document.getElementById('taxAmt').innerText = vInfos[3];
				}
			}
		}
		function changePriceOrNumber(){
			var vPrice = document.getElementById("billItem.goodsPrice").value;
			var vNumber = document.getElementById("billItem.goodsNo").value;
			var vTaxFlag = document.getElementById("billItem.taxFlag").value;
			var vTaxRate = document.getElementById("billItem.taxRate").value;
			if (vNumber != null && vPrice != null){
				// 计算金额
				var vAmt = vPrice * vNumber;
				document.getElementById("billItem.amt").value = vAmt.toFixed(2);
				document.getElementById('amt').innerText = vAmt.toFixed(2);
				// 计算税额
				if (vTaxFlag == '1' && vTaxRate != null){
					var vTaxAmt = vTaxRate * vAmt;
					document.getElementById("billItem.taxAmt").value = vTaxAmt.toFixed(2);
					document.getElementById('taxAmt').innerText = vTaxAmt.toFixed(2);
				}
			}
		}
		function changeDiscountRate(){
			var vDisRate = document.getElementById("billItem.discountRate").value;
			var vAmt = document.getElementById("billItem.amt").value;
			var vTaxAmt = document.getElementById("billItem.taxAmt").value;
			var vDisAmt = document.getElementById("disAmt").value;
			var vDisTaxAmt = document.getElementById("disTaxAmt").value;
			if (vDisRate != null){
				if (vAmt != null){
					vDisAmt = eval(vAmt) - eval(vDisRate * vAmt);
					document.getElementById('disAmt').innerText = vDisAmt.toFixed(2);
				}
				if (vTaxAmt != null){
					vDisTaxAmt = eval(vTaxAmt) - eval(vDisRate * vTaxAmt);
					document.getElementById('disTaxAmt').innerText = vDisTaxAmt.toFixed(2);
				}
			}
		}
		function save(){
			if(saveBillItemCheck()){
				document.forms[0].submit();
			}
		}
	</script>
</head>
<body onload="page.dispatch()">
    <form name="frm" action="<s:if test="flag=='dis'">disBillItem</s:if><s:else>saveBillItem</s:else>.action" method="post">
	<%
		BillItemInfo billItem = (BillItemInfo)request.getAttribute("billItem");
		String flag = (String)request.getAttribute("flag");
		String disabled = "";
		if ("dis".equals(flag)){
			disabled = " readonly class=\"readOnlyText\" onfocus=\"this.blur();\" ";
		}
	%>
	<div style="height:110px">	
		<div id="editpanel">
			<div id="editsubpanel" class="editsubpanel">
			<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0" >			
				<input type="hidden" name="billItem.billId" value="<s:property value='billItem.billId'/>"/>
				<input type="hidden" name="billItem.billItemId" value="<s:property value='billItem.billItemId'/>"/>
				<input type="hidden" name="billItem.transId" value="<s:property value='billItem.transId'/>"/>
				<input type="hidden" name="billItem.disItemId" value="<s:property value='billItem.disItemId'/>"/>
			<tr class="row1">
				<td colspan="6">
					<%=!"dis".equals(flag)?"票据明细信息":"原票据明细信息"%>
				</td>
			</tr>
			<tr class="row1">
				<td align="right" width="10%">交易类型:&nbsp;</td>
				<td align="left" width="20%">
					<s:select id="billItem.transType" name="billItem.transType" 
						onchange="loadTransTypeInfo(this.value)" headerKey="" headerValue="请选择..." 
						list="businessList" listKey='businessCode' listValue='businessCName'/>
				</td>
				<td align="right" width="10%">商品名称:&nbsp;</td>
				<td align="left" width="30%">
					<input type="text" name="billItem.goodsName" value="<s:property value='billItem.goodsName'/>" <%=disabled%> />
				</td>
				<td align="right" width="10%">规格型号:&nbsp;</td>
				<td align="left" width="20%">
					<input type="text" name="billItem.specandmodel" value="<s:property value='billItem.specandmodel'/>" <%=disabled%> />
				</td>
			</tr>
			<tr class="row1">
				<td align="right">商品数量:&nbsp;</td>
				<td>
					<input type="text" name="billItem.goodsNo" value="<s:property value='billItem.goodsNo'/>" onkeypress="checkkey(value);" onblur="formatAmount(this,2,0,'true');" onchange="changePriceOrNumber()" <%=disabled%> />
				</td>
				<td align="right">商品单价:&nbsp;</td>
				<td>
					<input type="text" name="billItem.goodsPrice" value="<s:property value='billItem.goodsPrice'/>" onkeypress="checkkey(value);" onblur="formatAmount(this,2,0,'true');" onchange="changePriceOrNumber()" <%=disabled%> />
				</td>
				<td></td>
				<td></td>
			</tr>
			<tr class="row1">
				<td align="right">金额:&nbsp;</td>
				<td>
					<label id="amt"><s:property value='billItem.amt'/></label>
					<input type="hidden" name="billItem.amt" value="<s:property value='billItem.amt'/>" />
				</td>
				<td align="right">税率:&nbsp;</td>
				<td>
					<label id="taxRate"><s:property value='billItem.taxRate'/></label>
					<input type="hidden" name="billItem.taxRate" value="<s:property value='billItem.taxRate'/>" />
				</td>
				<td align="right">税额:&nbsp;</td>
				<td>
					<label id="taxAmt"><s:property value='billItem.taxAmt'/></label>
					<input type="hidden" name="billItem.taxAmt" value="<s:property value='billItem.taxAmt'/>" />
				</td>
			</tr>
	<%
		if ("dis".equals(flag)){
	%>
			<tr class="row1">
				<td colspan="6">
					折扣记录信息
				</td>
			</tr>
			<tr class="row1">
				<td align="right">折扣率:&nbsp;</td> 
				<td>
					<input type="text" name="billItem.discountRate" id="billItem.discountRate" value="<s:property value='billItem.discountRate'/>"
						onkeypress="checkkey(value);" onblur="changeDiscountRate();formatAmount(this,2,0,'true');"/>
				</td>
				<td align="right">折扣金额:&nbsp;</td>
				<td>
					<label id="disAmt"><s:property value='disAmt'/></label>
				</td>
				<td align="right">折扣税额:&nbsp;</td>
				<td>
					<label id="disTaxAmt"><s:property value='disTaxAmt'/></label>
				</td>
			</tr>
	<%
		}
	%>
			</table>
			</div>
		</div>
	</div>
	<div id="ctrlbutton" class="ctrlbutton" style="border:0px">
		<s:if test="!configForbidSave.equals('yes')">
		<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 
			 onclick="save();" name="BtnSave" value="保存" id="BtnSave"/>
		</s:if>
		<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 
			 onclick="document.forms[0].action='listBillItem.action?billId=<s:property value='billItem.billId'/>';document.forms[0].submit();" name="BtnView" value="返回" id="BtnView"/>            
	</div>
    </form>
    <script language="javascript">
		document.forms[0].elements["billItem.transType"].focus();
		if (document.getElementById("billItem.taxFlag").value == '1'){
			document.getElementById('taxFlag').innerText = '含税';
		} else if (document.getElementById("billItem.taxFlag").value == '0'){
			document.getElementById('taxFlag').innerText = '不含';
		}
	</script>
</body>
</html>
