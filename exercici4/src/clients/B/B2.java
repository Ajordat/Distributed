package clients.B;

import models.NodeRole;

/**
 * @author Ajordat
 * @version 1.0
 **/
public class B2 extends NodeB {

	private B2() {
		super(NodeRole.B2);
	}

	public static void main(String[] args) {
		B2 node = new B2();

		node.startNotifyThread(new NodeRole[]{NodeRole.C1, NodeRole.C2});
		node.startRoutine();
	}
}
