<%@ taglib prefix="S" uri="/struts-tags"%>
<!--file: <%=request.getRequestURI()%> -->
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
	<script type="text/javascript" src="<%=webapp%>/page/js/vms.js"></script>
	<script language="javascript" type="text/javascript"
			src="<%=bopTheme%>/js/main.js"></script>
	<meta http-equiv="Pragma" content="no-cache" />
	<title>历史数据管理</title>

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
			filter: alpha(opacity = 90);
			-moz-opacity: 0.9;
			display: none;
		}
	</style>

</head>
<body>
<form id="Form1" name="Form1" action="historyTransInfoList.action"
	  method="post">
	<input type="hidden" id="busiNessIds" name="busiNessIds" />
	<table id="tbl_main" cellpadding="0" cellspacing="0"
		   class="tablewh100">
		<tr>
			<td class="centercondition">
				<div id="tbl_current_status">
					<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> <span
						class="current_status_menu">当前位置：</span> <span
						class="current_status_submenu1">历史数据管理</span> <span
						class="current_status_submenu">历史销项</span> <span
						class="current_status_submenu">历史销项交易</span>
				</div>
				<div class="widthauto1">
					<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
						<tr>
							<td style="text-align: right;width: 6%;">投保单号</td>
							<td style="text-align: left;width:14%;"><input
									class="tbl_query_text" type="text" name="historyInfo.ttmPrcno"
									value="<s:property value='historyInfo.ttmPrcno'/>" maxlength="20" /></td>
							<td style="text-align: right;width: 6%;">保单号</td>
							<td style="text-align: left;width:14%;"><input
									class="tbl_query_text" type="text" name="historyInfo.cherNum"
									value="<s:property value='historyInfo.cherNum'/>" maxlength="20"/></td>
							<td style="text-align: right;width: 6%;">交易日期</td>
							<td style="text-align: left;width:20%;"><input
									class="tbl_query_time" id="trdtBeginStr" type="text"
									name="historyInfo.trdtBeginStr"
									value="<s:property value='historyInfo.trdtBeginStr'/>"
									onfocus="WdatePicker({realFullFmt:'%Date %Time',maxDate:'#F{$dp.$D(\'trdtEndStr\')}'})"
									size='11' /> -- <input class="tbl_query_time" id="trdtEndStr"
														   type="text" name="historyInfo.trdtEndStr"
														   value="<s:property value='historyInfo.trdtEndStr'/>"
														   onfocus="WdatePicker({realFullFmt:'%Date %Time',minDate:'#F{$dp.$D(\'trdtBeginStr\')}'})"
														   size='11' /></td>
						</tr>
						<tr>
							<td style="text-align: right;">渠道</td>
							<td style="text-align: left;"><s:select
									name="historyInfo.zntCode07" list="chanNelList" listKey="key"
									listValue='value' cssClass="tbl_query_text" headerKey=""
									headerValue="请选择" /></td>
							<td style="text-align: right;">客户名称</td>
							<td style="text-align: left;"><input type="text"
																 class="tbl_query_text" name="historyInfo.cownName"
																 value="<s:property value='historyInfo.cownName'/>"
																 maxlength="100" /></td>
							<td style="text-align: right;">承保日期</td>
							<td style="text-align: left;"><input class="tbl_query_time"
																 id="hisSdteBeginStr" type="text"
																 name="historyInfo.hisSdteBeginStr"
																 value="<s:property value='historyInfo.hisSdteBeginStr'/>"
																 onfocus="WdatePicker({realFullFmt:'%Date %Time',maxDate:'#F{$dp.$D(\'hisSdteEndStr\')}'})"
																 size='11' /> -- <input class="tbl_query_time"
																						id="hisSdteEndStr" type="text" name="historyInfo.hisSdteEndStr"
																						value="<s:property value='historyInfo.hisSdteEndStr'/>"
																						onfocus="WdatePicker({realFullFmt:'%Date %Time',minDate:'#F{$dp.$D(\'hisSdteBeginStr\')}'})"
																						size='11' /></td>
						</tr>
						<tr>
							<td style="text-align: right;">费用类型</td>
							<td style="text-align: left;">
								<s:select name="historyInfo.feeTyp" list="feeTypList" listKey="key"
									listValue='value' cssClass="tbl_query_text" headerKey=""
									headerValue="请选择" />
							</td>
							<td style="text-align: right;">数据来源</td>
							<td style="text-align: left;">
								<s:select name="historyInfo.dsouRce" list="dsouRceList" listKey="key"
										  listValue='value' cssClass="tbl_query_text" headerKey=""
										  headerValue="请选择" />
							</td>
							<td style="text-align: right;width: 6%;">交费频率</td>
							<td style="text-align: left;"><s:select
									name="historyInfo.billFreq" list="billFreqlList" listKey="key"
									listValue='value' cssClass="tbl_query_text" headerKey=""
									headerValue="请选择" /></td>
						</tr>
					</table>
				</div>
				<table id="tbl_tools" width="100%" border="0">
					<tr align="left">
						<td style="text-align:left"><a href="#" name="cmdDistribute"
													   id="cmdDistribute" onclick="findHistoryTransInfoList()"> <img
								src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1022.png" />查询
						</a> <a href="#" onclick="transFerTransInfo()" name="cmdExcel"
								id="cmdExcel"><img
								src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />移送</a>
						</td>
					</tr>
				</table>
				<div id="lessGridList4" style="overflow: auto;">
					<div style="width: 0px;height: auto;" id="rDiv">
						<table class="lessGrid" cellspacing="0" rules="all" border="0"
							   cellpadding="0" style="border-collapse: collapse;width: 100%;">
							<tr class="lessGrid head">
								<th width="3%" style="text-align:center"><input
										id="CheckAll" style="width:13px;height:13px;" type="checkbox"
										onClick="checkAll(this,'batchIdList')" /></th>
								<th style="text-align: center">序号</th>
								<th style="text-align: center">投保单号</th>
								<th style="text-align: center">保单号</th>
								<th style="text-align: center">旧保单号</th>
								<th style="text-align: center">交易日期</th>
								<th style="text-align: center">险种名称</th>
								<th style="text-align: center">客户名称</th>
								<th style="text-align: center">交易金额</th>
								<th style="text-align: center">渠道</th>
								<th style="text-align: center">费用类型</th>
								<th style="text-align: center">承保日期</th>
								<th style="text-align: center">交费频率</th>
								<th style="text-align: center">年度</th>
								<th style="text-align: center">期数</th>
								<th style="text-align: center">数据来源</th>
								<th style="text-align: center">交费起始日期</th>
								<th style="text-align: center">交费终止日期</th>
								<th style="text-align: center">特殊类型</th>
								<th style="text-align: center">后续处理的具体内容</th>
								<th style="text-align: center">是否重复数据</th>
								<th style="text-align: center">是否已打印</th>
								<th style="text-align: center">原DMP发票号码</th>
								<th style="text-align: center">状态</th>
							</tr>
							<s:iterator value="paginationList.recordList" id="iList"
										status="stuts">

								<tr align="center"
									class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
									<td><input type="checkbox"
											   style="width:13px;height:13px;" name="batchIdList"
											   value="<s:property value="#iList.busiNessId"/>"
											   <s:if test="transFerFlag==1">disabled</s:if>/></td>
									<td align="center"><s:property value='#stuts.count' /></td>
									<td><s:property value="ttmPrcno" /></td>
									<td><s:property value="cherNum" /></td>
									<td><s:property value="repNum" /></td>
									<td><s:property value="trdtStr" /></td>
									<td><s:property value="planLongDesc" /></td>
									<td><s:property value="cownName" /></td>
									<td><s:property value="accTamt" /></td>
									<td><s:property value="chanNelCh" /></td>
									<td><s:property value="feeTypCh" /></td>
									<td><s:property value="hisSdteStr" /></td>
									<td><s:property value="billFreqCh" /></td>
									<td><s:property value="polYear" /></td>
									<td><s:property value="premTerm" /></td>
									<td><s:property value="dsouRceCh" /></td>
									<td><s:property value="instFromStr" /></td>
									<td><s:property value="insTtoStr" /></td>
									<td><s:property value="speFlag" /></td>
									<td><s:property value="spereMark" /></td>
									<td><s:property value="dupynStr" /></td>
									<td><s:property value="priynCh" /></td>
									<td><s:property value="impno" /></td>
									<td><s:property value="transFerFlagCh" /></td>

								</tr>
							</s:iterator>
						</table>
					</div>
				</div> <input type="hidden" name="paginationList.showCount" value="false">
				<div id="anpBoud" align="Right" style="overflow:auto;width:100%;">
					<table width="100%" cellspacing="0" border="0">
						<tr>
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
	function findHistoryTransInfoList() {
		$("#Form1").submit();
	}
	function offWH() {
		var pdiv = document.getElementById("lessGridList4").offsetWidth;
		$("#rdiv").attr("style", "width:" + pdiv * 0.98 + "px;");
		//        var div = document.getElementById("rdiv").offsetWidth;
		alert(pdiv * 0.98);
	}
	function checkAll(obj, itemName) {
		var inputs = document.getElementsByName(itemName);
		for (var i = 0; i < inputs.length; i++) {
			if (inputs[i].disabled == false) {
				inputs[i].checked = obj.checked;
			} else {
				inputs[i].checked = false;
			}
		}
	}

	function transFerTransInfo() {
		var id = document.getElementsByName("batchIdList");
		var busiNessIds = "";
		for (var i = 0; i < id.length; i++) {
			if (id[i].checked == true) {
				busiNessIds += id[i].value + ",";
			}
		}
		if(busiNessIds != ""){
			$("#busiNessIds").val(busiNessIds.substring(busiNessIds,busiNessIds.length-1));
		}else{
			alert("请选择需要移送的记录!");
		}
		var form = document.getElementById("Form1");
		form.action="hTransInfoToTransInfo.action";
		form.submit();
		form.action="historyTransInfoList.action";
	}
</script>
</html>
