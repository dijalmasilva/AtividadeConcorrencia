package ag.pod.questao11;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ServerForwarder {
  
  private int extractContentLength(DataInputStream reader) throws ServerForwarderException{
    //
    boolean contentLengthFound = false;
    String line;
    int responseContentLength = -1;
    String key = "Content-length:".toLowerCase();
    //
    do {
      //recuperar as linhas
      try {
        //System.out.println("Iniciando processo de extração do tamanho do buufer.");
        line = reader.readLine();
        //System.out.println(line);
      } 
      catch (IOException e) {
        throw new ServerForwarderException("error reading from server");
      }
      //se não houver linhas para leitura
      if (line == null) {
        throw new ServerForwarderException("unexpected EOF reading server response");
      }
      //
      if (line.toLowerCase().startsWith(key)) {
        responseContentLength = Integer.parseInt(line.substring(key.length()).trim());
        contentLengthFound = true;
      }
      //parar na linha vazia (iniciando com \r ou \n)
    } while ((line.length() != 0) && (line.charAt(0) != '\r') && (line.charAt(0) != '\n'));
    //
    if (!contentLengthFound || responseContentLength < 0)
      throw new ServerForwarderException("missing or invalid content length in server response");
    //
    return responseContentLength;
  }

  private void forwardRequest(DataInputStream input, Socket localServer) throws ServerForwarderException, IOException {
    //
    int length = extractContentLength(input);
    //
    DataInputStream clientIn = input;
    byte[] buffer = new byte[length];
    //
    try {
      clientIn.readFully(buffer);//vai ler da posicao correta?
    } 
    catch (EOFException e) {
      throw new ServerForwarderException("unexpected EOF " + "reading request body");
    } 
    catch (IOException e) {
      throw new ServerForwarderException("error reading request" + " body");
    }
    //
    DataOutputStream socketOut = null;
    // send to local server in HTTP
    try {
      //
      System.out.println("----> request inicializado sucesso.");
      //
      socketOut = new DataOutputStream(localServer.getOutputStream());
      socketOut.writeBytes("POST / HTTP/1.0\r\n");
      socketOut.writeBytes("Content-length: " + length + "\r\n\r\n");
      socketOut.write(buffer);
      socketOut.flush();
      //
      System.out.println("----> request finalizado com sucesso.");
    } 
    catch (IOException e) {
      throw new ServerForwarderException("error writing to server");
    }
  }

  private void forwardResponse(OutputStream output, Socket localServer) throws IOException, 
      ServerForwarderException {
    //
    DataInputStream socketIn = new DataInputStream(localServer.getInputStream());
    int responseContentLength = extractContentLength(socketIn);
    //
    byte[] buffer = new byte[responseContentLength];
    try {
      System.out.println("iniciando a leitura do rmi-stream");
      socketIn.readFully(buffer);
    } 
    catch (EOFException e) {
      throw new ServerForwarderException("unexpected EOF reading server response");
    } 
    catch (IOException e) {
      throw new ServerForwarderException("error reading from server");
    }
    //
    try {
      System.out.println("----> response inicializado sucesso.");
      output.write("HTTP/1.0 200 OK\r\n".getBytes());
      output.write("Content-type: application/octet-stream\r\n".getBytes());
      output.write(("Content-length: " + buffer.length + "\r\n\r\n").getBytes());
      output.write(buffer);
      output.flush();
      System.out.println("----> response finalizado com sucesso.");
    } 
    catch (IOException e) {
      throw new ServerForwarderException("error writing response");
    }
  }

  public void forward(DataInputStream dataInputStream, OutputStream output, int port) throws IOException, ServerForwarderException {
    //
    Socket localSocket = null;
    try {
      localSocket = new Socket(InetAddress.getLocalHost(), port);
      forwardRequest(dataInputStream, localSocket);
      forwardResponse(output, localSocket);
    } 
    finally {
      if (localSocket != null) {
        localSocket.close();
      }
    }
  }

}
