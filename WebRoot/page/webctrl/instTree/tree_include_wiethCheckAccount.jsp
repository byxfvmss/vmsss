<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<% 
String treeType=request.getParameter("treeType");
if(treeType==null){
	treeType="";
}
String taskId_tree=request.getParameter("taskId_tree");
if(taskId_tree==null){
	taskId_tree="";
}
String isAll=request.getParameter("isAll");
if(isAll==null){
	isAll="";
}
String bankId_tree=request.getParameter("bankId_tree");
if(bankId_tree==null){
	bankId_tree="";
}
String width_tree=request.getParameter("width_tree");
if(width_tree==null){
	width_tree="";
}
String bankName_tree=request.getParameter("bankName_tree");
if(bankName_tree==null){
	bankName_tree="";
}
String callback_tree=request.getParameter("callback_tree");
if(callback_tree==null){
	callback_tree="";
}
String task_tree=request.getParameter("task_tree");
if(task_tree==null){
	task_tree="";
}
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";
%>
<script type="text/javascript"
	src="<%=basePath%>page/js/jquery/jquery_1.42.js"></script>
	<!-- 树形结构图 -->
	<link rel="stylesheet" type="text/css" href="<%=basePath%>page/webctrl/zTree/css/zTreeStyle/zTreeStyle.css" />
	<script type="text/javascript" src="<%=basePath%>page/webctrl/zTree/js/jquery.ztree.core-3.0.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>page/webctrl/zTree/js/jquery.ztree.excheck-3.0.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>page/webctrl/instTree/JSON.js"></script>
	<script type="text/javascript" src="<%=basePath%>page/webctrl/instTree/tree.js"></script>
	

	<!-- end -->
<script language="javascript" type="text/javascript">
	var tree=null; 
	function setOrg(obj)
	{
		var task_tree="<%=task_tree%>";
		var taskId_tree="<%=taskId_tree%>";
		//任务机构树
		/* if(task_tree!=null&&task_tree!=""){
			//无任务不弹出机构树
			if(taskId_tree==null || taskId_tree==""){
				return;
			} 
		} */
		var bankName=$("#<%=bankName_tree%>");
		var bankId=$("#<%=bankId_tree%>");
		if(tree==null){
			var param=new Object();
			param.isAll="<%=isAll%>";
			if(param.isAll==null || param.isAll==""){
				param.isAll="all";
			}
			param.taskId=taskId_tree;

			/* var url="Init.do?method=";
			if(param.taskId!=null&&param.taskId!=""){
				url+="addTaskInstanceBankDimTree";//按审核关系类型
			}else{
				url+="addTreeNode";//按机构上下级
			} */
			var url = "loadInstXmlWiethCheckAccount.action"
			param.treeType="<%=treeType%>";
			tree=bankTree_public.loadTree(url,"simpleTree",param);
		}
		if(tree!=null&&bankId.val()!=""){
			tree.setSelectedBank(bankId.val());
		}
		if($("#cbxOrgAll")){
			$("#cbxOrgAll").checked=false;
		}
	    var parentPos = $(obj).position();
	    if(tree!=null&&tree.params.treeType=='check'){
	    	$("#treeboxbox_tree").css("width",400)
		}
		$("#treeboxbox_tree").css("top",parentPos.top + $(obj).outerHeight())
		$("#treeboxbox_tree").css("left",parentPos.left)
		$("#treeboxbox_tree").css("display","");
	}


	function SelectOrg(){
		var bankName=$("#<%=bankName_tree%>");
		var bankId=$("#<%=bankId_tree%>");
		var bankInfos=tree.getSelectedBankIdAndName();
		if(null==bankInfos){
			alert("请选择机构！");
			return;
		}
		bankId.val(bankInfos.id);
		bankName.val(bankInfos.name);
		bankInfos=null;
		hideTree();
		<%
		if(callback_tree!=null&&!"".equals(callback_tree)){%>
			<%=callback_tree%>
		<%}
		%>
	}
 
	//取消
	function hideTree(){
		$("#treeboxbox_tree").css("display","none");
	}
 
	//清除
	function clearOrg(){
		var bankName=$("#<%=bankName_tree%>");
		var bankId=$("#<%=bankId_tree%>");
		bankId.val("");
		bankName.val("");
		tree.ztree.checkAllNodes(false);
		hideTree();
	}
	
	//全选 
	function selectedAll(object){
		if(tree!=null&&object.checked){
			tree.ztree.checkAllNodes(true);
		}else if(tree!=null){
			tree.ztree.checkAllNodes(false);
		}
	}
	
	//机构名称查询机构树
	function queryOrg_tree(){
		var bankId=$("#<%=bankId_tree%>");
		var bankName=$("#bankname_tree").val();
		if(bankName==null){
			bankName="";
		}
		var param=tree.params;
		param.bankName=bankName;
		if($("#instSelect_tree")){
			var instID=$("#instSelect_tree").val();
			if(instID==null){
				instID="";
			}
			param.instID=instID;
		}
		tree.reload(param);
		tree.setSelectedBank(bankId.val());
	}

	//按机构级选中机构
	function instSelecedBank_tree(instID){
		var bankName=$("#bankname_tree").val();
		if(bankName==null){
			bankName="";
		}
		var param=tree.params;
		param.bankName=bankName;
		param.instID=instID;
		tree.reload(param);
	}
	
	//级联选择
	function select_tree(object){
		if(tree!=null&&object.checked){
			tree.setChkboxType({'Y' : 'ps', 'N' : 'ps'});
		}else if(tree!=null){
			tree.setChkboxType({'Y' : '', 'N' : ''});
		}
	}
	
	//展开
	function expand_tree(object){
		if(tree!=null&&object.checked){
			tree.ztree.expandAll(true);
		}else if(tree!=null){
			tree.ztree.expandAll(false);
		}
	}
</script>

<div id="treeboxbox_tree"
	style="width: 350;height: 300px;border: 1px solid #165AB3; position: absolute; background-color: #FFFFFF; display: none; z-index: 999">
	<table cellSpacing=0 cellPadding=0 class="lessGrid">
		<tr style="background-color: #ffffff;">
			<%if("check".equals(treeType)){%>
				<td width="40%" align="left" id="lbxeInstList_tbSet" nowrap="nowrap">
					
						<INPUT id=cbxOrgAll onclick="selectedAll(this);"  type=checkbox />全选
						<select id="instSelect_tree" name="instSelect_tree"  <c:if test="${taskDefineForm.isTotal eq 'Y'}">disabled</c:if>
							title="请注意 选择的机构必须是 所选报表汇总关系 中的机构" style="width: 115px;"
							onchange="instSelecedBank_tree(this.value)">
							<option value="">请选择机构集</option>
							<c:forEach items="${instSelectList}" var="inst">
								<option value="<c:out value='${inst.instID}' />"><c:out
									value="${inst.instName}" /></option>
							</c:forEach>
						</select>
					
				</td>
			<%} %>
			<td width="40%" id="lbxeInstList_tbSearchText" align="left">
				机构 <input name="bankname_tree" type="text" style="width: 100px" id="bankname_tree" />
			</td>
			<td width="20%" nowrap="nowrap" id="lbxeInstList_tbSearchButton" align="left">
				<input type="button" 
					class="tbl_query_button" 
					onMouseMove="this.className='tbl_query_button_on'" 
					onMouseOut="this.className='tbl_query_button'" 
					onclick="queryOrg_tree()" id="lbxeInstList:BtnQuery"
					name="lbxeInstList:BtnQuery" value="查询"/>
			</td>
		</tr>
		<tr style="background-color: #ffffff; width: 100%;">
			<td colspan="3" height="180" width="100%">
				<div style=" height: 220px; width: 100%;overflow: auto; background-color: #ffffff;" >
					<ul id="simpleTree" class="ztree" ></ul>
				</div>
			</td>
		</tr>
		<%if("check".equals(treeType)){%>
		<tr style="background-color: #ffffff;">
			<td colspan="3" width="100%">
				<input type="checkbox" id="checkLevel_tree" value="10" onclick="select_tree(this)">级联选择
			</td>
		</tr>
		<%} %>
	</table>
		<input type="button" 
			class="tbl_query_button" 
			onMouseMove="this.className='tbl_query_button_on'" 
			onMouseOut="this.className='tbl_query_button'" 
			onclick="SelectOrg();" name="BtnView" value="确定" id="BtnView"
			style="margin-left: 50px; margin-top: 4px"/>
		<input type="button" 
			class="tbl_query_button" 
			onMouseMove="this.className='tbl_query_button_on'" 
			onMouseOut="this.className='tbl_query_button'" 
			onclick="hideTree();" name="BtnView" value="取消" id="BtnView"
			style="margin-left: 5px; margin-top: 4px"/>
		<input type="button" 
			class="tbl_query_button" 
			onMouseMove="this.className='tbl_query_button_on'" 
			onMouseOut="this.className='tbl_query_button'" 
			onclick="clearOrg();" name="BtnView" value="清除" id="BtnView"
			style="margin-left: 5px; margin-top: 4px"/>
</div>