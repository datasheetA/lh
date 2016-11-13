<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/common/meta.jsp"%>
<title>用户登录</title>
<style type="text/css">
	.body1 {font-family: "宋体", "Verdana";font-size: 12px;line-height:160%;background-color:#ffffff;}
	.login_top_bg { background-image: url(${pageContext.request.contextPath}/images/login_top_bg.gif);}
	.login_id_bg {background-image: url(${pageContext.request.contextPath}/images/login_form_bg.jpg); background-repeat: no-repeat;}
</style>
<script language="javascript">
<!--
	function trim(obj){
	  return obj.replace(/^\s+/gi,"").replace(/\s+$/gi,"");
	}
	
	function doSubmit(){
	  if (trim(document.myForm.userAccount.value) == ''){
	    alert('用户账号必须填写！');
	    document.myForm.userAccount.focus();
	    return false;
	  }
	  
	  if (trim(document.myForm.passwd.value) == ''){
	    alert('密码必须填写！');
	    document.myForm.passwd.focus();
	    return false;
	  }
	  
	  if (trim(document.myForm.validcode.value) == ''){
	    alert('验证码必须填写！');
	    document.myForm.validcode.focus();
	    return false;
	  }
	
	  document.myForm.operFlag.value = 'login';//这设置的是下面一个输入框中的值
	  document.myForm.submit();
	  return true;
	}
	
	function refreshImage(img){
	    img.src = img.src + '?' + Math.random();

	}
//-->
</script>
</head>

<body style="margin:0" class="body1" onload="document.myForm.userAccount.focus();">
<p>&nbsp;</p>
<p>&nbsp;</p>
<table border="0" align="center" cellpadding="0" cellspacing="0" bordercolor="0">
  <tr>
    <td width="519" height="354" align="center" valign="top">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td height="44" class="login_top_bg">&nbsp;</td>
        </tr>
        <tr> 
          <td>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr> 
                <td>
                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr> 
                      <td height="315" align="center" valign="top" class="login_id_bg"> 
                        <table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
                          <tr>
                            <td width="42%" height="40">&nbsp;</td>
                            <td width="58%">&nbsp;</td>
                          </tr>
                          <tr>
                            <td height="154">&nbsp;</td>
                            <td valign="top">
                              <table border="0" cellspacing="0" cellpadding="0">
                                <tr>
                                  <td>
                                    <form id="myForm" name="myForm" method="post" action="${pageContext.request.contextPath}/login.do">
                                      <input type="hidden" id="operFlag" name="operFlag" value=""/>
                                      <table border="0" cellspacing="10" cellpadding="0">
                                        <tr>
                                          <td align="left"><font color="4E4E4E"><strong>用户账号：</strong></font></td>
                                          <td align="left">
                                            <input type="text" id="userAccount" name="userAccount" maxLength="30" size="20" />
                                          </td>
                                        </tr>
                                        <tr>
                                          <td align="left"><font color="4E4E4E"><strong>密 码 ：</strong></font></td>
                                          <td align="left">
                                            <input type="password" id="passwd" name="passwd" maxLength="20" size="20" />
                                          </td>
                                        </tr>
                                        <tr>
                                          <td align="left"><font color="4E4E4E"><strong>验证码：</strong></font></td>
                                          <td align="left" valign="middle">
                                            <input type="text" id="validcode" name="validcode" maxLength="4" minLength="4" size="4"  onKeyPress="if(event.keyCode==13){doSubmit();}"/>
                                            <img align="absmiddle" border="0" src="${pageContext.request.contextPath}/common/ValidateGenerate.jsp" onClick="refreshImage(this);" style="cursor:hand;"/>
                                          </td>
                                        </tr>
                                        <tr>
                                          <td align="center">&nbsp;</td>
                                          <td align="left"><img src="${pageContext.request.contextPath}/images/login_ok.gif" width="71" height="28" border="0" onClick="doSubmit();" style="cursor:hand"/></td>
                                        </tr>
                                      </table>
                                    </form>
                                  </td>
                                </tr>
                              </table>
                            </td>
                          </tr>
                          <tr>
                            <td height="126">&nbsp;</td>
                            <td align="left" valign="middle">
                              <table width="80%" border="0" cellspacing="0" cellpadding="0">
                                <tr>
                                  <td align="center">
                                    <font color="4E4E4E">鏉庢亽 版权所有<br>
                                    Copyright &copy; 2013 - 2099 鏉庢亽 </font>
                                  </td>
                                </tr>
                              </table>
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </table>
                </td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
</body>
</html>

<script language="javascript">

<!--
if ("${message}" != ""){
  alert("${message}");

}
//-->
</script>