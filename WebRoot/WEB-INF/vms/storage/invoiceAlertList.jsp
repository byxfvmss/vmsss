<!-- file: <%=request.getRequestURI() %> -->
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
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
<script type="text/javascript" src="<%=bopTheme%>/js/validator.js"></script>
<script src="<%=bopTheme%>/js/MessageBox/js/messageBox.js" type="text/javascript"></script>
<link href="<%=bopTheme%>/js/MessageBox/css/messageBox.css" rel="stylesheet" type="text/css" />
<meta http-equiv="Pragma" content="no-cache" />
<title>发票预警</title>

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
	function reset(url){
		//OpenModalWindow(url,650,350,true);
		location.href=url;
	}
	// 打开空白发票作废页面
	function invalidBlankPaperInvoice(){
		var url = 'invalidBlankPaperInvoice.action';
		OpenModalWindow(url,650,250,true);
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
		$("#cmdRet").click(function(){
			$inst_id=$("#inst_id").val();
			$invoice_type=$("#invoice_type").val();
			 //机构是否为空
		    if(fucCheckNull(document.getElementById("inst_id"),"请选择机构")==false){
		       return false;
		    }
		    //领取人员是否为空
		    if(fucCheckNull(document.getElementById("invoice_type"),"请选择发票类型")==false){
		       return false;
		    }
			var url = '<c:out value='${webapp}'/>/initInstAlert.action?inst_id='+$inst_id+'&invoice_type='+$invoice_type;
			OpenModalWindow(url,650,250,true);
		});
	});
	
</script>
</head>
<body>
<form id="main" action="<c:out value='${webapp}'/>/listInstAlert.action" method="post">
<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
	<tr>
		<td class="centercondition">
		<div id="tbl_current_status">
			<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> 
			<span class="current_status_menu">当前位置：</span> 
			<span class="current_status_submenu1">销项税管理</span>
			<span class="current_status_submenu">库存管理</span>
			<span class="current_status_submenu">发票预警</span>
		</div>
		<div class="widthauto1">
			<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td width="50">机构名称</td> 
					<td width="130">
						<input type="hidden" id="inst_id" name="inst_id" value='<s:property value="inst_id"/>'> 
						<input type="text" class="tbl_query_text" id="inst_Name" name="inst_Name" value='<s:property value="inst_Name"/>' onclick="setOrg(this);" readonly="readonly">
					</td>
					<td width="50">发票类型</td> 
					<td width="130">
						<select id="invoice_type" name="invoice_type" ><option value="" <s:if test='invoice_type==""'>selected</s:if><s:else></s:else>>全部</option>
						<option value="0" <s:if test='invoice_type=="0"'>selected</s:if><s:else></s:else>>增值税专用发票</option>
						<option value="1" <s:if test='invoice_type=="1"'>selected</s:if><s:else></s:else>>增值税普通发票</option>
						</select>
					</td>
					<td>
					<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" 
					onMouseOut="this.className='tbl_query_button'" onclick="submitForm('listInstAlert.action')" 
					name="cmdQueryBtn" value="查询" id="cmdQueryBtn" />
					<!--<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 
					name="cmdRet" value="重置预警" id="cmdRet" />
					--></td>
				</tr>
			</table>
		</div>
		<div id="lessGridList" style="overflow: auto; width: 100%;">
		<table  class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0"  style="border-collapse: collapse;width: 100%;">
			<tr class="lessGrid head">
				<th style="text-align: center">序号</th>
				<th style="text-align: center">机构名称</th>
				<th style="text-align: center">发票类型</th>
				<th style="text-align: center">预警值</th>
				<th style="text-align: center">库存</th>
			</tr>
			<s:iterator value="paginationList.recordList" id="iList" status="stuts">
				
				<tr align="center"  <s:if test='#iList.alertFlag =="1"'>style="background-color: yellow"</s:if>>
					<td align="center"><s:property value='#stuts.count' /></td>		
					<td><s:property value='#iList.instName'/></td>
					<td>
					    <s:if test='invoiceType=="0"'>增值税专用发票</s:if><s:elseif test='invoiceType=="1"'>增值税普通发票</s:elseif><s:else></s:else>
					</td>
					<td><s:property value="#iList.alertNum"/></td>
					<td><s:property value='#iList.unusedInvoiceNum'/></td>
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
<c:import url="${webapp}/page/webctrl/instTree/tree_include.jsp" charEncoding="UTF-8">
	<c:param name="treeType" value="radio"/>
	<c:param name="bankName_tree" value="inst_Name"/>
	<c:param name="bankId_tree" value="inst_id"/>
	<c:param name="taskId_tree" value=""/>
	<c:param name="task_tree" value="task_tree"/>
	<c:param name="webapp" value="${webapp}"/>
    </c:import>
</body>
<script type="text/javascript">
	
	$(function(){
	
		$(".edit_insurtax").click(function(){
// 			$checkedStore=$("input[name='paper_invoice_stock_id']:checked");
// 			if($checkedStore.size()==0){
// 				alert("请选择您要分发的库存发票");
// 				return;
// 			}
// 			$store_ids="";
// 			for($idx=0;$idx<$checkedStore.size();$idx++){
// 				if(($idx+1)!=$checkedStore.size()){
// 					$store_ids+=$($checkedStore[$idx]).val()+",";
// 				}else{
// 					$store_ids+=$($checkedStore[$idx]).val();
// 				}
// 			}
			bill_id=$(this).attr("rel");
			OpenModalWindow("<c:out value='${webapp}'/>/editParamInSurtax.action?bill_id="+bill_id,800,600,true);
		});
	});
</script>
</html>

