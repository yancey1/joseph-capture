<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset="utf-8">
<title> 拉取管理 | 拉取列表</title>
<@fm.header />
<script>
function doSearch(){    
    $('#tt').datagrid('load', {    
        userId: $('#userId').combobox("getValue"),
        appId: $('#appId').combobox("getValue"),
        channelName:$("#channelName").val(),
        statisDate:$("#statisDate").datebox("getValue")
    });
}
function capture(){
	var userId=$("#userId").combobox("getValue");
	var appId=$("#appId").combobox("getValue");
	var randomCode=$("#randomCode").val();
	if(userId==''){
		$.messager.alert("系统提示！","账号不能为空","info");
		return;
	}
	if(appId==''){
		$.messager.alert("系统提示！","应用名称不能为空","info");
		return;
	}
	if(randomCode==''){
		$.messager.alert("系统提示！","验证码不能为空","info");
		return;
	}
	showProcess(true);		
	$.ajax({
		url:"/captureData/getData",
		type:"post",
		data:{"userId":userId,"appId":appId,"randomCode":randomCode},
		success:function(data){
			showProcess(false);		
			if (data.return_code == '1') {
				top.showMsg(market.content.title, data.return_msg, alert);
				doSearch();
			} else {
				$.messager.alert(market.content.title, data.return_msg);
			}
		}
	})
}

function captureImage(){
    return document.getElementById('CheckPic').src='/unifiedCapture/unifiedCaptchaImage?'+Math.random() ;
}
</script>
</head>
<body class="cmp-list-body">
<div id="tb" style="padding:3px">
   <span>账号:</span>
   <input class="easyui-combobox" style="width:165px" id="userId"  data-options="
	                 valueField:'id',
	                 textField:'name',
	                 url:'/appInfo/getAccountList',
	                 onSelect:function(rec){
	                 	var url='/appInfo/getAppList/'+rec.id;
	                 	$('#appId').combobox('clear');
	                 	$('#appId').combobox('reload',url);
	                 }
	"/>
	<span>应用名称:</span>
   <input class="easyui-combobox" style="width:165px" id="appId" data-options="
	                 valueField:'id',
	                 textField:'appName'
	"/>
	<label id="laCode">验证码：</label><input class="text" style="width:60px;" type="text" id="randomCode" value="">&nbsp;<img id="CheckPic" width="85px" height="30px" title="看不清，换一张" style="cursor:pointer" src="/unifiedCapture/unifiedCaptchaImage" align="absmiddle" onclick="captureImage()">    
    <a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="capture();">拉取数据</a>
    <br>
    <span>渠道名称:</span>
    <input id="channelName" style="line-height:20px;border:1px solid #ccc">
    <span>统计时间:</span>
    <input id="statisDate" type="text" class="easyui-datebox" ></input>  
	<a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="doSearch();">查询</a> 
</div>
<table id="tt" class="easyui-datagrid" title=" 数据管理 | 数据列表" iconCls="icon-save" 
	url="/captureData/list?json" toolbar="#tb" rownumbers="true" 
	singleSelect="true" pagination="true" fitColumns="true" pageSize="15" pageList="[15,30,60,80,100]">     
    <thead>
        <tr>
            <th align="left" width="15%" field="userName">账号</th>
            <th align="left" width="15%" field="appName">应用名称</th>
            <th align="left" width="15%" field="channelName">渠道名称</th>
            <th align="left" width="15%" field="newsUserAmount">新增用户</th>
            <th align="left" width="15%" field="statisDate">统计时间</th>
        </tr>
    </thead>   
</table>
</body>
</html>