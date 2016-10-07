package ag.pod.questao11;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Operacao extends Remote {
  String SERVICE_NAME = "__Service__";
  String op2() throws RemoteException;
}
