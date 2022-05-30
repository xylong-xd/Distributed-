import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
    public static final int THREAD_POOL_SIZE = 3;

    public static void main(String[] args) throws IOException {
        System.out.println("服务端已开启");
        //监听8999
        ServerSocket socket = new ServerSocket(8909);

        for (int i = 0; i < THREAD_POOL_SIZE; i++) {
            Thread thread = new Thread(() -> {
                while (true){
                    try {
                        Socket client = socket.accept();
                        EchoHandler.executeClient(client);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }

    }
}
