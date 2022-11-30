package Protocol;

public class IllegalParameterCount extends IllegalArgumentException {
    public IllegalParameterCount(Request request) {
        super(request.toString() + " requires " + request.paramCount);
    }
}
