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
	loadColumnName(webroot);
	changeExp();
	return;
}

function loadColumnName(webroot){
	var tableSelectId=document.getElementById("tableIds").value;
	var url = webroot + "/asyLoadColumnName.action";
	var param = "tableSelectId=" + tableSelectId;
	var result = sendXmlHttpPost(url, param);
	resetcolumnSelect("columnIdsF",result);
	resetcolumnSelect("columnIdsS",result);
	resetcolumnSelect("columnIdsT",result);
	document.getElementById("valueF").value = '';
	document.getElementById("valueS").value = '';
	document.getElementById("valueT").value = '';
}

function resetcolumnSelect(selectId,result){
	var columnSelectObj = document.getElementById(selectId);
	var lengh = columnSelectObj.length;
	for (var i = 0; i < lengh; i++) {
		columnSelectObj.options[0] = null;
	}
	var addOptions = result.split(",");
	for (var j = 0; j < addOptions.length - 1; j++) {
		columnSelectObj.options[columnSelectObj.length] = new Option(addOptions[j + 1], addOptions[j]);
		j = j + 1;
	}
}

function exportToExcel(webroot){
	document.forms[0].action = webroot+"/exportToExcel.action";
	document.forms[0].submit();
	document.forms[0].action = webroot+"/listSearchData.action";
}
function query(webroot){
	document.forms[0].action = webroot+"/listSearchData.action";
	document.forms[0].submit();
}
function querylog(webroot){
	document.forms[0].action = webroot+"/dataLogSearch.action";
	document.forms[0].submit();
}
function changeExp(){
	var button=document.getElementById("BtnExcel");
	button.disabled=true;
}
function getAjaxTaxperName(taxperNumber, objName){
	$.ajax({url: 'getAjaxTaxperName.action',
	type: 'POST',
	async:false,
	data:{taxperNumber:taxperNumber},
	dataType: 'html',
	timeout: 1000,
	error: function(){objName.value = "";return;},
	success: function(result){
		if (result == "null"){
			objName.value = "";
		}else {
			objName.value = result;
		}
		return;
	}
	});
}

//设置查询结果div坐标
function ChangeCoords() {
    //    var left = $("#instId")[0].offsetLeft; //获取距离最左端的距离，像素，整型
    //    var top = $("#instId")[0].offsetTop + 26; //获取距离最顶端的距离，像素，整型（20为搜索输入框的高度）
    var left = $("#taxperName").position().left; //获取距离最左端的距离，像素，整型
    var top = $("#taxperName").position().top + 20; ; //获取距离最顶端的距离，像素，整型（20为搜索输入框的高度）
    $("#searchInstResult").css("left", left + "px"); //重新定义CSS属性
    $("#searchInstResult").css("top", top + "px"); //同上
}

/**$(function () {
    $("#taxperName").keyup(function (evt) {
        ChangeCoords(); //控制查询结果div坐标
        var k = window.event ? evt.keyCode : evt.which;
        //输入框的id为taxperName，这里监听输入框的keyup事件
        //不为空 && 不为上箭头或下箭头或回车
        if ($("#taxperName").val() != "" && k != 38 && k != 40 && k != 13) {
            $.ajax({
                type: 'POST',
                //async: false, //同步执行，不然会有问题
                url: 'getAjaxTaxperNameList.action', //提交的页面
                //data: obj,//"{'userName':'" + $("#taxperName").val() + "'}",//参数（如果没有参数：null）
                //data:"{'taxperName':'" + $("#taxperName").val() + "','taxperNumber':'" + $("#taxPerNumber").val() + "'}",//参数（如果没有参数：null）
                data:{taxperName:$("#taxperName").val(),taxperNumber:$("#taxPerNumber").val()},
                dataType: 'html',
            	timeout: 1000,
                error: function (e) {//请求失败处理函数
                    return;
                },
                success: function (result) { //请求成功后处理函数。
                    var objData = result;//eval("(" + data + ")");
                    if (objData.length > 0) {
                    	var layer = objData;
                        //将结果添加到div中    
                        $("#searchInstResult").empty();
                        $("#searchInstResult").append(layer);
                        $(".line:first").addClass("hover");
                        $("#searchInstResult").css("display", "");
                        //鼠标移动事件

                        $(".line").hover(function () {
                            $(".line").removeClass("hover");
                            $(this).addClass("hover");
                        }, function () {
                            $(this).removeClass("hover");
                            //$("#searchInstResult").css("display", "none");
                        });
                        //鼠标点击事件
                        $(".line").click(function () {
                            $("#taxperName").val($(this).text());
                            $("#searchInstResult").css("display", "none");
                        });
                    } else {
                        $("#searchInstResult").empty();
                        $("#searchInstResult").css("display", "none");
                    }
                }
            });
        }
        else if (k == 38) {//上箭头
            $('#taxperNameId tr.hover').prev().addClass("hover");
            $('#taxperNameId tr.hover').next().removeClass("hover");
            $('#taxperName').val($('#taxperNameId tr.hover').text());
        } else if (k == 40) {//下箭头
            $('#taxperNameId tr.hover').next().addClass("hover");
            $('#taxperNameId tr.hover').prev().removeClass("hover");
            $('#taxperName').val($('#taxperNameId tr.hover').text());
        }
        else if (k == 13) {//回车
            $('#taxperName').val($('#taxperNameId tr.hover').text());
            $("#searchInstResult").empty();
            $("#searchInstResult").css("display", "none");
        }
        else {
            $("#searchInstResult").empty();
            $("#searchInstResult").css("display", "none");
        }
    });
    $("#searchInstResult").bind("mouseleave", function () {
        $("#searchInstResult").empty();
        $("#searchInstResult").css("display", "none");
    });
});*/
