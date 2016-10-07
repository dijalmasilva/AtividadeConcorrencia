package pod.longpolling;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class SendMessageTest {

  public static void main(String[] args) throws UnknownHostException, IOException {
    //
    String message = "message=SRC:" + "Opa";
    //
    Socket socket = new Socket("ag-pod-questao13.appspot.com", 80);
    socket.getOutputStream().write("POST /message HTTP/1.1\r\n".getBytes());
    socket.getOutputStream().write("Host: ag-pod-questao13.appspot.com\r\n".getBytes());
    socket.getOutputStream().write("User-Agent: agcurl/1.0.0\r\n".getBytes());
    socket.getOutputStream().write("Content-Type: application/x-www-form-urlencoded\r\n".getBytes());
    socket.getOutputStream().write(("Content-length: " + message.length() + "\r\n").getBytes());
    socket.getOutputStream().write("\r\n".getBytes());
    socket.getOutputStream().write(message.getBytes());
    socket.getOutputStream().write("\r\n".getBytes());
    socket.close();
  }
}
