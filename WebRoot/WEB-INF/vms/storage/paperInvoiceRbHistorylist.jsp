<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html> 
<head>
<base target="_self">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../../page/include.jsp"%>
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
<meta http-equiv="Pragma" content="no-cache" />
<title>领取退还明细</title>
</head>
<body scroll="no" style="overflow:hidden;">
<form id="main" action="<c:out value='${webapp}'/>/listPaperInvoiceRbHistory.action" method="post">
	<input type="hidden" name="paper_invoice_distribute_id" value="<s:property value="paperInvoiceDistributeId" />" />
<div class="windowtitle" style="background:#004C7E; height:45px; line-height:45px; text-align:center; color:#FFF; font-size:14px; font-weight:bold;">
领取退还明细
</div>
<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
	<tr>
		<td>
			<div id="lessGridList4" style="overflow: auto; width: 100%;">
				<table class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0" style="border-collapse: collapse; width: 100%;">
					<tr class="lessGrid head">
						<th style="text-align: center">部门</th>
						<th style="text-align: center">领/还时间</th>
						<th style="text-align: center">领票人</th>
						<th style="text-align: center">发票编码</th>
						<th style="text-align: center">发票起始号码</th>
						<th style="text-align: center">发票终止号码</th>
						<th style="text-align: center">领用/退还</th>
					</tr>
				<s:iterator value="paginationList.recordList" id="iList" status="stuts">
					<tr align="center"class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">	
						<td><s:property value='receiveInstId'/></td>
						<td><s:property value="createTime.substring(0,19)"/></td>
						<td><s:property value='receiveUserId'/></td>
						<td><s:property value='invoiceCode'/></td>
						<td><s:property value='invoiceBeginNo'/></td>
						<td><s:property value='invoiceEndNo'/></td>
						<td>
							<s:if test='operatorFlag=="0"'>领用</s:if>
							<s:elseif test='operatorFlag=="1"'>退还</s:elseif>
							<s:elseif test='operatorFlag=="2"'>已用完</s:elseif>
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
		</td>
	</tr>
</table>
  	
</form>
</body>
<script type="text/javascript">
	$(function(){
		$(".receive_bt").click(function(){
			$paperInvoiceDistributeId=$(this).attr("rel");
			OpenModalWindow("<c:out value='${webapp}'/>/initReceiveDistribute.action?paper_invoice_distribute_id="+$paperInvoiceDistributeId,600,400,false);
		});
		$(".history_bt").click(function(){
			$paperInvoiceDistributeId=$(this).attr("rel");
			OpenModalWindow("<c:out value='${webapp}'/>/invoiceRbHistoryList.action?paper_invoice_distribute_id="+$paperInvoiceDistributeId,600,400,false);
		});
	
	})
</script>
</html>















