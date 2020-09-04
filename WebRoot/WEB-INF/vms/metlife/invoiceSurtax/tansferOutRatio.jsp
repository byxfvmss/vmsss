<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<!--
	createTime:2016.3.
	author:沈磊
	content:转出比例 metlife
  -->
<html>
<head>
<title>增值税管理平台</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../../../page/include.jsp"%>

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
		document.forms[0].action="transferOutRatioInfo.action";
		document.forms[0].submit();
	}
	function edit(){
// 	if (checkChkBoxesSelected("ruleId")) {
// 			 var vtorIds = document.getElementsByName("ruleId");
// 			 var vtorId="";
//                     for (i = 0; i < vtorIds.length; i++) {
//                         if (vtorIds[i].checked == true) {
			
//                            vtorId += vtorIds[i].value+",";
//                         }
//                     }
//             vtorId=vtorId.substring(0,vtorId.length-1);
        document.forms[0].action="updatetransferOutRatio.action";
		document.forms[0].submit();
// 	}else{
// 		alert("请选择数据!");
// 	}
	
	}
</script>
</head>
<body>
	<form name="Form1" method="post" action="transferOutRatioInfo.action" id="Form1" enctype="multipart/form-data">
		<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
			<tr>
				<td class="centercondition">
					<s:component template="rocketMessage" />
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" />
						<span class="current_status_menu">当前位置：</span>
						<span class="current_status_submenu1">VMS进项管理</span>
						<span class="current_status_submenu">进项管理</span>
						<span class="current_status_submenu">转出比例</span>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" width="80%" border="0">
							<tr>
								<td align="left">财务月</td>
								<td>
									<input id="billCode" name="inputInvoiceInfo.accountPeriod" type="text" class="tbl_query_text" value="<s:property value='inputInvoiceInfo.accountPeriod' />"  />
								</td>
								<td align="left">财务月起止日期</td>
								<td align="right">
								<input id="startDate" class="tbl_query_time"  type="text"
                                       name="inputInvoiceInfo.accountPeriodStrart"
                                       value="<s:property value='inputInvoiceInfo.accountPeriodStrart'/>"
                                       onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}'})"
                                       size='11'/>~
                                       <input id="endDate" class="tbl_query_time"  type="text" name="inputInvoiceInfo.accountPeriodEnd"
                                       value="<s:property value='inputInvoiceInfo.accountPeriodEnd'/>"
                                       onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}'})"
                                       size='11'/>	
								</td>
								<td >
									纳税人识别号
								</td>
								<td >
									<input id="billCode" name="inputInvoiceInfo.taName" type="text" class="tbl_query_text" value="<s:property value='inputInvoiceInfo.taName' />"  />
								</td>
								<td>机构</td>
								<td>
									<s:if
										test="authInstList != null && authInstList.size > 0">
										<s:select name="inputInvoiceInfo.instId" list="authInstList"
											listKey="instId" listValue='instName'
											cssClass="tbl_query_text5" headerKey="" headerValue="全部" />
									</s:if> <s:if test="authInstList == null || authInstList.size == 0">
										<select name="inputInvoiceInfo.instId" class="readOnlyText">
											<option value="">请分配机构权限</option>
										</select>
									</s:if>
								</td>
							</tr>
							<tr>
								<td></td>
								<td>
									
								</td>
								<td></td>
								<td>
									
								</td>
								<td></td>
								<td>
								</td>
								<td>
								</td>
								<td>
									<input type="button" class="tbl_query_button" value="查询" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" name="BtnView" id="BtnView"
										onclick="querylist()"/>
								
								</td>
							</tr>
						</table>
					</div>
					<table id="tbl_tools" width="100%" border="0">
						<tr>
							<td align="center" >
								<a href="#" name="upLoad" id="upLoad" onclick="edit()">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />
									计算
								</a>
							</td>
							<td align="center" >
							</td>
							<td align="center" >
							</td>
							<td align="center" ></td>
							<td align="center" ></td>
							<td align="center" width="50%"></td>
						</tr>
					</table>
					<div id="lessGridList4" style="overflow: auto; width: 100%;">
						<table class="lessGrid" cellspacing="0" rules="all" border="0" cellpadding="0" style="border-collapse: collapse;width: 100%;">
							<tr class="lessGrid head">
<!-- 								<th width="3%" style="text-align:center"> -->
<!-- 									<input id="CheckAll" style="width:13px;height:13px;" type="checkbox" onClick="checkAll(this,'ruleId')" /> -->
<!-- 								</th> -->
								<th style="text-align:center" width="5%">财务月</th>
								<th style="text-align:center" width="5%">机构</th>
								<th style="text-align:center" width="5%">纳税人识别号</th>						
								<th style="text-align:center" width="5%">免税收入</th>
								<th style="text-align:center" width="5%">总收入</th>
								<th style="text-align:center" width="5%">转出比例</th>
<!-- 								<th style="text-align:center" width="5%">转出金额</th> -->
							</tr>
							
							<s:iterator value="paginationList.recordList" id="iList" status="stuts">
								<tr>
<!-- 									<td style="text-align:center;width:13px;"> -->
<!-- 										<input type="checkbox" style="width:13px;height:13px;" name="ruleId" value="<s:property value="vtorId"/>" /> -->
<!-- 									</td> -->
									<td style="text-align:center"><s:property value='accountPeriod' /></td>
									<td style="text-align:center"><s:property value='instId' /></td>
									<td style="text-align:center"><s:property value='taName' /></td>
									<td style="text-align:center"><s:property value='taxExemptAmt' /></td>
									<td style="text-align:center"><s:property value='vtoiSumAmt' /></td>
									<td style="text-align:center"><s:property value='getTransferRatioToString'/></td>
<!-- 									<td style="text-align:center"><s:property value='transferOutAmt' /></td> -->
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
			</tr>
		</table>
	</form>
</body>
</html>