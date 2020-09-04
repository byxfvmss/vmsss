<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal" import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.util.PaginationList"
	import="com.cjit.vms.trans.model.billInvalid.BillCancelInfo"
	import="com.cjit.vms.trans.model.BillInfo"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../../../page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<title>增值税管理平台</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%=bopTheme2%>/css/main.css" type="text/css"
	rel="stylesheet">
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/calendar_notime.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/XmlHttp.js"></script>
<!-- <script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/search.js"></script> -->
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/ylb.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/lhb.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/search.js"></script>
<script type="text/javascript"
	src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/vms.js"></script>
<script type="text/javascript">
        var msg = '<s:property value="message"/>';
        if (msg != null && msg != '') {
            alert(msg);
        }
        // [查询]按钮
        function query() {
            //document.forms[0].submit();
            submitAction(document.forms[0], "listBillCancelAuditing.action");
            document.forms[0].action = "listBillCancelAuditing.action";
        }
        // 审核通过按钮
        function auditingPass() {
            if (checkChkBoxesSelected("selectBillIds")) {
                var billIds = document.Form1.selectBillIds;
                if (billIds[0] == undefined) {
                    if (!confirm("确定将选中票据进行审核处理？")) {
                        return false;
                    }
                    submitAction(document.forms[0], "updateBillCancelAuditingStatus.action?dataStatus=14");//审核通过
                    document.forms[0].action = "listBillCancelAuditing.action";
                } else {
                    if (!isNaN(billIds.length)) {
                        if (!confirm("确定将选中票据进行审核处理？")) {
                            return false;
                        }
                        submitAction(document.forms[0], "updateBillCancelAuditingStatus.action?dataStatus=14");//审核通过
                        document.forms[0].action = "listBillCancelAuditing.action";
                    }
                }
            } else {
                alert("请选择发票记录！");
            }
        }
        // 审核拒绝按钮
        function auditingRefuse() {
            if (checkChkBoxesSelected("selectBillIds")) {
                var billIds = document.Form1.selectBillIds;
                var billId = "";
                if (billIds.length == undefined) {
                    billId = billIds.value;
                } else {
                    for (var i = 0; i < billIds.length; i++) {
                        if (billIds[i].checked) {
                            billId = billId == '' ? billIds[i].value : billId + "," + billIds[i].value;
                        }
                    }
                }
                if (billIds[0] == undefined) {
                    /*if(!confirm("确定将选中票据进行拒绝处理？")){
                     return false;
                     }*/
                    OpenModalWindowSubmit("billCancelToRefuse.action?billId=" + billId, 500, 250, true);
                    document.forms[0].action = "listBillCancelAuditing.action";
                    //submitAction(document.forms[0], "billCancelToRefuse.action");//审核通过
                } else {
                    if (!isNaN(billIds.length)) {
                        /*if(!confirm("确定将选中票据进行拒绝处理？")){
                         return false;
                         }*/
                        OpenModalWindowSubmit("billCancelToRefuse.action?billId=" + billId, 500, 250, true);
                        document.forms[0].action = "listBillCancelAuditing.action";
                        //submitAction(document.forms[0], "billCancelToRefuse.action");//审核通过
                    }
                }
            } else {
                alert("请选择发票记录！");
            }
        }

        // 导出按钮
        function exportCancelBill() {
            submitAction(document.forms[0], "cancelBillToExcel.action?reqExportSource=billCancelAuditing");
            document.forms[0].action = "listBillCancelAuditing.action";
        }

        function OpenModalWindowSubmit(newURL, width, height, needReload) {
            var retData = false;
            if (typeof(width) == 'undefined') {
                width = screen.width * 0.9;
            }
            if (typeof(height) == 'undefined') {
                height = screen.height * 0.9;
            }
            if (typeof(needReload) == 'undefined') {
                needReload = false;
            }
            retData = showModalDialog(newURL,
                    window,
                    "dialogWidth:" + width
                    + "px;dialogHeight:" + height
                    + "px;center=1;scroll=0;help=0;status=0;");
            if (needReload && retData) {
                window.document.forms[0].submit();
            }
        }
    </script>
<style type="text/css">
.style1 {
	width: 242px;
}
</style>
</head>
<body onkeydow="enterkey(event)">
	<form name="Form1" method="post" action="listBillCancelAuditing.action"
		id="Form1">
		<%
        String currMonth = (String) request.getAttribute("currMonth");
    %>
		<input type="hidden" name="submitFlag" id="submitFlag" value="" /> <input
			type="hidden" name="currMonth" id="currMonth" value="<%=currMonth%>" />
		<table id="tbl_main" cellpadding="0" cellspacing="0"
			class="tablewh100">
			<tr>
				<td class="centercondition"><s:component
						template="rocketMessage" />
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> <span
							class="current_status_menu">当前位置：</span> <span
							class="current_status_submenu1">销项税管理</span> <span
							class="current_status_submenu">作废管理</span> <span
							class="current_status_submenu">作废审核</span>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" width="80%"
							border="0">
							<tr>
								<td>借据号</td>
								<td><input type="text" class="tbl_query_text"
									name="billCancelInfo.ttmpRcno"
									value="<s:property value='billCancelInfo.ttmpRcno'/>"
									maxlength="20" /></td>
								<td align="left">合同号</td>
								<td><input type="text" class="tbl_query_text"
									name="billCancelInfo.cherNum"
									value="<s:property value='billCancelInfo.cherNum'/>"
									maxlength="20" /></td>
								<td align="left">开票日期</td>
								<td><input class="tbl_query_time" id="billBeginDate"
									type="text" name="billCancelInfo.billBeginDate"
									value="<s:property value='billCancelInfo.billBeginDate'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'billEndDate\')}'})"
									size='11' /> -- <input class="tbl_query_time" id="billEndDate"
									type="text" name="billCancelInfo.billEndDate"
									value="<s:property value='billCancelInfo.billEndDate'/>"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'billBeginDate\')}'})"
									size='11' /></td>
							</tr>
							<tr>
								<td>渠道</td>
								<td><s:select name="billCancelInfo.channel"
										list="chanNelList" listKey="key" listValue='value'
										cssClass="tbl_query_text" headerKey="" headerValue="请选择" /></td>
								<td align="left">客户名称</td>
								<td><input type="text" class="tbl_query_text"
									name="billCancelInfo.customerName"
									value="<s:property value='billCancelInfo.customerName'/>" /></td>
								<td align="left">到账日期</td>
								<td><input class="tbl_query_time" id="hissBeginDate"
									type="text" name="billCancelInfo.hissBeginDte"
									value="<s:property value='billCancelInfo.hissBeginDte'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'hissEndDte\')}'})"
									size='11' /> -- <input class="tbl_query_time" id="hissEndDte"
									type="text" name="billCancelInfo.hissEndDte"
									value="<s:property value='billCancelInfo.hissEndDte'/>"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'hissBeginDate\')}'})"
									size='11' /></td>
							</tr>
							<tr>
								<%-- <td align="left">交费频率</td>
								<td><s:select name="billCancelInfo.billFreq"
										list="billFreqlList" listKey="key" listValue='value'
										cssClass="tbl_query_text" headerKey="" headerValue="请选择" /></td>
								<td align="left">数据来源</td>
								<td><s:select name="billCancelInfo.dSource"
										list="dsouRceList" listKey="key" listValue='value'
										cssClass="tbl_query_text" headerKey="" headerValue="请选择" /></td> --%>
								<td align="left">费用类型</td>
								<td><s:select name="billCancelInfo.feeTyp"
										list="feeTypList" listKey="key" listValue='value'
										cssClass="tbl_query_text" headerKey="" headerValue="请选择" /></td>
							</tr>
							<tr>
								<td align="left">开具类型</td>
								<td><select id="billCancelInfo.issueType"
									name="billCancelInfo.issueType" style="width: 120px">
										<option value=""
											<s:if test='billCancelInfo.issueType==""'>selected</s:if>
											<s:else></s:else>>全部</option>
										<option value="1"
											<s:if test='billCancelInfo.issueType=="1"'>selected</s:if>
											<s:else></s:else>>单笔</option>
										<option value="2"
											<s:if test='billCancelInfo.issueType=="2"'>selected</s:if>
											<s:else></s:else>>合并</option>
										<option value="3"
											<s:if test='billCancelInfo.issueType=="3"'>selected</s:if>
											<s:else></s:else>>拆分</option>
								</select></td>
								<td align="left">发票类型</td>
								<td><select id="billCancelInfo.fapiaoType"
									name="billCancelInfo.fapiaoType">
										<option value="">全部</option>
										<option value="0"
											<s:if test='billCancelInfo.fapiaoType=="0"'>selected</s:if>
											<s:else></s:else>>增值税专用发票</option>
										<option value="1"
											<s:if test='billCancelInfo.fapiaoType=="1"'>selected</s:if>
											<s:else></s:else>>增值税普通发票</option>
								</select></td>
								<td></td>
								<td></td>
							</tr>
							<tr>
								<td>发票代码</td>
								<td><input type="text" class="tbl_query_text"
									name="billCancelInfo.billCode" maxlength="10"
									value="<s:property value='billCancelInfo.vtiBillCode'/>" /></td>
								</td>
								<td>发票号码</td>
								<td><input type="text" class="tbl_query_text"
									name="billCancelInfo.billNo" maxlength="10"
									value="<s:property value='billCancelInfo.billNo'/>" /></td>
								</td>
								<td></td>
								<td><input type="button" class="tbl_query_button"
									value="查询" onMouseMove="this.className='tbl_query_button_on'"
									onMouseOut="this.className='tbl_query_button'" name="BtnView"
									id="BtnView" onclick="query()" /></td>
							</tr>
						</table>
					</div>
					<table id="tbl_tools" width="100%" border="0">
						<tr>
							<td align="left"><a href="#" onClick="auditingPass()"><img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1017.png" />审核通过</a>
								<a href="#" onClick="auditingRefuse()"><img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1018.png" />审核打回</a>
								<a href="#" onClick="exportCancelBill()"><img
									src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />导出</a>
							</td>
						</tr>
					</table>
					<div id="lessGridList4" style="overflow: auto; width: 100%;">
						<div id="rDiv" style="width: 0px; height: auto;">
							<table class="lessGrid" cellspacing="0" rules="all" border="0"
								cellpadding="0" style="border-collapse: collapse; width: 100%;">
								<tr class="lessGrid head">
									<th style="text-align: center"><input
										style="width: 13px; height: 13px;" id="CheckAll"
										type="checkbox" onclick="cbxselectall(this,'selectBillIds')" />
									</th>
									<th style="text-align: center">序号</th>
									<th style="text-align: center">借据号</th>
									<th style="text-align: center">合同号</th>
									<th style="text-align: center">开票日期</th>
									<th style="text-align: center">开票日期</th>
									<th style="text-align: center">客户名称</th>
									<th style="text-align: center">发票代码</th>
									<th style="text-align: center">发票号码</th>
									<th style="text-align: center">合计金额</th>
									<th style="text-align: center">合计税额</th>
									<th style="text-align: center">价税合计</th>
									<th style="text-align: center">是否手工录入</th>
									<th style="text-align: center">开具类型</th>
									<th style="text-align: center">发票类型</th>
									<th style="text-align: center">状态</th>
									<th style="text-align: center">渠道</th>
									<th style="text-align: center">费用类型</th>
									<th style="text-align: center">操作</th>
								</tr>
								<%
                                PaginationList paginationList = (PaginationList) request.getAttribute("paginationList");
                                if (paginationList != null) {
                                    List billCancelInfoList = paginationList.getRecordList();
                                    if (billCancelInfoList != null && billCancelInfoList.size() > 0) {
                                        for (int i = 0; i < billCancelInfoList.size(); i++) {
                                            BillCancelInfo billCancel = (BillCancelInfo) billCancelInfoList.get(i);
                                            if (i % 2 == 0) {
                            %>
								<tr class="lessGrid rowA">
									<%
					}else{
	%>
								
								<tr class="lessGrid rowB">
									<%
                                    }
                                %>
									<td align="center"><input
										style="width: 13px; height: 13px;" type="checkbox"
										name="selectBillIds"
										value="<%=BeanUtils.getValue(billCancel,"billId")%>" /></td>
									<td align="center"><%=i + 1%></td>
									<td align="center"><%=billCancel.getTtmpRcno()==null?"":billCancel.getTtmpRcno() %>
									</td>
									<td align="center"><%=billCancel.getInsureId()==null?"":billCancel.getInsureId() %>
									</td>
<%-- 									<td align="center"><%=billCancel.getRepnum()==null?"":billCancel.getRepnum() %>--%>									</td>
									<td align="center"><%=billCancel.getBillDate() != null ? billCancel.getBillDate() : ""%>
									</td>
									<td align="center"><%=billCancel.getCustomerName()==null?"":billCancel.getCustomerName()%>
									</td>
									<td align="center"><%=billCancel.getBillCode()==null?"":billCancel.getBillCode()%>
									</td>
									<td align="center"><%=billCancel.getBillNo()==null?"":billCancel.getBillNo()%>
									</td>
									<td align="right"><%=NumberUtils.format(billCancel.getAmtSum(), "", 2)%>
									</td>
									<td align="right"><%=NumberUtils.format(billCancel.getTaxAmtSum(), "", 2)%>
									</td>
									<td align="right"><%=NumberUtils.format(billCancel.getSumAmt(), "", 2)%>
									</td>
									<td align="left"><%=(billCancel.getIsHandiwork() == null) ? "" : billCancel.getIsHandiwork().equals("1") ? "自动开票" : billCancel.getIsHandiwork().equals("2") ? "人工审核" : "人工开票"%>
									</td>
									<td align="left"><%=(billCancel.getIssueType() == null) ? "" : billCancel.getIssueType().equals("1") ? "单笔" : billCancel.getIssueType().equals("2") ? "合并" : "拆分"%>
									</td>
									<td align="right"><%=DataUtil.getFapiaoTypeCH(billCancel.getFapiaoType()) %>
									</td>
									<td align="right"><%=DataUtil.getDataStatusCH(billCancel.getDataStatus(), "BILL") %>
									</td>
									<td align="center"><%=billCancel.getChannelCh()==null?"":billCancel.getChannelCh() %>
									</td>
									<td align="center"><%=billCancel.getFeeTypCh()==null?"":billCancel.getFeeTypCh() %>
									</td>
									<td align="center"><%=billCancel.getHissDte()==null?"":billCancel.getHissDte() %>
									</td>
									<%-- <td align="center"><%=billCancel.getBillFreqCh() %></td>
									<td align="center"><%=billCancel.getdSourceCh() %></td>
 --%>									<td align="center"><a
										href="seeTransWithBill.action?reqSource=billCancelAuditing&billId=<%=BeanUtils.getValue(billCancel,"billId")%>">
											<img
											src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1014.png"
											title="查看交易" style="border-width: 0px;" />
									</a> <a href="javascript:void(0);"
										onClick="OpenModalWindowSubmit('viewImgFromBillForBillCancel.action?billId=<%=billCancel.getBillId()%>',1000,650,true)">
											<img
											src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1015.png"
											title="查看票样" style="border-width: 0px;" />
									</a></td>
								</tr>
								<%
                                        }
                                    }
                                }
                            %>
								</tr>
							</table>
						</div>
					</div>
					<div id="anpBoud" align="Right"
						style="overflow: auto; width: 100%;">
						<table width="100%" cellspacing="0" border="0">
							<tr>
								<td align="right"><s:component template="pagediv" /></td>
							</tr>
						</table>
					</div></td>
			</tr>
		</table>
	</form>
</body>
</html>