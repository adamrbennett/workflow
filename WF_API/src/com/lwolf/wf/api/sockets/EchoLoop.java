package com.lwolf.wf.api.sockets;

import java.io.IOException;

import javax.websocket.Session;

public class EchoLoop implements Runnable {
	
	private Session session = null;
	
	public EchoLoop(Session session) {
		this.session = session;
	}
	
	@Override
	public void run() {
		try {
			session.getBasicRemote().sendText("echo");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
