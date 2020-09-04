<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.util.PaginationList"
	import="com.cjit.vms.input.model.InputInvoiceInfo"
	import="com.cjit.vms.trans.model.BillItemInfo"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../../page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<base target="_self">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">

<title>票据查看</title>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
	
<script type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
	<script type="text/javascript">
		// [查询]按钮
		function submitForm(){
		document.forms[0].action = "<%=webapp%>/inputBillInfoCheckSave.action";
		var dealNodStr = "";
		$('#table_y tr').each(function(){
			id = $(this).attr("id");
			if(id!=""){
				dealNodStr += id+",";
				//parseInt($(this).children("td").eq(10).html());  	
			} 
		});
	
		document.forms[0].dealNodStr.value=dealNodStr;
		$.ajax({
			cache: true,
			url: "<%=webapp%>/inputBillInfoCheckSave.action",
			 type: "POST",
			data:$('#frm').serialize(),// 你的formid
			success:function(data){
			CloseWindow();
			}
		});
		//document.forms[0].submit();
	}
		

function goN(t){
		var tr = $(t).parents('tr:first').attr("id");
		var len_n = $(window.frames["iframepage"].document).find("#table_n tr").length;
		var rowA_class = "lessGrid rowA";
		var rowB_class = "lessGrid rowB";
		var trHTML = "<tr id='"+tr+"' align='center' ";
		if(len_n%2==0){
			trHTML += "class='"+rowB_class+"'>";
		}else{
			trHTML += "class='"+rowA_class+"'>";
		}
		valArr=[];
		var temp="";
		$("#"+tr).find("td").each(function(index,element){
			if(index==0){
				n = len_n;
				trHTML += "<td>"+n+"</td>";
			}
			else if(index==6){
				trHTML += "<td><a href='javascript:void(0);' onClick='goY(this)'>勾稽</a></td>";
			}else{
				trHTML += "<td>"+$.trim($(this).text())+"</td>";
			}
			//valArr.push($.trim($(this).text()));//.text()获取td的文本内容，$.trim()去空格
		});
		$(window.frames["iframepage"].document).find("#table_n").append(trHTML);
		$(t).parents('tr:first').remove();
		var len_y = $("#table_y tr").length;
		for (i = 0; i < len_y; i++) {
             $("#table_y tr").eq(i).children("td").eq(0).html(i-1);  //给每行的第一列重写赋值
        }
	}
	</script>
	<style type="text/css">
		
	</style>
</head>
<body style=" background:#FFF; padding-right:17px;overflow-x:hidden!important;">
<div class="showBoxDiv">
<form id="frm" method="post" action="<c:out value='${webapp}'/>/inputBillInfoCheckSave.action"  >
<input type="hidden" id="odldealNoJson" name="odldealNoJson" value="<s:property value='#request.dealNoJson' />"/>
<input type="hidden" id="dealNodStr" name="dealNodStr" />
<input type="hidden" id="transAmt" name="transAmt" />
<input type="hidden" id="transAmtOrg" name='transAmtOrg'/>
<input type="hidden" id="billId" name="billId" value="<%=request.getParameter("billId")%>"/>
<input type="hidden" id="vendId" name="vendId" value="<%=request.getParameter("vendorTaxno")%>"/>
<div style=" width: 100%; " >
<div class="windowtitle" style="background:#004C7E; height:45px; line-height:45px; text-align:center; color:#FFF; font-size:14px; font-weight:bold;">票据查看--<%=request.getParameter("billId")%></div>
			<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0" style="background:#FFF;">
				<tr>
					<input type="hidden" name="billId" value="<s:property value="inputInvoiceInfo.billId"/>"/>
					<td width="15%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">发票代码:&nbsp;&nbsp;&nbsp;</td>
					<td style="background-color:#F0F0F0;color:#727375;">
						<s:property value='inputInvoiceInfo.billCode'/>
					</td>
					<td width="15%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">发票号码:&nbsp;&nbsp;&nbsp;</td>
					<td style="background-color:#F0F0F0;color:#727375;"><s:property value='inputInvoiceInfo.billNo'/>
						<s:property value='inputInvoiceInfo.billNo'/>
					</td>
					<td width="15%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">开票日期:&nbsp;&nbsp;&nbsp;</td>
					<td style="background-color:#F0F0F0;color:#727375;"><s:property value='inputInvoiceInfo.billNo'/>
						
					</td>
				</tr>
				<tr>
					<td width="15%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">供应商中文名称:&nbsp;&nbsp;&nbsp;</td>
					<td style="background-color:#F0F0F0;color:#727375;">
						<s:property value='inputInvoiceInfo.vendorName'/>
					</td>
					<td width="15%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">供应商纳税人识别号:&nbsp;&nbsp;&nbsp;</td>
					<td style="background-color:#F0F0F0;color:#727375;"><s:property value='inputInvoiceInfo.billNo'/>
						<s:property value='inputInvoiceInfo.vendorTaxno'/>
					</td>
					<td width="15%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">供应商地址 电话:&nbsp;&nbsp;&nbsp;</td>
					<td style="background-color:#F0F0F0;color:#727375;"><s:property value='inputInvoiceInfo.billNo'/>
						<s:property value='inputInvoiceInfo.vendorAddressandphone'/>
					</td>
				</tr>
				<tr>
					<td width="15%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">供应商银行账号:&nbsp;&nbsp;&nbsp;</td>
					<td style="background-color:#F0F0F0;color:#727375;">
						<s:property value='inputInvoiceInfo.vendorBankandaccount'/>
					</td>
					<td width="15%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">合计金额:&nbsp;&nbsp;&nbsp;</td>
					<td style="background-color:#F0F0F0;color:#727375;"><s:property value='inputInvoiceInfo.billNo'/>
						<s:property value='inputInvoiceInfo.amtSum'/>
					</td>
					<td width="15%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">合计税额:&nbsp;&nbsp;&nbsp;</td>
					<td style="background-color:#F0F0F0;color:#727375;"><s:property value='inputInvoiceInfo.billNo'/>
						<s:property value='inputInvoiceInfo.taxAmtSum'/>
					</td>
				</tr>
				<tr>
					<td width="15%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">价税合计金额:&nbsp;&nbsp;&nbsp;</td>
					<td colspan="5" style="background-color:#F0F0F0;color:#727375;">
						<s:property value='inputInvoiceInfo.sumAmt'/>
					</td>
				</tr>
			</table>
			
			
			

	
	<table  class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0"  style="border-collapse: collapse;width: 100%;">
	<tr class="lessGrid head">
		<th>商品名称</th>
		<th>规格型号</th>
		<th>商品数量</th>
		<th>商品单价</th>
		<th>金额</th>
		<th>税率</th>
		<th>税额</th>
	</tr>
	<s:iterator value="billItemList" id="iList" status="stuts">
		<tr align="center"class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
			<td align="center"><s:property value='goodsName'/></td>
			<td align="center"><s:property value='specandmodel'/></td>
			<td align="center"><s:property value='goodsNo'/></td>
			<td align="center"><s:property value='goodsPrice'/></td>
			<td align="center"><s:property value='amt'/></td>
			<td align="center"><s:property value='taxRate'/></td>
			<td align="center"><s:property value='taxAmt'/></td>
		</tr>
	</s:iterator> 
	</table>



<div class="windowtitle"  style=" background:#004C7E; height:30px; line-height:30px; padding-left:10px; color:#FFF;">已勾稽的数据</div>
<div style="overflow: auto; height:232px;">
<table id="table_y" id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
	<tr >
			<th style="text-align: center">序号</th>
			<th style="text-align: center">交易编号</th>
			<th style="text-align: center">金额</th>
			<th style="text-align: center">税额</th>
			<th style="text-align: center">供应商纳税人识别号</th>
			<th style="text-align: center">交易发生机构</th>
			<th style="text-align: center">撤销勾稽</th>
		</tr>
		<s:iterator value="inputTransList" id="iList" status="stuts">
		<tr id="<s:property value='dealNo' />" align="center" class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
			<td align="center"><s:property value='#stuts.count' /></td>		
			<td align="center"><s:property value='dealNo'/></td>
			<td align="center"><s:property value='amtCny'/></td>
			<td align="center"><s:property value='taxAmtCny'/></td>
			<td align="center"><s:property value='vendorId '/></td>
			<td align="center"><s:property value='bankCode'/></td>
			<td align="center"><a href="javascript:void(0);" onClick='goN(this)'>撤销</a></td>
		</tr>
	</s:iterator>  
</table>
</div>


<div class="windowtitle"  style=" background:#004C7E; height:30px; line-height:30px; padding-left:10px; color:#FFF;">未勾稽的数据</div>
<iframe src="listInputnoTransByVen.action?vendorId=<s:property value='inputInvoiceInfo.vendorTaxno'/>&billId=<s:property value='inputInvoiceInfo.billId'/>" id="iframepage" name="iframepage" frameborder="0" marginheight="0" style="overflow: auto; width: 100%; height:310px; background:#FFF;">
</iframe>
</div>
	<div id="ctrlbutton" class="ctrlbutton" style="border:0px">
	<input type="button" class="tbl_query_button" onMo  useMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="submitForm();" name="BtnSave" value="提交" id="BtnSave"/>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="CloseWindow();" name="BtnReturn" value="取消" id="BtnReturn"/>
	</div>

</div>
	<script language="javascript" type="text/javascript" charset="UTF-8">
window.onload = function(){
	var hightValue = screen.availHeight - 550;
	var hightValueStr = "height:"+ hightValue + "px";
	
	if (typeof(eval("document.all.datalist1"))!= "undefined"){
		document.getElementById("datalist1").setAttribute("style", hightValueStr);
	}
}
</script>
	</form>
</body>
</html>