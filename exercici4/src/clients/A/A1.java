package clients.A;

import models.NodeRole;

/**
 * @author Ajordat
 * @version 1.0
 **/
public class A1 extends NodeA {

	private A1() {
		super(NodeRole.A1);
	}

	public static void main(String[] args) {
		NodeA node = new A1();

		node.startRoutine();
	}
}
