<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
         import="java.math.BigDecimal"
         import="com.opensymphony.util.BeanUtils"
         import="com.cjit.common.constant.ScopeConstants"
         import="com.cjit.common.util.NumberUtils"
         import="com.cjit.common.util.PaginationList"
         import="com.cjit.vms.filem.util.FileUtil"
         import="com.cjit.vms.trans.model.BillInfo"
         import="com.cjit.vms.trans.util.DataUtil"
         contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="../../../page/include.jsp" %>
<OBJECT id='DocCenterCltObj' width=100 height=100 classid="clsid:1E25664C-EA53-4F39-8575-684737626D6F"
        style="display: none;"></OBJECT>
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
    <script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/window.js"></script>
    <script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
    <script type="text/javascript" src="<%=webapp%>/page/js/vms.js"></script>
    <!-- s税控服务器js -->
    <script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/taxSelver/validatorTax.js"></script>
    <script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/taxSelver/PrintMargin.js"></script>
    <script type="text/javascript">
        var faPiaoType = 0;
        // [查询]按钮
        function submitQuery() {
            submitAction(document.forms[0], "listBillPrint.action?paginationList.showCount=" + "false");
            document.forms[0].action = "listBillPrint.action?paginationList.showCount=" + "false";
        }
        // [打印发票]按钮
        function print() {
            if (checkChkBoxesSelected("selectBillIds")) {
                submitAction(document.forms[0], "printBill.action");
                document.getElementById('BtnView').disabled = true;
            } else {
                alert("请选择发票记录!");
            }
        }

        function printNew() {

            if (!checkChkBoxesSelected("selectBillIds")) {
                alert("请选择票据记录");
                return false;
            }
            checkDiskAndSelver(printNewDisk(), "")
        }
        /** 税控服务器版****/
        function printNewSelver() {
            try {
                var printLimitValue = document.getElementById("printLimitValue").value;
                var faPiaoType = document.getElementById("billInfo.fapiaoType").value;
                var fileName = "发票打印.xml";
                var billIds = document.getElementsByName("selectBillIds");
                var ids = "";
                var selectNum = 0;
                for (var i = 0; i < billIds.length; i++) {
                    if (billIds[i].checked) {
                        ids = ids === "" ? billIds[i].value : ids + "," + billIds[i].value;
                        selectNum++;
                    }
                }
                showOcxSelver(ids, faPiaoType, printLimitValue, fileName)
            } catch (e) {

                alert(e.message + ",errno:" + e.number);


            }
        }
        function printNewDisk() {
            var printLimitValue = document.getElementById("printLimitValue").value;
            var faPiaoType = document.getElementById("billInfo.fapiaoType").value;

            if (checkChkBoxesSelected("selectBillIds")) {
                try {
                    var taxDiskNo = DocCenterCltObj.FunGetPara('', 'taxDiskNo');
                    // alert(taxDiskNo);
                    var arr = new Array();
                    arr = taxDiskNo.split("|");
                    if (arr[0] == 0) {
                        alert("请连接税控盘");
                        return false;
                    }
                    taxDiskNo = arr[1];
                    //alert(taxDiskNo);
                    //return false;

                    var billIds = document.getElementsByName("selectBillIds");
                    var ids = "";
                    var selectNum = 0;
                    for (var i = 0; i < billIds.length; i++) {
                        if (billIds[i].checked) {
                            ids = ids === "" ? billIds[i].value : ids + "," + billIds[i].value;
                            selectNum++;
                        }
                    }
                    $.ajax({
                        async: false,
                        type: "POST",
                        cache: false,
                        url: "showLock.action",
                        data: {taxDiskNo: taxDiskNo, fapiaoType: faPiaoType},
                        dataType: "text",
                        error: function () {
                            return false;
                        },
                        success: function (result) {
                            if (result == '') {
                                alert("查询信息失败。");
                                return false;
                            } else if (result == 'lock') {
                                alert("不能同时操作数据。");
                                return false;
                            } else if (result == 'registeredInfoError') {
                                alert("获取税控盘注册码失败。");
                                return false;
                            } else if (result == 'taxPwdError') {
                                alert("获取税控盘口令和证书口令失败。");
                                return false;
                            } else {

                                showOcxString(result, ids, faPiaoType, printLimitValue);
                            }
                        }
                    });
                    document.forms[0].action = "listBillPrint.action?paginationList.showCount=" + "false";
                } catch (e) {
                    alert("请安装税控盘插件！");
                }
            } else {
                alert("请选择票据记录！");
            }
        }
        function showOcxString(result1, ids, faPiaoType, printLimitValue) {
            $.ajax({
                type: "POST",
                async: false,
                cache: false,
                url: 'showOCXstring.action',
                data: {billIds: ids, faPiaoType: faPiaoType, printLimitValue: printLimitValue},
                dataType: 'html',
                success: function (result) {
                    if (result == 'printValueError') {
                        alert("单次打印超过限定的值" + printLimitValue + "请重新输入");
                        return false;
                    }
                    var arr = new Array();
                    arr = result.split("+");
                    var ocxString = arr[1];
                    var billNo = arr[2];
                    var sum = arr[3];
                    //alert(ocxString);
                    if (arr[0] == 0) {
                        alert(arr[1]);
                        return false;
                    }
                    //alert(arr[0])
                    //return false;
                    if (confirm(billNo + "\n是否打印这些发票?")) {
                        //ocxString=result1+'|'+sum+'|'+ocxString;
                        ///alert(ocxString);
                        //var statas=DocCenterCltObj.FunGetPara(ocxString,'printInvoice');
                        //alert(statas);
                        updateBillStatus(result1, sum, ocxString);
                    }
                },
                error: function () {
                    alert("获取组装传进OCX的字符串过程出现异常!");
                }
            });
            document.forms[0].action = "listBillPrint.action?paginationList.showCount=" + "false";
        }
        function updateBillStatus(result1, sum, ocxString) {
            //alert(sum);
            var arr = new Array();
            arr = ocxString.split("|");
            var j = 0;
            for (var i = 0; i < parseInt(sum); i++) {
                ocxString1 = arr[i];
                ocxString1 = result1 + '|' + 1 + "|" + ocxString1;
                var statas = DocCenterCltObj.FunGetPara(ocxString1, 'printInvoice');
                // alert(statas);
                j++;
                var arr1 = new Array();
                arr1 = statas.split("|");
                if (arr1[0] == 0) {
                    var arr2 = new Array();
                    arr2 = arr[1].split("^");
                    alert(arr2[3]);
                    break;
                }
            }
            alert("打印成功" + j + "条");
            if (j == 0) {
                return false;
            }
            $.ajax({
                type: "POST",
                async: false,
                cache: false,
                url: 'updatePrintResult.action',
                data: {resultOCX: ocxString, num: j},
                dataType: "html",
                success: function (result) {

                    // clearInterval(intervalID);
                    //alert(result);
                    submitAction(document.forms[0], "listBillPrint.action?paginationList.showCount=" + "false");
                    document.forms[0].action = "listBillPrint.action?paginationList.showCount=" + "false";

                },
                error: function () {
                    alert("根据OCX返回结果更新发票状态过程出现异常!");
                }
            });
            document.forms[0].action = "listBillPrint.action?paginationList.showCount=" + "false";
        }
        // 增值税专用发票    货物运输业增值税专用发票  机动车销售统一发票  XML导出
        function billsToXml(obj) {
            var findz = document.getElementById('findz').value;
            var findp = document.getElementById('findp').value;
            if (obj == "1" && findz == "1") {
                submitAction(document.forms[0], "billsToXml.action?billType=" + obj);
                document.forms[0].action = "listBillPrint.action";
            } else if (obj == "4" && findp == "1") {
                submitAction(document.forms[0], "billsToXml.action?billType=" + obj);
                document.forms[0].action = "listBillPrint.action";
            } else {
                alert("无记录!");
            }
        }
        function toExcel() {
            submitAction(document.forms[0], "xxsToExcel.action");
            document.forms[0].action = "listBillPrint.action";
        }

        function toExcelNew() {
            submitAction(document.forms[0], "xxsToExcelNew.action");
            document.forms[0].action = "listBillPrint.action";
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

        }

        function queryYupiaoTest() {
            //	a)	操作代码：surplusInvoice
//b)	输入：注册码|税控盘口令|证书口令|发票类型-0（专）1（普）
            //taxDiskNo;taxDiskPas;certificatePas;faPiaoType;
            var taxDiskNo = DocCenterCltObj.FunGetPara('', 'taxDiskNo');
            //	alert(taxDiskNo);
            var faPiaoType = document.getElementById("billInfo.fapiaoType").value;
            $.ajax({
                async: false,
                type: "POST",
                cache: false,
                url: "getInvoiceNum.action",
                data: {taxDiskNo: taxDiskNo, fapiaoType: faPiaoType},
                dataType: "text",
                error: function () {
                    return false;
                },
                success: function (result) {
                    if (result == '') {
                        alert("查询信息失败。");
                        return false;
                    } else if (result == 'lock') {
                        alert("不能同时操作数据。");
                        return false;
                    } else if (result == 'registeredInfoError') {
                        alert("获取税控盘注册码失败。");
                        return false;
                    } else if (result == 'taxPwdError') {
                        alert("获取税控盘口令和证书口令失败。");
                        return false;
                    } else {
                        alert(result);
                        var statas = DocCenterCltObj.FunGetPara(result, 'surplusInvoice');
                        //alert(statas);

                    }
                }
            });
            document.forms[0].action = "listBillPrint.action?paginationList.showCount=" + "false";


        }


        function monitorData() {
            //纳税人识别号|税控盘号|税控盘口令|发票类盘型(0:专,1:普)
            var taxNo = '500102010003760';
            var inputdata = taxNo + '|' + '499000135701' + '|' + taxDiskPas + '|' + 0;
            alert(inputdata);
            var statas = DocCenterCltObj.FunGetPara(inputdata, 'monitorDataQuery');
            alert(statas);
        }//500102010003760|499000135701|88888888|0
        function taxDiskinfo() {
            var inputdata = '88888888';
            var statas = DocCenterCltObj.FunGetPara(inputdata, 'DataQuery');
            alert(statas);


        }
        function taxItems() {

            //	b)	输入：纳税人识别号|税控盘号|税控盘口令|发票类型 b)	输入：纳税人识别号|税控盘号|税控盘口令|发票类型
            var taxNo = '500102010003760';
            var inputdata = taxNo + '|' + '499000135701' + '|' + taxDiskPas + '|' + faPiaoType;
            alert(inputdata);
            var statas = DocCenterCltObj.FunGetPara(inputdata, 'taxesQuery');
            alert(statas);
            //c)	输出：发票类型|税种税目索引号^税种税目代码^税率^税种名称^税目名称^|
        }
        function buyInvoice() {// 纳税人识别号|税控盘号|税盘口令|发票类型
            //uyInvoiceMsgb)	输入：注册码|税控盘口令|证书口令|发票类型-0（专）1（普）
            var taxDiskNo = DocCenterCltObj.FunGetPara('', 'taxDiskNo');
            alert(taxDiskNo);
            var faPiaoType = document.getElementById("billInfo.fapiaoType").value;
            $.ajax({
                async: false,
                type: "POST",
                cache: false,
                url: "getInvoiceNum.action",
                data: {taxDiskNo: taxDiskNo, fapiaoType: faPiaoType},
                dataType: "text",
                error: function () {
                    return false;
                },
                success: function (result) {
                    if (result == '') {
                        alert("查询信息失败。");
                        return false;
                    } else if (result == 'lock') {
                        alert("不能同时操作数据。");
                        return false;
                    } else if (result == 'registeredInfoError') {
                        alert("获取税控盘注册码失败。");
                        return false;
                    } else if (result == 'taxPwdError') {
                        alert("获取税控盘口令和证书口令失败。");
                        return false;
                    } else {
                        alert(result);
                        var statas = DocCenterCltObj.FunGetPara(result, 'buyInvoiceMsg');
                        alert(statas);

                    }
                }
            });
            document.forms[0].action = "listBillPrint.action?paginationList.showCount=" + "false";

            //	b)	输入：纳税人识别号|税控盘号|税盘口令|发票类型
//c)	输出：发票类型|当前发票代码|当前发票号码|总剩余份数|发票代码^发票起始号码^发票终止号码^发票领购份数^剩余份数^领购日期^领购人员^|

        }
        function configPrint() {
            var topMar = '3';
            var leftMar = '-8';
            var inputdata = faPiaoType + '|' + topMar + '|' + leftMar;
            var statas = DocCenterCltObj.FunGetPara(inputdata, 'configPrint');
            alert(statas);
        }

    </script>
    <style type="text/css">
        .style1 {
            width: 242px;
        }
    </style>
</head>
<body onkeydow="enterkey(event)">
<form name="Form1" method="post" id="Form1">
    <input type="hidden" name="taxParam" id="taxParam" value="<s:property value='taxParam'/>"/>
    <%
        String currMonth = (String) request.getAttribute("currMonth");
        String projectCode = (String) request.getAttribute("projectCode");
        String SKPXXCX = FileUtil.createXmlStringForSKPXXCX(FileUtil.skpkl);
        String res = "";
        System.out.println("SKPXXCX:");
        System.out.println(SKPXXCX);

        String findz = (String) request.getAttribute("zbillInfo");
        String findp = (String) request.getAttribute("pbillInfo");
    %>
    <input type="hidden" id="printLimitValue" name="printLimitValue" value="<s:property value="printLimitValue"/>">
    <input type="hidden" name="currMonth" id="currMonth" value="<%=currMonth%>"/>
    <input type="hidden" name="reasionInfo" id="reasionInfo" value=""/>
    <input type="hidden" name=findz id="findz" value="<%=findz%>"/>
    <input type="hidden" name="findp" id="findp" value="<%=findp%>"/>
    <table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
        <tr>
            <td class="centercondition">
                <s:component template="rocketMessage"/>
                <div id="tbl_current_status">
                    <img src="<%=bopTheme%>/themes/images/icons/icon13.png"/>
                    <span class="current_status_menu">当前位置：</span>
                    <span class="current_status_submenu1">销项税管理</span>
                    <span class="current_status_submenu">发票管理</span>
                    <span class="current_status_submenu">发票打印</span>
                </div>
                <div class="widthauto1">
                    <table id="tbl_query" cellpadding="0" cellspacing="0" width="80%" border="0">
                        <tr>
                            <td style="text-align: right;width: 6%;">投保单号</td>
                            <td style="text-align: left;width:14%;">
                                <input class="tbl_query_text" type="text" name="ttmpRcno"
                                       value="<s:property value='billInfo.ttmpRcno'/>" maxlength="20"/>
                            </td>
                            <td style="text-align: right;width: 6%;">保单号</td>
                            <td style="text-align: left;width:14%;">
                                <input class="tbl_query_text" type="text" name="cherNum"
                                       value="<s:property value='billInfo.cherNum'/>"  maxlength="20"/>
                            </td>
                            <td style="text-align: right;width: 6%;">
                                开票日期
                            </td>
                            <td style="text-align: left;width:20%;">
                                <input class="tbl_query_time" type="text" name="billInfo.billBeginDate"
                                       value="<s:property value='billInfo.billBeginDate'/>" id="billEndDate"
                                       onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'billEndDate\')}'})"
                                       size='11'/>
                                --
                                <input class="tbl_query_time" type="text" name="billInfo.billEndDate"
                                       value="<s:property value='billInfo.billEndDate'/>" id="billBeginDate"
                                       onfocus="WdatePicker({minDate:'#F{$dp.$D(\'billBeginDate\')}'})"
                                       size='11'/>
                            </td>
                            <td style="text-align: right;width: 6%;">渠道</td>
                            <td style="text-align: left;width:14%;">
                                <s:select name="billInfo.chanNel" list="chanNelList" listKey="key"
                                          listValue='value' cssClass="tbl_query_text" headerKey=""
                                          headerValue="请选择"/>
                                <%--<input class="tbl_query_text" type="text" name="chanNel"--%>
                                <%--value="<s:property value='billInfo.chanNel'/>"/>--%>
                            </td>
                        <tr>
                        <tr>
                            <td style="text-align: right;width: 6%;">客户名称</td>
                            <td style="text-align: left;width:14%;">
                                <input type="text" class="tbl_query_text" name="billInfo.customerName"
                                       value="<s:property value='billInfo.customerName'/>" maxlength="100"/>
                            </td>
                            <td style="text-align: right;width: 6%;">缴费频率</td>
                            <td style="text-align: left;width:14%;">
                                <s:select name="billInfo.billFreq" list="billFreqlList" listKey="key"
                                          listValue='value' cssClass="tbl_query_text" headerKey=""
                                          headerValue="请选择"/>
                                <%--<input class="tbl_query_text" type="text" name="billFreq"--%>
                                <%--value="<s:property value='billInfo.billFreq'/>"/>--%>
                            </td>
                            <td style="text-align: right;width: 6%;">
                                承保日期
                            </td>
                            <td style="text-align: left;width:20%;">
                                <input class="tbl_query_time" type="text" name="hissBeginDte"
                                       value="<s:property value='billInfo.hissBeginDte'/>" id="hissBeginDte"
                                       onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'hissBeginDte\')}'})"
                                       size='11'/>
                                --
                                <input class="tbl_query_time" type="text" name="hissEndDte"
                                       value="<s:property value='billInfo.hissEndDte'/>" id="hissEndDte"
                                       onfocus="WdatePicker({minDate:'#F{$dp.$D(\'hissEndDte\')}'})"
                                       size='11'/>
                            </td>
                            <td style="text-align: right;width: 6%;">费用类型</td>
                            <td style="text-align: left;width:14%;">
                                <s:select name="billInfo.feeTyp" list="feeTypList" listKey="key"
                                          listValue='value' cssClass="tbl_query_text" headerKey=""
                                          headerValue="请选择"/>
                            </td>
                        </tr>
                        <tr>
                            <td style="text-align: right;width: 6%;">数据来源</td>
                            <td style="text-align: left;width:14%;">
                                <s:select name="billInfo.dsouRce" list="dsouRceList" listKey="key"
                                          listValue='value' cssClass="tbl_query_text" headerKey=""
                                          headerValue="请选择"/>
                                <%--<input class="tbl_query_text" type="text" name="dsouRce"--%>
                                <%--value="<s:property value='billInfo.dsouRce'/>"/>--%>
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
                            <td style="text-align: right;width: 6%;">发票号码</td>
                            <td style="text-align: left;width:14%;">
                                <input type="text" class="tbl_query_text" name="billNo"
                                       value="<s:property value='billInfo.billNo'/>" maxlength="100"/>
                            </td>
                        </tr>
                        <tr>
                            <td style="text-align: right;width: 6%;">发票代码</td>
                            <td style="text-align: left;width:14%;">
                                <input type="text" class="tbl_query_text" name="billCode"
                                       value="<s:property value='billInfo.billCode'/>" maxlength="100"/>
                            </td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td style="text-align: left;width:14%;">
                                <input type="button" class="tbl_query_button" value="查询"
                                       onMouseMove="this.className='tbl_query_button_on'"
                                       onMouseOut="this.className='tbl_query_button'"
                                       name="BtnView" id="BtnView" onclick="submitQuery()"/>
                            </td>
                        </tr>
                    </table>
                </div>
                <table id="tbl_tools" width="100%" border="0">
                    <tr>
                        <td align="left">
                            <a href="#" name="BtnView" id="BtnView" onclick="printNew()"><img
                                    src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1021.png"/>打印发票</a>
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
                                <th style="text-align:center">旧保单号</th>
                                <th style="text-align:center">开票日期</th>
                                <th style="text-align:center">客户名称</th>
                                <th style="text-align:center">客户纳税人识别号</th>
                                <th style="text-align:center">发票代码</th>
                                <th style="text-align:center">发票号码</th>
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
                                <th style="text-align:center">操作</th>
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
                                    String billAuditStatus = "";
                                %>
                                <td align="center">
                                    <input style="width:13px;height:13px;" type="checkbox" id="selectBillIds"
                                           name="selectBillIds" value="<%=BeanUtils.getValue(bill,"billId")%>"/>
                                    <input type="hidden" name="selectBillDates" value="<%=bill.getBillDate()%>"/>
                                    <input type="hidden" name="billId" value="<%=bill.getBillId()%>"/>
                                </td>
                                <td align="left"><%=i + 1%>
                                </td>
                                <td align="center"><%=bill.getTtmpRcno() == null ? "" : bill.getTtmpRcno()%>
                                </td>
                                <td align="center"><%=bill.getCherNum() == null ? "" : bill.getCherNum()%>
                                </td>
                                <td align="center"><%=bill.getRepNum() == null ? "" : bill.getRepNum()%>
                                <td align="left"><%=bill.getBillDate()%>
                                </td>
                                <td align="left"><%=bill.getCustomerName()%>
                                </td>
                                <td align="left"><%=bill.getCustomerTaxno()%>
                                </td>
                                <td align="left"><%=bill.getBillCode()%>
                                </td>
                                <td align="left"><%=bill.getBillNo()%>
                                </td>
                                <td align="left"><%=NumberUtils.format(bill.getAmtSum(), "", 2)%>
                                </td>
                                <td align="left"><%=NumberUtils.format(bill.getTaxAmtSum(), "", 2)%>
                                </td>
                                <td align="left"><%=NumberUtils.format(bill.getSumAmt(), "", 2)%>
                                </td>
                                <td align="left"><%=(bill.getIsHandiwork() == null) ? "" : bill.getIsHandiwork().equals("1") ? "自动开票" : bill.getIsHandiwork().equals("2") ? "人工审核" : "人工开票"%>
                                </td>
                                <td align="left"><%=(bill.getIssueType() == null) ? "" : bill.getIssueType().equals("1") ? "单笔" : bill.getIssueType().equals("2") ? "合并" : "拆分"%>
                                </td>
                                <td align="left"><%=(bill.getFapiaoType() == null) ? "" : bill.getFapiaoType().equals("0") ? "增值税专用发票" : "增值税普通发票"%>
                                </td>
                                <td align="left"><%=(bill.getDataStatus() == null) ? "" : bill.getDataStatus().equals("5") ? "已开具" : bill.getDataStatus().equals("6") ? "已开具" : "打印失败"%>
                                </td>
                                <td><%=bill.getChanNel() == null ? "" : bill.getChanNel()%>
                                </td>
                                <td><%=bill.getFeeTyp() == null ? "" : bill.getFeeTyp()%>
                                </td>
                                <td><%=bill.getHissDte() == null ? "" : bill.getHissDte()%>
                                </td>
                                <td><%=bill.getBillFreq() == null ? "" : bill.getBillFreq()%>
                                </td>
                                <td><%=bill.getDsouRce() == null ? "" : bill.getDsouRce()%>

                                <td align="center">
                                    <a href='<%=webapp%>/billEditToTrans.action?billId=<%=bill.getBillId()%>'>
                                        <img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1014.png"
                                             title="查看"
                                             style="border-width: 0px;"/></a>
                                    <a href="javascript:void(0);"
                                       onClick="OpenModalWindowSubmit('viewImgFromBillPrint.action?billId=<%=bill.getBillId()%>',1000,650,true)">
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
</html>