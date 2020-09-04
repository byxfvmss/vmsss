<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2016/2/18
  Time: 13:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ include file="/page/include.jsp" %>
<base target="_self">
<html>
<head>
    <title>开具文件处理</title>
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
    <script language="javascript" type="text/javascript"
            src="<%=webapp%>/page/js/taxSelver/compareCurrentBill.js"></script>
    <script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/taxSelver/billCancel.js"></script>
</head>
<%--inmputMatchInfoList.action--%>
<body>
<form name="Form1" method="post" action="inmputMatchInfoList.action?flag=false" id="Form1"
      enctype="multipart/form-data">
    <input type="hidden" name="billId" id="billId" value="<s:property value="billId" />">
    <table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
        <tr>
            <td class="centercondition">
                <div id="tbl_current_status">
                    <img src="<%=bopTheme%>/themes/images/icons/icon13.png"/>
                    <span class="current_status_menu">当前位置：</span>
                    <span class="current_status_submenu1">销项税管理</span>
                    <span class="current_status_submenu">开票管理</span>
                    <span class="current_status_submenu">发票开具</span>
                    <span class="current_status_submenu">开具文件处理</span>
                </div>
                </br>
                <div class="widthauto1">
                    <table id="tbl_query" cellpadding="0" cellspacing="0" width="100%" border="0">
                        <tr>
                            <td colspan="2" align="left" width="80" class="pleft15p">
                                <s:file name="attachment" accept="application/msexcel" size="30"
                                        style="height:26px;" ></s:file>
                            </td>
                            <td align="left">
                                <input type="button" value="导入开票文件" onclick="submitForms()"/>
                            </td>

                        </tr>
                        <tr>
                            <td style="text-align: right;">
                                导入文件日期:
                            </td>
                            <td>
                                <input class="tbl_query_time" id="createBeginTime" type="text"
                                       name="beginDate"
                                       value="<s:property value='billInfo.applyBeginDate'/>"
                                       onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'createEndTime\')}'})"
                                       size='11'/>
                                --
                                <input class="tbl_query_time" id="createEndTime" type="text"
                                       name="endDate"
                                       value="<s:property value='billInfo.applyEndDate'/>"
                                       onfocus="WdatePicker({minDate:'#F{$dp.$D(\'createBeginTime\')}'})"
                                       size='11'/>
                            </td>
                            <td>
                                <input type="button" onclick="exportMatchInfoListClick()" value="下载未匹配保单号"/>
                            </td>
                        </tr>
                    </table>
                </div>
                <div id="lessGridList5" style="overflow: auto; width: 100%;">
                    <table class="lessGrid" cellspacing="0" rules="all" border="0" cellpadding="0"
                           style="border-collapse: collapse; width: 100%;">
                        <tr class="lessGrid head">
                            <th style="text-align: center">投保单号</th>
                            <th style="text-align: center">项目名称</th>
                            <th style="text-align: center">分公司</th>
                        </tr>
                        <s:if test="matchInfoList!=null||matchInfoList.size>0">
                            <s:iterator value="matchInfoList" id="iList" status="stuts">
                                <tr>
                                    <td><s:property value="#iList.ttmPrcno"/></td>
                                    <td><s:property value="#iList.chanNelName"/></td>
                                    <td><s:property value="#iList.braNch"/></td>
                                </tr>
                            </s:iterator>
                        </s:if>
                    </table>
                </div>
                <table id="tbl_toolss" width="100%" border="0">
                    <tr>
                        <td align="left">
                            <input type="button" class="tbl_query_button" value="开具"
                                   onMouseMove="this.className='tbl_query_button_on'"
                                   onMouseOut="this.className='tbl_query_button'"
                                   name="btWriteOff" id="btWriteOff"
                                   onclick="parentWindow()"/>
                            <input type="button" class="tbl_query_button" value="返回"
                                   onMouseMove="this.className='tbl_query_button_on'"
                                   onMouseOut="this.className='tbl_query_button'"
                                   name="btCancel" id="btCancel" onclick="CloseWindow();"/>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</form>
</body>
<script type="text/javascript">
    function submitForms() {
        document.getElementById("Form1").submit();
    }
    function exportMatchInfoListClick() {
    	$("base").attr("target","");
        document.getElementById("Form1").action = "exportMatchInfoList.action";
        document.getElementById("Form1").submit();
        document.getElementById("Form1").action = "inmputMatchInfoList.action?flag=false";
    	$("base").attr("target","_self");
    }
    function parentWindow() {
        var billId = $("#billId").val();
        if (billId != null && billId != "") {
            CloseWindow();
            if(typeof(eval(window.dialogArguments.BillIssueSelvlet)) == "function"){
            	window.dialogArguments.BillIssueSelvlet(billId,true);
            }else if(typeof(eval(window.dialogArguments.issueBillDisk)) == "function"){
	            window.dialogArguments.issueBillDisk(billId,true);
            }
        }
    }
</script>
</html>
