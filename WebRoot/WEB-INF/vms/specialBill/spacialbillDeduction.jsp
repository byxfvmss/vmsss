<%@ page language="java" import="java.util.*" import="java.math.BigDecimal" import="com.opensymphony.util.BeanUtils" import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils" import="com.cjit.common.util.PaginationList" import="com.cjit.vms.input.model.SpecialBillWithhold" import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<title>增值税管理平台</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../../page/include.jsp"%>
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
<link href="<%=webapp%>/page/common.css" type="text/css" rel="stylesheet">
<script src="<%=bopTheme%>/js/MessageBox/js/messageBox.js" type="text/javascript"></script>
<link href="<%=bopTheme%>/js/MessageBox/css/messageBox.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/search.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
<meta http-equiv="Pragma" content="no-cache" />
<script type="text/javascript">
	function editbill(url) {
		document.forms[0].action = url;
		document.forms[0].submit();
		document.forms[0].action = "inputSpecialbillDeduction.action";
	}
	function submitForm(url) {

		document.forms[0].action = url;
		document.forms[0].submit();
		document.forms[0].action = "inputSpecialbillDeduction.action";
	}
	function OpenDetial(url) {
		document.forms[0].action = url;
		document.forms[0].submit();
		document.forms[0].action = "inputSpecialbillDeduction.action";
	}
	function submitExport(url) {
		document.forms[0].action = url;
		document.forms[0].submit();
		document.forms[0].action = "inputSpecialbillDeduction.action";
	}
	function cmdRollBackBtn() {
		$billids = $("input[name='billid']:checked");
		if ($billids.size() == 0) {
			alert("请选择您要撤回的数据");
			return false;
		}
		submitform("specialbillRollBack.action");
	}
	function submitform(url) {
		document.forms[0].action = url;
		document.forms[0].submit();
		document.forms[0].action = "inputSpecialbillDeduction.action";
	}
</script>
</head>
<body>
	<form name="Form1" method="post" action="inputSpecialbillDeduction.action" id="Form1" enctype="multipart/form-data">
		<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
			<tr>
				<td class="centercondition">
					<s:component template="rocketMessage" />
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" />
						<span class="current_status_menu">当前位置：</span>
						<span class="current_status_submenu1">进项税管理</span>
						<span class="current_status_submenu">其他票据</span>
						<span class="current_status_submenu">票据代缴代扣</span>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" width="80%" border="0">
							<tr>
								<td align="left">税票票号</td>
								<td>
									<input type="text" class="tbl_query_text" name="billNo" value="<s:property value='billNo'/>" maxlength="100" />
								</td>
								<td align="left">填发日期</td>
								<td>
									<input class="tbl_query_time" id="" type="text" name="writeData" value="<s:property value='writeData'/>" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" size='11' "/>
								</td>
								<td align="left">缴款期限日期</td>
								<td>
									<input class="tbl_query_time" id="" type="text" name="payData" value="<s:property value='payData'/>" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" size='11' "/>
								</td>
								<td>票据种类</td>
								<td>
									<s:select list="specialbilltype" listKey="billType" listValue="billName" name="billtype"></s:select>
								</td>
							</tr>
							<tr>
								<td>缴款单位全称</td>
								<td>
									<input type="text" class="tbl_query_text" name="taxInstChn" value="<s:property value='taxInstChn'/>" maxlength="100" />
								</td>
								<td>缴款单位代码</td>
								<td>
									<input type="text" class="tbl_query_text" name="taxNo" value="<s:property value='taxNo'/>" maxlength="100" />
								</td>
								<td>发票状态</td>
								<td>
									<select id="billCancelInfo.fapiaoType" name="dataStatus" value="<s:property value='dataStatus'/>">
										<option value="" <s:if test='dataStatus=="0"'>selected</s:if> <s:else></s:else>>全部</option>
										<option value="2" <s:if test='dataStatus=="3"'>selected</s:if> <s:else></s:else>>待抵扣</option>
										<option value="3" <s:if test='dataStatus=="3"'>selected</s:if> <s:else></s:else>>已抵扣</option>
										<option value="4" <s:if test='dataStatus=="4"'>selected</s:if> <s:else></s:else>>不可已抵扣</option>
									</select>
								</td>
								<td>
									<input type="button" class="tbl_query_button" value="查询" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" name="BtnView" id="BtnView"
										onclick="submitForm('inputSpecialbillDeduction.action')"
									/>
								</td>
								<td></td>
							</tr>
						</table>
					</div>
					<table id="tbl_tools" width="100%" border="0">
						<tr>
							<td align="center">
								<a href="#" name="upLoad" id="upLoad" onclick="submitExport('exportSpecialbillDeduction.action')">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />
									导出
								</a>
							</td>
							<td align="center">
								<a href="#" id="cmdRollBackBtn" name="cmdRollBackBtn" onclick="cmdRollBackBtn()">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1032.png" />
									撤回数据
								</a>
							</td>
							<td align="center"></td>
						</tr>
					</table>
					<div id="lessGridList4" style="overflow: auto; width: 100%;">
						<table class="lessGrid" cellspacing="0" rules="all" border="0" cellpadding="0" style="border-collapse: collapse;width: 100%;">
							<tr class="lessGrid head">
								<th width="3%" style="text-align:center">
									<input id="CheckAll" style="width:13px;height:13px;" type="checkbox" onClick="checkAll(this,'billid')" />
								</th>
								<th style="text-align:center">序号</th>
								<th style="text-align:center">税票票号</th>
								<th style="text-align:center">缴款单位代码</th>
								<th style="text-align:center">缴款单位全称</th>
								<th style="text-align:center">科目编码</th>
								<th style="text-align:center">科目名称</th>
								<th style="text-align:center">填发日期</th>
								<th style="text-align:center">税款开始时间</th>
								<th style="text-align:center">税款结束时间</th>
								<th style="text-align:center">税款限缴日期</th>
								<th style="text-align:center">实缴金额</th>
								<th style="text-align:center">发票状态</th>
								<th style="text-align:center">操作</th>
							</tr>
							<s:iterator value="paginationList.recordList" id="iList" status="stuts">
								<tr>
									<td>
										<input type="checkbox" style="width:13px;height:13px;" name="billid" value="<s:property value="billNo"/>" />
									</td>
									<td>
										<s:property value="#stuts.count" />
									</td>
									</td>
									<td>
										<s:property value="billNo" />
									</td>
									<td>
										<s:property value="taxNo" />
									</td>
									<td>
										<s:property value="taxInstChn" />
									</td>
									<td>
										<s:property value="subjectId" />
									</td>
									<td>
										<s:property value="subjectName" />
									</td>
									<td>
										<s:property value="writeData" />
									</td>
									<td>
										<s:property value="belongDataS" />
									</td>
									<td>
										<s:property value="belongDataE" />
									</td>
									<td>
										<s:property value="payData" />
									</td>
									<td>
										<s:property value="taxAmtSum" />
									</td>
									<td>
										<s:if test="dataStatus==2">待抵扣</s:if>
										<s:if test="dataStatus==3">已抵扣</s:if>
										<s:if test="dataStatus==4">不可抵扣</s:if>
									</td>
									<td align="center">
										<a href="javascript:void(0);" onClick="editbill('toEditSpecialBill.action?billid=<s:property value="billNo"/>')">
											<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1005.png" title="编辑" style="border-width: 0px;" />
										</a>
										<a href="javascript:void(0);" onClick="OpenDetial('viewSpecialbillDetail.action?billid=<s:property value="billNo"/>')">
											<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1014.png" title="查看明细" style="border-width: 0px;" />
										</a>
									</td>
								</tr>
							</s:iterator>
						</table>
					</div>
					<div id="anpBoud" align="Right" style="overflow:auto;width:100%;">
						<table width="100%" cellspacing="0" border="0">
							<tr>
								<td align="right">
									<s:component template="pagediv" />
								</td>
							</tr>
						</table>
					</div>
			</tr>
		</table>
	</form>
</body>
</html>