<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html> 
<head>
<base target="_self">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../page/include.jsp"%>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">

<title>预警编辑</title>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
	
<script type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
<script type="text/javascript">
	function submitForm(){
		document.forms[0].action = "<%=webapp%>/billInfoCheckSave.action";
		var transIdStr = "";
		var sumAmtB = 0;
		var transAmt="";
		var transAmtOrg="";
		var amtSum = $("#amtSum").html();
		$('#table_y tr').each(function(){
			id = $(this).attr("id");
			if(id!=""){
				transIdStr += id+",";
				transAmt +=$("#"+id+"a").val()+",";
				transAmtOrg +=$("#"+id+"b").val()+","
				sumAmtB += Number($("#"+id+"a").val());
				//alert(transAmtOrg);
				//parseInt($(this).children("td").eq(10).html());  	
			} 
		});
		if(sumAmtB>amtSum){
			alert("票据金额不能大于交易金额");
		}else{
			document.forms[0].transAmtOrg.value=transAmtOrg;
			document.forms[0].transIdStr.value=transIdStr;
			document.forms[0].transAmt.value=transAmt;
//			$.ajax({
//			cache:?true,
//			url: "<%=webapp%>/billInfoCheckSave.action",
//			type:?"POST",
//			data:$('#frm').serialize(),//?你的formid
//			success:function(data){
//			CloseWindow();
//			}
//		});
			document.forms[0].submit();
			document.forms[0].action ="billInfoCheckExe.action";
		}
	}
	function goN(t){
		var tr = $(t).parents('tr:first').attr("id");
		var len_n = $(window.frames["iframepage"].document).find("#table_n tr").length;
		var rowA_class = "lessGrid rowA";
		var rowB_class = "lessGrid rowB";
		var trHTML = "<tr id='"+tr+"' align='center' ";
		if(len_n%2 ==0){
			trHTML += "class='"+rowB_class+"'>";
		}else{
			trHTML += "class='"+rowA_class+"'>";
		}
		valArr=[];
		var temp="";
		
		if($(window.frames["iframepage"].document).find("#"+tr+"c").val()!=null){
			var x=Number($(window.frames["iframepage"].document).find("#"+tr+"c").val())+Number($("#"+tr+"b").val())
			$(window.frames["iframepage"].document).find("#"+tr+"c").val(x);
			$(window.frames["iframepage"].document).find("#"+tr+"d").text(x);
			$(t).parents('tr:first').remove();
		}else{
		$("#"+tr).find("td").each(function(index,element){
			if(index==0){
				n = len_n;
				trHTML += "<td>"+n+"</td>";
			}
			else if(index==4){
				trHTML += "<td>"+$.trim($(this).text())+"</td>";
			}
			else if(index==10){
				trHTML += "<td id='"+tr+"d''>"+$("#"+tr+"b").val()+"<input type='hidden' id='"+tr+"a' value='$('#"+tr+"b').val()'"+"</td>";
			}
			else if(index==13){
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
	}
	
	
function changebalence(id,value,idi){
		
		if(Number(value)>Number(id)){
		alert("修改金额不能大于未开票金额");
		$("#BtnSave").hide();
		return;
		}
		if(Number(value)==0){
		alert("金额不能为0");
		$("#BtnSave").hide();
		return;
		}
		if(value==null||value==""){
		alert("金额不能为空");
		$("#BtnSave").hide();
		return;
		}
		if(Number(value)<0){
		alert("金额不能为负数");
		$("#BtnSave").hide();
		return;
		}
		$("#"+idi+"a").val(value);
		$("#BtnSave").show();
}
	
	
</script>
</head>
<body style=" background:#FFF; padding-right:17px;overflow-x:hidden!important;">
<div class="showBoxDiv">
<form id="frm" method="post" action="<c:out value='${webapp}'/>/billInfoCheckSave.action">
<input type="hidden" id="transIdJson" name="transIdJson" value="<s:property value='#request.transIdJson' />"/>
<input type="hidden" id="transIdStr" name="transIdStr" />
<input type="hidden" id="transAmt" name="transAmt" />
<input type="hidden" id="transAmtOrg" name='transAmtOrg'/>
<input type="hidden" id="billId" name="billId" value="<s:property value='billId' />"/>
<div style=" width: 100%; " >
<div class="windowtitle" style="background:#004C7E; height:45px; line-height:45px; text-align:center; color:#FFF; font-size:14px; font-weight:bold;">票据查看--<s:property value="billId" /></div>
<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0" style="background:#FFF;">
	<tr>
		<td width="15%" style="text-align: right" class="listbar">申请开票日期:</td>
		<td width="35%"><s:property value="billInfo.applyDate" /></td>
		<td width="15%" style="text-align: right" class="listbar">开票日期:</td>
		<td><s:property value="billInfo.billDate" /></td>
	</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">发票类型:</td>
		<td width="35%">
			<s:iterator value="mapVatType" id="entry">
				<s:if test='billInfo.fapiaoType==#entry.key'><s:property value="value"/></s:if>
			</s:iterator>
		</td>
		<td width="15%" style="text-align: right" class="listbar">客户名称</td>
		<td width="35%"><s:property value="billInfo.customerName" /></td>
	</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">客户纳税人识别号:</td>
		<td width="35%"><s:property value="billInfo.customerTaxno" /></td>
		<td width="15%" style="text-align: right" class="listbar">客户地址电话:</td>
		<td><s:property value="billInfo.customerAddressandphone" /></td>
	</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">客户开户行及账号:</td>
		<td width="35%"><s:property value="billInfo.customerBankandaccount" /></td>
		<td width="15%" style="text-align: right" class="listbar">合计金额:</td>
		<td id="amtSum"><s:property value="billInfo.amtSum" /></td>
	</tr>
	<tr>
		<td width="15%" style="text-align: right" class="listbar">合计税额:</td>
		<td width="35%"><s:property value="billInfo.taxAmtSum" /></td>
		<td width="15%" style="text-align: right" class="listbar">价税合计:</td>
		<td><s:property value="billInfo.sumAmt" /></td>
	</tr>
</table>
<table  class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0"  style="border-collapse: collapse;width: 100%;">
	<tr >
		<th style="text-align: center">商品名称</th>
		<th style="text-align: center">规格型号</th>
		<th style="text-align: center">商品数量</th>
		<th style="text-align: center">商品单价</th>
		<th style="text-align: center">金额</th>
		<th style="text-align: center">税率</th>
		<th style="text-align: center">税额</th>
	</tr>
	<s:iterator value="itemList" id="iList" status="stuts">
		
		<tr align="center"class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
			<td><s:property value='goodsName'/></td>
			<td><s:property value='specandmodel'/></td>
			<td><s:property value='goodsNo'/></td>
			<td><s:property value='goodsPrice'/></td>
			<td><s:property value='amt'/></td>
			<td><s:property value='taxRate'/></td>
			<td><s:property value='taxAmt'/></td>
		</tr>
	</s:iterator> 
</table>
<div class="windowtitle"  style=" background:#004C7E; height:30px; line-height:30px; padding-left:10px; color:#FFF;">已勾稽的数据</div>
<div style="overflow: auto; height:232px;">
<table id="table_y" id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
	<tr>
		<th style="text-align: center">序号</th>
		<th style="text-align: center">交易时间</th>
		<th style="text-align: center">客户名称</th>
		<th style="text-align: center">交易类型</th>
		<th style="text-align: center">交易金额</th>
		<s:if test='configCustomerFlag.equals("KBC")'>
		<th style="text-align: center">数据来源</th>
		</s:if>
		<th style="text-align: center">税率</th>
		<th style="text-align: center">税额</th>
		<th style="text-align: center">收入</th>
		<th style="text-align: center">价税合计</th>
		<th style="text-align: center">未开票金额</th>
		<th style="text-align: center">发票类型</th>
		<th style="text-align: center">交易状态</th>
		<th style="text-align: center">撤销勾稽</th>
	</tr>
	<s:iterator value="checkYList" id="iList" status="stuts">
		
		<tr id="<s:property value='transId' />" align="center" class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
			<td align="center"><s:property value='#stuts.count' /></td>		
			<td><s:property value='transDate'/></td>
			<td><s:property value='customerName'/></td>
			<td><s:property value='transTypeName'/></td>
			<td><s:property value='amt'/></td>
			<s:if test='configCustomerFlag.equals("KBC")'>
		 	<td><s:property value='dataSources'/></td>
			</s:if>
			<td><s:property value='taxRate'/></td>
			<td><s:property value='taxAmt'/></td>
			<td><s:property value='income'/></td>
			<td>
				<s:if test='taxFlag=="Y"'>
					<s:property value='amt'/>
				</s:if>
				<s:if test='taxFlag=="N"'>
					<s:property value='amt+taxAmt'/>
				</s:if>
			</td>
			<td>
			<input type="hidden" id="<s:property value='transId' />b" value="<s:property value='balance'/>"/>
			<input id='<s:property value='transId' />a' value="<s:property value='balance'/>" onblur="changebalence(<s:property value='amt'/>,this.value,<s:property value='transId' />);"/></td>
			<td>
				<s:iterator value="mapVatType" id="entry">  
					<s:if test='fapiaoType==#entry.key'><s:property value="value"/></s:if>
				</s:iterator>
			</td>
			<td><s:property value='mapDataStatus[#iList.dataStatus]'/></td>
			<td><a href="javascript:void(0);" onClick='goN(this)'>撤销</a></td>
		</tr>
	</s:iterator>  
</table>
</div>
<div class="windowtitle"  style=" background:#004C7E; height:30px; line-height:30px; padding-left:10px; color:#FFF;">未勾稽的数据</div>
<iframe src="<c:out value='${webapp}'/>/transInfoCheckNQuery.action?billId=<s:property value='billId' />" id="iframepage" name="iframepage" frameborder="0" marginheight="0" style="overflow: auto; width: 100%; height:310px; background:#FFF;">
</iframe>
</div>


<div id="ctrlbutton" class="ctrlbutton" style="border:0px">
	<input type="button" class="tbl_query_button" onMo  useMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="submitForm();" name="BtnSave" value="提交" id="BtnSave"/>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="CloseWindow();" name="BtnReturn" value="取消" id="BtnReturn"/>
</div>
</form>
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
</body>

</html>
