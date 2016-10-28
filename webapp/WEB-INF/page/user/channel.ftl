<!doctype html>
<html lang="en">
<head>
<meta charset="utf-8" />
<title>账号管理 | 添加渠道</title>
<@fm.header />
<script>
function doSearch(){    
    $('#tt').datagrid('load', {    
        contentName: $('#contentName').val()  
    });
}
 function formatterCmType(value,row,index){
	if(value == 1){
		return "CPA";
	}
	if(value == 2){
		return "CPS";
	}
 }
 function save(){
 
 	var checkedItems = $("#tt").datagrid('getChecked');
 	var cmIds = [];
 	var userId = ${user.userId};
 	cmIds.push(userId);
 	$.each(checkedItems, function(index, item){
		cmIds.push(item.cmId);
	}); 
	
	cmIds.join(",");
	
	var usmId = cmIds.toString();
	if(usmId == userId){
		showMsg(market.content.title, market.content.tips, alert);
	}else{
		$.post('/user/'+usmId+'/channel', function(data) {
		  	if (data.return_code == '1') {
				top.showMsg(market.content.title, data.return_msg, alert);
				$("#tt").datagrid('reload');
			} else {
				$.messager.alert(market.content.title, data.return_msg);
			}
	});
	}
	
	
 }
</script>
</head>
<body class="cmp-list-body">
<div id="tb" style="padding:3px">
    <span>代理应用名称:</span>    
    <input id="contentName" style="line-height:20px;border:1px solid #ccc">
    <a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="doSearch();">查询</a> 
    <a href="#" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="save();">保存渠道</a> 
</div>
<table id="tt" class="easyui-datagrid" title="渠道列表列表" iconCls="icon-save" 
	url="/user/channel?json" toolbar="#tb" rownumbers="true" idField="cmId"
	singleSelect="false" pagination="true" fitColumns="true" pageSize="20">    
    <thead>
        <tr>
        	<th field="cmId" checkbox="true"></th> 
            <th align="center" width="15%" field="contentName">内容名称</th>
            <th align="center" width="15%" field="channelCode">渠道号</th>
            <th align="center" width="15%" field="channelName">渠道名称</th>
        </tr>
    </thead>   
</table>
</body>
</html>