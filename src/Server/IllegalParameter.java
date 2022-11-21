package Server;

/**
 * IllegalParameter
 * <p>
 * throw when the parameters number don't match or the parameter content is different from expected
 *
 * @author Yulin Lin, 001
 * @version 11/21/2022
 */
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
