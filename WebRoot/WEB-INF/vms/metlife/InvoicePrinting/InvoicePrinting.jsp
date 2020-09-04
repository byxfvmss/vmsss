<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <%@ include file="../../../../page/include.jsp" %>
    <link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
    <script type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
    <script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery.jqprint-0.3.js"></script>
    <script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
    <script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
    <meta http-equiv="Pragma" content="no-cache"/>
    <title>打印快递页面</title>
</head>
<body>
<form id="main" action="" method="post" enctype="multipart/form-data">
                    <table class="Noprint" id="tbl_tools" cellpadding="1" cellspacing="0">
                        <tr align="left">
                            <td>
                                <object id=WebBrowser classid=CLSID:8856F961-340A-11D0-A96B-00C04FD705A2 height=0 width=0></object>
                                <a href="#" onclick=document.all.WebBrowser.ExecWB(6,6) >
                                    <img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1006.png"/>
                                    打印</a>
                            </td>
                        </tr>
                    </table>
                        <!--startprint1-->
                        <table class="lessGrid" cellspacing="0" rules="all" border="0px"
                               cellpadding="0" style="border-collapse: collapse;width: 100%;">
                            <tr>
                                <td>邮编：</td>
                                <td></td>
                            </tr>
                            <tr>
                                <td>地址：</td>
                                <td></td>
                            </tr>
                            <tr>
                                <td>收件人：</td>
                                <td></td>
                            </tr>
                            <tr>
                                <td>发票号码：</td>
                                <td></td>
                            </tr>
                            <tr>
                                <td>保单号：</td>
                                <td></td>
                            </tr>
                            </table>
                            <div class="PageNext"></div>
    <table class="lessGrid" cellspacing="0" rules="all" border="0px"
           cellpadding="0" style="border-collapse: collapse;width: 100%;">
                            <tr>
                                <td>邮编：</td>
                                <td></td>
                            </tr>
                            <tr>
                                <td>地址：</td>
                                <td></td>
                            </tr>
                            <tr>
                                <td>收件人：</td>
                                <td></td>
                            </tr>
                            <tr>
                                <td>发票号码：</td>
                                <td></td>
                            </tr>
                            <tr>
                                <td>保单号：</td>
                                <td></td>
                            </tr>
                        </table>
                        <!--endprint1-->
</form>
</body>
<script type="text/javascript">
    function PrintWriter(oper) {
        if (oper < 10) {
            var bdhtml = window.document.body.innerHTML;
            //获取当前页的html代码
            var sprnstr = "<!--startprint" + oper + "-->";
            //设置打印开始区域
            var eprnstr = "<!--endprint" + oper + "-->";
            // 设置打印结束区域
            var prnhtml = bdhtml.substring(bdhtml.indexOf(sprnstr) + 18);
            // 从开始代码向后取html
            prnhtml = prnhtml.substring(0, prnhtml.indexOf(eprnstr));
            //从结束代码向前取html
            window.document.body.innerHTML = prnhtml;
            window.print();
            window.document.body.innerHTML = bdhtml;
        }
        else {
            window.print();
        }
    }
</script>
<!--media=print 这个属性可以在打印时有效-->
<style media=print>
    .Noprint{display:none;}
    .PageNext{page-break-after: always;}
</style>
</html>