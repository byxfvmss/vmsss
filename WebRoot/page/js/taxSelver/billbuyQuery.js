function buyQuery(fapiaoType){
	var aa=""
		$.ajax({url: 'createBillBuyQueryXml.action',
						type: 'POST',
						async:false,
						data:{fapiaoType:fapiaoType},
						dataType: 'text',
					//	timeout: 1000,
						error: function(){
							return false;},
						success: function(result){
							//	alert(result);
								try
							    {
							ret = sk.Operate(result);
							//alert(ret);
							
								aa=parseBuyQueryXml(ret,fapiaoType);
							    }
							catch(e)
							    {
							alert(e.message + ",errno:" + e.number);
							    }	
							
								
						}
							});
	//alert(aa);
	return aa;
	}
	function parseBuyQueryXml(param,fapiaoType){
		var aa="";
		$.ajax({url: 'parseBillBuyQueryReturnXml.action',
						type: 'POST',
						async:false,
						data:{param:param,fapiaoType:fapiaoType},
						dataType: 'text',
					//	timeout: 1000,
						error: function(){
							return false;},
						success: function(result){
							//	alert(result);
								var arr=new Array();
								arr=result.split("|");
								//alert(arr);
								//alert(arr[0]);
								if(arr[0]==1){
									aa=result;
								}else{
									//alert(arr[1]);
									aa="error";
								}
								
						}
							});
		//alert(aa);
		return aa;
	}