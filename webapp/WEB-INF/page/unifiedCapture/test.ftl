<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset="utf-8">
<script type="text/javascript" src="/resources/frame/uploadify/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="/resources/frame/uploadify/jquery.uploadify.v2.1.0.min.js"></script>
<script type="text/javascript" src="/resources/frame/uploadify/swfobject.js"></script>
<link rel="stylesheet" type="text/css" href="/resources/frame/uploadify/default.css">
<link rel="stylesheet" type="text/css" href="/resources/frame/uploadify/uploadify.css">
<title>通用数据抓取</title>
<@fm.header />
<script type="text/javascript">
		$(document).ready(function() {
			$("#uploadify").uploadify({
		        'uploader': '/resources/uploadify/uploadify.swf',
		        'script': 'UploadCommonController',
		        //'buttonImg':'resources/frame/easyui/themes/icons/view.png',   
		        'cancelImg': 'resources/frame/uploadify/uploadify-cancel.png',
		        'queueID': 'fileQueue',
		        'auto': false,
		        'multi': true,
		        'queueSizeLimit' :999,  
		        'wmode'          : 'transparent',   
		        'fileExt'        : '*.png;*.gif;*.jpg;*.bmp;*.jpeg',   
		        'fileDesc'       : '图片文件(*.png;*.gif;*.jpg;*.bmp;*.jpeg)', 
		        'scriptData'      : {'module':'','puk_id':''},
		        'onAllComplete'  :function(event,data)    {   
		            $('#result').html(data.filesUploaded +'个图片上传成功');   
		        } 
		    });
});

function uploadpara(puk_id) {  
    //自定义传递参数  
	var module=$('#module').val();
	$("#uploadify").uploadifySettings('scriptData',{'module':module,'puk_id':puk_id});
    $('#uploadify').uploadifyUpload();  
}  
	</script>
</head>
<body class="cmp-list-body">
<table cellpadding="0" cellspacing="0" class="form_input">
				  <tr>   
		            <td style="width: 100px;">   
		                <input type="file" name="uploadify" id="uploadify" value="11" />   
		                <span id="result" style="font-size: 13px;color: red"></span> 
		            </td>   
		            <td align="left">   
		                <a href="#" onclick="uploadpara()" class="edit_check">点击编辑上传</a>|   
		            </td>   
		             <div id="fileQueue" style="width: 250px;height: 200px; border: 2px solid green;"></div>
        		</tr>
        		<input type="hidden" value="baseapp" name="module" id="module">   
</table>

</body>
</html>