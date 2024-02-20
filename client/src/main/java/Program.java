import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Введите своё имя: ");
        String name = scan.nextLine();

        try {
            InetAddress address = InetAddress.getLocalHost();
            Socket socket = new Socket(address, 5555);

            Client client = new Client(socket, name);
            InetAddress inetAddress = socket.getInetAddress();
            String remoteIP = inetAddress.getHostAddress();
            System.out.println("Inet Address: " + inetAddress);
            System.out.println("Remote IP: " + remoteIP);
            System.out.println("Local Port: " + socket.getLocalPort());
            client.receivingMsg();
            client.sendToAll();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
