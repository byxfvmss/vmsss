  /*
  * ��    �ܣ���ɼ���
  * ��    �ߣ�ʷ���� IRD
  * ��    �ڣ�2008-10-21
  * ���л�����IE 6.0+
  */
  function SimpleTree(){
      this.container    = null;
      this.iconPath     = null;
      this.dataStr      = null;
      this.maxDepth     = 0;
      this.selectedNode = null;
      this.checkBox     = false;
      this.radioBox     = false;
      this.updateBox    = false;
      this.checkLevel   = 10; 
  }

  SimpleTree.prototype.$ = function(id){
      return document.getElementById(id);
  }
  SimpleTree.prototype.$C = function(tagName,attrObject){
      var elm = window.document.createElement(tagName);
      if(null != attrObject && typeof(attrObject) == "object"){
          for(var item in attrObject){
              if(item == "innerHTML" || item == "className")
                  elm[item] = attrObject[item];
              else
                  elm.setAttribute(item,attrObject[item]);
          }
      }
      return elm;
  }
  SimpleTree.prototype.$A = function(elm,pElm){
      return pElm ? pElm.appendChild(elm) : document.body.appendChild(elm);
  }
  SimpleTree.prototype.$R = function(elm,pElm){
      return pElm ? pElm.removeChild(elm) : document.body.removeChild(elm);
  }
  SimpleTree.prototype.$T = function(tagName,pElm){
      return pElm ? pElm.getElementsByTagName(tagName) : document.getElementsByTagName(tagName);
  }

  SimpleTree.prototype.init = function(){
      if(null == this.container || null == this.dataStr){return;}
      this.data = this.ConvertToXML();
      this.Load();
      
      //_alert(this.data);
      //alert((new XMLSerializer()).serializeToString(this.data));
  }

  SimpleTree.prototype.ConvertToXML = function(){
      //var xmlDoc = new XmlReader(this.dataStr);
      //return new XmlNode(xmlDoc.documentElement);
      return this.getXmlData(this.dataStr);
  }
  SimpleTree.prototype.getXmlData = function(dataStr){
      var xmldom = null;
      var parser = null;
      if(window.ActiveXObject){
          var service = ["MSXML2.DOMDOCUMENT", "Microsoft.DOMDOCUMENT", "MSXML.DOMDOCUMENT", "MSXML3.DOMDOCUMENT"];
          for(var i = 0 , iLen = service.length ; i < iLen ; i ++){
              try{
                  xmldom = new ActiveXObject(service[i]);
              }catch(ex){
              }
          }
          if(null == xmldom){
              alert("�������δ��װ�κ�XML������");
          }
      }else if(window.DOMParser){
          parser = new DOMParser();
      }

      
      if(window.ActiveXObject){
          xmldom.async = true;
          xmldom.loadXML(dataStr);
      }else if(window.DOMParser){
          xmldom = parser.parseFromString(dataStr, "text/xml");
          this.addProperty();
      }
      return xmldom.documentElement;
  }

  SimpleTree.prototype.addProperty = function(){
      if( document.implementation.hasFeature("XPath", "3.0") ){
          XMLDocument.prototype.selectNodes = function(cXPathString, xNode){
              if( !xNode ){ 
                  xNode = this; 
              } 
              var oNSResolver = this.createNSResolver(this.documentElement);
              var aItems = this.evaluate(cXPathString, xNode, oNSResolver, 
              XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);
              var aResult = [];
              for( var i = 0; i < aItems.snapshotLength; i++){
                  aResult[i] = aItems.snapshotItem(i);
              }
              return aResult;
          }

          Element.prototype.selectNodes = function(cXPathString){
              if(this.ownerDocument.selectNodes){
                  return this.ownerDocument.selectNodes(cXPathString, this);
              }else{
                  throw "For XML Elements Only";
              }
          }
      }

      if( document.implementation.hasFeature("XPath", "3.0") ){
          XMLDocument.prototype.selectSingleNode = function(cXPathString, xNode){
              if( !xNode ){
                  xNode = this; 
              } 
              var xItems = this.selectNodes(cXPathString, xNode);
              if( xItems.length > 0 ){
                  return xItems[0];
              }else{
                  return null;
              }
          }

          // prototying the Element
          Element.prototype.selectSingleNode = function(cXPathString){ 
              if(this.ownerDocument.selectSingleNode){
                  return this.ownerDocument.selectSingleNode(cXPathString, this);
              }else{
                  throw "For XML Elements Only";
              }
          }
      }	
  }

  SimpleTree.prototype.Load = function(){
      this.containerID = this.container.id;
      this.container.className = "simpleTree";
      this.SetDepth(this.data.selectSingleNode("./Data/Tree"),0);
      this.SetLineOrder(this.data,0);
      this.container.innerHTML = "";
      this.BuildTree();      
      this.CheckLeafStatus();
  }  
  
  SimpleTree.prototype.SetDepth = function(curNode,depth){
      this.maxDepth = this.maxDepth > depth ? this.maxDepth : depth;
      depth = depth + 1;
      var nodes = curNode.selectNodes("./TreeNode");
      var nLen = nodes.length;
      for(var i = 0 ; i < nLen ; i++){
          var node = nodes[i];
          node.setAttribute("_depth",depth);
          this.SetDepth(node,depth);
      }
  }

  SimpleTree.prototype.SetLineOrder = function(curNode){
      var nodes = curNode.selectNodes(".//TreeNode");
      for(var i=0,iLen=nodes.length;i<iLen;i++){
          var node = nodes[i];
          var pNode = node.parentNode;
          while(pNode != null){
              var nextNode = this.GetNextSibling(pNode);
              if(null != nextNode){
                  var depth = pNode.getAttribute("_depth");
                  depth = depth - 1;
                  if(depth != 0){
                      var lineOrder = node.getAttribute("_lineOrder");
                      if(lineOrder == null){
                          node.setAttribute("_lineOrder",depth);
                      }else{
                          node.setAttribute("_lineOrder",lineOrder + "," + depth);
                      }
                  }
              }
              pNode = pNode.parentNode;
          }
      }
  }

  SimpleTree.prototype.BuildTree = function(node){
      if(null == node)
          node = this.data.selectSingleNode("./Data/Tree");
      var iLen = node.selectNodes("./TreeNode").length;
      for(var i=0;i<iLen;i++){
          var curNode = node.selectNodes("./TreeNode")[i];
          var pNode = curNode.parentNode;
          this.GiveTreeNodeContent(curNode,pNode);
          this.BuildTree(curNode);
      }
  }

  SimpleTree.prototype.CheckLeafStatus = function(){
      var treeNodes = this.data.selectNodes(".//TreeNode");
      for(var i=0,iLen=treeNodes.length;i<iLen;i++){
          var curNode = treeNodes[i];
          var opened = curNode.getAttribute("_opened");
          if(opened == "false"){
              var id = curNode.getAttribute("id");
              var expandDiv = this.$(this.container.id+"_tree_Expand_"+id);
              if(expandDiv){
                  curNode.setAttribute("_opened","true");
                  this.resetLeaf(expandDiv);
              }
          }
      }
  }

  SimpleTree.prototype.GiveTreeNodeContent = function(node,pNode){
      var oThis = this;

      var name = node.getAttribute("name");
      var id = node.getAttribute("id");
      var depth = node.getAttribute("_depth");
      var canSelect = node.getAttribute("_canSelect");
      var selected = node.getAttribute("_selected");
      canSelect = canSelect == null ? "1" : canSelect;
      selected = selected == null ? "0" : selected;
      var nCLen = node.selectNodes(".//TreeNode").length;

      var pId = pNode == null ? null : pNode.getAttribute("id");

      var childsBox = this.$(this.containerID + "_childs_box_" + pId);
      if(null == childsBox){
          childsBox = this.$C("div",{
              id:this.containerID + "_childs_box_" + pId
          });
          var pBox = pId == null ? this.container : this.$(this.containerID + "_treeNode_Box_" + pId);
          this.$A(childsBox,pBox);
      }

      var boxDiv = this.$C("div",{id:this.containerID+"_treeNode_Box_" + id,className:"boxDiv"});
      this.$A(boxDiv,childsBox);

      var table = this.$C("table",{cellSpacing:"0",cellPadding:"0"});
      var tbody = this.$C("tbody");
      var tr = this.$C("tr");
      this.$A(table,boxDiv);
      this.$A(tbody,table);
      this.$A(tr,tbody);

      if(depth == "1"){
          var imgHtml = nCLen != 0 ? "<img _isRoot=\"1\" nodeId=\""+id+"\" src=\"" + this.iconPath + "root_contract.gif\"/>" : "<img src=\"" + this.iconPath + "root_leaf.gif\"/>";
          var imgDiv = this.$C("div",{className:"treeNodeIconRootDiv",innerHTML:imgHtml,id:this.container.id+"_tree_Expand_"+id});
          if(nCLen !=0){
              imgDiv.style.backgroundImage = "url(" + this.iconPath + "bg_vline_half.gif" + ")";
              imgDiv.style.backgroundPosition = "14px 10px";
              imgDiv.style.backgroundRepeat = "no-repeat";

              imgDiv.onclick = function(){
                  oThis.resetLeaf(this);
              }
          }
          var td = this.$C("td");
          this.$A(td,tr);
          this.$A(imgDiv,td);

          // 2009-4-28 ���CheckBox
          if(this.checkBox || this.radioBox){
              this.AddCheckBox(tr,canSelect,selected,id);
          }

          var div = this.$C("div",{
              innerHTML:"<a href=\"javascript:void(0);\">" + name + "</a>",
              className:"treeNodeDiv",
              id:this.containerID + "_treeNode_Leaf_" + id,
              treeID:id
          });

          var td2 = this.$C("td");
          this.$A(td2,tr);
          this.$A(div,td2);

          div.onclick = function(){
              oThis.clickNode(this.getAttribute("treeID"));
          }
      }else{
          if(depth == "2") boxDiv.style.paddingLeft = "3px";
          for(var i=0;i<(depth-1);i++){
              var spaceDiv = this.$C("div",{className:"treeNodeSpaceDiv"});
              
              var lineOrder = node.getAttribute("_lineOrder");
              if(lineOrder != null){
                  if(lineOrder.indexOf(",") != -1){
                      for(var j=0,jLen=lineOrder.split(",").length;j<jLen;j++){
                          if(i == lineOrder.split(",")[j]){
                              spaceDiv.style.backgroundImage = "url(" + this.iconPath + "bg_vline.gif" + ")";
                              spaceDiv.style.backgroundPosition = "-5px 0px";
                              spaceDiv.style.backgroundRepeat = "repeat-y";
                          }
                      }
                  }else{
                      if(i == lineOrder){
                          spaceDiv.style.backgroundImage = "url(" + this.iconPath + "bg_vline.gif" + ")";
                          spaceDiv.style.backgroundPosition = "-5px 0px";
                          spaceDiv.style.backgroundRepeat = "repeat-y";
                      }
                  }
              }
              var td = this.$C("td");
              this.$A(td,tr);
              this.$A(spaceDiv,td);
          }

          var lineDiv = this.$C("div",{className:"treeNodeLineDiv"});

          if(this.GetNextSibling(node) != null){
              lineDiv.style.backgroundImage = "url(" + this.iconPath + "bg_vline.gif" + ")";
              lineDiv.style.backgroundPosition = "-5px 0px";
              lineDiv.style.backgroundRepeat = "repeat-y";
          }else{
              lineDiv.style.backgroundImage = "url(" + this.iconPath + "bg_vline_half.gif" + ")";
              lineDiv.style.backgroundPosition = "-5px 0px";
              lineDiv.style.backgroundRepeat = "no-repeat";
          }
          var lineTD = this.$C("td");
          this.$A(lineTD,tr);
          this.$A(lineDiv,lineTD);

          var imgHtml = (nCLen != 0  || node.getAttribute("_hasChild") == "1" ) ? 
            "<img _isRoot=\"0\" nodeId=\""+id+"\" src=\"" + this.iconPath + "contract.gif\"/>" 
            : "<img src=\"" + this.iconPath + "leaf.gif\"/>";
          var imgDiv = this.$C("div",{
              className:"treeNodeIconDiv",
              innerHTML:imgHtml,
              id:this.container.id+"_tree_Expand_"+id
          });

          if(nCLen != 0 || node.getAttribute("_hasChild") == "1"){
              imgDiv.style.backgroundImage = "url(" + this.iconPath + "bg_vline_half.gif" + ")";
              imgDiv.style.backgroundPosition = "10px 10px";
              imgDiv.style.backgroundRepeat = "no-repeat";

              imgDiv.onclick = function(){
                  oThis.resetLeaf(this);
              }
          }
          var imgTD = this.$C("td");
          this.$A(imgTD,tr);
          this.$A(imgDiv,imgTD);

          // 2009-4-28 ���CheckBox
          if(this.checkBox || this.radioBox){
              this.AddCheckBox(tr,canSelect,selected,id);
          }

          var div = this.$C("div",{
              innerHTML:"<a href=\"javascript:void(0);\">" + name + "</a>",
              className:"treeNodeDiv",
              id:this.containerID+"_treeNode_Leaf_" + id,
              treeID:id
          });

          var divTD = this.$C("td");
          this.$A(divTD,tr);
          this.$A(div,divTD);

          div.onclick = function(){
              oThis.clickNode(this.getAttribute("treeID"));
          }
      }
  }
  SimpleTree.prototype.GetNextSibling = function(node){
      var nNode = node.nextSibling;
      while(nNode != null && nNode.nodeType != "1"){
          nNode = nNode.nextSibling;
      }
      return nNode;
  }

  SimpleTree.prototype.AddCheckBox = function(tr,canSelect,selected,id){
      var oThis = this;
      var checkType = canSelect == "0" ? "-1" : selected;
      var cbTD = this.$C("td",{className:"treeCheckBoxTD"});
      var cbDiv = this.$C("div",{
          className:this.radioBox ? "treeRadioBox" : "treeCheckBox",
          _type:"checkBox",
          _checkType:checkType,
          innerHTML:"&nbsp;",
          id:this.containerID+"_tree_checkbox_"+id,
          dataID:id
      });

      this.$A(cbTD,tr);
      this.$A(cbDiv,cbTD);
      var yPos;
      if(checkType == "-1"){
          yPos = "-50";
      }else if(checkType == "0"){
          yPos = "0";
      }else{
          yPos = "-20";
      }
      cbDiv.style.backgroundPosition = "0px " + yPos + "px";
      cbDiv.style.cursor = "hand";

      cbDiv.onclick = function(){
          oThis.BoxClick(this);
      }
  }

  SimpleTree.prototype.BoxClick = function(elm,fromNode){
      var cType = elm.getAttribute("_checkType");
      if(cType == "-1")
          return;
      if(this.radioBox && cType == "1")
          return;

      var oThis = this;
      var selectedID = elm.getAttribute("dataID");
      var treeNode = oThis.data.selectSingleNode(".//TreeNode[@id=\""+selectedID+"\"]");
      if(oThis.radioBox){
          if(oThis.radioObj == null){
              oThis.radioObj = elm;
          }else{
              oThis.radioObj.setAttribute("_checkType","0");
              oThis.radioObj.style.backgroundPosition = "0px 0px";
              oThis.radioObj = elm;
          }
      }
      if(cType == "0"){
          elm.setAttribute("_checkType","1");
          elm.style.backgroundPosition = "0px -20px";
          treeNode.setAttribute("_newSelected","1");
      }else{
          elm.setAttribute("_checkType","0");
          elm.style.backgroundPosition = "0px 0px";
          treeNode.setAttribute("_newSelected","0");
      }

      if(oThis.checkBox)
          this.CheckRelateBoxStatus(selectedID,cType);
      
      oThis.BoxOnClick();
      if(oThis.updateBox && !fromNode){
          oThis.clickNode(selectedID,true);
      }
  }

  SimpleTree.prototype.resetLeaf = function(imgDiv){
      var imgObj = imgDiv.firstChild;
      var nodeID = imgObj.getAttribute("nodeId");
      var node = this.data.selectSingleNode(".//TreeNode[@id='"+nodeID+"']");

      var _opened = node.getAttribute("_opened");
      var _isRoot = imgObj.getAttribute("_isRoot");

      if(_opened != "false"){
          if(_isRoot == "1"){
              imgObj.src = this.iconPath + "root_expand.gif";
          }else{
              imgObj.src = this.iconPath + "expand.gif";
          }
          imgDiv.style.backgroundImage = "url()";

          imgObj.setAttribute("_opened","false");
          node.setAttribute("_opened","false");
          
          var box = this.$(this.containerID + "_childs_box_" + nodeID);
          if(null != box)
              box.style.display = "none";
      }else{     
          if(_isRoot == "1"){
              imgObj.src = this.iconPath + "root_contract.gif";
          }else{
              imgObj.src = this.iconPath + "contract.gif";
          }
          imgDiv.style.backgroundImage = "url(" + this.iconPath + "bg_vline_half.gif" + ")";
          imgDiv.style.backgroundPosition = _isRoot == "1" ? "14px 10px" : "10px 10px";
          imgDiv.style.backgroundRepeat = "no-repeat";

          imgObj.setAttribute("_opened","true");
          node.setAttribute("_opened","true");
          
          var box = this.$(this.containerID + "_childs_box_" + nodeID);
          if(null != box)
              box.style.display = "";
          var hasChild = node.getAttribute("_hasChild");
          if("1" == hasChild && node.selectNodes(".//TreeNode").length == 0){
              this.GetAnalisyNodes(nodeID);
          }
      }
  }

  SimpleTree.prototype.clickNode = function(id,isFromBox){
      var treeNode = this.data.selectSingleNode(".//TreeNode[@id='" + id + "']");

      if(this.selectedNode != null){
          var oldID = this.selectedNode.getAttribute("id");
          var oldDiv = this.$(this.containerID+"_treeNode_Leaf_" + oldID);
          oldDiv.className = "treeNodeDiv";          
      }
      var div = this.$(this.containerID + "_treeNode_Leaf_" + id);
      div.className = "treeNodeDiv_Select";

      this.selectedNode = treeNode;

      if(this.updateBox && !isFromBox){
          var box = this.$(this.containerID+"_tree_checkbox_"+id);
          if(null != box)
              this.BoxClick(box,true);//box.onclick(true);
      }

      this.OnClick();
  }

  SimpleTree.prototype.OnClick = function(){
  }

  SimpleTree.prototype.BoxOnClick = function(){
  }

  SimpleTree.prototype.GetTreeXML = function(){
      return this.data;
  }
  // �첽�����ӽڵ�
  SimpleTree.prototype.GetAnalisyNodes = function(id){
  }
  SimpleTree.prototype.BuildAnalisyNodes = function(id,dataStr){
      var data = this.getXmlData(dataStr);
      var node = this.data.selectSingleNode(".//TreeNode[@id='" + id + "']");
      if(null != data && null != node){
          var treeNodes = data.selectNodes("./TreeNode");
          for(var i=0,iLen=treeNodes.length;i<iLen;i++)
              this.AppendChildTreeNodes(treeNodes[i],node);
      }
      this.SetDepth(this.data.selectSingleNode("./Data/Tree"),0);
      this.SetLineOrder(this.data,0);
      this.BuildSubTree(node);
      this.CheckLeafStatus();
  }
  SimpleTree.prototype.AppendChildTreeNodes = function(node,pNode){
      pNode.appendChild(node);
      var nodes = node.selectNodes("./TreeNode");
      for(var i=0,iLen=nodes.length;i<iLen;i++)
          this.AppendChildTreeNodes(nodes[i],node);
  }

  SimpleTree.prototype.BuildSubTree = function(node){
      var iLen = node.selectNodes("./TreeNode").length;
      for(var i=0;i<iLen;i++){
          var curNode = node.selectNodes("./TreeNode")[i];
          var pNode = curNode.parentNode;
          this.GiveTreeNodeContent(curNode,pNode);
          this.BuildSubTree(curNode);
      }
  }

  SimpleTree.prototype.GetSelectedTreeNodes = function(isHalf){
      if(!(this.checkBox || this.radioBox)){
          return null;
      }

      if(this.radioBox){
          var obj = this.radioObj;
          if(null == obj){
              return null;
          }
          var tds = obj.parentNode.parentNode.getElementsByTagName("td");
          var div = tds[tds.length-1].getElementsByTagName("div")[0];
          var id = div.getAttribute("treeID");
          var treeNode = this.data.selectSingleNode(".//TreeNode[@id='"+id+"']");
          return treeNode;
      }

      if(this.checkBox){
          var rValue = [];
          var divs = this.container.getElementsByTagName("div");
          for(var i=0;i<divs.length;i++){
              var curDiv = divs[i];
              if(curDiv.getAttribute("_type") == "checkBox"){
                  var selected = curDiv.getAttribute("_checkType");
                  if(isHalf){
                      if(selected != "0" && selected != "-1"){
                          var tds = curDiv.parentNode.parentNode.getElementsByTagName("td");
                          var div = tds[tds.length-1].getElementsByTagName("div")[0];
                          var id = div.getAttribute("treeID");
                          var treeNode = this.data.selectSingleNode(".//TreeNode[@id='"+id+"']");
                          rValue.push(treeNode);
                      }
                  }else{
                      if(selected != "0" && selected != "-1" && selected != "2"){
                          var tds = curDiv.parentNode.parentNode.getElementsByTagName("td");
                          var div = tds[tds.length-1].getElementsByTagName("div")[0];
                          var id = div.getAttribute("treeID");
                          var treeNode = this.data.selectSingleNode(".//TreeNode[@id='"+id+"']");
                          rValue.push(treeNode);
                      }
                  }
              }
          }
          return rValue;
      }
  }

  SimpleTree.prototype.HighlightNode = function(id){
      var treeNode = this.data.selectSingleNode(".//TreeNode[@id='" + id + "']");
      if(null != treeNode){
            var pageNode = $(this.containerID + "_treeNode_Leaf_" + id);
            pageNode.className = "treeNodeDiv_Select";
            if(null != this.selectedNode){
                var oldPageNode = $(this.containerID + "_treeNode_Leaf_" + this.selectedNode.getAttribute("id"));
                oldPageNode.className = "treeNodeDiv";
            }
            this.selectedNode = treeNode;
      }
  }

  SimpleTree.prototype.CheckRelateBoxStatus = function(id,cType){
      var treeNode = this.data.selectSingleNode(".//TreeNode[@id='" + id + "']");
      if(this.checkLevel == 0)
      return ;
      if(null != treeNode){
          // ���´���
          var cNodes = treeNode.selectNodes(".//TreeNode");
          for(var i=0,iLen=cNodes.length;i<iLen;i++){
              var curNode = cNodes[i];
              var cID = curNode.getAttribute("id");
              var canSelect = curNode.getAttribute("_canSelect");
              var elm = this.$(this.containerID+"_tree_checkbox_"+cID);
              if(cType == "0" && canSelect != "0"){
                  elm.setAttribute("_checkType","1");
                  elm.style.backgroundPosition = "0px -20px";
                  curNode.setAttribute("_newSelected","1");
              }else if(canSelect != "0"){
                  elm.setAttribute("_checkType","0");
                  elm.style.backgroundPosition = "0px 0px";
                  curNode.setAttribute("_newSelected","0");
              }
          }
          
          // ���ϴ���
          var pNode = treeNode.parentNode;
          while(null != pNode && pNode.nodeName == "TreeNode"){
              var nNodes = pNode.selectNodes("./TreeNode");
              var sArr = [],nArr = [];
              for(var i=0,iLen=nNodes.length;i<iLen;i++){
                  var selected = nNodes[i].getAttribute("_newSelected");
                  if(selected == "1")
                      sArr.push(0);
                  else if(selected != "2")
                      nArr.push(0);
              }
              var elm = this.$(this.containerID+"_tree_checkbox_"+pNode.getAttribute("id"));           
              var canSelect = pNode.getAttribute("_canSelect");
              if(sArr.length == nNodes.length && canSelect != "0"){
                  elm.setAttribute("_checkType","1");
                  elm.style.backgroundPosition = "0px -20px";
                  pNode.setAttribute("_newSelected","1");
              }else if(nArr.length == nNodes.length && canSelect != "0"){
                  elm.setAttribute("_checkType","0");
                  elm.style.backgroundPosition = "0px 0px";
                  pNode.setAttribute("_newSelected","0");
              }else if(canSelect != "0"){
                  elm.setAttribute("_checkType","2");
                  elm.style.backgroundPosition = "0px -10px";
                  pNode.setAttribute("_newSelected","2");
              }
              pNode = pNode.parentNode;
          }
      }
  }

  SimpleTree.prototype.SelectNodesByID = function(arr){
      var nodes = this.data.selectNodes(".//TreeNode");
      for(var i=0,iLen=nodes.length;i<iLen;i++){
          var curNode = nodes[i];
          var id = curNode.getAttribute("id");
          var canSelect = curNode.getAttribute("_canSelect");

          var elm = this.$(this.containerID+"_tree_checkbox_"+id);
          if(null != elm){
              if(arr.indexOf(id) != -1 && canSelect != "0"){
                  elm.setAttribute("_checkType","1");
                  elm.style.backgroundPosition = "0px -20px";
                  curNode.setAttribute("_newSelected","1");
              }else if(canSelect != "0"){
                  elm.setAttribute("_checkType","0");
                  elm.style.backgroundPosition = "0px 0px";
                  curNode.setAttribute("_newSelected","0");
              }
          }
      }
  }
  SimpleTree.prototype.ShowWaitingLayer = function(){
      if(null == this.layer){
          this.layer = this.$C("div",{innerHTML:"<div style='margin:0 auto;font:normal 10px Verdana;'>Loading...</div>"});
          this.$A(this.layer,this.container);
      }
      this.layer.style.display = "";
      this.layer.style.width = this.container.offsetWidth + "px";
      this.layer.style.height = this.container.offsetHeight + "px";
      this.layer.style.position = "absolute";
      this.layer.style.left = "0px";
      this.layer.style.top = "0px";
      this.layer.style.zIndex = 9999;
  }
  SimpleTree.prototype.HideWaitingLayer = function(){
      if(null != this.layer)
          this.layer.style.display = "none";
  }
  
  if(![].indexOf){
      Array.prototype.indexOf = function(key){
          var index = -1;
          for(var i=0,iLen=this.length;i<iLen;i++){
              if(this[i] == key){
                  index = i;
                  break;
              }
          }
          return index;
      }
  }