<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/page/include.jsp"%>
<script type="text/javascript" src="<c:out value="${bopTheme}"/>/js/SimpleTree/js/ajax.js"></script>
<script type="text/javascript" src="<c:out value="${bopTheme}"/>/js/SimpleTree/js/simpleTree.js"></script>
<link href="<c:out value="${bopTheme}"/>/js/SimpleTree/css/simpleTree.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<c:out value="${bopTheme}"/>/js/zTree/css/demo.css" type="text/css">
<link rel="stylesheet" href="<c:out value="${bopTheme}"/>/js/zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="<c:out value="${bopTheme}"/>/js/zTree/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="<c:out value="${bopTheme}"/>/js/zTree/js/jquery.ztree.core-3.5.js"></script>
<script language="javascript" type="text/javascript" src="<c:out value='${webapp}'/>/page/js/window.js" charset="UTF-8"></script>
<script language="javascript">
	PAGE_CACHE = {};
	function getSelectedItem() {
		var node;
		var treeObj = $.fn.zTree.getZTreeObj("simpleTree");
		var sNodes = treeObj.getSelectedNodes();
		if (sNodes.length > 0) {
			node = sNodes[0];
		}
		return node;
	}
	function createItem() {
		var node = getSelectedItem();

		var date = new Date();
		var url = 'editItemInfo.action';
		url = url + "?parentItemInfoPram.itemCode =" + node.itemCode;
		url = url + "&parentItemInfoPram.path =" + node.path
		OpenModalWindow(url, 500, 350, true);
	}

	function editItem() {
		var tId = getSelectedItem().itemCode;
		var url = 'editItemInfo.action?itemInfoPram.itemCode =' + tId;
		OpenModalWindow(url, 500, 350, true);
		/* if (window.ActiveXObject) {
			var treeNode = PAGE_CACHE.tree.GetSelectedTreeNodes();
			if (null == treeNode)
				return;
			var id = treeNode.getAttribute("id");
			var level = treeNode.getAttribute("levelType");

			if (level == 1) {
			} else {
				var url = "<c:out value='${bopTheme}'/>/editAuth.action?roleId="
						+ id;
				OpenModalWindow(url, 500, 350, true);
			}
		} else {
			var zTree = $.fn.zTree.getZTreeObj("simpleTree");//获取ztree对象 
			var node = zTree.getSelectedNodes();
			if (node[0] == null || node[0].isParent) {
				alert("请选择角色信息。");
				return;
			} else {
				var id = node[0].pIds;
				var url = "<c:out value='${bopTheme}'/>/editAuth.action?roleId="
						+ id;
				OpenModalWindow(url, 500, 350, true);
			}
		} */
	}

	function deleteItem() {
		var tId = getSelectedItem().itemCode;
		var url = 'removeItemInfo.action?itemInfoForm.itemCode =' + tId;
		window.location = url;
		/* if (window.ActiveXObject) {
			var treeNode = PAGE_CACHE.tree.GetSelectedTreeNodes();
			if (null == treeNode)
				return;
			var id = treeNode.getAttribute("id");
			var level = treeNode.getAttribute("levelType");

			if (level == 1) {
			} else {
				if (confirm('确定要删除当前选中角色吗？')) {

					document.forms[0].action = "<c:out value='${bopTheme}'/>/deleteAuth.action?returnView=1&roleId="
							+ id;
					document.forms[0].roleId.value = id;
					document.forms[0].roleId.returnView = "1";
					document.forms[0].submit();
				}
			}
		} else {
			var zTree = $.fn.zTree.getZTreeObj("simpleTree");//获取ztree对象 
			var node = zTree.getSelectedNodes();
			if (node[0] == null || node[0].isParent) {
				alert("请选择角色信息。");
				return;
			} else {
				var id = node[0].pIds;
				if (confirm('确定要删除当前选中角色吗？')) {
					document.forms[0].action = "<c:out value='${bopTheme}'/>/deleteAuth.action?returnView=1&roleId="
							+ id;
					document.forms[0].roleId.value = id;
					document.forms[0].roleId.returnView = "1";
					document.forms[0].submit();
				}
			}
			;
		} */
	}

	//默认选择第一项
	function setSelectFirst() {
		var treeNodes = PAGE_CACHE.tree.data.selectNodes(".//TreeNode");
		if (treeNodes == null)
			return;
		for (var i = 0; i < treeNodes.length; i++) {
			var id = treeNodes[i].getAttribute("id");
			var level = treeNodes[i].getAttribute("levelType");
			if (level == '2') {
				PAGE_CACHE.tree.clickNode(id, false);
				return;
			}
		}
	}
	window.onload = init;
	function init() {

		var setting = {
			view : {
				showIcon : showIconForTree
			},
			data : {
				key : {
					name : "shortName",
					title: "taxperNumber"
				},
				simpleData : {
					enable : true,
					idKey : "id",
					pIdKey : "parentId"
				}
			},
			callback : {
			//点击  树下面 
				onClick : function zTreeOnClick(event, treeId, treeNode) {
					if (treeNode) {
						url = "showAlertList.action";
						url += "?instCode="+treeNode.id;
						//url +="?verificationInfo.taxNo="+treeNode.taxperNumber;
						parent.frames["alertList"].location.href = url;
					}
				}
			}
		};
		$.ajax({
			type : "Post",
			url : "loadInstFullInfoXml.action?X=" + (new Date()).getTime(),
			dataType : "json", //可以是text，如果用text，返回的结果为字符串；如果需要json格式的，可是设置为json
			success : function(data) {
				
				$.fn.zTree.init($("#simpleTree"), setting, data)
						.expandAll(true);
				var zTree = $.fn.zTree.getZTreeObj("simpleTree");//获取ztree对象 
				//var node = zTree.getNodeByParam("id", 1);
				var nodes = zTree.getNodes()[0];
				zTree.selectNode(nodes);//选择点  
				zTree.setting.callback.onClick(null, zTree.setting.treeId,
						nodes);
			},
			error : function(msg) {
				alert(" 数据加载失败！" + msg);
			}
		});

	}
	function getZtreeFristSubNodes(treeNode) {

	}
	function showIconForTree(treeId, treeNode) {
		return !treeNode.isParent;
	};
</script>
</head>
<body topmargin="0" leftmargin="0" rightmargin="0" class="pl12">
	<div class="p-station">
		<table id="tbl_tools" class="tree_top_bar" style="width:100%" cellpadding="1" cellspacing="1">
			<tr>
				<td align="left">机构列表</td>
				<td align="right"></td>
			</tr>
		</table>
		<div id="treeboxbox_tree" class="treeContainerBox" style="overflow:auto">
			<div class="clearall"></div>
			<div id="simpleTree" iconPath="<c:out value="${bopTheme}"/>/js/SimpleTree/images/">loading...</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	document.getElementById("treeboxbox_tree").style.height = screen.availHeight - 251;
</script>
</html>
