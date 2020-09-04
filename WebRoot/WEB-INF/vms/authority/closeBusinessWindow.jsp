<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%@include file="../../../page/include.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  </head>
  <body>
	<script type="text/javascript">
		var msg = '<s:property value="message" escape="false"/>';
		if (msg != null && msg != ''){
			alert(msg);
		}
		function CloseWindow(needReload){ 
			if(typeof(needReload) == 'undefined'){
				needReload 	= false;
			}
			window.returnValue = needReload;
		    var ua = navigator.userAgent; 
		    var ie = navigator.appName=="Microsoft Internet Explorer" ? true : false; 
		    if(ie){
		    	var IEversion = parseFloat(ua.substring(ua.indexOf("MSIE ")+5, ua.indexOf(";",ua.indexOf("MSIE ")))); 
				 if(IEversion< 5.5){
						var str = '';
						document.body.insertAdjacentHTML("beforeEnd", str);
						document.all.noTipClose.Click(); 
				    } else {
				    	window.opener =null;
					    window.close();
				    }
		   }else{
		   		window.close();
		   }
		}
		try{
			CloseWindow(true);
		}catch(e){
		 	window.close();
		}
	</script>
  </body>
</html>