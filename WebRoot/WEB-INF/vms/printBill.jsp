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
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'printBill.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  <script type="text/javascript">
	function OperateDisk(){
	}
  </script>
  <body>
	<%
		String SKPXXCX = FileUtil.createXmlStringForSKPXXCX(FileUtil.skpkl);
		String res = "";
		System.out.println("SKPXXCX:");
		System.out.println(SKPXXCX);
	%>
    <table>
    	<input type="hidden" name=""
    <tr>
    <td>
		<OBJECT ID="BPrinter" WIDTH=0 HEIGHT=0
			CODEBASE="BPrinter.CAB#Version=1,0,0,1"
			CLASSID="CLSID:BE362B78-BABC-494E-9BB9-567AE6D93384"></OBJECT>
    </td>
    <td>
    	<input type="button" id="PrintBtn" value="操作税控盘" onclick="OperateDisk()" >
    </td>
    </tr>
    </table>
  </body>
</html>
