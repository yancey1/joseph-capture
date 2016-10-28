<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset="utf-8">
<title>帐号管理 | 编辑帐号</title>
<@fm.header />

</head>
<body>
<div class="easyui-layout" style="text-align: center; height: 270px;" fit="true">
	<div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
	    <div id="main-2"></div>
	    <input class="easyui-validatebox" type="hidden" name="userName" value="${role.roleId}" />
	</div>
	<div region="south" border="false" style="text-align: center; ">
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="allSelect()">全选</a>
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="killAllSelect()">取消全选</a>
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm1('/role/${role.roleId}/authority')">提交</a>
	</div>
</div>
<form id="ff" method="post">
	<input type="hidden" name="resObj" value="">
</form>
<script>
function submitForm1(url){
	// 处理数据及验证
	var f_str = '';
	$('#main-2').find('input[type="checkbox"]:checked').map(function(idx, ele){
		f_str +=";"+ $(ele).val();
	});
	if(f_str == ''){
		alert("对不起，您选择的权限为空！");
	}else{
		$('#ff').form(
		'submit',
		{
			url : url,
			onSubmit : function() {
			    $("input[name='resObj']").val(f_str);
				//var flag = $(this).form('validate');
				var flag = true;
				if (flag) {
					showProcess(true, market.content.title,	market.content.submitting);
				}
				return flag;
			},
			success : function(data) {
				showProcess(false);
				var data = eval('(' + data + ')');
				if (data.return_code == '1') {
					top.showMsg(market.content.title, data.return_msg, alert);
					if (parent !== undefined) {
						if ($.isFunction(parent.reloadParent)) {
							parent.reloadParent.call();
							parent.closeMyWindow();
						} else {
							parent.$("#tt").datagrid('reload');
							parent.closeMyWindow();
						}
					}
				} else {
					$.messager.alert(market.content.title, data.return_msg);
				}
			},
			onLoadError : function() {
				showProcess(false);
				$.messager.alert(market.content.title, market.content.networkError);
			}
		});
	}
	
    
}

function allSelect(){
	$('#main-2').find('input[type="checkbox"]').attr("checked",true);
}

function killAllSelect(){
	$('#main-2').find('input[type="checkbox"]').attr("checked",false);
}

var resourceId = '';
var apiURLMenu = '/menu/form?json';
market.loadMenuData = function() {
	$.ajax({
		url: apiURLMenu,
		dataType: 'json',
		data: {resourceId: resourceId},
		success: market.onLoadMenuData
	});
};
market.onLoadMenuData = function(data) {
	
	var idsArr = [];
	<#if role?exists && role.menus?size gt 0>
	<#list role.menus as menu>
		var id = ${menu.resourceId};
		idsArr.push(id);
	</#list> 
	</#if>
	
	if (data != null && data !== undefined && data.length > 0) {
		var html = '';
		var hh = '<p>';
		var i=0, length=data.length, menu;
		for(i; i<length; i++) {
			menu = data[i];
			html += '<div">';
			html += '<span class="apk-info-basic">';
			if(menu.parent == 0) {
				var checked = '';
				if(idsArr.length > 0) {
					for(var k=0;k<idsArr.length;k++){
						if(idsArr[k] == menu.resourceId){
							checked = 'checked="checked"';
						}
					}
				}
				html += '<input type="checkbox" name="resId" ' + checked + '  value="' + menu.resourceId + '"/><label style="font-weight: bold;font-size:16px;">'+menu.resourceName+'</label>';						
			}
			html += '<p>';
			for(var j=0;j<length;j++){
				var mm = data[j];
				if(menu.resourceId == mm.parent){
					var checked = '';
					if(idsArr.length>0) {
						for(var m=0;m<idsArr.length;m++) {
							if(idsArr[m] == mm.resourceId) {
								checked = 'checked="checked"';
							}
						}
					}
					html += '<input  type="checkbox" name="resId" '+checked+' value="' + mm.resourceId + '"/><label style="font-size:14px;">'+mm.resourceName+'</label>';
				}
			}
			html += '</p>';
			html += '</span>';
			html += '</div>';
		}
		$('#main-2').append(html);
	} else {
		pageApk = 1;
	}
};
$(function() {
   market.loadMenuData();
   
});
</script>
</body>
</html>