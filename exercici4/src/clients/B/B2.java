package clients.B;

import models.Node;
import network.Frame;

import java.io.IOException;

/**
 * @author Ajordat
 * @version 1.0
 **/
public class B2 extends NodeB {

	private B2() {
		super(Node.B2);
	}

	private void startNotifyThread() {

		(new Thread(() -> {
			String transaction;

			try {
				while (true) {
					Thread.sleep(5000);

					transaction = fileHandler.toTransaction();

					if (transaction.isEmpty())
						continue;

					for (Node node : new Node[]{Node.C1, Node.C2}) {
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

	public static void main(String[] args) {
		B2 node = new B2();

		node.startNotifyThread();
		node.startRoutine();
	}
}
