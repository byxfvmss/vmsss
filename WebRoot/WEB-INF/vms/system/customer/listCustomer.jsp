<!-- file: <%=request.getRequestURI()%> -->
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
<link href="<c:out value="${bopTheme2}"/>/css/main.css" rel="stylesheet" type="text/css" />
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/window.js"></script>
<script charset="gbk" language="javascript" type="text/javascript" src="<%=bopTheme%>/js/validator.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="js/My97DatePicker/WdatePicker.js"></script>
<head>
<title>客户管理</title>
<script type="text/javascript">
	function submitForm(actionUrl) {
		var form = document.getElementById("main");
		form.action = actionUrl;
		form.submit();
		form.action = "listCustomer.action";
	}
	function toExcel() {
		var form = document.getElementById("main");
		form.action = "exportCustomer.action";
		form.submit();
		form.action = "listCustomer.action?showCount=" + "false";

	}
</script>
</head>
<body>
	<form id="main" action="listCustomer.action" method="post" enctype="multipart/form-data">
		<table id="tbl_main" cellpadding="0" cellspacing="0" border="0">
			<tr width="100%">
				<td align="left">
					<table id="tbl_current_status">
						<tr>
							<td class="centercondition">
								<s:component template="rocketMessage" />
								<div id="tbl_current_status">
									<img src="<%=bopTheme%>/themes/images/icons/icon13.png" />
									<span class="current_status_menu">当前位置：</span>
									<span class="current_status_submenu1">销项税管理</span>
									<span class="current_status_submenu">客户管理</span>
									<span class="current_status_submenu">客户信息管理</span>
								</div>
						</tr>
					</table>
					<table id="tbl_query" cellpadding="0" cellspacing="0" border="0" width="100%">
						<tr>
							<td>客户编号:</td>
							<td>
								<input id="customerCode" class="tbl_query_text" name="customerCode" type="text" value="<s:property value='customerCode' />" />
							</td>
							<td>客户名称:</td>
							<td>
								<input id="customerCName" class="tbl_query_text" name="customerCName" type="text" value="<s:property value='customerCName' />" />
							</td>
							<td>客户类型</td>
							<td>
								<select id="customerType" name="customerType">
									<option value="" <s:if test='customerType==""'>selected</s:if> <s:else></s:else>>全部</option>
									<option value="I" <s:if test='customerType=="I"'>selected</s:if> <s:else></s:else>>私人客户</option>
									<option value="C" <s:if test='customerType=="C"'>selected</s:if> <s:else></s:else>>公司客户</option>
								</select>
							</td>
							<td>发票类型:</td>
							<td>
								<select id="vatInvoice" name="vatInvoice">
									<option value="" <s:if test='vatInvoice==""'>selected</s:if> <s:else></s:else>>全部</option>
									<option value="0" <s:if test='vatInvoice=="0"'>selected</s:if> <s:else></s:else>>增值税专用发票</option>
									<option value="1" <s:if test='vatInvoice=="1"'>selected</s:if> <s:else></s:else>>增值税普通发票</option>
								</select>
							</td>
							<%--<td>
					GHO类 
				</td>
				private String ghoClass;//gho 类
	private String datasSource;//数据来源
				
				<td>
				<input type="text" class="tbl_query_text"   name="ghoClass" value="<s:property value='ghoClass'/>">
				</td>
				--%>
						</tr>
						<tr>
							<td>客户纳税人识别号:</td>
							<td>
								<input id="taxNo" name="taxNo" class="tbl_query_text" type="text" value="<s:property value='taxNo' />" />
							</td>
							<td align="right" class="listbar">国籍:</td>
							<td>
								<s:select id="customerNationality" name="customerNationality" list="countrys" listKey='customerNationality' listValue='countrySName' headerKey="" headerValue="所有" />
							</td>
							<td>纳税人类型:</td>
							<td>
								<select id="taxpayerType" name="taxpayerType">
									<option value="" <s:if test='taxpayerType==""'>selected</s:if> <s:else></s:else>>全部</option>
									<option value="S" <s:if test='taxpayerType=="S"'>selected</s:if> <s:else></s:else>>小规模纳税人</option>
									<option value="G" <s:if test='taxpayerType=="G"'>selected</s:if> <s:else></s:else>>一般纳税人</option>
									<option value="I" <s:if test='taxpayerType=="I"'>selected</s:if> <s:else></s:else>>个体纳税人</option>
									<option value="O" <s:if test='taxpayerType=="O"'>selected</s:if> <s:else></s:else>>其他</option>
								</select>
							</td>
							<td>是否打票</td>
							<td>
								<select id="customerFapiaoFlag" name="customerFapiaoFlag">
									<option value="" <s:if test='customerFapiaoFlag==""'>selected</s:if> <s:else></s:else>>全部</option>
									<option value="A" <s:if test='customerFapiaoFlag=="A"'>selected</s:if> <s:else></s:else>>自动打印</option>
									<option value="M" <s:if test='customerFapiaoFlag=="M"'>selected</s:if> <s:else></s:else>>手动打印</option>
									<option value="N" <s:if test='customerFapiaoFlag=="N"'>selected</s:if> <s:else></s:else>>永不打印</option>
								</select>
							</td>
							<td>数据来源</td>
							<td>
								<s:select list="dataSourceList" name="datasSource" id="datasSource" headerKey="" headerValue="全部" listKey="value" listValue="text"></s:select>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<table id="tbl_tools" width="100%" border="0">
			<tr>
				<td align="left" width="270">
					<a href="#" name="BtnView" id="BtnView" onclick="submitForm('listCustomer.action');">
						<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1010.png" />
						查询
					</a>
					<a href="#" onClick="OpenModalWindowSubmit('<c:out value="${webapp}"/>/createCustomer.action?editType=add',700,500,true,'add')">
						<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon16.png" />
						新增
					</a>
					<a href="#" onClick="beforeDeleteCustomer('<c:out value="${webapp}"/>/deleteCustomer.action')">
						<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1019.png" />
						删除
					</a>
				</td>
				<td align="right" width="255">
					<s:file name="attachmentCustomer" size="30" style="height:26px; line-height:30px; "></s:file>
				</td>
				<td>
					<a href="#" onclick="submitForm('importCustomer.action');">
						<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1007.png" />
						导入
					</a>
					<a href="#" onClick="toExcel();">
						<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png" />
						导出
					</a>
				</td>
			</tr>
		</table>
		<div id="lessGridList4" style="overflow: auto; width: 100%; ">
			<table class="lessGrid" cellspacing="0" rules="all" border="0" cellpadding="0" style="border-collapse: collapse;width: 100%;">
				<tr class="lessGrid head">
					<th width="3%" style="text-align:center">
						<input id="CheckAll" style="width:13px;height:13px;" type="checkbox" onClick="checkAll(this,'customerIdList')" />
					</th>
					<th width="3%" style="text-align: center">序号</th>
					<th width="15%" style="text-align: center">客户编号</th>
					<th width="15%" style="text-align: center">客户名称</th>
					<th width="15%" style="text-align: center">客户纳税人识别号</th>
					<th width="5%" style="text-align: center">国籍</th>
					<th width="15%" style="text-align: center">客户类型</th>
					<th width="15%" style="text-align: center">纳税人类型</th>
					<th width="10%" style="text-align: center">发票类型</th>
					<th width="10%" style="text-align: center">是否打票</th>
					<%--<th width="15%" style="text-align: center">GHO类</th>
		--%>
					<th width="15%" style="text-align: center">数据来源</th>

					<th width="5%" style="text-align: center">操作</th>

				</tr>
				<s:iterator value="paginationList.recordList" id="iList" status="stuts">
					<tr align="center" class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
						<td>
							<input type="checkbox" style="width:13px;height:13px;" name="customerIdList" value="<s:property value="#iList.customerID"/>" <s:if test='#iList.dataAuditStatus == 0'>disabled</s:if> />
						</td>
						<td align="center">
							<s:property value='#stuts.count' />
						</td>
						<td>
							<s:property value='customerID' />
						</td>
						<td>
							<s:property value='customerCName' />
						</td>
						<td>
							<s:property value='customerTaxno' />
						</td>
						<td>
							<s:property value='countrySName' />
						</td>
						<td>
							<s:property value='customerTypeName' />
						</td>
						<td>
							<s:if test='taxPayerType=="G"'>一般纳税人</s:if>
							<s:if test='taxPayerType=="S"'>小规模纳税人</s:if>
							<s:if test='taxPayerType=="I"'>个体纳税人</s:if>
							<s:if test='taxPayerType=="O"'>其他</s:if>
						</td>
						<td>
							<s:property value='fapiaoTypeName' />
						</td>
						<td>
							<s:property value='customerFapiaoFlagName' />
						</td>
						<%--<td><s:property value="ghoClass"/></td>
			--%>
						<td>
							<s:if test='datasSource=="1"'>手工</s:if>
							<s:if test='datasSource=="2"'>系统</s:if>
						</td>
						<span>
							<td style="display: none">
								<s:property value='customerId' />
							</td>
							<td align="center">
								<s:if test='#iList.dataAuditStatus != 0'>
									<a title="修改" href="javascript:void(0)" onClick="OpenModalWindowSubmit('<c:out value="${webapp}"/>/editCustomer.action?customerId=<s:property value="#iList.customerID"/>',700,500,'view') ">
										<img src="<c:out value="${bopTheme}"/>/themes/images/icons/edit.png" alt="修改" style="border-width: 0px;" style="<s:if test='#iList.dataAuditStatus == 0'>visibility:hidden</s:if>" />
									</a>
								</s:if>
								<s:if test='#iList.customerType=="C"'>
									<a title="子公司编辑" href="listSubCustomer.action?subCustomerSearch.customerId=<s:property value="customerID"/>">
										<img src="<c:out value="${bopTheme}"/>/themes/images/icons/add.png" alt="子公司编辑" style="border-width: 0px;" />
									</a>
								</s:if>
								<a title="收件地址编辑" href="listCustomerAddress.action?customerAddressSearch.customerId=<s:property value="customerID"/>">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon13.png" alt="地址编辑" style="border-width: 0px;" />
								</a>
								<a title="收件人编辑" href="listCustomerReceiver.action?customerReceiverSearch.customerId=<s:property value="customerID"/>">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/admin.png" alt="收件人编辑" style="border-width: 0px;" />
								</a>
								<a href="javascript:void(0)" onClick="OpenModalWindow('<c:out value="${webapp}"/>/viewCustomerDetail.action?customerId=<s:property value="#iList.customerID"/>',700,500,'view') ">
									<img src="<c:out value="${bopTheme}"/>/themes/images/icons/view.png" alt="查看" style="border-width: 0px;" />
								</a>
							</td>
					</tr>
				</s:iterator>
			</table>
		</div>
		<div id="anpBoud" align="Right" style="width:100%;">
			<table width="100%" cellspacing="0" border="0">
				<tr>
					<input type="hidden" name="paginationList.showCount" value="false">
					<td align="right">
						<s:component template="pagediv" />
					</td>
				</tr>
			</table>
		</div>
	</form>
</body>

<script language="javascript">
	function OpenModalWindowSubmit(newURL, width, height, needReload, s) {
		var retData = false;
		if (typeof (width) == 'undefined') {
			width = screen.width * 0.9;
		}
		if (typeof (height) == 'undefined') {
			height = screen.height * 0.9;
		}
		if (typeof (needReload) == 'undefined') {
			needReload = false;
		}
		retData = showModalDialog(newURL, window, "dialogWidth:" + width
				+ "px;dialogHeight:" + height
				+ "px;center=1;scroll=1;help=0;status=0;");
		if (needReload && retData) {
			//window.document.forms[0].submit();
			submitAction(document.forms[0], "listCustomer.action?newdate="
					+ new Date());
			document.forms[0].action = "listCustomer.action";

		}
	}
	function beforeDeleteCustomerlk(actionName) {
		var t = "";
		var inputs = document.getElementsByName('customerIdList');
		for (var i = 0; i < inputs.length; i++) {
			if (inputs[i].checked == true) {
				t += inputs[i].value + ",";
			}
		}
		if (t.length == 0) {
			alert("请先选择要删除的客户！");
			return;
		}

		if (confirm('确认删除所选客户？')) {
			document.forms[0].action = actionName;
			document.forms[0].submit();
			document.forms[0].action = "listCustomer.action";
		}

	}
	function beforeDeleteCustomer(actionName) {
		var t = "";
		var inputs = document.getElementsByName('customerIdList');
		for (var i = 0; i < inputs.length; i++) {
			if (inputs[i].checked == true) {
				t += inputs[i].value + ",";
			}
		}
		if (t.length == 0) {
			alert("请先选择要删除的客户！");
			return;
		}

		if (confirm('确认删除所选客户？')) {
			//document.forms[0].action = actionName;
			deletebefore(t);

		}

	}
	function deletebefore(t) {
		$.ajax({
			url : 'deleteCustomer.action',

			type : 'POST',
			async : false,
			data : {
				customerIdList : t
			},
			dataType : 'text',
			//	timeout: 1000,
			error : function() {
				return false;
			},
			success : function(result) {

				alert(result);
				document.forms[0].submit();
				document.forms[0].action = "listCustomer.action";

			}
		});
	}
</script>
</html>