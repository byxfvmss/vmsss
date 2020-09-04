var isLimit=1;
var oPopup = window.createPopup();
var popTop=50;
var currentRecord="";
var soundUrl;
function closeDiv()
{
	clearTimeout(mytime);
	popTop = 50;
	oPopup.hide();	
}
function viewInfo()
{
	jump(15,32);
	closeDiv();
}
function ignore()
{
	if(navigator.cookieEnabled){
		if(currentRecord && currentRecord !=""){
			writeRecord(currentRecord);
		}
	}
	closeDiv();
}
function playSound(url){     
  	window.document.getElementById("soundDiv").insertAdjacentHTML('beforeEnd',"<embed  src='"+url+"' hidden='true' autostart='true' loop='false'>")   
 } 
function popmsg(msg,sound){
	//var winstr="<DIV id=eMeng style='BORDER-RIGHT: #455690 1px solid; BORDER-TOP: #a6b4cf 1px solid; Z-INDEX:99999; LEFT: 0px; BORDER-LEFT: #a6b4cf 1px solid; WIDTH: 200px; BORDER-BOTTOM: #455690 1px solid; POSITION: absolute; TOP: 0px; HEIGHT: 150px; BACKGROUND-COLOR: #c9d3f3'><TABLE style='BORDER-TOP: #ffffff 1px solid; BORDER-LEFT: #ffffff 1px solid' cellSpacing=0 cellPadding=0 width='100%' bgColor=#cfdef4 border=0><TBODY><TR><TD style='FONT-SIZE: 12px; BACKGROUND-IMAGE: url(msgTopBg.gif); COLOR: #0f2c8c' width=30 height=24></TD><TD style='FONT-WEIGHT: normal; FONT-SIZE: 12px; BACKGROUND-IMAGE: url(msgTopBg.gif); COLOR: #1f336b; PADDING-TOP: 4px;PADDING-left: 4px' vAlign=center width='100%'> 消息提示：</TD><TD style='BACKGROUND-IMAGE: url(msgTopBg.gif); PADDING-TOP: 2px;PADDING-right:2px' vAlign=center align=right width=19><span title=关闭 style='CURSOR: hand;color:red;font-size:12px;font-weight:bold;margin-right:4px;' onclick='parent.closeDiv();' >×</span></TD></TR><TR><TD style='PADDING-RIGHT: 1px; BACKGROUND-IMAGE: url(1msgBottomBg.jpg); PADDING-BOTTOM: 1px' colSpan=3 height=90><DIV style='BORDER-RIGHT: #b9c9ef 1px solid; PADDING-RIGHT: 13px; BORDER-TOP: #728eb8 1px solid; PADDING-LEFT: 13px; FONT-SIZE: 12px; PADDING-BOTTOM: 13px; BORDER-LEFT: #728eb8 1px solid; WIDTH: 100%; COLOR: #1f336b; PADDING-TOP: 18px; BORDER-BOTTOM: #b9c9ef 1px solid; HEIGHT: 100%;'><DIV align=center style='word-break:break-all;font-size:14'>"+msg+"</DIV></DIV></TD></TR></TBODY></TABLE><DIV style='position:relative;top:6px;' align=center><input type='button' onclick='parent.viewInfo()' value='查看' style='border-style:solid;border-width:1px;BACKGROUND-COLOR: #c9d3f3'/>&nbsp;&nbsp;&nbsp;&nbsp;<input type='button' onclick='parent.ignore()' value='忽略' style='border-style:solid;border-width:1px;BACKGROUND-COLOR: #c9d3f3'/> </DIV></DIV>";
	var winstr="<DIV id=eMeng style='filter:progid:DXImageTransform.Microsoft.Gradient(gradienttype=0, startcolorstr=#ffffff, endcolorstr=#D9E8EB);background-position: right;background-repeat: repeat-y;border: 1px solid #C3DCE0;Z-INDEX:99999; LEFT: 0px;  WIDTH: 200px; POSITION: absolute; TOP: 0px; HEIGHT: 150px'>"
					+"<TABLE width='100%' border=0 cellPadding=0 cellSpacing=0 style='BORDER-TOP: #ffffff 1px solid; BORDER-LEFT: #ffffff 1px solid'>"
						+"<TBODY>"
							+"<TR>"
								+"<TD  width=30 ></TD>"
								+"<TD vAlign=center width='100%' style='filter:progid:DXImageTransform.Microsoft.Gradient(gradienttype=0, startcolorstr=#0E6DA6, endcolorstr=#8BC2DC);font-weight: bold;color: #FFFFFF;text-decoration: none;line-height: 16px;height: 22px;padding: 3px;font-size: 12px;'> 消息提示：</TD>"
								+"<TD  vAlign=center align=right id='ttpop' width=19 style='filter:progid:DXImageTransform.Microsoft.Gradient(gradienttype=0, startcolorstr=#0E6DA6, endcolorstr=#8BC2DC);font-weight: bold;color: #FFFFFF;text-decoration: none;line-height: 16px;height: 22px;padding: 3px;font-size: 12px;'>"
									+"<span title=关闭  onclick='parent.closeDiv();' style='cursor:hand;' >×</span>"
								+"</TD>"
							+"</TR>"
							+"<TR><TD style='PADDING-RIGHT: 1px; BACKGROUND-IMAGE: url(1msgBottomBg.jpg); PADDING-BOTTOM: 1px' colSpan=3 height=90>"
								+"<DIV style='PADDING-RIGHT: 13px;  PADDING-LEFT: 13px; FONT-SIZE: 12px; PADDING-BOTTOM: 13px; WIDTH: 100%; COLOR: #1f336b; PADDING-TOP: 18px;  HEIGHT: 100%;'>"
									+"<DIV align=center style='text-align: left;text-indent: 24px;line-height: 18px;color: #336699;font-size: 13px;'>"+msg+"</DIV>"
									+"</DIV>"
								+"</TD>"
							+"</TR>"
						+"</TBODY>"
					+"</TABLE>"
		+"</DIV>";
	oPopup.document.body.innerHTML = winstr;
	popshow();
	playSound(soundUrl);
}
function popshow(){
	if(popTop>1500){
	closeDiv();
	return;
	}
	else if(popTop>1400&&popTop<1500){
		oPopup.show(screen.width-250,screen.height,200,1500-popTop);
	}else if(popTop>1300&&popTop<1400){
		oPopup.show(screen.width-250,screen.height+(popTop-1300),200,150);
	}else if(popTop<180){
		oPopup.show(screen.width-250,screen.height,200,popTop);
	}else if(popTop<200){
		oPopup.show(screen.width-250,screen.height-popTop,200,150);
	}
	popTop+=10;
	window.mytime=setTimeout("popshow();",70);
}
function circleExe(sound){
	if(isLimit==1){
		//soundUrl=sound || "sound/sound.wav";
		soundUrl=sound;
		window.setInterval("formMessage();",300000);	
	}
}

function formMessage(){	
	popmsg("你使用的是试用版本<br>获取正式版本请联系:上海华颉信息技术有限公司");	
}
function getInfoRecord(record){
	var obj = new Array();
	var cookieName = 'myNoReceive';
	var cookieValue = getCookie(cookieName);
	if(cookieValue){
		obj = arrayDistinct(cookieValue.split(';'),record.split(';'));
	} else{
		obj = record.split(";");
	}
	return obj.length;
}
function arrayDistinct(mainArr,valueArr)
	{		
		var result= new Array();
		for (i = 0; i < valueArr.length; i++)
		{
			if(valueArr[i] !="")
			{
				var isAdd = true;
				for(j = 0; j < mainArr.length; j++)
				{
					if (mainArr[j] == valueArr[i])
					{		
						isAdd = false;
						break;
					}
				}
				if (isAdd)
				{
					result.push(valueArr[i]);
				}
			}
		}
		return result;
	}
function writeRecord(v){
	if(navigator.cookieEnabled){
		var cookieName = 'myNoReceive';
		if(getCookie(cookieName)){
			deleteCookie(cookieName);			
		} 
		setCookie(cookieName,v,1);
	}
}
function setCookie(cookieName,cvalue,expiredays,path){
  var expireDate=new Date();
  var expireStr="";
  if(expiredays!=null) {
	expireDate.setTime(expireDate.getTime()+(expiredays*24*3600*1000));
	expireStr="; expires="+expireDate.toGMTString();
  }
  pathStr=(path==null)?"; path=/":"; path="+path;
  document.cookie=cookieName+'='+escape(cvalue)+expireStr+pathStr;
}

function getCookie(cookieName){
  var index=-1;
  if(document.cookie)
 	index=document.cookie.indexOf(cookieName);
  if(index==-1){
 	return "";
  }else{
 	var iBegin = (document.cookie.indexOf("=", index) +1);
     var iEnd =document.cookie.indexOf(";", index);
     if(iEnd == -1){
        iEnd = document.cookie.length;
     }
     return unescape(document.cookie.substring(iBegin,iEnd));
  }
}

 function deleteCookie(name,path,domain) 
    {
        if(getCookie(name))
        {
            document.cookie = name + "=" +
                ((path) ? "; path=" + path : "; path=/") +
                ((domain) ? "; domain=" + domain : "") +
                "; expires=Thu, 01-Jan-70 00:00:01 GMT";
        }
    }
