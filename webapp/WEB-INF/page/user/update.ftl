<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset="utf-8">
<title>帐号管理 | 编辑帐号</title>
<@fm.header />
<style>
tr.hide
{display:none;}
</style>
</head>
<body>
<div class="easyui-layout" style="text-align: center; height: 270px;" fit="true">
	<div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
	<form id="ff" method="post">
	    <table>
	        <tr>
	            <td><label for="userName">帐号:</label></td>
	            <td><input class="easyui-validatebox" type="text" name="userName" value="${user.userName}" data-options="required:true,validType:'length[1,25]'"></input></td>
	        </tr>
	        <tr>
	            <td><label for="nickname">昵称:</label></td>
	            <td><input class="easyui-validatebox" type="text" name="nickname" value="${user.nickname}" data-options="required:true,validType:'length[1,25]'"></input></td>
	        </tr>
	        <tr>
	            <td><label for="password">密码:</label></td>
	            <td><input class="easyui-validatebox" type="text" name="password" data-options="validType:'length[1,20]'"></input></td>
	        </tr>
	        <tr>
	            <td><label for="roleId">所属角色:</label></td>
	            <td><input id="cc" class="easyui-combobox" style="width: 155px;" name="roleId" formatter="formatterRoleId"  data-options="
						url:'/role/content?defaultValues=${user.roleNames}',
						method:'get',
						valueField:'roleId',
						textField:'roleName',
						multiple:true,
						panelHeight:'auto' " /></td>
	       </tr>
	       <tr class="hide" id="channel">
	            <td><label for="cbId">所属渠道商:</label></td>
	            <td><input id="cc" class="easyui-combobox" style="width: 155px;" name="cbId"  data-options="
						url:'/channelBusiness/content?defaultValue=${user.business.company}',
						mode:'remote',
						method:'get',
						valueField:'cbId',
						textField:'company',
						editable:true,
						panelHeight:'auto' " /></td>
	        </tr>
	        <tr>
	            <td><label for="status">状态:</label></td>
	            <td>
	                <select class="easyui-combobox" name="status" panelHeight="auto" style="width:155px" data-options="required:true">
		                <option value="1" <#if user.status=='1'>selected</#if>>启用</option>
	                	<option value="0" <#if user.status=='0'>selected</#if>>禁用</option>
		            </select>
	            </td>
	        </tr>
	        <tr>
	            <td><label for="description">备注:</label></td>
	            <td><textarea class="easyui-validatebox" name="description" style="width:200px;height:40px;" data-options="validType:'length[0,200]'">${user.description}</textarea></td>
	        </tr>
	    </table>
	</form>
	</div>
	<div region="south" border="false" style="text-align: center; padding: 5px 5px 5px 0;">
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm('/user/${user.userId}/update')">提交</a>
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm()">重置</a>
	</div>
</div>
<script>
$(function(){
	<#if user.cbId gt 0>
		$("#channel").removeClass("hide");
	</#if>
});

function formatterRoleId(value){
	value = $('#cc').combobox('getValues');
	return value;
}
</script>
</body>
</html>