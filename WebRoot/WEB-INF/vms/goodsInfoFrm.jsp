<!--file: <%=request.getRequestURI()%> -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<base target="_self">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../page/include.jsp"%>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
<link href="<%=webapp%>/page/subWindow.css" type="text/css" rel="stylesheet">
<title><s:if test="updFlg==0">新增</s:if><s:elseif test="updFlg==1">修改</s:elseif>商品管理
</title>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script type="text/javascript" src="<%=bopTheme%>/js/validator.js"></script>
<!-- MessageBox -->
<script src="<%=bopTheme%>/js/MessageBox/js/messageBox.js" type="text/javascript"></script>
<link href="<%=bopTheme%>/js/MessageBox/css/messageBox.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript">
//标识页面是否已提交
var subed = false;
function checkForm(){

    //验证是否重复提交
    if (subed == true){
       alert("信息正在发送给服务器，请不要重复提交信息！");
       return false;
    }
    
	// 纳税人识别号
	if(fucCheckNull(document.getElementById("taxno"),"请选择纳税人识别号")==false){
		return false;
	}
	
  	// 税目编号
  	//if (document.getElementById("updFlg").value == "0"){
	    if(fucCheckNull(document.getElementById("goodsNo"),"请输入发票商品编号")==false){
	       return false;
	    }
	//}
  	
  	// 商品名称
    if(fucCheckNull(document.getElementById("goodsName"),"请输入商品名称")==false){
       return false;
    }
    /* 
	// 交易类型
	if(fucCheckNull(document.getElementById("transType"),"请选择交易类型")==false){
		return false;
	} */
	
	// 商品管理  商品存在性check
	if(chkGoodsInfo(document.getElementById("taxno").value, document.getElementById("goodsNo").value, 
			document.getElementById("goodsName").value, document.getElementById("taxnoBak").value, 
			document.getElementById("goodsNameBak").value, document.getElementById("updFlg").value) == false){
		var obj = document.getElementById("goodsName")
		var m = new MessageBox(obj);
		m.Show("该商品已经存在，请重新输入。");	
		obj.focus();
		return false;
	}
	
	// 商品管理  商品明细存在性check
	/* if(chkGoodsItemInfo(document.getElementById("taxno").value, document.getElementById("goodsNo").value, document.getElementById("transType").value, 
			document.getElementById("transTypeBak").value, document.getElementById("updFlg").value) == false){
		var obj = document.getElementById("transType")
		var m = new MessageBox(obj);
		m.Show("该商品的交易类型已经存在，请重新输入。");	
		obj.focus();
		return false;
	} */
	
    subed=true;
    return true;
}



function submitForm(){
	if(true == checkForm()){
		
		var selectTransTypes2=document.getElementById("subTableFrame2").contentWindow.b();
		var selectTransTypes="";
		if(document.getElementById("subTableFrame")!=null){
		var selectTransTypes1=document.getElementById("subTableFrame").contentWindow.d();
		selectTransTypes = selectTransTypes2 === "" ? selectTransTypes1 : selectTransTypes1 + "," + selectTransTypes2;
				}else{
					selectTransTypes=selectTransTypes2;
				}		
		document.forms[0].action = "<%=webapp%>	/addOrUpdGoodsInfo.action?selectTransTypes="
					+ selectTransTypes;
			document.forms[0].submit();

		} else {
			subed = false;
		}
		parent.location.reload();
	}
	function chkGoodsInfo(taxno, goodsNo, goodsName, taxnoBak, goodsNameBak,
			updFlg) {
		var vv = "0";
		$.ajax({
			url : 'chkVmsGoodsInfo.action',
			type : 'POST',
			async : false,
			data : {
				taxno : taxno,
				goodsNo : goodsNo,
				goodsName : goodsName,
				taxnoBak : taxnoBak,
				goodsNameBak : goodsNameBak,
				updFlg : updFlg
			},
			dataType : 'html',
			timeout : 1000,
			error : function() {
				return false;
			},
			success : function(result) {
				vv = result;
			}
		});
		if (vv == "0") {
			return true;
		}
		return false;
	}
	function chkGoodsItemInfo(taxno, goodsNo, transType, transTypeBak, updFlg) {
		var vv = "0";
		$.ajax({
			url : 'chkVmsGoodsItemInfo.action',
			type : 'POST',
			async : false,
			data : {
				taxno : taxno,
				goodsNo : goodsNo,
				transType : transType,
				transTypeBak : transTypeBak,
				updFlg : updFlg
			},
			dataType : 'html',
			timeout : 1000,
			error : function() {
				return false;
			},
			success : function(result) {
				vv = result;
			}
		});
		if (vv == "0") {
			return true;
		}
		return false;
	}
</script>
</head>
<body style="overflow-x:hidden!important;">
	<div class="showBoxDiv">
		<form id="frm1" action="<c:out value='${webapp}'/>/addOrUpdGoodsInfo.action" method="post">
			<input type="hidden" id="taxnoBak" name="taxnoBak" value="<s:property value="taxno" />" />
			<input type="hidden" id="goodsNameBak" name="goodsNameBak" value="<s:property value="goodsName" />" />
			<input type="hidden" id="transTypeBak" name="transTypeBak" value="<s:property value="transType" />" />
			<div id="editsubpanel" class="editsubpanel">
				<div style="padding-right:17px;">
					<input type="hidden" name="updFlg" id="updFlg" value="<s:property value="updFlg" />" />
					<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
						<tr class="header">
							<th colspan="4">
								<s:if test="updFlg==0">新增</s:if>
								<s:elseif test="updFlg==1">修改</s:elseif>
								商品管理
							</th>
						</tr>
						<tr>
							<td width="15%" style="text-align: right" class="listbar">商品名称:</td>
							<s:if test='updFlg==1'>
								<td width="85%">
									<input id="goodsName" name="goodsName" type="text" class="tbl_query_text2" value="<s:property value="goodsName" />" maxlength="50" style="ime-mode: disabled" disabled="disabled" />
									<input type="hidden" id="goodsName" name="goodsName" value="<s:property value="goodsName" />" />
								</td>
							</s:if>
							<s:else>
								<td width="85%">
									<input id="goodsName" name="goodsName" type="text" class="tbl_query_text2" value="<s:property value="goodsName" />" maxlength="50" />
									<span class="spanstar">*</span>
								</td>
							</s:else>
							<%-- <td width="15%" style="text-align: right" class="listbar">交易类型:</td>
		<td>
			<s:select id="transType" name="transType" headerKey="" headerValue="请选择" 
				list="businessList" listKey='businessCode' listValue='businessCName'/>
		</td> --%>
						</tr>
						<tr>
							<td width="15%" style="text-align: right" class="listbar">发票商品编号:</td>
							<td width="85%">
								<s:if test='updFlg==1'>
									<input id="goodsNo1" name="goodsNo1" type="text" class="tbl_query_text2" value="<s:property value="goodsNo" />" maxlength="20" style="ime-mode: disabled" disabled="disabled" />
									<input type="hidden" id="goodsNo" name="goodsNo" value="<s:property value="goodsNo" />" />
								</s:if>
								<s:else>
									<input id="goodsNo" name="goodsNo" type="text" class="tbl_query_text2" value="<s:property value="goodsNo" />" maxlength="20" style="ime-mode: disabled" />
									<span class="spanstar">*</span>
								</s:else>
							</td>
						</tr>
						<tr>
							<td width="15%" style="text-align: right" class="listbar">纳税人识别号:</td>
							<td width="85%">
								<s:if test='updFlg==1'>
									<s:if test="taxperList != null && taxperList.size > 0">
										<s:select name="taxno" list="taxperList" listKey='taxperNumber' listValue='taxperNumber' headerKey="" headerValue="请选择" disabled="true" />
										<input type="hidden" id="taxno" name="taxno" value="<s:property value="taxno" />" />
									</s:if>
								</s:if>
								<s:else>
									<s:if test="taxperList != null && taxperList.size > 0">
										<s:select name="taxno" list="taxperList" listKey='taxperNumber' listValue='taxperNumber' headerKey="" headerValue="请选择" />
									</s:if>
									<s:if test="taxperList == null || taxperList.size == 0">
										<select name="taxno" class="readOnlyText">
											<option value="">请分配机构权限</option>
										</select>
									</s:if>
									<span class="spanstar">*</span>
								</s:else>
							</td>
						</tr>
						<s:if test='updFlg==1'>
							<tr class="row1">
								<td valign="top" colspan="10">
									<iframe id="subTableFrame" scrolling="auto" src="getBusiList.action?taxno=<s:property value="taxno"/>&goodsNo=<s:property value="goodsNo"/>" height="260px" width="100%" frameborder="0"></iframe>
								</td>
							</tr>
						</s:if>
						<tr class="row1">
							<td valign="top" colspan="10">
								<table cellspacing="0" border="0" cellpadding="0" width="100%" style="BORDER:#D6D6D6 1px solid" height="290px;">
									<tr>
										<td>
											<iframe id="subTableFrame2" scrolling="auto" src="getBusitoList.action?taxno=<s:property value="taxno"/>&goodsNo=<s:property value="goodsNo"/>" height="290px" width="100%" frameborder="0"></iframe>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</div>
				<div id="ctrlbutton" class="ctrlbutton" style="border:0px">
					<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="submitForm();" name="BtnSave" value="保存"
						id="BtnSave"
					/>
					<!-- 	<input type="reset" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" name="BtnReset" value="重置" id="BtnReset"/> -->
					<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="CloseWindow();" name="BtnReturn"
						value="取消" id="BtnReturn"
					/>
				</div>
		</form>
	</div>
</body>
</html>