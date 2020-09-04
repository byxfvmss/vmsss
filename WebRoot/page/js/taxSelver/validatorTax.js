

function checkDiskAndSelver(disk,selver){
	var taxParam=document.getElementById("taxParam").value;
			var arr=new Array();
			if(taxParam==1){
				disk;
				
			}else if(taxParam==2){
				aa=ParamSet();
				if(aa==false){
					return aa;
				}
					selver;
			}
			
}
	function ParamSet(){
			var aa="";
			$.ajax({url: 'getParamSet.action',
					type: 'POST',
					async:false,
					dataType: 'text',
					error: function(){
						return false;},
					success: function(result){
					try
					    {
						//alert(aa);
					var ret = sk.Operate(result);
					 aa=getParamResult(ret);
					
					//alert(aa);
					    }
					catch(e)
					    {
					alert("ocx加载失败");
					aa= false;
					    }	
					}
				});
			return aa;
		}
		//参数解析
		function getParamResult(ret){
			var aa="";
			$.ajax({url: 'getParamResult.action',
					type: 'POST',
					async:false,
					data:{param:ret},
					dataType: 'text',
					error: function(){
						return false;},
					success: function(result){
							if(result=="success"){
								alert("参数设置成功");
								aa="success";
								
							}else{
								alert(result);
								aa=false;
							}
							
					}
				});
			return aa;
		}