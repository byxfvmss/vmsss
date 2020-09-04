<!--file: <%=request.getRequestURI()%> -->
<%@ page language="java" import="java.util.*"
         import="java.math.BigDecimal" import="com.opensymphony.util.BeanUtils"
         import="com.cjit.common.util.NumberUtils"
         import="com.cjit.common.util.PaginationList"
         import="com.cjit.vms.trans.model.ExchangeRate"
         import="com.cjit.vms.trans.util.DataUtil"
         import="com.cjit.gjsz.datadeal.model.SelectTag"
         contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="../../../../page/include.jsp" %>
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
    <script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
    <script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/vms.js"></script>

    <script type="text/javascript">
        function submitForm() {
            document.forms[0].submit();
        }
        function checkChkBoxesSelectedOne(chkBoexName) {
            var j = 0;
            var chkBoexes = document.getElementsByName(chkBoexName);
            for (i = 0; i < chkBoexes.length; i++) {
                if (chkBoexes[i].checked) {
                    j++;
                }
            }
            return j;
        }
        function checkCheckBoxSelectByCherNumDelete() {
            if (!checkChkBoxesSelected("selectBillIds")) {
                return alert("请选择一条记录!");
            }
            var t = "";
            var inputs = document.getElementsByName("selectBillIds");
            for (var i = 0; i < inputs.length; i++) {
                if (inputs[i].checked == true) {
                    t += "'" + inputs[i].value + "',";
                }
            }
            document.getElementById("Form1").action = "delRegIsterList.action?cherNum=" + t.substring(0, t.length - 1);
            document.getElementById("Form1").submit();
            document.getElementById("Form1").action = "getRegisterAction.action";

        }
        function openUpWindow(flag) {
            if (flag == "add") {
                OpenModalWindow1(encodeURI("addOrUpRegisterList.action?flag=" + flag), 800, 520, true);
                return;
            }
            if (checkChkBoxesSelected("selectBillIds") && flag == "update") {
                if (checkChkBoxesSelectedOne("selectBillIds") == 1) {
                    var t = "";
                    var inputs = document.getElementsByName("selectBillIds");
                    for (var i = 0; i < inputs.length; i++) {
                        if (inputs[i].checked == true) {
                            t += inputs[i].value + "&repNum=" + $(inputs[i]).attr("repnum");
                        }
                    }
                    OpenModalWindow1(encodeURI("addOrUpRegisterList.action?cherNum=" + t + "&flag=" + flag), 800, 520, true);
                    return;
                } else {
                    alert("请选择一条登记记录");
                    return;
                }
            } else {
                alert("请选择一条登记记录");
                return;
            }

        }
        function OpenModalWindow1(newURL, width, height, needReload) {
            try {
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
                try {
                    retData = showModalDialog(newURL,
                            window,
                            "dialogWidth:" + width
                            + "px;dialogHeight:" + height
                            + "px;dialogLeft:" + ($(window).width() - width) / 2
                            + "px;dialogTop:" + ($(window).height() - height) / 2
                            + "px;");
                } catch (e) {
                    alert(e);
                }
                if (needReload && retData) {
                    document.forms[0].action = "getRegisterAction.action";
                    document.forms[0].submit();
                }
                return retData;
            } catch (err) {
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
<form name="Form1" method="post" action="getRegisterAction.action"
      id="Form1" enctype="multipart/form-data">
    <table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
        <tr>
            <td class="centercondition">
                <s:component template="rocketMessage"/>
                <div id="tbl_current_status">
                    <img src="<%=bopTheme%>/themes/images/icons/icon13.png"/>
                    <span class="current_status_menu">当前位置：</span>
                    <span class="current_status_submenu1">参数管理</span>
                    <span class="current_status_submenu">业务参数管理</span>
                    <span class="current_status_submenu">特殊登记</span>
                </div>
                <div class="widthauto1">
                    <table class="lessGridList4" id="tbl_query" cellpadding="0" cellspacing="0" width="80%" border="0">
                        <tr>
                            <td width="5%"></td>
                            <td style="text-align: right;" width="8%">投保单号:</td>
                            <td style="text-align: left;" width="18%">
                                <input class="tbl_query_text5" type="text"
                                       name="ister.ttmPrcno" maxlength="20"
                                       value="<s:property value='ister.ttmPrcno'/>"/>
                            </td>
                            <td style="text-align: right;" width="8%">保单号:</td>
                            <td style="text-align: left;" width="18%">
                                <input class="tbl_query_text5" type="text"
                                       name="ister.cherNum" maxlength="20"
                                       value="<s:property value='ister.cherNum'/>"
                                /></td>
                            <td style="text-align: right;" width="5%">旧保单号:</td>
                            <td style="text-align: left;" width="18%">
                                <input class="tbl_query_text5" type="text"
                                       name="ister.repNum" maxlength="20"
                                       value="<s:property value='ister.repNum'/>"
                                />
                            </td>
                            <td width="11%"></td>
                        </tr>
                        <tr>
                            <td width="5%"></td>
                            <td style="text-align: right;" width="5%">机构:</td>
                            <td style="text-align: left;" width="18%">
                                <s:if test="authInstList != null && authInstList.size > 0">
                                    <s:select name="ister.instId" list="authInstList" listKey="instId"
                                              listValue='instName' cssClass="tbl_query_text5"
                                              headerKey="" headerValue="全部"/>
                                </s:if>
                                <s:if test="authInstList == null || authInstList.size == 0">
                                    <select name="ister.instId" class="readOnlyText">
                                        <option value="">请分配机构权限</option>
                                    </select>
                                </s:if>
                            </td>

                            <td style="text-align: right;" width="5%">渠道:</td>
                            <td style="text-align: left;" width="18%">
                                <s:select name="ister.chanNel" list="chanNelList" listKey="key"
                                          listValue='value' cssClass="tbl_query_text5" headerKey="" headerValue="请选择"/>
                            </td>
                            <td style="text-align: right;" width="11%">后续处理类型:</td>
                            <td style="text-align: left;" width="18%">
                                <s:select name="ister.signParam" list="signList" listKey="key"
                                          listValue='value' cssClass="tbl_query_text5" headerKey=""
                                          headerValue="请选择"/></td>
                            <td width="11%"></td>
                        </tr>
                        <tr>
                            <td width="5%"></td>
                            <td style="text-align: right;" width="11%">适用单证类别:</td>
                            <td style="text-align: left;" width="18%">
                                <s:select name="ister.docType" list="docTypeList" listKey="key"
                                          listValue='value' cssClass="tbl_query_text5" headerKey="" headerValue="请选择"/>
                            </td>
                            <td width="5%"></td>
                            <td width="18%"></td>
                            <td width="11%"></td>
                            <td width="18%" align="left">
                                <input type="button" class="tbl_query_button"
                                       onMouseMove="this.className='tbl_query_button_on'"
                                       onMouseOut="this.className='tbl_query_button'"
                                       name="cmdDistribute" onclick="submitForm();" value="查询" id="btnSearch"
                                />
                            </td>
                        </tr>
                    </table>
                </div>
                <table id="tbl_tools" width="100%" border="0">
                    <tr>
                        <td align="left">
                            <a href="#" onclick="openUpWindow('add');" name="btAdd"><img
                                    src="<c:out value="${bopTheme}"/>/themes/images/icons/icon16.png"/>添加</a>
                            <a href="#" onclick="openUpWindow('update');" name="btDelete" id="btDelete"><img
                                    src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1033.png"/>修改</a>
                            <a href="#" name="btDelete" id="btDelete" onclick="checkCheckBoxSelectByCherNumDelete();"><img
                                    src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1019.png"/>删除</a>
                        </td>
                    </tr>
                </table>

                <div id="lessGridList4" style="overflow: auto; width: 100%;">
                	<div style="width: 0px;height: auto;" id="rDiv">
	                    <table class="lessGrid" cellspacing="0" rules="all" border="0" cellpadding="0"
	                           style="border-collapse: collapse; width: 100%;">
	                        <tr class="lessGrid head">
	                            <th style="text-align:center;">
	                                <input style="width:13px;height:13px;" id="CheckAll" type="checkbox"
	                                       onclick="cbxselectall(this,'selectBillIds')"/>
	                            </th>
	                            <th style="text-align:center;">序号</th>
	                            <th style="text-align:center;">投保单号</th>
	                            <th style="text-align:center">保单号</th>
	                            <th style="text-align:center">旧保单号</th>
	                            <th style="text-align:center">分支机构</th>
	                            <th style="text-align:center">渠道</th>
	                            <th style="text-align:center">后续处理类型</th>
	                            <th style="text-align:center">适用单证类别</th>
	                            <th style="text-align:center">后续处理的具体内容</th>
	                            <th style="text-align:center">登记时间</th>
	                            <th style="text-align:center">登记人</th>
	                        </tr>
	                        <s:iterator value="paginationList.recordList" id="iList" status="stuts">
	                            <tr align="center"
	                                class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>"
	                                id="<s:property value="#iList.cherNum"/>">
	                                <td><input type="checkbox" repNum="<s:property value="#iList.repNum"/>"
	                                           style="width:13px;height:13px;" name="selectBillIds"
	                                           value="<s:property value="#iList.cherNum"/>"></td>
	                                <td align="center"><s:property value='#stuts.count'/></td>
	                                <td align="center"><s:property value='ttmPrcno'/></td>
	                                <td align="center"><s:property value='cherNum'/></td>
	                                <td align="center"><s:property value='repNum'/></td>
	                                <td align="center"><s:property value='instName'/></td>
	                                <td align="left"><s:property value='chanNelCh'/></td>
	                                <td align="left"><s:property value='signType'/></td>
	                                <td align="center">
	                                    <s:if test="docType==0">全部</s:if>
	                                    <s:if test="docType==1">发票</s:if>
	                                    <s:if test="docType==2">收费凭证</s:if>
	                                </td>
	                                <td align="center"><s:property value='reMarks'/></td>
	                                <td align="center"><s:property value='createDate'/></td>
	                                <td align="center"><s:property value='createUser'/></td>
	                            </tr>
	                        </s:iterator>
	                    </table>
                    </div>
                </div>
                <div id="anpBoud" align="Right" style="overflow:auto;width:100%;">
                    <table width="100%" cellspacing="0" border="0">
                        <tr>
                            <input type="hidden" name="paginationList.showCount" value="false"/>
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