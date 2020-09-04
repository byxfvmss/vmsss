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
		billInterfaceAjax.createTaxInfo(params,function(ajaxReturn) {
			if (ajaxReturn.isNormal) {
				//alert(ajaxReturn.attributes.StringXml);
				var StringXml = ocxObj.Operate(ajaxReturn.attributes.StringXml);
				//alert(StringXml);
				billInterfaceAjax.checkTaxInfo({StringXml:StringXml}, function(ajaxReturn1) {
					if (ajaxReturn1.isNormal) {
						diskNo=ajaxReturn1.attributes.diskNo;
						MachineNo=ajaxReturn1.attributes.diskNo;
						billInterfaceAjax.createRegistInfo({diskNo:diskNo}, function(ajaxReturn2) {
							if (ajaxReturn2.isNormal) {
								//alert(ajaxReturn2.attributes.StringXml);
								var StringXml = ocxObj.Operate(ajaxReturn2.attributes.StringXml);
								//alert(StringXml)
									billInterfaceAjax.checkRegistInfo({StringXml:StringXml}, function(ajaxReturn3) {
									if(ajaxReturn3.isNormal){
										//alert(ajaxReturn3.attributes.StringXml);
										var StringXml = ocxObj.Operate(ajaxReturn3.attributes.StringXml);
										//alert(StringXml);
										//taxInfoCheckOk = true;
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
			var ids = new Array();
			ids = params.ids.split(",");

			var flag = true;
			for (var i = 0; i < ids.length; i++) {
				var billId = ids[i];
				billInterfaceAjax
						.createCurBillNoInfo(
								{
									fapiaoType : params.fapiaoType,diskNo:diskNo
								},
								function(ajaxReturn) {
									// alert(ajaxReturn.attributes.StringXml);
									if (ajaxReturn.isNormal) {
										//alert(ajaxReturn.attributes.StringXml);
										var StringXml = ocxObj.Operate(ajaxReturn.attributes.StringXml);
										// alert(StringXml);
										billInterfaceAjax.checkCurBillNoInfo({
											StringXml : StringXml
											},
											function(ajaxReturn1) {
												if (ajaxReturn1.isNormal) {
													billInterfaceAjax.createBillissue({
														billId : billId,
														fapiaoType : params.fapiaoType,
														diskNo : diskNo,
														MachineNo : MachineNo
													},
													function(ajaxReturn2) {
														if (ajaxReturn2.isNormal) {
															//alert(ajaxReturn2.attributes.StringXml);
															var StringXml = ocxObj.Operate(ajaxReturn2.attributes.StringXml);
															// alert(StringXml);
															billInterfaceAjax.updateBillIssueResult({
																diskNo : diskNo,
																MachineNo : MachineNo,
																billId : billId,
																StringXml : StringXml
															},
															function(ajaxReturn3) {
																if (!ajaxReturn3.isNormal) {
																	alert(ajaxReturn3.message);
																	flag = false;
																} 
																else {
																	billInterfaceAjax.invoiceIssueAccessCore({
																		billId : billId
																},
																function(ajaxReturn4) {
																	if (!ajaxReturn4.isNormal) {
																		alert("发票开具与核心交互失败" + "，回写核心失败原因：" + ajaxReturn4.message);
																	}
															});
														}
													});
												} else {
													alert(ajaxReturn2.message);
													flag = false;
												}
											});
										} else {
											alert(ajaxReturn1.message);
											flag = false;
									}
								});
							} else {
								alert(ajaxReturn.message);
								flag = false;
						}
					});
				if (!flag) {
					break;
				}
			}
			if (flag) {
				alert("开具成功");
			}
	};
	
	/** 
	 * 创建打印 报文
	*/

	this.createBillPrint=function(params){
			var ids=new Array();
			var ids=params.ids.split(",");
			$.ajaxSetup({async: false});
			var flag=true;
			for(var i=0;i<ids.length;i++){
				var billId=ids[i];
				  billInterfaceAjax.createBillPrint({billId:billId,diskNo:diskNo},function(ajaxReturn){
					if(ajaxReturn.isNormal){
						//alert(ajaxReturn.attributes.StringXml);
							var StringXml=ocxObj.Operate(ajaxReturn.attributes.StringXml);
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
	this.createBillCancel=function(params){
			billInterfaceAjax.createBillCancel({billId:params.billId,flag:'1',diskNo:diskNo},function(ajaxReturn){
				if(ajaxReturn.isNormal){   //创建报文成功
					//alert(ajaxReturn.attributes.StringXml);
					var StringXml=ocxObj.Operate(ajaxReturn.attributes.StringXml);
					//alert(StringXml);
					billInterfaceAjax.updateBillCancelResult({StringXml:StringXml,billId:params.billId},function(ajaxReturn1){
						if(ajaxReturn1.isNormal){  //与税控交互成功
							alert("作废成功");
//							billInterfaceAjax.cancelBillAccessCore({billId:params.billId},function(ajaxReturn2){
//								if(ajaxReturn2.isNormal){   //与核心交互成功
//									alert("作废成功");
//								}else{
//									alert("作废成功"+"，回写核心失败："+ajaxReturn2.message);
//								}
//							});		
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
