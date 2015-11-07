define(function() {require(["jquery", "stomp", "sockjs", "buffer", "map"], function($, Stomp, SockJS, StringBuffer, Map) {
	var socket = new SockJS(document.ctx + "/join");
	var stompClient = Stomp.over(socket);
	var arrayToString = function(array){
		var buffer = new StringBuffer();
		for (var i = 0; i < array.length - 1; i++) {
			buffer.append(array[i]);
			buffer.append(",");
		}
		buffer.append(array[array.length -1]);
		return buffer.toString();
	}
	var init = function () {
		stompClient.connect({},
			function (e) {
				stompClient.subscribe("/topic/current/time", function(message) {
					$('a.title:last').html(message.body);
//					stompClient.send("/app/current/time", {"id" : "current-time"});
				}, {"id" : "current-time"});
//				stompClient.send("/app/current/time", {"id" : "current-time"});
			},
			function (e){
				if (e.headers != null && e.headers.message.indexOf("NotLoginedException")) {
					stompClient.disconnect(function(){
						window.location.href = document.ctx + "/login";
					});
				}
			}
		);
	};
	/**
	 * 订阅分片发送文件内容后返回的文件信息并发送下一个文件片
	 * @param fileUploadBox	文件上传所需服务信息Map集合
	 */
	var subscribeFileInfo = function (fileUploadBox){
		var fileId = fileUploadBox.get("fileId").value;
		var username = fileUploadBox.get("fileUploadUser").value;
		var fileUploadBtn = fileUploadBox.get("fileUploadBtn");
		var sendFileContent = function (fileUploadBlockSize){
			var file = fileUploadBox.get("file");
			var fileSize = Number.parseInt(file.size);
			var fileProgress = fileUploadBox.get("fileProgress");
			var begin = Number.parseInt(fileUploadBlockSize);
			var end = (begin + 8192) > fileSize ? fileSize : (begin + 8192);
			if (begin < fileSize) {
				var reader = new FileReader();
				reader.readAsArrayBuffer(file.slice(begin, end));
				reader.onload = function(e) {
					stompClient.send("/app/upload/file/content/" + username + "/" + fileId, {"id" : fileId}, arrayToString(new Int8Array(e.target.result)));
				};
				fileProgress.innerHTML = (begin * 100 / fileSize).toFixed(2) + "%";
			} else {
				stompClient.unsubscribe(fileId);
				fileProgress.innerHTML = "100%";
				fileUploadBtn.innerHTML = "上传";
			}
		};
		stompClient.subscribe("/queue/upload/file/info/" + username + "/" + fileId, function(data) {
			if(data.body == "error"){
				stompClient.unsubscribe(fileId);
				fileUploadBtn.innerHTML = "上传";
			} else {
				sendFileContent(data.body);
			}
		}, {"id" : fileId});
		sendFileContent(fileUploadBox.get("fileUploadBlockSize"));
	};
	$(function() {
		init();
		$("table").delegate("tr td a.upload", "click", function(){ 
			var _this = this;
			var thisSiblings = $(_this).siblings();
			var fileUploadBox = new Map();
			fileUploadBox.put("file", thisSiblings[1].files[0]);
			fileUploadBox.put("fileId", thisSiblings[0]);
			fileUploadBox.put("fileProgress", thisSiblings[2]);
			fileUploadBox.put("fileUploadBtn", _this);
			fileUploadBox.put("fileUploadUser", $("input#uname")[0]);
			var username = fileUploadBox.get("fileUploadUser").value;
			if (this.innerHTML == "上传") {
				var file = fileUploadBox.get("file");
				if (file == undefined) {
					return;
				}
				var reader = new FileReader();
				reader.readAsArrayBuffer(new Blob([file.slice(0, 512), file.slice(file.size - 512, file.size)]));
				reader.onload = function(e) {
					$.post(
						document.ctx + "/upload/file/info",
						{
							"id" : arrayToString(new Int8Array(e.target.result)),
							"username" : username,
							"name" : file.name,
							"size" : file.size,
							"type" : file.type,
							"modifyTime" : file.lastModified,
						},
					    function(data, status){
							if(status == "success"){
								if (data.id == null) {
									fileUploadBox.get("fileProgress").innerHTML = "已上传";
									return;
								}
								_this.innerHTML = "暂停";
								fileUploadBox.get("fileId").value= data.id;
								fileUploadBox.put("fileUploadBlockSize", data.block);
								subscribeFileInfo(fileUploadBox);
							}
					    }
					);
				};
				return;
			}
			if (_this.innerHTML == "暂停") {
				var fileId = fileUploadBox.get("fileId").value;
				stompClient.unsubscribe(fileId);
				$.get(document.ctx + "/upload/file/pause/" + username + "/" + fileId, function(data, status){
					if(status == "success" && data == "pause"){
						_this.innerHTML = "上传";
					}
				});
			}
		});
		$('a#add').click(function() {
			$('table').append(
				$("<tr>").append(
					$("<td>").append(
						$("<input type=\"text\" class=\"fileId\" disabled=\"disabled\">")
					).append(
						$("<input type=\"file\" class=\"fileContent\"/>")
					).append(
						$("<span>&nbsp;</span>")
					).append(
						$("<a class=\"upload\">上传</a>")
					)
				)
			);
		});
	});
});});