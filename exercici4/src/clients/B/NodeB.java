package clients.B;

import network.BaseNode;
import models.FileHandler;
import models.NodeRole;
import network.Frame;
import network.WebSocketEndpoint;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author Ajordat
 * @version 1.0
 **/
public abstract class NodeB extends BaseNode {
	private FileHandler fileHandler;
	private static final int REFRESH_SECONDS = 10;
	private WebSocketEndpoint webSocketEndpoint;

	NodeB(NodeRole node) {
		super(node, true);
		this.fileHandler = new FileHandler(node.toString() + ".log");

		this.webSocketEndpoint = new WebSocketEndpoint(
				new InetSocketAddress("localhost", node.getWsPort()),
				logger
		);
		this.webSocketEndpoint.start();
	}

	private String solveReadRequest(String data) {
		String[] actions = data.split(",(?![^(]*\\))");
		StringBuilder readTransactions = new StringBuilder();
		int variable, value;

		logger.debug("Received: [" + data + "]");

		for (String action : actions) {

			variable = Integer.parseInt(action.substring(2, action.length() - 1));
			value = this.fileHandler.getValue(variable);
			logger.print("Read variable " + variable + ": " + value);

			if (!readTransactions.toString().isEmpty())
				readTransactions.append(',');
			readTransactions.append("r(").append(variable).append(',').append(value).append(")");
		}

		return readTransactions.toString();
	}

	private void solveWriteRequest(String data) {
		String[] actions = data.split(",(?![^(]*\\))");
		int index, variable, value;

		logger.debug("Received: [" + data + "]");

		for (String action : actions) {
			logger.debug(action);

			index = action.indexOf(',');
			variable = Integer.parseInt(action.substring(2, index));
			value = Integer.parseInt(action.substring(index + 1, action.length() - 1));
			logger.print("Write " + value + " on variable " + variable + ".");
			fileHandler.setValue(variable, value);

			webSocketEndpoint.updateNodeStatus(variable, value);
		}
	}

	@SuppressWarnings("InfiniteLoopStatement")
	void startNotifyThread(NodeRole[] nodes) {

		(new Thread(() -> {
			String transaction;

			try {
				while (true) {
					Thread.sleep(REFRESH_SECONDS * 1000);

					transaction = fileHandler.toTransaction();

					if (transaction.isEmpty())
						continue;

					for (NodeRole node : nodes) {
						try {
							request(node.getPort(), Frame.Type.POST_BC, transaction);
						} catch (IOException | ClassNotFoundException e) {
							logger.error("Couldn't reach node " + node + ".");
						}
					}

				}
			} catch (InterruptedException e) {
				logger.error("Sleep interrupted. NodeC communication stopped.");
			}
		})).start();
	}

	@Override
	protected void action(Frame frame) throws IOException {

		switch (frame.getType()) {

			case REQUEST_CLIENT:
				String readTransactions = solveReadRequest((String) frame.getData());
				reply(Frame.Type.REPLY_CLIENT, readTransactions);
				break;

			case POST_AB:
				reply(Frame.Type.REPLY_BA, true);
				solveWriteRequest((String) frame.getData());
				break;

			default:
				reply(Frame.Type.REPLY_KO);
		}
	}

}
