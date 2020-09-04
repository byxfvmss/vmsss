BillInterfaceAjax = function() {
	var billId = '';
	var doPost = function(url, params, callback) {
		$.ajax({
			type: 'POST', 
			url: url, 
			data: params, 
			dataType: 'json', 
			async: false, 
			success: function(ajaxReturn) {
				callback(ajaxReturn);
			
			},
			error:function(ajaxReturn){
				alert(ajaxReturn.message);	
			}
         });
	};
	/**
	 *  初始化参数
	 */
	this.init = function(params) {
		billId = params.billId;
	};
	/**
	 *  创建发票开具报文
	 */
	this.createBillissue = function(params, callback) {
		doPost('createBillissue.action', params, callback);
	};
	/**
	 *  更新发票开具结果
	 */
	this.updateBillIssueResult = function(params, callback) {
		doPost('updateBillIssueResult.action', params, callback);
	};
	/**
	 * 发票开具与核心交互
	 */
	this.invoiceIssueAccessCore = function(params, callback) {
		doPost('invoiceIssueAccessCore.action', params, callback);
	};
	/**
	 *  创建发票打印报文
	 */
	this.createBillPrint = function(params, callback) {
		doPost('createBillPrint.action', params, callback);
	};
	/**
	 *  更新发票打印结果
	 */
	this.updateBillPrintResult = function(params, callback) {
		doPost('updateBillPrintResult.action', params, callback);
	};
	/**
	 *  创建发票作废报文
	 */
	this.createBillCancel = function(params, callback) {
		doPost('createBillCancel.action', params, callback);
	};
	/**
	 *  更新发票作废结果
	 */
	this.updateBillCancelResult = function(params, callback) {
		doPost('updateBillCancelResult.action', params, callback);
	};
	/**
	 *  发票作废成功与核心交互
	 */
	this.cancelBillAccessCore = function(params, callback) {
		doPost('cancelBillAccessCore.action', params, callback);
	};
	/**
	 *  更新发票打印结果
	 */
	this.updateBillPrintResult = function(params, callback) {
		doPost('updateBillPrintResult.action', params, callback);
	};
	/**
	 *  向税务软件同步税目信息
	 */
	this.saveTaxItemInfo = function(params, callback) {
		doPost('saveTaxItemInfo.action', params, callback);
	};
	/**
	 *  创建查询税控阈值信息报文
	 */
	this.createTaxMonitor = function(params, callback) {
		doPost('createTaxMonitor.action', params, callback);
	};
	/**
	 *  保存税控阈值信息
	 */
	this.saveTaxMonitor = function(params, callback) {
		doPost('saveTaxMonitor.action', params, callback);
	};
	/**
	 *  创建库存统计信息报文
	 */
	this.createStockInfo = function(params, callback) {
		doPost('createStockInfo.action', params, callback);
	};
	/**
	 *  保存库存统计信息
	 */
	this.saveStockInfo = function(params, callback) {
		doPost('saveStockInfo.action', params, callback);
	};
	/**
	 *  创建库存分发报文
	 */
	this.createStockIssue = function(params, callback) {
		doPost('createStockIssue.action', params, callback);
	};
	/**
	 *  保存库存分发
	 */
	this.saveStockIssue = function(params, callback) {
		doPost('saveStockIssue.action', params, callback);
	};
	/**
	 *  创建库存收回报文
	 */
	this.createStockRecover = function(params, callback) {
		doPost('createStockRecover.action', params, callback);
	};
	/**
	 *  保存库存收回
	 */
	this.saveStockRecover = function(params, callback) {
		doPost('saveStockRecover.action', params, callback);
	};
	/**
	 *  创建税控软件基本信息报文
	 */
	this.createTaxInfo = function(params, callback) {
		doPost('createTaxInfo.action', params, callback);
	};
	/**
	 *  校验税控软件基本信息
	 */
	this.checkTaxInfo = function(params, callback) {
		doPost('checkTaxInfo.action', params, callback);
	};
	/**
	 *  保存税控软件基本信息
	 */
	this.saveTaxInfo = function(params, callback) {
		doPost('saveTaxInfo.action', params, callback);
	};
	/**
	 *   创建注册信息
	 */
	this.createRegistInfo = function(params, callback) {
		doPost('createRegistInfo.action', params, callback);
	};
	/**
	 *   创建注册信息
	 */
	this.checkRegistInfo = function(params, callback) {
		doPost('checkRegistInfo.action', params, callback);
	};
	/**
	 *  创建当前发票号码信息
	 */
	this.createCurBillNoInfo = function(params, callback) {
		doPost('createCurBillNoInfo.action', params, callback);
	};
	/**
	 *  校验当前发票号码信息
	 */
	this.checkCurBillNoInfo = function(params, callback) {
		doPost('checkCurBillNoInfo.action', params, callback);
	};
}