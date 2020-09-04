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

<title>票据查看</title>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
	
<script type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
<script type="text/javascript">
	function submitForm(actionUrl){
		document.forms[0].action = actionUrl;
		var transIdNotIn = "";
		$(window.parent.document).find("#table_y tr").each(function(){
			id = $(this).attr("id");
			if(id!=""){
				transIdNotIn += "'"+id+"',";
			} 
		});
		document.forms[0].transIdNotIn.value=transIdNotIn;
		document.forms[0].submit();
	}
	function goY(t){
		var tr = $(t).parents('tr:first').attr("id");
		var len_y = $(window.parent.document).find("#table_y tr").length;
		var rowA_class = "lessGrid rowA";
		var rowB_class = "lessGrid rowB";
		var trHTML = "<tr id='"+tr+"' align='center' ";
		if(len_y%2 ==0){
			trHTML += "class='"+rowA_class+"'>";
		}else{
			trHTML += "class='"+rowA_class+"'>";
		}
		valArr=[];
		var xxp="";
//		alert($(window.parent.document).find("#"+tr+"a").val());
		if($(window.parent.document).find("#"+tr+"a").val()!=null){
			$(window.parent.document).find("#"+tr+"a").val(
			Number($(window.parent.document).find("#"+tr+"a").val())+Number($("#"+tr+"c").val())
			)
			$(window.parent.document).find("#"+tr+"b").val(
			Number($(window.parent.document).find("#"+tr+"b").val())+Number($("#"+tr+"c").val())
			)
			$(t).parents('tr:first').remove();
		}else{
		$("#"+tr).find("td").each(function(index,element){
			if(index==0){
				n = len_y-1;
				trHTML += "<td>"+n+"</td>";
			}
			else if(index==4){
				xxp=$.trim($(this).text())
				trHTML += "<td>"+$.trim($(this).text())+"</td>";
				}
			else if(index==10){
				trHTML += "<td>"+"<input type='hidden' id='"+tr+"b'value='"+$.trim($(this).text())+"'/>"+"<input id='"+tr+"a' value='"+$.trim($(this).text())+"' onblur='changebalence("+xxp+",this.value,"+tr+")'/>"+"</td>";
			}
			else if(index==13){
				trHTML += "<td><a href='javascript:void(0);' onClick='goN(this)'>撤销</a></td>";
			}
			else{
				trHTML += "<td>"+$.trim($(this).text())+"</td>";
			}
			//valArr.push($.trim($(this).text()));//.text()获取td的文本内容，$.trim()去空格
		});
		$(window.parent.document).find("#table_y").append(trHTML);
		$(t).parents('tr:first').remove();
		var len_n = $("#table_n tr").length;
		for (i = 0; i < len_n; i++) {
             $("#table_n tr").eq(i).children("td").eq(0).html(i);  //给每行的第一列重写赋值
        }
       }
	}
</script>
</head>
<body scroll="no" style="overflow:hidden; background:#FFF;">

<table class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
	<tr>
		<td align="left" colspan='14'>
		<form id="main" action="transInfoCheckNQuery.action" method="post">
		<input type="hidden" id="billId" name="billId" value="<s:property value='billId' />"/>
		<input type="hidden" id="transIdNotIn" name="transIdNotIn" />
		<table id="tbl_query" cellpadding="0" cellspacing="0" border="0" width="100%" >
			<tr>
				<td>交易业务编号:</td> 
				<td><input id="transBusId" class="tbl_query_text" name="transBusId" type="text" value="<s:property value='transBusId' />"/></td>
				<td>客户ID:</td> 
				<td><input id="customerId" class="tbl_query_text" name="customerId" type="text" value="<s:property value='customerId' />"/></td>
				<td>交易发生机构:</td> 
				<td>
					<s:if test="authInstList != null && authInstList.size > 0">
					<s:select name="bankCode" list="authInstList" listKey='id' listValue='name' headerKey="" headerValue="所有"/>
					</s:if>
					<s:if test="authInstList == null || authInstList.size == 0">
					<select name="bankCode" class="readOnlyText">
					<option value=""></option>
					</select>
					</s:if>
				</td>
				<td>
					<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 
					onclick="submitForm('transInfoCheckNQuery.action');" name="cmdSelect" value="查询" id="cmdSelect" />			
				</td>
			</tr>
		</table>
		</form>
		</td>
	</tr>
</table>
<div style="overflow: auto; height:232px;">
<table id="table_n"  class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0" >
	<tr >
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
		<th style="text-align: center">确认勾稽</th>
	</tr>
	<s:iterator value="checkNList" id="iList" status="stuts">
		
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
			<td id="<s:property value='transId' />d"><s:property value='balance'/><input type='hidden' id="<s:property value='transId' />c" value="<s:property value='balance'/>" /></td>
			<td>
				<s:iterator value="mapVatType" id="entry">  
					<s:if test='fapiaoType==#entry.key'><s:property value="value"/></s:if>
				</s:iterator>
			</td>
			<td><s:property value='mapDataStatus[#iList.dataStatus]'/></td>
			<td>
				<a href="javascript:void(0);" onClick='goY(this)'>勾稽</a>
			</td>
		</tr>
	</s:iterator> 
</table>
</div>
<div id="anpBoud" align="Right" style="width:100%;">
        <table width="100%" cellspacing="0" border="0">
            <tr>           
                <td align="right"><s:component template="pagediv"/></td>
            </tr>
        </table>
</div>
</body>
</html>
