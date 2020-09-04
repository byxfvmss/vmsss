<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
         import="java.math.BigDecimal"
         import="com.opensymphony.util.BeanUtils"
         import="com.cjit.common.constant.ScopeConstants"
         import="com.cjit.common.util.NumberUtils"
         import="com.cjit.common.util.PaginationList"
         import="com.cjit.vms.trans.model.BillInfo"
         import="com.cjit.vms.trans.util.DataUtil"
         contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="../../../page/include.jsp" %>
<OBJECT id='ocxObj' width=100 height=100 classid="clsid:1E25664C-EA53-4F39-8575-684737626D6F"
        style="display: none;"></OBJECT>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
    <title>增值税管理平台</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
    <script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/calendar_notime.js"></script>
    <script language="javascript" type="text/javascript"
            src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
    <script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/editview.js"></script>
    <script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
    <script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
    <script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/XmlHttp.js"></script>
    <script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/search.js"></script>
    <script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/ylb.js"></script>
    <script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/lhb.js"></script>
    <script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
    <script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/search.js"></script>
    <!-- 税控服务器 -->
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/taxSelver/currentbill.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/taxSelver/validatorTax.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/taxSelver/compareCurrentBill.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/taxSelver/billCancel.js"></script>
	<!-- 税控软件 -->
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/billinterface/billInterfaceAjax.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/billinterface/bw_disk.js"></script>
	
    <script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
    <script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/vms.js"></script>
    <script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/billinterface/<%=(String) request.getAttribute("jspParam")%>.js"></script>
    
    <script type="text/javascript">
        var billInterface = new BillInterface();
	    billInterface.init({});
        // [查询]按钮  [导出]按钮
        function submitForm(actionUrl) {
            submitAction(document.forms[0], actionUrl);
            document.forms[0].action = "listIssueBill.action?paginationList.showCount=" + "false";
        }

        //开具按钮
        function issueBill() {
        	
        	var billIds = document.getElementsByName("selectBillIds");
			var fapiaoTypes = document.getElementsByName("fapiaoTypes");
			var fapiaoType = "";
			if(fapiaoTypes.length>0){
				fapiaoType = fapiaoTypes[0].value;
			}
			var ids="";
				
			for(var i=0;i<billIds.length;i++){
				if (billIds[i].checked){
					ids = ids === "" ? billIds[i].value : ids + "," + billIds[i].value;
				}
			}
				
			if(ids==''){
				alert("请选择票据记录");
				return false;
			}
			if(confirm("确认开具？")){
				billInterface.createBillissue({ids:ids,fapiaoType:fapiaoType});
			}
		    submitAction(document.forms[0], "listIssueBill.action?paginationList.showCount="+"false");
        }
        

        // 撤销按钮
        function cancelBill() {
            if (checkChkBoxesSelected("selectBillIds")) {
                var billIds = document.getElementsByName("selectBillIds");
                var isHandiworks = document.getElementsByName("isHandiworks");
//                for (var i = 0; i < billIds.length; i++) {
//                    if (billIds[i].checked) {
//                        if ("1" == isHandiworks[i].value) {
//                            alert("选中票据包含自动开票记录，不允许撤销");
//                            return false;
//                        }
//                    }
//                }

                if (!confirm("是否确定对选中票据进行撤销处理？")) {
                    return false;
                }
                document.forms[0].action = 'revokeBillFromIssue.action';
                document.forms[0].submit();
                document.forms[0].action = "listIssueBill.action?paginationList.showCount=" + "false";
            } else {
                alert("请选择票据记录！");
            }
        }
        var msg = '<s:property value="message"/>';
        if (msg != null && msg != '') {
            alert(msg);
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
                    + "px;center=1;scroll=1;help=0;status=0;");
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
<form name="Form1" method="post" action="listIssueBill.action" id="Form1">
    <input type="hidden" name="taxParam" id="taxParam" value="<s:property value='taxParam'/>"/>
    <%
        String currMonth = (String) request.getAttribute("currMonth");
        Long invalidInvoiceNum = (Long) request.getAttribute("invalidInvoiceNum");
    %>
    <input type="hidden" name="submitFlag" id="submitFlag" value=""/>
    <input type="hidden" name="currMonth" id="currMonth" value="<%=currMonth%>"/>
    <input type="hidden" name="invalidInvoiceNum" id="invalidInvoiceNum" value="<%=invalidInvoiceNum %>"/>
    <table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
        <tr>
            <td class="centercondition">
                <s:component template="rocketMessage"/>
                <div id="tbl_current_status">
                    <img src="<%=bopTheme%>/themes/images/icons/icon13.png"/>
                    <span class="current_status_menu">当前位置：</span>
                    <span class="current_status_submenu1">销项税管理</span>
                    <span class="current_status_submenu">发票管理</span>
                    <span class="current_status_submenu">发票开具</span>
                </div>
                <div class="widthauto1">
                    <table id="tbl_query" cellpadding="0" cellspacing="0" width="100%" border="0">
                        <tr>
                            <td style="text-align: right;width: 6%;">投保单号</td>
                            <td style="text-align: left;width:14%;"><input
                                    class="tbl_query_text" type="text" name="ttmpRcno"
                                    value="<s:property value='billInfo.ttmpRcno'/>" maxlength="20"/></td>
                            <td style="text-align: right;width: 6%;">保单号</td>
                            <td style="text-align: left;width:14%;"><input
                                    class="tbl_query_text" type="text" name="cherNum"
                                    value="<s:property value='billInfo.cherNum'/>" maxlength="20"/></td>
                            <td style="text-align: right;width: 8%;">申请开票日期</td>
                            <td style="text-align: left;width: 20%;"><input
                                    class="tbl_query_time" id="applyBeginDate" type="text"
                                    name="billInfo.applyBeginDate"
                                    value="<s:property value='billInfo.applyBeginDate'/>"
                                    onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'applyEndDate\')}'})"
                                    size='11'/> -- <input class="tbl_query_time"
                                                          id="applyEndDate" type="text" name="billInfo.applyEndDate"
                                                          value="<s:property value='billInfo.applyEndDate'/>"
                                                          onfocus="WdatePicker({minDate:'#F{$dp.$D(\'applyBeginDate\')}'})"
                                                          size='11'/></td>
                            <td style="text-align: right;width: 6%;">渠道</td>
                            <td style="text-align: left;width:14%;"><s:select
                                    list="chanNelList" listKey="key" listValue='value'
                                    cssClass="tbl_query_text" headerKey="" headerValue="请选择"/></td>
                        </tr>
                        <tr>
                            <td style="text-align: right;width: 6%;">客户名称</td>
                            <td style="text-align: left;width:14%;"><input type="text"
                                                                           class="tbl_query_text"
                                                                           name="billInfo.customerName"
                                                                           value="<s:property value='billInfo.customerName'/>"
                                                                           maxlength="100"/></td>
                            <td style="text-align: right;width: 6%;">缴费频率</td>
                            <td style="text-align: left;width:14%;"><s:select
                                    name="wada" list="billFreqlList" listKey="key"
                                    listValue='value' cssClass="tbl_query_text" headerKey=""
                                    headerValue="请选择"/></td>
                            <td style="text-align: right;width: 6%;">承保日期</td>
                            <td style="text-align: left;width: 20%;"><input
                                    class="tbl_query_time" id="hissBeginDte" type="text"
                                    name="hissBeginDte"
                                    value="<s:property value='billInfo.hissBeginDte'/>"
                                    onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'applyEndDate\')}'})"
                                    size='11'/> -- <input class="tbl_query_time" id="hissEndDte"
                                                          type="text" name="hissEndDte"
                                                          value="<s:property value='billInfo.hissEndDte'/>"
                                                          onfocus="WdatePicker({minDate:'#F{$dp.$D(\'applyBeginDate\')}'})"
                                                          size='11'/></td>
                            <td style="text-align: right;width: 6%;">费用类型</td>
                            <td style="text-align: left;width:14%;"><s:select
                                    name="billInfo.feeTyp" list="feeTypList" listKey="key"
                                    listValue='value' cssClass="tbl_query_text" headerKey=""
                                    headerValue="请选择"/></td>
                        </tr>
                        <tr>
                            <td style="text-align: right;width: 6%;">特殊标记</td>
                            <td style="text-align: left;width:14%;"><input
                                    class="tbl_query_text" type="text" name=""
                                    value="<s:property value=''/>"/></td>
                            <td style="text-align: right;width: 6%;">开具类型</td>
                            <td style="text-align: left;width:14%;"><select
                                    name="billInfo.issueType" style="width:135px"
                                    class="tbl_query_text">
                                <option value=""
                                        <s:if test='billInfo.issueType==""'>selected</s:if>
                                        <s:else></s:else>>全部
                                </option>
                                <option value="1"
                                        <s:if test='billInfo.issueType=="1"'>selected</s:if>
                                        <s:else></s:else>>单笔
                                </option>
                                <option value="2"
                                        <s:if test='billInfo.issueType=="2"'>selected</s:if>
                                        <s:else></s:else>>合并
                                </option>
                                <option value="3"
                                        <s:if test='billInfo.issueType=="3"'>selected</s:if>
                                        <s:else></s:else>>拆分
                                </option>
                            </select></td>
                            <td style="text-align: right;width: 6%;">发票类型</td>
                            <td style="text-align: left;width:14%;"><select
                                    name="billInfo.fapiaoType" style="width:135px">
                                <option value="0"
                                        <s:if test='billInfo.fapiaoType=="0"'>selected</s:if>
                                        <s:else></s:else>>增值税专用发票
                                </option>
                                <option value="1"
                                        <s:if test='billInfo.fapiaoType=="1"'>selected</s:if>
                                        <s:else></s:else>>增值税普通发票
                                </option>
                            </select></td>
                            <td style="text-align: right;width: 6%;">状态</td>
                            <td style="text-align: left;width:14%;"><select
                                    name="billInfo.dataStatus" style="width:135px">
                                <option value=""
                                        <s:if test='billInfo.dataStatus==""'>selected</s:if>
                                        <s:else></s:else>>全部
                                </option>
                                <option value="3"
                                        <s:if test='billInfo.dataStatus=="1"'>selected</s:if>
                                        <s:else></s:else>>审核通过
                                </option>
                                <option value="4"
                                        <s:if test='billInfo.dataStatus=="2"'>selected</s:if>
                                        <s:else></s:else>>无需审核
                                </option>
                                <option value="7"
                                        <s:if test='billInfo.dataStatus=="3"'>selected</s:if>
                                        <s:else></s:else>>开具失败
                                </option>
                            </select></td>
                        </tr>
                        <tr>
                            <td style="text-align: right;width: 6%;">数据来源</td>
                            <td style="text-align: left;width:14%;"><s:select
                                    name="billInfo.dsouRce" list="dsouRceList" listKey="key"
                                    listValue='value' cssClass="tbl_query_text" headerKey=""
                                    headerValue="请选择"/></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td style="text-align: right;width: 6%;"></td>
                            <td style="text-align: left;width:14%;">
                                <input type="button" class="tbl_query_button" value="查询"
                                       onMouseMove="this.className='tbl_query_button_on'"
                                       onMouseOut="this.className='tbl_query_button'"
                                       name="BtnView" id="BtnView" onclick="submitForm('listIssueBill.action')"/>
                            </td>
                        </tr>
                    </table>
                </div>
                <table id="tbl_tools" width="100%" border="0">
                    <tr>
                        <td align="left">
                            <a href="#" name="BtnView" id="BtnView" onclick="issueBill()"><img
                                    src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1010.png"/>开具</a>
                            <a href="#" name="BtnView" id="BtnView" onclick="submitForm('exportIssueBill.action')"><img
                                    src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png"/>导出</a>
                            <a href="#" name="BtnView" id="BtnView" onclick="cancelBill()"><img
                                    src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1002.png"/>撤销</a>
                            <%-- <a href="#" name="BtnView" id="BtnView"
                               onclick="openWindows1('inmputMatchInfoList.action?flag=true')"><img
                                    src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1002.png" />开具文件处理</a>
 --%>
                        </td>
                    </tr>
                </table>

                <div id="lessGridList4" style="overflow: auto; width: 100%;">
                    <div id="rDiv" style="width: 0px;height: auto;">
                        <table class="lessGrid" cellspacing="0" rules="all" border="0" cellpadding="0"
                               style="border-collapse: collapse; width: 100%;">
                            <tr class="lessGrid head">
                                <th style="text-align:center">
                                    <input style="width:13px;height:13px;" id="CheckAll" type="checkbox"
                                           onclick="cbxselectall(this,'selectBillIds')"/>
                                </th>
                                <th style="text-align:center">序号</th>
                                <th style="text-align:center">投保单号</th>
                                <th style="text-align:center">保单号</th>
                                <th style="text-align:center">保全受理号</th>
                                <th style="text-align:center">交易日期</th>
                                <th style="text-align:center">客户名称</th>
                                <th style="text-align:center">客户纳税人识别号</th>
                                <th style="text-align:center">合计金额</th>
                                <th style="text-align:center">合计税额</th>
                                <th style="text-align:center">价税合计</th>
                                <th style="text-align:center">是否手工录入</th>
                                <th style="text-align:center">开具类型</th>
                                <th style="text-align:center">发票类型</th>
                                <th style="text-align:center">状态</th>
                                <th style="text-align:center">渠道</th>
                                <th style="text-align:center">费用类型</th>
                                <th style="text-align:center">承保日期</th>
                                <th style="text-align:center">缴费频率</th>
                                <th style="text-align:center">数据来源</th>
                                <th style="text-align:center">明细</th>
                            </tr>
                            <%
                                PaginationList paginationList = (PaginationList) request.getAttribute("paginationList");
                                if (paginationList != null) {
                                    List billInfoList = paginationList.getRecordList();
                                    if (billInfoList != null && billInfoList.size() > 0) {
                                        for (int i = 0; i < billInfoList.size(); i++) {
                                            BillInfo bill = (BillInfo) billInfoList.get(i);
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
                                <td align="center">
                                    <input style="width:13px;height:13px;" type="checkbox" name="selectBillIds"
                                           value="<%=BeanUtils.getValue(bill,"billId")%>"/>
                                    <input type="hidden" name="fapiaoTypes" value="<%=bill.getFapiaoType() %>">
                                    <input type="hidden" name="isHandiworks" value="<%=bill.getIsHandiwork() %>">
                                </td>
                                <td align="center" id='<%=bill.getBillId() + "Count"%>'><%=i + 1%>
                                </td>
                                <td align="center"><%=bill.getTtmpRcno() == null ? "" : bill
                                        .getTtmpRcno()%>
                                </td>
                                <td align="center"><%=bill.getInsureId() == null ? "" : bill
                                        .getInsureId()%>
                                </td>
                                <td align="center"><%=bill.getRepNum() == null ? "" : bill
                                        .getRepNum()%>
                                </td>
                                <td align="center"><%=bill.getApplyDate() == null ? "" : bill
                                        .getApplyDate()%>
                                </td>
                                <td align="left"><%=bill.getCustomerName()%>
                                </td>
                                <td align="left"><%=bill.getCustomerTaxno()%>
                                </td>
                                <td align="right"><%=NumberUtils.format(bill.getAmtSum(), "", 2)%>
                                </td>
                                <td align="right"><%=NumberUtils.format(bill.getTaxAmtSum(), "", 2)%>
                                </td>
                                <td align="right"><%=NumberUtils.format(bill.getSumAmt(), "", 2)%>
                                </td>
                                <td align="center"><%=DataUtil.getIsHandiworkCH(bill
                                        .getIsHandiwork())%>
                                </td>
                                <td align="center"><%=DataUtil.getIssueTypeCH(bill.getIssueType())%>
                                </td>
                                <td align="center"><%=DataUtil.getFapiaoTypeCH(bill.getFapiaoType())%>
                                </td>
                                <td align="center"><%=DataUtil.getDataStatusCH(
                                        bill.getDataStatus(), "BILL")%>
                                </td>
                                <td><%=bill.getChanNel() == null ? "" : bill
                                        .getChanNelCh()%>
                                </td>
                                <td><%=bill.getFeeTypCh() == null ? "" : bill
                                        .getFeeTypCh()%>
                                </td>
                                <td><%=bill.getHissDte() == null ? "" : bill
                                        .getHissDte()%>
                                </td>
                                <td><%=bill.getBillFreqCh() == null ? "" : bill
                                        .getBillFreqCh()%>
                                </td>
                                <td><%=bill.getDsouRceCh() == null ? "" : bill
                                        .getDsouRceCh()%>
                                </td>
                                <td align="center">
                                    <a href="billEditToTrans.action?curPage=<s:property value='paginationList.currentPage'/>&billId=<%=BeanUtils.getValue(bill,"billId")%>">
                                        <img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1014.png"
                                             title="查看交易" style="border-width: 0px;"/></a>
                                    <a href="javascript:void(0);"
                                       onClick="OpenModalWindow('viewSample.action?billId=<%=bill.getBillId()%>',1000,650,true)">
                                        <img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1015.png"
                                             title="查看票样" style="border-width: 0px;"/></a>
                                </td>
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
                <div id="anpBoud" align="Right" style="overflow:auto;width:100%;">
                    <input type="hidden" name="paginationList.showCount" value="false">
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
</html>