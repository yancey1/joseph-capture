<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset="utf-8">
<title>帐号管理 | 修改密码</title>
<@fm.header />
</head>
<body>
<div class="easyui-layout" style="text-align: center; height: 270px;" fit="true">
	<div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
	<form id="ff" method="post">
	    <table>
	        <tr>
	            <td><label for="pwd">原密码:</label></td>
	            <td><input class="easyui-validatebox" type="password" name="pwd" value="" data-options="required:true,validType:'length[1,25]'"></td>
	        </tr>
	        <tr>
	            <td><label for="npwd">新密码:</label></td>
	            <td><input class="easyui-validatebox" type="password" id="npwd" name="npwd" value="" data-options="required:true,validType:'length[1,25]'"></td>
	        </tr>
	        <tr>
	            <td><label for="rpwd">新密码确认:</label></td>
	            <td><input class="easyui-validatebox" type="password" name="rpwd" required="required" validType="equals['#npwd', '两次密码不匹配！']"></td>
	        </tr>
	    </table>
	</form>
	</div>
	<div region="south" border="false" style="text-align: center; padding: 5px 5px 5px 0;">
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm('/user/resetPwd')">提交</a>
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm()">重置</a>
	</div>
</div>
</body>
</html>