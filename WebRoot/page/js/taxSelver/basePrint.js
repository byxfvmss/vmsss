/*** 
 * 顶层类 打印字符串
****/
function showOcxSelver(ids,faPiaoType,printLimitValue,fileName){
			var a="success";
			$.ajax({
							   type: "POST",
							   async:false,
							   cache:false,
							   url: 'showOCXstring.action',
							   data:{billIds:ids,faPiaoType:faPiaoType,printLimitValue:printLimitValue},
							   dataType: 'html',
							   success : function(result){
							   if(result=='printValueError'){
							   	 alert("单次打印超过限定的值"+printLimitValue+"请重新选择");
							   	 a= "error";
							    }
							  var arr= new Array();
							  //alert(result);
							arr = result.split("+");
					    	var ocxString=arr[0];
					    	if(arr[0]==0){
					    		alert(arr[1]);
					    		return false;
					    	}
					    	var billNo=arr[1];
					   		var sum=arr[2];
					    	if(confirm(billNo+"\n是否打印这些发票?")){
    							ocxString=ocxString;
    							alert(ocxString);
    							dataSelverutil(ocxString,fileName,faPiaoType)
					    		
					    		//updateBillStatus(statas);
    							}
							   },
							   error:function(){
							      alert("获取组装传进OCX的字符串过程出现异常!");
							   }
							 });
			
		}
		function dataSelverutil(ocxString,fileName,faPiaoType){
			alert(ocxString);
			var ar=new Array();
			ar=ocxString.split("|");
			//alert(ar.length)
			var a=ar.length==1?1:ar.length-1;
			var j=0;
			//alert(a);
			for(var i=0;i<a;i++){
				var arr=new Array();
				arr=ar[i].split("^");
				var billId=arr[1];
				var billCode=arr[2];
				alert(billCode);
				var billNo=arr[3];
				//alert(j);
				//alert(billNo);
				//return false;
				j=dataprintBase(billId,faPiaoType,billCode,billNo,fileName,j,a-1,i)
				if(j=="error"){
				break;
				}
			}
		}
		/***TAXPARAMETERS
		createBillPrintXml 创建打印的xml 文件
		
		*/
		function dataprintBase(billId,fapiaoType,billCode,billNo,fileName,j,a,i){
		//	alert("11");
			alert(billCode);
			$.ajax({
					   async:false,
					   type: "POST",
					   cache:false,
					   url: "createBillPrintXml.action",
					   data:{fapiaoType:fapiaoType,billCode:billCode,billNo:billNo,fileName:fileName},
					   dataType:"text",
					   error: function(){
							return false;},
					   success : function(result){
									//alert(result);
								try
							    {
							var ret = sk.Operate(result);
							
								//alert(ret)
							 j= updateBillServerStatus(ret,billId,fileName,j,a,i);
							    }
							catch(e)
							    {
							alert(e.message + ",errno:" + e.number);
							    }	
								
					  }
					});
			return j;
			
		}
	function updateBillServerStatus(statas,billId,fileName,j,a,i){
		//alert(statas);
		$.ajax({
							   type: "POST",
							   async:false, 
							   cache:false,
							   url: 'updatePrintServerResult.action',
							   data:{data:statas,billId:billId,fileName:fileName},
							   dataType: "html",
							   success : function(result){
								   if(result=="success"){
									   j++;
								   }
								 	 if(result!="success"){
									   alert(result);
									   j="error";
							   	submitAction(document.forms[0], "listBillPrint.action??paginationList.showCount="+"false");
							   	 document.forms[0].action="listBillPrint.action?paginationList.showCount="+"false";
							   	 return j;
								 }else{
								  if(a==i){
									  
									    a=a+1;
								alert("打印"+a+"条!打印成功"+j+"条");
							submitAction(document.forms[0], "listBillPrint.action??paginationList.showCount="+"false");
							   document.forms[0].action="listBillPrint.action?paginationList.showCount="+"false";
									
								  } 
								  }
							   },
							   error:function(){
							      alert("根据OCX返回结果更新发票状态过程出现异常!");
							   }
							 })
							 return j;
							
	}