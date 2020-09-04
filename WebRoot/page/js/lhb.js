 function formatAmount(thisPoint,lScale,lIsMin,canEmpty)
 {
 	var strData = thisPoint.value;
	strData = reverseFormatAmountString(strData);
	//strData = parseFloat(strData);
	//if (strData>999999999999.99)
	//	strData = 0.00;
	strData = formatAmountString(strData,lScale,lIsMin,canEmpty);
	thisPoint.value=strData;
 }


 function reverseFormatAmountString(strAmount)
 {
		strTemp=new String(strAmount);
		strAmount="";
		for(var i=0;i<strTemp.length;i++)
		{
			var cData;
			cData=strTemp.charAt(i);
			if (cData!=",")
			{
				strAmount=strAmount+cData;
			}
		}
		return strAmount;
 }
 

 function formatAmountString(strData,lScale,lIsMin,canEmpty)
 {
	//if(!isNaN(parseFloat(strData)))
	if(!isNaN((strData)))
 	{
		if(strData!=null)
 		{
			var i,strTemp;

			strTemp=new String(strData);
			var isValidZero = true;
			var isValidComma = true;
			if(strTemp.charAt(0)==".")
			{
				strTemp=new String("0"+strData);
			}
			if(strTemp.charAt(0)!="-" && lIsMin == -1)
			{
				strTemp=new String("-"+strData);
			}
			strData="";
			for(i=0;i<strTemp.length;i++)
			{
				var cData;
				cData=strTemp.charAt(i);
				if(cData=="-" && i==0 && (lIsMin==null || lIsMin=="undefined" || lIsMin == 0 || lIsMin == -1))
				{
					strData = "-";
				}
				else
				if (cData=="0")
				{
					if (isValidZero)
						strData = strData + cData;
				}
				else
				if (cData==".")
				{
					if (strData!="" && isValidComma)
					{
						strData = strData + cData;
						isValidComma=true;
					}
				}
				else
				if (cData!="," && cData!=" ")
				{
					if (!isNaN(cData) || cData==".")
					{
						strData=strData+cData;
						isValidZero = true;
					}
					else
					{
						strData="";
						i=10000;
					}
				}
			}
		}
		if(strData!="")
 		{
			//var strRoundAmunt ;
			//strRoundAmunt = getRoundAmount(strData,lScale);
			//strData = "" + strRoundAmunt;

	 		var nPoint;
	 		nPoint=strData.indexOf(".");
	 		var strFront=strData,strEnd="";
	 		if(nPoint!=-1)
	 		{
	 			strFront=strData.substring(0,nPoint);
	 			strEnd=strData.substring(nPoint+1,strData.length);
	 		}

			strTemp=new String(strFront);
			var bHaveMinus=false;
			if(strFront.substring(0,1)=="-")
			{
				bHaveMinus=true;
				strTemp=strTemp.substring(1,strTemp.length);
			}
			strFront="";
			var nNum;
			nNum=0;
			for(i=strTemp.length-1;i>=0;i--)
			{
				if(nNum==3)
				{
					strFront=","+strFront ;
					nNum=0;
				}
				nNum++;
				var cData;
				cData=strTemp.charAt(i);
				strFront=cData+strFront;
			}
			if(bHaveMinus)
			{
				strFront="-" + strFront;
			}
	 		if(strEnd.length>lScale)
	 		{
	 			strEnd=strEnd.substring(0,lScale);
	 		}
	 		else
	 		{
	 			for (i=strEnd.length;i<lScale;i++)
					strEnd = strEnd + "0";
	 		}

			if (parseInt(lScale,10)> 0)
				{
			 		strData=strFront+"." + strEnd;
				}
			else
				{
					strData=strFront;
				}
 		}
		else
		{
			strData="";
		}
	}
	else
	{
		strData = "";
	}

	if (strData=="")
	{
		if (canEmpty!="true")
		{
			if (parseInt(lScale,10)> 0)
				{
					strData="0.";
					for (i=0;i<lScale;i++)
						{
							strData=strData+"0";
						}
				}
			else
				{
					strData="0";
				}
		}
	}
	return strData;
 }

 function getRoundAmount(strData,lScale)
 {
 	var lRectify = 1;
 	if (lScale==0)
		lRectify = 1;
	else if (lScale==1)
		lRectify = 10;
	else if (lScale==2)
		lRectify = 100;
	else if (lScale==3)
		lRectify = 1000;
	else if (lScale==4)
		lRectify = 10000;
	else if (lScale==5)
		lRectify = 100000;
	return Math.round(parseFloat(strData)*lRectify)/lRectify;
 }

String.prototype.startWith=function(str){ 
	var reg=new RegExp("^"+str); 
	return reg.test(this); 
} 
String.prototype.endWith=function(str){ 
	var reg=new RegExp(str+"$"); 
	return reg.test(this); 
}

function isRadioChecked(checkIdObj)
{
	//对象是否为空
	if (checkIdObj == null)
	{
		return false;
	}
	//checkbox,并且列表对象有多个
	if(checkIdObj.length)
	{
		var sum = 0;
		for (i=0; i<checkIdObj.length; i++)
		{
			if(checkIdObj[i].checked  &&  checkIdObj[i].disabled==false)
			{
				sum++;
			}
		}
		//checkbox中只有一个选中
		if (sum == 1)
		{
			return true;
		}
	}
	//radio,或checkbox列表对象只有一个
	else if(checkIdObj.checked   &&  checkIdObj.disabled==false)
	{
		return true;
	}
}