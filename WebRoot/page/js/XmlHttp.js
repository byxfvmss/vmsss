function sendXmlData(xmlData,serverUrl)
{
	var xmldoc = new ActiveXObject("Msxml2.DOMDocument");
	xmldoc.async = false;
	xmldoc.loadXML(xmlData);
	var xmlhttp = new ActiveXObject("Msxml2.XMLHTTP");	
	var url=serverUrl;	
	xmlhttp.open("post", url, false);
	xmlhttp.send(xmldoc);
	if(testXmlhttp(xmlhttp))
	{
		return xmlhttp.responseText;
	}
	else
	{
		var result="<Result><value>0</value><msg>" + xmlhttp.statusText + "</msg></Result>";
		return result;
	}
}
function sendXmlHttp(serviceUrl)
{
	var xmlhttp = new ActiveXObject("MSXML2.XMLHTTP");
	var date = new Date();
	if(serviceUrl.indexOf("?")>0)
	{
		serviceUrl += "&" + date.getTime();
	}
	else
	{
		serviceUrl += "?" + date.getTime();
	}
	xmlhttp.open("GET", serviceUrl, false);
	xmlhttp.send();
	var reMsg="";
	if(testXmlhttp(xmlhttp))
	{
        reMsg=xmlhttp.responseText;
    }
    return reMsg;
}
function sendXmlHttpPost(serviceUrl,param)
{
	var xmlhttp = new ActiveXObject("MSXML2.XMLHTTP");
	var date = new Date();
	if(serviceUrl.indexOf("?")>0)
	{
		serviceUrl += "&" + date.getTime();
	}
	else
	{
		serviceUrl += "?" + date.getTime();
	}	
	xmlhttp.open("post", serviceUrl, false);
	xmlhttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	xmlhttp.setRequestHeader("charset","utf-8");
	param = param || null;
	xmlhttp.send(param);
	var reMsg="";
	if(testXmlhttp(xmlhttp))
	{
        reMsg=xmlhttp.responseText;
    }
    return reMsg;
}	
function testXmlhttp(xmlhttp)
{
	if(xmlhttp.status != 200)
	{
		return false;
	}
	return true;
}
