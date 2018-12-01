package clients.C;

import models.Node;

/**
 * @author Ajordat
 * @version 1.0
 **/
public class C1 extends NodeC {

	private C1() {
		super(Node.C1);
	}

	public static void main(String[] args) {
		C1 node = new C1();

		node.startRoutine();
	}
}
