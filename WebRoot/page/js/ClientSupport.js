var searchObject = null;
var mSpac = 21;
var iii = 0;
var mValue = "";
var buttonStatu = false;
			
function DigitalOnly()
{
	var nCode = event.keyCode;
    if(nCode < 48 || nCode > 57)
	{
		event.keyCode = 0;
	}
}
function NotQuote()
{
	var nCode = event.keyCode;
    if(nCode==39)
	{
		event.keyCode = 8217;
	}
}

function FloatOnly(obj)
{
	var strValue = obj.value;	
	var nCode = event.keyCode;
	if(nCode == 46)
	{
		if(strValue.indexOf(".") > - 1)
		{
			event.keyCode = 0;
			return;
		}
	}
	else if(nCode==45){
		if(strValue.indexOf("-") > -1){
			event.keyCode = 0;
			return;
		}
	}
	else if((nCode < 48 || nCode > 57) && nCode!=45)
	{
		event.keyCode = 0;
		return ;
	}
}

function FloatOnlyNew(obj)
{
	var strValue = obj.value;	
	reg=/^((-?|\+?)\d+)(\.\d+)?$/;
	if(strValue.match(reg)==null){
		event.keyCode = 0;
	}
}

function CheckIsBlank(obj)
{
	var str = obj.value;
	if(str == '' || str == null)
		return true;
	var length = str.length;
	for(var i = 0;i < length;i ++)
	{ 
		if(str.charAt(i) != ' ')  
			return false;
	}
	return true;
}

function CheckLength(obj,len)
{
	var str = obj.value;
	var vlen = int(len);
	if(str == null || str.length !=vlen)
	{
		alert('字符长度应为'+vlen);
		return false;
	}
		
	return true;
}




//页面上搜索文本


function findGo(obj,cValue){
	if(event.keyCode == 39 || event.keyCode == 13 || event.keyCode == 40 || event.keyCode == 37 || event.keyCode == 38){
		findText(obj,cValue);
		event.keyCode == 13;
	}
}


function findText(obj,cValue)
{
	var iFlag = 1;
	if(obj ==null)
	{
		obj = document.body;
	}
	if (cValue == null || cValue == '')
	{
		return;
	}
	if(mValue!=cValue)
	{
		iii = 0;
	}
	var rng = obj.createTextRange();
	for (i=0; rng.findText(cValue)!=false; i++) 
	{
		var k = rng.duplicate();
	    rng.collapse(false);
		if (i>=iii)
		{
		    k.select();
			k.scrollIntoView();
			iii+=iFlag;
			mValue = cValue;
			break;

		}

	}
	if(i<iii)
	{
	}
	else
	{
		iii=0;
	}
	
}

function findNext()
{
	if(event.keyCode==13)
		event.keyCode=9;
}

//复选框全选

function chkall(input1,input2)
{
    var objForm = document.forms[input1];
    var objLen = objForm.length;
    for (var iCount = 0; iCount < objLen; iCount++)
    {
        if (input2.checked == true)
        {
            if (objForm.elements[iCount].type == "checkbox")
            {
                objForm.elements[iCount].checked = true;
            }
        }
        else
        {
            if (objForm.elements[iCount].type == "checkbox")
            {
                objForm.elements[iCount].checked = false;
            }
        }
    }
} 

//页面上搜索文本

function findTextFillItem(obj,cValue)
{
	var searchObject = null;
	var mSpac = 21;
	var iii = 0;
	var mValue = "";
	var buttonStatu = false;
	
	var iFlag = 1;
	if(obj ==null)
	{
		obj = document.body;
	}
	if (cValue == null || cValue == '')
	{
		alert("Invalid Value!");
		return;
	}
	if(mValue!=cValue)
	{
		iii = 0;
	}
	var rng = obj.createTextRange();
	for (i=0; rng.findText(cValue)!=false; i++) 
	{
		var k = rng.duplicate();
		rng.collapse(false);
		if (i>=iii)
		{
			k.select();
			k.scrollIntoView();
			iii+=iFlag;
			mValue = cValue;
			break;
		}
	}
	if(i<iii)
	{
		//alert(" '" + cValue + "'第 "+ (i + 1) +" 项已找到.   ");
	}
	else
	{
		alert("Not Find!");
		iii=0;
	}
}
  
/*****************************************************************************
函数名称：CheckAll
处理机能：全选页面中的CheckBox
参数	：object 控制全选的对象
日期	：2007-07-20 
作者    ：wanwanyi
修改人  ：-----修改日--------概要--------------------------
******************************************************************************/
function CheckAll(object)
{
	var aa = document.getElementsByTagName("input");   
	//得到那个总控的复选框的选中状态  
	var state = object.checked;    
	for (var i=0; i<aa.length; i++)   
	{   
		if ( aa[i].type == "checkbox" )   
		aa[i].checked = state;   
	}   
}   
 
/*****************************************************************************
函数名称：CheckAll
处理机能：全选页面中指定DataGrid的CheckBox
参数	：object 控制全选的对象  datagrid 全选的对象
日期	：2007-07-20 
作者    ：wanwanyi
修改人  ：-----修改日--------概要--------------------------
******************************************************************************/
function CheckAll(object,datagrid)
{
	var aa = datagrid.getElementsByTagName("input");   
	//得到那个总控的复选框的选中状态  
	var state = object.checked;    
	for (var i=0; i<aa.length; i++)   
	{   
		if ( aa[i].type == "checkbox" && aa[i].disabled == false )   
		aa[i].checked = state;   
	}   
}  

/*****************************************************************************
函数名称：Change 
处理机能：根据CheckBox状态判断删除按钮是否可用
参数	：CheckBox 事件改变的CheckBox;DataGrid 事件作用的DataGrid;btnDelete 删除按钮ID
日期	：2007-12-07 
作者    ：wanwanyi
修改人  ：-----修改日--------概要--------------------------
******************************************************************************/
function Change(CheckBox,DataGrid,btnDelete)
{    
	//是否有选中
	if(CheckBox.checked && isSelected(DataGrid))
	{   
		btnDelete.disabled = false; 
		//判断是否全部选中，是则将全选置为true
		if(isAllSelected(DataGrid,true))
		{
			DataGrid.getElementsByTagName("input")[0].checked = true;
		} 
	}
	else
	{
		//已经至少一个没有选中，将全选置为false
		DataGrid.getElementsByTagName("input")[0].checked = false;
		//判断是否为全部未选中，是则将删除按钮置灰
		if(isAllSelected(DataGrid,false))
		{
			btnDelete.disabled = true;
		}
	}
}
//是否全部选中/未选中 true:全部选中 false:全部未选中
function isAllSelected(DataGrid,blState)
{ 
	var arrCheck = DataGrid.getElementsByTagName("input");
	//获取除全选框外所有Checkbox的个数
	for(var i = 1;i<arrCheck.length;i++)
	{
		if(arrCheck[i].type == "checkbox" && arrCheck[i].checked != blState)
		{ 
			return false; 
		}
	}
	return true;
}
//判断除全选框外是否有选中
function isSelected(DataGrid)
{
	var arrCheck = DataGrid.getElementsByTagName("input");
	//从第二个开始，第一个为全选
	for(var i = 1 ;i < arrCheck.length ;i ++)
	{ 
		if(arrCheck[i].type == "checkbox" && arrCheck[i].checked)
		{
			return true; 
		}
	}
	return false;
}
		
/*****************************************************************************
函数名称：SelectAll
处理机能：默认全选页面中指定对象的CheckBox
参数	：object 对象 
日期	：2007-07-20 
作者    ：wanwanyi
修改人  ：-----修改日--------概要--------------------------
******************************************************************************/
function SelectAll(object)
{   
	var datagrid = document.getElementById(object);
	if(datagrid == null || datagrid == undefined || (!datagrid))
	{ 
		return false;
	}
	else
	{
		var aa = datagrid.getElementsByTagName("input");    
		for (var i=0; i<aa.length; i++)   
		{   
			if( aa[i].disabled == false )   
				aa[i].checked = true;   
		}   
		return true;
	} 
}  
	
	
/*****************************************************************************
函数名称：OpenViewNewWindow
处理机能：打开查看的非模态窗口

参数	：id  对象id
日期	：2007-07-20 16:09
作者    ：jz_guo
修改人  ：-----修改日--------概要--------------------------
******************************************************************************/
function OpenViewNewWindow(url)
{
	ContentWindow=window.open(url,"_blank","width=620,height=500,directories=no,status=no,scrollbars=yes,resizable=yes,menubar=yes,top=0,left=100")
}

/*****************************************************************************
函数名称：OpenViewTitleNewWindow
处理机能：打开标题查看的非模态窗口

参数	：id  对象id
日期	：2007-07-23 14:09
作者    ：jz_guo
修改人  ：-----修改日--------概要--------------------------
******************************************************************************/
function OpenViewTitleNewWindow(newURL)
{
	var Width = screen.width * 0.8;
	var height = screen.height * 0.8;
	var leftLen = screen.width * 0.1;
	var topLen = screen.height * 0.05;
	ContentWindow = window.open(newURL,"_blank","toolbar=no,width="+Width+",height="+height+",left="+leftLen+",top="+topLen+",directories=no,status=no,scrollbars=yes,resizable=yes,menubar=yes")
}

/*****************************************************************************
函数名称：OpenModifyWindow
处理机能：打开修改窗口
参数	：id  对象id
日期	：2007-07-25 10:18
作者    ：杨洋

修改人  ：-----修改日--------概要--------------------------
******************************************************************************/
function OpenModifyWindow(newURL)
{
	var Width = screen.width * 0.8;
	var height = screen.height * 0.8;
	var leftLen = screen.width * 0.1;
	var topLen = screen.height * 0.05;
	ContentWindow = window.open(newURL,"_self")
}

/*****************************************************************************
函数名称：scrollback

处理机能：页面刷新后记住滚动条位置，待测试

参数	：object 对象 
日期	：2007-07-20 
作者    ：wanwanyi
修改人  ：-----修改日--------概要--------------------------
*****************************************************************************/  
function Trim(strValue)     
{
	return strValue.replace(/^\s*|\s*$/g,"");     
}     
function SetCookie(sName,sValue)  
{     
  document.cookie = sName + "=" + escape(sValue);     
}     
function GetCookie(sName)     
{     
	var aCookie = document.cookie.split(";");     
	for (var i=0; i < aCookie.length; i++)     
	{     
		var aCrumb = aCookie[i].split("=");     
		if (sName == Trim(aCrumb[0]))     
		{     
			return unescape(aCrumb[1]);     
		}     
	}
	return   null; 
}     
function scrollback(object)     
{     
	if(GetCookie("scroll")!=null)
	{
		var ss = document.getElementById(object);
		ss.scrollTop=GetCookie("scroll")
	}     
}      

/*****************************************************************************
函数名称：goToNextFoce
处理机能：用于单元格跳转
	注意： 需要预定义rptType，三种类型，分别为DW->不定长,YW->1104定长,Y0原定长，表示报表类型
		   非1104定长需要预定义startItem, endItem, colCount;1104定长需要预定义startItem, endItem;只适用于定长表
参数	：object 对象 
日期	：2007-08-23 
作者    ：

修改人  ：-----修改日--------概要--------------------------
*****************************************************************************/ 
function goToNextFoce(obj)
{
	if(event.keyCode == 39 || event.keyCode == 13 || event.keyCode == 40 || event.keyCode == 37 || event.keyCode == 38)
	{				
		var strNewName = obj.name;
		/*
		var i = 100;
		do	
		{
			strNewName = getNextItemName(strNewName);
			
			// 避免死循环

			if (i > 100)
				break;
			i++;
		}
		while(document.kkk.elements[strNewName] == null);
		*/
		var form = document.getElementsByTagName("form")[0];
		var inputs = form.getElementsByTagName("input"); 
		for(var i=0;i<inputs.length;i++){
			strNewName = getNextItemName(strNewName);
			var curInput = document.kkk.elements[strNewName];
			if(null != curInput){
				curInput.focus();
				
				curInput.select();
				
				//alert("已搜索至末尾   ");
				break;
			}
		}
		/*
		if( document.kkk.elements[strNewName] != null )
		{
			var curInput = document.kkk.elements[strNewName];
			curInput.focus();
			curInput.select();
		}
		*/
		//event.keyCode = 35;
	}
	return false;
}

function getfocusSelected(obj)
{
		this.select();	
}

function getNextItemName(fullname)
{
	var len = fullname.length;
	var head = "";
	var tail = "";
	if(len == 13)
	{		
		head = fullname.substring(0, len - 9);
		var index = parseInt(fullname.substring(len - 9, len), 10);
		var start = parseInt(startItem.substring(len - 9, len), 10);
		var end = parseInt(endItem.substring(len - 9, len), 10);
		tail = getNextItemTail(index, start, end, 9);
	}
	else if(len == 19)
	{
		// 1104
		head = fullname.substring(0, len - 10);
		
		var lenstart = startItem.length;	
		var lenend = endItem.length;	
		
		var col = parseInt(fullname.substring(len - 5, len), 10);
		var row = parseInt(fullname.substring(len - 10, len - 5), 10);		
		var startCol = parseInt(startItem.substring(lenstart - 5, lenstart), 10);
		var startRow = parseInt(startItem.substring(lenstart - 10, lenstart - 5), 10);		
		var endCol = parseInt(endItem.substring(lenend - 5, lenend), 10);
		var endRow = parseInt(endItem.substring(lenend - 10, lenend - 5), 10);

		tail = getNextItemTailRC(row, col,startRow, endRow, startCol, endCol );
	}
	else if(rptType == "DW")
	{
		head = fullname.substring(0, len - 5);
		var index = parseInt(fullname.substring(len - 5, len), 10);
		var start = parseInt(startItem.substring(len - 5, len), 10);
		var end = parseInt(endItem.substring(len - 5, len), 10);
		tail = getNextItemTail(index, start, end, 5);
		
	}
	if(tail != "")
		fullname = head + tail;

	return fullname;
}

function getNextItemTail(index, start, end, mlen)
{		
	if(event.keyCode == 39)
		index = index + 1;
	else if (event.keyCode == 13 || event.keyCode == 40)
		index = index + colCount;
	else if (event.keyCode == 37 )
		index = index - 1;
	else if (event.keyCode == 38)	
		index = index - colCount;
	
	if(index > end || index < start)
		return ""
	
	var tail = "" + index;
	
	var len1 = tail.length;
	while(len1 < mlen )
	{
		tail = "0" + tail;
		len1++;
	}	
	return tail;
}

// 1104
function getNextItemTailRC(row, col, startRow, endRow, startCol, endCol)
{
	var tail;
	if(event.keyCode == 39)
	{		
		col++;
		if( col > endCol )
		{
			row++;
			col = startCol;
		}
	}
	else if (event.keyCode == 37 )
	{
		col--;
		if(col < startCol)
		{
			row--;
			col = endCol;
		}
	}
	else if (event.keyCode == 13 || event.keyCode == 40)
		row++;
	else if (event.keyCode == 38)
		row--;
	
	if(row < startRow || row > endRow)
		return "";
	
	row = "" + row;
	col = "" + col;
		
	var len1 = row.length;
	while(len1 < 5 )
	{
		row = "0" + row;
		len1++;
	}
	
	len1 = col.length;
	while(len1 < 5 )
	{
		col = "0" + col;
		len1++;
	}
	
	return row + col;
}

 
/*****************************************************************************
函数名称：RedirectFirstUrl
处理机能：点击菜单后主页面跳转到第一个功能点的第一个链接

参数	： 
日期	：2007-08-25  
作者    ：wangwy 
修改人  ：--
******************************************************************************/
function RedirectFirstUrl()
{
	var firstUrl = document.getElementsByTagName("A")[0].href;
	parent.sysmain.location.href = firstUrl; 
}

/*****************************************************************************
函数名称：HideDropDownList
处理机能：修正DropDownList盖住Header文字的BUG
参数	：HeaderText Header显示的文字

日期	：2007-08-27  
作者    ：wangwy 
修改人  ：--
******************************************************************************/
function HideDropDownList(HeaderText)
{ 
	var str = '<table cellpadding=0 cellspacing=0 style="MARGIN: 0px; border:0px;width:100%;height=100%; filter:progid:DXImageTransform.Microsoft.Gradient(gradienttype=0, startcolorstr=#d4f0f9, endcolorstr=#b2def1);color:#2d6391;font-size:12px;font-weight:bold"><tr><td>' + HeaderText + '</td></tr></table>' ;
	var doc = window.frames["WebFrame"].document; 
	doc.open();
	doc.write('<html><head></head><body topmargin=-1 leftmargin=0>' + str + '</body></html>');
	doc.close(); 
}

/*****************************************************************************
函数名称：chkall
处理机能：改变指定Table的列中Checkbox状态 用于补录指标管理中多个币种时的全选
参数	：tableName Table名称
		：inputName 复选框对象
日期	：2008-12-10  
作者    ：wangwy 
修改人  ：--
******************************************************************************/
function chkall(tableName,inputName)
{ 
    //当前操作的列
    var index = inputName.parentElement.cellIndex;   
    //复选框的状态
    var state = inputName.checked;
    //数据Table
    var objTable = document.getElementById(tableName);
    //循环每行   
    for (var iCount = 0; iCount < objTable.rows.length; iCount++)
    {   
        var arr = objTable.rows[iCount].cells[index].getElementsByTagName("input"); 
        for(var i = 0 ; i< arr.length ; i++)
        {
            if(arr[i].type == "checkbox")     
                arr[i].checked = state; 
        } 
    }  
}  