<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="include.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN" >
<HTML>
	<HEAD>
		<title>外汇管理局国际收支数据申报系统 - 登录</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<LINK href="${bopTheme}/css/login.css" type="text/css"
			rel="stylesheet">
		<script type="text/javascript">
		function clearUser(){
			document.getElementById("username").value="";
			document.getElementById("password").value="";
		}
		</script>
	</HEAD>
	<body bottomMargin="0" leftMargin="0" topMargin="0" rightMargin="0">
	<form action="check.action" method="post" onSubmit="return Validator.Validate(this,3)">
		<table height="100%" cellSpacing="0" cellPadding="0" width="100%"
			border="0">
			<tr>
				<td align="center" valign="middle">
					<table border="0" cellpadding="0" cellspacing="0" class="login">
						<tr>
							<td valign="bottom">
							
								<table width="100%" border="0" cellspacing="0" cellpadding="0"
									class="loginbox">
									<tr>
									<td height="15"><FONT face="宋体">&nbsp;&nbsp;<s:component template="rocketMessage"/></FONT></td>
									
									</tr>
									<tr>
										<td align="center">
											用户名：
                                            <span>
											<input name="user.username" type="text" id="username"
												class="input_text"
												onKeyDown="if(event.keyCode==13) event.keyCode=9" size="14" dataType="Require" msg="请输入用户名。"  /></span>
											&nbsp;密&nbsp;&nbsp;码：
                                             <span>
											<input name="user.password" type="password" id="password"
												class="input_text" size="14" dataType="Require" msg="请输入密码。"  />
                                                </span>
											<input type="submit" name="BtnLogin" value="登&nbsp;录" id="BtnLogin"
												class="input_button" />
											<input type="button" name="BtnClear" value="重&nbsp;置" id="BtnClear"
												class="input_button" onClick="clearUser()" />
										</td>
									</tr>
									<tr>
										<td align="center" class="copyright">
											版权所有：上海华颉信息技术有限公司
										</td>
									</tr>									
								</table>								
								<table width="100%" border="0" cellspacing="0" cellpadding="0"
									class="copyright">
									<tr>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		</form>
	</body>
</HTML>
