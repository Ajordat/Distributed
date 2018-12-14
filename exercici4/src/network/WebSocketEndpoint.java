package network;


import models.Logger;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import java.net.InetSocketAddress;

public class WebSocketEndpoint extends org.java_websocket.server.WebSocketServer {
	private Logger logger;

	public WebSocketEndpoint(InetSocketAddress address, Logger logger) {
		super(address);
		this.logger = logger;
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		conn.send("Welcome to the server!"); //This method sends a message to the new client
		logger.print("new connection to " + conn.getRemoteSocketAddress());
	}

	@Override
	public void onClose(WebSocket webSocket, int code, String reason, boolean b) {
		logger.print("closed " + webSocket.getRemoteSocketAddress() + " with exit code " + code + " additional info: " + reason);
	}

	@Override
	public void onMessage(WebSocket conn, String msg) {
		logger.print("received message from " + conn.getRemoteSocketAddress() + ": " + msg);
	}

	@Override
	public void onError(WebSocket conn, Exception e) {
		logger.error("an error occurred on connection " + conn.getRemoteSocketAddress() + ":" + e);
	}

	@Override
	public void onStart() {
		logger.print("server started successfully");
	}
}
