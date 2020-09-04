<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<!--
	createTime:2016.2.
	author:沈磊
	content: 单证号生成
  -->
<html>
<head>
<title>增值税管理平台</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../../../page/include.jsp"%>
<%@ include file="../../../../page/modalPage.jsp"%>
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
<link href="<%=webapp%>/page/common.css" type="text/css" rel="stylesheet">
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
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<meta http-equiv="Pragma" content="no-cache" />
<script type="text/javascript">
	var flag="<s:property value='documentManageInfo.curNumBegin'/>";
	if(flag!=null&&flag!=""){
	$("#fisrt").hide();
	}
	function turnBack(){
		window.close();
		//document.forms[0].action="manageInsureCode.action?fromFlag=menu";
		//document.forms[0].submit();
	}
	function createcode(){
		document.forms[0].action="createCodeNum.action";
		document.forms[0].submit();
	}
	function check(){
		if(!/^[0-9]*$/.test($("#countNum").val())){
			return $("#countNum").val("");
		}
	}
</script>
</head>
<body>
	<form name="Form1" method="post" action="createKeyCode.action" id="Form1" enctype="multipart/form-data">
		<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
			<tr>
				<td class="centercondition">
					 <s:component template="rocketMessage" />
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" />
						<span class="current_status_menu">当前位置：</span>
						<span class="current_status_submenu1">VMS单证管理</span>
						<span class="current_status_submenu">单证管理</span>
						<span class="current_status_submenu">投保单号保险单证号生成</span>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" width="80%" border="0">
							<tr>
								<td align="left">单证类型</td>
								<td>
									<s:select name="documentManageInfo.type" list="typeList" listKey="key"
                                          listValue='value' cssClass="tbl_query_text5" headerKey="" />
								</td>
								<td align="left">机构</td>
								<td>
									<s:if test="authInstList != null && authInstList.size > 0">
                                    <s:select name="documentManageInfo.instId" list="authInstList" listKey="instId"
                                              listValue='instName' cssClass="tbl_query_text5"
                                              headerKey="" headerValue="全部"/>
                                </s:if>
                                <s:if test="authInstList == null || authInstList.size == 0">
                                    <select name="documentManageInfo.instId" class="readOnlyText">
                                        <option value="">请分配机构权限</option>
                                    </select>
                                </s:if>
								</td>
								
							</tr>
							<tr>
								<td align="left">渠道</td>
								<td>
									<s:select name="documentManageInfo.channel" list="chanNelList" listKey="key"
                                          listValue='value' cssClass="tbl_query_text5" headerKey="" headerValue="请选择"/>
								</td>
								<td>合作机构</td>
								<td>
									<input type="text" class="tbl_query_text" name='documentManageInfo.bank' value='<s:property value="documentManageInfo.bank"/>'  />
								</td>
								
							</tr>
							<tr>
								<td align="left">数量</td>
								<td>
									<input type="text"  maxlength="10" class="tbl_query_text" id="countNum" name='countNum' value='<s:property value='documentManageInfo.countNum' />' onkeyup="check();" />
								</td>
								<td align="left"></td>
								<td>
									
								</td>	
							</tr>
						</table>
					</div>
					<table id="tbl_tools" width="100%" border="0">
						<tr>
							<td align="center" width="25%" >
								<a href="#" name="upLoad" id="fisrt" onclick="createcode()">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />
									生成
								</a>
							</td>
						</tr>
					</table>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" width="80%" border="0">
							<tr disabled="disabled">
								<td  width="5%">号段</td>
								<td width="5%" >
									<input type="text" class="tbl_query_text" id="create"  value='<s:property value="documentManageInfo.curNumBegin"/>'  />
								</td>								
								<td width="5%">
									<input type="text" class="tbl_query_text"  value="<s:property value='documentManageInfo.curNumEnd'/>"  />
								</td>
								<td width="5%"></td>
								<td width="5%"></td>
								<td width="5%"></td>
							</tr>
						</table>
					</div>
					<table id="tbl_tools" width="100%" border="0">
						<tr>
							<td width="25%"></td>
							<td width="25%"></td>
							<td width="25%"></td>
							<td align="center" width="25%">
								<a href="#" name="upLoad" id="upLoad" onclick="turnBack()">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />
									关闭
								</a>
							</td>
						</tr>
					</table>
			</tr>
		</table>
	</form>
</body>
</html>