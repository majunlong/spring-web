package com.web.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.dao.EmployeeDao;
import com.web.model.FileHandler;
import com.web.support.annotation.Login;
import com.web.support.repository.UserRepository;

@Login
@Controller
public class FileUploadController {

	@Autowired
	private EmployeeDao employeeDao;
	@Autowired
	private SimpMessagingTemplate template;
	private UserRepository<String, String, FileHandler> repository = new UserRepository<String, String, FileHandler>();

	@ResponseBody
	@RequestMapping(path = "/upload/file/info", method = RequestMethod.POST)
	public FileHandler loadFileInfo(FileHandler fileHandler) throws Exception {
		File file = new File("C:\\Users\\Administrator\\Desktop\\[" + fileHandler.getId() + "]" + fileHandler.getName());
		if (file.exists()) {
			if (file.length() < fileHandler.getSize()) {
				fileHandler.setBlock(file.length());
			} else {
				return new FileHandler();
			}
		}
		FileHandler handler = this.repository.get(fileHandler);
		if (handler == null) {
			handler = fileHandler;
			this.repository.put(handler);
		}
		return new FileHandler(handler.getId(), handler.getBlock());
	}

	@ResponseBody
	@RequestMapping(path = "/upload/file/pause/{username}/{id}", method = RequestMethod.GET)
	public String loadFilePause(@PathVariable String username, @PathVariable String id) throws Exception {
		FileHandler handler = this.repository.get(this.employeeDao.getISOEncodingRESTFulParam(username), id);
		if (handler != null) {
			this.repository.remove(handler);
			handler.close();
		}
		return "pause";
	}

	@MessageMapping({ "/upload/file/content/{username}/{id}", "/upload/file/content" })
	public void loadFileContent(@Payload String fileContent, @DestinationVariable String username, @DestinationVariable String id, @Headers Map<String, Object> headers) throws Exception {
		FileHandler handler = this.repository.get(username, id);
		if (handler == null) {
			this.template.convertAndSend("/queue/upload/file/info/" + username + "/" + id, "error", headers);
			return;
		}
		if (handler.write(fileContent) || handler.getBlock() >= handler.getSize()) {
			this.repository.remove(handler);
			handler.close();
		}
		this.template.convertAndSend("/queue/upload/file/info/" + username + "/" + id, handler.getBlock(), headers);
	}

	private Map<String, Object> headers = new HashMap<String, Object>();
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	{
		this.headers.put("id", "current-time");
	}

	@Scheduled(fixedDelay = 1000)
	public void currentTime() {
		this.template.convertAndSend("/topic/current/time", this.sdf.format(new Date()), this.headers);
	}

	@Scheduled(fixedDelay = 30000)
	public void checkFileHandler() {
		Long currentTimeStamp = new Date().getTime();
		for (FileHandler handler : this.repository.getContent()) {
			if (handler.getTimeStamp() != null && currentTimeStamp - handler.getTimeStamp() > 20000L) {
				this.repository.remove(handler);
				handler.close();
			}
		}
	}

}
