import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private final Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private final Scanner scan = new Scanner(System.in);
    private final String name;

    /**
     * Конструктор
     * @param socket сокет
     * @param name имя клиента
     */
    public Client(Socket socket, String name) {
        this.socket = socket;
        this.name = name;
        try{
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }catch (IOException e){
            System.out.println(e.getMessage());
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
     * Отправка сообщений
     */
    public void sendToAll(){
        try{
            writer.println(getName());

            while (socket.isConnected()){
                String msg = scan.nextLine();
                writer.println(getName() + ": " + msg);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            closeClient(socket, writer, reader);
        }
    }

    /**
     * Получение сообщений
     */
    public void receivingMsg(){
        new Thread(() -> {
            String msg;
            while (socket.isConnected()){
                try {
                    msg = reader.readLine();
                    System.out.println(msg);
                }catch (Exception e){
                    System.out.println(e.getMessage());
                    closeClient(socket, writer, reader);
                }
            }
        }).start();
    }

    /**
     * Закрытие клиента
     * @param socket сокет
     * @param writer PrintWriter
     * @param reader BufferedReader
     */
    private void closeClient(Socket socket, PrintWriter writer, BufferedReader reader){
        try{
            if(!socket.isClosed()) socket.close();
            if(writer != null) writer.close();
            if(reader != null) reader.close();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
