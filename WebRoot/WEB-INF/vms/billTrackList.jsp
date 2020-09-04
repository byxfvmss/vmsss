<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
         import="java.math.BigDecimal" import="com.opensymphony.util.BeanUtils"
         import="com.cjit.common.constant.ScopeConstants"
         import="com.cjit.common.util.NumberUtils"
         import="com.cjit.common.util.PaginationList"
         import="com.cjit.vms.trans.model.BillInfo"
         import="com.cjit.vms.trans.util.DataUtil"
         contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="../../page/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
    <title>增值税管理平台</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link href="<%=bopTheme2%>/css/main.css" type="text/css"
          rel="stylesheet">
    <script language="javascript" type="text/javascript"
            src="<%=webapp%>/page/js/calendar_notime.js"></script>
    <script language="javascript" type="text/javascript"
            src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
    <script language="javascript" type="text/javascript"
            src="<%=webapp%>/page/js/editview.js"></script>
    <script language="javascript" type="text/javascript"
            src="<%=webapp%>/page/js/XmlHttp.js"></script>
    <script language="javascript" type="text/javascript"
            src="<%=webapp%>/page/js/search.js"></script>
    <script language="javascript" type="text/javascript"
            src="<%=webapp%>/page/js/ylb.js"></script>
    <script language="javascript" type="text/javascript"
            src="<%=webapp%>/page/js/lhb.js"></script>
    <script language="javascript" type="text/javascript"
            src="<%=bopTheme%>/js/main.js"></script>
    <script language="javascript" type="text/javascript"
            src="<%=webapp%>/page/js/search.js"></script>
    <script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
    <script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/vms.js"></script>
    <script type="text/javascript">
        // [查询]按钮  [导出]按钮
        function submitForm(actionUrl) {
            submitAction(document.forms[0], actionUrl);
            document.forms[0].action = 'listBillTrack.action';
        }
        // 撤销按钮
        function cancelBill() {
            if (checkChkBoxesSelected("selectBillIds")) {
                var billIds = document.Form1.selectBillIds;
                var billDataStatus = document.Form1.selectBillDataStatus;
                var errorMsg = "";
                var canCancel = true;
                var ids = "";
                for (var i = 0; i < billIds.length; i++) {
                    if (billIds[i].checked) {
                        if (billDataStatus[i].value != "3" && billDataStatus[i].value != "4" && billDataStatus[i].value != "9") {
                            errorMsg += "第" + (i + 1) + "行票据不能撤销！\n";
                            canCancel = false;
                        }
                        ids = ids == "" ? billIds[i].value : ids + "," + billIds[i].value;
                    }
                }
                if (canCancel == false) {
                    alert(errorMsg + "撤销处理，应选择状态为审核通过、无需审核、打印失败的记录！");
                    return false;
                }
                if (!confirm("确定将选中票据进行撤销处理？")) {
                    return false;
                }
//				goToPage("toBackBill.action?billIds=" + ids);
                showModalDialog("toBackBill.action?billIds=" + ids, window,
                        "dialogWidth:700px;dialogHeight:410px;center=1;scroll=1;help=0;status=0;");
            } else {
                alert("请选择票据记录！");
            }
        }
        var msg = '<s:property value="message"/>';
        if (msg != null && msg != '') {
            alert(msg);
        }

        function cancelBillSuccess() {
            alert("选中的票据撤销成功！");
            document.forms[0].submit();
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
                document.forms[0].action = 'listBillTrack.action';
            }
        }

        function toExcel() {
            if (checkChkBoxesSelected("selectBillIds")) {
                var billIds = document.Form1.selectBillIds;
                var ids = "";
                for (var i = 0; i < billIds.length; i++) {
                    if (billIds[i].checked) {
                        ids = ids == "" ? billIds[i].value : ids + "," + billIds[i].value;
                    }
                }
                document.forms[0].action = "exportBill.action?billIds=" + ids;
                document.forms[0].submit();
                document.forms[0].action = 'listBillTrack.action';
            } else {
                alert("请选择票据信息！");
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
<form name="Form1" method="post" action="listBillTrack.action"
      id="Form1">
    <%
        String currMonth = (String) request.getAttribute("currMonth");
    %>
    <input type="hidden" name="submitFlag" id="submitFlag" value=""/> <input
        type="hidden" name="currMonth" id="currMonth" value="<%=currMonth%>"/>
    <table id="tbl_main" cellpadding="0" cellspacing="0"
           class="tablewh100">
        <tr>
            <td class="centercondition"><s:component
                    template="rocketMessage"/>
                <div id="tbl_current_status">
                    <img src="<%=bopTheme%>/themes/images/icons/icon13.png"/> <span
                        class="current_status_menu">当前位置：</span> <span
                        class="current_status_submenu1">销项税管理</span> <span
                        class="current_status_submenu">综合查询</span> <span
                        class="current_status_submenu">票据查询</span>
                </div>

                <div class="widthauto1">
                    <table id="tbl_query" cellpadding="0" cellspacing="0" width="100%"
                           border="0">
                        <tr>
                            <td align="left">申请开票日期</td>
                            <td><input class="tbl_query_time" id="applyBeginDate"
                                       type="text" name="billInfo.applyBeginDate"
                                       value="<s:property value='billInfo.applyBeginDate'/>"
                                       onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'applyEndDate\')}'})"
                                       size='11' /> - <input class="tbl_query_time" id="applyEndDate"
                                                              type="text" name="billInfo.applyEndDate"
                                                              value="<s:property value='billInfo.applyEndDate'/>"
                                                              onfocus="WdatePicker({minDate:'#F{$dp.$D(\'applyBeginDate\')}'})"
                                                              size='11' />
                            </td>
                            <td align="left">发票代码</td>
                            <td><input type="text" class="tbl_query_text"
                                       name="billInfo.billCode"
                                       value="<s:property value='billInfo.billCode'/>" maxlength="10"
                                       onkeypress="checkkey(value);"/></td>
                            <td align="left">发票号码</td>
                            <td><input type="text" class="tbl_query_text"
                                       name="billInfo.billNo"
                                       value="<s:property value='billInfo.billNo'/>" maxlength="8"
                                       onkeypress="checkkey(value);"/></td>
                        </tr>
                        <tr>
                            <td align="left">开票日期</td>
                            <td><input class="tbl_query_time" id="billBeginDate"
                                       type="text" name="billInfo.billBeginDate"
                                       value="<s:property value='billInfo.billBeginDate'/>"
                                       onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'billEndDate\')}'})"
                                       size='11' /> - <input class="tbl_query_time" id="billEndDate"
                                                              type="text" name="billInfo.billEndDate"
                                                              value="<s:property value='billInfo.billEndDate'/>"
                                                              onfocus="WdatePicker({minDate:'#F{$dp.$D(\'billBeginDate\')}'})"
                                                              size='11' />
                            </td>
                            <td align="left">客户纳税人识别号</td>
                            <td><input type="text" class="tbl_query_text"
                                       name="billInfo.customerTaxno"
                                       value="<s:property value='billInfo.customerTaxno'/>"
                                       maxlength="25"/></td>
                            <td align="left">客户名称</td>
                            <td><input type="text" class="tbl_query_text"
                                       name="billInfo.customerName"
                                       value="<s:property value='billInfo.customerName'/>"
                                       maxlength="100"/></td>


                        </tr>
                        <tr>
                            <td align="left">发票类型</td>
                            <td><select name="billInfo.fapiaoType"
                                        class="tbl_query_text">
                                <option value=""
                                        <s:if test='billInfo.fapiaoType==""'>selected</s:if>
                                        <s:else></s:else>>全部
                                </option>
                                <option value="0"
                                        <s:if test='billInfo.fapiaoType=="0"'>selected</s:if>
                                        <s:else></s:else>>专
                                </option>
                                <option value="1"
                                        <s:if test='billInfo.fapiaoType=="1"'>selected</s:if>
                                        <s:else></s:else>>普
                                </option>
                            </select></td>
                            <td align="left">是否手工录入</td>
                            <td><select name="billInfo.isHandiwork"
                                        class="tbl_query_text">
                                <option value=""
                                        <s:if test='billInfo.isHandiwork==""'>selected</s:if>
                                        <s:else></s:else>>全部
                                </option>
                                <option value="1"
                                        <s:if test='billInfo.isHandiwork=="1"'>selected</s:if>
                                        <s:else></s:else>>自动开票
                                </option>
                                <option value="2"
                                        <s:if test='billInfo.isHandiwork=="2"'>selected</s:if>
                                        <s:else></s:else>>人工审核
                                </option>
                                <option value="3"
                                        <s:if test='billInfo.isHandiwork=="3"'>selected</s:if>
                                        <s:else></s:else>>人工开票
                                </option>
                            </select></td>
                            <td align="left">开具类型</td>
                            <td><select name="billInfo.issueType"
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
                        </tr>
                        <tr>
                            <td align="left">状态</td>
                            <td><s:select id="billInfo.dataStatus"
                                          name="billInfo.dataStatus" list="billDataStatusList"
                                          headerKey="" headerValue="全部" listKey='value' listValue='text'
                                          cssClass="tbl_query_text"/></td>
                                          
                                          
                             <td align="left">分公司</td>
                            <td><input type="text" class="tbl_query_text" name=""
                                       value="<s:property value=''/>" maxlength="25"/></td>
                            <td colspan="4"><input type="button"
                                                   class="tbl_query_button" value="查询"
                                                   onMouseMove="this.className='tbl_query_button_on'"
                                                   onMouseOut="this.className='tbl_query_button'" name="BtnView"
                                                   id="BtnView"
                                                   onclick="submitForm('listBillTrack.action?fromFlag=select')"/>
                            </td>
                            
                            <td align="left"></td>
                            <td>
                            </td>
                            
                        </tr>
                    </table>
                </div>
                <table id="tbl_tools" width="100%" border="0">
                    <tr>
                        <td align="left"><a href="#"
                                            onclick="submitForm('exportBill.action')"><img
                                src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png"/>导出</a>
                        </td>
                    </tr>
                </table>
                <div id="lessGridList4" style="overflow: auto; width: 100%;">
                    <div id="rDiv" style="width:0px;height:auto;">
                        <table class="lessGrid" cellspacing="0" rules="all" border="0"
                               cellpadding="0" style="border-collapse: collapse;width: 100%;">
                            <tr class="lessGrid head">
                                <th style="text-align:center">序号</th>
                                <th style="text-align:center">借据号</th>
                                <th style="text-align:center">合同号</th>
                                <!-- <th style="text-align:center">保全受理号</th> -->
                                <th style="text-align:center">分公司</th>
                                <th style="text-align:center">申请日期</th>
                                <th style="text-align:center">开票日期</th>
                                <th style="text-align:center">客户名称</th>
                                <th style="text-align:center">客户纳税人识别号</th>
                                <th style="text-align:center">发票代码</th>
                                <th style="text-align:center">发票号码</th>
                                <th style="text-align:center">合计金额</th>
                                <th style="text-align:center">合计税额</th>
                                <th style="text-align:center">价税合计</th>
                                <th style="text-align:center">是否手工录入</th>
                                <th style="text-align:center">发票类型</th>
                                <th style="text-align:center">开具类型</th>
                                <!-- <th style="text-align:center">渠道</th> -->
                                <th style="text-align:center">状态</th>
                                <th style="text-align:center" colspan="2">操作</th>
                            </tr>
                            <%
                                PaginationList paginationList = (PaginationList) request
                                        .getAttribute("paginationList");
                                if (paginationList != null) {
                                    List billInfoList = paginationList.getRecordList();
                                    if (billInfoList != null && billInfoList.size() > 0) {
                                        for (int i = 0; i < billInfoList.size(); i++) {
                                            BillInfo bill = (BillInfo) billInfoList.get(i);
                                            if (i % 2 == 0) {
                            %>
                            <tr class="lessGrid rowA">
                                    <%
									} else {
								%>

                            <tr class="lessGrid rowB">
                                <%
                                    }
                                %>
                                <td align="center"><%=i + 1%>
                                </td>
                                <td align="center"><%=bill.getTtmpRcno() == null ? "" : bill
                                        .getTtmpRcno()%>
                                </td>
                                <td align="center"><%=bill.getInsureId() == null ? "" : bill
                                        .getInsureId()%>
                                </td>
                              <%--   <td align="center"><%=bill.getRepNum() == null ? "" : bill
                                        .getRepNum()%>
                                </td> --%>
                                <td align="center"><%=bill.getRepNum() == null ? "" : bill
                                        .getRepNum()%>
                                </td>
                                <td align="center"><%=bill.getApplyDate() == null ? "" : bill
                                        .getApplyDate()%>
                                </td>
                                <td align="center"><%=bill.getBillDate() == null ? "" : bill
                                        .getBillDate()%>
                                </td>
                                <td align="left"><%=bill.getCustomerName()%>
                                </td>
                                <td align="left"><%=bill.getCustomerTaxno()%>
                                </td>
                                <td align="center"><%=bill.getBillCode()%>
                                </td>
                                <td align="center"><%=bill.getBillNo()%>
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
                                <td align="center"><%=DataUtil.getFapiaoTypeCH(bill.getFapiaoType())%>
                                </td>
                                <td align="center"><%=DataUtil.getIssueTypeCH(bill.getIssueType())%>
                                </td>
                               <%--  <td align="center"><%=bill.getChanNelCh() == null ? "" : bill
                                        .getChanNel()%>
                                </td> --%>
                                <td align="center"><%=DataUtil.getDataStatusCH(
                                        bill.getDataStatus(), "BILL")%>
                                </td>
                                <td align="center"><a
                                        href="billToTrans.action?curPage=<s:property value='paginationList.currentPage'/>&billId=<%=BeanUtils.getValue(bill, "billId")%>">
                                    <img
                                            src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1014.png"
                                            title="查看交易" style="border-width: 0px;"/>
                                </a>
                                </td>
                                <td align="center"><a href="javascript:void(0);"
                                                      onClick="OpenModalWindowSubmit('viewImgFromBill.action?billId=<%=bill.getBillId()%>',1000,650,true)">
                                    <img
                                            src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1015.png"
                                            title="查看票样" style="border-width: 0px;"/>
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
                <div id="anpBoud" align="Right" style="width:100%;">
                    <table width="100%" cellspacing="0" border="0">
                        <tr>
                            <td align="right"><s:component template="pagediv"/>
                            </td>
                        </tr>
                    </table>
                </div>
            </td>
        </tr>
    </table>
</form>
</body>
</html>