package clients.client;

import models.BaseServer;
import models.Logger;
import models.Role;
import network.Frame;
import network.ReplyData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author Ajordat
 * @version 1.0
 **/
public class Client extends BaseServer {
	private String filename;
	private Logger logger;

	private Client(String filename) {
		this.filename = filename;
		this.logger = new Logger(true, "CLI");
	}

	private Role decideNode(int layer) {
		return Role.A1;
	}

	private void startRoutine() {
		Scanner scanner = null;
		Role targetNode;
		String line, transaction, target;

		try {
			scanner = new Scanner(new File(this.filename));
		} catch (FileNotFoundException e) {
			logger.error("Couldn't find file \"" + this.filename + "\".\nExiting.");
			System.exit(1);
		}

		while(scanner.hasNextLine()) {
			line = scanner.nextLine();

			logger.debug(line);

			int index = line.indexOf(',');
			try {
				transaction = line.substring(index + 1, line.length() - 2);
			} catch (StringIndexOutOfBoundsException e) {
				logger.debug("Empty transaction");
				continue;
			}
			target = line.substring(1, index);

			targetNode = decideNode(target.isEmpty() ? 0 : Integer.parseInt(target));

			try {
				logger.debug("Sending frame");
				Frame response = this.request(targetNode.getPort(), Frame.Type.REQUEST_CLIENT, transaction);
				logger.debug("Frame received");
				logger.debug(response.getData().toString());
				Thread.sleep(500);
			} catch (IOException | ClassNotFoundException e) {
				logger.error("Couldn't reach node " + targetNode + ".");
			} catch (InterruptedException e) {
				logger.error("Interrupted sleep. Continuing");
			}
		}

		scanner.close();
	}

	public static void main(String[] args) {
		Client c = new Client(args[0]);

		c.startRoutine();
	}
}
