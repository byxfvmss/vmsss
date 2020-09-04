/*
  * 功  能 : DOM操作封装
  * 作  者 : ShiCH 
  * 日  期 : 2009-8-21
  */
  DOM = {
      $ : function(id) {
          return document.getElementById(id);
      },
      $C : function(tagName, attrObject) {
          var elm = window.document.createElement(tagName);
          var spArr = ["innerHTML", "className", "cellSpacing", "cellPadding","id", "colSpan", "rowSpan"];
          if (null != attrObject && typeof(attrObject) == "object") {
              for (var item in attrObject) {
                  if (spArr.indexOf(item) != -1)
                      elm[item] = attrObject[item];
                  else
                      elm.setAttribute(item, attrObject[item]);
              }
          }
          return elm;
      },
      $A : function(elm, pElm) {
          return pElm ? pElm.appendChild(elm) : document.body.appendChild(elm);
      },
      $I : function(elm, oElm, pElm) {
          return pElm ? pElm.insertBefore(elm, oElm) : document.body.insertBefore(elm, oElm);
      },
      $R : function(elm, pElm) {
    	  /* 如果elm不是body的直接子元素的话, 就会异常 
    	  	modified by yuanshihong at 20091112 
          	return pElm ? pElm.removeChild(elm) : document.body.removeChild(elm);
          */

    	  if(elm.parentElement){
    	  elm.parentElement.removeChild(elm);
    	  }else{
    	  document.body.removeChild(elm);
    	  }
      },
      $T : function(tagName, pElm) {
          return pElm ? pElm.getElementsByTagName(tagName) : document.getElementsByTagName(tagName);
      },
      $M : function(att,pElm) {
          if (att && typeof(att) == "string") {
              var reg = /(^\s*)|(\s*$)/g;
              var a = att.split("=")[0].replace(reg, "");
              var v = att.split("=")[1].replace(reg, "");
          } else {
              return [];
          }
          var arr = [];
          var elms = this.$T("*",pElm);
          for (var i = 0, iLen = elms.length; i < iLen; i++) {
              if (elms[i].getAttribute(a) == v) {
                  arr.push(elms[i]);
              }
          }
          return arr;
      },
      $MH : function(att,tagName,pElm) {
          if (att && typeof(att) == "string") {
              var reg = /(^\s*)|(\s*$)/g;
              var a = att.split("=")[0].replace(reg, "");
              var v = att.split("=")[1].replace(reg, "");
          } else {
              return [];
          }
          var arr = [];
          var elms = this.$T(tagName,pElm);
          for (var i = 0, iLen = elms.length; i < iLen; i++) {
              if (elms[i].getAttribute(a) == v) {
                  arr.push(elms[i]);
              }
          }
          return arr;
      },
      addEvent : function(elm,type,handler,useCapture){
          try{
              elm.attachEvent("on"+type,function(){handler();});
          }catch(e){
              try{
                  if(null == useCapture) useCapture = false;
                  elm.addEventListener(type,handler,useCapture);
              }catch(e1){
                  elm["on"+type] = function(){
                      handler;
                  }
              }
          }
      }
  }

  if (![].indexOf) {
      Array.prototype.indexOf = function(key) {
          var index = -1;
          for (var i = 0, iLen = this.length; i < iLen; i++) {
              if (this[i] == key) {
                  index = i;
                  break;
              }
          }
          return index;
      }
  }
  



var className ="grd_row_1";
function MM(evt){
try{
   	  var element;
  if (window.event){
  	 element=window.event.srcElement;
  }else{
    	  element=evt.target;
  }
  if (element.tagName=='TD' 
  && (element.parentNode.className=="grd_row_1" || element.parentNode.className=="grd_row_2")){
className = element.parentNode.className;	
  	element.parentNode.className="grd_row_active";
  }
  }catch(e){}
}

function MO(evt){
try{
  var element;
  if (window.event){
  	 element=window.event.srcElement;
  }else{
    	  element=evt.target;
  }
  if (element.tagName=='TD' && element.parentNode.className=="grd_row_active"){
  	element.parentNode.className=className;
  }
   }catch(e){}
}

function selectAllClicked(obj,form){
      var inputs=form.getElementsByTagName("input");
      
      for(var i=0;i<inputs.length;i++){
     	if (inputs[i].name=="id"){
     	    inputs[i].checked=obj.checked;   
     	}
     }
  }
  
 function checkAll(obj,itemName){
      var inputs=document.getElementsByName(itemName);	      
      for(var i=0;i<inputs.length;i++){
      	if(inputs[i].disabled == false){
     	    inputs[i].checked=obj.checked; 
      	}else{
      	inputs[i].checked=false; 
      	}
     }
  }
  
  function submitAction(form, url){
      form.action = url;
      form.submit();
  }
  
  
// 添加GRID列表样式 ShiCH
$G = function (id){
return document.getElementById(id);
}
var grid = {
rootPath : GetRootPath(),
init : function(){
var oThis = this;
var g = $G("lessGridList");
if(null == g) return;

this.img = new Image();
this.img.src = this.rootPath+"/theme/default/img/jes/lessGridRow.gif";	

var trs = g.getElementsByTagName("tr");
for(var i=1;i<trs.length;i++){
trs[i].onmouseover = function(){oThis.over(this);}
trs[i].onmouseout = function(){oThis.out(this);}
        var tds = trs[i].getElementsByTagName("td");
        
        for(var j=0,jLen=tds.length;j<jLen;j++){
            tds[j].style.backgroundImage = "url("+this.img.src+")";
            tds[j].style.backgroundPosition = "0px -200px";
            tds[j].style.backgroundRepeat = "repeat-x";
        }
}	
},
over : function(elm){
      var tds = elm.getElementsByTagName("td");
      for(var j=0,jLen=tds.length;j<jLen;j++){
          tds[j].style.backgroundPosition = "0px 0px";
      }
},
out : function(elm){
      var tds = elm.getElementsByTagName("td");
      for(var j=0,jLen=tds.length;j<jLen;j++){
          tds[j].style.backgroundPosition = "0px -200px";
      }
}
}	
function GetRootPath(){
var scripts = document.getElementsByTagName("script"),path = "";
for(var i=0,iLen=scripts.length;i<iLen;i++){
var cS = scripts[i],src = cS.src;
if(src.indexOf("main.js") != -1){
path = src.substring(0,src.indexOf("/js/main.js"));
break;
}
}
return path;
}

if(window.attachEvent){
window.attachEvent("onload",function(){grid.init();});
}else if(window.addEventListener){
window.addEventListener("load",function(){grid.init();},false);
}
  
  /*
  * 功 能 : 模仿按钮
  * 作 者 : 史纯华
  * 日 期 : 2009-7-30
  */
  var IS_IE = /msie/i.test(window.navigator.userAgent);
  var BTN = {
      $G : function(id){
          return document.getElementById(id);
      },
      $C : function(tagName,attrObject){
          var elm = window.document.createElement(tagName);
          var spArr = ["innerHTML","className","cellSpacing","cellPadding","id","colSpan","rowSpan"];
          if(null != attrObject && typeof(attrObject) == "object"){
              for(var item in attrObject){
                  if(spArr.indexOf(item) != -1)
                      elm[item] = attrObject[item];
                  else
                      elm.setAttribute(item,attrObject[item]);
              }
          }
          return elm;
      },
      $A : function(elm,pElm){
          return pElm ? pElm.appendChild(elm) : document.body.appendChild(elm);
      },
      $I : function(elm,oElm,pElm){
          return pElm ? pElm.insertBefore(elm,oElm) : document.body.insertBefore(elm,oElm);
      },
      $R : function(elm,pElm){
          return pElm ? pElm.removeChild(elm) : document.body.removeChild(elm);
      },
      $T : function(tagName,pElm){
          return pElm ? pElm.getElementsByTagName(tagName) : document.getElementsByTagName(tagName);
      },
      $M : function(att){
          if(att && typeof(att) == "string"){
              var reg = /(^\s*)|(\s*$)/g;
              var a = att.split("=")[0].replace(reg,"");
              var v = att.split("=")[1].replace(reg,"");
          }else{
              return [];
          }
          var arr = [];
          var elms = this.$T("*");
          for(var i=0,iLen=elms.length;i<iLen;i++){
              var _temp = IS_IE ? elms[i][a] : elms[i].getAttribute(a);
              if(_temp == v){
                  arr.push(elms[i]);
              }
          }
          return arr;
      },
      addEvent : function(elm,type,handler,useCapture){
          try{
              elm.attachEvent("on"+type,function(){handler();});
          }catch(e){
              try{
                  if(null == useCapture) useCapture = false;
                  elm.addEventListener(type,handler,useCapture);
              }catch(e1){
                  elm["on"+type] = function(){
                      handler;
                  }
              }
          }
      },
      initBtns : function(){
          var btns = this.$M("type=btn");
          for(var i=0,iLen=btns.length;i<iLen;i++){
              var btn = btns[i];
              var e = btn.getAttribute("onclick");
              var v = btn.getAttribute("value");
              var img = btn.getAttribute("img");
              btn.className = "BTN";
              var m = this.$C("div",{className:"main"});
              var l = this.$C("div",{className:"left"});
              var c = this.$C("div",{className:"center"});
              var s = this.$C("span",{className:"text",innerHTML:v});
              var r = this.$C("div",{className:"right"});
              this.$A(m,btn),this.$A(l,m),this.$A(c,m),this.$A(r,m),this.$A(s,c);
              if(img && img != ""){
                  var g = this.$C("img",{src:img,border:0});
                  this.$I(g,s,c);
              }
              
              this.addEvent(m,"mouseover",function(event){
                  var evt = event ? event : window.event;
                  var elm = evt.srcElement || evt.target;
                  var pNode = elm.parentNode;
                  while(pNode != null && pNode.className != "main"){
                      pNode = pNode.parentNode;
                  }
                  if(pNode != null && pNode.className == "main")
                      pNode.className = "mainOver";
              });
              this.addEvent(m,"mouseout",function(event){
                  var evt = event ? event : window.event;
                  var elm = evt.srcElement || evt.target;
                  var pNode = elm.parentNode;
                  while(pNode != null && pNode.className != "mainOver"){
                      pNode = pNode.parentNode;
                  }
                  if(pNode != null && pNode.className == "mainOver")
                      pNode.className = "main";
              });
          }
      },
      initBtn : function(id){
          var btn = this.$G(id);
          var e = btn.getAttribute("onclick");
          var v = btn.getAttribute("value");
          var img = btn.getAttribute("img");
          btn.className = "BTN";
          var m = this.$C("div",{className:"main"});
          var l = this.$C("div",{className:"left"});
          var c = this.$C("div",{className:"center"});
          var s = this.$C("span",{className:"text",innerHTML:v});
          var r = this.$C("div",{className:"right"});
          this.$A(m,btn),this.$A(l,m),this.$A(c,m),this.$A(r,m),this.$A(s,c);
          if(img && img != ""){
              var g = this.$C("img",{src:img,border:0});
              this.$I(g,s,c);
          }
          
          this.addEvent(m,"mouseover",function(event){
              var evt = event ? event : window.event;
              var elm = evt.srcElement || evt.target;
              var pNode = elm.parentNode;
              while(pNode != null && pNode.className != "main"){
                  pNode = pNode.parentNode;
              }
              if(pNode.className == "main")
                  pNode.className = "mainOver";
          });
          this.addEvent(m,"mouseout",function(event){
              var evt = event ? event : window.event;
              var elm = evt.srcElement || evt.target;
              var pNode = elm.parentNode;
              while(pNode != null && pNode.className != "mainOver"){
                  pNode = pNode.parentNode;
              }
              if(pNode.className == "mainOver")
                  pNode.className = "main";
          });
      }
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
  BTN.addEvent(window,"load",function(){BTN.initBtns();});
  
  WaitingLayer = {
      show:function(){
          var layer = BTN.$G("ajaxWaitingLayer");
          if(null == layer){
              layer = BTN.$C("div",{id:"ajaxWaitingLayer",
                className:"ajaxWaitingLayer",
                innerHTML:"<TABLE width=\"100%\" height=\"100%\"><TR><TD align=\"center\" valign=\"middle\"><span style=\"border:1px solid #7998B7;width:186pxpx;height:22px;background:#FFFFFF;padding:2px;display:inline-block;\"><span class='waitImg'></span><span style=\"font:normal 12px Verdana;color:#2B61BA; height:22px; padding:5px 2px 0px 5px;\">Loading...</span></span></TD></TR></TABLE><div style=\"background:transparent;filter:alpha(opacity=0);-moz-opacity:0;-khtml-opacity:0;width:100%;height:100%;position:absolute;left:0;top:0;z-index:10000;\"></div>"});
              BTN.$A(layer);
          }
          layer.style.display = "block";
      },
      hide:function(){
          var layer = BTN.$G("ajaxWaitingLayer");
          if(layer)
              setTimeout(function(){layer.style.display = "none";},300);
      }
  }