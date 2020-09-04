
function goToPage(page){
	document.location=page;
}

function openWindow(url){
	window.open(url,'_blank','width=700,height=500,left=300,top=20,menubar=no,titlebar=no')
}

function openBigWindow(url){
	window.open(url,'_blank','width=1024,height=735,left=0,top=0,menubar=no,titlebar=no')
}

//检查多选框集是否至少有一个被选中
function checkChkBoxesSelected(chkBoxName){
	var chkBoexes= document.getElementsByName(chkBoxName);
	for(i=0;i<chkBoexes.length;i++){
		if(chkBoexes[i].checked){
			return true;
		}
	}
	return false;
}

//提交ACTION
function submitAction(form,url){
	form.action=url;
	form.submit();
}
function submitActionListDatasReadOnly(orderColumn,orderDirection){
	submitAction(document.getElementById('Form1'),'listDatasReadOnly.action?orderColumn='+orderColumn+'&orderDirection='+orderDirection);
}
function submitActionListDatas(orderColumn,orderDirection){
	submitAction(document.getElementById('Form1'),'listDatas.action?orderColumn='+orderColumn+'&orderDirection='+orderDirection);
}
function submitActionListDatasContract(orderColumn,orderDirection){
	submitAction(document.getElementById('Form1'),'listDatasContract.action?orderColumn='+orderColumn+'&orderDirection='+orderDirection);
}
function submitActionListCustConfig(orderColumn,orderDirection){
	submitAction(document.getElementById('Form1'),'listCustomerConfig.action?orderColumn='+orderColumn+'&orderDirection='+orderDirection);
}
//zhaoqian query
function submitActionListDatasQuery(orderColumn,orderDirection){
	submitAction(document.getElementById('Form1'),'listSearchData.action?orderColumn='+orderColumn+'&orderDirection='+orderDirection);
}
function submitActionListDatasInner(subTableId,orderColumn,orderDirection,subCanModify){
	submitAction(document.getElementById('Form1'),'listDatasInner.action?tableIdInner='+subTableId+'&orderColumnSub='+orderColumn+'&orderDirectionSub='+orderDirection+'&subCanModify='+subCanModify);
}
//zhaoqian query
function submitActionListDatasInnerQuery(orderColumn,orderDirection){
	submitAction(document.getElementById('Form1'),'listQueryDatasInner.action?orderColumnSub='+orderColumn+'&orderDirectionSub='+orderDirection);
}

function submitActionListDatasInnerVcountry(orderColumn,orderDirection,subCanModify){
	submitAction(document.getElementById('Form1'),'listDatasInnerVcountry.action?orderColumnSub='+orderColumn+'&orderDirectionSub='+orderDirection+'&subCanModify='+subCanModify);
}

function submitActionListDatasAudit(orderColumn,orderDirection){
	submitAction(document.getElementById('Form1'),'dataAudit.action?orderColumn='+orderColumn+'&orderDirection='+orderDirection);
}

function submitActionListDatasLowerStatus(orderColumn,orderDirection){
	submitAction(document.getElementById('Form1'),'dataLowerStatus.action?orderColumn='+orderColumn+'&orderDirection='+orderDirection);
}

function submitActionListDatasLogQuery(orderColumn,orderDirection){
	submitAction(document.getElementById('Form1'),'dataLogSearch.action?orderColumnLog='+orderColumn+'&orderDirectionLog='+orderDirection);
}

//将ID代表的按钮禁用
function disableButtonById(buttonId){
	document.getElementById(buttonId).disabled=true;
}

//将ID代表的按钮禁用
function disableButtonById2(buttonId, val, fangyan1){
	document.getElementById(buttonId).disabled=true;
	var txt = val.options[val.selectedIndex].dataValue;
	var txt1 = txt.substring(7);
	if (document.getElementById(fangyan1) != null){
		document.getElementById(fangyan1).value = txt1;
	}
}
