<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@ include file="../../../page/include.jsp"%>
	<script language="javascript" type="text/javascript" src="<%=bopTheme %>/js/main.js"></script>
	<link href="<%=bopTheme2 %>/css/main.css" rel="stylesheet" type="text/css" />
	<script language="javascript" type="text/javascript" src="<%=bopTheme %>/js/window.js"></script>
	<script charset="gb2312"  language="javascript" type="text/javascript" src="<%=bopTheme %>/js/validator.js"></script>
	<script type="text/javascript" src="<%=bopTheme %>/js/SimpleTree/js/ajax.js"></script>
	<script type="text/javascript" src="<%=bopTheme %>/js/SimpleTree/js/simpleTree.js"></script>
	<link href="<%=bopTheme %>/js/SimpleTree/css/simpleTree.css" rel="stylesheet" type="text/css">
	<script language="javascript">
		function displayTreeInst(){
			var t = document.getElementById("treeboxbox_tree");
			if(parent.frame.cols=="300,*")
	        {
				parent.frame.cols="0,*";
	        }
			else
			{
				parent.frame.cols="300,*";
			}
		}
		
		window.onload = init;
		var instTree = null;
		function init(path){
			WaitingLayer.show();
			var p = new ajax();
			p.url = "<%=webapp%>/loadBusinessAndUsrXml.action?X=" + (new Date()).getTime();
			instTree=new SimpleTree();
			p.onresult = function(){
				WaitingLayer.hide();
				if(null == this.data) return;
				instTree.container = instTree.$("simpleTree");
				instTree.iconPath = instTree.$("simpleTree").getAttribute("iconPath");
				instTree.dataStr = this.text;
				instTree.radioBox = true;
				instTree.updateBox = true;
				instTree.OnClick = function(){
				    //var b=parent.frames["BusinessSearchResultFrame"].document.getElementById("BtnMove");
				    var treeNode = this.selectedNode;
		            var id = treeNode.getAttribute("id");//获取交易码
		            //var level = treeNode.getAttribute("levelType");
					parent.frames["BusinessSearchResultFrame"].location.href="<%=webapp%>/findBusiAndBusiSup.action?businesscode="+id+"&fromFlag=menu";
				}
				instTree.GetAnalisyNodes = function(id){
		        	WaitingLayer.show();
		            var p = new ajax();
		            p.url = "<%=webapp%>/loadBusinessAndUsrXml.action?" + (new Date()).getTime()+ "&id="+id;
		            p.onresult = function(){
		                var data = this.text;
		                instTree.BuildAnalisyNodes(id,data);				    
		                WaitingLayer.hide();
		            };
		            p.send();
		        }
		        instTree.init();
			}
			p.send();
		}
		function refreshTreeInst(){
			init();
		}
	</script>
  </head>
  
  <body topmargin="0" leftmargin="0" rightmargin="0">
	<table id="treetitlebar" class="tree_top_bar" width="100%"  cellpadding="1" cellspacing="1">
		<tr>
			<td align="left">交易认定列表</td>
			<td align="right" valign="top">
			<span class="controlbtn_Ref" onclick="refreshTreeInst();" title="刷新"></span>
            </td>
		</tr>
	</table>	
	<div id="treeboxbox_tree" class="treeContainerBox"  style="overflow:auto">
		<div id="simpleTree" iconPath="<%=webapp%>/page/js/SimpleTree/images/">loading...</div>
	</div>
</body>
<script type="text/javascript">
	document.getElementById("treeboxbox_tree").style.height = screen.availHeight - 340;
</script>
</html>
