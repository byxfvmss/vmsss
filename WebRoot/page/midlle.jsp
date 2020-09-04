<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="include.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
	<head>
		<title>伸缩栏</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<script>
	function bar_onclick(flag) {
            var mfp = window.parent.frames["mainFramePage"];
            var ifrm = parent.document.getElementById("frame");
            if (flag == true) {
                mfp.missWidth();
            } else {
                // 修改兼容性问题 2009-08-03 13:13 ShiCH
                if (ifrm.cols == "230,10,*") {
                    ifrm.cols = "0,10,*";
                    document.getElementById("ImgArrow").src = "<c:out value="${webapp}"/>/themes/images/icons/right.png";
                    if (mfp.mess && typeof (mfp.mess) === "function") {
                        mfp.mess(1);
                    }
                } else {
                    ifrm.cols = "230,10,*"
                    document.getElementById("ImgArrow").src = "<c:out value="${webapp}"/>/themes/images/icons/left.png";
                    if (mfp.mess && typeof (mfp.mess) === "function") {
                        mfp.mess(2);
                    }
                }
            }
        }
	</script>
	</head>
	<BODY bgColor="#cee5ff" leftMargin="0" topMargin="0">
		<TABLE cellSpacing="0" cellPadding="0" ID="Table1" onClick="bar_onclick(false)" style="height:100%; width:0px; border:0px; CURSOR:hand">
			<TR>
				<TD width="5" height="122">
					<IMG id="ImgArrow" src="&{bopTheme}/img/al.gif" border="0" name="ImgArrow">
				</TD>
			</TR>
		</TABLE>
	</BODY>
</html>
