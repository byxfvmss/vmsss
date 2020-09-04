BillInterface = function() {
	var billInterfaceAjax = new BillInterfaceAjax();
	var ocxObj = null;
	var taxInfoCheckOk = false;
	var diskNo = '';
	var MachineNo='';
	/**
	 *  初始化参数
	 */
	this.init = function(params) {
		ocxObj = document.getElementById('ocxObj');
		billInterfaceAjax.init(params);

		billInterfaceAjax.createTaxInfo(params, function(ajaxReturn) {
			if (ajaxReturn.isNormal) {
				var StringXml = ocxObj.OperateDiskX(ajaxReturn.attributes.StringXml);
				billInterfaceAjax.checkTaxInfo({StringXml: StringXml}, function(ajaxReturn1) {
					if(ajaxReturn1.isNormal){
						params.diskNo = ajaxReturn1.attributes.diskNo;
						diskNo=ajaxReturn1.attributes.diskNo;
						MachineNo=ajaxReturn1.attributes.MachineNo;
						billInterfaceAjax.createRegistInfo(params,function(ajaxReturn2) {
							if(ajaxReturn2.isNormal) {
								var StringXml = ocxObj.OperateDiskX(ajaxReturn2.attributes.StringXml);
								billInterfaceAjax.checkRegistInfo({StringXml: StringXml}, function(ajaxReturn3) {
									if (ajaxReturn3.isNormal) {
										taxInfoCheckOk = true;
									} else {
										alert(ajaxReturn3.message);
									}
								});
							} else {
								alert(ajaxReturn2.message);
							}
						});
					} else {
						alert(ajaxReturn1.message);
					}
				});
			} else {
				alert(ajaxReturn.message);
			}
		});
	};
	
	/**
	 *  创建发票开具报文
	 */
	this.createBillissue = function(params) {
		if (true) {//taxInfoCheckOk
			var ids=new Array();
			ids=params.ids.split(",");
			 
			var flag = true;
			for(var i=0;i<ids.length;i++){
				var billId=ids[i];
				billInterfaceAjax.createBillissue({billId:billId,fapiaoType:params.fapiaoType,diskNo:diskNo,MachineNo:MachineNo},function(ajaxReturn){
					if(ajaxReturn.isNormal){
						//alert(ajaxReturn.attributes.StringXml);
						var StringXml=ocxObj.OperateDiskX(ajaxReturn.attributes.StringXml);
						//alert(StringXml);
						billInterfaceAjax.updateBillIssueResult({
							diskNo:diskNo,MachineNo:MachineNo,billId:billId, StringXml:StringXml
						}, function(ajaxReturn1) {
							if(!ajaxReturn1.isNormal){
								alert(ajaxReturn1.message);
								flag = false;
							}else{
								billInterfaceAjax.invoiceIssueAccessCore({billId:billId},function(ajaxReturn2){
									if(ajaxReturn2.isNormal){   //与核心交互成功
										alert("发票开具与核心交互成功");
									}else{
										alert("发票开具与核心交互失败"+"，回写核心失败："+ajaxReturn2.message);
										flag = false;
									}
								});	
							}
						});
					} else {
						alert(ajaxReturn.message);
						flag = false;
					}
				});
				if (!flag) {
					break;
				}else{
					billInterfaceAjax.invoiceIssueAccessCore({billId:billId},function(ajaxReturn2){
						if(ajaxReturn2.isNormal){   //与核心交互成功
							alert("发票开具与核心交互成功");
						}else{
							alert("发票开具与核心交互失败"+"，回写核心失败："+ajaxReturn2.message);
							flag = false;
						}
					});	
				}
			}
			if(flag){
				alert("开具成功");
			}
		}
	};
	/**
	 *  创建税控盘报文
	 */
	this.createTaxInfo = function(params, callback) {
		billInterfaceAjax.createTaxInfo(params, function(ajaxReturn) {
			if (ajaxReturn.isNormal) {
				var StringXml = ocxObj.OperateDiskX(ajaxReturn.attributes.StringXml);
				alert(StringXml);
			} else {
				alert(ajaxReturn.message);
			}
		});
	};
	/**
	 *  校验税控软件基本信息
	 */
	this.checkTaxInfo = function(params, callback) {
		billInterfaceAjax.checkTaxInfo(params,function(ajaxReturn){
			if (ajaxReturn.isNormal) {
				 StringXml= OperateDiskX(ajaxReturn.attributes.StringXml);
			} else {
				alert(ajaxReturn.message);
			}
		});
	};
	/** 
	* 更新开具结果
	*/
	this.updateBillIssueResult = function(params) {
		billInterfaceAjax.createTaxInfo(params, function(ajaxReturn) {
			if (!ajaxReturn.isNormal) {
				var StringXml= OperateDiskX(ajaxReturn.attributes.StringXml);
			} else {
				alert(ajaxReturn.message);
			}
		});
	};
	this.createBillPrint=function(params){
		if (taxInfoCheckOk) {
			var ids=new Array();
			var ids=params.ids.split(",");
			$.ajaxSetup({async: false});
			var flag=true;
			for(var i=0;i<ids.length;i++){
				var billId=ids[i];
				  billInterfaceAjax.createBillPrint({billId:billId,diskNo:diskNo},function(ajaxReturn){
					if(ajaxReturn.isNormal){
							var StringXml=ocxObj.OperateDiskX(ajaxReturn.attributes.StringXml);
							//alert(StringXml)
							 billInterfaceAjax.updateBillPrintResult({billId:billId, StringXml:StringXml}, function(ajaxReturn1) {
								if(!ajaxReturn1.isNormal){
									alert(ajaxReturn1.message);
									flag=false;
								}
							});
						}else{
							alert(ajaxReturn.message);
							flag=false;
						}
				});
				if (!flag) {
					break;
				}
			}if(flag){
				alert("打印成功!")
			}
			
			
		}
	}
	this.createBillCancel=function(params){
		if (taxInfoCheckOk) {
			billInterfaceAjax.createBillCancel({billId:params.billId,diskNo:diskNo},function(ajaxReturn){
				if(ajaxReturn.isNormal){  //创建报文成功
					//alert(ajaxReturn.attributes.StringXml);
					var StringXml=ocxObj.OperateDiskX(ajaxReturn.attributes.StringXml);
					//alert(StringXml);
					billInterfaceAjax.updateBillCancelResult({StringXml:StringXml,diskNo:diskNo,billId:params.billId},function(ajaxReturn1){
						if(ajaxReturn1.isNormal){  //与税控交互成功
							billInterfaceAjax.cancelBillAccessCore({billId:params.billId},function(ajaxReturn2){
								if(ajaxReturn2.isNormal){   //与核心交互成功
									alert("作废成功");
								}else{
									alert("作废成功"+"，回写核心失败："+ajaxReturn2.message);
								}
							});		
						}else{
							alert(ajaxReturn1.message);
						}
						submitAction(document.forms[0], "listBillCancel.action");
					});
				}else{
					alert(ajaxReturn.message);
				}
				
			});
		}
	}
}
