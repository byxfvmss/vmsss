
/**
* extend the riginal openItem method
* if itemId is null then open the tree root
* itemId node's id
* huangshiqiang
* 2007/11/13
*/
 dhtmlXTreeObject.prototype.openItemExt=function(itemId){
 	var tempId = itemId||this.rootId;
 	var temp=this._globalIdStorageFind(tempId);
 	if(!temp)return 0;
 	this._openItem(temp.childNodes[0]);
};
/**
* extend the riginal openAllItems method
* if itemId is null then open the tree root
* itemId node's id
* huangshiqiang
* 2007/11/13
*/
dhtmlXTreeObject.prototype.openAllItemsExt=function(itemId)
{
	var tempId = itemId||this.rootId;
 	var temp=this._globalIdStorageFind(tempId);
 	if(!temp)return 0;
 	this._xopenAll(temp);
};
dhtmlXTreeObject.prototype.getAllCheckedLeaf=function(){
		var checked = this.getAllChecked();
		var leaves = this.getAllLeafs();
		var commas = ",";
		var checkedArr = splitStr(checked,commas);
		var leafArr = splitStr(leaves,commas);
		var result = compareStrSame(checkedArr,leafArr,commas);
		return result;
}

dhtmlXTreeObject.prototype.getAllUcCheckedLeaf=function(){
		var unchecked = this.getAllUnchecked();
		var leaves = this.getAllLeafs();
		var commas = ",";
		var uncheckedArr = splitStr(unchecked,commas);
		var leafArr = splitStr(leaves,commas);
		var result = compareStrSame(uncheckedArr,leafArr,commas);
		return result;
}

/**
* tree node
* id node's id
* name node's name
*/
treeNode = function(id,name){
	this.id=id;
	this.name=name;
}
/**
 * select all checked node
 * return the selected nods 
 * huangshiqiang
 * 2007/10/19
 */  
 dhtmlXTreeObject.prototype.getAllCheckedNode=function(){
 	  var arr = [];
      return this._getAllCheckedNode(arr,"","",1);
   };
   
 /**
 * select all checked node
 * return the selected nods 
 * huangshiqiang
 * 2007/10/19
 */ 
dhtmlXTreeObject.prototype._getAllCheckedNode=function(arr,htmlNode,list,mode){
      if (!htmlNode) htmlNode=this.htmlNode;
      var node = new treeNode();
      if (htmlNode.checkstate==mode){
         if (!htmlNode.nocheckbox)  {
         	node.id=htmlNode.id;
         	node.name=htmlNode.span.innerHTML;
         	arr.push(node);      	
         }
      }   
      var j=htmlNode.childsCount;
      for (var i=0; i<j; i++)
      {      	
         this._getAllCheckedNode(arr,htmlNode.childNodes[i],list,mode);        
      }
      return arr;
   }; 
   
   /**
 * select all checked node with userData
 * return the selected nods  with userData
 * userData userData name array
 * huangshiqiang
 * 2008/1/24
 */  
 dhtmlXTreeObject.prototype.getAllCheckedNodeWithUserData=function(userData){
 	  var arr = [];
      return this._getAllCheckedNodeWithUserData(arr,"","",1,userData);
   };
   
 /**
 * select all checked node with userData
 * return the selected nods  with userData
 * userData userData name array
 * huangshiqiang
 * 2008/1/24
 */ 
dhtmlXTreeObject.prototype._getAllCheckedNodeWithUserData=function(arr,htmlNode,list,mode,userData){
      if (!htmlNode) htmlNode=this.htmlNode;
      var node = new treeNode();
      if (htmlNode.checkstate==mode){
         if (!htmlNode.nocheckbox)  {
         	node.id=htmlNode.id;
         	node.name=htmlNode.span.innerHTML;
         	/*if(userData && userData.length>0){
	         	for(i=0;i<userData.length;i++){
	         		var result=this.getUserData(htmlNode.id,userData[i]);
					var scriptStr="node."+userData[i]+"="+result;
					eval(scriptStr);
				}
			}*/
         	arr.push(node);      	
         }
      }   
      var j=htmlNode.childsCount;
      for (var i=0; i<j; i++)
      {      	
         this._getAllCheckedNode(arr,htmlNode.childNodes[i],list,mode);        
      }
      return arr;
   }; 
/**
*  all level single select
*  huangshiqiang
*  2007/12/15
**/ 
dhtmlXTreeObject.prototype.singleSelectExt=function(){
   	this.setOnCheckHandler(this._singleSelectExt);
   };
/**
*  all level single select
*  huangshiqiang
*  2007/12/15
**/
dhtmlXTreeObject.prototype._singleSelectExt=function(id){
	var checked = this.getAllChecked();
	var commas = ",";
	var checkedArr = splitStr(checked,commas);
	if(checkedArr && checkedArr.length>0){
		for(i=0;i<checkedArr.length;i++){
			this.setCheck(checkedArr[i],false);
		}
	}
	this.setCheck(id,true);
};
/**
 * get the tree's first node's id
 * huangshiqiang
 * 2007/12/18
 **/
dhtmlXTreeObject.prototype.getFirstNodeId =function()
{
	var tempId = this.rootId;
 	var temp=this._globalIdStorageFind(tempId);
 	if(temp)return temp.childNodes[0].id;
 	else return "";
};
/**
 * asynchronize load tree
 * default load the first node's children
 * different from the original loadXML just in
 * dtmlXMLLoaderObject(this._parseXMLTree,this,true,this.no_cashe)
 * the third param ture to false
 * huangshiqiang
 * 2007/12/18
 **/
dhtmlXTreeObject.prototype.loadXMLExt=function(file,afterCall){
 var that=this;
 if((this.onXLS)&&(!this.parsCount))that.onXLS(that,this._ld_id);
 this._ld_id=null;
 this.xmlstate=1;
 this.XMLLoader=new dtmlXMLLoaderObject(this._parseXMLTree,this,false,this.no_cashe);
 if(afterCall)this.XMLLoader.waitCall=afterCall;
 this.XMLLoader.loadXML(file);
 };
 
 
dhtmlXTreeObject.prototype.checkAll=function(mode){
	this._checkAll("",mode);
}

dhtmlXTreeObject.prototype._checkAll=function(htmlNode,mode){
      if (!htmlNode) htmlNode=this.htmlNode;
      if (!htmlNode.nocheckbox)  {
         this.setSubChecked(htmlNode.id,mode);         	       	
      }
      var j=htmlNode.childsCount;
      for (var i=0; i<j; i++)
      {      	
         list=this._checkAll(htmlNode.childNodes[i],mode);        
      }
}; 
function compareStrDistinct(mainArr,valueArr,sChar)
	{		
		var result="";
		for (i = 0; i < valueArr.length; i++)
		{
			var isAdd = true;
			for(j = 0; j < mainArr.length; j++)
			{
				if (mainArr[j] == valueArr[i])
				{		
					isAdd = false;
					break;
				}
			}
			if (isAdd)
			{
				result = result + valueArr[i] + sChar;
			}
		}
		return result;
	}
function compareStrSame(mainArr,valueArr,sChar)
	{		
		var result="";
		for (i = 0; i < valueArr.length; i++)
		{
			for(j = 0; j < mainArr.length; j++)
			{
				if (mainArr[j] == valueArr[i])
				{		
					result = result + valueArr[i] + sChar;
					break;
				}
			}
		}
		return result;
	}

function splitStr(str,sChar)
	{
		toId=str.split(sChar);
		return toId;	
	}


