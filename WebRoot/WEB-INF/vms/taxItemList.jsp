<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../page/include.jsp"%>
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">

<script type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
<%@ include file="taxServer/ocxObject.jsp" %>
<meta http-equiv="Pragma" content="no-cache" />
<title>税目管理</title>
<script type="text/javascript">
	function openWindows(url){
		OpenModalWindow(url,650,200,true);
	}
	

	function toExcel(){
		document.forms[0].action = 'taxItemInfoToExcel.action';
		document.forms[0].submit();
		document.forms[0].action = "listTaxItemInfo.action";
	}
	
	// 打开空白发票作废页面
	function invalidBlankPaperInvoice(){
		var url = 'invalidBlankPaperInvoice.action';
		OpenModalWindow(url,650,250,true);
	}
	function submitForm(actionUrl){
		document.forms[0].action = actionUrl;
		document.forms[0].submit();
		document.forms[0].action = "listTaxItemInfo.action";
	}
	
	function deleteTransBatch(actionUrl){
	
		var form = document.getElementById("main");
		form.action=actionUrl;
		form.submit();
		document.forms[0].action = "listTaxItemInfo.action";
	}
	
	$(function(){
		$("#cmdDel").click(function(){
			$checkedStore=$("input[name='taxItemIdList']:checked");
			if($checkedStore.size()==0){
				alert("请选择要删除的记录");
				return;
			}
			if(confirm("确定要删除选择的记录吗？")){
				$selectedIds="";
				for($idx=0;$idx<$checkedStore.size();$idx++){
					if(($idx+1)!=$checkedStore.size()){
						$selectedIds+=$($checkedStore[$idx]).val()+",";
					}else{
						$selectedIds+=$($checkedStore[$idx]).val();
					}
				}
				submitForm("delTaxItemInfo.action?selectedIds="+$selectedIds);
				document.forms[0].action = "listTaxItemInfo.action";
			}
		});
	});
<%--	9、	查询税种税目接口
a)	操作代码：taxesQuery
b)	输入：注册码|纳税人识别号|税控盘号|税控盘口令|发票类型
c)	输出：返回结果（0失败1成功）|返回信息（”成功”或者具体错误）|发票类型|税种税目索引号^税种税目代码^税率^税种名称^税目名称^|
d)	红色部分为循环域
	--%>


			function syncDiskTaxDetil(){
			
		// 稅控盘口令
		try{
				//数控盘查询盘号
				var taxDiskNo = DocCenterCltObj.FunGetPara('','taxDiskNo');
				// alert(taxDiskNo);
				var arr=new Array();
				arr=taxDiskNo.split("|");
				if(arr[0]==0){
					alert("请连接税控盘");
					return false;
				}
			
				taxDiskNo=arr[1];
				alert(taxDiskNo);
				$.ajax({url: 'getDiskInfo.action',
						type: 'POST',
						async:false,
						data:{taxDiskNo:taxDiskNo},
						dataType: 'text',
					//	timeout: 1000,
						error: function(){
							return false;},
						success: function(result){
						if (result == ''){
								alert("查询信息失败。");
								return false;
							} else if (result == 'lock') {
								alert("不能同时操作数据。");
								return false;
							} else if (result == 'registeredInfoError') {
								alert("获取税控盘注册码失败。");
								return false;
							} else if (result == 'taxPwdError') {
								alert("获取税控盘口令和证书口令失败。");
								return false;
							}else{
								//alert(result);
								taxmu(result);
							}
							}
					});
				
		}catch(e){
			alert("请安装税控盘插件！");
			}
}<%-- 注册码|税控盘口令|证书口令--%>
		function taxmu(result){
			var aa=new Array();
		
		aa=result.split("|");
		//注册码
		var registeredInfo=aa[0];
		//税控盘口令
		var taxDiskpwd=aa[1];
		//证书口令
		var certpwd=aa[2];
		alert(taxDiskpwd);
		//查询税控盘信息
		//税控盘编号|纳税人识别号|纳税人名称|税务机关代码|税务机关名称|发票类型代码|当前时钟|启用时间|版本号|开票机号|企业类型|保留信息|其它扩展信息
		//返回结果（0失败1成功）|返回信息（”成功”或者具体错误）|税控盘编号|纳税人识别号|纳税人名称|税务机关代码|税务机关名称|发票类型代码|当前时钟|启用时间|版本号|开票机号|企业类型|保留信息|其它扩展信息
		//	1						2							3			4				5	6					7		8			9		10		11	12			13		14		15			
		var DataQuery = DocCenterCltObj.FunGetPara(taxDiskpwd,'DataQuery');
		//alert(DataQuery);
		var arr=new Array();
		arr=DataQuery.split("|");
		if(arr[0]==0){
			alert(arr[1]);
			return false;
		}
		var diskNo=arr[2];
		var taxNo=arr[3];
		var data0=registeredInfo+"|"+taxNo+"|"+diskNo+"|"+taxDiskpwd+"|0";
		var data1=registeredInfo+"|"+taxNo+"|"+diskNo+"|"+taxDiskpwd+"|1";
		//alert(data0);
		var taxesQuery0=taxNo+"|"+0+"|"+DocCenterCltObj.FunGetPara(data0,'taxesQuery');
		var taxesQuery1=taxNo+"|"+1+"|"+DocCenterCltObj.FunGetPara(data1,'taxesQuery');
		//alert(taxesQuery1);
		$.ajax({url: 'saveTaxInfo.action',
						type: 'POST',
						async:false,
						data:{taxesQuery0:taxesQuery0,taxesQuery1:taxesQuery1},
						dataType: 'text',
					//	timeout: 1000,
						error: function(){
							return false;},
						success: function(result){
								//alert(result)
								if(result=='success'){
									alert("税目信息保存成功");
									// monTaxDisk(taxesQuery,diskNo);
								}else{
									alert(result);
									return false;
								}
						}
							});
			//注册码|纳税人识别号|税控盘号|税控盘口令|发票类型
		}
</script>
	</head>
	<body>
	<!-- <form id="main" action="<c:out value='${webapp}'/>/listTaxItemInfo.action" method="post"> -->
		<form name="main" action="listTaxItemInfo.action" method="post" enctype="multipart/form-data">
		   <table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
			<tr>
				<td class="centercondition">
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> 
						<span class="current_status_menu">当前位置：</span> 
						<span class="current_status_submenu1">参数管理</span>
						<span class="current_status_submenu">业务参数管理</span>
						<span class="current_status_submenu">税目管理</span>
					</div>
				<div class="widthauto1">
					<table id="tbl_query" cellpadding="1" cellspacing="0">
						<tr>
							<td align="right">纳税人识别号</td>
							<td><input name="taxno" id="taxno" type="text" class="tbl_query_text" value="<s:property value="taxno"/>" style="width: 150;" />
<!--								<s:if test="taxperList != null && taxperList.size > 0">-->
<!--								<s:select name="taxno" list="taxperList" listKey='taxperNumber' listValue='taxperNumber' headerKey="" headerValue="所有"/>-->
<!--								</s:if>-->
<!--								<s:if test="taxperList == null || taxperList.size == 0">-->
<!--								<select name="taxno" class="readOnlyText">-->
<!--								<option value="">请分配机构权限</option>-->
<!--								</select>-->
<!--								</s:if>-->
							</td>
							<td align="right">税类</td>
							<td><s:select id="fapiaoType" name="fapiaoType" 
							    list="#{'':'所有','0':'增值税专用发票','1':'增值税普通发票'}" listKey="key" listValue="value"
							    value='fapiaoType'/>
					        </td>	
							<td align="right">税目编号</td>
							<td><input name="taxId" id="taxId" type="text" class="tbl_query_text"  value="<s:property value="taxId"/>" style="width: 150;" /></td>
							<td><input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
							           onclick="submitForm('listTaxItemInfo.action');" name="BtnView" value="查询" id="BtnView" /></td>	
						</tr>
					</table>
				</div>
			<!-- <table id="tbl_main" cellpadding="0" cellspacing="0">
				<tr>
					<td align="left">
						<table id="tbl_tools" cellpadding="1" cellspacing="0">
							<tr>
								<td align="left" style="padding: 1px 0px 0px 5px;">
									<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 
									    value="增加"	onClick="openWindows('taxItemInfoAddOrUpdInit.action?updFlg=0');" />
									<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 
										name="cmdDel" value="删除" id="cmdDel" />	
									<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 
									    onclick="toExcel();" name="delBtn" value="导出" id="export" />
			                        <input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" 
				                        onMouseOut="this.className='tbl_query_button'" onclick="submitForm('importTaxItemInfo.action');" name="cmdFilter2" value="导入" id="cmdFilter2" />
				                    <s:file name="attachmentTaxItem"></s:file>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				                    <input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" 
				                        onMouseOut="this.className='tbl_query_button'" onclick="alert('功能未实现！！！');return false;" name="cmdFilter3" value="自动获取" id="cmdFilter3" />
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table> -->
			 
	 		<table id="tbl_tools" width="100%" border="0">
				<tr>
					<td align="left" width="200">
					<a href="#" onClick="openWindows('taxItemInfoAddOrUpdInit.action?updFlg=0');" ><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon16.png"/>增加</a>
					<a href="#" name="cmdDel" id="cmdDel"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1019.png"/>删除</a>
					</td><td align="right"  width="255">
					<s:file name="attachmentTaxItem" size="30" style="height:26px;"></s:file>
					</td><td align="left" >
					<a href="#" onclick="submitForm('importTaxItemInfo.action');" ><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1007.png"/>导入</a>
					<a href="#" onclick="toExcel();"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png"/>导出</a>
					</td><td align="right" >
					<a href="#" onclick="syncDiskTaxDetil()" ><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1004.png"/>自动获取</a>
					</td>
				</tr>
			</table>
  			<div id="lessGridList1" style="overflow: auto; width: 100%;">
				<table class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0" style="border-collapse: collapse; width: 100%;">
					<tr class="lessGrid head">
						<th width="4%" style="text-align:center">
							<input id="CheckAll" style="width: 13px; height: 13px;" type="checkbox" onClick="checkAll(this,'taxItemIdList')" />
						</th>
						<th style="text-align: center">序号</th>
						<th width="22%"  style="text-align:center" nowrap>
							纳税人识别号
						</th>
						<th width="22%"  style="text-align:center" nowrap>
							税类
						</th>
						<th width="22%" style="text-align:center" nowrap>
							税目编号
						</th>
						<th width="22%" style="text-align:center" nowrap>
							税率
						</th>
						<th  style="text-align:center" nowrap>
							修改
						</th>
					</tr>
					<s:iterator value="paginationList.recordList" id="iList" status="stuts">
						<tr align="center"class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
							<td align="center" >
							<input type="checkbox" style="width: 13px; height: 13px;" name="taxItemIdList" value="<s:property value="taxId"/>" />
							</td>
							<td align="center"><s:property value='#stuts.count' /></td>
							<td align="center">
								<s:property value="taxno" />
							</td>
							<td align="center">
								<s:if test='fapiaoType=="0"'>增值税专用发票</s:if>
								<s:elseif test='fapiaoType=="1"'>增值税普通发票</s:elseif>
								<s:else></s:else>
							</td>
							<td align="center">
								<s:property value="taxId" />
							</td>
							<td align="center">
								<s:if test="taxRate==0"><s:property value="taxRate" />00</s:if>
								<s:elseif test="taxRate.length()==3"><s:property value="taxRate" />0</s:elseif>
								<s:else><s:property value="taxRate" /></s:else>
							</td>
							<td align="center">
								<a href="#"
									onClick="openWindows('taxItemInfoAddOrUpdInit.action?updFlg=1&taxId=<s:property value="taxId" />&taxno=<s:property value="taxno" />')">
									<img src ="<c:out value="${bopTheme}"/>/themes/images/icons/icon1005.png" title="修改" style="border-width: 0px;" />
								</a>
							</td>
						</tr>
					</s:iterator>

				</table>
			</div>
			<div id="anpBoud" align="Right" style="width:100%;vlign=top;">
		        <table width="100%" cellspacing="0" border="0">
		            <tr>
		                <td align="left"></td>
		                <td align="right"><s:component template="pagediv"/></td>
		            </tr>
		        </table>
	    	</div>
	    </td>
			</tr>	
		    </table>
		</form>
	</body>
	<script type="text/javascript">
		var msgHight = document.getElementById("anpBoud").offsetHeight;
		var tophight = document.getElementById("tbl_query").offsetHeight;
	document.getElementById("lessGridList1").style.height = screen.availHeight -310-msgHight-tophight
</script>
</html>
