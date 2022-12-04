package Protocol;

import MessageCore.IllegalMessageException;
import MessageCore.IllegalTargetException;
import MessageCore.IllegalUserAccessException;
import Server.IllegalParameter;
import Server.IllegalRequestFormat;
import UserCore.*;

import java.io.IOException;

public enum ProtocolErrorType {
    //message exception(should never happen)
    ILLEGAL_MESSAGE(IllegalMessageException.class),
    ILLEGAL_TARGET(IllegalTargetException.class),
    ILLEGAL_USER_ACCESS(IllegalUserAccessException.class),

    //server side errors on request packet(should never happen)
    ILLEGAL_PARAMETER(IllegalParameter.class),
    ILLEGAL_REQUEST_FORMAT(IllegalRequestFormat.class),

    //UserCore exception
    EMAIL_FORMAT(EmailFormatException.class),
    ILLEGAL_STORE_NAME(IllegalStoreNameException.class),
    ILLEGAL_USER_LOGIN_STATUS(IllegalUserLoginStatus.class), //should never happen
    ILLEGAL_USER_NAME(IllegalUserNameException.class),
    WRONG_PASSWORD(InvalidPasswordException.class),

    //general ones
    IO_ERROR(IOException.class),
    ARRAY_OUT_OF_BOUND(ArrayIndexOutOfBoundsException.class);


    private final Class<?> exceptionClass;

    ProtocolErrorType(Class<?> e) {
        this.exceptionClass = e;
    }

    /**
     * return the enum ProtocolErrorType based on the exception passed in. null if such exception is not included
     *
     * @param e the exception
     * @return the enum ProtocolErrorType based on the exception passed in. null if such exception is not included
     */
    public static ProtocolErrorType getExceptionsFromException(Exception e) {
        for (ProtocolErrorType protocolErrorType : ProtocolErrorType.values()) {
            if (protocolErrorType.exceptionClass.isInstance(e)) {
                return protocolErrorType;
            }
        }
        return null;
    }
}
