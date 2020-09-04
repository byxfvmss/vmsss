	function configPrintselver(){
			var top ="10";
			var left="10";
			 $.ajax({
					   async:false,
					   type: "POST",
					   cache:false,
					   url: "getPrintMargin.action",
					   data:{top:top,left:left},
					   dataType:"text",
					   error: function(){
							return false;},
					   success : function(result){
								alert(result);
								
								try
								
								    {
								ret = sk.Operate(result);
								 getPrintMarginResult(ret);
								alert(ret);
								    }
								catch(e)
								    {
								alert(e.message + ",errno:" + e.number);
								    }	
					  }
					});
		}
		function getPrintMarginResult(ret){
			
			 $.ajax({
					   async:false,
					   type: "POST",
					   cache:false,
					   url: "getPrintMarginResult.action",
					   data:{param:ret},
					   dataType:"text",
					   error: function(){
							return false;},
					   success : function(result){
								alert(result);
								
					  }
					});
		}