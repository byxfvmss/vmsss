<!-- file: <%=request.getRequestURI()%> -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/page/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
    <title>增值税管理平台</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
    <script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/calendar_notime.js"></script>
    <script language="javascript" type="text/javascript"
            src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
    <script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/editview.js"></script>
    <script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/XmlHttp.js"></script>
    <script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/search.js"></script>
    <script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/ylb.js"></script>
    <script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
    <script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/search.js"></script>
    <script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
    <script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/vms.js"></script>
    <script type="text/javascript">
        //MetLife版本 Check校验
        function checktransToEachMetlife(chkBoxName){
            var chkBoexes= document.getElementsByName(chkBoxName);
            for(i=0;i<chkBoexes.length;i++){
                if(chkBoexes[i].checked){
                    //var accAmt = $(chkBoexes[i]).closest("tr").find("[name=amt]").text().replace(",","").trim();
                    var accAmt = $.trim($(chkBoexes[i]).closest("tr").find("[name=amt]").text().replace(",",""))
                    if(parseFloat(accAmt) < 0){
                        return "负值交易不能单独开票";
                    }
                    return true;
                }
            }
            return false;
        }
        function checktransToOneMetlife(chkBoxName){
            var amt = 0;
            var cherNum = "";
            var feeTyp = "";
            var leng = 0;
            var dj = "";
            var array = new Array();
            var chkBoexes= document.getElementsByName(chkBoxName);
            for(i=0;i<chkBoexes.length;i++){
                if(chkBoexes[i].checked){
                    //dj = $(chkBoexes[i]).closest("tr").find("[name='feeTyp']").text().trim();
                    dj = $.trim($(chkBoexes[i]).closest("tr").find("[name='feeTyp']").text());
                    leng++;
                    feeTyp += $.trim($(chkBoexes[i]).closest("tr").find("[name=feeTyp]").text()) +",";
                    amt += parseFloat($.trim($(chkBoexes[i]).closest("tr").find("[name=amt]").text().replace(",","")));
                    cherNum += $.trim($(chkBoexes[i]).closest("tr").find("[name=cherNum]").text()) +",";
                }
            }
            var p = cherNum.substr(0,cherNum.length-1).split(",");
            for(var i=0;i< p.length;i++){
               /* 20160715:根据余文丽要求：只要客户名一样就可开票，注释掉此部分 */
               /* if(i > 0){
                    if(p[0] != p[i]){
                        return "不同保单不能合并开票";
                    }
                }*/
            }

            var f = feeTyp.substr(0,feeTyp.length-1).split(",");
            for(var i=0;i< f.length;i++){
                if(i > 0){
                    if(f[0] != f[i]){
                        return "不同费用类型不能合并开票";
                    }
                }
            }

            if(amt < 0){
                return "合计为负值，不能开票";
            }else{
                if(leng > 0){
                    if(dj == '015'){
                        if( confirm("请确认发票金额（ " +amt+ " ）与实际收费是否一致")){
                            return true;
                        }else{
                            return null;
                        }
                    }else{
                        return true;
                    }
                }else{
                    return false;
                }
            }
        }

        jQuery.extend({
            checkTransDate: function () {
                //提示逻辑
                return true;
            }
        });

        // [查询]按钮
        function submit() {
            document.forms[0].submit();
        }

        // [开票]按钮
        function transToEachBill() {
            if (checkChkBoxesSelected("selectTransIds")) {
                submitAction(document.forms[0], "transToEachBill.action?paginationList.showCount=" + "false");
                document.forms[0].action = "listTrans.action?paginationList.showCount=" + "false";
            } else {
                alert("请选择交易记录！");
            }
        }
        function transtoCustmer() {
            if (!confirm("确定对选中交易进行关联客户吗？")) {
                return false;
            }
            var flag = false;
            var count = 1;
            var checkboxes = document.getElementsByName("selectTransIds");
            var selectedIds = "";
            var customerIds = document.getElementsByName("selectCustomers");
            var customerId = "";
            var customerTypes = document.getElementsByName("selectCustomerTypes");
            var selectDataStatuses = document.getElementsByName("selectDataStatuses");
            var customerType = "";
            var selectIncome = document.getElementsByName("selectIncome");
            var selectBalance = document.getElementsByName("selectBalance");
            for (var i = 0; i < checkboxes.length; i++) {
                var box = checkboxes[i];
                if (box.checked) {
                    flag = true;
                    if (count == 1) {
                        customerId = customerIds[i].value;
                        customerType = customerTypes[i].value;
                    }
                    if (i == checkboxes.length - 1) {
                        selectedIds = selectedIds + box.value;
                    } else {
                        selectedIds = selectedIds + box.value + ",";
                    }
                   /*  if (selectDataStatuses[i].value != '1') {
                        alert("所选部分交易已经开票，不能再修改客户信息！");
                        return false;
                    } */
                   /*  if (selectIncome[i].value != selectBalance[i].value) {
                        alert("所选部分交易已经做过拆分开票，不能再修改客户信息！");
                        return false;
                    } */
                    if (customerId != customerIds[i].value) {
                        alert("请选择相同客户所发生的交易");
                        return false;
                    }
                    if (customerType != customerTypes[i].value){
                    	alert("请选择相同客户类型的交易");
                    	return false;
                    }
                    count = count + 1;
                }
            }
            if (!flag) {
                alert("请选择至少一条交易记录");
                return false;
            }
            var newUrl = "connectCustomer.action?transIds=" + selectedIds + "&orgCustomerId=" + customerId + "&orgCustomerType=" + customerType;
            submitAction(document.forms[0], newUrl);
            document.forms[0].action = "listTrans.action?paginationList.showCount=" + "false";
        }
        
        //添加申请方法通过接口向核心获取数据
        function applyInvoice() {
        	var busCode = $('#business_code').val();
        	
        	
        	if(busCode == null || busCode == ""){
        		alert("请填写保单号");
        		return;
        	}
        	
        	
        	$('#business_code2').val(busCode);
        	
        	//首先根据业务编号查询开票数据中是否存在已开票的数据
        	document.forms[1].submit();
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

        function transToOneBill() {
            var selectTransIds = "";
            var selectCustomers = "";
            var cherNum = "";
            var cherNums = document.getElementsByName("selectCherNum");
            var transIds = document.getElementsByName("selectTransIds");
            var customers = document.getElementsByName("selectCustomers");
            for (var i = 0; i < customers.length; i++) {
                if (transIds[i].checked) {
                    selectCustomers += customers[i].value + ",";
                }
            }
            for (var i = 0; i < transIds.length; i++) {
                if (transIds[i].checked) {
                    selectTransIds += transIds[i].value + ",";
                }
            }

            $.ajax({
                url: "selectTransToOneBill.action",
                type: 'POST',
                async: false,
                data: {
                    selectTransIds: selectTransIds.substring(0, selectTransIds.length - 1),
                    selectCustomers: selectCustomers.substring(0, selectCustomers.length - 1)
                },
                dataType: "json",
                error: function () {
                    return false;
                },
                success: function (result) {
                    if (result.checkFlag == "N") {
                        alert(result.checkResultMsg);
                        return;
                    }
                    /* if(result=="N"){
                     submitAction(document.forms[0], "transToOneBill.action");
                     return;
                     } */
                    if (result.checkFlag == "Y") {
                        if (confirm(result.checkResultMsg)) {
                            submitAction(document.forms[0], "transToOneBill.action?paginationList.showCount=" + "false");
                            document.forms[0].action = "listTrans.action?paginationList.showCount=" + "false";
                        }
                    }

                }
            });
        }
        // [合并开票]按钮
        function transToOneBills() {
           var flag = checktransToOneMetlife("selectTransIds");
            if (flag == true) {
                var transIds = document.Form1.selectTransIds;
                var customers = document.Form1.selectCustomers;
                var fapiaoTypes = document.Form1.selectFapiaoTypes;
                //保单号
                var cherNums = document.getElementsByName("selectCherNum");
                var cherNum = "";
                var customer = "";
                var fapiaoType = "";
                var selectTransIds = "";
                var selectPremTerm = document.Form1.selectPremTerm;
                var premTermStr = "";
                if(selectPremTerm.length>0){
                    for(var i = 0;i < selectPremTerm.length; i++){
                        if(document.Form1.selectTransIds[i].checked){
                            premTermStr += document.Form1.selectPremTerm[i].value + ",";
                        }
                    }
                    var premTermArray=premTermStr.substring(0, premTermStr.length-1).split(",");
                    for(var i=0;i<premTermArray.length;i++){
                        for(var j= i + 1 ; j < premTermArray.length;j++){
                            if(premTermArray[i] > premTermArray[j]){
                                var temp = premTermArray[i];
                                premTermArray[i] = premTermArray[j];
                                premTermArray[j] = temp;
                            }
                        }
                    }
                }
                $("#premTermArray").val(premTermArray);
                if (!isNaN(transIds.length)) {
                    for (var i = 0; i < transIds.length; i++) {
                        if (document.Form1.selectTransIds[i].checked) {
                            selectTransIds += document.Form1.selectCustomers[i].value + ",";
                            if (customer == "") {
                                customer = document.Form1.selectCustomers[i].value;
                                cherNum = cherNums[i].value;
                            } else if (customer != document.Form1.selectCustomers[i].value) {
                                alert("所选交易属于不同客户，不能合并开票！");
                                return false;
                            }
                            /* 20160715:根据余文丽要求：只要客户名一样就可开票，注释掉此部分 */
                            /*  else if(cherNum != cherNums[i].value){
                            	alert("所选交易的保单号不相同，不能合并开票");
                            	return false;
                            }*/
                            if (fapiaoType == "") {
                                fapiaoType = document.Form1.selectFapiaoTypes[i].value;
                            } else if (fapiaoType != document.Form1.selectFapiaoTypes[i].value) {
                                alert("所选交易之发票类型不一致，不能合并开票！");
                                return false;
                            }
                        }
                    }
                } else {
                    // 列表中仅一笔可供选交易记录
                }
                transToOneBill()
            } else {
                if(flag != false){
                    if(flag != null){
                        alert(flag);
                    }
                }else{
                    alert("请选择交易记录！");
                }
            }
        }
        // [拆分开票]按钮
        function transToManyBill() {
            if (checkChkBoxesSelected("selectTransIds")) {
                var transIds = document.Form1.selectTransIds;
                var selectTransId = "";
                if (!isNaN(transIds.length)) {
                    var selectCount = 0;
                    for (var i = 0; i < transIds.length; i++) {
                        if (document.Form1.selectTransIds[i].checked) {
                            if (selectCount == 1) {
                                alert("只可选择一笔交易进行拆分！");
                                return false;
                            } else {
                                selectCount = selectCount + 1;
                                selectTransId = document.Form1.selectTransIds[i].value;
                            }
                        }
                    }
                } else {
                	alert("进入拆分逻辑！");
                    selectTransId = document.Form1.selectTransIds.value;
                    //判断汇率是否为零
                   /*  if (document.Form1.selectTaxRate.value == "") {
                        alert("税率不存在,不可以拆分");
                        return;
                    } */
                }

                $.ajax({
                    url: "selectTransToOneBill.action",
                    type: 'POST',
                    async: false,
                    data: {
                        selectTransIds: selectTransId
                    },
                    dataType: "json",
                    error: function () {
                        return false;
                    },
                    success: function (result) {
                        if (result.checkFlag == "N") {
                            alert(result.checkResultMsg);
                            return;
                        }
                        /* if(result=="N"){
                         submitAction(document.forms[0], "transToOneBill.action");
                         return;
                         } */
                        if (result.checkFlag == "Y") {
                        	
                            var newUrl = "splitTrans.action?selectTransIds=" + selectTransId;
                            OpenModalWindowSubmit(newUrl, 600, 400, true);
                            document.forms[0].action = "listTrans.action?paginationList.showCount=" + "false";
                        }

                    }
                });


            } else {
                alert("请选择交易记录！");
            }
        }
        
        //【删除】按钮 
        function transtoDelete(){
        	if(checkChkBoxesSelected("selectTransIds")){
        		var transIds = document.getElementsByName("selectTransIds");
        		var selectTransIds = "";
        		//var dataStatus = document.Form1.selectDataStatuses;
        		var dataStatus = document.getElementsByName("selectDataStatuses");
                if(transIds.length>0){
                    for(var i = 0;i < transIds.length; i++){
                        if(transIds[i].checked){
                        	if(dataStatus[i].value == '1'){
                        		selectTransIds += transIds[i].value + ",";
                        	}else{
                        		alert("你选择的记录中有数据处于开票流程中，不能删除");
                        		return;
                        	}
                        }
                    }
                    if(selectTransIds != ""){
                    	if(confirm("确定删除该条记录？")){
                    		$.ajax({
                            	url: "deleteTransData.action",
                                type: 'POST',
                                async: false,
                                data: {
                                    selectTransIds: selectTransIds.substr(0,selectTransIds.length-1)
                                },
                                dataType: "text",
                                error: function () {
                                    return false;
                                },
                                success: function (result) {
                                	if(result=='Y'){
            							alert("删除成功");
            							document.forms[0].submit();
            							return;
            						}else if(result=='N'){
            							alert("对不起,删除过程发生错误,未能删除成功");
            							return;
            						}
                                }
                            });
                    	}
                    }
                }
        	}else{
            	alert("请选择要删除的数据");
            	return;
            }
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
            }
        }
        // 刷新本页面
        function splitTransSuccess() {
            alert("拆分开票保存成功！");
            document.forms[0].submit();
        }

        // [交易冲账]按钮
        function transToReverse() {
            if (checkChkBoxesSelected("selectTransIds")) {
                var transIds = document.Form1.selectTransIds;
                var customers = document.Form1.selectCustomers;
                if (!isNaN(transIds.length)) {
                    var customer = "";
                    var transId = "";
                    for (var i = 0; i < transIds.length; i++) {
                        if (document.Form1.selectTransIds[i].checked) {
                            if (customer == "") {
                                customer = document.Form1.selectCustomers[i].value;
                                transId = document.Form1.selectTransIds[i].value.substring(0, 44);
                            } else if (customer != document.Form1.selectCustomers[i].value) {
                                alert("所选交易属于不同客户，不能一同冲账！");
                                return false;
                            } else {
                                var thisTransId = document.Form1.selectTransIds[i].value.substring(0, 44);
                                if (transId != thisTransId) {
                                    alert("所选交易流水号不通，不能一同冲账！");
                                    return false;
                                }
                            }
                        }
                    }
                }
                if (!confirm("确定对选中交易进行冲账？")) {
                    return false;
                }
                submitAction(document.forms[0], "transToReverse.action?paginationList.showCount=" + "false");
                document.forms[0].action = "listTrans.action?paginationList.showCount=" + "false";
            } else {
                alert("请选择交易记录！");
            }
        }

        $(function () {
            function showMsg() {
                var message = $("#message").val();
                message = message.replace(/^\s*/, '').replace(/\s*$/, '');
                if ("AmtOverFull" == message) {
                    message = "存在交易金额超出单张发票开票金额限额的交易，请做拆分开票。";
                }
                if ("NotExistsTaxRate" == message) {
                    message = "存在税目信息表中无对应税率信息的交易，不能进行开票。";
                }
                if ("NotExistsGoods" == message) {
                    message = "存在无对应发票商品的交易信息，不能进行开票。";
                }
                if ("NotExistsTrans" == message) {
                    message = "不存在对应交易交易信息，不能进行开票。";
                }
                if ("TransStatusError" == message) {
                    message = "交易信息不是未开票状态，不能进行开票。";
                }

                if (null != message && '' !== message) {
                    alert(message);
                }

            }

            showMsg();
        })


    </script>
</head>
<body onkeydow="enterkey(event)">
<form name="Form1" method="post" action="listTrans.action" id="Form1">
    <s:hidden id="message" value="%{message}"></s:hidden>
    <input name="fromFlag" type="hidden" value="list"/>
    <input id="premTermArray" name="premTermArray" type="hidden"/>
    <table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
        <tr>
            <td class="centercondition">
                <s:component template="rocketMessage"/>
                <div id="tbl_current_status">
                    <img src="<%=bopTheme%>/themes/images/icons/icon13.png"/>
                    <span class="current_status_menu">当前位置：</span>
                    <span class="current_status_submenu1">销项税管理</span>
                    <span class="current_status_submenu">开票管理</span>
                    <span class="current_status_submenu">开票申请</span>
                </div>
                <div class="widthauto1">
                    <table id="tbl_query" cellpadding="0" cellspacing="0" width="100%" border="0">
                        <tr>
                            <td style="text-align: right;width: 6%;">借据号</td>
                            <td style="text-align: left;width:14%;"><input
                                    class="tbl_query_text" type="text" name="transInfo.ttmpRcno"
                                    value="<s:property value='transInfo.ttmpRcno'/>" /></td>
                            <td style="text-align: right;width: 6%;">合同号</td>
                            <td style="text-align: left;width:14%;"><input
                                    class="tbl_query_text" type="text" name="transInfo.cherNum"
                                    value="<s:property value='transInfo.cherNum'/>" /></td>
                            <td style="text-align: right;width: 6%;">交易日期</td>
                            <td style="text-align: left;width:20%;"><input
                                    class="tbl_query_time" id="billBeginDate" type="text"
                                    name="transInfo.billBeginDate"
                                    value="<s:property value='transInfo.billBeginDate'/>"
                                    onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'billEndDate\')}'})"
                                    size='11' /> -- <input class="tbl_query_time" id="billEndDate"
                                                           type="text" name="transInfo.billEndDate"
                                                           value="<s:property value='transInfo.billEndDate'/>"
                                                           onfocus="WdatePicker({minDate:'#F{$dp.$D(\'billBeginDate\')}'})"
                                                           size='11' /></td>
                            <td style="text-align: right;width: 6%;"></td>
                            <td style="text-align: left;width: 14%;">
                            <%-- <s:select
                                    name="transInfo.billFreq" list="billFreqlList" listKey="key"
                                    listValue='value' cssClass="tbl_query_text" headerKey=""
                                    headerValue="请选择" /> --%>
                            </td>
                        </tr>
                        <tr>
                            <!-- <td style="text-align: right;">渠道</td>
                            <td style="text-align: left;"><s:select
                                    name="transInfo.chanNel" list="chanNelList" listKey="key"
                                    listValue='value' cssClass="tbl_query_text" headerKey=""
                                    headerValue="请选择" /></td> -->
                            <td style="text-align: right;">客户名称</td>
                            <td style="text-align: left;"><input type="text"
                                                                 class="tbl_query_text" name="transInfo.customerName"
                                                                 value="<s:property value='transInfo.customerName'/>"
                                                                 maxlength="100" /></td>
                            <td style="text-align: right;">交易状态</td>
                            <td style="text-align: left;"><s:select
                                    id="transInfo.dataStatus" name="transInfo.dataStatus"
                                    list="transDataStatusList" headerKey="" headerValue="所有"
                                    listKey='value' listValue='text' cssClass="tbl_query_text" />
                            </td>
                            <td style="text-align: right;">到账日期</td>
                            <td style="text-align: left;"><input class="tbl_query_time"
                                                                 id="hissBeginDte" type="text" name="transInfo.hissBeginDte"
                                                                 value="<s:property value='transInfo.hissBeginDte'/>"
                                                                 onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'billEndDate\')}'})"
                                                                 size='11' /> -- <input class="tbl_query_time" id="hissEndDte"
                                                                                        type="text" name="transInfo.hissEndDte"
                                                                                        value="<s:property value='transInfo.hissEndDte'/>"
                                                                                        onfocus="WdatePicker({minDate:'#F{$dp.$D(\'billBeginDate\')}'})"
                                                                                        size='11' /></td>
                        <!-- </tr>
                        <tr>
                            <td style="text-align: right;">费用类型</td>
                            <td style="text-align: left;"><s:select
                                    name="transInfo.feeTyp" list="feeTypList" listKey="key"
                                    listValue='value' cssClass="tbl_query_text" headerKey=""
                                    headerValue="请选择" /></td>
                            <td style="text-align: right;">数据来源</td>
                            <td style="text-align: left;"><s:select
                                    name="transInfo.dsouRce" list="dsouRceList" listKey="key"
                                    listValue='value' cssClass="tbl_query_text" headerKey=""
                                    headerValue="请选择" /></td> -->

                            <td style="text-align: right;"><input type="button"
                                                                  class="tbl_query_button" value="查询"
                                                                  onMouseMove="this.className='tbl_query_button_on'"
                                                                  onMouseOut="this.className='tbl_query_button'" name="btSubmit"
                                                                  id="btSubmit" onclick="submit()" /></td>
                        </tr>
                    </table>
                </div>
                <table id="tbl_tools" width="100%" border="0">
                    <tr>
                        <td align="left">
                            <a href="#" onclick="transToEachBill()">
                                <img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1010.png"/>
                                开票
                            </a>
                            <a href="#" onclick="transToOneBills()">
                                <img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1011.png"/>
                                合并开票
                            </a>
                            <a href="#" onclick="transToManyBill()">
                                <img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1012.png"/>
                                拆分开票
                            </a>
                            <a href="#" onclick="transtoCustmer()">
                                <img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1013.png"/>
                                关联客户
                            </a>
                          <%--   <a href="#" onclick="transtoDelete()">
                                <img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1019.png"/>
                               删除
                            </a> --%>
                        </td>
                    </tr>
                   <%--  <tr>
                    	<td>
                    		<div sytle="float:left;">
                            	<div style="float:left;">
		                            <span>业务类型&nbsp;</span>
		                            <s:select
		                                    name="business_type" list="applyFeeTypList" listKey="key"
		                                    listValue='value' cssClass="tbl_query_text" headerKey=""
		                                    headerValue="请选择" />
		                            <span>业务编号&nbsp;</span><input
		                                    class="tbl_query_text" type="text" id="business_code" name="business_code"
		                                    value="" />
		                            <span>数据来源&nbsp;</span><s:select
		                                    name="source" list="dsouRceList" listKey="key"
		                                    listValue='value' cssClass="tbl_query_text" headerKey=""
		                                    headerValue="请选择" />
                            	</div>
	                            <a style="margin-left:6px;" href="#" onclick="applyInvoice()">
	                                <img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1010.png"/>
	                                申请
	                            </a>
                            </div>
                         </td>
                    </tr> --%>
                    
                     <tr>
                    	<td>
                    		<div sytle="float:left;">
                            	<div style="float:left;">
		                          
		                            <span>合同号&nbsp;</span><input
		                                    class="tbl_query_text" type="text" id="business_code" name="business_code"
		                                    value="" />
		                           
                            	</div>
	                            <a style="margin-left:6px;" href="#" onclick="applyInvoice()">
	                                <img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1010.png"/>
	                                申请
	                            </a>
                            </div>
                         </td>
                    </tr>
                    <tr></tr>
                </table>
                <div id="lessGridList4" style="overflow: auto">
                    <div id="rDiv" style="width:0px;height:auto;">
                        <table class="lessGrid" cellspacing="0" rules="all" border="0" cellpadding="0"
                               style="border-collapse: collapse; width: 100%;">
                            <tr class="lessGrid head">
                                <th style="text-align:center">
                                    <input style="width:13px;height:13px;" id="CheckAll" type="checkbox"
                                           onclick="cbxselectall(this,'selectTransIds')"/>
                                </th>
                                <th style="text-align:center">序号</th>
                                <th style="text-align:center">借据号 </th>
                                <th style="text-align:center">合同号</th>
                                <!-- <th style="text-align:center">保全受理号</th> -->
                                <th style="text-align:center">交易日期</th>
                                <th style="text-align:center">交易类型</th>
                                <th style="text-align:center">客户名称</th>
                                <th style="text-align:center">交易金额</th>
                                <th style="text-align:center">税率</th>
                                <th style="text-align:center">税额</th>
                                <th style="text-align:center">价税合计</th>
                                <th style="text-align:center">未开票金额</th>
                                <th style="text-align:center">交易状态</th><!--
                                <th style="text-align:center">渠道</th>
                                <th style="text-align:center">费用类型</th>-->
                                <th style="text-align:center">到账日期</th>
                                <!-- <th style="text-align:center">缴费频率</th> -->
                                <th style="text-align:center">月度</th>
                                <th style="text-align:center">期数</th>
                                <!--<th style="text-align:center">数据来源</th>
                                --><th style="text-align:center">明细</th>
                            </tr>
                            <s:iterator value="paginationList.recordList" id="iList" status="stuts">
                                <tr align="center" title="<s:property value="transId"/>"
                                    id='<s:property value="transId"/>'
                                    class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
                                    <s:set value="@com.cjit.vms.trans.util.DataUtil@BILL_STATUS_2"
                                           id="status2"></s:set>
                                    <s:set value="#status2 eq dataStatus?true:false" id="isDataStatus2"></s:set>
                                    <s:set value="balance==0?true:false" id="isEmptyBalance"></s:set>
                                    <td align="center">
                                        <input style="width:13px;height:13px;" class="selectTransIds" type="checkbox"
                                               name="selectTransIds" value='<s:property value="transId"/>'
                                                <s:property value="#isDataStatus2&&#isEmptyBalance?'disabled':''"/>
                                        />
                                        <s:hidden name="selectCustomers" value="%{customerId}"></s:hidden>
                                        <!-- 保单号 -->
                                        <s:hidden name="selectCherNum" value="%{cherNum}"></s:hidden>
                                        <s:hidden name="selectCustomerTypes" value="%{customerType}"></s:hidden>
                                        <s:hidden name="selectFapiaoTypes" value="%{fapiaoType}"></s:hidden>
                                        <s:hidden name="selectDataStatuses" value="%{dataStatus}"></s:hidden>
                                        <s:hidden name="selectIncome" value="%{income}"></s:hidden>
                                        <s:hidden name="selectBalance" value="%{balance}"></s:hidden>
                                        <s:hidden name="selectPremTerm" value="%{premTerm}"></s:hidden>
                                        <input name="selectTaxRate" type="hidden"
                                               value="<fmt:formatNumber value="${taxRate}" pattern="0.0000"></fmt:formatNumber>">
                                    </td>
                                    <s:property value=""/>
                                    <td align="center">
                                        <s:property value="#stuts.index+1"/>
                                    </td>
                                    <td align="center"><s:property value="ttmpRcno"/></td>
                                    <td align="center" name="cherNum"><s:property value="cherNum"/></td>
                                    <%-- <td align="center"><s:property value="repNum"/></td> --%>
                                    <td align="center" class="transDate"><s:property value="transDate"/></td>
                                    <td align="center"><s:property value="transTypeName"/></td>
                                    <td align="left"><s:property value="customerName"/></td>
                                    <td align="right" name="incomeCcy"><s:property value="incomeCcy"/></td>
                                    <td align="right"><s:property value="taxRate"/></td>
                                    <td align="right" name="taxAmt"><s:property value="taxAmt"/></td>
                                    <td align="right"><s:property value="amt"/></td>
                                    <td align="right"><s:property value="balance"/></td>
                                    <td >
                                    <s:if test='dataStatus=="1"'>未开票</s:if>
							        <s:if test='dataStatus=="2"'>已审核</s:if>
							        <s:if test='dataStatus=="3"'>已审核</s:if>
                                    </td>
                                    <!--
                                    <td align="center"><s:property value="chanNelCh"/></td>
                                    <td align="center"><s:property value="feeTypCh"/></td>
                                    <td hidden align="center" name="feeTyp"><s:property value="feeTyp"/></td>-->
                                    <td align="center"><s:property value="hissDte"/></td>
                                    <%-- <td align="center"><s:property value="billFreqCh"/></td> --%>
                                     <td align="center"><s:property value="polYear"/></td> 
                                    <td align="center"><s:property value="premTerm"/></td>
                                    
                                    <td align="center">
                                        <a href="#" onclick="goToPage('transDetail.action?transId=<s:property value="transId"/> ')">
                                            <img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1014.png"
                                                 title="查看" style="border-width: 0px;"/>
                                        </a>
                                    </td>
                                </tr>
                            </s:iterator>
                        </table>
                    </div>
                </div>
                <input type="hidden" name="paginationList.showCount" value="false">
                <div id="anpBoud" align="Right" style="overflow:auto;width:100%;">
                    <table width="100%" cellspacing="0" border="0">
                        <tr>
                            <td align="right">
                                <s:component template="pagediv"/>
                            </td>
                        </tr>
                </div>
            </td>
        </tr>
    </table>
</form>
<form name="Form2" method="post" action="applyInvoice.action" id="Form2">
	<s:hidden id="message" value="%{message}"></s:hidden>
    <input name="fromFlag" type="hidden" value="list"/>
	<input name="business_code2" id="business_code2" type="hidden" value=""/>
</form>
<c:import url="${webapp}/page/webctrl/instTree/tree_include.jsp" charEncoding="UTF-8">
    <c:param name="treeType" value="radio"/>
    <c:param name="bankName_tree" value="inst_Name"/>
    <c:param name="bankId_tree" value="inst_id"/>
    <c:param name="taskId_tree" value=""/>
    <c:param name="task_tree" value="task_tree"/>
    <c:param name="webapp" value="${webapp}"/>
</c:import>
</body>
</html>