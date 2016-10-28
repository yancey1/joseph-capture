<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset="utf-8">
<title>基本信息管理 | 新增</title>
<@fm.header />
</head>
<body>
<div class="easyui-layout" style="text-align: center; height:200px;" fit="true">
	<div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
	<form id="ff" method="post" >
	    <table style=" border-spacing:10px; ">
	        <tr>
	            <td><label >账号:</label></td>
	            <td><input class="easyui-validatebox" style="width:200px" type="text" value="${account.name}" name="name" required=true  /></td>
	        </tr>
	        <tr>
	            <td><label >密码:</label></td>
	            <td><input class="easyui-validatebox" style="width:200px" type="text" value="${account.password}" name="password" required=true  /></td>
	        </tr>
	        <tr>
	            <td><label >备注:</label></td>
	            <td><textarea style="width:200px;" name="remark" rows="3" cols="3">${account.remark}</textarea></td>
	            <input type="hidden" name="id" value="${account.id}"/>
	        </tr>
	        
	    </table>
	</form>
	</div>
	<div region="south" border="false" style="text-align: center; padding: 5px 5px 5px 0;">
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm('/account/update');">提交</a>
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm();">重置</a>
	</div>
</div>
</body>
</html>