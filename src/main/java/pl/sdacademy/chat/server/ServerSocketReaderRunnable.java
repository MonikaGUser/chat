package pl.sdacademy.chat.server;
import pl.sdacademy.chat.model.ChatMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ServerSocketReaderRunnable implements Runnable{
    private final Socket client;
    private final ChatLog chatLog;

    public ServerSocketReaderRunnable(Socket client, ChatLog chatLog){
        this.client = client;
        this.chatLog = chatLog;
    }

    @Override
    public void run() {
        if(chatLog.register(client)){
            try(ObjectInputStream clientInput = new ObjectInputStream(client.getInputStream())){
                processClientInput(clientInput);
            }catch (IOException e){
                System.out.println("###Client disconnected due to network problem");
            }catch (ClassNotFoundException e){
                System.out.println("### Client disconnected due to invalid message format###");
            }
            chatLog.unregister(client);
        }


        //zarejestrowac klienta w czat logu
        //jesli sie udalo to pobierz object inputstream dla klienata (try-with-resources
        //w petli odczytaj komunikaty od klienta tak dlugo az pojawi sie exit
        // komunikat przekaz do czat logu ale nie przekazuj komunikatu exit
        //jezli pojawil sie exit lub nie udalo sie odczytac komunikatu od klienta
        // to wyrejestruj sie z czat logu i zakoncz to zadanie

    }

    private void processClientInput(ObjectInputStream clientInput) throws IOException, ClassNotFoundException {
        while(true){
            ChatMessage chatMessage = (ChatMessage) clientInput.readObject();
            if (chatMessage.getMessage()== null || chatMessage.getMessage().equalsIgnoreCase("exit")){
                break;
            }
            chatLog.acceptMessage(chatMessage);
        }
    }
}
