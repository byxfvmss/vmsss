<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="include.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN" >
<HTML>
	<HEAD>
		<title>Header</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<script language="javascript" type="text/javascript"
			src="<%=bopTheme%>/js/popup.js"></script>
        <script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/XmlHttp.js"></script>
		<script>
		
		
		function init(){	
			//firstId=document.getElementById("firstId");	 
			//window.parent.frames.leftFrame.location.href='Left.aspx?strXmlName=SJCLMap.xml'
			  try{  		
  					circleExe("<%=webapp%>"+"/page/webctrl/sound/sound.wav"); 
 				} catch(e){   
 				 }
			}  
		
		 
		//处理点击Class
		var firstId=null;
		function tabChang(p)
		{
			firstId.className="";
			p.className="on";
			firstId=p;	  
			window.parent.frames['frame'].cols = '140,5,*'; 
			window.parent.frames['midFrame'].document.getElementById("ImgArrow").src="<%=bopTheme2%>/img/al.gif";
		}	
		
		function tabChangEx(p)
		{
			firstId.className="";
			p.className="on";
			firstId=p;	
			//隐藏左边框架			
			window.parent.frames['frame'].cols = '0,0,*';  
		}	
		  
		  
		function Updatepassword()
		{
			var obj=window.showModalDialog("<%=webapp%>/page/system/modifyPassword.jsp?randuuid="+rnd_str(32,true,true,true),"","dialogWidth=500px;dialogHeight=300px;scroll:no;noresizable:yes;");
			if(obj)
			{
				alert("修改密码成功！");
			}
		} 
		
		function refreshCache() {
			var url = "refreshCache.action";
			var param = "";
			var result = sendXmlHttpPost(url, param);
			if("success" == result){
				alert("缓存更新成功");
			}else{
				alert("缓存更新失败");
			}
		}
		
		</script>
		<LINK href="<%=bopTheme%>/css/top.css" type="text/css"
			rel="stylesheet">
	</HEAD>
	<body leftMargin="0" topMargin="0" scroll="no" marginheight="0"
		marginwidth="0">
		<form name="Form1" method="post" action="Header.aspx" id="Form1">
			<input type="hidden" name="__VIEWSTATE"
				value="dDwtMTM0MTI1MjQyMDt0PDtsPGk8MD47PjtsPHQ8O2w8aTwxPjtpPDU+O2k8OT47aTwxMT47PjtsPHQ8cDxwPGw8VGV4dDs+O2w8XDx0ZCBpZD0iZmlyc3RJZCIgQ2xhc3M9Im9uIiBPbkNsaWNrPSJ0YWJDaGFuZyh0aGlzKVw7Ilw+XDxhIGhyZWY9J0xlZnQuYXNweD9zdHJYbWxOYW1lPVNKQ0xNYXAueG1sJyB0YXJnZXQ9J2xlZnRGcmFtZSdcPuaVsOaNruWkhOeQhlw8L2FcPlw8L3RkXD47Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPOezu+e7n+euoeeQhuWRmFJlc29mdDs+Pjs+Ozs+O3Q8cDxwPGw8VGV4dDs+O2w8XDxpbWFnZSBzdHlsZT0nY3Vyc29yOmhhbmQnIHNyYz0naW1hZ2VzL3B0dF9jaGdwd2QuZ2lmJyBvbmNsaWNrPSdqYXZhY3JpcHQ6YWxlcnQoIuaCqOW3sue7j+aciTM2MeWkqeayoeacieS/ruaUueWvhueggeS6huOAgiIpJyAvXD47Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPFw8QSBocmVmPSJsb2dpbi5hc3B4P3R5cGU9bG9nb3V0IiB0YXJnZXQ9Il90b3AiXD7ms6jplIBcPC9BXD47Pj47Pjs7Pjs+Pjs+Pjs+cpQaLIcFkGL72JPPnJMp0kKEtEA=" />

			<table class="top" cellSpacing="0" cellPadding="0" width="100%"
				border="0">
				<tr>
					<td class="logo">&nbsp;
						
					</td>
					<td>&nbsp;
						
					</td>
					<td class="topright"  align="right">
						<table>
							<tr>
								<td style="PADDING-RIGHT: 12px" noWrap align="right">
									<span id="lblVersion"></span>&nbsp;&nbsp;
									<span id="lblWelcome"><span id="LblUserName" class="user"><s:property value='currentUser.name'/></span>
										欢迎您！</span>
									<span  class="password"><A
										href="#" onClick="Updatepassword()">修改密码</A>
									</span>|
                                    <span  class="password"><A
										href="#" onClick="refreshCache()">更新缓存</A>
									</span>|
									<span  class="password"><A href="logout.action" target="_top">注销</A></span>
								</td></tr>
						 </table>
					</td>
				</tr>
			</table>
		</form>
		<div id="soundDiv" name="soundDiv"></div>
	</body>
</HTML>
