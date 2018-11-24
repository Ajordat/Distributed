package models;

public enum Role {
	A1(3000),
	A2(3001),
	A3(3002),
	B1(3003),
	B2(3004),
	C1(3005),
	C2(3006);

	private final int port;

	Role(int port) {
		this.port = port;
	}

	public int getPort() {
		return port;
	}

	public int[] getBroadcastAddresses() {
		switch (this) {
			case A1:
				return new int[]{A2.port, A3.port};
			case A2:
				return new int[]{A1.port, A3.port};
			case A3:
				return new int[]{A1.port, A2.port};

			case B1:
				return new int[]{B2.port};
			case B2:
				return new int[]{B1.port};

			case C1:
				return new int[]{C2.port};
			case C2:
				return new int[]{C1.port};
		}

		return new int[]{};
	}

	@Override
	public String toString() {
		return this.name();
	}
}