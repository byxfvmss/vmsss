<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<script language="javascript" type="text/javascript" charset="UTF-8">
	function CloseWindow(needReload){ 
		if(typeof(needReload) == 'undefined'){
			needReload 	= false;
		}		
		window.returnValue = needReload;
		
	     var ua = navigator.userAgent; 
	     var ie = navigator.appName=="Microsoft Internet Explorer" ? true : false; 
	     if(ie){
			 var IEversion = parseFloat(ua.substring(ua.indexOf("MSIE ")+5, ua.indexOf(";",ua.indexOf("MSIE ")))); 
			 if( IEversion< 5.5){
					 var str = '';
					 document.body.insertAdjacentHTML("beforeEnd", str);
					 document.all.noTipClose.Click(); 
			    } else {
			     window.opener =null; window.close();
			    }
		   }else{ 
		   		window.close() 
		   }
	}
	
	try{
		<%
		if(request.getParameter("RESULT_MESSAGE") !=null && !"".equals(request.getParameter("RESULT_MESSAGE"))){
		%>
			alert("<%=java.net.URLDecoder.decode(request.getParameter("RESULT_MESSAGE"),"utf-8")%>");
		<%
		}else if(request.getAttribute("RESULT_MESSAGE") !=null && !"".equals(request.getAttribute("RESULT_MESSAGE"))){	
		%>
		    alert("<%=java.net.URLDecoder.decode(request.getAttribute("RESULT_MESSAGE").toString(),"utf-8")%>");
		<%
		}else if(request.getParameter("resultMessages") !=null && !"".equals(request.getParameter("resultMessages"))){	
		%>
			alert("<%=request.getParameter("resultMessages")%>");
		<%
		}else if(request.getAttribute("resultMessages") !=null && !"".equals(request.getAttribute("resultMessages"))){	
		%>
		    alert("<%=request.getAttribute("resultMessages")%>");
		<%
		}	
		%>
		CloseWindow(true);
	}catch(e){
	 	window.close();
	}
	












	
</script>
	
	
</head>
<body >


</body>
</html>
