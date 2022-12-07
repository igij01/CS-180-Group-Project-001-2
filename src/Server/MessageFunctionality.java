package Server;

import Protocol.ProtocolResponseType;
import UserCore.FullUser;

import java.nio.ByteBuffer;
import java.util.Objects;

public class MessageFunctionality {
    private FullUser user;

    public MessageFunctionality(FullUser user) {
        this.user = user;
    }

    protected ByteBuffer displayConversationTitles() {
        return MessageSystem.toByteBufferPacket(ProtocolResponseType.CONVERSATION_TITLES,
                Objects.requireNonNullElseGet(user.printConversationTitlesArray(),
                        () -> new String[]{"You have no conversation!"}));
    }
}
