<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset="utf-8">
<title>角色管理 | 帐号列表</title>
<@fm.header />
<script>
function doSearch(){    
    $('#tt').datagrid('load', {    
        roleName: $('#role_name').val()    
    });
}
function add(){
    showMyWindow('角色管理 | 新增角色', '/role/add', 380,240);
}
function edit(){
	var row = $('#tt').datagrid('getSelected');
    if (row){
	    showMyWindow('角色管理 | 编辑角色权限', '/role/'+row.roleId+'/update', 400, 300);
    } else {
    	showMsg(market.content.title, market.content.tips, alert);
    }
}
function authority(){
	var row = $('#tt').datagrid('getSelected');
    if (row){
	    showMyWindow('角色管理 | 设置权限', '/role/'+row.roleId+'/authority',500, 550);
    } else {
    	showMsg(market.content.title, market.content.tips, alert);
    }
}
function del(){
	var row = $('#tt').datagrid('getSelected');
    if (row){
	   showConfirm(market.content.title, market.content.confirm, function(){
		  $.post('/role/'+row.roleId+'/delete', function(data) {
		  	if (data.return_code == '1') {
				top.showMsg(market.content.title, data.return_msg, alert);
				$("#tt").datagrid('reload');
			} else {
				$.messager.alert(market.content.title, data.return_msg);
			}
		  });
	   });
    } else {
    	showMsg(market.content.title, market.content.tips, alert);
    }
}
function formatOpt(value,row,index){
	return '<a href="#" onclick="editrow(this)">权限管理</a> ';
}
function editrow(target){
    $('#tt').datagrid('beginEdit', getRowIndex(target));
}
function getRowIndex(target){
	$.messager.alert(target);
}
</script>
</head>
<body class="cmp-list-body">
<div id="tb" style="padding:3px">
    <span>角色名称:</span>    
    <input id="role_name" style="line-height:20px;border:1px solid #ccc">
    <a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="doSearch();">查询</a> 
    <a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="add();">新增</a>
    <a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="del();">删除</a>
    <a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="edit();">修改</a>
    <a href="#" class="easyui-linkbutton" iconCls="icon-auth" plain="true" onclick="authority();">设置权限</a>
</div>
<table id="tt" class="easyui-datagrid" title="角色管理 | 帐号列表" iconCls="icon-save" 
	url="/role/list?json" toolbar="#tb" rownumbers="true" singleSelect="true" pagination="true" fitColumns="true" pageSize="15" pageList="[15,30,60,80,100]">    
    <thead>
        <tr>
            <th align="center" width="12%" field="roleName">角色名称</th>  
            <th align="center" width="12%" field="roles">角色代码</th>  
            <th align="center" width="12%" field="description">角色描述</th>
            <th align="center" width="12%" field="createTime">创建时间</th>
            <th align="center" width="12%" field="modifyTime">修改时间</th>
        </tr>
    </thead>   
</table>
</body>
</html>