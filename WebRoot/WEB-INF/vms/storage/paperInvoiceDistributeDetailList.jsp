<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../../page/include.jsp"%>
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">

<script type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/editview.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
<meta http-equiv="Pragma" content="no-cache" />
<title>纸质发票分发明细</title>

<style type="text/css">
.detailInfoDIV {
	border: 1px solid green;
	background-color: khaki;
	top: 110px;
	left: 150px;
	width: 450px;
	height: 300px;
	position: absolute;
	z-index: 2;
	filter: alpha(opacity =     90);
	-moz-opacity: 0.9;
	display: none;
}
</style>
<script type="text/javascript">
	function addPaperInvoice(){
		var url = 'addPaperInvoice.action';
		OpenModalWindow(url,650,600,true);
	}
	function submitForm(actionUrl){
		var form = document.getElementById("main");
		form.action=actionUrl;
		form.submit();
	}
	
	function deleteTransBatch(actionUrl){
	
		var form = document.getElementById("main");
		form.action=actionUrl;
		form.submit();
	}
	
  function hideDetailInfoDIV(){
		document.getElementById("detailInfoDIV").style.display='none';
	}

	function showDetailInfoDIV(logID){
	 	var currRow =  window.event.srcElement.parentElement.parentElement;// 获取当前行

	 	document.getElementById("_td2").innerHTML=logID;

	 	for(var i=3;i<16;i++){
	 		document.getElementById("_td"+i).innerHTML=currRow.cells[i-1].title;
	 	}
	 	
	 	document.getElementById("detailInfoDIV").style.display='block';
	}
	
	function output() {
		//拷贝 
		var elTable = document.getElementById("lessGridList"); //这里的page1 是包含表格的Div层的ID
		var oRangeRef = document.body.createTextRange();
		oRangeRef.moveToElementText( elTable );
		oRangeRef.execCommand("Copy");
		
		
		//粘贴 
		try{
			var appExcel = new ActiveXObject( "Excel.Application" ); 
			appExcel.Visible = true; 
			appExcel.Workbooks.Add().Worksheets.Item(1).Paste(); 
		//appExcel = null; 
		}catch(e){
			alert("使用此功能必须在浏览器中设置:Internet选项->安全->将本站加入“受信任的站点”。"); 
		}
		
		
		var elTable1 = document.getElementById("lessGridList"); 
		var oRangeRef1 = document.body.createTextRange(); 
		oRangeRef1.moveToElementText( elTable1 ); 
		oRangeRef1.execCommand( "Copy" );
		
		
		//粘贴 
		try{ 
		appExcel.Visible = true; 
		appExcel.Worksheets.Item(2).Paste(); 
		appExcel1 = null; 
		}catch(e){ 
		alert("使用此功能必须在浏览器中设置:Internet选项->安全->将本站加入“受信任的站点”。"); 
		} 
	}
	$(function(){
		$("#cmdDistribute").click(function(){
			$checkedStore=$("input[name='paper_invoice_stock_id']:checked");
			if($checkedStore.size()==0){
				alert("请选择您要分发的库存发票");
			}
			$store_ids="";
			for($idx=0;$idx<$checkedStore.size();$idx++){
				if(($idx+1)!=$checkedStore.size()){
					$store_ids+=$($checkedStore[$idx]).val()+",";
				}else{
					$store_ids+=$($checkedStore[$idx]).val();
				}
			}
			OpenModalWindow("<c:out value='${webapp}'/>/initDistrubute.action?paper_invoice_stock_ids="+$store_ids,800,600,true);
		});
	});
</script>
</head>
<body>
<form id="main" action="<c:out value='${webapp}'/>/listDistrubute.action" method="post">
<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
	<tr>
		<td class="centercondition">
		<div id="tbl_current_status">
			<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> 
			<span class="current_status_menu">当前位置：</span> 
			<span class="current_status_submenu1">销项税管理</span>
			<span class="current_status_submenu">库存管理</span>
			<span class="current_status_submenu">发票跟踪</span>
		</div>
		<div class="widthauto1">
		<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td width="60">领取人机构</td> 
				<td width="130">
					<input type="hidden" id="receiveInstId" name="invoiceDistribute.receiveInstId" value='<s:property value="invoiceDistribute.receiveInstId"/>'> 
					<input type="text" class="tbl_query_text" id="receiveInstName" name="invoiceDistribute.receiveInstName" value='<s:property value="invoiceDistribute.receiveInstName"/>' onclick="setOrg(this, '#receiveInstId', '#receiveInstName');" readonly="readonly">
					<!-- 
					<s:if test="authInstList != null && authInstList.size > 0">
					<s:select name="invoiceDistribute.receiveInstId" list="authInstList" listKey='id' listValue='name' headerKey="" headerValue="所有"/>
					</s:if>
					<s:if test="authInstList == null || authInstList.size == 0">
					<select name="invoiceDistribute.receiveInstId" class="readOnlyText">
					<option value="">请分配机构权限</option>
					</select>
					</s:if> -->	
				</td>
				<td width="50">领取人</td> 
				<td width="130"><input id="receiveUserId" class="tbl_query_text" name="invoiceDistribute.receiveUserId" type="text" value="<s:property value='invoiceDistribute.receiveUserId' />" /></td>
				<td width="50">发票代码</td> 
				<td width="130"><input id="invoiceCode" class="tbl_query_text" name="invoiceDistribute.invoiceCode" type="text" value="<s:property value='invoiceDistribute.invoiceCode' />" onkeypress="checkkey(value);"  maxlength="10"/></td>
				<td width="50">分发时间</td>
				<td width="280">
					<input type="hidden" id="createTime" value="<s:property value='invoiceDistribute.createTime' />"/>
					<input name="invoiceDistribute.createTime" type="text"  value="<s:property value='invoiceDistribute.createTime' />" class="tbl_query_time1"	 
						onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:document.getElementById('createTime').value == '' ? new Date():document.getElementById('createTime').value})" />
				</td>
			</tr>
			<tr>
				<td width="60">分发人机构</td> 
				<td width="130">
					<input type="hidden" id="createInstId" name="invoiceDistribute.createInstId" value='<s:property value="invoiceDistribute.createInstId"/>'> 
					<input type="text" class="tbl_query_text" id="createInstName" name="invoiceDistribute.createInstName" value='<s:property value="invoiceDistribute.createInstName"/>' onclick="setOrg(this, '#createInstId', '#createInstName');" readonly="readonly">
					<!-- 
					<s:if test="authInstList != null && authInstList.size > 0">
					<s:select name="invoiceDistribute.createInstId" list="authInstList" listKey='id' listValue='name' headerKey="" headerValue="所有"/>
					</s:if>
					<s:if test="authInstList == null || authInstList.size == 0">
					<select name="invoiceDistribute.createInstId" class="readOnlyText">
					<option value="">请分配机构权限</option>
					</select>
					</s:if> -->
				</td>
				<td width="50">分发人</td> 
				<td width="130"><input id="createUserId" class="tbl_query_text" name="invoiceDistribute.createUserId" type="text" value="<s:property value='invoiceDistribute.createUserId' />" /></td>
				<td width="50">发票号码</td> 
				<td width="130"><input id="invoiceNo" class="tbl_query_text" name="invoiceDistribute.invoiceNo" type="text" value="<s:property value='invoiceDistribute.invoiceNo' />" onkeypress="checkkey(value);" maxlength="8"/></td>
				<td colspan="2">
				<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 
				onclick="submitForm('listDistrubute.action');" name="cmdSelect" value="查询" id="cmdSelect" />				
				</td>
			</tr>
		</table>
		</div>
		<div id="lessGridList1" class="mtop10" style="overflow: auto; width: 100%;">
		<table  class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0"  style="border-collapse: collapse;width: 100%;">
			<tr class="lessGrid head">
				<th style="text-align: center">序号</th>
				<th style="text-align: center">领取人机构</th>
				<th style="text-align: center">领取人</th>
				<th style="text-align: center">发票类型</th>
				<th style="text-align: center">发票代码</th>
				<th style="text-align: center">发票起始号码</th>
				<th style="text-align: center">发票终止号码</th>
				<th style="text-align: center">分发张数</th>
				<th style="text-align: center">剩余打印张数</th>
				<th style="text-align: center">未领用张数</th>
				<th style="text-align: center">分发时间</th>
				<th style="text-align: center">分发人机构</th>
				<th style="text-align: center">分发人</th>
				<th style="text-align: center">操作</th>
			</tr>
			<s:iterator value="paginationList.recordList" id="iList" status="stuts">
				
				<tr align="center"class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
					<td align="center"><s:property value='#stuts.count' /></td>		
					<td><s:property value='receiveInstName'/></td>
					<td><s:property value='receiveUserId'/></td>
					<td><s:if test="faPiaoType==0">增值税专用发票</s:if><s:if test="faPiaoType==1">增值税普通法票</s:if></td>
					<td><s:property value='invoiceCode'/></td>
					<td><s:property value='invoiceBeginNo'/></td>
					<td><s:property value='invoiceEndNo'/></td>
					<td><s:property value='distributeNum'/></td>
					<td><s:property value='#iList.balanceNum'/></td>
					<td><s:property value='#iList.unReceiveNum'/></td>
					<td><s:property value='createTime.substring(0,19)'/></td>
					<td><s:property value='createInstName'/></td>
					<td><s:property value='createUserName'/></td>
					<td>
					<s:if test="#iList.distributeNum != #iList.hasReceiveNum">
					<%--<s:if test="#iList.balanceNum == 0">
					
					--%><a href="javascript:;" class="receive_bt" rel="<s:property value='paperInvoiceDistributeId'/>">领用</a>&nbsp;&nbsp;
				
					<%--<s:elseif test="#iList.invoiceEndNo==#iList.currentbillNo">
					<a href="javascript:;" class="receive_bt" rel="<s:property value='paperInvoiceDistributeId'/>">领用</a>&nbsp;&nbsp;
					</s:elseif>
					--%></s:if>
					<%--<s:if test='#iList.hasReceiveNum == "0" '>
					<a href="javascript:;" class="return_bt" rel="<s:property value='paperInvoiceDistributeId'/>">领用</a>
					</s:if>
					--%><s:if test='#iList.hasReceiveNum != "0" '>
					<a href="javascript:;" class="return_bt" pro="<s:property value='paperInvoiceStockId'/>" rel="<s:property value='paperInvoiceDistributeId'/>">退还</a>
					</s:if>
					 <a href="javascript:;" class="history_bt" rel="<s:property value='paperInvoiceDistributeId'/>">明细</a>
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
	<%--
	<tr>
		<td align="left">		
		<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td>机构:&nbsp;&nbsp;&nbsp;</td> 
				<td><input id="instId" class="tbl_query_text" name="instId" type="text" value="<s:property value='instId' />" /></td>
				<td>领购时间:&nbsp;&nbsp;&nbsp;</td>
				<td><input id="receiveInvoiceTime" name="receiveInvoiceTime" type="text"  value="<s:property value='receiveInvoiceTime' />" class="tbl_query_time"	 onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" />&nbsp;&nbsp;&nbsp;</td>
				<td>领购人员:&nbsp;&nbsp;&nbsp;</td> 
				<td><input id="receiveUserId" class="tbl_query_text" name="receiveUserId" type="text" value="<s:property value='receiveUserId' />" /></td>
				<td>发票类型:&nbsp;&nbsp;&nbsp;</td> 
				<td><select id="invoiceType" name="invoiceType" ><option value="" <s:if test='invoiceType==""'>selected</s:if><s:else></s:else>>全部</option>
					<option value="1" <s:if test='invoiceType=="1"'>selected</s:if><s:else></s:else>>增值税专用发票</option>
					<option value="2" <s:if test='invoiceType=="2"'>selected</s:if><s:else></s:else>>增值税普通发票</option></select></td>
				<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td style="text-align:left">
				<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 
				onclick="submitForm('listPageInvoice.action');" name="cmdSelect" value="查询" id="cmdSelect" />				
				</td>
			</tr>
		</table>
		<table id="tbl_query" cellpadding="1" cellspacing="0">
			<tr align="left">
				<td style="text-align:left">
				<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" 
				onMouseOut="this.className='tbl_query_button'" onclick="addPaperInvoice();" name="cmdFilter" value="新增" id="cmdFilter" />				
				</td>
				<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td style="text-align:left">
				<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" 
				onMouseOut="this.className='tbl_query_button'" 
				name="cmdDistribute" value="分发" id="cmdDistribute" />				
				</td>
				<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td style="text-align:left">
				<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 
				onclick="beforeDelete('<c:out value="${webapp}"/>/deleteTransBatch.action','batchIdList',600,410,true,'delete')" name="cmdFilter" value="空白发票作废" id="cmdFilter" />				
				</td>
	         </tr>
		</table>		
		</td>
	</tr>
	 --%>
</table>
</form>
<c:import url="${webapp}/page/webctrl/instTree/tree_include2.jsp" charEncoding="UTF-8">
	<c:param name="treeType" value="radio"/>
	<c:param name="bankName_tree" value="receiveInstName"/>
	<c:param name="bankId_tree" value="receiveInstId"/>
	<c:param name="taskId_tree" value=""/>
	<c:param name="task_tree" value="task_tree"/>
	<c:param name="webapp" value="${webapp}"/>
</c:import>
</body>
<script type="text/javascript">
	$(function(){
		$(".receive_bt").click(function(){
			$paperInvoiceDistributeId=$(this).attr("rel");
			OpenModalWindow("<c:out value='${webapp}'/>/initReceiveDistribute.action?paper_invoice_distribute_id="+$paperInvoiceDistributeId,600,300,true);
		});
		$(".history_bt").click(function(){
			$paperInvoiceDistributeId=$(this).attr("rel");
			OpenModalWindow("<c:out value='${webapp}'/>/listPaperInvoiceRbHistory.action?paper_invoice_distribute_id="+$paperInvoiceDistributeId,900,500,false);
		});
		$(".return_bt").click(function(){
			$paperInvoiceDistributeId=$(this).attr("rel");
			$paperInvoiceStockId=$(this).attr("pro");
			OpenModalWindow("<c:out value='${webapp}'/>/initBackDistribute.action?paper_invoice_distribute_id="+$paperInvoiceDistributeId+"&paperInvoiceStockId="+$paperInvoiceStockId,600,300,true);
		});
	})
	
</script>
</html>















