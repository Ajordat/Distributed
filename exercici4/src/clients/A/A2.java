package clients.A;

import models.NodeRole;

/**
 * @author Ajordat
 * @version 1.0
 **/
public class A2 extends NodeA {

	private A2() {
		super(NodeRole.A2, NodeRole.B1);
	}

	public static void main(String[] args) {
		NodeA node = new A2();

		node.startRoutine();
	}

}
