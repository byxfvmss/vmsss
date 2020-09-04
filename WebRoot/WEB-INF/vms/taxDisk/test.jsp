
<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
         import="java.math.BigDecimal"
         import="com.opensymphony.util.BeanUtils"
         import="com.cjit.common.constant.ScopeConstants"
         import="com.cjit.common.util.NumberUtils"
         import="com.cjit.common.util.PaginationList"
         import="com.cjit.vms.trans.model.BillInfo"
         import="com.cjit.vms.trans.util.DataUtil"
         contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="../../../page/include.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
    <title>增值税管理平台</title>
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
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/taxSelver/compareCurrentBill.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/taxSelver/billCancel.js"></script>
	<!-- 税控软件 -->
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/billinterface/billInterfaceAjax.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/billinterface/bw_disk.js"></script>
	
    <script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
    <script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/vms.js"></script>
    <script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/billinterface/<%=(String) request.getAttribute("jspParam")%>.js"></script>
    
    <script type="text/javascript">
        var billInterface = new BillInterface();
	    billInterface.init({});
        // [查询]按钮  [导出]按钮
        function submitForm(actionUrl) {
            submitAction(document.forms[0], actionUrl);
            document.forms[0].action = "listIssueBill.action?paginationList.showCount=" + "false";
        }

      //开具按钮
        function issueBill() {
        	
        	var billIds = document.getElementsByName("selectBillIds");
			var fapiaoTypes = document.getElementsByName("fapiaoTypes");
			var fapiaoType = "";
			if(fapiaoTypes.length>0){
				fapiaoType = fapiaoTypes[0].value;
			}
			var ids="";
				
			for(var i=0;i<billIds.length;i++){
				if (billIds[i].checked){
					ids = ids === "" ? billIds[i].value : ids + "," + billIds[i].value;
				}
			}
				
			if(ids==''){
				alert("请选择票据记录");
				return false;
			}
			if(confirm("确认开具？")){
				billInterface.createBillissue({ids:ids,fapiaoType:fapiaoType});
			}
		    submitAction(document.forms[0], "listIssueBill.action?paginationList.showCount="+"false");
        }
        </script>
</head>

</html>