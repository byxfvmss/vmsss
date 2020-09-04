

/*******************************************************************************
 * 函数名称：OpenFXYJDataQueryWindow 处理机能：风险数据查询打开一个没有返回值的新的非模态窗口
 * 
 * 参数 ：newURL 新窗口的链接地址 日期 ：2007/10/22 0:46 作者 ：jz_guo 修改人 ：-- 修改日 ：-- 概要 ：--
 ******************************************************************************/
function OpenFXYJDataQueryWindow(newURL) {
	try {
		// 初始化变量，用于接收页面反回值。
		var recdata = false;
		var width = screen.width * 0.9;
		var height = screen.height * 0.9;
		var leftLen = screen.width * 0.05;
		var TopLen = screen.height * 0.05;

		// 模式窗口打开指定的窗口链接
		recdata = showModalDialog(newURL, "DescWindow", "dialogWidth:" + width
				+ "px;dialogHeight:" + height
				+ "px;center=1;scroll=1;help=0;status=0");
	} catch (err) {
	}
}



// 列表对象 全选
function selectall(listID) {
	var i;
	var obj = document.getElementsByName(listID);
	if (obj) {
		for (i = 0; i < obj.length; i++) {
			if (obj[i].type.toLowerCase() == 'checkbox') {
				if (obj[i].checked == false && obj[i].disabled == false) {
					obj[i].checked = true;
				}
			}
		}
	}
}
// 列表对象取消全选
function unselectall(listID) {
	var i;
	var obj = document.getElementsByName(listID);
	if (obj) {
		for (i = 0; i < obj.length; i++) {
			if (obj[i].type.toLowerCase() == 'checkbox') {
				if (obj[i].checked == true) {
					obj[i].checked = false;
				}
			}
		}
	}
}
//根据CheckBox 设置是否全部选择
function cbxselectall(obj,listID){
	if(obj.checked){
		selectall(listID);
	}else{
		unselectall(listID);
	}
}
function check(vals) {
	var ids = document.getElementsByName(vals);
	var isOK = false;
	for (i = 0; i < ids.length; i++) {
		var obj = ids[i];
		if (obj.checked) {
			isOK = true;
			break;
		}
	}
	if (isOK) {
		return true;
	} else {
		alert("请您先选择要操作的记录！");
		return false;
	}
}

function beforeDelete(actionName, ids) {
	if (check(ids)) {
		if (confirm('确定要删除当前选中所有项吗？')) {
			document.forms[0].action = actionName;
			document.forms[0].submit();
		}
	}

}

//--------------------------随机字符-------------------------- 
//str_0 长度 
//str_1 是否大写字母 
//str_2 是否小写字母 
//str_3 是否数字 
function rnd_str(str_0,str_1,str_2,str_3) 
{ 
	var Seed_array=new Array(); 
	var seedary; 
	var i; 
	
	Seed_array[0]="" 
	Seed_array[1]= "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z"; 
	Seed_array[2]= "a b c d e f g h i j k l m n o p q r s t u v w x y z"; 
	Seed_array[3]= "0 1 2 3 4 5 6 7 8 9"; 
	
	
	if (!str_1&&!str_2&&!str_3){str_1=true;str_2=true;str_3=true;} 
	
	if (str_1){Seed_array[0]+=Seed_array[1];} 
	if (str_2){Seed_array[0]+=" "+Seed_array[2];} 
	if (str_3){Seed_array[0]+=" "+Seed_array[3];} 
	
	Seed_array[0]= Seed_array[0].split(" "); 
	seedary="" 
	for (i=0;i<str_0;i++) 
	{ 
		seedary+=Seed_array[0][Math.round(Math.random( )*(Seed_array[0].length-1))] 
	} 
	return(seedary); 

}
