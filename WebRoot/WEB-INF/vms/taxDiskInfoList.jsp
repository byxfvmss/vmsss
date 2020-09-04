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
<meta http-equiv="Pragma" content="no-cache" />
<title>税控盘基本信息管理</title>

<style type="text/css">
.detailInfoDIV {
	border: 1px solid green;
	background-color: khaki;
	top: 110px;
	left: 150px;
	width: 450px;
	height: 300px;
	position: absolute;
	z-index: 2;
	filter: alpha(opacity =     90);
	-moz-opacity: 0.9;
	display: none;
}
</style>
<script type="text/javascript">
	function openTaxDiskInfo(url){
		OpenModalWindow(url,500,550,true);
	}
	function submitForm(actionUrl){
		var form = document.getElementById("main");
		form.action=actionUrl;
		form.submit();
		form.action="taxDiskInfoList.action";
	}
	
	function submitQueryForm(actionUrl){
		$("#instid").val($("#instId").val());
		var form = document.getElementById("main");
		form.action=actionUrl;
		form.submit();
		form.action="taxDiskInfoList.action";
	}
	function submitForms(actionUrl,value){
		$("#instid").val(value);
		$("#type").val(1);
		var form = document.getElementById("main");
		form.action=actionUrl;
		form.submit();
		form.action="taxDiskInfoList.action";
	}
	function tanNoCopy(value){
		$("#type").val(value);
	}
	
	function setTaxperList(value){
		if(value==""){
			value = "";
		}
// 		转action查询信息
		$.ajax({url: 'findInfoTaxNo.action',
			type: 'POST',
			async:false,
			data:{authInst:value},
			error: function(){return false;},
			success: function(result){
			    document.getElementById("taxpayerNo").options.length=0;
				var p = new Array();
					p = result.split(",");
					for(var i=0;i<p.length;i++){
						if(result==""||result==null){
							document.getElementById("taxpayerNo").options.add(new Option("所有",""));
						}else{
							if(value==""){
								document.getElementById("taxpayerNo").options.add(new Option("所有",""));
							}
							document.getElementById("taxpayerNo").options.add(new Option(p[i]));
						}
					} 			    
			}
		});
	}
	
	$(function(){
		$("#cmdDelbt").click(function(){
			$del_items=$("input[name='selectTaxDisks']:checked");
			if($del_items.size()==0){
				alert("请选择您要删除的数据！");
				return false;
			}
			if(confirm("您确定要删除吗？")){
				submitForm('deleteTaxDiskInfo.action')
			}
		});
	});
</script>
</head>
<body>
<form id="main" action="<c:out value='${webapp}'/>/taxDiskInfoList.action" method="post">
<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
	<tr>
		<td class="centercondition">
		<div id="tbl_current_status">
			<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> 
			<span class="current_status_menu">当前位置：</span> 
			<span class="current_status_submenu1">参数管理</span>
			<span class="current_status_submenu">税控设备管理</span>
			<span class="current_status_submenu">税控盘信息管理</span>
		</div>
		<div class="widthauto1">
		<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td>机构</td> 
				<td>
					<s:if test="authInstList != null && authInstList.size > 0">
					<s:select style="width:150px" id = "instId" name="instId" list="authInstList" 
					listKey='instId' listValue='instName' headerKey="" headerValue="所有" 
					onchange="setTaxperList(this.value)"/>
					</s:if>
					<s:if test="authInstList == null || authInstList.size == 0">
					<select style="width:150px" name="instId" class="readOnlyText">
					<option value="">请分配机构权限</option>
					</select>
					</s:if>
				</td>
				<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td>纳税人识别号</td> 
				<td>
					<s:if test="taxperLists != null && taxperLists.size > 0">
					<s:select style="width:150px" name="taxpayerNo" list="instTaxno"
					 listKey='tanNo' listValue='tanNo' headerKey="" headerValue="所有" />
					</s:if>
					<s:if test="taxperLists == null || taxperLists.size == 0">
						<select style="width:150px" name="taxpayerNo" >
						<option value="">所有</option>
						</select>
						</s:if>
					
				</td>
				<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td style="text-align:left">
				<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 
				onclick="submitQueryForm('taxDiskInfoList.action?type=2');" name="cmdSelect" value="查询" id="cmdSelect" />				
				</td>
			</tr>
		</table>
		</div>
		
 		<table id="tbl_tools" width="100%" border="0">
			<tr>
				<td align="left">
				<a href="#" onclick="openTaxDiskInfo('addTaxDiskInfo.action');" name="cmdFilter" id="cmdFilter"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon16.png"/>新增</a>
				<a href="#" name="cmdDelbt" id="cmdDelbt" ><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1019.png"/>删除</a>
				<a href="#" id="cmdExpbt" name="cmdExpbt" onClick="submitForm('exportTaxDiskInfo.action');"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png"/>导出</a>
				</td>
			</tr>
		</table>
<div id="lessGridList1" style="overflow: auto; width: 100%;">
<table  class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0"  style="border-collapse: collapse;width: 100%;">
	<tr class="lessGrid head">
	<th width="3%" style="text-align:center"><input id="CheckAll" style="width:13px;height:13px;" type="checkbox" onClick="checkAll(this,'selectTaxDisks')" /></th>
		<th style="text-align: center">序号</th>
		<th style="text-align: center">税控盘号</th>
		<th style="text-align: center">开票机号</th>
		<th style="text-align: center">版本号</th>
		<th style="text-align: center">时钟</th>
		<th style="text-align: center">纳税人识别号</th>
		<th style="text-align: center">纳税人名称</th>
<!-- 		<th style="text-align: center">税控盘口令</th> -->
<!-- 		<th style="text-align: center">证书口令</th> -->
		<th style="text-align: center">税务机关代码</th>
		<th style="text-align: center">税务机关名称</th>
		<th style="text-align: center">发票类型代码</th>
		<th style="text-align: center">企业类型</th>
		<th style="text-align: center">保留信息</th>
		<th style="text-align: center">启用时间</th>
		<th style="text-align: center">操作</th>
	</tr>
	<s:iterator value="paginationList.recordList" id="iList" status="stuts">
		
		<tr align="center"class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
		    <td><input type="checkbox" style="width:13px;height:13px;" name="selectTaxDisks" value="<s:property value="#iList.taxDiskNo"/>,<s:property value="#iList.taxpayerNo"/>"/></td>
			<td align="center"><s:property value='#stuts.count' /></td>		
			<td><s:property value='taxDiskNo'/></td>
			<td><s:property value='billMachineNo'/></td>
			<td><s:property value='taxDiskVersion'/></td>
			<td><s:property value='taxDiskDate'/></td>
			<td><s:property value='taxpayerNo'/></td>
			<td><s:property value='taxpayerNam'/></td>
<!-- 			<td><s:property value='taxDiskPsw'/></td> -->
<!-- 			<td><s:property value='taxCertPsw'/></td> -->
			<td><s:property value='taxBureauNum'/></td>
			<td><s:property value='taxBureauNam'/></td>
			<td><s:property value='diskBillType'/></td>
			<td><s:property value='diskCustType'/></td>
			<td><s:property value='retainInfo'/></td>
			<td><s:property value='enableDt'/></td>
			<td>
				<a href="javascript:void(0);" onClick="openTaxDiskInfo('editTaxDiskInfo.action?taxDiskNo=<s:property value="#iList.taxDiskNo" />&taxpayerNo=<s:property value="#iList.taxpayerNo" />');"><img src ="<c:out value="${bopTheme}"/>/themes/images/icons/icon1005.png" title="编辑" style="border-width: 0px;" /></a>
			</td>
		</tr>
	</s:iterator> 
</table>
</div>
<div id="anpBoud" align="Right" style="width:100%;">
        <table width="100%" cellspacing="0" border="0">
            <tr>           
                <td align="right"><s:component template="pagediv"/></td>
            </tr>
        </table>
</div>
		</td>
	</tr>
</table>
</form>
</body>
</html>















