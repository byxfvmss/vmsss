<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.util.PaginationList"
	import="com.cjit.vms.input.model.VendorInfo"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../../page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<title>增值税管理平台</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/calendar_notime.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/XmlHttp.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/search.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/ylb.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/lhb.js"></script>
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/search.js"></script>
	<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
	<script language="javascript" type="text/javascript" charset="UTF-8">
	</script>
	<script type="text/javascript">
		function submitForm(actionUrl, flag){
			submitAction(document.forms[0], actionUrl);
			document.forms[0].action="listVendor.action";
		}
		
		function OpenModalWindowSubmit(newURL,width,height,needReload){
			var retData = false;
			if(typeof(width) == 'undefined'){
				width = screen.width * 0.9;
			}
			if(typeof(height) == 'undefined'){
				height = screen.height * 0.9;
			}
			if(typeof(needReload) == 'undefined'){
				needReload = false;
			}
			retData = showModalDialog(newURL, 
						window, 
						"dialogWidth:" + width
							+ "px;dialogHeight:" + height
							+ "px;center=1;scroll=1;help=0;status=0;");
			if(needReload && retData){
				window.document.forms[0].submit();
			}
		}
		
		function saveVendorSuccess(){
			document.forms[0].submit();
			document.forms[0].action="listVendor.action";
		}
		
		function deleteVendor() {
			if(checkChkBoxesSelected("selectVendorIds")){
				var vendorIds = document.Form1.selectVendorIds;
				var ids = "";
				for (var i = 0; i < vendorIds.length; i++){
					if (vendorIds[i].checked){
						ids = ids == "" ? vendorIds[i].value : ids + "," + vendorIds[i].value;
					}
				}
				if(!confirm("确定将选中供应商删除？")){
					alert("aa");
					return false;
				}
				$.ajax({
				url: 'deleteVendor.action',
	
					type: 'POST',
					async:false,
					data:{vendorIds:ids},
					dataType: 'text',
				//	timeout: 1000,
					error: function(){
						return false;},
					success: function(result){
						
						alert(result);
						document.forms[0].submit();
						document.forms[0].action="listVendor.action";
					
							
			}			
				});
			}else{
				alert("请选择需要删除的供应商！");
			}
		}
		
		// [导入]按钮
		function importData(webroot){
			var fileId = document.getElementById("fileId");
			if(fileId.value.length > 0){
				if(fileId.value.lastIndexOf(".XLS") > -1||fileId.value.lastIndexOf(".xls")>-1){
					document.forms[0].action = webroot+"/importVendor.action";
					document.forms[0].submit();
					//document.forms[0].action = 'listVendor.action';
					document.forms[0].action="listVendor.action";
				}else{
					alert("文件格式不对，请上传Excel文件。");
				}
			}else{
				alert("请先选择要上传的文件。");
			}
		}
		
		var msg = '<s:property value="message" escape="false"/>';
		if (msg != null && msg != ''){
			alert(msg);
		}
		
	</script>
	<style type="text/css">
		.style1{
			width: 242px;
		}
	</style>
</head><%--
private String vendorId;// vendor_id varchar2(50)
	private String vendorCName;// vendor_cname varchar2(100) not null,/*供应商中文名称*/
	private String vendorEName;// vendor_ename varchar2(100) not null,/*供应商英文名称*/
	private String vendorTaxNo;// vendor_taxno varchar2(25) not null,/*供应商纳税人识别号，即税务登记号*/
	private String vendorAccount;// vendor_account varchar2(50) not null,/*账号，主键*/
	private String vendorCBank;// vendor_cbank varchar2(100) not null,/*供应商开户银行中文名称*/
	private String vendorEBank;// vendor_ebank varchar2(100) not null,/*供应商开户银行英文名称*/
	private String vendorPhone;// vendor_phone varchar2(50) not null,/*供应商电话*/
	private String vendorEmail;// vendor_email varchar2(100),/*供应商地址*/
	private String vendorAddress;// vendor_address varchar2(100),/*供应商地址*/
	private String vendorLinkman;// vendor_linkman varchar2(100) not null,/*供应商联系人*/
	private String addressee;// addressee varchar2(100),/*供应商收件人*/
	private String addresseePhone;// addressee_phone varchar2(50),/*供应商收件人电话*/
	private String addresseeAddress;// addressee_address varchar2(100),/*供应商收件地址*/
	private String addresseeAddressdetail;// addressee_addressdetail varchar2(100),/*供应商详细收件地址*/
	private String addresseeZipcode;// addressee_zipcode varchar2(100),/*供应商收件邮编*/
	private String taxpayerType;  //供应商纳税人类别  S/G/O/I    S-小规模纳税人    G-一般纳税人    O-其他    I-个体纳税人
	private String dataOperationLabel;//DATA_OPERATION_LABEL	VARCHAR2(1)	N			数据操作标志
	private String dataAuditStatus;//DATA_AUDIT_STATUS	VARCHAR2(1)	N			数据审核状态
--%><body onkeydow="enterkey(event)">
<form name="Form1" method="post" action="listVendor.action" id="Form1" enctype="multipart/form-data">
<table id="tbl_main" cellpadding=0 cellspacing=0>
	<tr>
		<td class="centercondition">
		<s:component template="rocketMessage" />
				<div id="tbl_current_status">
					<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> 
					<span class="current_status_menu">当前位置：</span> 
					<span class="current_status_submenu1">进项税管理</span>
					<span class="current_status_submenu">供应商管理</span>
					<span class="current_status_submenu">供应商信息管理</span>
				</div>
				<div class="widthauto1">
				<table id="tbl_query" cellpadding="0" cellspacing="0" width="90%" border="0">
				<tr>
					<td align="left">
						供应商名称
					</td>
					<td>
						<input type="text" class="tbl_query_text" name="vendorInfo.vendorCName" value="<s:property value='vendorInfo.vendorCName'/>" />
					</td>
					<td align="left">
						账号
					</td>
					<td>
						<input type="text" class="tbl_query_text" name="vendorInfo.vendorAccount" value="<s:property value='vendorInfo.vendorAccount'/>" />
					</td>
					<td align="left">
						税务登记号
					</td>
					<td>
						<input type="text" class="tbl_query_text" name="vendorInfo.vendorTaxNo" value="<s:property value='vendorInfo.vendorTaxNo'/>" />
					</td>
					<td align="left">
						供应商类别
					</td>
					<td>
						<select name="vendorInfo.taxpayerType" style="width:135px">
							<option value="" <s:if test='vendorInfo.taxpayerType==""'>selected</s:if><s:else></s:else>>全部</option>
							<option value="S" <s:if test='vendorInfo.taxpayerType=="S"'>selected</s:if><s:else></s:else>>小规模纳税人</option>
							<option value="G" <s:if test='vendorInfo.taxpayerType=="G"'>selected</s:if><s:else></s:else>>一般纳税人</option>
							<option value="I" <s:if test='vendorInfo.taxpayerType=="I"'>selected</s:if><s:else></s:else>>个体纳税人</option>
							<option value="O" <s:if test='vendorInfo.taxpayerType=="O"'>selected</s:if><s:else></s:else>>其他</option>
						</select>
					</td>
					<td style="width:80px;" align="right" colspan="2">
						<input type="button" class="tbl_query_button" value="查询"
							onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
							name="BtnView" id="BtnView" onclick="submitForm('listVendor.action', 'select');" />
					</td>
				</tr>
				</table>
				</div>
				<table id="tbl_tools" width="100%" border="0">
					<tr>
						<td align="left"  width="200">
						<a href="#"  onClick="OpenModalWindowSubmit('toNewVendor.action',650,600,true)" ><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon16.png"/>新增</a>
						<a href="#" onClick="deleteVendor()" ><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1019.png"/>删除</a>
						
						
<!--						 <input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" -->
<!--						 onMouseOut="this.className='tbl_query_button'" value="新增" -->
<!--						 onClick="OpenModalWindowSubmit('toNewVendor.action',650,400,true)"/>-->
<!--						 <input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" -->
<!--						 onMouseOut="this.className='tbl_query_button'" value="删除" onClick="deleteVendor()"/>-->
<!--						 <input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" -->
<!--						 onMouseOut="this.className='tbl_query_button'" value="导出" onClick="submitForm('exportVendor.action', 'export');"/>-->
						</td><td  width="234">
						<s:file name="attachmentVendor" id="fileId" size='25' style="height:26px;"></s:file>
						</td><td>
<!--						<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" -->
<!--						onMouseOut="this.className='tbl_query_button'" onclick="importData('<%=webapp%>')" name="cmdFilter2" value="导入" id="cmdFilter2" />-->
						<a href="#" onclick="importData('<%=webapp%>')" ><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1007.png"/>导入</a>
						<a href="#" onClick="submitForm('exportVendor.action', 'export');" ><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png"/>导出</a>
						</td>
			        </tr>
				</table>
				<div id="lessGridList1" style="overflow: auto; width: 100%;">
				<table class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0" style="border-collapse: collapse; width: 100%;">
				<tr class="lessGrid head">
					<th style="text-align:center">
						<input style="width:13px;height:10px;" id="CheckAll" type="checkbox" onclick="cbxselectall(this,'selectVendorIds')" />
					</th>
					<th style="text-align: center">序号</th>
					<th style="text-align: center">供应商名称</th>
					<th style="text-align: center">开户银行</th>
					<th style="text-align: center">账号</th>
					<th style="text-align: center">税务登记号</th>
					<th style="text-align: center">电话</th>
					<th style="text-align: center">邮件地址</th>
					<th style="text-align: center">联系人</th>
					<th style="text-align: center">供应商类别</th>
					<th style="text-align: center" colspan="2">操作</th>
				</tr>
				
				
					<s:iterator value="paginationList.recordList" id="iList" status="stuts">
				<tr align="center"
			class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>"
			>
			
			
			<td><input type="checkbox" style="width:13px;height:13px;" name="selectVendorIds" 
			value="<s:property value="#iList.vendorId"/>" <s:if test='#iList.dataAuditStatus == 0'>disabled</s:if>/></td>
			<td align="center"><s:property value='#stuts.count' /></td>	
			<td align="center"><s:property value='vendorCName' /></td>	
			<td align="center"><s:property value='vendorCBank' /></td>	
			<td align="center"><s:property value='vendorAccount' /></td>	
			<td align="center"><s:property value='vendorTaxNo' /></td>	
			<td align="center"><s:property value='vendorPhone' /></td>	
			<td align="center"><s:property value='vendorEmail' /></td>	
			<td align="center"><s:property value='vendorLinkman' /></td>	
			<td><s:if test='taxpayerType=="G"'>一般纳税人</s:if>
				<s:if test='taxpayerType=="S"'>小规模纳税人</s:if>
				<s:if test='taxpayerType=="I"'>个体纳税人</s:if>
				<s:if test='taxpayerType=="O"'>其他</s:if></td>
		<td align="center">
					<s:if test='#iList.dataAuditStatus != 0'>
		
						<a href="javascript:void(0);" 
						onClick="OpenModalWindowSubmit('toEditVendor.action?vendorId=<s:property value="#iList.vendorId"/>',650,500,true)">
						<img src ="<c:out value="${bopTheme}"/>/themes/images/icons/icon1005.png" title="修改供应商" style="border-width: 0px;" /></a>
					</s:if>
					</td>
					<td align="center">
						<a href="javascript:void(0);" 
						onClick="OpenModalWindowSubmit('viewVendorDetail.action?vendorId=<s:property value="#iList.vendorId"/>',650,500,true)">
						<img src ="<c:out value="${bopTheme}"/>/themes/images/icons/icon1014.png" title="查看明细" style="border-width: 0px;" /></a>
					</td>
			
				<%--<%
					PaginationList paginationList = (PaginationList)request.getAttribute("paginationList");
					if (paginationList != null){
						List vendorInfoList = paginationList.getRecordList();
						if (vendorInfoList != null && vendorInfoList.size() > 0){
							for(int i=0; i<vendorInfoList.size(); i++){
								VendorInfo vendor = (VendorInfo)vendorInfoList.get(i);
								if(i%2==0){
				%>
				<tr class="lessGrid rowA">
				<%
								}else{
				%>
				<tr class="lessGrid rowB">
				<%
								}
				%>
					<td align="center">
						<input style="width:13px;height:13px;" type="checkbox" name="selectVendorIds" value="<%=BeanUtils.getValue(vendor,"vendorId")%>" />
						
						<input type="text" name="addressee" value="<%=vendor.getVendorPhone()==null ? "":vendor.getVendorPhone()%>">
						<s:property value="addressee"/>
					</td>
					<td align="center"><%=i + 1%></td>
					<td align="left"><%=vendor.getVendorCName()==null ? "" :vendor.getVendorCName()%></td>
					<td align="left"><%=vendor.getVendorCBank()==null ? "" :vendor.getVendorCBank()%></td>
					<td align="left"><%=vendor.getVendorAccount()==null ? "" :vendor.getVendorAccount()%></td>
					<td align="left"><%=vendor.getVendorTaxNo()==null ? "" : vendor.getVendorTaxNo()%></td>
					<td align="left"><%=vendor.getVendorPhone()==null ? "":vendor.getVendorPhone()%></td>
					<td align="left"><%=vendor.getVendorEmail()==null ? "":vendor.getVendorEmail()%></td>
					<td align="left"><%=vendor.getVendorLinkman()==null ? "":vendor.getVendorLinkman()%></td>
					<td align="center"><%=DataUtil.getTaxpayerTypeCH(vendor.getTaxpayerType())%></td>
					
				</tr>
				<%
							}
						}
					}
				%>
				--%></tr>
				</s:iterator>
				</table>
				</div>
				<div id="anpBoud" align="Right" style="overflow:auto;width:100%;">
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
<script type="text/javascript">
		var msgHight = document.getElementById("anpBoud").offsetHeight;
		var tophight = document.getElementById("tbl_query").offsetHeight;
	document.getElementById("lessGridList1").style.height = screen.availHeight -310-msgHight-tophight
</script>
</html>