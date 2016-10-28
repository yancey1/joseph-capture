<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset="utf-8">
<title>角色管理 | 新增角色</title>
<@fm.header />
</head>
<body>
<div class="easyui-layout" style="text-align: center; height:200px;" fit="true">
	<div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
	<form id="ff" method="post" >
	    <table style=" border-spacing:10px; ">
	        <tr>
	            <td><label for="roleName">角色名称:</label></td>
	            <td><input class="easyui-validatebox" type="text" name="roleName" data-options="required:true,validType:'length[2,25]'"></input></td>
	        </tr>
	        <tr>
	            <td><label for="roles">角色代码:</label></td>
	            <td><input class="easyui-validatebox" type="text" name="roles" data-options="validType:'length[2,25]'"></input></td>
	        </tr>
	        <tr>
	            <td><label for="description">角色描述:</label></td>
	            <td><textarea class="easyui-validatebox" type="text" name="description" style="width:200px;height:40px;" data-options="validType:'length[2,25]'"></textarea></td>
	        </tr>
	    </table>
	</form>
	</div>
	<div region="south" border="false" style="text-align: center; padding: 5px 5px 5px 0;">
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm('/role/add');">提交</a>
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm();">重置</a>
	</div>
</div>
<script>
$(function(){
	$("input").bind("keyup afterpaste", function(){
		$(this).val($(this).val().replace(/\s/g,''));
	});
});
</script>
</body>
</html>