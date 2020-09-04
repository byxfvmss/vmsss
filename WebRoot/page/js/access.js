function changeSelect(webroot) {
	var infoType = document.getElementById("infoTypeCode").value;
	var url = webroot + "/asyLoadTableNameAndId.action";
	var param = "infoTypeCode=" + infoType;
	var result = sendXmlHttpPost(url, param);
	var tableSelectObj = document.getElementById("tableIds");
	var lengh = tableSelectObj.length;
	for (var i = 0; i < lengh; i++) {
		tableSelectObj.options[0] = null;
	}
	var addOptions = result.split(",");
	for (var j = 0; j < addOptions.length - 1; j++) {
		tableSelectObj.options[tableSelectObj.length] = new Option(addOptions[j + 1], addOptions[j]);
		j = j + 1;
	}
	return;
}
function destroySelf(obj) {
	var tdObj = obj.parentNode;
	tdObj.parentNode.removeNode(true);
}
//判断是否重复
function checkDouble(tableId,orgId){
	var tableSelectIds=document.getElementsByName("tableSelectId");
	var orgSelectIds=document.getElementsByName("orgSelectId");
	for(var k=0;k<tableSelectIds.length;k++){
		if(tableSelectIds[k].value==tableId){
			if(orgSelectIds[k].value==orgId){
				return true;
			}			
		}
	}
	return false;
}
//动态增加
function dynamicAdd(webapp,type,container, tableName, tableId, orgName, orgId) {
	if(checkDouble(tableId,orgId)){
		return;
	}
	var tr = document.createElement("tr");
	container.appendChild(tr);
	var cell = tr.insertCell();
	if(type==1){
		cell.innerHTML = "基础信息";
	}else if(type==2){
		cell.innerHTML = "申报信息";
	}else if(type==3){
		cell.innerHTML = "核销信息";
	}else if(type==5){
		cell.innerHTML = "单位基本信息";
	}	
	cell = tr.insertCell();
	cell.innerHTML = tableName;
	cell = tr.insertCell();
	cell.innerHTML = orgName;
	cell = tr.insertCell();
	cell.innerHTML = "<img src='" + webapp + "/image/button/delete.gif' onclick='destroySelf(this)' title='删除' style='cursor:hand;'/>";
	cell = tr.insertCell();
	cell.style.display = "none";
	cell.innerHTML = "<input type='hidden' name='tableSelectId' value='" + tableId + "'/>";
	cell = tr.insertCell();
	cell.style.display = "none";
	cell.innerHTML = "<input type='hidden' name='orgSelectId' value='" + orgId + "'/>";
}
function add(webapp) {
	var taObj = document.getElementById("resultTable");
	var tableSelectObj = document.getElementById("tableIds");
	var instCodeObj = document.getElementById("instCodeIds");
	var infoType = document.getElementById("infoTypeCode").value;
	var flag=0;
	for (var m = 0; m < tableSelectObj.options.length; m++) {
		if (true == tableSelectObj.options[m].selected) {
			flag=1;
			for (var n = 0; n < instCodeObj.options.length; n++) {
				if (true == instCodeObj.options[n].selected) {
					dynamicAdd(webapp,infoType,taObj.tBodies[0], tableSelectObj.options[m].text, tableSelectObj.options[m].value, instCodeObj.options[n].text, instCodeObj.options[n].value);
					flag=2;
				}
			}
		}
	}
	if(flag==0){
		alert("请选择单据!");
	}else if(flag==1){
		alert("请选择机构!");
	}
}

