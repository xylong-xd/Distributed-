import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class EchoHandler implements Runnable{


    private Socket socket =null;

    public EchoHandler(Socket socket) {
        this.socket = socket;
    }


    public static void executeClient(Socket socket) throws IOException {
        //读取
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        PrintStream out = new PrintStream(socket.getOutputStream());
        boolean f = true;
        while (f){
            String str = bufferedReader.readLine();
            System.out.println("from client:"+str);
            out.println(str);
            if(str == null){
                f = false;
            }

        }
        out.close();
        bufferedReader.close();
        socket.close();

    }


    @Override
    public void run() {
        try {
            executeClient(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
