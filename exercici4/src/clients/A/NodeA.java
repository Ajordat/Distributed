package clients.A;

import models.BaseNode;
import models.FileHandler;
import models.Node;
import models.TransactionResults;
import network.Frame;

import java.io.IOException;

/**
 * @author Ajordat
 * @version 1.0
 **/
public abstract class NodeA extends BaseNode {
	private final static int MAX_TRANSACTIONS = 2;
	private FileHandler fileHandler;
	private int transactionsCounter;
	private String transactionsData;
	private Node lowerNode;

	NodeA(Node node, Node lowerNode) {
		super(node, true);
		this.fileHandler = new FileHandler(node.toString() + ".log");
		this.lowerNode = lowerNode;

		this.transactionsCounter = 0;
		this.transactionsData = "";
	}

	NodeA(Node node) {
		this(node, null);
	}

	private TransactionResults solveClientRequest(String data) {
		String[] actions = data.split(",(?![^(]*\\))");
		StringBuilder writeTransactions = new StringBuilder();
		StringBuilder readTransactions = new StringBuilder();
		int index, variable, value;

		logger.debug("Received: [" + data + "]");

		for (String action : actions) {
			logger.debug(action);

			if (action.charAt(0) == 'w') {
				index = action.indexOf(',');
				variable = Integer.parseInt(action.substring(2, index));
				value = Integer.parseInt(action.substring(index + 1, action.length() - 1));
				logger.print("Write " + value + " on variable " + variable + ".");
				fileHandler.setValue(variable, value);

				if (!writeTransactions.toString().isEmpty())
					writeTransactions.append(',');
				writeTransactions.append(action);

			} else {
				variable = Integer.parseInt(action.substring(2, action.length() - 1));
				value = this.fileHandler.getValue(variable);
				logger.print("Read variable " + variable + ": " + value);

				if (!readTransactions.toString().isEmpty())
					readTransactions.append(',');
				readTransactions.append("r(").append(variable).append(',').append(value).append(")");
			}
		}

		return new TransactionResults(writeTransactions.toString(), readTransactions.toString());
	}

	@Override
	protected void action(Frame frame) throws IOException, ClassNotFoundException {
		Frame response;
		TransactionResults results;
		String writeTransactions;

		switch (frame.getType()) {

			case REQUEST_CLIENT:
				results = solveClientRequest((String) frame.getData());

				if (results.hasWriteResults()) {

					for (Node node : this.node.getBroadcastNodes()) {
						response = request(node.getPort(), Frame.Type.POST_AA, results.getWriteResult());

						if (response.getType() == Frame.Type.REPLY_AA && (boolean) response.getData())
							logger.debug("NODE " + node + " CONFIRMED");
						else
							logger.debug("NODE " + node + " DECLINED");
					}
				}

				reply(Frame.Type.REPLY_CLIENT, results.getReadResult());

				afterTransaction(results.getWriteResult());
				break;

			case POST_AA:
				writeTransactions = (String) frame.getData();
				logger.print(writeTransactions);
				solveClientRequest(writeTransactions);
				reply(Frame.Type.REPLY_AA, true);

				afterTransaction(writeTransactions);
				break;

			default:
				reply(Frame.Type.REPLY_KO);
		}

	}

	void afterTransaction(String transactions) throws IOException, ClassNotFoundException {
		Frame response;

		if (!this.transactionsData.isEmpty())
			this.transactionsData += ',';
		this.transactionsData += transactions;
		this.transactionsCounter++;

		if (transactionsCounter >= MAX_TRANSACTIONS) {
			response = request(lowerNode.getPort(), Frame.Type.POST_AB, transactionsData);

			if (response.getType() == Frame.Type.REPLY_BA && (boolean) response.getData()) {
				logger.debug(lowerNode + " confirmed the transaction");
			} else {
				logger.debug(lowerNode + " denied the transaction");
			}

			this.transactionsCounter = 0;
			this.transactionsData = "";
		}
	}

}
