<%@ page pageEncoding="UTF-8"%>
<html>
<head>
<title>WEBSOCKET</title>
<style type="text/css">
</style>
<style type="text/css">
div.websocket {
	padding: 50px;
	float: left;
}

td input.fileId {
	border-bottom: 0;
}

td input.fileContent {
	border-top: 0;
	margin-bottom: 5px;
}


td input {
	width: 242px;
	border: 1px solid #000;
	float: left;
	clear: left;
}

td span {
	margin: 0 10px 0 10px;
	width : 80px;
	float: left;
}

td a {
	float: left;
}
</style>
<script type="text/javascript">
	require(["view/websocket/index"]);
</script>
</head>
<body>
	<input id="uname" type="hidden" value="${sessionScope.name}">
	<div class="websocket">
		<table>
			<tr>
				<td>
					<input type="text" class="fileId" disabled="disabled"><input type="file" class="fileContent"/><span>&nbsp;</span><a class="upload">上传</a><a id="add" style="margin-left:85px;">更多</a>
				</td>
			</tr>
			<tr>
				<td>
					<input type="text" class="fileId" disabled="disabled"><input type="file" class="fileContent"/><span>&nbsp;</span><a class="upload">上传</a>
				</td>
			</tr>
			<tr>
				<td>
					<input type="text" class="fileId" disabled="disabled"><input type="file" class="fileContent"/><span>&nbsp;</span><a class="upload">上传</a>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>
