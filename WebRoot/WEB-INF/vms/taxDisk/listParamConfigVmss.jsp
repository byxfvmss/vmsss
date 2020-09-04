<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>

<%@page import="java.util.*"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@ include file="../../../page/include.jsp"%>
		<OBJECT id='DocCenterCltObj' width=100 height=100 classid="clsid:1E25664C-EA53-4F39-8575-684737626D6F" style="display: none;"></OBJECT>
<script language="javascript" type="text/javascript" src="<%=bopTheme2%>/js/main.js"></script>
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/calendar_notime.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/XmlHttp.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/search.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/ylb.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/lhb.js"></script>
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/search.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/taxSelver/validatorTax.js"></script>
	
	
		<script language="javascript" type="text/javascript">
		var page = {};
		page.submitForm = function(obj){
			if(Validator.Validate(obj,3)){
				return page.checkRepassword();
			}
			return false;
		}
		
		function Expan(o)
			{
				var obj=document.all(o); //声明一个变量
				obj.style.display=obj.style.display=="none"?"":"none"; //判断是否隐藏
			}
			
			
			var obj_table = null;
			function ExpanAll(){
				obj_table=document.getElementsByTagName ("TABLE");						
				for( var i = 0 ; i<obj_table.length; i++){	
					if(obj_table[i] != undefined && obj_table[i] != null && obj_table[i].id != null && obj_table[i].id != ""){
						obj_table[i].style.display=""; //判断是否隐藏
					}					
				}
				
				obj_table=document.getElementsByTagName ("TD");						
				for( var i = 0 ; i<obj_table.length; i++){	
					if(obj_table[i] != undefined && obj_table[i] != null && obj_table[i].id != null && obj_table[i].id != ""){
						obj_table[i].style.display=""; //判断是否隐藏
					}					
				}
				
			}
			
			function RetractAll(){
				obj_table=document.getElementsByTagName ("TABLE");
				for( var i = 0 ; i<obj_table.length; i++){
					if(obj_table[i] != undefined && obj_table[i] != null && obj_table[i].id != null && obj_table[i].id != ""){
						if(obj_table[i].id!='tbl_current_status')
							obj_table[i].style.display="none"; //判断是否隐藏
					}
				}	
				
				obj_table=document.getElementsByTagName ("TD");						
				for( var i = 0 ; i<obj_table.length; i++){	
					if(obj_table[i] != undefined && obj_table[i] != null && obj_table[i].id != null && obj_table[i].id != ""){
						obj_table[i].style.display=""; //判断是否隐藏
					}					
				}			
			}
			
			function tabChange(e) {
				if (e.className == "selct") return;
				if (e.className == "on") e.className = "normal";
				else e.className = "on";
			} 
			
			function clickTab(e,divId) {
				if (!window._CURR_C) 
					window._CURR_C = document.getElementById("firstTab");
				window._CURR_C.className = "normal";
				window._CURR_C = e;
				window._CURR_C.className = "selct";
				//切换tab页
				document.getElementById(divId).style.display="";
				var divs = document.getElementsByTagName("div");
				for(i=0;i<divs.length;i++){
					if(divs[i].id!=null&&divs[i].id!=''&&divs[i].id.indexOf('tab_content_')>-1){
						if(divId!=divs[i].id){
							document.getElementById(divs[i].id).style.display="none";
						}
					}
				}
				//置选中标志
				document.forms[0].selectTab.value=divId.replace('tab_content_','');
			}
			
			window.onload = function(){
				var msg = "<s:property value="innerHtml"/>";
				if(msg!=''&&msg=='success'){
					alert('保存成功');
				}
				if(msg!=''&&msg=='noChanges'){
					alert('没有变更');
				}
				//if(msg!=''&&msg=='saveSuccess'){
				//	alert('保存成功，待审核后生效');
				//}
				//if(msg!=''&&msg=='saveFaliures'){
				//	alert('保存失败，在待审核信息中已存在该项信息');
				//}
				if(msg!=''&&msg=='fail'){
					alert('保存失败');
				}
				//if(msg!=''&&msg=="cannot"){
				//	alert('输入项中的值在数据库中保存的长度不能超过200');
				//}
			}
			
			function saveConfig(){
				
				if(document.forms[0].selectTab.value==''){
					document.forms[0].selectTab.value=document.getElementById('firstTab').innerText.replace(/^(\s)*|(\s)*$/g,"");
				}
				document.forms[0].action="<c:out value='${webapp}'/>/saveParamConfigVmss.action";
				
			}	
				
			/*
			* 查询提交
			*/
			function findOutSubmit() {
				 var p1 = document.getElementsByName("param_1")[0].value;
				// var p2 = document.getElementsByName("param_2")[0].value;
				 var p3 = document.getElementsByName("param_3")[0].value;
				 var p4 = document.getElementsByName("param_4")[0].value;
				 var p5 = document.getElementsByName("param_5")[0].value;
				 var p6 = document.getElementsByName("param_6")[0].value;
				 var p11 = document.getElementsByName("param_11")[0].value;
				 if(p1==""||p3==""||p4==""|p5==""|p6==""|p11==""){
				 alert("请输入值！");
				 return false;
				 }
				if(p1<0||p1>100){  
					 alert("请输入0-100的数值"); 
					 return false;
					 
					 }
	//			if(!/^\d+(\.\d+)?$/.test(p2)){  
	//				 alert("请输入大于等于0的尾差"); 
	//				 return false;
					 
	//				} 
				if(!/^\d+(\.\d+)?$/.test(p3)){  
					 alert("请输入大于等于0的预警通知时间"); 
					 return false;
					 
					} 
				if(!/^[0-9]*[1-9][0-9]*$/.test(p5)){  
					 alert("打印限定值请输入大于0的整数"); 
					 return false;
					 
					}
				if(!/^[0-9]*[1-9][0-9]*$/.test(p6)){  
					 alert("红冲周期请输入大于0的整数"); 
					 return false;
					}
				
				
				if(!/^[1-2]*$/.test(p11)){  
					 alert("税控参数1代表税控盘2代表税控服务器"); 
					 return false;
					}
				
				
				if(!/^\d+(\.\d+)?$/.test(p4)){  
					 alert("请输入大于等于0的作废时间流参数"); 
					 return false;
			 }else{
			 	document.frm.action="saveParamVmssConfig.action";
				document.frm.method="post";
				document.frm.submit();
			 }
			}
</script>

		<script language="javascript">
	// 2009-07-17 14:08 ShiCH 为IE添加背景缓存功能,可解决TAB鼠标事件时重新加载图片闪烁问题
	if(/msie/gi.test(navigator.userAgent))
		try{document.execCommand("BackgroundImageCache", false, true);}catch(e){};
		
		function functions(){
			var param_1 = document.getElementById("param_1").value;
			document.getElementById("p_1").innerHTML=" %";
			<%--var param_2 = document.getElementById("param_2").value;
			document.getElementById("p_2").innerHTML=" 元";--%>
			var param_3 = document.getElementById("param_3").value;
			document.getElementById("p_3").innerHTML=" 天";
			var param_4 = document.getElementById("param_4").value;
			document.getElementById("p_4").innerHTML=" 时";
			var param_5 = document.getElementById("param_5").value;
			document.getElementById("p_5").innerHTML=" 张";
			var param_6 = document.getElementById("param_6").value;
			document.getElementById("p_6").innerHTML=" 天";
		}
		function onBlurs(value,id,name){
			if(id == "param_0"){
				var valueof = parseFloat(value).toFixed(2);
				if(valueof>=100){
					valueof = 99.99;
				}
				$("#param_0").val(valueof);
			}else if(id == "param_2"){
				var valueof = parseFloat(value).toFixed(4);
				$("#param_2").val(valueof);
			}
		}
	</script>
	</head>
	<body onload="functions();">
		<form name="frm" action="saveParamConfig.action" method="post">
			<input type="hidden" name="selectTab" value="<s:property value="selectTab"/>" />
			<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
			<tr>
				<td class="centercondition">
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> 
						<span class="current_status_menu">当前位置：</span> 
						<span class="current_status_submenu1">参数管理</span>
						<span class="current_status_submenu">业务参数管理</span>
						<span class="current_status_submenu">业务参数管理</span>
					</div>
					<div>
					<s:set name="selectTab" value="selectTab"></s:set>
					<s:iterator value="paramMaps" status="va">
					    <s:set name="key" value="key"></s:set>
					    <s:set id="value" name="value" value="value"></s:set>
	                    <%String key1=(String)pageContext.getAttribute("key");
					      String systemId1=key1.substring(0,key1.indexOf("#"));
					      String systemName1=key1.substring(key1.indexOf("#")+1);
					      pageContext.setAttribute("systemId",systemId1);
					      pageContext.setAttribute("systemName",systemName1);
					    %>							
						<div id="tab_content_<c:out value="${systemId}"/>" class="tab_body" style='<c:if test="${selectTab!=systemId}">display : block </c:if>;'>
							<div class="body">
								<div align="right">
									<s:property value="value" escape="false" />
								</div>
							</div>
						</div>							
					</s:iterator>
					</div>
				<%--
				
				<tr>
					<td align="right" valign="middle" height="35" style="padding-right:17px;"><input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="findOutSubmit()" value="保存" /></td>
				</tr>
				
			--%>
				</td>
				</tr>
			</table>
		</form>
	</body>	
</html>
