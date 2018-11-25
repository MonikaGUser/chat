package pl.sdacademy.chat.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class DatedChatMessageTest {
    @Test

    public void shouldSetAuthor(){
        String author = "Thomas";
        String message = "Hello";
        ChatMessage chatMessage = new ChatMessage(author, message);

        DatedChatMessage datedChatMessage = new DatedChatMessage(chatMessage);

        assertEquals(message, datedChatMessage.getMessage(),"Hello");
        assertEquals(author, datedChatMessage.getAuthor(),"Thomas");
        assertNotNull(datedChatMessage.getReceivedDate());

    }


}