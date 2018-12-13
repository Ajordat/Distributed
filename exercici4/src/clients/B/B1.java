package clients.B;

import models.NodeRole;

/**
 * @author Ajordat
 * @version 1.0
 **/
public class B1 extends NodeB {

	private B1() {
		super(NodeRole.B1);
	}

	public static void main(String[] args) {
		NodeB node = new B1();

		node.startRoutine();
	}
}
