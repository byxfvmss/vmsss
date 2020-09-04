<%@ page language="java"
         contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<!--
createTime:2016.2.
author:沈磊
content:销项 收费凭证 metlife
-->
<html>
<head>
    <title>增值税管理平台</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <%@ include file="../../../../page/include.jsp" %>

    <link href="<%=bopTheme2%>/css/main.css" type="text/css"
          rel="stylesheet">
    <link href="<%=webapp%>/page/common.css" type="text/css"
          rel="stylesheet">
    <link href="<%=bopTheme2%>/css/main.css" type="text/css"
          rel="stylesheet">
    <script language="javascript" type="text/javascript"
            src="<%=webapp%>/page/js/calendar_notime.js"></script>
    <script language="javascript" type="text/javascript"
            src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
    <script language="javascript" type="text/javascript"
            src="<%=webapp%>/page/js/XmlHttp.js"></script>
    <script language="javascript" type="text/javascript"
            src="<%=webapp%>/page/js/search.js"></script>
    <script language="javascript" type="text/javascript"
            src="<%=webapp%>/page/js/ylb.js"></script>
    <script language="javascript" type="text/javascript"
            src="<%=webapp%>/page/js/lhb.js"></script>
    <script language="javascript" type="text/javascript"
            src="<%=webapp%>/page/js/comm.js"></script>
    <script language="javascript" type="text/javascript"
            src="<%=bopTheme%>/js/main.js"></script>
    <script language="javascript" type="text/javascript"
            src="<%=bopTheme%>/js/search.js"></script>
    <script type="text/javascript"
            src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
    <script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
    <meta http-equiv="Pragma" content="no-cache"/>
    <script type="text/javascript">
        function querylist() {
            document.forms[0].action = "chargesVoucherDetial.action";
            document.forms[0].submit();
        }
        function toExcel() {
            document.forms[0].action = "chargesVoucherToExcel.action";
            document.forms[0].submit();
            document.forms[0].action = "chargesVoucherDetial.action";
        }
    </script>
</head>
<body marginwidth="300px;">
<form name="Form1" method="post"
      action="chargesVoucherDetial.action" id="Form1"
      enctype="multipart/form-data">
    <table id="tbl_main" cellpadding="0" cellspacing="0"
           class="tablewh100">
        <tr>
            <td class="centercondition">
                <s:component
                        template="rocketMessage"/>
                <div id="tbl_current_status">
                    <img src="<%=bopTheme%>/themes/images/icons/icon13.png"/> <span
                        class="current_status_menu">当前位置：</span> <span
                        class="current_status_submenu1">VMS销项管理</span> <span
                        class="current_status_submenu">收费凭证</span> <span
                        class="current_status_submenu">收费凭证一览</span>
                </div>
                <div class="widthauto1">
                    <table id="tbl_query" cellpadding="0" cellspacing="0" width="80%"
                           border="0">
                        <tr>
                            <td align="left">投保单号</td>
                            <td><input name="chargesVoucherInfo.ttmprcno"
                                       class="tbl_query_text"
                                       value="<s:property value="chargesVoucherInfo.ttmprcno"/>" maxlength="20">
                            </td>
                            <td align="left">机构</td>
                            <td><s:if
                                    test="authInstList != null && authInstList.size > 0">
                                <s:select name="chargesVoucherInfo.instId" list="authInstList"
                                          listKey="instId" listValue='instName'
                                          cssClass="tbl_query_text5" headerKey="" headerValue="全部"/>
                            </s:if> <s:if test="authInstList == null || authInstList.size == 0">
                                <select name="chargesVoucherInfo.instId" class="readOnlyText">
                                    <option value="">请分配机构权限</option>
                                </select>
                            </s:if></td>
                            <td>续期扣款银行</td>
                            <td><input type="text" class="tbl_query_text"
                                       name='chargesVoucherInfo.loanBank'
                                       value='<s:property value="chargesVoucherInfo.loanBank"/>'/>
                            </td>
                            <td align="left">到账日</td>
                            <td>
                                <input id="startDate" class="tbl_query_time" type="text"
                                       name="chargesVoucherInfo.trdtBegin"
                                       value="<s:property value='chargesVoucherInfo.trdtBegin'/>"
                                       onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}'})"
                                       size='11'/>
                                --
                                <input id="endDate" class="tbl_query_time" type="text" name="chargesVoucherInfo.trdtEnd"
                                       value="<s:property value='chargesVoucherInfo.trdtEnd'/>"
                                       onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}'})"
                                       size='11'/>
                            </td>

                        </tr>
                        <tr>
                            <td>保单号</td>
                            <td><input type="text" name="chargesVoucherInfo.cherNum"
                                       value='<s:property value="chargesVoucherInfo.cherNum"/>'
                                       class="tbl_query_text" maxlength="20"/></td>
                            <td>渠道</td>
                            <td><s:select name="chargesVoucherInfo.channel"
                                          list="chanNelList" listKey="key" listValue='value'
                                          cssClass="tbl_query_text5" headerKey="" headerValue="请选择"/></td>
                            <td>后续处理</td>
                            <td>
                                <input type="text" name="chargesVoucherInfo.sign"
                                       value="<s:property value="chargesVoucherInfo.sign"/>"
                                       class="tbl_query_text"/>
                            </td>
                            <td>TM项目名称</td>
                            <td><input type="text" name="chargesVoucherInfo.longDesc"
                                       value="<s:property value="chargesVoucherInfo.longDesc"/>"
                                       class="tbl_query_text"/></td>
                        </tr>
                        <tr>
                            <td>旧保单号</td>
                            <td><input type="text" name="chargesVoucherInfo.repNum"
                                       value="<s:property value="chargesVoucherInfo.repNum"/>"
                                       class="tbl_query_text"/></td>
                            <td>是否为信用卡</td>
                            <td><select class="tbl_query_text5"
                                        id="chargesVoucherInfo.ccardYn"
                                        name="chargesVoucherInfo.ccardYn">
                                <option value="">请选择</option>
                                <option value="1"
                                        <s:if test='chargesVoucherInfo.ccardYn=="1"'>selected</s:if>
                                        <s:else></s:else>>是
                                </option>
                                <option value="0"
                                        <s:if test='chargesVoucherInfo.ccardYn=="0"'>selected</s:if>
                                        <s:else></s:else>>否
                                </option>
                            </select></td>
                            <td></td>
                            <td></td>
                            <td><input type="button" class="tbl_query_button"
                                       value="查询" onMouseMove="this.className='tbl_query_button_on'"
                                       onMouseOut="this.className='tbl_query_button'" name="BtnView"
                                       id="BtnView" onclick="querylist()"/></td>
                            <td></td>
                        </tr>
                    </table>
                </div>
                <table id="tbl_tools" width="100%" border="0">
                    <tr>
                        <td align="center" width="5%">
                            <a href="#" name="upLoad" id="upLoad" onclick="toExcel()"> <img
                                src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png"/>导出</a></td>
                        <%--<td align="center" width="5%"></td>--%>
                        <%--<td align="center" width="5%"></td>--%>
                        <%--<td align="center" width="5%"></td>--%>
                        <%--<td align="center" width="5%">`</td>--%>
                        <%--<td align="center" width="5%"></td>--%>
                        <%--<td align="center" width="5%"></td>--%>
                        <%--<td align="center" width="35%"></td>--%>
                    </tr>
                </table>
                <div id="lessGridList3" style="overflow: auto; width: 100%;">
                    <table class="lessGrid" cellspacing="0" rules="all" border="0"
                           cellpadding="0" style="border-collapse: collapse;width: 100%;">
                        <tr class="lessGrid head">
                            <th width="3%" style="text-align:center"><input
                                    id="CheckAll" style="width:13px;height:13px;" type="checkbox"
                                    onClick="checkAll(this,'ruleId')"/></th>
                            <th style="text-align:center">邮编</th>
                            <th style="text-align:center">收费地址</th>
                            <th style="text-align:center">投保人姓名</th>
                            <th style="text-align:center">被保人姓名</th>
                            <th style="text-align:center">分支机构</th>
                            <th style="text-align:center">渠道</th>
                            <th style="text-align:center">保单号</th>
                            <th style="text-align:center">旧保单号</th>
                            <th style="text-align:center">投保单号</th>
                            <th style="text-align:center">主险名称</th>
                            <th style="text-align:center">交费频率</th>
                            <th style="text-align:center">收费项目</th>
                            <th style="text-align:center">交费起始日期</th>
                            <th style="text-align:center">交费终止日期</th>
                            <th style="text-align:center">金额</th>
                            <th style="text-align:center">本期保费到帐日</th>
                            <th style="text-align:center">生成日期</th>
                            <th style="text-align:center">TM项目名称</th>
                            <th style="text-align:center">是否为信用卡</th>
                            <th style="text-align:center">续期扣款银行</th>
                            <th style="text-align:center">后续处理类型</th>
                            <th style="text-align:center">后续处理的具体内容</th>
                        </tr>
                        <s:iterator value="paginationList.recordList" id="iList"
                                    status="stuts">
                            <tr id="<s:property value="num"/>">
                                <td style="text-align:center"><input type="checkbox"
                                                                     style="width:13px;height:13px;" name="ruleId"
                                                                     value="<s:property value="vcvId"/>"/>
                                </td>
                                <td><s:property value='zipCode'/>
                                </td>
                                <td><s:property value='feeAdd'/>
                                </td>
                                <td><s:property value='cownNum'/>
                                </td>
                                <td><s:property value='lifCnum'/>
                                </td>
                                <td><s:property value='instId'/>
                                </td>
                                <td><s:property value='channel'/>
                                </td>
                                <td><s:property value='cherNum'/>
                                </td>
                                <td><s:property value='repNum'/>
                                </td>
                                <td><s:property value='ttmprcno'/>
                                </td>
                                <td><s:property value='planLongDesc'/>
                                </td>
                                <td><s:property value='billFreq'/>
                                </td>
                                <td><s:property value='feeTyp'/>
                                </td>
                                <td><s:property value='instFrom'/>
                                </td>
                                <td><s:property value='instTo'/>
                                </td>
                                <td><s:property value='acctAmt'/>
                                </td>
                                <td><s:property value='trdt'/>
                                </td>
                                <td><s:property value='createDate'/>
                                </td>
                                <td><s:property value='longDesc'/>
                                </td>
                                <td><s:if test='ccardYn=="1"'>是</s:if>
                                    <s:if test='ccardYn=="0"'>否</s:if>
                                </td>
                                <td><s:property value='loanBank'/>
                                </td>
                                <td><s:property value='sign'/>
                                </td>
                                <td><s:property value='reMarks'/>
                                </td>
                            </tr>
                        </s:iterator>
                    </table>
                </div>
                <div id="anpBoud" align="Right" style="overflow:auto;width:100%;">
                    <table width="100%" cellspacing="0" border="0">
                        <tr>
                            <td align="right"><s:component template="pagediv"/></td>
                        </tr>
                    </table>
                </div>
        </tr>
    </table>
</form>
</body>
</html>
