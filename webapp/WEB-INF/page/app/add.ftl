<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset="utf-8">
<title>基本信息管理 | 新增</title>
<@fm.header />
</head>
<body>
<div class="easyui-layout" style="text-align: center; height:200px;" fit="true">
	<div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
	<form id="ff" method="post" >
	    <table style=" border-spacing:10px; ">
	        <tr>
	            <td><label >所属账号:</label></td>
	            <td><input class="easyui-combobox" style="width:205px" name="accountId" data-options="
	            	 required:true,
	                 valueField:'id',
	                 textField:'name',
	                 url:'/appInfo/getAccountList'
	               "/></td>
	        </tr>
	        <tr>
	            <td><label >应用名称:</label></td>
	            <td><input class="easyui-validatebox" style="width:200px" type="text" name="appName" required=true  /></td>
	        </tr>
	         <tr>
	            <td><label >应用地址:</label></td>
	            <td><input class="easyui-validatebox" style="width:200px" type="text" name="appUrl" required=true  /></td>
	        </tr>
	        <tr>
	            <td><label >备注:</label></td>
	            <td><textarea style="width:200px;" name="remark" rows="3" cols="3"></textarea></td>
	        </tr>
	        
	    </table>
	</form>
	</div>
	<div region="south" border="false" style="text-align: center; padding: 5px 5px 5px 0;">
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm('/appInfo/add');">提交</a>
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm();">重置</a>
	</div>
</div>
</body>
</html>