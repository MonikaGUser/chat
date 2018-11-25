package pl.sdacademy.chat.server;
import pl.sdacademy.chat.model.ChatMessage;
import pl.sdacademy.chat.model.DatedChatMessage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ChatLog {
    private Map<Socket, ObjectOutputStream> registeredClients;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ChatLog() {
        registeredClients = new ConcurrentHashMap<>();
    }

    public boolean register(Socket client) {

        try {
            ObjectOutputStream streamToClient = new ObjectOutputStream(client.getOutputStream());
            registeredClients.put(client, streamToClient);
            return true;
            //zapisz klienta w kolekcji wszystkich klientow
        } catch (IOException e) {
            System.out.println("### Someone tried to connect but was rejected###");
            return false;
        }
    }

    public boolean unregister(Socket client) {

        ObjectOutputStream connectionToClient = registeredClients.remove(client);
        if (connectionToClient != null) {
            try {
                connectionToClient.close();
                return true;
            } catch (IOException e) {
            }
        }
        return false;
        //usun
        //zamknij strumien
    }

    public void acceptMessage(ChatMessage message) {

        DatedChatMessage datedMessage = new DatedChatMessage(message);
        printMessage(datedMessage);

        //przekonwertowac chat message na dated chat message
        //wypisz na ekran wiadomosc w formacie : <Data> <Author>: <Message>
        // wyślij DatedChatMessage do wszystkich klientow
        //jezeli nie udalo sie wyslac komunikatu do ktorego z klientow to wyrejestruj tego klienta
    }

    private void printMessage(DatedChatMessage datedMessage) {

        System.out.println(dateTimeFormatter.format(datedMessage.getReceivedDate()) + " "
                + datedMessage.getAuthor() + " " + datedMessage.getMessage());
    }

    private void updateClients(DatedChatMessage datedMessage) {

        Set<Map.Entry<Socket, ObjectOutputStream>> allEntries = registeredClients.entrySet();

        for (Map.Entry<Socket, ObjectOutputStream> entry : allEntries) {
            ObjectOutputStream connectionToClient = entry.getValue();
            try {
                connectionToClient.writeObject(datedMessage);
                connectionToClient.flush();
            } catch (IOException e) {
                unregister(entry.getKey());
            }
        }
    }
}
