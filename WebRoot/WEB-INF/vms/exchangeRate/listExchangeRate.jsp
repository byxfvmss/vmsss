<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.util.PaginationList"
	import="com.cjit.vms.trans.model.ExchangeRate"
	import="com.cjit.vms.trans.util.DataUtil"
	import="com.cjit.gjsz.datadeal.model.SelectTag"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	List currencyList = (List) request.getAttribute("currencyList");
	Map map = new HashMap();
	for (int m=0; m<currencyList.size(); m++) {
		SelectTag st = (SelectTag) currencyList.get(m);
		map.put(st.getValue(), st.getText());
	}
%>
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
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
	<script type="text/javascript">
		function submitForm(actionUrl){
			submitAction(document.forms[0], actionUrl);
			document.forms[0].action="listExchangeRate.action";
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
				document.forms[0].action="listExchangeRate.action";
			}
		}
		
		function saveSuccess(){
			document.forms[0].submit();
			document.forms[0].action="listExchangeRate.action";
		}
		
		// [导入]按钮
		function importData(webroot){
			var fileId = document.getElementById("fileId");
			if(fileId.value.length > 0){
				if(fileId.value.lastIndexOf(".XLS") > -1||fileId.value.lastIndexOf(".xls")>-1){
					document.forms[0].action = webroot+"/importExchangeRate.action";
					document.forms[0].submit();
					//document.forms[0].action = 'listExchangeRate.action';
					document.forms[0].action="listExchangeRate.action";
				}else{
					alert("文件格式不对，请上传Excel文件。");
				}
			}else{
				alert("请先选择要上传的文件。");
			}
		}
		
		function saveExchangeSuccess(){
			document.forms[0].submit();
			document.forms[0].action="listExchangeRate.action";
		}
	</script>
	<style type="text/css">
		.style1{
			width: 242px;
		}
	</style>
</head>
<body onkeydow="enterkey(event)">
	<form name="Form1" method="post" action="listExchangeRate.action" id="Form1" enctype="multipart/form-data">
	<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
	<tr>
		<td class="centercondition">
		<s:component template="rocketMessage" />
		<div id="tbl_current_status">
			<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> 
			<span class="current_status_menu">当前位置：</span> 
			<span class="current_status_submenu1">参数管理</span>
			<span class="current_status_submenu">业务参数管理</span>
			<span class="current_status_submenu">汇率管理</span>
		</div>
		<div class="widthauto1">
		<table id="tbl_query" cellpadding="0" cellspacing="0" width="90%" border="0">
		<tr>
			<td align="left">
				数据日期
			</td>
			<td>
				<input class="tbl_query_time" type="text" name="exchangeRate.dataDt" value="<s:property value='exchangeRate.dataDt'/>"	onfocus="WdatePicker()"	size='11'"/>
			</td>
			<td align="left">
				基准币种
			</td>
			<td>
				<s:select id="exchangeRate.basicCcy" name="exchangeRate.basicCcy" style="width:135px" 
					list="currencyList" headerKey="" headerValue="全部" listKey='value' listValue='text'/>
			</td>
			<td align="left">
				汇率日期
			</td>
			<td>
				<input class="tbl_query_time" type="text" name="exchangeRate.ccyDate" value="<s:property value='exchangeRate.ccyDate'/>"	onfocus="WdatePicker()"	size='11'"/>
			</td>
		</tr><tr>
			<td align="left">
				折算币种
			</td>
			<td>
				<s:select id="exchangeRate.forwardCcy" name="exchangeRate.forwardCcy"  style="width:135px"
					list="currencyList" headerKey="" headerValue="全部" listKey='value' listValue='text'/>
			</td>
			<td align="left">
				折算类型
			</td>
			<td>
				<select name="exchangeRate.convertTyp" style="width:135px">
					<option value="" <s:if test='exchangeRate.convertTyp==""'>selected</s:if><s:else></s:else>>全部</option>
					<option value="M" <s:if test='exchangeRate.convertTyp=="M"'>selected</s:if><s:else></s:else>>M:乘</option>
					<option value="D" <s:if test='exchangeRate.convertTyp=="D"'>selected</s:if><s:else></s:else>>D:除</option>
				</select>
			</td>
			<td colspan="2">
				<input type="button" class="tbl_query_button" value="查询"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="BtnView" id="BtnView" onclick="submitForm('listExchangeRate.action');" />
			</td>
		</tr>
		</table>
</div>
	<!-- <table id="tbl_query" cellpadding="1" cellspacing="0">
			<tr align="left">
			<td>
				 <input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" 
				 onMouseOut="this.className='tbl_query_button'" value="新增" 
				 onClick="OpenWindow('toEditExchangeRate.action?method=add',400,200,500, 300)"/>
			</td>
			<td>
				 <input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" 
				 onMouseOut="this.className='tbl_query_button'" value="删除" 
				 onClick="beforeDelete('<c:out value="${webapp}"/>/deleteExchangeRate.action','selectExchangeRateIds',600,410,true,'delete')"/>
			</td>
			<td>
				 <input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" 
				 onMouseOut="this.className='tbl_query_button'" value="导出" onClick="submitForm('exportExchangeRate.action');"/>
			</td>
			<td>
				<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" 
				onMouseOut="this.className='tbl_query_button'" onclick="importData('<%=webapp%>')" name="cmdFilter2" value="导入" id="cmdFilter2" />
				<s:file name="attachmentExchangeRate" id="fileId"></s:file>
			</td>
	        </tr>
		</table> -->
	 	<table id="tbl_tools" width="100%" border="0">
			<tr>
				<td align="left" width="200">
				<a href="#" onClick="OpenModalWindowSubmit('toEditExchangeRate.action?method=add',500,300,true)"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon16.png"/>新增</a>
				<a href="#" onClick="beforeDelete('<c:out value="${webapp}"/>/deleteExchangeRate.action','selectExchangeRateIds',600,410,true,'delete')"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1019.png"/>删除</a>
				</td><td  width="255">
				<s:file name="attachmentExchangeRate" id="fileId" size="30" style="height:26px;"></s:file>
				</td><td>
				<a href="#" onclick="importData('<%=webapp%>')"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1007.png"/>导入</a>
				<a href="#" onClick="submitForm('exportExchangeRate.action');" ><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png"/>导出</a>
				</td>
			</tr>
		</table>
  	<div id="lessGridList4" style="overflow: auto; width: 100%;">
		<table class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0" style="border-collapse: collapse; width: 100%;">
	<tr class="lessGrid head">
		<th style="text-align:center">
			<input style="width:13px;height:10px;" id="CheckAll" type="checkbox" onclick="cbxselectall(this,'selectExchangeRateIds')" />
		</th>
		<th style="text-align: center">序号</th>
		<th style="text-align: center">数据日期</th>
		<th style="text-align: center">基准币种</th>
		<th style="text-align: center">汇率日期</th>
		<th style="text-align: center">折算币种</th>
		<th style="text-align: center">折算类型</th>
		<th style="text-align: center">汇率</th>
		<th style="text-align: center">修改</th>
	</tr>
	<%
		PaginationList paginationList = (PaginationList)request.getAttribute("paginationList");
		if (paginationList != null){
			List exchangeRateList = paginationList.getRecordList();
			if (exchangeRateList != null && exchangeRateList.size() > 0){
				for(int i=0; i<exchangeRateList.size(); i++){
					ExchangeRate exchangeRate = (ExchangeRate)exchangeRateList.get(i);
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
			<input style="width:13px;height:13px;" type="checkbox" name="selectExchangeRateIds" value="<%=BeanUtils.getValue(exchangeRate,"exchangeRateId")%>" />
		</td>
		<td align="center"><%=i + 1%></td>
		<td align="center"><%=exchangeRate.getDataDt()%></td>
		<td align="center"><%=map.get(exchangeRate.getBasicCcy())%></td>
		<td align="center"><%=exchangeRate.getCcyDate()%></td>
		<td align="center"><%=map.get(exchangeRate.getForwardCcy())%></td>
		<td align="center"><%=DataUtil.getConvertTypCH(exchangeRate.getConvertTyp())%></td>
		<td align="center"><%=NumberUtils.format(exchangeRate.getCcyRate(),"",2)%></td>
		<td align="center">
			<a href="javascript:void(0);"
				onclick="OpenModalWindowSubmit('toEditExchangeRate.action?method=edit&exchangeRateId=<%=exchangeRate.getExchangeRateId()%>',500, 300, true)">
			<img src ="<c:out value="${bopTheme}"/>/themes/images/icons/icon1005.png" title="修改汇率" style="border-width: 0px;" /></a>
		</td>
	</tr>
	<%
				}
			}
		}
	%>
	</tr>
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
</html>