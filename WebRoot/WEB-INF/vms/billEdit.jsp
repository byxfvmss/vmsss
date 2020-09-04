<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
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
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/editview.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/XmlHttp.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/vms.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/lhb.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
	<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
<!-- 	<link href="<c:out value="${bopTheme2}"/>/css/subWindow.css" type="text/css" rel="stylesheet"> -->
	<link href="<%=bopTheme2%>/css/editView.css" type="text/css" rel="stylesheet">
	<script language="javascript" type="text/javascript">
		var issave = false;
		var msg = '<s:property value="message"/>';
		function save(){
			if(issave){
				alert("数据保存中，请稍候...");
				return false;
			}
			
			var strPayee = document.getElementById("payee").value;
			var lenPayee = 0;
		    for (var i=0; i<strPayee.length; i++) { 
		     var c = strPayee.charCodeAt(i); 
		    //单字节加1 
		     if ((c >= 0x0001 && c <= 0x007e) || (0xff60<=c && c<=0xff9f)) { 
		    	 lenPayee++; 
		     } 
		     else { 
		    	 lenPayee+=2; 
		     } 
		    } 
		    if(lenPayee>16){
		    	alert("收款人名称过长！");
		    	return;
		    }
		    
		    var strRemark = document.getElementById("remark").value;
		    var fapiaoType = document.getElementById("fapiaoType").value;
			var lenRemark = 0;
		    for (var i=0; i<strRemark.length; i++) { 
		     var c = strRemark.charCodeAt(i); 
		    //单字节加1 
		     if ((c >= 0x0001 && c <= 0x007e) || (0xff60<=c && c<=0xff9f)) { 
		    	 lenRemark++; 
		     } 
		     else { 
		    	 lenRemark+=2; 
		     } 
		    } 
		    if(lenRemark>150&&fapiaoType=='1'||lenRemark>150&&fapiaoType=='0'){
		    	alert("附言过长！");
		    	return;
		    }
		    
			var isblank=true;
			var oo = document.getElementById("Form1").getElementsByTagName("input");
			for(var k = 0, len = oo.length; k < len; k++){
				var t = oo[k].type;
				if(t=="text" ){
					var v=oo[k].value;
					if(v&&v!="" &&v.replace(/(^\s*)|(\s*$)/g, "")!=""){
						isblank=false;
						break;
					}
				}
			}
			if (document.getElementById('submitFlag').value=='S'){
				if (!submitBillCheck()){
					return false;
				}
			}
			if(!isblank){
				document.getElementById("Form1").submit();
				issave = true;
				if (document.getElementById('submitFlag').value=='S'){
				    alert("提交成功");
				} else {
				    alert("保存成功");
				}
			}else{
				alert("表单没有输入数据,无需保存。 \n\n如要退出，请点击'返回'按钮");
			}
		}
		function revoke(){
			var billId = document.forms[0].elements['billInfo.billId'].value;
			submitAction(document.forms[0], "revokeBill.action?selectBillIds=" + billId);
		}
		function loadOurInfo(instCode){
			var webroot = '<%=webapp%>';
			var url = webroot + "/loadOurInfo.action";
			var param = "instCode=" + document.getElementById("instCode").value;
			var result = sendXmlHttpPost(url, param);
			var vOurInfos = result.split("###");
			if (vOurInfos.length == 6){
				if (document.getElementById("billInfo.name") != null){
					document.getElementById("billInfo.name").value = vOurInfos[0];
				}
				if (document.getElementById("billInfo.taxno") != null){
					document.getElementById("billInfo.taxno").value = vOurInfos[1];
				}
				if (document.getElementById("billInfo.addressandphone") != null){
					document.getElementById("billInfo.addressandphone").value = vOurInfos[2] + " " + vOurInfos[3];
				}
				if (document.getElementById("billInfo.bankandaccount") != null){
					document.getElementById("billInfo.bankandaccount").value = vOurInfos[4] + " " + vOurInfos[5];
				}
			}
		}
		function loadBillAmtInfo(){
			var webroot = '<%=webapp%>';
			var url = webroot + "/loadBillAmtInfo.action";
			var billId = document.forms[0].elements['billInfo.billId'].value;
			if (billId == null || billId == ''){
				return false;
			}
			var result = sendXmlHttpPost(url, "billId=" + billId);
			var vAmtInfos = result.split("###");
			if (vAmtInfos.length == 3){
				if (document.getElementById("billInfo.amtSum") != null){
					document.getElementById("billInfo.amtSum").value = vAmtInfos[0];
				}
				if (document.getElementById("billInfo.taxAmtSum") != null){
					document.getElementById("billInfo.taxAmtSum").value = vAmtInfos[1];
				}
				if (document.getElementById("billInfo.sumAmt") != null){
					document.getElementById("billInfo.sumAmt").value = vAmtInfos[2];
				}
			}
		}

		function revokeToList(){
			submitAction(document.forms[0], "listBillModify.action?fromFlag=view");
		}
	</script>
	<script language="javascript" type="text/javascript">
		var vDataStatus = $("#dataStatus").val();
		if (vDataStatus == "<%=DataUtil.BILL_STATUS_2%>" || vDataStatus == "<%=DataUtil.BILL_STATUS_4%>"){
			document.getElementById("btSave").disabled = true;
			document.getElementById("btSubmit").disabled = true;
			document.getElementById("btCancel").disabled = true;
		}
	</script>
</head>
<body onLoad="javascript:tips()" onmousemove="MM(event)" onmouseout="MO(event)">
	<form name="Form1" method="post" action="saveBill.action" id="Form1">
	<input type="hidden" name="billInfo.billId" value="<s:property value='billInfo.billId'/>"/>

	<input type="hidden" id="dataStatus" name="billInfo.dataStatus" value="<s:property value='billInfo.dataStatus'/>"/>
	<input type="hidden" name="submitFlag" id="submitFlag" value="U"/>
	<div id="tbl_current_status">
		<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> 
		<span class="current_status_menu">当前位置：</span> 
		<span class="current_status_submenu1">销项税管理</span>
		<span class="current_status_submenu">开票管理</span>
		<span class="current_status_submenu">票据编辑</span>
		<span class="current_status_submenu">信息编辑</span>
	</div>
	<div class="centercondition">
		<div class="blankbox" id="blankbox" style="overflow:auto;height: 100%;" >
			<table id="contenttable" class="lessGridS"  cellspacing="0" width="100%" align="center" cellpadding="0" style="border-collapse: collapse;">
			<tr>
				<s:if test='configCustomerFlag.equals("KBC")'>
				<td align="right" style="width: 10%">
					客户号
				</td>
				<td align="left" style="width: 25%">
					<input type="text" class="tbl_query_text_readonly" name="billInfo.customerId" id="billInfo.customerId" value="<s:property value='billInfo.customerId'/>" size="80" readonly />
				</td>
				</s:if>
				<td align="right" style="width: 10%">
					客户纳税人名称
				</td>
				<td align="left" style="width: 25%">
					<input type="text" class="tbl_query_text_readonly" name="billInfo.customerName" id="billInfo.customerName" value="<s:property value='billInfo.customerName'/>" size="80" readonly />
				</td>
				<td align="right" style="width: 10%">
					客户纳税人识别号
				</td>
				<td align="left" style="width: 25%">
					<input type="text" class="tbl_query_text_readonly" name="billInfo.customerTaxno" id="billInfo.customerTaxno" value="<s:property value='billInfo.customerTaxno'/>" size="80" readonly />
				</td>
				<td align="right" style="width: 20%">
					发票类型
				</td>
				<td style="width: 15%">
					<select id="fapiaoType" name="billInfo.fapiaoType">
						发票类型
						<option value="0" <s:if test='billInfo.fapiaoType=="0"'>selected</s:if> <s:else></s:else>>
							增值税专用发票
						</option>
						<option value="1" <s:if test='billInfo.fapiaoType=="1"'>selected</s:if> <s:else></s:else>>
							增值税普通发票
						</option>
					</select>
				</td>
				
			</tr>
			<tr>
				<td align="right">
					客户地址电话
				</td>
				<td align="left" >
					<input type="text" class="tbl_query_text_readonly" name="billInfo.customerAddressandphone" id="billInfo.customerAddressandphone" value="<s:property value='billInfo.customerAddressandphone'/>" size="80" readonly />
				</td>
				<td align="right">
					客户银行账号
				</td>
				<td align="left" >
					<input type="text" class="tbl_query_text_readonly" name="billInfo.customerBankandaccount" id="billInfo.customerBankandaccount" value="<s:property value='billInfo.customerBankandaccount'/>" size="80" readonly />
				</td>
			</tr>
			<tr>
				<td align="right">
					价税合计
				</td>
				<td>
					<!-- <input type="text" name="billInfo.sumAmt" id="billInfo.sumAmt" value="<s:property value='billInfo.sumAmt'/>"
						onfocus="loadBillAmtInfo();" onkeypress="checkkey(value);" onblur="formatAmount(this,2,0,'true');"/> -->
					<input type="text" class="tbl_query_text_readonly " name="billInfo.sumAmt" id="billInfo.sumAmt" value="<s:property value='billInfo.sumAmt'/>" readonly />
				</td>
				<td align="right">
					合计金额
				</td>
				<td>
					<input type="text" class="tbl_query_text_readonly" name="billInfo.amtSum" id="billInfo.amtSum" value="<s:property value='billInfo.amtSum'/>"
						onfocus="loadBillAmtInfo();" onkeypress="checkkey(value);" onblur="formatAmount(this,2,0,'true');" readonly />
				</td>
				<td align="right" style="width: 10%">
					合计税额
				</td>
				<td style="width: 25%">
					<input type="text" class="tbl_query_text_readonly" name="billInfo.taxAmtSum" id="billInfo.taxAmtSum" value="<s:property value='billInfo.taxAmtSum'/>"
						onfocus="loadBillAmtInfo();" onkeypress="checkkey(value);" onblur="formatAmount(this,2,0,'true');" readonly />
				</td>
			</tr>
			
			<tr>
				<td align="right" style="width: 10%;padding-top: 20px;">
					我方开票机构
				</td>
				<td style="width: 25%">
					<s:if test="authInstList != null && authInstList.size > 0">
					<s:select name="instCode" list="authInstList" listKey='id' listValue='name' onchange="loadOurInfo(this.value)" disabled="true" />
					</s:if>
					<s:if test="(authInstList == null || authInstList.size == 0)&&(null==instCode||''==instCode)">
					<select name="instCode" class="readOnlyText">
					<option value="">请分配机构权限</option>
					</select>
					</s:if>
					<s:if test="(authInstList == null || authInstList.size == 0)&&(null!=instCode||''!=instCode)">
					<select name="instCode" class="readOnlyText">
					<option value="<s:property value='instCode'/>"><s:property value="billInfo.bankName"/></option>
					</select>
					</s:if>
				</td>
				<td align="right">
					我方纳税人名称
				</td>
				<td>
					<input type="text" class="tbl_query_text_readonly" name="billInfo.name" id="billInfo.name" value="<s:property value='billInfo.name'/>" size="80" readonly />
				</td>
				<td></td>
				<td></td>
			</tr>
			<tr>
				<td align="right">
					我方纳税人识别号
				</td>
				<td>
					<input type="text" class="tbl_query_text_readonly" name="billInfo.taxno" id="billInfo.taxno" value="<s:property value='billInfo.taxno'/>" size="80" readonly />
				</td>
				<td align="right">
					我方地址电话
				</td>
				<td>
					<input type="text" class="tbl_query_text_readonly" name="billInfo.addressandphone" id="billInfo.addressandphone" value="<s:property value='billInfo.addressandphone'/>" size="80" readonly/>
				</td>
				<td></td>
				<td></td>
			</tr>
			<tr>
				<td align="right">
					我方银行账号
				</td>
				<td>
					<input type="text" class="tbl_query_text_readonly" name="billInfo.bankandaccount" id="billInfo.bankandaccount" value="<s:property value='billInfo.bankandaccount'/>" size="80" readonly/>
				</td>

				<td >
				</td>
				<td></td>
				<td></td>
			</tr>
			<tr>
				<td align="right">
					备注 
				</td>
				<td colspan="4">
					<textarea name="billInfo.remark" id="remark" cols="600" rows="5"><s:property value='billInfo.remark'/></textarea>
				</td>
			</tr>
			<tr>
				<td align="right">
					收款人
				</td>
				<td>
					<input type="text" class="tbl_query_text" name="billInfo.payee" id="payee" value="<s:property value='billInfo.payee'/>"/>
				</td>
				<td align="right">
					复核人
				</td>
				<td>
					<input type="text" class="tbl_query_text" name="billInfo.reviewer" id="billInfo.reviewer" value="<s:property value='billInfo.reviewer'/>" readonly />
				</td>
				<td align="right">
					开票人
				</td>
				<td>
					<input type="text" class="tbl_query_text" name="billInfo.drawer" id="billInfo.drawer" value="<s:property value='billInfo.drawer'/>" readonly />
				</td>
			</tr>
			</table>
			<iframe id="subTableFrame" scrolling="auto" src="listBillItemYS.action?billId=<s:property value='billInfo.billId'/>&isHandiwork=<s:property value='billInfo.isHandiwork'/>"  width="100%" height="100px;" frameborder="0" style="background:#FFF;"></iframe>
		</div>
		<div class="bottombtn">
			<input type="button" class="tbl_query_button" value="保存"
			onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
			name="btSave" id="btSave" onclick="save()" />
			<input type="button" class="tbl_query_button" value="提交"
			onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
			name="btSubmit" id="btSubmit" onclick="document.getElementById('submitFlag').value='S';save()" />
			<input type="button" class="tbl_query_button" value="返回"
			onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
			name="btCancel" id="btCancel" onclick="revokeToList()" />
		</div>
	</div>
	<!-- <div id="ctrlbutton" class="ctrlbutton" style="border:0px">
		<input type="button" class="tbl_query_button" value="保存"
			onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
			name="btSave" id="btSave" onclick="save()" />
		<input type="button" class="tbl_query_button" value="提交"
			onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
			name="btSubmit" id="btSubmit" onclick="document.getElementById('submitFlag').value='S';save()" />
		<input type="button" class="tbl_query_button" value="取消"
			onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
			name="btCancel" id="btCancel" onclick="revokeToList()" />
	</div>
	<script type="text/javascript">
		var msgHight = document.getElementById("ctrlbutton").offsetHeight;
		document.getElementById("list1").style.height = screenHeight - 280 - msgHight;
	</script> -->
	</form>
<script language="javascript" type="text/javascript" charset="UTF-8">
window.onload = function(){
	var hightValue = screen.availHeight - 270;
	var hightValueStr = "height:"+ hightValue + "px";
	
	
	if (typeof(eval("document.all.blankbox"))!= "undefined"){
		document.getElementById("blankbox").setAttribute("style", hightValueStr);
	}
}
</script>
</body>
</html>