<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<!--
	createTime:2016.2.
	author:沈磊
	content:投保单号保险单证号管理 metlife
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
		document.forms[0].action="manageInsureCode.action";
		document.forms[0].submit();
	}
	function createNum(){
		OpenModalWindow("createNum.action",750,400,true);
		//document.forms[0].action="createNum.action";
		//document.forms[0].submit();
	}
	//入库
	function changeStatuslist(){
		var flag=false;
		var res = "入库成功";
		var resError = "入库失败";
		if (checkChkBoxesSelected("ruleId")) {
			var ruleId="";
			var num="";
			var status="";
			var type="";
			var channel="";
            var inputs = document.getElementsByName("ruleId");
			for ( var i = 0; i < inputs.length; i++) {
				if (inputs[i].checked == true) {
				  $("#" + inputs[i].value).find("td").each(function (index, element) {
                        if (index == 6) {
                           status=$.trim($(this).text()); 
                        }
                      	if(index==1){
                        	type=$.trim($(this).text());
                        }
                        if(index==5){
                        	channel=$.trim($(this).text());
                        }
                    });
                        if(status=="已生成"){                       
                        		res="";
                        		flag=true;
                        }
                        if(type=="银保通合同号"&&status=="领用"){
                        		alert(2);
                        		flag=true;
                        }
                        if(type=="投保单号"&&channel=="DM"&&status=="领用"){
                        		alert(3);
                        		flag=true;
                        }           
					if(!flag){
					 alert("数据状态必须为已生成或银保通号!");
                     return;
					}
					num += inputs[i].value + ",";
				}
			}
			
			num=num.substring(0,num.length-1)
			if(confirm("是否确认入库?")){
			document.forms[0].action="changeStatus.action?num="+num+"&status=1";
			document.forms[0].submit();
			document.forms[0].action="manageInsureCode.action";
			}else{
				return;
			}
		}else{
		alert("请选择数据!");
		}
	}
	//分发
	function distribute(){
		var flag=false;
		var res = "";
		var resError = "";
		if (checkChkBoxesSelected("ruleId")) {
			var ruleId="";
			var num="";
			var status="";
			var type="";
			var channel="";
            var inputs = document.getElementsByName("ruleId");
			for ( var i = 0; i < inputs.length; i++) {
				if (inputs[i].checked == true) {
				  $("#" + inputs[i].value).find("td").each(function (index, element) {
                        if (index == 6) {
                           status=$.trim($(this).text()); 
                        }
                      	if(index==1){
                        	type=$.trim($(this).text());
                        }
                        if(index==5){
                        	channel=$.trim($(this).text());
                        }
                    });
                     
                        if(type=="银保通合同号"&&status=="已入库"){
                        		
                        		flag=true;
                        }
                        if(type=="投保单号"&&channel=="DM"&&status=="已入库"){
                        	
                        		flag=true;
                        }           
					if(!flag){
					 alert("数据状态必须为已入库并且为银保通合同号或渠道为银保通!");
                     return;
					}
					num += inputs[i].value + ",";
				}
			}
			
			num=num.substring(0,num.length-1)
			if(confirm("是否确认分发?")){
			OpenModalWindowReloadByForm("distribute.action?fromFlag=menu&num="+num+"&status=2", 450,100, true);
			//document.forms[0].action="changeStatus.action?num="+num+"&status=2";
			//document.forms[0].submit();
			}else{
				return;
			}
		}else{
		alert("请选择数据!");
		}
	} 
	//领用
	function takecode(){
		var flag=false;
		if (checkChkBoxesSelected("ruleId")) {
			var ruleId="";
			var num="";
			var status="";
			var type="";
			var channel="";
            var inputs = document.getElementsByName("ruleId");
			for ( var i = 0; i < inputs.length; i++) {
				if (inputs[i].checked == true) {
				  $("#" + inputs[i].value).find("td").each(function (index, element) {
                        if (index == 6) {
                           status=$.trim($(this).text()); 
                        }
                      	if(index==1){
                        	type=$.trim($(this).text());
                        }
                        if(index==5){
                        	channel=$.trim($(this).text());
                        }
                    });
                
                        if(status=="已入库"){
                        		
                        		flag=true;
                        }
                        if(status=="已分发"){
                        		
                        		flag=true;
                        }           
					if(!flag){
					 alert("数据状态必须为已入库或已分发!");
                     return;
					}
					num += inputs[i].value + ",";
				}
			}
			
			num=num.substring(0,num.length-1)
			if(confirm("是否确认领取?")){
			OpenModalWindowReloadByForm("takecode.action?fromFlag=menu&num="+num+"&status=3", 750, 400, true);
			//document.forms[0].action="takecode.action?num="+num+"&status=3";
			//document.forms[0].submit();
			}else{
				return;
			}
		}else{
		alert("请选择数据!");
		}
	}
	
	function transTo(){
		var flag=false;
		if (checkChkBoxesSelected("ruleId")) {
			var ruleId="";
			var num="";
			var status="";
			var orderNum="";
			var type="";
			var channel="";
            var inputs = document.getElementsByName("ruleId");
			for ( var i = 0; i < inputs.length; i++) {
				if (inputs[i].checked == true) {
				  $("#" + inputs[i].value).find("td").each(function (index, element) {
                        if (index == 6) {
                           status=$.trim($(this).text()); 
                        }
                      	if(index==1){
                        	type=$.trim($(this).text());
                        }
                        if(index==5){
                        	channel=$.trim($(this).text());
                        }
                        if(index==3){
                        	orderNum+=$.trim($(this).text())+",";
                        }
                    });
                
                        if(status=="已领用"&&type=="投保单号"){
                        		
                        		flag=true;
                        }
                        
					if(!flag){
					 alert("数据状态必须为投保单号!");
                     return;
					}
					num += inputs[i].value + ",";
				}
			}
			orderNum=orderNum.substring(0,orderNum.length-1);
			num=num.substring(0,num.length-1);
			if(confirm("是否确认调拨?")){
			OpenModalWindow("transTo.action?fromFlag=menu&num="+num+"&status=4&orderNum="+orderNum, 650, 400, true);
			//document.forms[0].action="takecode.action?num="+num+"&status=3";
			//document.forms[0].submit();
			}else{
				return;
			}
		}else{
		alert("请选择数据!");
		}
	
	}
	//遗失
	function loseObject(){
		var flag=false;
		if (checkChkBoxesSelected("ruleId")) {
			var ruleId="";
			var num="";
			var status="";
			var type="";
			var channel="";
			var orderNum="";
            var inputs = document.getElementsByName("ruleId");
			for ( var i = 0; i < inputs.length; i++) {
				if (inputs[i].checked == true) {
				  $("#" + inputs[i].value).find("td").each(function (index, element) {
                        if (index == 6) {
                           status=$.trim($(this).text()); 
                        }
                      	if(index==1){
                        	type=$.trim($(this).text());
                        }
                        if(index==5){
                        	channel=$.trim($(this).text());
                        }
                        if(index==3){
                        	orderNum+=$.trim($(this).text())+",";
                        }
                    });
                
                        if(status=="已生成"||status=="已入库"||status=="已领用"||status=="已调拨"){
                        		
                        		flag=true;
                        }    
					if(!flag){
					 alert("数据状态不可用!");
                     return;
					}
					num += inputs[i].value + ",";
				}
			}
			orderNum=orderNum.substring(0,orderNum.length-1);
			num=num.substring(0,num.length-1)
			if(confirm("是否确认遗失?")){
			document.forms[0].action="changeStatusy.action?num="+num+"&status=5&orderNum="+orderNum;
			document.forms[0].submit();
			}else{
				return;
			}
		}else{
		alert("请选择数据!");
		}
	}
	//作废
	function cancel(){
		var flag=false;
		if (checkChkBoxesSelected("ruleId")) {
			var ruleId="";
			var num="";
			var status="";
			var type="";
			var channel="";
			var orderNum="";
            var inputs = document.getElementsByName("ruleId");
			for ( var i = 0; i < inputs.length; i++) {
				if (inputs[i].checked == true) {
				  $("#" + inputs[i].value).find("td").each(function (index, element) {
                        if (index == 6) {
                           status=$.trim($(this).text()); 
                        }
                      	if(index==1){
                        	type=$.trim($(this).text());
                        }
                        if(index==5){
                        	channel=$.trim($(this).text());
                        }
                        if(index==3){
                        	orderNum+=$.trim($(this).text())+",";
                        }
                    });
                
                        if(status=="已生成"||status=="已入库"||status=="已领用"||status=="已调拨"){
                        		
                        		flag=true;
                        }    
					if(!flag){
					 alert("数据状态不可用!");
                     return;
					}
					num += inputs[i].value + ",";
				}
			}
			orderNum=orderNum.substring(0,orderNum.length-1);
			num=num.substring(0,num.length-1)
			if(confirm("是否确认作废?")){
			document.forms[0].action="changeStatusy.action?num="+num+"&status=6&orderNum="+orderNum;
			document.forms[0].submit();
			}else{
				return;
			}
		}else{
		alert("请选择数据!");
		}
	}
</script>
</head>
<body>
	<form name="Form1" method="post" action="manageInsureCode.action" id="Form1" enctype="multipart/form-data">
		<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
			<tr>
				<td class="centercondition">
					<s:component template="rocketMessage" />
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" />
						<span class="current_status_menu">当前位置：</span>
						<span class="current_status_submenu1">VMS单证管理</span>
						<span class="current_status_submenu">单证管理</span>
						<span class="current_status_submenu">投保单号保险单证号管理</span>
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
								<td align="left">渠道</td>
								<td>
									<s:select name="documentManageInfo.channel"
										list="chanNelList" listKey="key" listValue='value'
										cssClass="tbl_query_text5" headerKey="" headerValue="请选择" />
								</td>
								<td>合作机构</td>
								<td>
									<input type="text" class="tbl_query_text" name='documentManageInfo.bank' value='<s:property value="documentManageInfo.bank"/>'  />
								</td>
							</tr>
							<tr>
								<td>
								序列号
								</td>
								<td>
								<input type="text" name="documentManageInfo.beginNum" value='<s:property value="documentManageInfo.beginNum"/>' class="tbl_query_text" />~<input type="text" name="documentManageInfo.endNum" value='<s:property value="documentManageInfo.endNum"/>' class="tbl_query_text" />
								</td>
								<td>状态</td>
								<td>
								<s:select name="documentManageInfo.status" list="statusList" listKey="key"
                                          listValue='value' cssClass="tbl_query_text5" />
								</td>
								<td>
								分发人
								</td>
								<td>
								<input type="text" name="documentManageInfo.disId" value="<s:property value="documentManageInfo.disId"/>" class="tbl_query_text" />
								</td>
								<td>
								领用人
								</td>
								<td>
								<input type="text" name="documentManageInfo.recId" value="<s:property value="documentManageInfo.recId"/>" class="tbl_query_text" />
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
									<input type="button" class="tbl_query_button" value="查询" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" name="BtnView" id="BtnView"
										onclick="querylist()"
									/>
								</td>
								<td></td>
							</tr>
						</table>
					</div>
					<table id="tbl_tools" width="100%" border="0">
						<tr>
							<td align="center" width="10%">
								<a href="#" name="upLoad" id="upLoad" onclick="createNum()">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />
									生成
								</a>
							</td>
							<td align="center" width="10%">
								<a href="#"  onclick="changeStatuslist();">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />
									入库
								</a>
							</td>
							<td align="center" width="10%">
								<a href="#" name="upLoad" id="upLoad" onclick="distribute()">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />
									分发
								</a>
							</td>
							<td align="center" width="10%">
								<a href="#" onclick="takecode()">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />
									领用
								</a>
							</td>
						
							<td align="center" width="10%">
								<a href="#" name="upLoad" id="upLoad" onclick="transTo()">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />
									调拨
								</a>
							</td>
							<td align="center" width="10%">
								<a href="#" name="upLoad" id="upLoad" onclick="loseObject()">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />
									遗失
								</a>
							</td>
							<td align="center" width="10%">
								<a href="#" id="cmdRollBackBtn" name="cmdRollBackBtn" onclick="cancel()">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1032.png" />
									作废
								</a>
							</td>
							<td align="center" width="30%"></td>
						</tr>
					</table>
					<div id="lessGridList3" style="overflow: auto; width: 100%;" >
						<table class="lessGrid" cellspacing="0" rules="all" border="0" cellpadding="0" style="border-collapse: collapse;width: 100%;">
							<tr class="lessGrid head">
								<th width="3%" style="text-align:center">
									<input id="CheckAll" style="width:13px;height:13px;" type="checkbox" onClick="checkAll(this,'ruleId')" />
								</th>
								<th style="text-align:center" width="5%">单证类型</th>
								<th style="text-align:center" width="5%">序列号</th>
								<th style="text-align:center" width="5%">编号</th>
								<th style="text-align:center" width="5%">机构</th>
								<th style="text-align:center" width="5%">渠道</th>
								<th style="text-align:center" width="5%">状态</th>
								<th style="text-align:center" width="5%">分发人</th>
								<th style="text-align:center" width="5%">领用人编号</th>
								<th style="text-align:center" width="5%">领用人名称</th>
								
							</tr>
							
							<s:iterator value="paginationList.recordList" id="iList" status="stuts">
								<tr id="<s:property value="num"/>">
									<td style="text-align:center">
										<input type="checkbox" style="width:13px;height:13px;" name="ruleId" value="<s:property value="num"/>" />
									</td>
									<td>
									<s:if test='type=="1"'>投保单号</s:if>
									<s:if test='type=="2"'>合同专用纸</s:if>
									<s:if test='type=="3"'>银保通合同号</s:if>
									</td>
									<td><s:property value='num' /></td>
									<td><s:property value='vdiOrder' /></td>
									<td><s:property value='instId' /></td>
									<td><s:property value='channelStr'/></td>
									<td>
									<s:if test='status=="0"'>已生成</s:if>
									<s:if test='status=="1"'>已入库</s:if>
									<s:if test='status=="2"'>已分发</s:if>
									<s:if test='status=="3"'>已领用</s:if>
									<s:if test='status=="4"'>已调拨</s:if>
									<s:if test='status=="5"'>已遗失</s:if>
									<s:if test='status=="6"'>已作废</s:if>
									</td>
									<td><s:property value='disId' /></td>
									<td><s:property value='recId' /></td>
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
			</tr>
		</table>
	</form>
</body>
</html>
