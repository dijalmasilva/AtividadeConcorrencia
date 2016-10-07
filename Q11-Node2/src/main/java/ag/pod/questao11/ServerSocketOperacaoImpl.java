package ag.pod.questao11;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerSocketOperacaoImpl {
  
  private int extractPort(String line){
    //POST /cgi-bin/java-rmi.cgi?forward=1099 HTTP/1.1
    //POST /cgi-bin/java-rmi.cgi?forward=51992 HTTP/1.1
    String regex = "POST \\/cgi-bin\\/java-rmi\\.cgi\\?forward=([0-9]{4,5}) HTTP\\/1\\.1";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(line);
    if (matcher.find()){
      String _port = matcher.group(1);
      return Integer.parseInt(_port);
    }
    else {
      return 1099;
    }
  }
  
  public void start(int webPort) throws IOException, ServerForwarderException{
    //criar um socket para servidor
    ServerSocket serverSocket = new ServerSocket(webPort);
    //entrar em looping
    while(true){
      //aguardar uma conexão
      Socket socket = serverSocket.accept();
      //recuperar o stream de entrada
      InputStream input = socket.getInputStream();
      DataInputStream dataInputStream = new DataInputStream(input);
      //recuperar a primeira linha da chamada HTTP
      String line = dataInputStream.readLine();
      //verificar qual a requisição foi chamada
      if (line.contains("POST /op1/update")){//vindo de node3
        //atualizar op1
        GlobalVariable variable = GlobalVariable.instance();
        variable.incrOp1();
        socket.getOutputStream().write(variable.toString().getBytes());
      }
      else if (line.contains("POST /cgi")){//vindo de node1
        //extrair a porta para redirecionamento
        int rmiPort = extractPort(line);
        //chamar o rmi e atualizar op2
        ServerForwarder forwarder = new ServerForwarder();
        forwarder.forward(dataInputStream, socket.getOutputStream(), rmiPort);
      }
      else {
        socket.getOutputStream().write("ERRO".getBytes());
      }
      //
      socket.close();
    }
  }

}
