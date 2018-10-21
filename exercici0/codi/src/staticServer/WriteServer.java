package staticServer;

/**
 * @author Ajordat
 * @version 1.0
 **/
public class WriteServer extends StaticServer {

	@Override
	public void updateCurrentValue(int value) {
		this.value = value + 1;
		verbose("Current value is: " + this.value, Role.CLIENT);
	}

	public static void main(String[] args) throws ClassNotFoundException {
		(new WriteServer()).startRoutine();
	}

}
