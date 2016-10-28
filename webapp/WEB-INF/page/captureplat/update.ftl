<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset="utf-8">
<title>基本信息管理 | 编辑</title>
<@fm.header />
</head>
<body>
<div class="easyui-layout" style="text-align: center; height: 270px;" fit="true">
	<div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
	<form id="ff" method="post">
	    <table>
	        <tr>
	            <td><label for="roleName">用户名称:</label></td>
	            <td><input class="easyui-validatebox" type="text" value="${capturePlat.userName}" name="userName" data-options="validType:'length[2,100]'" /></td>
	        </tr>
	        <tr>
	            <td><label for="roles">密码:</label></td>
	            <td><input class="easyui-validatebox" type="text" value="${capturePlat.password}" name="password" data-options="validType:'length[2,20]'" /></td>
	        </tr>
	        <tr>
	            <td><label for="productName">产品名称:</label></td>
	            <td><input class="easyui-validatebox" type="text" value="${capturePlat.productName}" name="productName" data-options="validType:'length[2,100]'" /></td>
	        </tr>
	        <tr>
	            <td><label for="roles">渠道号:</label></td>
	            <td><input class="easyui-validatebox" type="text" value="${capturePlat.channelCode}" name="channelCode" data-options="validType:'length[2,20]'" /></td>
	        </tr>
	        <tr>
	            <td><label for="description">公司:</label></td>
	            <td><input class="easyui-validatebox" type="text" value="${capturePlat.company}" name="company" data-options="validType:'length[2,100]'" /></td>
	        </tr>
	        <tr>
	            <td><label for="roles">登录地址:</label></td>
	            <td><input class="easyui-validatebox" type="text" value="${capturePlat.loginUrl}" style="width:350px;" name="loginUrl" data-options="validType:'length[2,200]'" /></td>
	        </tr>
	        <tr>
	            <td><label for="roles">验证码地址:</label></td>
	            <td><input class="easyui-validatebox" type="text" value="${capturePlat.loginImgUrl}"  style="width:350px;"name="loginImgUrl" data-options="validType:'length[2,200]'" /></td>
	        </tr>
	        <tr>
	            <td><label for="roles">查询页面地址:</label></td>
	            <td><input class="easyui-validatebox" type="text" value="${capturePlat.queryPageUrl}" style="width:350px;" name="queryPageUrl" data-options="validType:'length[2,200]'" /></td>
	        </tr>
	        <tr>
	            <td><label for="roles">查询数据地址:</label></td>
	            <td><input class="easyui-validatebox" type="text" value="${capturePlat.queryUrl}" style="width:350px;" name="queryUrl" data-options="validType:'length[2,200]'" /></td>
	        </tr>
	    </table>
	</form>
	</div>
	<div region="south" border="false" style="text-align: center; padding: 5px 5px 5px 0;">
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm('/capturePlat/${capturePlat.id}/update')">提交</a>
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm()">重置</a>
	</div>
</div>
</body>
</html>