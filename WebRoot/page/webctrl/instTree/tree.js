
var bankTree_public={
	tree:{
		divId:null,
		params:null,
		chkboxType:{
				'Y' : '', 
				'N' : ''
		},
		ztree:null,
		setting : null,
		setSelectedBank : function(insts){
			if(insts || insts!=""){
				var node=null;
				var inst=null;
				if(insts instanceof Array){
					for(var i=0;i<insts.length;i++){
						inst =  insts[i];
						node=this.ztree.getNodeByParam("id", inst, null);
						if(node){
							this.ztree.checkNode(node, true);
						}
					}
					return;
				}
				node=this.ztree.getNodeByParam("id", insts, null);
				if(node){
					this.ztree.checkNode(node, true);
				}
			}
		},
		reload : function(param){
			if(param.isAll=="all"){
				var zNodes=$.ajax({  url:this.setting.async.url,data:param,contentType:'application/x-www-form-urlencoded; charset=UTF-8',cache:false,async: false  }).responseText;
				zNodes=JSON.decode(zNodes);
				this.ztree =$.fn.zTree.init($('#' + this.setting.treeType), this.setting,zNodes);
			}else{
				if(this.ztree==null){
					alert("机构树未创建，无法刷新！");
					return ;
				}
				this.ztree.setting.async.otherParam =JSON.encode(param);
				this.ztree.reAsyncChildNodes(null, 'refresh', true);
			}
			this.params=param;
		},
		setChkboxType : function(type){
			//{'Y' : 'ps', 'N' : 'ps'}  级联选择所有上下级
			if(this.ztree==null){
				return null;
			}
			this.ztree.setting.check.chkboxType = type;
			this.setting.check.chkboxType = type;
		},
		clearTree : function(divId){
			this.ztree = $.fn.zTree.init($('#' + this.divId), null,null);
			this.ztree=null;
		},
		getSelectedBankIdAndName:function (){
			if(this.ztree==null){
				return null;
			}
			var nodes=this.ztree.getCheckedNodes(true);
			if(nodes.length>0){
				var bankInfo=new Object();
				bankInfo.id="";
				bankInfo.name="";
				if(nodes.length==1){
					bankInfo.id=nodes[0].id;
					bankInfo.name=nodes[0].name;
					return bankInfo;
				}
				for(var i=0;i<nodes.length;i++){
					bankInfo.name+=nodes[i].name+";";
					bankInfo.id+=nodes[i].id+";";
				}
				return bankInfo;
			}
			return null;
		},
		getNodes :function (){
			return this.ztree.getNodes();
		},
		getNodesByParamFuzzy : function (key,value,parentNode){
			return this.ztree.getNodesByParamFuzzy(key,value,parentNode);
		},
		transformToArray : function (){
			return this.ztree.transformToArray(this.getNodes());
		}
	},
	radioSetting : {
		view: {
			selectedMulti: false
		},
		check : { 
					enable : true ,
					chkStyle: "radio",
					radioType: "all" //level同级唯一

		},
		async : { 
			enable : true,
			autoParam : ["id"]
	 	},
	 	data: {
			simpleData: {
				enable: true
			}
		},
		callback: {
	 		onAsyncSuccess: onAsyncSuccess,
	 		onExpand: zTreeOnExpand

		}
	},
	checkSetting : {
		view: {
			selectedMulti: true
		},
		check : { 
			enable : true , 
			chkboxType : {'Y' : '', 'N' : ''} 
		},
		async : { 
				enable : true,
				autoParam : ["id"]
		},
		data: {
			simpleData: {
				enable: true
			}
		},
		callback: {
			beforeExpand:true,
			onAsyncSuccess: onAsyncSuccess,
			onExpand: zTreeOnExpand
		}
	},
	loadTree : function(url,divId,params,isNoCheck,zNodes){
		if(params.treeType=='check'){
			this.tree.setting=this.checkSetting;
		}else if(params.treeType=='radio'){
			this.tree.setting=this.radioSetting
		}else{
			alert("未知treeType"+treeType);
		}
		this.tree.setting.async.url = url;
		this.tree.setting.treeType = divId;
		if(isNoCheck!=null){
			this.tree.setting.check.enable=isNoCheck;
		}
		if(params.isAll=='all'&&url!=null&&url!=""){
			this.tree.setting.async.enable=false;
			var addNodes=$.ajax({  url: url,data:params,cache:false,async: false ,contentType:'application/x-www-form-urlencoded; charset=UTF-8'}).responseText;
			addNodes=JSON.decode(addNodes);
			if(zNodes==null || zNodes==""){
				zNodes=addNodes;
			}else if(zNodes instanceof Array&&addNodes instanceof Array){
				zNodes.push(addNodes);
			}else if(zNodes instanceof Array){
				zNodes.push(addNodes);
			}else if(addNodes instanceof Array){
				addNodes.push(zNodes);
				zNodes=addNodes;
			}
		}else{
			this.tree.setting.async.otherParam =JSON.encode(params);
		}
		this.tree.params=params;
		this.tree.divId=divId;
		this.tree.ztree = $.fn.zTree.init($('#' + divId), this.tree.setting,zNodes);
		return this.tree;
	}
}
	
	/***
	 * 异步加载
	 * @param event
	 * @param treeId
	 * @param treeNode
	 * @param msg
	 * @return
	 */
	function onAsyncSuccess(event, treeId, treeNode, msg) {
		if (!msg || msg.length == 0) {
			return;
		}
	}
	
	/***
	 * 修改展开后无子节点的节点图标
	 * @param event
	 * @param treeId
	 * @param treeNode
	 * @return
	 */
	function zTreeOnExpand(event, treeId, treeNode) {
		if(treeNode){
			if(!treeNode.childs){
				treeNode.iconSkin = null;
				treeNode.isParent = false;
				var zTree = $.fn.zTree.getZTreeObj(treeId);
				zTree.updateNode(treeNode);
			}
		}
	}
