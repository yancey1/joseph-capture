if (!window.console || !console.firebug) {
	var names = ["log", "debug", "info", "warn", "error", "assert", "dir", "dirxml", "group", "groupEnd", "time", "timeEnd", "count", "trace", "profile", "profileEnd"];
	window.console = {};
	for (var i = 0; i < names.length; ++i) {
		window.console[names[i]] = function() {
		};
	}
}

/*
 * =========== MARKET ===============
 */
var market = {};

/*
 * 页面的通用的一些提示语常量
 */
market.content = {
	title : '温馨提示',
	tips : '请先选择要操作的数据！',
	confirm : '您确定要删除该条记录吗？',
	submitting : '正在提交数据...',
	networkError : '由于网络或服务器太忙，提交失败，请重试！'
};
$(function() {
	$('body').append('<div id="myWindow" class="easyui-dialog" closed="true"></div>');
});
function showMsg(title, msg, isAlert) {
	if (isAlert !== undefined && isAlert) {
		$.messager.alert(title, msg);
	} else {
		$.messager.show({
			title : title,
			msg : msg,
			showType : 'show'
		});
	}
}
function showMyWindow(title, href, width, height, modal, minimizable, maximizable) {
	$('#myWindow').window({
		title : title,
		width : width === undefined ? 600 : width,
		height : height === undefined ? 400 : height,
		content : '<iframe scrolling="yes" frameborder="0" src="' + href + '" style="width:100%;height:98%;"></iframe>',
		// href: href === undefined ? null : href,
		modal : modal === undefined ? true : modal,
		minimizable : minimizable === undefined ? false : minimizable,
		maximizable : maximizable === undefined ? false : maximizable,
		shadow : false,
		cache : false,
		closed : false,
		collapsible : false,
		resizable : false,
		loadingMessage : '正在加载数据，请稍等片刻......'
	});
}
function closeMyWindow() {
	$('#myWindow').window('close');
}
function showConfirm(title, msg, callback) {
	$.messager.confirm(title, msg, function(r) {
		if (r) {
			if (jQuery.isFunction(callback))
				callback.call();
		}
	});
}
function showProcess(isShow, title, msg) {
	if (!isShow) {
		$.messager.progress('close');
		return;
	}
	var win = $.messager.progress({
		title : title,
		msg : msg
	});
}
function submitForm(url) {
	$('#ff').form('submit',	{
		url : url,
		onSubmit : function() {
			var flag = $(this).form('validate');
			if (flag) {
				showProcess(true, market.content.title, market.content.submitting);
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
function clearForm(){
    $('#ff').form('clear');
}

// extends jquery function for scroll to top
$.fn.DynamicToTop = function(options) {
	var defaults = {
		text: "",
		min: "200",
		fade_in: 600,
		fade_out: 400,
		speed: "1000",
		easing: "",
		version: "",
		id: 'dynamic-to-top'
	};
	var settings = $.extend(defaults, options);
	if (settings.version == "") {
		settings.text = '';
	}
	var $toTop = $('<a href=\"#\" id=\"' + settings.id + '\"></a>').html(settings.text);
	$toTop.appendTo(document.body);
	$toTop.hide().click(function() {
		$('html, body').stop().animate({
			scrollTop: 0
		},
		settings.speed, settings.easing);
		return false;
	});
	$(window).scroll(function() {
		var sd = $(window).scrollTop();
		if (typeof document.body.style.maxHeight === "undefined") {
			$toTop.css({
				'position': 'absolute',
				'top': $(window).scrollTop() + $(window).height() - mv_dynamic_to_top.margin
			});
		}
		if (sd > settings.min) {
			$toTop.fadeIn(settings.fade_in);
		} else {
			$toTop.fadeOut(settings.fade_out);
		}
	});
};