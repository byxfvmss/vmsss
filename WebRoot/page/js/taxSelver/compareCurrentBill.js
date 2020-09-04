/**
* 当前票号比对
*/
		function CompareCurrentBill(){
			
			var aa=CreateCurrentBillXML();
			//alert("当前票号为"+aa);
			var re="";
			if(aa=="nobill"){
				alert("发票领购信息已用完");
				re="error";
			}else{
				$.ajax({url: 'CompareCurrentBill.action',
					type: 'POST',
					async:false,
					data:{result:aa},
					dataType: 'text',
					error: function(){
						return false;
						},
					success: function(result){
					//alert("比对结果"+result);
					re=result;
					}
				});
				}
			return re;
		}