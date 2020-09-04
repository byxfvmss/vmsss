<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="include.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN" >
<HTML>
	<HEAD>
		<title>left</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<script language="javascript">
		function RedirectFirstUrl()
		{
			parent.sysmain.location.href = '<%=webapp%>/page/welcome.jsp'; 		
		}
		</script>
	</HEAD>
	<body onLoad="RedirectFirstUrl();">
		<form name="Form1" method="post" action="Left.aspx?strXmlName=SJCLMap.xml" id="Form1">
			<input type="hidden" name="__VIEWSTATE" value="dDwxOTcwODk3NzUxOzs+f60L+g+tYTAgNZXArPaf6WLEO00=" />

			<base target="sysmain">
			<link href="<%=bopTheme%>/css/leftNav.css" rel='stylesheet' type=text/css>
			<SCRIPT language=JavaScript>var layerTop=0;       //菜单顶边距 
				var layerLeft=0;      //菜单左边距 
				var layerWidth=140;    //菜单总宽 
				var titleHeight=23;    //标题栏高度 
				var contentHeight=270; //内容区高度 
				var stepNo=10;         //移动步数，数值越大移动越慢 
				var itemNo=0;runtimes=0; 
				document.write('<span id=itemsLayer style="position:absolute;overflow:hidden;border:1px solid #F1F1F1;left:'+layerLeft+';top:'+layerTop+';width:'+layerWidth+';">'); 
				function addItem(itemTitle,itemContent){ 
				itemHTML='<div id=item'+itemNo+' itemIndex='+itemNo+' style="position:relative;left:0;top:'+(-contentHeight*itemNo)+';width:'+layerWidth+';">'
				+'<table width=100% cellspacing=0 cellpadding=0>'+
					   '<tr><td height='+titleHeight+'  onclick=changeItem('+itemNo+') class=titleStyle align=center>'+itemTitle+'</td></tr>'
					   + '<tr><td height='+contentHeight+' class=contentStyle valign=top>'+itemContent+'</td></tr>'
					   +'</table></div>';document.write(itemHTML);itemNo++;}
       		</SCRIPT>
       
       
			<SCRIPT language=JavaScript> 			
			var parentMenu="";
			var childMenu="";
			<s:iterator value="menus" id="allmenus">
				<s:iterator value="#allmenus" id="tmp">
									parentMenu='<s:property value="#tmp.key" />';
									
									<s:iterator value="#tmp.value" id="authList">
									childMenu=childMenu+'<a  class="submenu" href=<%=webapp%>/'
									
											+'<s:property value="#authList.urls" />'
											+'  target ="sysmain" >'
											+'<s:property value="#authList.name" />'
											+'</a>';
									</s:iterator>
									
				</s:iterator>
				addItem('<img src="<%=bopTheme2%>/img/arrow.gif>&nbsp;'+parentMenu,
				'<div width=98% style="OVERFLOW: auto; HEIGHT: '+contentHeight+'" ><center>'
				+childMenu
				+'</div></center>');				
				parentMenu="";
				childMenu="";
			</s:iterator>            
            </SCRIPT>
			<SCRIPT language=JavaScript>
			
			document.write('</span>');
			document.all.itemsLayer.style.height=itemNo*titleHeight+contentHeight;
			toItemIndex=itemNo-1;
			onItemIndex=itemNo-1; 
			function changeItem(clickItemIndex){
				toItemIndex=clickItemIndex;
				if(toItemIndex-onItemIndex>0) {
					moveUp();
				}else{
					 moveDown();
				 }
				runtimes++;
				if(runtimes>=stepNo){
					onItemIndex=toItemIndex;
					runtimes=0;
				}else
					setTimeout('changeItem(toItemIndex)',10);
			} 
			function moveUp(){
				if(onItemIndex!=-1){
					for(i=onItemIndex+1;i<=toItemIndex;i++){
						eval('document.all.item'+i+'.style.top=parseInt(document.all.item'+i+'.style.top)-contentHeight/stepNo;');
					}
				}
			} 
			function moveDown(){
				for(i=onItemIndex;i>toItemIndex;i--)
				eval('document.all.item'+i+'.style.top=parseInt(document.all.item'+i+'.style.top)+contentHeight/stepNo;');
			}
			changeItem(0);            
            </SCRIPT>
		</form>
	</body>
</HTML>
