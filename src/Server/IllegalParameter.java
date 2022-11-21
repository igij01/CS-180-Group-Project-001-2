package Server;

public class IllegalParameter extends IllegalRequestFormat {
    public IllegalParameter(String expectedParam, String wrongParam) {
        super("Expect " + expectedParam + " but found " + wrongParam);
    }

    public IllegalParameter(int paramExpected, int paramActual) {
        super("Expect " + paramExpected + " parameters but found " + paramActual + " parameters");
    }

    public IllegalParameter() {
        super("some parameters are empty!");
    }
}
