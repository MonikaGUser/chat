package pl.sdacademy.chat.model;

import java.time.LocalDateTime;

public class DatedChatMessage extends ChatMessage {
    private final LocalDateTime receivedDate;

    public DatedChatMessage(ChatMessage chatMessage) {
        super (chatMessage.getAuthor(),chatMessage.getMessage());
        this.receivedDate = LocalDateTime.now();
    }

    public LocalDateTime getReceivedDate() {
        return receivedDate;
    }
}
