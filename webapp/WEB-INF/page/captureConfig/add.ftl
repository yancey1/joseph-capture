<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset="utf-8">
<title>数据抓取配置| 新增</title>
<@fm.header />
</head>
<body>
<div class="easyui-layout" style="text-align: center; height:200px;" fit="true">
	<div region="center" border="false" style="padding: 5px; background: #fff; border: 1px solid #ccc;">
	<form id="ff" method="post" >
	    <table style=" border-spacing:5px; ">
	    	<tr>
	            <td><label for="description">公司:</label></td>
	            <td><input class="easyui-validatebox" type="text" name="cpName" required="true" missingMessage="公司不能为空" data-options="validType:'length[2,100]'" /></td>
	        	 <td><label for="productName">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;产品名称:</label></td>
	            <td><input class="easyui-validatebox" type="text" name="appName" /></td>
	        </tr>
	        <tr>
	            <td><label for="roleName">用户名称:</label></td>
	            <td><input class="easyui-validatebox" type="text" name="userName" required="true" missingMessage="用户名称不能为空" data-options="validType:'length[1,100]'" /></td>
	        	 <td><label for="roles">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;密码:</label></td>
	            <td><input class="easyui-validatebox" type="text" name="password" required="true" missingMessage="密码不能为空" data-options="validType:'length[1,20]'" /></td>
	        </tr>
	       
	        <tr>
	            <td><label for="roles">渠道号:</label></td>
	            <td><input class="easyui-validatebox" type="text" name="channelCode" /></td>
	        	<td><label for="roles">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;页面分页的关键字:</label></td>
	            <td><input class="easyui-validatebox" type="text"  name="pageKey"  /></td>
	        </tr>
	        <tr>
	         	<input type="hidden" name="randomCodeType" id="randomCodeType"/>
	            <td><label for="description">验证码生产类型:</label></td>
	            <td> <select class="easyui-combobox"  id="codeType" panelHeight="auto" style="width:155px" data-options="required:true">
		                <option value="1">后台生成</option>
	                	<option value="2">前台生成</option>
		            </select></td>
		            <input type="hidden" name="bizType" id="bizType"/>
	            <td><label for="description">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;供应商类型:</label></td>
	            <td> <select class="easyui-combobox"  id="bizT" panelHeight="auto" style="width:155px" data-options="required:true">
		                <option value="1">CPA</option>
	                	<option value="2">CPS</option>
		            </select></td>
	        </tr>
	        
	        <tr>
	         	<input type="hidden" name="dateType" id="dateType"/>
	            <td><label for="description">时间类型:</label></td>
	            <td> <select class="easyui-combobox"  id="dateT" panelHeight="auto" style="width:155px" data-options="required:true">
		                <option value="1">YYYY-MM-DD</option>
	                	<option value="2">YYYYMMDD</option>
		            </select></td>
		            <input type="hidden" name="timeQueryType" id="timeQueryType"/>
	            <td><label for="description">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;时间查询类型:</label></td>
	            <td> <select class="easyui-combobox"  id="timeQuery" panelHeight="auto" style="width:155px" data-options="required:true">
		                <option value="1">时间日区间</option>
	                	<option value="2">区间内的每一天</option>
	                	<option value="3">区间月区间</option>
	                	<option value="4">区间内的每一月</option>
		            </select></td>
	        </tr>
	        
	         <tr>
	         	<input type="hidden" name="paramType" id="paramType"/>
	            <td><label for="description">数据类型:</label></td>
	            <td> <select class="easyui-combobox"  id="param" panelHeight="auto" style="width:155px" data-options="required:true">
		                <option value="1">普通类型</option>
	                	<option value="2">json类型</option>
		            </select></td>
		            <input type="hidden" name="identifyType" id="identifyType"/>
	            <td><label for="description">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;是否需要验证码:</label></td>
	            <td> <select class="easyui-combobox"  id="idenType" panelHeight="auto" style="width:155px" data-options="required:true">
		                <option value="1">需要</option>
	                	<option value="2">不需要</option>
		            </select></td>
	        </tr>
	        
	        <tr>
	            <td><label for="roles">登录地址:</label></td>
	            <td colspan="3"><input class="easyui-validatebox" type="text" style="width:500px;" required="true" missingMessage="登陆地址不能为空" name="loginUrl" data-options="validType:'length[2,200]'" /></td>
	        </tr>
	        <tr>
	            <td><label for="description">登陆参数:</label></td>
	            <td colspan="3">
	           <input class="easyui-validatebox" type="text" style="width:500px;" required="true" missingMessage="登陆参数不能为空" name="loginParam" data-options="validType:'length[2,500]'" />
	            </td>
	        </tr>
	        <tr>
	            <td><label for="roles">表格属性:</label></td>
	            <td colspan="3"><input class="easyui-validatebox" type="text" style="width:500px;" name="tableAttr" /></td>
	        </tr>
	        
	        <tr>
	            <td><label for="roles">表格属性值:</label></td>
	            <td colspan="3"><input class="easyui-validatebox" type="text" style="width:500px;"  name="tableAttrValue" /></td>
	        </tr>
	        <tr>
	            <td><label for="description">拉取字段:</label></td>
	            <td colspan="3"><input class="easyui-validatebox" type="text" style="width:500px;"  name="dataIndex" /></td>
	        </tr>
	        <tr>
	            <td><label for="roles">验证码地址:</label></td>
	            <td colspan="3"><input class="easyui-validatebox" type="text"  style="width:500px;"name="loginImgUrl" data-options="validType:'length[2,200]'" /></td>
	        </tr>
	        <tr>
	            <td><label for="roles">查询页面地址:</label></td>
	            <td colspan="3"><input class="easyui-validatebox" type="text" style="width:500px;" name="queryPageUrl" data-options="validType:'length[2,200]'" /></td>
	        </tr>
	        <tr>
	            <td><label for="roles">查询数据地址:</label></td>
	            <td colspan="3"><input class="easyui-validatebox" type="text" style="width:500px;" name="queryUrl" data-options="validType:'length[2,200]'" /></td>
	        </tr>
	        
	        <tr>
	            <td><label for="roles">页面验证码来源:</label></td>
	            <td colspan="3"><input class="easyui-validatebox" type="text" style="width:500px;" name="randomSrc" data-options="validType:'length[2,200]'" /></td>
	        </tr>
	        
	         <tr>
	            <td><label for="description">拉取参数:</label></td>
	            <td colspan="3">
	            <textarea style="width:500px;" name="params" rows="3" cols="3"></textarea>
	            </td>
	        </tr>
	    </table>
	</form>
	</div>
	<div region="south" border="false" style="text-align: center; padding: 5px 5px 5px 0;">
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="addCartureConfig()">提交</a>
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm();">重置</a>
	</div>
</div>
<script>
$(function(){
	$("input").bind("keyup afterpaste", function(){
		$(this).val($(this).val().replace(/\s/g,''));
	});
});
	
	function addCartureConfig(){
		if($("#ff").form("validate")){
			var type=$("#param").combobox("getValue");
			var codeType=$("#codeType").combobox("getValue");
			var bizType=$("#bizT").combobox("getValue");
			var dateType=$("#dateT").combobox("getValue");
			var timeQueryType=$("#timeQuery").combobox("getValue");
			var idenType=$("#idenType").combobox("getValue");
			$("#identifyType").val(idenType);
			$("#timeQueryType").val(timeQueryType);
			$("#dateType").val(dateType);
			$("#bizType").val(bizType);
			$("#randomCodeType").val(codeType);
			$("#paramType").val(type);
			submitForm("/captureConfig/add");
		}
	}
</script>
</body>
</html>