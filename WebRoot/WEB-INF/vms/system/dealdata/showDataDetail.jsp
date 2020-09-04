<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <%@ include file="../../../../page/include.jsp" %>
    <link href="<%=bopTheme2%>/css/main.css" type="text/css"
          rel="stylesheet">
    <script type="text/javascript"
            src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript"
            src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
    <script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
    <script language="javascript" type="text/javascript"
            src="<%=bopTheme%>/js/main.js"></script>
    <script language="javascript" type="text/javascript"
            src="<%=webapp%>/page/js/ylb.js"></script>
    <script language="javascript" type="text/javascript"
            src="<%=webapp%>/page/js/lhb.js"></script>
    <script language="javascript" type="text/javascript"
            src="<%=bopTheme%>/js/search.js"></script>
    <meta http-equiv="Pragma" content="no-cache"/>
    <title>数据导入</title>
    <script type="text/javascript">
        function submitForm(actionUrl) {
            var form = document.getElementById("main");
            form.action = actionUrl;
            form.submit();
        }

        function showMessage(transId) {
            alert($('#message_' + transId).val());
        }
        function showColor(status) {
            if(status=="0"){
                return;
            }
            var dStatus = $("[name=dStatus]");
            var dataList = $("[name=detailList]");
            var rowNum = dataList.size();
            for (var i = 0; i < rowNum; i++) {
                var statusObj = dStatus[i];
                var statusList = $(statusObj).val();
                var row = dataList[i];
                var column = $(row).find("td[name=data]").size();
                for (var j = 1; j < column; j++) {
                    var status = statusList.substr(j, 1);
                    if (j == 1) {
                        $(row).css("background", "#FFFF37");
                        break;
                    } else {
                        if ("1" == status) {
                            var td = $(row).find("td[name=data]")[j];
                            $(td).css("background", "#FFFF37");
                        }
                    }
                }
            }
        }
    </script>
</head>
<body onload="showColor('<s:property value="status" />')">
<form id="main" action="showDataDetail.action" method="post"
      enctype="multipart/form-data">
    <input type="hidden" id="impBatchId" name="impBatchId"
           value="<s:property value='impBatchId' />"/>
    <table id="tbl_main" cellpadding="0" cellspacing="0"
           class="tablewh100">
        <tr>
            <td class="centercondition">
                <div id="tbl_current_status">
                    <img src="<%=bopTheme%>/themes/images/icons/icon13.png"/> <span
                        class="current_status_menu">当前位置：</span> <span
                        class="current_status_submenu1">销项税管理</span> <span
                        class="current_status_submenu">开票管理</span> <span
                        class="current_status_submenu">数据导入</span> <span
                        class="current_status_submenu">认定结果</span>
                </div>
                <div class="widthauto1">
                    <table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td>交易时间</td>
                            <td><input id="startTime" name="detailInfo.startTime" type="text"
                                       value="<s:property value='detailInfo.startTime' />" class="tbl_query_time"
                                       onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endTime\')||\'new Date()\'}'})"/>
                                &nbsp;&nbsp;&nbsp;--&nbsp;&nbsp;&nbsp; <input id="endTime"
                                                                              name="detailInfo.endTime" type="text"
                                                                              value="<s:property value='detailInfo.endTime' />"
                                                                              class="tbl_query_time"
                                                                              onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startTime\')}',maxDate:new Date()})"/>
                            </td>
                            <td>交易金额</td>
                            <td><input name="detailInfo.transAmtStart" id="transAmtStart"
                                       type="text" class="tbl_query_text" style="width: 80px;"
                                       value="<s:property value='detailInfo.transAmtStart' />"/>
                                &nbsp;&nbsp;--&nbsp;&nbsp; <input name="detailInfo.transAmtEnd"
                                                                  id="transAmtEnd" type="text" class="tbl_query_text"
                                                                  style="width: 80px;"
                                                                  value="<s:property value='detailInfo.transAmtEnd' />"/>
                            </td>
                            <td>客户号</td>
                            <td><input name="detailInfo.customerId" id="customerId" type="text"
                                       class="tbl_query_text" style="width: 150px;"
                                       value="<s:property value='detailInfo.customerId' />"/>
                            </td>
                            <td style="text-align:left"></td>
                        </tr>
                        <tr>
                            <td>交易类型</td>
                            <td><input name="detailInfo.transType" id="transType" type="text"
                                       class="tbl_query_text" style="width: 150px;"
                                       value="<s:property value='detailInfo.transType' />"/>
                            </td>
                            <td></td>
                            <td></td>
                            <td style="text-align:left"><input type="button"
                                                               class="tbl_query_button"
                                                               onMouseMove="this.className='tbl_query_button_on'"
                                                               onMouseOut="this.className='tbl_query_button'"
                                                               onclick="submitForm('showDataDetail.action');"
                                                               name="cmdFilter"
                                                               value="查询" id="cmdFilter"/>
                            </td>
                        </tr>
                    </table>
                </div>
                <table id="tbl_tools" width="100%" border="0">
                    <tr>
                        <td width="92%" align="left"><a href="#"
                                                        onclick="submitForm('impdataToExcel.action');"> <img
                                src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png"/>
                            导出 </a>
                        </td>
                        <td width="8%" align="right">&nbsp;</td>
                    </tr>
                </table>
                <div id="lessGridList19" style="overflow: auto; width: 100%;">
                    <table class="lessGrid" cellspacing="0" rules="all" border="0"
                           cellpadding="0" style="border-collapse: collapse;width: 100%;">
                        <tr class="lessGrid head">
                            <!-- 业务流水号 客户号 交易时间 交易类型 交易金额 是否含税 交易发生机构 发票类型 是否冲账 原始业务流水号 备注 -->
                            <th style="text-align: center">序号</th>
                            <th style="text-align: center">业务流水号</th>
                            <th style="text-align: center">客户号</th>
                            <th style="text-align: center">交易时间</th>
                            <th style="text-align: center">交易类型</th>
                            <th style="text-align: center">交易金额</th>
                            <th style="text-align: center">是否含税</th>
                            <th style="text-align: center">税率</th>
                            <th style="text-align: center">税额</th>
                            <th style="text-align: center">交易发生机构</th>
                            <th style="text-align: center">发票类型</th>
                            <th style="text-align: center">是否冲账</th>
                            <th style="text-align: center">原始业务流水号</th>
                            <th style="text-align: center">是否打票</th>
                            <th style="text-align: center">备注</th>
                            <th style="text-align: center">状态</th>
                        </tr>
                        <s:iterator value="paginationList.recordList" id="iList"
                                    status="stuts">
                            <%--<s:iterator value="#request.transInfoImp" id="iList"
                            status="stuts"> --%>
                            <s:hidden name="dStatus"/>
                            <tr align="center" name="detailList"
                                class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
                                <td align="center"><s:property value='#stuts.count'/></td>

                                <td name="data">
                                    <s:property value='transId'/>
                                </td>
                                <td name="data">
                                    <s:property value='customerId'/>
                                </td>
                                <td name="data">
                                    <s:property value='transDate'/>
                                </td>
                                <td name="data">
                                    <s:property value='transType'/>
                                </td>
                                <s:if test='taxFlag=="Y"'>
                                    <td name="data">
                                        <s:property value='transAmt'/>
                                    </td>
                                </s:if>
                                <s:else>
                                    <td name="data">
                                        <s:property value='income'/>
                                    </td>
                                </s:else>
                                <td name="data"><s:if test='taxFlag=="Y"'>是</s:if> <s:if
                                        test='taxFlag=="N"'>否</s:if>
                                </td>
                                <td name="data">0<s:property value='taxRate'/>
                                </td>
                                <td name="data">0<s:property value='taxAmt'/>
                                </td>
                                <td name="data"><s:property value='bankCode'/>
                                </td>
                                <td name="data">
                                    <s:if test='fapiaoType=="0"'>增值税专用发票</s:if>
                                    <s:if test='fapiaoType=="1"'>增值税普通发票</s:if>
                                </td>
                                <td name="data">
                                    <s:if test='isReverse=="Y"'>是</s:if>
                                    <s:if test='isReverse=="N"'>否</s:if>
                                </td>
                                <td name="data"><s:property value='reverseTransId'/></td>
                                <!-- 是否打票-->
                                <td name="data">
                                    <s:if test='transFapiaoType=="A"'>自动打印</s:if>
                                    <s:if test='transFapiaoType=="M"'>手动打印</s:if>
                                    <s:if test='transFapiaoType=="N"'>不用打印</s:if>
                                </td>
                                <td>
                                    <s:property value='remark'/>
                                </td>
                                <td>
                                    <s:if test='status=="0"'>未校验</s:if>
                                    <s:elseif test='dStatus=="000000000000"'>通过校验</s:elseif>
                                    <s:elseif test='dStatus!="000000000000"'>
                                        <a href="#" onclick="getMessage('<s:property value="message"/>');">未通过校验</a>
                                    </s:elseif>
                                </td>
                            </tr>
                        </s:iterator>
                    </table>
                </div>
                <div id="anpBoud" align="Right" style="width:100%;">
                    <table width="100%" cellspacing="0" border="0">
                        <tr>
                            <td align="right"><s:component template="pagediv"/>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="ctrlbuttonnew1">
                    <s:hidden name="flag"></s:hidden>
                    <s:if test="flag=='audit'">
                        <input type="button" class="tbl_query_button" value="返回"
                               onMouseMove="this.className='tbl_query_button_on'"
                               onMouseOut="this.className='tbl_query_button'" name="btCancel"
                               id="btCancel" onclick="javascript:goToPage('listAuditImpdata.action')"/>
                    </s:if>
                    <s:else>
                        <input type="button" class="tbl_query_button" value="返回"
                               onMouseMove="this.className='tbl_query_button_on'"
                               onMouseOut="this.className='tbl_query_button'" name="btCancel"
                               id="btCancel" onclick="javascript:goToPage('listImpdata.action')"/>
                    </s:else>
                </div>
            </td>
        </tr>
    </table>
</form>
</body>
<script type="text/javascript">
    function getMessage(msg) {
        if (msg != null && msg != "") {
            alert(msg);
        }
    }

</script>
</html>