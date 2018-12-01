package clients.A;

import models.Node;

/**
 * @author Ajordat
 * @version 1.0
 **/
public class A3 extends NodeA {

	private A3() {
		super(Node.A3, Node.B2);
	}

	public static void main(String[] args) {
		NodeA node = new A3();

		node.startRoutine();
	}
}
