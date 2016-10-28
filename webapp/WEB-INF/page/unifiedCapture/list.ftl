<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset="utf-8">
<title>通用数据抓取</title>
<@fm.header />
<script>
var tage=false;
$(function(){
	$("#cpName").combobox({
		onSelect: function(newValue,oldValue){
			var name=$("#cpName").combobox("getText");
			var id=$("#cpName").combobox("getValue");
			if(id != ''&&name != ''){
				$("#userName").combobox("clear");
				$("#channelCode").combobox("clear");
				$("#appName").combobox("clear");
				$("#userName").combobox('reload','/combobox/user/list/'+name);
			}
        }  
	});
	$("#userName").combobox({
		onSelect: function(newValue,oldValue){
			var cpName=$("#cpName").combobox("getText");
			var userName=$("#userName").combobox("getValue");
			if(userName!=''){
				$("#channelCode").combobox("clear");
				$("#appName").combobox("clear");
				$("#channelCode").combobox('reload','/combobox/channel/list/'+userName+'/'+cpName);
				checkCode(cpName,userName);
			}
        }  
	});
	$("#channelCode").combobox({
		onSelect: function(newValue,oldValue){
			var cpName=$("#cpName").combobox("getText");
			var userName=$("#userName").combobox("getValue");
			var channelCode=$("#channelCode").combobox("getValue");
			if(channelCode!=''){
				$("#appName").combobox("clear");
				$("#appName").combobox('reload','/combobox/app/list/'+cpName+'/'+userName+'/'+channelCode);
			}
        }  
	});
	$("#seYear").combobox("setValue","0");
	$("#yearType").hide();
	$("#monthType").hide();
})

var state=false;
var timeState=false;
function checkCode(name,user){
	$.ajax({
		url:"/combobox/checkCode",
		type:"post",
		//async : false,  
		data:{"name":name,"user":user},
		success:function(data){
			if(data.code!=null&&data.code!=''){
				var ran=data.code.split(":");
				if(ran[0]=='1'){
					state=true;
					$("#laCode").css("visibility","visible");
					$("#randomCode").css("visibility","visible");
					$("#CheckPic").css("visibility","visible");
				}else{
					state=false;
					$("#laCode").css("visibility","hidden");
					$("#randomCode").css("visibility","hidden");
					$("#CheckPic").css("visibility","hidden");
				}
				if(ran[1]==3){
					$("#dayType").hide();
					$("#monthType").show();
					$("#yearType").show();
					timeState=true;
				}else{
					$("#dayType").show();
					$("#monthType").hide();
					$("#yearType").hide();
					timeState=false;
				}
			}
		}
	})
}

function doSearch(flag){    
	var cpName = $('#cpName').combobox('getText')
    var channelCode = $('#channelCode').combobox('getValue')
    var appName = $('#appName').combobox('getValue')
     var userName = $('#userName').combobox('getValue')
	var startTime,endTime;
	if(!timeState){
		startTime = $("#searchStartTime").datebox('getValue');
		endTime = $("#searchEndTime").datebox('getValue');
		if(startTime == ""){
		 	startTime = $("#startTime").datebox('getValue');
			if(startTime==""){
				$.messager.alert("系统提示！","开始时间不能为空,请选择开始时间！","info");
				return ;
			}
		}
		if(endTime == ""){
			endTime = $("#endTime").datebox('getValue');
			if(endTime==""){
				$.messager.alert("系统提示！","结束时间不能为空,请选择结束时间！","info");
				return ;
			}
		}
		if (startTime > endTime) {
			$.messager.alert("系统提示！","开始时间不能大于结束时间！","info");
			return ;
		}
	}else{
			startTime = $("#searchStartTime").datebox('getValue');
			endTime = $("#searchEndTime").datebox('getValue');
			if(startTime==""&&endTime==""){
				year=$("#seYear").combobox("getValue");
				time=$("#seMonth").combobox("getValue");
				if(year==0&&time==0){
					$.messager.alert("系统提示！","开始日期不能为空！","info");
					return;
				}
				if(year==0){
					$.messager.alert("系统提示！","请选择年份！","info");
					return;
				}
				if(time==0){
					$.messager.alert("系统提示！","请选择月份！","info");
					return;
				}
				var date=new Date();
				//var year=new Date().getFullYear();
				date.setMonth(time);
				date.setDate(0);
				var day=date.getDate();
				 if(time<10){
			       startTime=year+"-0"+time+"-01";
			       endTime=year+"-0"+time+"-"+day;
			     }else{
			       startTime=year+"-"+time+"-01";
			       endTime=year+"-"+time+"-"+day;
			     }		
			}else{
				if(startTime == ""){
					$.messager.alert("系统提示！","开始时间不能为空,请选择开始时间！","info");
						return ;
				}
				if(endTime == ""){
						$.messager.alert("系统提示！","结束时间不能为空,请选择结束时间！","info");
						return ;
				}
				if (startTime > endTime) {
					$.messager.alert("系统提示！","开始时间不能大于结束时间！","info");
					return ;
				}
			}
	}
    $('#unifiedCapture').datagrid('load', {    
        state: $('#state').combobox('getValue'),
        cpName: cpName,
        appName:appName,
        channelCode : channelCode,
        startTime: startTime,
        endTime: endTime,
        userName:userName
    });
}

function captureImage(){
    var cpName = $('#cpName').combobox('getValue')
    if (cpName == ''){
 		alert("CP不能为空,请选择CP！");
		return ;      
    }
    return document.getElementById('CheckPic').src='/unifiedCapture/unifiedCaptchaImage?'+Math.random() + '&cpName=' + cpName;
}

function capture(){
    var cpName = $('#cpName').combobox('getText')
    if (cpName == ''){
 		$.messager.alert("系统提示！","CP不能为空,请选择CP！","info");
		return ;      
    }
    var userName = $('#userName').combobox('getValue')
    	if (userName == ''){
	 		$.messager.alert("系统提示！","用戶名不能为空,请选择用戶名！","info");
			return ;      
    	}
    var channelCode = $('#channelCode').combobox('getValue');
    var appName = $('#appName').combobox('getValue');
    var startTime,endTime;
	if(!timeState){
		 endTime = $("#endTime").datebox('getValue');
		 startTime = $("#startTime").datebox('getValue');
		if(startTime == ""){
			$.messager.alert("系统提示！","开始时间不能为空,请选择开始时间！","info");
			return ;
		}
		if(endTime == ""){
			$.messager.alert("系统提示！","结束时间不能为空,请选择结束时间！","info");
			return ;
		}
		if (startTime > endTime) {
			$.messager.alert("系统提示！","开始时间不能大于结束时间！","info");
			return ;
		}
	}else{
		var year=$("#seYear").combobox("getValue");
		 var time=$("#seMonth").combobox("getValue");
		 if(year==0){
			$.messager.alert("系统提示！","请选择年份！","info");
			return;
		}
		if(time==0){
			$.messager.alert("系统提示！","请选择月份！","info");
			return;
		}
		var date=new Date();
		//var year=new Date().getFullYear();
		date.setMonth(time);
		date.setDate(0);
		var day=date.getDate();
		 if(time<10){
	       startTime=year+"-0"+time+"-01";
	       endTime=year+"-0"+time+"-"+day;
	     }else{
	       startTime=year+"-"+time+"-01";
	       endTime=year+"-"+time+"-"+day;
	     }
	}
	var randomCode = $("#randomCode").val();
	if(state){
    	if(randomCode == ""){
			$.messager.alert("系统提示！","验证码不能为空！","info");
			$("#randomCode").focus();
			return false;
		}	
    }else{
    	randomCode="";
    }
    showProcess(true);		
	$.ajax({
		url:'/unifiedCapture/pull',
		type:'post',
		data:{
		startTime:startTime,
		endTime:endTime,
		randomCode:randomCode,
		cpName: $('#cpName').combobox('getText'),
		channelCode:channelCode,
		appName:appName,
		userName:userName
		},
		dataType:'json',
		success: function(data){
			showProcess(false);
			if(data.return_code == '1'){    
			        top.showMsg(market.content.title, data.return_msg, alert);
			        doSearch(1);
			 }else {
				$.messager.alert(market.content.title, data.return_msg);
			}
		}
	}); 
	
	
	
}
function add(){
	//var cpName = $('#cpName').combobox('getText')
    //var channelCode = $('#channelCode').combobox('getValue')
    //var appName = $('#appName').combobox('getValue')
	//var startTime = $("#startTime").datebox('getValue');
	//var endTime = $("#endTime").datebox('getValue');
	
	var cpName = $('#cpName').combobox('getText')
    var channelCode = $('#channelCode').combobox('getValue')
    var appName = $('#appName').combobox('getValue')
	var startTime = $("#searchStartTime").datebox('getValue');
	var endTime = $("#searchEndTime").datebox('getValue');
	var userName=$("#userName").combobox("getValue");
	if (startTime > endTime) {
		alert("开始时间不能大于结束时间！");
		return ;
	}
	showProcess(true);
	$.ajax({
		url:'/unifiedCapture/add',
		type:'post',
		data:'startTime='+startTime+'&endTime='+endTime+'&appName='+appName+'&channelCode='+channelCode+"&cpName="+cpName+"&userName="+userName,
		dataType:'json',
		success: function(data){
			showProcess(false);
			if(data.return_code == '1'){    
		              top.showMsg(market.content.title, data.return_msg, alert);
		              $("#unifiedCapture").datagrid('reload');
		         }else {
				$.messager.alert(market.content.title, data.return_msg);
			}
		
		}
	
	
	});
	
}

function deleteScr(){
	$.messager.confirm("系统提示！","您确定要删除吗？",function(data){
		if(data){
			var cpName = $('#cpName').combobox('getText')
		    var channelCode = $('#channelCode').combobox('getValue')
		    var appName = $('#appName').combobox('getValue')
			var startTime = $("#searchStartTime").datebox('getValue');
			var endTime = $("#searchEndTime").datebox('getValue');
			var state = $("#state").combobox('getValue');
			var userName=$("#userName").combobox("getValue");
			if (startTime > endTime) {
				alert("开始时间不能大于结束时间！");
				return ;
			}
		    $.ajax({
		    	url:"/unifiedCapture/delete",
		    	type:'post',
		    	data:{"cpName":cpName,"channelCode":channelCode,"appName":appName,"startTime":startTime,"endTime":endTime,"state":state,"userName":userName},
		    	success:function(data){
		    		showProcess(false);
					if(data.return_code == '1'){    
				              top.showMsg(market.content.title, data.return_msg, alert);
				              doSearch();
				    }else {
						$.messager.alert(market.content.title, data.return_msg);
					}
		    	}
		    })
		}else{
			return;
		}
	})
	
}
function update(){
	var cpName = $('#cpName').combobox('getText')
    var channelCode = $('#channelCode').combobox('getValue')
    var appName = $('#appName').combobox('getValue')
	var startTime = $("#searchStartTime").datebox('getValue');
	var endTime = $("#searchEndTime").datebox('getValue');
	showProcess(true);
	$.ajax({
		url:'/unifiedCapture/update',
		type:'post',
		data:'startTime='+startTime+'&endTime='+endTime+'&appName='+appName+'&channelCode='+channelCode+"&cpName="+cpName,
		dataType:'json',
		success: function(data){
			showProcess(false);
			if(data.return_code == '1'){    
		              top.showMsg(market.content.title, data.return_msg, alert);
		              doSearch();
		         }else {
				$.messager.alert(market.content.title, data.return_msg);
			}
		
		}
	
	
	});
	
}
function checkTime(date) {
    var startTime = $("#startTime").datebox('getValue');
    var endTime = $("#endTime").datebox('getValue');
    var start=new Date(startTime.replace(/\-/g,'/'));  
    var end=new Date(endTime.replace(/\-/g,'/'));  
    if(end<start){
    	var msg="开始时间不能大于结束时间，请重新选择！";
    	$.messager.alert(market.content.title, msg)
    }
	    
}

function checkSearchTime(){
	 var startTime = $("#searchStartTime").datebox('getValue');
    var endTime = $("#searchEndTime").datebox('getValue');
    var start=new Date(startTime.replace(/\-/g,'/'));  
    var end=new Date(endTime.replace(/\-/g,'/'));  
    if(end<start){
    	var msg="开始时间不能大于结束时间，请重新选择！";
    	$.messager.alert(market.content.title, msg)
    }
}

function formatterState(value,row,index){
	if(value == 0){
		return "未入库";
	}
	if(value == 1){
		return "已入库";
	}
}

function formatterType(value,row,index){
	if(value == 1){
		return "CPA";
	}
	if(value == 2){
		return "CPS";
	}
	
}
</script>
</head>
<body class="cmp-list-body">
<div id="tb" style="padding:3px">

    <div style="padding:3px;">
    <span>CP名称:</span>     
    <input id="cpName" class="easyui-combobox" style="width: 155px;"  data-options="
					url:'/combobox/cp/list',
					method:'get',
					valueField:'cpName',
					mode:'remote',
					textField:'cpName'" />
	<span>用户名:</span>     
    <input id="userName" class="easyui-combobox" style="width: 155px;"  data-options="
    				mode:'remote',
    				method:'get',
					valueField:'userName',
					textField:'userName',
					editable:true"
					" />				
    <span>渠道号:</span>     
    <input id="channelCode" class="easyui-combobox" style="width: 155px;"  data-options="
    				mode:'remote',
    				method:'get',
					valueField:'channelCode',
					textField:'channelCode',
					editable:true"
					" />
					
    <span>产品名称:</span>    
    <input id="appName" class="easyui-combobox" style="width: 155px;" data-options="
	            mode:'remote',
	            method:'get',
	            valueField:'appName',
	            textField:'appName',
	            editable:true">  
    <select class="easyui-combobox" id="state" name="status" panelHeight="auto" style="width:125px" data-options="required:true">
    				<option value="-1">全部状态</option>
		                <option value="0">未入库</option>
		                <option value="1">已入库</option>
    </select>	            
	</div>    
	<div>       
	<div id="dayType" style="display:inline;padding:3px;">   					
    <span  for="startTime">开始日期:</span>
    <input class="easyui-datebox" id="startTime" value="<#if scrab??>${scrab.bizDate}</#if>" style="width: 155px;">
    <span  for="endTime">结束日期:</span>
    <input class="easyui-datebox" id="endTime" style="width: 155px;" data-options="onSelect:checkTime">
    </div>
     <div id="yearType" style="display:inline;padding:3px;" > <span id="spYear">年份:</span>
	    <input class="easyui-combobox" id="seYear"  panelHeight="auto" style="width:125px;display:none" data-options="
	    	url:'/unifiedCapture/getYearList',
	    	valueField:'id',    
	    	textField:'year'"
	    </input>
    </div>
   <div id="monthType" style="display:inline;padding:3px;" > <span id="spMonth">月份:</span>
    <select class="easyui-combobox" id="seMonth"  panelHeight="auto" style="width:125px;display:none" data-options="required:true">
    				<option value="0" selected>请选择月份</option>
    				<option value="1">1月份</option>
		                <option value="2">2月份</option>
		                <option value="3">3月份</option>
		                <option value="4">4月份</option>
		                <option value="5">5月份</option>
		                <option value="6">6月份</option>
		                <option value="7">7月份</option>
		                <option value="8">8月份</option>
		                <option value="9">9月份</option>
		                <option value="10">10月份</option>
		                <option value="11">11月份</option>
		                <option value="12">12月份</option>
    </select>
    </div>
    <label id="laCode">验证码：</label><input class="text" style="width:80px;" type="text" id="randomCode" onkeydown="if(event.keyCode==32) return false; if(event.keyCode==13) return checkForm();" value=""><img id="CheckPic" width="85px" height="30px" title="看不清，换一张" style="cursor:pointer" src="/unifiedCapture/unifiedCaptchaImage" align="absmiddle" onclick="captureImage()">    
    &nbsp;
    <a href="#" class="easyui-linkbutton" id="saveData" iconCls="icon-add" plain="true" onclick="capture()">拉取数据</a>
    <br>
    <div style="display:inline;padding:3px;">  
    <span  for="startTime">开始日期:</span>
    <input class="easyui-datebox" id="searchStartTime" value="<#if scrab??>${scrab.bizDate}</#if>" style="width: 155px;">
    <span  for="endTime">结束日期:</span>
    <input class="easyui-datebox" id="searchEndTime" style="width: 155px;" data-options="onSelect:checkSearchTime">
    <a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="doSearch();">查询</a> 
    <a href="#" class="easyui-linkbutton" id="saveData" iconCls="icon-save" plain="true" onclick="add()">入库</a>
    <a href="#" class="easyui-linkbutton" id="updateData" iconCls="icon-save" plain="true" onclick="update()">修改为未入库</a>
    <a href="#" class="easyui-linkbutton" id="updateData" iconCls="icon-remove" plain="true" onclick="deleteScr()">删除</a>
    </div>
    </div>
</div>
<table id="unifiedCapture" class="easyui-datagrid" title="通用数据抓取" iconCls="icon-save" 
	url="/unifiedCapture/list?json" toolbar="#tb" rownumbers="true" idField="index"
	singleSelect="false" pagination="true" fitColumns="true" pageSize="40" pageList="[40,60,80,100]">    
    <thead>
        <tr>
            <th align="center" width="100" field="bizDate">日期</th>    
            <th align="center" width="150" field="productName">产品</th>   
            <th align="center" width="150" field="channelId">渠道号</th>
            <th align="center" width="50" field="bizType" formatter="formatterType">营收方式</th>
            <th align="center" width="120" field="bizAmount">安装数/信息费</th>
            <th align="center" width="50" field="state" formatter="formatterState">状态</th>
            <th align="center" width="150" field="mark">备注</th>
        </tr>
    </thead>   
</table>
</body>
</html>