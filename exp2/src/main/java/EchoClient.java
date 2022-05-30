import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class EchoClient {
    public static void main(String[] args) throws IOException {
        System.out.println("客户端开启\n to server：");
        Socket socket = new Socket("localhost", 8909);
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        //输出流
        PrintStream out = new PrintStream(socket.getOutputStream());
        //输入流
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));


        boolean f = true;
        while (f){
            //发送数据
            String msg = input.readLine();
            out.println(msg);
            System.out.println("发送成功");

            //接收数据
            String res = in.readLine();
            System.out.println("from server:" + res);

            if (msg.equals("quit")){
                f = false;
            }
        }

    }
}
