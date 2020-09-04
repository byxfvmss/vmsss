<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
	<head>
		<title>数据库配置</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=bopTheme%>/css/common.css" type="text/css" rel="stylesheet">
		<script type="text/javascript">
			function operateResult(operResult){
				if(operResult==0){
					return;
				}else if(operResult==1){
					alert("创建数据库成功！ 为使配置生效，请重新启动应用服务器！")
					window.close();
				
				}else if(operResult==2){
					alert("创建数据库失败！ 请检查数据库连接，重新启动应用服务器后重试！")
					window.close();
				}
			}
		</script>
	</head>
	<body onload="operateResult(<s:property value='operResult'/>">
		<form name="frm" action="restoreDb.action" method="post" onSubmit="return Validator.Validate(this,3)">
			<table class="location" id="Table1">
				<tr>
					<td>
						初始化数据库
					</td>
				</tr>
			</table>
			<s:component template="rocketMessage" />
			<table class="toolbar" id="Table2" width="100%" border="0">
				<tr>
					<td align="left">
						<input type="submit" name="BtnSave" value="创建数据库" id="BtnSave" class="input_button" />
					</td>
				</tr>
			</table>
			<div class="editblock">
				<table cellspacing="4" width="90%" align="center">
					<tr>
						<td width="10%" class="style1">
							数据库服务器IP地址<span style="color: #FF0000">*</span>
						</td>
						<td width="90%" class="style2">
							<input name="IP" type="text" value="<s:property value='IP'/>"  dataType="IP" msg="必填且需要符合IP地址格式！"  />
							&nbsp;
							
						</td>
					</tr>
					<tr>
						<td width="10%" class="style1">
							用户名
						</td>
						<td width="90%" class="style2">
							<input type="text" value="sa"  readonly />
						</td>
					</tr>
					<tr>
						<td class="style1">
							密码
						</td>
						<td class="style2">
							<input name="password" type="password" value="<s:property value='password'/>"/>
							&nbsp;
						</td>
					</tr>
					<tr>
						<td class="style1">
							<span style="color: #FF0000">提示：</span>
						</td>
						<td class="style2">
							<span style="color: #FF0000">1.数据库为本机，IP请输入127.0.0.1 </span>
						</td>
					</tr>
					<tr>
						<td class="style1">
						</td>
						<td class="style2">
							<span style="color: #FF0000">2.请关闭使用该数据库的其它应用程序 </span>
						</td>
					</tr>
				</table>
			</div>
		</form>
	</body>

</html>
