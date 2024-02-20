import java.io.IOException;
import java.net.ServerSocket;

public class Program {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5555);
            Server server = new Server(serverSocket);
            System.out.println("Сервер запущен");
            server.runServer();

        }catch (IOException e){
            System.out.println(e.getMessage());
        }

    }
}
