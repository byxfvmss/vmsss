function syncTaxServlet(){
	var TaxKeyString=taxKeyQuery();
	var generalBillString=buyQuery("1");
	var specialBillString=buyQuery("0");
	if(TaxKeyString=="error"){
		return false;
	}//alert(TaxKeyString);
	if(generalBillString=="error"){
		return false;
	}
	
	if(specialBillString=="error"){
		return false;
	}
	//alert(generalBillString);
	//alert(specialBillString);
	
	$.ajax({url: 'savePaperAutoInvoiceSelvet.action',
						type: 'POST',
						async:false,
						data:{TaxKeyString:TaxKeyString,generalBillString:generalBillString,specialBillString:specialBillString},
						dataType: 'text',
					//	timeout: 1000,
						error: function(){
							return false;},
						success: function(result){
								alert(result);
					submitAction(document.forms[0],"listPageTaxInvoice.action");

								
						}
							});
	
} 