<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%
    response.setHeader("Pragma", "No-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
    <base target="_self">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <%@ include file="../../../../page/include.jsp" %>
    <title><s:if test="flag=='add'">新增</s:if> <s:elseif test="flag=='ipdate'">修改</s:elseif>特殊登记</title>
    <script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
    <script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
    <script type="text/javascript" src="<%=webapp%>/page/js/SimpleTree/js/validator.js"></script>
    <script type="text/javascript" src="<%=webapp%>/page/js/SimpleTree/js/validator.js"></script>
    <script type="text/javascript" src="<%=webapp%>/page/js/SimpleTree/js/MessageBox/messageBox.js"></script>
    <link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
    <link href="<%=webapp%>/page/js/SimpleTree/js/MessageBox/messageBox.css" type="text/css" rel="stylesheet">
    <link href="<c:out value="${bopTheme2}"/>/css/subWindow.css" type="text/css" rel="stylesheet">
    <link href="<%=bopTheme2%>/css/editView.css" type="text/css" rel="stylesheet">
    <script type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
    <script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
    <script language="javascript" type="text/javascript">
        //标识页面是否已提交
        var subed = false;
        function checkForm() {

            //验证是否重复提交
            if (subed == true) {
                alert("信息正在发送给服务器，请不要重复提交信息！");
                return false;
            }
            if (fucCheckNullLength(document.getElementById("ttmPrcno"), 20,
                            (/^[A-Za-z0-9]+$/.test($("#ttmPrcno").val())), "投保单号不可为空!", "投保单号必须是20位",
                            "投单号必须为数字或者英文字母组合!") == false) {
                return false;
            }
            if ($("#cherNum").val().length > 1 && $("#cherNum").val().length != " ") {
                if ($("#cherNum").val().length > 0 && fucCheckNullLength(document.getElementById("cherNum"), 20,
                                (/^[A-Za-z0-9]+$/.test($("#cherNum").val())), "保单号不可为空!", "保单号必须是20位",
                                "保单号必须为数字或者英文字母组合!") == false) {
                    return false;
                }
            }
            if ($("#repNum").val().length > 1 && $("#repNum").val().length != " ") {
                if ($("#repNum").val().length > 0 && fucCheckNullLength(document.getElementById("repNum"), 20,
                                (/^[A-Za-z0-9]+$/.test($("#repNum").val())), "投保单号不可为空!", "投保单号必须是20位",
                                "投单号必须为数字或者英文字母组合!") == false) {
                    return false;
                }
            }
            if (fucCheckNullBoth(document.getElementById("cherNum"),
                            document.getElementById("repNum")) == false) {
                return false;

            }
            if (fucCheckNull(document.getElementById("docType"), "请选择适用单证类别!") == false) {
                return false;
            }
            subed = true;
            return true;
        }
        function cherNumBlur(t, value) {

        }
        function keyUpTrim(value) {
            return value.trim();
        }
        function submitForm() {
            if (checkForm()) {
                document.getElementById("frm").submit();
            }
        }
        function fun() {
            var grid = $("#contenttable").find("tr td input[type=checkbox]");
            $(grid).change(function () {
                if (this.checked == true) {
                    return this.value = 'Y'
                } else {
                    this.value = 'N'
                }
            });
            for (var i = 0; i < grid.length; i++) {
                if ($(grid[i]).val() != "" && $(grid[i]).val() == "Y") {
                    $(grid[i]).val("Y");
                    $(grid[i]).attr("checked", "checked");
                }
            }
        }
    </script>
</head>
<body scroll="no" style="overflow:hidden;" onload="fun();">
<div class="showBoxDiv">

    <form id="frm" action="saveRegIsterList.action" method="post">
        <div id="editsubpanel" class="editsubpanel">
            <div style="overflow: auto; width: 100%;">
                <input type="hidden" name="flag" id="flag"
                       value="<s:property value="flag" />"/>
                <table id="contenttable" class="lessGrid" cellspacing="0"
                       width="100%" align="center" cellpadding="0">
                    <tr class="header">
                        <th colspan="8"><s:if test="flag=='add'">新增 </s:if><s:elseif
                                test="flag=='update'">修改 </s:elseif>特殊登记
                        </th>
                    </tr>
                    <tr style="font-size: 12px;" class="listbar">
                        <%--<td></td>--%>
                        <td style="text-align: right;" width="11%" class="listbar">投保单号:</td>
                        <td style="text-align: left;" width="12%">
                            <input class="tbl_query_text5" type="text" name="ister.ttmPrcno"
                                   value="<s:property value='ister.ttmPrcno'/>" id="ttmPrcno"
                                   maxlength="20" onkeyup="keyUpTrim(this.value);"/></td>
                        <td style="text-align: right;" width="8%" class="listbar">保单号:</td>
                        <td style="text-align: left;" width="12%">
                            <input class="tbl_query_text5 cherNumC" type="text" name="ister.cherNum"
                                   <s:if test="flag=='update'">readonly="readonly"</s:if>
                                   value="<s:property value='ister.cherNum'/>" maxlength="20"
                                   onblur="cherNumBlur(this,this.value);"
                                   onkeyup="keyUpTrim(this.value);" id="cherNum"/>
                        </td>
                        <td style="text-align: right;" width="8%" class="listbar">旧保单号:</td>
                        <td style="text-align: left;" width="12%">
                            <input class="tbl_query_text5" type="text" name="ister.repNum"
                                   <s:if test="flag=='update'">readonly="readonly"</s:if>
                                   value="<s:property value='ister.repNum'/>" id="repNum"
                                   maxlength="20" onkeyup="keyUpTrim(this.value);"/>
                        </td>
                        <td width="8%"></td>
                    </tr>
                    <tr style="font-size: 12px;" class="listbar">
                        <td style="text-align: right;width: 8%;">机构:</td>
                        <td>
                            <s:if test="authInstList != null && authInstList.size > 0">
                                <s:if test="authInstList.size==1">
                                    <s:select name="ister.instId" list="authInstList" listKey='instId'
                                              listValue='instName' cssClass="tbl_query_text5"
                                              headerKey="" headerValue="请选择"/>
                                </s:if>
                                <s:else>
                                    <s:select name="ister.instId" list="authInstList" listKey='instId'
                                              listValue='instName' cssClass="tbl_query_text5"
                                              headerKey="" headerValue="请选择"/>
                                </s:else>
                            </s:if>
                            <s:if test="authInstList == null || authInstList.size == 0">
                                <select name="instCode" class="readOnlyText">
                                    <option value="">请分配机构权限</option>
                                </select>
                            </s:if>
                        </td>
                        <td style="text-align: right;width: 8%;">渠道:</td>
                        <td>
                            <s:select name="ister.chanNel" list="chanNelList" listKey="key"
                                      listValue='value' cssClass="tbl_query_text5" headerKey="" headerValue="请选择"/>
                        </td>
                        <td colspan="2" id="cols"></td>
                        <td></td>
                    </tr>
                    <tr style="font-size: 12px;" class="listbar">
                        <td style="text-align: right;" rowspan="5" width="11%">后续处理类型:</td>
                        <td colspan="7"></td>
                    </tr>
                    <tr style="font-size: 12px;" class="listbar">
                        <td style="font-size: 12px;text-align: right;width:12%;">拒收:</td>
                        <td><input style="width:13px;height:13px;"
                                   name="ister.sign_k" value="<s:property value="ister.sign_k" />"
                                   type="checkbox"/>
                        </td>
                        <td style="font-size: 12px;text-align: right;width:12%;">快递:</td>
                        <td><input style="width:13px;height:13px;"
                                   name="ister.sign_b" type="checkbox" value="<s:property value="ister.sign_b" />"/>
                        </td>
                        <td style="font-size: 12px;text-align: right;width:12%;">难字:</td>
                        <td><input style="width:13px;height:13px;"
                                   name="ister.sign_c" type="checkbox" value="<s:property value="ister.sign_c" />"/>
                        </td>
                    </tr>
                    <tr style="font-size: 12px;" class="listbar">
                        <td style="font-size: 12px;text-align: right;width:12%;">转代理人:</td>
                        <td><input style="width:13px;height:13px;"
                                   name="ister.sign_d" type="checkbox" value="<s:property value="ister.sign_d" />"/>
                        </td>
                        <td style="font-size: 12px;text-align: right;width:12%;">临时地址:</td>
                        <td><input style="width:13px;height:13px;"
                                   name="ister.sign_e" type="checkbox" value="<s:property value="ister.sign_e" />"/>
                        </td>
                        <td style="text-align: right;width:12%;">平信:</td>
                        <td><input style="width:13px;height:13px;"
                                   name="ister.sign_f" type="checkbox" value="<s:property value="ister.sign_f" />"/>
                        </td>
                    </tr>
                    <tr style="font-size: 12px;" class="listbar">
                        <td style="text-align: right;width:12%;">挂号:</td>
                        <td>
                            <input style="width:13px;height:13px;"
                                   name="ister.sign_g" type="checkbox" value="<s:property value="ister.sign_g" />"/>
                        </td>
                        <td style="text-align: right;width:12%;">集汇件:</td>
                        <td>
                            <input style="width:13px;height:13px;" name="ister.sign_h"
                                   type="checkbox" value="<s:property value="ister.sign_h" />"/>
                        </td>
                        <td style="text-align: right;width:12%;">客户自取:</td>
                        <td>
                            <input style="width:13px;height:13px;" name="ister.sign_i"
                                   type="checkbox" value="<s:property value="ister.sign_i" />"/>
                        </td>
                    </tr>
                    <tr style="font-size: 12px;" class="listbar">
                        <td style="text-align: right;width:12%;">送前电话通知:</td>
                        <td><input style="width:13px;height:13px;" name="ister.sign_j"
                                   type="checkbox" value="<s:property value="ister.sign_j" />"/>
                        </td>
                        <td style="text-align: right;width:12%;">月寄:</td>
                        <td><input style="width:13px;height:13px;" name="ister.sign_a"
                                   type="checkbox" value="<s:property value="ister.sign_a" />"/>
                        </td>
                        <td style="text-align: right;width:12%;">定期邮寄:</td>
                        <td><input style="width:13px;height:13px;" name="ister.sign_l"
                                   type="checkbox" value="<s:property value="ister.sign_l" />"/>
                        </td>
                    </tr>
                    <tr style="font-size: 12px;" class="listbar">
                        <td style="text-align: right;">适用单证类别:</td>
                        <td><s:select name="ister.docType" list="docTypeList" listKey="key" id="docType"
                                      listValue='value' cssClass="tbl_query_text5" headerKey="" headerValue="请选择"/>
                            <span style="color: red;">*</span>
                        </td>
                        <td colspan="6"></td>
                    </tr>
                    <tr style="font-size: 12px;" class="listbar">
                        <td style="text-align: right;">备注:</td>
                        <td colspan="5">
                            <input style="width: 100%;height: 100%;" class="tbl_query_text5" name="ister.reMarks"
                                   value="<s:property value="ister.reMarks" />" maxlength="60" id="reMarks"/>
                        </td>
                        <td></td>
                    </tr>
                </table>
            </div>
            <div id="ctrlbutton" class="ctrlbutton" style="border:0px">
                <input type="button" class="tbl_query_button"
                       onMouseMove="this.className='tbl_query_button_on'"
                       onMouseOut="this.className='tbl_query_button'"
                       onclick="submitForm();" name="BtnSave" value="保存" id="BtnSave"/>
                <input type="button" class="tbl_query_button"
                       onMouseMove="this.className='tbl_query_button_on'"
                       onMouseOut="this.className='tbl_query_button'"
                       onclick="CloseWindow();" name="BtnReturn" value="关闭"
                       id="BtnReturn"/>
            </div>
        </div>
    </form>
</div>
</body>
<script type="text/javascript">
    $.fn.onlyNumAlpha = function () {
        $(this).keypress(function (event) {
            var eventObj = event || e;
            var keyCode = eventObj.keyCode || eventObj.which;
            if ((keyCode >= 48 && keyCode <= 57) || (keyCode >= 65 && keyCode <= 90) || (keyCode >= 97 && keyCode <= 122))
                return true;
            else
                return false;
        }).focus(function () {
            this.style.imeMode = 'disabled';
        }).bind("paste", function () {
            var clipboard = window.clipboardData.getData("Text");
            if (/^(\d|[a-zA-Z])+$/.test(clipboard))
                return true;
            else
                return false;
        });
    };
    $(function () {
    })
</script>
</html>
