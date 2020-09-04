<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<!--
	createTime:2016.2.
	author:沈磊
	content:序列号生成主界面 metlife
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
	function add(){
		//OpenModalWindow("createCode.action", 650, 350, true);
		document.forms[0].action="createCode.action";
		document.forms[0].submit();
		document.forms[0].action="createKeyCode.action";
	}
	
	function querylist(){
		document.forms[0].action="createKeyCode.action";
		document.forms[0].submit();
	}
	function edit(){
	if (checkChkBoxesSelected("ruleId")) {
		if (checkChkBoxesSelectedOne("ruleId") == 1) {
			 var ruleIds = document.getElementsByName("ruleId");
			 var ruleId;
                    for (i = 0; i < ruleIds.length; i++) {
                        if (ruleIds[i].checked) {
                            ruleId = ruleIds[i].value;
                           // OpenModalWindow("createCode.action?ruleId="+ruleId, 650, 350, true);
                           document.forms[0].action="editKeyCode.action?ruleId="+ruleId;
							document.forms[0].submit();
                        }
                    }
             
		}
		else{
			alert("请选择一条数据!");
		}
	}else{
		alert("请选择数据!");
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
        
        
	function cancel(){
				var res = "删除成功";
				var resError = "删除失败";
		if (checkChkBoxesSelected("ruleId")) {
			var ruleId="";
            var inputs = document.getElementsByName("ruleId");
			for ( var i = 0; i < inputs.length; i++) {
				if (inputs[i].checked == true) {
					ruleId += inputs[i].value + ",";
				}
			}
			ruleId=ruleId.substring(0,ruleId.length-1)
			if(confirm("是否确认删除?")){
			alert(1);
				$.ajax({
					url: 'cancelKeyCode.action',
					type: 'POST',
					data:{rule:ruleId},
					dataType: 'text',
					error: function(){
								return false;
							},
					success: function(result){
						
							alert(res);
							document.forms[0].submit();
						
					}			
				});
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
						<span class="current_status_submenu">序号生成</span>
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
								<a href="#" name="upLoad" id="upLoad" onclick="add()">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />
									新增
								</a>
							</td>
							<td align="center" width="10%">
								<a href="#" name="upLoad" id="upLoad" onclick="edit()">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />
									修改
								</a>
							</td>
							<td align="center" width="10%">
								<a href="#" id="cmdRollBackBtn" name="cmdRollBackBtn" onclick="cancel()">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1032.png" />
									删除
								</a>
							</td>
							<td align="center" ></td>
							<td align="center" ></td>
							<td align="center" width="50%"></td>
						</tr>
					</table>
					<div id="lessGridList4" style="overflow: auto; width: 100%;">
						<table class="lessGrid" cellspacing="0" rules="all" border="0" cellpadding="0" style="border-collapse: collapse;width: 100%;">
							<tr class="lessGrid head">
								<th width="3%" style="text-align:center">
									<input id="CheckAll" style="width:13px;height:13px;" type="checkbox" onClick="checkAll(this,'ruleId')" />
								</th>
								<th style="text-align:center" width="5%">单证类型</th>
								<th style="text-align:center" width="5%">机构</th>
								<th style="text-align:center" width="5%">渠道</th>
								<th style="text-align:center" width="5%">合作机构</th>
								<th style="text-align:center" width="5%">渠道代码</th>
								<th style="text-align:center" width="5%">机构代码</th>
								<th style="text-align:center" width="5%">是否包含年度</th>
								<th style="text-align:center" width="5%">特殊项目码</th>
								<th style="text-align:center" width="5%">其他补充代码</th>
								<th style="text-align:center" width="5%">流水号长度</th>
								<th style="text-align:center" width="12%">规则序列</th>
								<th style="text-align:center" width="12%">当前最大号码</th>
								
							</tr>
							
							<s:iterator value="paginationList.recordList" id="iList" status="stuts">
								<tr>
									<td style="text-align:center">
										<input type="checkbox" style="width:13px;height:13px;" name="ruleId" value="<s:property value="ruleId"/>" />
									</td>
									<td>
									<s:if test='type=="1"'>投保单号</s:if>
									<s:if test='type=="2"'>合同专用纸</s:if>
									<s:if test='type=="3"'>银保通合同号</s:if>
									<s:if test='type=="4"'>花旗投保单</s:if>
									<s:if test='type=="5"'>汇丰投保单</s:if>
									<s:if test='type=="6"'>IMAP投保单</s:if>
									</td>
									<td><s:property value='instId' /></td>
									<td><s:property value='channelStr' /></td>
									<td><s:property value='bank' /></td>
									<td><s:property value='channelCode' /></td>
									<td><s:property value='instCode' /></td>
									<td>
									<s:if test='yearYn=="0"'>否</s:if>
									<s:if test='yearYn=="1"'>是</s:if>
									</td>
									<td><s:property value='speCode' /></td>
									<td><s:property value='sufCode' /></td>
									<td><s:property value='length' /></td>
									<td><s:property value='rule' /></td>
									<td><s:property value='curNum' /></td>
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