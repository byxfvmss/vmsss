<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<!--
	createTime:2016.2.
	author:沈磊
	content:修改序列号 metlife
  -->
<html>
<head>
<title>增值税管理平台</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../../../page/include.jsp"%>

<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
<link href="<%=webapp%>/page/common.css" type="text/css" rel="stylesheet">
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/calendar_notime.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/XmlHttp.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/search.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/ylb.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/lhb.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/search.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<meta http-equiv="Pragma" content="no-cache" />
<script type="text/javascript">
	function turnBack(){
		document.forms[0].action="createKeyCode.action?fromFlag=menu";
		document.forms[0].submit();
	}
	function save(){
		document.forms[0].action="saveKeyCode.action";
		document.forms[0].submit();
		saveKeyCode
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
						<span class="current_status_submenu">修改序号</span>
					</div>
					<div class="widthauto1">
							 <font color="#FF0000">*生成规则为：机构代码 + 年度 + 渠道代码 + 特殊项目码 + 其他补充代码  <font color="#FF0000">
						<table id="tbl_query" cellpadding="0" cellspacing="0" width="80%" border="0">
							 <input type="hidden" value="<s:property value="documentManageInfo.ruleId"/>" name="documentManageInfo.ruleId">
							<tr>
								<td align="left">单证类型
								<input type="hidden" value="<s:property value="documentManageInfo.type"/>" name="documentManageInfo.type">
								</td>
								<td disabled="disabled">
									<select style="width:150px;" id="type" name="documentManageInfo.type"  readonly class="readonly"  class="tbl_query_text_readonly" width="50%">
										<option value="1" <s:if test='documentManageInfo.type=="1"'>selected</s:if> <s:else></s:else>>投保单号</option>
										<option value="2" <s:if test='documentManageInfo.type=="2"'>selected</s:if> <s:else></s:else>>合同专用纸</option>
										<option value="3" <s:if test='documentManageInfo.type=="3"'>selected</s:if> <s:else></s:else>>银保通合同号</option>
									</select>
								</td>
								<td align="left"></td>
								<td>
								</td>
								
							</tr>
							<tr>
								<td align="left">机构
									<input type="hidden" value="<s:property value="documentManageInfo.instId"/>" name="documentManageInfo.instId">
								</td>
								<td disabled="disabled">
									<input type="hidden" id="inst_id" name="documentManageInfo.instId" value='<s:property value="instId"/>'>
									<s:if test="authInstList != null && authInstList.size > 0">
                                    <s:select name="documentManageInfo.instId" list="authInstList" listKey="instId"
                                              listValue='instName' cssClass="tbl_query_text5"
                                              headerKey="" headerValue="全部"/>
                                </s:if>
                                <s:if test="authInstList == null || authInstList.size == 0">
                                    <select name="documentManageInfo.instName" class="readOnlyText">
                                        <option value="">请分配机构权限</option>
                                    </select>
                                </s:if>
								</td>
								<td align="left"></td>
								<td>
								</td>
								
							</tr>
							<tr>
								<td align="left">合作机构
								<input  type="hidden" value="<s:property value="documentManageInfo.bank"/>" name="documentManageInfo.bank">
								</td>
								<td  disabled="disabled">
									<input type="text" maxlength="10" class="tbl_query_text" name='documentManageInfo.bank' value='<s:property value='documentManageInfo.bank' />'  readonly class="readonly" class="tbl_query_text_readonly" width="50px;"/>
								</td>
								<td align="left">机构代码</td>
								<td>
									<input type="text" maxlength="3" class="tbl_query_text" name='documentManageInfo.instCode' value='<s:property value='documentManageInfo.instCode' />' />
								</td>
								
							</tr>
							<tr>
								<td align="left">渠道
								<input type="hidden" value="<s:property value="documentManageInfo.channel"/>" name="documentManageInfo.channel">
								</td>
								<td disabled="disabled">
									<s:select name="documentManageInfo.channel" list="chanNelList" listKey="key"  listValue='value' cssClass="tbl_query_text5" headerKey="" headerValue="请选择"/>
								</td>
								<td align="left">渠道代码
								<input type="hidden" value="<s:property value="documentManageInfo.channelCode"/>" name="documentManageInfo.channelCode">
								</td>
								<td disabled="disabled">
									<input type="text" maxlength="1" class="tbl_query_text" name='documentManageInfo.channelCode' value='<s:property value='documentManageInfo.channelCode' />'  />	
								</td>
								
							</tr>
							<tr>
								<td align="left">是否包含年度</td>
								<td>
									<select style="width:150px;" id="documentManageInfo.yearYn" name="documentManageInfo.yearYn">
										<option value="0" <s:if test='documentManageInfo.yearYn=="0"'>selected</s:if> <s:else></s:else>>否</option>
										<option value="1" <s:if test='documentManageInfo.yearYn=="1"'>selected</s:if> <s:else></s:else>>是</option>
									</select>
								</td>
								<td align="left">特殊项目码</td>
								<td>
									<input width="200px;" maxlength="1" type="text" class="tbl_query_text" name='documentManageInfo.speCode' value='<s:property value='documentManageInfo.speCode' />' />	
								</td>
								
							</tr>
							<tr>
								<td align="left">流水号长度</td>
								<td>
									<input type="text" maxlength="1"  class="tbl_query_text" name='documentManageInfo.length' value='<s:property value='documentManageInfo.length' />' />	
								</td>
								<td align="left">其他补充代码</td>
								<td>
									<input type="text" maxlength="2" class="tbl_query_text"  name='documentManageInfo.sufCode' value='<s:property value='documentManageInfo.sufCode' />' />	
								</td>
								
							</tr>
							
						</table>
					</div>
					<table id="tbl_tools" width="100%" border="0">
						<tr>
							<td></td>
							<td></td>
							<td></td>
							<td align="center">
								<a href="#" name="upLoad" id="upLoad" onclick="turnBack()">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />
									返回
								</a>
							</td>
							<td align="right">
								<a href="#" id="cmdRollBackBtn" name="cmdRollBackBtn" onclick="save()">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1032.png" />
									保存
								</a>
							</td>
							<td align="center"></td>
						</tr>
					</table>
			</tr>
		</table>
	</form>
</body>
</html>