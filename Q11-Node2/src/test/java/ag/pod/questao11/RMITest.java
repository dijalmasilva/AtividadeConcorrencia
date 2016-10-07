package ag.pod.questao11;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMISocketFactory;

import junit.framework.TestCase;


@SuppressWarnings("restriction")
public class RMITest extends TestCase {
  
  public void testLocal() throws RemoteException, NotBoundException{
    Registry registry = LocateRegistry.getRegistry(1099);
    Operacao operacao = (Operacao) registry.lookup(OperacaoImpl.SERVICE_NAME);
    System.out.println(operacao.op2());
  }
  
  public void testRemote() throws NotBoundException, IOException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
    //
    RMISocketFactory.setSocketFactory(new sun.rmi.transport.proxy.RMIHttpToCGISocketFactory()); 
    //
    Registry registry = LocateRegistry.getRegistry(
        "127.0.0.1", 10999, RMISocketFactory.getSocketFactory());
    //
    Operacao operacao = (Operacao) registry.lookup(OperacaoImpl.SERVICE_NAME);
    System.out.println(operacao.op2());
    
  }
  
}
