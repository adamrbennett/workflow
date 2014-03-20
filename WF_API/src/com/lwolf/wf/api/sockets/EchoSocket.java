package com.lwolf.wf.api.sockets;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * Servlet implementation class EchoServlet
 */
@ServerEndpoint("/echo")
public class EchoSocket {
	
	@Resource(name="ScheduledExecutorService")
	private ManagedScheduledExecutorService ses;
	
	private ScheduledFuture<?> future;
	
	@OnOpen
	public void onOpen(Session session) {
		future = ses.scheduleAtFixedRate(new EchoLoop(session), 0, 15, TimeUnit.SECONDS);
	}
	
	@OnMessage
	public String onMessage(String message, Session session) {
		return null;
	}
	
	@OnClose
	public void onClose(Session session) {
		future.cancel(true);
	}
	
	@OnError
	public void onError(Session session, Throwable throwable) {
		future.cancel(true);
	}
}
