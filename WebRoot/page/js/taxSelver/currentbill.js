/**
 * *
	查询当前票号
**/
		function CreateCurrentBillXML(){
			var fapiaoTypes = document.getElementsByName("fapiaoTypes");
				var fapiaoType = fapiaoTypes[0].value;
			//alert(fapiaoType);
			var aa="";
			$.ajax({url: 'CreateCurrentBillXML.action',
					type: 'POST',
					async:false,
					data:{fapiaoType:fapiaoType},
					dataType: 'text',
					error: function(){
						return false;},
					success: function(result){
							//alert(result);
					try
					    {
					var ret = sk.Operate(result);
					aa=parseCurrentBillXml(ret);
					//alert(ret);
					    }
					catch(e)
					    {
					alert(e.message + ",errno:" + e.number);
					    }	
					}
				});
			//alert("当前票号为"+aa);
			return aa;
		}
		/*
		**
		* 解析当前票号
		*/
		function parseCurrentBillXml(ret){
			var aa="";
		//	alert(aa);
			$.ajax({url: 'parseCurrentBillXml.action',
					type: 'POST',
					data:{param:ret},
					async:false,
					dataType: 'text',
					error: function(){
						return false;},
					success: function(result){
						//	alert(result);
							if(result=="nobill"){
								
								aa="nobill";
							}else{
								aa=result;
							//	alert("当前票号"+aa);
							}
					}
				});
			return aa;
			
		}