package clients.A;

import models.Node;

/**
 * @author Ajordat
 * @version 1.0
 **/
public class A1 extends NodeA {

	private A1() {
		super(Node.A1);
	}

	@Override
	void afterTransaction(String transactions) {}

	public static void main(String[] args) {
		NodeA node = new A1();

		node.startRoutine();
	}
}
