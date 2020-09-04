<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" 
	import="java.util.*" 
	import="com.cjit.vms.system.model.InitRunningLog"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<title>增值税管理平台</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
    <script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
    <script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/comm.js"></script>
    <script language="javascript" type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
    <script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/ylb.js"></script>
	<script language="javascript" type="text/javascript">
	function initData(){
		var dataDate = document.getElementById('dataDate').value;
		document.getElementById('dataDate').disabled = true;
		document.getElementById('btInit').disabled = true;
		document.getElementById('btClear').disabled = true;
		document.all.slowShow.style.display = "block";
		submitAction(document.forms[0],'initData.action?dataDate=' + dataDate);
	}
	function submit(){
		document.forms[0].submit();
	}
	function tips(){
	<%
		Object message = request.getAttribute("message");
		if (message == null){
			message = "";
		}else{
			message = String.valueOf(message);
		}
	%>
		var msg = '<%=message%>';
		if(msg != null && msg.length>0){
			alert(msg);
		}
	}
	</script>
    <style type="text/css">
        .style1{
            width: 242px;
        }
    </style>
	<%
		List initLogList = (List)request.getAttribute("initLogList");
		String dataDate = (String)request.getAttribute("dataDate");
		String buttonValue = "加载数据";
		if (initLogList != null && initLogList.size() > 0){
			InitRunningLog initLog = (InitRunningLog)initLogList.get(0);
			if (dataDate != null && initLog.getDataDate().equals(dataDate.replaceAll("-", ""))){
				buttonValue = "重新加载数据";
			}
		}
	%>
</head>
<body onLoad="javascript:tips()">
    <form name="Form1" method="post" action="initDataList.action" id="Form1">
    	<input type="hidden" name="busiDataType" value="<s:property value='busiDataType'/>"/>
	<table id="tbl_main" cellpadding="0" cellspacing="0">
	<tr>
	    <td align="left">
	    <table id="tbl_current_status">
		<tr>
			<td>
				<img src="<%=bopTheme2%>/img/jes/icon/home.png" />
				<span class="current_status_menu">当前位置：</span> 
				<span class="current_status_submenu">数据加载
					<span class="actionIcon">-&gt;</span>数据加载
				</span>
			</td>
		</tr>
		</table>
	    </td>
    </tr>
    <tr>
	    <td align="left">
	    <table id="tbl_query" cellpadding="0" cellspacing="0" width="80%" border="0">
		<tr>
            <td align="left">
				数据日期：
				<input class="tbl_query_time" id="dataDate" type="text" name="dataDate"
					value="<s:property value='dataDate'/>"
					onfocus="WdatePicker()" size='11'" onchange="submit()"/>
			</td>
      		<td style="width:200px;" align="right">
				<input type="button" class="tbl_query_button" value="<%=buttonValue%>"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="btInit" id="btInit" onclick="initData()" />
				<div type="btn" img="<%=bopTheme%>/image/button/view.gif" 
					onclick="clearData()" name="btClear" value="清除" id="btClear" style="display: none"></div>
            </td>
        </tr>
	    </table>
	    </td>
    </tr>
    </table>
    <div style="overflow:auto;SCROLLBAR-FACE-COLOR:#B7B7B7;width:100%;"id="list1">
	<script type="text/javascript">
		document.getElementById("list1").style.height = screenHeight - 350;
	</script>
    <table id="lessGridList" width="100%" class="lessGrid" rules="all" 
    	cellspacing="0" border="0" cellpadding="0" display="none" style="border-collapse:collapse;">
	<tr class="lessGrid head">
		<th width="15%">执行用户ID</th>
		<th width="20%">数据日期</th>
		<th width="20%">执行时间</th>
		<th width="45%">执行情况</th>
	</tr>
	<%
		if (initLogList != null && initLogList.size() > 0){
			for (int i = 0; i < initLogList.size(); i++){
				InitRunningLog runLog = (InitRunningLog)initLogList.get(i);
				String trClass = "";
				if(i%2==0){
					trClass = "lessGrid rowA";
				}else{
					trClass = "lessGrid rowB";
				}
	%>
	<tr class="<%=trClass%>">
		<td><%=runLog.getUserId()%></td>
		<td><%=runLog.getDataDate()%></td>
		<td><%=runLog.getRunTime()%></td>
		<td><%=runLog.getDescription()%></td>
	</tr>
	<%
			}
		}
	%>
	</table>
    </div>
	<div id="slowShow" style="position:absolute;left:35%;top:35%;width:100%;display: none;z-index: 100;">
	<table width="100%" height="100%" align="center">
		<font size="3" color="blue"><strong>正在加载数据，在完成前请勿执行其他操作！</strong></font>
		<img src="<%=webapp%>/page/LoaderBar.gif">
	</div>
    </form>
</body>
</html>