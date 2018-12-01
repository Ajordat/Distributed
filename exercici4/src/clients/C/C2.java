package clients.C;

import models.Node;

/**
 * @author Ajordat
 * @version 1.0
 **/
public class C2 extends NodeC {

	private C2() {
		super(Node.C2);
	}

	public static void main(String[] args) {
		C2 node = new C2();

		node.startRoutine();
	}
}
