function fixWidth(selectObj)
{//return false;
    var newSelectObj = document.createElement("select");
    newSelectObj = selectObj.cloneNode(true);
    newSelectObj.selectedIndex = selectObj.selectedIndex;
    newSelectObj.onmouseover = null;
    
    var e = selectObj;
    var absTop = e.offsetTop;
    var absLeft = e.offsetLeft;
    while(e = e.offsetParent)
    {
        absTop += e.offsetTop;
        absLeft += e.offsetLeft;
    }
    with (newSelectObj.style)
    {
        position = "absolute";
        top = absTop + "px";
        left = absLeft + "px";
        width = "auto";
    }
    
    var rollback = function(){ RollbackWidth(selectObj, newSelectObj); };
    if(window.addEventListener)
    {
        newSelectObj.addEventListener("blur", rollback, false);
        newSelectObj.addEventListener("change", rollback, false);
    }
    else
    {
        newSelectObj.attachEvent("onblur", rollback);
        newSelectObj.attachEvent("onchange", rollback);
    }
    
    selectObj.style.visibility = "hidden";
    document.body.appendChild(newSelectObj);
    newSelectObj.focus();
}

function RollbackWidth(selectObj, newSelectObj)
{
    selectObj.selectedIndex = newSelectObj.selectedIndex;
    selectObj.style.visibility = "visible";
    document.body.removeChild(newSelectObj);
}

//增加金额的输入判断
function checkkey(v,d){ 
	var kc=event.keyCode;
	if(d=='0' && (v.indexOf('.') >-1 || kc ==46)){
		event.returnValue = false; 
		alert("只能是整数");
	}
	//只允许输入“+”“-”“.”“回车”“0-9”等字符。 
	if (kc==43 || kc ==45 || kc ==46 || (kc >47 && kc <58)){
		if(kc==46 && v.indexOf('.') >-1){
			//不允许输入多个小数点
			event.returnValue = false; 
		}
		if(kc==43){
			//不允许输入多个正号，不允许在数字中间输入正号 
			if (v.length>0 && v.indexOf('+')>-1){
				//event.returnValue = false;
			}
		}else if (kc==45){
			//不允许输入多个负号，不允许在数字中间输入负号 
			if (v.length>0 && v.indexOf('-')>-1){
				//event.returnValue = false;
			}
		}
	}else{
		event.returnValue = false; 
		alert("请输入数值类型"); 
	}
}