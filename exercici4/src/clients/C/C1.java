package clients.C;

import models.NodeRole;

/**
 * @author Ajordat
 * @version 1.0
 **/
public class C1 extends NodeC {

	private C1() {
		super(NodeRole.C1);
	}

	public static void main(String[] args) {
		C1 node = new C1();

		node.startRoutine();
	}
}
