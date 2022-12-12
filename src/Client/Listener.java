package Client;

import Protocol.ErrorPacket;
import Protocol.ProtocolRequestType;
import Protocol.ProtocolResponseType;
import Protocol.ResponsePacket;

import java.util.Iterator;

/**
 * Listener
 * <br>
 * A basic listener that watches for a specific types of response and specific request type from the error
 *
 * @author Yulin Lin, 001
 * @version 12/11/2022
 */
public class Listener extends Thread {
    private ProtocolResponseType response;
    private ProtocolRequestType errorRequest;
    private ClientCore client;
    private ResponsePacket responsePacket;
    private ErrorPacket errorPacket;

    public Listener(ClientCore client, ProtocolResponseType response, ProtocolRequestType errorRequest) {
        this.client = client;
        this.response = response;
        this.errorRequest = errorRequest;
        this.responsePacket = null;
        this.errorPacket = null;
    }

    @Override
    public void run() {
        Object packet;
        boolean flag;
        do {
            flag = true;
            Iterator<Object> iterator = client.getReadQueueIterator();
            while (iterator.hasNext()) {
                packet = iterator.next();
                if (packet instanceof ResponsePacket) {
                    ResponsePacket responsePacket1 = (ResponsePacket) packet;
                    if (responsePacket1.protocolResponseType == response) {
                        this.responsePacket = responsePacket1;
                        client.removeFromQueue(responsePacket1);
                        flag = false;
                        break;
                    }
                } else if (packet instanceof ErrorPacket) {
                    ErrorPacket errorPacket1 = (ErrorPacket) packet;
                    if (errorPacket1.requestType == errorRequest) {
                        this.errorPacket = errorPacket1;
                        client.removeFromQueue(errorPacket1);
                        flag = false;
                        break;
                    }
                }
            }
        } while (flag);
    }

    public ResponsePacket getResponsePacket() {
        return responsePacket;
    }

    public ErrorPacket getErrorPacket() {
        return errorPacket;
    }
}
