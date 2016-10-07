package ag.pod.questao11;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class OperacaoImpl extends UnicastRemoteObject implements Operacao {
  private static final long serialVersionUID = 3689237026512161592L;

  protected OperacaoImpl() throws RemoteException {
    super();
  }

  public String op2() throws RemoteException {
    GlobalVariable variable = GlobalVariable.instance();
    String value;
    synchronized (variable) {
      variable.incrOp2();
      value = variable.toString();
    }
    return value;
  }

}
