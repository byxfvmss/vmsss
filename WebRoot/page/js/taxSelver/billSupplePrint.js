function billSupplementPrintSelver(){
			var faPiaoType=document.getElementById("billCancelInfo.fapiaoType").value;
			//alert(faPiaoType);
			var billIds = document.getElementsByName("selectBillIds");
			var fileName="billSupplementPrint.xml";
					if (!checkChkBoxesSelected("selectBillIds")) {
						alert("请选择票据记录！");
						return false;
					}
					try{
						
					var myBillNo=new Array();
					var j=0;
					for (var i = 0; i < billIds.length; i++){
						if (billIds[i].checked){
							myBillNo.push(billIds[i].value);
							j++;
						}
					}
					var ids=myBillNo[0];
					//alert(j);
					if(j>1){
						alert("请选择一条记录进行补打");
						return false;
						}
					showOcxSelverString('',ids,faPiaoType,fileName)
					}catch(e){
						alert("请安装税控服务插件");
					}
					
		}
		function showOcxSelverString(result1,ids,faPiaoType,fileName){
			$.ajax({
							   type: "POST",
							   async:false,
							   cache:false,
							   url: 'showOCXSubstring.action',
							   data:{billIds:ids,faPiaoType:faPiaoType},
							   dataType: 'html',
							   success : function(result){
					   		//var sum=arr[2];
					    	if(confirm("\n是否打印发票?")){
    						//alert(result);
					    		dataSelverutil1(result,fileName,faPiaoType);
    							}
							   },
							   error:function(){
							      alert("获取组装传进OCX的字符串过程出现异常!");
							   }
							 });
			
			
		}
		function dataSelverutil1(ocxString,fileName,faPiaoType){
		//	alert(fileName);
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
				var billNo=arr[3];
				//alert(j);
				//alert(billNo);
				//return false;
				j=dataprintBase1(billId,faPiaoType,billCode,billNo,fileName,j,a-1,i)
				
			}
		}
		function dataprintBase1(billId,fapiaoType,billCode,billNo,fileName,j,a,i){
		//	alert("11");
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
							
				//			alert(ret)
							 j= updateBillServerStatus1(ret,billId,fileName,j,a,i);
							    }
							catch(e)
							    {
							alert(e.message + ",errno:" + e.number);
							    }	
								
					  }
					});
			return j;
			
		}
	function updateBillServerStatus1(statas,billId,fileName,j,a,i){
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
								  if(a==i){
									  if(a==0&result!="success"){
									  alert(result);
								submitAction(document.forms[0], "listBillSupplementPrint.action??paginationList.showCount="+"false");
							   	 document.forms[0].action="listBillSupplementPrint.action?paginationList.showCount="+"false";
							   		// location.reload();

									  }else{
										  a=a+1;
								alert("打印"+a+"条!打印成功"+j+"条");
							submitAction(document.forms[0], "listBillSupplementPrint.action??paginationList.showCount="+"false");
							   document.forms[0].action="listBillSupplementPrint.action?paginationList.showCount="+"false";
							   	 }
								  } 
							   },
							   error:function(){
							      alert("根据OCX返回结果更新发票状态过程出现异常!");
							   }
							 })
							 return j;
							
	}
		