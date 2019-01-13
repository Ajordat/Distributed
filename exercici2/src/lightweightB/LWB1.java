package lightweightB;

import models.Role;

/**
 * @author Ajordat
 * @version 1.0
 **/
public class LWB1 extends LightWeightB {
	private LWB1() {
		super(Role.LWB1);
	}

	public static void main(String[] args) {
		LWB1 lwa = new LWB1();

		lwa.run();
	}
}
