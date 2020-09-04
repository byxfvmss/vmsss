<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../../page/include.jsp"%>
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
<link href="<%=webapp%>/page/subwindow.css" type="text/css" rel="stylesheet">
<!-- MessageBox -->
<script src="<%=bopTheme%>/js/MessageBox/js/messageBox.js" type="text/javascript"></script>
<link href="<%=bopTheme%>/js/MessageBox/css/messageBox.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=bopTheme%>/js/validator.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<meta http-equiv="Pragma" content="no-cache" />
<base target="_self">
<title>纸质发票分发</title>

<style type="text/css">
.detailInfoDIV {
	border: 1px solid green;
	background-color: khaki;
	top: 110px;
	left: 150px;
	width: 450px;
	height: 300px;
	position: absolute;
	z-index: 2;
	filter: alpha(opacity =     90);
	-moz-opacity: 0.9;
	display: none;
}
 
.unSelectBox { 
text-align: center; 
vertical-align:middle; 
height: 39px; 
overflow: hidden; 
moz-user-select: -moz-none; 
-moz-user-select: none; 
-o-user-select:none; 
-khtml-user-select:none; /* you could also put this in a class */ 
-webkit-user-select:none;/* and add the CSS class here instead */ 
-ms-user-select:none; 
user-select:none;/**禁止选中文字*/ 
} 
.user_class{
	font-size:12px;
	font-family:Arial,Verdana,宋体; 
	vertical-align:middle ;
	border: 1px solid #CCCCCC;
	height: 26px;
	width:126px;
	line-height:24px;
	padding:0 3px;
}
.page_used_invoice_num{
	font-size:12px;
	font-family:Arial,Verdana,宋体; 
	vertical-align:middle ;
	border: 1px solid #CCCCCC;
	height: 26px;
	width:126px;
	line-height:24px;
	padding:0 3px;
}


</style>
<script type="text/javascript">
	var max_distrubute_limit=<s:property value='max_distrubute_limit' />;
	
	function trim(str){
		if(str == null)return "";
		return str.replace(/(^\s*)|(\s*$)/g, "");
	}
	var subed = false;
	function checkForm(){
		$input_trs=$(".input_tr");
		for($i=0;$i<$input_trs.size();$i++){
			$sel_inst_obj=$($input_trs[$i]).find(".inst_class").get(0);
			if(fucCheckNull($sel_inst_obj,"请选择领用机构")==false){
	        	return false;
	        }
	        
	        $sel_user_obj=$($input_trs[$i]).find(".user_class").get(0);
	        if(fucCheckNull($sel_user_obj,"领用人不能为空")==false){
	        	return false;
	        }
	        
	        $sel_num_obj=$($input_trs[$i]).find(".page_used_invoice_num").get(0);
	        if(fucIsUnsignedInteger($sel_num_obj,"分发张数必须大于0")==false){
	        	return false;
	        }
		}
		
		$curr_distribute_item_invoices=$(".page_used_invoice_num");
		$curr_distribute_invoice_num=0;
		if($curr_distribute_item_invoices.size()>0){
			for($idx=0;$idx<$curr_distribute_item_invoices.size();$idx++){
				$item_invoice_num=$($curr_distribute_item_invoices[$idx]).val();
				$curr_distribute_invoice_num=$curr_distribute_invoice_num+parseInt($item_invoice_num);
			}
		}
		
		if($curr_distribute_invoice_num > max_distrubute_limit){
			alert("本次分发数超出限制，最大分发数不能超过1000");
			return;
		}

	    //验证是否重复提交
	    if (subed == true){
	       alert("信息正在发送给服务器，请不要重复提交信息！");
	       return false;
	    }
	    subed=true;
   		return true;
	}
	function submitForm(actionUrl){
		if(true == checkForm()){
			document.forms[0].action =actionUrl;
			document.forms[0].submit();
		}
	}
	$(function(){
// 		alert(max_distrubute_limit);
		/*获取库存中已经分发的数量*/
		function getStoreDistributeNum(stock_id,invoice_code){
			return parseInt($("#ori_distribute_"+stock_id+"_"+invoice_code+"_num").val());
		}
		
		/*获取库存中的终止编号*/
		function getStoreInvoiceCodeEndNo(stock_id,invoice_code){
			return parseInt(trim($("#ori_end_"+stock_id+"_"+invoice_code+"_no").text()));
		}
		/*获取库存中的起始编号*/
		function getStoreInvoiceCodeBeginNo(stock_id,invoice_code){
			return parseInt(trim($("#ori_begin_"+stock_id+"_"+invoice_code+"_no").text()));
		}
		
		/*获取该次分发的起始No*/
		function getFistPreDistributeBeginNo(stock_id,invoice_code){
			$ori_code_begin_no=getStoreInvoiceCodeBeginNo(stock_id,invoice_code);
			return $ori_code_begin_no+getStoreDistributeNum(stock_id,invoice_code);
		}
		
		function createInstSelect($idx){
			$postData={}
			ajax_post("<c:out value='${webapp}'/>/getInstList.action",$postData,function(data){
				json=eval('('+data+')');
				if(json.msg=='ok'){
					var selectHtml="<select name='receive_inst_id_"+$idx+"'  id='receive_inst_id_"+$idx+"' class='inst_class'>";
					selectHtml+="<option value=''>请选择领用机构</option>"
					$.each(json.lstOrg,function(idx,item){
		                	selectHtml += "<option value=\"" + item.value + "\">" + decodeURI (item.name) + "</option>";
					});
					selectHtml+="</select>"
					//alert($("#"+name+"_tb").html());
					$("#receive_inst_id_"+$idx+"_td").html(selectHtml);
					//return 	selectHtml;
			    }else{
			    		alert("获取领用机构列表失败！");
			    		return;
			    }
			});
		}
	Number.prototype.Left3=function(){
	  if(this.toString().length>=8)
		  return this.toString().substr(0,8);
	  else{
	    var str=this.toString();
	    var a=8-str.length;
	    for(var i=0;i<a;i++){
	    	str="0"+str;
	    	}
	    //alert(str);
	    	return str;
	  	}
	}
		$(".sd_item").mouseover(function(){
			  $(this).css("background-color","#00ffca");
		}).mouseleave(function(){
			 $(this).css("background-color","#FFF");
		});
		
		
		$seq_add_idx=0;
		$(".sd_item").live("dblclick",function(){
			$store_id=$(this).attr("rel");
			//alert($store_id);
			$current_no=$(this).attr("aa");
			//alert($current_no);
			$invoice_code=trim($(this).find('.invoice_code').text());
			// $current_no=trim($(this).find('.current_no').val());
			//alert($current_no);
			if(cal_valid_invoice_count($store_id,$invoice_code)<=0){
				alert("发票编号["+$invoice_code+"]已经分发完毕，不能再次分发！");
				return;
			}
			$seq_add_idx+=1;
// 			$last_items=$(".last_"+$store_id+"_"+$invoice_code);
			//$select_inst_html=createInstSelect("receive_inst_id_"+$seq_add_idx);
			$tr_item_begin_no=$("#temp_end_"+$store_id+"_"+$invoice_code+"_no").val();
			//alert($tr_item_begin_no);
			var st=trim($("#ori_begin_"+$store_id+"_"+$invoice_code+"_no").text());
			//alert(st);
			//alert($tr_item_begin_no);
			if($tr_item_begin_no==''){
				$tr_item_begin_no=getFistPreDistributeBeginNo($store_id,$invoice_code);
				$tr_item_begin_no=$tr_item_begin_no.Left3();
			}else{
				$tr_item_begin_no=parseInt($tr_item_begin_no)+1;
				$tr_item_begin_no=$tr_item_begin_no.Left3();
			}
			$current_no=trim($(this).find('.current_no').text());
			//alert($current_no);
			//$input_currentNo=parseInt(trim($("#invoice_current_"+stock_id+"_"+current_no+"_no").val()))
			$tr_html='<tr style="background-color:#fff" class="itr_'+$store_id+'_'+$invoice_code+' input_tr">';
			$tr_html+='<td style="text-align: center">'+$seq_add_idx+'<input type="hidden" name="paper_invoice_stock_id_'+$seq_add_idx+'" value="'+$store_id+'"/></td>';
			$tr_html+='<td style="text-align: center" id="receive_inst_id_'+$seq_add_idx+'_td">请选择领用机构</td>';
// 			$tr_html+='<td style="text-align: center"  id="receive_inst_id_1_td">hahahah</td>';
			$tr_html+='<td style="text-align: center"><input type="text" name="receive_user_id_'+$seq_add_idx+'" class="user_class"  value=""/></td>';
			$tr_html+='<td style="text-align: center">'+$invoice_code+'<input type="hidden" name="page_current_num_'+$seq_add_idx+'" value="'+trim($(this).find('.current_no').text())+'"/><input type="hidden" name="invoice_code_'+$seq_add_idx+'" value="'+trim($(this).find('.invoice_code').text())+'"/></td>';
// 			$tr_html+='<td style="text-align: center" class="item_begin_no">'+$(this).find('.invoice_begin_no').text()+'</td>';
			$tr_html+='<td style="text-align: center" class="item_begin_no">'+$tr_item_begin_no+'</td>';
// 			$tr_html+='<td style="text-align: center" class="item_end_no"></td>';
			$tr_html+='<td style="text-align: center" class="item_end_no">'+$(this).find('.invoice_end_no').text()+'</td>';
			$tr_html+='<td style="text-align: center" ><input type="text" name="page_used_invoice_num_'+$seq_add_idx+'" value="'+$validate_invoice_count+'" class="page_used_invoice_num" sid="'+$store_id+'" icode="'+$invoice_code+'" style="width:50px;"/></td>';
			$tr_html+='</tr>';
			$("#distribute_table").append($tr_html);
			createInstSelect($seq_add_idx);
// 			for($lldx=0;$lldx<$last_items.size();$lldx++){
// 				$($last_items[$lldx]).removeClass("last_"+$store_id+"_"+$invoice_code);
// 			}
			
			$("#distribute_max_size").val($seq_add_idx);
			$(".page_used_invoice_num").change(function(){
				if(!isInteger($(this).val())){
					alert("张数必须是数字!");
					return;
				}
				if(parseInt($(this).val())<=0){
					alert("张数必须大于0!");
					return;
				}
				$valid_invoice_count=cal_valid_invoice_count($store_id,$invoice_code);
				if($valid_invoice_count<=0){
					alert("发票编号["+$invoice_code+"]已经分发完毕，不能再次分发！");
					return;
					$(this).val($valid_invoice_count);
				}
				$change_store_id=$(this).attr("sid");
				$change_invoice_code=$(this).attr("icode");
				$change_tr_items=$(".itr_"+$change_store_id+"_"+$change_invoice_code);
				for($ci=0; $ci < $change_tr_items.size(); $ci++){
					$tr_item=$($change_tr_items[$ci]);
					$item_cur_num=parseInt($tr_item.find(".page_used_invoice_num").val());
					$begin_change_code_no=0;
					$end_change_code_no=0;
					var a=0;
					if($ci==0){
						$begin_change_code_no=getFistPreDistributeBeginNo($change_store_id,$change_invoice_code);
						a=$begin_change_code_no;
						$begin_change_code_no=$begin_change_code_no.Left3();
					}else{
						$begin_change_code_no=parseInt($("#temp_end_"+$change_store_id+"_"+$change_invoice_code+"_no").val())+1;
						a=$begin_change_code_no;
						$begin_change_code_no=$begin_change_code_no.Left3();
					}
					$end_change_code_no=(a+$item_cur_num-1).Left3();
					//$end_change_code_no=$end_change_code_no.Left3();
					$tr_item.find(".item_begin_no").html($begin_change_code_no);
					$tr_item.find(".item_end_no").html($end_change_code_no);
					$("#temp_end_"+$change_store_id+"_"+$change_invoice_code+"_no").val($end_change_code_no);
				}
				
				print_invoice_count();
			});
			print_invoice_count();
		});
		
		print_invoice_count();
	});
	
	function cal_valid_invoice_count($store_id,$invoice_code){
			$total_invoice_count=parseInt(trim($(".total_"+$store_id+'_'+$invoice_code).text()));
			$distribute_invoice_count=parseInt($(".distribute_"+$store_id+'_'+$invoice_code).val());
			$itr_invoice_count=0;
			$itr_items=$('.itr_'+$store_id+'_'+$invoice_code);
			for($i=0;$i<$itr_items.size();$i++){
				$itr_item_count=parseInt($($itr_items[$i]).find(".page_used_invoice_num").val());
				$itr_invoice_count=$itr_invoice_count+$itr_item_count;
			}
			$validate_invoice_count=$total_invoice_count-$distribute_invoice_count-$itr_invoice_count;
			//if($validate_invoice_count<0){
			//	return false;
			//}
			return $validate_invoice_count;
	}
	
	function print_invoice_count(){
		$total_store_invoice_count=total_invoice_num();
		$total_used_invoice_count=used_invoice_num();
		$("#total_td_invoice_show").html($total_store_invoice_count);
		$("#total_td_unused_invoice_show").html($total_store_invoice_count-$total_used_invoice_count);
	}
	function total_invoice_num(){
		$total_invoice_num=0;
		$item_invoices=$(".ditem_invoice_num");
		if($item_invoices.size()==0){
			return 0;
		}
		for($idx=0;$idx<$item_invoices.size();$idx++){
			$item_invoice_num=$($item_invoices[$idx]).val();
			$total_invoice_num=$total_invoice_num+parseInt($item_invoice_num);
		}
		return $total_invoice_num;
	}
	
	function used_invoice_num(){
		$total_used_invoice_num=0;
		$item_used_invoices=$(".ditem_used_num");
		if($item_used_invoices.size()==0){
			return 0;
		}
		
		$item_page_used_invoices=$(".page_used_invoice_num");
		$total_page_used_invoice_num=0;
		if($item_page_used_invoices.size()>0){
			for($idx=0;$idx<$item_page_used_invoices.size();$idx++){
				$item_page_used_invoice_num=$($item_page_used_invoices[$idx]).val();
				$total_page_used_invoice_num=$total_page_used_invoice_num+parseInt($item_page_used_invoice_num);
			}
		}
		for($idx=0;$idx<$item_used_invoices.size();$idx++){
			$item_used_invoice_num=$($item_used_invoices[$idx]).val();
			$total_used_invoice_num=$total_used_invoice_num+parseInt($item_used_invoice_num);
		}
		
		
		
		return $total_used_invoice_num+$total_page_used_invoice_num;
	}
	
	function isInteger(strInteger){
		 strInteger=strInteger.replace(/^(\s)*|(\s)*$/g,"");//去掉字符串两边的空格
		//验证规则：整数
		var newPar=/^(-|\+)?\d+$/;
		if(strInteger.length>0 && newPar.test(strInteger)==false)	{
		   return false;	
		 } else{
		    return true;
		 }
	}
	function ajax_post(the_url,the_param,succ_callback){
		$.ajax({
			type:'POST',
			url:the_url,
			data:the_param,
			success:succ_callback,
			error:function(html){
				alert("提交数据失败，代码:" +html.status+ "，请稍候再试");
			}
		});
	}
	
	$(function(){
		
		<%
			String retry=request.getParameter("retry");
			if(org.apache.commons.lang.StringUtils.isNotEmpty(retry)){ 
		%>
			alert("分发失败，请您重新再试！");
		<%
			}
		%>
	});
</script>
</head>
<body scroll="no" style="overflow:hidden;">
<div class="showBoxDiv">
<form id="frm" action="<c:out value='${webapp}'/>/doDistrubute.action" method="post" >

<div id="editpanel">
<div id="editsubpanel" class="editsubpanel">
<div style="overflow: auto; width: 100%;" >  
<div class="windowtitle" style="background:#004C7E; height:45px; line-height:45px; text-align:center; color:#FFF; font-size:14px; font-weight:bold;">分发发票</div>
<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
	<tr>
		<td align="center" style="background:#FFF;">
		<div id="lessGridList20" style="overflow: auto; width: 100%;">
		<table class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0"  style="border-collapse: collapse;width: 100%;" >
			<tr class="lessGrid head">
				<th width="15%" style="text-align: center">序号</th> 
				<th width="25%" style="text-align: center">发票代码</th>
				<th width="25%" style="text-align: center">发票起始号码</th>
				<th width="25%" style="text-align: center">发票终止号码</th>
				<th width="25%" style="text-align: center">当前发票号码</th>
				<th width="15%" style="text-align: center">张数</th> 
			</tr>
			<s:iterator value="lstInvoiceStoreDetail" id="dItem" status="stuts">
			<tr class="sd_item unSelectBox" rel="<s:property value='#dItem.paperInvoiceStockId' />" aa="<s:property value='#dItem.currentbillNo' />">
				<input type="hidden" id="temp_end_<s:property value='#dItem.paperInvoiceStockId' />_<s:property value='#dItem.invoiceCode'/>_no" value=""/>
				<input type="hidden" class="ditem_invoice_num" id="invoiceNum" value="<s:property value='#dItem.invoiceNum' />"/>
				<input type="hidden" id="ori_distribute_<s:property value='#dItem.paperInvoiceStockId' />_<s:property value='#dItem.invoiceCode'/>_num" class="ditem_used_num  distribute_<s:property value='#dItem.paperInvoiceStockId' />_<s:property value='#dItem.invoiceCode' />"  id="invoiceNum" value="<s:property value='#dItem.hasDistributeNum' />"/>
				<td width="15%" style="text-align: center">
				<s:property value='#stuts.count' />
				</td><%-- 
								<input type="text" class="current_no" id="invoice_current_<s:property value='#dItem.paperInvoiceStockId' />_<s:property value='#dItem.currentbillNo' />_no" value="<s:property value='#dItem.currentbillNo' />"/>
				
				--%><td width="25%" style="text-align: center" class="invoice_code">
				<s:property value='#dItem.invoiceCode' />
				</td> 
				<td width="25%" style="text-align: center" class="invoice_begin_no" id="ori_begin_<s:property value='#dItem.paperInvoiceStockId' />_<s:property value='#dItem.invoiceCode'/>_no">
				<s:property value='#dItem.invoiceBeginNo' />
				</td>
				<td width="25%" style="text-align: center" class="invoice_end_no" id="ori_end_<s:property value='#dItem.paperInvoiceStockId' />_<s:property value='#dItem.invoiceCode'/>_no">
				<s:property value='#dItem.invoiceEndNo' />
				</td>
				<td width="25%" style="text-align: center" class="current_no" id="ori_current_no_<s:property value='#dItem.paperInvoiceStockId' />_<s:property value='#dItem.currentbillNo' />_no">
				<s:property value='#dItem.currentbillNo' />
				</td>
				<td width="15%" style="text-align: center" class="total_<s:property value='#dItem.paperInvoiceStockId' />_<s:property value='#dItem.invoiceCode' />" >
				<s:property value='#dItem.invoiceNum' />
				</td>
			</tr>
			</s:iterator>
		</table>
		</div>
		<table id="tbl_query" cellpadding="0" cellspacing="0" border="0"   width="100%" align="center">
			<tr>
				<td style="text-align:right;width:100px;">
					总张数:
				</td>
				<td id="total_td_invoice_show">
				</td>
			</tr>
			<tr>
				<td  style="text-align:right;">
					剩余张数:
				</td>
				<td id="total_td_unused_invoice_show">
				</td>
			</tr>
		</table>
		<%--
		<table id="tbl_query" cellpadding="1" cellspacing="0">
			<tr align="left">
				<td style="text-align:left">
				<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" 
				onMouseOut="this.className='tbl_query_button'" onclick="submitForm('uploadData.action');" name="cmdFilter" value="新增" id="cmdFilter" />				
				</td>
				<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td style="text-align:left">
				<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" 
				onMouseOut="this.className='tbl_query_button'" onclick="submitForm('<c:out value='${webapp}'/>/initDistrubute.action');" name="cmdFilter" value="分发" id="cmdFilter" />				
				</td>
				<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td style="text-align:left">
				<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 
				onclick="beforeDelete('<c:out value="${webapp}"/>/deleteTransBatch.action','batchIdList',600,410,true,'delete')" name="cmdFilter" value="空白发票作废" id="cmdFilter" />				
				</td>
	         </tr>
		</table>
		--%>
<div id="lessGridList" style="overflow: auto; width: 100%;">
<table  class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0"  style="border-collapse: collapse;width: 100%;" id="distribute_table">
	<input type="hidden" name="distribute_max_size" id="distribute_max_size" value="0"/>
	<tr class="lessGrid head" style="background: #1ca3ca;" width="100%">
		<th style="text-align: center">序号</th>
		<th style="text-align: center">领用机构</th>
		<th style="text-align: center">领取人</th>
		<th style="text-align: center">发票代码</th>
		<th style="text-align: center">发票起始号码</th>
		<th style="text-align: center">发票终止号码</th>
		<th style="text-align: center" width="100px;">张数</th>
	</tr>
	<%-- 
	<s:iterator value="paginationList.recordList" id="iList" status="stuts">
		<tr align="center"class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
		    <td><input type="checkbox" style="width:13px;height:13px;" name="batchIdList" value="<s:property value="#iList.batchId"/>" 
		         <s:if test='status=="1"'>abled</s:if><s:else>disabled</s:else>="true"/></td>
			<td align="center" style="<s:if test='status=="1"'>background-color:#FFFF37;</s:if><s:else></s:else>"><s:property value='#stuts.count' /></td>		
			<td style="<s:if test='status=="1"'>background-color:#FFFF37;</s:if><s:else></s:else>"><s:property value='impInst' /></td>
			<td style="<s:if test='status=="1"'>background-color:#FFFF37;</s:if><s:else></s:else>"><s:date name="impTime" format="yyyy-MM-dd hh:mm:ss" /></td>
			<td style="<s:if test='status=="1"'>background-color:#FFFF37;</s:if><s:else></s:else>"><s:property value='impUser'/></td>
			<td style="<s:if test='status=="1"'>background-color:#FFFF37;</s:if><s:else></s:else>">
			    <s:if test='status=="1"'>失败</s:if><s:else>通过</s:else></td>
			<td style="<s:if test='status=="1"'>background-color:#FFFF37;</s:if><s:else></s:else>"><s:property value='impUser'/></td>
			<td style="<s:if test='status=="1"'>background-color:#FFFF37;</s:if><s:else></s:else>"><s:property value='impUser'/></td>
			<td style="<s:if test='status=="1"'>background-color:#FFFF37;</s:if><s:else></s:else>"><s:property value='impUser'/></td>
			<td style="<s:if test='status=="1"'>background-color:#FFFF37;</s:if><s:else></s:else>"><s:property value='impUser'/></td>
			<td style="<s:if test='status=="1"'>background-color:#FFFF37;</s:if><s:else></s:else>"><s:property value='impUser'/></td>
			<td style="<s:if test='status=="1"'>background-color:#FFFF37;</s:if><s:else></s:else>"><s:property value='impUser'/></td>
			<td style="<s:if test='status=="1"'>background-color:#FFFF37;</s:if><s:else></s:else>"><s:property value='impUser'/></td>
			<td style="display: none"><s:property value='batchId'/></td>
		</tr>
		</s:iterator> 
		--%>
</table>
<%-- 
<table width="100%" cellspacing="0" border="0">
     <tr>           
         <td align="left">
         <input type="submit" style="width:80px;" value="保存"/>
         <input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="submitForm('<c:out value='${webapp}'/>/doDistrubute.action')" name="BtnSave" value="保存" id="BtnSave"/>
         </td>
     </tr>
 </table>
 --%>
 	<div id="ctrlbutton" class="ctrlbutton" style="border:0px;margin-top:30px;">	
 	<input type="button" style="display:none;"/>	
	<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="submitForm('<c:out value='${webapp}'/>/doDistrubute.action')"  value="保存"/>
	<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="window.close()" value="关闭" />	
	</div>
</div>

	
<%-- 
<div id="anpBoud" align="Right" style="width:100%;">
        <table width="100%" cellspacing="0" border="0">
            <tr>           
                <td align="right"><s:component template="pagediv"/></td>
            </tr>
        </table>
</div>
--%>
		</td>
	</tr>
</table>
</div>
</div>
</div>
</form>
</div>
</body>
<script type="text/javascript">
	document.getElementById("lessGridList").style.height = screen.availHeight - 375;
</script>
</html>