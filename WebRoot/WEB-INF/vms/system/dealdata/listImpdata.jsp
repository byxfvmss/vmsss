<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@ include file="../../../../page/include.jsp"%>
	<link href="<%=bopTheme2%>/css/main.css" type="text/css"
		  rel="stylesheet">
	<script type="text/javascript"
			src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript"
			src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
	<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
	<script language="javascript" type="text/javascript"
			src="<%=bopTheme%>/js/main.js"></script>
	<meta http-equiv="Pragma" content="no-cache" />
	<title>数据导入</title>
	<script type="text/javascript">
		function submitForm(actionUrl) {
			var form = document.getElementById("main");
			form.action = actionUrl;
			form.submit();
			form.action = "listImpdata.action";
		}

		function deleteTransBatch(actionUrl) {

			var form = document.getElementById("main");
			form.action = actionUrl;
			form.submit();
		}
		// 校验数据
		function checkData(t) {
			if (checkChkBoxesSelected("batchIdList")) {
				var batchIds="";
				var items = $('[name = "batchIdList"]:checkbox:checked');
				for ( var i = 0; i < items.length; i++) {
					// 如果i+1等于选项长度则取值后添加空字符串，否则为逗号
					batchIds = (batchIds + items.get(i).value) + (((i + 1)== items.length) ? '':',');
				}
				$(t).attr("href","checkMetLifeData.action?batchId="+ batchIds);
//				var from = document.getElementById("main");
//				from.action="checkMetLifeData.action?batchId="+ batchIds;
//				from.submit();
//				from.action="listImpdata.action";
			} else {
				alert("请选择要校验的数据！");
			}
		}
		//检查多选框集是否至少有一个被选中
		function checkChkBoxesSelected(chkBoexName) {
			var flg = false;
			var chkBoexes = document.getElementsByName(chkBoexName);
			for (i = 0; i < chkBoexes.length; i++) {
				if (chkBoexes[i].checked) {
					return true;
				}
			}
			return false;
		}

		// [删除]按钮
		function revoke(t){
			if(checkChkBoxesSelected("batchIdList")){
				if(!confirm("确定是否删除？")){
					return false;
				}
				var batchID="";
				var items = $('[name = "batchIdList"]:checkbox:checked');
				for ( var i = 0; i < items.length; i++) {
					// 如果i+1等于选项长度则取值后添加空字符串，否则为逗号
					batchID = (batchID + items.get(i).value) + (((i + 1)== items.length) ? '':',');
				}
				$(t).attr("href","deleteTransBatchDataInfo.action?batchId="+ batchID);
			}else{
				alert("请选择要删除的数据！");
			}
		}

		//[提交]按钮
		function saveInfo(t){
			if(checkChkBoxesSelected("batchIdList")){
				if(!confirm("确定是否提交？")){
					return false;
				}
				var batchIds="";
				var batchStatus = "";
				var items = $('[name = "batchIdList"]:checkbox:checked');
				for ( var i = 0; i < items.length; i++) {
					// 如果i+1等于选项长度则取值后添加空字符串，否则为逗号
					var batchId = items.get(i).value;
					//判断是否教研,是否全部校验失败
					var passCount = $("[name=batchIdList][value="+batchId+"]").closest("td").find("[name=passCount]").val();
					var status = $("[name=batchIdList][value="+batchId+"]").closest("td").find("[name=status]").val();
					if(status==0){
						alert("未校验不可提交");
						return;
					}
					if(passCount==0){
						alert("校验未通过不可提交");
						return;
					}
					batchIds = batchIds + batchId + (((i + 1)== items.length) ? '':',');
					batchStatus=encodeURI($("#"+items.val()+"").text());
				}
				batchStatus=encodeURI(batchStatus);
				$(t).attr("href","upTransBatchInfo.action?batchId="+ batchIds);
			}else{
				alert("请选择要提交的数据！");
			}
		}

		// 审核通过
		function AuditPass(t) {
			if (checkChkBoxesSelected("batchIdList")) {
				var batchIds="";
				var items = $('[name = "batchIdList"]:checkbox:checked');
				for ( var i = 0; i < items.length; i++) {
					// 如果i+1等于选项长度则取值后添加空字符串，否则为逗号
					batchIds = (batchIds + items.get(i).value+"-"+$(items.get(i)).closest("tr").find("[name=unPassCount]").text().trim()) + (((i + 1)== items.length) ? '':',');
				}
				$(t).attr("href","AuditPassTransBatchDataInfo.action?batchId="+batchIds+"&status=<s:property value='status'/>");
			} else {
				alert("请选择要审核通过的数据！");
			}
		}

		//审核拒绝
		function auditDataNo(t){
			if (checkChkBoxesSelected("batchIdList")) {
				var batchIds="";
				var items = $('[name = "batchIdList"]:checkbox:checked');
				for ( var i = 0; i < items.length; i++) {
					// 如果i+1等于选项长度则取值后添加空字符串，否则为逗号
					batchIds = (batchIds + items.get(i).value) + (((i + 1)== items.length) ? '':',');
				}
				$(t).attr("href","AuditNoTransBatchDataInfo.action?batchId="+batchIds+"&status=<s:property value="status"/>");
			} else {
				alert("请选择要审核拒绝的数据！");
			}
		}

//		function changeCheckBox(){
//			alert(1);
//		}
	</script>
</head>
<body>
<form id="main" action="listImpdata.action" method="post" enctype="multipart/form-data">
	<input type="hidden" name="status" value="<s:property value='status'/>">
	<table id="tbl_main" cellpadding="0" cellspacing="0"
		   class="tablewh100">
		<tr>
			<td class="centercondition">
				<div id="tbl_current_status">
					<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> <span
						class="current_status_menu">当前位置：</span> <span
						class="current_status_submenu1">销项税管理</span> <span
						class="current_status_submenu">开票管理</span>
					<span class="current_status_submenu"><s:if test='status=="3"'>数据导入审核</s:if><s:else>数据导入</s:else></span>
				</div>
				<div class="widthauto1">
					<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
						<tr>
							<td width="50">导入时间</td>
							<td width="280"><input id="startTimes" name="dataInfo.startTime"
												   type="text" value="<s:property value='dataInfo.startTime' />"
												   class="tbl_query_time1"
												   onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endTimes\')||\'new Date()\'}'})" />
								&nbsp;&nbsp;&nbsp;-&nbsp;&nbsp;&nbsp; <input id="endTimes"
																			 name="dataInfo.endTime" type="text"
																			 value="<s:property value='dataInfo.endTime' />" class="tbl_query_time1"
																			 onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startTimes\')}',maxDate:new Date()})" />
							</td>
							<s:if test='status!="3"'>
								<td width="25">状态</td>
								<td width="130" width="50">
									<select id="status" name="dataInfo.mStatus">
										<option value="" <s:if test='dataInfo.status==""'>selected</s:if>
												<s:else></s:else>>全部</option>
										<option value="0" <s:if test='dataInfo.status=="0"'>selected</s:if>
												<s:else></s:else>>未校验</option>
										<option value="1" <s:if test='dataInfo.status=="1"'>selected</s:if>
												<s:else></s:else>>校验未通过</option>
										<option value="2" <s:if test='dataInfo.status=="2"'>selected</s:if>
												<s:else></s:else>>通过校验</option>
										<option value="6" <s:if test='dataInfo.status=="6"'>selected</s:if>
												<s:else></s:else>>审核退回</option>
										</option>
									</select>
								</td>
							</s:if>
							<td style="text-align:left"><input type="button"
															   class="tbl_query_button"
															   onMouseMove="this.className='tbl_query_button_on'"
															   onMouseOut="this.className='tbl_query_button'"
															   onclick="submitForm('listImpdata.action');" name="cmdFilter"
															   value="查询" id="cmdFilter" />
							</td>
						</tr>
					</table>
				</div>
				<table id="tbl_tools" cellpadding="1" cellspacing="0">
					<tr align="left">
						<s:if test='status=="3"'>
							<td align="left"><a href="#" onclick="return AuditPass(this);" >
								<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1006.png" />
								审核通过 </a> <a href="javascript:void(0);" onClick="auditDataNo(this)">
								<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1018.png" />
								审核拒绝 </a></td>
						</s:if>
						<s:else>
							<td><a href="#" onclick="return checkData(this);" >
								<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1006.png" />
								校验 </a> <!-- submitTransInfoByState.action?batchStatus=<s:property value="status"/> -->
								<a href="#" name="btSubmit" id="btSubmit" onclick="saveInfo(this)">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1006.png" />
									提交 </a> <a href="#" name="btDelete" id="btDelete"
											   onclick="revoke(this)"> <img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1019.png" />
									删除 </a></td>
							<td align="left" width="280" class="pleft15p"><s:file
									name="attachment" accept="application/msexcel" size="30"
									style="height:26px;"></s:file>
							</td>
							<td align="left"><a href="#"
												onclick="submitForm('upLoadTransDataList.action');" > <img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1007.png" />
								上传 </a>
							</td>
						</s:else>
					</tr>
				</table>
				<div id="lessGridList1" style="overflow: auto; width: 100%;">
					<table class="lessGrid" cellspacing="0" rules="all" border="0"
						   cellpadding="0" style="border-collapse: collapse;width: 100%;">
						<tr class="lessGrid head">
							<th width="3%" style="text-align:center">
								<input  id="CheckAll" style="width:13px;height:13px;"
									   type="checkbox" onClick="checkAll(this,'batchIdList')" />
							</th>
							<th style="text-align: center">序号</th>
							<th style="text-align: center">导入日期</th>
							<th style="text-align: center">上传用户</th>
							<th style="text-align: center">上传总数</th>
							<th style="text-align: center">通过总数</th>
							<th style="text-align: center">未通过总数</th>
							<th style="text-align: center">状态</th>
							<th style="text-align: center">认定结果</th>
						</tr>
						<s:iterator value="paginationList.recordList" id="iList"
									status="stuts">
							<tr align="center" class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
								<td><input type="checkbox" style="width:13px;height:13px;"
										   name="batchIdList" value="<s:property value="batchId"/>" />
									<s:hidden name="status"/>
									<s:hidden name="passCount"/>
								</td>
								<td align="center"><s:property value='#stuts.count' />
								</td>
								<td><s:property value='impTime' />
								</td>
								<td><s:property value='impUser' />
								</td>
								<td name="count"><s:property value='count' />
								</td>
								<td name="passCount"><s:property value='passCount' />
								</td>
								<td name="unPassCount">
									<s:if test="status==0">0</s:if>
									<s:else><s:property value='unPassCount' /></s:else>
								</td>
								<td><s:property value="statusStr" /></td>
								<td align="center">
									<s:if test='status=="0"||status=="6"'>
										<s:if test='status=="6"'>
											<s:if test='upperStatus=="1"'>
												<a href="findTransDataInfoListByBatchId.action?batchId=<s:property value="batchId"/>">
													<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1008.png"
														 title="校验结果 " style="border-width: 0px;" /> </a>
											</s:if>
											<s:if test='upperStatus=="2"'>
												<a href="findTransDataInfoListByBatchId.action?batchId=<s:property value="batchId"/>">
													<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1009.png"
														 title="校验结果" style="border-width: 0px;" /> </a>
											</s:if>
										</s:if>
										<s:else>
											<a href="findTransDataInfoListByBatchId.action?batchId=<s:property value="batchId"/>">
												<img src="<c:out value="${bopTheme}"/>/themes/images/icons/folder-open.png"
													 title="导入结果" style="border-width: 0px;" /> </a>
										</s:else>
									</s:if>
									<s:if test='status=="1"'>
										<a href="findTransDataInfoListByBatchId.action?batchId=<s:property value="batchId"/>">
											<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1008.png"
												 title="校验结果 " style="border-width: 0px;" /> </a>
									</s:if>
									<s:if test='status=="2"'>
										<a href="findTransDataInfoListByBatchId.action?batchId=<s:property value="batchId"/>">
											<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1009.png"
												 title="校验结果" style="border-width: 0px;" /> </a>
									</s:if>
									<s:if test='status=="3"'>
										<a href="findTransDataInfoListByBatchId.action?status=<s:property value="status"/>&batchId=<s:property value="batchId"/>">
											<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1009.png"
												 title="校验结果" style="border-width: 0px;" /> </a>
									</s:if>
								</td>
							</tr>
						</s:iterator>
					</table>
				</div>
				<div id="anpBoud" align="Right" style="width:100%;">
					<table width="100%" cellspacing="0" border="0">
						<input type="hidden" name="paginationList.showCount"
							   value="false">
						<td align="right"><s:component template="pagediv" /></td>
						</tr>
					</table>
				</div>
			</td>
		</tr>
	</table>
</form>
</body>
<script type="text/javascript">
	var msgHight = document.getElementById("anpBoud").offsetHeight;
	var tophight = document.getElementById("tbl_query").offsetHeight;
</script>
</html>
