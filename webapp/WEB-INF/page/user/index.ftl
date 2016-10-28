<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8"/>
<title>数据抓取系统</title>
<@fm.header />
<style type="text/css">
.panel-body{
	overflow-y: hidden;
}
body {
	font: 12px/20px "微软雅黑", "宋体", Arial, sans-serif, Verdana, Tahoma;
	padding: 0;
	margin: 0;
}
.layout-split-proxy-h{
	position:absolute;
	width:2px;
	background:#888;
	font-size:1px;
	cursor:e-resize;
	display:none;
	z-index:5;
}
.layout-split-north{
	border-bottom:5px solid #efefef;
}
.layout-split-south{
	border-top:5px solid #efefef;
}
.layout-split-east{
	border-left:0px solid #efefef;
}
.layout-split-west{
	border-right:0px solid #efefef;
}
a:link {
 text-decoration: none;
}
a:visited {
 text-decoration: none;
}
a:hover {
 text-decoration: underline;
}
a:active {
 text-decoration: none;
}
.cs-north {
	height:40px;
	overflow:hidden;
}
.cs-north-bg {
	width: 100%;
	height: 100%;
	background: url(/resources/js/easyui/themes/bootstrap/images/header_bg.png) repeat-x;
}
.cs-north-logo {
	height: 30px;
	margin: 10px 0px 20px 5px;
	display: inline-block;
	color:#0E2D5F;
	font-size:20px;
	font-weight:bold;
	text-decoration:none;
}
.cs-north-info {
	position: absolute;
	color: #777;
	top: 10px;
	right: 30px;
	display: inline-block;
}
.cs-west {
	width:160px; padding:0px;
}
.cs-navi-tab {
	font-size: 12px;
	font-weight: bold;
	color: #777;
	height: 13px;
	line-height: 16px;
	width: 120px;
	padding: 5px 0 5px 20px;
	display: block;
}
.cs-tab-menu {
	width:120px;
}
.cs-home-remark {
	padding: 10px;
}
.wrapper {
    float: right;
    height: 30px;
    margin-left: 10px;
}
.cs-div-menu {
	color:red;
}
</style>
<script type="text/javascript">
function addTab(title, url){
	if ($('#tabs').tabs('exists', title)) {
		$('#tabs').tabs('select', title);//选中并刷新
		var currTab = $('#tabs').tabs('getSelected');
		var url = $(currTab.panel('options').content).attr('src');
		if(url != undefined && currTab.panel('options').title != 'Home') {
			$('#tabs').tabs('update',{
				tab:currTab,
				options:{
					content:createFrame(url)
				}
			})
		}
	} else {
		var content = createFrame(url);
		$('#tabs').tabs('add',{
			title:title,
			content:content,
			closable:true
		});
	}
	tabClose();
}
function createFrame(url) {
	var s = '<iframe scrolling="auto" frameborder="0" src="' + url + '" style="width:100%;height:100%;"></iframe>';
	return s;
}
function tabClose() {
	/*双击关闭TAB选项卡*/
	$(".tabs-inner").dblclick(function(){
		var subtitle = $(this).children(".tabs-closable").text();
		$('#tabs').tabs('close',subtitle);
	})
	/*为选项卡绑定右键*/
	$(".tabs-inner").bind('contextmenu',function(e){
		$('#mm').menu('show', {
			left: e.pageX,
			top: e.pageY
		});
		var subtitle =$(this).children(".tabs-closable").text();
		$('#mm').data("currtab",subtitle);
		$('#tabs').tabs('select',subtitle);
		return false;
	});
}		
//绑定右键菜单事件
function tabCloseEven() {
	//刷新
	$('#mm-tabupdate').click(function(){
		var currTab = $('#tabs').tabs('getSelected');
		var url = $(currTab.panel('options').content).attr('src');
		if(url != undefined && currTab.panel('options').title != 'Home') {
			$('#tabs').tabs('update',{
				tab:currTab,
				options:{
					content:createFrame(url)
				}
			})
		}
	})
	//关闭当前
	$('#mm-tabclose').click(function(){
		var currtab_title = $('#mm').data("currtab");
		$('#tabs').tabs('close',currtab_title);
	})
	//全部关闭
	$('#mm-tabcloseall').click(function(){
		$('.tabs-inner span').each(function(i,n){
			var t = $(n).text();
			if(t != 'Home') {
				$('#tabs').tabs('close',t);
			}
		});
	});
	//关闭除当前之外的TAB
	$('#mm-tabcloseother').click(function(){
		var prevall = $('.tabs-selected').prevAll();
		var nextall = $('.tabs-selected').nextAll();		
		if(prevall.length>0){
			prevall.each(function(i,n){
				var t=$('a:eq(0) span',$(n)).text();
				if(t != 'Home') {
					$('#tabs').tabs('close',t);
				}
			});
		}
		if(nextall.length>0) {
			nextall.each(function(i,n){
				var t=$('a:eq(0) span',$(n)).text();
				if(t != 'Home') {
					$('#tabs').tabs('close',t);
				}
			});
		}
		return false;
	});
	//关闭当前右侧的TAB
	$('#mm-tabcloseright').click(function(){
		var nextall = $('.tabs-selected').nextAll();
		if(nextall.length==0){
			//msgShow('系统提示','后边没有啦~~','error');
			alert('后边没有啦~~');
			return false;
		}
		nextall.each(function(i,n){
			var t=$('a:eq(0) span',$(n)).text();
			$('#tabs').tabs('close',t);
		});
		return false;
	});
	//关闭当前左侧的TAB
	$('#mm-tabcloseleft').click(function(){
		var prevall = $('.tabs-selected').prevAll();
		if(prevall.length==0){
			alert('到头了，前边没有啦~~');
			return false;
		}
		prevall.each(function(i,n){
			var t=$('a:eq(0) span',$(n)).text();
			$('#tabs').tabs('close',t);
		});
		return false;
	});
	//退出
	$("#mm-exit").click(function(){
		$('#mm').menu('hide');
	})
}
function logout(){
	$.messager.confirm('退出系统', '您确定现在退出系统吗？', function(r){
		if(r) {
			window.location.href="/logout";
		}
	});
}
function resetPWD(){
	showMyWindow('修改密码', '/user/resetPwd', 420, 200);
}

function changeColor(title){
	var nav=$(".cs-navi-tab");
	for(var i=0;i<nav.length;i++){
		if($(nav[i]).text()==title){
			$(nav[i]).siblings().removeClass("cs-div-menu");
			$(nav[i]).addClass("cs-div-menu");
		};	
	}
}

$(function() {
	tabCloseEven();
	$('.cs-navi-tab').click(function() {
		var $this = $(this);
		$this.siblings().removeClass("cs-div-menu");
		$this.addClass("cs-div-menu");
		var href = $this.attr('src');
		var title = $this.text();
		addTab(title, href);
	});
});
</script>
</head>
<body class="easyui-layout">
	<div region="north" border="true" class="cs-north">
		<div class="cs-north-bg">
			<div class="cs-north-logo">数据抓取系统</div>
			<div class="cs-north-info">
				您好，<#if user?exists && user.nickname != ''>${user.nickname!''}</#if>[<@shiro.principal />]！
				<a href="#" class="easyui-linkbutton" data-options="plain:true" onclick="javascript:resetPWD();">修改密码</a>
				<a href="#" class="easyui-linkbutton" data-options="plain:true" onclick="javascript:logout();">退出系统</a>
			 </div>
		</div>
	</div>
	<div region="west" border="true" split="true" title="菜单导航" class="cs-west">
		<div class="easyui-accordion" fit="true" border="false">
			<div title="数据抓取">
				<a href="javascript:void(0);" src="/account/list" class="cs-navi-tab">账号管理</a> 
				<a href="javascript:void(0);" src="/appInfo/list" class="cs-navi-tab">应用管理</a> 
				<a href="javascript:void(0);" src="/captureData/list" class="cs-navi-tab">抓取数据</a> 
			</div>
		</div>
	</div>
	<div id="mainPanle" region="center" border="true" border="false">
		 <div id="tabs" class="easyui-tabs"  fit="true" border="false" data-options="onSelect:changeColor" >
            <div title="首页">
			<div class="cs-home-remark">
				<h1>欢迎登录数据抓取系统!</h1> <br />
			</div>
			</div>
        </div>
	</div>
	<div region="south" border="false" id="south"><center><@fm.copyright date="${.now?string('yyyy')}" /></center></div>
	<div id="mm" class="easyui-menu cs-tab-menu">
		<div id="mm-tabupdate">刷新</div>
		<div class="menu-sep"></div>
		<div id="mm-tabclose">关闭</div>
		<div id="mm-tabcloseother">关闭其他</div>
		<div id="mm-tabcloseall">关闭全部</div>
	</div>
</body>
</html>