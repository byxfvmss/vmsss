/*
	税控信息查询
**/
function taxKeyQuery(){
	var aa="";
				$.ajax({url: 'createTaxKeyQueryXml.action',
					type: 'POST',
					async:false,
					dataType: 'text',
				//	timeout: 1000,
					error: function(){
						return false;},
					success: function(result){
					//	alert(result);
						try
						    {
						///alert(result);
						ret = sk.Operate(result);
						aa=	parseTaxKeyXml(ret)
						//alert(ret);
						    }
						catch(e)
						    {
						alert(e.message + ",errno:" + e.number);
						    }	
						
					}
				});
				return aa;
	}
		function parseTaxKeyXml(ret){
			var aa="";
						$.ajax({url: 'parseTaxKeyReturnXml.action',
					type: 'POST',
					async:false,
					data:{param:ret},
					dataType: 'text',
				//	timeout: 1000,
					error: function(){
						return false;},
					success: function(result){
						//alert(result);
						//aa=result;
						var arr=new Array();
						arr=result.split("|");
						if(arr[0]==1){
							aa=result;
						}else{
							aa="error";
							alert(arr[1]);
						}
					}
				});
				return aa;
		}