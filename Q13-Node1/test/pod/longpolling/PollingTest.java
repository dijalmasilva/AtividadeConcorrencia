package pod.longpolling;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;



public class PollingTest {

  /**
   * Envia uma mensagem para o servidor
   * 
   * @param msg
   */
  private void send(final String msg){
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        try {
          //
          String message = "message=SRC:" + msg;
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
          if (-1 == socket.getInputStream().read()){
            throw new IOException("EOF");
          }
          socket.close();
        }
        catch(IOException e){
          e.printStackTrace();
        }
      }
    };
    Thread senderThread = new Thread(runnable);
    senderThread.start();
  }
  
  /**
   * Verifica se existem mensagens no servidor
   */
  private void poll(){
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        while(true){
          try {
            Socket socket = new Socket("ag-pod-questao13.appspot.com", 80);
            socket.getOutputStream().write("GET /polling HTTP/1.1\r\n".getBytes());
            socket.getOutputStream().write("Host: ag-pod-questao13.appspot.com\r\n".getBytes());
            socket.getOutputStream().write("\r\n".getBytes());
            //
            InputStreamReader in = new InputStreamReader(socket.getInputStream());
            BufferedReader reader = new BufferedReader(in);
            //
            String startPayload = null;
            do {
              startPayload = reader.readLine();
            } while(startPayload != null && !startPayload.isEmpty());
            //
            String message = reader.readLine();
            writeInConsole(message.replaceAll("SRC\\:", ""));
            //
            socket.close();
          }
          catch(IOException e){
            e.printStackTrace();
          }
        }
      }
    };
    Thread pollThread = new Thread(runnable);
    pollThread.start();
  }
  
  private void writeInConsole(String message){
    synchronized (System.out) {
      System.out.println(message);
    }
  }
  
  public void test() throws InterruptedException{
    //
    poll();
    //
    Scanner scanner = new Scanner(System.in);
    while(true){
      String message = scanner.nextLine();
      send(message);
    }
  }
  
  public static void main(String[] args) throws InterruptedException {
    PollingTest testCase = new PollingTest();
    testCase.test();
  }
  
}
