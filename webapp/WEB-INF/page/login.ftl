<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8"/>
<title>数据抓取系统</title>
<link rel="shortcut icon" type="image/x-icon" href="/resources/image/favicon.ico" />
<link href="/resources/css/login/login.css" rel="stylesheet"/>
<script>
if(window.parent.length>0){
	window.parent.location = "/";
}
</script>
</head>
<body>
<div class="login">
	<div class="menus">
    	<div class="public"></div>
    </div>
    <div class="box png">
        <form action="/login" method="post" id="loginForm">
        <div class="header">
            <h2 class="logo png"></h2>
            <span class="alt"></span>
        </div>
        <ul>
            <li><label>帐   号</label><input class="text" type="text" name="account" size="30" id="username" maxlength="20" onkeydown="if(event.keyCode==32) return false; if(event.keyCode==13) return checkForm();" value="<#if user_account?exists>${user_account}</#if>"</li>
            <li><label>密   码</label><input class="text" type="password" name="password" id="userpass" size="30" maxlength="20" onkeydown="if(event.keyCode==32) return false; if(event.keyCode==13) return checkForm();" value="<#if user_password?exists>${user_password}</#if>"/></li>
            <li><label>验证码</label><input class="text" style="width:80px;" type="text" name="randomCode" id="randomCode" onkeydown="if(event.keyCode==32) return false; if(event.keyCode==13) return checkForm();" value=""><img id="CheckPic" title="看不清，换一张" style="cursor:pointer" src="/captchaImage" align="absmiddle" onclick="document.getElementById('CheckPic').src='/captchaImage?'+Math.random()"></li> 
            <li class="submits"><input class="submit" type="button" value="登录" onclick="return checkForm();"/><div id="lblerr"><#if msg?exists>${msg}</#if></div></li>
        </ul>
        <div class="copyright"><@fm.copyright date="${.now?string('yyyy')}" /></div>
        </form>
    </div>
    <div class="air-balloon ab-1 png"></div><div class="air-balloon ab-2 png"></div>
    <div class="footer"></div>
</div>
<script src="/resources/js/jquery-1.8.0.min.js"></script>
<script src="/resources/js/login/fun.base.js"></script>
<script src="/resources/js/login/login.js"></script>
<script>
$(function(){
	$("input.text").each(function(index, ele){
		if ($(ele).val() == '') {
			this.focus();
			return false;
		}
	});
    var msg = "${hasLogin?exists}";
	if(msg!=""){
		if(msg=="hasLogin"){
			alert("该账号已在其他地方登陆！");
			return;
		}
	}
});
function checkForm() {
	var uname = $("#username").val();
	if(uname == ""){
		$("#lblerr").text("帐号不能为空！");
		$("#username").focus();
		return false;
	}
	var passwd = $("#userpass").val();
	if(passwd == ""){
		$("#lblerr").text("密码不能为空！");
		$("#userpass").focus();
		return false;
	}
	var randomCode = $("#randomCode").val();
	if(randomCode == ""){
		$("#lblerr").text("验证码不能为空！");
		$("#randomCode").focus();
		return false;
	}
	$("#loginForm").submit();
	return true;
}
</script>
<!--[if lt IE 8]>
<script src="/resources/js/login/PIE.js" type="text/javascript"></script>
<script type="text/javascript">
$(function(){
    if (window.PIE && ( $.browser.version >= 6 && $.browser.version < 10 )){
        $('input.text,input.submit').each(function(){
            PIE.attach(this);
        });
    }
});
</script>
<![endif]-->
<!--[if IE 6]>
<script src="/resources/js/login/DD_belatedPNG.js" type="text/javascript"></script>
<script>DD_belatedPNG.fix('.png')</script>
<![endif]-->
</body>
</html>