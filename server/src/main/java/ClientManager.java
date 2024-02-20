import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientManager implements Runnable{

    private final Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private String name;
    private static final List<ClientManager> clients = new ArrayList<>();

    /**
     * Конструктор
     * @param socket сокет
     */
    public ClientManager(Socket socket) {
        this.socket = socket;
        try {
            this.writer = new PrintWriter(socket.getOutputStream(), true);
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            clients.add(this);
            this.name = reader.readLine();
            System.out.println(getName() + " подключился к чату");
            sendToAllClients("Server: " + getName() + " подключился к чату");
        }catch (IOException e){
            System.out.println(e.getMessage());
            closeClient(socket, writer, reader);
        }
    }

    /**
     * Получение имени
     * @return String
     */
    private String getName() {
        return name;
    }

    /**
     * Отправка сообщений всем клиентам
     * @param msg текст сообщения
     */
    private void sendToAllClients(String msg){
        for(ClientManager client : clients){
            if(!client.equals(this) && msg != null){
                client.writer.println(msg);
            }
        }
    }

    /**
     * Отправка индивидуальных сообщений
     * строка должна содержать тэг #ind#, затем через пробел
     * имя клиента, которому отправляется сообщение и затем
     * (снова через пробел) текст сообщения
     * Пример: "#ind# имяКлиента любой текст сообщения"
     * @param msg строка для отправления
     */
    private void sendToIndividual(String msg){
        String[] str = msg.split(" ", 4);
        String whom = str[2];
        String text = str[3];
        for (ClientManager client : clients){
            if(client.getName().equals(whom)) client.writer.println(str[0] + " " + text);
        }
    }

    /**
     * Удаление клиента из списка
     */
    private void removeClient(){
        clients.remove(this);
        System.out.println(getName() + " нас покинул");
        sendToAllClients("Server: " + getName() + " нас покинул");
    }

    /**
     * Закрытие клиента
     * @param socket сокет
     * @param writer PrintWriter
     * @param reader BufferedReader
     */
    private void closeClient(Socket socket, PrintWriter writer, BufferedReader reader){
        removeClient();
        try{
            if(writer != null) writer.close();
            if(reader != null) reader.close();
            if(!socket.isClosed()) socket.close();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Переопределение метода run()
     */
    @Override
    public void run() {
        String msgFromClient;
        while (!socket.isClosed()){
            try {
                msgFromClient = reader.readLine();
                if(!msgFromClient.contains("#ind#")) sendToAllClients(msgFromClient);
                else sendToIndividual(msgFromClient);
            }catch (IOException e){
                System.out.println(e.getMessage());
                closeClient(socket, writer, reader);
            }
        }
    }
}
