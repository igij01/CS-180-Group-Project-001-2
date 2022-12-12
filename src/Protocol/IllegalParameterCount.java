package Protocol;

/**
 * IllegalParameterCount
 * <br>
 * when the number of parameter of the request differs from expected.
 * <i>Note: this exception should never be thrown and are just there to ensure the programmer made the right request</i>
 *
 * @author Yulin Lin, 001
 * @version 12/11/2022
 */
public class IllegalParameterCount extends IllegalArgumentException {
    public IllegalParameterCount(ProtocolRequestType protocolRequestType) {
        super(protocolRequestType.toString() + " requires " + protocolRequestType.getParamCount());
    }
}
