<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset="utf-8">
<title> 账号管理 | 帐号列表</title>
<@fm.header />
<script>
function doSearch(){    
    $('#tt').datagrid('load', {    
        appName: $('#appName').val(),
        accountId:$("#accountId").combobox("getValue")
    });
}
function add(){
    showMyWindow(' 应用管理 | 新建应用', '/appInfo/add', 400, 385);
}
function edit(){
	var row = $('#tt').datagrid('getSelected');
    if (row){
	    showMyWindow(' 应用管理 | 修改应用', '/appInfo/'+row.id+'/update', 400, 385);
    } else {
    	showMsg(market.content.title, market.content.tips, alert);
    }
}
function del(){
	var row = $('#tt').datagrid('getSelected');
    if (row){
	   showConfirm(market.content.title, market.content.confirm, function(){
		  $.post('/appInfo/'+row.id+'/delete', function(data) {
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
</script>
</head>
<body class="cmp-list-body">
<div id="tb" style="padding:3px">
	<span>所属账号:</span>
	<input class="easyui-combobox" style="width:165px" id="accountId" name="accountId" data-options="
	                 valueField:'id',
	                 textField:'name',
	                 url:'/appInfo/getAccountList'
	               "/>
    <span>应用名称:</span>
    <input id="appName" style="line-height:20px;border:1px solid #ccc">
    <a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="doSearch();">查询</a> 
    <a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="add();">新增</a>
    <a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="edit();">修改</a>
    <a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="del();">删除</a>
    
</div>
<table id="tt" class="easyui-datagrid" title=" 渠道商管理 | 帐号列表" iconCls="icon-save" 
	url="/appInfo/list?json" toolbar="#tb" rownumbers="true" 
	singleSelect="true" pagination="true" fitColumns="true" pageSize="15" pageList="[15,30,60,80,100]">     
    <thead>
        <tr>
            <th align="left" width="15%" field="accountName">所属账号</th>
            <th align="left" width="15%" field="appName">应用名称</th>
            <th align="left" width="15%" field="appUrl">应用地址</th>
            <th align="left" width="15%" field="remark">备注</th>
            <th align="left" width="15%" field="createTime">创建时间</th>
            <th align="left" width="15%" field="modifyTime">修改时间</th>
            
        </tr>
    </thead>   
</table>
</body>
</html>