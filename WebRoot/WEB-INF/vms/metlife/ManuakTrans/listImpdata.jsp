<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../../../page/include.jsp"%>
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/ylb.js"></script>
<meta http-equiv="Pragma" content="no-cache" />
<title>数据导入</title>
<script type="text/javascript">
	$(function(){
		var detailList = $("[name=detailList]");
		for(var i=0;i<detailList.size();i++){
			var dStatus = $(detailList.get(i)).find("[name=dStatus]").val();
			dStatus = dStatus.split(",");
			var tr = $(detailList.get(i));
			for(var j=0;j<dStatus.length;j++){
				var s = j;
				if(dStatus[j] == 1){
					$($(tr).find("td")[(s+2)]).css("background", "#FFFF37");
				}
			}
		}

	});

	function submitForm(actionUrl) {
		var form = document.getElementById("main");
		form.action = actionUrl;
		form.submit();
		form.action = "listImpdata.action";
	}
</script>
</head>
<body>
	<form id="main" action="findTransDataInfoListByBatchId.action" method="post" enctype="multipart/form-data">
		<input type="hidden" name="batchId" value="<s:property value='batchId'/>">
		<input type="hidden" name="status" value="<s:property value='status'/>">
		<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
			<tr>
				<td class="centercondition">
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" />
						<span class="current_status_menu">当前位置：</span>
						<span class="current_status_submenu1">销项税管理</span>
						<span class="current_status_submenu">开票管理</span>
						<span class="current_status_submenu">
							<s:if test='status=="3"'>数据导入审核明细</s:if>
							<s:else>数据导入明细</s:else>
						</span>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td width="50">导入时间</td>
								<td width="280">
									<input id="startTime" name="dataInfo.startTime" type="text" value="<s:property value='startTime' />" class="tbl_query_time1" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endTime\')||\'new Date()\'}'})" />
									&nbsp;&nbsp;&nbsp;-&nbsp;&nbsp;&nbsp;
									<input id="endTime" name="dataInfo.endTime" type="text" value="<s:property value='endTime' />" class="tbl_query_time1" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startTime\')}',maxDate:new Date()})" />
								</td>
								<td width="25">机构</td>
								<td width="130">
									<input type="hidden" id="t10" name="dataInfo.t10" value='<s:property value="instId"/>'>
									<input type="text" class="tbl_query_text" id="inst_Name" name="instName" value='<s:property value="instName"/>' onclick="setOrg(this);" readonly="readonly">
								</td>
								<td width="25">状态</td>
								<td width="130" width="50">
									<select id="mStatus" name="dataInfo.mStatus">
										<option value="" <s:if test='status==""'>selected</s:if> <s:else></s:else>>全部</option>
										<option value="0" <s:if test='status=="0"'>selected</s:if> <s:else></s:else>>未校验</option>
										<option value="1" <s:if test='status=="1"'>selected</s:if> <s:else></s:else>>未通过校验</option>
										<option value="2" <s:if test='status=="2"'>selected</s:if> <s:else></s:else>>通过校验</option>
										<option value="2" <s:if test='status=="2"'>selected</s:if> <s:else></s:else>>通过审核</option>
									</select>
								</td>
								<td style="text-align:left">
									<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="submitForm();"
										name="cmdFilter" value="查询" id="cmdFilter"
									/>
								</td>
							</tr>
						</table>
					</div>
					<table id="tbl_tools" cellpadding="1" cellspacing="0">
						<tr align="left">
							<td width="92%" align="left">
								<a href="#" onclick="submitForm('impdataMetLifeToExcel.action');"> <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png"/>
								导出 </a>
							</td>
							<td>
								<input type="button" class="tbl_query_button" value="返回"
									   onMouseMove="this.className='tbl_query_button_on'"
									   onMouseOut="this.className='tbl_query_button'" name="btCancel"
									   id="btCancel" onclick="javascript:goToPage('listImpdata.action<s:if test='status=="3"'>?status=<s:property value="status"/></s:if>')"/>
							</td>
						</tr>
					</table>
					<div id="lessGridList1" style="overflow: auto; width: 100%;">
						<table class="lessGrid" cellspacing="0" rules="all" border="0" cellpadding="0" style="border-collapse: collapse;width: 100%;">
							<tr class="lessGrid head">
								<th width="3%" style="text-align:center">
									<input id="CheckAll" style="width:13px;height:13px;" type="checkbox" onClick="checkAll(this,'batchIdList')" />
								</th>
								<th style="text-align: center">序号</th>
								<th style="text-align: center">客户号</th>
								<th style="text-align: center">产品代码</th>
								<th style="text-align: center">交易时间</th>
								<th style="text-align: center">交易金额</th>
								<th style="text-align: center">税率</th>
								<th style="text-align: center">收入会计科目</th>
								<th style="text-align: center">T1</th>
								<th style="text-align: center">T2</th>
								<th style="text-align: center">T3</th>
								<th style="text-align: center">T4</th>
								<th style="text-align: center">T5</th>
								<th style="text-align: center">T6</th>
								<th style="text-align: center">T7</th>
								<th style="text-align: center">T8</th>
								<th style="text-align: center">T9</th>
								<th style="text-align: center">T10</th>
								<th style="text-align: center">状态</th>
							</tr>
							<s:iterator value="paginationList.recordList" id="iList" status="stuts">
								<tr name="detailList" align="center" class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
									<s:hidden name="dStatus"/>
									<td class="yellow" name="batchId">
										<input type="checkbox" style="width:13px;height:13px;" name="batchIdList" value="<s:property value="dataId"/>" <s:if test='status=="2" || status=="3"'>disabled="true"</s:if> />
									</td>
									<td align="center" class="yellow" name="count">
										<s:property value='#stuts.count' />
									</td>
									<td class="yellow" name="cowNnum">
										<s:property value='cowNnum' />
									</td>
									<td class="yellow" name="proDuctCode">
										<s:property value='proDuctCode' />
									</td>
									<td class="yellow" name="trdt">
										<s:property value='trdt' />
									</td>
									<td class="yellow" name="accTamt">
										<fmt:formatNumber value="${accTamt}" pattern="#,##0.00"/>
									</td>
									<td class="yellow" name="taxRate">
										<fmt:formatNumber value="${taxRate}" pattern="#,##0.00"/>
									</td>
									<td class="yellow" name="alTref">
										<s:property value='alTref' />
									</td>
									<td class="yellow" name="t1">
										<s:property value='t1' />
									</td>
									<td class="yellow" name="t2">
										<s:property value='t2' />
									</td>
									<td class="yellow" name="t3">
										<s:property value='t3' />
									</td>
									<td class="yellow" name="t4">
										<s:property value='t4' />
									</td>
									<td class="yellow" name="t5">
										<s:property value='t5' />
									</td>
									<td class="yellow" name="t6">
										<s:property value='t6' />
									</td>
									<td class="yellow" name="t7">
										<s:property value='t7' />
									</td>
									<td class="yellow" name="t8">
										<s:property value='t8' />
									</td>
									<td class="yellow" name="t9">
										<s:property value='t9' />
									</td>
									<td class="yellow" name="t10">
										<s:property value='t10' />
									</td>
									<td>
										<s:if test='mStatus=="0"' >未校验</s:if>
										<s:elseif test='mStatus=="2"' >通过校验</s:elseif>
										<s:elseif test='dStatus!="1"' >
											<a href="#" onclick="getMessageBox('<s:property value="message"/>')">未通过校验</a>
										</s:elseif>
									</td>
								</tr>
							</s:iterator>
						</table>
					</div>
					<div id="anpBoud" align="Right" style="width:100%;">
						<table width="100%" cellspacing="0" border="0">
							<tr>
								<td align="right">
									<s:component template="pagediv" />
								</td>
							</tr>
						</table>
					</div>
				</td>
			</tr>
		</table>

	</form>
	<c:import url="${webapp}/page/webctrl/instTree/tree_include.jsp" charEncoding="UTF-8">
		<c:param name="treeType" value="radio" />
		<c:param name="bankName_tree" value="inst_Name" />
		<c:param name="bankId_tree" value="inst_id" />
		<c:param name="taskId_tree" value="" />
		<c:param name="task_tree" value="task_tree" />
		<c:param name="webapp" value="${webapp}" />
	</c:import>
</body>
<script type="text/javascript" >
	function getMessageBox(msg){
		if(msg != null && msg != ""){
			alert(msg);
		}
	}
</script>
</html>