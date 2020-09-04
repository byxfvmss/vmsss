//发 票空白作废
			
		function BillCancel(){
			var fapiaoTypes = document.getElementsByName("fapiaoTypes");
			var fapiaoType = fapiaoTypes[0].value;
		
			$.ajax({url: 'billCancelSelver.action',
					type: 'POST',
					data:{fapiaoType:fapiaoType},
					async:false,
					dataType: 'text',
					error: function(){
						return false;},
					success: function(result){
				//alert(result);
			try
				    {
				ret = sk.Operate(result);
				//	alert(ret);
				getBillCancelResult(ret);
				
				 	   }
					catch(e)
				   	 {
				alert(e.message + ",errno:" + e.number);
				 	  }
							}
				});
		}
		//发票作废解析
		function getBillCancelResult(ret){
			var aa="";
			//alert(ret);
			$.ajax({
				url:'getBillBankCancelResult.action',
					type: 'POST',
					async:false,
					data:{param:ret},
					dataType: 'text',
					error: function(){
						return false;},
					success: function(result){
							alert(result);
							if(result=="errorNo"){
							//alert(aa);
							}else if(result=="success"){
								//alert("发票作废成功");
								
								
							}
							
					}
				});
			return aa;
		}
		