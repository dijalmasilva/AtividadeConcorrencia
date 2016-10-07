package pod.polling.sample;

import java.util.ArrayList;
import java.util.List;

public class MessageService {
  private static MessageService service = new MessageService();
  private static List<String> list = new ArrayList<String>();
  
  private MessageService() {}
  
  public void append(String message){
    list.add(message);
  }
  
  public String[] messages(){
    List<String> result = new ArrayList<String>();
    for (int i = 0; i < list.size(); i++) {
      String m = list.get(i);
      if (m.startsWith("SRC:")){
        result.add(m);
        list.set(i, "REMOVED");
      }
    }
    String[] a = new String[result.size()];
    return result.toArray(a);
  }
  
  public static MessageService instance(){
    return service;
  }
  
}
