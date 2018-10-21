package models;

public enum Role {
	HWA(3000),
	HWB(3001),
	LWA1(3002),
	LWA2(3003),
	LWA3(3004),
	LWB1(3005),
	LWB2(3006);

	private final int port;

	Role(int port) {
		this.port = port;
	}

	public int getPort() {
		return port;
	}

	public int getIndex() {
		if (this == LWA2 || this == LWB2) return 1;
		if (this == LWA3) return 2;
		return 0;
	}

	public int getLightWeightCount() {
		if (this == HWA) return 3;
		if (this == HWB) return 2;
		return 0;
	}

	public int[] getBroadcastAddresses() {
		switch (this) {
			case HWA:
				return new int[]{HWB.port};
			case HWB:
				return new int[]{HWA.port};

			case LWA1:
				return new int[]{LWA2.port, LWA3.port};
			case LWA2:
				return new int[]{LWA1.port, LWA3.port};
			case LWA3:
				return new int[]{LWA1.port, LWA2.port};

			case LWB1:
				return new int[]{LWB2.port};
			case LWB2:
				return new int[]{LWB1.port};
		}

		return new int[]{};
	}

	@Override
	public String toString() {
		switch (this) {
			case HWA:
				return "HeavyWeight-A";
			case LWA1:
				return "LightWeight-A1";
			case LWA2:
				return "LightWeight-A2";
			case LWA3:
				return "LightWeight-A3";
			case HWB:
				return "HeavyWeight-B";
			case LWB1:
				return "LightWeight-B1";
			case LWB2:
				return "LightWeight-B2";
		}
		return "Unknown";
	}
}