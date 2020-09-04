<!-- file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
         import="java.math.BigDecimal" import="com.opensymphony.util.BeanUtils"
         import="com.cjit.common.constant.ScopeConstants"
         import="com.cjit.common.util.NumberUtils"
         import="com.cjit.common.util.PaginationList"
         import="com.cjit.vms.trans.model.TransInfo"
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
            src="<%=bopTheme%>/js/main.js"></script>
    <script language="javascript" type="text/javascript"
            src="<%=bopTheme%>/js/search.js"></script>
    <script type="text/javascript"
            src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
    <script type="text/javascript"
            src="<%=webapp%>/page/js/vms.js"></script>
    <script language="javascript" type="text/javascript"
            src="<c:out value='${webapp}'/>/page/js/window.js" charset="UTF-8"></script>
    <script>
        function LaunchApp() {
            //if (!document.all) {
            //    alert ("This ActiveXObject is only available for Internet Explorer");
            //    return;
            // }
            var ws = new ActiveXObject("WScript.Shell");
            ws.Exec("C:\\WINDOWS\\system32\\notepad.exe");
        }
    </script>
    <script type="text/javascript">

        function toExcel(flag) {
            if (flag) {
                document.forms[0].action = "transMainQueryToExcel.action";
            } else {
                document.forms[0].action = "transMainQueryWhetherToRecoverToExcel.action?type=true";
            }
            document.forms[0].submit();
            document.forms[0].action = "mainQuery.action";
        }

        // [查询]按钮
        function submitform1() {
            submitAction(document.forms[0], "mainQuery.action");
            document.forms[0].action = "mainQuery.action";
        }
        function checkAmt(a) {
            if (fucCheckAmt(a, "") == false) {
                alert("请输入正确的金额");

            }

        }
        function fucCheckAmt(obj, strAlertMsg) {
            strAddress = obj;
            strAddress = strAddress.replace(/^(\s)*|(\s)*$/g, "");//去掉字符串两边的空格
            if (strAddress.substr(0, 1) == '-') {
                strAddress = strAddress.substr(1);

            }
            //匹配规则：
            //只允许以字母开头，用a-z,A-Z,0-9以及下划线组成的email名
            //email后面的域名只允许字母或下划线开头,至少一个.,以字母或下划线结束
            //var newPar=/^[a-zA-Z](\w*)@\w+\.(\w|.)*\w+$/
            var isNum = /^\d+(\.\d+)?$/;
            var tflag = isNaN(strAddress);

            if (strAddress.length > 0 && tflag == true) {
                return false;
            }
            else {
                return true;
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
<form name="Form1" method="post" action="mainQuery.action" id="Form1">
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
                        class="current_status_submenu">交易查询</span>
                </div>
                <div class="widthauto1">
                    <table id="tbl_query" cellpadding="0" cellspacing="0" width="100%"
                           border="0">
                        <tr>
                            <td style="text-align: right;width: 5%;">交易时间</td>
                            <td style="text-align: left;width:18%;"><input
                                    class="tbl_query_time" id="transBeginDate" type="text"
                                    name="transInfo.transBeginDate"
                                    value="<s:property value='transInfo.transBeginDate'/>"
                                    onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'transBeginDate\')}'})"
                                    size='11'/> -- <input class="tbl_query_time"
                                                          id="transEndDate" type="text" name="transInfo.transEndDate"
                                                          value="<s:property value='transInfo.transEndDate'/>"
                                                          onfocus="WdatePicker({minDate:'#F{$dp.$D(\'transEndDate\')}'})"
                                                          size='11'/></td>
                            <td style="text-align: right;width: 5%;">客户名称</td>
                            <td style="text-align: left;width: 11%;">
                                <input type="text"
                                       class="tbl_query_text"
                                       name="transInfo.customerName"
                                       value="<s:property value='transInfo.customerName'/>"/>
                            </td>
                            <td style="text-align: right;width: 6%;">纳税人类型</td>
                            <td style="text-align: left;width: 11%;"><s:select
                                    list="#{'':'全部' ,'S':'小规模纳税人','G':'一般纳税人','I':'个体纳税人','O':'其他'}"
                                    name="transInfo.customerTaxPayerType" label="abc"
                                    listKey="key" listValue="value" cssClass="tbl_query_text"/></td>
                            </td>

                            <td style="text-align: right;width: 5%;">发票类型</td>
                            <td style="text-align: left;width: 11%;"><select
                                    name="transInfo.fapiaoType">
                                <option value=""
                                        <s:if test='transInfo.fapiaoType==""'>selected</s:if>
                                        <s:else></s:else>>全部
                                </option>
                                <option value="0"
                                        <s:if test='transInfo.fapiaoType=="0"'>selected</s:if>
                                        <s:else></s:else>>专
                                </option>
                                <option value="1"
                                        <s:if test='transInfo.fapiaoType=="1"'>selected</s:if>
                                        <s:else></s:else>>普
                                </option>
                            </select></td>


                        </tr>
                        <tr>
                            <td style="text-align: right;width: 5%;">到账日期</td>
                            <td style="text-align: left;width:18%;"><input
                                    class="tbl_query_time" id="hissBeginDte" type="text"
                                    name="transInfo.hissBeginDte"
                                    value="<s:property value='transInfo.hissBeginDte'/>"
                                    onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'hissBeginDte\')}'})"
                                    size='11'/> -- <input class="tbl_query_time" id="hissEndDte"
                                                          type="text" name="transInfo.hissEndDte"
                                                          value="<s:property value='transInfo.hissEndDte'/>"
                                                          onfocus="WdatePicker({minDate:'#F{$dp.$D(\'hissEndDte\')}'})"
                                                          size='11'/></td>
                            <td style="text-align: right;width: 5%;">合同号</td>
                            <td style="text-align: left;width:11%;"><input
                                    class="tbl_query_text" type="text" name="transInfo.cherNum"
                                    value="<s:property value='transInfo.cherNum'/>" maxlength="20"/></td>
                                    
                            <td style="text-align: right;width: 5%;">状态</td>
                            <td style="text-align: left;width:11%;"><s:select
                                    id="transInfo.dataStatus" name="transInfo.dataStatus"
                                    list="transDataStatusList" headerKey="" headerValue="全部"
                                    listKey='value' listValue='text'/></td>
                                    
                            <td style="text-align: right;width: 5%;"></td>
                            <td style="text-align: left;width:11%;">
                               <%--  <input type="text"
                                       class="tbl_query_text"
                                       name="transInfo.transTypeName"
                                       value="<s:property value="transInfo.transTypeName" />"/> --%>
                                       
                           

                        </tr>
                        <tr>
                            <td style="text-align: right;width: 5%;">分公司</td>
                            <td style="text-align: left;width:11%;"><input
                                    class="tbl_query_text" type="text" name=""
                                    value="<s:property value=''/>"/></td>
                                    
                            <td style="text-align: right;width: 5%;"></td>
                            <td style="text-align: left;width:11%;"></td>
                           
                            <!-- Abel:Metlife End -->
                            <td style="width:80px;" align="right">
                                <input type="button" class="tbl_query_button" value="查询"
                                       onMouseMove="this.className='tbl_query_button_on'"
                                       onMouseOut="this.className='tbl_query_button'" name="BtnView"
                                       id="BtnView" onclick="submitform1()"/>
                            </td>
                            <td style="width:80px;" align="right"></td>
                        </tr>
                    </table>
                    <%--<table id="tbl_tools" width="100%" border="0">
    <tr>
        <td align="left">

        </td>
    </tr>
    </table>
    --%>
                </div>

                <table id="tbl_tools" width="100%" border="0">
                    <tr>
                        <td align="left"><a href="#" onclick="toExcel(true)"><img
                                src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png"/>导出</a>
                            <a href="#" onclick="toExcel(false)"><img
                                    src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png"/>导出是否收回发票差异报表</a>
                        </td>
                    </tr>
                </table>
                <div id="lessGridList4" style="overflow: auto; width: 100%;">
                    <div id="rDiv" style="width: 0px;height: auto;">
                        <table class="lessGrid" cellspacing="0" rules="all" border="0"
                               cellpadding="0" style="border-collapse: collapse; width: 100%;">
                            <tr class="lessGrid head">
                                <th style="text-align:center"><input
                                        style="width:13px;height:13px;" id="CheckAll" type="checkbox"
                                        onclick="cbxselectall(this,'selectTransIds')"/></th>
                                <th style="text-align: center">序号</th>
                                <th style="text-align: center">交易时间</th>
                                <th style="text-align: center">借据号</th>
                                <th style="text-align: center">合同号</th>
                                <!-- <th style="text-align: center">保全受理号</th> -->
                                <th style="text-align: center">客户名称</th>
                                <%--<th style="text-align: center">纳税人类型</th>--%>
                               <!--  <th style="text-align: center">险种名称</th> -->
                                <th style="text-align: center">交易金额</th>
                                <th style="text-align: center">税率</th>
                                <th style="text-align: center">税额</th>
                                <th style="text-align: center">价税合计</th>
                                <th style="text-align: center">未开票金额</th>
                                <th style="text-align: center">发票类型</th>
                                <th style="text-align: center">是否打票</th>
                                <th style="text-align: center">交易标志</th>
                                <th style="text-align: center">状态</th>
                                <!-- <th style="text-align: center">渠道</th> -->
                                <th style="text-align: center">费用类型</th>
                                <!-- <th style="text-align: center">保单年度</th> -->
                                <th style="text-align: center">期数</th>
                                <th style="text-align: center">到账日期</th>
                               <!--  <th style="text-align: center">交费频率</th> -->
                                <th style="text-align: center">明细</th>
                            </tr>

                            <%
                                PaginationList paginationList = (PaginationList) request
                                        .getAttribute("paginationList");
                                if (paginationList != null) {
                                    List transInfoList = paginationList.getRecordList();
                                    if (transInfoList != null && transInfoList.size() > 0) {
                                        for (int i = 0; i < transInfoList.size(); i++) {
                                            TransInfo trans = (TransInfo) transInfoList.get(i);
                                            if (i % 2 == 0) {
                            %>
                            <tr class="lessGrid rowA">
                                    <%
									} else {
								%>

                            <tr class="lessGrid rowB">
                                <%
                                    }
                                    String canCheck = "";
                                    if (DataUtil.TRANS_STATUS_2.equals(trans
                                            .getDataStatus())
                                            || trans.getBalance().compareTo(
                                            new BigDecimal(0)) == 0) {
                                        canCheck = "disabled";
                                    }
                                %>
                                <td align="center"><input style="width:13px;height:13px;"
                                                          type="checkbox" name="selectTransIds"
                                                          value="<%=BeanUtils.getValue(trans, "transId")%>"/> <%--<input type="hidden" name="selectCustomers" value="<%=trans.getCustomerAccount()%>" />
			<input type="hidden" name="selectTransTypes" value="<%=trans.getTransType()%>" /> 
		--%>
                                </td>
                                <td align="center"><%=i + 1%>
                                </td>
                                <td align="center"><%=DataUtil.getDateFormat(trans.getTransDate())%>
                                </td>
                                <td align="center"><%=trans.getTtmpRcno() == null ? "" : trans
                                        .getTtmpRcno()%>
                                </td>
                                <td align="center"><%=trans.getCherNum() == null ? "" : trans
                                        .getCherNum()%>
                                </td>
                               <%--  <td align="center"><%=trans.getRepNum() == null ? "" : trans
                                        .getRepNum()%>
                                </td> --%>
                                <td align="left"><%=trans.getCustomerName()%>
                                </td>
                               <%--  <td align="center"><%=trans.getTransType() == null ? "" : trans
                                        .getTransType()%>
                                </td> --%>
                                <td align="right"><%=NumberUtils.format(trans.getAmt(), "", 2)%>
                                </td>
                                <td align="right"><%=NumberUtils.format(trans.getTaxRate(), "", 4)%>
                                </td>
                                <td align="right"><%=NumberUtils.format(trans.getTaxAmt(), "", 2)%>
                                </td>
                                <td align="right"><%=NumberUtils.format(trans.getAmt(), "", 2)%>
                                </td>                        
                                <td align="right"><%=NumberUtils.format(trans.getBalance(), "", 2)%>
                                </td>
                                <td align="left"><%=DataUtil.getFapiaoTypeCH(trans
                                        .getFapiaoType())%>
                                </td>
                                <td align="left"><%=DataUtil.getFapiaoFlagCH(trans
                                        .getTransFapiaoFlag())%>
                                </td>
                                <td align="left"><%=DataUtil.getTransFlagName(trans
                                        .getTransFlag())%>
                                </td>
                                <td align="left"><%=DataUtil.getDataStatusCH(
                                        trans.getDataStatus(), "TRANS")%>
                                </td>
                                <%-- <td align="center"><%=trans.getChanNelCh() == null ? "" : trans
                                        .getChanNelCh()%>
                                </td> --%>
                                <td align="center"><%=trans.getFeeTypCh() == null ? "" : trans
                                        .getFeeTypCh()%>
                                </td>
                                <%-- <td align="center"><%=trans.getPolYear() == null ? "" : trans
                                        .getPolYear()%>
                                </td> --%>
                                <td align="center"><%=trans.getPremTerm() == null ? "" : trans
                                        .getPremTerm()%>
                                </td>
                                <td align="center"><%=trans.getHissDte() == null ? "" : trans
                                        .getHissDte()%>
                                </td>
                                <%-- <td align="center"><%=trans.getBillFreqCh() == null ? "" : trans
                                        .getBillFreqCh()%>
                                </td> --%>
                                <td align="center"><a href="javascript:void(0)"
                                                      onClick="OpenModalWindow('<c:out
                                                              value="${webapp}"/>/queryBill.action?transId=<%=trans.getTransId()%>', 1500,609,true) ">
                                    <img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1014.png"
                                            title="查看" style="border-width: 0px;"/>
                                </a></td>
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
