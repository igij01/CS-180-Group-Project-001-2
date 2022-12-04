package Experiment;

import Protocol.*;
import UserCore.InvalidPasswordException;

/**
 * testDataPacketSerialization
 * <p>
 * test the Externalizable interface speed by serializing each packet 1 million time
 *
 * @author Yulin Lin, 001
 * @version 12/3/2022
 */
public class testDataPacketSerialization {
    public static void main(String[] args) {
        DataPacket packet = new DataPacket(ProtocolRequestType.LOGIN, "user", "password");
        ErrorPacket errorPacket = new ErrorPacket(ProtocolRequestType.LOGIN, new InvalidPasswordException());
        ResponsePacket responsePacket = new ResponsePacket(ProtocolResponseType.PROFILE, "new user....");
        long dataPacketTime = System.nanoTime();
        for (int i = 0; i < 1_000_000; i++) {
            DataPacket.serialize(packet);
        }
        dataPacketTime = System.nanoTime() - dataPacketTime;
        long errorPacketTime = System.nanoTime();
        for (int i = 0; i < 1_000_000; i++) {
            DataPacket.serialize(errorPacket);
        }
        errorPacketTime = System.nanoTime() - errorPacketTime;
        long responsePacketTime = System.nanoTime();
        for (int i = 0; i < 1_000_000; i++) {
            DataPacket.serialize(responsePacket);
        }
        responsePacketTime = System.nanoTime() - responsePacketTime;
        System.out.println("data packet 1 million test: " + dataPacketTime / 1_000_000 + "ms");
        System.out.println("error packet 1 million test: " + errorPacketTime / 1_000_000 + "ms");
        System.out.println("response packet 1 million test: " + responsePacketTime / 1_000_000 + "ms");
    }
}
