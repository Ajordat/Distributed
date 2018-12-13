package clients.A;

import models.NodeRole;

/**
 * @author Ajordat
 * @version 1.0
 **/
public class A3 extends NodeA {

	private A3() {
		super(NodeRole.A3, NodeRole.B2);
	}

	public static void main(String[] args) {
		NodeA node = new A3();

		node.startRoutine();
	}
}
