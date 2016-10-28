<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset="utf-8">
<title>数据抓取配置</title>
<@fm.header />
<script>
function doSearch(){    
    $('#tt').datagrid('load', {    
        cpName: $.trim($('#company').val())    
    });
}
function add(){
    showMyWindow('数据抓取配置 | 新增', '/captureConfig/add', 700,510);
}
function edit(){
	var row = $('#tt').datagrid('getSelected');
    if (row){
	    showMyWindow('数据抓取配置 | 编辑', '/captureConfig/'+row.id+'/update', 700,510);
    } else {
    	showMsg(market.content.title, market.content.tips, alert);
    }
}

function del(){
	var row = $('#tt').datagrid('getSelected');
    if (row){
	   showConfirm(market.content.title, market.content.confirm, function(){
		  $.post('/captureConfig/'+row.id+'/delete', function(data) {
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
    <span>公司名称:</span>    
    <input id="company" style="line-height:20px;border:1px solid #ccc">
    <a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="doSearch();">查询</a> 
    <a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="add();">新增</a>
    <a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="del();">删除</a>
    <a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="edit();">修改</a>
</div>
<table id="tt" class="easyui-datagrid" title="数据抓取配置 |列表查询" iconCls="icon-save" 
	url="/captureConfig/list?json" toolbar="#tb" rownumbers="true" singleSelect="true" pagination="true" fitColumns="true" pageSize="15" pageList="[15,30,60,80,100]">    
    <thead>
        <tr>
        	<th align="center" width="12%" field="cpName">公司名称</th>
            <th align="center" width="10%" field="userName">用户名称</th>  
            <th align="center" width="10%" field="password">密码</th> 
            <th align="center" width="12%" field="appName">产品名称</th>   
            <th align="center" width="12%" field="channelCode">渠道号</th>
            <th align="center" width="16%" field="loginUrl">登录地址</th>
            <th align="center" width="16%" field="loginImgUrl">验证码地址</th>
            <th align="center" width="16%" field="queryPageUrl">查询页面地址</th>
            <th align="center" width="16%" field="queryUrl">查询数据地址</th>
        </tr>
    </thead>   
</table>
</body>
</html>