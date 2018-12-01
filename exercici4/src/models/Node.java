package models;

public enum Node {
	A1(3000),
	A2(3001),
	A3(3002),
	B1(3003),
	B2(3004),
	C1(3005),
	C2(3006);

	private final int port;

	Node(int port) {
		this.port = port;
	}

	public int getPort() {
		return port;
	}

	public Node[] getBroadcastNodes() {
		switch (this) {
			case A1: return new Node[]{A2, A3};
			case A2: return new Node[]{A1, A3};
			case A3: return new Node[]{A1, A2};

			case B1: return new Node[]{B2};
			case B2: return new Node[]{B1};

			case C1: return new Node[]{C2};
			case C2: return new Node[]{C1};

			default: return new Node[]{};
		}
	}

	public int[] getBroadcastAddresses() {
		switch (this) {
			case A1: return new int[]{A2.port, A3.port};
			case A2: return new int[]{A1.port, A3.port};
			case A3: return new int[]{A1.port, A2.port};

			case B1: return new int[]{B2.port};
			case B2: return new int[]{B1.port};

			case C1: return new int[]{C2.port};
			case C2: return new int[]{C1.port};
		}

		return new int[]{};
	}

	public static Node[] getArray() {
		return new Node[]{A1, A2, A3, B1, B2, C1, C2};
	}

	@Override
	public String toString() {
		return this.name();
	}
}