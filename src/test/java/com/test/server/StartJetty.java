package com.test.server;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.xml.sax.SAXException;

public class StartJetty {
	
	public static void main(String[] args) {
		try {
			Server server = new Server(8080);
			WebAppContext webAppContext = new WebAppContext("spring-web", "/spring-web");
			webAppContext.setDescriptor("src/main/webapp/WEB-INF/web.xml");
			webAppContext.setResourceBase("src/main/webapp");
			webAppContext.setDisplayName("spring-web");
			webAppContext.setClassLoader(Thread.currentThread().getContextClassLoader());
			webAppContext.setConfigurationDiscovered(true);
			webAppContext.setParentLoaderPriority(true);
			server.setHandler(webAppContext);
			server.start();
			server.join();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
