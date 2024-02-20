import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final ServerSocket serverSocket;

    /**
     * Конструктор
     * @param serverSocket серверный сокет
     */
    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    /**
     * Запуск сервера
     */
    public void runServer(){
        try{
            while (!serverSocket.isClosed()){
                Socket socket = serverSocket.accept();

                ClientManager clientManager = new ClientManager(socket);
                new Thread(clientManager).start();
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
            closeSocket();
        }
    }

    /**
     * Закрытие сервера
     */
    public void closeSocket(){
        try{
            if(!serverSocket.isClosed()) serverSocket.close();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
