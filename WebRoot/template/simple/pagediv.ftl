<script type="text/javascript">
//向指定页码翻页
function turnPage(pageNumber){
	document.forms[0].reset();
	document.forms[0].action = document.forms[0].action + "?paginationList.currentPage=" + pageNumber+"&pagefalg=P";
	document.forms[0].submit()
}



function enterkey()
{
   var pageSize = document.getElementById("paginationList.pageSize");
   var key = window.event.keyCode;
   if(key == 13){
      //document.forms[0].action = document.forms[0].action+"?paginationList.currentPage=" + pageSize.value;
      document.forms[0].submit();
   }
}
function isNumber(s)   
{   
	var pageSize = document.getElementById("paginationList.pageSize");
	var patrn=/^[0-9]*[1-9][0-9]*$/; 
	if (!patrn.exec(pageSize.value)){ 
		alert("请输入大于1的整数。");
		pageSize.value="1";
		return false
	}
	if (pageSize.value>100){
	    document.getElementById("paginationList.pageSize").value = 100;
	    alert("每页显示条数不能大于100");
	}
	
	
	return true  
}  

function createPagination(doccount,pcount,curpage){
	var showCountStr = '${paginationList.showCount}';
	var toLongFlag = "...";
	var inputTxt = "每页显示条数 <input type='text' size='3'  onkeydown='enterkey()' onkeyup='isNumber(this)' id='paginationList.pageSize' name='paginationList.pageSize' value='"+pcount+"'>";
	
	if(doccount>0){
  		var strhtml = "<div id=page>";
  		var spoint = (curpage-1)*pcount+1;
 	 	if(doccount<=pcount){
      			strhtml += "("+spoint+"－"+doccount+")/共"+doccount+"&nbsp;&nbsp;";
      			strhtml += "第<font color=#ff0000>1</font>页/共1页&nbsp;&nbsp;";
      			strhtml += inputTxt + "&nbsp;&nbsp;";
       	}
    	else{
      			var epoint = spoint + pcount - 1;
      			var curpage = epoint/pcount;
      			var pagesum;
      			var count = doccount%pcount;
      			if(count==0)
        			pagesum = doccount/pcount;
      			else
        			pagesum = (doccount-count)/pcount+1;
      			if(curpage==pagesum){
      				
      				if("false"==showCountStr){
      					strhtml += "("+spoint+"－"+doccount+")/共"+toLongFlag+"&nbsp;&nbsp;";
      				}else{
      					strhtml += "("+spoint+"－"+doccount+")/共"+doccount+"&nbsp;&nbsp;";
      				}
      			}        			
      			else{
      				if("false"==showCountStr){
      					strhtml += "("+spoint+"－"+(pcount*curpage)+")/共"+toLongFlag+"&nbsp;&nbsp;";
      				}else{
      					strhtml += "("+spoint+"－"+(pcount*curpage)+")/共"+doccount+"&nbsp;&nbsp;";
      				}
      				
      			}
      			
      			if("false"==showCountStr){
      				strhtml += "第<font color=red>"+curpage+"</font>页/共"+toLongFlag+"页&nbsp;&nbsp;";
  				}else{
  					strhtml += "第<font color=red>"+curpage+"</font>页/共"+pagesum+"页&nbsp;&nbsp;";
  				}
        			
      			
      			strhtml += inputTxt + "&nbsp;&nbsp;";
      			if(curpage==1){
        			strhtml += "<font color=#aaaaaa>首页</font>&nbsp;&nbsp;";
        			strhtml += "<font color=#aaaaaa>上一页</font>&nbsp;&nbsp;";
         		}
      			else{
        			strhtml += "<a href=javascript:turnPage(1)>首页</a>&nbsp;&nbsp;";
        			strhtml += "<a href=javascript:turnPage('"+(curpage-1)+"')>上一页</a>&nbsp;&nbsp;";
          		}
      			if(curpage==pagesum){
        			strhtml += "<font color=#aaaaaa>下一页</font>&nbsp;&nbsp;";
        			strhtml += "<font color=#aaaaaa>尾页</font>&nbsp;&nbsp;";
        	 	}
      			else{
        			strhtml += "<a href=javascript:turnPage('"+(curpage+1)+"')>下一页</a>&nbsp;&nbsp;";
        			strhtml += "<a href=javascript:turnPage('"+pagesum+"')>尾页</a>&nbsp;&nbsp;";
          		}
      			strhtml += "跳转至：<select onchange=turnPage(this.options[this.selectedIndex].value)>"
      			
             	
             	if("false"==showCountStr){
             		for(i=1;i<=100;i++){
	        			if(curpage==i)
	          				strhtml += "<option selected value="+i+">"+i;
	        			else
	          				strhtml += "<option value="+i+">"+i;
	             	}
  				}else{
	  				for(i=1;i<=pagesum;i++){
	        			if(curpage==i)
	          				strhtml += "<option selected value="+i+">"+i;
	        			else
	          				strhtml += "<option value="+i+">"+i;
	             	}
  				}
     	}
     	strhtml += "</div>";
    	document.write(strhtml);
	}
}
createPagination(${paginationList.recordCountStr},${paginationList.pageSizeStr},${paginationList.currentPageStr});
</script>