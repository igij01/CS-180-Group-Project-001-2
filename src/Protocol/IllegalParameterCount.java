package Protocol;

public class IllegalParameterCount extends IllegalArgumentException {
    public IllegalParameterCount(ProtocolRequestType protocolRequestType) {
        super(protocolRequestType.toString() + " requires " + protocolRequestType.getParamCount());
    }
}
