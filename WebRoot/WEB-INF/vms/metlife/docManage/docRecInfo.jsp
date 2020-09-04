<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<!--
	createTime:2016.2.
	author:沈磊
	content:领用人弹窗 metlife
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
	function querylist(){
		document.forms[0].action="takecode.action";
		document.forms[0].submit();
	}
	function changeStatus(){
	if (checkChkBoxesSelected("ruleId")) {
                if (checkChkBoxesSelectedOne("ruleId") == 1) {
                    var ruleIds = document.getElementsByName("ruleId");
                    var ruleId;
                    for (i = 0; i < ruleIds.length; i++) {
                        if (ruleIds[i].checked) {
                            ruleId = ruleIds[i].value;
 						
                        }
                    }
                //document.forms[0].action="changeStatusx.action?vdName="+ruleId;
				//document.forms[0].submit();
				$("#vdName").val(ruleId);
				$.ajax({
				url: "<%=webapp%>/changeStatusx.action?",
				type: "POST",
				data:$('#Form1').serialize(),
				success:function(data){
				window.dialogArguments.querylist();
// 				CloseWindow();
				window.close();
			}
		});
                } else {
                    alert("请选择一条交易记录");
                }

            } else {
                alert("请选择交易记录！");
            }
	
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
</script>
</head>
<body>
	<form name="Form1" method="post" action="takecode.action" id="Form1" >
	<input type="hidden" name="vdName" id="vdName" />
		<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh10" height="400px;">
			<tr height="400px;">
				<td class="centercondition">
					<s:component template="rocketMessage" />
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" />
						<span class="current_status_menu">当前位置：</span>
						<span class="current_status_submenu1">VMS单证管理</span>
						<span class="current_status_submenu">单证管理</span>
						<span class="current_status_submenu">领用人</span>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" width="80%" border="0">
							<tr>
								<td align="left">领用人编号</td>
								<td>
									<input type="text" class="tbl_query_text" name='documentManageInfo.vdriId' value='<s:property value="documentManageInfo.vdriId"/>'  />
								</td>
								<td align="left">领用人名称</td>
								<td>
								<input type="text" class="tbl_query_text" name='documentManageInfo.vdriName' value='<s:property value="documentManageInfo.vdriName"/>'  />
								</td>
								
								<td>
								<input type="hidden" name="num" value="<s:property value="documentManageInfo.num"/>">
								<input type="hidden" name="status" value="<s:property value="documentManageInfo.vdriStatus"/>">
								</td>
								<td><input type="button" class="tbl_query_button" value="查询" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" name="BtnView" id="BtnView"
										onclick="querylist()"
									/></td>
								<td>
								</td>
							</tr>
						</table>
					</div>
					<div id="lessGridList2" style="overflow: auto; width: 100%;height:100px;"  >
						<table class="lessGrid" cellspacing="0" rules="all" border="0" cellpadding="0" style="border-collapse: collapse;">
							<tr>
								<th width="3%" style="text-align:center">
									<input id="CheckAll" style="width:13px;height:13px;" type="checkbox" onClick="checkAll(this,'ruleId')" />
								</th>
								<th style="text-align:center" width="5%">领用人编号</th>
								<th style="text-align:center" width="5%">领用人名称</th>
							</tr>
							<s:iterator value="paginationList.recordList" id="iList" status="stuts">
								<tr id="<s:property value="num"/>">
									<td style="text-align:center">
										<input type="checkbox" style="width:13px;height:13px;" name="ruleId" value="<s:property value="vdriId"/>" />
									</td>
									<td>
										<s:property value='vdriId' />
									</td>
									<td><s:property value='vdriName' /></td>
								</tr>
							</s:iterator>
						</table>
					</div>
					<div id="anpBoud" align="Right" style="overflow:auto;width:100%;">
						<table width="100%" cellspacing="0" border="0">
							<tr>
								<td align="right">
									<s:component template="pagediv" />
								</td>
							</tr>
						</table>
					</div>
					<table id="tbl_tools" width="100%" border="0">
						<tr>
							<td align="center" width="10%">
								<a href="#" name="upLoad" id="upLoad" onclick="changeStatus()">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />
									领用
								</a>
							</td>
							
							<td align="center" width="5%">
						
							</td>
						
							<td align="center" width="5%">
							
							</td>
							<td align="center" width="5%">
						
							</td>
							<td align="center" width="5%">
						
							</td>
							<td align="center" width="35%"></td>
						</tr>
					</table>
				</td>	
			</tr>
		</table>
	</form>
</body>
</html>