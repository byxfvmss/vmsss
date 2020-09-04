<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
         import="java.math.BigDecimal"
         import="com.opensymphony.util.BeanUtils"
         import="com.cjit.common.constant.ScopeConstants"
         import="com.cjit.common.util.NumberUtils"
         import="com.cjit.common.util.PaginationList"
         import="com.cjit.vms.trans.model.BillInfo"
         import="com.cjit.vms.trans.model.EmsInfo"
         import="com.cjit.vms.trans.util.DataUtil"
         contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="../../page/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
    <title>增值税管理平台</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
    <script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/calendar_notime.js"></script>
    <script language="javascript" type="text/javascript"
            src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
    <script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/XmlHttp.js"></script>
    <script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/search.js"></script>
    <script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/ylb.js"></script>
    <script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/lhb.js"></script>
    <script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
    <script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/search.js"></script>
    <script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
    <script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/vms.js"></script>
    <script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
    <script type="text/javascript">
        // [查询]按钮
        function query() {
            //document.forms[0].submit();
            submitAction(document.forms[0], "billEmsMessage.action?paginationList.showCount=" + "false");
            document.forms[0].action = "billEmsMessage.action?paginationList.showCount=" + "false";
        }
        // 添加EMS信息按钮
        function saveEms() {
            if (checkChkBoxesSelected("selectBillIds")) {
                var billIds = document.Form1.selectBillIds;
                var EmsNos = document.Form1.selectEmsNos;
                var canCancel = true;
                if (!isNaN(billIds.length)) {
                    for (var i = 0; i < billIds.length; i++) {
                        if (billIds[i].checked) {
                            if (EmsNos[i].value != "") {
                                alert("所选信息中有快递信息记录，请检查后重新添加！");
                                canCancel = false;
                                break;
                            }
                        }
                    }
                }
                if (canCancel == false) {
                    return false;
                }
                if (!confirm("确定是否添加？")) {
                    return false;
                }
                submitAction(document.forms[0], "addbillEmsMessage.action?paginationList.showCount=" + "false");
                document.forms[0].action = "billEmsMessage.action?paginationList.showCount=" + "false";
            } else {
                alert("请选择交易记录！");
            }
        }
        function deleteEms() {
            if (checkChkBoxesSelected("selectBillIds")) {
                var billIds = document.Form1.selectBillIds;
                if (!confirm("确定是否删除？")) {
                    return false;
                }
                submitAction(document.forms[0], "deleteEmsMessage.action?paginationList.showCount=" + "false");
                document.forms[0].action = "billEmsMessage.action?paginationList.showCount=" + "false";
            } else {
                alert("请选择交易记录！");
            }
        }


        function signEms() {
            if (checkChkBoxesSelected("selectBillIds")) {
                var billIds = document.Form1.selectBillIds;
                var EmsStatus = document.Form1.selectEmsStatus;
                var canCancel = true;
                if (!isNaN(billIds.length)) {
                    for (var i = 0; i < billIds.length; i++) {
                        if (billIds[i].checked) {
                            if (EmsStatus[i].value == null || EmsStatus[i].value == 3 || EmsStatus[i].value == 2 || EmsStatus[i].value == "") {
                                alert("该票据已签收或未快递，请检查后重新签收！");
                                canCancel = false;
                                break;
                            }
                        }
                    }
                }
                if (canCancel == false) {
                    return false;
                }
                if (!confirm("确定是否签收？")) {
                    return false;
                }
                submitAction(document.forms[0], "editEmsStatus.action?paginationList.showCount=" + "false");
                document.forms[0].action = "billEmsMessage.action?paginationList.showCount=" + "false";
            } else {
                alert("请选择交易记录！");
            }
        }

        // 导出Excel按钮
        function exportEmsMsgtoExcel() {
            submitAction(document.forms[0], "exportEmsMsgtoExcel.action?paginationList.showCount=" + "false");
            document.forms[0].action = "billEmsMessage.action";
        }

        function submitForm() {
            submitAction(document.forms[0], "importEmsMsgtoExcel.action?paginationList.showCount=" + "false");
            document.forms[0].action = "billEmsMessage.action?paginationList.showCount=" + "false";
        }
    </script>
    <style type="text/css">
        .style1 {
            width: 242px;
        }
    </style>
</head>
<body onkeydow="enterkey(event)">
<form name="Form1" method="post" action="billEmsMessage.action?paginationList.showCount=false" id="Form1"
      enctype="multipart/form-data">
    <%
        String currMonth = (String) request.getAttribute("currMonth");
    %>
    <input type="hidden" name="submitFlag" id="submitFlag" value=""/>
    <input type="hidden" name="currMonth" id="currMonth" value="<%=currMonth%>"/>
    <table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
        <tr>
            <td class="centercondition">
                <s:component template="rocketMessage"/>
                <div id="tbl_current_status">
                    <img src="<%=bopTheme%>/themes/images/icons/icon13.png"/>
                    <span class="current_status_menu">当前位置：</span>
                    <span class="current_status_submenu1">销项税管理</span>
                    <span class="current_status_submenu">开票管理</span>
                    <span class="current_status_submenu">发票快递</span>
                </div>
                <div class="widthauto1">
                    <table id="tbl_query" cellpadding="0" cellspacing="0" width="80%" border="0">
                        <tr>
                           <!--  <td style="text-align: right;width: 6%;">投保单号</td> -->
                            <td style="text-align: right;width: 6%;">借据号</td>
                            <td style="text-align: left;width:14%;">
                                <input class="tbl_query_text" type="text" name="billInfo.ttmpRcno"
                                       value="<s:property value='billInfo.ttmpRcno'/>" maxlength="20"/>
                            </td>
                            <!-- <td style="text-align: right;width: 6%;">保单号</td> -->
                            <td style="text-align: right;width: 6%;">合同号</td>
                            <td style="text-align: left;width:14%;">
                                <input class="tbl_query_text" type="text" name="billInfo.cherNum"
                                       value="<s:property value='billInfo.cherNum'/>"  maxlength="20"/>
                            </td>
                            <td style="text-align: right;width: 6%;">
                                开票日期
                            </td>
                            <td style="text-align: left;width:20%;">
                                <input class="tbl_query_time" type="text" name="billBeginDate"
                                       value="<s:property value='billInfo.billBeginDate'/>" id="billBeginDate"
                                       onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'billBeginDate\')}'})"
                                       size='11'/>
                                --
                                <input class="tbl_query_time" type="text" name="billEndDate"
                                       value="<s:property value='billInfo.billEndDate'/>" id="billEndDate"
                                       onfocus="WdatePicker({minDate:'#F{$dp.$D(\'billEndDate\')}'})"
                                       size='11'/>
                            </td>
                            
                        <tr>
                        <tr>
                            <td style="text-align: right;width: 6%;">客户名称</td>
                            <td style="text-align: left;width:14%;">
                                <input type="text" class="tbl_query_text" name="billInfo.customerName"
                                       value="<s:property value='billInfo.customerName'/>" maxlength="100"/>
                            </td>

                            <td style="text-align: right;width: 6%;">费用类型</td>
                            <td style="text-align: left;width:14%;">
                                <s:select name="billInfo.feeTyp" list="feeTypList" listKey="key"
                                    listValue='value' cssClass="tbl_query_text" headerKey=""
                                    headerValue="请选择" />
                            </td>
                          
                            <td style="text-align: right;width: 6%;">
                                到账日期
                            </td>
                            <td style="text-align: left;width:20%;">
                                <input class="tbl_query_time" type="text" name="billInfo.hissBeginDte"
                                       value="<s:property value='billInfo.hissBeginDte'/>" id="hissBeginDte"
                                       onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'hissBeginDte\')}'})"
                                       size='11'/>
                                --
                                <input class="tbl_query_time" type="text" name="billInfo.hissEndDte"
                                       value="<s:property value='billInfo.hissEndDte'/>" id="hissEndDte"
                                       onfocus="WdatePicker({minDate:'#F{$dp.$D(\'hissEndDte\')}'})"
                                       size='11'/>
                            </td>
                            
                        </tr>
                        <tr>
                            <td style="text-align: right;width: 6%;">数据来源</td>
                            <td style="text-align: left;width:14%;">
                                <s:select name="transInfo.dsouRce" list="dsouRceList" listKey="key"
                                    listValue='value' cssClass="tbl_query_text" headerKey=""
                                    headerValue="请选择" />
                            </td>
                            <td style="text-align: right;width: 6%;">发票类型</td>
                            <td style="text-align: left;width: 14%;">
                                <select id="billInfo.fapiaoType" name="billInfo.fapiaoType" style="width:120px">
                                    <option value="0"
                                            <s:if test='billInfo.fapiaoType=="0"'>selected</s:if>
                                            <s:else></s:else>>专
                                    </option>
                                    <option value="1"
                                            <s:if test='billInfo.fapiaoType=="1"'>selected</s:if>
                                            <s:else></s:else>>普
                                    </option>
                                </select>
                            </td>
                            <td style="text-align: right;width: 6%;">开具类型</td>
                            <td style="text-align: left;width: 14%;">
                                <select id="billInfo.issueType" name="billInfo.issueType" style="width:120px">
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
                                </select>
                            </td>
                            <td style="text-align: right;width: 6%;">特殊标记</td>
                            <td style="text-align: left;width:14%;">
                                <input type="text" class="tbl_query_text" name=""
                                       value="<s:property value=''/>" maxlength="100"/>
                            </td>
                        </tr>
                        <tr>
                            <td style="text-align: right;width: 6%;">发票号码</td>
                            <td style="text-align: left;width:14%;">
                                <input type="text" class="tbl_query_text" name="billInfo.billCode"
                                       value="<s:property value='billInfo.billCode'/>" maxlength="100"/>
                            </td>
                            <td style="text-align: right;width: 6%;">发票代码</td>
                            <td style="text-align: left;width:14%;">
                                <input type="text" class="tbl_query_text" name="billInfo.billNo"
                                       value="<s:property value='billInfo.billNo'/>" maxlength="100"/>
                            </td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td>
                                <input type="button" class="tbl_query_button" value="查询"
                                       onMouseMove="this.className='tbl_query_button_on'"
                                       onMouseOut="this.className='tbl_query_button'"
                                       name="BtnView" id="BtnView" onclick="query()"/>
                            </td>
                    </table>
                </div>
                <table id="tbl_tools" width="100%" border="0">
                    <tr>
                        <td align="left" width="200">
                            <a href="#" onClick="printInvoice(this)"><img
                                    src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1021.png"/>打印快递信息</a>
                            <input name=Button onClick=document.all.WebBrowser.ExecWB(6,1) type=button value=打印>
                            <OBJECT classid=CLSID:8856F961_340A_11D0_A96B_00C04FD705A2 height=0 id=WebBrowser width=0></OBJECT>
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
                                <th style="text-align:center">借据号</th>
                                <th style="text-align:center">合同号</th>
                               <!--  <th style="text-align:center">旧保单号</th> -->
                                <th style="text-align:center">开票日期</th>
                                <th style="text-align:center">发票代码</th>
                                <th style="text-align:center">发票号码</th>
                                <th style="text-align:center">客户纳税人名称</th>
                                <th style="text-align:center">收件人</th>
                                <th style="text-align:center">收件人电话</th>
                                <!--		<th style="text-align:center">收件人邮箱</th>-->
                                <th style="text-align:center">收件地址</th>
                                <!--		<th style="text-align:center">详细收件地址</th>-->
                                <th style="text-align:center">快递公司</th>
                                <th style="text-align:center">快递单号</th>
                                <th style="text-align:center">快递状态</th>
                                <th style="text-align:center">寄件人</th>
                                <!-- <th style="text-align:center">渠道</th> -->
                                <th style="text-align:center">特殊标记</th>
                                <th style="text-align:center">费用类型</th>
                                <th style="text-align:center">到账日期</th>
                               <!--  <th style="text-align:center">缴费频率</th> -->
                                <th style="text-align:center">数据来源</th>
                                <th style="text-align:center" colspan="2">操作</th>
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
                                    <input type="hidden" name="selectBillDates" value="<%=bill.getBillDate()%>"/>
                                    <input type="hidden" name="selectEmsNos"
                                           value="<%=bill.getEmsNo()==null?"":bill.getEmsNo()%>"/>
                                    <input type="hidden" name="selectEmsStatus" value="<%=bill.getEmsStatus()%>"/>
                                </td>
                                <td align="center"><%=i + 1%>
                                </td>
                                <td align="center"><%=bill.getTtmpRcno() == null ? "" : bill.getTtmpRcno()%>
                                </td>
                                <td align="center"><%=bill.getCherNum() == null ? "" : bill.getCherNum()%>
                                </td>
                               <%--  <td align="center"><%=bill.getRepNum() == null ? "" : bill.getRepNum()%>
                                </td> --%>
                                <td align="center"><%=bill.getBillDate().length() > 10 ? bill.getBillDate().substring(0, 10) : bill.getBillDate()%>
                                </td>
                                <td align="center"><%=bill.getBillCode()%>
                                </td>
                                <td align="center"><%=bill.getBillNo()%>
                                </td>
                                <td align="center"><%=bill.getCustomerName() == null ? "" : bill.getCustomerName()%>
                                </td>
                                <td align="center"><%=bill.getAddressee() == null ? "" : bill.getAddressee()%>
                                </td>
                                <td align="center"><%=bill.getAddresseePhone() == null ? "" : bill.getAddresseePhone()%>
                                </td>
                                <!--		<td align="center"><%=bill.getCustomerEMail()==null?"":bill.getCustomerEMail()%></td>-->
                                <td align="center"><%=bill.getAddresseeAddress() == null ? "" : bill.getAddresseeAddress()%>
                                </td>
                                <!--		<td align="center"><%=bill.getAddresseeAddressdetail()==null?"":bill.getAddresseeAddressdetail()%></td>-->
                                <td align="center"><%=bill.getFedexExpress() == null ? "" : bill.getFedexExpress()%>
                                </td>
                                <td align="center"><%=bill.getEmsNo() == null ? "" : bill.getEmsNo()%>
                                </td>
                                <td align="center"><%=DataUtil.getEmsStatusCH(bill.getEmsStatus())%>
                                </td>
                                <td align="center"><%=bill.getSender() == null ? "" : bill.getSender()%>
                                </td>
                               <%--  <td align="center"><%=bill.getChanNel() == null ? "" : bill.getChanNelCh()%>
                                </td> --%>
                                <td align="center"><%=bill.getTtmpRcno() == null ? "" : bill.getTtmpRcno()%>
                                </td>
                                <td align="center"><%=bill.getFeeTyp() == null ? "" : bill.getFeeTypCh()%>
                                </td>
                                <td align="center"><%=bill.getHissDte() == null ? "" : bill.getHissDte()%>
                                </td>
                               <%--  <td align="center"><%=bill.getBillFreq() == null ? "" : bill.getBillFreqCh()%>
                                </td> --%>
                                <td align="center"><%=bill.getDsouRce() == null ? "" : bill.getDsouRceCh()%>
                                </td>
                                <td align="center">
                                    <% if (!bill.getEmsStatus().equals("3")) {%>
                                    <a href="editbillEmsMessage.action?curPage=<s:property value='paginationList.currentPage'/>&billId=<%=BeanUtils.getValue(bill,"billId")%>&billNo=<%=BeanUtils.getValue(bill,"billNo")%>">
                                        <img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1005.png"
                                             title="编辑" style="border-width: 0px;"/></a>
                                    <%}%>
                                </td>
                                <td align="center">
                                    <a href="viewbillEmsMessage.action?curPage=<s:property value='paginationList.currentPage'/>&billId=<%=BeanUtils.getValue(bill,"billId")%>&billNo=<%=BeanUtils.getValue(bill,"billNo")%>">
                                        <img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1014.png"
                                             title="明细" style="border-width: 0px;"/></a>
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
                    <table width="100%" cellspacing="0" border="0">
                        <tr><input type="hidden" name="paginationList.showCount" value="false">
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

    function printInvoice(t){
        var batchIds = "";
        var billIds = $('[name = "selectBillIds"]:checkbox:checked');
        for(var i=0;i<billIds.length;i++){
            var batchId = billIds.get(i).value;
            batchIds = batchIds + batchId + (((i + 1)== billIds.length) ? '':',');
        }
        $(t).attr("href","InvoicePrintingBillInfo.action?billId="+batchIds);

    }
</script>
</html>
